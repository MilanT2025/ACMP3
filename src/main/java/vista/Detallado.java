/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista;

import Controlador.Conexion;
import Controlador.FuncionesGlobales;
import com.formdev.flatlaf.FlatIntelliJLaf;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.oxbow.swingbits.table.filter.TableRowFilterSupport;

/**
 *
 * @author Farmacia-Ingeniero
 */
public class Detallado extends javax.swing.JDialog {

    private final Modelo modelo = new Modelo();
    private int ColumnaSumar;

    private Color originalAlternateRowColor;


    /**
     * Creates new form Detallado
     */
    public Detallado(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        
        originalAlternateRowColor = (Color) UIManager.getLookAndFeelDefaults().get("Table.alternateRowColor");

        if (originalAlternateRowColor == null) {
            originalAlternateRowColor = UIManager.getColor("Table.alternateRowColor");
        }

        UIManager.getLookAndFeelDefaults().put("Table.alternateRowColor", new Color(254, 238, 184));

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (originalAlternateRowColor == null) {
                    UIManager.getLookAndFeelDefaults().remove("Table.alternateRowColor");
                } else {
                    UIManager.getLookAndFeelDefaults().put("Table.alternateRowColor", originalAlternateRowColor);
                }
            }
        });
        
    }

    public class Modelo extends DefaultTableModel {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

    }

    private String buscarCodigos(String nombretxt, String concepto) {
        txt_concepto.setText(concepto);
        Set<String> uniqueCuentas = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(nombretxt))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    if (parts[0].trim().equals(concepto)) {
                        String planCuenta = parts[1].trim();
                        uniqueCuentas.add(planCuenta);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String cuentas = String.join(",", uniqueCuentas);

        return cuentas;
    }
    
    private String buscarGlosaRUC(String nombretxt, String concepto) {
        txt_concepto.setText(concepto);
        Set<String> uniqueCuentas = new HashSet<>();
        Set<String> uniqueNumeros = new HashSet<>();
         boolean agregarCondicionNumeros = false;

        try (BufferedReader br = new BufferedReader(new FileReader(nombretxt))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    if (parts[0].trim().equals(concepto)) {
                        String glosa = parts[1].trim();
                        String[] palabras = glosa.split("%");
                        for (String palabra : palabras) {
                            uniqueCuentas.add("GE.GLOSA LIKE '%" + palabra + "%'");
                        }

                        String numero = parts[2].trim();
                        uniqueNumeros.add( numero );
                        agregarCondicionNumeros = true;
                    }
                }
                if (parts.length == 2) {
                    if (parts[0].trim().equals(concepto)) {
                        String glosa = parts[1].trim();
                        String[] palabras = glosa.split("%");
                        for (String palabra : palabras) {
                            uniqueCuentas.add("GE.GLOSA LIKE '%" + palabra + "%'");
                        }
                         agregarCondicionNumeros = false;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String cuentas = String.join(" OR ", uniqueCuentas);
        String numeros = String.join(",", uniqueNumeros);
        String resultadoFinal = "'" + numeros.replace(",", "','") + "'";
        String condicionNumeros = agregarCondicionNumeros ? " AND [Nro Doc# Ident# Benef#] IN (" + resultadoFinal + ")" : "";


        return cuentas + condicionNumeros;

    }
    

    public void cargarDatos(int año, int mes, String tabla, String nombretxt, String concepto) {
        String cuentas = buscarCodigos(nombretxt, concepto);
        DecimalFormat df = new DecimalFormat("###,###,##0.00");
        JTableHeader header = tb_data.getTableHeader();
        header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 40));
        header.setBackground(new java.awt.Color(0, 102, 153));
        header.setForeground(new java.awt.Color(255, 255, 255));
        tb_data.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

        try {
            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "";
            int columnasumar = 0;

            if (tabla.equals("ingreso")) {
                columnasumar = 20;
                ColumnaSumar = columnasumar;
                sql = "SELECT [Movimiento] "
                        + "      ,[Nº Cuenta] "
                        + "      ,[Descripcion] "
                        + "      ,[Tipo] "
                        + "      ,[Numero] "
                        + "      ,[F# Registro] "
                        + "      ,[CUO] "
                        + "      ,[Nro Doc# Ident# Benef#] "
                        + "      ,[Razon Social Beneficiario] "
                        + "      ,[Plan Cuenta] "
                        + "      ,[Descripcion1] "
                        + "      ,[Comprobante Nº] "
                        + "      ,[Nro Doc# Ident# Compr#] "
                        + "      ,[Razon Social Comprobante] "
                        + "      ,[Moneda] "
                        + "      ,[Abono] "
                        + "      ,[TC ME] "
                        + "      ,[Debe] "
                        + "      ,[Haber] "
                        + "      ,[Debe MN] "
                        + "      ,[Haber MN] "
                        + "      ,[Debe ME] "
                        + "      ,[Haber ME] "
                        + "FROM ingreso "
                        + "WHERE "
                        + "	Año = " + año + " AND "
                        + "	Mes = " + mes + " AND [Haber MN] > 0 AND "
                        + "	[Plan Cuenta] IN (" + cuentas + ")";

            } else if (tabla.equals("egreso")) {
                columnasumar = 19;
                ColumnaSumar = columnasumar;
                sql = "SELECT "
                        + "       [Movimiento] "
                        + "      ,[Nº Cuenta] "
                        + "      ,[Descripcion] "
                        + "      ,[Tipo] "
                        + "      ,[Numero] "
                        + "      ,[F# Registro] "
                        + "      ,[CUO] "
                        + "      ,[Nro Doc# Ident# Benef#] "
                        + "      ,[Razon Social Beneficiario] "
                        + "      ,[Plan Cuenta] "
                        + "      ,[Descripcion1] "
                        + "      ,[Comprobante Nº] "
                        + "      ,[Nro Doc# Ident# Compr#] "
                        + "      ,[Razon Social Comprobante] "
                        + "      ,[Moneda] "
                        + "      ,[Abono] "
                        + "      ,[TC ME] "
                        + "      ,[Debe] "
                        + "      ,[Haber] "
                        + "      ,-[Debe MN] AS [Debe MN] "
                        + "      ,[Haber MN] "
                        + "      ,[Debe ME] "
                        + "      ,[Haber ME] "
                        + "FROM [dbo].[egreso] "
                        + "WHERE "
                        + "	Año = " + año + " AND "
                        + "	Mes = " + mes + " AND [Debe MN] > 0 AND "
                        + "	[Plan Cuenta] IN (" + cuentas + ")";

            } else if (tabla.equals("cuenta33")) {
                columnasumar = 19;
                ColumnaSumar = columnasumar;
                sql = "SELECT  "
                        + "      [Periodo] "
                        + "      ,[Nº] "
                        + "      ,[CUO] "
                        + "      ,[F# Emision] "
                        + "      ,[F# Venc#] "
                        + "      ,[Tipo] "
                        + "      ,[Serie Nº] "
                        + "      ,[Numero] "
                        + "      ,[Nro Doc# Ident#] "
                        + "      ,[Razon Social] "
                        + "      ,[Forma Pago] "
                        + "      ,[Comprobante Ref#] "
                        + "      ,[Regimen IGV] "
                        + "      ,[Codigo] "
                        + "      ,[Descripcion] "
                        + "      ,[Cant#] "
                        + "      ,[U/M] "
                        + "      ,[Moneda] "
                        + "      ,[Precio Sin IGV] "
                        + "      ,-[Sub Total MN] AS [Sub Total MN]"
                        + "      ,[Sub Total ME] "
                        + "      ,[Cta# Cont#] "
                        + "FROM [dbo].[cuenta33] "
                        + "WHERE "
                        + "	Año = " + año + " AND "
                        + "	Mes = " + mes + " AND [Sub Total MN] > 0 AND "
                        + "	[Cta# Cont#] IN (" + cuentas + ")";
            }

            ResultSet rs = st.executeQuery(sql);

            ResultSetMetaData metaData = rs.getMetaData();

            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                modelo.addColumn(metaData.getColumnName(i));
            }

            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                modelo.addRow(row);
            }
            
            TableRowFilterSupport.forTable(tb_data).searchable(true).actions(true).useTableRenderers(true).apply();

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tb_data.getModel());
        tb_data.setRowSorter(sorter);

        // Añadir el RowSorterListener
        sorter.addRowSorterListener(new RowSorterListener() {
            @Override
            public void sorterChanged(RowSorterEvent e) {
                calculateAndDisplayTotal(tb_data, txt_total);
            }
        });

            if (tb_data.getRowCount() > 0) {
                calculateAndDisplayTotal(tb_data, txt_total);
            } else {
                txt_total.setText("0.00");
            }

            packColumns(tb_data);

        } catch (SQLException ex) {
            Logger.getLogger(Detallado.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void cargarDatosCajaEgresos(int año, int mes, String tabla, String nombretxt, String concepto) {
        String cuentas = buscarGlosaRUC(nombretxt, concepto);

        DecimalFormat df = new DecimalFormat("###,###,##0.00");
        JTableHeader header = tb_data.getTableHeader();
        header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 40));
        header.setBackground(new java.awt.Color(0, 102, 153));
        header.setForeground(new java.awt.Color(255, 255, 255));
        tb_data.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

        try {
            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "";
            int columnasumar = 0;

            if (tabla.equals("egreso")) {
                columnasumar = 20;
                ColumnaSumar = columnasumar;
                sql = "SELECT "
                        + "      [Movimiento] "
                        + "      ,[Nº Cuenta] "
                        + "      ,[Descripcion] "
                        + "      ,[Tipo] "
                        + "      ,E.[Numero] "
                        + "      ,[F# Registro] "
                        + "      ,[CUO] "
                        + "      ,[Nro Doc# Ident# Benef#] "
                        + "      ,[Razon Social Beneficiario] "
                        + "      ,[Plan Cuenta] "
                        + "      ,[Descripcion1] "
                        + "      ,[Comprobante Nº] "
                        + "      ,[Nro Doc# Ident# Compr#] "
                        + "      ,[Razon Social Comprobante] "
                        + "      ,[Moneda] "
                        + "      ,[Abono] "
                        + "      ,[TC ME] "
                        + "      ,[Debe] "
                        + "      ,[Haber] "
                        + "      ,[Debe MN]"
                        + "      ,CASE WHEN Moneda = 'US$' THEN FORMAT(CONVERT(DECIMAL(18, 5), (-([TC ME] * Haber))), '0.00###', 'en-US') ELSE FORMAT(-([Haber MN]), '0.00###', 'en-US') END AS [Haber MN] "
                        + "      ,[Debe ME] "
                        + "      ,[Haber ME] "
                        + "	  ,GE.Glosa "
                        + "  FROM [dbo].[egreso] E "
                        + "	LEFT JOIN glosaEgreso GE ON E.CUO = GE.Numero AND E.Año = GE.Año AND E.Mes = GE.Mes "
                        + "  WHERE "
                        + "	E.Año = " + año + " AND Abono > 0 AND [Haber MN] > 0 AND "
                        + "	E.Mes = " + mes + " AND ( " + cuentas + " ) ";
            } 
            

            ResultSet rs = st.executeQuery(sql);

            ResultSetMetaData metaData = rs.getMetaData();

            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                modelo.addColumn(metaData.getColumnName(i));
            }

            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                modelo.addRow(row);
            }
            
            TableRowFilterSupport.forTable(tb_data).searchable(true).actions(true).useTableRenderers(true).apply();

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tb_data.getModel());
        tb_data.setRowSorter(sorter);

        // Añadir el RowSorterListener
        sorter.addRowSorterListener(new RowSorterListener() {
            @Override
            public void sorterChanged(RowSorterEvent e) {
                calculateAndDisplayTotal(tb_data, txt_total);
            }
        });

            if (tb_data.getRowCount() > 0) {
                calculateAndDisplayTotal(tb_data, txt_total);
            } else {
                txt_total.setText("0.00");
            }

            packColumns(tb_data);

        } catch (SQLException ex) {
            Logger.getLogger(Detallado.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void cargarDatosPatrimonio(int año, int mes, String tipo, String cuentas, String concepto, int columna) {
        DecimalFormat df = new DecimalFormat("###,###,##0.00");
        JTableHeader header = tb_data.getTableHeader();
        header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 40));
        header.setBackground(new java.awt.Color(0, 102, 153));
        header.setForeground(new java.awt.Color(255, 255, 255));
        tb_data.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

        try {
            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "";
            int columnasumar = columna;

            if (tipo.equals("capital")) {
                ColumnaSumar = columnasumar;
                sql = "SELECT "
                        + "      [Cuenta] "
                        + "      ,[Descripcion] "
                        + "      ,[Debe Anterior] "
                        + "      ,[Haber Anterior] "
                        + "      ,[Debe Mes (restando)] "
                        + "      ,[Haber Mes (sumar)] "
                        + "      ,[Debe Acumulado] "
                        + "      ,[Haber Acumulado] "
                        + "      ,[Deudor] "
                        + "      ,[Acreedor] "
                        + "      ,[Activo] "
                        + "      ,[Pasivo] "
                        + "      ,[Perdida Naturaleza] "
                        + "      ,[Ganancia Naturaleza] "
                        + "      ,[Perdida Funcion] "
                        + "      ,[Ganancia Funcion] "
                        + "  FROM [dbo].[patrimonio] "
                        + "WHERE Cuenta IN (" + cuentas + ") AND Año = " + año + " AND Mes = " + mes + " ";
            } 
            
            ResultSet rs = st.executeQuery(sql);

            ResultSetMetaData metaData = rs.getMetaData();

            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                modelo.addColumn(metaData.getColumnName(i));
            }

            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                modelo.addRow(row);
            }
            
            TableRowFilterSupport.forTable(tb_data).searchable(true).actions(true).useTableRenderers(true).apply();

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tb_data.getModel());
        tb_data.setRowSorter(sorter);

        // Añadir el RowSorterListener
        sorter.addRowSorterListener(new RowSorterListener() {
            @Override
            public void sorterChanged(RowSorterEvent e) {
                calculateAndDisplayTotal(tb_data, txt_total);
            }
        });

            if (tb_data.getRowCount() > 0) {
                calculateAndDisplayTotal(tb_data, txt_total);
            } else {
                txt_total.setText("0.00");
            }

            packColumns(tb_data);

        } catch (SQLException ex) {
            Logger.getLogger(Detallado.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void calculateAndDisplayTotal(JTable table, JLabel totalLabel) {
        DecimalFormat df = new DecimalFormat("###,###,##0.00");
        double total = 0.0;

        TableModel model = table.getModel();
        RowSorter<? extends TableModel> rowSorter = table.getRowSorter();

        // Suponiendo que ColumnaSumar contiene el índice de la columna que deseas sumar
        int columnaSumar = ColumnaSumar; // Asegúrate de que ColumnaSumar tenga el valor adecuado

        for (int i = 0; i < rowSorter.getViewRowCount(); i++) {
            Object value = model.getValueAt(rowSorter.convertRowIndexToModel(i), columnaSumar);
            if (value != null) {
                total += Double.parseDouble(value.toString());
            }
        }

        // Mostrar el total en el label
        totalLabel.setText("<html>Total: S/ <font color='RED'>" + df.format(total) + "</font></html>");
    }

    private void packColumns(JTable table) {
        TableColumnModel columnModel = table.getColumnModel();
        for (int columnIndex = 0; columnIndex < columnModel.getColumnCount(); columnIndex++) {
            TableColumn column = columnModel.getColumn(columnIndex);

            // Obtenemos el título de la columna
            TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
            Object headerValue = column.getHeaderValue();
            Component headerComponent = headerRenderer.getTableCellRendererComponent(table, headerValue, false, false, -1, columnIndex);
            int headerWidth = headerComponent.getPreferredSize().width;

            // Iteramos sobre cada fila para determinar el ancho mínimo
            int maxCellWidth = headerWidth;
            for (int rowIndex = 0; rowIndex < table.getRowCount(); rowIndex++) {
                TableCellRenderer cellRenderer = table.getCellRenderer(rowIndex, columnIndex);
                Component cellComponent = table.prepareRenderer(cellRenderer, rowIndex, columnIndex);
                int cellWidth = cellComponent.getPreferredSize().width;
                maxCellWidth = Math.max(maxCellWidth, cellWidth);
            }

            // Establecemos el ancho mínimo de la columna
            column.setMinWidth(maxCellWidth);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        jPanel1 = new javax.swing.JPanel();
        txt_concepto = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_data = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        txt_total = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        txt_total1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Detallado de Conceptos");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        txt_concepto.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        txt_concepto.setForeground(new java.awt.Color(255, 0, 0));
        txt_concepto.setText("-");

        tb_data.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        tb_data.setModel(modelo);
        tb_data.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tb_data.setRowHeight(25);
        tb_data.setShowHorizontalLines(true);
        jScrollPane1.setViewportView(tb_data);

        jLabel3.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel3.setText("Concepto:");

        txt_total.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        txt_total.setForeground(new java.awt.Color(255, 0, 0));
        txt_total.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txt_total.setText("-");

        jButton1.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Microsoft excel.png"))); // NOI18N
        jButton1.setText("Exportar a Excel");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        txt_total1.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        txt_total1.setForeground(new java.awt.Color(255, 0, 0));
        txt_total1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1265, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_concepto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_total1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_total, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_concepto)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_total1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_total, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            File archivog;
            jFileChooser1.setAcceptAllFileFilterUsed(false);
            jFileChooser1.addChoosableFileFilter(new FileNameExtensionFilter("Archivo Excel", "xlsx"));
            jFileChooser1.setSelectedFile(new File("Detallado Concepto - " + txt_concepto.getText()));

            int si = jFileChooser1.showSaveDialog(this);
            if (si == JFileChooser.APPROVE_OPTION) {
                archivog = jFileChooser1.getSelectedFile();
                String filename = jFileChooser1.getSelectedFile().toString();
                if (!filename.endsWith(".xlsx")) {
                    filename += ".xlsx";
                }

                if (FuncionesGlobales.Writer_XLSX(tb_data, filename, txt_concepto.getText())) {
                    JOptionPane.showMessageDialog(rootPane, "Se Genero Correctamente el Archivo Excel", "Exportar", JOptionPane.INFORMATION_MESSAGE);
                    int response = JOptionPane.showConfirmDialog(null, "¿Desea abrir el archivo Excel generado?", "Abrir archivo", JOptionPane.YES_NO_OPTION);
                    if (response == JOptionPane.YES_OPTION) {
                        if (Desktop.isDesktopSupported()) {
                            Desktop desktop = Desktop.getDesktop();
                            try {
                                desktop.open(new File(filename));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }

            }
        } catch (IOException ex) {
            Logger.getLogger(Detallado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Failed to initialize LaF");
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Detallado dialog = new Detallado(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tb_data;
    private javax.swing.JLabel txt_concepto;
    private javax.swing.JLabel txt_total;
    public static javax.swing.JLabel txt_total1;
    // End of variables declaration//GEN-END:variables
}
