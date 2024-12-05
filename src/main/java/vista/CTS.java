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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Farmacia-Ingeniero
 */
public class CTS extends javax.swing.JFrame {

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
    public CTS() {
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
                Application anteriorFrame = new Application();
                anteriorFrame.setVisible(true);
            }
        });
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        jdc_mes.setMonth((Calendar.getInstance().get(Calendar.MONTH))-1);
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
        modelo.setRowCount(0);
        modelo.setColumnCount(0);

        JTableHeader header = tb_resultado.getTableHeader();
        header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 50));
        header.setBackground(new java.awt.Color(255, 217, 102));
        tb_resultado.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

        modelo.addColumn("Nº");
        modelo.addColumn("Nro Doc. Ident.");
        modelo.addColumn("Trabajador");
        modelo.addColumn("Inicio CTS");
        modelo.addColumn("Rem. Base");
        modelo.addColumn("F. Ingreso");
        modelo.addColumn("Cargo");
        modelo.addColumn("Centro Costo");
        modelo.addColumn("Sub Nivel");
        modelo.addColumn("Asig. Fam.");
        modelo.addColumn("Neto Pagar");
        modelo.addColumn("Gratificacion Sin 9%");
        modelo.addColumn("Comisiones / Solo Bonf Prod");
        modelo.addColumn("Computable");

        agregarColumnasPorMes(jdc_mes.getMonth() + 1);

        
        modelo.addColumn("Diferencia");
        modelo.addColumn("Moneda");
        modelo.addColumn("Situacion");

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
            if (i == 4 || i == 9 || i == 10 || i == 13 || i == 24 || i == 25 || i == 26) {
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

        cargaEmpleados();

    }

    String inicioCTS, cierreCTS, queryComision, queryDescuento;

    public void agregarColumnasPorMes(int mesSeleccionado) {
        int selectedMonth = jdc_mes.getMonth(); // Obtener el mes seleccionado
        int selectYear = jdc_año.getYear();
        int newMonth = selectedMonth - 1; // Restar 1 mes
        
        if (newMonth < 0) {
            newMonth = 11; 
            selectYear = selectYear -1;
        }

        
        String[] meses = {
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto",
            "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };

        if (mesSeleccionado >= 5 && mesSeleccionado <= 10) {
            cierreCTS = java.time.LocalDate.of(jdc_año.getYear(), mesSeleccionado, 1).withDayOfMonth(java.time.LocalDate.of(jdc_año.getYear(), mesSeleccionado, 1).lengthOfMonth()).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            modelo.addColumn(cierreCTS);
            modelo.addColumn("PROVISION DIAS");

            String valorMeses = "";

            for (int i = 4; i < 10; i++) {
                modelo.addColumn("DSCTO DIAS [" + meses[i] + " " + jdc_año.getYear() + "]");

                if (i + 1 <= mesSeleccionado) {
                    valorMeses = valorMeses + (i + 1) + ",";
                }
            }
            
            modelo.addColumn("Ajuste dias");
            modelo.addColumn("Pago");
            modelo.addColumn("Provision");
            modelo.addColumn("Provision - Sistema [" + meses[newMonth] + " " + selectYear + "]");

            inicioCTS = "01/05/" + jdc_año.getYear();

            queryComision = "SELECT "
                    + "    [Nro Doc# Ident#], "
                    + "    CONVERT(DECIMAL(20,2), "
                    + "	(( "
                    + "	SUM(CASE WHEN Mes = 5 THEN [Bonif# Prod#] ELSE 0 END) + "
                    + "    SUM(CASE WHEN Mes = 6 THEN [Bonif# Prod#] ELSE 0 END) + "
                    + "    SUM(CASE WHEN Mes = 7 THEN [Bonif# Prod#] ELSE 0 END) + "
                    + "    SUM(CASE WHEN Mes = 8 THEN [Bonif# Prod#] ELSE 0 END) + "
                    + "    SUM(CASE WHEN Mes = 9 THEN [Bonif# Prod#] ELSE 0 END) + "
                    + "    SUM(CASE WHEN Mes = 10 THEN [Bonif# Prod#] ELSE 0 END)) "
                    + "	/6)) AS Comision "
                    + "FROM PlanillaSueldo "
                    + "WHERE "
                    + "    Año = " + jdc_año.getYear() + " AND Mes IN (" + valorMeses.substring(0, valorMeses.length() - 1) + ") "
                    + "GROUP BY "
                    + "    [Nro Doc# Ident#] "
                    + "HAVING "
                    + "    (CASE WHEN SUM(CASE WHEN Mes = 5 THEN [Bonif# Prod#] ELSE 0 END) > 0 THEN 1 ELSE 0 END + "
                    + "     CASE WHEN SUM(CASE WHEN Mes = 6 THEN [Bonif# Prod#] ELSE 0 END) > 0 THEN 1 ELSE 0 END + "
                    + "     CASE WHEN SUM(CASE WHEN Mes = 7 THEN [Bonif# Prod#] ELSE 0 END) > 0 THEN 1 ELSE 0 END + "
                    + "     CASE WHEN SUM(CASE WHEN Mes = 8 THEN [Bonif# Prod#] ELSE 0 END) > 0 THEN 1 ELSE 0 END + "
                    + "     CASE WHEN SUM(CASE WHEN Mes = 9 THEN [Bonif# Prod#] ELSE 0 END) > 0 THEN 1 ELSE 0 END + "
                    + "     CASE WHEN SUM(CASE WHEN Mes = 10 THEN [Bonif# Prod#] ELSE 0 END) > 0 THEN 1 ELSE 0 END) >= 3";

            queryDescuento = "SELECT "
                    + "	FORMAT(DATEFROMPARTS(Año, Mes, 1), 'MMMM yyyy') AS Mes_Año, "
                    + "	[Nro Doc# Ident#], "
                    + "	Falta "
                    + "FROM PlanillaSueldo "
                    + "WHERE "
                    + "	Año = " + jdc_año.getYear() + " AND "
                    + "	Mes IN (" + valorMeses.substring(0, valorMeses.length() - 1) + ") AND Falta > 0";

        } else if (mesSeleccionado >= 11) {
            cierreCTS = java.time.LocalDate.of((jdc_año.getYear()), mesSeleccionado, 1).withDayOfMonth(java.time.LocalDate.of((jdc_año.getYear()), mesSeleccionado, 1).lengthOfMonth()).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            modelo.addColumn(cierreCTS);
            modelo.addColumn("PROVISION DIAS");

            String valorMeses = "";

            for (int i = 10; i < 12; i++) {
                modelo.addColumn("DSCTO DIAS [" + meses[i] + " " + jdc_año.getYear() + "]");
                if (i < mesSeleccionado) {
                    valorMeses = valorMeses + (i + 1) + ",";
                }
            }
            for (int i = 0; i < 4; i++) {
                modelo.addColumn("DSCTO DIAS [" + meses[i] + " " + (jdc_año.getYear() + 1) + "]");
            }
            
            modelo.addColumn("Ajuste dias");
            modelo.addColumn("Pago");
            modelo.addColumn("Provision");
            modelo.addColumn("Provision - Sistema [" + meses[newMonth] + " " + selectYear + "]");

            inicioCTS = "01/11/" + jdc_año.getYear();

            queryDescuento = "SELECT "
                    + "	FORMAT(DATEFROMPARTS(Año, Mes, 1), 'MMMM yyyy') AS Mes_Año, "
                    + "	[Nro Doc# Ident#], "
                    + "	Falta "
                    + "FROM PlanillaSueldo "
                    + "WHERE "
                    + "	Año = " + jdc_año.getYear() + " AND Mes IN (" + valorMeses.substring(0, valorMeses.length() - 1) + ") "
                    + "	AND "
                    + "	Falta > 0";

        } else if (mesSeleccionado <= 4) {
            cierreCTS = java.time.LocalDate.of(jdc_año.getYear(), mesSeleccionado, 1).withDayOfMonth(java.time.LocalDate.of(jdc_año.getYear(), mesSeleccionado, 1).lengthOfMonth()).format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            modelo.addColumn(cierreCTS);
            modelo.addColumn("PROVISION DIAS");

            String valorMeses = "";

            for (int i = 10; i < 12; i++) {
                modelo.addColumn("DSCTO DIAS [" + meses[i] + " " + (jdc_año.getYear() - 1) + "]");
            }

            for (int i = 0; i < 4; i++) {
                modelo.addColumn("DSCTO DIAS [" + meses[i] + " " + jdc_año.getYear() + "]");
                if (i < mesSeleccionado) {
                    valorMeses = valorMeses + (i + 1) + ",";
                }
            }
            
            modelo.addColumn("Ajuste dias");
            modelo.addColumn("Pago");
            modelo.addColumn("Provision");
            modelo.addColumn("Provision - Sistema [" + meses[newMonth] + " " + selectYear + "]");
            
            inicioCTS = "01/11/" + (jdc_año.getYear() - 1);

            queryComision = "SELECT "
                    + "    [Nro Doc# Ident#], "
                    + "    CONVERT(DECIMAL(20,2), "
                    + "	(( "
                    + "	SUM(CASE WHEN Mes = 11 THEN [Bonif# Prod#] ELSE 0 END) + "
                    + "    SUM(CASE WHEN Mes = 12 THEN [Bonif# Prod#] ELSE 0 END) + "
                    + "    SUM(CASE WHEN Mes = 1 THEN [Bonif# Prod#] ELSE 0 END) + "
                    + "    SUM(CASE WHEN Mes = 2 THEN [Bonif# Prod#] ELSE 0 END) + "
                    + "    SUM(CASE WHEN Mes = 3 THEN [Bonif# Prod#] ELSE 0 END) + "
                    + "    SUM(CASE WHEN Mes = 4 THEN [Bonif# Prod#] ELSE 0 END)) "
                    + "	/6)) AS Comision "
                    + "FROM PlanillaSueldo "
                    + "WHERE "
                    + "	(Año = " + (jdc_año.getYear() - 1) + " AND Mes IN (11, 12)) "
                    + "	OR "
                    + "    (Año = " + jdc_año.getYear() + " AND Mes IN (" + valorMeses.substring(0, valorMeses.length() - 1) + ")) "
                    + "GROUP BY "
                    + "    [Nro Doc# Ident#] "
                    + "HAVING "
                    + "    (CASE WHEN SUM(CASE WHEN Mes = 11 THEN [Bonif# Prod#] ELSE 0 END) > 0 THEN 1 ELSE 0 END + "
                    + "     CASE WHEN SUM(CASE WHEN Mes = 12 THEN [Bonif# Prod#] ELSE 0 END) > 0 THEN 1 ELSE 0 END + "
                    + "     CASE WHEN SUM(CASE WHEN Mes = 1 THEN [Bonif# Prod#] ELSE 0 END) > 0 THEN 1 ELSE 0 END + "
                    + "     CASE WHEN SUM(CASE WHEN Mes = 2 THEN [Bonif# Prod#] ELSE 0 END) > 0 THEN 1 ELSE 0 END + "
                    + "     CASE WHEN SUM(CASE WHEN Mes = 3 THEN [Bonif# Prod#] ELSE 0 END) > 0 THEN 1 ELSE 0 END + "
                    + "     CASE WHEN SUM(CASE WHEN Mes = 4 THEN [Bonif# Prod#] ELSE 0 END) > 0 THEN 1 ELSE 0 END) >= 3";

            queryDescuento = "SELECT "
                    + "	FORMAT(DATEFROMPARTS(Año, Mes, 1), 'MMMM yyyy') AS Mes_Año, "
                    + "	[Nro Doc# Ident#], "
                    + "	Falta "
                    + "FROM PlanillaSueldo "
                    + "WHERE "
                    + "	(Año = " + (jdc_año.getYear() - 1) + " AND Mes IN (11, 12)) "
                    + "	OR "
                    + "	(Año = " + jdc_año.getYear() + " AND Mes IN (" + valorMeses.substring(0, valorMeses.length() - 1) + ")) "
                    + "	AND "
                    + "	Falta > 0";
        } else {
            System.out.println("Mes no válido. Ingrese un número de mes entre 1 y 12.");
        }

    }

    private void cargaEmpleados() {
        try {
            int c = 1;
            Object data[] = new Object[9];
            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "SELECT "
                    + "	[Nro Doc# Ident#], "
                    + "	(SELECT TOP 1 Trabajador FROM PlanillaSueldo WHERE [Nro Doc# Ident#] = PS.[Nro Doc# Ident#] ORDER BY Año DESC, Mes DESC) AS Trabajador, "
                    + "	(SELECT TOP 1 [Rem# Base] FROM PlanillaSueldo WHERE [Nro Doc# Ident#] = PS.[Nro Doc# Ident#] ORDER BY Año DESC, Mes DESC) AS [Rem# Base], "
                    + "	(SELECT TOP 1 FORMAT([F# Ingreso], 'dd/MM/yyyy') FROM PlanillaSueldo WHERE [Nro Doc# Ident#] = PS.[Nro Doc# Ident#] ORDER BY Año DESC, Mes DESC) AS [F# Ingreso], "
                    + "	(SELECT TOP 1 Cargo FROM PlanillaSueldo WHERE [Nro Doc# Ident#] = PS.[Nro Doc# Ident#] ORDER BY Año DESC, Mes DESC) AS Cargo, "
                    + "	(SELECT TOP 1 [Centro Costo] FROM PlanillaSueldo WHERE [Nro Doc# Ident#] = PS.[Nro Doc# Ident#] ORDER BY Año DESC, Mes DESC) AS [Centro Costo], "
                    + "	(SELECT TOP 1 [Sub Nivel] FROM PlanillaSueldo WHERE [Nro Doc# Ident#] = PS.[Nro Doc# Ident#] ORDER BY Año DESC, Mes DESC) AS [Sub Nivel] "
                    + "FROM PlanillaSueldo PS "
                    + "GROUP BY "
                    + "	[Nro Doc# Ident#] "
                    + "ORDER BY "
                    + "	Trabajador";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                data[0] = String.valueOf(c);
                data[1] = rs.getObject(1);
                data[2] = rs.getObject(2);
                data[3] = inicioCTS;
                data[4] = rs.getObject(3);
                data[5] = rs.getObject(4);
                data[6] = rs.getObject(5);
                data[7] = rs.getObject(6);
                data[8] = rs.getObject(7);
                modelo.addRow(data);
                c++;
            }
            rs.close();
            st.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(CTS.class.getName()).log(Level.SEVERE, null, ex);
        }

        cargaAsigNetoGrati();
    }

    private void cargaAsigNetoGrati() {
        try {
            Connection con = Conexion.getConnection();

            String sql = "SELECT [Nro Doc# Ident#], [Asig# Fam#], [Neto Pagar] FROM PlanillaSueldo WHERE Mes = ? AND Año = ?";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setInt(1, jdc_mes.getMonth() + 1); // Mes
            pst.setInt(2, jdc_año.getYear()); // Año

            ResultSet rs = pst.executeQuery();

            boolean tieneRegistros = false;

            while (rs.next()) {
                tieneRegistros = true;

                String nroDocIdent = rs.getString(1); // Primer campo de la consulta (Nro Doc# Ident#)
                Object asigFam = rs.getObject(2); // Segundo campo de la consulta (Asig# Fam#)
                Object netoapagar = rs.getObject(3); // tercer campo de la consulta (Neto a pagar)

                for (int i = 0; i < tb_resultado.getRowCount(); i++) {
                    if (tb_resultado.getValueAt(i, 1) != null
                            && tb_resultado.getValueAt(i, 1).toString().trim().equals(nroDocIdent.trim())) {
                        tb_resultado.setValueAt(asigFam, i, 9);
                        tb_resultado.setValueAt(netoapagar, i, 10);
                    }
                }
            }

            if (!tieneRegistros) {
                JOptionPane.showMessageDialog(null, "Falta cargar el archivo de Planilla Sueldo correspondiente al año y mes seleccionado.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

            rs.close();
            pst.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(CTS.class.getName()).log(Level.SEVERE, null, ex);
        }

        cargaComisiones();
    }

    private void cargaComisiones() {
        if (queryComision != null) {
            try {
                Connection con = Conexion.getConnection();
                Statement st = con.createStatement();
                String sql = queryComision;
                ResultSet rs = st.executeQuery(sql);

                while (rs.next()) {
                    String nroDocIdent = rs.getString(1);
                    Object comision = rs.getObject(2);

                    for (int i = 0; i < tb_resultado.getRowCount(); i++) {
                        if (tb_resultado.getValueAt(i, 1) != null
                                && tb_resultado.getValueAt(i, 1).toString().trim().equals(nroDocIdent.trim())) {
                            tb_resultado.setValueAt(comision, i, 12);
                        }
                    }
                }

                rs.close();
                st.close();
                con.close();

            } catch (SQLException ex) {
                Logger.getLogger(CTS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        cargaComputable();
    }

    private void cargaComputable() {
        for (int i = 0; i < tb_resultado.getRowCount(); i++) {
            double rembase = tb_resultado.getValueAt(i, 4) != null ? Double.parseDouble(tb_resultado.getValueAt(i, 4).toString()) : 0.0;
            double asigfam = tb_resultado.getValueAt(i, 9) != null ? Double.parseDouble(tb_resultado.getValueAt(i, 9).toString()) : 0.0;
            double grati = tb_resultado.getValueAt(i, 11) != null ? Double.parseDouble(tb_resultado.getValueAt(i, 11).toString()) : 0.0;
            double comisiones = tb_resultado.getValueAt(i, 12) != null ? Double.parseDouble(tb_resultado.getValueAt(i, 12).toString()) : 0.0;

            double computable = rembase + asigfam + grati + comisiones;
            tb_resultado.setValueAt(computable, i, 13);
        }

        calculadiasProvision();
    }

    private void calculadiasProvision() {
        for (int i = 0; i < tb_resultado.getRowCount(); i++) {
            calcularDiferencia(tb_resultado.getValueAt(i, 5).toString(), tb_resultado.getColumnName(14), i);
        }
        
        calculaMesesDescuento();
    }

    private void calculaMesesDescuento() {
        if (queryDescuento != null) {
            try {
                Connection con = Conexion.getConnection();
                Statement st = con.createStatement();
                String sql = queryDescuento;
                ResultSet rs = st.executeQuery(sql);

                while (rs.next()) {
                    String anniomes = rs.getString(1);
                    String nroDocIdent = rs.getString(2);
                    int faltas = rs.getInt(3);

                    // Recorremos las filas de tb_resultado
                    for (int i = 0; i < tb_resultado.getRowCount(); i++) {
                        // Recorremos las columnas de tb_resultado
                        for (int j = 0; j < tb_resultado.getColumnCount(); j++) {
                            // Solo consideramos las columnas entre 16 y 21
                            if (j >= 16 && j <= 21) {
                                // Obtener el nombre de la columna
                                String columnName = tb_resultado.getColumnName(j).toLowerCase();

                                // Usar matcher para extraer el contenido entre []
                                Matcher matcher = Pattern.compile("\\[(.*?)\\]").matcher(columnName);
                                if (matcher.find()) {
                                    // Comparar el contenido extraído con anniomes
                                    if (matcher.group(1).equals(anniomes)) {
                                        // Comparar nroDocIdent con el valor en la tabla
                                        if (tb_resultado.getValueAt(i, 1) != null && tb_resultado.getValueAt(i, 1).equals(nroDocIdent)) {
                                            // Actualizar la celda con el valor de faltas
                                            tb_resultado.setValueAt(faltas, i, j);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                rs.close();
                st.close();
                con.close();

            } catch (SQLException ex) {
                Logger.getLogger(CTS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        calculaProvision();
    }
    
    private void calculaProvision(){
        for (int i = 0; i < tb_resultado.getRowCount(); i++) {
            if (tb_resultado.getValueAt(i, 10) == null || tb_resultado.getValueAt(i, 10).toString().equals("0.0")) {
                tb_resultado.setValueAt("0.00", i, 24);
            }else{
                double computable = Double.parseDouble(tb_resultado.getValueAt(i, 13).toString());
                double mesevalua = Double.parseDouble(tb_resultado.getValueAt(i, 14).toString());
                double primeraparteformula = computable / 12 * mesevalua;
                
                double prevision = Double.parseDouble(tb_resultado.getValueAt(i, 15).toString());
                double mes1 = tb_resultado.getValueAt(i, 16) != null ? Double.parseDouble(tb_resultado.getValueAt(i, 16).toString()) : 0.0;
                double mes2 = tb_resultado.getValueAt(i, 17) != null ? Double.parseDouble(tb_resultado.getValueAt(i, 17).toString()) : 0.0;
                double mes3 = tb_resultado.getValueAt(i, 18) != null ? Double.parseDouble(tb_resultado.getValueAt(i, 18).toString()) : 0.0;
                double mes4 = tb_resultado.getValueAt(i, 19) != null ? Double.parseDouble(tb_resultado.getValueAt(i, 19).toString()) : 0.0;
                double mes5 = tb_resultado.getValueAt(i, 20) != null ? Double.parseDouble(tb_resultado.getValueAt(i, 20).toString()) : 0.0;
                double mes6 = tb_resultado.getValueAt(i, 21) != null ? Double.parseDouble(tb_resultado.getValueAt(i, 21).toString()) : 0.0;
                double ajustedias = tb_resultado.getValueAt(i, 22) != null ? Double.parseDouble(tb_resultado.getValueAt(i, 22).toString()) : 0.0;
                
                double segundaparteformula = computable / 360 * (prevision - mes1 - mes2 - mes3 - mes4 - mes5 - mes6 - ajustedias);
                
                double unirformula = primeraparteformula + segundaparteformula;
                
                tb_resultado.setValueAt(unirformula, i, 24);
            }
        }
    
        cargarProvisionHistorial();
    }
    
    private void cargarProvisionHistorial(){
        try {
            String columnName = tb_resultado.getColumnName(25).toLowerCase();
            int year = 0;
            String formattedMonth = null;

            // Usar matcher para extraer el contenido entre []
            Matcher matcher = Pattern.compile("\\[(.*?)\\]").matcher(columnName);
            if (matcher.find()) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");

                // Parsear el String en un objeto Date
                Date date = sdf.parse(matcher.group(1));

                // Obtener el mes (0 para enero, 11 para diciembre)
                int month = date.getMonth();  // El mes es 0-indexed (enero = 0, diciembre = 11)

                // Obtener el año
                year = date.getYear() + 1900; // getYear() retorna el año desde 1900, por lo que hay que sumar 1900

                // Formatear el mes a 2 dígitos (si es necesario)
                formattedMonth = String.format("%02d", month + 1); // Ajuste para convertir 0 -> 01, 1 -> 02, ..., 11 -> 12

            }
            
            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "SELECT NroDocumento, Provision FROM ProvisionHistorial WHERE Año = " + year + " AND Mes = " + formattedMonth + " ";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                for (int i = 0; i < tb_resultado.getRowCount(); i++) {
                    if (tb_resultado.getValueAt(i, 1).toString().equals(rs.getString(1))) {
                        tb_resultado.setValueAt(rs.getObject(2), i, 25);
                    }
                }
            }
            rs.close();
            st.close();
            con.close();
            
            
        } catch (SQLException ex) {
            Logger.getLogger(CTS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(CTS.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        cargarDiferenciasyAdicionales();
    }
    
    private void cargarDiferenciasyAdicionales(){
        for (int i = 0; i < tb_resultado.getRowCount(); i++) {
            double provision = Double.parseDouble(tb_resultado.getValueAt(i, 24).toString());
            double provisionHistorial = tb_resultado.getValueAt(i, 25) != null ? Double.parseDouble(tb_resultado.getValueAt(i, 25).toString()) : 0.0;
            double diferencia = provision - provisionHistorial;
            tb_resultado.setValueAt(diferencia, i, 26);
            tb_resultado.setValueAt("SOLES", i, 27);
            if (tb_resultado.getValueAt(i, 9) == null) {
                tb_resultado.setValueAt("INACTIVO", i, 28);
            }else{
                tb_resultado.setValueAt("ACTIVO", i, 28);
            }
        }
        packColumns(tb_resultado);
    }
    
    
    private void guardarPrevisionMensual() {
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        jButton3.setEnabled(false);
        Connection cn = null;
        try {
            String deleteSQL = "DELETE FROM ProvisionHistorial WHERE Año = ? AND Mes = ?";
            String insertSQL = "INSERT INTO ProvisionHistorial (Año, Mes, NroDocumento, Provision) VALUES (?, ?, ?, ?)";
            cn = Conexion.getConnection();
            // Iniciar una transacción
            cn.setAutoCommit(false);

            // Eliminar registros existentes para el año seleccionado
            try (PreparedStatement deleteStmt = cn.prepareStatement(deleteSQL)) {
                deleteStmt.setInt(1, jdc_año.getYear());
                deleteStmt.setInt(2, (jdc_mes.getMonth() + 1));
                deleteStmt.executeUpdate();
            }

            // Insertar nuevos registros en lotes
            try (PreparedStatement insertStmt = cn.prepareStatement(insertSQL)) {
                for (int i = 0; i < tb_resultado.getRowCount(); i++) {
                    // Obtener valores de las columnas
                    Object nroDocumentoObj = tb_resultado.getValueAt(i, 1);
                    Object valor25Obj = tb_resultado.getValueAt(i, 25);
                    Object valor26Obj = tb_resultado.getValueAt(i, 26);

                    // Manejar valores nulos o vacíos
                    String nroDocumento = (nroDocumentoObj != null) ? nroDocumentoObj.toString() : null;
                    double valor25 = (valor25Obj != null && !valor25Obj.toString().trim().isEmpty()) ? Double.parseDouble(valor25Obj.toString()) : 0.0;
                    double valor26 = (valor26Obj != null && !valor26Obj.toString().trim().isEmpty()) ? Double.parseDouble(valor26Obj.toString()) : 0.0;

                    // Calcular el valor de provision
                    double provision = valor25 + valor26;

                    // Configurar parámetros para el insert
                    insertStmt.setInt(1, jdc_año.getYear());
                    insertStmt.setInt(2, (jdc_mes.getMonth() + 1));
                    insertStmt.setString(3, nroDocumento);
                    insertStmt.setDouble(4, provision);

                    // Añadir al batch
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch(); // Ejecutar el lote
            }

            // Confirmar la transacción
            cn.commit();
            JOptionPane.showMessageDialog(this, "Prevision Mensual Guardada Exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            Logger.getLogger(Depreciacion.class.getName()).log(Level.SEVERE, "Error al guardar prevision", ex);
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
    
    
    // METODOS ADICIONALES ******************************************************
    public void calcularDiferencia(String fechaInicio, String fechaFin, int fila) {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        try {
            // Convertir las cadenas de texto en objetos Date
            Date inicio = formato.parse(fechaInicio);
            Date fin = formato.parse(fechaFin);

            // Crear objetos Calendar para trabajar con las fechas
            Calendar inicioCal = Calendar.getInstance();
            inicioCal.setTime(inicio);
            Calendar finCal = Calendar.getInstance();
            finCal.setTime(fin);

            // Inicializar las variables para la diferencia
            int anios = finCal.get(Calendar.YEAR) - inicioCal.get(Calendar.YEAR);
            int meses = finCal.get(Calendar.MONTH) - inicioCal.get(Calendar.MONTH);
            int dias = finCal.get(Calendar.DAY_OF_MONTH) - inicioCal.get(Calendar.DAY_OF_MONTH);

            // Ajustar la diferencia en meses y años
            if (dias < 0) {
                // Si los días del mes final son menores que los días del mes de inicio, restar un mes
                meses--;
                finCal.add(Calendar.MONTH, -1);
                dias += finCal.getActualMaximum(Calendar.DAY_OF_MONTH);
            }

            if (meses < 0) {
                // Si los meses son negativos, restar un año
                meses += 12;
                anios--;
            }

            // Devolver la diferencia en formato Años, Meses, Días
            int numeroLimite = obtenerPosicion((jdc_mes.getMonth() + 1));

            if (anios > 0 || meses >= numeroLimite) {
                tb_resultado.setValueAt(numeroLimite, fila, 14);
                tb_resultado.setValueAt(0, fila, 15);
            } else {
                tb_resultado.setValueAt(meses, fila, 14);
                tb_resultado.setValueAt(dias, fila, 15);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int obtenerPosicion(int mes) {
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("Mes no válido: " + mes);
        }

        // Semestre de mayo a octubre (5 a 10)
        if (mes >= 5 && mes <= 10) {
            return mes - 4; // Ajuste para que mayo sea 1
        }

        // Semestre de noviembre a abril (11, 12, 1 a 4)
        if (mes >= 11 || mes <= 4) {
            if (mes >= 11) {
                return mes - 10; // Ajuste para que noviembre sea 1
            } else {
                return mes + 2; // Ajuste para que enero sea 3
            }
        }

        // No debería llegar aquí
        throw new IllegalStateException("Error inesperado al calcular la posición.");
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_resultado = new javax.swing.JTable();
        jPanel14 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jdc_año = new com.toedter.calendar.JYearChooser();
        jLabel14 = new javax.swing.JLabel();
        jdc_mes = new com.toedter.calendar.JMonthChooser();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel16 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jPanel15 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        txt_razonsocial2 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        ExportarExcel = new javax.swing.JMenuItem();

        jButton2.setText("jButton2");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Calculo mensual de CTS (Compensación de Tiempo de Servicio)");

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
        jLabel14.setText("o");

        jdc_mes.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jdc_mes.setForeground(new java.awt.Color(255, 0, 0));
        jdc_mes.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jRadioButton1.setText("Semestre");
        jRadioButton1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton1ItemStateChanged(evt);
            }
        });

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jRadioButton2.setSelected(true);
        jRadioButton2.setText("Mes");
        jRadioButton2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioButton2ItemStateChanged(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel16.setText("->");

        jComboBox1.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CTS Mayo", "CTS Noviembre" }));
        jComboBox1.setEnabled(false);
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
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
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jdc_mes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(597, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jdc_mes, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(jdc_año, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jRadioButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox1)
                    .addComponent(jRadioButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton3.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Save.png"))); // NOI18N
        jButton3.setText("Guardar Prevision Mensual");
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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1248, Short.MAX_VALUE)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_razonsocial2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_razonsocial2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        jMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Upload To FTP.png"))); // NOI18N
        jMenu3.setText("Subir");
        jMenu3.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N

        jMenuItem3.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/sobresalir.png"))); // NOI18N
        jMenuItem3.setText("Archivo Excel");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        jMenuBar1.add(jMenu3);

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/flecha-hacia-abajo.png"))); // NOI18N
        jMenu2.setText("Exportar");
        jMenu2.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed(evt);
            }
        });

        jMenuItem2.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Terms and Conditions.png"))); // NOI18N
        jMenuItem2.setText("Asiento Contable");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

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

                // Estilo para cabeceras
                CellStyle headerStyle = workbook.createCellStyle();
                Font headerFont = workbook.createFont();
                headerFont.setFontName("Bahnschrift");
                headerFont.setFontHeightInPoints((short) 10);
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);
                headerStyle.setAlignment(HorizontalAlignment.CENTER);
                headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                headerStyle.setWrapText(true);

                // Estilo para datos
                CellStyle dataStyle = workbook.createCellStyle();
                Font dataFont = workbook.createFont();
                dataFont.setFontName("Calibri");
                dataFont.setFontHeightInPoints((short) 10);
                dataStyle.setFont(dataFont);
                dataStyle.setAlignment(HorizontalAlignment.LEFT);
                dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);

                // Estilo para datos numéricos
                CellStyle numericStyle = workbook.createCellStyle();
                numericStyle.setFont(dataFont);
                numericStyle.setAlignment(HorizontalAlignment.RIGHT);
                numericStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                numericStyle.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));

                // Crear encabezados
                Row headerRow = sheet.createRow(0);
                headerRow.setHeightInPoints(66);
                for (int i = 0; i < model.getColumnCount(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(model.getColumnName(i));
                    cell.setCellStyle(headerStyle);
                }

                // Crear datos
                for (int i = 0; i < model.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        Cell cell = row.createCell(j);
                        Object value = model.getValueAt(i, j);

                        if (value instanceof Number) {
                            cell.setCellValue(((Number) value).doubleValue());
                            cell.setCellStyle(numericStyle);
                        } else {
                            String cellValue = (value != null) ? value.toString() : "";
                            cell.setCellValue(cellValue);
                            cell.setCellStyle(dataStyle);
                        }
                    }
                }

                // Ajustar el tamaño de las columnas
                for (int i = 0; i < model.getColumnCount(); i++) {
                    // Ajustar al tamaño de los títulos
                    sheet.autoSizeColumn(i);
                    int currentWidth = sheet.getColumnWidth(i);
                    sheet.autoSizeColumn(i);
                    int adjustedWidth = Math.max(sheet.getColumnWidth(i), currentWidth);
                    sheet.setColumnWidth(i, adjustedWidth);
                }

                // Inmovilizar fila 1
                sheet.createFreezePane(0, 1);

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
                "¿Desea guardar la prevision mensual del mes seleccionado?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            guardarPrevisionMensual();
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

    private void jRadioButton1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton1ItemStateChanged
        if (jRadioButton1.isSelected()) {
            jComboBox1.setEnabled(true);
            jdc_mes.setEnabled(false);
            if (jComboBox1.getSelectedItem().equals("CTS Mayo")) {
                jdc_mes.setMonth(3);
            } else if (jComboBox1.getSelectedItem().equals("CTS Noviembre")) {
                jdc_mes.setMonth(9);
            }
        }
    }//GEN-LAST:event_jRadioButton1ItemStateChanged

    private void jRadioButton2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioButton2ItemStateChanged
        if (jRadioButton2.isSelected()) {
            jComboBox1.setEnabled(false);
            jdc_mes.setEnabled(true);
        }
    }//GEN-LAST:event_jRadioButton2ItemStateChanged

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        CargaArchivos ini = new CargaArchivos(this, true);
        ini.toFront();
        ini.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        if (jComboBox1.getSelectedItem().equals("CTS Mayo")) {
            jdc_mes.setMonth(3);
        } else if (jComboBox1.getSelectedItem().equals("CTS Noviembre")) {
            jdc_mes.setMonth(9);
        }
    }//GEN-LAST:event_jComboBox1ItemStateChanged

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        DefaultTableModel model = new DefaultTableModel();
        
        model.addColumn("Cuenta");	
        model.addColumn("Descripcion");	
        model.addColumn("Moneda");	
        model.addColumn("TipoCambio");	
        model.addColumn("DebeSoles");	
        model.addColumn("HaberSoles");	
        model.addColumn("Glosa");	
        model.addColumn("TipoAnexo");	
        model.addColumn("CodigoAnexo");	
        model.addColumn("NroDocumento");	
        model.addColumn("FechaEmisionDoc");	
        model.addColumn("FechaVencimientoDoc");

        
        int año = jdc_año.getYear();
        
        String[] meses = {
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };

        int mesSeleccionado = jdc_mes.getMonth();
        
        String cTS = null, fInicio = null, fFin = null;
        
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, año);

        calendar.set(Calendar.MONTH, mesSeleccionado);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        
        int ultimoDia = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        if (mesSeleccionado >= 4 && mesSeleccionado <= 9) {
            cTS = "CTS " + meses[4].substring(0, 3).toUpperCase() + " " + String.valueOf(año).substring(2, 4) + " - " 
                    + meses[9].substring(0, 3).toUpperCase() + " " + String.valueOf(año).substring(2, 4);
            fInicio = "01/05/" + año;
            fFin = ultimoDia + "/10/" + año;  
        } else if (mesSeleccionado >= 10) {
            cTS = "CTS " + meses[10].substring(0, 3).toUpperCase() + " " + String.valueOf(año).substring(2, 4) + " - " 
                    + meses[3].substring(0, 3).toUpperCase() + " " + String.valueOf(año + 1).substring(2, 4);
            fInicio = "01/11/" + año;
            fFin = ultimoDia + "/04/" + (año + 1); 
        } else if (mesSeleccionado <= 3) {
            cTS = "CTS " + meses[10].substring(0, 3).toUpperCase() + " " + String.valueOf(año - 1).substring(2, 4) + " - " 
                    + meses[3].substring(0, 3).toUpperCase() + " " + String.valueOf(año).substring(2, 4);
            fInicio = "01/11/" + (año - 1);
            fFin = ultimoDia + "/04/" + año;  
        }
        
        Object data[] = new Object[12];
        
        for (int i = 0; i < tb_resultado.getRowCount(); i++) {
            if (Double.parseDouble(tb_resultado.getValueAt(i, 26).toString()) > 0) {
                data[0] = 415110;
                data[1] = "A";
                data[2] = "SOLES";
                data[3] = "3.722";
                data[4] = "-";
                data[5] = tb_resultado.getValueAt(i, 26);
                data[6] = "PROVISION DE CTS MES DE " + meses[mesSeleccionado].toUpperCase() + " " + jdc_año.getYear();
                data[7] = "TRABAJADOR";
                data[8] = tb_resultado.getValueAt(i, 1);
                data[9] = cTS;
                data[10] = fInicio;
                data[11] = fFin;
            }
            
            model.addRow(data);
        }
        
        exportarAExcel(model, meses[mesSeleccionado].substring(0, 3).toUpperCase() + " " + String.valueOf(año).substring(2, 4));
        
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    // Método para exportar los datos de la tabla a Excel
    public static void exportarAExcel(DefaultTableModel model, String mesannio) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar archivo Excel");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos Excel", "xlsx"));
        fileChooser.setSelectedFile(new File("Carga Asiento CTS - " + mesannio + ".xlsx"));

        // Mostrar el diálogo para guardar archivo
        int resultado = fileChooser.showSaveDialog(null);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            String rutaArchivo = archivoSeleccionado.getAbsolutePath();
            if (!rutaArchivo.endsWith(".xlsx")) {
                archivoSeleccionado = new File(rutaArchivo + ".xlsx");
            }

            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Voucher");
                Font fuenteGeneral = workbook.createFont();
                fuenteGeneral.setFontName("Bahnschrift");
                fuenteGeneral.setFontHeightInPoints((short) 10);

                // Estilo general para las celdas
                CellStyle estiloGeneral = workbook.createCellStyle();
                estiloGeneral.setFont(fuenteGeneral);

                // Estilo para el encabezado
                CellStyle headerStyle = workbook.createCellStyle();
                headerStyle.setFont(fuenteGeneral);
                headerStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
                headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                headerStyle.setBorderBottom(BorderStyle.THIN);
                headerStyle.setBorderTop(BorderStyle.THIN);

                // Crear la fila de encabezados
                Row headerRow = sheet.createRow(0);
                for (int col = 0; col < model.getColumnCount(); col++) {
                    Cell cell = headerRow.createCell(col);
                    cell.setCellValue(model.getColumnName(col));
                    cell.setCellStyle(headerStyle);
                }

                // Crear las filas de datos
                for (int row = 0; row < model.getRowCount(); row++) {
                    Row dataRow = sheet.createRow(row + 1);
                    for (int col = 0; col < model.getColumnCount(); col++) {
                        Cell cell = dataRow.createCell(col);
                        Object value = model.getValueAt(row, col);

                        // Formatear y establecer el valor en la celda
                        if (value instanceof Number) {
                            cell.setCellValue(((Number) value).doubleValue());
                            CellStyle numberStyle = workbook.createCellStyle();
                            numberStyle.cloneStyleFrom(estiloGeneral);

                            // Formato con 2 decimales si es decimal
                            DataFormat format = workbook.createDataFormat();
                            numberStyle.setDataFormat(format.getFormat("#.##"));
                            cell.setCellStyle(numberStyle);
                        } else if (value instanceof String && value.toString().matches("\\d{4}-\\d{2}-\\d{2}")) {
                            cell.setCellValue(value.toString());
                            CellStyle dateStyle = workbook.createCellStyle();
                            dateStyle.cloneStyleFrom(estiloGeneral);
                            DataFormat format = workbook.createDataFormat();
                            dateStyle.setDataFormat(format.getFormat("yyyy-mm-dd"));
                            cell.setCellStyle(dateStyle);
                        } else {
                            cell.setCellValue(value.toString());
                            cell.setCellStyle(estiloGeneral);
                        }

                        // Aplicar bordes verticales
                        CellStyle borderStyle = workbook.createCellStyle();
                        borderStyle.cloneStyleFrom(estiloGeneral);
                        borderStyle.setBorderLeft(BorderStyle.THIN);
                        borderStyle.setBorderRight(BorderStyle.THIN);
                        cell.setCellStyle(borderStyle);
                    }
                }

                // Ajustar el tamaño de las columnas automáticamente
                for (int col = 0; col < model.getColumnCount(); col++) {
                    sheet.autoSizeColumn(col);
                }

                // Guardar el archivo Excel
                try (FileOutputStream fileOut = new FileOutputStream(archivoSeleccionado)) {
                    workbook.write(fileOut);
                    System.out.println("Los datos han sido exportados a Excel exitosamente.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    
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
                    UIManager.getLookAndFeelDefaults().put("Table.alternateRowColor", new Color(254, 238, 184));
                    new CTS().setVisible(true);
                }
            });
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(CTS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem ExportarExcel;
    private javax.swing.ButtonGroup buttonGroup1;
    public static final javax.swing.JButton jButton2 = new javax.swing.JButton();
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    public static com.toedter.calendar.JYearChooser jdc_año;
    public static com.toedter.calendar.JMonthChooser jdc_mes;
    private javax.swing.JTable tb_resultado;
    private javax.swing.JLabel txt_razonsocial2;
    // End of variables declaration//GEN-END:variables
}
