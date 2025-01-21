/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import Controlador.ComboBoxTableCellRenderer;
import static Controlador.ExcelExporter.exportToExcel;
import static Controlador.ExcelExporter.exportToExcel2;
import Controlador.FuncionesGlobales;
import static Controlador.FuncionesGlobales.getLastDirectory;
import static Controlador.FuncionesGlobales.saveLastDirectory;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
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
import main.Application;
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
public class Txt_Plame extends javax.swing.JFrame {

    private final Modelo modelo1 = new Modelo();
    private final Modelo modelo2 = new Modelo();
    public static Map<String, String> tipoDocumento, tipoSuspension, ingresosMap, domiciliado, tipoComprobante, tipoRetencion, tipoRegimen;
    
     TableRowSorter trsfiltro, trsfiltro2;

    /**
     * Creates new form LibrosE_SIRE
     */
    public Txt_Plame(String usuario) {
        initComponents();
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

        // Obtener la fecha actual
        LocalDate fechaActual = LocalDate.now();

        // Restar un mes a la fecha actual
        LocalDate fechaMesAnterior = fechaActual.minusMonths(1);

        // Configurar el JDateChooser con la fecha del mes anterior
        mesPlanilla.setMonth(fechaMesAnterior.getMonthValue() - 1);
        jMonthChooser3.setMonth(fechaMesAnterior.getMonthValue() - 1);

        // Configurar el JYearChooser con el año del mes anterior
        añoPlanilla.setYear(fechaMesAnterior.getYear());
        jYearChooser3.setYear(fechaMesAnterior.getYear());
        
        jTextField6.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            if (rb_dni.isSelected()) {
                filterTable(tb_planilla, jTextField6.getText(), 1);
            }else if(rb_trabajador.isSelected()){
                filterTable(tb_planilla, jTextField6.getText(), 2);
            }else if(rb_cargo.isSelected()){
                filterTable(tb_planilla, jTextField6.getText(), 9);
            }else if(rb_centrocosto.isSelected()){
                filterTable(tb_planilla, jTextField6.getText(), 10);
            }else if(rb_regimen.isSelected()){
                filterTable(tb_planilla, jTextField6.getText(), 7);
            }
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            if (rb_dni.isSelected()) {
                filterTable(tb_planilla, jTextField6.getText(), 1);
            }else if(rb_trabajador.isSelected()){
                filterTable(tb_planilla, jTextField6.getText(), 2);
            }else if(rb_cargo.isSelected()){
                filterTable(tb_planilla, jTextField6.getText(), 9);
            }else if(rb_centrocosto.isSelected()){
                filterTable(tb_planilla, jTextField6.getText(), 10);
            }else if(rb_regimen.isSelected()){
                filterTable(tb_planilla, jTextField6.getText(), 7);
            }
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            if (rb_dni.isSelected()) {
                filterTable(tb_planilla, jTextField6.getText(), 1);
            }else if(rb_trabajador.isSelected()){
                filterTable(tb_planilla, jTextField6.getText(), 2);
            }else if(rb_cargo.isSelected()){
                filterTable(tb_planilla, jTextField6.getText(), 9);
            }else if(rb_centrocosto.isSelected()){
                filterTable(tb_planilla, jTextField6.getText(), 10);
            }else if(rb_regimen.isSelected()){
                filterTable(tb_planilla, jTextField6.getText(), 7);
            }
        }
    });
        
        tb_planilla.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    FuncionesGlobales.seleccionarFilas(tb_planilla, tb_planillaProcesada, 1, 1);
                }
            }
        });
        
        jTextField7.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            if (rb_femision.isSelected()) {
                filterTable(TablaInicial2, jTextField7.getText(), 3);
            }else if(rb_serie.isSelected()){
                filterTable(TablaInicial2, jTextField7.getText(), 4);
            }else if(rb_documento.isSelected()){
                filterTable(TablaInicial2, jTextField7.getText(), 5);
            }else if(rb_ruc.isSelected()){
                filterTable(TablaInicial2, jTextField7.getText(), 6);
            }else if(rb_razonsocial.isSelected()){
                filterTable(TablaInicial2, jTextField7.getText(), 7);
            }
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
             if (rb_femision.isSelected()) {
                filterTable(TablaInicial2, jTextField7.getText(), 3);
            }else if(rb_serie.isSelected()){
                filterTable(TablaInicial2, jTextField7.getText(), 4);
            }else if(rb_documento.isSelected()){
                filterTable(TablaInicial2, jTextField7.getText(), 5);
            }else if(rb_ruc.isSelected()){
                filterTable(TablaInicial2, jTextField7.getText(), 6);
            }else if(rb_razonsocial.isSelected()){
                filterTable(TablaInicial2, jTextField7.getText(), 7);
            }
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            if (rb_femision.isSelected()) {
                filterTable(TablaInicial2, jTextField7.getText(), 3);
            }else if(rb_serie.isSelected()){
                filterTable(TablaInicial2, jTextField7.getText(), 4);
            }else if(rb_documento.isSelected()){
                filterTable(TablaInicial2, jTextField7.getText(), 5);
            }else if(rb_ruc.isSelected()){
                filterTable(TablaInicial2, jTextField7.getText(), 6);
            }else if(rb_razonsocial.isSelected()){
                filterTable(TablaInicial2, jTextField7.getText(), 7);
            }
        }
    });
        
        TablaInicial2.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    FuncionesGlobales.seleccionarFilas(TablaInicial2, TablaProcesada2, 6, 1);
                }
            }
        });
        

        ListasPredefinidas();
    }

    public class Modelo extends DefaultTableModel {

        @Override
        public boolean isCellEditable(int row, int column) {
            return true;
        }
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
    
    private void ListasPredefinidas(){
        tipoDocumento = new HashMap<>();
        tipoDocumento.put("[01] - DNI", "01");
        tipoDocumento.put("[04] - CARNÉ EXT.", "04");
        tipoDocumento.put("[06] - RUC", "06");
        tipoDocumento.put("[07] - PASAPORTE", "07");
        tipoDocumento.put("[09] - CARNÉ SOLIC REFUGIO", "09");
        tipoDocumento.put("[11] - PART. NAC.", "11");
        tipoDocumento.put("[22] - C.IDENT.-RREE", "22");
        tipoDocumento.put("[23] - PTP", "23");
        tipoDocumento.put("[24] - DOC.ID.EXTR.", "24");
        tipoDocumento.put("[26] - CPP", "26");
        
        domiciliado = new HashMap<>();
        domiciliado.put("[1] - Domiciliado", "1");
        domiciliado.put("[2] - No Domiciliado.", "2");
        
        tipoComprobante = new HashMap<>();
        tipoComprobante.put("[R] - RECIBO POR HONORARIOS", "R");
        tipoComprobante.put("[N] - NOTA DE CREDITO", "N");
        tipoComprobante.put("[D] - DIETA", "D");
        tipoComprobante.put("[O] - OTRO COMPROBANTE", "O");
        
        tipoRetencion = new HashMap<>();
        tipoRetencion.put("[0] - NO", "0");
        tipoRetencion.put("[1] - SI", "1");
        
        tipoRegimen = new HashMap<>();
        tipoRegimen.put("", "");
        tipoRegimen.put("[1] - Sistema Nacional de Pensiones (ONP)", "1");
        tipoRegimen.put("[2] - Sistema Privado de Pensiones", "2");
        tipoRegimen.put("[3] - Sin retención / No aplica", "3");

        tipoSuspension = new HashMap<>();
        tipoSuspension.put("[01] - S.P. SANCIÓN DISCIPLINARIA", "01");
        tipoSuspension.put("[02] - S.P. EJERCICIO DERECHO HUELGA", "02");
        tipoSuspension.put("[03] - S.P. DETENCIÓN DEL TRABAJADOR", "03");
        tipoSuspension.put("[04] - S.P. INHAB. ADMINIST., JUDICIAL O PENA PRIVATIVA", "04");
        tipoSuspension.put("[05] - S.P. PERMISO, LICENCIA U OTROS SIN GOCE DE HABER", "05");
        tipoSuspension.put("[06] - S.P. CASO FORTUITO O FUERZA MAYOR", "06");
        tipoSuspension.put("[07] - S.P. FALTA NO JUSTIFICADA", "07");
        tipoSuspension.put("[08] - S.P. POR TEMPORADA O INTERMITENTE", "08");
        tipoSuspension.put("[09] - S.P. MATERNIDAD - PRE Y POST NATAL", "09");
        tipoSuspension.put("[10] - S.P. SENTENCIA TERR, NARC,CORRUP Y VIOLAC.", "10");
        tipoSuspension.put("[11] - S.P. IMPOSICIÓN MEDIDA CAUTELAR", "11");
        tipoSuspension.put("[12] - S.P. ENFERM. PADRE, CÓNYUGE O CONVIVIENTE", "12");
        tipoSuspension.put("[20] - S.I. ENFERM/ACCIDENTE (20 PRIMEROS DÍAS)", "20");
        tipoSuspension.put("[21] - S.I. INCAP TEMPORAL (SUBSIDIADO))", "21");
        tipoSuspension.put("[22] - S.I. MATERNIDAD - PRE Y POST NATAL", "22");
        tipoSuspension.put("[23] - S.I. DESCANSO VACACIONAL", "23");
        tipoSuspension.put("[24] - S.I. LIC  DESEMP CARGO CÍVICO Y PARA SMO", "24");
        tipoSuspension.put("[25] - S.I. LIC DESEMPEÑO CARGOS SINDICALES", "25");
        tipoSuspension.put("[26] - S.I. LICENCIA U OTROS MOTIVOS CON GOCE DE HABER", "26");
        tipoSuspension.put("[27] - S.I. DÍAS COMPENS POR HORAS DE SOBRETIEMPO", "27");
        tipoSuspension.put("[28] - S.I. DÍAS LICENCIA POR PATERNIDAD", "28");
        tipoSuspension.put("[29] - S.I. DIAS LICENCIA POR ADOPCIÓN ", "29");
        tipoSuspension.put("[30] - S.I. IMPOSICS.I. IMPOSICIÓN MEDIDA CAUTELAR", "30");
        tipoSuspension.put("[31] - S.I. CITAC. JUDICIAL, MILITAR, POLICIAL O ADMINIST.", "31");
        tipoSuspension.put("[32] - S.I. FALLECIMIENTO PADRES,  HERMANOS, CÓNYUGE O HIJOS", "32");
        tipoSuspension.put("[33] - S.I. REPRESENT. DEL ESTADO EN EVENTOS", "33");
        tipoSuspension.put("[34] - S.I. DESC VACAC LIC POR ASISTE MÉDICA O TERAP REHAB", "34");
        tipoSuspension.put("[35] - S.I. ENFERM. GRAVE O TERMINAL O ACCIDENTE GRAVE DE FAM DIRECTO", "35");

        
        ingresosMap = new HashMap<>();
        ingresosMap.put("[0100] - INGRESOS", "0100");
        ingresosMap.put("[0101] - ALIMENTACIÓN PRINCIPAL EN DINERO", "0101");
        ingresosMap.put("[0102] - ALIMENTACIÓN PRINCIPAL EN ESPECIE", "0102");
        ingresosMap.put("[0103] - COMISIONES O DESTAJO", "0103");
        ingresosMap.put("[0104] - COMISIONES EVENTUALES A TRABAJADORES", "0104");
        ingresosMap.put("[0105] - TRABAJO EN SOBRETIEMPO (HORAS EXTRAS) 25%", "0105");
        ingresosMap.put("[0106] - TRABAJO EN SOBRETIEMPO (HORAS EXTRAS) 35%", "0106");
        ingresosMap.put("[0107] - TRABAJO EN DÍA FERIADO O DÍA DE DESCANSO", "0107");
        ingresosMap.put("[0108] - INCREMENTO EN SNP 3.3 %", "0108");
        ingresosMap.put("[0109] - INCREMENTO POR AFILIACIÓN A AFP 10.23%", "0109");
        ingresosMap.put("[0110] - INCREMENTO POR AFILIACIÓN A AFP 3.00%", "0110");
        ingresosMap.put("[0111] - PREMIOS POR VENTAS", "0111");
        ingresosMap.put("[0112] - PRESTACIONES ALIMENTARIAS - SUMINISTROS DIRECTOS", "0112");
        ingresosMap.put("[0113] - PRESTACIONES ALIMENTARIAS - SUMINISTROS INDIRECTOS", "0113");
        ingresosMap.put("[0114] - VACACIONES TRUNCAS", "0114");
        ingresosMap.put("[0115] - REMUNERACIÓN DÍA DE DESCANSO Y FERIADOS (INCLUIDA LA DEL 1° DE MAYO)", "0115");
        ingresosMap.put("[0116] - REMUNERACIÓN EN ESPECIE", "0116");
        ingresosMap.put("[0117] - COMPENSACIÓN VACACIONAL", "0117");
        ingresosMap.put("[0118] - REMUNERACIÓN VACACIONAL", "0118");
        ingresosMap.put("[0119] - REMUNERACIONES DEVENGADAS", "0119");
        ingresosMap.put("[0120] - SUBVENCIÓN ECONÓMICA MENSUAL (PRACTICANTE SENATI)", "0120");
        ingresosMap.put("[0121] - REMUNERACIÓN O JORNAL BÁSICO", "0121");
        ingresosMap.put("[0122] - REMUNERACIÓN PERMANENTE", "0122");
        ingresosMap.put("[0123] - REMUNERACIÓN DE LOS SOCIOS DE COOPERATIVAS", "0123");
        ingresosMap.put("[0124] - REMUNERACIÓN POR LA HORA DE PERMISO POR LACTANCIA", "0124");
        ingresosMap.put("[0125] - REMUNERACIÓN INTEGRAL ANUAL - CUOTA", "0125");
        ingresosMap.put("[0126] - INGRESOS DEL CONDUCTOR DE LA MICROEMPRESA AFILIADO AL SIS", "0126");
        ingresosMap.put("[0127] - INGRESOS DEL CONDUCTOR DE LA MICROEMPRESA - SEGURO REGULAR", "0127");
        ingresosMap.put("[0128] - RENUNERACIÓN QUE EXCEDE EL VALOR DE MERCADO (DIVIDENDOS)", "0128");
        ingresosMap.put("[0200] - INGRESOS: ASIGNACIONES", "0200");
        ingresosMap.put("[0201] - ASIGNACIÓN FAMILIAR", "0201");
        ingresosMap.put("[0202] - ASIGNACIÓN O BONIFICACIÓN POR EDUCACIÓN (1)", "0202");
        ingresosMap.put("[0203] - ASIGNACIÓN POR CUMPLEAÑOS", "0203");
        ingresosMap.put("[0204] - ASIGNACIÓN POR MATRIMONIO", "0204");
        ingresosMap.put("[0205] - ASIGNACIÓN  POR NACIMIENTO DE HIJOS", "0205");
        ingresosMap.put("[0206] - ASIGNACIÓN POR FALLECIMIENTO DE FAMILIARES", "0206");
        ingresosMap.put("[0207] - ASIGNACIÓN POR OTROS MOTIVOS PERSONALES (2)", "0207");
        ingresosMap.put("[0208] - ASIGNACIÓN POR FESTIVIDAD", "0208");
        ingresosMap.put("[0209] - ASIGNACIÓN PROVISIONAL POR DEMANDA DE TRABAJADOR DESPEDIDO", "0209");
        ingresosMap.put("[0210] - ASIGNACIÓN VACACIONAL", "0210");
        ingresosMap.put("[0211] - ASIGNACIÓN POR ESCOLARIDAD 30 JORNALES BASICOS/AÑO", "0211");
        ingresosMap.put("[0212] - ASIGNACIONES OTORGADAS POR ÚNICA VEZ CON MOTIVO DE CIERTAS CONTINGENCIAS", "0212");
        ingresosMap.put("[0213] - ASIGNACIONES OTORGADAS REGULARMENTE", "0213");
        ingresosMap.put("[0214] - ASIGNACIÓN POR FALLECIMIENTO 1 UIT", "0214");
        ingresosMap.put("[0300] - INGRESOS: BONIFICACIONES", "0300");
        ingresosMap.put("[0301] - BONIFICACIÓN POR 25 Y 30 AÑOS DE SERVICIOS", "0301");
        ingresosMap.put("[0302] - BONIFICACIÓN POR CIERRE DE PLIEGO", "0302");
        ingresosMap.put("[0303] - BONIFICACIÓN POR PRODUCCIÓN, ALTURA, TURNO, ETC.", "0303");
        ingresosMap.put("[0304] - BONIFICACIÓN POR RIESGO DE CAJA", "0304");
        ingresosMap.put("[0305] - BONIFICACIONES POR TIEMPO DE SERVICIOS", "0305");
        ingresosMap.put("[0306] - BONIFICACIONES REGULARES", "0306");
        ingresosMap.put("[0307] - BONIFICACIONES CAFAE (3)", "0307");
        ingresosMap.put("[0308] - COMPENSACIÓN POR TRABAJOS EN DÍAS DE DESCANSO Y EN FERIADOS", "0308");
        ingresosMap.put("[0309] - BONIFICACIÓN POR TURNO NOCTURNO 20% JORNAL BASICO", "0309");
        ingresosMap.put("[0310] - BONIFICACIÓN CONTACTO DIRECTO CON AGUA 20% JORNAL BÁSICO", "0310");
        ingresosMap.put("[0311] - BONIFICACION UNIFICADA DE CONSTRUCCIÓN", "0311");
        ingresosMap.put("[0312] - BONIFICACIÓN EXTRAORDINARIA TEMPORAL – LEY 29351 Y 30334", "0312");
        ingresosMap.put("[0313] - BONIFICACIÓN EXTRAORDINARIA PROPORCIONAL – LEY 29351 Y 30334", "0313");
        ingresosMap.put("[0314] - BONIFICACIÓN ESPECIAL POR TRABAJO AGRARIO LEY 31110 – BETA", "0314");
        ingresosMap.put("[0400] - INGRESOS: GRATIFICACIONES / AGUINALDOS", "0400");
        ingresosMap.put("[0401] - GRATIFICACIONES DE FIESTAS PATRIAS Y NAVIDAD", "0401");
        ingresosMap.put("[0402] - OTRAS GRATIFICACIONES ORDINARIAS", "0402");
        ingresosMap.put("[0403] - GRATIFICACIONES EXTRAORDINARIAS", "0403");
        ingresosMap.put("[0404] - AGUINALDOS DE JULIO Y DICIEMBRE", "0404");
        ingresosMap.put("[0405] - GRATIFICACIONES PROPORCIONAL", "0405");
        ingresosMap.put("[0406] - GRATIFICACIONES DE FIESTAS PATRIAS Y NAVIDAD – LEY 29351 Y 30334", "0406");
        ingresosMap.put("[0407] - GRATIFICACIONES PROPORCIONAL – LEY 29351 Y 30334", "0407");
        ingresosMap.put("[0408] - GRATIFIC FIESTAS PATRIAS Y NAVIDAD TRABAJ PESQ LEY 30334", "0408");
        ingresosMap.put("[0409] - GRATIFICACIONES PROPORCIONAL/TRUNCA TRABAJ PESQUEROS LEY 30334", "0409");
        ingresosMap.put("[0410] - GRATIF FORMA PARTE REMUNER EXCEDE VALOR DE MERCADO (DIVIDENDO)", "0410");
        ingresosMap.put("[0500] - INGRESOS: INDEMNIZACIONES", "0500");
        ingresosMap.put("[0501] - INDEMNIZACIÓN POR DESPIDO INJUSTIFICADO U HOSTILIDAD", "0501");
        ingresosMap.put("[0502] - INDEMNIZACIÓN POR MUERTE O INCAPACIDAD", "0502");
        ingresosMap.put("[0503] - INDEMNIZACIÓN POR RESOLUCIÓN DE CONTRATO SUJETO A MODALIDAD", "0503");
        ingresosMap.put("[0504] - INDEMNIZACIÓN POR VACACIONES NO GOZADAS", "0504");
        ingresosMap.put("[0505] - INDEMNIZACIÓN POR RETENCIÓN INDEBIDA DE CTS ART. 52 D.S Nº 001-97-TR", "0505");
        ingresosMap.put("[0506] - INDEMNIZACIÓN POR NO REINCORPORAR A UN TRABAJADOR CESADO EN UN PROCEDIMIENTO DE CESE COLECTIVO - DS 001-96-TR", "0506");
        ingresosMap.put("[0507] - INDEMNIZACIÓN POR REALIZAR HORAS EXTRAS IMPUESTAS POR EL EMPLEADOR", "0507");
        ingresosMap.put("[0900] - CONCEPTOS VARIOS", "0900");
        ingresosMap.put("[0901] - BIENES DE LA PROPIA EMPRESA OTORGADOS PARA EL CONSUMO DEL TRABAJADOR", "0901");
        ingresosMap.put("[0902] - BONO DE PRODUCTIVIDAD", "0902");
        ingresosMap.put("[0903] - CANASTA DE NAVIDAD O SIMILARES", "0903");
        ingresosMap.put("[0904] - COMPENSACIÓN POR TIEMPO DE SERVICIOS", "0904");
        ingresosMap.put("[0905] - GASTOS DE REPRESENTACIÓN (MOVILIDAD, VESTUARIO, VIÁTICOS Y SIMILARES) - LIBRE DISPONIBILIDAD", "0905");
        ingresosMap.put("[0906] - INCENTIVO POR CESE DEL TRABAJADOR (4)", "0906");
        ingresosMap.put("[0907] - LICENCIA CON GOCE DE HABER", "0907");
        ingresosMap.put("[0908] - MOVILIDAD DE LIBRE DISPOSICIÓN", "0908");
        ingresosMap.put("[0909] - MOVILIDAD SUPEDITADA A ASISTENCIA Y QUE CUBRE SÓLO EL TRASLADO", "0909");
        ingresosMap.put("[0910] - PARTICIPACIÓN EN LAS UTILIDADES - PAGADAS ANTES DE LA DECLARACIÓN ANUAL DEL IMPUESTO A LA RENTA", "0910");
        ingresosMap.put("[0911] - PARTICIPACIÓN EN LAS UTILIDADES - PAGADAS DESPUES DE LA DECLARACIÓN ANUAL DEL IMPUESTO A LA RENTA", "0911");
        ingresosMap.put("[0912] - PENSIONES DE JUBILACIÓN O CESANTÍA, MONTEPÍO O INVALIDEZ", "0912");
        ingresosMap.put("[0913] - RECARGO AL CONSUMO", "0913");
        ingresosMap.put("[0914] - REFRIGERIO QUE NO ES ALIMENTACIÓN PRINCIPAL", "0914");
        ingresosMap.put("[0915] - SUBSIDIOS POR MATERNIDAD", "0915");
        ingresosMap.put("[0916] - SUBSIDIOS DE  INCAPACIDAD POR ENFERMEDAD", "0916");
        ingresosMap.put("[0917] - CONDICIONES DE TRABAJO", "0917");
        ingresosMap.put("[0918] - IMPUESTO A LA RENTA DE QUINTA CATEGORÍA ASUMIDO", "0918");
        ingresosMap.put("[0919] - SISTEMA NACIONAL DE PENSIONES ASUMIDO", "0919");
        ingresosMap.put("[0920] - SISTEMA PRIVADO DE PENSIONES ASUMIDO", "0920");
        ingresosMap.put("[0921] - PENSIONES DE JUBILACIÓN O CESANTÍA, MONTEPÍO O INVALIDEZ PENDIENTES POR LIQUIDAR", "0921");
        ingresosMap.put("[0922] - SUMAS O BIENES QUE NO SON DE LIBRE DISPOSICIÓN", "0922");
        ingresosMap.put("[0923] - INGRESOS DE CUARTA CATEGORIA QUE SON CONSIDERADOS DE QUNTA CATEGORIA", "0923");
        ingresosMap.put("[0924] - INGRESOS CUARTA-QUINTA SIN RELACIÓN DE DEPENDENCIA  (9)", "0924");
        ingresosMap.put("[0925] - INGRESO DEL PESCADOR Y PROCESADOR ARTESANAL INDEPENDIENTE - BASE DE CALCULO APORTE ESSALUD - LEY 27177", "0925");
        ingresosMap.put("[0926] - TRANSFERENCIA DIRECTA AL EXPESCADOR - LEY 30003", "0926");
        ingresosMap.put("[0927] - BONIFICACIÓN PRIMA TEXTIL", "0927");
        ingresosMap.put("[0928] - DEVOLUCIÓN RETENCIÓN EXCESO DE IMP. RENTA 5TA CAT.", "0928");
        ingresosMap.put("[0929] - OTRAS ASIGN, BONIF QUE FORMAN PARTE REMUNER Y EXCED VALOR DE MERCADO (DIVIDENDOS)", "0929");
        ingresosMap.put("[0930] - REMUNERACIÓN QUE EXCEDE EL VALOR DE MERCADO (DIVID) DE PERÍODOS ANTERIORES", "0930");
        ingresosMap.put("[1000] - OTROS CONCEPTOS", "1000");
        ingresosMap.put("[1001] - OTROS CONCEPTOS 1", "1001");
        ingresosMap.put("[1002] - OTROS CONCEPTOS 2", "1002");
        ingresosMap.put("[1003] - OTROS CONCEPTOS 3", "1003");
        ingresosMap.put("[1004] - OTROS CONCEPTOS 4", "1004");
        ingresosMap.put("[1005] - OTROS CONCEPTOS 5", "1005");
        ingresosMap.put("[1006] - OTROS CONCEPTOS 6", "1006");
        ingresosMap.put("[1007] - OTROS CONCEPTOS 7", "1007");
        ingresosMap.put("[1008] - OTROS CONCEPTOS 8", "1008");
        ingresosMap.put("[1009] - OTROS CONCEPTOS 9", "1009");
        ingresosMap.put("[1010] - OTROS CONCEPTOS 10", "1010");
        ingresosMap.put("[1011] - OTROS CONCEPTOS 11", "1011");
        ingresosMap.put("[1012] - OTROS CONCEPTOS 12", "1012");
        ingresosMap.put("[1013] - OTROS CONCEPTOS 13", "1013");
        ingresosMap.put("[1014] - OTROS CONCEPTOS 14", "1014");
        ingresosMap.put("[1015] - OTROS CONCEPTOS 15", "1015");
        ingresosMap.put("[1016] - OTROS CONCEPTOS 16", "1016");
        ingresosMap.put("[1017] - OTROS CONCEPTOS 17", "1017");
        ingresosMap.put("[1018] - OTROS CONCEPTOS 18", "1018");
        ingresosMap.put("[1019] - OTROS CONCEPTOS 19", "1019");
        ingresosMap.put("[1020] - OTROS CONCEPTOS 20", "1020");
        ingresosMap.put("[2000] - RÉGIMEN LABORAL PÚBLICO", "2000");
        ingresosMap.put("[2001] - REMUNERACIÓN", "2001");
        ingresosMap.put("[2002] - SALARIO OBREROS", "2002");
        ingresosMap.put("[2003] - BONIFICACIÓN PERSONAL - QUINQUENIO", "2003");
        ingresosMap.put("[2004] - BONIFICACIÓN FAMILIAR", "2004");
        ingresosMap.put("[2005] - BONIFICACIÓN DIFERENCIAL", "2005");
        ingresosMap.put("[2006] - AGUINALDOS", "2006");
        ingresosMap.put("[2007] - REMUNERACIÓN VACACIONAL", "2007");
        ingresosMap.put("[2008] - ASIGNACIÓN POR AÑOS DE SERVICIOS", "2008");
        ingresosMap.put("[2009] - BONIFICACIÓN POR ESCOLARIDAD", "2009");
        ingresosMap.put("[2010] - COMPENSACIÓN POR TIEMPO DE SERVICIOS", "2010");
        ingresosMap.put("[2011] - ALIMENTACIÓN - CAFAE (3)", "2011");
        ingresosMap.put("[2012] - MOVILIDAD - CAFAE (3)", "2012");
        ingresosMap.put("[2013] - RACIONAMIENTO - CAFAE", "2013");
        ingresosMap.put("[2014] - INCENTIVOS LABORALES - CAFAE (3)", "2014");
        ingresosMap.put("[2015] - BONO JURISDICCIONAL/ BONO FISCAL", "2015");
        ingresosMap.put("[2016] - GASTOS OPERATIVOS DE MAGISTRADOS Y FISCALES ", "2016");
        ingresosMap.put("[2017] - ASIGNACIÓN POR SERVICIO EXTERIOR", "2017");
        ingresosMap.put("[2018] - BONIFICACIÓN CONSULAR", "2018");
        ingresosMap.put("[2019] - ASIGNACIÓN ESPECIAL PARA DOCENTES UNIVERSITARIOS ", "2019");
        ingresosMap.put("[2020] - HOMOLOGACIÓN  DE DOCENTES UNIVERSITARIOS", "2020");
        ingresosMap.put("[2021] - ASIGNACIÓN ESPECIAL POR LABOR PEDAGÓGICA EFECTIVA", "2021");
        ingresosMap.put("[2022] - ASIGNACIÓN EXTRAORDINARIA POR TRABAJO ASISTENCIAL", "2022");
        ingresosMap.put("[2023] - SERVICIOS EXTRAORDINARIOS PNP", "2023");
        ingresosMap.put("[2024] - ASIGNACIONES FFAA Y PNP", "2024");
        ingresosMap.put("[2025] - COMBUSTIBLE FFAA Y PNP ", "2025");
        ingresosMap.put("[2026] - OTROS INGRESOS REMUNERATIVOS PERSONAL ADMINISTRATIVO", "2026");
        ingresosMap.put("[2027] - OTROS INGRESOS NO REMUNERATIVOS PERSONAL ADMINISTRATIVO", "2027");
        ingresosMap.put("[2028] - OTROS INGRESOS REMUNERATIVOS MAGISTRADOS", "2028");
        ingresosMap.put("[2029] - OTROS INGRESOS NO REMUNERATIVOS MAGISTRADOS", "2029");
        ingresosMap.put("[2030] - OTROS INGRESOS REMUNERATIVOS DOCENTES UNIVERSITARIOS", "2030");
        ingresosMap.put("[2031] - OTROS INGRESOS NO REMUNERATIVOS DOCENTES UNIVERSITARIOS", "2031");
        ingresosMap.put("[2032] - OTROS INGRESOS REMUNERATIVOS PROFESORADO", "2032");
        ingresosMap.put("[2033] - OTROS INGRESOS NO REMUNERATIVOS PROFESORADO", "2033");
        ingresosMap.put("[2034] - OTROS INGRESOS REMUNERATIVOS PROFESIONALES DE LA SALUD", "2034");
        ingresosMap.put("[2035] - OTROS INGRESOS NO REMUNERATIVOS PROFESIONALES DE LA SALUD", "2035");
        ingresosMap.put("[2036] - OTROS INGRESOS REMUNERATIVOS FFAA Y PNP", "2036");
        ingresosMap.put("[2037] - OTROS INGRESOS NO REMUNERATIVOS FFAA Y PNP", "2037");
        ingresosMap.put("[2038] - ASIGNACIÓN ESPECIAL - D.U. 126-2001", "2038");
        ingresosMap.put("[2039] - INGRESOS D.LEG. 1057 - CAS", "2039");
        ingresosMap.put("[2040] - REMUNERACIÓN POR DÍAS CON RELACIÓN LABORAL EN EL PERÍODO  DE UN CAS", "2040");
        ingresosMap.put("[2041] - AGUINALDOS DE JULIO Y DICIEMBRE – LEY 29351", "2041");
        ingresosMap.put("[2042] - BONIFICACIÓN ESPECIAL A SERV PUB - DU 037-94 (5)", "2042");
        ingresosMap.put("[2043] - REMUNERACIÓN VACACIONAL-D.LEG. 1057-CAS", "2043");
        ingresosMap.put("[2044] - AGUNALDOS DE JULIO Y DICIEMBRE - D.LEG. 1057 - CAS(6 y 7)", "2044");
        ingresosMap.put("[2045] - LICENCIA CON GOCE DE HABER - D.LEG. 1057 - CAS (6)", "2045");
        ingresosMap.put("[2046] - VALORIZACIÓN PRINCIPAL - LEY 30057", "2046");
        ingresosMap.put("[2047] - VALORIZACIÓN AJUSTADA - LEY 30057", "2047");
        ingresosMap.put("[2048] - VALORIZACIÓN PRIORIZADA - LEY 30057", "2048");
        ingresosMap.put("[2049] - ENTREGA ECONÓMICA POR VACACIONES - LEY 30057", "2049");
        ingresosMap.put("[2050] - ENTREGA ECONÓMICA POR VACACIONES TRUNCAS - LEY 30057", "2050");
        ingresosMap.put("[2051] - AGUINALDOS POR FIESTAS PATRIAS Y NAVIDAD- LEY 30057 Y LEY  30334", "2051");
        ingresosMap.put("[2052] - AGUINALDOS TRUNCOS POR FIESTAS PATRIAS Y NAVIDAD- LEY 30057 Y LEY  30334", "2052");
        ingresosMap.put("[2053] - AGUINALDOS PROPORCIONALES POR FIESTAS PATRIAS Y NAVIDAD- LEY 30057 Y LEY  30334", "2053");
        ingresosMap.put("[2054] -  INDEMNIZ POR DESTITUCIÓN NULA O INJUSTIFICADA - LEY 30057", "2054");
        ingresosMap.put("[2055] - REMUNERACIÓN INTEGRAL MENSUAL - LEY 29944", "2055");
        ingresosMap.put("[2056] - ASIGNACIÓN POR DIRECTOR DE UNIDAD DE GESTIÓN EDUCATIVA LOCAL - LEY 29944", "2056");
        ingresosMap.put("[2057] - ASIGNACIÓN POR DIRECTOR DE GESTIÓN PEDAGÓGICA - LEY 29944", "2057");
        ingresosMap.put("[2058] - ASIGNACIÓN POR ESPECIALISTA EN EDUCACIÓN - LEY  29944", "2058");
        ingresosMap.put("[2059] - ASIGNACIÓN POR ESPECIALISTA EN INNOVACIÓN E INVESTIGACIÓN - LEY 29944", "2059");
        ingresosMap.put("[2060] - ASIGNACIÓN POR DIRECTOR DE INSTITUCIÓN EDUCATIVA - LEY 29944", "2060");
        ingresosMap.put("[2061] - ASIGNACIÓN POR SUB DIRECTOR DE INSTITUCIÓN EDUCATIVA - LEY 29944", "2061");
        ingresosMap.put("[2062] - ASIGNACIÓN POR CARGOS JERÁRQUICOS DE INSTITUCIÓN EDUCATIVA - LEY  29944", "2062");
        ingresosMap.put("[2063] - ASIGNACIÓN POR SERVICIO EN INSTITUCIÓN  UNIDOCENTE, MULTIGRADO O BILINGÚE - LEY 29944", "2063");
        ingresosMap.put("[2064] - ASIGNACIÓN POR TRABAJO EN ÁMBITO RURAL O DE FRONTERA  -  LEY  29944", "2064");
        ingresosMap.put("[2065] - ASIGNACIÓN POR ASESORÍA. FORMACIÓN, CAPACITACIÓN Y/O ACOMPAÑAMIENTO - LEY 29944", "2065");
        ingresosMap.put("[2066] - ASIGNACIÓN POR TIEMPO DE SERVICIOS - LEY 29944", "2066");
        ingresosMap.put("[2067] - ASIGNACIÓN ESPECIAL A PROFESORES DEL VRAEM - Ley 29944", "2067");
        ingresosMap.put("[2068] - INCENTIVO POR EXCELENCIA PROFESIONAL Y DESEMPEÑO DESTACADO - LEY 29944", "2068");
        ingresosMap.put("[2069] - INCENTIVO POR ESTUDIOS DE POSTGRADO - LEY  29944", "2069");
        ingresosMap.put("[2070] - ASIGNACIÓN DIFERENCIADA POR MAESTRÍA Y DOCTORADO - LEY 29944", "2070");
        ingresosMap.put("[2071] - SUBSIDIO POR LUTO Y SEPELIO - LEY 29944", "2071");
        ingresosMap.put("[2072] - COMPENSACIÓN EXTRAORDINARIA TRANSITORIA – LEY 29944", "2072");
        ingresosMap.put("[2073] - DIFERENCIAL SUBSIDIO PAGAGO POR LA ENTIDAD PÚBLICA - D.LEG. 1057-CAS", "2073");
        ingresosMap.put("[2074] - VALORIZACIÓN PRINCIPAL - DEC.LEGISL 1153", "2074");
        ingresosMap.put("[2075] - BONIF POR PUESTO DE RESPONS JEFAT DE DPTO O SERVICIO -DL 1153", "2075");
        ingresosMap.put("[2076] - BONIF POR PUESTO DE RESPONS JEFAT EN ESTABLEC DE SALUD I3, I4, MICRORREDES O REDES -DL 1153", "2076");
        ingresosMap.put("[2077] - BONIF POR PUESTO ESPEC O DE DEDIC EXCLUS EN SERV DE SALUD PÚBLICA - DL 1153", "2077");
        ingresosMap.put("[2078] - BONIF POR PUESTO ESPECIFICO - DL 1153", "2078");
        ingresosMap.put("[2079] - BONIF POR PUESTO EN SERV DE SALUD PUBLICA-DL 1153", "2079");
        ingresosMap.put("[2080] - DESCANSO VACACIONAL -- DL 1153", "2080");
        ingresosMap.put("[2081] - DESCANSO VACACIONAL PROPORC -- DL 1153", "2081");
        ingresosMap.put("[2082] - VAL PRIORIZ ZONA ALEJADA DE FRONTERA - DL 1153", "2082");
        ingresosMap.put("[2083] - VAL PRIORIZ ZONA DE EMERGENCIA  - DL 1153", "2083");
        ingresosMap.put("[2084] - VAL PRIORIZ ATENCIÓN PRIMARIA DE SALUD  - DL 1153", "2084");
        ingresosMap.put("[2085] - VAL PRIORIZ ATENCIÓN ESPECIALIZADA  - DL 1153", "2085");
        ingresosMap.put("[2086] - VAL PRIORIZ ATENCIÓN EN SERV CRÍTICOS  - DL 1153", "2086");
        ingresosMap.put("[2087] - VAL PRIORIZ ATENCIÓN ESPEC DE SOPORTE  - DL 1153", "2087");
        ingresosMap.put("[2088] - SERVICIOS DE GUARDIA", "2088");
        ingresosMap.put("[2089] - SERVV COMPLEMENTARIO EN SALUD - DL 1153", "2089");
        ingresosMap.put("[2090] - ENTREGA ECON POR 25 AÑOS DE SERVICIO - DL 1153", "2090");
        ingresosMap.put("[2091] - ENTREGA ECON POR 30  AÑOS DE SERVICIO - DL 1153", "2091");
        ingresosMap.put("[2092] - ENTREGA ECON POR LUTO Y SEPELIO - DL 1153", "2092");
        ingresosMap.put("[2093] - ASIGN POR CUMPLIM DE METAS INST, INDIC DESEMP - DL 1153", "2093");
        ingresosMap.put("[2094] - GASTOS OPERATIVOS DE MAGISTRADOS Y FISCALES  NO CONSTITUYE CONDICIÓN DE TRABAJO", "2094");
        ingresosMap.put("[2095] - MONTO ÚNICO CONSOLIDADO (MUC) DU 038-2019", "2095");
        ingresosMap.put("[2096] - ASIGNACIÓN POR CUMPLIR 25 AÑOS DE SERV DU 038-2019", "2096");
        ingresosMap.put("[2097] - ASIGNACIÓN POR CUMPLIR 30 AÑOS DE SERV DU 038-2019", "2097");
        ingresosMap.put("[2098] - ASIGNACIÓN POR LUTO Y SEPELIO DU 038-2019", "2098");
        ingresosMap.put("[2099] - BET FIJO IMPONIBLE (DL 276)", "2099");
        ingresosMap.put("[2100] - BET FIJO NO IMPONIBLE (DL 276)", "2100");
        ingresosMap.put("[2101] - BET VARIABLE BONIFICACIÓN FAMILIAR (DL 276)", "2101");
        ingresosMap.put("[2102] - BET VARIABLE BONIFICACIÓN DIFERENCIAL PERMANENTE (DL 276)", "2102");
        ingresosMap.put("[2103] - BET VARIABLE BONIFICACIÓN DIFERENCIAL TEMPORAL (DL 276)", "2103");
        ingresosMap.put("[2104] - BET VARIABLE CENTROS DE PRODUC Y SIMILARES (DL 276)", "2104");
        ingresosMap.put("[2105] - BET VARIABLE OTROS DETERMIN POR LA DGGFRH (DL 276)", "2105");
        ingresosMap.put("[2106] - COMPENSACIÓN POR VACACIONES TRUNCAS DU 38-2019 (DL 276)", "2106");
        ingresosMap.put("[2107] - SUBSIDIO POR GASTOS DE SEPELIO (DL 276)", "2107");
        ingresosMap.put("[2108] - MUC Y BET AFECTO A CARGAS SOCIALES NOTIF EN RD", "2108");
        ingresosMap.put("[2109] - MUC Y BET NO AFECTO A CARGAS SOCIALES", "2109");
        ingresosMap.put("[2110] - ASIGNACIÓN POR DESEMPEÑO EN PUESTOS DE GESTIÓN PEDAGÓGICA", "2110");
        ingresosMap.put("[2111] - ASIG POR DESEMPEÑO EN PUESTO DE DIRECTOR GRAL (EESP)", "2111");
        ingresosMap.put("[2112] - BONIF POR INSTIT EDUC UNIDOCENTE, MULTIGRADO O BILINGÜE", "2112");
        ingresosMap.put("[2113] - BONIF. POR PRESTAR SERVICIO EN ÁMBITO RURAL O FRONTERA", "2113");
        ingresosMap.put("[2114] - BONIFICACIÓN POR PALMAS MAGISTERIALES", "2114");
        ingresosMap.put("[2115] - PREMIO ANUAL POR RESULTADOS DESTACADOS", "2115");
        ingresosMap.put("[2116] - BONO DE INCENTIVO AL DESEMPEÑO ESCOLAR", "2116");
        ingresosMap.put("[0600] - APORTACIONES DEL TRABAJADOR / PENSIONISTA", "0600");
        ingresosMap.put("[0601] - SISTEMA PRIVADO DE PENSIONES - COMISIÓN PORCENTUAL", "0601");
        ingresosMap.put("[0602] - CONAFOVICER", "0602");
        ingresosMap.put("[0603] - CONTRIBUCIÓN SOLIDARIA PARA LA ASISTENCIA PREVISIONAL", "0603");
        ingresosMap.put("[0604] - ESSALUD +VIDA", "0604");
        ingresosMap.put("[0605] - RENTA QUINTA CATEGORÍA RETENCIONES", "0605");
        ingresosMap.put("[0606] - SISTEMA PRIVADO DE PENSIONES - PRIMA DE SEGURO", "0606");
        ingresosMap.put("[0607] - SISTEMA NACIONAL DE PENSIONES - D.L.19990", "0607");
        ingresosMap.put("[0608] - SISTEMA PRIVADO DE PENSIONES - APORTACIÓN OBLIGATORIA", "0608");
        ingresosMap.put("[0609] - SISTEMA PRIVADO DE PENSIONES - APORTACIÓN VOLUNTARIA", "0609");
        ingresosMap.put("[0610] - ESSALUD - SEGURO REGULAR - PENSIONISTA", "0610");
        ingresosMap.put("[0611] - OTROS APORTACIONES DEL TRABAJADOR / PENSIONISTA", "0611");
        ingresosMap.put("[0612] - SISTEMA NACIONAL DE PENSIONES - ASEGURA TU PENSIÓN", "0612");
        ingresosMap.put("[0613] - RÉGIMEN PENSIONARIO - D.L. 20530", "0613");
        ingresosMap.put("[0614] - RÉGIMEN PENSIONARIO DEL SERV. DIPLOMÁTICO", "0614");
        ingresosMap.put("[0615] - RÉGIMEN DE PENSIONES MILITAR-POLICIAL", "0615");
        ingresosMap.put("[0616] - APORTE MENSUAL AL FCJMMS  -  LEY 29741 (1)", "0616");
        ingresosMap.put("[0617] - CUOTA- FRACCIONAMIENTO- FONDO CJMMS - LEY 29741  (2)", "0617");
        ingresosMap.put("[0618] - RENTA CUARTA CATEGORÍA RETENCIONES –CAS", "0618");
        ingresosMap.put("[0619] - SNP – TRABAJADOR INDEPENDIENTE - LEY 29903", "0619");
        ingresosMap.put("[0620] - REP - TRAB. PESQUERO LEY 30003 – RETENCIÓN", "0620");
        ingresosMap.put("[0621] - RENTA QUINTA CATEGORÍA REGUL EJERC ANTERIOR", "0621");
        ingresosMap.put("[0700] - DESCUENTOS AL TRABAJADOR", "0700");
        ingresosMap.put("[0701] - ADELANTO", "0701");
        ingresosMap.put("[0702] - CUOTA SINDICAL", "0702");
        ingresosMap.put("[0703] - DESCUENTO AUTORIZADO U ORDENADO POR MANDATO JUDICIAL", "0703");
        ingresosMap.put("[0704] - TARDANZAS", "0704");
        ingresosMap.put("[0705] - INASISTENCIAS", "0705");
        ingresosMap.put("[0706] - OTROS DESCUENTOS NO DEDUCIBLES DE LA BASE IMPONIBLE ", "0706");
        ingresosMap.put("[0707] - OTROS DESCUENTOS DEDUCIBLES DE LA BASE IMPONIBLE ", "0707");
        ingresosMap.put("[0800] - APORTACIONES DE CARGO DEL EMPLEADOR", "0800");
        ingresosMap.put("[0801] - SISTEMA PRIVADO DE PENSIONES - APORTACIÓN VOLUNTARIA", "0801");
        ingresosMap.put("[0802] - FONDO DE DERECHOS SOCIALES DEL ARTISTA", "0802");
        ingresosMap.put("[0803] - PÓLIZA DE SEGURO - D. LEG. 688", "0803");
        ingresosMap.put("[0804] - ESSALUD (SEGURO REGULAR, CBBSP, AGRARIO/ACUICULTOR) - TRABAJADOR", "0804");
        ingresosMap.put("[0805] - PENSIONES – SEGURO COMPLEMENTARIO DE TRABAJO DE RIESGO", "0805");
        ingresosMap.put("[0806] - ESSALUD – SEGURO COMPLEMENTARIO DE TRABAJO DE RIESGO", "0806");
        ingresosMap.put("[0807] - SENATI", "0807");
        ingresosMap.put("[0808] - IMPUESTO EXTRAORDINARIO DE SOLIDARIDAD", "0808");
        ingresosMap.put("[0809] - OTRAS APORTACIONES DE CARGO DEL EMPLEADOR", "0809");
        ingresosMap.put("[0810] - EPS - SEGURO COMPLEMENTARIO DE TRABAJO DE RIESGO", "0810");
        ingresosMap.put("[0811] - SEGURO INTEGRAL DE SALUD - SIS", "0811");
        ingresosMap.put("[0812] - REP – TRAB. PESQUERO LEY 30003 – EMPLEADOR", "0812");
        ingresosMap.put("[0813] - ONP - SEGURO COMPLEMENTARIO DE TRABAJO DE RIESGO", "0813");
        ingresosMap.put("[0814] - COMPAÑÍA SEGURO - SEGURO COMPLEMENTARIO DE TRABAJO DE RIESGO", "0814");
        ingresosMap.put("[0815] - ESSALUD +VIDA (3)", "0815");
    }

    private void llenar_tabla_JOR() {
        String[] columnNames = new String[]{
            "Tipo de Documento",
            "N° de Documento",
            "N° de Horas Ordinarias Trabajadas",
            "N° de Minutos Ordinarios Trabajados",
            "N° de Horas en Sobretiempo Trabajadas",
            "N° de Minutos en Sobretiempo Trabajados"
        };

        modelo1.setColumnIdentifiers(columnNames);

        JTableHeader header = tb_planillaProcesada.getTableHeader();
        header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 40)); // Establece la altura deseada (por ejemplo, 30 píxeles)

        // Cambiar el color del encabezado
        header.setBackground(new java.awt.Color(255, 217, 102)); // Melon (RGB: 255, 140, 105)
        tb_planillaProcesada.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

        TableColumnModel columnModel = tb_planillaProcesada.getColumnModel();
        for (int col = 0; col < tb_planillaProcesada.getColumnCount(); col++) {
            TableColumn tableColumn = columnModel.getColumn(col);
            Component comp = header.getDefaultRenderer().getTableCellRendererComponent(tb_planillaProcesada, tableColumn.getHeaderValue(), false, false, 0, col);
            int width = comp.getPreferredSize().width + 25;
            tableColumn.setPreferredWidth(width);
        }

        tb_planillaProcesada.getColumnModel().getColumn(0).setCellEditor(new ComboBoxTableCellRenderer(tipoDocumento));
    }
    
    private void llenar_tabla_PS4() {
        String[] columnNames = new String[]{
            "Tipo de Documento",
            "N° de Documento",
            "Apellido Paterno",
            "Apellido Materno",
            "Nombres",
            "Domiciliado",
            "Convenio para evitar doble tributacion"
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

        TablaProcesada2.getColumnModel().getColumn(0).setCellEditor(new ComboBoxTableCellRenderer(tipoDocumento));
        TablaProcesada2.getColumnModel().getColumn(5).setCellEditor(new ComboBoxTableCellRenderer(domiciliado));
    }
    
    private void llenar_tabla_4TA() {
        String[] columnNames = new String[]{
            "Tipo de Documento del Prestador",
            "N° de RUC",
            "Tipo de Comprobante emitido",
            "Serie de Comprobante emitido",
            "Nº de Comprobante emitido",
            "Monto Total del Servicio",
            "Fecha de Emision",
            "Fecha de Pago",
            "Indicador de Retenciòn de Cuarta Cat.",
            "Indicador de Retención a Régimen Pensionario (obligatorio a partir del per. 08/2013).",
            "Importe del aporte al Régimen Pensionario"
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

        TablaProcesada2.getColumnModel().getColumn(0).setCellEditor(new ComboBoxTableCellRenderer(tipoDocumento));
        TablaProcesada2.getColumnModel().getColumn(2).setCellEditor(new ComboBoxTableCellRenderer(tipoComprobante));
        TablaProcesada2.getColumnModel().getColumn(8).setCellEditor(new ComboBoxTableCellRenderer(tipoRetencion));
        TablaProcesada2.getColumnModel().getColumn(9).setCellEditor(new ComboBoxTableCellRenderer(tipoRegimen));
    }

    private void llenar_tabla_REM() {

        String[] columnNames = new String[]{
            "Tipo de documento del trabajador",
            "Número de documento del trabajador",
            "Código de concepto remunerativo y/o no remunerativo",
            "Monto devengado",
            "Monto pagado/descontado"
        };

        modelo1.setColumnIdentifiers(columnNames);

        JTableHeader header = tb_planillaProcesada.getTableHeader();
        header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 40)); // Establece la altura deseada (por ejemplo, 30 píxeles)

        // Cambiar el color del encabezado
        header.setBackground(new java.awt.Color(255, 217, 102)); // Melon (RGB: 255, 140, 105)
        tb_planillaProcesada.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

        TableColumnModel columnModel = tb_planillaProcesada.getColumnModel();
        for (int col = 0; col < tb_planillaProcesada.getColumnCount(); col++) {
            TableColumn tableColumn = columnModel.getColumn(col);
            Component comp = header.getDefaultRenderer().getTableCellRendererComponent(tb_planillaProcesada, tableColumn.getHeaderValue(), false, false, 0, col);
            int width = comp.getPreferredSize().width + 25;
            tableColumn.setPreferredWidth(width);
        }

        tb_planillaProcesada.getColumnModel().getColumn(0).setCellEditor(new ComboBoxTableCellRenderer(tipoDocumento));
        tb_planillaProcesada.getColumnModel().getColumn(2).setCellEditor(new ComboBoxTableCellRenderer(ingresosMap));
    }

    private void llenar_tabla_SNL() {
        String[] columnNames = new String[]{
            "Tipo de documento del trabajador",
            "Número de documento del trabajador",
            "Tipo de suspensión de la relación laboral",
            "Número de días de suspensión de labores"
        };

        modelo1.setColumnIdentifiers(columnNames);

        JTableHeader header = tb_planillaProcesada.getTableHeader();
        header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 40)); // Establece la altura deseada (por ejemplo, 30 píxeles)

        // Cambiar el color del encabezado
        header.setBackground(new java.awt.Color(255, 217, 102)); // Melon (RGB: 255, 140, 105)
        tb_planillaProcesada.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

        TableColumnModel columnModel = tb_planillaProcesada.getColumnModel();
        for (int col = 0; col < tb_planillaProcesada.getColumnCount(); col++) {
            TableColumn tableColumn = columnModel.getColumn(col);
            Component comp = header.getDefaultRenderer().getTableCellRendererComponent(tb_planillaProcesada, tableColumn.getHeaderValue(), false, false, 0, col);
            int width = comp.getPreferredSize().width + 25;
            tableColumn.setPreferredWidth(width);
        }

        tb_planillaProcesada.getColumnModel().getColumn(0).setCellEditor(new ComboBoxTableCellRenderer(tipoDocumento));
        tb_planillaProcesada.getColumnModel().getColumn(2).setCellEditor(new ComboBoxTableCellRenderer(tipoSuspension));

    }

    private DefaultTableModel readExcel(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0); // Assuming the first sheet

        // Get headers
        Row headerRow = sheet.getRow(0);
        String[] headers = new String[headerRow.getLastCellNum()];
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            headers[i] = cell != null ? cell.getStringCellValue() : "";
        }

        // Get data
        DefaultTableModel model = new DefaultTableModel(headers, 0);
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Object[] rowData = new Object[headers.length];
                for (int j = 0; j < headers.length; j++) {
                    Cell cell = row.getCell(j);
                    if (cell != null) {
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
                                rowData[j] = null;
                        }
                    } else {
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

    public static String buscarDescripcionPorNumero(Map<String, String> tipoSuspension, String numeroBuscado) {
        for (Map.Entry<String, String> entry : tipoSuspension.entrySet()) {
            if (entry.getValue().equals(numeroBuscado)) {
                return entry.getKey();
            }
        }
        return null; // Devolver null si no se encuentra ninguna descripción asociada al número
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

    public static String sumarTiempos(String[] tiempos) {
        // Sumar horas y minutos
        int totalHoras = 0;
        int totalMinutos = 0;
        for (String tiempo : tiempos) {
            String[] partes = tiempo.split(":");
            int horas = Integer.parseInt(partes[0]);
            int minutos = Integer.parseInt(partes[1]);
            totalHoras += horas;
            totalMinutos += minutos;
        }

        // Ajustar horas y minutos
        totalHoras += totalMinutos / 60; // Añadir horas adicionales
        totalMinutos %= 60; // Quitar horas adicionales y actualizar minutos

        // Formatear el resultado
        return String.format("%d:%01d", totalHoras, totalMinutos); 
    }
    
  public static String restarTiempos(String[] tiempos) {
    // Convertir el primer tiempo a horas y minutos
    String[] primeraParte = tiempos[0].split(":");
    int totalHoras = Integer.parseInt(primeraParte[0]);
    int totalMinutos = Integer.parseInt(primeraParte[1]);

    // Restar los tiempos restantes
    for (int i = 1; i < tiempos.length; i++) {
        String[] partes = tiempos[i].split(":");
        int horas = Integer.parseInt(partes[0]);
        int minutos = Integer.parseInt(partes[1]);
        totalHoras -= horas;
        totalMinutos -= minutos;
    }

    // Ajustar los minutos negativos
    while (totalMinutos < 0) {
        totalMinutos += 60;
        totalHoras--;
    }

    // Asegurarse de que las horas estén en el rango correcto (0-23)
    while (totalHoras < 0) {
        totalHoras += 24;
    }
    
    // Formatear el resultado
    return String.format("%d:%01d", totalHoras, totalMinutos); 
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
        buttonGroup2 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tb_planillaProcesada = new javax.swing.JTable();
        jSeparator2 = new javax.swing.JSeparator();
        btn_exportartxt = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        añoPlanilla = new com.toedter.calendar.JYearChooser();
        jLabel5 = new javax.swing.JLabel();
        mesPlanilla = new com.toedter.calendar.JMonthChooser();
        txt_rutaplanilla = new javax.swing.JTextField();
        btn_cargar = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        rucPlanilla = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        razonSocialPlanilla = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        monedaPlanilla = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_planilla = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        btn_jor = new javax.swing.JButton();
        btn_rem = new javax.swing.JButton();
        btn_snl = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        rb_dni = new javax.swing.JRadioButton();
        rb_trabajador = new javax.swing.JRadioButton();
        rb_cargo = new javax.swing.JRadioButton();
        rb_centrocosto = new javax.swing.JRadioButton();
        jTextField6 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        rb_regimen = new javax.swing.JRadioButton();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        TablaProcesada2 = new javax.swing.JTable();
        jSeparator4 = new javax.swing.JSeparator();
        btn_complementar1 = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jYearChooser3 = new com.toedter.calendar.JYearChooser();
        jLabel14 = new javax.swing.JLabel();
        jMonthChooser3 = new com.toedter.calendar.JMonthChooser();
        jTextField5 = new javax.swing.JTextField();
        btn_ventas1 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        txt_ruc1 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txt_razonsocial1 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txt_moneda1 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        TablaInicial2 = new javax.swing.JTable();
        jSeparator3 = new javax.swing.JSeparator();
        btn_ps4 = new javax.swing.JButton();
        btn_4ta = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        rb_femision = new javax.swing.JRadioButton();
        rb_serie = new javax.swing.JRadioButton();
        rb_ruc = new javax.swing.JRadioButton();
        rb_razonsocial = new javax.swing.JRadioButton();
        jTextField7 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        rb_documento = new javax.swing.JRadioButton();
        jPanel16 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TXT PLAME");

        jTabbedPane1.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "Datos Procesados", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 12))); // NOI18N

        tb_planillaProcesada.setModel(modelo1);
        tb_planillaProcesada.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tb_planillaProcesada.setRowHeight(28);
        tb_planillaProcesada.setShowHorizontalLines(true);
        tb_planillaProcesada.setShowVerticalLines(true);
        tb_planillaProcesada.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_planillaProcesadaMouseClicked(evt);
            }
        });
        tb_planillaProcesada.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tb_planillaProcesadaKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tb_planillaProcesadaKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(tb_planillaProcesada);

        btn_exportartxt.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btn_exportartxt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Rich Text Converter_1.png"))); // NOI18N
        btn_exportartxt.setText("EXPORTAR TXT");
        btn_exportartxt.setEnabled(false);
        btn_exportartxt.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btn_exportartxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_exportartxtActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(btn_exportartxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jSeparator2)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(btn_exportartxt, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "Datos / Carga de Archivo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 13))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel4.setText("Año:");

        jLabel5.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel5.setText("Año:");

        mesPlanilla.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        mesPlanilla.setForeground(new java.awt.Color(255, 0, 0));
        mesPlanilla.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N

        txt_rutaplanilla.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        txt_rutaplanilla.setDisabledTextColor(new java.awt.Color(255, 0, 0));
        txt_rutaplanilla.setEnabled(false);

        btn_cargar.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btn_cargar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_add_32px.png"))); // NOI18N
        btn_cargar.setText("Abrir Excel - Planilla");
        btn_cargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cargarActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel11.setText("Razón Social:");

        rucPlanilla.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        rucPlanilla.setForeground(new java.awt.Color(255, 0, 0));
        rucPlanilla.setText("20505606435");

        jLabel13.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel13.setText("RUC:");

        razonSocialPlanilla.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        razonSocialPlanilla.setForeground(new java.awt.Color(255, 0, 0));
        razonSocialPlanilla.setText("ASOCIACION CIRCULO MILITAR DEL PERU");

        jLabel15.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel15.setText("Tipo Moneda:");

        monedaPlanilla.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        monedaPlanilla.setForeground(new java.awt.Color(255, 0, 0));
        monedaPlanilla.setText("PEN");

        jButton1.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Place Marker.png"))); // NOI18N
        jButton1.setText("Posicion REM");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Place Marker.png"))); // NOI18N
        jButton2.setText("Posicion SNL");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
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
                        .addComponent(txt_rutaplanilla)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(añoPlanilla, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mesPlanilla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rucPlanilla, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(razonSocialPlanilla, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(monedaPlanilla, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2))
                    .addComponent(btn_cargar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(añoPlanilla, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(mesPlanilla, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rucPlanilla, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(razonSocialPlanilla, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(monedaPlanilla, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_cargar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_rutaplanilla))
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "Carga de Datos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 12))); // NOI18N

        tb_planilla.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tb_planilla.setRowHeight(28);
        tb_planilla.setShowHorizontalLines(true);
        tb_planilla.setShowVerticalLines(true);
        jScrollPane1.setViewportView(tb_planilla);

        btn_jor.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btn_jor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_outgoing_data_32px.png"))); // NOI18N
        btn_jor.setText("GENERAR  TXT .JOR (Jornada Laboral)");
        btn_jor.setEnabled(false);
        btn_jor.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btn_jor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_jorActionPerformed(evt);
            }
        });

        btn_rem.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btn_rem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_outgoing_data_32px.png"))); // NOI18N
        btn_rem.setText("GENERAR  TXT .REM (Ingresos, Tributos y Descuentos)");
        btn_rem.setEnabled(false);
        btn_rem.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btn_rem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_remActionPerformed(evt);
            }
        });

        btn_snl.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btn_snl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_outgoing_data_32px.png"))); // NOI18N
        btn_snl.setText("GENERAR  TXT .SNL (Dias Subsidiados y No Laborados)");
        btn_snl.setEnabled(false);
        btn_snl.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btn_snl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_snlActionPerformed(evt);
            }
        });

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filtros", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 12))); // NOI18N

        buttonGroup1.add(rb_dni);
        rb_dni.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        rb_dni.setText("DNI");
        rb_dni.setEnabled(false);
        rb_dni.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rb_dniItemStateChanged(evt);
            }
        });

        buttonGroup1.add(rb_trabajador);
        rb_trabajador.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        rb_trabajador.setSelected(true);
        rb_trabajador.setText("Trabajador");
        rb_trabajador.setEnabled(false);
        rb_trabajador.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rb_trabajadorItemStateChanged(evt);
            }
        });

        buttonGroup1.add(rb_cargo);
        rb_cargo.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        rb_cargo.setText("Cargo");
        rb_cargo.setEnabled(false);
        rb_cargo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rb_cargoItemStateChanged(evt);
            }
        });

        buttonGroup1.add(rb_centrocosto);
        rb_centrocosto.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        rb_centrocosto.setText("Centro Costo");
        rb_centrocosto.setEnabled(false);
        rb_centrocosto.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rb_centrocostoItemStateChanged(evt);
            }
        });
        rb_centrocosto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_centrocostoActionPerformed(evt);
            }
        });

        jTextField6.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        jTextField6.setEnabled(false);

        jButton4.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Restart.png"))); // NOI18N
        jButton4.setText("Restaurar");
        jButton4.setEnabled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        buttonGroup1.add(rb_regimen);
        rb_regimen.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        rb_regimen.setText("Regimen Pensionario");
        rb_regimen.setEnabled(false);
        rb_regimen.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rb_regimenItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rb_dni)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rb_trabajador)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rb_regimen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rb_cargo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rb_centrocosto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rb_dni)
                    .addComponent(rb_trabajador)
                    .addComponent(rb_cargo)
                    .addComponent(rb_centrocosto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4)
                    .addComponent(rb_regimen))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(btn_jor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_rem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_snl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_jor, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_rem, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_snl, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel6.setBackground(new java.awt.Color(0, 102, 153));

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("TXT - PLAME (PLANILLA)");
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
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
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("PLANILLA", new javax.swing.ImageIcon(getClass().getResource("/images/Summary List.png")), jPanel2); // NOI18N

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "Datos Procesados", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 12))); // NOI18N

        TablaProcesada2.setModel(modelo2);
        TablaProcesada2.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        TablaProcesada2.setRowHeight(28);
        TablaProcesada2.setShowHorizontalLines(true);
        TablaProcesada2.setShowVerticalLines(true);
        jScrollPane5.setViewportView(TablaProcesada2);

        btn_complementar1.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btn_complementar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Rich Text Converter_1.png"))); // NOI18N
        btn_complementar1.setText("EXPORTAR TXT");
        btn_complementar1.setEnabled(false);
        btn_complementar1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btn_complementar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_complementar1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 1284, Short.MAX_VALUE)
                    .addComponent(jSeparator4)
                    .addComponent(btn_complementar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_complementar1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
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

        btn_ventas1.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btn_ventas1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_add_32px.png"))); // NOI18N
        btn_ventas1.setText("Abrir Excel - 4ta");
        btn_ventas1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ventas1ActionPerformed(evt);
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
                        .addGap(0, 293, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addComponent(jTextField5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_ventas1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                    .addComponent(btn_ventas1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField5))
                .addContainerGap())
        );

        jPanel15.setBackground(new java.awt.Color(255, 255, 255));
        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "Carga de Datos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 12))); // NOI18N

        TablaInicial2.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        TablaInicial2.setRowHeight(28);
        TablaInicial2.setShowHorizontalLines(true);
        TablaInicial2.setShowVerticalLines(true);
        jScrollPane6.setViewportView(TablaInicial2);

        btn_ps4.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btn_ps4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_outgoing_data_32px.png"))); // NOI18N
        btn_ps4.setText("GENERAR  TXT .PS4 (Prestadores de Servicio con Rentas de 4ta Cat.)");
        btn_ps4.setEnabled(false);
        btn_ps4.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btn_ps4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ps4ActionPerformed(evt);
            }
        });

        btn_4ta.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btn_4ta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_outgoing_data_32px.png"))); // NOI18N
        btn_4ta.setText("GENERAR  TXT .4TA (Prestadores de Servicio con Rentas de 4ta Cat. - Detalle)");
        btn_4ta.setEnabled(false);
        btn_4ta.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btn_4ta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_4taActionPerformed(evt);
            }
        });

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filtros", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 12))); // NOI18N

        buttonGroup2.add(rb_femision);
        rb_femision.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        rb_femision.setText("Fecha Emisión");
        rb_femision.setEnabled(false);
        rb_femision.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rb_femisionItemStateChanged(evt);
            }
        });

        buttonGroup2.add(rb_serie);
        rb_serie.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        rb_serie.setText("N° Serie");
        rb_serie.setEnabled(false);
        rb_serie.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rb_serieItemStateChanged(evt);
            }
        });

        buttonGroup2.add(rb_ruc);
        rb_ruc.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        rb_ruc.setText("RUC");
        rb_ruc.setEnabled(false);
        rb_ruc.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rb_rucItemStateChanged(evt);
            }
        });
        rb_ruc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_rucActionPerformed(evt);
            }
        });

        buttonGroup2.add(rb_razonsocial);
        rb_razonsocial.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        rb_razonsocial.setSelected(true);
        rb_razonsocial.setText("Razón Social");
        rb_razonsocial.setEnabled(false);
        rb_razonsocial.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rb_razonsocialItemStateChanged(evt);
            }
        });
        rb_razonsocial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_razonsocialActionPerformed(evt);
            }
        });

        jTextField7.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        jTextField7.setEnabled(false);

        jButton5.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Restart.png"))); // NOI18N
        jButton5.setText("Restaurar");
        jButton5.setEnabled(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        buttonGroup2.add(rb_documento);
        rb_documento.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        rb_documento.setText("N° Documento");
        rb_documento.setEnabled(false);
        rb_documento.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rb_documentoItemStateChanged(evt);
            }
        });
        rb_documento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_documentoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rb_femision)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rb_serie)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rb_documento)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rb_ruc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rb_razonsocial)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rb_femision)
                    .addComponent(rb_serie)
                    .addComponent(rb_ruc)
                    .addComponent(rb_razonsocial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5)
                    .addComponent(rb_documento))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 1284, Short.MAX_VALUE)
                    .addComponent(jSeparator3)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addComponent(btn_ps4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_4ta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_ps4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_4ta, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel16.setBackground(new java.awt.Color(0, 102, 153));

        jLabel19.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("TXT - PLAME (4TA CATEGORIA)");
        jLabel19.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
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
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("4TA CATEGORIA", new javax.swing.ImageIcon(getClass().getResource("/images/Sustainability.png")), jPanel12); // NOI18N

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

    private void btn_cargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cargarActionPerformed
        jFileChooser1 = new JFileChooser();
        jFileChooser1.setDialogTitle("Elija el archivo de Excel");
        jFileChooser1.setFileFilter(new FileNameExtensionFilter("Archivos Excel", "xlsx"));
        // Obtener la última ubicación guardada
        File lastDirectory = getLastDirectory();

        // Establecer la última ubicación como directorio inicial del JFileChooser
        if (lastDirectory != null && lastDirectory.exists()) {
            jFileChooser1.setCurrentDirectory(lastDirectory);
        }
        int result = jFileChooser1.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jFileChooser1.getSelectedFile();
            txt_rutaplanilla.setText(selectedFile.getAbsolutePath());
            try {
                btn_cargar.setEnabled(false);
                añoPlanilla.setEnabled(false);
                mesPlanilla.setEnabled(false);
                this.setCursor(new Cursor(Cursor.WAIT_CURSOR));

                DefaultTableModel model = readExcel(selectedFile);
                
                // Verifica el número de columnas
                int columnCount = model.getColumnCount();
                if (columnCount != 95) {
                    JOptionPane.showMessageDialog(null, "El archivo Excel debe contener exactamente 95 columnas.", "Error", JOptionPane.ERROR_MESSAGE);
                    btn_cargar.setEnabled(true);
                    añoPlanilla.setEnabled(true);
                    mesPlanilla.setEnabled(true);
                    this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    return; // Sale del método si el número de columnas no es 95
                }
                
                tb_planilla.setModel(model);

                JTableHeader header = tb_planilla.getTableHeader();
                header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 40)); 
                
                header.setBackground(new java.awt.Color(0, 102, 153)); 
                header.setForeground(new java.awt.Color(255, 255, 255)); 
                tb_planilla.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

                ((TitledBorder) jPanel4.getBorder()).setTitle("Carga de Datos - Existen " + tb_planilla.getRowCount() + " Registros");
                jPanel4.revalidate();
                jPanel4.repaint();
                
                trsfiltro = new TableRowSorter(tb_planilla.getModel());
                tb_planilla.setRowSorter(trsfiltro);
                

                btn_jor.setEnabled(true);
                btn_rem.setEnabled(true);
                btn_snl.setEnabled(true);
                btn_cargar.setEnabled(true);
                añoPlanilla.setEnabled(true);
                mesPlanilla.setEnabled(true);
                
                rb_dni.setEnabled(true);
                rb_cargo.setEnabled(true);
                rb_centrocosto.setEnabled(true);
                rb_regimen.setEnabled(true);
                rb_trabajador.setEnabled(true);
                
                jTextField6.setEnabled(true);
                
                jButton4.setEnabled(true);
                
                
                packColumns(tb_planilla);
                saveLastDirectory(selectedFile.getParentFile());
                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error al leer el Excel", "Error", JOptionPane.ERROR_MESSAGE);
                btn_cargar.setEnabled(true);
                añoPlanilla.setEnabled(true);
                mesPlanilla.setEnabled(true);
                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }//GEN-LAST:event_btn_cargarActionPerformed

    private void btn_jorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_jorActionPerformed
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));

        DefaultTableModel model = (DefaultTableModel) tb_planilla.getModel();

        if (model.getValueAt(0, 0) == null) {
            model.removeRow(0);
        }

        llenar_tabla_JOR();

        modelo1.setRowCount(0);

        String[] datos = new String[6];
        for (int i = 0; i < tb_planilla.getRowCount(); i++) {
            if (!tb_planilla.getValueAt(i, 0).toString().isEmpty()) {
                if (tb_planilla.getValueAt(i, 1).toString().length() <= 8) {
                    datos[0] = "[01] - DNI";
                } else {
                    datos[0] = "[04] - CARNÉ EXT.";
                }
                datos[1] = tb_planilla.getValueAt(i, 1).toString();
                datos[2] = restarTiempos(new String[]{tb_planilla.getValueAt(i, 13).toString(), tb_planilla.getValueAt(i, 14).toString()}).split(":")[0];
                datos[3] = restarTiempos(new String[]{tb_planilla.getValueAt(i, 13).toString(), tb_planilla.getValueAt(i, 14).toString()}).split(":")[1];
                datos[4] = sumarTiempos(new String[]{tb_planilla.getValueAt(i, 16).toString(), tb_planilla.getValueAt(i, 17).toString(), tb_planilla.getValueAt(i, 18).toString()}).split(":")[0];
                datos[5] = sumarTiempos(new String[]{tb_planilla.getValueAt(i, 16).toString(), tb_planilla.getValueAt(i, 17).toString(), tb_planilla.getValueAt(i, 18).toString()}).split(":")[1];
                modelo1.addRow(datos);
            }
        }

        tb_planillaProcesada.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        btn_exportartxt.setEnabled(true);
        
        btn_exportartxt.setText("EXPORTAR TXT - JOR");

        ((TitledBorder) jPanel3.getBorder()).setTitle("Datos Procesados - Existen " + tb_planillaProcesada.getRowCount() + " Registros");
        jPanel3.revalidate();
        jPanel3.repaint();

        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

    }//GEN-LAST:event_btn_jorActionPerformed

    private void btn_ventas1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ventas1ActionPerformed
        jFileChooser1 = new JFileChooser();
        jFileChooser1.setDialogTitle("Elija el archivo de Excel");
        jFileChooser1.setFileFilter(new FileNameExtensionFilter("Archivos Excel", "xlsx"));
        // Obtener la última ubicación guardada
        File lastDirectory = getLastDirectory();

        // Establecer la última ubicación como directorio inicial del JFileChooser
        if (lastDirectory != null && lastDirectory.exists()) {
            jFileChooser1.setCurrentDirectory(lastDirectory);
        }
        int result = jFileChooser1.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jFileChooser1.getSelectedFile();
            jTextField5.setText(selectedFile.getAbsolutePath());
            try {
                this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                btn_ventas1.setEnabled(false);
                jYearChooser3.setEnabled(false);
                jMonthChooser3.setEnabled(false);
                
                DefaultTableModel model = readExcel(selectedFile);
                
                // Verifica el número de columnas
                int columnCount = model.getColumnCount();
                if (columnCount != 18) {
                    JOptionPane.showMessageDialog(null, "El archivo Excel debe contener exactamente 18 columnas.", "Error", JOptionPane.ERROR_MESSAGE);
                    this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    btn_ventas1.setEnabled(true);
                    jYearChooser3.setEnabled(true);
                    jMonthChooser3.setEnabled(true);
                    return; // Sale del método si el número de columnas no es 95
                }
                
                TablaInicial2.setModel(model);
               
                JTableHeader header = TablaInicial2.getTableHeader();
                header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 40));

                header.setBackground(new java.awt.Color(0, 102, 153)); 
                header.setForeground(new java.awt.Color(255, 255, 255)); 
                TablaInicial2.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

                ((TitledBorder) jPanel15.getBorder()).setTitle("Carga de Datos - Existen " + TablaInicial2.getRowCount() + " Registros");
                
                jPanel15.revalidate();
                jPanel15.repaint();
                
                trsfiltro2 = new TableRowSorter(TablaInicial2.getModel());
                TablaInicial2.setRowSorter(trsfiltro2);

                btn_ps4.setEnabled(true);
                btn_4ta.setEnabled(true);
                btn_ventas1.setEnabled(true);
                jYearChooser3.setEnabled(true);
                jMonthChooser3.setEnabled(true);
                
                rb_femision.setEnabled(true);
                rb_serie.setEnabled(true);
                rb_documento.setEnabled(true);
                rb_ruc.setEnabled(true);
                rb_razonsocial.setEnabled(true);
                jTextField7.setEnabled(true);
                jButton5.setEnabled(true);
                
                packColumns(TablaInicial2);
                saveLastDirectory(selectedFile.getParentFile());
                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

            } catch (IOException ex) {
                this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                btn_ventas1.setEnabled(true);
                jYearChooser3.setEnabled(true);
                jMonthChooser3.setEnabled(true);
                JOptionPane.showMessageDialog(null, "Error al leer el archivo Excel", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btn_ventas1ActionPerformed

    private void btn_complementar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_complementar1ActionPerformed
        switch (btn_complementar1.getText()) {
            case "EXPORTAR TXT - PS4":
                exportToExcel("PS4", TablaProcesada2, this, "0601" + jYearChooser3.getYear() + String.format("%02d", (jMonthChooser3.getMonth()+1)) + txt_ruc1.getText(), "ps4");
                break;
            case "EXPORTAR TXT - 4TA":
                exportToExcel2("4TA", TablaProcesada2, this, "0601" + jYearChooser3.getYear() + String.format("%02d", (jMonthChooser3.getMonth()+1)) + txt_ruc1.getText(), "4ta");
                break;
        }
    }//GEN-LAST:event_btn_complementar1ActionPerformed

    private void btn_ps4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ps4ActionPerformed
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        llenar_tabla_PS4();

        modelo2.setRowCount(0);

        String[] datos = new String[7];
        for (int i = 0; i < TablaInicial2.getRowCount(); i++) {
            datos[0] = "[06] - RUC";
            datos[1] = TablaInicial2.getValueAt(i, 6).toString();
            String[] nomape = TablaInicial2.getValueAt(i, 7).toString().split(",", 2);
            datos[2] = nomape[0].trim().split("\\s+")[0];
            datos[3] = nomape[0].trim().split("\\s+")[1];
            datos[4] =  (nomape.length > 1) ? nomape[1].trim() : nomape[0].trim().split("\\s+")[2];
            datos[5] = "[1] - Domiciliado";
            datos[6] = "0";
            modelo2.addRow(datos);
        }
        
// Usamos un conjunto para almacenar las filas únicas
        Set<String> filasUnicas = new HashSet<>();

// Iteramos sobre las filas de la tabla original
        for (int i = modelo2.getRowCount() - 1; i >= 0; i--) {
            // Obtenemos una representación de la fila como cadena
            StringBuilder filaString = new StringBuilder();
            for (int j = 0; j < modelo2.getColumnCount(); j++) {
                filaString.append(modelo2.getValueAt(i, j)).append(",");
            }
            // Si la fila está en el conjunto, la eliminamos de la tabla
            if (!filasUnicas.add(filaString.toString())) {
                modelo2.removeRow(i);
            }
        }
        
        TablaProcesada2.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        btn_complementar1.setText("EXPORTAR TXT - PS4");
        btn_complementar1.setEnabled(true);

        ((TitledBorder) jPanel13.getBorder()).setTitle("Datos Procesados - Existen " + TablaProcesada2.getRowCount() + " Registros");
        jPanel13.revalidate();
        jPanel13.repaint();

        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

    }//GEN-LAST:event_btn_ps4ActionPerformed

    private void btn_remActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_remActionPerformed
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));

        DefaultTableModel model = (DefaultTableModel) tb_planilla.getModel();

        if (model.getValueAt(0, 0) == null) {
            model.removeRow(0);
        }

        model.insertRow(0, new Object[model.getColumnCount()]);

        String nombreArchivo = "datosREM.txt"; // Nombre del archivo
        try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;

            while ((linea = reader.readLine()) != null) {
                // Dividir la línea en la posición de la columna y el dato
                String[] partes = linea.split(",");
                int posicionColumna = Integer.parseInt(partes[0]);
                String dato = partes[1];

                // Establecer el dato en la fila actual y la columna correspondiente
                tb_planilla.setValueAt(dato, 0, posicionColumna);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        llenar_tabla_REM();
        modelo1.setRowCount(0);

        String[] datos = new String[5];
        for (int i = 1; i < tb_planilla.getRowCount(); i++) {
            for (int j = 0; j < tb_planilla.getColumnCount(); j++) {
                if (!tb_planilla.getValueAt(i, 0).toString().isEmpty()) {
                    if (tb_planilla.getValueAt(0, j) != null) {
                        if (tb_planilla.getValueAt(i, 1).toString().length() <= 8) {
                            datos[0] = "[01] - DNI";
                        } else {
                            datos[0] = "[04] - CARNÉ EXT.";
                        }
                        datos[1] = tb_planilla.getValueAt(i, 1).toString();
                        datos[2] = buscarDescripcionPorNumero(ingresosMap, tb_planilla.getValueAt(0, j).toString().trim());
                        datos[3] = tb_planilla.getValueAt(i, j).toString();
                        datos[4] = tb_planilla.getValueAt(i, j).toString();
                        modelo1.addRow(datos);
                    }
                }
            }
        }
        
        DefaultTableModel model2 = (DefaultTableModel) tb_planillaProcesada.getModel();
        Map<String, Double[]> groupedData = new LinkedHashMap<>(); // Usamos LinkedHashMap para mantener el orden original

        // Iterar sobre las filas de la tabla
        for (int i = 0; i < model2.getRowCount(); i++) {
            // Obtener los valores de las columnas 1 al 3
            String key = model2.getValueAt(i, 0) + "|" + model2.getValueAt(i, 1) + "|" + model2.getValueAt(i, 2);
            Double[] values = groupedData.getOrDefault(key, new Double[]{0.0, 0.0});

            // Sumar los valores de las columnas 4 y 5
            values[0] += Double.parseDouble(model2.getValueAt(i, 3).toString());
            values[1] += Double.parseDouble(model2.getValueAt(i, 4).toString());

            groupedData.put(key, values);
        }

        // Limpiar el modelo de tabla
        model2.setRowCount(0);
        
        // Crear un formato para mostrar solo los decimales cuando sea necesario
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        // Agregar los datos agrupados al modelo de tabla
        for (Map.Entry<String, Double[]> entry : groupedData.entrySet()) {
            String[] keys = entry.getKey().split("\\|");
            Double[] values = entry.getValue();
            // Formatear los valores para mostrar solo "0" en lugar de "0.0"
            String value1 = values[0] == Math.floor(values[0]) ? String.valueOf(values[0].intValue()) : decimalFormat.format(values[0]);
            String value2 = values[1] == Math.floor(values[1]) ? String.valueOf(values[1].intValue()) : decimalFormat.format(values[1]);
            model2.addRow(new Object[]{keys[0], keys[1], keys[2], value1, value2});
        }
        


        // Obtener el modelo de la tabla
        // Recorrer las filas de la tabla desde abajo hacia arriba para evitar problemas al eliminar filas
        DefaultTableModel modelo1 = (DefaultTableModel) tb_planillaProcesada.getModel();

        // Suponiendo que tienes una tabla llamada "tb_planilla" con un modelo de tabla DefaultTableModel
        DefaultTableModel tb_planilla2 = (DefaultTableModel) tb_planilla.getModel();

        // Crear un conjunto para almacenar los números de la columna 2 que contienen "AFP" en la columna 8
        Set<String> numerosConAFP = new HashSet<>();

        // Iterar sobre las filas de la tabla tb_planilla2 para encontrar los números de la columna 2 que contienen "AFP" en la columna 8
        for (int i = 0; i < tb_planilla2.getRowCount(); i++) {
            String valorColumna8 = (String) tb_planilla2.getValueAt(i, 7); // Suponiendo que la columna 8 es la indexada en 7
            if (valorColumna8 != null && valorColumna8.contains("AFP")) {
                String numeroColumna2 = (String) tb_planilla2.getValueAt(i, 1); // Suponiendo que la columna 2 es la indexada en 1
                numerosConAFP.add(numeroColumna2);
            }
        }
        
        Set<String> numerosSinReg = new HashSet<>();

        // Iterar sobre las filas de la tabla tb_planilla2 para encontrar los números de la columna 2 que contienen "AFP" en la columna 8
        for (int i = 0; i < tb_planilla2.getRowCount(); i++) {
            String valorColumna8 = (String) tb_planilla2.getValueAt(i, 7); // Suponiendo que la columna 8 es la indexada en 7
            if (valorColumna8 == null || valorColumna8.isEmpty()) {
                String numeroColumna2 = (String) tb_planilla2.getValueAt(i, 1); // Suponiendo que la columna 2 es la indexada en 1
                numerosSinReg.add(numeroColumna2);
            }
        }

        // Iterar sobre las filas del modelo1
        for (int fila = modelo1.getRowCount() - 1; fila >= 0; fila--) {
            Object valorColumna3 = modelo1.getValueAt(fila, 2); // Suponiendo que la columna 3 es la indexada en 2

            // Verificar si el valor de la columna 3 es diferente de los valores a excluir y si el número de la columna 2 contiene "AFP"
            if (valorColumna3 != null) {
                if (valorColumna3.equals("[0605] - RENTA QUINTA CATEGORÍA RETENCIONES")) {
                    // Para "[0605] - RENTA QUINTA CATEGORÍA RETENCIONES", mantener el registro
                    continue; // Salta a la próxima iteración del bucle for
                } else if (valorColumna3.equals("[0601] - SISTEMA PRIVADO DE PENSIONES - COMISIÓN PORCENTUAL")) {
                    // Para "[0601] - SISTEMA PRIVADO DE PENSIONES - COMISIÓN PORCENTUAL"
                    Object numeroColumna2 = modelo1.getValueAt(fila, 1);
                    // Verificar si el número de la columna 2 está en numerosConAFP
                    if (numeroColumna2 != null && numerosConAFP.contains(numeroColumna2)) {
                        // Si el número está en numerosConAFP, mantener el registro
                        continue; // Salta a la próxima iteración del bucle for
                    } else if (numeroColumna2 != null && numerosSinReg.contains(numeroColumna2)) {
                        // Si el número está en numerosConAFP, mantener el registro
                        continue; // Salta a la próxima iteración del bucle for
                    } else {
                        // Si el número no está en numerosConAFP, eliminar la fila
                        modelo1.removeRow(fila);
                        continue; // Salta a la próxima iteración del bucle for
                    }
                } else if (valorColumna3.equals("[0608] - SISTEMA PRIVADO DE PENSIONES - APORTACIÓN OBLIGATORIA")) {
                    // Para "[0601] - SISTEMA PRIVADO DE PENSIONES - COMISIÓN PORCENTUAL"
                    Object numeroColumna2 = modelo1.getValueAt(fila, 1);
                    // Verificar si el número de la columna 2 está en numerosConAFP
                    if (numeroColumna2 != null && numerosConAFP.contains(numeroColumna2)) {
                        // Si el número está en numerosConAFP, mantener el registro
                        continue; // Salta a la próxima iteración del bucle for
                    } else if (numeroColumna2 != null && numerosSinReg.contains(numeroColumna2)) {
                        // Si el número está en numerosConAFP, mantener el registro
                        continue; // Salta a la próxima iteración del bucle for
                    } else {
                        // Si el número no está en numerosConAFP, eliminar la fila
                        modelo1.removeRow(fila);
                        continue; // Salta a la próxima iteración del bucle for
                    }
                }
            }

// Obtener los valores de las columnas 4 y 5
            Object valorColumna4 = modelo1.getValueAt(fila, 3);
            Object valorColumna5 = modelo1.getValueAt(fila, 4);

// Verificar si los valores son cero y eliminar la fila si es así
            if (valorColumna4 != null && valorColumna5 != null
                    && valorColumna4.equals("0") && valorColumna5.equals("0")) {
                modelo1.removeRow(fila);
            }
        }
    
        
        

        tb_planillaProcesada.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        btn_exportartxt.setEnabled(true);
        
        btn_exportartxt.setText("EXPORTAR TXT - REM");

        ((TitledBorder) jPanel3.getBorder()).setTitle("Datos Procesados - Existen " + tb_planillaProcesada.getRowCount() + " Registros");
        jPanel3.revalidate();
        jPanel3.repaint();

        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

    }//GEN-LAST:event_btn_remActionPerformed

    private void btn_snlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_snlActionPerformed
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        DefaultTableModel model = (DefaultTableModel) tb_planilla.getModel();

        if (model.getValueAt(0, 0) == null) {
            model.removeRow(0);
        }

        model.insertRow(0, new Object[model.getColumnCount()]);

        String nombreArchivo = "datosSNL.txt"; // Nombre del archivo
        try (BufferedReader reader = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;

            while ((linea = reader.readLine()) != null) {
                // Dividir la línea en la posición de la columna y el dato
                String[] partes = linea.split(",");
                int posicionColumna = Integer.parseInt(partes[0]);
                String dato = partes[1];

                // Establecer el dato en la fila actual y la columna correspondiente
                tb_planilla.setValueAt(dato, 0, posicionColumna);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        llenar_tabla_SNL();

        modelo1.setRowCount(0);

        String[] datos = new String[4];
        for (int i = 1; i < tb_planilla.getRowCount(); i++) {
            for (int j = 0; j < tb_planilla.getColumnCount(); j++) {
                if (!tb_planilla.getValueAt(i, 0).toString().isEmpty()) {
                    if (tb_planilla.getValueAt(0, j) != null) {
                        if (Integer.parseInt(tb_planilla.getValueAt(i, j).toString()) > 0) {
                            if (tb_planilla.getValueAt(i, 1).toString().length() <= 8) {
                                datos[0] = "[01] - DNI";
                            } else {
                                datos[0] = "[04] - CARNÉ EXT.";
                            }
                            datos[1] = tb_planilla.getValueAt(i, 1).toString();
                            
                            if (!tb_planilla.getValueAt(0, j).toString().trim().equals("20")) {
                                datos[2] = buscarDescripcionPorNumero(tipoSuspension, tb_planilla.getValueAt(0, j).toString().trim());
                            } else {
                                int valor = Integer.parseInt(tb_planilla.getValueAt(i, j).toString());
                                if (valor > 20) {
                                    datos[2] = buscarDescripcionPorNumero(tipoSuspension, "21");
                                } else {
                                    datos[2] = buscarDescripcionPorNumero(tipoSuspension, tb_planilla.getValueAt(0, j).toString().trim());
                                }
                            }
                            datos[3] = tb_planilla.getValueAt(i, j).toString();
                            modelo1.addRow(datos);
                        }

                    }
                }
            }
        }

        tb_planillaProcesada.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        btn_exportartxt.setEnabled(true);
        
        btn_exportartxt.setText("EXPORTAR TXT - SNL");

        ((TitledBorder) jPanel3.getBorder()).setTitle("Datos Procesados - Existen " + tb_planillaProcesada.getRowCount() + " Registros");
        jPanel3.revalidate();
        jPanel3.repaint();

        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

    }//GEN-LAST:event_btn_snlActionPerformed

    private void btn_4taActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_4taActionPerformed
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        
        llenar_tabla_4TA();

        modelo2.setRowCount(0);

        String[] datos = new String[11];
        for (int i = 0; i < TablaInicial2.getRowCount(); i++) {
            datos[0] = "[06] - RUC";
            datos[1] = TablaInicial2.getValueAt(i, 6).toString();
            datos[2] = "[R] - RECIBO POR HONORARIOS";
            datos[3] = TablaInicial2.getValueAt(i, 4).toString();
            datos[4] = TablaInicial2.getValueAt(i, 5).toString();
            datos[5] = TablaInicial2.getValueAt(i, 10).toString();
            datos[6] = TablaInicial2.getValueAt(i, 3).toString();
            datos[7] = TablaInicial2.getValueAt(i, 2).toString();
            if (Double.parseDouble(TablaInicial2.getValueAt(i, 11).toString()) > 0) {
                datos[8] = "[1] - SI";
            } else {
                datos[8] = "[0] - NO";
            }
            datos[9] = "";
            datos[10] = "";
            modelo2.addRow(datos);
        }
        
        TablaProcesada2.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        btn_complementar1.setText("EXPORTAR TXT - 4TA");
        btn_complementar1.setEnabled(true);

        ((TitledBorder) jPanel13.getBorder()).setTitle("Datos Procesados - Existen " + TablaProcesada2.getRowCount() + " Registros");
        jPanel13.revalidate();
        jPanel13.repaint();

        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btn_4taActionPerformed

    private void btn_exportartxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_exportartxtActionPerformed
        switch (btn_exportartxt.getText()) {
            case "EXPORTAR TXT - JOR":
                exportToExcel("JOR", tb_planillaProcesada, this, "0601" + añoPlanilla.getYear() + String.format("%02d", (mesPlanilla.getMonth()+1)) + rucPlanilla.getText(), "jor");
                break;
            case "EXPORTAR TXT - REM":
                exportToExcel("REM", tb_planillaProcesada, this, "0601" + añoPlanilla.getYear() + String.format("%02d", (mesPlanilla.getMonth()+1)) + rucPlanilla.getText(), "rem");
                break;
            case "EXPORTAR TXT - SNL":
                exportToExcel("SNL", tb_planillaProcesada, this, "0601" + añoPlanilla.getYear() + String.format("%02d", (mesPlanilla.getMonth()+1)) + rucPlanilla.getText(), "snl");
                break;
        }
    }//GEN-LAST:event_btn_exportartxtActionPerformed

    private void tb_planillaProcesadaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_planillaProcesadaMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1) {
             // Obtener el valor de la segunda columna de la nueva fila
                    Object cellValue = modelo1.getValueAt(tb_planillaProcesada.getSelectedRow(), 1);
                    
                    // Buscar y seleccionar la fila correspondiente en la otra tabla
                    for (int row = 0; row < tb_planilla.getRowCount(); row++) {
                        Object value = tb_planilla.getValueAt(row, 1);
                        if (value != null && value.equals(cellValue)) {
                            tb_planilla.changeSelection(row, 1, false, false);
                            break;
                        }
                    }
        }
    }//GEN-LAST:event_tb_planillaProcesadaMouseClicked

    private void tb_planillaProcesadaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tb_planillaProcesadaKeyPressed
       if (evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_DOWN) {
                    int selectedRow = tb_planillaProcesada.getSelectedRow();
                    int rowCount = tb_planillaProcesada.getRowCount();
                    int newRow;
                    
                    // Calcular la nueva fila seleccionada
                    if (evt.getKeyCode() == KeyEvent.VK_UP) {
                        newRow = Math.max(0, selectedRow - 1);
                    } else {
                        newRow = Math.min(rowCount - 1, selectedRow + 1);
                    }
                    
                    // Obtener el valor de la segunda columna de la nueva fila
                    Object cellValue = tb_planillaProcesada.getValueAt(newRow, 1);
                    
                    // Buscar y seleccionar la fila correspondiente en la otra tabla
                    for (int row = 0; row < tb_planilla.getRowCount(); row++) {
                        Object value = tb_planilla.getValueAt(row, 1);
                        if (value != null && value.equals(cellValue)) {
                            tb_planilla.changeSelection(row, 1, false, false);
                            break;
                        }
                    }
                }
            
    }//GEN-LAST:event_tb_planillaProcesadaKeyPressed

    private void tb_planillaProcesadaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tb_planillaProcesadaKeyTyped
         
    }//GEN-LAST:event_tb_planillaProcesadaKeyTyped

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        PosicionCod ini = new PosicionCod(this, true);
        ini.REM();
        ini.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        PosicionCod ini = new PosicionCod(this, true);
        ini.SNL();
        ini.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void rb_dniItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rb_dniItemStateChanged
        if (rb_dni.isSelected()) {
            jTextField6.setText("");
            repaint();
            filterTable(tb_planilla, "", 1);
            jTextField6.requestFocus();
        }
    }//GEN-LAST:event_rb_dniItemStateChanged

    private void rb_trabajadorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rb_trabajadorItemStateChanged
        if (rb_trabajador.isSelected()) {
            jTextField6.setText("");
            repaint();
            filterTable(tb_planilla, "", 2);
            jTextField6.requestFocus();
        }
    }//GEN-LAST:event_rb_trabajadorItemStateChanged

    private void rb_cargoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rb_cargoItemStateChanged
        if (rb_cargo.isSelected()) {
            jTextField6.setText("");
            repaint();
            filterTable(tb_planilla, "", 9);
            jTextField6.requestFocus();
        }
    }//GEN-LAST:event_rb_cargoItemStateChanged

    private void rb_centrocostoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rb_centrocostoItemStateChanged
        if (rb_centrocosto.isSelected()) {
            jTextField6.setText("");
            repaint();
            filterTable(tb_planilla, "", 10);
            jTextField6.requestFocus();
        }
    }//GEN-LAST:event_rb_centrocostoItemStateChanged

    private void rb_centrocostoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_centrocostoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rb_centrocostoActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        jTextField6.setText("");
        repaint();
        filterTable(tb_planilla, "", 2);
        jTextField6.requestFocus();
        rb_trabajador.setSelected(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void rb_regimenItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rb_regimenItemStateChanged
        if (rb_regimen.isSelected()) {
            jTextField6.setText("");
            repaint();
            filterTable(tb_planilla, "", 7);
            jTextField6.requestFocus();
        }
    }//GEN-LAST:event_rb_regimenItemStateChanged

    private void rb_femisionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rb_femisionItemStateChanged
       if (rb_femision.isSelected()) {
            jTextField7.setText("");
            repaint();
            filterTable(TablaInicial2, "", 3);
            jTextField7.requestFocus();
        }
    }//GEN-LAST:event_rb_femisionItemStateChanged

    private void rb_serieItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rb_serieItemStateChanged
        if (rb_serie.isSelected()) {
            jTextField7.setText("");
            repaint();
            filterTable(TablaInicial2, "", 4);
            jTextField7.requestFocus();
        }
    }//GEN-LAST:event_rb_serieItemStateChanged

    private void rb_rucItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rb_rucItemStateChanged
        if (rb_ruc.isSelected()) {
            jTextField7.setText("");
            repaint();
            filterTable(TablaInicial2, "", 6);
            jTextField7.requestFocus();
        }
    }//GEN-LAST:event_rb_rucItemStateChanged

    private void rb_razonsocialItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rb_razonsocialItemStateChanged
        if (rb_razonsocial.isSelected()) {
            jTextField7.setText("");
            repaint();
            filterTable(TablaInicial2, "", 7);
            jTextField7.requestFocus();
        }
    }//GEN-LAST:event_rb_razonsocialItemStateChanged

    private void rb_razonsocialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_razonsocialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rb_razonsocialActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        jTextField7.setText("");
        repaint();
        filterTable(TablaInicial2, "", 2);
        jTextField7.requestFocus();
        rb_razonsocial.setSelected(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void rb_documentoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rb_documentoItemStateChanged
        if (rb_documento.isSelected()) {
            jTextField7.setText("");
            repaint();
            filterTable(TablaInicial2, "", 5);
            jTextField7.requestFocus();
        }
    }//GEN-LAST:event_rb_documentoItemStateChanged

    private void rb_rucActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_rucActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rb_rucActionPerformed

    private void rb_documentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_documentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rb_documentoActionPerformed

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
                new Txt_Plame(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TablaInicial2;
    private javax.swing.JTable TablaProcesada2;
    private com.toedter.calendar.JYearChooser añoPlanilla;
    private javax.swing.JButton btn_4ta;
    private javax.swing.JButton btn_cargar;
    private javax.swing.JButton btn_complementar1;
    private javax.swing.JButton btn_exportartxt;
    private javax.swing.JButton btn_jor;
    private javax.swing.JButton btn_ps4;
    private javax.swing.JButton btn_rem;
    private javax.swing.JButton btn_snl;
    private javax.swing.JButton btn_ventas1;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private com.toedter.calendar.JMonthChooser jMonthChooser3;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private com.toedter.calendar.JYearChooser jYearChooser3;
    private com.toedter.calendar.JMonthChooser mesPlanilla;
    private javax.swing.JLabel monedaPlanilla;
    private javax.swing.JLabel razonSocialPlanilla;
    private javax.swing.JRadioButton rb_cargo;
    private javax.swing.JRadioButton rb_centrocosto;
    private javax.swing.JRadioButton rb_dni;
    private javax.swing.JRadioButton rb_documento;
    private javax.swing.JRadioButton rb_femision;
    private javax.swing.JRadioButton rb_razonsocial;
    private javax.swing.JRadioButton rb_regimen;
    private javax.swing.JRadioButton rb_ruc;
    private javax.swing.JRadioButton rb_serie;
    private javax.swing.JRadioButton rb_trabajador;
    private javax.swing.JLabel rucPlanilla;
    private javax.swing.JTable tb_planilla;
    private javax.swing.JTable tb_planillaProcesada;
    private javax.swing.JLabel txt_moneda1;
    private javax.swing.JLabel txt_razonsocial1;
    private javax.swing.JLabel txt_ruc1;
    private javax.swing.JTextField txt_rutaplanilla;
    // End of variables declaration//GEN-END:variables
}
