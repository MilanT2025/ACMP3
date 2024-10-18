/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import java.io.BufferedWriter;
import java.io.File;
import javax.swing.table.DefaultTableModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
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
public class ExcelExporter {

    public static void exportToExcel(String nombreHoja, JTable table, JFrame parentFrame, String nombreArchivo, String extension) {
        // Crear un JFileChooser con un nombre predeterminado
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar como archivo Excel");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos Excel (*.xlsx)", "xlsx"));
        fileChooser.setSelectedFile(new File(nombreArchivo));

        // Mostrar el diálogo para seleccionar la ubicación y el nombre del archivo
        int userSelection = fileChooser.showSaveDialog(parentFrame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            // Obtener la ruta del archivo seleccionado por el usuario
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            exportToText(filePath, extension, table, parentFrame);
            JOptionPane.showMessageDialog(null, "<html>El archivo de <b>ENVIO " + extension.toUpperCase() + "</b> se ha exportado exitosamente con el siguiente nombre:<br><br><font color='red'><b>" + nombreArchivo + "." + extension + " </font></b></html>", "Exportación Exitosa", JOptionPane.INFORMATION_MESSAGE);

            if (JOptionPane.showConfirmDialog(null, "¿Desea exportar un excel para verificar de informacion enviada?", "Confirmación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                // Guardar los datos del JTable en un archivo Excel
                try (Workbook workbook = new XSSFWorkbook(); FileOutputStream fileOut = new FileOutputStream(filePath + "_" + extension + ".xlsx")) {
                    // Crear una nueva hoja en el archivo Excel
                    Sheet sheet = workbook.createSheet(nombreHoja);

                    // Obtener el modelo de tabla y el número de filas y columnas
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    int numRows = model.getRowCount();
                    int numCols = model.getColumnCount();

                    // Crear una lista para almacenar los nombres de las columnas
                    List<String> columnNames = new ArrayList<>();
                    // Obtener los nombres de las columnas del JTable y agregarlos a la lista
                    for (int i = 0; i < numCols; i++) {
                        columnNames.add(model.getColumnName(i));
                    }

                    // Crear una fila para el encabezado y establecer el formato de texto en negrita
                    Font headerFont = workbook.createFont();
                    headerFont.setBold(true);
                    CellStyle headerCellStyle = workbook.createCellStyle();
                    headerCellStyle.setFont(headerFont);

                    Row headerRow = sheet.createRow(0);
                    // Escribir los nombres de las columnas como encabezado en negrita
                    for (int i = 0; i < numCols; i++) {
                        Cell cell = headerRow.createCell(i);
                        cell.setCellValue(columnNames.get(i));
                        cell.setCellStyle(headerCellStyle);
                    }

                    // Escribir los datos del JTable en el archivo Excel
                    for (int i = 0; i < numRows; i++) {
                        Row row = sheet.createRow(i + 1);
                        for (int j = 0; j < numCols; j++) {
                            row.createCell(j).setCellValue(model.getValueAt(i, j).toString());
                        }
                    }

                    // Autoajustar el ancho de las columnas
                    for (int i = 0; i < numCols; i++) {
                        sheet.autoSizeColumn(i);
                        // Limitar el ancho máximo de la columna a 50 caracteres
                        sheet.setColumnWidth(i, Math.min(sheet.getColumnWidth(i), 50 * 256));
                    }

                    // Escribir el contenido del libro de trabajo en el archivo
                    workbook.write(fileOut);
                    // Cerrar el flujo de salida del archivo
                    fileOut.close();

                    JOptionPane.showMessageDialog(null, "<html>El archivo de <b>EXCEL</b> se ha exportado exitosamente con el siguiente nombre:<br><br><font color='red'><b>" + nombreArchivo + ".xlsx </font></b></html>", "Exportación Exitosa", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(parentFrame, "Se ha producido un error al exportar el archivo EXCEL y TXT.",
                            "Error de Exportación", JOptionPane.ERROR_MESSAGE);
                }

            }

        }
    }
    
    
    public static void exportToExcel2(String filePath, String nombreHoja, JTable table, JFrame parentFrame, String extension) {

        // Guardar los datos del JTable en un archivo Excel
        try (Workbook workbook = new XSSFWorkbook(); FileOutputStream fileOut = new FileOutputStream(filePath + "_" + extension + ".xlsx")) {
            // Crear una nueva hoja en el archivo Excel
            Sheet sheet = workbook.createSheet(nombreHoja);

            // Obtener el modelo de tabla y el número de filas y columnas
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            int numRows = model.getRowCount();
            int numCols = model.getColumnCount();

            // Crear una lista para almacenar los nombres de las columnas
            List<String> columnNames = new ArrayList<>();
            // Obtener los nombres de las columnas del JTable y agregarlos a la lista
            for (int i = 0; i < numCols; i++) {
                columnNames.add(model.getColumnName(i));
            }

            // Crear una fila para el encabezado y establecer el formato de texto en negrita
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(headerFont);

            Row headerRow = sheet.createRow(0);
            // Escribir los nombres de las columnas como encabezado en negrita
            for (int i = 0; i < numCols; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnNames.get(i));
                cell.setCellStyle(headerCellStyle);
            }

            // Escribir los datos del JTable en el archivo Excel
            for (int i = 0; i < numRows; i++) {
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < numCols; j++) {
                    row.createCell(j).setCellValue(model.getValueAt(i, j).toString());
                }
            }

            // Autoajustar el ancho de las columnas
            for (int i = 0; i < numCols; i++) {
                sheet.autoSizeColumn(i);
                // Limitar el ancho máximo de la columna a 50 caracteres
                sheet.setColumnWidth(i, Math.min(sheet.getColumnWidth(i), 50 * 256));
            }

            // Escribir el contenido del libro de trabajo en el archivo
            workbook.write(fileOut);
            // Cerrar el flujo de salida del archivo
            fileOut.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void exportToExcel2(String nombreHoja, JTable table, JFrame parentFrame, String nombreArchivo, String extension) {
        // Crear un JFileChooser con un nombre predeterminado
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar como archivo Excel");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos Excel (*.xlsx)", "xlsx"));
        fileChooser.setSelectedFile(new File(nombreArchivo));

        // Mostrar el diálogo para seleccionar la ubicación y el nombre del archivo
        int userSelection = fileChooser.showSaveDialog(parentFrame);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            // Obtener la ruta del archivo seleccionado por el usuario
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();

            exportToText2(filePath, extension, table, parentFrame);
            JOptionPane.showMessageDialog(null, "<html>El archivo de <b>ENVIO " + extension.toUpperCase() + "</b> se ha exportado exitosamente con el siguiente nombre:<br><br><font color='red'><b>" + nombreArchivo + "." + extension + " </font></b></html>", "Exportación Exitosa", JOptionPane.INFORMATION_MESSAGE);

            if (JOptionPane.showConfirmDialog(null, "¿Desea exportar un excel para verificar de informacion enviada?", "Confirmación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                // Guardar los datos del JTable en un archivo Excel
                try (Workbook workbook = new XSSFWorkbook(); FileOutputStream fileOut = new FileOutputStream(filePath + "_" + extension + ".xlsx")) {
                    // Crear una nueva hoja en el archivo Excel
                    Sheet sheet = workbook.createSheet(nombreHoja);

                    // Obtener el modelo de tabla y el número de filas y columnas
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    int numRows = model.getRowCount();
                    int numCols = model.getColumnCount();

                    // Crear una lista para almacenar los nombres de las columnas
                    List<String> columnNames = new ArrayList<>();
                    // Obtener los nombres de las columnas del JTable y agregarlos a la lista
                    for (int i = 0; i < numCols; i++) {
                        columnNames.add(model.getColumnName(i));
                    }

                    // Crear una fila para el encabezado y establecer el formato de texto en negrita
                    Font headerFont = workbook.createFont();
                    headerFont.setBold(true);
                    CellStyle headerCellStyle = workbook.createCellStyle();
                    headerCellStyle.setFont(headerFont);

                    Row headerRow = sheet.createRow(0);
                    // Escribir los nombres de las columnas como encabezado en negrita
                    for (int i = 0; i < numCols; i++) {
                        Cell cell = headerRow.createCell(i);
                        cell.setCellValue(columnNames.get(i));
                        cell.setCellStyle(headerCellStyle);
                    }

                    // Escribir los datos del JTable en el archivo Excel
                    for (int i = 0; i < numRows; i++) {
                        Row row = sheet.createRow(i + 1);
                        for (int j = 0; j < numCols; j++) {
                            row.createCell(j).setCellValue(model.getValueAt(i, j).toString());
                        }
                    }

                    // Autoajustar el ancho de las columnas
                    for (int i = 0; i < numCols; i++) {
                        sheet.autoSizeColumn(i);
                        // Limitar el ancho máximo de la columna a 50 caracteres
                        sheet.setColumnWidth(i, Math.min(sheet.getColumnWidth(i), 50 * 256));
                    }

                    // Escribir el contenido del libro de trabajo en el archivo
                    workbook.write(fileOut);
                    // Cerrar el flujo de salida del archivo
                    fileOut.close();

                    JOptionPane.showMessageDialog(null, "<html>El archivo de <b>EXCEL</b> se ha exportado exitosamente con el siguiente nombre:<br><br><font color='red'><b>" + nombreArchivo + ".xlsx </font></b></html>", "Exportación Exitosa", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(parentFrame, "Se ha producido un error al exportar el archivo EXCEL y TXT.",
                            "Error de Exportación", JOptionPane.ERROR_MESSAGE);
                }

            }

        }
    }
    
    private static void exportToText(String filePath, String extension, JTable table, JFrame parentFrame) {

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath + "." + extension))) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            int numRows = model.getRowCount();
            int numCols = model.getColumnCount();

            // Expresión regular para buscar el número entre corchetes
            Pattern pattern = Pattern.compile("\\[(\\d+)\\]");

            // Iterar sobre las filas y columnas de la tabla
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    Object value = model.getValueAt(i, j);
                    if (value != null) {
                        String cellValue = value.toString();

                        // Usar expresiones regulares para extraer el número entre corchetes
                        Matcher matcher = pattern.matcher(cellValue);
                        if (matcher.find()) {
                            String numero = matcher.group(1);
                            // Escribir el número en el archivo de texto
                            bufferedWriter.write(numero);
                        } else {
                            // Si no se encuentra ningún número entre corchetes, escribir el valor de la celda
                            bufferedWriter.write(cellValue);
                        }
                    }
                    // Separar las columnas por un palote "|" (o como desees)
                    if (j < numCols) {
                        bufferedWriter.write("|");
                    }
                }
                // Agregar un salto de línea al final de cada fila, excepto para la última fila
                if (i < numRows - 1) {
                    bufferedWriter.newLine();
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    /*private static void exportToText(String filePath, String extension, JTable table, JFrame parentFrame) {

        try (FileWriter fileWriter = new FileWriter(filePath + "." + extension)) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            int numRows = model.getRowCount();
            int numCols = model.getColumnCount();

            // Expresión regular para buscar el número entre corchetes
            Pattern pattern = Pattern.compile("\\[(\\d+)\\]");

            // Iterar sobre las filas y columnas de la tabla
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    Object value = model.getValueAt(i, j);
                    if (value != null) {
                        String cellValue = value.toString();

                        // Usar expresiones regulares para extraer el número entre corchetes
                        Matcher matcher = pattern.matcher(cellValue);
                        if (matcher.find()) {
                            String numero = matcher.group(1);
                            // Escribir el número en el archivo de texto
                            fileWriter.write(numero);
                        } else {
                            // Si no se encuentra ningún número entre corchetes, escribir el valor de la celda
                            fileWriter.write(cellValue);
                        }
                    }
                    // Separar las columnas por un palote "|" (o como desees)
                    if (j < numCols) {
                        fileWriter.write("|");
                    }
                }
                // Agregar un salto de línea al final de cada fila
                fileWriter.write("\n");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }*/
    
    private static void exportToText2(String filePath, String extension, JTable table, JFrame parentFrame) {

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath + "." + extension))) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            int numRows = model.getRowCount();
            int numCols = model.getColumnCount();

            // Expresión regular para buscar el número entre corchetes
            Pattern pattern = Pattern.compile("\\[([^\\[\\]]+)\\]");

            // Iterar sobre las filas y columnas de la tabla
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    Object value = model.getValueAt(i, j);
                    if (value != null) {
                        String cellValue = value.toString();

                        // Usar expresiones regulares para extraer el número entre corchetes
                        Matcher matcher = pattern.matcher(cellValue);
                        if (matcher.find()) {
                            String numero = matcher.group(1);
                            // Escribir el número en el archivo de texto
                            bufferedWriter.write(numero);
                        } else {
                            // Si no se encuentra ningún número entre corchetes, escribir el valor de la celda
                            bufferedWriter.write(cellValue);
                        }
                    }
                    // Separar las columnas por un palote "|" (o como desees)
                    if (j < numCols) {
                        bufferedWriter.write("|");
                    }
                }
                // Agregar un salto de línea al final de cada fila, excepto para la última fila
                if (i < numRows - 1) {
                    bufferedWriter.newLine();
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
     

    
}
