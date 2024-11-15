/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import Controlador.Conexion;
import com.formdev.flatlaf.FlatIntelliJLaf;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Farmacia-Ingeniero
 */
public class LibroMayor extends javax.swing.JFrame {

    private final Modelo modelo = new Modelo();

    /**
     * Creates new form LibroMayor
     */
    public LibroMayor() {
        initComponents();
        this.setIconImage(new ImageIcon(System.getProperty("user.dir") + "/logoACMP.png").getImage());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(false);
                Principal anteriorFrame = new Principal();
                anteriorFrame.setVisible(true);
            }
        });

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        llenar_tabla();
    }

    private void llenar_tabla() {
        String[] columnNames = new String[]{
            "A",
            "B",
            "C",
            "D",
            "E",
            "F",
            "G",
            "H"
        };

        modelo.setColumnIdentifiers(columnNames);

        JTableHeader header = tb_resultado.getTableHeader();
        header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 40));

        header.setBackground(new java.awt.Color(0, 102, 153));
        header.setForeground(new java.awt.Color(255, 255, 255));
        tb_resultado.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int i = 0; i < tb_resultado.getColumnCount(); i++) {
            tb_resultado.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);

        }
    }

    public class Modelo extends DefaultTableModel {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    private void openFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecciona un archivo Excel (.xlsx)");

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                loadExcelData(selectedFile);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al leer el archivo: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /*private void loadExcelData(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file); Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            modelo.setRowCount(0);

            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                int lastCellNum = row.getLastCellNum();
                if (lastCellNum < 0) {
                    lastCellNum = sheet.getRow(0).getLastCellNum(); // Asegura tener las mismas columnas
                }
                Object[] rowData = new Object[lastCellNum];

                for (int colIndex = 0; colIndex < lastCellNum; colIndex++) {
                    Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowData[colIndex] = getCellValue(cell);
                }

                modelo.addRow(rowData);
            }
        }
    }*/
    private void loadExcelData(File file) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        try (FileInputStream fis = new FileInputStream(file); Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            modelo.setRowCount(0);

            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                int lastCellNum = row.getLastCellNum();
                if (lastCellNum < 0) {
                    lastCellNum = sheet.getRow(0).getLastCellNum(); // Asegura tener las mismas columnas
                }
                Object[] rowData = new Object[lastCellNum];

                for (int colIndex = 0; colIndex < lastCellNum; colIndex++) {
                    Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                    // Columna 0 - Fecha en formato dd/MM/yyyy
                    if (colIndex == 0) {
                        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                            Date date = cell.getDateCellValue();
                            rowData[colIndex] = dateFormat.format(date);
                        } else if (cell.getCellType() == CellType.STRING) {
                            rowData[colIndex] = cell.getStringCellValue(); // Deja el valor si es cadena
                        } else {
                            rowData[colIndex] = ""; // Si no es fecha ni cadena, deja vacío
                        }
                    } // Columna 1 - Enteros
                    else if (colIndex == 1) {
                        if (cell.getCellType() == CellType.NUMERIC) {
                            rowData[colIndex] = (int) cell.getNumericCellValue(); // Solo convierte si es numérico
                        } else if (cell.getCellType() == CellType.STRING) {
                            rowData[colIndex] = cell.getStringCellValue(); // Deja el valor si es cadena
                        } else {
                            rowData[colIndex] = ""; // Si no es válido, deja vacío
                        }
                    } // Columna 3 - Cadenas (String)
                    else if (colIndex == 3) {
                        if (cell.getCellType() == CellType.STRING) {
                            rowData[colIndex] = cell.getStringCellValue();
                        } else {
                            rowData[colIndex] = ""; // Deja vacío si no es cadena
                        }
                    } // Columna 4 y 6 - Decimal con formato 0.00
                    else if (colIndex == 4 || colIndex == 5) {
                        if (cell.getCellType() == CellType.NUMERIC) {
                            rowData[colIndex] = decimalFormat.format(cell.getNumericCellValue());
                        } else if (cell.getCellType() == CellType.STRING) {
                            rowData[colIndex] = cell.getStringCellValue(); // Deja el valor si es cadena
                        } else {
                            rowData[colIndex] = ""; // Deja vacío si no es numérico ni cadena
                        }
                    } // Otras columnas
                    else {
                        rowData[colIndex] = getCellValue(cell); // Deja el valor original si no hay formato especial
                    }
                }

                modelo.addRow(rowData);
            }
        }
    }

    private Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    public void replicarDatos(DefaultTableModel modelo, String numeroCuenta, String valorEncabezado1, String valorEncabezado3) {
        int filaInicial = -1, filaFinal = -1;

        for (int i = 0; i < modelo.getRowCount(); i++) {
            Object valorColumna1 = modelo.getValueAt(i, 1);
            if (valorColumna1 != null && valorColumna1.toString().equals(numeroCuenta)) {
                filaInicial = i;
            }

            Object valorColumna3 = modelo.getValueAt(i, 3);
            if (filaInicial > -1 && valorColumna3 != null && (valorColumna3.toString().equals("SALDO DEUDOR:  ") || valorColumna3.toString().equals("SALDO ACREEDOR:  "))) {
                filaFinal = i;
                break;
            }
        }

        if (filaInicial == -1 || filaFinal == -1) {
            //System.out.println("Error: No se encontró la fila inicial o final.");
            return;
        }

        String[] encabezado = new String[6];
        modelo.addRow(encabezado);

        encabezado[0] = "Cuenta: ";
        encabezado[1] = valorEncabezado1;
        encabezado[3] = valorEncabezado3;

        modelo.addRow(encabezado);

        for (int i = filaInicial + 1; i <= filaFinal; i++) {
            Object valor0 = modelo.getValueAt(i, 0);
            Object valor1 = modelo.getValueAt(i, 1);
            Object valor2 = modelo.getValueAt(i, 2);
            Object valor3 = modelo.getValueAt(i, 3);
            Object valor4 = modelo.getValueAt(i, 4);
            Object valor5 = modelo.getValueAt(i, 5);

            encabezado[0] = (valor0 != null) ? valor0.toString() : "";
            encabezado[1] = (valor1 != null) ? valor1.toString() : "";
            encabezado[2] = (valor2 != null) ? valor2.toString() : "";
            encabezado[3] = (valor3 != null) ? valor3.toString() : "";
            encabezado[4] = (valor4 != null) ? valor4.toString() : "";
            encabezado[5] = (valor5 != null) ? valor5.toString() : "";

            modelo.addRow(encabezado);
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
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_resultado = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        txtBuscar = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(0, 102, 102));

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("LIBRO MAYOR");

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
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Datos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 12))); // NOI18N

        tb_resultado.setModel(modelo);
        tb_resultado.setRowHeight(25);
        jScrollPane1.setViewportView(tb_resultado);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 543, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton1.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jButton1.setText("1. Cargar Libro Mayor");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jButton2.setText("Cuentas Equivalentes");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("jButton3");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("buscar");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 538, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton3)
                                .addGap(18, 18, 18)
                                .addComponent(jButton4))
                            .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 16, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                        .addComponent(jTextField1)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton4)
                            .addComponent(jButton3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        openFileChooser();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "SELECT * FROM LibroMayorEquivalencias";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                replicarDatos(modelo, rs.getString(1), rs.getString(3), rs.getString(4));
            }

            rs.close();
            st.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(LibroMayor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        /*for (int i = 0; i < modelo.getRowCount(); i++) {
            if (modelo.getValueAt(i, 0).equals("Cuenta: ") && !modelo.getValueAt(i, 1).equals("")) {
                System.out.println("Fila Inicial: " + i);
            }
            
            if (modelo.getValueAt(i, 3).equals("SALDO DEUDOR:  ") || modelo.getValueAt(i, 3).equals("SALDO ACREEDOR:  ")) {
                System.out.println("Fila Final: " + i);
            }
        }*/

        ordenarPorNumeroDeCuenta(tb_resultado);

    }//GEN-LAST:event_jButton3ActionPerformed

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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LibroMayor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTable tb_resultado;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables

    public static void ordenarPorNumeroDeCuenta(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int rowCount = model.getRowCount();
        
        // Lista para almacenar bloques
        List<List<Object[]>> bloques = new ArrayList<>();
        
        List<Object[]> bloqueActual = new ArrayList<>();
        
        for (int i = 0; i < rowCount; i++) {
            // Verificación de null para evitar NullPointerException
            String columna0 = (model.getValueAt(i, 0) != null) ? model.getValueAt(i, 0).toString() : "";
            String columna1 = (model.getValueAt(i, 1) != null) ? model.getValueAt(i, 1).toString() : "";
            String columna3 = (model.getValueAt(i, 3) != null) ? model.getValueAt(i, 3).toString() : "";

            // Identificar el inicio de un bloque
            if (columna0.equals("Cuenta: ")) {
                // Si ya hay un bloque en curso, lo añadimos a la lista
                if (!bloqueActual.isEmpty()) {
                    bloques.add(bloqueActual);
                }
                // Iniciar un nuevo bloque
                bloqueActual = new ArrayList<>();
            }

            // Añadir la fila actual al bloque
            bloqueActual.add(new Object[]{
                model.getValueAt(i, 0), 
                model.getValueAt(i, 1),
                model.getValueAt(i, 2),
                model.getValueAt(i, 3)
            });

            // Si es el fin de un bloque ("SALDO DEUDOR:" o "SALDO ACREEDOR:")
            if (columna3.equals("SALDO DEUDOR: ") || columna3.equals("SALDO ACREEDOR: ")) {
                bloques.add(bloqueActual);
                bloqueActual = new ArrayList<>();
            }
        }

        // Asegurarse de añadir el último bloque si existe
        if (!bloqueActual.isEmpty()) {
            bloques.add(bloqueActual);
        }

        // Ordenar los bloques por el número de cuenta (columna 1)
        Collections.sort(bloques, new Comparator<List<Object[]>>() {
            @Override
            public int compare(List<Object[]> bloque1, List<Object[]> bloque2) {
                // Verificar si el valor en la columna 1 es numérico
                int cuenta1 = obtenerNumeroDeCuenta(bloque1);
                int cuenta2 = obtenerNumeroDeCuenta(bloque2);
                return Integer.compare(cuenta1, cuenta2);
            }
        });

        // Limpiar la tabla antes de reinsertar los datos
        model.setRowCount(0);

        // Volver a llenar la tabla con los bloques ordenados
        for (List<Object[]> bloque : bloques) {
            for (Object[] fila : bloque) {
                model.addRow(fila);
            }
        }
    }

    // Método auxiliar para obtener el número de cuenta, manejando valores no numéricos
    private static int obtenerNumeroDeCuenta(List<Object[]> bloque) {
        String valorCuenta = bloque.get(0)[1].toString(); // Valor de la columna 1 (Número de cuenta)
        if (valorCuenta != null && !valorCuenta.isEmpty()) {
            try {
                return Integer.parseInt(valorCuenta); // Intentar convertir a número
            } catch (NumberFormatException e) {
                // Manejar el caso de valores no numéricos
                System.err.println("Valor inválido para el número de cuenta: " + valorCuenta);
                return Integer.MAX_VALUE; // Asignar un valor muy alto para que este bloque vaya al final
            }
        }
        return Integer.MAX_VALUE; // Si está vacío o no es un número, se envía al final
    }
}
