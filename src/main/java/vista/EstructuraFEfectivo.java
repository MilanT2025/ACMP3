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
public class EstructuraFEfectivo extends javax.swing.JDialog {

    private final Modelo modelo = new Modelo();

    /**
     * Creates new form PosicionCod
     */
    public EstructuraFEfectivo(java.awt.Frame parent, boolean modal) {
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
            "Concepto",
            "Plan Cuenta"
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

    public void Ingresos_Efectivo() {
        try {
            setTitle("Conceptos y Plan Cuenta de Ingresos");
            jLabel1.setText("Flujo de Efectivo - Conceptos y Plan Cuenta");
            jButton2.setText("Guardar Conceptos y Plan Cuenta - INGRESOS");

            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "SELECT Concepto FROM EstructuraFlujoEfectivo WHERE Tipo = 'INGRESO' ORDER BY Concepto";
            ResultSet rs = st.executeQuery(sql);

            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

            while (rs.next()) {
                String concepto = rs.getString(1);
                model.addElement(concepto);
            }

            JComboBox<String> comboBox = new JComboBox<>(model);

            TableColumn optionsColumn = tb_resultado.getColumnModel().getColumn(0);
            optionsColumn.setCellEditor(new DefaultCellEditor(comboBox));

            String nombreArchivo = "ingreso_FlujoEfectivo.txt"; // Nombre del archivo
            try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
                String linea;

                while ((linea = reader.readLine()) != null) {
                    String[] partes = linea.split(",");

                    boolean found = false;
                    for (int i = 0; i < model.getSize(); i++) {
                        if (model.getElementAt(i).equals(partes[0])) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        partes[0] = "";
                    }

                    modelo.addRow(partes);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            rs.close();
            st.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(EstructuraFEfectivo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void Ingresos_Caja() {
        try {
            setTitle("Conceptos y Plan Cuenta de Ingresos");
            jLabel1.setText("Flujo de Caja - Conceptos y Plan Cuenta");
            jButton2.setText("Guardar Conceptos y Plan Cuenta - INGRESOS - CAJA");

            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "SELECT Concepto FROM EstructuraFlujoCaja WHERE Tipo = 'INGRESO' ORDER BY Concepto";
            ResultSet rs = st.executeQuery(sql);

            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

            while (rs.next()) {
                String concepto = rs.getString(1);
                model.addElement(concepto);
            }

            JComboBox<String> comboBox = new JComboBox<>(model);

            TableColumn optionsColumn = tb_resultado.getColumnModel().getColumn(0);
            optionsColumn.setCellEditor(new DefaultCellEditor(comboBox));

            String nombreArchivo = "ingreso_FlujoCaja.txt"; // Nombre del archivo
            try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
                String linea;

                while ((linea = reader.readLine()) != null) {
                    String[] partes = linea.split(",");

                    boolean found = false;
                    for (int i = 0; i < model.getSize(); i++) {
                        if (model.getElementAt(i).equals(partes[0])) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        partes[0] = "";
                    }

                    modelo.addRow(partes);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            rs.close();
            st.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(EstructuraFEfectivo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void Egresos_Efectivo() {
        try {
            setTitle("Conceptos y Plan Cuenta de Egresos");
            jLabel1.setText("Flujo de Efectivo - Conceptos y Plan Cuenta");
            jButton2.setText("Guardar Conceptos y Plan Cuenta - EGRESOS");

            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "SELECT Concepto FROM EstructuraFlujoEfectivo WHERE Tipo = 'EGRESO' ORDER BY Concepto";
            ResultSet rs = st.executeQuery(sql);

            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

            while (rs.next()) {
                String concepto = rs.getString(1);
                model.addElement(concepto);
            }

            JComboBox<String> comboBox = new JComboBox<>(model);

            TableColumn optionsColumn = tb_resultado.getColumnModel().getColumn(0);
            optionsColumn.setCellEditor(new DefaultCellEditor(comboBox));

            String nombreArchivo = "egreso_FlujoEfectivo.txt"; // Nombre del archivo
            try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
                String linea;

                while ((linea = reader.readLine()) != null) {
                    String[] partes = linea.split(",");

                    boolean found = false;
                    for (int i = 0; i < model.getSize(); i++) {
                        if (model.getElementAt(i).equals(partes[0])) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        partes[0] = "";
                    }

                    modelo.addRow(partes);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            rs.close();
            st.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(EstructuraFEfectivo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void Cuenta33_Efectivo() {
        try {
            setTitle("Conceptos y Plan Cuenta de Cuentas 33");
            jLabel1.setText("Flujo de Efectivo - Conceptos y Plan Cuenta");
            jButton2.setText("Guardar Conceptos y Plan Cuenta - CUENTAS 33");

            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "SELECT Concepto FROM EstructuraFlujoEfectivo WHERE Tipo = 'CUENTA33' ORDER BY Concepto";
            ResultSet rs = st.executeQuery(sql);

            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

            while (rs.next()) {
                String concepto = rs.getString(1);
                model.addElement(concepto);
            }

            JComboBox<String> comboBox = new JComboBox<>(model);

            TableColumn optionsColumn = tb_resultado.getColumnModel().getColumn(0);
            optionsColumn.setCellEditor(new DefaultCellEditor(comboBox));

            String nombreArchivo = "cuenta_FlujoEfectivo.txt"; // Nombre del archivo
            try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
                String linea;

                while ((linea = reader.readLine()) != null) {
                    String[] partes = linea.split(",");

                    boolean found = false;
                    for (int i = 0; i < model.getSize(); i++) {
                        if (model.getElementAt(i).equals(partes[0])) {
                            found = true;
                            break;
                        }
                    }

                    if (!found) {
                        partes[0] = "";
                    }

                    modelo.addRow(partes);

                }
                
            } catch (IOException e) {
                e.printStackTrace();
            }

            rs.close();
            st.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(EstructuraFEfectivo.class.getName()).log(Level.SEVERE, null, ex);
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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        if (tb_resultado.isEditing()) {
            tb_resultado.getCellEditor().stopCellEditing();
        }
        for (int i = 0; i < tb_resultado.getRowCount(); i++) {
            if (tb_resultado.getValueAt(i, 0) == null || tb_resultado.getValueAt(i, 0).toString().isEmpty() || tb_resultado.getValueAt(i, 1) == null || tb_resultado.getValueAt(i, 1).toString().isEmpty()) {
                JOptionPane.showMessageDialog(null, "No se puede ingresar una fila con concepto o plan cuenta en blanco, verifique los datos.", "Error", JOptionPane.ERROR_MESSAGE);
                tb_resultado.setRowSelectionInterval(i, i);
                return;
            }
        }

        if (jButton2.getText().equals("Guardar Conceptos y Plan Cuenta - INGRESOS")) {
            // Mostrar un cuadro de diálogo de confirmación
            int response = JOptionPane.showConfirmDialog(
                    null,
                    "¿Estás seguro de que deseas guardar los datos?",
                    "Confirmar Guardado",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            // Si el usuario confirma la acción, proceder con el guardado
            if (response == JOptionPane.YES_OPTION) {
                try {
                    // Ruta del archivo de texto en la raíz del proyecto
                    String filePath = "ingreso_FlujoEfectivo.txt";
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
                    JOptionPane.showMessageDialog(
                            null,
                            "Los datos se han guardado correctamente en el archivo.",
                            "Guardado Exitoso",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    this.dispose();
                    Flujo_Efectivo.btn_cargar.doClick();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    // Mostrar un mensaje de error si ocurre un problema al guardar los datos
                    JOptionPane.showMessageDialog(
                            null,
                            "Se ha producido un error al guardar los datos.",
                            "Error al Guardar",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        } else if (jButton2.getText().equals("Guardar Conceptos y Plan Cuenta - EGRESOS")) {
            // Mostrar un cuadro de diálogo de confirmación
            int response = JOptionPane.showConfirmDialog(
                    null,
                    "¿Estás seguro de que deseas guardar los datos?",
                    "Confirmar Guardado",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            // Si el usuario confirma la acción, proceder con el guardado
            if (response == JOptionPane.YES_OPTION) {
                try {
                    // Ruta del archivo de texto en la raíz del proyecto
                    String filePath = "egreso_FlujoEfectivo.txt";
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
                    JOptionPane.showMessageDialog(
                            null,
                            "Los datos se han guardado correctamente en el archivo.",
                            "Guardado Exitoso",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    this.dispose();
                    Flujo_Efectivo.btn_cargar.doClick();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    // Mostrar un mensaje de error si ocurre un problema al guardar los datos
                    JOptionPane.showMessageDialog(
                            null,
                            "Se ha producido un error al guardar los datos.",
                            "Error al Guardar",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        } else if (jButton2.getText().equals("Guardar Conceptos y Plan Cuenta - CUENTAS 33")) {
            // Mostrar un cuadro de diálogo de confirmación
            int response = JOptionPane.showConfirmDialog(
                    null,
                    "¿Estás seguro de que deseas guardar los datos?",
                    "Confirmar Guardado",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            // Si el usuario confirma la acción, proceder con el guardado
            if (response == JOptionPane.YES_OPTION) {
                try {
                    // Ruta del archivo de texto en la raíz del proyecto
                    String filePath = "cuenta_FlujoEfectivo.txt";
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
                    JOptionPane.showMessageDialog(
                            null,
                            "Los datos se han guardado correctamente en el archivo.",
                            "Guardado Exitoso",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    this.dispose();
                    Flujo_Efectivo.btn_cargar.doClick();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    // Mostrar un mensaje de error si ocurre un problema al guardar los datos
                    JOptionPane.showMessageDialog(
                            null,
                            "Se ha producido un error al guardar los datos.",
                            "Error al Guardar",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        } else if (jButton2.getText().equals("Guardar Conceptos y Plan Cuenta - INGRESOS - CAJA")) {
            // Mostrar un cuadro de diálogo de confirmación
            int response = JOptionPane.showConfirmDialog(
                    null,
                    "¿Estás seguro de que deseas guardar los datos?",
                    "Confirmar Guardado",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            // Si el usuario confirma la acción, proceder con el guardado
            if (response == JOptionPane.YES_OPTION) {
                try {
                    // Ruta del archivo de texto en la raíz del proyecto
                    String filePath = "ingreso_FlujoCaja.txt";
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
                    JOptionPane.showMessageDialog(
                            null,
                            "Los datos se han guardado correctamente en el archivo.",
                            "Guardado Exitoso",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    this.dispose();
                    Flujo_Caja.btn_cargar.doClick();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    // Mostrar un mensaje de error si ocurre un problema al guardar los datos
                    JOptionPane.showMessageDialog(
                            null,
                            "Se ha producido un error al guardar los datos.",
                            "Error al Guardar",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(EstructuraFEfectivo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EstructuraFEfectivo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EstructuraFEfectivo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EstructuraFEfectivo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                EstructuraFEfectivo dialog = new EstructuraFEfectivo(new javax.swing.JFrame(), true);
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
