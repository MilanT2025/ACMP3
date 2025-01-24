/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista;

import Controlador.Conexion;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 *
 * @author Farmacia-Ingeniero
 */
public class MostrarBonificaciones extends javax.swing.JDialog {
    private final Modelo modelo = new Modelo();

    public MostrarBonificaciones(java.awt.Frame parent, boolean modal, String documento, String trabajador, String inicioCTS, int datos, double division) {
        super(parent, modal);
        initComponents();
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cerrar");

        getRootPane().getActionMap().put("cerrar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();  // Cierra el diálogo
            }
        });
        
        txtDocumento.setText(documento);
        txtTrabajador.setText(trabajador);
        txtInicioCTS.setText(inicioCTS);
        this.setLocationRelativeTo(null);
        llenar_tabla();
        filtrarRegistros(tb_resultado, txtInicioCTS, datos);
        calcular(division);
    }
    
    public class Modelo extends DefaultTableModel {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }
    
    private void CursorCargando(){
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
    }
    
    private void CursorNormal(){
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    
    private void calcular(double division){
        int contador = 0;
        Double total = 0.0;
        for (int i = 0; i < tb_resultado.getRowCount(); i++) {
            if (Double.parseDouble(tb_resultado.getValueAt(i, 3).toString()) > 0) {
                contador++;
                total += Double.parseDouble(tb_resultado.getValueAt(i, 3).toString());
            }
        }
        
        txtSumaTotal.setText(String.format(Locale.US, "%.2f", total));
        txtMesesBonif.setText(String.valueOf(contador));
        
        if (contador >= 3) {
            txtCumple.setText("SI");
            txtPorcentajeBonif.setText(String.format(Locale.US, "%.2f", total / division));
        }else{
            txtCumple.setText("NO");
            txtPorcentajeBonif.setText("0.00");
        }
        
    
    }
    
    private void llenar_tabla() {
        CursorCargando();
        
        modelo.setRowCount(0);
        modelo.setColumnCount(0);

        JTableHeader header = tb_resultado.getTableHeader();
        header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 35));
        header.setBackground(new java.awt.Color(255, 217, 102));
        tb_resultado.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

        modelo.addColumn("Año");
        modelo.addColumn("MesNumero");
        modelo.addColumn("Mes");
        modelo.addColumn("Bonif. / Prod.");
        
        cargarBonificaciones(txtDocumento.getText());
    }
    
    private void cargarBonificaciones(String documento) {
        Object [] data = new Object[modelo.getColumnCount()];
        try {
            Connection con = Conexion.getConnection();
            String sql = "SELECT "
                    + "	Año, "
                    + "	Mes, "
                    + "	DATENAME(MONTH, DATEFROMPARTS(Año, Mes, 1)) AS NombreMes, "
                    + "	[Bonif# Prod#] "
                    + "FROM PlanillaSueldo "
                    + "WHERE "
                    + "	[Nro Doc# Ident#] = ? "
                    + "ORDER BY "
                    + "	Año, "
                    + "	Mes";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1, documento);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                data[0] = rs.getInt(1);
                data[1] = rs.getInt(2);
                data[2] = rs.getString(3);
                data[3] = rs.getDouble(4);
                modelo.addRow(data);
            } 
            rs.close();
            pstm.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(MostrarBonificaciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        CursorNormal();
    }
    
    
    public void filtrarRegistros(JTable table, JTextField fechaTextField, int meses) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            // Obtener la fecha de referencia del JTextField
            Date fechaReferencia = dateFormat.parse(fechaTextField.getText());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fechaReferencia);

            // Obtener el año y mes de la fecha de referencia
            int añoReferencia = calendar.get(Calendar.YEAR);
            int mesReferencia = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH es 0-based

            // Calcular el mes límite superior sumando los meses especificados
            calendar.add(Calendar.MONTH, meses);
            int añoLimite = calendar.get(Calendar.YEAR);
            int mesLimite = calendar.get(Calendar.MONTH) + 1;

            DefaultTableModel model = (DefaultTableModel) table.getModel();

            for (int i = model.getRowCount() - 1; i >= 0; i--) {
                int añoFila = (int) model.getValueAt(i, 0);
                int mesFila = (int) model.getValueAt(i, 1);

                // Comprobar si el registro está fuera del rango permitido
                if (esAnterior(añoFila, mesFila, añoReferencia, mesReferencia) || 
                    esPosterior(añoFila, mesFila, añoLimite, mesLimite)) {
                    model.removeRow(i); // Eliminar la fila fuera del rango
                }
            }

        } catch (ParseException e) {
            JOptionPane.showMessageDialog(null, "Formato de fecha incorrecto. Usa dd/MM/yyyy");
        }
    }

    // Método para verificar si una fecha es anterior a otra
    private static boolean esAnterior(int añoFila, int mesFila, int añoRef, int mesRef) {
        return añoFila < añoRef || (añoFila == añoRef && mesFila < mesRef);
    }

    // Método para verificar si una fecha es posterior a otra
    private static boolean esPosterior(int añoFila, int mesFila, int añoLimite, int mesLimite) {
        return añoFila > añoLimite || (añoFila == añoLimite && mesFila > mesLimite);
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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtDocumento = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtInicioCTS = new javax.swing.JTextField();
        txtTrabajador = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtSumaTotal = new javax.swing.JTextField();
        txtMesesBonif = new javax.swing.JTextField();
        txtPorcentajeBonif = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtCumple = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Calculo de Bonif. / Prod.");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        tb_resultado.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        tb_resultado.setModel(modelo);
        tb_resultado.setRowHeight(30);
        tb_resultado.setShowHorizontalLines(true);
        tb_resultado.setShowVerticalLines(true);
        jScrollPane1.setViewportView(tb_resultado);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel1.setText("Nro Doc. Ident.:");

        txtDocumento.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        txtDocumento.setDisabledTextColor(new java.awt.Color(255, 0, 0));
        txtDocumento.setEnabled(false);

        jLabel2.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel2.setText("Trabajador:");

        jLabel3.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel3.setText("Inicio CTS:");

        txtInicioCTS.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        txtInicioCTS.setDisabledTextColor(new java.awt.Color(255, 0, 0));
        txtInicioCTS.setEnabled(false);
        txtInicioCTS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtInicioCTSActionPerformed(evt);
            }
        });

        txtTrabajador.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        txtTrabajador.setDisabledTextColor(new java.awt.Color(255, 0, 0));
        txtTrabajador.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTrabajador)
                    .addComponent(txtInicioCTS)
                    .addComponent(txtDocumento))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtDocumento)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtTrabajador)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtInicioCTS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 0, 0));
        jLabel6.setText("Calculo Bonif. / Prod.");

        jLabel8.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel8.setText(":");

        jLabel9.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 0, 0));
        jLabel9.setText(":");

        jLabel12.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel12.setText(": ");

        jLabel4.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel4.setText("Suma Bonif. / Prod.");

        jLabel5.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel5.setText("Meses con Bonif. / Prod.");

        txtSumaTotal.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        txtSumaTotal.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtSumaTotal.setEnabled(false);

        txtMesesBonif.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        txtMesesBonif.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtMesesBonif.setEnabled(false);

        txtPorcentajeBonif.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        txtPorcentajeBonif.setDisabledTextColor(new java.awt.Color(255, 0, 0));
        txtPorcentajeBonif.setEnabled(false);

        jLabel13.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel13.setText(": ");

        jLabel14.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel14.setText("Cumple Condicion > 3 M.");

        txtCumple.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        txtCumple.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txtCumple.setEnabled(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(9, 9, 9))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel12)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addComponent(jLabel9))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPorcentajeBonif)
                    .addComponent(txtCumple)
                    .addComponent(txtMesesBonif)
                    .addComponent(txtSumaTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel8)
                    .addComponent(txtSumaTotal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12)
                    .addComponent(txtMesesBonif))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtCumple)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(txtPorcentajeBonif))
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void txtInicioCTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtInicioCTSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtInicioCTSActionPerformed

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
            java.util.logging.Logger.getLogger(MostrarBonificaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MostrarBonificaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MostrarBonificaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MostrarBonificaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MostrarBonificaciones dialog = new MostrarBonificaciones(new javax.swing.JFrame(), true, null, null, null, 0, 0.0);
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tb_resultado;
    private javax.swing.JTextField txtCumple;
    private javax.swing.JTextField txtDocumento;
    private javax.swing.JTextField txtInicioCTS;
    private javax.swing.JTextField txtMesesBonif;
    private javax.swing.JTextField txtPorcentajeBonif;
    private javax.swing.JTextField txtSumaTotal;
    private javax.swing.JTextField txtTrabajador;
    // End of variables declaration//GEN-END:variables
}
