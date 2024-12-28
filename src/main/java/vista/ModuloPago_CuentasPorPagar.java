/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista;

import Controlador.Conexion;
import com.formdev.flatlaf.FlatIntelliJLaf;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
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
 * @author Farmacia-Ingeniero
 */
public class ModuloPago_CuentasPorPagar extends javax.swing.JDialog {

    private final Modelo modelo = new Modelo();

    private Color originalAlternateRowColor;
    
    Locale locale;
    DecimalFormatSymbols symbols;

    /**
     * Creates new form Detallado
     */
    public ModuloPago_CuentasPorPagar(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        jDateChooser1.setDate(new Date());
        
        locale = new Locale("es", "PE");
        Locale.setDefault(locale);
        
        symbols = new DecimalFormatSymbols(locale);
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        
        this.setLocationRelativeTo(null);
        
        modelo.addColumn("FECHA PROC.");//0
        modelo.addColumn("N°");//1
        modelo.addColumn("RUC");//2
        modelo.addColumn("PROVEEDOR");//3
        modelo.addColumn("FACT");//4
        modelo.addColumn("FEC/E");//5
        modelo.addColumn("FEC/V");//6
        modelo.addColumn("MONTO TOTAL");//7
        modelo.addColumn("TIPO DET/RET");//8
        modelo.addColumn("MONTO DET/RET");//9
        modelo.addColumn("TOTAL SIN DET/RET");//10
        modelo.addColumn("RUBRO");//11
        modelo.addColumn("PAGO TOTAL");//12
        modelo.addColumn("PAGO %");//13
        modelo.addColumn("TOTAL PAGADO");//14
        modelo.addColumn("N° TRANSFERENCIA");//15
        modelo.addColumn("IDCUENTA"); //16
        modelo.addColumn("PORCENPAGADO"); //17
        modelo.addColumn("TOTALPAGADO"); //18
        modelo.addColumn("NROTRANSFER"); //19
        modelo.addColumn("MONTO"); //20
        
        JTableHeader header = tb_data.getTableHeader();
        header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 40));
        header.setBackground(new java.awt.Color(0, 102, 153));
        header.setForeground(new java.awt.Color(255, 255, 255));
        tb_data.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

        TableRowFilterSupport.forTable(tb_data).searchable(true).actions(true).useTableRenderers(true).apply();

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tb_data.getModel());
        tb_data.setRowSorter(sorter);
        

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
        
         tb_data.getColumnModel().getColumn(12).setCellRenderer(new AlternateRowCheckBoxRenderer());

    
        
       modelo.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                
                if (column == 12) { // Índice 11 porque las columnas empiezan en 0
                    Boolean valor12 = Boolean.valueOf(tb_data.getValueAt(row, column).toString());
                    if (valor12) {
                        tb_data.setValueAt("100", row, 13);
                    } else {
                        tb_data.setValueAt("", row, 13);
                    }

                    
                }

                // Detectar si se modificó la columna 12
                if (column == 13) { // Índice 11 porque las columnas empiezan en 0
                    Object valor13 = tb_data.getValueAt(row, column);
                    if (valor13 == null || valor13.toString().isEmpty()) {
                        tb_data.setValueAt("", row, 14);

                    } else {
                        double valor13double = Double.parseDouble(valor13.toString());
                        double valor7 = Double.parseDouble(tb_data.getValueAt(row, 7).toString());

                        tb_data.setValueAt(valor7 * (valor13double / 100), row, 14);
                    }

                    
                }
                
                             
                
                
            }
        });
       
        
       
        llenar_tabla();
        
         jDateChooser1.addPropertyChangeListener("date", (java.beans.PropertyChangeEvent evt) -> {
            if (jDateChooser1.getDate() != null) {
                llenar_tabla(); // Llama al método para llenar la tabla
            }
        });
         
         ocultarColumnaAncho(tb_data, 16);
         ocultarColumnaAncho(tb_data, 17);
         ocultarColumnaAncho(tb_data, 18);
         ocultarColumnaAncho(tb_data, 19);
         ocultarColumnaAncho(tb_data, 20);
    }
    
    public void ocultarColumnaAncho(JTable tabla, int columnaIndex) {
        tabla.getColumnModel().getColumn(columnaIndex).setMinWidth(0);
        tabla.getColumnModel().getColumn(columnaIndex).setMaxWidth(0);
        tabla.getColumnModel().getColumn(columnaIndex).setWidth(0);
    }
    
    static class CheckBoxHeaderRenderer extends JCheckBox implements TableCellRenderer {

        private final JTable table;

        public CheckBoxHeaderRenderer(JTable table) {
            this.table = table;
            this.setHorizontalAlignment(SwingConstants.CENTER);

            this.addItemListener(e -> {
                boolean selected = e.getStateChange() == ItemEvent.SELECTED;
                for (int i = 0; i < table.getRowCount(); i++) {
                    table.setValueAt(selected, i, 12); // Marca/desmarca las filas de la primera columna
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
    
    private void llenar_tabla() {
        modelo.setRowCount(0);
        try {
            int c = 1;
            Object data[] = new Object[21];
            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "SELECT "
                    + "	FORMAT(FechaEvaluacion, 'dd/MM/yyyy') AS FechaEvaluacion, "
                    + "	ROW_NUMBER() OVER (ORDER BY Proveedor) AS Numero, "
                    + "	RUC, "
                    + "	Proveedor, "
                    + "	Factura, "
                    + "	FechaEmision, "
                    + "	FechaVencimiento, "
                    + "	Monto-ISNULL(TotalPagado, 0) AS Monto, "
                    + "	CASE WHEN TotalPagado IS NULL THEN Porcentaje  WHEN (Monto-ISNULL(TotalPagado, 0) - CASE WHEN TotalPagado IS NULL THEN ValorCalculado ELSE 0.00 END) = 0.00 THEN 'PAGADO'ELSE 'PENDIENTE' END AS Porcentaje, "
                    + "	CASE WHEN TotalPagado IS NULL THEN ValorCalculado ELSE '0.00' END AS ValorCalculado, "
                    + "	Monto-ISNULL(TotalPagado, 0) - CASE WHEN TotalPagado IS NULL THEN ValorCalculado ELSE 0.00 END AS Total, "
                    + "	Rubro, "
                    + "	idPagos, "
                    + "	PorcentajePagado, "
                    + "	TotalPagado, "
                    + "	[N° Transferencia], "
                    + "	Monto "
                    + "FROM CuentasPorPagarDiario "
                    + "ORDER BY Proveedor";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                
                data[0] = rs.getObject(1);
                data[1] = rs.getObject(2);
                data[2] = rs.getObject(3);
                data[3] = rs.getObject(4);
                data[4] = rs.getObject(5);
                data[5] = rs.getObject(6);
                data[6] = rs.getObject(7);
                data[7] = rs.getObject(8);
                data[8] = rs.getObject(9);
                data[9] = rs.getObject(10);
                data[10] = rs.getObject(11);
                data[11] = rs.getObject(12);
                data[16] = rs.getObject(13);
                data[17] = rs.getObject(14);
                data[18] = rs.getObject(15);
                data[19] = rs.getObject(16);
                data[20] = rs.getObject(17);
               
                modelo.addRow(data);
                c++;
            }
            rs.close();
            st.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(Vacaciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        packColumns();
    }
    

    

    private void guardarInformacion() {
        try {
            Connection con = Conexion.getConnection();

            for (int i = 0; i < tb_data.getRowCount(); i++) {
                if (tb_data.getValueAt(i, 14) != null && !tb_data.getValueAt(i, 14).toString().equals("")) {
                    int idPagos = Integer.parseInt(tb_data.getValueAt(i, 16).toString()); // Este es el ID de la fila que deseas actualizar (puedes usar cualquier valor que corresponda)
                    Object porcentajePagado = tb_data.getValueAt(i, 13); // Nuevo porcentaje pagado
                    double totalPagado = Double.parseDouble(tb_data.getValueAt(i, 14).toString()); // Nuevo total pagado
                    String nrotransferencia = tb_data.getValueAt(i, 15).toString(); // Nueva fecha de evaluación
                    
                    Object value = tb_data.getValueAt(i, 18);
                    double totalpagadoult = (value == null || value.toString().isEmpty()) ? 0.0 : Double.parseDouble(value.toString());
                    
                    String nrotransferult = (tb_data.getValueAt(i, 19) == null) ? "" : tb_data.getValueAt(i, 19).toString() + ", ";

                    
                    Object value2 = tb_data.getValueAt(i, 20);
                    double montotal = (value2 == null || value2.toString().isEmpty()) ? 0.0 : Double.parseDouble(value2.toString());
                    
                    // Consulta UPDATE SQL
                    String sql = "UPDATE CuentasPorPagarDiario SET "
                            + "PorcentajePagado = ?, "
                            + "TotalPagado = ?, "
                            + "[N° Transferencia] = ?, "
                            + "FechaPago = GETDATE() "
                            + "WHERE idPagos = ?";
                    PreparedStatement stmt = con.prepareStatement(sql);
                    stmt.setString(1, String.valueOf(calcularPorcentaje(montotal, totalpagadoult + totalPagado))); // Establecer porcentajePagado si no es null o vacío
                    stmt.setDouble(2, totalpagadoult + totalPagado); // Establece el nuevo porcentaje pagado
                    stmt.setString(3, nrotransferult + nrotransferencia); // Establece el nuevo total pagado
                    stmt.setInt(4, idPagos); // Establece el ID de la fila a actualizar

                    // Ejecutar la consulta UPDATE
                    stmt.executeUpdate();
                }
                
            }

            JOptionPane.showMessageDialog(this, "Se han guardado los datos correctamente");

           
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(ModuloPago_CuentasPorPagar.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public static double calcularPorcentaje(double montoTotal, double importe) {
        if (montoTotal == 0) {
            // Evitar división por cero
            System.out.println("El monto total no puede ser 0.");
            return 0;
        }
        return (importe / montoTotal) * 100;
    }
    
    private double parseDoubleOrDefault(Object value) {
        if (value == null || value.toString().trim().isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            return 0.0; // Si el valor no es convertible a número, se usa 0 como valor predeterminado
        }
    }
    
   private String getStringOrDefault(Object value, String defaultValue) {
    return value == null ? defaultValue : value.toString();
}

   
    public class Modelo extends DefaultTableModel {

        @Override
        public boolean isCellEditable(int row, int column) {
            if (column == 12 || column == 13 || column == 14 || column == 15 ) {
                return true;
            }

            return false;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 12) {
                return Boolean.class;
            }
            return super.getColumnClass(columnIndex);
        }
    

        /*@Override
        public void setValueAt(Object aValue, int row, int column) {
            if (column == 1 || column == 7 || column == 8 || column == 9 || column == 16) {
                try {
                    if (aValue == null || aValue.toString().trim().isEmpty()) {
                        super.setValueAt(null, row, column);
                    } else {
                        int intValue = Integer.parseInt(aValue.toString().trim());
                        super.setValueAt(intValue, row, column);
                    }
                } catch (NumberFormatException ex) {
                    javax.swing.JOptionPane.showMessageDialog(null,
                            "Por favor ingrese un número entero válido en la columna",
                            "Error de entrada",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Permitir otros valores sin restricciones en las demás columnas
                super.setValueAt(aValue, row, column);
            }
        }*/

    }
    

    public void packColumns() {
        TableColumnModel columnModel = tb_data.getColumnModel();
        for (int columnIndex = 0; columnIndex < columnModel.getColumnCount(); columnIndex++) {
            TableColumn column = columnModel.getColumn(columnIndex);

            TableCellRenderer headerRenderer = tb_data.getTableHeader().getDefaultRenderer();
            Object headerValue = column.getHeaderValue();
            Component headerComponent = headerRenderer.getTableCellRendererComponent(tb_data, headerValue, false, false, -1, columnIndex);
            int headerWidth = headerComponent.getPreferredSize().width;

            int maxCellWidth = headerWidth;
            for (int rowIndex = 0; rowIndex < tb_data.getRowCount(); rowIndex++) {
                TableCellRenderer cellRenderer = tb_data.getCellRenderer(rowIndex, columnIndex);
                Component cellComponent = tb_data.prepareRenderer(cellRenderer, rowIndex, columnIndex);
                int cellWidth = cellComponent.getPreferredSize().width;
                maxCellWidth = Math.max(maxCellWidth, cellWidth);
            }

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
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_data = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser("dd/MM/yyyy","##/##/####", '_');
        jMenuBar1 = new javax.swing.JMenuBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Procesamiento de Informacion - Cuentas por Pagar");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        tb_data.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        tb_data.setModel(modelo);
        tb_data.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tb_data.setRowHeight(35);
        tb_data.setShowHorizontalLines(true);
        jScrollPane1.setViewportView(tb_data);

        jButton1.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_add_32px.png"))); // NOI18N
        jButton1.setText("Pagar y Exportar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(0, 102, 153));

        jLabel13.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("MODULO DE PAGO - CUENTAS POR PAGAR");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addContainerGap())
        );

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
                .addContainerGap(1011, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1250, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 584, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );

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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
          if (tb_data.isEditing()) {
            tb_data.getCellEditor().stopCellEditing(); // Detiene la edición actual si está en curso
        }
        
        int opcion = JOptionPane.showConfirmDialog(
            null,
            "¿Deseas guardar la información?",
            "Confirmación",
            JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {
        
        for (int i = 0; i < tb_data.getRowCount(); i++) {
            if (tb_data.getValueAt(i, 14) != null && !tb_data.getValueAt(i, 14).toString().equals("")) {
                if (tb_data.getValueAt(i, 15) == null || tb_data.getValueAt(i, 15).toString().trim().equals("") && !tb_data.getValueAt(i, 8).toString().equals("PAGADO")) {
                    JOptionPane.showMessageDialog(this, "Debe ingresar un N° de Transferencia a los registros por Pagar", "ERROR", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }
        
        
        guardarInformacion();
       
        DefaultTableModel modelo = (DefaultTableModel) tb_data.getModel();

        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "No hay datos para exportar.");
            return;
        }
        
        exportarArchivoExcel(modelo);    
        CuentasporPagar.jButton1.doClick();
        this.dispose();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange

    }//GEN-LAST:event_jDateChooser1PropertyChange

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
                ModuloPago_CuentasPorPagar dialog = new ModuloPago_CuentasPorPagar(new javax.swing.JFrame(), true);
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
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tb_data;
    // End of variables declaration//GEN-END:variables
}
