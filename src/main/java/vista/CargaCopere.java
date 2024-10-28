/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import Controlador.Conexion;
import com.formdev.flatlaf.FlatIntelliJLaf;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author ejmg3
 */
public class CargaCopere extends javax.swing.JFrame {
    //ESTADOS -> 1 ES PROCESADO  -- 2 FALTA DE LIQUIDEZ Y 3 NO RECIBE SUELDO
    
    private final Modelo modelo = new Modelo();
    private final Modelo2 modelo2 = new Modelo2();

    private void mostrarDialogoProgreso(JFrame parentFrame) {
        JDialog dialogoProgreso = new JDialog(parentFrame, "Guardando...", true);
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true); // Mostrar el porcentaje

        // Configurar el diálogo
        dialogoProgreso.setLayout(new BorderLayout());
        dialogoProgreso.add(progressBar, BorderLayout.CENTER);
        dialogoProgreso.setSize(300, 100);
        dialogoProgreso.setLocationRelativeTo(parentFrame); // Centrar sobre la ventana principal

        // Crear el SwingWorker para realizar el guardado en segundo plano
        SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() {
                try {
                    // Conexión a la base de datos
                    Connection con = Conexion.getConnection();

                    // Eliminar los datos existentes
                    String sql = "DELETE FROM HistorialCopere WHERE Año = ? AND Mes = ?";
                    PreparedStatement pstm = con.prepareStatement(sql);
                    pstm.setInt(1, jdc_año.getYear());
                    pstm.setInt(2, (jdc_mes.getMonth() + 1));
                    pstm.executeUpdate();

                    // Preparar la inserción de nuevos datos
                    sql = "INSERT INTO HistorialCopere ([FechaRegistro],[Año],[Mes],[Numero_CIP],[Monto],[Estado]) VALUES (GETDATE(), ?, ?, ?, ?, 1);";
                    pstm = con.prepareStatement(sql);
                    pstm.setInt(1, jdc_año.getYear());
                    pstm.setInt(2, (jdc_mes.getMonth() + 1));

                    int rowCount = modelo.getRowCount();
                    for (int i = 0; i < rowCount; i++) {
                        // Insertar los datos
                        pstm.setString(3, modelo.getValueAt(i, 2).toString());
                        pstm.setDouble(4, Double.parseDouble(modelo.getValueAt(i, 3).toString()));
                        pstm.executeUpdate();

                        // Calcular y actualizar el progreso
                        int progress = (int) ((i + 1) * 100 / rowCount);
                        publish(progress); // Enviar el progreso
                    }

                    pstm.close();
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(CargaCopere.class.getName()).log(Level.SEVERE, null, ex);
                }

                return null;
            }

            @Override
            protected void process(List<Integer> chunks) {
                // Actualizar la barra de progreso con el valor más reciente
                int mostRecentProgress = chunks.get(chunks.size() - 1);
                progressBar.setValue(mostRecentProgress);
            }

            @Override
            protected void done() {
                // Cerrar el diálogo al finalizar
                dialogoProgreso.dispose();
                JOptionPane.showMessageDialog(parentFrame, "Se ha guardado correctamente los descuentos COPERE", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }
        };

        // Ejecutar el SwingWorker en segundo plano
        worker.execute();

        // Mostrar el diálogo mientras el SwingWorker trabaja
        dialogoProgreso.setVisible(true);
    }

    private void CargarDatosCajaPensiones() {
        try {
            modelo2.setRowCount(0);
            
            String[] data = new String[9];
            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "SELECT * FROM EstructuraCajaPensiones";
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                data[0] = "004460192";
                data[1] = rs.getString(1);
                data[2] = rs.getString(2);
                data[3] = "LE";
                data[4] = rs.getString(3);
                data[5] = "  ";
                data[6] = String.valueOf(jdc_año.getYear());
                data[7] = String.format("%02d" , (jdc_mes.getMonth()+1));
                data[8] = String.format(Locale.US, "%010.2f", rs.getDouble(4));
                modelo2.addRow(data);
            }
            
            if (modelo.getRowCount()>0) {
                btnExportar1.setEnabled(true);
            }
            
            rs.close();
            st.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(CargaCopere.class.getName()).log(Level.SEVERE, null, ex);
        }

        
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
     * Creates new form CargaCopere
     */
    public CargaCopere() {
        initComponents();
        Locale.setDefault(new Locale("es", "ES"));
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
        
        cargarEstructuraCopere();
        cargarEstructuraCajaPensiones();
        
        CargarDatosCopere();
        CargarDatosCajaPensiones();
    }
    
    private void exportarXLSCopere() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar como");

        int mesSeleccionado = jdc_mes.getMonth();
        String mesEnTexto = new DateFormatSymbols().getShortMonths()[mesSeleccionado].toUpperCase();
        int añoSeleccionado = jdc_año.getYear();
        String nombreArchivo = "8070-" + mesEnTexto + " " + añoSeleccionado + ".xls";

        fileChooser.setSelectedFile(new File(nombreArchivo));
        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".xls")) {
                filePath += ".xls";
            }

            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet(String.valueOf(añoSeleccionado) + String.valueOf(mesSeleccionado));

            // Crear estilo de celda para números
            CellStyle numberStyle = workbook.createCellStyle();
            numberStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00"));  // Formato numérico

            // Crear estilo de fuente Arial 11
            Font font = workbook.createFont();
            font.setFontName("Arial");
            font.setFontHeightInPoints((short) 11);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(font);  // Aplicar la fuente a la celda de encabezado
            
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setFont(font); 
            // Crear fila de encabezados
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < modelo.getColumnCount(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(modelo.getColumnName(i));  // Asigna el nombre de la columna como encabezado
                cell.setCellStyle(headerStyle);  // Aplicar estilo de encabezado
            }

            // Crear filas con los datos
            for (int rowIndex = 0; rowIndex < modelo.getRowCount(); rowIndex++) {
                Row row = sheet.createRow(rowIndex + 1);  // +1 porque la fila 0 es para encabezados
                for (int colIndex = 0; colIndex < modelo.getColumnCount(); colIndex++) {
                    Cell cell = row.createCell(colIndex);
                    Object value = modelo.getValueAt(rowIndex, colIndex);
                    if (value != null) {
                        if (colIndex == 3) { // Columna 0 y 3 como número
                            cell.setCellValue(Double.parseDouble(value.toString()));  // Convertir a número
                            cell.setCellStyle(numberStyle); // Aplicar formato numérico
                        } else {
                            cell.setCellValue(value.toString());  // Convertir los valores a String
                        }
                    }
                }
            }

            // Ajustar el ancho de las columnas automáticamente
            for (int i = 0; i < modelo.getColumnCount(); i++) {
                sheet.autoSizeColumn(i); // Ajustar el ancho de la columna
            }

            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
                JOptionPane.showMessageDialog(null, "Exportación exitosa a " + filePath);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al exportar archivo: " + e.getMessage());
            }
        }
    }
    
    
    private void cargarEstructuraCopere() {
                String[] columnNames = new String[]{
                    "MES PROCESO",
                    "COD_DESCUENTO",
                    "NUM_ADM",
                    "MONTO",
                    "FECHA DESEMBOLSO",
                    "HORA DESEMBOLSO",
                    "NRO CUOTA",
                    "TOTAL CUOTAS",
                    "NUM_CHEQUE"
                };

                modelo.setColumnIdentifiers(columnNames);

                JTableHeader header = tbCopere.getTableHeader();
                header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 40));

                header.setBackground(new java.awt.Color(255, 217, 102));
                tbCopere.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

                TableColumnModel columnModel = tbCopere.getColumnModel();
                for (int col = 0; col < tbCopere.getColumnCount(); col++) {
                    TableColumn tableColumn = columnModel.getColumn(col);
                    Component comp = header.getDefaultRenderer().getTableCellRendererComponent(tbCopere, tableColumn.getHeaderValue(), false, false, 0, col);
                    int width = comp.getPreferredSize().width + 25;
                    tableColumn.setPreferredWidth(width);
                }

    }
    
    
    private void cargarEstructuraCajaPensiones() {
                String[] columnNames = new String[]{
                    "COD. FIJO",
                    "NRO. CIP",
                    "TIP. DOC.",
                    "COD. FIJO 2",
                    "DOCUMENTO",
                    "COD. FIJO 3",
                    "AÑO",
                    "MES",
                    "MONTO APORTE"
                };

                modelo2.setColumnIdentifiers(columnNames);

                JTableHeader header = tbCajaPensiones.getTableHeader();
                header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 40));

                header.setBackground(new java.awt.Color(255, 217, 102));
                tbCajaPensiones.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

                TableColumnModel columnModel = tbCajaPensiones.getColumnModel();
                for (int col = 0; col < tbCajaPensiones.getColumnCount(); col++) {
                    TableColumn tableColumn = columnModel.getColumn(col);
                    Component comp = header.getDefaultRenderer().getTableCellRendererComponent(tbCajaPensiones, tableColumn.getHeaderValue(), false, false, 0, col);
                    int width = comp.getPreferredSize().width + 25;
                    tableColumn.setPreferredWidth(width);
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

        jPanel16 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jdc_año = new com.toedter.calendar.JYearChooser();
        jLabel14 = new javax.swing.JLabel();
        jdc_mes = new com.toedter.calendar.JMonthChooser();
        jLabel16 = new javax.swing.JLabel();
        txt_ruc1 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txt_razonsocial1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        btn_Nuevo = new javax.swing.JButton();
        btnNoProcesados = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbCopere = new javax.swing.JTable();
        txtBuscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnExportar = new javax.swing.JButton();
        btnGrabar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnAfiliadoCaja = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbCajaPensiones = new javax.swing.JTable();
        txtBuscar1 = new javax.swing.JTextField();
        btnBuscar1 = new javax.swing.JButton();
        btnExportar1 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel16.setBackground(new java.awt.Color(93, 173, 226));

        jLabel19.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Registro de Contable");
        jLabel19.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
        );

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Fecha de Proceso", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 13))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel12.setText("Año:");

        jdc_año.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdc_añoPropertyChange(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel14.setText("Mes");

        jdc_mes.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jdc_mes.setForeground(new java.awt.Color(255, 0, 0));
        jdc_mes.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        jdc_mes.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jdc_mesPropertyChange(evt);
            }
        });
        jdc_mes.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                jdc_mesVetoableChange(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel16.setText("Razón Social:");

        txt_ruc1.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        txt_ruc1.setForeground(new java.awt.Color(255, 0, 0));
        txt_ruc1.setText("20505606435");

        jLabel17.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel17.setText("RUC:");

        txt_razonsocial1.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        txt_razonsocial1.setForeground(new java.awt.Color(255, 0, 0));
        txt_razonsocial1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_razonsocial1.setText("ASOCIACION CIRCULO MILITAR DEL PERU");

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_ruc1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_razonsocial1, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_razonsocial1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_ruc1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jdc_mes, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                    .addComponent(jdc_año, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Opciones", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 12))); // NOI18N

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane1.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        btn_Nuevo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btn_Nuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_add_32px.png"))); // NOI18N
        btn_Nuevo.setText("Registrar Afiliado");
        btn_Nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_NuevoActionPerformed(evt);
            }
        });

        btnNoProcesados.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnNoProcesados.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/flecha-hacia-abajo.png"))); // NOI18N
        btnNoProcesados.setText("Modulo de No Procesados");
        btnNoProcesados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNoProcesadosActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Datos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 12))); // NOI18N

        tbCopere.setModel(modelo);
        tbCopere.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tbCopere.setFocusTraversalPolicyProvider(true);
        tbCopere.setRowHeight(32);
        tbCopere.setSelectionBackground(new java.awt.Color(0, 153, 204));
        tbCopere.setShowGrid(true);
        tbCopere.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbCopereMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tbCopere);
        if (tbCopere.getColumnModel().getColumnCount() > 0) {
            tbCopere.getColumnModel().getColumn(0).setResizable(false);
            tbCopere.getColumnModel().getColumn(1).setResizable(false);
            tbCopere.getColumnModel().getColumn(2).setResizable(false);
            tbCopere.getColumnModel().getColumn(3).setResizable(false);
            tbCopere.getColumnModel().getColumn(4).setResizable(false);
            tbCopere.getColumnModel().getColumn(5).setResizable(false);
        }

        btnBuscar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buscar.png"))); // NOI18N
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnExportar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnExportar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/sobresalir.png"))); // NOI18N
        btnExportar.setText("Exportar XLSX");
        btnExportar.setEnabled(false);
        btnExportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportarActionPerformed(evt);
            }
        });

        btnGrabar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Save.png"))); // NOI18N
        btnGrabar.setText("Grabar Información en BD");
        btnGrabar.setEnabled(false);
        btnGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabarActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 24)); // NOI18N
        jLabel1.setText(">");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtBuscar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnGrabar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnExportar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                .addGap(9, 9, 9)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnExportar, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnGrabar, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btn_Nuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNoProcesados, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Nuevo)
                    .addComponent(btnNoProcesados, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("COPERE", jPanel3);

        btnAfiliadoCaja.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAfiliadoCaja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_add_32px.png"))); // NOI18N
        btnAfiliadoCaja.setText("Registrar Afiliado");
        btnAfiliadoCaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAfiliadoCajaActionPerformed(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Datos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 12))); // NOI18N

        tbCajaPensiones.setModel(modelo2);
        tbCajaPensiones.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tbCajaPensiones.setFocusTraversalPolicyProvider(true);
        tbCajaPensiones.setRowHeight(32);
        tbCajaPensiones.setSelectionBackground(new java.awt.Color(0, 153, 204));
        tbCajaPensiones.setShowGrid(true);
        tbCajaPensiones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbCajaPensionesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbCajaPensiones);
        if (tbCajaPensiones.getColumnModel().getColumnCount() > 0) {
            tbCajaPensiones.getColumnModel().getColumn(0).setResizable(false);
            tbCajaPensiones.getColumnModel().getColumn(1).setResizable(false);
            tbCajaPensiones.getColumnModel().getColumn(2).setResizable(false);
            tbCajaPensiones.getColumnModel().getColumn(3).setResizable(false);
            tbCajaPensiones.getColumnModel().getColumn(4).setResizable(false);
            tbCajaPensiones.getColumnModel().getColumn(5).setResizable(false);
        }

        btnBuscar1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBuscar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buscar.png"))); // NOI18N
        btnBuscar1.setText("Buscar");
        btnBuscar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscar1ActionPerformed(evt);
            }
        });

        btnExportar1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnExportar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Rich Text Converter_1.png"))); // NOI18N
        btnExportar1.setText("Exportar TXT");
        btnExportar1.setEnabled(false);
        btnExportar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportar1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(txtBuscar1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscar1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnExportar1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscar1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)
                .addGap(9, 9, 9)
                .addComponent(btnExportar1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAfiliadoCaja, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAfiliadoCaja)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("CAJA DE PENSIONES", jPanel4);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 930, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 428, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("OPREFA", jPanel5);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        TableRowSorter<DefaultTableModel> rowSorter = new TableRowSorter<>(modelo);
        tbCopere.setRowSorter(rowSorter);
        if (!txtBuscar.getText().isEmpty()) {
            RowFilter<DefaultTableModel, Object> filter = RowFilter.regexFilter(txtBuscar.getText());
            rowSorter.setRowFilter(filter);
            txtBuscar.setText("");
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btn_NuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_NuevoActionPerformed
        RegistroAfiliadoCopere ini = new RegistroAfiliadoCopere(this, true);
        ini.setVisible(true);
    }//GEN-LAST:event_btn_NuevoActionPerformed

    private void btnNoProcesadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNoProcesadosActionPerformed
        ModuloNoProcesado ini = new ModuloNoProcesado(this, true);
        ini.setVisible(true);
    }//GEN-LAST:event_btnNoProcesadosActionPerformed

    private void btnExportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Desea EXPORTAR el archivo COPERE", "Confirmación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            exportarXLSCopere();
        }
    }//GEN-LAST:event_btnExportarActionPerformed

    private void tbCopereMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbCopereMouseClicked
        if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
            DetalladoCopere ini = new DetalladoCopere(this, true, modelo.getValueAt(tbCopere.getSelectedRow(), 2).toString());
            ini.setVisible(true);
        }
    }//GEN-LAST:event_tbCopereMouseClicked

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Esta seguro que desea GUARDAR/REEMPLAZAR la informacion COPERE?", "Confirmación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            mostrarDialogoProgreso(this);
        }
    }//GEN-LAST:event_btnGrabarActionPerformed

    private void jdc_mesPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdc_mesPropertyChange
        CargarDatosCopere();
        CargarDatosCajaPensiones();
    }//GEN-LAST:event_jdc_mesPropertyChange

    private void jdc_mesVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_jdc_mesVetoableChange
        
    }//GEN-LAST:event_jdc_mesVetoableChange

    private void jdc_añoPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jdc_añoPropertyChange
        CargarDatosCopere();
        CargarDatosCajaPensiones();
    }//GEN-LAST:event_jdc_añoPropertyChange

    private void btnAfiliadoCajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAfiliadoCajaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAfiliadoCajaActionPerformed

    private void tbCajaPensionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbCajaPensionesMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbCajaPensionesMouseClicked

    private void btnBuscar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscar1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscar1ActionPerformed

    private void btnExportar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportar1ActionPerformed
        saveTableToFile(tbCajaPensiones, jdc_año.getYear(), (jdc_mes.getMonth()+1));
    }//GEN-LAST:event_btnExportar1ActionPerformed

    
    public static void saveTableToFile(JTable table, int year, int month) {
        // Formato del nombre de archivo
        String fileName = String.format("0044_DSCTO_%04d%02d.TXT", year, month);
        
        // Crear JFileChooser
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar archivo");
        fileChooser.setSelectedFile(new java.io.File(fileName)); // Nombre por defecto

        // Mostrar el diálogo
        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileChooser.getSelectedFile()))) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                for (int i = 0; i < model.getRowCount(); i++) {
                    StringBuilder sb = new StringBuilder();
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        sb.append(model.getValueAt(i, j).toString());
                    }
                    writer.write(sb.toString());
                    writer.newLine(); // Nueva línea para cada registro
                }
                JOptionPane.showMessageDialog(null, "Archivo guardado con éxito: " + fileChooser.getSelectedFile().getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al guardar el archivo: " + e.getMessage());
            }
        }
    }
    
    
    private void CargarDatosCopere() {
        String[] data = new String[4];
        Connection cnn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            modelo.setRowCount(0);
            cnn = Conexion.getConnection();
            String sql = "SELECT Cod_Descuento = '8070', Numero_CIP, Monto_Descuento FROM EstructuraCopere";
            st = cnn.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                data[0] = jdc_año.getYear() + "" + String.format("%02d" , (jdc_mes.getMonth() + 1));
                for (int i = 1; i < data.length; i++) {
                    data[i]  = rs.getString(i);
                }
                
                modelo.addRow(data);
            }
            
            if (modelo.getRowCount()>0) {
                btnExportar.setEnabled(true);
                btnGrabar.setEnabled(true);
            }

        } catch (SQLException ex) {
            ex.printStackTrace(); // Muestra el error en la consola
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (cnn != null) {
                    cnn.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace(); // Muestra el error en la consola
            }
        }
    }

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
                new CargaCopere().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAfiliadoCaja;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnBuscar1;
    private javax.swing.JButton btnExportar;
    private javax.swing.JButton btnExportar1;
    private javax.swing.JButton btnGrabar;
    private javax.swing.JButton btnNoProcesados;
    private javax.swing.JButton btn_Nuevo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    public static com.toedter.calendar.JYearChooser jdc_año;
    public static com.toedter.calendar.JMonthChooser jdc_mes;
    private javax.swing.JTable tbCajaPensiones;
    private javax.swing.JTable tbCopere;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtBuscar1;
    private javax.swing.JLabel txt_razonsocial1;
    private javax.swing.JLabel txt_ruc1;
    // End of variables declaration//GEN-END:variables
}
