/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import Controlador.Conexion;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ItemEvent;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import main.Application;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.oxbow.swingbits.table.filter.TableRowFilterSupport;

/**
 *
 * @author Ernesto
 */
public class CuentasporPagar extends javax.swing.JFrame {

    private final Modelo modelo = new Modelo();
    Date fecha = new Date();

    Locale locale;
    DecimalFormatSymbols symbols;

    public class Modelo extends DefaultTableModel {

        @Override
        public boolean isCellEditable(int row, int column) {
            if (column == 0) {
                return true;
            }

            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) {
                return Boolean.class;
            }
            return super.getColumnClass(columnIndex);
        }

    }

    /**
     * Creates new form CuentasporPagar
     */
    public CuentasporPagar() {
        initComponents();
        locale = new Locale("es", "PE");
        Locale.setDefault(locale);

        jDateChooser1.setDate(new Date());

        symbols = new DecimalFormatSymbols(locale);
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');

        UIManager.getLookAndFeelDefaults().put("Table.alternateRowColor", new Color(254, 238, 184));
        Locale.setDefault(new Locale("es", "ES"));
        this.setIconImage(new ImageIcon(System.getProperty("user.dir") + "/logoACMP.png").getImage());
//        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//        addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                setVisible(false);
//                FlatMacDarkLaf.setup();
//                Application anteriorFrame = new Application();
//                anteriorFrame.setVisible(true);
//            }
//        });
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        jDateChooser1.addPropertyChangeListener("date", (java.beans.PropertyChangeEvent evt) -> {
            if (jDateChooser1.getDate() != null) {
                llenar_tabla(); // Llama al método para llenar la tabla
            }
        });

        llenar_tabla();
    }
    
   
int valor = 0;
    //FuncionesGlobales.colocarnombremesannio (jdc_año, jdc_mes, txt_razonsocial2);
    private void llenar_tabla() {
        txt_razonsocial2.setText("Fecha de Carga: " + new SimpleDateFormat("dd/MM/yyyy").format(jDateChooser1.getDate()));

        modelo.setRowCount(0);
        modelo.setColumnCount(0);

        JTableHeader header = tb_resultado.getTableHeader();
        header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 50));
        header.setBackground(new java.awt.Color(255, 217, 102));
        tb_resultado.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

        modelo.addColumn("Seleccionar");
        modelo.addColumn("Estado");
        modelo.addColumn("CUO");
        modelo.addColumn("Comprobante N°");
        modelo.addColumn("F. Registro");
        modelo.addColumn("F. Emision");
        modelo.addColumn("F. Vencimiento");
        modelo.addColumn("Nro Doc. Ident.");
        modelo.addColumn("Razon Social");
        modelo.addColumn("Moneda");
        modelo.addColumn("Total");
        modelo.addColumn("Saldo MN");
        modelo.addColumn("Saldo ME");
        modelo.addColumn("Plan Cuenta");
        modelo.addColumn("Descripcion");
        modelo.addColumn("Centro Costo");
        modelo.addColumn("% Pagado");
        modelo.addColumn("Total Pagado");
        modelo.addColumn("N° Transferencia");
        modelo.addColumn("Fecha Pago");

        TableColumnModel columnModel = tb_resultado.getColumnModel();
        for (int col = 0; col < tb_resultado.getColumnCount(); col++) {
            TableColumn tableColumn = columnModel.getColumn(col);
            Component comp = header.getDefaultRenderer().getTableCellRendererComponent(tb_resultado, tableColumn.getHeaderValue(), false, false, 0, col);
            int width = comp.getPreferredSize().width + 25;
            tableColumn.setPreferredWidth(width);
        }
        
        tb_resultado.getColumnModel().getColumn(0).setCellRenderer(new AlternateRowCheckBoxRenderer());

        TableColumn column = tb_resultado.getColumnModel().getColumn(0);
        column.setHeaderRenderer(new CheckBoxHeaderRenderer(tb_resultado));

        TableRowFilterSupport.forTable(tb_resultado).searchable(true).actions(true).useTableRenderers(true).apply();

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tb_resultado.getModel());
        tb_resultado.setRowSorter(sorter);

         tb_resultado.setDefaultRenderer(Object.class, new CustomRowColorRenderer(1)); // Índice 4 = columna 17

        
        cargaDatos();

    }
    
     static class CustomRowColorRenderer extends DefaultTableCellRenderer {
        private final int columnToCheck;

        public CustomRowColorRenderer(int columnToCheck) {
            this.columnToCheck = columnToCheck;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Obtener el valor de la columna específica
            Object estado = table.getValueAt(row, columnToCheck);

            // Determinar el color de fondo según el valor de la columna
            if (!isSelected) {
                if ("PAGADO".equals(estado)) {
                    Color alternateColor = UIManager.getLookAndFeelDefaults().getColor("Table.alternateRowColor");
                    c.setBackground((row % 2 == 0) ? table.getBackground() : alternateColor);
                    c.setForeground(Color.BLUE); // Fondo azul si el valor es "100%"
                } else if ("PROCESADO".equals(estado)){
                    Color alternateColor = UIManager.getLookAndFeelDefaults().getColor("Table.alternateRowColor");
                    c.setBackground((row % 2 == 0) ? table.getBackground() : alternateColor);
                    c.setForeground(Color.MAGENTA);
                }else {
                    // Respetar los colores alternos de UIManager
                    Color alternateColor = UIManager.getLookAndFeelDefaults().getColor("Table.alternateRowColor");
                    c.setBackground((row % 2 == 0) ? table.getBackground() : alternateColor);
                    c.setForeground(Color.BLACK);
                }
            } else {
                // Respetar colores de selección
                c.setBackground(table.getSelectionBackground());
                c.setForeground(table.getSelectionForeground());
            }

            return c;
        }
    }

    
    static class CheckBoxHeaderRenderer extends JCheckBox implements TableCellRenderer {

        private final JTable table;

        public CheckBoxHeaderRenderer(JTable table) {
            this.table = table;
            this.setHorizontalAlignment(SwingConstants.CENTER);

            this.addItemListener(e -> {
                boolean selected = e.getStateChange() == ItemEvent.SELECTED;
                for (int i = 0; i < table.getRowCount(); i++) {
                    table.setValueAt(selected, i, 0); // Marca/desmarca las filas de la primera columna
                }
            });
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            this.setSelected((value != null && (Boolean) value));
            return this;
        }
    }

    static class AlternateRowCheckBoxRenderer extends JCheckBox implements TableCellRenderer {

        public AlternateRowCheckBoxRenderer() {
            this.setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            this.setSelected(value != null && (Boolean) value);

            // Aplicar colores alternos
            Color alternateColor = (Color) UIManager.getLookAndFeelDefaults().get("Table.alternateRowColor");
            Color defaultColor = table.getBackground();

            if (!isSelected) {
                this.setBackground(row % 2 == 0 ? defaultColor : alternateColor);
            } else {
                this.setBackground(table.getSelectionBackground());
            }

            return this;
        }
    }

    private void cargaDatos() {
        try {
            Object data[] = new Object[20];
            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "SELECT "
                    + "	   CASE WHEN (SELECT TOP 1 CONCAT(PorcentajePagado, '%') FROM CuentasPorPagarDiario WHERE Factura = [Comprobante Nº] ORDER BY FechaEvaluacion DESC) = '100%' THEN 'PAGADO' WHEN (SELECT TOP 1 CONCAT(PorcentajePagado, '%') FROM CuentasPorPagarDiario WHERE Factura = [Comprobante Nº] ORDER BY FechaEvaluacion DESC) = '100.0%' THEN 'PAGADO'WHEN (SELECT COUNT(*) FROM CuentasPorPagarDiario WHERE Factura = [Comprobante Nº]) > 0 THEN 'PROCESADO' ELSE Estado END AS [Estado]    "
                    + "      ,CONVERT(INT, [CUO]) AS CUO "
                    + "      ,[Comprobante Nº] "
                    + "      ,FORMAT([F# Registro], 'dd/MM/yyyy') AS [F# Registro]"
                    + "      ,FORMAT([F# Emision], 'dd/MM/yyyy') AS [F# Emision]"
                    + "      ,FORMAT([F# Vencimiento], 'dd/MM/yyyy') AS [F# Vencimiento]"
                    + "      ,[Nro Doc# Ident#] "
                    + "      ,[Razon Social] "
                    + "      ,[Moneda] "
                    + "      ,[Total] "
                    + "      ,[Saldo MN] "
                    + "      ,[Saldo ME] "
                    + "      ,[Plan Cuenta] "
                    + "      ,[Descripcion] "
                    + "      ,[Centro Costo] "
                    + "      ,(SELECT TOP 1 CASE WHEN CONCAT(PorcentajePagado, '%') = '%' THEN '' ELSE CONCAT(PorcentajePagado, '%') END FROM CuentasPorPagarDiario WHERE Factura = [Comprobante Nº] ORDER BY FechaEvaluacion DESC) as PorcentajePag "
                    + "      ,(SELECT TOP 1 TotalPagado FROM CuentasPorPagarDiario WHERE Factura = [Comprobante Nº] ORDER BY FechaEvaluacion DESC) as TotalPago "
                    + "      ,(SELECT TOP 1 [N° Transferencia] FROM CuentasPorPagarDiario WHERE Factura = [Comprobante Nº] ORDER BY FechaEvaluacion DESC) as Transf  "
                    + "      ,(SELECT TOP 1 FORMAT(FechaPago, 'dd/MM/yyyy') FROM CuentasPorPagarDiario WHERE Factura = [Comprobante Nº] ORDER BY FechaEvaluacion DESC) AS FechaPago "
                    + "FROM CuentasPorPagar "
                    + "WHERE "
                    + "	Estado = 'PENDIENTE' AND "
                    + "	[Plan Cuenta] LIKE '42%' AND "
                    + "	FechaCarga = '" + new SimpleDateFormat("dd/MM/yyyy").format(jDateChooser1.getDate()) + "' "
                    + "	ORDER BY [Razon Social], [F# Emision] " ;

            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                data[0] = false;
                for (int i = 1; i < data.length; i++) {
                    data[i] = rs.getObject(i);
                }
                modelo.addRow(data);
            }
            rs.close();
            st.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(Mercaderia.class.getName()).log(Level.SEVERE, null, ex);
        }

        packColumns(tb_resultado);
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

        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_resultado = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser("dd/MM/yyyy","##/##/####", '_');
        txt_razonsocial2 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        ExportarExcel = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        ExportarExcel1 = new javax.swing.JMenuItem();
        ExportarExcel2 = new javax.swing.JMenuItem();

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cuentas por Pagar");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        tb_resultado.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        tb_resultado.setModel(modelo);
        tb_resultado.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tb_resultado.setRowHeight(30);
        tb_resultado.setShowGrid(true);
        tb_resultado.setShowHorizontalLines(false);
        tb_resultado.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tb_resultado);

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Fecha de Proceso", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 13))); // NOI18N

        jDateChooser1.setForeground(new java.awt.Color(255, 0, 0));
        jDateChooser1.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jDateChooser1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jDateChooser1PropertyChange(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                .addContainerGap())
        );

        txt_razonsocial2.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        txt_razonsocial2.setForeground(new java.awt.Color(255, 0, 0));
        txt_razonsocial2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_razonsocial2.setText("-");

        jButton2.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_outgoing_data_32px.png"))); // NOI18N
        jButton2.setText("Procesar Informacion");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Leyenda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 13))); // NOI18N

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Pendiente");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(255, 0, 255));

        jLabel2.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Procesado > 0% y <100%");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(0, 0, 255));

        jLabel3.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Pagado 100%");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(625, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton3.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jButton3.setText("...");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_razonsocial2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_razonsocial2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        ExportarExcel1.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        ExportarExcel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Profit.png"))); // NOI18N
        ExportarExcel1.setText("Modulo de Pago");
        ExportarExcel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExportarExcel1ActionPerformed(evt);
            }
        });
        jMenu1.add(ExportarExcel1);

        ExportarExcel2.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        ExportarExcel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Rich Text Converter_1.png"))); // NOI18N
        ExportarExcel2.setText("Valores [Ret/Det]");
        ExportarExcel2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExportarExcel2ActionPerformed(evt);
            }
        });
        jMenu1.add(ExportarExcel2);

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

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        CargaArchivos ini = new CargaArchivos(this, true);
        ini.toFront();
        ini.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void ExportarExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExportarExcelActionPerformed
        DefaultTableModel modelo = (DefaultTableModel) tb_resultado.getModel();

        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "No hay datos para exportar.");
            return;
        }

        exportarArchivoExcel(modelo);
    }//GEN-LAST:event_ExportarExcelActionPerformed

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

                // Crear estilos
                CellStyle headerStyle = workbook.createCellStyle();
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);

                CellStyle integerStyle = workbook.createCellStyle();
                integerStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("0"));

                CellStyle decimalStyle = workbook.createCellStyle();
                decimalStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("0.00"));

                // Crear encabezados
                Row headerRow = sheet.createRow(0);
                for (int i = 1; i < model.getColumnCount(); i++) { // Comenzar desde la segunda columna para ocultar la primera
                    Cell cell = headerRow.createCell(i - 1); // Ajustar índice
                    cell.setCellValue(model.getColumnName(i));
                    cell.setCellStyle(headerStyle);
                }

                // Crear datos
                for (int i = 0; i < model.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 1; j < model.getColumnCount(); j++) { // Comenzar desde la segunda columna
                        Cell cell = row.createCell(j - 1); // Ajustar índice
                        Object value = model.getValueAt(i, j);

                        if (value instanceof Integer) {
                            cell.setCellValue((Integer) value);
                            cell.setCellStyle(integerStyle);
                        } else if (value instanceof Double) {
                            cell.setCellValue((Double) value);
                            cell.setCellStyle(decimalStyle);
                        } else {
                            String cellValue = (value != null) ? value.toString() : "";
                            cell.setCellValue(cellValue);
                        }
                    }
                }

                // Autoajustar columnas
                for (int i = 0; i < model.getColumnCount() - 1; i++) {
                    sheet.autoSizeColumn(i);
                }

                workbook.write(fos);
                JOptionPane.showMessageDialog(null, "Archivo exportado exitosamente.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al exportar el archivo: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Exportación cancelada.");
        }
    }

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed

    }//GEN-LAST:event_jMenu2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        boolean registroSeleccionado = false;
        boolean mensajeAdvertencia = false;  // Variable para detectar si se deben mostrar advertencias

// Primero recorremos los registros para realizar las verificaciones previas
        for (int i = 0; i < tb_resultado.getRowCount(); i++) {
            // Verificar si el checkbox está marcado en la columna 0
            boolean isChecked = Boolean.parseBoolean(tb_resultado.getValueAt(i, 0).toString());

            // Verificar el estado en la columna 1
            String estado = tb_resultado.getValueAt(i, 1).toString();

            // Si el checkbox está marcado y el estado no es "PAGADO" ni "PROCESADO", es un registro válido
            if (isChecked && estado.equals("PENDIENTE")) {
                // Se marcará como seleccionado para continuar con el proceso
                registroSeleccionado = true;
            }

            // Si el checkbox está marcado pero el estado es "PAGADO" o "PROCESADO", se añadirá a la advertencia
            if (isChecked && (estado.equals("PAGADO") || estado.equals("PROCESADO"))) {
                mensajeAdvertencia = true;
            }
        }

// Si no se ha seleccionado ningún registro, se muestra un mensaje de error
        if (!registroSeleccionado) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar como mínimo un registro", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

// Ahora se procesan los registros seleccionados
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        jButton2.setEnabled(false);
        ProcesarCuentasporPagar ini = new ProcesarCuentasporPagar(this, true, new SimpleDateFormat("dd/MM/yyyy").format(jDateChooser1.getDate()));

        Object[] data = new Object[7];
        int c = 1;
        String mensaje = "";

// Recorrer nuevamente los registros para procesar solo los seleccionados y en estado "PENDIENTE"
        for (int i = 0; i < tb_resultado.getRowCount(); i++) {
            if (Boolean.parseBoolean(tb_resultado.getValueAt(i, 0).toString()) && tb_resultado.getValueAt(i, 1).toString().equals("PENDIENTE")) {
                // Procesamos el registro "PENDIENTE"
                data[0] = c;
                data[1] = tb_resultado.getValueAt(i, 7);
                data[2] = tb_resultado.getValueAt(i, 8);
                data[3] = tb_resultado.getValueAt(i, 3);
                data[4] = tb_resultado.getValueAt(i, 5);
                data[5] = tb_resultado.getValueAt(i, 6);
                data[6] = tb_resultado.getValueAt(i, 11);
                ini.cargarInfo(data);
                c++;
            }
        }

// Si hubo registros "PAGADO" o "PROCESADO", mostramos un mensaje de advertencia
        if (mensajeAdvertencia) {
            mensaje = "Uno o más registros están en estado PROCESADO y/o PAGADO, estos no serán procesados.";
            JOptionPane.showMessageDialog(this, mensaje, "Advertencia", JOptionPane.INFORMATION_MESSAGE);
        }

        ini.packColumns();
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        jButton2.setEnabled(true);
        ini.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange
    }//GEN-LAST:event_jDateChooser1PropertyChange

    private void ExportarExcel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExportarExcel1ActionPerformed
        ModuloPago_CuentasPorPagar ini = new ModuloPago_CuentasPorPagar(this, true);
        ini.setVisible(true);
    }//GEN-LAST:event_ExportarExcel1ActionPerformed

    private void ExportarExcel2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExportarExcel2ActionPerformed
        AgregarValoresRetDet ini = new AgregarValoresRetDet(this, true);
        ini.setVisible(true);
    }//GEN-LAST:event_ExportarExcel2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       llenar_tabla();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        ProcesarCuentasporPagar ini = new ProcesarCuentasporPagar(this, true, new SimpleDateFormat("dd/MM/yyyy").format(jDateChooser1.getDate()));
        ini.packColumns();
        ini.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

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
                    new CuentasporPagar().setVisible(true);
                }
            });
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(CuentasporPagar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem ExportarExcel;
    private javax.swing.JMenuItem ExportarExcel1;
    private javax.swing.JMenuItem ExportarExcel2;
    public static javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tb_resultado;
    private javax.swing.JLabel txt_razonsocial2;
    // End of variables declaration//GEN-END:variables
}
