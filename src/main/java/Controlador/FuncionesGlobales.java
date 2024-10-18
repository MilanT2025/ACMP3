/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Farmacia-Ingeniero
 */
public class FuncionesGlobales {
    
     public static boolean Writer_XLSX(JTable jTable1, String Location, String hoja) throws FileNotFoundException, IOException {
        boolean correcto = false;
        XSSFWorkbook fWorkbook = new XSSFWorkbook();
        XSSFSheet fSheet = fWorkbook.createSheet(hoja);
        XSSFFont sheetTitleFont = fWorkbook.createFont();
        XSSFCellStyle cellStyle = fWorkbook.createCellStyle();
        sheetTitleFont.setBold(true);
        cellStyle.setFont(sheetTitleFont);

        POIXMLProperties xmlProps = fWorkbook.getProperties();
        POIXMLProperties.CoreProperties coreProps = xmlProps.getCoreProperties();
        coreProps.setCreator("SISGALENFARMA");

        TableModel model = jTable1.getModel();
        RowSorter<? extends TableModel> rowSorter = jTable1.getRowSorter();
        fSheet.createFreezePane(0, 1);

        // Get Header
        TableColumnModel tcm = jTable1.getColumnModel();
        XSSFRow hRow = fSheet.createRow(0);
        for (int j = 0; j < tcm.getColumnCount(); j++) {
            XSSFCell cell = hRow.createCell(j);
            cell.setCellValue(tcm.getColumn(j).getHeaderValue().toString());
            cell.setCellStyle(cellStyle);
        }

        // Get Filtered details
        for (int i = 0; i < rowSorter.getViewRowCount(); i++) {
            XSSFRow fRow = fSheet.createRow(i + 1);
            int modelRow = rowSorter.convertRowIndexToModel(i);
            for (int j = 0; j < model.getColumnCount(); j++) {
                XSSFCell cell = fRow.createCell(j);
                Object value = model.getValueAt(modelRow, j);
                cell.setCellValue(value != null ? value.toString() : "");
               // cell.setCellStyle(cellStyle);
            }
        }

        // Auto-size columns
        for (int j = 0; j < model.getColumnCount(); j++) {
            fSheet.autoSizeColumn(j);
        }

        // Apply filter
        fSheet.setAutoFilter(new CellRangeAddress(0, rowSorter.getViewRowCount(), 0, model.getColumnCount() - 1));

        // Write to file
        try (FileOutputStream fileOutputStream = new FileOutputStream(Location); BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream)) {
            fWorkbook.write(bos);
        }

        correcto = true;
        return correcto;
}

    

    public static void seleccionarFilas(JTable origen, JTable destino, int columnaOrigen, int columnaDestino) {
        ListSelectionModel seleccionOrigen = origen.getSelectionModel();
        ListSelectionModel seleccionDestino = destino.getSelectionModel();

        // Limpiar la selección anterior en la tabla de destino
        seleccionDestino.clearSelection();

        int[] filasSeleccionadas = origen.getSelectedRows();
        for (int fila : filasSeleccionadas) {
            Object valorOrigen = origen.getValueAt(fila, columnaOrigen);
            for (int filaDestino = 0; filaDestino < destino.getRowCount(); filaDestino++) {
                Object valorDestino = destino.getValueAt(filaDestino, columnaDestino);
                if (valorOrigen != null && valorDestino != null && valorOrigen.equals(valorDestino)) {
                    seleccionDestino.addSelectionInterval(filaDestino, filaDestino);
                    destino.changeSelection(filaDestino, 1, true, true);
                }
            }
        }
    }

    public static void seleccionarFilas2(JTable origen, JTable destino, int columnaOrigen1, int columnaOrigen2, int columnaDestino1, int columnaDestino2) {
        ListSelectionModel seleccionOrigen = origen.getSelectionModel();
        ListSelectionModel seleccionDestino = destino.getSelectionModel();

        // Limpiar la selección anterior en la tabla de destino
        seleccionDestino.clearSelection();

        int[] filasSeleccionadas = origen.getSelectedRows();
        for (int fila : filasSeleccionadas) {
            Object valorOrigen1 = origen.getValueAt(fila, columnaOrigen1);
            Object valorOrigen2 = origen.getValueAt(fila, columnaOrigen2);
            for (int filaDestino = 0; filaDestino < destino.getRowCount(); filaDestino++) {
                Object valorDestino1 = destino.getValueAt(filaDestino, columnaDestino1);
                Object valorDestino2 = destino.getValueAt(filaDestino, columnaDestino2);
                if (valorOrigen1 != null && valorOrigen2 != null && valorDestino1 != null && valorDestino2 != null
                        && valorOrigen1.equals(valorDestino1) && valorOrigen2.equals(valorDestino2)) {
                    seleccionDestino.addSelectionInterval(filaDestino, filaDestino);
                    destino.changeSelection(filaDestino, 1, true, true);
                }
            }
        }
    }
    
    public static void saveLastDirectory(File directory) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("directorio.txt"))) {
            writer.println(directory.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static File getLastDirectory() {
        File lastDirectory = null;
        try (BufferedReader reader = new BufferedReader(new FileReader("directorio.txt"))) {
            String directoryPath = reader.readLine();
            if (directoryPath != null) {
                lastDirectory = new File(directoryPath);
            }
        } catch (IOException e) {
            // El archivo de configuración no existe o no se puede leer, no hacer nada
        }
        return lastDirectory;
    }
    
    public static void setBoldAndColorRows(JTable table, List<Integer> rowIndexes) {
       // Verificar si los índices de fila son válidos
        for (int rowIndex : rowIndexes) {
            if (rowIndex < 0 || rowIndex >= table.getRowCount()) {
                throw new IllegalArgumentException("Índice de fila no válido: " + rowIndex);
            }
        }

        Color color = new Color(255, 230, 153);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!isSelected && rowIndexes.contains(row)) {
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                    c.setBackground(color);
                } else {
                    c.setBackground(table.getBackground()); // Restaurar el color de fondo por defecto para otras filas
                }

                return c;
            }
        });

        // Actualizar la tabla para reflejar los cambios en el renderizador de celdas
        table.repaint();
    }
    
    public static void centerColumnWithStyle(JTable table, int column) {
    // Obtener el renderizador actual de la columna
    TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
    TableCellRenderer currentRenderer = table.getColumnModel().getColumn(column).getCellRenderer();

    // Crear un nuevo renderizador personalizado que centre el contenido
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            return label;
        }
    };
    
    // Establecer el nuevo renderizador solo si no hay un renderizador personalizado
    if (currentRenderer == null || currentRenderer instanceof DefaultTableCellRenderer) {
        table.getColumnModel().getColumn(column).setCellRenderer(centerRenderer);
    } else {
        // Si hay un renderizador personalizado, combinarlo con el centrado
        Component originalComponent = currentRenderer.getTableCellRendererComponent(table, null, false, false, 0, column);
        if (originalComponent instanceof JLabel) {
            JLabel originalLabel = (JLabel) originalComponent;
            centerRenderer.setForeground(originalLabel.getForeground());
            centerRenderer.setBackground(originalLabel.getBackground());
            centerRenderer.setFont(originalLabel.getFont());
        }
        table.getColumnModel().getColumn(column).setCellRenderer(centerRenderer);
    }
    
    // Mantener el renderizador del encabezado
    table.getTableHeader().setDefaultRenderer(headerRenderer);
}
    
}
