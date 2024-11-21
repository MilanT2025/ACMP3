/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista;

import Controlador.Conexion;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

/**
 *
 * @author Farmacia-Ingeniero
 */
public class EstructuraCEDepreciacion extends javax.swing.JDialog {

    private final Modelo modelo = new Modelo();

    /**
     * Creates new form PosicionCod
     */
    public EstructuraCEDepreciacion(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        llenar_tabla();
    }

    public class Modelo extends DefaultTableModel {

        @Override
        public boolean isCellEditable(int row, int column) {
            return true;
        }
    }

    private void llenar_tabla() {
        String[] columnNames = new String[]{
            "CuentaContable",
            "Descripción",
            "CuentaDepreciación"
        };

        modelo.setColumnIdentifiers(columnNames);

        JTableHeader header = tb_resultado.getTableHeader();
        header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 40)); // Establece la altura deseada (por ejemplo, 30 píxeles)

        header.setBackground(new java.awt.Color(0, 102, 153));
        header.setForeground(new java.awt.Color(255, 255, 255));
        tb_resultado.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Aplicar el renderizador a todas las columnas
        for (int i = 0; i < tb_resultado.getColumnCount(); i++) {
            tb_resultado.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void MostrarCEquivalentes() {
    }

    private void GuardarCEquivalentes() {
        try {
            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "INSERT INTO [dbo].[CuentasDepreciacionEquivalentes] "
                    + "           ([CuentaContable], [Descripcion], [CuentaDepreciacion]) "
                    + "     VALUES "
                    + "           (?, ?, ?)";

            PreparedStatement pstm = con.prepareStatement(sql);

            // Recorrer las filas de la tabla
            for (int i = 0; i < modelo.getRowCount(); i++) {
                // Asegúrate de que estás accediendo a los índices correctos
                String cuentaContableStr = modelo.getValueAt(i, 0).toString(); // Cuenta Contable
                String descripcion = modelo.getValueAt(i, 1).toString(); // Descripción
                String cuentaDepreciacionStr = modelo.getValueAt(i, 2).toString(); // Cuenta Depreciación

                // Validar y convertir a int
                if (cuentaContableStr.isEmpty() || cuentaDepreciacionStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Error: Cuenta contable y cuenta de depreciación no pueden estar vacíos en la fila " + (i + 1));
                    continue; // Salta a la siguiente fila
                }

                int cuentaContable = Integer.parseInt(cuentaContableStr);
                int cuentaDepreciacion = Integer.parseInt(cuentaDepreciacionStr);

                // Establecer los valores en el PreparedStatement
                pstm.setInt(1, cuentaContable);
                pstm.setString(2, descripcion);
                pstm.setInt(3, cuentaDepreciacion);

                System.out.println("Cuenta Contable: " + cuentaContable);
                System.out.println("Descripción: " + descripcion);
                System.out.println("Cuenta Depreciación: " + cuentaDepreciacion);

                // Ejecutar la inserción
                pstm.executeUpdate();
            }

            JOptionPane.showMessageDialog(null, "Datos registrados correctamente");
        } catch (SQLException ex) {
            Logger.getLogger(EstructuraCEDepreciacion.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Error al registrar: " + ex.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Asegúrate de que los valores de cuenta contable y cuenta de depreciación sean números válidos.");
        }

    }

//    private void EliminarCEquivalentes() {
//        try {
//            Connection con = Conexion.getConnection();
//            String sql = "DELETE FROM [dbo].[CuentasDepreciacionEquivalentes] "
//                    + "      WHERE CuentaContable = ?";
//
//            PreparedStatement pstm = con.prepareStatement(sql);
//
//            // Recorrer las filas de la tabla
//            for (int i = 0; i < modelo.getRowCount(); i++) {
//                // Obtener la CuentaContable y convertirla a int
//                int cuentaContable = Integer.parseInt(modelo.getValueAt(i, 0).toString());
//
//                // Establecer los valores en el PreparedStatement
//                pstm.setInt(1, cuentaContable);
//                // Ejecutar la inserción
//                pstm.executeUpdate();
//            }
//
//            JOptionPane.showMessageDialog(null, "Fila eliminada correctamente");
//        } catch (SQLException ex) {
//            Logger.getLogger(EstructuraCEDepreciacion.class.getName()).log(Level.SEVERE, null, ex);
//            JOptionPane.showMessageDialog(null, "Error al registrar: " + ex.getMessage());
//        }
//    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_resultado = new javax.swing.JTable();
        BtnGuardarCuentasEqui = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Posiciones Tablas");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        tb_resultado.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        tb_resultado.setModel(modelo);
        tb_resultado.setGridColor(new java.awt.Color(0, 102, 102));
        tb_resultado.setRowHeight(25);
        tb_resultado.setShowHorizontalLines(true);
        tb_resultado.setShowVerticalLines(true);
        jScrollPane1.setViewportView(tb_resultado);

        BtnGuardarCuentasEqui.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        BtnGuardarCuentasEqui.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Map Pinpoint.png"))); // NOI18N
        BtnGuardarCuentasEqui.setText("Guardar Cuentas Contable");
        BtnGuardarCuentasEqui.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGuardarCuentasEquiActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_add_32px.png"))); // NOI18N
        jButton3.setText("Agregar Fila");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Close_2.png"))); // NOI18N
        jButton4.setText("Eliminar Fila");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(0, 102, 153));

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 15)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Estructura de Patrimonio Neto");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(BtnGuardarCuentasEqui, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 147, Short.MAX_VALUE)
                        .addComponent(jButton4)))
                .addContainerGap())
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BtnGuardarCuentasEqui, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
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
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        modelo.addRow(new Object[]{"","",""}); // Inicializa con valores predeterminados
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        int selectedRow = tb_resultado.getSelectedRow();
        if (selectedRow != -1) { // Verificar si hay una fila seleccionada
            // Mostrar un cuadro de diálogo de confirmación
            int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de que quieres eliminar esta fila?",
                    "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // Eliminar la fila seleccionada del modelo de datos
                modelo.removeRow(selectedRow);
            }
        } else {
            // Si no hay fila seleccionada, mostrar un mensaje de advertencia
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una fila para eliminar.",
                    "Fila no Seleccionada", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void BtnGuardarCuentasEquiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGuardarCuentasEquiActionPerformed
        if (tb_resultado.isEditing()) {
            tb_resultado.getCellEditor().stopCellEditing();
        }
        GuardarCEquivalentes();
    }//GEN-LAST:event_BtnGuardarCuentasEquiActionPerformed

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
            java.util.logging.Logger.getLogger(EstructuraCEDepreciacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EstructuraCEDepreciacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EstructuraCEDepreciacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EstructuraCEDepreciacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                EstructuraCEDepreciacion dialog = new EstructuraCEDepreciacion(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton BtnGuardarCuentasEqui;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable tb_resultado;
    // End of variables declaration//GEN-END:variables
}
