
package vista;

import Controlador.Conexion;
import java.awt.Component;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


public class DetalladoCopere extends javax.swing.JDialog {

    private final Modelo modelo = new Modelo();

    private void llenar_tabla() {
        modelo.addColumn("Año");
        modelo.addColumn("Enero");
        modelo.addColumn("Febrero");
        modelo.addColumn("Marzo");
        modelo.addColumn("Abril");
        modelo.addColumn("Mayo");
        modelo.addColumn("Junio");
        modelo.addColumn("Julio");
        modelo.addColumn("Agosto");
        modelo.addColumn("Septiembre");
        modelo.addColumn("Octubre");
        modelo.addColumn("Noviembre");
        modelo.addColumn("Diciembre");
        modelo.addColumn("Total Aporte");

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
    }

    public class Modelo extends DefaultTableModel {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    /**
     * Creates new form DetalladoCopere
     */
    public DetalladoCopere(java.awt.Frame parent, boolean modal, String numerocip) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        llenar_tabla();
        cargarDatos(numerocip);
    }

    private void cargarDatos(String numerocip) {
        try {
            String[] data = new String[14];
            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "SELECT Año,Numero_CIP, "
                    + "		   SUM(CASE WHEN Mes = 1 THEN Monto ELSE 0 END) AS Enero, "
                    + "		   SUM(CASE WHEN Mes = 2 THEN Monto ELSE 0 END) AS Febrero, "
                    + "            SUM(CASE WHEN Mes = 3 THEN Monto ELSE 0 END) AS Marzo, "
                    + "            SUM(CASE WHEN Mes = 4 THEN Monto ELSE 0 END) AS Abril, "
                    + "            SUM(CASE WHEN Mes = 5 THEN Monto ELSE 0 END) AS Mayo, "
                    + "            SUM(CASE WHEN Mes = 6 THEN Monto ELSE 0 END) AS Junio, "
                    + "            SUM(CASE WHEN Mes = 7 THEN Monto ELSE 0 END) AS Julio, "
                    + "            SUM(CASE WHEN Mes = 8 THEN Monto ELSE 0 END) AS Agosto, "
                    + "            SUM(CASE WHEN Mes = 9 THEN Monto ELSE 0 END) AS Septiembre, "
                    + "            SUM(CASE WHEN Mes = 10 THEN Monto ELSE 0 END) AS Octubre, "
                    + "            SUM(CASE WHEN Mes = 11 THEN Monto ELSE 0 END) AS Noviembre, "
                    + "            SUM(CASE WHEN Mes = 12 THEN Monto ELSE 0 END) AS Diciembre, "
                    + "		   ISNULL ((pe.Nombres+''+pe.A_Paterno+''+pe.A_Materno),'No se encuentra el afiliado en la BD') AS Afiliado, ISNULL(pe.Dni,'-') AS DNI"
                    + "            FROM HistorialCopere hc "
                    + "LEFT JOIN Personal as pe on hc.Numero_CIP = pe.NroCip "
                    + "WHERE Numero_CIP = '" + numerocip + "' AND Estado = 1 "
                    + "GROUP BY Año, Numero_CIP,pe.Nombres,pe.A_Paterno,pe.A_Materno,pe.Dni "
                    + "ORDER BY Año DESC;";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                txtdni.setText(rs.getString(16));
                txtnomape.setText(rs.getString(15));
                txtcip.setText(rs.getString(2));

                data[0] = rs.getString(1);
                data[1] = rs.getString(3);
                data[2] = rs.getString(4);
                data[3] = rs.getString(5);
                data[4] = rs.getString(6);
                data[5] = rs.getString(7);
                data[6] = rs.getString(8);
                data[7] = rs.getString(9);
                data[8] = rs.getString(10);
                data[9] = rs.getString(11);
                data[10] = rs.getString(12);
                data[11] = rs.getString(13);
                data[12] = rs.getString(14);
                data[13]
                        = String.valueOf(
                                rs.getDouble(3)
                                + rs.getDouble(4)
                                + rs.getDouble(5)
                                + rs.getDouble(6)
                                + rs.getDouble(7)
                                + rs.getDouble(8)
                                + rs.getDouble(9)
                                + rs.getDouble(10)
                                + rs.getDouble(11)
                                + rs.getDouble(12)
                                + rs.getDouble(13)
                                + rs.getDouble(14)
                        );

                modelo.addRow(data);

            }
        } catch (SQLException ex) {
            Logger.getLogger(DetalladoCopere.class.getName()).log(Level.SEVERE, null, ex);
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
        jLabel4 = new javax.swing.JLabel();
        txtdni = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtnomape = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtcip = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_resultado = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(0, 102, 102));

        jLabel4.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("DETALLADO APORTANTE");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel4)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        txtdni.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        txtdni.setForeground(new java.awt.Color(255, 0, 0));
        txtdni.setDisabledTextColor(new java.awt.Color(255, 0, 0));
        txtdni.setEnabled(false);

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel1.setText("DNI:");

        txtnomape.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        txtnomape.setForeground(new java.awt.Color(255, 0, 0));
        txtnomape.setDisabledTextColor(new java.awt.Color(255, 0, 0));
        txtnomape.setEnabled(false);

        jLabel2.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel2.setText("Apellidos y Nombres:");

        jLabel3.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel3.setText("CIP:");

        txtcip.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        txtcip.setForeground(new java.awt.Color(255, 0, 0));
        txtcip.setDisabledTextColor(new java.awt.Color(255, 0, 0));
        txtcip.setEnabled(false);

        tb_resultado.setModel(modelo);
        tb_resultado.setRowHeight(30);
        tb_resultado.setShowGrid(true);
        tb_resultado.setShowHorizontalLines(true);
        tb_resultado.setShowVerticalLines(true);
        jScrollPane1.setViewportView(tb_resultado);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtdni, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtnomape, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtcip, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtdni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(txtnomape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtcip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 148, Short.MAX_VALUE)
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
            java.util.logging.Logger.getLogger(DetalladoCopere.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DetalladoCopere.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DetalladoCopere.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DetalladoCopere.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DetalladoCopere dialog = new DetalladoCopere(new javax.swing.JFrame(), true, null);
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tb_resultado;
    private javax.swing.JTextField txtcip;
    private javax.swing.JTextField txtdni;
    private javax.swing.JTextField txtnomape;
    // End of variables declaration//GEN-END:variables
}
