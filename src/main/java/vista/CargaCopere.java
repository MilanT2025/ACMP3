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

    private final Modelo modeloCopere = new Modelo();
    private final Modelo2 modeloCaja = new Modelo2();
    private final Modelo3 modeloOprefa = new Modelo3();

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
        cargarEstructuraOprefa();

        CargarDatosCopere();
        CargarDatosCajaPensiones();
        CargarDatosOprefa();
    }

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

                    int rowCount = modeloCopere.getRowCount();
                    for (int i = 0; i < rowCount; i++) {
                        // Insertar los datos
                        pstm.setString(3, modeloCopere.getValueAt(i, 2).toString());
                        pstm.setDouble(4, Double.parseDouble(modeloCopere.getValueAt(i, 3).toString()));
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

    private void mostrarDialogoProgresoCajaPensiones(JFrame parentFrame) {
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
                    String sql = "DELETE FROM HistorialCajaPensiones WHERE Año = ? AND Mes = ?";
                    PreparedStatement pstm = con.prepareStatement(sql);
                    pstm.setInt(1, jdc_año.getYear());
                    pstm.setInt(2, (jdc_mes.getMonth() + 1));
                    pstm.executeUpdate();

                    // Preparar la inserción de nuevos datos
                    sql = "INSERT INTO HistorialCajaPensiones ([FechaRegistro],[Año],[Mes],[Documento],[Monto],[Estado]) VALUES (GETDATE(), ?, ?, ?, ?, 1);";
                    pstm = con.prepareStatement(sql);
                    pstm.setInt(1, jdc_año.getYear());
                    pstm.setInt(2, (jdc_mes.getMonth() + 1));

                    int rowCount = modeloCaja.getRowCount();
                    for (int i = 0; i < rowCount; i++) {
                        // Insertar los datos
                        pstm.setString(3, modeloCaja.getValueAt(i, 4).toString());
                        pstm.setDouble(4, Double.parseDouble(modeloCaja.getValueAt(i, 8).toString()));
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
                JOptionPane.showMessageDialog(parentFrame, "Se ha guardado correctamente los descuentos CAJA DE PENSIONES", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }
        };

        // Ejecutar el SwingWorker en segundo plano
        worker.execute();

        // Mostrar el diálogo mientras el SwingWorker trabaja
        dialogoProgreso.setVisible(true);
    }

    private void mostrarDialogoProgresoOprefa(JFrame parentFrame) {
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
                    String sql = "DELETE FROM HistorialOprefa WHERE Año = ? AND Mes = ?";
                    PreparedStatement pstm = con.prepareStatement(sql);
                    pstm.setInt(1, jdc_año.getYear());
                    pstm.setInt(2, (jdc_mes.getMonth() + 1));
                    pstm.executeUpdate();

                    // Preparar la inserción de nuevos datos
                    sql = "INSERT INTO HistorialOprefa ([FechaRegistro],[Año],[Mes],[Documento],[Monto],[Estado]) VALUES (GETDATE(), ?, ?, ?, ?, 1);";
                    pstm = con.prepareStatement(sql);
                    pstm.setInt(1, jdc_año.getYear());
                    pstm.setInt(2, (jdc_mes.getMonth() + 1));

                    int rowCount = modeloOprefa.getRowCount();
                    for (int i = 0; i < rowCount; i++) {
                        pstm.setString(3, modeloOprefa.getValueAt(i, 1).toString());
                        pstm.setDouble(4, Double.parseDouble(modeloOprefa.getValueAt(i, 2).toString()));
                        pstm.executeUpdate();

                        int progress = (int) ((i + 1) * 100 / rowCount);
                        publish(progress);
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
                JOptionPane.showMessageDialog(parentFrame, "Se ha guardado correctamente los descuentos CAJA DE PENSIONES", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }
        };

        // Ejecutar el SwingWorker en segundo plano
        worker.execute();

        // Mostrar el diálogo mientras el SwingWorker trabaja
        dialogoProgreso.setVisible(true);
    }

    private void CargarDatosCajaPensiones() {
        try {
            modeloCaja.setRowCount(0);

            String[] data = new String[9];
            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "SELECT Codigo_CIP, Codigo_Documento, Documento, Monto FROM EstructuraCajaPensiones WHERE Estado = 1";
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                data[0] = "0044601920";
                data[1] = rs.getString(1);
                data[2] = rs.getString(2);
                data[3] = "LE";
                data[4] = rs.getString(3);
                data[5] = "  ";
                data[6] = String.valueOf(jdc_año.getYear());
                data[7] = String.format("%02d", (jdc_mes.getMonth() + 1));
                data[8] = String.format(Locale.US, "%010.2f", rs.getDouble(4));
                modeloCaja.addRow(data);
            }

            if (modeloCaja.getRowCount() > 0) {
                btnExportarCaja.setEnabled(true);
            }

            rs.close();
            st.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(CargaCopere.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void CargarDatosOprefa() {
        try {
            int cont = 1;
            modeloOprefa.setRowCount(0);

            Object[] data = new Object[4];
            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "SELECT DNI, Monto_Descuento FROM EstructuraOprefa WHERE Estado = 1";
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                data[0] = cont;
                data[1] = rs.getString(1);
                data[2] = rs.getString(2);
                data[3] = "";
                modeloOprefa.addRow(data);
                cont++;
            }

            if (modeloOprefa.getRowCount() > 0) {
                btnExportarOprefa.setEnabled(true);
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

    public class Modelo3 extends DefaultTableModel {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    /**
     * Creates new form CargaCopere
     */
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
            for (int i = 0; i < modeloCopere.getColumnCount(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(modeloCopere.getColumnName(i));  // Asigna el nombre de la columna como encabezado
                cell.setCellStyle(headerStyle);  // Aplicar estilo de encabezado
            }

            // Crear filas con los datos
            for (int rowIndex = 0; rowIndex < modeloCopere.getRowCount(); rowIndex++) {
                Row row = sheet.createRow(rowIndex + 1);  // +1 porque la fila 0 es para encabezados
                for (int colIndex = 0; colIndex < modeloCopere.getColumnCount(); colIndex++) {
                    Cell cell = row.createCell(colIndex);
                    Object value = modeloCopere.getValueAt(rowIndex, colIndex);
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
            for (int i = 0; i < modeloCopere.getColumnCount(); i++) {
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

    private void exportarXLSOprefa() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar como");

        int mesSeleccionado = jdc_mes.getMonth();
        String mesEnTexto = new DateFormatSymbols().getShortMonths()[mesSeleccionado].toUpperCase();
        int añoSeleccionado = jdc_año.getYear();
        String nombreArchivo = "8070 ACMP_" + mesEnTexto + " " + añoSeleccionado + ".xls";

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

            CellStyle numberStyle = workbook.createCellStyle();
            numberStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00"));

            Font font = workbook.createFont();
            font.setFontName("Arial");
            font.setFontHeightInPoints((short) 11);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(font);

            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setFont(font);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < modeloOprefa.getColumnCount(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(modeloOprefa.getColumnName(i));
                cell.setCellStyle(headerStyle);
            }

            for (int rowIndex = 0; rowIndex < modeloOprefa.getRowCount(); rowIndex++) {
                Row row = sheet.createRow(rowIndex + 1);
                for (int colIndex = 0; colIndex < modeloOprefa.getColumnCount(); colIndex++) {
                    Cell cell = row.createCell(colIndex);
                    Object value = modeloOprefa.getValueAt(rowIndex, colIndex);
                    if (value != null) {
                        if (colIndex == 2) {
                            cell.setCellValue(Double.parseDouble(value.toString()));
                            cell.setCellStyle(numberStyle);
                        } else {
                            cell.setCellValue(value.toString());
                        }
                    }
                }
            }

            for (int i = 0; i < modeloOprefa.getColumnCount(); i++) {
                sheet.autoSizeColumn(i);
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
            "NUM_CHEQUE",
            "TOTAL APORTE"
        };

        modeloCopere.setColumnIdentifiers(columnNames);

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

        modeloCaja.setColumnIdentifiers(columnNames);

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

    private void cargarEstructuraOprefa() {
        String[] columnNames = new String[]{
            "N°",
            "DNI",
            "MONTO",
            "OBSERVACIONES"
        };

        modeloOprefa.setColumnIdentifiers(columnNames);

        JTableHeader header = tbOprefa.getTableHeader();
        header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 40));

        header.setBackground(new java.awt.Color(255, 217, 102));
        tbOprefa.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

        TableColumnModel columnModel = tbOprefa.getColumnModel();
        for (int col = 0; col < tbOprefa.getColumnCount(); col++) {
            TableColumn tableColumn = columnModel.getColumn(col);
            Component comp = header.getDefaultRenderer().getTableCellRendererComponent(tbOprefa, tableColumn.getHeaderValue(), false, false, 0, col);
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
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        btnAfiliadoCopere = new javax.swing.JButton();
        btnNoProcesadosCopere = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbCopere = new javax.swing.JTable();
        txtBuscarCopere = new javax.swing.JTextField();
        btnBuscarCopere = new javax.swing.JButton();
        btnExportarCopere = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnAfiliadoCaja = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbCajaPensiones = new javax.swing.JTable();
        txtBuscarCaja = new javax.swing.JTextField();
        btnBuscarCaja = new javax.swing.JButton();
        btnExportarCaja = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        btnNoProcesadosCaja = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        btnAfiliadoOprefa = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tbOprefa = new javax.swing.JTable();
        txtBuscarOprefa = new javax.swing.JTextField();
        btnBuscarOprefa = new javax.swing.JButton();
        btnExportarOprefa = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        btnNoProcesadosOprefa = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        MenuRegistrarEmpleado = new javax.swing.JMenuItem();

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

        jLabel14.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel14.setText("Mes");

        jdc_mes.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jdc_mes.setForeground(new java.awt.Color(255, 0, 0));
        jdc_mes.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N

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

        jButton1.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jButton1.setText("Cargar Datos");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
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
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_razonsocial1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_ruc1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jdc_mes, javax.swing.GroupLayout.PREFERRED_SIZE, 29, Short.MAX_VALUE)
                    .addComponent(jdc_año, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Opciones", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 12))); // NOI18N

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        jTabbedPane1.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        btnAfiliadoCopere.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAfiliadoCopere.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_add_32px.png"))); // NOI18N
        btnAfiliadoCopere.setText("Registrar Afiliado");
        btnAfiliadoCopere.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAfiliadoCopereActionPerformed(evt);
            }
        });

        btnNoProcesadosCopere.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnNoProcesadosCopere.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/flecha-hacia-abajo.png"))); // NOI18N
        btnNoProcesadosCopere.setText("Modulo de No Procesados");
        btnNoProcesadosCopere.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNoProcesadosCopereActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Datos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 12))); // NOI18N

        tbCopere.setModel(modeloCopere);
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

        btnBuscarCopere.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBuscarCopere.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buscar.png"))); // NOI18N
        btnBuscarCopere.setText("Buscar");
        btnBuscarCopere.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarCopereActionPerformed(evt);
            }
        });

        btnExportarCopere.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnExportarCopere.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/sobresalir.png"))); // NOI18N
        btnExportarCopere.setText("Guardar y Exportar EXCEL");
        btnExportarCopere.setEnabled(false);
        btnExportarCopere.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportarCopereActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel1.setText("Apellidos y Nombres:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBuscarCopere)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscarCopere, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnExportarCopere, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtBuscarCopere, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnBuscarCopere))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                .addGap(9, 9, 9)
                .addComponent(btnExportarCopere, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap(516, Short.MAX_VALUE)
                        .addComponent(btnAfiliadoCopere, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNoProcesadosCopere))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAfiliadoCopere)
                    .addComponent(btnNoProcesadosCopere, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("COPERE", jPanel3);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

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

        tbCajaPensiones.setModel(modeloCaja);
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

        btnBuscarCaja.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBuscarCaja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buscar.png"))); // NOI18N
        btnBuscarCaja.setText("Buscar");
        btnBuscarCaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarCajaActionPerformed(evt);
            }
        });

        btnExportarCaja.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnExportarCaja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Rich Text Converter_1.png"))); // NOI18N
        btnExportarCaja.setText("Guardar y Exportar TXT");
        btnExportarCaja.setEnabled(false);
        btnExportarCaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportarCajaActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel2.setText("Apellidos y Nombres:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBuscarCaja)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscarCaja, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnExportarCaja, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBuscarCaja, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarCaja)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                .addGap(9, 9, 9)
                .addComponent(btnExportarCaja, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        btnNoProcesadosCaja.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnNoProcesadosCaja.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/flecha-hacia-abajo.png"))); // NOI18N
        btnNoProcesadosCaja.setText("Modulo de No Procesados");
        btnNoProcesadosCaja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNoProcesadosCajaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAfiliadoCaja, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNoProcesadosCaja))
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNoProcesadosCaja, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAfiliadoCaja, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("CAJA DE PENSIONES", jPanel4);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        btnAfiliadoOprefa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnAfiliadoOprefa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_add_32px.png"))); // NOI18N
        btnAfiliadoOprefa.setText("Registrar Afiliado");
        btnAfiliadoOprefa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAfiliadoOprefaActionPerformed(evt);
            }
        });

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Datos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 12))); // NOI18N

        tbOprefa.setModel(modeloOprefa);
        tbOprefa.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tbOprefa.setFocusTraversalPolicyProvider(true);
        tbOprefa.setRowHeight(32);
        tbOprefa.setSelectionBackground(new java.awt.Color(0, 153, 204));
        tbOprefa.setShowGrid(true);
        tbOprefa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbOprefaMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tbOprefa);
        if (tbOprefa.getColumnModel().getColumnCount() > 0) {
            tbOprefa.getColumnModel().getColumn(0).setResizable(false);
            tbOprefa.getColumnModel().getColumn(1).setResizable(false);
            tbOprefa.getColumnModel().getColumn(2).setResizable(false);
            tbOprefa.getColumnModel().getColumn(3).setResizable(false);
            tbOprefa.getColumnModel().getColumn(4).setResizable(false);
            tbOprefa.getColumnModel().getColumn(5).setResizable(false);
        }

        btnBuscarOprefa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBuscarOprefa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buscar.png"))); // NOI18N
        btnBuscarOprefa.setText("Buscar");
        btnBuscarOprefa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarOprefaActionPerformed(evt);
            }
        });

        btnExportarOprefa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnExportarOprefa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/sobresalir.png"))); // NOI18N
        btnExportarOprefa.setText("Guardar y Exportar EXCEL");
        btnExportarOprefa.setEnabled(false);
        btnExportarOprefa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportarOprefaActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel3.setText("Apellidos y Nombres:");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBuscarOprefa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscarOprefa, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnExportarOprefa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtBuscarOprefa, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnBuscarOprefa))
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE)
                .addGap(9, 9, 9)
                .addComponent(btnExportarOprefa, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        btnNoProcesadosOprefa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnNoProcesadosOprefa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/flecha-hacia-abajo.png"))); // NOI18N
        btnNoProcesadosOprefa.setText("Modulo de No Procesados");
        btnNoProcesadosOprefa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNoProcesadosOprefaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAfiliadoOprefa, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNoProcesadosOprefa))
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNoProcesadosOprefa, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAfiliadoOprefa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        jMenu1.setText("Opciones");

        MenuRegistrarEmpleado.setText("Registrar Empleados");
        MenuRegistrarEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuRegistrarEmpleadoActionPerformed(evt);
            }
        });
        jMenu1.add(MenuRegistrarEmpleado);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

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

    private void btnBuscarCopereActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarCopereActionPerformed
        TableRowSorter<DefaultTableModel> rowSorter = new TableRowSorter<>(modeloCopere);
        tbCopere.setRowSorter(rowSorter);
        if (!txtBuscarCopere.getText().isEmpty()) {
            RowFilter<DefaultTableModel, Object> filter = RowFilter.regexFilter(txtBuscarCopere.getText());
            rowSorter.setRowFilter(filter);
            txtBuscarCopere.setText("");
        }
    }//GEN-LAST:event_btnBuscarCopereActionPerformed

    private void btnAfiliadoCopereActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAfiliadoCopereActionPerformed
        RegistroAfiliadoCopere ini = new RegistroAfiliadoCopere(this, true);
        ini.setVisible(true);
    }//GEN-LAST:event_btnAfiliadoCopereActionPerformed

    private void btnNoProcesadosCopereActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNoProcesadosCopereActionPerformed
        NoProcesadosCOPERE ini = new NoProcesadosCOPERE(this, true);
        ini.setVisible(true);
    }//GEN-LAST:event_btnNoProcesadosCopereActionPerformed

    private void btnExportarCopereActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarCopereActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Esta seguro que desea GUARDAR/REEMPLAZAR la informacion COPERE?", "Confirmación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            mostrarDialogoProgreso(this);
        }

        if (JOptionPane.showConfirmDialog(null, "Desea EXPORTAR el archivo XLS COPERE", "Confirmación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            exportarXLSCopere();
        }
    }//GEN-LAST:event_btnExportarCopereActionPerformed

    private void tbCopereMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbCopereMouseClicked
        if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
            DetalladoCopere ini = new DetalladoCopere(this, true, modeloCopere.getValueAt(tbCopere.getSelectedRow(), 2).toString());
            ini.setVisible(true);
        }
    }//GEN-LAST:event_tbCopereMouseClicked

    private void btnAfiliadoCajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAfiliadoCajaActionPerformed
        RegistroAfiliadoCaja ini = new RegistroAfiliadoCaja(this, true);
        ini.setVisible(true);
    }//GEN-LAST:event_btnAfiliadoCajaActionPerformed

    private void tbCajaPensionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbCajaPensionesMouseClicked
        if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
            DetalladoCajaP ini = new DetalladoCajaP(this, true, modeloCaja.getValueAt(tbCajaPensiones.getSelectedRow(), 1).toString());
            ini.setVisible(true);
        }

    }//GEN-LAST:event_tbCajaPensionesMouseClicked

    private void btnBuscarCajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarCajaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarCajaActionPerformed

    private void btnExportarCajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarCajaActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Esta seguro que desea GUARDAR/REEMPLAZAR la informacion CAJA DE PENSIONES?", "Confirmación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            mostrarDialogoProgresoCajaPensiones(this);
        }

        if (JOptionPane.showConfirmDialog(null, "Desea EXPORTAR el archivo TXT CAJA DE PENSIONES", "Confirmación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            saveTableToFile(tbCajaPensiones, jdc_año.getYear(), (jdc_mes.getMonth() + 1));
        }

    }//GEN-LAST:event_btnExportarCajaActionPerformed

    private void btnAfiliadoOprefaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAfiliadoOprefaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAfiliadoOprefaActionPerformed

    private void tbOprefaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbOprefaMouseClicked
        if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
            DetalladoOprefa ini = new DetalladoOprefa(this, true, modeloOprefa.getValueAt(tbOprefa.getSelectedRow(), 1).toString());
            ini.setVisible(true);
        }
    }//GEN-LAST:event_tbOprefaMouseClicked

    private void btnBuscarOprefaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarOprefaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBuscarOprefaActionPerformed

    private void btnExportarOprefaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarOprefaActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Esta seguro que desea GUARDAR/REEMPLAZAR la informacion OPREFA?", "Confirmación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            mostrarDialogoProgresoOprefa(this);
        }

        if (JOptionPane.showConfirmDialog(null, "Desea EXPORTAR el archivo EXCEL OPREFA?", "Confirmación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            exportarXLSOprefa();
        }

    }//GEN-LAST:event_btnExportarOprefaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        CargarDatosOprefa();
        CargarDatosCajaPensiones();
        CargarDatosCopere();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void MenuRegistrarEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuRegistrarEmpleadoActionPerformed
        RegistrarEmpleado dialog = new RegistrarEmpleado(CargaCopere.this);
        dialog.setVisible(true); // Mostrar el JDialog
    }//GEN-LAST:event_MenuRegistrarEmpleadoActionPerformed

    private void btnNoProcesadosCajaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNoProcesadosCajaActionPerformed
        NoProcesadosCAJAP ini = new NoProcesadosCAJAP(this, true);
        ini.setVisible(true);
    }//GEN-LAST:event_btnNoProcesadosCajaActionPerformed

    private void btnNoProcesadosOprefaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNoProcesadosOprefaActionPerformed
        NoProcesadosOPREFA ini = new NoProcesadosOPREFA(this, true);
        ini.setVisible(true);
    }//GEN-LAST:event_btnNoProcesadosOprefaActionPerformed

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
            modeloCopere.setRowCount(0);
            cnn = Conexion.getConnection();
            String sql = "SELECT Cod_Descuento = '8070', Numero_CIP, Monto_Descuento FROM EstructuraCopere WHERE Estado = 1";
            st = cnn.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                data[0] = jdc_año.getYear() + "" + String.format("%02d", (jdc_mes.getMonth() + 1));
                for (int i = 1; i < data.length; i++) {
                    data[i] = rs.getString(i);
                }

                modeloCopere.addRow(data);
            }

            if (modeloCopere.getRowCount() > 0) {
                btnExportarCopere.setEnabled(true);
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
    private javax.swing.JMenuItem MenuRegistrarEmpleado;
    private javax.swing.JButton btnAfiliadoCaja;
    private javax.swing.JButton btnAfiliadoCopere;
    private javax.swing.JButton btnAfiliadoOprefa;
    private javax.swing.JButton btnBuscarCaja;
    private javax.swing.JButton btnBuscarCopere;
    private javax.swing.JButton btnBuscarOprefa;
    private javax.swing.JButton btnExportarCaja;
    private javax.swing.JButton btnExportarCopere;
    private javax.swing.JButton btnExportarOprefa;
    private javax.swing.JButton btnNoProcesadosCaja;
    private javax.swing.JButton btnNoProcesadosCopere;
    private javax.swing.JButton btnNoProcesadosOprefa;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    public static com.toedter.calendar.JYearChooser jdc_año;
    public static com.toedter.calendar.JMonthChooser jdc_mes;
    private javax.swing.JTable tbCajaPensiones;
    private javax.swing.JTable tbCopere;
    private javax.swing.JTable tbOprefa;
    private javax.swing.JTextField txtBuscarCaja;
    private javax.swing.JTextField txtBuscarCopere;
    private javax.swing.JTextField txtBuscarOprefa;
    private javax.swing.JLabel txt_razonsocial1;
    private javax.swing.JLabel txt_ruc1;
    // End of variables declaration//GEN-END:variables
}
