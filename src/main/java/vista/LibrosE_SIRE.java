/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import Controlador.ExcelExporter;
import Controlador.FuncionesGlobales;
import static Controlador.FuncionesGlobales.getLastDirectory;
import static Controlador.FuncionesGlobales.saveLastDirectory;
import com.formdev.flatlaf.FlatIntelliJLaf;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import org.apache.poi.ss.usermodel.Cell;
import static org.apache.poi.ss.usermodel.CellType.BOOLEAN;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Farmacia-Ingeniero
 */
public class LibrosE_SIRE extends javax.swing.JFrame {

    private final Modelo modelo1 = new Modelo();

    private final Modelo modelo2 = new Modelo();

    TableRowSorter trsfiltro, trsfiltro2;

    /**
     * Creates new form LibrosE_SIRE
     */
    public LibrosE_SIRE() {
        initComponents();
        this.setIconImage(new ImageIcon(System.getProperty("user.dir") + "/logoACMP.png").getImage());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Acciones que deseas realizar antes de cerrar el JFrame
                setVisible(false); // Oculta el JFrame actual
                // Muestra otro JFrame anterior
                Principal anteriorFrame = new Principal();
                anteriorFrame.setVisible(true);
            }
        });

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Obtener la fecha actual
        LocalDate fechaActual = LocalDate.now();

        // Restar un mes a la fecha actual
        LocalDate fechaMesAnterior = fechaActual.minusMonths(1);

        // Configurar el JDateChooser con la fecha del mes anterior
        jMonthChooser1.setMonth(fechaMesAnterior.getMonthValue() - 1);
        jMonthChooser3.setMonth(fechaMesAnterior.getMonthValue() - 1);

        // Configurar el JYearChooser con el año del mes anterior
        jYearChooser1.setYear(fechaMesAnterior.getYear());
        jYearChooser3.setYear(fechaMesAnterior.getYear());

//        jTextField7.getDocument().addDocumentListener(new DocumentListener() {
//            @Override
//            public void insertUpdate(DocumentEvent e) {
//                if (rb_emision.isSelected()) {
//                    filterTable(TablaInicial, jTextField7.getText(), 1);
//                } else if (rb_serie.isSelected()) {
//                    filterTable(TablaInicial, jTextField7.getText(), 6);
//                } else if (rb_comprobante.isSelected()) {
//                    filterTable(TablaInicial, jTextField7.getText(), 7);
//                } else if (rb_documento.isSelected()) {
//                    filterTable(TablaInicial, jTextField7.getText(), 9);
//                } else if (rb_razonsocial.isSelected()) {
//                    filterTable(TablaInicial, jTextField7.getText(), 10);
//                }
//            }
//
//            @Override
//            public void removeUpdate(DocumentEvent e) {
//                if (rb_emision.isSelected()) {
//                    filterTable(TablaInicial, jTextField7.getText(), 1);
//                } else if (rb_serie.isSelected()) {
//                    filterTable(TablaInicial, jTextField7.getText(), 6);
//                } else if (rb_comprobante.isSelected()) {
//                    filterTable(TablaInicial, jTextField7.getText(), 7);
//                } else if (rb_documento.isSelected()) {
//                    filterTable(TablaInicial, jTextField7.getText(), 9);
//                } else if (rb_razonsocial.isSelected()) {
//                    filterTable(TablaInicial, jTextField7.getText(), 10);
//                }
//            }
//
//            @Override
//            public void changedUpdate(DocumentEvent e) {
//                if (rb_emision.isSelected()) {
//                    filterTable(TablaInicial, jTextField7.getText(), 1);
//                } else if (rb_serie.isSelected()) {
//                    filterTable(TablaInicial, jTextField7.getText(), 6);
//                } else if (rb_comprobante.isSelected()) {
//                    filterTable(TablaInicial, jTextField7.getText(), 7);
//                } else if (rb_documento.isSelected()) {
//                    filterTable(TablaInicial, jTextField7.getText(), 9);
//                } else if (rb_razonsocial.isSelected()) {
//                    filterTable(TablaInicial, jTextField7.getText(), 10);
//                }
//            }
//        });
//
//        TablaInicial.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent e) {
//                if (!e.getValueIsAdjusting()) {
//                    FuncionesGlobales.seleccionarFilas2(TablaInicial, TablaProcesada, 6, 7, 7, 8);
//                }
//            }
//        });
        llenar_tabla();
        llenar_tabla2();
    }

    public void filterTable(JTable table, String filterText, int indiceColumna) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        TableRowSorter<DefaultTableModel> rowSorter = (TableRowSorter<DefaultTableModel>) table.getRowSorter();
        if (filterText.trim().length() == 0) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + filterText, indiceColumna));
        }
    }

    public class Modelo extends DefaultTableModel {

        @Override
        public boolean isCellEditable(int row, int column) {
            return true;
        }
    }

    private void llenar_tabla() {
        String[] columnNames = new String[]{
            "RUC",
            "ID",
            "Periodo",
            "CAR SUNAT",
            "Fecha de emisión",
            "Fecha Vcto/Pago",
            "Tipo CP/Doc.",
            "Serie del CDP",
            "Nro CP o Doc Nro Inicial (Rango)",
            "Nro Final (Rango)",
            "Tipo Doc Identidad",
            "Nro Doc Identidad",
            "Apellidos Nombres/ Razon  Social",
            "Valor Facturado Exportación",
            "BI Gravada",
            "Dscto BI",
            "IGV / IPM DG",
            "Dscto IGV / IPM",
            "Mto Exonerado",
            "Mto Inafecto",
            "ISC",
            "BI Grav IVAP",
            "IVAP",
            "ICBPER",
            "Otros Tributos",
            "Total CP",
            "Moneda",
            "Tipo de Cambio",
            "Fecha Emision Doc Modificado",
            "Tipo CP Modificado",
            "Serie CP Modificado",
            "Nro CP Modificado",
            "ID Proyecto Operadores Atribución",
            "Existe en Propuesta"
        };

        modelo1.setColumnIdentifiers(columnNames);

        JTableHeader header = TablaProcesada.getTableHeader();
        header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 40)); // Establece la altura deseada (por ejemplo, 30 píxeles)

        // Cambiar el color del encabezado
        header.setBackground(new java.awt.Color(255, 217, 102)); // Melon (RGB: 255, 140, 105)
        TablaProcesada.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

        TableColumnModel columnModel = TablaProcesada.getColumnModel();
        for (int col = 0; col < TablaProcesada.getColumnCount(); col++) {
            TableColumn tableColumn = columnModel.getColumn(col);
            Component comp = header.getDefaultRenderer().getTableCellRendererComponent(TablaProcesada, tableColumn.getHeaderValue(), false, false, 0, col);
            int width = comp.getPreferredSize().width + 25;
            tableColumn.setPreferredWidth(width);
        }
    }

    private void llenar_tabla2() {
        String[] columnNames = new String[]{
            "RUC",
            "ID",
            "Periodo",
            "CAR SUNAT",
            "Fecha de emisión",
            "Fecha Vcto/Pago",
            "Tipo CP/Doc.",
            "Serie del CDP",
            "Año",
            "Nro CP o Doc. Nro Inicial (Rango)",
            "Nro Final (Rango)",
            "Tipo Doc Identidad",
            "Nro Doc Identidad",
            "Apellidos Nombres/ Razon Social",
            "BI Gravado DG",
            "IGV / IPM DG",
            "BI Gravado DGNG",
            "IGV / IPM DGNG",
            "BI Gravado DNG",
            "IGV / IPM DNG",
            "Valor Adq. NG",
            "ISC",
            "ICBPER",
            "Otros Trib/ Cargos",
            "Total CP", "Moneda",
            "Tipo de Cambio",
            "Fecha Emision Doc Modificado",
            "Tipo CP Modificado",
            "Serie CP Modificado",
            "COD. DAM O DSI",
            "Nro CP Modificado",
            "Clasif de Bss y Sss",
            "ID Proyecto Operadores/Partícip es",
            "PorcPart",
            "IMB",
            "CAR Orig",
            "Existe Propuesta"
        };

        modelo2.setColumnIdentifiers(columnNames);

        JTableHeader header = TablaProcesada2.getTableHeader();
        header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 40)); // Establece la altura deseada (por ejemplo, 30 píxeles)

        // Cambiar el color del encabezado
        header.setBackground(new java.awt.Color(255, 217, 102)); // Melon (RGB: 255, 140, 105)
        TablaProcesada2.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

        TableColumnModel columnModel = TablaProcesada2.getColumnModel();
        for (int col = 0; col < TablaProcesada2.getColumnCount(); col++) {
            TableColumn tableColumn = columnModel.getColumn(col);
            Component comp = header.getDefaultRenderer().getTableCellRendererComponent(TablaProcesada2, tableColumn.getHeaderValue(), false, false, 0, col);
            int width = comp.getPreferredSize().width + 25;
            tableColumn.setPreferredWidth(width);
        }
    }

    public DefaultTableModel readExcel(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0); // Assuming the first sheet

        // Create model
        DefaultTableModel model = new DefaultTableModel(new Object[]{
            "Cod. Operación",
            "Fecha de Emisión",
            "",
            "Fecha Vencimiento",
            "",
            "Tipo Comprobante",
            "N° de Serie",
            "N° Comprobante",
            "Tipo Documento",
            "N° Documento",
            "Razon Social",
            "Valor Facturado",
            "Base - Operación Grabada",
            "Importe Exonerado",
            "Importe Inafecta",
            "ISC",
            "",
            "IGV",
            "Otros Tributos",
            "Importe Total",
            "Tipo de Cambio",
            "Fecha Modifica",
            "Tipo Modifica",
            "Serie Modifica",
            "N° Modifica"
        }, 0);

        // Read data
        int numRows = sheet.getLastRowNum() + 1;
        int startRow = 9; // Starting from row 10
        for (int i = startRow; i < numRows - 1; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Object[] rowData = new Object[25]; // Assuming 15 columns
                for (int j = 0; j < 25; j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    switch (cell.getCellType()) {
                        case STRING:
                            rowData[j] = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                Date dateCellValue = cell.getDateCellValue();
                                rowData[j] = new SimpleDateFormat("dd/MM/yyyy").format(dateCellValue);
                            } else {
                                double numericValue = cell.getNumericCellValue();
                                if (numericValue == Math.floor(numericValue)) {
                                    rowData[j] = (int) numericValue;
                                } else {
                                    rowData[j] = String.format("%.2f", numericValue);
                                }
                            }
                            break;
                        case BOOLEAN:
                            rowData[j] = cell.getBooleanCellValue();
                            break;
                        default:
                            rowData[j] = "";
                    }
                }
                model.addRow(rowData);
            }
        }

        workbook.close();
        inputStream.close();

        return model;
    }

    public DefaultTableModel readExcel2(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0); // Assuming the first sheet

        // Create model
        DefaultTableModel model = new DefaultTableModel(new Object[]{
            "Cod. Operación",
            "Fecha de Emisión",
            "",
            "Fecha Vencimiento",
            "",
            "Tipo Comprobante",
            "N° de Serie",
            "N° Comprobante",
            "Año",
            "Tipo Documento",
            "N° Documento",
            "Razon Social",
            "Base Imponible - Op Grab.",
            "IGV - Op Grab.",
            "Valor Adquis. - Op NO Grab.",
            "Otros Tributos",
            "",
            "Importe Total",
            "N° Constancia Detraccion",
            "Fecha Constancia Detraccion",
            "Tipo de Cambio",
            "Fecha Modifica",
            "Tipo Modifica",
            "Serie Modifica",
            "N° Modifica"
        }, 0);

        // Read data
        int numRows = sheet.getLastRowNum() + 1;
        int startRow = 9; // Starting from row 10
        for (int i = startRow; i < numRows - 1; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Object[] rowData = new Object[25]; // Assuming 15 columns
                for (int j = 0; j < 25; j++) {
                    Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    switch (cell.getCellType()) {
                        case STRING:
                            rowData[j] = cell.getStringCellValue();
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                Date dateCellValue = cell.getDateCellValue();
                                rowData[j] = new SimpleDateFormat("dd/MM/yyyy").format(dateCellValue);
                            } else {
                                double numericValue = cell.getNumericCellValue();
                                if (numericValue == Math.floor(numericValue)) {
                                    rowData[j] = (int) numericValue;
                                } else {
                                    rowData[j] = String.format("%.2f", numericValue);
                                }
                            }
                            break;
                        case BOOLEAN:
                            rowData[j] = cell.getBooleanCellValue();
                            break;
                        default:
                            rowData[j] = "";
                    }
                }
                model.addRow(rowData);
            }
        }

        workbook.close();
        inputStream.close();

        return model;
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

    public static void exportarATXT(JTable tabla, File archivoTXT) throws IOException {
        FileWriter writer = new FileWriter(archivoTXT);

        // Obtener el número de filas y columnas en la tabla
        int filas = tabla.getRowCount();
        int columnas = tabla.getColumnCount() - 1;

        // Iterar sobre cada fila de la tabla
        for (int i = 0; i < filas; i++) {
            // Iterar sobre cada columna de la fila
            for (int j = 0; j < columnas; j++) {
                Object valorCelda = tabla.getValueAt(i, j);
                if (valorCelda == null) {
                    valorCelda = "";
                }
                writer.write(valorCelda.toString()); // Escribir el valor de la celda
                if (j < columnas - 1) {
                    writer.write("|"); // Agregar un palote "|" como separador, excepto en la última columna
                }
            }
            writer.write("|||||||||||||||||");
            writer.write(System.lineSeparator()); // Agregar un salto de línea después de cada fila
        }

        writer.close();
    }

    public static void exportarATXT_COMP(JTable tabla, File archivoTXT) throws IOException {
        FileWriter writer = new FileWriter(archivoTXT);

        // Obtener el número de filas y columnas en la tabla
        int filas = tabla.getRowCount();
        int columnas = tabla.getColumnCount() - 1;

        // Iterar sobre cada fila de la tabla
        for (int i = 0; i < filas; i++) {
            // Obtener el valor en la columna 33 de la fila actual
            Object valorCelda = tabla.getValueAt(i, 33); // Se resta 1 porque el índice de columna en Java es base 0
            // Verificar si el valor en la columna 33 es "SI"
            if (valorCelda != null && valorCelda.toString().equals("NO")) {
                // Iterar sobre cada columna de la fila
                for (int j = 0; j < columnas; j++) {
                    valorCelda = tabla.getValueAt(i, j);
                    writer.write(valorCelda.toString()); // Escribir el valor de la celda
                    if (j < columnas - 1) {
                        writer.write("|"); // Agregar un palote "|" como separador, excepto en la última columna
                    }
                }
                writer.write("|||||||||||||||||");
                writer.write(System.lineSeparator()); // Agregar un salto de línea después de cada fila
            }
        }

        writer.close();
    }

    public static void exportarATXT_COMP2(JTable tabla, File archivoTXT) throws IOException {
        FileWriter writer = new FileWriter(archivoTXT);

        // Obtener el número de filas y columnas en la tabla
        int filas = tabla.getRowCount();
        int columnas = tabla.getColumnCount() - 1;

        // Iterar sobre cada fila de la tabla
        for (int i = 0; i < filas; i++) {
            // Obtener el valor en la columna 33 de la fila actual
            Object valorCelda = tabla.getValueAt(i, 37); // Se resta 1 porque el índice de columna en Java es base 0
            // Verificar si el valor en la columna 33 es "SI"
            if (valorCelda != null && valorCelda.toString().equals("NO")) {
                // Iterar sobre cada columna de la fila
                for (int j = 0; j < columnas; j++) {
                    valorCelda = tabla.getValueAt(i, j);
                    writer.write(valorCelda.toString()); // Escribir el valor de la celda
                    if (j < columnas - 1) {
                        writer.write("|"); // Agregar un palote "|" como separador, excepto en la última columna
                    }
                }
                writer.write("|||||||||||||||||");
                writer.write(System.lineSeparator()); // Agregar un salto de línea después de cada fila
            }
        }

        writer.close();
    }

    public static void generarZIP(File archivoTXT, String nombreZIP) throws IOException {
        File archivoZIP = new File(archivoTXT.getParent(), nombreZIP + ".zip");
        FileOutputStream fos = new FileOutputStream(archivoZIP);
        ZipOutputStream zos = new ZipOutputStream(fos);
        ZipEntry entrada = new ZipEntry(nombreZIP + ".txt");
        zos.putNextEntry(entrada);
        FileInputStream fis = new FileInputStream(archivoTXT);
        byte[] buffer = new byte[1024];
        int longitud;
        while ((longitud = fis.read(buffer)) > 0) {
            zos.write(buffer, 0, longitud);
        }
        zos.closeEntry();
        fis.close();
        zos.close();
    }

    public static List<String> readAndConcatenateFromZip(String zipFilePath) throws IOException {
        List<String> concatenatedDataList = new ArrayList<>();

        try (ZipFile zipFile = new ZipFile(zipFilePath)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (!entry.isDirectory() && entry.getName().endsWith(".txt")) {
                    InputStream inputStream = zipFile.getInputStream(entry);
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                    BufferedReader reader = new BufferedReader(inputStreamReader);

                    String line;
                    int lineCount = 0;
                    while ((line = reader.readLine()) != null) {
                        // Incrementar el contador de registros leídos
                        lineCount++;

                        // Comenzar a concatenar los datos después del segundo registro
                        if (lineCount > 1) {
                            // Dividir la cadena por el carácter "|"
                            String[] datos = line.split("\\|");
                            // Verificar si hay suficientes elementos en el array antes de acceder a las posiciones necesarias
                            if (datos.length > 21) { // Ajustar este número según la cantidad de datos que necesitas
                                // Concatenar los datos necesarios
                                StringBuilder concatenatedData = new StringBuilder();
                                concatenatedData.append(datos[4]).append(datos[6]).append(datos[7]).append(datos[8])
                                        .append(datos[10]).append(datos[11]).append(datos[12]).append(datos[14])
                                        .append(datos[16]).append(datos[25]); // Ajustar este número según la posición del último dato requerido
                                concatenatedData.append(System.lineSeparator()); // Agregar un salto de línea
                                concatenatedDataList.add(concatenatedData.toString());
                            }
                        }
                    }
                }
            }
        }

        return concatenatedDataList;
    }

    public static List<String> readAndConcatenateFromZip2(String zipFilePath) throws IOException {
        List<String> concatenatedDataList = new ArrayList<>();

        try (ZipFile zipFile = new ZipFile(zipFilePath)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (!entry.isDirectory() && entry.getName().endsWith(".txt")) {
                    InputStream inputStream = zipFile.getInputStream(entry);
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                    BufferedReader reader = new BufferedReader(inputStreamReader);

                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Dividir la cadena por el carácter "|"
                        String[] datos = line.split("\\|");
                        // Verificar si hay suficientes elementos en el array antes de acceder a las posiciones necesarias
                        if (datos.length > 24) { // Ajustar este número según la cantidad de datos que necesitas
                            // Concatenar los datos necesarios
                            StringBuilder concatenatedData = new StringBuilder();
                            concatenatedData.append(datos[4]).append(datos[6]).append(datos[7]).append(datos[9])
                                    .append(datos[11]).append(datos[12]).append(datos[13]).append(datos[14])
                                    .append(datos[15]).append(datos[20]).append(datos[24]); // Ajustar este número según la posición del último dato requerido
                            concatenatedData.append(System.lineSeparator()); // Agregar un salto de línea
                            concatenatedDataList.add(concatenatedData.toString());
                        }
                    }
                }
            }
        }

        return concatenatedDataList;
    }

    public void cargarDatosDesdeTxtt(DefaultTableModel tableModel, String rutaArchivo) {
        int cc = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;

            // Leer línea por línea el archivo
            while ((linea = br.readLine()) != null) {
                String[] columnas = linea.split("\\|");

                // Creamos un arreglo de tamaño acorde a la cantidad de columnas en el modelo
                Object[] fila = new Object[tableModel.getColumnCount()];

                fila[0] = txt_ruc.getText();
                fila[1] = txt_razonsocial.getText();
                // Vamos a saltar el dato 1, 2 y 3, y comenzar desde el dato 4
                fila[2] = columnas[0].substring(0, 6);
                if (columnas.length >= 4) {
                    // Colocar el cuarto dato en la columna 5
                    fila[4] = columnas[3]; // Colocar el cuarto dato del archivo en la columna 5

                    // Si hay más datos después del cuarto, los añadimos a las siguientes columnas
                    for (int i = 4; i < columnas.length - 11; i++) {
                        if (i < fila.length - 1) {
                            fila[i + 1] = columnas[i]; // Desplazar los datos hacia adelante

                            if (i == 7) {
                                fila[i + 1] = "";
                            }
                        }

                    }
                }
                // Agregar la fila con los datos nuevos al modelo
                tableModel.addRow(fila);
                cc++;
            }
            packColumns(TablaProcesada);
            JOptionPane.showMessageDialog(this, "Se han añadido " + cc + " registros a la tabla.", "Exito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cargarDatosDesdeTxt(DefaultTableModel tableModel, String rutaArchivo, int datosrestados) {
        int c = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;

            // Leer línea por línea el archivo
            while ((linea = br.readLine()) != null) {
                String[] columnas = linea.split("\\|");
                
                if (columnas[0].substring(0, 6).equals(jYearChooser3.getYear() + "" + String.format("%02d" , (jMonthChooser3.getMonth() + 1)))) {
                    // Creamos un arreglo de tamaño acorde a la cantidad de columnas en el modelo
                    Object[] fila = new Object[tableModel.getColumnCount()];

                    fila[0] = txt_ruc1.getText();
                    fila[1] = txt_razonsocial1.getText();
                    // Vamos a saltar el dato 1, 2 y 3, y comenzar desde el dato 4
                    fila[2] = columnas[0].substring(0, 6);
                    if (columnas.length >= 4) {
                        // Colocar el cuarto dato en la columna 5
                        fila[4] = columnas[3]; // Colocar el cuarto dato del archivo en la columna 5

                        // Si hay más datos después del cuarto, los añadimos a las siguientes columnas
                        for (int i = 4; i < columnas.length - datosrestados; i++) {
                            if (i < fila.length - 1) {
                                fila[i + 1] = columnas[i]; // Desplazar los datos hacia adelante
                                if (i == 7) {
                                    fila[i + 1] = "";
                                }
                            }
                        }
                    }
                    // Agregar la fila con los datos nuevos al modelo
                    tableModel.addRow(fila);
                    c++;
                }
            }
            packColumns(TablaProcesada2);
            JOptionPane.showMessageDialog(this, "Se han añadido " + c + " registros a la tabla.", "Exito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void cargarDatosDesdeTxt(DefaultTableModel tableModel, String rutaArchivo) {
        int c = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;

            // Leer línea por línea el archivo
            while ((linea = br.readLine()) != null) {
                String[] columnas = linea.split("\\|");

                System.out.println(jYearChooser1.getYear()+""+String.format("%02d", (jMonthChooser1.getMonth()+1)));
                System.out.println(columnas[0].substring(0, 6));
                if (columnas[0].substring(0, 6).equals(jYearChooser1.getYear()+""+String.format("%02d", (jMonthChooser1.getMonth()+1)))) {
                    // Creamos un arreglo de tamaño acorde a la cantidad de columnas en el modelo
                Object[] fila = new Object[tableModel.getColumnCount()];

                fila[0] = txt_ruc.getText();
                fila[1] = txt_razonsocial.getText();
                // Vamos a saltar el dato 1, 2 y 3, y comenzar desde el dato 4
                fila[2] = columnas[0].substring(0, 6);
                if (columnas.length >= 4) {
                    // Colocar el cuarto dato en la columna 5
                    fila[4] = columnas[3]; // Colocar el cuarto dato del archivo en la columna 5

                    // Si hay más datos después del cuarto, los añadimos a las siguientes columnas
                    for (int i = 4; i < columnas.length; i++) {
                        if (i < fila.length - 1) {
                            fila[i + 1] = columnas[i]; // Desplazar los datos hacia adelante

                        }

                    }
                }
                // Agregar la fila con los datos nuevos al modelo
                tableModel.addRow(fila);
                c++;
                }               
            }
            packColumns(TablaProcesada);
            JOptionPane.showMessageDialog(this, "Se han añadido " + c + " registros a la tabla.", "Exito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cargarDatosDesdeZipp(DefaultTableModel tableModel, String rutaArchivo) {
        int c = 0;
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(rutaArchivo))) {
            ZipEntry entry;

            // Buscar el archivo .txt dentro del ZIP
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith(".txt")) {
                    // Leer el contenido del archivo TXT
                    BufferedReader reader = new BufferedReader(new InputStreamReader(zis));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Dividir la línea por el delimitador "|"
                        String[] rowData = line.split("\\|");

                        // Si la tabla está vacía, añade las columnas
                        if (tableModel.getColumnCount() == 0) {
                            for (String column : rowData) {
                                tableModel.addColumn(column);
                            }
                        } else {
                            // Añadir la fila a la tabla
                            tableModel.addRow(rowData);
                            c++;
                        }
                    }
                    break; // Salir después de procesar el archivo .txt
                }
            }
            packColumns(TablaProcesada);
            JOptionPane.showMessageDialog(this, "Se han añadido " + c + " registros a la tabla.", "Exito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al procesar el archivo ZIP", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cargarDatosDesdeZip(DefaultTableModel tableModel, String rutaArchivo) {
        int c = 0;
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(rutaArchivo))) {
            ZipEntry entry;

            // Buscar el archivo .txt dentro del ZIP
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith(".txt")) {
                    // Leer el contenido del archivo TXT
                    BufferedReader reader = new BufferedReader(new InputStreamReader(zis));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Dividir la línea por el delimitador "|"
                        String[] rowData = line.split("\\|");

                        // Si la tabla está vacía, añade las columnas
                        if (tableModel.getColumnCount() == 0) {
                            for (String column : rowData) {
                                tableModel.addColumn(column);
                            }
                        } else {
                            // Añadir la fila a la tabla
                            tableModel.addRow(rowData);
                            c++;
                        }
                    }
                    break; // Salir después de procesar el archivo .txt
                }
            }
            packColumns(TablaProcesada2);
            JOptionPane.showMessageDialog(this, "Se han añadido " + c + " registros a la tabla.", "Exito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al procesar el archivo ZIP", "Error", JOptionPane.ERROR_MESSAGE);
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
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPasswordField1 = new javax.swing.JPasswordField();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TablaProcesada = new javax.swing.JTable();
        jSeparator2 = new javax.swing.JSeparator();
        btn_ReemplazarV = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jYearChooser1 = new com.toedter.calendar.JYearChooser();
        jLabel5 = new javax.swing.JLabel();
        jMonthChooser1 = new com.toedter.calendar.JMonthChooser();
        jTextField2 = new javax.swing.JTextField();
        btn_Rventas = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        txt_ruc = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txt_razonsocial = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txt_moneda = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        btn_Bventas = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        TablaProcesada2 = new javax.swing.JTable();
        jSeparator4 = new javax.swing.JSeparator();
        btn_ReemplazarC = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jYearChooser3 = new com.toedter.calendar.JYearChooser();
        jLabel14 = new javax.swing.JLabel();
        jMonthChooser3 = new com.toedter.calendar.JMonthChooser();
        jTextField5 = new javax.swing.JTextField();
        btn_Rcompras = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        txt_ruc1 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txt_razonsocial1 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txt_moneda1 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        btn_Bcompras = new javax.swing.JButton();
        jPanel16 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();

        jPasswordField1.setText("jPasswordField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Libros Electronicos SIRE");

        jTabbedPane1.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "Datos Procesados", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 12))); // NOI18N

        TablaProcesada.setModel(modelo1);
        TablaProcesada.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        TablaProcesada.setRowHeight(28);
        TablaProcesada.setShowHorizontalLines(true);
        TablaProcesada.setShowVerticalLines(true);
        jScrollPane2.setViewportView(TablaProcesada);

        btn_ReemplazarV.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btn_ReemplazarV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/WinRAR_1.png"))); // NOI18N
        btn_ReemplazarV.setText("REEMPLAZAR PROPUESTA RVIE");
        btn_ReemplazarV.setEnabled(false);
        btn_ReemplazarV.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btn_ReemplazarV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ReemplazarVActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(204, 0, 0));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("-");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1136, Short.MAX_VALUE)
                    .addComponent(jSeparator2)
                    .addComponent(btn_ReemplazarV, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 601, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_ReemplazarV, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "Datos / Carga de Archivo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 13))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel4.setText("Año:");

        jLabel5.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel5.setText("Año:");

        jMonthChooser1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jMonthChooser1.setForeground(new java.awt.Color(255, 0, 0));
        jMonthChooser1.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N

        jTextField2.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        jTextField2.setDisabledTextColor(new java.awt.Color(255, 0, 0));
        jTextField2.setEnabled(false);

        btn_Rventas.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btn_Rventas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_add_32px.png"))); // NOI18N
        btn_Rventas.setText("Abrir TXT PLE - RECREACIÓN");
        btn_Rventas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_RventasActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel11.setText("Razón Social:");

        txt_ruc.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        txt_ruc.setForeground(new java.awt.Color(255, 0, 0));
        txt_ruc.setText("20505606435");

        jLabel13.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel13.setText("RUC:");

        txt_razonsocial.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        txt_razonsocial.setForeground(new java.awt.Color(255, 0, 0));
        txt_razonsocial.setText("ASOCIACION CIRCULO MILITAR DEL PERU");

        jLabel15.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel15.setText("Tipo Moneda:");

        txt_moneda.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        txt_moneda.setForeground(new java.awt.Color(255, 0, 0));
        txt_moneda.setText("PEN");

        jTextField4.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        jTextField4.setDisabledTextColor(new java.awt.Color(255, 0, 0));
        jTextField4.setEnabled(false);

        btn_Bventas.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btn_Bventas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_add_32px.png"))); // NOI18N
        btn_Bventas.setText("Abrir TXT PLE - BAZAR");
        btn_Bventas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_BventasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jYearChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jMonthChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_ruc, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_razonsocial, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_moneda, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextField4, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btn_Bventas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btn_Rventas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jYearChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jMonthChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_ruc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_razonsocial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_moneda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_Rventas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_Bventas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField4))
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(0, 102, 153));

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Registro de Ventas Electronico (RVIE)");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("RVIE (VENTAS)", new javax.swing.ImageIcon(getClass().getResource("/images/Return Purchase_1.png")), jPanel2); // NOI18N

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "Datos Procesados", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 12))); // NOI18N

        TablaProcesada2.setModel(modelo2);
        TablaProcesada2.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        TablaProcesada2.setRowHeight(28);
        TablaProcesada2.setShowHorizontalLines(true);
        TablaProcesada2.setShowVerticalLines(true);
        jScrollPane5.setViewportView(TablaProcesada2);

        btn_ReemplazarC.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btn_ReemplazarC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/WinRAR_1.png"))); // NOI18N
        btn_ReemplazarC.setText("REEMPLAZAR PROPUESTA RCE");
        btn_ReemplazarC.setEnabled(false);
        btn_ReemplazarC.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btn_ReemplazarC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ReemplazarCActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Roboto", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 0, 0));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("-");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 1136, Short.MAX_VALUE)
                    .addComponent(jSeparator4)
                    .addComponent(btn_ReemplazarC, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 601, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(btn_ReemplazarC, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "Datos / Carga de Archivo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 13))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel12.setText("Año:");

        jLabel14.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel14.setText("Año:");

        jMonthChooser3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jMonthChooser3.setForeground(new java.awt.Color(255, 0, 0));
        jMonthChooser3.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N

        jTextField5.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        jTextField5.setDisabledTextColor(new java.awt.Color(255, 0, 0));
        jTextField5.setEnabled(false);

        btn_Rcompras.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btn_Rcompras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_add_32px.png"))); // NOI18N
        btn_Rcompras.setText("Abrir TXT PLE - RECREACIÓN");
        btn_Rcompras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_RcomprasActionPerformed(evt);
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
        txt_razonsocial1.setText("ASOCIACION CIRCULO MILITAR DEL PERU");

        jLabel18.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel18.setText("Tipo Moneda:");

        txt_moneda1.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        txt_moneda1.setForeground(new java.awt.Color(255, 0, 0));
        txt_moneda1.setText("PEN");

        jTextField6.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        jTextField6.setDisabledTextColor(new java.awt.Color(255, 0, 0));
        jTextField6.setEnabled(false);

        btn_Bcompras.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btn_Bcompras.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_add_32px.png"))); // NOI18N
        btn_Bcompras.setText("Abrir TXT PLE - BAZAR");
        btn_Bcompras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_BcomprasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jYearChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jMonthChooser3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_ruc1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_razonsocial1, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_moneda1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 145, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTextField6, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btn_Bcompras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btn_Rcompras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jYearChooser3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jMonthChooser3, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_ruc1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_razonsocial1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_moneda1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_Rcompras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_Bcompras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField6))
                .addContainerGap())
        );

        jPanel16.setBackground(new java.awt.Color(0, 102, 153));

        jLabel19.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Registro de Compras Electronico (RCE)");
        jLabel19.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 1072, Short.MAX_VALUE)
                .addGap(50, 50, 50))
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("RCE (COMPRAS)", new javax.swing.ImageIcon(getClass().getResource("/images/Buy.png")), jPanel12); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sumarcolumnascompras() {
        double baseimponible = 0.0, igv = 0.0, exo = 0.0, totalcp = 0.0;
        for (int i = 0; i < modelo2.getRowCount(); i++) {
            baseimponible = baseimponible + Double.parseDouble(modelo2.getValueAt(i, 14).toString());
            igv = igv + Double.parseDouble(modelo2.getValueAt(i, 15).toString());
            exo = exo + Double.parseDouble(modelo2.getValueAt(i, 20).toString());
            totalcp = totalcp + Double.parseDouble(modelo2.getValueAt(i, 24).toString());
        }
        
        NumberFormat formatoPeru = NumberFormat.getCurrencyInstance(new Locale("es", "PE"));

        jLabel2.setText("Base Imponible: " + formatoPeru.format(baseimponible) + "  |  IGV: " + formatoPeru.format(igv) + "  |  Valor Adq. NG: " + formatoPeru.format(exo) + "  |  Total CP: " + formatoPeru.format(totalcp) + "");
    }
    
    private void sumarcolumnascompras2() {
        double baseimponible = 0.0, igv = 0.0, exo = 0.0, totalcp = 0.0;
        for (int i = 0; i < modelo1.getRowCount(); i++) {
            baseimponible = baseimponible + Double.parseDouble(modelo1.getValueAt(i, 14).toString());
            igv = igv + Double.parseDouble(modelo1.getValueAt(i, 15).toString());
            exo = exo + Double.parseDouble(modelo1.getValueAt(i, 20).toString());
            totalcp = totalcp + Double.parseDouble(modelo1.getValueAt(i, 24).toString());
        }
        
        NumberFormat formatoPeru = NumberFormat.getCurrencyInstance(new Locale("es", "PE"));

        jLabel3.setText("Base Imponible: " + formatoPeru.format(baseimponible) + "  |  IGV: " + formatoPeru.format(igv) + "  |  Valor Adq. NG: " + formatoPeru.format(exo) + "  |  Total CP: " + formatoPeru.format(totalcp) + "");
    }
    
    private void validaciones_posteriores(){
        int c = 0;
        
        String fechavcto_eliminar = "01/01/0001";
        String tipocambio_reemplazar = "3.750";
        String fechadocmodificado_eliminar = "01/01/0001";
        String tipocpmodificado_eliminar = "00";
        String seriecpmodificado_eliminar = "-";
        String nrocpmodificado_eliminar = "-";
        
        for (int i = 0; i < modelo2.getRowCount(); i++) {
            if (modelo2.getValueAt(i, 5).equals(fechavcto_eliminar)) {
                modelo2.setValueAt("", i, 5);
                c++;
            }
        }
        
        for (int i = 0; i < modelo2.getRowCount(); i++) {
            if (modelo2.getValueAt(i, 32) != null) {
                if (modelo2.getValueAt(i, 32).equals(fechavcto_eliminar)) {
                modelo2.setValueAt("", i, 32);
                c++;
                }
            }
            
        }
        
        //JOptionPane.showMessageDialog(null, "Se han eliminado " + c + " Clasfi de Bss y Sss " + fechavcto_eliminar);
        
        c = 0;
        
        for (int i = 0; i < modelo2.getRowCount(); i++) {
            if (Double.parseDouble(modelo2.getValueAt(i, 26).toString()) == 0) {
                modelo2.setValueAt(tipocambio_reemplazar, i, 26);
                c++;
            }
        }
        
        //JOptionPane.showMessageDialog(null, "Se han reemplazado " + c + " Tipo de Cambio con el numero " + tipocambio_reemplazar);
        
        c = 0;
        
        for (int i = 0; i < modelo2.getRowCount(); i++) {
            if (modelo2.getValueAt(i, 27).equals(fechadocmodificado_eliminar)) {
                modelo2.setValueAt("", i, 27);
                c++;
            }
        }
        
        //JOptionPane.showMessageDialog(null, "Se han eliminado " + c + " Fecha Emision Doc Modificado con el formato " + fechadocmodificado_eliminar);
        
        c = 0;
        
        for (int i = 0; i < modelo2.getRowCount(); i++) {
            if (modelo2.getValueAt(i, 28).equals(tipocpmodificado_eliminar)) {
                modelo2.setValueAt("", i, 28);
                c++;
            }
        }
        
        //JOptionPane.showMessageDialog(null, "Se han eliminado " + c + " Tipo CP Modificado con el formato " + tipocpmodificado_eliminar);
        
        c = 0;
        
        for (int i = 0; i < modelo2.getRowCount(); i++) {
            if (modelo2.getValueAt(i, 29).equals(seriecpmodificado_eliminar)) {
                modelo2.setValueAt("", i, 29);
                c++;
            }
        }
        
        //JOptionPane.showMessageDialog(null, "Se han eliminado " + c + " Serie CP Modificado con el formato " + seriecpmodificado_eliminar);
        
        c = 0;
        
        for (int i = 0; i < modelo2.getRowCount(); i++) {
            if (modelo2.getValueAt(i, 31) != null) {
                if (modelo2.getValueAt(i, 31).equals(nrocpmodificado_eliminar)) {
                    modelo2.setValueAt("", i, 31);
                    c++;
                }
            }

        }
        
        //JOptionPane.showMessageDialog(null, "Se han eliminado " + c + " Nro CP Modificado con el formato " + nrocpmodificado_eliminar);
        
        
         c = 0;
        
        for (int i = 0; i < modelo2.getRowCount(); i++) {
            if (Double.parseDouble(modelo2.getValueAt(i, 24).toString()) < 0) {
                if (Double.parseDouble(modelo2.getValueAt(i, 20).toString()) > 0) {
                    modelo2.setValueAt("-"+modelo2.getValueAt(i, 20).toString(), i, 20);
                    c++;
                }
            }

        }
        
        JOptionPane.showMessageDialog(null, "Se han reemplazado " + c + " valores negativos en la columna 21 con totales negativos");
        
         c = 0;
        
        for (int i = 0; i < modelo2.getRowCount(); i++) {
            if (modelo2.getValueAt(i, 6).toString().equals("13")) {
                if (Double.parseDouble(modelo2.getValueAt(i, 20).toString()) < 0) {
                    //modelo2.setValueAt(String.format("%.2f" ,Double.parseDouble(modelo2.getValueAt(i, 14).toString()) + Double.parseDouble(modelo2.getValueAt(i, 20).toString())), i, 14);
                   // modelo2.setValueAt(String.format("%.2f" ,Double.parseDouble(modelo2.getValueAt(i, 14).toString()) * 0.18), i, 15);
                    modelo2.setValueAt("0.00", i, 20);
                    c++;
                }
                
                
            }

        }
        JOptionPane.showMessageDialog(null, "Se han reemplazado " + c + " valores negativos en la Columna 21 con totales positivos en tipo 13");
        
         c = 0;
        
        for (int i = 0; i < modelo2.getRowCount(); i++) {
            if (modelo2.getValueAt(i, 6).toString().equals("01")) {
                
                if (Double.parseDouble(modelo2.getValueAt(i, 20).toString()) < 0) {
                    //modelo2.setValueAt(String.format("%.2f" ,Double.parseDouble(modelo2.getValueAt(i, 14).toString()) + Double.parseDouble(modelo2.getValueAt(i, 20).toString())), i, 14);
                   // modelo2.setValueAt(String.format("%.2f" ,Double.parseDouble(modelo2.getValueAt(i, 14).toString()) * 0.18), i, 15);
                    modelo2.setValueAt("0.00", i, 20);
                    c++;
                }
                
                
            }

        }
        JOptionPane.showMessageDialog(null, "Se han reemplazado " + c + " valores negativos en la Columna 21 con totales positivos en tipo 1");
        
        
          c = 0;
        
        for (int i = 0; i < modelo2.getRowCount(); i++) {
            Object col28 = modelo2.getValueAt(i, 28);
            Object col29 = modelo2.getValueAt(i, 29); 

            if (col29 != null && !col29.toString().isEmpty() && (col28 == null || col28.toString().isEmpty())) {
                    modelo2.setValueAt("01", i, 28);
                    c++;
            }

        }
        
        //JOptionPane.showMessageDialog(null, "Se han reemplazado " + c + " tipo CP Modificado con series modificados");
        
         c = 0;
         
        Set<String> uniqueRecords = new HashSet<>();

        // Recorrer la tabla de principio a fin, eliminando duplicados
        for (int i = modelo2.getRowCount() - 1; i >= 0; i--) {
            // Obtener los valores de las columnas
            String ruc = modelo2.getValueAt(i, 0).toString();
            String tipoComprobante = modelo2.getValueAt(i, 6).toString();
            String serieComprobante = modelo2.getValueAt(i, 7).toString();
            String numeroComprobante = modelo2.getValueAt(i, 9).toString();
            String rucproveedor = modelo2.getValueAt(i, 12).toString();

            // Quitar ceros a la izquierda del número de comprobante
            String numeroSinCeros = numeroComprobante.replaceFirst("^0+(?!$)", "");

            // Concatenar los valores relevantes
            String combinedKey = ruc + "_" + tipoComprobante + "_" + serieComprobante + "_" + numeroSinCeros + "_" + rucproveedor;

            // Si la clave ya existe, eliminar el registro actual
            if (!uniqueRecords.add(combinedKey)) {
                System.out.println(combinedKey);
                modelo2.removeRow(i);
                c++;
            }
        }
        JOptionPane.showMessageDialog(null, "Se han eliminado " + c + " registros con Comprobante de Pagos DUPLICADOS");
        
    
    }
    
    private void btn_ReemplazarCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ReemplazarCActionPerformed
        // Reemplazar Propuesta RCE
        String nombreArchivo = "";
        String tipoMoneda = "";
        if (txt_moneda1.getText().equals("PEN")) {
            tipoMoneda = "1";
        } else if (txt_moneda1.getText().equals("USD")) {
            tipoMoneda = "2";
        }
        nombreArchivo = "LE" + txt_ruc.getText() + jYearChooser3.getYear() + String.format("%02d", (jMonthChooser3.getMonth() + 1)) + "00" + "080400" + "02" + "1" + "1" + tipoMoneda + "2";

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar archivo TXT");
        fileChooser.setSelectedFile(new File(nombreArchivo + ".txt"));

        // Filtro para mostrar solo archivos de texto
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de texto (*.txt)", "txt");
        fileChooser.setFileFilter(filter);

        int seleccion = fileChooser.showSaveDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivoTXT = fileChooser.getSelectedFile();

            try {
                btn_ReemplazarC.setEnabled(false);
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                exportarATXT(TablaProcesada2, archivoTXT);
                generarZIP(archivoTXT, nombreArchivo);
                JOptionPane.showMessageDialog(null, "<html>El archivo de <b>ENVIO ZIP</b> se ha exportado exitosamente con el siguiente nombre:<br><br><font color='red'><b>" + nombreArchivo + ".zip </font></b></html>", "Exportación Exitosa", JOptionPane.INFORMATION_MESSAGE);

                if (JOptionPane.showConfirmDialog(null, "¿Desea exportar un excel para verificar de informacion enviada?", "Confirmación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    ExcelExporter.exportToExcel2(archivoTXT.getAbsolutePath(), "RCE", TablaProcesada2, this, "REEMPLAZAR_PROPUESTA");
                    JOptionPane.showMessageDialog(null, "<html>El archivo de <b>EXCEL</b> se ha exportado exitosamente con el siguiente nombre:<br><br><font color='red'><b>" + nombreArchivo + "_REEMPLAZAR_PROPUESTA.xlsx </font></b></html>", "Exportación Exitosa", JOptionPane.INFORMATION_MESSAGE);
                }

                btn_ReemplazarC.setEnabled(true);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al exportar y comprimir: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        }
    }//GEN-LAST:event_btn_ReemplazarCActionPerformed

    private void btn_RcomprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_RcomprasActionPerformed
        String nombreArchivo = "";
        nombreArchivo = "LE" + txt_ruc1.getText() + jYearChooser3.getYear() + String.format("%02d", (jMonthChooser3.getMonth() + 1)) + "00" + "080100";

        jFileChooser1 = new JFileChooser();
        jFileChooser1.setDialogTitle("Seleccione Archivo TXT");
        jFileChooser1.setFileFilter(new FileNameExtensionFilter("Archivo TXT", "txt"));

        // Obtener la última ubicación guardada
        File lastDirectory = getLastDirectory();

        // Establecer la última ubicación como directorio inicial del JFileChooser
        if (lastDirectory != null && lastDirectory.exists()) {
            jFileChooser1.setCurrentDirectory(lastDirectory);
        }

        int result = jFileChooser1.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jFileChooser1.getSelectedFile();
            jTextField5.setText(selectedFile.getAbsolutePath());
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            btn_Rcompras.setEnabled(false);
            jYearChooser3.setEnabled(false);
            jMonthChooser3.setEnabled(false);

            if (!nombreArchivo.equals(selectedFile.getName().substring(0, 27))) {
                JOptionPane.showMessageDialog(this, "Verifique el tipo de archivo y/o seleccione el archivo txt, segun el mes y año seleccionado", "Error", JOptionPane.ERROR_MESSAGE);
                jTextField5.setText("");
                btn_Rcompras.setEnabled(true);
                jYearChooser3.setEnabled(true);
                jMonthChooser3.setEnabled(true);
                saveLastDirectory(selectedFile.getParentFile());
                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                return;
            }

            cargarDatosDesdeTxt(modelo2, selectedFile.getAbsolutePath(), 11);

            btn_Rcompras.setEnabled(true);
            btn_Bcompras.setEnabled(true);
            jYearChooser3.setEnabled(true);
            jMonthChooser3.setEnabled(true);
            btn_ReemplazarC.setEnabled(true);
            saveLastDirectory(selectedFile.getParentFile());

            validaciones_posteriores();
            sumarcolumnascompras();
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_btn_RcomprasActionPerformed

    private void btn_BcomprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BcomprasActionPerformed
        String nombreArchivo = "";
        nombreArchivo = "LE" + txt_ruc1.getText() + jYearChooser3.getYear() + String.format("%02d", (jMonthChooser3.getMonth() + 1)) + "00" + "080100";

        jFileChooser1 = new JFileChooser();
        jFileChooser1.setDialogTitle("Seleccione Archivo TXT");
        jFileChooser1.setFileFilter(new FileNameExtensionFilter("Archivo TXT", "txt"));

        // Obtener la última ubicación guardada
        File lastDirectory = getLastDirectory();

        // Establecer la última ubicación como directorio inicial del JFileChooser
        if (lastDirectory != null && lastDirectory.exists()) {
            jFileChooser1.setCurrentDirectory(lastDirectory);
        }

        int result = jFileChooser1.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jFileChooser1.getSelectedFile();
            jTextField6.setText(selectedFile.getAbsolutePath());
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            btn_Bcompras.setEnabled(false);
            jYearChooser3.setEnabled(false);
            jMonthChooser3.setEnabled(false);

            if (!nombreArchivo.equals(selectedFile.getName().substring(0, 27))) {
                JOptionPane.showMessageDialog(this, "Verifique el tipo de archivo y/o seleccione el archivo txt, segun el mes y año seleccionado", "Error", JOptionPane.ERROR_MESSAGE);
                jTextField6.setText("");
                btn_Bcompras.setEnabled(true);
                jYearChooser3.setEnabled(true);
                jMonthChooser3.setEnabled(true);
                saveLastDirectory(selectedFile.getParentFile());
                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                return;
            }

            cargarDatosDesdeTxt(modelo2, selectedFile.getAbsolutePath(), 11);

            btn_Bcompras.setEnabled(true);
            btn_Rcompras.setEnabled(true);
            jYearChooser3.setEnabled(true);
            jMonthChooser3.setEnabled(true);
            btn_ReemplazarC.setEnabled(true);
            saveLastDirectory(selectedFile.getParentFile());

            validaciones_posteriores();
            sumarcolumnascompras();

            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }

        //        String nombreArchivo = "";
        //        String tipoMoneda = "";
        //        nombreArchivo = "LE" + txt_ruc1.getText() + jYearChooser3.getYear() + String.format("%02d", (jMonthChooser3.getMonth() + 1)) + "00" + "080400";
        //
        //        jFileChooser1 = new JFileChooser();
        //        jFileChooser1.setDialogTitle("Choose ZIP File");
        //        jFileChooser1.setFileFilter(new FileNameExtensionFilter("ZIP files", "zip"));
        //        // Obtener la última ubicación guardada
        //        File lastDirectory = getLastDirectory();
        //
        //        // Establecer la última ubicación como directorio inicial del JFileChooser
        //        if (lastDirectory != null && lastDirectory.exists()) {
            //            jFileChooser1.setCurrentDirectory(lastDirectory);
            //        }
        //
        //        int result = jFileChooser1.showOpenDialog(this);
        //        if (result == JFileChooser.APPROVE_OPTION) {
            //            File selectedFile = jFileChooser1.getSelectedFile();
            //            jTextField6.setText(selectedFile.getAbsolutePath());
            //
            //            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            //            btn_Bcompras.setEnabled(false);
            //            jYearChooser3.setEnabled(false);
            //            jMonthChooser3.setEnabled(false);
            //
            //            if (!nombreArchivo.equals(selectedFile.getName().substring(0, 27))) {
                //                JOptionPane.showMessageDialog(this, "Verifique el tipo de archivo y/o seleccione el archivo txt, segun el mes y año seleccionado", "Error", JOptionPane.ERROR_MESSAGE);
                //                jTextField6.setText("");
                //                btn_Bcompras.setEnabled(true);
                //                jYearChooser3.setEnabled(true);
                //                jMonthChooser3.setEnabled(true);
                //                saveLastDirectory(selectedFile.getParentFile());
                //                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                //                return;
                //            }
            //
            //            cargarDatosDesdeZip(modelo2, selectedFile.getAbsolutePath());
            //
            //            btn_Bcompras.setEnabled(true);
            //            btn_Rcompras.setEnabled(true);
            //            jYearChooser3.setEnabled(true);
            //            jMonthChooser3.setEnabled(true);
            //            btn_ReemplazarC.setEnabled(true);
            //            saveLastDirectory(selectedFile.getParentFile());
            //            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            //        }
    }//GEN-LAST:event_btn_BcomprasActionPerformed

    private void Validaciones() {
//        int filasEliminadas = 0; // Contador para las filas eliminadas
//
//        // Eliminar filas que contengan el período "202405"
//        int totalFilas = modelo1.getRowCount();
//        for (int i = totalFilas - 1; i >= 0; i--) {
//            Object valor = modelo1.getValueAt(i, 3); // Obtener el valor de la columna 3
//            if (valor != null && valor.equals("202405")) { // Verificar la condición
//                modelo1.removeRow(i); // Eliminar la fila
//                filasEliminadas++;
//            }
//        }
//
//        // Mensaje de confirmación sobre las filas eliminadas
//        if (filasEliminadas > 0) {
//            JOptionPane.showMessageDialog(null, "Se han eliminado " + filasEliminadas + " filas con el período 202405.");
//        } else {
//            JOptionPane.showMessageDialog(null, "No se encontraron filas para eliminar con el período 202405");
//        }
        // Segundo bucle para validar y modificar valores
        for (int i = 0; i < modelo1.getRowCount(); i++) {
            modelo1.setValueAt("", i, 27);

            // Asegúrate de que el valor no sea null antes de compararlo
            if (modelo1.getValueAt(i, 10) != null && modelo1.getValueAt(i, 10).equals("0")) {
                modelo1.setValueAt("", i, 10);
            }
            if (modelo1.getValueAt(i, 11) != null && modelo1.getValueAt(i, 11).equals("0")) {
                modelo1.setValueAt("", i, 11);
            }
            if (modelo1.getValueAt(i, 12) != null && modelo1.getValueAt(i, 12).equals("-")) {
                modelo1.setValueAt("", i, 12);
            }

            if (modelo1.getValueAt(i, 10) != null && modelo1.getValueAt(i, 10).equals("")) {
                modelo1.setValueAt("1", i, 10);
            }
            if (modelo1.getValueAt(i, 11) != null && modelo1.getValueAt(i, 11).equals("")) {
                modelo1.setValueAt("00000001", i, 11);
            }
            if (modelo1.getValueAt(i, 12) != null
                    && (modelo1.getValueAt(i, 12).toString().contains("CLIENTES VARIOS")
                    || modelo1.getValueAt(i, 12).toString().contains("CLIENTE VARIOS")
                    || modelo1.getValueAt(i, 12).toString().contains("CLIENTE VARIOS,"))) {
                modelo1.setValueAt("", i, 12);
            }
            if (modelo1.getValueAt(i, 28) != null && modelo1.getValueAt(i, 28).equals("01/01/0001")) {
                modelo1.setValueAt("", i, 28);
            }
            if (modelo1.getValueAt(i, 29) != null && modelo1.getValueAt(i, 29).equals("00")) {
                modelo1.setValueAt("", i, 29);
            }
            if (modelo1.getValueAt(i, 30) != null && modelo1.getValueAt(i, 30).equals("-")) {
                modelo1.setValueAt("", i, 30);
            }
            if (modelo1.getValueAt(i, 31) != null && modelo1.getValueAt(i, 31).equals("-")) {
                modelo1.setValueAt("", i, 31);
            }
        }
    }
    
    private void btn_ReemplazarVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ReemplazarVActionPerformed
        Validaciones();
        // Reemplazar Propuesta RVIE
        String nombreArchivo = "";
        String tipoMoneda = "";
        if (txt_moneda1.getText().equals("PEN")) {
            tipoMoneda = "1";
        } else if (txt_moneda1.getText().equals("USD")) {
            tipoMoneda = "2";
        }
        nombreArchivo = "LE" + txt_ruc.getText() + jYearChooser1.getYear() + String.format("%02d", (jMonthChooser1.getMonth() + 1)) + "00" + "140400" + "02" + "1" + "1" + tipoMoneda + "2";

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar archivo TXT");
        fileChooser.setSelectedFile(new File(nombreArchivo + ".txt"));

        // Filtro para mostrar solo archivos de texto
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de texto (*.txt)", "txt");
        fileChooser.setFileFilter(filter);

        int seleccion = fileChooser.showSaveDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivoTXT = fileChooser.getSelectedFile();

            try {
                btn_ReemplazarV.setEnabled(false);
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                exportarATXT(TablaProcesada, archivoTXT);
                generarZIP(archivoTXT, nombreArchivo);
                JOptionPane.showMessageDialog(null, "<html>El archivo de <b>ENVIO ZIP</b> se ha exportado exitosamente con el siguiente nombre:<br><br><font color='red'><b>" + nombreArchivo + ".zip </font></b></html>", "Exportación Exitosa", JOptionPane.INFORMATION_MESSAGE);

                btn_ReemplazarV.setEnabled(true);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al exportar y comprimir: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        }
    }//GEN-LAST:event_btn_ReemplazarVActionPerformed

    private void btn_RventasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_RventasActionPerformed
        String nombreArchivo = "";
        nombreArchivo = "LE" + txt_ruc1.getText() + jYearChooser1.getYear() + String.format("%02d", (jMonthChooser1.getMonth() + 1)) + "00" + "140100";
        jFileChooser1 = new JFileChooser();
        jFileChooser1.setDialogTitle("Seleccione Archivo TXT");
        jFileChooser1.setFileFilter(new FileNameExtensionFilter("Archivo TXT", "txt"));

        // Obtener la última ubicación guardada
        File lastDirectory = getLastDirectory();

        // Establecer la última ubicación como directorio inicial del JFileChooser
        if (lastDirectory != null && lastDirectory.exists()) {
            jFileChooser1.setCurrentDirectory(lastDirectory);
        }

        int result = jFileChooser1.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jFileChooser1.getSelectedFile();
            jTextField2.setText(selectedFile.getAbsolutePath());
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            btn_Rventas.setEnabled(false);
            jYearChooser1.setEnabled(false);
            jMonthChooser1.setEnabled(false);

            if (!nombreArchivo.equals(selectedFile.getName().substring(0, 27))) {
                JOptionPane.showMessageDialog(this, "Verifique el tipo de archivo y/o seleccione el archivo txt, segun el mes y año seleccionado", "Error", JOptionPane.ERROR_MESSAGE);
                jTextField2.setText("");
                btn_Rventas.setEnabled(true);
                jYearChooser1.setEnabled(true);
                jMonthChooser1.setEnabled(true);
                saveLastDirectory(selectedFile.getParentFile());
                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                return;
            }

            cargarDatosDesdeTxt(modelo1, selectedFile.getAbsolutePath());
            
            sumarcolumnascompras2();

            btn_Rventas.setEnabled(true);
            btn_Bventas.setEnabled(true);
            jYearChooser3.setEnabled(true);
            jMonthChooser3.setEnabled(true);
            btn_ReemplazarV.setEnabled(true);
            saveLastDirectory(selectedFile.getParentFile());
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }//GEN-LAST:event_btn_RventasActionPerformed

    private void btn_BventasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BventasActionPerformed

        String nombreArchivo = "";
        nombreArchivo = "LE" + txt_ruc.getText() + jYearChooser1.getYear() + String.format("%02d", (jMonthChooser1.getMonth() + 1)) + "00" + "140100";

        jFileChooser1 = new JFileChooser();
        jFileChooser1.setDialogTitle("Seleccione Archivo TXT");
        jFileChooser1.setFileFilter(new FileNameExtensionFilter("Archivo TXT", "txt"));

        // Obtener la última ubicación guardada
        File lastDirectory = getLastDirectory();

        // Establecer la última ubicación como directorio inicial del JFileChooser
        if (lastDirectory != null && lastDirectory.exists()) {
            jFileChooser1.setCurrentDirectory(lastDirectory);
        }

        int result = jFileChooser1.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jFileChooser1.getSelectedFile();
            jTextField4.setText(selectedFile.getAbsolutePath());
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            btn_Bventas.setEnabled(false);
            jYearChooser1.setEnabled(false);
            jMonthChooser1.setEnabled(false);

            if (!nombreArchivo.equals(selectedFile.getName().substring(0, 27))) {
                JOptionPane.showMessageDialog(this, "Verifique el tipo de archivo y/o seleccione el archivo txt, segun el mes y año seleccionado", "Error", JOptionPane.ERROR_MESSAGE);
                jTextField4.setText("");
                btn_Bventas.setEnabled(true);
                jYearChooser1.setEnabled(true);
                jMonthChooser1.setEnabled(true);
                saveLastDirectory(selectedFile.getParentFile());
                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                return;
            }

            cargarDatosDesdeTxt(modelo1, selectedFile.getAbsolutePath());
            
            sumarcolumnascompras2();

            btn_Bventas.setEnabled(true);
            btn_Rventas.setEnabled(true);
            jYearChooser1.setEnabled(true);
            jMonthChooser1.setEnabled(true);
            btn_ReemplazarV.setEnabled(true);
            saveLastDirectory(selectedFile.getParentFile());
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        //        String nombreArchivo = "";
        //        String tipoMoneda = "";
        //        nombreArchivo = "LE" + txt_ruc1.getText() + jYearChooser3.getYear() + String.format("%02d", (jMonthChooser3.getMonth() + 1)) + "00" + "140400";
        //
        //        jFileChooser1 = new JFileChooser();
        //        jFileChooser1.setDialogTitle("Choose ZIP File");
        //        jFileChooser1.setFileFilter(new FileNameExtensionFilter("ZIP files", "zip"));
        //        // Obtener la última ubicación guardada
        //        File lastDirectory = getLastDirectory();
        //
        //        // Establecer la última ubicación como directorio inicial del JFileChooser
        //        if (lastDirectory != null && lastDirectory.exists()) {
            //            jFileChooser1.setCurrentDirectory(lastDirectory);
            //        }
        //
        //        int result = jFileChooser1.showOpenDialog(this);
        //        if (result == JFileChooser.APPROVE_OPTION) {
            //            File selectedFile = jFileChooser1.getSelectedFile();
            //            jTextField4.setText(selectedFile.getAbsolutePath());
            //
            //            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            //            btn_Bventas.setEnabled(false);
            //            jYearChooser3.setEnabled(false);
            //            jMonthChooser3.setEnabled(false);
            //
            //            if (!nombreArchivo.equals(selectedFile.getName().substring(0, 27))) {
                //                JOptionPane.showMessageDialog(this, "Verifique el tipo de archivo y/o seleccione el archivo txt, segun el mes y año seleccionado", "Error", JOptionPane.ERROR_MESSAGE);
                //                jTextField4.setText("");
                //                btn_Bventas.setEnabled(true);
                //                jYearChooser3.setEnabled(true);
                //                jMonthChooser3.setEnabled(true);
                //                saveLastDirectory(selectedFile.getParentFile());
                //                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                //                return;
                //            }
            //
            //            cargarDatosDesdeZip(modelo1, selectedFile.getAbsolutePath());
            //
            //            btn_Bventas.setEnabled(true);
            //            btn_Rventas.setEnabled(true);
            //            jYearChooser3.setEnabled(true);
            //            jMonthChooser3.setEnabled(true);
            //            btn_ReemplazarV.setEnabled(true);
            //            saveLastDirectory(selectedFile.getParentFile());
            //            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            //        }
    }//GEN-LAST:event_btn_BventasActionPerformed

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
                new LibrosE_SIRE().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TablaProcesada;
    private javax.swing.JTable TablaProcesada2;
    private javax.swing.JButton btn_Bcompras;
    private javax.swing.JButton btn_Bventas;
    private javax.swing.JButton btn_Rcompras;
    private javax.swing.JButton btn_ReemplazarC;
    private javax.swing.JButton btn_ReemplazarV;
    private javax.swing.JButton btn_Rventas;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private com.toedter.calendar.JMonthChooser jMonthChooser1;
    private com.toedter.calendar.JMonthChooser jMonthChooser3;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private com.toedter.calendar.JYearChooser jYearChooser1;
    private com.toedter.calendar.JYearChooser jYearChooser3;
    private javax.swing.JLabel txt_moneda;
    private javax.swing.JLabel txt_moneda1;
    private javax.swing.JLabel txt_razonsocial;
    private javax.swing.JLabel txt_razonsocial1;
    private javax.swing.JLabel txt_ruc;
    private javax.swing.JLabel txt_ruc1;
    // End of variables declaration//GEN-END:variables
}
