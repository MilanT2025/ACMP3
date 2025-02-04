/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista;

import static vista.Txt_Plame.ingresosMap;
import static vista.Txt_Plame.tipoSuspension;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Farmacia-Ingeniero
 */
public class PosicionCod extends javax.swing.JDialog {

    private final Modelo modelo = new Modelo();

    /**
     * Creates new form PosicionCod
     */
    public PosicionCod(java.awt.Frame parent, boolean modal) {
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
            "N° de Columna",
            "Codigo TXT"
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

    public void REM() {
        setTitle("Posicion de Codigos - REM");
        jLabel1.setText("Posicion de Codigos - REM");
        jButton1.setText("Exportar Listado - REM");
        jButton2.setText("Guardar Posiciones - REM");

        String nombreArchivo = "datosREM.txt"; // Nombre del archivo
        try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;

            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                modelo.addRow(partes);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void SNL() {
        setTitle("Posicion de Codigos - SNL");
        jLabel1.setText("Posicion de Codigos - SNL");
        jButton1.setText("Exportar Listado - SNL");
        jButton2.setText("Guardar Posiciones - SNL");

        String nombreArchivo = "datosSNL.txt"; // Nombre del archivo
        try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;

            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                modelo.addRow(partes);

            }
        } catch (IOException e) {
            e.printStackTrace();
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
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
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

        jButton1.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Microsoft excel.png"))); // NOI18N
        jButton1.setText("Exportar Listado - ");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Map Pinpoint.png"))); // NOI18N
        jButton2.setText("Guardar Posiciones - REM");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
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
        jLabel1.setText("Posicion de Codigos - ");

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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        modelo.addRow(new Object[]{"", ""});
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

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (jButton2.getText().equals("Guardar Posiciones - REM")) {
            try {
                // Ruta del archivo de texto en la raíz del proyecto
                String filePath = "datosREM.txt";
                // Crear un BufferedWriter para escribir en el archivo
                BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
                // Recorrer el modelo de la tabla y escribir los datos en el archivo
                for (int i = 0; i < tb_resultado.getRowCount(); i++) {
                    for (int j = 0; j < tb_resultado.getColumnCount(); j++) {
                        // Escribir el valor de la celda en el archivo
                        writer.write(tb_resultado.getValueAt(i, j).toString());
                        // Si no es la última columna, agregar una coma
                        if (j < tb_resultado.getColumnCount() - 1) {
                            writer.write(",");
                        }
                    }
                    // Agregar un salto de línea al final de cada fila
                    writer.newLine();
                }
                // Cerrar el BufferedWriter
                writer.close();
                // Mostrar un mensaje de éxito
                JOptionPane.showMessageDialog(null, "Los datos se han guardado correctamente en el archivo.",
                        "Guardado Exitoso", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Mostrar un mensaje de error si ocurre un problema al guardar los datos
                JOptionPane.showMessageDialog(null, "Se ha producido un error al guardar los datos.",
                        "Error al Guardar", JOptionPane.ERROR_MESSAGE);
            }
        } else if (jButton2.getText().equals("Guardar Posiciones - SNL")) {
            try {
                // Ruta del archivo de texto en la raíz del proyecto
                String filePath = "datosSNL.txt";
                // Crear un BufferedWriter para escribir en el archivo
                BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
                // Recorrer el modelo de la tabla y escribir los datos en el archivo
                for (int i = 0; i < tb_resultado.getRowCount(); i++) {
                    for (int j = 0; j < tb_resultado.getColumnCount(); j++) {
                        // Escribir el valor de la celda en el archivo
                        writer.write(tb_resultado.getValueAt(i, j).toString());
                        // Si no es la última columna, agregar una coma
                        if (j < tb_resultado.getColumnCount() - 1) {
                            writer.write(",");
                        }
                    }
                    // Agregar un salto de línea al final de cada fila
                    writer.newLine();
                }
                // Cerrar el BufferedWriter
                writer.close();
                // Mostrar un mensaje de éxito
                JOptionPane.showMessageDialog(null, "Los datos se han guardado correctamente en el archivo.",
                        "Guardado Exitoso", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Mostrar un mensaje de error si ocurre un problema al guardar los datos
                JOptionPane.showMessageDialog(null, "Se ha producido un error al guardar los datos.",
                        "Error al Guardar", JOptionPane.ERROR_MESSAGE);
            }

        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (jButton1.getText().equals("Exportar Listado - REM")) {
            // Crear un JFileChooser
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar como archivo Excel");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos Excel (*.xlsx)", "xlsx"));
            fileChooser.setSelectedFile(new File("Listado REM.xlsx"));

            // Mostrar el diálogo para seleccionar la ubicación y el nombre del archivo
            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                // Obtener la ruta del archivo seleccionado por el usuario
                String filePath = fileChooser.getSelectedFile().getAbsolutePath() + ".xlsx";

                // Guardar los datos del HashMap en un archivo Excel
                try (Workbook workbook = new XSSFWorkbook(); FileOutputStream fileOut = new FileOutputStream(filePath)) {
                    // Crear una nueva hoja en el archivo Excel
                    Sheet sheet = workbook.createSheet("Listado REM");

                    // Crear una fila para los títulos de columna y establecer el estilo de fuente en negrita
                    Font boldFont = workbook.createFont();
                    boldFont.setBold(true);
                    CellStyle boldStyle = workbook.createCellStyle();
                    boldStyle.setFont(boldFont);

                    Row titleRow = sheet.createRow(0);
                    titleRow.createCell(0).setCellValue("Descripcion");
                    titleRow.getCell(0).setCellStyle(boldStyle);
                    titleRow.createCell(1).setCellValue("Codigo");
                    titleRow.getCell(1).setCellStyle(boldStyle);

                    // Contador para el número de fila
                    int rowNum = 1;

                    // Escribir los datos del HashMap en el archivo Excel
                    for (Map.Entry<String, String> entry : ingresosMap.entrySet()) {
                        Row row = sheet.createRow(rowNum++);
                        row.createCell(0).setCellValue(entry.getKey());
                        row.createCell(1).setCellValue(entry.getValue());
                    }

                    // Autoajustar el ancho de las columnas
                    for (int i = 0; i < 2; i++) {
                        sheet.autoSizeColumn(i);
                        // Limitar el ancho máximo de la columna a 50 caracteres
                        sheet.setColumnWidth(i, Math.min(sheet.getColumnWidth(i), 100 * 256));
                    }

                    // Escribir el contenido del libro de trabajo en el archivo
                    workbook.write(fileOut);
                    // Cerrar el flujo de salida del archivo
                    fileOut.close();

                    JOptionPane.showMessageDialog(null, "Los datos se han guardado correctamente en el archivo Excel.",
                            "Guardado Exitoso", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Se ha producido un error al guardar los datos en el archivo Excel.",
                            "Error al Guardar", JOptionPane.ERROR_MESSAGE);
                }
            }

        } else if (jButton1.getText().equals("Exportar Listado - SNL")) {
            // Crear un JFileChooser
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar como archivo Excel");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos Excel (*.xlsx)", "xlsx"));
            fileChooser.setSelectedFile(new File("Listado SNL.xlsx"));

            // Mostrar el diálogo para seleccionar la ubicación y el nombre del archivo
            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                // Obtener la ruta del archivo seleccionado por el usuario
                String filePath = fileChooser.getSelectedFile().getAbsolutePath() + ".xlsx";

                // Guardar los datos del HashMap en un archivo Excel
                try (Workbook workbook = new XSSFWorkbook(); FileOutputStream fileOut = new FileOutputStream(filePath)) {
                    // Crear una nueva hoja en el archivo Excel
                    Sheet sheet = workbook.createSheet("Listado SNL");

                    // Crear una fila para los títulos de columna y establecer el estilo de fuente en negrita
                    Font boldFont = workbook.createFont();
                    boldFont.setBold(true);
                    CellStyle boldStyle = workbook.createCellStyle();
                    boldStyle.setFont(boldFont);

                    Row titleRow = sheet.createRow(0);
                    titleRow.createCell(0).setCellValue("Descripcion");
                    titleRow.getCell(0).setCellStyle(boldStyle);
                    titleRow.createCell(1).setCellValue("Codigo");
                    titleRow.getCell(1).setCellStyle(boldStyle);

                    // Contador para el número de fila
                    int rowNum = 1;

                    // Escribir los datos del HashMap en el archivo Excel
                    for (Map.Entry<String, String> entry : tipoSuspension.entrySet()) {
                        Row row = sheet.createRow(rowNum++);
                        row.createCell(0).setCellValue(entry.getKey());
                        row.createCell(1).setCellValue(entry.getValue());
                    }

                    // Autoajustar el ancho de las columnas
                    for (int i = 0; i < 2; i++) {
                        sheet.autoSizeColumn(i);
                        // Limitar el ancho máximo de la columna a 50 caracteres
                        sheet.setColumnWidth(i, Math.min(sheet.getColumnWidth(i), 100 * 256));
                    }

                    // Escribir el contenido del libro de trabajo en el archivo
                    workbook.write(fileOut);
                    // Cerrar el flujo de salida del archivo
                    fileOut.close();

                    JOptionPane.showMessageDialog(null, "Los datos se han guardado correctamente en el archivo Excel.",
                            "Guardado Exitoso", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Se ha producido un error al guardar los datos en el archivo Excel.",
                            "Error al Guardar", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(PosicionCod.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PosicionCod.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PosicionCod.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PosicionCod.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PosicionCod dialog = new PosicionCod(new javax.swing.JFrame(), true);
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
