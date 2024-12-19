/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista;

import Controlador.Conexion;
import com.formdev.flatlaf.FlatIntelliJLaf;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
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
public class ProcesarCuentasporPagar extends javax.swing.JDialog {

    private final Modelo modelo = new Modelo();

    private Color originalAlternateRowColor;

    /**
     * Creates new form Detallado
     */
    public ProcesarCuentasporPagar(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        
        modelo.addColumn("N°");
        modelo.addColumn("PROVEEDOR");
        modelo.addColumn("FACT");
        modelo.addColumn("FEC/E");
        modelo.addColumn("FEC/V");
        modelo.addColumn("MONTO");
        modelo.addColumn("DET [3%]");
        modelo.addColumn("RET [1.5%]");
        modelo.addColumn("RET [4%]");
        modelo.addColumn("TOTAL");
        modelo.addColumn("RUBRO");
        modelo.addColumn("PORCENTAJE");
        
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
        
       modelo.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();

                // Detectar si se modificó la columna 12
                if (column == 11) { // Índice 11 porque las columnas empiezan en 0
                    String value = (String) modelo.getValueAt(row, column);

                    // Validar que el valor no sea nulo y tenga formato de porcentaje (por ejemplo, "3%")
                    if (value != null && value.matches("\\d+(\\.\\d+)?%")) {
                        // Extraer el porcentaje como número
                        double percentage = Double.parseDouble(value.replace("%", "")) / 100.0;

                        // Buscar la columna cuyo encabezado contiene el porcentaje dentro de "[]"
                        int targetColumn = -1;
                        for (int col = 0; col < modelo.getColumnCount(); col++) {
                            String header = modelo.getColumnName(col);
                            if (header.contains("[" + value + "]")) {
                                targetColumn = col;
                                break;
                            }
                        }

                        // Si se encuentra la columna objetivo, realizar el cálculo
                        if (targetColumn != -1) {
                            try {
                                // Obtener el valor de la columna 6
                                double col6Value = Double.parseDouble(modelo.getValueAt(row, 5).toString());

                                // Calcular el porcentaje del valor de la columna 6
                                double result = col6Value * percentage;

                                // Actualizar la columna 10 con el resultado
                                modelo.setValueAt(result, row, targetColumn);
                                
                                modelo.setValueAt(col6Value - result, row, 9);
                            } catch (NumberFormatException ex) {
                                System.err.println("Error en formato numérico: " + ex.getMessage());
                            }
                        }
                    }
                }
            }
        });

    }

    private void guardarInformacion() {
        try {
            Connection con = Conexion.getConnection();
            String sql = "INSERT INTO [dbo].[CuentasPorPagarDiario] " +
             "([N°], [Proveedor], [Factura], [FechaEmision], [FechaVencimiento], [Monto], " +
             "[ValorCalculado], [Total], [Rubro], [Porcentaje], [FechaEvaluacion]) " +
             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE())";
            PreparedStatement pstm = con.prepareStatement(sql);
            for (int i = 0; i < tb_data.getRowCount(); i++) {
                if (tb_data.getValueAt(i, 11) != null) {
                    pstm.setInt(1, (int) tb_data.getValueAt(i, 0));
                    pstm.setString(2, tb_data.getValueAt(i, 1).toString());
                    pstm.setString(3, tb_data.getValueAt(i, 2).toString());
                    pstm.setString(4, tb_data.getValueAt(i, 3).toString());
                    pstm.setString(5, tb_data.getValueAt(i, 4).toString());
                    pstm.setDouble(6, Double.parseDouble(tb_data.getValueAt(i, 5).toString()));
                    pstm.setDouble(7, (parseDoubleOrDefault(tb_data.getValueAt(i, 6))
                            + parseDoubleOrDefault(tb_data.getValueAt(i, 7))
                            + parseDoubleOrDefault(tb_data.getValueAt(i, 8))));
                    pstm.setDouble(8, parseDoubleOrDefault(tb_data.getValueAt(i, 9)));
                    pstm.setString(9, getStringOrDefault(tb_data.getValueAt(i, 10), ""));
                    pstm.setString(10, tb_data.getValueAt(i, 11).toString());
                    pstm.executeUpdate();
                }
            }
            JOptionPane.showMessageDialog(this, "Se han guardado los datos correctamente");
            
        } catch (SQLException ex) {
            Logger.getLogger(ProcesarCuentasporPagar.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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
            return true;
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
    
    public void cargarInfo(Object[] rowData){
        modelo.addRow(rowData);
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
        jButton1.setText("Guardar y Exportar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(0, 102, 153));

        jLabel13.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("CUENTAS POR PAGAR AL 18/09/2024");

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1250, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 658, Short.MAX_VALUE)
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
       guardarInformacion();
       
        DefaultTableModel modelo = (DefaultTableModel) tb_data.getModel();

        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "No hay datos para exportar.");
            return;
        }
        
        exportarArchivoExcel(modelo);      
    }//GEN-LAST:event_jButton1ActionPerformed

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
                ProcesarCuentasporPagar dialog = new ProcesarCuentasporPagar(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel jLabel13;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tb_data;
    // End of variables declaration//GEN-END:variables
}
