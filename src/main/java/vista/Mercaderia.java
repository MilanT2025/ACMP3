/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import Controlador.Conexion;
import Controlador.FuncionesGlobales;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import main.Application;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Farmacia-Ingeniero
 */
public class Mercaderia extends javax.swing.JFrame {

    private final Modelo modelo = new Modelo();

    Locale locale;
    DecimalFormatSymbols symbols;

    public class Modelo extends DefaultTableModel {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    /**
     * Creates new form Depreciacion
     */
    public Mercaderia() {
        initComponents();

        locale = new Locale("es", "PE");
        Locale.setDefault(locale);

        symbols = new DecimalFormatSymbols(locale);
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');

        UIManager.getLookAndFeelDefaults().put("Table.alternateRowColor", new Color(254, 238, 184));
        Locale.setDefault(new Locale("es", "ES"));
        this.setIconImage(new ImageIcon(System.getProperty("user.dir") + "/logoACMP.png").getImage());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(false);
                FlatMacDarkLaf.setup();
                Application anteriorFrame = new Application();
                anteriorFrame.setVisible(true);
            }
        });
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        jdc_mes.setMonth((Calendar.getInstance().get(Calendar.MONTH)) - 1);

        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                realizarBusqueda(txtBuscar.getText().trim(), 4, tb_resultado);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                realizarBusqueda(txtBuscar.getText().trim(), 4, tb_resultado);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                realizarBusqueda(txtBuscar.getText().trim(), 4, tb_resultado);
            }
        });

        cargarDatos();

        jdc_mes.addPropertyChangeListener("month", (java.beans.PropertyChangeEvent evt) -> {
            cargarDatos();
        });
        jdc_año.addPropertyChangeListener("year", (java.beans.PropertyChangeEvent evt) -> {
            cargarDatos();
        });
    }

    private void llenar_tabla() {
        modelo.setRowCount(0);
        modelo.setColumnCount(0);

        JTableHeader header = tb_resultado.getTableHeader();
        header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 50));
        header.setBackground(new java.awt.Color(255, 217, 102));
        tb_resultado.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

        modelo.addColumn("Tipo");
        modelo.addColumn("Sede");
        modelo.addColumn("Codigo");
        modelo.addColumn("Cuenta");
        modelo.addColumn("Producto");
        modelo.addColumn("U/M");
        modelo.addColumn("Cant. Stock");
        modelo.addColumn("Precio Vta. Unitario");
        modelo.addColumn("Total Precio de Venta");
        modelo.addColumn("IGV");
        modelo.addColumn("Total de Valor de Venta");

        TableColumnModel columnModel = tb_resultado.getColumnModel();
        for (int col = 0; col < tb_resultado.getColumnCount(); col++) {
            TableColumn tableColumn = columnModel.getColumn(col);
            Component comp = header.getDefaultRenderer().getTableCellRendererComponent(tb_resultado, tableColumn.getHeaderValue(), false, false, 0, col);
            int width = comp.getPreferredSize().width + 25;
            tableColumn.setPreferredWidth(width);
        }

        DecimalFormat decimalFormat = new DecimalFormat("0.00", symbols);

        int columnCount = tb_resultado.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            if (i == 7 || i == 8 || i == 9 || i == 10) {
                tb_resultado.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
                    @Override
                    public void setValue(Object value) {
                        if (value instanceof Number) {
                            value = decimalFormat.format(value);
                        }
                        super.setValue(value);
                    }
                });
            }
        }

        cargaStockMercaderias();

    }

    private void cargaStockMercaderias() {
        try {
            Object data[] = new Object[11];
            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "DECLARE @Anio INT, @Mes INT; "
                    + "SET @Anio = " + jdc_año.getYear() + "; "
                    + "SET @Mes = " + (jdc_mes.getMonth() + 1) + "; "
                    + "WITH cte AS ( "
                    + "    SELECT Codigo, [Precio Sin IGV], [F# Emision], "
                    + "           ROW_NUMBER() OVER (PARTITION BY Codigo ORDER BY [F# Emision] DESC) AS rn "
                    + "    FROM ComprasDetalladasGlobal "
                    + ") "
                    + "SELECT Codigo, [Precio Sin IGV] INTO #Temp1 "
                    + "FROM cte "
                    + "WHERE rn = 1; "
                    + "SELECT "
                    + "    SA.Tipo, "
                    + "    SA.Sede, "
                    + "    SA.Codigo, "
                    + "    SUBSTRING(SA.Codigo, 0, 7) AS Cuenta, "
                    + "    SA.Producto, "
                    + "    SA.[U/M], "
                    + "    SA.[Cant# Stock] "
                    + "INTO #Temp2 "
                    + "FROM StockAlmacen SA "
                    + "WHERE "
                    + " SA.Año = @Anio AND SA.Tipo NOT LIKE 'PRODUCTOS TERMINADOS' AND "
                    + "    SA.Mes = @Mes "
                    + "ORDER BY SA.Producto; "
                    + "SELECT "
                    + "    Tipo,	Sede,	T2.Codigo,	Cuenta,	Producto,	[U/M],	[Cant# Stock],	ISNULL([Precio Sin IGV], PM.Precio) AS [Precio Sin IGV],	[Cant# Stock] * ISNULL([Precio Sin IGV], PM.Precio) AS [Total Precio de Venta], 	(([Cant# Stock] * ISNULL([Precio Sin IGV], PM.Precio)) * 0.18) AS [IGV],	([Cant# Stock] * ISNULL([Precio Sin IGV], PM.Precio)) + ((([Cant# Stock] * ISNULL([Precio Sin IGV], PM.Precio)) * 0.18)) AS [Total Valor de Venta] "
                    + "FROM #Temp2 T2 "
                    + "LEFT JOIN #Temp1 T1 ON T2.Codigo = T1.Codigo "
                    + "LEFT JOIN PrecioTempMercaderia PM ON T2.Codigo = PM.Codigo "
                    + "ORDER BY T2.Producto; "
                    + "IF OBJECT_ID('tempdb..#Temp1') IS NOT NULL "
                    + "    DROP TABLE #Temp1; "
                    + "IF OBJECT_ID('tempdb..#Temp2') IS NOT NULL "
                    + "    DROP TABLE #Temp2;";

            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                for (int i = 0; i < data.length; i++) {
                    data[i] = rs.getObject(i + 1);
                }
                modelo.addRow(data);
            }
            rs.close();
            st.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(Mercaderia.class.getName()).log(Level.SEVERE, null, ex);
        }

        calcularTotales();
        packColumns(tb_resultado);
    }

    private void calcularTotales() {
        double sv = 0, si = 0, stv = 0;
        DecimalFormat df = new DecimalFormat("0.00", symbols);

        for (int i = 0; i < tb_resultado.getRowCount(); i++) {
            // Verificar si las celdas están vacías o son nulas
            String valorVenta = tb_resultado.getValueAt(i, 8) != null ? tb_resultado.getValueAt(i, 8).toString() : "0";
            String valorIgv = tb_resultado.getValueAt(i, 9) != null ? tb_resultado.getValueAt(i, 9).toString() : "0";
            String valorTotal = tb_resultado.getValueAt(i, 10) != null ? tb_resultado.getValueAt(i, 10).toString() : "0";

            // Si están vacíos, se consideran como 0
            sv += valorVenta.isEmpty() ? 0 : Double.parseDouble(valorVenta);
            si += valorIgv.isEmpty() ? 0 : Double.parseDouble(valorIgv);
            stv += valorTotal.isEmpty() ? 0 : Double.parseDouble(valorTotal);
        }

        // Mostrar los totales formateados con dos decimales
        sumatotalperio.setText(df.format(sv));
        sumaigv.setText(df.format(si));
        sumatotalventa.setText(df.format(stv));

    }

    // METODOS ADICIONALES ******************************************************
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

    public void ocultarColumnaAncho(JTable tabla, int columnaIndex) {
        tabla.getColumnModel().getColumn(columnaIndex).setMinWidth(0);
        tabla.getColumnModel().getColumn(columnaIndex).setMaxWidth(0);
        tabla.getColumnModel().getColumn(columnaIndex).setWidth(0);
    }

    private void realizarBusqueda(String textoBusqueda, int columna, JTable jtable) {
        // Si el texto de búsqueda está vacío, restablecemos la selección
        if (textoBusqueda.isEmpty()) {
            jtable.clearSelection();
            return;
        }

        // Iteramos por todas las filas de la tabla para encontrar coincidencias
        boolean encontrado = false;
        for (int i = 0; i < jtable.getRowCount(); i++) {
            // Obtener el valor de la columna 9
            Object valorColumna9 = jtable.getValueAt(i, columna);

            // Verificar si el valor es null
            if (valorColumna9 == null) {
                continue; // Si es null, no hacemos nada y pasamos a la siguiente fila
            }

            // Convertir el valor a String y hacer la búsqueda
            String valorColumna9String = valorColumna9.toString().toLowerCase();

            // Si la búsqueda coincide con la columna 9, seleccionamos esa fila
            if (valorColumna9String.contains(textoBusqueda.toLowerCase())) {
                // Seleccionar la fila que coincide
                jtable.setRowSelectionInterval(i, i);
                encontrado = true;

                // Desplazar la vista para que la fila seleccionada sea visible
                Rectangle rect = jtable.getCellRect(i, 0, true);
                jtable.scrollRectToVisible(rect);

                break; // Detenemos la búsqueda después de encontrar la primera coincidencia
            }
        }

        // Si no se encuentra ninguna coincidencia, deseleccionamos todas las filas
        if (!encontrado) {
            jtable.clearSelection();
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

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_resultado = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jdc_año = new com.toedter.calendar.JYearChooser();
        jdc_mes = new com.toedter.calendar.JMonthChooser();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        sumaigv = new javax.swing.JLabel();
        sumatotalventa = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        sumatotalperio = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txt_razonsocial2 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        ExportarExcel = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        AgregarSedes = new javax.swing.JMenuItem();
        MenuRegistrarEmpleado2 = new javax.swing.JMenuItem();
        MenuRegistrarEmpleado = new javax.swing.JMenuItem();
        MenuRegistrarEmpleado1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Valorizacion de Mercaderias y Existencias");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        tb_resultado.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        tb_resultado.setModel(modelo);
        tb_resultado.setRowHeight(30);
        tb_resultado.setShowGrid(true);
        tb_resultado.setShowHorizontalLines(false);
        tb_resultado.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tb_resultado);

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Fecha de Proceso", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 13))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel12.setText("Año:");

        jdc_mes.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jdc_mes.setForeground(new java.awt.Color(255, 0, 0));
        jdc_mes.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N

        jRadioButton2.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jRadioButton2.setSelected(true);
        jRadioButton2.setText("Mes");
        jRadioButton2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton2ItemStateChanged(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel16.setText("->");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jdc_año, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jdc_mes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(803, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jdc_mes, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(jdc_año, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jRadioButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel1.setText("Buscar por Producto:");

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel7.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel7.setText(": S/");

        jLabel8.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 0, 0));
        jLabel8.setText(": S/");

        sumaigv.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        sumaigv.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        sumaigv.setText("0.00");

        sumatotalventa.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        sumatotalventa.setForeground(new java.awt.Color(255, 0, 0));
        sumatotalventa.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        sumatotalventa.setText("0.00");

        jLabel2.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel2.setText("Suma [Total de Precio de Venta]");

        jLabel3.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel3.setText("Suma [IGV]");

        jLabel4.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 0, 0));
        jLabel4.setText("Suma [Total de Valor de Venta]");

        sumatotalperio.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        sumatotalperio.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        sumatotalperio.setText("0.00");

        jLabel6.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel6.setText(": S/");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sumatotalventa, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sumaigv, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sumatotalperio, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sumatotalperio, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sumaigv, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sumatotalventa, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txt_razonsocial2.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        txt_razonsocial2.setForeground(new java.awt.Color(255, 0, 0));
        txt_razonsocial2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_razonsocial2.setText("-");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBuscar))
                    .addComponent(txt_razonsocial2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_razonsocial2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Upload To FTP.png"))); // NOI18N
        jMenu3.setText("Subir");
        jMenu3.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N

        jMenuItem3.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/sobresalir.png"))); // NOI18N
        jMenuItem3.setText("Archivo Excel");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuBar1.add(jMenu3);

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/flecha-hacia-abajo.png"))); // NOI18N
        jMenu2.setText("Exportar");
        jMenu2.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed(evt);
            }
        });

        ExportarExcel.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        ExportarExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Microsoft excel.png"))); // NOI18N
        ExportarExcel.setText("Excel");
        ExportarExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExportarExcelActionPerformed(evt);
            }
        });
        jMenu2.add(ExportarExcel);

        jMenuBar1.add(jMenu2);

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ajustes.png"))); // NOI18N
        jMenu1.setText("Opciones");

        AgregarSedes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/agregarsedes 1.png"))); // NOI18N
        AgregarSedes.setText("Agregar Sedes");
        AgregarSedes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AgregarSedesActionPerformed(evt);
            }
        });
        jMenu1.add(AgregarSedes);

        MenuRegistrarEmpleado2.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        MenuRegistrarEmpleado2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Profit.png"))); // NOI18N
        MenuRegistrarEmpleado2.setText("Agregar Precio");
        MenuRegistrarEmpleado2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuRegistrarEmpleado2ActionPerformed(evt);
            }
        });
        jMenu1.add(MenuRegistrarEmpleado2);

        MenuRegistrarEmpleado.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        MenuRegistrarEmpleado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/flecha-derecha.png"))); // NOI18N
        MenuRegistrarEmpleado.setText("Resumen");
        MenuRegistrarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuRegistrarEmpleadoActionPerformed(evt);
            }
        });
        jMenu1.add(MenuRegistrarEmpleado);

        MenuRegistrarEmpleado1.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        MenuRegistrarEmpleado1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Upload to the Cloud_1.png"))); // NOI18N
        MenuRegistrarEmpleado1.setText("Verificacion de Carga");
        MenuRegistrarEmpleado1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuRegistrarEmpleado1ActionPerformed(evt);
            }
        });
        jMenu1.add(MenuRegistrarEmpleado1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

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
    // Método para exportar archivos en XLSX

    public static void exportarArchivoExcel(DefaultTableModel model) {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setDialogTitle("Guardar archivo Excel");
        jFileChooser.setFileFilter(new FileNameExtensionFilter("Archivos XLSX", "xlsx"));

        int result = jFileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jFileChooser.getSelectedFile();
            if (!selectedFile.getName().endsWith(".xlsx")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".xlsx");
            }
            try (Workbook workbook = new XSSFWorkbook(); FileOutputStream fos = new FileOutputStream(selectedFile)) {
                Sheet sheet = workbook.createSheet("Datos");

                // Estilo para cabeceras
                CellStyle headerStyle = workbook.createCellStyle();
                Font headerFont = workbook.createFont();
                headerFont.setFontName("Bahnschrift");
                headerFont.setFontHeightInPoints((short) 10);
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);
                headerStyle.setAlignment(HorizontalAlignment.CENTER);
                headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                headerStyle.setWrapText(true);

                // Estilo para datos
                CellStyle dataStyle = workbook.createCellStyle();
                Font dataFont = workbook.createFont();
                dataFont.setFontName("Calibri");
                dataFont.setFontHeightInPoints((short) 10);
                dataStyle.setFont(dataFont);
                dataStyle.setAlignment(HorizontalAlignment.LEFT);
                dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);

                // Estilo para datos numéricos
                CellStyle numericStyle = workbook.createCellStyle();
                numericStyle.setFont(dataFont);
                numericStyle.setAlignment(HorizontalAlignment.RIGHT);
                numericStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                numericStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

                // Crear encabezados
                Row headerRow = sheet.createRow(0);
                headerRow.setHeightInPoints(40);
                for (int i = 0; i < model.getColumnCount(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(model.getColumnName(i));
                    cell.setCellStyle(headerStyle);
                }

                // Crear datos
                for (int i = 0; i < model.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        Cell cell = row.createCell(j);
                        Object value = model.getValueAt(i, j);

                        if (value instanceof Number) {
                            cell.setCellValue(((Number) value).doubleValue());
                            cell.setCellStyle(numericStyle);
                        } else {
                            String cellValue = (value != null) ? value.toString() : "";
                            cell.setCellValue(cellValue);
                            cell.setCellStyle(dataStyle);
                        }
                    }
                }

                // Ajustar el tamaño de las columnas
                for (int i = 0; i < model.getColumnCount(); i++) {
                    // Ajustar al tamaño de los títulos
                    sheet.autoSizeColumn(i);
                    int currentWidth = sheet.getColumnWidth(i);
                    sheet.autoSizeColumn(i);
                    int adjustedWidth = Math.max(sheet.getColumnWidth(i), currentWidth);
                    sheet.setColumnWidth(i, adjustedWidth);
                }

                // Inmovilizar fila 1
                sheet.createFreezePane(0, 1);

                workbook.write(fos);
                JOptionPane.showMessageDialog(null, "Archivo exportado exitosamente.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al exportar el archivo: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Exportación cancelada.");
        }
    }

    private void cargarDatos() {
        llenar_tabla();
        FuncionesGlobales.colocarnombremesannio(jdc_año, jdc_mes, txt_razonsocial2);
    }

    private void ExportarExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExportarExcelActionPerformed
        DefaultTableModel modelo = (DefaultTableModel) tb_resultado.getModel();

        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "No hay datos para exportar.");
            return;
        }

        exportarArchivoExcel(modelo);    }//GEN-LAST:event_ExportarExcelActionPerformed

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed

    }//GEN-LAST:event_jMenu2ActionPerformed

    private void jRadioButton2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton2ItemStateChanged

    }//GEN-LAST:event_jRadioButton2ItemStateChanged

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        CargaArchivos ini = new CargaArchivos(this, true);
        ini.toFront();
        ini.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void MenuRegistrarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuRegistrarEmpleadoActionPerformed
        VerificacionMercaderias ini = new VerificacionMercaderias(this, true, txt_razonsocial2.getText(), sumatotalventa.getText());
        ini.setVisible(true);
    }//GEN-LAST:event_MenuRegistrarEmpleadoActionPerformed

    private void MenuRegistrarEmpleado1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuRegistrarEmpleado1ActionPerformed
        VerificacionCargaMercaderia ini = new VerificacionCargaMercaderia(this, true);
        ini.setVisible(true);
    }//GEN-LAST:event_MenuRegistrarEmpleado1ActionPerformed

    private void MenuRegistrarEmpleado2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuRegistrarEmpleado2ActionPerformed
        AgregarPrecioMercaderia ini = new AgregarPrecioMercaderia(this, true);
        ini.setVisible(true);
    }//GEN-LAST:event_MenuRegistrarEmpleado2ActionPerformed

    private void AgregarSedesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AgregarSedesActionPerformed
        AgregarSedes ini = new AgregarSedes(this, true);
        ini.setVisible(true);
    }//GEN-LAST:event_AgregarSedesActionPerformed

    // Método para exportar los datos de la tabla a Excel
    public static void exportarAExcel(DefaultTableModel model, String mesannio) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar archivo Excel");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos Excel", "xlsx"));
        fileChooser.setSelectedFile(new File("Carga Asiento CTS - " + mesannio + ".xlsx"));

        // Mostrar el diálogo para guardar archivo
        int resultado = fileChooser.showSaveDialog(null);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            String rutaArchivo = archivoSeleccionado.getAbsolutePath();
            if (!rutaArchivo.endsWith(".xlsx")) {
                archivoSeleccionado = new File(rutaArchivo + ".xlsx");
            }

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Voucher");
                Font fuenteGeneral = workbook.createFont();
                fuenteGeneral.setFontName("Bahnschrift");
                fuenteGeneral.setFontHeightInPoints((short) 10);

                // Estilo general para las celdas
                CellStyle estiloGeneral = workbook.createCellStyle();
                estiloGeneral.setFont(fuenteGeneral);

                // Estilo para el encabezado
                CellStyle headerStyle = workbook.createCellStyle();
                headerStyle.setFont(fuenteGeneral);
                headerStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
                headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                headerStyle.setBorderBottom(BorderStyle.THIN);
                headerStyle.setBorderTop(BorderStyle.THIN);

                // Crear la fila de encabezados
                Row headerRow = sheet.createRow(0);
                for (int col = 0; col < model.getColumnCount(); col++) {
                    Cell cell = headerRow.createCell(col);
                    cell.setCellValue(model.getColumnName(col));
                    cell.setCellStyle(headerStyle);
                }

                // Crear las filas de datos
                for (int row = 0; row < model.getRowCount(); row++) {
                    Row dataRow = sheet.createRow(row + 1);
                    for (int col = 0; col < model.getColumnCount(); col++) {
                        Cell cell = dataRow.createCell(col);
                        Object value = model.getValueAt(row, col);

                        // Formatear y establecer el valor en la celda
                        if (value instanceof Number) {
                            cell.setCellValue(((Number) value).doubleValue());
                            CellStyle numberStyle = workbook.createCellStyle();
                            numberStyle.cloneStyleFrom(estiloGeneral);

                            // Formato con 2 decimales si es decimal
                            DataFormat format = workbook.createDataFormat();
                            numberStyle.setDataFormat(format.getFormat("#.##"));
                            cell.setCellStyle(numberStyle);
                        } else if (value instanceof String && value.toString().matches("\\d{4}-\\d{2}-\\d{2}")) {
                            cell.setCellValue(value.toString());
                            CellStyle dateStyle = workbook.createCellStyle();
                            dateStyle.cloneStyleFrom(estiloGeneral);
                            DataFormat format = workbook.createDataFormat();
                            dateStyle.setDataFormat(format.getFormat("yyyy-mm-dd"));
                            cell.setCellStyle(dateStyle);
                        } else {
                            cell.setCellValue(value.toString());
                            cell.setCellStyle(estiloGeneral);
                        }

                        // Aplicar bordes verticales
                        CellStyle borderStyle = workbook.createCellStyle();
                        borderStyle.cloneStyleFrom(estiloGeneral);
                        borderStyle.setBorderLeft(BorderStyle.THIN);
                        borderStyle.setBorderRight(BorderStyle.THIN);
                        cell.setCellStyle(borderStyle);
                    }
                }

                // Ajustar el tamaño de las columnas automáticamente
                for (int col = 0; col < model.getColumnCount(); col++) {
                    sheet.autoSizeColumn(col);
                }

                // Guardar el archivo Excel
                try (FileOutputStream fileOut = new FileOutputStream(archivoSeleccionado)) {
                    workbook.write(fileOut);
                    System.out.println("Los datos han sido exportados a Excel exitosamente.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            /* Set the Nimbus look and feel */
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
            /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
            * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
             */
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
            //</editor-fold>

            /* Create and display the form */
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    UIManager.getLookAndFeelDefaults().put("Table.alternateRowColor", new Color(254, 238, 184));
                    new Mercaderia().setVisible(true);
                }
            });
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Mercaderia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem AgregarSedes;
    private javax.swing.JMenuItem ExportarExcel;
    private javax.swing.JMenuItem MenuRegistrarEmpleado;
    private javax.swing.JMenuItem MenuRegistrarEmpleado1;
    private javax.swing.JMenuItem MenuRegistrarEmpleado2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    public static com.toedter.calendar.JYearChooser jdc_año;
    public static com.toedter.calendar.JMonthChooser jdc_mes;
    private javax.swing.JLabel sumaigv;
    private javax.swing.JLabel sumatotalperio;
    private javax.swing.JLabel sumatotalventa;
    private javax.swing.JTable tb_resultado;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JLabel txt_razonsocial2;
    // End of variables declaration//GEN-END:variables
}
