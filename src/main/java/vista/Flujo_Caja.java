/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import Controlador.Conexion;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import main.Application;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Farmacia-Ingeniero
 */
public class Flujo_Caja extends javax.swing.JFrame {

    private final Modelo modelo = new Modelo();

    /**
     * Creates new form LibrosE_SIRE
     */
    public Flujo_Caja() {
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

        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaMesAnterior = fechaActual.minusMonths(1);
        año.setYear(fechaMesAnterior.getYear());

        llenar_tabla();

        btn_cargar.doClick();
    }

    public class Modelo extends DefaultTableModel {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    private void llenar_tabla() {
        try {
            tb_resultado.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);

            modelo.addColumn("");

            for (int month = 1; month <= 12; month++) {
                String monthYear = new SimpleDateFormat("MMMM-yyyy", new Locale("es", "ES")).format(new Date(currentYear - 1900, month - 1, 1));
                modelo.addColumn(monthYear);
            }

            modelo.addColumn("Total");

            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "SELECT Concepto FROM EstructuraFlujoCaja ORDER BY posicion ASC";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                modelo.addRow(new String[]{rs.getString(1)});
            }

            MultiRowCellRenderer renderer = new MultiRowCellRenderer();

            tb_resultado.setDefaultRenderer(Object.class, renderer);

            sql = "SELECT Posicion, Color_Fila, Fila_Negrita FROM EstructuraFlujoCaja ORDER BY posicion ASC";
            rs = st.executeQuery(sql);
            while (rs.next()) {
                renderer.addRowStyle(rs.getInt(1), rs.getString(2), rs.getBoolean(3));
            }

            renderer.setCenterColumns(1, tb_resultado.getColumnCount() - 1);
            renderer.setBoldColumn(tb_resultado.getColumnCount() - 1);

            rs.close();
            st.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Flujo_Caja.class.getName()).log(Level.SEVERE, null, ex);
        }

        año.addPropertyChangeListener("year", (java.beans.PropertyChangeEvent evt) -> {
            try {
                int selectedYear = año.getYear();

                modelo.setRowCount(0);
                modelo.setColumnCount(0);

                modelo.addColumn("");

                for (int month = 1; month <= 12; month++) {
                    String monthYear = new SimpleDateFormat("MMMM-yyyy", new Locale("es", "ES")).format(new Date(selectedYear - 1900, month - 1, 1));
                    modelo.addColumn(monthYear);
                }

                modelo.addColumn("Total");

                Connection con = Conexion.getConnection();
                Statement st = con.createStatement();
                String sql = "SELECT Concepto FROM EstructuraFlujoCaja ORDER BY posicion ASC";
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {
                    modelo.addRow(new String[]{rs.getString(1)});
                }

                MultiRowCellRenderer renderer = new MultiRowCellRenderer();

                tb_resultado.setDefaultRenderer(Object.class, renderer);

                sql = "SELECT Posicion, Color_Fila, Fila_Negrita FROM EstructuraFlujoCaja ORDER BY posicion ASC";
                rs = st.executeQuery(sql);
                while (rs.next()) {
                    renderer.addRowStyle(rs.getInt(1), rs.getString(2), rs.getBoolean(3));
                }

                renderer.setCenterColumns(1, tb_resultado.getColumnCount() - 1);
                renderer.setBoldColumn(tb_resultado.getColumnCount() - 1);

                rs.close();
                st.close();
                con.close();

                btn_cargar.doClick();
            } catch (SQLException ex) {
                Logger.getLogger(Flujo_Caja.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    class RowStyle {

        Color color;
        boolean bold;

        public RowStyle(Color color, boolean bold) {
            this.color = color;
            this.bold = bold;
        }
    }

    class MultiRowCellRenderer extends DefaultTableCellRenderer {

        private final Map<Integer, RowStyle> rowStyles = new HashMap<>();
        private int centerFromColumn = -1;
        private int centerToColumn = -1;
        private int boldColumn = -1;

        public void addRowStyle(int row, String colorHex, boolean bold) {
            Color color = Color.decode(colorHex);
            rowStyles.put(row, new RowStyle(color, bold));
        }

        public void setCenterColumns(int from, int to) {
            this.centerFromColumn = from;
            this.centerToColumn = to;
        }

        public void setBoldColumn(int column) {
            this.boldColumn = column;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
            Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            RowStyle style = rowStyles.get(row);
            if (style != null) {
                if (isSelected) {
                    comp.setBackground(table.getSelectionBackground());
                    comp.setForeground(table.getSelectionForeground());
                } else {
                    comp.setBackground(style.color);
                    comp.setForeground(table.getForeground());
                }
                comp.setFont(comp.getFont().deriveFont(style.bold ? Font.BOLD : Font.PLAIN));
            } else {
                if (isSelected) {
                    comp.setBackground(table.getSelectionBackground());
                    comp.setForeground(table.getSelectionForeground());
                } else {
                    comp.setBackground(table.getBackground());
                    comp.setForeground(table.getForeground());
                }
                comp.setFont(comp.getFont().deriveFont(Font.PLAIN));
            }

            if (column >= centerFromColumn && column <= centerToColumn) {
                setHorizontalAlignment(SwingConstants.CENTER);
            } else {
                setHorizontalAlignment(SwingConstants.LEFT);
            }

            if (column == boldColumn) {
                comp.setFont(comp.getFont().deriveFont(Font.BOLD));
            }

            return comp;
        }
    }

    private void ajustarColumnas(JTable table) {
        TableColumnModel columnModel = table.getColumnModel();
        for (int columnIndex = 0; columnIndex < columnModel.getColumnCount(); columnIndex++) {
            TableColumn column = columnModel.getColumn(columnIndex);

            TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
            Object headerValue = column.getHeaderValue();
            Component headerComponent = headerRenderer.getTableCellRendererComponent(table, headerValue, false, false, -1, columnIndex);
            int headerWidth = headerComponent.getPreferredSize().width;

            int maxCellWidth = headerWidth;
            for (int rowIndex = 0; rowIndex < table.getRowCount(); rowIndex++) {
                TableCellRenderer cellRenderer = table.getCellRenderer(rowIndex, columnIndex);
                Component cellComponent = table.prepareRenderer(cellRenderer, rowIndex, columnIndex);
                int cellWidth = cellComponent.getPreferredSize().width;
                maxCellWidth = Math.max(maxCellWidth, cellWidth);
            }

            column.setMinWidth(maxCellWidth);
        }
    }

    private void limpiartabla() {
        for (int j = 1; j < tb_resultado.getColumnCount(); j++) {
            for (int i = 0; i < tb_resultado.getRowCount(); i++) {
                tb_resultado.setValueAt("", i, j);
            }
        }
    }

    public void datos_ingreso() {
        try {
            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "SELECT FORMAT(DATEFROMPARTS(año, mes, 1), 'MMMM-yyyy', 'es-ES') as Mes,  I.[Plan Cuenta], SUM(I.[Haber MN]) AS [Haber MN] FROM ingreso I WHERE Año = " + año.getYear() + " "
                    + "GROUP BY I.Año, I.Mes, I.[Plan Cuenta] "
                    + "ORDER BY I.Mes ASC";

            ResultSet rs = st.executeQuery(sql);
            ResultSetMetaData metaData = rs.getMetaData();

            int columnCount = metaData.getColumnCount();

            DefaultTableModel model = new DefaultTableModel();

            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(metaData.getColumnName(i));
            }

            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }

            model.addColumn("Concepto");

            tb_IngresosTemporal.setModel(model);

            try (BufferedReader br = new BufferedReader(new FileReader("ingreso_FlujoCaja.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        String concepto = parts[0].trim();
                        String planCuenta = parts[1].trim();
                        for (int i = 0; i < model.getRowCount(); i++) {
                            if (planCuenta.equals(model.getValueAt(i, 1))) {
                                model.setValueAt(concepto, i, 3);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Map<String, Double> groupedData = new LinkedHashMap<>();

            for (int i = 0; i < model.getRowCount(); i++) {
                String key = model.getValueAt(i, 0) + "|" + model.getValueAt(i, 3);
                double valor = Double.parseDouble(model.getValueAt(i, 2).toString());
                groupedData.put(key, groupedData.getOrDefault(key, 0.0) + valor);
            }

            model.setRowCount(0);

            DecimalFormat df = new DecimalFormat("###,###,##0.00");

            for (Map.Entry<String, Double> entry : groupedData.entrySet()) {
                String[] keys = entry.getKey().split("\\|");
                model.addRow(new Object[]{keys[0], keys[1], df.format(entry.getValue())});
            }

            for (int i = 0; i < model.getRowCount(); i++) {
                Object fecha = model.getValueAt(i, 0);
                Object concepto = model.getValueAt(i, 1);
                Object total = model.getValueAt(i, 2);
                for (int j = 0; j < tb_resultado.getRowCount(); j++) {
                    if (tb_resultado.getValueAt(j, 0).equals(concepto)) {
                        for (int k = 0; k < tb_resultado.getColumnCount(); k++) {
                            if (tb_resultado.getColumnName(k).equals(fecha)) {
                                tb_resultado.setValueAt(total, j, k);
                            }
                        }
                    }

                }
            }

            rs.close();
            st.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(Flujo_Caja.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    String conceptosduplicados = "";

    public void datos_egreso() {
        try {
            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "SELECT "
                    + "	FORMAT(DATEFROMPARTS(E.año, E.mes, 1), 'MMMM-yyyy', 'es-ES') as Mes, "
                    + "	E.[Nro Doc# Ident# Benef#], "
                    + "	Glosa, "
                    + "	CASE WHEN Moneda = 'US$' THEN [TC ME] * Haber ELSE [Haber MN] END AS [Haber MN] "
                    + "FROM egreso E "
                    + "	LEFT JOIN glosaEgreso GE ON E.CUO = GE.Numero AND E.Año = GE.Año AND E.Mes = GE.Mes "
                    + "WHERE "
                    + " Abono > 0  AND "
                    + "	[Haber MN] > 0 AND "
                    + "	Glosa IS NOT NULL AND "
                    + "	E.Año = " + año.getYear() + " "
                    + "GROUP BY "
                    + "	E.Moneda, "
                    + "	E.Año, "
                    + "	E.Mes, "
                    + "	E.[Nro Doc# Ident# Benef#], "
                    + "	Glosa, "
                    + "	[TC ME], "
                    + "	Haber, "
                    + "	[Haber MN] "
                    + "ORDER BY "
                    + "	E.Mes ASC";

            ResultSet rs = st.executeQuery(sql);
            ResultSetMetaData metaData = rs.getMetaData();

            int columnCount = metaData.getColumnCount();

            DefaultTableModel model = new DefaultTableModel();

            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(metaData.getColumnName(i));
            }

            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }

            model.addColumn("Concepto");

            tb_IngresosTemporal.setModel(model);

            try (BufferedReader br = new BufferedReader(new FileReader("egreso_FlujoCaja.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        String concepto = parts[0].trim();
                        String glosa = parts[1].trim();
                        String ruc = parts[2].trim();

                        String[] valores = glosa.split("%");

                        for (int i = 0; i < model.getRowCount(); i++) {
                            String valorcadena = model.getValueAt(i, 2).toString();
                            boolean contiene = false;
                            for (String v : valores) {
                                if (valorcadena.contains(v)) {
                                    contiene = true;
                                    break;
                                }
                            }
                            if (ruc.equals(model.getValueAt(i, 1).toString()) && contiene) {
                                model.setValueAt(concepto, i, 4);
                            }
                        }
                    } else if (parts.length == 2) {
                        String concepto = parts[0].trim();
                        String glosa = parts[1].trim();

                        String[] valores = glosa.split("%");

                        for (int i = 0; i < model.getRowCount(); i++) {
                            String valorcadena = model.getValueAt(i, 2).toString();
                            boolean contiene = false;
                            for (String v : valores) {
                                if (valorcadena.contains(v)) {
                                    contiene = true;
                                    break;
                                }
                            }
                            if (contiene) {
                                if (model.getValueAt(i, 4) != null) {
                                    conceptosduplicados = conceptosduplicados + model.getValueAt(i, 0) + " |  - Documento: " + model.getValueAt(i, 1) + " | Glosa: " + model.getValueAt(i, 2) + "\n";
                                }
                                model.setValueAt(concepto, i, 4);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Map<String, Double> groupedData = new LinkedHashMap<>();

            for (int i = 0; i < model.getRowCount(); i++) {
                String key = model.getValueAt(i, 0) + "|" + model.getValueAt(i, 4);
                double valor = Double.parseDouble(model.getValueAt(i, 3).toString());
                groupedData.put(key, groupedData.getOrDefault(key, 0.0) + valor);
            }

            // Limpiar el modelo de tabla
            model.setRowCount(0);

            // Crear un formato para mostrar solo los decimales cuando sea necesario
            DecimalFormat df = new DecimalFormat("###,###,##0.00");

            // Agregar los datos agrupados al modelo de tabla
            for (Map.Entry<String, Double> entry : groupedData.entrySet()) {
                String[] keys = entry.getKey().split("\\|");
                model.addRow(new Object[]{keys[0], keys[1], entry.getValue()});
            }

            for (int i = 0; i < model.getRowCount(); i++) {
                Object fecha = model.getValueAt(i, 0);
                Object concepto = model.getValueAt(i, 1);
                Object total = model.getValueAt(i, 2);
                for (int j = 0; j < tb_resultado.getRowCount(); j++) {
                    if (tb_resultado.getValueAt(j, 0).equals(concepto)) {
                        for (int k = 0; k < tb_resultado.getColumnCount(); k++) {
                            if (tb_resultado.getColumnName(k).equals(fecha)) {
                                tb_resultado.setValueAt("-" + df.format(total), j, k);
                            }
                        }
                    }

                }
            }

            rs.close();
            st.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(Flujo_Caja.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void datos_cambiasigno() {
        try {
            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "SELECT Concepto, Cambia_Signo FROM EstructuraFlujoCaja WHERE Suma_Total = 1 ORDER BY posicion ASC";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                for (int i = 0; i < tb_resultado.getRowCount(); i++) {
                    if (tb_resultado.getValueAt(i, 0).equals(rs.getString(1)) && rs.getBoolean(2) == true) {
                        for (int j = 1; j < tb_resultado.getColumnCount() - 1; j++) {
                            if (!tb_resultado.getValueAt(i, j).equals("")) {
                                tb_resultado.setValueAt(String.valueOf(Double.parseDouble(tb_resultado.getValueAt(i, j).toString().replaceAll(",", "")) * -1), i, j);
                            }
                        }
                    }
                }
            }
            rs.close();
            st.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(Flujo_Caja.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void datos_sumafilastotales() {
        try {
            DecimalFormat df = new DecimalFormat("###,###,##0.00");

            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "SELECT Posicion, Suma_Posiciones FROM EstructuraFlujoCaja WHERE Suma_Posiciones IS NOT NULL";
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                String[] posicionesStr = rs.getString(2).split(",");

                int[] filasASumar = new int[posicionesStr.length];
                for (int i = 0; i < posicionesStr.length; i++) {
                    filasASumar[i] = Integer.parseInt(posicionesStr[i].trim());
                }

                int filaDestino = rs.getInt(1);

                for (int j = 1; j < tb_resultado.getColumnCount(); j++) {
                    double suma = 0.0;

                    for (int fila : filasASumar) {
                        Object valor = tb_resultado.getValueAt(fila, j);
                        if (valor != null && !valor.toString().isEmpty()) {
                            try {
                                suma += Double.parseDouble(valor.toString().replace(",", ""));
                            } catch (NumberFormatException e) {
                                Logger.getLogger(Flujo_Caja.class.getName()).log(Level.SEVERE, null, e);
                            }
                        }
                    }

                    if (suma == 0.0) {
                        tb_resultado.setValueAt("", filaDestino, j);
                    } else {
                        tb_resultado.setValueAt(df.format(suma), filaDestino, j);
                    }
                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(Flujo_Caja.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void datos_sumartotalacumulado() {
        try {
            DecimalFormat df = new DecimalFormat("###,###,##0.00");

            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "SELECT Posicion FROM EstructuraFlujoCaja WHERE Suma_Total = 1";
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                double sumaFila = 0;
                for (int j = 0; j < tb_resultado.getColumnCount() - 1; j++) {
                    Object value = tb_resultado.getValueAt(rs.getInt(1), j);
                    double valor = 0;
                    if (value != null && !value.toString().isEmpty()) {
                        try {
                            valor = Double.parseDouble(value.toString().replaceAll(",", ""));
                        } catch (NumberFormatException e) {

                        }
                    }
                    sumaFila += valor;
                }
                tb_resultado.setValueAt(df.format(sumaFila), rs.getInt(1), tb_resultado.getColumnCount() - 1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Flujo_Caja.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void datos_saldoacumulado() {
        try {
            DecimalFormat df = new DecimalFormat("###,###,##0.00");

            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "SELECT Posicion, Fila_SaldoAcumulado FROM EstructuraFlujoCaja WHERE Es_SaldoAcumulado = 1";
            ResultSet rs = st.executeQuery(sql);

            Calendar calendar = Calendar.getInstance();
            int currentMonth = calendar.get(Calendar.MONTH);
            int currentYear = calendar.get(Calendar.YEAR);

            SimpleDateFormat sdf = new SimpleDateFormat("MMMM-yyyy", Locale.getDefault());

            int limitColumn = -1;
            for (int j = 1; j < tb_resultado.getColumnCount() - 1; j++) {
                String columnName = tb_resultado.getColumnName(j);
                try {
                    Date date = sdf.parse(columnName);
                    Calendar colCalendar = Calendar.getInstance();
                    colCalendar.setTime(date);
                    int colMonth = colCalendar.get(Calendar.MONTH);
                    int colYear = colCalendar.get(Calendar.YEAR);

                    if (colYear == currentYear && colMonth == currentMonth) {
                        limitColumn = j;
                        break;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if (limitColumn == -1) {
                limitColumn = tb_resultado.getColumnCount() - 1;
            }

            while (rs.next()) {
                double sumaFila = 0;
                for (int j = 1; j < limitColumn; j++) {
                    Object value = tb_resultado.getValueAt(rs.getInt(2), j);
                    double valor = 0;
                    if (value != null && !value.toString().isEmpty()) {
                        try {
                            valor = Double.parseDouble(value.toString().replaceAll(",", ""));
                        } catch (NumberFormatException e) {

                        }
                    }
                    sumaFila += valor;
                    tb_resultado.setValueAt(df.format(sumaFila), rs.getInt(1), j);
                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(Flujo_Caja.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String obtenerPlanesCuenta(String txt) {
        String filePath = txt;
        StringBuilder result = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length >= 2) {
                    result.append("'").append(columns[1]).append("',");
                }
            }
            if (result.length() > 0) {
                result.setLength(result.length() - 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();

    }

    private String buscarGlosaRUC(String nombretxt) {
        Set<String> uniqueCuentas = new HashSet<>();
        Set<String> uniqueNumeros = new HashSet<>();
        boolean agregarCondicionNumeros = false;

        try (BufferedReader br = new BufferedReader(new FileReader(nombretxt))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String glosa = parts[1].trim();
                    String[] palabras = glosa.split("%");
                    for (String palabra : palabras) {
                        uniqueCuentas.add("GE.GLOSA NOT LIKE '%" + palabra + "%'");
                    }

                    String numero = parts[2].trim();
                    uniqueNumeros.add(numero);
                    agregarCondicionNumeros = true;
                } else if (parts.length == 2) {
                    String glosa = parts[1].trim();
                    String[] palabras = glosa.split("%");
                    for (String palabra : palabras) {
                        uniqueCuentas.add("GE.GLOSA NOT LIKE '%" + palabra + "%'");
                    }
                    agregarCondicionNumeros = false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String cuentas = String.join(" AND ", uniqueCuentas);
        String numeros = String.join(",", uniqueNumeros);
        String resultadoFinal = "'" + numeros.replace(",", "','") + "'";
        String condicionNumeros = agregarCondicionNumeros ? " OR [Nro Doc# Ident# Benef#] NOT IN (" + resultadoFinal + ")" : "";

        return cuentas + condicionNumeros;
    }

    private void verificar_totalizados() throws SQLException, ParseException {
        for (int j = 1; j < tb_resultado.getColumnCount() - 1; j++) {
            try {
                String dateString = tb_resultado.getColumnName(j);

                SimpleDateFormat inputFormat = new SimpleDateFormat("MMMM-yyyy", new Locale("es", "ES"));

                Date date = inputFormat.parse(dateString);

                SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

                String month = monthFormat.format(date);
                String year = yearFormat.format(date);
                
                obtener_PlanCuentaFaltantesIngresos(year, month, obtenerPlanesCuenta("ingreso_FlujoCaja.txt"));
                
                if (!conceptosduplicados.equals("")) {
                    contador2++;
                    mostrarConceptosDuplicados(conceptosduplicados, tb_resultado, dateString);
                }
                
                obtener_PlanCuentaFaltantesEgresos(year, month, buscarGlosaRUC("egreso_FlujoCaja.txt"));

            } catch (ParseException ex) {
                Logger.getLogger(Flujo_Caja.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void mostrarConceptosDuplicados(String conceptosDuplicados, JTable tb_resultado, String columnName) {
        String[] lineas = conceptosDuplicados.split("\n");
        Map<String, List<String>> conceptosPorFecha = new HashMap<>();

        for (String linea : lineas) {
            String fecha = linea.split(" \\| ")[0].trim();
            conceptosPorFecha.putIfAbsent(fecha, new ArrayList<>());
            conceptosPorFecha.get(fecha).add(linea);
        }

        if (conceptosPorFecha.containsKey(columnName)) {
            List<String> conceptos = conceptosPorFecha.get(columnName);
            StringBuilder mensaje = new StringBuilder("Conceptos para " + columnName + ":\n");
            for (String concepto : conceptos) {
                mensaje.append(concepto).append("\n");
            }
            JOptionPane.showMessageDialog(null, mensaje.toString(), "Conceptos Duplicados", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void obtener_PlanCuentaFaltantesIngresos(String vaño, String vmes, String planes) {
        DateFormatSymbols dfs = new DateFormatSymbols(new Locale("es", "ES"));
        String[] months = dfs.getMonths();

        String cuentas = "<html><font color = 'black'>En el mes de " + months[Integer.parseInt(vmes) - 1] + " del año " + vaño + " falta considerar en INGRESOS las siguientes cuentas:</font><font color= 'red'><br><b>";
        boolean found = false;

        try {
            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "SELECT DISTINCT [Plan Cuenta] FROM ingreso WHERE [Haber MN] > 0 and año = " + vaño + " and mes = " + vmes + " AND [Plan Cuenta] NOT IN (" + planes + ")";
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                cuentas += " - " + rs.getString(1) + "<br>";
                found = true;
            }

            if (found) {
                contador++;
                cuentas += "</b></font></html>";
                JOptionPane.showMessageDialog(null, cuentas, "ERROR", JOptionPane.ERROR_MESSAGE);
            }

            con.close();
            st.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(Flujo_Caja.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void exportQueryToExcel(Connection connection, String sql, String excelFilePath) {
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            XSSFWorkbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Query Results");

            ResultSetMetaData metaData = rs.getMetaData();
            int numberOfColumns = metaData.getColumnCount();
            Row headerRow = sheet.createRow(0);
            for (int i = 1; i <= numberOfColumns; i++) {
                Cell headerCell = headerRow.createCell(i - 1);
                headerCell.setCellValue(metaData.getColumnName(i));
            }

            int rowIndex = 1;
            while (rs.next()) {
                Row row = sheet.createRow(rowIndex++);
                for (int i = 1; i <= numberOfColumns; i++) {
                    Cell cell = row.createCell(i - 1);
                    cell.setCellValue(rs.getString(i));
                }
            }

            try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
                workbook.write(outputStream);
            }

            workbook.close();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void obtener_PlanCuentaFaltantesEgresos(String vaño, String vmes, String glosasruc) {
        DateFormatSymbols dfs = new DateFormatSymbols(new Locale("es", "ES"));
        String[] months = dfs.getMonths();
        boolean found = false;

        try {
            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "SELECT *  FROM egreso E "
                    + "LEFT JOIN glosaEgreso GE ON E.CUO = GE.Numero AND E.Año = GE.Año AND E.Mes = GE.Mes "
                    + "WHERE [Haber MN] > 0 AND Abono > 0 AND E.Año = " + vaño + " AND E.MES = " + vmes + " AND (GE.GLOSA NOT LIKE '%TRANSFERENCIA%' AND " + glosasruc + " )";
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                found = true;
            }

            if (found) {
                contador2++;

                if (JOptionPane.showConfirmDialog(this,
                        "<html><font color = 'black'>En el mes de</font> <font color= 'red'><b>" + months[Integer.parseInt(vmes) - 1] + " del año " + vaño
                        + "</b></font><font color= 'black'> falta considerar en</font><font color= 'red'> <b>EGRESOS</b></font> <font color= 'black'>algunos registros con las siguientes GLOSAS,<br><br><b>¿Desea exportar un Archivo Excel?</b></font>", "Mensaje", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION) {

                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Especifica el archivo para guardar");

                    int userSelection = fileChooser.showSaveDialog(null);

                    if (userSelection == JFileChooser.APPROVE_OPTION) {
                        File fileToSave = fileChooser.getSelectedFile();
                        String excelFilePath = fileToSave.getAbsolutePath();
                        if (!excelFilePath.endsWith(".xlsx")) {
                            excelFilePath += ".xlsx";
                        }
                        exportQueryToExcel(con, sql, excelFilePath);

                    }

                }
            }

            con.close();
            st.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(Flujo_Caja.class.getName()).log(Level.SEVERE, null, ex);
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
        jScrollPane2 = new javax.swing.JScrollPane();
        tb_IngresosTemporal = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tb_EgresosTemporal = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        tb_cuenta33 = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_resultado = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        btn_anual = new javax.swing.JButton();
        btn_guardar1 = new javax.swing.JButton();
        btn_cargar = new javax.swing.JButton();
        año = new com.toedter.calendar.JYearChooser();
        jLabel5 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();

        tb_IngresosTemporal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(tb_IngresosTemporal);

        tb_EgresosTemporal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(tb_EgresosTemporal);

        tb_cuenta33.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(tb_cuenta33);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Flujo de Caja");

        jPanel6.setBackground(new java.awt.Color(0, 102, 153));

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Estado de Flujo de Caja Ejecutado");
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
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "Datos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 12))); // NOI18N
        jPanel4.setPreferredSize(new java.awt.Dimension(1160, 383));

        tb_resultado.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        tb_resultado.setModel(modelo);
        tb_resultado.setRowHeight(28);
        tb_resultado.setShowHorizontalLines(true);
        tb_resultado.setShowVerticalLines(true);
        tb_resultado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_resultadoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tb_resultado);

        jLabel2.setFont(new java.awt.Font("Bahnschrift", 1, 13)); // NOI18N
        jLabel2.setText("ASOCIACIÓN CÍRCULO MILITAR DEL PERÚ");

        jLabel3.setFont(new java.awt.Font("Bahnschrift", 1, 13)); // NOI18N
        jLabel3.setText("Dirección de Recreación");

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Opciones", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 13))); // NOI18N

        btn_anual.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btn_anual.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Terms and Conditions.png"))); // NOI18N
        btn_anual.setText("Imprimir Reporte Anual");
        btn_anual.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btn_anual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_anualActionPerformed(evt);
            }
        });

        btn_guardar1.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btn_guardar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_outgoing_data_32px.png"))); // NOI18N
        btn_guardar1.setText("Validar Datos");
        btn_guardar1.setEnabled(false);
        btn_guardar1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btn_guardar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_guardar1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_anual)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_guardar1)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_guardar1)
                    .addComponent(btn_anual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btn_cargar.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btn_cargar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Checkmark.png"))); // NOI18N
        btn_cargar.setText("Cargar Datos");
        btn_cargar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btn_cargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cargarActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel5.setText("Año:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 622, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(año, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_cargar)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(año, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_cargar)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 1163, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 765, Short.MAX_VALUE)
                .addContainerGap())
        );

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Upload To FTP.png"))); // NOI18N
        jMenu1.setText("Subir");
        jMenu1.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N

        jMenuItem1.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/sobresalir.png"))); // NOI18N
        jMenuItem1.setText("Archivo Excel");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ajustes.png"))); // NOI18N
        jMenu2.setText("Configuración");
        jMenu2.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/flecha-derecha.png"))); // NOI18N
        jMenuItem2.setText("Ingresos");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/flecha-hacia-abajo.png"))); // NOI18N
        jMenuItem3.setText("Egresos");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/MacBook Settings.png"))); // NOI18N
        jMenuItem5.setText("Estructura");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_cargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cargarActionPerformed
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        conceptosduplicados = "";

        //LIMPIAMOS TABLA
        limpiartabla();

        //LLENAMOS DATOS DE BD
        datos_ingreso();
        datos_egreso();

        //CAMBIA SIGNO SI ES POSITIVO O NEGATIVO EL VALOR
        datos_cambiasigno();

        //SUMA LAS FILAS TOTALES      
        datos_sumafilastotales();

        datos_saldoacumulado();

        //SUMA COLUMNA TOTAL ACUMULADO
        datos_sumartotalacumulado();

        ajustarColumnas(tb_resultado);
        btn_guardar1.setEnabled(true);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btn_cargarActionPerformed

    private JRDataSource createDataSourceFromJTable(JTable table) {
        List<Map<String, ?>> data = new ArrayList<>();

        for (int row = 0; row < table.getRowCount(); row++) {
            Map<String, Object> rowData = new HashMap<>();

            for (int column = 0; column < table.getColumnCount(); column++) {
                rowData.put("Column" + (column + 1), table.getValueAt(row, column));
            }

            TableCellRenderer renderer = table.getCellRenderer(row, 0);
            Component component = table.prepareRenderer(renderer, row, 0);
            Color backgroundColor = component.getBackground();

            boolean isBold = component.getFont().isBold();

            String colorName = getColorHex(backgroundColor);
            rowData.put("backgroundColor", colorName);
            rowData.put("isBold", isBold);

            data.add(rowData);
        }

        return new JRBeanCollectionDataSource(data);
    }

    private String getColorHex(Color color) {
        if (color == null) {
            return null;
        }

        if (Color.WHITE.equals(color)) {
            return null;
        }

        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    private Map<String, Object> getColumnNamesAsParameters(JTable table) {
        Map<String, Object> parameters = new HashMap<>();
        for (int i = 0; i < table.getColumnCount(); i++) {
            parameters.put("COLUMN_NAME_" + (i + 1), table.getColumnName(i));
        }
        return parameters;
    }
    
    private void btn_anualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_anualActionPerformed
        int añoIngresado = año.getYear();

        int añoActual = Calendar.getInstance().get(Calendar.YEAR);

        Calendar calendar = Calendar.getInstance();
        if (añoIngresado > añoActual) {
            JOptionPane.showMessageDialog(null, "Verifique el año seleccionado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (añoIngresado <= añoActual) {
            calendar.set(Calendar.MONTH, Calendar.DECEMBER);
            calendar.set(Calendar.YEAR, añoIngresado);
        }

        try {
            btn_anual.setEnabled(false);
            setCursor(new Cursor(Cursor.WAIT_CURSOR));

            JRDataSource dataSource = createDataSourceFromJTable(tb_resultado);

            Map<String, Object> parameters = getColumnNamesAsParameters(tb_resultado);

            JasperReport report = JasperCompileManager.compileReport(System.getProperty("user.dir") + "/reportes/CajaAnualDinamico.jrxml");
            JasperPrint print = JasperFillManager.fillReport(report, parameters, dataSource);
            JasperViewer viewer = new JasperViewer(print, false);

            JPanel panel = new JPanel(new BorderLayout());
            panel.add(viewer.getContentPane(), BorderLayout.CENTER);

            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Rectangle bounds = env.getMaximumWindowBounds();

            JDialog dialog = new JDialog();
            dialog.setTitle("Flujo de Caja - Anual");
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setModal(true);
            dialog.setSize(bounds.width, bounds.height);
            dialog.setLocationRelativeTo(null);
            dialog.getContentPane().add(panel);
            btn_anual.setEnabled(true);
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            dialog.setVisible(true);

        } catch (JRException ex) {
            Logger.getLogger(Flujo_Caja.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_anualActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        CargaArchivos ini = new CargaArchivos(this, true);
        ini.toFront();
        ini.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void tb_resultadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_resultadoMouseClicked
        if (evt.getClickCount() == 2) {
            Connection con = null;
            Statement st = null;
            ResultSet rs = null;

            try {
                int fila = tb_resultado.getSelectedRow();
                int columna = tb_resultado.getSelectedColumn();
                
                if (tb_resultado.getValueAt(fila, columna) == null || tb_resultado.getValueAt(fila, columna).toString().equals("") || tb_resultado.getValueAt(fila, columna).toString().isEmpty()) {
                    return;
                }

                String concepto = tb_resultado.getValueAt(fila, 0).toString();
                String mesaño = tb_resultado.getColumnName(columna);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM-yyyy", new Locale("es"));
                YearMonth fecha = YearMonth.parse(mesaño, formatter);

                int month = fecha.getMonthValue();
                int year = fecha.getYear();

                DecimalFormat df = new DecimalFormat("###,###,##0.00");

                con = Conexion.getConnection();
                st = con.createStatement();
                String sql = "SELECT Posicion, LOWER(Tipo) AS Tipo FROM EstructuraFlujoCaja WHERE Tipo IS NOT NULL";
                rs = st.executeQuery(sql);

                while (rs.next()) {
                    if (rs.getInt(1) == fila && rs.getString(2).equals("ingreso")) {
                        Detallado ini = new Detallado(this, true);
                        ini.cargarDatos(year, month, rs.getString(2), rs.getString(2) + "_FlujoCaja.txt", concepto);
                        ini.toFront();
                        ini.setVisible(true);
                    }
                    
                    if (rs.getInt(1) == fila && rs.getString(2).equals("egreso")) {
                        Detallado ini = new Detallado(this, true);
                        ini.cargarDatosCajaEgresos(year, month, rs.getString(2), rs.getString(2) + "_FlujoCaja.txt", concepto);
                        ini.toFront();
                        ini.setVisible(true);
                    }
                }

            } catch (SQLException ex) {
                Logger.getLogger(Flujo_Caja.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (st != null) {
                        st.close();
                    }
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(Flujo_Caja.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_tb_resultadoMouseClicked

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        EstructuraFEfectivo ini = new EstructuraFEfectivo(this, true);
        ini.Ingresos_Caja();
        ini.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        EstructuraFCaja ini = new EstructuraFCaja(this, true);
        ini.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed
    int contador = 0, contador2 = 0;
    private void btn_guardar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_guardar1ActionPerformed
        try {
            contador = 0;
            contador2 = 0;

            verificar_totalizados();
            if (contador == 0 && contador2 == 0) {
                JOptionPane.showMessageDialog(null, "Se han verificado y los datos se encuentran conformes", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException | ParseException ex) {
            Logger.getLogger(Flujo_Caja.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btn_guardar1ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        EstructuraFC ini = new EstructuraFC(this, true);
        ini.setVisible(true);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

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
                new Flujo_Caja().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JYearChooser año;
    private javax.swing.JButton btn_anual;
    public static javax.swing.JButton btn_cargar;
    private javax.swing.JButton btn_guardar1;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable tb_EgresosTemporal;
    private javax.swing.JTable tb_IngresosTemporal;
    private javax.swing.JTable tb_cuenta33;
    private javax.swing.JTable tb_resultado;
    // End of variables declaration//GEN-END:variables
}
