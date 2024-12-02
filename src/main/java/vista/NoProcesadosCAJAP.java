/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista;

import Controlador.Conexion;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Farmacia-Ingeniero
 */
public class NoProcesadosCAJAP extends javax.swing.JDialog {

    private final Modelo modelo = new Modelo();
    int c = 1;

    private void llenar_tabla() {
        modelo.addColumn("N°");
        modelo.addColumn("DNI");
        modelo.addColumn("Descuento");
        modelo.addColumn("Observacion");
        modelo.addColumn("Eliminar");

        JTableHeader header = tb_resultado.getTableHeader();
        header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 35));

        header.setBackground(new java.awt.Color(255, 217, 102));
        tb_resultado.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

        TableColumnModel columnModel = tb_resultado.getColumnModel();
        for (int col = 0; col < tb_resultado.getColumnCount(); col++) {
            TableColumn tableColumn = columnModel.getColumn(col);
            Component comp = header.getDefaultRenderer().getTableCellRendererComponent(tb_resultado, tableColumn.getHeaderValue(), false, false, 0, col);
            int width = comp.getPreferredSize().width + 25;
            tableColumn.setPreferredWidth(width);
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tb_resultado.getColumnCount(); i++) {
            tb_resultado.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private void ejecutarNoProcesados() {
        try {
            Connection con = Conexion.getConnection();
            String sql = "UPDATE HistorialCajaPensiones SET Estado = ? WHERE Año = ? AND Mes = ? AND Documento = ? ";
            PreparedStatement pstm = con.prepareStatement(sql);
            for (int i = 0; i < modelo.getRowCount(); i++) {
                pstm.setInt(1, Integer.parseInt(modelo.getValueAt(i, 3).toString().substring(0, 1)));
                pstm.setInt(2, CargaCopere.jdc_año.getYear());
                pstm.setInt(3, (CargaCopere.jdc_mes.getMonth() + 1));
                pstm.setString(4, modelo.getValueAt(i, 1).toString());
                pstm.executeUpdate();
            }

            sql = "UPDATE EstructuraCajaPensiones SET Estado = 0, FechaBaja = GETDATE() WHERE Codigo_CIP = ?";
            pstm = con.prepareStatement(sql);
            for (int i = 0; i < modelo.getRowCount(); i++) {
                if ("SI".equals(modelo.getValueAt(i, 4).toString())) {
                    pstm.setString(1, modelo.getValueAt(i, 1).toString());
                    pstm.executeUpdate();
                }
            }

            JOptionPane.showMessageDialog(null, "Se han actualizado los registros no procesados", "Aviso", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            Logger.getLogger(NoProcesadosCAJAP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public class Modelo extends DefaultTableModel {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    /**
     * Creates new form ModuloNoProcesado
     */
    public NoProcesadosCAJAP(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        int monthIndex = CargaCopere.jdc_mes.getMonth();
        String monthName = getMonthName(monthIndex);
        txtMes.setText(monthName);
        txtAño.setText(String.valueOf(CargaCopere.jdc_año.getYear()));

        llenar_tabla();
    }

    private static String getMonthName(int monthIndex) {
        String[] monthNames = {
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };
        return monthNames[monthIndex];
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
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_resultado = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtNroAdmin = new javax.swing.JTextField();
        btnIngresar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        cbObservacion = new javax.swing.JComboBox<>();
        btnProcesar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtAño = new javax.swing.JLabel();
        txtMes = new javax.swing.JLabel();
        txtTotalIndebidos = new javax.swing.JLabel();
        txtTotalRegistros = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(93, 173, 226));

        jLabel5.setBackground(new java.awt.Color(255, 0, 0));
        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("MODULO DE NO PROCESADOS - C. DE PENSIONES");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                .addContainerGap())
        );

        tb_resultado.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        tb_resultado.setModel(modelo);
        tb_resultado.setRowHeight(30);
        tb_resultado.setShowHorizontalLines(true);
        tb_resultado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tb_resultadoKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tb_resultado);

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel1.setText("Nro. DNI:");

        txtNroAdmin.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        txtNroAdmin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNroAdminKeyPressed(evt);
            }
        });

        btnIngresar.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        btnIngresar.setText("Ingresar");
        btnIngresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIngresarActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel2.setText("Observacion");

        cbObservacion.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        cbObservacion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<<SELECCIONE>>", "2 - FALTA DE LIQUIDEZ", "3 - NO RECIBE SUELDO" }));

        btnProcesar.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        btnProcesar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/flecha-hacia-abajo.png"))); // NOI18N
        btnProcesar.setText("Procesar");
        btnProcesar.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnProcesar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcesarActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel3.setText("Año:");

        jLabel4.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel4.setText("Mes:");

        txtAño.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        txtAño.setForeground(new java.awt.Color(255, 0, 0));
        txtAño.setText("-");

        txtMes.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        txtMes.setForeground(new java.awt.Color(255, 0, 0));
        txtMes.setText("-");

        txtTotalIndebidos.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        txtTotalIndebidos.setForeground(new java.awt.Color(255, 0, 0));
        txtTotalIndebidos.setText("TOTAL INDEBIDOS DE LA ENTIDAD S/ : 0.00");

        txtTotalRegistros.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        txtTotalRegistros.setForeground(new java.awt.Color(255, 0, 0));
        txtTotalRegistros.setText("TOTAL DE REGISTROS: 0");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(btnProcesar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtAño, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNroAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbObservacion, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnIngresar))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtMes, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(txtTotalIndebidos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTotalRegistros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(txtAño)
                    .addComponent(txtMes))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNroAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnIngresar)
                    .addComponent(jLabel2)
                    .addComponent(cbObservacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTotalIndebidos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTotalRegistros)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnProcesar, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnIngresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIngresarActionPerformed
        if (cbObservacion.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una observacion para continuar", "Error", JOptionPane.ERROR_MESSAGE);
            cbObservacion.showPopup();
            return;
        }

        try {
            int repeticiones = 0;

            Calendar calendario = Calendar.getInstance();
            calendario.set(CargaCopere.jdc_año.getYear(), CargaCopere.jdc_mes.getMonth(), 1);
            String ultimoAño, ultimoMes, penultimoAño, penultimoMes;
            calendario.add(Calendar.MONTH, -1);

            ultimoAño = String.valueOf(calendario.get(Calendar.YEAR));
            ultimoMes = String.valueOf((calendario.get(Calendar.MONTH) + 1));

            calendario.add(Calendar.MONTH, -1);
            penultimoAño = String.valueOf(calendario.get(Calendar.YEAR));
            penultimoMes = String.valueOf(calendario.get(Calendar.MONTH) + 1);

            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "SELECT Estado FROM HistorialCajaPensiones WHERE Documento = '" + txtNroAdmin.getText() + "' AND Año IN (" + ultimoAño + ") AND Mes IN (" + ultimoMes + ") AND Estado <> 1";
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                repeticiones++;
            }

            sql = "SELECT Estado FROM HistorialCajaPensiones WHERE Documento = '" + txtNroAdmin.getText() + "' AND Año IN (" + penultimoAño + ") AND Mes IN (" + penultimoMes + ") AND Estado <> 1";
            rs = st.executeQuery(sql);
            if (rs.next()) {
                repeticiones++;
            }
            Object[] data = new Object[5];

            sql = "SELECT Monto FROM HistorialCajaPensiones WHERE Documento = '" + txtNroAdmin.getText() + "' AND Año IN (" + CargaCopere.jdc_año.getYear() + ") AND Mes IN (" + (CargaCopere.jdc_mes.getMonth() + 1) + ")";
            rs = st.executeQuery(sql);
            if (rs.next()) {
                data[0] = c;
                data[1] = txtNroAdmin.getText();
                data[2] = rs.getDouble(1);
                data[3] = cbObservacion.getSelectedItem();
                data[4] = "NO";
                if (repeticiones == 2) {
                    if (JOptionPane.showConfirmDialog(null, "El Nro. Admin. registrado, ya cuenta con los 2 meses anteriores sin aportaciones, desea eliminarlo para el siguiente reporte mensualizado?", "Confirmación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        data[4] = "SI";
                    }
                }
                modelo.addRow(data);
                c++;
                txtNroAdmin.setText("");
                txtNroAdmin.requestFocus();
                actualizarIndebidosyRegistros();
                ajustarColumnas(tb_resultado);
            } else {
                JOptionPane.showMessageDialog(null, "Verifique el Nro. Admin, no existe o se encuentra inhabilitado", "Error", JOptionPane.ERROR_MESSAGE);
                txtNroAdmin.requestFocus();
                txtNroAdmin.selectAll();
            }

        } catch (SQLException ex) {
            Logger.getLogger(NoProcesadosCAJAP.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnIngresarActionPerformed

    private void tb_resultadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tb_resultadoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            if (JOptionPane.showConfirmDialog(null, "¿Estas seguro que desea eliminar el registro seleccionado?", "Advertencia", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                int filaSeleccionada = tb_resultado.getSelectedRow();
                if (filaSeleccionada != -1) {
                    modelo.removeRow(filaSeleccionada);
                    actualizarNumeracion();
                }
            }
        }
    }//GEN-LAST:event_tb_resultadoKeyPressed

    private void txtNroAdminKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNroAdminKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnIngresar.doClick();
        }
    }//GEN-LAST:event_txtNroAdminKeyPressed

    private void btnProcesarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcesarActionPerformed
        if (JOptionPane.showConfirmDialog(null, "¿Estas seguro que desea GUARDAR los registros NO PROCESADOS", "Advertencia", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            ejecutarNoProcesados();
            
            CargaCopere.jButton1.doClick();
            this.dispose();
        }


    }//GEN-LAST:event_btnProcesarActionPerformed

    private void actualizarNumeracion() {
        c = modelo.getRowCount();
        for (int i = 0; i < c; i++) {
            modelo.setValueAt(i + 1, i, 0);
        }
        c++;
    }

    private void actualizarIndebidosyRegistros() {
        double totalIndebidos = 0.0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            totalIndebidos = totalIndebidos + Double.parseDouble(modelo.getValueAt(i, 2).toString());
        }

        txtTotalIndebidos.setText("TOTAL INDEBIDOS DE LA ENTIDAD S/ : " + String.format("%,.2f", totalIndebidos));
        txtTotalRegistros.setText("TOTAL DE REGISTROS: " + String.valueOf(modelo.getRowCount()));

    }

    private static void ajustarColumnas(JTable table) {
        for (int col = 0; col < table.getColumnCount(); col++) {
            TableColumn column = table.getColumnModel().getColumn(col);

            // Ajuste basado en el encabezado
            int width = table.getTableHeader().getDefaultRenderer()
                    .getTableCellRendererComponent(table, column.getHeaderValue(), false, false, 0, col)
                    .getPreferredSize().width;

            // Ajuste basado en el contenido de las celdas
            for (int row = 0; row < table.getRowCount(); row++) {
                Component comp = table.getCellRenderer(row, col)
                        .getTableCellRendererComponent(table, table.getValueAt(row, col), false, false, row, col);
                width = Math.max(width, comp.getPreferredSize().width);
            }

            // Añadir un pequeño margen
            width += 10;

            // Configurar el ancho de la columna
            column.setPreferredWidth(width);
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
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NoProcesadosCAJAP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NoProcesadosCAJAP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NoProcesadosCAJAP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NoProcesadosCAJAP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
                NoProcesadosCAJAP dialog = new NoProcesadosCAJAP(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnIngresar;
    private javax.swing.JButton btnProcesar;
    private javax.swing.JComboBox<String> cbObservacion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tb_resultado;
    private javax.swing.JLabel txtAño;
    private javax.swing.JLabel txtMes;
    private javax.swing.JTextField txtNroAdmin;
    private javax.swing.JLabel txtTotalIndebidos;
    private javax.swing.JLabel txtTotalRegistros;
    // End of variables declaration//GEN-END:variables
}
