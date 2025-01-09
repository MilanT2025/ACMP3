/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista;

import Controlador.Conexion;
import com.formdev.flatlaf.FlatIntelliJLaf;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.oxbow.swingbits.table.filter.TableRowFilterSupport;

/**
 *
 * @author Farmacia-Ingeniero
 */
public class ProcesarCuentasporPagar extends javax.swing.JDialog {

    private final Modelo modelo = new Modelo();

    private Color originalAlternateRowColor;
    
    Locale locale;
    DecimalFormatSymbols symbols;

    /**
     * Creates new form Detallado
     */
    public ProcesarCuentasporPagar(java.awt.Frame parent, boolean modal, String fecha) {
        super(parent, modal);
        initComponents();
        
        locale = new Locale("es", "PE");
        Locale.setDefault(locale);
        
        symbols = new DecimalFormatSymbols(locale);
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        
        jLabel13.setText("CUENTAS POR PAGAR AL " + fecha);
        this.setLocationRelativeTo(null);
        
        modelo.addColumn("N°"); //0
        modelo.addColumn("RUC"); //1
        modelo.addColumn("PROVEEDOR"); //2
        modelo.addColumn("FACT"); //3
        modelo.addColumn("FEC/E"); //4
        modelo.addColumn("FEC/V"); //5
        modelo.addColumn("MONTO"); //6
        modelo.addColumn("OPCION"); //7
        
        agregarColumnasPorcentaje();
        modelo.addColumn("TOTAL");
        modelo.addColumn("RUBRO");
        modelo.addColumn("MANUAL");
        
        JTableHeader header = tb_data.getTableHeader();
        header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 40));
        header.setBackground(new java.awt.Color(0, 102, 153));
        header.setForeground(new java.awt.Color(255, 255, 255));
        tb_data.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

        TableRowFilterSupport.forTable(tb_data).searchable(true).actions(true).useTableRenderers(true).apply();

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tb_data.getModel());
        tb_data.setRowSorter(sorter);
        

        originalAlternateRowColor = (Color) UIManager.getLookAndFeelDefaults().get("Table.alternateRowColor");

        if (originalAlternateRowColor == null) {
            originalAlternateRowColor = UIManager.getColor("Table.alternateRowColor");
        }

        UIManager.getLookAndFeelDefaults().put("Table.alternateRowColor", new Color(254, 238, 184));

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (originalAlternateRowColor == null) {
                    UIManager.getLookAndFeelDefaults().remove("Table.alternateRowColor");
                } else {
                    UIManager.getLookAndFeelDefaults().put("Table.alternateRowColor", originalAlternateRowColor);
                }
            }
        });
        
       modelo.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();

                // Detectar si se modificó la columna 12
                if (column == 7) { // Índice 11 porque las columnas empiezan en 0
                    if (modelo.getValueAt(row, column) == null) {
                        return;
                    }
                    
                    String value = (String) modelo.getValueAt(row, column);
                    
                    if (value.equals("")) {
                        for (int i = 8; i < tb_data.getColumnCount()-2; i++) {
                            tb_data.setValueAt(null, row, i);
                        }
                        return;
                    }
                    
                    // Validar que el valor no sea nulo y tenga formato de porcentaje (por ejemplo, "3%")
                    if (value != null) {
                        for (int i = 8; i < tb_data.getColumnCount()-2; i++) {
                            tb_data.setValueAt(null, row, i);
                        }

                        // Buscar la columna cuyo encabezado contiene el porcentaje dentro de "[]"
                        int targetColumn = -1;
                        for (int col = 0; col < modelo.getColumnCount(); col++) {
                            String header = modelo.getColumnName(col);
                            if (header.equals(value)) {
                                targetColumn = col;
                                break;
                            }
                        }
                        
                        String valor = modelo.getColumnName(targetColumn);

                        // Expresión regular para buscar un número con % dentro de corchetes
                        Pattern pattern = Pattern.compile("\\[(\\d+(\\.\\d+)?%)\\]");
                        Matcher matcher = pattern.matcher(valor);
                        
                        String porcentaje2 = null;
                        if (matcher.find()) {
                            porcentaje2 = matcher.group(1); // Extrae el contenido dentro de los corchetes
                        } else {
                            System.out.println("No se encontró ningún porcentaje dentro de corchetes.");
                            return;
                        }
                        
                         double percentage = Double.parseDouble(porcentaje2.replace("%", "")) / 100.0;

                        // Si se encuentra la columna objetivo, realizar el cálculo
                        if (targetColumn != -1) {
                            try {
                                double col6Value = Double.parseDouble(modelo.getValueAt(row, 6).toString());
                                double result = col6Value * percentage;
                                if (tb_data.getColumnName(targetColumn).contains("RET")) {
                                    modelo.setValueAt(result, row, targetColumn);
                                    modelo.setValueAt(col6Value - result, row, tb_data.getColumnCount() - 3);
                                } else if (tb_data.getColumnName(targetColumn).contains("DET")) {
                                    BigDecimal bdResult = new BigDecimal(result);
                                    bdResult = bdResult.setScale(0, RoundingMode.HALF_UP);
                                    modelo.setValueAt(bdResult, row, targetColumn);
                                    modelo.setValueAt(col6Value - bdResult.intValue(), row, tb_data.getColumnCount() - 3);
                                }else if (tb_data.getColumnName(targetColumn).contains("NO APLICA")) {
                                    modelo.setValueAt(col6Value, row, tb_data.getColumnCount() - 3);
                                }
                                
                                
                            } catch (NumberFormatException ex) {
                                System.err.println("Error en formato numérico: " + ex.getMessage());
                            }
                        }
                    }
                }
            }
        });
       
        DecimalFormat decimalFormat = new DecimalFormat("0.00", symbols);

        int columnCount = tb_data.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            if (i == 6 || (i >= 7 && i <= modelo.getColumnCount()-2)) {
                tb_data.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
                    @Override
                    public void setValue(Object value) {
                        if (value instanceof Number) {
                            value = decimalFormat.format(value);
                        }
                        super.setValue(value);
                    }
                });
            }
        }

        configurarComboBoxEnTabla();
       
        ocultarColumnaAncho(tb_data, modelo.getColumnCount()-1);
    }
    
    private JComboBox<String> obtenerComboBoxDesdeBaseDeDatos() {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.addItem("");
        try (Connection con = Conexion.getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery("SELECT NombreColumna FROM CuentasPorPagarValores ORDER BY Posicion")) {
            while (rs.next()) {
                comboBox.addItem(rs.getString(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CuentasporPagar.class.getName()).log(Level.SEVERE, null, ex);
        }
        return comboBox;
    }
    
    private void configurarComboBoxEnTabla() {
        // Crear el JComboBox desde la base de datos
        JComboBox<String> comboBox = obtenerComboBoxDesdeBaseDeDatos();

        // Editor de celdas
        DefaultCellEditor cellEditor = new DefaultCellEditor(comboBox);
        tb_data.getColumnModel().getColumn(7).setCellEditor(cellEditor); // Cambia el índice según tu columna

        // Renderer para mantener colores de intercalación y mostrar valores seleccionados correctamente
        tb_data.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Aplicar colores alternos desde el UIManager
                if (!isSelected) {
                    Color alternateColor = UIManager.getColor("Table.alternateRowColor");
                    if (alternateColor != null && row % 2 != 0) {
                        c.setBackground(alternateColor);
                    } else {
                        c.setBackground(UIManager.getColor("Table.background"));
                    }
                } else {
                    c.setBackground(table.getSelectionBackground());
                }

                // Mostrar el valor seleccionado como texto
                if (value != null) {
                    setText(value.toString());
                }
                return c;
            }
        });
    }
    
    public void ocultarColumnaAncho(JTable tabla, int columnaIndex) {
        tabla.getColumnModel().getColumn(columnaIndex).setMinWidth(0);
        tabla.getColumnModel().getColumn(columnaIndex).setMaxWidth(0);
        tabla.getColumnModel().getColumn(columnaIndex).setWidth(0);
    }

    private void guardarInformacion() {
        try {
            Connection con = Conexion.getConnection();
            String sql = "INSERT INTO [dbo].[CuentasPorPagarDiario] " +
             "([N°], [RUC], [Proveedor], [Factura], [FechaEmision], [FechaVencimiento], [Monto], [Porcentaje], " +
             "[ValorCalculado], [Total], [Rubro], [FechaEvaluacion], [EsManual]) " +
             "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), ?)";
            PreparedStatement pstm = con.prepareStatement(sql);
            for (int i = 0; i < tb_data.getRowCount(); i++) {
                double total = 0;
                if (tb_data.getValueAt(i, 7) != null && !tb_data.getValueAt(i, 7).toString().trim().equals("")) {
                    pstm.setInt(1, (int) tb_data.getValueAt(i, 0));
                    pstm.setString(2, tb_data.getValueAt(i, 1).toString());
                    pstm.setString(3, tb_data.getValueAt(i, 2).toString());
                    pstm.setString(4, tb_data.getValueAt(i, 3).toString());
                    pstm.setString(5, tb_data.getValueAt(i, 4).toString());
                    pstm.setString(6, tb_data.getValueAt(i, 5).toString());
                    pstm.setDouble(7, Double.parseDouble(tb_data.getValueAt(i, 6).toString()));
                    pstm.setString(8, tb_data.getValueAt(i, 7).toString());
                    
                    int totalColumnIndex = tb_data.getColumnCount() - 3; 

                    for (int j = 8; j < totalColumnIndex; j++) {
                        total += parseDoubleOrDefault(tb_data.getValueAt(i, j));
                    }
                    pstm.setDouble(9, total);
                    pstm.setString(10, getStringOrDefault(tb_data.getValueAt(i, tb_data.getColumnCount()-3), ""));
                    pstm.setString(11, getStringOrDefault(tb_data.getValueAt(i, tb_data.getColumnCount()-2), ""));
                    pstm.setBoolean(12, Boolean.parseBoolean(tb_data.getValueAt(i, tb_data.getColumnCount()-1).toString()));
                   
                    pstm.executeUpdate();
                }
            }
            JOptionPane.showMessageDialog(this, "Se han guardado los datos correctamente");
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(ProcesarCuentasporPagar.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private double parseDoubleOrDefault(Object value) {
        if (value == null || value.toString().trim().isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            return 0.0; // Si el valor no es convertible a número, se usa 0 como valor predeterminado
        }
    }
    
   private String getStringOrDefault(Object value, String defaultValue) {
    return value == null ? defaultValue : value.toString();
}

    private void agregarColumnasPorcentaje() {
        try {
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            
            con = Conexion.getConnection();

            String sql = "SELECT NombreColumna FROM CuentasPorPagarValores ORDER BY Posicion";
            ps = con.prepareStatement(sql);

            rs = ps.executeQuery();

            while (rs.next()) {
                modelo.addColumn(rs.getString(1));
            }

            rs.close();
            ps.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(ProcesarCuentasporPagar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public class Modelo extends DefaultTableModel {

        @Override
        public boolean isCellEditable(int row, int column) {
            int lastColumnIndex = this.getColumnCount() - 2;

            // Permitir edición solo en la columna 7 (índice 6) y la última columna
            if (column == 7 || column == lastColumnIndex) {
                return true;
            }

            // Deshabilitar edición para otras columnas
            return false;
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            // Validar columnas 5, 7 a penúltima
            if (column == 6 || (column >= 8 && column < this.getColumnCount() - 2)) {
                try {
                    if (aValue == null || aValue.toString().trim().isEmpty()) {
                        super.setValueAt(null, row, column);
                    } else {
                        // Validar si el valor es decimal o entero
                        if (aValue.toString().contains(".")) {
                            double doubleValue = Double.parseDouble(aValue.toString().trim());
                            super.setValueAt(doubleValue, row, column);
                        } else {
                            int intValue = Integer.parseInt(aValue.toString().trim());
                            super.setValueAt(intValue, row, column);
                        }
                    }
                } catch (NumberFormatException ex) {
                    javax.swing.JOptionPane.showMessageDialog(null,
                            "Por favor ingrese un número válido (entero o decimal) en la columna.",
                            "Error de entrada",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // Permitir otros valores sin restricciones en las demás columnas
                super.setValueAt(aValue, row, column);
            }
        }
    }
    
    public void cargarInfo(Object[] rowData){
        modelo.addRow(rowData);
    }
    
    private void cargarRubro() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = Conexion.getConnection();

            // Crear el PreparedStatement para evitar múltiples declaraciones
            String sql = "SELECT TOP 1 DESCRIPCION FROM cuenta33 WHERE Tipo+'-'+[Serie Nº]+'-'+[Numero] = ?";
            ps = con.prepareStatement(sql);

            // Iterar sobre las filas de la tabla
            for (int i = 0; i < tb_data.getRowCount(); i++) {
                String clave = tb_data.getValueAt(i, 3).toString();

                // Asignar el valor al parámetro de la consulta
                ps.setString(1, clave);
                rs = ps.executeQuery();

                // Actualizar la tabla con el resultado
                if (rs.next()) {
                    tb_data.setValueAt(rs.getString("DESCRIPCION"), i, tb_data.getColumnCount() - 2);
                }

                // Cerrar el ResultSet para evitar fugas de recursos
                if (rs != null) {
                    rs.close();
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(ProcesarCuentasporPagar.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Cerrar todos los recursos en el bloque finally
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ProcesarCuentasporPagar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


    public void packColumns() {
        cargarRubro();
        
        TableColumnModel columnModel = tb_data.getColumnModel();
        for (int columnIndex = 0; columnIndex < columnModel.getColumnCount(); columnIndex++) {
            TableColumn column = columnModel.getColumn(columnIndex);

            TableCellRenderer headerRenderer = tb_data.getTableHeader().getDefaultRenderer();
            Object headerValue = column.getHeaderValue();
            Component headerComponent = headerRenderer.getTableCellRendererComponent(tb_data, headerValue, false, false, -1, columnIndex);
            int headerWidth = headerComponent.getPreferredSize().width;

            int maxCellWidth = headerWidth;
            for (int rowIndex = 0; rowIndex < tb_data.getRowCount(); rowIndex++) {
                TableCellRenderer cellRenderer = tb_data.getCellRenderer(rowIndex, columnIndex);
                Component cellComponent = tb_data.prepareRenderer(cellRenderer, rowIndex, columnIndex);
                int cellWidth = cellComponent.getPreferredSize().width;
                maxCellWidth = Math.max(maxCellWidth, cellWidth);
            }

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

        jFileChooser1 = new javax.swing.JFileChooser();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_data = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        btnAgregar = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Procesamiento de Informacion - Cuentas por Pagar");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        tb_data.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        tb_data.setModel(modelo);
        tb_data.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tb_data.setRowHeight(35);
        tb_data.setShowHorizontalLines(true);
        tb_data.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tb_dataKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tb_data);

        jButton1.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_add_32px.png"))); // NOI18N
        jButton1.setText("Guardar y Exportar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(0, 102, 153));

        jLabel13.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("CUENTAS POR PAGAR AL 18/09/2024");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnAgregar.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_add_32px.png"))); // NOI18N
        btnAgregar.setText("Agregar Registro Manual");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1250, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAgregar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );

        setJMenuBar(jMenuBar1);

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
         if (tb_data.isEditing()) {
            tb_data.getCellEditor().stopCellEditing(); // Detiene la edición actual si está en curso
        }
         
        boolean verificarRegistro = false;

        for (int i = 0; i < tb_data.getRowCount(); i++) {
            Object valorTotal = tb_data.getValueAt(i, tb_data.getColumnCount() - 3);
            if (valorTotal != null && !valorTotal.toString().trim().isEmpty()) {
                verificarRegistro = true; // Hay al menos un valor en la columna
                break;
            }
        }

        if (!verificarRegistro) {
            JOptionPane.showMessageDialog(this, "Debe existir al menos un registro con RET/DET seleccionado en OPCION.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        } 
         
        if (tb_data.getRowCount() == 0) {
            JOptionPane.showMessageDialog(
                    null,
                    "La tabla debe contener al menos un registro",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;

        }
        
        int opcion = JOptionPane.showConfirmDialog(
            null,
            "¿Deseas guardar la información?",
            "Confirmación",
            JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {
            guardarInformacion();

            DefaultTableModel modelo = (DefaultTableModel) tb_data.getModel();

            if (modelo.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "No hay datos para exportar.");
                return;
            }

             try {
                 exportToExcel(tb_data);
             } catch (IOException ex) {
                 Logger.getLogger(ProcesarCuentasporPagar.class.getName()).log(Level.SEVERE, null, ex);
             }

            CuentasporPagar.jButton1.doClick();
            this.dispose();
        } 
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        AgregarRegManual ini = new AgregarRegManual(null, true);
        ini.setVisible(true);
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void tb_dataKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tb_dataKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            int selectedRow = tb_data.getSelectedRow();
            if (selectedRow != -1) { // Verificar que hay una fila seleccionada
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "¿Desea eliminar el registro seleccionado?",
                        "Aviso",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    // Eliminar la fila seleccionada del modelo
                    modelo.removeRow(selectedRow);
                }
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "No hay ninguna fila seleccionada.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }//GEN-LAST:event_tb_dataKeyPressed

    public void exportToExcel(JTable table) throws IOException {
        // Crear el JFileChooser para seleccionar el archivo
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar archivo Excel");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            // Asegurarse de que el archivo tenga la extensión .xlsx
            if (!filePath.toLowerCase().endsWith(".xlsx")) {
                filePath += ".xlsx";
            }

            // Crear el workbook y la hoja
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Previo");

            // Configurar estilos
            Font headerFont = workbook.createFont();
            headerFont.setFontName("Calibri");
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 11);

            Font normalFont = workbook.createFont();
            normalFont.setFontName("Calibri");
            normalFont.setFontHeightInPoints((short) 11);

            CellStyle boldStyle = workbook.createCellStyle();
            boldStyle.setFont(headerFont);
            boldStyle.setAlignment(HorizontalAlignment.CENTER);
            boldStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            CellStyle normalStyle = workbook.createCellStyle();
            normalStyle.setFont(normalFont);

            CellStyle numberStyle = workbook.createCellStyle();
            numberStyle.setFont(normalFont);
            numberStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

            CellStyle integerStyle = workbook.createCellStyle();
            integerStyle.setFont(normalFont);
            integerStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));

            CellStyle boldNumberStyle = workbook.createCellStyle();
            boldNumberStyle.setFont(headerFont);
            boldNumberStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

            // Zoom al 120%
            sheet.setZoom(120);

            // Obtener la cantidad de columnas del JTable
            int columnCount = table.getColumnCount();

            // Agregar título en la fila 0 y combinar celdas
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("ASOCIACION CIRCULO MILITAR DEL PERU");
            titleCell.setCellStyle(boldStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnCount - 1));

            // Agregar subtítulo en la fila 1 y combinar celdas
            Row subtitleRow = sheet.createRow(1);
            Cell subtitleCell = subtitleRow.createCell(0);
            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            subtitleCell.setCellValue("CUENTAS POR PAGAR AL \"" + currentDate + "\"");
            subtitleCell.setCellStyle(boldStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, columnCount - 1));

            // Crear una fila en blanco (fila 2)
            sheet.createRow(2);

            // Extraer encabezados del JTable
            TableModel model = table.getModel();
            Row headerRow = sheet.createRow(3);
            for (int col = 0; col < columnCount; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(model.getColumnName(col));
                cell.setCellStyle(boldStyle);
            }

            // Mapear proveedores y acumular totales para columnas relevantes
            Map<String, Double[]> providerSums = new HashMap<>();

            // Extraer datos del JTable
            int rowCount = model.getRowCount();
            int currentRow = 4; // Fila inicial para datos
            for (int row = 0; row < rowCount; row++) {
                Row excelRow = sheet.createRow(currentRow++); // Comenzar desde la fila 4
                String provider = model.getValueAt(row, 2).toString(); // Columna 3 (Índice 2)
                
                for (int col = 0; col < columnCount; col++) {
                    Cell cell = excelRow.createCell(col);
                    Object value = model.getValueAt(row, col);

                    // Detectar si la columna es la 6, 8 o posterior hasta la penúltima
                    if ((col == 6 || col >= 8 && col < columnCount - 1) && value != null) {
                        try {
                            if (value.toString().contains(".")) {
                                double numericValue = Double.parseDouble(value.toString());
                                cell.setCellValue(numericValue);
                                cell.setCellStyle(numberStyle);
                            } else {
                                int numericValue = Integer.parseInt(value.toString());
                                cell.setCellValue(numericValue);
                                cell.setCellStyle(integerStyle);
                            }
                        } catch (NumberFormatException e) {
                            cell.setCellValue(value.toString()); // Si no es número, exportarlo como texto
                            cell.setCellStyle(normalStyle);
                        }
                    } else {
                        cell.setCellValue(value != null ? value.toString() : "");
                        cell.setCellStyle(normalStyle);
                    }
                }

                // Acumular totales por proveedor para columnas desde la 7 (índice 6) hasta la penúltima
                for (int col = 6; col < columnCount - 1; col++) {
                    // Saltar la columna 8 (índice 7)
                    if (col == 7) {
                        continue;
                    }

                    double columnValue = 0;
                    try {
                        columnValue = Double.parseDouble(model.getValueAt(row, col).toString());
                    } catch (NumberFormatException | NullPointerException e) {
                        // Ignorar valores no numéricos
                    }
                    providerSums.putIfAbsent(provider, new Double[columnCount - 6]);
                    if (providerSums.get(provider)[col - 6] == null) {
                        providerSums.get(provider)[col - 6] = 0.0;
                    }
                    providerSums.get(provider)[col - 6] += columnValue;
                }

                // Si cambia de proveedor o es la última fila, agregar fila de totales
                boolean isLastRow = row == rowCount - 1;
                String nextProvider = isLastRow ? null : model.getValueAt(row + 1, 2).toString();
                if (isLastRow || !provider.equals(nextProvider)) {
                    Row summaryRow = sheet.createRow(currentRow++);
                    Cell providerCell = summaryRow.createCell(2); // Columna de proveedor
                    providerCell.setCellValue("Total " + provider);
                    providerCell.setCellStyle(boldStyle);

                    for (int col = 6; col < columnCount - 1; col++) {
                        // Saltar la columna 8 (índice 7)
                        if (col == 7) {
                            continue;
                        }

                        Cell sumCell = summaryRow.createCell(col);
                        sumCell.setCellValue(providerSums.get(provider)[col - 6]);
                        sumCell.setCellStyle(boldNumberStyle);
                    }

                    // Agregar una fila en blanco
                    sheet.createRow(currentRow++);

                    // Repetir los encabezados
                    Row repeatedHeaderRow = sheet.createRow(currentRow++);
                    for (int col = 0; col < columnCount; col++) {
                        Cell cell = repeatedHeaderRow.createCell(col);
                        cell.setCellValue(model.getColumnName(col));
                        cell.setCellStyle(boldStyle);
                    }

                    // Resetear acumulador para el siguiente proveedor
                    providerSums.remove(provider);
                }
            }

            // Autoajustar las columnas
            for (int col = 0; col < columnCount; col++) {
                sheet.autoSizeColumn(col);
            }

            // Escribir el archivo
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }

            // Cerrar el workbook
            workbook.close();

            JOptionPane.showMessageDialog(null, "Archivo guardado exitosamente en: " + filePath);
        } else {
            JOptionPane.showMessageDialog(null, "Exportación cancelada.");
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
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Failed to initialize LaF");
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ProcesarCuentasporPagar dialog = new ProcesarCuentasporPagar(new javax.swing.JFrame(), true, null);
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
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton jButton1;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTable tb_data;
    // End of variables declaration//GEN-END:variables
}
