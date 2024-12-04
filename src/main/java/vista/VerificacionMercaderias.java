/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista;

import Controlador.Conexion;
import java.awt.Color;
import java.awt.Component;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
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
public class VerificacionMercaderias extends javax.swing.JDialog {

    private final Modelo modelo = new Modelo();
    Double invfinal;
    
    public class Modelo extends DefaultTableModel {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }
    /**
     * Creates new form VerificacionCargaMercaderia
     */
    public VerificacionMercaderias(java.awt.Frame parent, boolean modal, String title, String invfinal) {
        super(parent, modal);
        initComponents();
        
        this.invfinal = Double.valueOf(invfinal);
        this.setTitle("Verificacion de Total Mercaderias - " + title);
        UIManager.getLookAndFeelDefaults().put("Table.alternateRowColor", new Color(254, 238, 184));
        this.setIconImage(new ImageIcon(System.getProperty("user.dir") + "/logoACMP.png").getImage());
        this.setLocationRelativeTo(null);
        llenar_tabla();
    }
    
    private void llenar_tabla() {
        modelo.setRowCount(0);
        modelo.setColumnCount(0);

        JTableHeader header = tb_resultado.getTableHeader();
        header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 50));
        header.setBackground(new java.awt.Color(255, 217, 102));
        tb_resultado.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

        modelo.addColumn("DESCRIPCION");
        modelo.addColumn("MONTO");
        
        verificacion(Mercaderia.jdc_año.getYear(), (Mercaderia.jdc_mes.getMonth()+1));
        
    }
    
    private void verificacion(int año, int mes) {
        String sql = "SELECT SUM([Debe Anterior]) - SUM([Haber Anterior]) AS [Inv. Inicial], "
                + "SUM([Debe Mes (restando)]) - SUM([Haber Mes (sumar)]) AS [Compras] "
                + "FROM patrimonio "
                + "WHERE Año = ? AND Mes = ? AND "
                + "(Cuenta LIKE '20%' OR Cuenta LIKE '24%' OR Cuenta LIKE '25%' OR Cuenta LIKE '28%')";

        try (Connection con = Conexion.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, año);
            ps.setInt(2, mes);

            try (ResultSet rs = ps.executeQuery()) {
                // Crear el DecimalFormat con los símbolos locales para español
                Locale locale = new Locale("es", "PE");
                DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
                symbols.setGroupingSeparator(',');
                symbols.setDecimalSeparator('.');
                DecimalFormat df = new DecimalFormat("#,###.00", symbols);

                if (rs.next()) {
                    // Formatear los valores usando DecimalFormat
                    modelo.addRow(new Object[]{"Inv. Inicial", rs.getObject(1) == null ? "0.00" : df.format(rs.getObject(1))});
                    modelo.addRow(new Object[]{"Compras", rs.getObject(2) == null ? "0.00" : df.format(rs.getObject(2))});
                } else {
                    modelo.addRow(new Object[]{"Inv. Inicial", "0.00"});
                    modelo.addRow(new Object[]{"Compras", "0.00"});
                }
            }

            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(VerificacionMercaderias.class.getName()).log(Level.SEVERE, null, ex);
        }

        calcularTotal();
    }
    
    private void calcularTotal() {
        // Obtener valores y formatearlos
        Double invInicial, compras;
        invInicial = Double.valueOf(tb_resultado.getValueAt(0, 1).toString().replaceAll(",", ""));
        compras = Double.valueOf(tb_resultado.getValueAt(1, 1).toString().replaceAll(",", ""));

        // Calcular el Costo de Ventas
        double costoVentas = invfinal - (invInicial + compras);

        // Crear el DecimalFormat para formatear los números con separadores
        Locale locale = new Locale("es", "PE");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#,###.00", symbols);

        // Agregar el Costo de Ventas formateado
        modelo.addRow(new Object[]{"Costo de Ventas", df.format(costoVentas)});
        modelo.addRow(new Object[]{"Inv. Final", df.format(invfinal)});

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
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_resultado = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Verificacion de Total Mercaderias");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        tb_resultado.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        tb_resultado.setModel(modelo);
        tb_resultado.setRowHeight(30);
        tb_resultado.setShowGrid(true);
        tb_resultado.setShowVerticalLines(false);
        jScrollPane1.setViewportView(tb_resultado);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
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
            java.util.logging.Logger.getLogger(VerificacionMercaderias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VerificacionMercaderias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VerificacionMercaderias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VerificacionMercaderias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                VerificacionMercaderias dialog = new VerificacionMercaderias(new javax.swing.JFrame(), true, null, null);
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tb_resultado;
    // End of variables declaration//GEN-END:variables
}
