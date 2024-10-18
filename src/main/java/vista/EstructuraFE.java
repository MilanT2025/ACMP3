/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista;

import Controlador.Conexion;
import com.formdev.flatlaf.FlatIntelliJLaf;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Farmacia-Ingeniero
 */
public class EstructuraFE extends javax.swing.JDialog {

    private final Modelo modelo = new Modelo();

    /**
     * Creates new form EstructuraFE
     *
     * @param parent
     * @param modal
     */
    public EstructuraFE(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        Rectangle screenBounds = gc.getBounds();
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);

        int screenWidth = screenBounds.width - screenInsets.left - screenInsets.right;
        int screenHeight = screenBounds.height - screenInsets.top - screenInsets.bottom;

        this.setSize(screenWidth, screenHeight);

        this.setLocation(screenInsets.left, screenInsets.top);

        llenar_tabla();
    }

    public class Modelo extends DefaultTableModel {

        @Override
        public boolean isCellEditable(int row, int column) {
            return true;
        }
        
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0 || columnIndex == 7) {
                return Integer.class;
            } else {
                return Object.class;
            }
        }
    }

    private void llenar_tabla() {

        try {
            modelo.addColumn("POSICIÓN (NUMERICO)");
            modelo.addColumn("CONCEPTO (ALFANUMERICO)");
            modelo.addColumn("TIPO ARCHIVO (SELECCION)");
            modelo.addColumn("COLOR (HEXADECIMAL)");
            modelo.addColumn("ES NEGRITA (SELECCION)");
            modelo.addColumn("TIENE VALOR 0 (SELECCION)");
            modelo.addColumn("SUMA LAS POSICIONES (NUMERICO SEPARADO POR ,)");
            modelo.addColumn("RESTA UNA POSICION ESPECIFICA (SOLO UN NUMERO)");
            modelo.addColumn("SE SUMAN EL TOTAL DE COLUMNAS (SELECCION)");
            modelo.addColumn("QUERY BD (ALFANUMERICO)");
            modelo.addColumn("DESCRIPCION QUERY (ALFANUMERICO)");
            modelo.addColumn("CAMBIA EL SIGNO RESULTANTE +/- (SELECCION)");
            modelo.addColumn("GUARDA DATOS DEL BOTON CIERRE MES (SELECCION)");
            modelo.addColumn("ES INICIO DE PERIODO (SELECCION)");
            modelo.addColumn("ES CIERRE DE PERIODO (SELECCION)");

            TableColumnModel colModel = tb_resultado.getColumnModel();

            for (int i = 0; i < colModel.getColumnCount(); i++) {
                colModel.getColumn(i).setHeaderRenderer(new MultiLineHeaderRenderer());
                colModel.getColumn(i).setCellRenderer(new CenteredCellRenderer(i));
            }

            String[] data = new String[15];
            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "SELECT "
                    + "		[Posicion] "
                    + "      ,[Concepto] "
                    + "      ,[Tipo] "
                    + "      ,[Color_Fila] "
                    + "      ,CASE WHEN [Fila_Negrita] = 0 THEN 'NO' ELSE 'SI' END AS [Fila_Negrita] "
                    + "      ,CASE WHEN [Valor_0] = 0 THEN 'NO' ELSE 'SI' END AS [Valor_0] "
                    + "      ,[Suma_Posiciones] "
                    + "      ,[Suma_Fila] "
                    + "      ,CASE WHEN [Suma_Total] = 0 THEN 'NO' ELSE 'SI' END AS [Suma_Total] "
                    + "      ,[Query_BD] "
                    + "      ,[Descripcion_Query] "
                    + "      ,CASE WHEN [Cambia_Signo] = 0 THEN 'NO' ELSE 'SI' END AS [Cambia_Signo] "
                    + "      ,CASE WHEN [Boton_CierreMes] = 0 THEN 'NO' ELSE 'SI' END AS [Boton_CierreMes] "
                    + "      ,CASE WHEN [Inicio_Periodo] = 0 THEN 'NO' ELSE 'SI' END AS [Inicio_Periodo] "
                    + "      ,CASE WHEN [Cierre_Periodo] = 0 THEN 'NO' ELSE 'SI' END AS [Cierre_Periodo] "
                    + "  FROM [dbo].[EstructuraFlujoEfectivo] "
                    + " "
                    + "  ORDER BY "
                    + "	Posicion";
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                for (int i = 0; i < data.length; i++) {
                    data[i] = rs.getString(i + 1);
                }
                modelo.addRow(data);
            }

            rs.close();
            st.close();
            con.close();

            tb_resultado.setGridColor(Color.LIGHT_GRAY);
            tb_resultado.setSelectionBackground(new Color(100, 149, 237));
            tb_resultado.setSelectionForeground(Color.WHITE);

            tb_resultado.getColumnModel().getColumn(2).setCellEditor(new CustomComboBoxEditor2());
            tb_resultado.getColumnModel().getColumn(3).setCellEditor(new ColorEditor());
            tb_resultado.getColumnModel().getColumn(4).setCellEditor(new CustomComboBoxEditor());
            tb_resultado.getColumnModel().getColumn(5).setCellEditor(new CustomComboBoxEditor());
            tb_resultado.getColumnModel().getColumn(8).setCellEditor(new CustomComboBoxEditor());
            tb_resultado.getColumnModel().getColumn(11).setCellEditor(new CustomComboBoxEditor());
            tb_resultado.getColumnModel().getColumn(12).setCellEditor(new CustomComboBoxEditor());
            tb_resultado.getColumnModel().getColumn(13).setCellEditor(new CustomComboBoxEditor());
            tb_resultado.getColumnModel().getColumn(14).setCellEditor(new CustomComboBoxEditor());
            
            modelo.addTableModelListener(new TableModelListener() {
                @Override
                public void tableChanged(TableModelEvent e) {
                    if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 0) {
                        ordenarFilas();
                    }
                }
            });

            packColumns(tb_resultado);

        } catch (SQLException ex) {
            Logger.getLogger(EstructuraFE.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    private void ordenarFilas() {
        // Convertir las filas a un array de arrays para ordenar
        Object[][] rows = new Object[modelo.getRowCount()][];
        for (int i = 0; i < modelo.getRowCount(); i++) {
            rows[i] = new Object[modelo.getColumnCount()];
            for (int j = 0; j < modelo.getColumnCount(); j++) {
                if (j == 0) {
                    // Si estamos en la columna 0, convertir el valor a Integer
                    rows[i][j] = Integer.parseInt(modelo.getValueAt(i, j).toString());
                } else {
                    // De lo contrario, mantener el valor como está
                    rows[i][j] = modelo.getValueAt(i, j);
                }
            }
        }

        // Ordenar el array de filas según el número correlativo (columna 0)
        Arrays.sort(rows, Comparator.comparingInt(a -> (Integer) a[0]));

        // Limpiar el modelo
        modelo.setRowCount(0);

        // Agregar las filas ordenadas al modelo
        for (Object[] row : rows) {
            modelo.addRow(row);
        }
    }

    public class MultiLineHeaderRenderer extends JLabel implements TableCellRenderer {

        public MultiLineHeaderRenderer() {
            setOpaque(true);
            setFont(new Font("Roboto", Font.BOLD, 12));
            setBackground(new Color(0, 102, 153));
            setBorder(BorderFactory.createLineBorder(Color.GRAY));
            setForeground(Color.WHITE);
            setPreferredSize(new Dimension(100, 50));
            setHorizontalAlignment(SwingConstants.CENTER);
            setVerticalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("<html><div style='text-align: center;'>" + (value != null ? value.toString() : "") + "</div></html>");
            return this;
        }
    }

    public class CenteredCellRenderer extends DefaultTableCellRenderer {

        public CenteredCellRenderer(int columna) {
            setOpaque(true);
            if (columna != 1) {
                setHorizontalAlignment(SwingConstants.CENTER);
            } else {
                setHorizontalAlignment(SwingConstants.LEFT);
            }

        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
             Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        String hexColor = (String) table.getValueAt(row, 3);
        String isBold = (String) table.getValueAt(row, 4); // Obtener el valor de la columna 4

        try {
            Color color = Color.decode(hexColor);

            // Verificar si el valor en la columna 4 es "SI"
            if (isBold.equals("SI")) {
                if (!isSelected) {
                    cellComponent.setBackground(color);
                } else {
                    cellComponent.setBackground(table.getSelectionBackground());
                }
                // Aplicar el estilo de fuente negrita a toda la fila
                cellComponent.setFont(cellComponent.getFont().deriveFont(Font.BOLD));
            } else {
                if (!isSelected) {
                    cellComponent.setBackground(color);
                } else {
                    cellComponent.setBackground(table.getSelectionBackground());
                }
                // Restaurar el estilo de fuente normal de toda la fila
                cellComponent.setFont(cellComponent.getFont().deriveFont(Font.PLAIN));
            }
        } catch (NumberFormatException e) {
            cellComponent.setBackground(table.getBackground());
        }

        return cellComponent;
        }
    }

    private void packColumns(JTable table) {
        TableColumnModel columnModel = table.getColumnModel();
        for (int columnIndex = 0; columnIndex < columnModel.getColumnCount(); columnIndex++) {
            TableColumn column = columnModel.getColumn(columnIndex);
            TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
            Object headerValue = column.getHeaderValue();
            Component headerComponent = headerRenderer.getTableCellRendererComponent(table, headerValue, false, false, -1, columnIndex);
            int headerWidth = headerComponent.getPreferredSize().width;
            int maxCellWidth = headerWidth;

            if (columnIndex == 9) {
                column.setMinWidth(200);
            } else {
                for (int rowIndex = 0; rowIndex < table.getRowCount(); rowIndex++) {
                    TableCellRenderer cellRenderer = table.getCellRenderer(rowIndex, columnIndex);
                    Component cellComponent = table.prepareRenderer(cellRenderer, rowIndex, columnIndex);
                    int cellWidth = cellComponent.getPreferredSize().width;
                    maxCellWidth = Math.max(maxCellWidth, cellWidth);
                }
                column.setMinWidth(maxCellWidth);
            }
        }
    }

    class CustomComboBoxEditor extends DefaultCellEditor {

        private JComboBox<String> comboBox;

        public CustomComboBoxEditor() {
            super(new JComboBox<String>(new String[]{"SI", "NO"}));
            comboBox = (JComboBox<String>) getComponent();
            comboBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value != null) {
                comboBox.setSelectedItem(value);
            }
            return comboBox;
        }

        @Override
        public Object getCellEditorValue() {
            return comboBox.getSelectedItem().equals("SI") ? "SI" : "NO";
        }
    }

    class CustomComboBoxEditor2 extends DefaultCellEditor {

        private JComboBox<String> comboBox;

        public CustomComboBoxEditor2() {
            super(new JComboBox<String>(new String[]{"INGRESO", "EGRESO", "CUENTA33", ""}));
            comboBox = (JComboBox<String>) getComponent();
            comboBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value != null) {
                comboBox.setSelectedItem(value);
            }
            return comboBox;
        }

        @Override
        public Object getCellEditorValue() {
            return comboBox.getSelectedItem(); // Devuelve el valor seleccionado del JComboBox
        }
    }

    class ColorEditor extends AbstractCellEditor implements TableCellEditor {

        private JPanel panel;
        private Color currentColor;

        public ColorEditor() {
            panel = new JPanel();
            panel.setOpaque(true);
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Color newColor = JColorChooser.showDialog(panel, "Elige un Color", currentColor);
                    if (newColor != null) {
                        currentColor = newColor;
                        panel.setBackground(currentColor);
                        fireEditingStopped();
                    }
                }
            });
        }

        @Override
        public Object getCellEditorValue() {
            return String.format("#%06X", (currentColor.getRGB() & 0xFFFFFF));
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value != null) {
                try {
                    currentColor = Color.decode(value.toString());
                    panel.setBackground(currentColor);
                } catch (NumberFormatException e) {
                    panel.setBackground(Color.WHITE); // Fallback to white on error
                }
            } else {
                panel.setBackground(Color.WHITE);
            }

            if (isSelected) {
                panel.setForeground(table.getSelectionForeground());
            } else {
                panel.setForeground(table.getForeground());
            }

            return panel;
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_resultado = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 102, 102), 2, true), "Editable", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 12))); // NOI18N

        tb_resultado.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        tb_resultado.setModel(modelo);
        tb_resultado.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tb_resultado.setCellSelectionEnabled(true);
        tb_resultado.setGridColor(new java.awt.Color(102, 102, 102));
        tb_resultado.setRowHeight(35);
        tb_resultado.setShowGrid(true);
        tb_resultado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_resultadoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tb_resultado);

        jLabel7.setFont(new java.awt.Font("Bahnschrift", 0, 13)); // NOI18N
        jLabel7.setText("<html>Al <b>DIA</b> del <b>MES AÑO</b></html>");

        jLabel2.setFont(new java.awt.Font("Bahnschrift", 1, 13)); // NOI18N
        jLabel2.setText("ASOCIACIÓN CÍRCULO MILITAR DEL PERÚ");

        jLabel3.setFont(new java.awt.Font("Bahnschrift", 1, 13)); // NOI18N
        jLabel3.setText("Dirección de Recreación");

        jLabel6.setFont(new java.awt.Font("Bahnschrift", 0, 13)); // NOI18N
        jLabel6.setText("Estado de Flujos de Efectivo");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1256, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(0, 102, 153));

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 15)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Estructura de Flujo de Efectivo");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 0, 0), 1, true), "Opciones", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 12))); // NOI18N

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

        jButton5.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Save.png"))); // NOI18N
        jButton5.setText("Guardar Estructura");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int valor = 0;
        
        for (int i = 0; i < tb_resultado.getRowCount(); i++) {
            valor = Integer.parseInt(tb_resultado.getValueAt(i, 0).toString()) + 1;
        }
        
        modelo.addRow(new Object[]{valor, "", "", "#FFFFFF", "NO", "NO", "", "", "NO", "", "", "NO", "NO", "NO", "NO"});
        
        int indiceFila = tb_resultado.getRowCount() - 1;

        SwingUtilities.invokeLater(() -> {
            tb_resultado.setRowSelectionInterval(indiceFila, indiceFila);
            tb_resultado.setColumnSelectionInterval(1, 1); 
            tb_resultado.scrollRectToVisible(tb_resultado.getCellRect(indiceFila, 0, true));
        });
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

    private void tb_resultadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_resultadoMouseClicked

    }//GEN-LAST:event_tb_resultadoMouseClicked

    private boolean verificarUnSiPorColumnasEspecificas(JTable table, int[] columnas) {
        for (int col : columnas) {
            int count = 0;
            for (int row = 0; row < table.getRowCount(); row++) {
                Object value = table.getValueAt(row, col);
                if (value != null && value.toString().equalsIgnoreCase("SI")) {
                    count++;
                }
            }
            if (count > 1) {
                String columnName = table.getColumnName(col);
                JOptionPane.showMessageDialog(
                        null,
                        "La columna '" + columnName + "' tiene múltiples 'SI'. Solo se permite un 'SI' por columna.",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE
                );
                return false;
            }
        }
        return true;
    }
    
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        int[] columnasAVerificar = {12, 13, 14};

        if (!verificarUnSiPorColumnasEspecificas(tb_resultado, columnasAVerificar)) {
            return; 
        }
        
        try {
            int respuesta = JOptionPane.showConfirmDialog(
                    this,
                    "¿Está seguro de que desea guardar la estructura de flujo de efectivo?",
                    "Confirmación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (respuesta == JOptionPane.YES_OPTION) {
                Connection conn = Conexion.getConnection();

                String sql = "DELETE FROM EstructuraFlujoEfectivo";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.executeUpdate();

                sql = "INSERT INTO EstructuraFlujoEfectivo "
                        + "(Posicion, Concepto, Tipo, Color_Fila, Fila_Negrita, Valor_0, Suma_Posiciones, "
                        + "Suma_Fila, Suma_Total, Query_BD, Descripcion_Query, Cambia_Signo, Boton_CierreMes, "
                        + "Inicio_Periodo, Cierre_Periodo) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                pstmt = conn.prepareStatement(sql);

                int rowCount = modelo.getRowCount();

                for (int i = 0; i < rowCount; i++) {
                    int posicion = Integer.parseInt(modelo.getValueAt(i, 0).toString());
                    String concepto = (String) modelo.getValueAt(i, 1);
                    String tipo = (String) modelo.getValueAt(i, 2);
                    String colorFila = (String) modelo.getValueAt(i, 3);
                    boolean filaNegrita = modelo.getValueAt(i, 4).equals("SI");
                    boolean valor0 = modelo.getValueAt(i, 5).equals("SI");
                    String sumaPosiciones = (String) modelo.getValueAt(i, 6);
                    String sumaFila = (String) modelo.getValueAt(i, 7);
                    boolean sumaTotal = modelo.getValueAt(i, 8).equals("SI");
                    String queryBD = (String) modelo.getValueAt(i, 9);
                    String descripcionQuery = (String) modelo.getValueAt(i, 10);
                    boolean cambiaSigno = modelo.getValueAt(i, 11).equals("SI");
                    boolean botonCierreMes = modelo.getValueAt(i, 12).equals("SI");
                    boolean inicioPeriodo = modelo.getValueAt(i, 13).equals("SI");
                    boolean cierrePeriodo = modelo.getValueAt(i, 14).equals("SI");

                    // Configurar los valores en la declaración preparada
                    pstmt.setInt(1, posicion);
                    pstmt.setString(2, concepto);

                    if (tipo == null || tipo.isBlank()) {
                        pstmt.setNull(3, java.sql.Types.VARCHAR);
                    } else {
                        pstmt.setString(3, tipo);
                    }

                    pstmt.setString(4, colorFila);
                    pstmt.setBoolean(5, filaNegrita);
                    pstmt.setBoolean(6, valor0);

                    if (sumaPosiciones == null || sumaPosiciones.isBlank()) {
                        pstmt.setNull(7, java.sql.Types.VARCHAR);
                    } else {
                        pstmt.setString(7, sumaPosiciones);
                    }

                    if (sumaFila == null || sumaFila.isBlank()) {
                        pstmt.setNull(8, java.sql.Types.VARCHAR);
                    } else {
                        pstmt.setString(8, sumaFila);
                    }

                    pstmt.setBoolean(9, sumaTotal);

                    if (queryBD == null || queryBD.isBlank()) {
                        pstmt.setNull(10, java.sql.Types.VARCHAR);
                    } else {
                        pstmt.setString(10, queryBD);
                    }

                    if (descripcionQuery == null || descripcionQuery.isBlank()) {
                        pstmt.setNull(11, java.sql.Types.VARCHAR);
                    } else {
                        pstmt.setString(11, descripcionQuery);
                    }

                    pstmt.setBoolean(12, cambiaSigno);
                    pstmt.setBoolean(13, botonCierreMes);
                    pstmt.setBoolean(14, inicioPeriodo);
                    pstmt.setBoolean(15, cierrePeriodo);

                    pstmt.executeUpdate();
                }
                JOptionPane.showMessageDialog(this, "Se ha actualizado la ESTRUCTURA DE FLUJO DE EFECTIVO, correctamente!", "Exito", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            }
        } catch (SQLException ex) {
            Logger.getLogger(EstructuraFE.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton5ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            /* Set the Nimbus look and feel */
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
            /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
            * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
            
            
             */
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
            //</editor-fold>

            /* Create and display the dialog */
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    EstructuraFE dialog = new EstructuraFE(new javax.swing.JFrame(), true);
                    dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosing(java.awt.event.WindowEvent e) {
                            System.exit(0);
                        }
                    });
                    dialog.setVisible(true);
                }
            });
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(EstructuraFE.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tb_resultado;
    // End of variables declaration//GEN-END:variables
}
