/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import Controlador.Conexion;
import Controlador.FuncionesGlobales;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import main.Application;
import org.apache.poi.ss.usermodel.Cell;
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
public class Depreciacion extends javax.swing.JFrame {

    private final Modelo modelo = new Modelo();

   
    public class Modelo extends DefaultTableModel {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    /**
     * Creates new form Depreciacion
     */
    public Depreciacion(String usuario) {
        initComponents();
        UIManager.getLookAndFeelDefaults().put("Table.alternateRowColor", new Color(254, 238, 184));
        Locale.setDefault(new Locale("es", "ES"));
        this.setIconImage(new ImageIcon(System.getProperty("user.dir") + "/logoACMP.png").getImage());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(false);
                FlatMacDarkLaf.setup();
                Application anteriorFrame = new Application(usuario);
                anteriorFrame.setVisible(true);
            }
        });
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        llenar_tabla();
        FuncionesGlobales.colocarnombremesannio(jdc_año, jdc_mes, txt_razonsocial2);
        
        
        jdc_mes.addPropertyChangeListener("month", (java.beans.PropertyChangeEvent evt) -> {
            llenar_tabla();
            FuncionesGlobales.colocarnombremesannio(jdc_año, jdc_mes, txt_razonsocial2);
        });
        jdc_año.addPropertyChangeListener("year", (java.beans.PropertyChangeEvent evt) -> {
            llenar_tabla();
            FuncionesGlobales.colocarnombremesannio(jdc_año, jdc_mes, txt_razonsocial2);
        });
    }

    private void llenar_tabla() {
        
        if ((jdc_mes.getMonth() + 1) == 12) {
            jButton3.setEnabled(true);
        } else {
            jButton3.setEnabled(false);
        }
        
        modelo.setRowCount(0);
        modelo.setColumnCount(0);

        JTableHeader header = tb_resultado.getTableHeader();
        header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 50));
        header.setBackground(new java.awt.Color(255, 217, 102));
        tb_resultado.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

        int year = jdc_año.getYear();

        modelo.addColumn("idActivo");
        modelo.addColumn("Item");
        modelo.addColumn("Periodo");
        modelo.addColumn("Ubicacion");
        modelo.addColumn("Comprobante");
        modelo.addColumn("Fecha de Activación"); 
        modelo.addColumn("Marca");
        modelo.addColumn("Cuenta de Gasto Nueva");
        modelo.addColumn("Cta Depre Nueva");
        modelo.addColumn("Cta Contab Nueva");
        modelo.addColumn("Descripcion");
        modelo.addColumn("Mon.");
        modelo.addColumn("Cantidad");
        modelo.addColumn("U/M");
        modelo.addColumn("Valor U/M");
        modelo.addColumn("Total");
        modelo.addColumn("Vida Util Meses");
        modelo.addColumn("Diciembre " + (year - 1));
        modelo.addColumn("Depreciacion Acumulada " + (year - 1));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (int month = 1; month <= 12; month++) {
            LocalDate lastDayOfMonth = LocalDate.of(year, month, 1).withDayOfMonth(LocalDate.of(year, month, 1).lengthOfMonth());

            String columnName = lastDayOfMonth.format(formatter);

            modelo.addColumn(columnName);
        }

        modelo.addColumn("Depreciación Acumulada " + year);
        modelo.addColumn("Depreciación " + year);
        modelo.addColumn("Saldo Bruto " + year);
        modelo.addColumn("Saldo Neto " + year);

        TableColumnModel columnModel = tb_resultado.getColumnModel();
        for (int col = 0; col < tb_resultado.getColumnCount(); col++) {
            TableColumn tableColumn = columnModel.getColumn(col);
            Component comp = header.getDefaultRenderer().getTableCellRendererComponent(tb_resultado, tableColumn.getHeaderValue(), false, false, 0, col);
            int width = comp.getPreferredSize().width + 25;
            tableColumn.setPreferredWidth(width);
        }

        DecimalFormat decimalFormat = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US));

        int columnCount = tb_resultado.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            if (i == 14 || i == 15 || i >= 17) {
                tb_resultado.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
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

        ocultarColumnaAncho(tb_resultado, 0);
        ocultarColumnaAncho(tb_resultado, 17);
        ocultarColumnaAncho(tb_resultado, 18);

        cargarActivos();
    }

    private void cargarActivos() {
        try {
            Object data[] = new Object[19];
            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();

            String query = "SELECT DISTINCT A.idActivo, [Item] "
                    + "      ,[Periodo] "
                    + "      ,[Ubicacion] "
                    + "      ,[Comprobante] "
                    + "      ,FORMAT([FechaActivacion], 'dd/MM/yyyy') AS FechaActivacion "
                    + "      ,[Marca] "
                    + "      ,[CuentaGastoNuevo] "
                    + "      ,[CuentaDepreNuevo] "
                    + "      ,[CuentaContaNuevo] "
                    + "      ,[Descripcion] "
                    + "      ,[Moneda] "
                    + "      ,[Cantidad] "
                    + "      ,[UnidadMedida] "
                    + "      ,[ValorUnidad] "
                    + "      ,[Total] "
                    + "      ,ISNULL([VidaUtilMeses], 0) AS VidaUtilMeses "
                    + "      ,[MontoDiciembre] "
                    + "      ,[MontoAcumulado] "
                    + "  FROM [Activos] A "
                    + "  LEFT JOIN ActivoHistorico AH ON A.idActivo = AH.idActivo AND Año = " + (jdc_año.getYear() - 1) + " "
                    + "  WHERE  YEAR(FechaActivacion) <= " + jdc_año.getYear() + " AND MONTH(FechaActivacion) <= " + (jdc_mes.getMonth() + 1) + " "
                    + "  ORDER BY Item ASC";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                for (int i = 0; i < data.length; i++) {
                    data[i] = rs.getObject(i + 1);
                }
                modelo.addRow(data);
            }

            cargarPrimerDatoColumna();

        } catch (SQLException ex) {
            Logger.getLogger(Depreciacion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void cargarPrimerDatoColumna() {
        double primerDato = 0.0;
        for (int i = 0; i < tb_resultado.getRowCount(); i++) {
            LocalDate fechaActivacion = LocalDate.parse(tb_resultado.getValueAt(i, 5).toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            if (fechaActivacion.getYear() < jdc_año.getYear()) {
                if (tb_resultado.getValueAt(i, 17) != null) {
                    if (Double.parseDouble(tb_resultado.getValueAt(i, 17).toString()) != 0.0) {
                        double total = Double.parseDouble(tb_resultado.getValueAt(i, 15).toString());
                        double vidaUtil = Double.parseDouble(tb_resultado.getValueAt(i, 16).toString());
                        tb_resultado.setValueAt(total / vidaUtil, i, 19);
                    } else {
                        tb_resultado.setValueAt(0.0, i, 19);
                    }
                }
            } else if (fechaActivacion.getYear() == jdc_año.getYear()) {
                for (int j = 19; j < 31; j++) {
                    LocalDate fechaColumna = LocalDate.parse(tb_resultado.getColumnName(j), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    long diasDiferencia = ChronoUnit.DAYS.between(fechaActivacion, fechaColumna);
                    double total = Double.parseDouble(tb_resultado.getValueAt(i, 15).toString());
                    double vidaUtil = Double.parseDouble(tb_resultado.getValueAt(i, 16).toString());

                    int ultimoDiaMes = YearMonth.of(fechaColumna.getYear(), fechaColumna.getMonth()).lengthOfMonth();

                    primerDato = (total / vidaUtil) * (diasDiferencia + 1) / ultimoDiaMes;

                    if (fechaActivacion.getMonthValue() == fechaColumna.getMonthValue()) {
                        tb_resultado.setValueAt(primerDato, i, j);
                    }
                }
            }
        }

        cargarRestoDatoColumna();
    }

    private void cargarRestoDatoColumna() {
        for (int i = 0; i < tb_resultado.getRowCount(); i++) {
            for (int j = 19; j < (19 + jdc_mes.getMonth()); j++) {
                if (tb_resultado.getValueAt(i, j) != null) {
                    if (Double.parseDouble(tb_resultado.getValueAt(i, j).toString()) != 0.0) {
                        double total = Double.parseDouble(tb_resultado.getValueAt(i, 15).toString());
                        double vidaUtil = Double.parseDouble(tb_resultado.getValueAt(i, 16).toString());
                        tb_resultado.setValueAt(total / vidaUtil, i, j + 1);
                    } else {
                        tb_resultado.setValueAt(0.0, i, j + 1);
                    }

                }
            }
        }

        cargarUltimosDatoColumna();

    }

    private void cargarUltimosDatoColumna() {
        for (int i = 0; i < tb_resultado.getRowCount(); i++) {
            double totalAnioActual = 0.0;
            for (int j = 19; j < (19 + (jdc_mes.getMonth() + 1)); j++) {
                if (tb_resultado.getValueAt(i, j) != null) {
                    totalAnioActual = totalAnioActual + Double.parseDouble(tb_resultado.getValueAt(i, j).toString());
                }
            }

            Object valor = tb_resultado.getValueAt(i, 18);
            double depreciacionAnioAnterior = 0.0;

            if (valor != null && !valor.toString().isEmpty()) {
                depreciacionAnioAnterior = Double.parseDouble(valor.toString());
            }

            tb_resultado.setValueAt(depreciacionAnioAnterior + totalAnioActual, i, 31);
            tb_resultado.setValueAt(totalAnioActual, i, 32);
            tb_resultado.setValueAt(Double.valueOf(tb_resultado.getValueAt(i, 15).toString()), i, 33);
            tb_resultado.setValueAt(Double.parseDouble(tb_resultado.getValueAt(i, 15).toString()) - (depreciacionAnioAnterior + totalAnioActual), i, 34);
        }

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

    public void ocultarColumnaAncho(JTable tabla, int columnaIndex) {
        tabla.getColumnModel().getColumn(columnaIndex).setMinWidth(0);
        tabla.getColumnModel().getColumn(columnaIndex).setMaxWidth(0);
        tabla.getColumnModel().getColumn(columnaIndex).setWidth(0);
    }
    
    private void guardarDepreciacion() {
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        jButton3.setEnabled(false);
        Connection cn = null;
        try {
            String deleteSQL = "DELETE FROM ActivoHistorico WHERE Año = ?";
            String insertSQL = "INSERT INTO ActivoHistorico (idActivo, Año, MontoDiciembre, MontoAcumulado) VALUES (?, ?, ?, ?)";
            cn = Conexion.getConnection();
            // Iniciar una transacción
            cn.setAutoCommit(false);

            // Eliminar registros existentes para el año seleccionado
            try (PreparedStatement deleteStmt = cn.prepareStatement(deleteSQL)) {
                deleteStmt.setInt(1, jdc_año.getYear());
                deleteStmt.executeUpdate();
            }

            // Insertar nuevos registros en lotes
            try (PreparedStatement insertStmt = cn.prepareStatement(insertSQL)) {
                for (int i = 0; i < tb_resultado.getRowCount(); i++) {
                    Object valor30 = tb_resultado.getValueAt(i, 30);
                    Object valor31 = tb_resultado.getValueAt(i, 31);

                    // Validar que los valores no sean nulos y que no sean infinitos o NaN
                    if (valor30 != null && valor31 != null) {
                        double montoDiciembre = Double.parseDouble(valor30.toString());
                        double montoAcumulado = Double.parseDouble(valor31.toString());

                        if (!Double.isInfinite(montoDiciembre) && !Double.isNaN(montoDiciembre)
                                && !Double.isInfinite(montoAcumulado) && !Double.isNaN(montoAcumulado)
                                && montoAcumulado != 0.00) {

                            insertStmt.setInt(1, Integer.parseInt(tb_resultado.getValueAt(i, 0).toString()));
                            insertStmt.setInt(2, jdc_año.getYear());
                            insertStmt.setDouble(3, montoDiciembre);
                            insertStmt.setDouble(4, montoAcumulado);
                            insertStmt.addBatch(); // Agregar al lote
                        }
                    }
                }
                insertStmt.executeBatch(); // Ejecutar el lote
            }

            // Confirmar la transacción
            cn.commit();
            JOptionPane.showMessageDialog(this, "Depreciación guardada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(Depreciacion.class.getName()).log(Level.SEVERE, "Error al guardar depreciación", ex);
            try {
                // Intentar deshacer la transacción en caso de error
                if (cn != null && !cn.isClosed()) {
                    cn.rollback();
                }
            } catch (SQLException rollbackEx) {
                Logger.getLogger(Depreciacion.class.getName()).log(Level.SEVERE, "Error al realizar rollback", rollbackEx);
            }
        } finally {
            jButton3.setEnabled(true);
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            try {
                if (cn != null && !cn.isClosed()) {
                    cn.close(); // Cerrar la conexión
                }
            } catch (SQLException closeEx) {
                Logger.getLogger(Depreciacion.class.getName()).log(Level.SEVERE, "Error al cerrar la conexión", closeEx);
            }
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
        jPanel14 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jdc_año = new com.toedter.calendar.JYearChooser();
        jLabel14 = new javax.swing.JLabel();
        jdc_mes = new com.toedter.calendar.JMonthChooser();
        jPanel15 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        txt_razonsocial2 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        ExportarExcel = new javax.swing.JMenuItem();

        jButton2.setText("jButton2");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        tb_resultado.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        tb_resultado.setModel(modelo);
        tb_resultado.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tb_resultado.setRowHeight(30);
        tb_resultado.setShowGrid(true);
        tb_resultado.setShowVerticalLines(false);
        tb_resultado.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tb_resultado);

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Fecha de Proceso", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 13))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel12.setText("Año:");

        jLabel14.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel14.setText("Mes");

        jdc_mes.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jdc_mes.setForeground(new java.awt.Color(255, 0, 0));
        jdc_mes.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jdc_año, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel14)
                .addGap(12, 12, 12)
                .addComponent(jdc_mes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(880, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jdc_mes, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(jdc_año, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton3.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Save.png"))); // NOI18N
        jButton3.setText("Guardar Depreciación Anual");
        jButton3.setEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jButton3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        txt_razonsocial2.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        txt_razonsocial2.setForeground(new java.awt.Color(255, 0, 0));
        txt_razonsocial2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_razonsocial2.setText("-");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_razonsocial2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_razonsocial2)
                .addGap(7, 7, 7)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ajustes.png"))); // NOI18N
        jMenu1.setText("Opciones");
        jMenu1.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N

        jMenuItem1.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_add_32px.png"))); // NOI18N
        jMenuItem1.setText("Añadir Activos");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/flecha-hacia-abajo.png"))); // NOI18N
        jMenu2.setText("Exportar");
        jMenu2.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed(evt);
            }
        });

        ExportarExcel.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        ExportarExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Microsoft excel.png"))); // NOI18N
        ExportarExcel.setText("Excel");
        ExportarExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExportarExcelActionPerformed(evt);
            }
        });
        jMenu2.add(ExportarExcel);

        jMenuBar1.add(jMenu2);

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
    // Método para exportar archivos en XLSX
    public static void exportarArchivoExcel(DefaultTableModel model) {
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setDialogTitle("Guardar archivo Excel");
        jFileChooser.setFileFilter(new FileNameExtensionFilter("Archivos XLSX", "xlsx"));

        int result = jFileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jFileChooser.getSelectedFile();
            if (!selectedFile.getName().endsWith(".xlsx")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".xlsx");
            }
            try (Workbook workbook = new XSSFWorkbook(); FileOutputStream fos = new FileOutputStream(selectedFile)) {
                Sheet sheet = workbook.createSheet("Datos");

                // Crear estilos
                CellStyle headerStyle = workbook.createCellStyle();
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);

                CellStyle integerStyle = workbook.createCellStyle();
                integerStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("0"));

                CellStyle decimalStyle = workbook.createCellStyle();
                decimalStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("0.00"));

                // Crear encabezados
                Row headerRow = sheet.createRow(0);
                for (int i = 1; i < model.getColumnCount(); i++) { // Comenzar desde la segunda columna para ocultar la primera
                    Cell cell = headerRow.createCell(i - 1); // Ajustar índice
                    cell.setCellValue(model.getColumnName(i));
                    cell.setCellStyle(headerStyle);
                }

                // Crear datos
                for (int i = 0; i < model.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 1; j < model.getColumnCount(); j++) { // Comenzar desde la segunda columna
                        Cell cell = row.createCell(j - 1); // Ajustar índice
                        Object value = model.getValueAt(i, j);

                        if (value instanceof Integer) {
                            cell.setCellValue((Integer) value);
                            cell.setCellStyle(integerStyle);
                        } else if (value instanceof Double) {
                            cell.setCellValue((Double) value);
                            cell.setCellStyle(decimalStyle);
                        } else {
                            String cellValue = (value != null) ? value.toString() : "";
                            cell.setCellValue(cellValue);
                        }
                    }
                }

                // Autoajustar columnas
                for (int i = 0; i < model.getColumnCount() - 1; i++) {
                    sheet.autoSizeColumn(i);
                }

                workbook.write(fos);
                JOptionPane.showMessageDialog(null, "Archivo exportado exitosamente.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al exportar el archivo: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Exportación cancelada.");
        }
    }
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea guardar la depreciación anual del año " + jdc_año.getYear() + "?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            guardarDepreciacion();
            
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void ExportarExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExportarExcelActionPerformed
        DefaultTableModel modelo = (DefaultTableModel) tb_resultado.getModel();

        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "No hay datos para exportar.");
            return;
        }

        exportarArchivoExcel(modelo);    }//GEN-LAST:event_ExportarExcelActionPerformed

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed

    }//GEN-LAST:event_jMenu2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        llenar_tabla();
        FuncionesGlobales.colocarnombremesannio(jdc_año, jdc_mes, txt_razonsocial2);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        AgregarActivos ini = new AgregarActivos(this, true);
        int maxValue = Integer.MIN_VALUE;
        for (int i = 0; i < tb_resultado.getRowCount(); i++) {
            Object value = tb_resultado.getValueAt(i, 1);
            if (value instanceof Number) {
                int num = ((Number) value).intValue();
                if (num > maxValue) {
                    maxValue = num;
                }
            }
        }
        ini.ultimoItem.setText(String.valueOf(maxValue));
        ini.toFront();
        ini.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

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

            /* Create and display the form */
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    
                    new Depreciacion(null).setVisible(true);
                }
            });
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Depreciacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem ExportarExcel;
    public static final javax.swing.JButton jButton2 = new javax.swing.JButton();
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JScrollPane jScrollPane1;
    public static com.toedter.calendar.JYearChooser jdc_año;
    public static com.toedter.calendar.JMonthChooser jdc_mes;
    private javax.swing.JTable tb_resultado;
    private javax.swing.JLabel txt_razonsocial2;
    // End of variables declaration//GEN-END:variables
}
