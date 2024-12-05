/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista;

import Controlador.Conexion;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Farmacia-Ingeniero
 */
public class AgregarPrecioMercaderia extends javax.swing.JDialog {

    private final Modelo modelo = new Modelo();

    private void cargarDescripcion() {
        try {
            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "SELECT TOP 1 Producto FROM StockAlmacen WHERE Codigo = '" + jTextField1.getText().trim() + "'";
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                jTextField2.setText(rs.getString(1));
                jTextField3.requestFocus();
            } else {
                jTextField2.setText("");
                JOptionPane.showMessageDialog(this, "Verifique que el codigo ingresado", "Error", JOptionPane.ERROR_MESSAGE);
                jTextField1.requestFocus();
                jTextField1.selectAll();
            }
            rs.close();
            st.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(AgregarPrecioMercaderia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void guardarPrecio() {
        try {
            Connection con = Conexion.getConnection();
            String sql = "INSERT INTO PrecioTempMercaderia (Codigo, Descripcion, Precio) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, jTextField1.getText());
            ps.setString(2, jTextField2.getText());
            ps.setDouble(3, Double.parseDouble(jTextField3.getText()));
            ps.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Se ha guardado el Precio Correctamente");
            limpiarCampos();
            cargarDatos();
            
            ps.close();
            con.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            //Logger.getLogger(AgregarPrecioMercaderia.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private void limpiarCampos(){
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
    
        jTextField1.requestFocus();
    }
    
    public class Modelo extends DefaultTableModel {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }
    
    /**
     * Creates new form VerificacionCargaMercaderia
     */
    public AgregarPrecioMercaderia(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        UIManager.getLookAndFeelDefaults().put("Table.alternateRowColor", new Color(254, 238, 184));
        Locale.setDefault(new Locale("es", "ES"));
        this.setIconImage(new ImageIcon(System.getProperty("user.dir") + "/logoACMP.png").getImage());
        this.setLocationRelativeTo(null);
       llenar_tabla();
    }
    
    private void llenar_tabla() {
        JTableHeader header = tb_resultado.getTableHeader();
        header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 50));
        header.setBackground(new java.awt.Color(255, 217, 102));
        tb_resultado.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

        modelo.addColumn("CODIGO");
        modelo.addColumn("DESCRIPCION");
        modelo.addColumn("PRECIO");
        
        cargarDatos();
        
    }
    
    private void cargarDatos() {
        modelo.setRowCount(0);
        
        Object[] data = new Object[3];
        String sql = "SELECT * FROM PrecioTempMercaderia ORDER BY Descripcion";

        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data[0] = rs.getObject(1);
                    data[1] = rs.getObject(2);
                    data[2] = rs.getObject(3);
                    modelo.addRow(data);
                }
            }

            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(AgregarPrecioMercaderia.class.getName()).log(Level.SEVERE, null, ex);
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_resultado = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Agregar Precio Temporal");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel1.setText("Codigo:");

        jTextField1.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField1KeyPressed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel2.setText("Descripcion");

        jButton1.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_add_32px.png"))); // NOI18N
        jButton1.setText("Agregar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField2.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        jTextField2.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jTextField2.setEnabled(false);

        jLabel3.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel3.setText("Precio sin IGV:");

        jTextField3.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(48, 48, 48)
                        .addComponent(jTextField1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField3)
                            .addComponent(jTextField2))))
                .addGap(9, 9, 9)
                .addComponent(jButton1)
                .addGap(9, 9, 9))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Lista de Precios Temporal", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 12), new java.awt.Color(255, 0, 0))); // NOI18N

        tb_resultado.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        tb_resultado.setModel(modelo);
        tb_resultado.setToolTipText("");
        tb_resultado.setRowHeight(30);
        tb_resultado.setShowGrid(true);
        tb_resultado.setShowVerticalLines(false);
        tb_resultado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tb_resultadoKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tb_resultado);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    private void jTextField1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cargarDescripcion();
        }
    }//GEN-LAST:event_jTextField1KeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (jTextField2.getText().equals("") || jTextField2.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un Codigo y presione ENTER", "Error", JOptionPane.ERROR_MESSAGE);
            jTextField1.requestFocus();
            return;
        }
        
        if (jTextField3.getText().equals("") || jTextField3.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un precio", "Error", JOptionPane.ERROR_MESSAGE);
            jTextField3.requestFocus();
            return;
        }
        
        guardarPrecio();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void tb_resultadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tb_resultadoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {  
            int row = tb_resultado.getSelectedRow();  
            if (row >= 0) {  
                Object id = tb_resultado.getValueAt(row, 0);  

                // Mostrar el cuadro de confirmación
                int confirm = JOptionPane.showConfirmDialog(this,
                        "¿Estás seguro de que deseas eliminar este registro?",
                        "Confirmación de eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    try (Connection conn = Conexion.getConnection()) {
                        String sql = "DELETE FROM PrecioTempMercaderia WHERE Codigo = ?"; 
                        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                            stmt.setObject(1, id); 
                            int rowsAffected = stmt.executeUpdate();
                            if (rowsAffected > 0) {
                                JOptionPane.showMessageDialog(this, "Registro eliminado correctamente.");
                                ((DefaultTableModel) tb_resultado.getModel()).removeRow(row);
                            } else {
                                JOptionPane.showMessageDialog(this, "No se encontró el registro.");
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error al eliminar el registro.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, selecciona un registro para eliminar.");
            }
        }
    }//GEN-LAST:event_tb_resultadoKeyPressed

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
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AgregarPrecioMercaderia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AgregarPrecioMercaderia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AgregarPrecioMercaderia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AgregarPrecioMercaderia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AgregarPrecioMercaderia dialog = new AgregarPrecioMercaderia(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTable tb_resultado;
    // End of variables declaration//GEN-END:variables
}
