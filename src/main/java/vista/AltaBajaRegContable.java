/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista;

import Controlador.Conexion;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Farmacia-Ingeniero
 */
public class AltaBajaRegContable extends javax.swing.JDialog {

    /**
     * Creates new form AltaBajaRegContrable
     */
    public AltaBajaRegContable(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        dcfechabaja.setDate(new Date());
        
        jLabel6.setVisible(false);
        dcfechabaja.setVisible(false);
        
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
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cbregcontable = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        txtdocumento = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtnomape = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cbopcion = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        dcfechabaja = new com.toedter.calendar.JDateChooser("dd/MM/yyyy","##/##/####", '_');
        txtmonto = new javax.swing.JTextField();
        btnguardar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(0, 102, 102));

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("ALTA / BAJA - REGISTRO CONTABLE");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel2.setText("Reg. Contable:");

        cbregcontable.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        cbregcontable.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Copere", "Caja de Pensiones", "Oprefa" }));
        cbregcontable.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbregcontableItemStateChanged(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel3.setText("N° CIP:");

        txtdocumento.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        txtdocumento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtdocumentoKeyPressed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel4.setText("Nombres Apellidos:");

        txtnomape.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        txtnomape.setForeground(new java.awt.Color(255, 0, 0));
        txtnomape.setDisabledTextColor(new java.awt.Color(255, 0, 0));
        txtnomape.setEnabled(false);

        jLabel5.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel5.setText("Opción:");

        cbopcion.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        cbopcion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Alta", "Baja", "Aporte" }));
        cbopcion.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbopcionItemStateChanged(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel6.setText("Fecha Baja:");

        jLabel7.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel7.setText("Monto Pagado:");

        dcfechabaja.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N

        txtmonto.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtdocumento)
                            .addComponent(cbregcontable, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtnomape)
                            .addComponent(cbopcion, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dcfechabaja, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtmonto, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cbregcontable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtdocumento)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtnomape)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbopcion)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dcfechabaja, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtmonto)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnguardar.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        btnguardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Save.png"))); // NOI18N
        btnguardar.setText("Guardar");
        btnguardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnguardarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnguardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnguardar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbregcontableItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbregcontableItemStateChanged
        if (cbregcontable.getSelectedItem().toString().equals("Copere")) {
            jLabel3.setText("N° CIP:");
        }else{
            jLabel3.setText("N° DNI:");
        }
    }//GEN-LAST:event_cbregcontableItemStateChanged

    private void txtdocumentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtdocumentoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cargarNomape();
        }
  
    }//GEN-LAST:event_txtdocumentoKeyPressed

    private void btnguardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnguardarActionPerformed
        if (txtnomape.getText().equals("") || txtnomape.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el Nro de Documento y luego presione ENTER", "Error", JOptionPane.ERROR_MESSAGE);
            txtdocumento.requestFocus();
            txtdocumento.selectAll();
            return;
        }
        
        
        if (cbopcion.getSelectedItem().toString().equals("Baja")) {
            String fechabaja = new SimpleDateFormat("dd/MM/yyyy").format(dcfechabaja.getDate());
            bajaPersonal(fechabaja);
        } else {
            Double monto = Double.valueOf(txtmonto.getText());
            String estado = "";
            if (cbopcion.getSelectedItem().toString().equals("Alta")) {
                estado = "4";
            } 
            altaPersonal(monto, estado);
        }
    }//GEN-LAST:event_btnguardarActionPerformed

    private void cbopcionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbopcionItemStateChanged
        if (cbopcion.getSelectedItem().toString().equals("Alta")|| cbopcion.getSelectedItem().toString().equals("Aporte")) {
            jLabel7.setVisible(true);
            txtmonto.setVisible(true);
            
            jLabel6.setVisible(false);
            dcfechabaja.setVisible(false);
            
        }else{
            jLabel7.setVisible(false);
            txtmonto.setVisible(false);
            
            jLabel6.setVisible(true);
            dcfechabaja.setVisible(true);
        
        }
    }//GEN-LAST:event_cbopcionItemStateChanged

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
            java.util.logging.Logger.getLogger(AltaBajaRegContable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AltaBajaRegContable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AltaBajaRegContable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AltaBajaRegContable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AltaBajaRegContable dialog = new AltaBajaRegContable(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnguardar;
    private javax.swing.JComboBox<String> cbopcion;
    private javax.swing.JComboBox<String> cbregcontable;
    private com.toedter.calendar.JDateChooser dcfechabaja;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField txtdocumento;
    private javax.swing.JTextField txtmonto;
    private javax.swing.JTextField txtnomape;
    // End of variables declaration//GEN-END:variables

    private void cargarNomape() {
        try {
            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "";
            if (cbregcontable.getSelectedItem().toString().equals("Copere")) {
                 sql = "SELECT CONCAT(Nombres, ' ', A_Paterno, ' ', A_Materno) FROM Personal WHERE NroCip = '" + txtdocumento.getText() + "'";
            } else {
                 sql = "SELECT CONCAT(Nombres, ' ', A_Paterno, ' ', A_Materno) FROM Personal WHERE Dni = '" + txtdocumento.getText() + "'";
            }
            
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                txtnomape.setText(rs.getString(1));
            } else {
                txtnomape.setText("");
                JOptionPane.showMessageDialog(this, "El N° de Documento No Existe", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            rs.close();
            st.close();
            con.close();
                    
        } catch (SQLException ex) {
            Logger.getLogger(AltaBajaRegContable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void bajaPersonal(String fechabaja) {
        try {
            Connection con = Conexion.getConnection();
            String sql = "";
            switch (cbregcontable.getSelectedItem().toString()) {
                case "Copere" -> {
                    sql = "UPDATE EstructuraCopere SET FechaBaja = ?, Estado = 0 WHERE Numero_CIP = ?";
                }
                case "Caja de Pensiones" -> {
                    sql = "UPDATE EstructuraCajaPensiones SET FechaBaja = ?, Estado = 0 WHERE Documento = ?";
                }
                case "Oprefa" -> {
                    sql = "UPDATE EstructuraOprefa SET FechaBaja = ?, Estado = 0 WHERE DNI = ?";
                }
            }
            
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, fechabaja);
            pstm.setString(2, txtdocumento.getText());
            pstm.executeUpdate();
            
            pstm.close();
            con.close();
            
            JOptionPane.showMessageDialog(this, "Se ha dado de Baja al Personal Correctamente", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            CargaCopere.jButton1.doClick();
            this.dispose();
            
        } catch (SQLException ex) {
            Logger.getLogger(AltaBajaRegContable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 // 4 SIGNIFICA ALTA Y 5 SIGNIFICA APORTRE
    private void altaPersonal(Double monto, String estado) {
            try {
                Connection con = Conexion.getConnection();
                String sql = "", read = "", update = "", insert = "";
                switch (cbregcontable.getSelectedItem().toString()) {
                    case "Copere" -> {
                        sql = "UPDATE EstructuraCopere SET Estado = 1 WHERE Numero_CIP = ?";
                        
                        read = "SELECT 1 FROM [dbo].[HistorialCopere] WHERE [Numero_CIP] = ? AND [Año] = ? AND [Mes] = ?";
                        update = "UPDATE [dbo].[HistorialCopere] SET [Monto] = ?, [Estado] = " + estado + " WHERE [Numero_CIP] = ? AND [Año] = ? AND [Mes] = ?";
                        insert = "INSERT INTO [dbo].[HistorialCopere] ([FechaRegistro], [Año], [Mes], [Numero_CIP], [Monto], [Estado]) VALUES (GETDATE(), ?, ?, ?, ?, 4)";
                    }
                    case "Caja de Pensiones" -> {
                        sql = "UPDATE EstructuraCajaPensiones SET Estado = 1 WHERE Documento = ?";
                        
                        read = "SELECT 1 FROM [dbo].[HistorialCajaPensiones] WHERE [Documento] = ? AND [Año] = ? AND [Mes] = ?";
                        update = "UPDATE [dbo].[HistorialCajaPensiones] SET [Monto] = ?, [Estado] = " + estado + " WHERE [Documento] = ? AND [Año] = ? AND [Mes] = ?";
                        insert = "INSERT INTO [dbo].[HistorialCajaPensiones] ([FechaRegistro], [Año], [Mes], [Documento], [Monto], [Estado]) VALUES (GETDATE(), ?, ?, ?, ?, 4)";
                    }
                    case "Oprefa" -> {
                        sql = "UPDATE EstructuraOprefa SET Estado = 1 WHERE DNI = ?";
                        
                        read = "SELECT 1 FROM [dbo].[HistorialOprefa] WHERE [Documento] = ? AND [Año] = ? AND [Mes] = ?";
                        update = "UPDATE [dbo].[HistorialOprefa] SET [Monto] = ?, [Estado] = " + estado + " WHERE [Documento] = ? AND [Año] = ? AND [Mes] = ?";
                        insert = "INSERT INTO [dbo].[HistorialOprefa] ([FechaRegistro], [Año], [Mes], [Documento], [Monto], [Estado]) VALUES (GETDATE(), ?, ?, ?, ?, 4)";
                    }
                }
                
                String documento = txtdocumento.getText();
                
                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setString(1, documento);
                pstm.executeUpdate();
                
                LocalDate fechaActual = LocalDate.now();
                int año = fechaActual.getYear();
                int mes = fechaActual.getMonthValue();
                
                try (Connection conexion = Conexion.getConnection()) {
                    String consultaExistencia = read;
                    boolean existeRegistro;
                    
                    try (PreparedStatement psExistencia = conexion.prepareStatement(consultaExistencia)) {
                        psExistencia.setString(1, documento);
                        psExistencia.setInt(2, año);
                        psExistencia.setInt(3, mes);
                        
                        try (ResultSet rs = psExistencia.executeQuery()) {
                            existeRegistro = rs.next();
                        }
                    }
                    
                    if (existeRegistro) {
                        // Actualizar registro existente
                        String actualizarSQL = update;
                        try (PreparedStatement psActualizar = conexion.prepareStatement(actualizarSQL)) {
                            psActualizar.setDouble(1, monto);
                            psActualizar.setString(2, documento);
                            psActualizar.setInt(3, año);
                            psActualizar.setInt(4, mes);
                            
                            psActualizar.executeUpdate();
                            JOptionPane.showMessageDialog(this, "Registro actualizado correctamente.");
                        }
                    } else {
                        // Insertar nuevo registro
                        String insertarSQL = insert;
                        try (PreparedStatement psInsertar = conexion.prepareStatement(insertarSQL)) {
                            psInsertar.setInt(1, año);
                            psInsertar.setInt(2, mes);
                            psInsertar.setString(3, documento);
                            psInsertar.setDouble(4, monto);
                            
                            psInsertar.executeUpdate();
                            JOptionPane.showMessageDialog(this, "Nuevo registro insertado correctamente.");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Error al ejecutar la operación: " + e.getMessage());
                }
            } catch (SQLException ex) {
                Logger.getLogger(AltaBajaRegContable.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            CargaCopere.jButton1.doClick();
            this.dispose();
        }
}
