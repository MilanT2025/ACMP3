/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import Controlador.Conexion;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import java.awt.Cursor;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import main.Application;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
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
                FlatMacDarkLaf.setup();
                Application anteriorFrame = new Application();
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
        header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 45));

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
            jTextField1.setText(selectedFile.getAbsolutePath());
            try {
                loadExcelData(selectedFile);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al leer el archivo: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String ValidacionCuentas = "";

    public void identificarCuentas(JTable tb_resultado) {
        // Obtener el modelo de la tabla
        DefaultTableModel model = (DefaultTableModel) tb_resultado.getModel();

        // Variable para contar cuentas encontradas
        int cuentasEncontradas = 0;

        // Iterar sobre todas las filas de la tabla
        for (int i = 0; i < model.getRowCount(); i++) {
            // Obtener el valor de la columna que contiene las cuentas (suponiendo que está en la columna 1)
            Object cuentaObj = model.getValueAt(i, 1);  // Cambia el índice según la columna de cuenta

            // Verificar si el valor es una cadena
            String cuenta = null;
            if (cuentaObj instanceof String) {
                cuenta = (String) cuentaObj;
            } else if (cuentaObj instanceof Integer) {
                cuenta = String.valueOf(cuentaObj); // Convertir Integer a String
            } else {
                // Continuar si el valor no es un String o Integer
                continue;
            }

            // Verificar si la cuenta tiene al menos 2 caracteres
            if (cuenta.length() >= 2) {
                // Comprobar si comienza con 90, 92, 94, 95 o 97
                if (cuenta.startsWith("90") || cuenta.startsWith("92")
                        || cuenta.startsWith("94") || cuenta.startsWith("95")
                        || cuenta.startsWith("97")) {
                    // Imprimir en consola
                    ValidacionCuentas = ValidacionCuentas + "('" + cuenta + "'),";
//                    System.out.println("Cuenta encontrada: " + cuenta);
                    cuentasEncontradas++;
                }
            }
        }
        System.out.println("las cuentas son las siguientes: " + ValidacionCuentas);
        // Mensaje si no se encontraron cuentas
        if (cuentasEncontradas == 0) {
            System.out.println("No se encontraron cuentas que comiencen con 90, 92, 94, 95 o 97.");
        }
    }

    private void validacionCuentas() {
        boolean conforme = true; // Asumimos inicialmente que todas tienen equivalencias
        Connection con = null;
        Statement st = null;
        ResultSet rs1 = null;

        try {
            con = Conexion.getConnection();
            st = con.createStatement();

            // Eliminar datos
            String deleteData = "DELETE FROM [dbo].[TempCuenta];";
            PreparedStatement ps = con.prepareStatement(deleteData);           
            ps.executeUpdate();

            // Insertar datos
            String insertDataSql = "INSERT INTO [dbo].[TempCuenta] ([Cuenta]) "
                    + "VALUES " + ValidacionCuentas.substring(0, ValidacionCuentas.length() - 1) + ";";
            st.executeUpdate(insertDataSql);

            // Seleccionar cuentas sin equivalencia
            String selectSql = "SELECT TC.Cuenta FROM LibroMayorEquivalencias LM "
                    + "RIGHT JOIN dbo.TempCuenta TC ON LM.Cuenta = TC.Cuenta "
                    + "WHERE LM.Cuenta_Equivalente IS NULL;";

            System.out.println(selectSql);
            rs1 = st.executeQuery(selectSql);

            // Procesar resultados
            while (rs1.next()) {
                conforme = false; // Al menos una cuenta no tiene equivalencia
                String cuenta = rs1.getString(1); // Obtener el valor de la cuenta
                if (cuenta == null) {
                    cuenta = "Cuenta desconocida"; // Mensaje alternativo
                }
                JOptionPane.showMessageDialog(this, cuenta + " No Tiene Equivalencia");
            }

            // Solo si conforme sigue siendo true, todas tienen equivalencias
            if (conforme) {
                JOptionPane.showMessageDialog(this, "Todas las cuentas tienen Equivalencias");
            }

        } catch (SQLException ex) {
            Logger.getLogger(LibroMayor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Cerrar recursos
            try {
                if (rs1 != null) {
                    rs1.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Manejar posibles excepciones al cerrar
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

    public static List<Bloque> encontrarBloques(JTable table) {
        List<Bloque> bloques = new ArrayList<>();
        int inicio = -1;
        String numeroCuenta = null;

        for (int i = 0; i < table.getRowCount(); i++) {
            String col1 = table.getValueAt(i, 0) != null ? table.getValueAt(i, 0).toString() : "";
            String col2 = table.getValueAt(i, 1) != null ? table.getValueAt(i, 1).toString() : "";
            String col4 = table.getValueAt(i, 3) != null ? table.getValueAt(i, 3).toString() : "";

            // Detectar posición inicial
            if ("Cuenta: ".equals(col1)) {
                inicio = i;
                numeroCuenta = col2.isEmpty() ? "999999" : col2.trim(); // Guardar el número de cuenta
            }

            // Detectar posición final
            if ("SALDO DEUDOR:  ".equals(col4) || "SALDO ACREEDOR:  ".equals(col4)) {
                if (inicio != -1) { // Solo si hay un inicio válido
                    if (i + 1 >= table.getRowCount()) {
                        bloques.add(new Bloque(inicio, i, numeroCuenta));
                    } else {
                        bloques.add(new Bloque(inicio, i + 1, numeroCuenta));
                    }

                    inicio = -1; // Reiniciar el inicio
                    numeroCuenta = null; // Reiniciar el número de cuenta
                }
            }
        }

        return bloques;
    }

    static class Bloque {

        int inicioFila;
        int finFila;
        String numeroCuenta;

        public Bloque(int inicioFila, int finFila, String numeroCuenta) {
            this.inicioFila = inicioFila;
            this.finFila = finFila;
            this.numeroCuenta = numeroCuenta;
        }
    }

    public void ordenarLibroMayor(List<Bloque> bloques, JTable tb_resultado) {
        // Obtener el modelo de la tabla original (tb_resultado)
        DefaultTableModel modelOriginal = (DefaultTableModel) tb_resultado.getModel();

        // Crear un modelo temporal para copiar los datos
        DefaultTableModel modelTemporal = new DefaultTableModel();

        // Agregar las columnas de tb_resultado al modelo temporal
        for (int i = 0; i < tb_resultado.getColumnCount(); i++) {
            modelTemporal.addColumn(tb_resultado.getColumnName(i));
        }

        for (int i = 0; i <= 8; i++) {
            Object[] row = new Object[tb_resultado.getColumnCount()];

            // Copiar los datos de la fila de tb_resultado a row
            for (int col = 0; col < tb_resultado.getColumnCount(); col++) {
                row[col] = tb_resultado.getValueAt(i, col);
            }

            // Agregar la fila a la tabla temporal
            modelTemporal.addRow(row);
        }

        // Recorrer el List<Bloque> y copiar las filas correspondientes
        for (Bloque bloque : bloques) {
            int inicioFila = bloque.inicioFila;
            int finFila = bloque.finFila;

            for (int fila = inicioFila; fila <= finFila; fila++) {
                // Crear un array para almacenar los datos de la fila
                Object[] row = new Object[tb_resultado.getColumnCount()];

                // Copiar los datos de la fila de tb_resultado a row
                for (int col = 0; col < tb_resultado.getColumnCount(); col++) {
                    row[col] = tb_resultado.getValueAt(fila, col);
                }

                // Agregar la fila a la tabla temporal
                modelTemporal.addRow(row);
            }
        }

        // Limpiar la tabla original (tb_resultado)
        modelOriginal.setRowCount(0); // Eliminar todas las filas

        // Copiar los datos de la tabla temporal a la tabla original
        for (int i = 0; i < modelTemporal.getRowCount(); i++) {
            Object[] row = new Object[modelTemporal.getColumnCount()];

            for (int j = 0; j < modelTemporal.getColumnCount(); j++) {
                row[j] = modelTemporal.getValueAt(i, j);
            }

            // Agregar la fila a la tabla original
            modelOriginal.addRow(row);
        }
    }

    private void encontrarequivalencias() {
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
    }

    private void ordenarJTableCuentas() {
        List<Bloque> bloques = encontrarBloques(tb_resultado);

        bloques.sort(Comparator.comparingInt(b -> Integer.valueOf(b.numeroCuenta.substring(0, 2))));

        if (tb_resultado.getRowCount() > 0) {
            ordenarLibroMayor(bloques, tb_resultado);
        }

    }

//    public static void exportarAExcel(JTable table) throws IOException {
//        // Crear un libro de trabajo
//        Workbook workbook = new XSSFWorkbook();
//        // Crear una hoja
//        Sheet sheet = workbook.createSheet("Datos");
//
//        // Crear una fuente de Arial, tamaño 9
//        Font fuente = workbook.createFont();
//        fuente.setFontName("Arial");
//        fuente.setFontHeightInPoints((short) 9);
//
//        // Crear un estilo de celda con la fuente
//        CellStyle estiloCelda = workbook.createCellStyle();
//        estiloCelda.setFont(fuente);
//
//        // Obtener el modelo de tabla
//        TableModel model = table.getModel();
//
//        // Crear las cabeceras de columna
//        Row filaCabecera = sheet.createRow(0);
//        for (int col = 0; col < model.getColumnCount(); col++) {
//            Cell celda = filaCabecera.createCell(col);
//            celda.setCellValue(model.getColumnName(col));
//            celda.setCellStyle(estiloCelda);  // Aplicar el estilo a las cabeceras
//        }
//
//        // Rellenar las filas con los datos
//        for (int row = 0; row < model.getRowCount(); row++) {
//            Row fila = sheet.createRow(row + 1);  // Empezar desde la segunda fila (la primera es la cabecera)
//            for (int col = 0; col < model.getColumnCount(); col++) {
//                Cell celda = fila.createCell(col);
//                Object valorCelda = model.getValueAt(row, col);
//
//                if (valorCelda != null) {
//                    if (valorCelda instanceof String) {
//                        celda.setCellValue((String) valorCelda);
//                    } else if (valorCelda instanceof Integer) {
//                        celda.setCellValue((Integer) valorCelda);
//                    } else if (valorCelda instanceof Double) {
//                        celda.setCellValue((Double) valorCelda);
//                    } else if (valorCelda instanceof Boolean) {
//                        celda.setCellValue((Boolean) valorCelda);
//                    }
//                }
//                celda.setCellStyle(estiloCelda);  // Aplicar el estilo a cada celda
//            }
//        }
//
//        // Ajustar el tamaño de las columnas
//        for (int i = 0; i < model.getColumnCount(); i++) {
//            sheet.autoSizeColumn(i);
//        }
//
//        // Abrir JFileChooser para seleccionar la ruta y el nombre del archivo
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setDialogTitle("Guardar archivo Excel");
//        fileChooser.setAcceptAllFileFilterUsed(false);
//        // Filtrar para solo aceptar archivos .xlsx
//        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos Excel", "xlsx");
//        fileChooser.addChoosableFileFilter(filter);
//
//        // Mostrar el JFileChooser
//        int seleccion = fileChooser.showSaveDialog(null);
//
//        if (seleccion == JFileChooser.APPROVE_OPTION) {
//            // Obtener la ruta y el nombre del archivo seleccionado
//            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
//
//            // Asegurarse de que el archivo termine con ".xlsx"
//            if (!filePath.endsWith(".xlsx")) {
//                filePath += ".xlsx";
//            }
//
//            // Guardar el archivo Excel en la ubicación seleccionada
//            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
//                workbook.write(fileOut);
//            }
//
//            // Mostrar mensaje de éxito
//            JOptionPane.showMessageDialog(null, "Archivo exportado con éxito: " + filePath);
//        } else {
//            JOptionPane.showMessageDialog(null, "Operación cancelada.");
//        }
//
//        // Cerrar el libro
//        workbook.close();
//    }
    public static void exportarAExcel(JTable table) throws IOException {
        // Crear un libro de trabajo
        Workbook workbook = new XSSFWorkbook();
        // Crear una hoja
        Sheet sheet = workbook.createSheet("Datos");

        // Crear una fuente de Arial, tamaño 9
        Font fuente = workbook.createFont();
        fuente.setFontName("Arial");
        fuente.setFontHeightInPoints((short) 9);

        // Crear un estilo de celda con la fuente normal
        CellStyle estiloCelda = workbook.createCellStyle();
        estiloCelda.setFont(fuente);

        // Crear un estilo de celda para negrita
        CellStyle estiloCeldaNegrita = workbook.createCellStyle();
        Font fuenteNegrita = workbook.createFont();
        fuenteNegrita.setFontName("Arial");
        fuenteNegrita.setFontHeightInPoints((short) 9);
        fuenteNegrita.setBold(true); // Establecer negrita
        estiloCeldaNegrita.setFont(fuenteNegrita);

        // Obtener el modelo de tabla
        TableModel model = table.getModel();

        // Crear las cabeceras de columna
        Row filaCabecera = sheet.createRow(0);
        for (int col = 0; col < model.getColumnCount(); col++) {
            Cell celda = filaCabecera.createCell(col);
            celda.setCellValue(model.getColumnName(col));
            celda.setCellStyle(estiloCelda);  // Aplicar el estilo a las cabeceras
        }

        // Rellenar las filas con los datos
        for (int row = 0; row < model.getRowCount(); row++) {
            Row fila = sheet.createRow(row + 1);  // Empezar desde la segunda fila (la primera es la cabecera)
            for (int col = 0; col < model.getColumnCount(); col++) {
                Cell celda = fila.createCell(col);
                Object valorCelda = model.getValueAt(row, col);

                if (valorCelda != null) {
                    if (valorCelda instanceof String) {
                        celda.setCellValue((String) valorCelda);
                    } else if (valorCelda instanceof Integer) {
                        celda.setCellValue((Integer) valorCelda);
                    } else if (valorCelda instanceof Double) {
                        celda.setCellValue((Double) valorCelda);
                    } else if (valorCelda instanceof Boolean) {
                        celda.setCellValue((Boolean) valorCelda);
                    }
                }
                celda.setCellStyle(estiloCelda);  // Aplicar el estilo a cada celda
            }

            // Verificar si la fila contiene "Cuenta:" en la primera columna
            if ("Cuenta: ".equals(model.getValueAt(row, 0))) {
                // Aplicar el estilo de celda en negrita
                for (int col = 0; col < model.getColumnCount(); col++) {
                    fila.getCell(col).setCellStyle(estiloCeldaNegrita);
                }
            }
        }

        // Ajustar el tamaño de las columnas
        for (int i = 0; i < model.getColumnCount(); i++) {
            sheet.autoSizeColumn(i);
        }

        // Abrir JFileChooser para seleccionar la ruta y el nombre del archivo
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar archivo Excel");
        fileChooser.setAcceptAllFileFilterUsed(false);
        // Filtrar para solo aceptar archivos .xlsx
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos Excel", "xlsx");
        fileChooser.addChoosableFileFilter(filter);

        // Mostrar el JFileChooser
        int seleccion = fileChooser.showSaveDialog(null);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            // Obtener la ruta y el nombre del archivo seleccionado
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            // Asegurarse de que el archivo termine con ".xlsx"
            if (!filePath.endsWith(".xlsx")) {
                filePath += ".xlsx";
            }

            // Guardar el archivo Excel en la ubicación seleccionada
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }

            // Mostrar mensaje de éxito
            JOptionPane.showMessageDialog(null, "Archivo exportado con éxito: " + filePath);
        } else {
            JOptionPane.showMessageDialog(null, "Operación cancelada.");
        }

        // Cerrar el libro
        workbook.close();
    }

//     public void buscarCuenta(JTable tb_resultado, JTextField txtBuscar) {
//        // Obtener el texto a buscar desde el JTextField
//        String textoBuscar = txtBuscar.getText().trim();
//
//        // Obtener el modelo de la tabla
//        DefaultTableModel model = (DefaultTableModel) tb_resultado.getModel();
//        
//        // Iterar sobre todas las filas de la tabla
//        boolean encontrado = false;  // Variable para saber si se encontró el valor
//        for (int i = 0; i < model.getRowCount(); i++) {
//            // Verificar si la columna 1 tiene el texto "Cuenta:"
//            String columna1 = (String) model.getValueAt(i, 0);  // Columna 1
//            if ("Cuenta: ".equals(columna1)) {
//                // Verificar si el valor en la columna 2 coincide con el texto a buscar
//                String columna2 = (String) model.getValueAt(i, 1);  // Columna 2
//                if (columna2 != null && columna2.contains(textoBuscar)) {
//                    // Si se encuentra, seleccionar la fila
//                    tb_resultado.setRowSelectionInterval(i, i);
//
//                    // Asegurarse de que la fila seleccionada sea visible
//                    tb_resultado.scrollRectToVisible(tb_resultado.getCellRect(i, 0, true));
//                    
//                    encontrado = true;
//                    break;  // Salir del bucle si ya se encontró el valor
//                }
//            }
//        }
//
//        // Si no se encuentra el texto
//        if (!encontrado) {
//            JOptionPane.showMessageDialog(null, "No se encontró el N° de Cuenta", "Resultado de búsqueda", JOptionPane.INFORMATION_MESSAGE);
//        }
//    }
    public void buscarCuenta(JTable tb_resultado, JTextField txtBuscar) {
        // Obtener el texto a buscar desde el JTextField
        String textoBuscar = txtBuscar.getText().trim();

        // Asegúrate de que el texto a buscar tenga al menos dos caracteres
        if (textoBuscar.length() < 2) {
            JOptionPane.showMessageDialog(null, "Por favor ingrese al menos dos caracteres para buscar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return; // Salir del método si no hay suficientes caracteres
        }

        // Obtener el modelo de la tabla
        DefaultTableModel model = (DefaultTableModel) tb_resultado.getModel();

        // Obtener los dos primeros caracteres del texto a buscar
        String dosPrimeros = textoBuscar.substring(0, 2);

        // Iterar sobre todas las filas de la tabla
        boolean encontrado = false;  // Variable para saber si se encontró el valor
        for (int i = 0; i < model.getRowCount(); i++) {
            // Verificar si la columna 1 tiene el texto "Cuenta:"
            String columna1 = (String) model.getValueAt(i, 0);  // Columna 1
            if ("Cuenta: ".equals(columna1)) {
                // Verificar si el valor en la columna 2 comienza con los dos primeros caracteres del texto a buscar
                String columna2 = (String) model.getValueAt(i, 1);  // Columna 2
                if (columna2 != null && columna2.startsWith(dosPrimeros)) {
                    // Si se encuentra, seleccionar la fila
                    tb_resultado.setRowSelectionInterval(i, i);

                    // Asegurarse de que la fila seleccionada sea visible
                    tb_resultado.scrollRectToVisible(tb_resultado.getCellRect(i, 0, true));

                    encontrado = true;
                    break;  // Salir del bucle si ya se encontró el valor
                }
            }
        }

        // Si no se encuentra el texto
        if (!encontrado) {
            JOptionPane.showMessageDialog(null, "No se encontró el N° de Cuenta", "Resultado de búsqueda", JOptionPane.INFORMATION_MESSAGE);
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
        txtBuscar = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        jButton5 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        AgregarSedes = new javax.swing.JMenuItem();
        MenuRegistrarEmpleado1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Libro Mayor (Cuentas Contables Clase 6)");

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
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tb_resultado.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        tb_resultado.setModel(modelo);
        tb_resultado.setRowHeight(30);
        tb_resultado.setShowGrid(true);
        jScrollPane1.setViewportView(tb_resultado);

        txtBuscar.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N

        jButton4.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Search_4.png"))); // NOI18N
        jButton4.setText("Buscar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel2.setText("Buscar N° Cuenta (Empieza con):");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1109, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBuscar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtBuscar)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
                .addContainerGap())
        );

        jButton1.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Upload to the Cloud_1.png"))); // NOI18N
        jButton1.setText("Cargar Libro Mayor");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField1.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        jTextField1.setDisabledTextColor(new java.awt.Color(255, 0, 0));
        jTextField1.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextField1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/flecha-hacia-abajo.png"))); // NOI18N
        jMenu2.setText("Exportar");
        jMenu2.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Microsoft excel.png"))); // NOI18N
        jButton5.setText("Excel");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jMenu2.add(jButton5);

        jMenuBar1.add(jMenu2);

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ajustes.png"))); // NOI18N
        jMenu1.setText("Opciones");

        AgregarSedes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/agregarsedes 1.png"))); // NOI18N
        AgregarSedes.setText("Cuentas Equivalentes");
        AgregarSedes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AgregarSedesActionPerformed(evt);
            }
        });
        jMenu1.add(AgregarSedes);

        MenuRegistrarEmpleado1.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        MenuRegistrarEmpleado1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Upload to the Cloud_1.png"))); // NOI18N
        MenuRegistrarEmpleado1.setText("Verificacion de Cuentas");
        MenuRegistrarEmpleado1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuRegistrarEmpleado1ActionPerformed(evt);
            }
        });
        jMenu1.add(MenuRegistrarEmpleado1);

        jMenuBar1.add(jMenu1);

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
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        jButton1.setEnabled(false);
        openFileChooser();
        encontrarequivalencias();
        ordenarJTableCuentas();
        jButton1.setEnabled(true);
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        if (tb_resultado.getRowCount() > 0) {
            jButton5.setEnabled(true);
        } else {
            jButton5.setEnabled(false);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        buscarCuenta(tb_resultado, txtBuscar);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        jButton5.setEnabled(false);
        try {
            exportarAExcel(tb_resultado);
        } catch (IOException ex) {
            Logger.getLogger(LibroMayor.class.getName()).log(Level.SEVERE, null, ex);
        }
        jButton5.setEnabled(true);
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed

    }//GEN-LAST:event_jMenu2ActionPerformed

    private void AgregarSedesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AgregarSedesActionPerformed
        AgregarCuentasEquivalentesLM ini = new AgregarCuentasEquivalentesLM(this, true);
        ini.setVisible(true);
    }//GEN-LAST:event_AgregarSedesActionPerformed

    private void MenuRegistrarEmpleado1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuRegistrarEmpleado1ActionPerformed
        identificarCuentas(tb_resultado); // Llamar al método y pasarle el JTable
        validacionCuentas();
    }//GEN-LAST:event_MenuRegistrarEmpleado1ActionPerformed

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
    private javax.swing.JMenuItem AgregarSedes;
    private javax.swing.JMenuItem MenuRegistrarEmpleado1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JMenuItem jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTable tb_resultado;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables

}
