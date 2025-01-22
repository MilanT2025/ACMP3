
package vista;

import Controlador.Conexion;
import java.awt.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


public class DetalladoOprefa extends javax.swing.JDialog {

    private final Modelo modelo = new Modelo();
    private final Modelo2 modelo2 = new Modelo2();

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
        
        modelo2.addColumn("Año");
        modelo2.addColumn("Enero");
        modelo2.addColumn("Febrero");
        modelo2.addColumn("Marzo");
        modelo2.addColumn("Abril");
        modelo2.addColumn("Mayo");
        modelo2.addColumn("Junio");
        modelo2.addColumn("Julio");
        modelo2.addColumn("Agosto");
        modelo2.addColumn("Septiembre");
        modelo2.addColumn("Octubre");
        modelo2.addColumn("Noviembre");
        modelo2.addColumn("Diciembre");
        modelo2.addColumn("Total Deuda");

        
        JTableHeader header2 = tb_resultado1.getTableHeader();
        header2.setPreferredSize(new java.awt.Dimension(header2.getWidth(), 35));

        header2.setBackground(new java.awt.Color(255, 217, 102));
        tb_resultado1.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

        TableColumnModel columnModel2 = tb_resultado1.getColumnModel();
        for (int col = 0; col < tb_resultado.getColumnCount(); col++) {
            TableColumn tableColumn = columnModel2.getColumn(col);
            Component comp = header2.getDefaultRenderer().getTableCellRendererComponent(tb_resultado1, tableColumn.getHeaderValue(), false, false, 0, col);
            int width = comp.getPreferredSize().width + 25;
            tableColumn.setPreferredWidth(width);
        }
    }

    private void cargarSumaTotal() {
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.'); // Usar punto como separador decimal
        DecimalFormat dm = new DecimalFormat("0.00", dfs);

        double aporte = 0, deuda = 0;
        
        for (int i = 0; i < tb_resultado.getRowCount(); i++) {
            aporte = aporte + Double.parseDouble(tb_resultado.getValueAt(i, 13).toString());
        }
        
        for (int i = 0; i < tb_resultado1.getRowCount(); i++) {
            deuda = deuda + Double.parseDouble(tb_resultado1.getValueAt(i, 13).toString());
        }
        
        jLabel5.setText("Total Aporte: S/ " + dm.format(aporte));
        jLabel7.setText("Total Deuda: S/ " + dm.format(deuda));
    }

    public class Modelo extends DefaultTableModel {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }
    
    public class Modelo2 extends DefaultTableModel {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    /**
     * Creates new form DetalladoCopere
     */
    public DetalladoOprefa(java.awt.Frame parent, boolean modal, String numerocip) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        llenar_tabla();
        cargarDatosPrincipales(numerocip);
        cargarDatosAporteDeuda(numerocip, "Aporte", modelo);
        cargarDatosAporteDeuda(numerocip, "Deuda", modelo2);
        cargarSumaTotal();

    }
    
    private void cargarDatosPrincipales(String dato) {
        try {
            String[] data = new String[14];
            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "SELECT ISNULL ((pe.Nombres+' '+pe.A_Paterno+' '+pe.A_Materno),'No se encuentra el afiliado en la BD') AS Afiliado, ISNULL(pe.Dni,'-') AS DNI, ISNULL(NroCip,'-') as CIP FROM Personal pe WHERE DNI = '" + dato + "' ";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                txtdni.setText(rs.getString(2));
                txtnomape.setText(rs.getString(1));
                txtcip.setText(rs.getString(3));
            }
            rs.close();
            st.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DetalladoOprefa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void cargarDatosAporteDeuda(String numerocip, String tipo, DefaultTableModel modelo) {
        String sql = null;
        switch (tipo) {
            case "Aporte":
                sql = "SELECT "
                        + "	Año, "
                        + "	SUM(Enero) AS Enero, "
                        + "	SUM(Febrero) AS Febrero, "
                        + "	SUM(Marzo) AS Marzo, "
                        + "	SUM(Abril) AS Abril, "
                        + "	SUM(Mayo) AS Mayo, "
                        + "	SUM(Junio) AS Junio, "
                        + "	SUM(Julio) AS Julio, "
                        + "	SUM(Agosto) AS Agosto, "
                        + "	SUM(Septiembre) AS Septiembre, "
                        + "	SUM(Octubre) AS Octubre, "
                        + "	SUM(Noviembre) AS Noviembre, "
                        + "	SUM(Diciembre) AS Diciembre "
                        + "FROM "
                        + " "
                        + "(SELECT Año, "
                        + " SUM(CASE WHEN Mes = 1 THEN Monto ELSE 0 END) AS Enero, "
                        + " SUM(CASE WHEN Mes = 2 THEN Monto ELSE 0 END) AS Febrero, "
                        + " SUM(CASE WHEN Mes = 3 THEN Monto ELSE 0 END) AS Marzo, "
                        + " SUM(CASE WHEN Mes = 4 THEN Monto ELSE 0 END) AS Abril, "
                        + " SUM(CASE WHEN Mes = 5 THEN Monto ELSE 0 END) AS Mayo, "
                        + " SUM(CASE WHEN Mes = 6 THEN Monto ELSE 0 END) AS Junio, "
                        + " SUM(CASE WHEN Mes = 7 THEN Monto ELSE 0 END) AS Julio, "
                        + " SUM(CASE WHEN Mes = 8 THEN Monto ELSE 0 END) AS Agosto, "
                        + " SUM(CASE WHEN Mes = 9 THEN Monto ELSE 0 END) AS Septiembre, "
                        + " SUM(CASE WHEN Mes = 10 THEN Monto ELSE 0 END) AS Octubre, "
                        + " SUM(CASE WHEN Mes = 11 THEN Monto ELSE 0 END) AS Noviembre, "
                        + " SUM(CASE WHEN Mes = 12 THEN Monto ELSE 0 END) AS Diciembre "
                        + " FROM HistorialOprefa hc "
                        + " WHERE Documento = ? AND Estado IN (1, 4, 5) "
                        + " GROUP BY Año "
                        + " "
                        + " UNION "
                        + " "
                        + " SELECT Año, "
                        + " SUM(CASE WHEN Mes = 1 THEN MontoPagado ELSE 0 END) AS Enero, "
                        + " SUM(CASE WHEN Mes = 2 THEN MontoPagado ELSE 0 END) AS Febrero, "
                        + " SUM(CASE WHEN Mes = 3 THEN MontoPagado ELSE 0 END) AS Marzo, "
                        + " SUM(CASE WHEN Mes = 4 THEN MontoPagado ELSE 0 END) AS Abril, "
                        + " SUM(CASE WHEN Mes = 5 THEN MontoPagado ELSE 0 END) AS Mayo, "
                        + " SUM(CASE WHEN Mes = 6 THEN MontoPagado ELSE 0 END) AS Junio, "
                        + " SUM(CASE WHEN Mes = 7 THEN MontoPagado ELSE 0 END) AS Julio, "
                        + " SUM(CASE WHEN Mes = 8 THEN MontoPagado ELSE 0 END) AS Agosto, "
                        + " SUM(CASE WHEN Mes = 9 THEN MontoPagado ELSE 0 END) AS Septiembre, "
                        + " SUM(CASE WHEN Mes = 10 THEN MontoPagado ELSE 0 END) AS Octubre, "
                        + " SUM(CASE WHEN Mes = 11 THEN MontoPagado ELSE 0 END) AS Noviembre, "
                        + " SUM(CASE WHEN Mes = 12 THEN MontoPagado ELSE 0 END) AS Diciembre "
                        + " FROM HistorialPagos hc "
                        + " WHERE Documento = ? AND RegContable = 'Oprefa' "
                        + " GROUP BY Año) AS D1 "
                        + " GROUP BY D1.Año "
                        + " ORDER BY Año DESC;";
                break;

            case "Deuda":
                sql = "SELECT "
                        + "    HC.Año, "
                        + "    CAST(SUM(CASE WHEN HC.Mes = 1 THEN HC.Monto - ISNULL(HP.TotalPagado, 0) ELSE 0 END) AS DECIMAL(10,2)) AS Enero, "
                        + "    CAST(SUM(CASE WHEN HC.Mes = 2 THEN HC.Monto - ISNULL(HP.TotalPagado, 0) ELSE 0 END) AS DECIMAL(10,2)) AS Febrero, "
                        + "    CAST(SUM(CASE WHEN HC.Mes = 3 THEN HC.Monto - ISNULL(HP.TotalPagado, 0) ELSE 0 END) AS DECIMAL(10,2)) AS Marzo, "
                        + "    CAST(SUM(CASE WHEN HC.Mes = 4 THEN HC.Monto - ISNULL(HP.TotalPagado, 0) ELSE 0 END) AS DECIMAL(10,2)) AS Abril, "
                        + "    CAST(SUM(CASE WHEN HC.Mes = 5 THEN HC.Monto - ISNULL(HP.TotalPagado, 0) ELSE 0 END) AS DECIMAL(10,2)) AS Mayo, "
                        + "    CAST(SUM(CASE WHEN HC.Mes = 6 THEN HC.Monto - ISNULL(HP.TotalPagado, 0) ELSE 0 END) AS DECIMAL(10,2)) AS Junio, "
                        + "    CAST(SUM(CASE WHEN HC.Mes = 7 THEN HC.Monto - ISNULL(HP.TotalPagado, 0) ELSE 0 END) AS DECIMAL(10,2)) AS Julio, "
                        + "    CAST(SUM(CASE WHEN HC.Mes = 8 THEN HC.Monto - ISNULL(HP.TotalPagado, 0) ELSE 0 END) AS DECIMAL(10,2)) AS Agosto, "
                        + "    CAST(SUM(CASE WHEN HC.Mes = 9 THEN HC.Monto - ISNULL(HP.TotalPagado, 0) ELSE 0 END) AS DECIMAL(10,2)) AS Septiembre, "
                        + "    CAST(SUM(CASE WHEN HC.Mes = 10 THEN HC.Monto - ISNULL(HP.TotalPagado, 0) ELSE 0 END) AS DECIMAL(10,2)) AS Octubre, "
                        + "    CAST(SUM(CASE WHEN HC.Mes = 11 THEN HC.Monto - ISNULL(HP.TotalPagado, 0) ELSE 0 END) AS DECIMAL(10,2)) AS Noviembre, "
                        + "    CAST(SUM(CASE WHEN HC.Mes = 12 THEN HC.Monto - ISNULL(HP.TotalPagado, 0) ELSE 0 END) AS DECIMAL(10,2)) AS Diciembre "
                        + "FROM HistorialOprefa hc "
                        + " LEFT JOIN "
                        + "    ( "
                        + "        SELECT Documento, Año, Mes, SUM(MontoPagado) AS TotalPagado "
                        + "        FROM HistorialPagos "
                        + "        WHERE RegContable = 'Oprefa' "
                        + "        GROUP BY Documento, Año, Mes "
                        + "    ) HP "
                        + "    ON HC.Documento = HP.Documento "
                        + "    AND HC.Año = HP.Año "
                        + "    AND HC.Mes = HP.Mes "
                        + " WHERE HC.Documento = ? AND Estado NOT IN (1, 4, 5) "
                        + " GROUP BY HC.Año "
                        + " ORDER BY HC.Año DESC;";
                break;

        }
        

        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
             ps.setString(1, numerocip);
            if (tipo.equals("Aporte")) {
                ps.setString(2, numerocip);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String[] data = new String[14];
                    for (int i = 0; i < 13; i++) {
                        data[i] = rs.getString(i + 1);
                    }
                    data[13] = calculateTotal(rs);
                    modelo.addRow(data);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DetalladoOprefa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String calculateTotal(ResultSet rs) throws SQLException {
        double total = 0;
        for (int i = 2; i <= 13; i++) {
            total += rs.getDouble(i);
        }
        return String.valueOf(total);
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
        jScrollPane2 = new javax.swing.JScrollPane();
        tb_resultado1 = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("COPERE - Detallado Aportante");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));

        jLabel4.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("APORTE");

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
                .addContainerGap(7, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addContainerGap(8, Short.MAX_VALUE))
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
        jScrollPane1.setViewportView(tb_resultado);

        tb_resultado1.setModel(modelo2);
        tb_resultado1.setRowHeight(30);
        tb_resultado1.setShowGrid(true);
        jScrollPane2.setViewportView(tb_resultado1);

        jLabel5.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 0, 0));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Total Aporte S/.");

        jPanel3.setBackground(new java.awt.Color(204, 0, 0));

        jLabel6.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("DEUDA");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel7.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 0, 0));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Total Deuda S/.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(8, 8, 8)
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
                                .addComponent(txtcip, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 864, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 864, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtdni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(txtnomape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtcip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addContainerGap(15, Short.MAX_VALUE))
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
            java.util.logging.Logger.getLogger(DetalladoOprefa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DetalladoOprefa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DetalladoOprefa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DetalladoOprefa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DetalladoOprefa dialog = new DetalladoOprefa(new javax.swing.JFrame(), true, null);
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
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tb_resultado;
    private javax.swing.JTable tb_resultado1;
    private javax.swing.JTextField txtcip;
    private javax.swing.JTextField txtdni;
    private javax.swing.JTextField txtnomape;
    // End of variables declaration//GEN-END:variables
}
