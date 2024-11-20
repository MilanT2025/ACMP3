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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
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
public class AgregarActivos extends javax.swing.JDialog {

    private final Modelo modelo = new Modelo();

    private Color originalAlternateRowColor;

    /**
     * Creates new form Detallado
     */
    public AgregarActivos(java.awt.Frame parent, boolean modal) {
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
            if (column == 0) { // Permitir edición para el checkbox
                return true;
            }
            // Permitir edición solo para columnas específicas
            return column == 1 || column == 3 || column == 5 || column == 7 || column == 8 || column == 9 || column == 13 || column == 16;
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) {
                return Boolean.class; // La primera columna será de tipo Boolean (checkbox)
            }
            return super.getColumnClass(columnIndex);
        }
    }

    public void cargarDatos(int año, int mes) {
        modelo.setRowCount(0);
        modelo.setColumnCount(0);
        
        JTableHeader header = tb_data.getTableHeader();
        header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 40));
        header.setBackground(new java.awt.Color(0, 102, 153));
        header.setForeground(new java.awt.Color(255, 255, 255));
        tb_data.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

        try {
            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "SELECT "
                    + "	'' AS Item, "
                    + "	FORMAT(DATEFROMPARTS(año, mes, 1), 'MMM-yy') AS Periodo, "
                    + "	'' AS Ubicacion, "
                    + "	CONCAT([Serie Nº], '-', Numero) AS Comprobante, "
                    + "	FORMAT([F# Emision], 'dd/MM/yyyy') AS [Fecha de Activación], "
                    + "	[Razon Social] AS Marca, "
                    + "	'' AS [Cuenta de Gasto Nueva], "
                    + "	CDE.CuentaDepreciacion AS [Cta Depre Nueva], "
                    + "	[Cta# Cont#] AS [Cta Contab Nueva], "
                    + "	C.Descripcion, "
                    + "	CASE WHEN Moneda = 'S/' THEN 'PEN' ELSE Moneda END AS [Mon.], "
                    + "	Cant# AS Cantidad, "
                    + "	[U/M], "
                    + "	[Precio Sin IGV] AS [Valor U/M], "
                    + "	[Sub Total MN] AS Total, "
                    + "	'' AS [Vida Util Meses] "
                    + "FROM cuenta33 C "
                    + "	LEFT JOIN CuentasDepreciacionEquivalentes CDE ON C.[Cta# Cont#] = CDE.CuentaContable "
                    + "WHERE "
                    + "	Año = " + año + " AND "
                    + "	Mes = " + mes + " ";

            ResultSet rs = st.executeQuery(sql);

            ResultSetMetaData metaData = rs.getMetaData();
            
            int columnCount = metaData.getColumnCount();
            
            modelo.addColumn("Seleccionar");
            
            for (int i = 1; i <= columnCount; i++) {
                modelo.addColumn(metaData.getColumnName(i));
            }

            while (rs.next()) {
                Object[] row = new Object[columnCount+1];
                row[0] = false;
                for (int i = 1; i <= columnCount; i++) {
                    row[i] = rs.getObject(i);
                }
                modelo.addRow(row);
            }
            
            tb_data.getColumnModel().getColumn(0).setCellRenderer(new AlternateRowCheckBoxRenderer());
            
            TableColumn column = tb_data.getColumnModel().getColumn(0);
            column.setHeaderRenderer(new CheckBoxHeaderRenderer(tb_data));

            TableRowFilterSupport.forTable(tb_data).searchable(true).actions(true).useTableRenderers(true).apply();

            TableRowSorter<TableModel> sorter = new TableRowSorter<>(tb_data.getModel());
            tb_data.setRowSorter(sorter);

            packColumns(tb_data);

        } catch (SQLException ex) {
            Logger.getLogger(AgregarActivos.class.getName()).log(Level.SEVERE, null, ex);
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

    private void packColumns(JTable table) {
        TableColumnModel columnModel = table.getColumnModel();
        for (int columnIndex = 0; columnIndex < columnModel.getColumnCount(); columnIndex++) {
            TableColumn column = columnModel.getColumn(columnIndex);

            TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
            Object headerValue = column.getHeaderValue();
            Component headerComponent = headerRenderer.getTableCellRendererComponent(table, headerValue, false, false, -1, columnIndex);
            int headerWidth = headerComponent.getPreferredSize().width;

            int maxCellWidth = headerWidth;
            for (int rowIndex = 0; rowIndex < table.getRowCount(); rowIndex++) {
                TableCellRenderer cellRenderer = table.getCellRenderer(rowIndex, columnIndex);
                Component cellComponent = table.prepareRenderer(cellRenderer, rowIndex, columnIndex);
                int cellWidth = cellComponent.getPreferredSize().width;
                maxCellWidth = Math.max(maxCellWidth, cellWidth);
            }

            column.setMinWidth(maxCellWidth);
        }
    }
    
    
    private void agregarActivos(){
        for (int i = 0; i < tb_data.getRowCount(); i++) {
            if (Boolean.parseBoolean(tb_data.getValueAt(i, 0).toString()) == true) {
                try {
                    if (tb_data.getValueAt(i, 1).toString().trim().equals("")) {
                        JOptionPane.showMessageDialog(null, "La columna [Item], no puede estar vacia", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (!tb_data.getValueAt(i, 1).toString().trim().equals("") && Integer.parseInt(tb_data.getValueAt(i, 1).toString()) <= Integer.parseInt(ultimoItem.getText())) {
                        JOptionPane.showMessageDialog(null, "La columna [Item], debe ser mayor al [Ultimo N° de Item]", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (tb_data.getValueAt(i, 8).toString().trim().equals("")) {
                        JOptionPane.showMessageDialog(null, "La columna [Cta Depre Nueva], no puede estar vacia", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (tb_data.getValueAt(i, 9).toString().trim().equals("")) {
                        JOptionPane.showMessageDialog(null, "La columna [Cta Contab Nueva], no puede estar vacia", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (tb_data.getValueAt(i, 16).toString().trim().equals("")) {
                        JOptionPane.showMessageDialog(null, "La columna [Vida Util Meses], no puede estar vacia", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (tb_data.isEditing()) {
                        tb_data.getCellEditor().stopCellEditing();
                    }
                    
                    Connection cn = Conexion.getConnection();
                    String sql = "INSERT INTO [dbo].[Activos] "
                            + "([Item], [Periodo], [Ubicacion], [Comprobante], [FechaActivacion], [Marca], "
                            + "[CuentaGastoNuevo], [CuentaDepreNuevo], [CuentaContaNuevo], [Descripcion], "
                            + "[Moneda], [Cantidad], [UnidadMedida], [ValorUnidad], [Total], [VidaUtilMeses]) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement pstmt = cn.prepareStatement(sql);
                    
                    pstmt.setInt(1, Integer.parseInt(tb_data.getValueAt(i, 1).toString()));
                    pstmt.setString(2, tb_data.getValueAt(i, 2).toString());
                    pstmt.setString(3, tb_data.getValueAt(i, 3).toString());
                    pstmt.setString(4, tb_data.getValueAt(i, 4).toString());
                    pstmt.setString(5, tb_data.getValueAt(i, 5).toString()); // java.sql.Date
                    pstmt.setString(6, tb_data.getValueAt(i, 6).toString());
                    pstmt.setInt(7, Integer.parseInt(tb_data.getValueAt(i, 7).toString()));
                    pstmt.setInt(8, Integer.parseInt(tb_data.getValueAt(i, 8).toString()));
                    pstmt.setInt(9, Integer.parseInt(tb_data.getValueAt(i, 9).toString()));
                    pstmt.setString(10, tb_data.getValueAt(i, 10).toString());
                    pstmt.setString(11, tb_data.getValueAt(i, 11).toString());
                    pstmt.setInt(12, Integer.parseInt(tb_data.getValueAt(i, 12).toString()));
                    pstmt.setString(13, tb_data.getValueAt(i, 13).toString());
                    pstmt.setDouble(14, Double.parseDouble(tb_data.getValueAt(i, 14).toString())); // Use BigDecimal for money
                    pstmt.setDouble(15, Double.parseDouble(tb_data.getValueAt(i, 15).toString()));      // Use BigDecimal for money
                    pstmt.setInt(16, Integer.parseInt(tb_data.getValueAt(i, 16).toString()));

                    pstmt.executeUpdate();
                    
                } catch (SQLException ex) {
                    Logger.getLogger(AgregarActivos.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
            }
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
        jPanel14 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jdc_año = new com.toedter.calendar.JYearChooser();
        jLabel14 = new javax.swing.JLabel();
        jdc_mes = new com.toedter.calendar.JMonthChooser();
        jButton2 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        ultimoItem = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Agregar Activos");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        tb_data.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        tb_data.setModel(modelo);
        tb_data.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tb_data.setRowHeight(25);
        tb_data.setShowHorizontalLines(true);
        jScrollPane1.setViewportView(tb_data);

        jButton1.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_add_32px.png"))); // NOI18N
        jButton1.setText("Agregar Activos");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Cuentas 33", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 13))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel12.setText("Año:");

        jLabel14.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel14.setText("Mes");

        jdc_mes.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jdc_mes.setForeground(new java.awt.Color(255, 0, 0));
        jdc_mes.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N

        jButton2.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Search_4.png"))); // NOI18N
        jButton2.setText("Cargar Datos");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

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
                .addComponent(jLabel14)
                .addGap(12, 12, 12)
                .addComponent(jdc_mes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 757, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jdc_mes, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                            .addComponent(jdc_año, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        jLabel13.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel13.setText("Ultimo N° de Item:");

        ultimoItem.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        ultimoItem.setForeground(new java.awt.Color(255, 0, 0));
        ultimoItem.setText("-");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ultimoItem)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(ultimoItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addGap(19, 19, 19))
        );

        jMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Upload To FTP.png"))); // NOI18N
        jMenu3.setText("Subir");
        jMenu3.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N

        jMenuItem1.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/sobresalir.png"))); // NOI18N
        jMenuItem1.setText("Archivo Excel");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem1);

        jMenuBar1.add(jMenu3);

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
        agregarActivos();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
         cargarDatos(jdc_año.getYear(), (jdc_mes.getMonth() + 1));
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        CargaArchivos ini = new CargaArchivos(null, true);
        ini.toFront();
        ini.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

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
                AgregarActivos dialog = new AgregarActivos(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton jButton2;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JScrollPane jScrollPane1;
    public static com.toedter.calendar.JYearChooser jdc_año;
    public static com.toedter.calendar.JMonthChooser jdc_mes;
    private javax.swing.JTable tb_data;
    public javax.swing.JLabel ultimoItem;
    // End of variables declaration//GEN-END:variables
}
