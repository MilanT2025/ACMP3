package vista;

import Controlador.ComboBoxTableCellRenderer;
import Controlador.ExcelExporter;
import Controlador.FuncionesGlobales;
import static Controlador.FuncionesGlobales.getLastDirectory;
import static Controlador.FuncionesGlobales.saveLastDirectory;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import main.Application;
import org.apache.poi.ss.usermodel.*;
import static org.apache.poi.ss.usermodel.CellType.BOOLEAN;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel_AFPNet extends javax.swing.JFrame {

    private final Modelo modelo1 = new Modelo();
    
    Map<String, String> tipoDocumento, excepcionAportar, relacionLaboral, tipoTrabajo;
    TableRowSorter trsfiltro;
    /**
     * Creates new form JGUIForm
     */
    public Excel_AFPNet() {
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
        
        //this.setLocationRelativeTo(null);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        llenar_tabla();

        // Obtener la fecha actual
        LocalDate fechaActual = LocalDate.now();

        // Restar un mes a la fecha actual
        LocalDate fechaMesAnterior = fechaActual.minusMonths(1);

        // Configurar el JDateChooser con la fecha del mes anterior
        jMonthChooser1.setMonth(fechaMesAnterior.getMonthValue() - 1);

        // Configurar el JYearChooser con el año del mes anterior
        jYearChooser1.setYear(fechaMesAnterior.getYear());
        
        jTextField5.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            if (rb_dni.isSelected()) {
                filterTable(TablaInicial, jTextField5.getText(), 1);
            }else if(rb_trabajador.isSelected()){
                filterTable(TablaInicial, jTextField5.getText(), 2);
            }else if(rb_cargo.isSelected()){
                filterTable(TablaInicial, jTextField5.getText(), 9);
            }else if(rb_centrocosto.isSelected()){
                filterTable(TablaInicial, jTextField5.getText(), 10);
            }else if(rb_regimen.isSelected()){
                filterTable(TablaInicial, jTextField5.getText(), 7);
            }
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            if (rb_dni.isSelected()) {
                filterTable(TablaInicial, jTextField5.getText(), 1);
            }else if(rb_trabajador.isSelected()){
                filterTable(TablaInicial, jTextField5.getText(), 2);
            }else if(rb_cargo.isSelected()){
                filterTable(TablaInicial, jTextField5.getText(), 9);
            }else if(rb_centrocosto.isSelected()){
                filterTable(TablaInicial, jTextField5.getText(), 10);
            }else if(rb_regimen.isSelected()){
                filterTable(TablaInicial, jTextField5.getText(), 7);
            }
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            if (rb_dni.isSelected()) {
                filterTable(TablaInicial, jTextField5.getText(), 1);
            }else if(rb_trabajador.isSelected()){
                filterTable(TablaInicial, jTextField5.getText(), 2);
            }else if(rb_cargo.isSelected()){
                filterTable(TablaInicial, jTextField5.getText(), 9);
            }else if(rb_centrocosto.isSelected()){
                filterTable(TablaInicial, jTextField5.getText(), 10);
            }else if(rb_regimen.isSelected()){
                filterTable(TablaInicial, jTextField5.getText(), 7);
            }
        }
    });
        
        TablaInicial.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    FuncionesGlobales.seleccionarFilas(TablaInicial, TablaProcesada, 1, 3);
                }
            }
        });
        
    }

    public class Modelo extends DefaultTableModel {

        @Override
        public boolean isCellEditable(int row, int column) {
            return true;
        }
    }

    private void llenar_tabla() {
        String[] columnNames = new String[]{
            "Número de secuencia", "CUSPP", "Tipo de documento de identidad", "Número de documento de identidad", "Apellido paterno", "Apellido materno", "Nombres", "Relación Laboral", "Inicio de RL", "Cese de RL", "Excepción de Aportar", "Remuneración asegurable", "Aporte voluntario del afiliado con fin previsional", "Aporte voluntario del afiliado sin fin previsional", "Aporte voluntario del empleador", "Tipo de trabajo o Rubro", "AFP (Conviene dejar en blanco)"
        };

        modelo1.setColumnIdentifiers(columnNames);

        JTableHeader header = TablaProcesada.getTableHeader();
        header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 40));
        header.setBackground(new java.awt.Color(255, 217, 102)); 
        TablaProcesada.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

        TableColumnModel columnModel = TablaProcesada.getColumnModel();
        for (int col = 0; col < TablaProcesada.getColumnCount(); col++) {
            TableColumn tableColumn = columnModel.getColumn(col);
            Component comp = header.getDefaultRenderer().getTableCellRendererComponent(TablaProcesada, tableColumn.getHeaderValue(), false, false, 0, col);
            int width = comp.getPreferredSize().width + 25;
            tableColumn.setPreferredWidth(width);
        }

        // Tipo Documento AFPNET
        tipoDocumento = new HashMap<>();
        tipoDocumento.put("[0] - DNI", "0");
        tipoDocumento.put("[1] - Carnet de Extranjería", "1");
        tipoDocumento.put("[2] - Carnet Militar y Policial", "2");
        tipoDocumento.put("[3] - Libreta Adolecentes Trabajador", "3");
        tipoDocumento.put("[4] - Pasaporte", "4");
        tipoDocumento.put("[5] - Inexistente/Afilia", "5");
        tipoDocumento.put("[6] - Permiso Temporal de Permamencia", "6");
        tipoDocumento.put("[7] - Carnet de identidad de Relaciones Exteriores  ", "7");
        tipoDocumento.put("[8] - Cédula de identidad de extranjero", "8");
        tipoDocumento.put("[9] - Carnet de solicitante de refugio", "9");

        // Excepcion Aportar AFPNET
        excepcionAportar = new HashMap<>();
        excepcionAportar.put("", "");
        excepcionAportar.put("[L] - Licencia sin goce de haber.", "L");
        excepcionAportar.put("[U] - Subsidio pagado directamente por ESSALUD al trabajador.", "U");
        excepcionAportar.put("[J] - Afiliado pensionado por jubilación.", "J");
        excepcionAportar.put("[I] - Afiliado pensionado por invalidez.", "I");
        excepcionAportar.put("[P] - Aporte postergado por inicio de la Relación Laboral posterior al cierre de planillas.", "P");
        excepcionAportar.put("[O] - Otro motivo por el cual el trabajador no percibió remuneración por el mes.", "O");

        // Relacion Laboral AFPNET
        relacionLaboral = new HashMap<>();
        relacionLaboral.put("[S] - SI", "S");
        relacionLaboral.put("[N] - NO", "N");

        //Tipo de Trabajo AFPNET
        tipoTrabajo = new HashMap<>();
        tipoTrabajo.put("[N] - Dependiente Normal", "N");
        tipoTrabajo.put("[C] - Dependiente Contrucción", "C");
        tipoTrabajo.put("[M] - Dependiente Minería", "M");
        tipoTrabajo.put("[P] - Pesquero", "P");

        TablaProcesada.getColumnModel().getColumn(2).setCellEditor(new ComboBoxTableCellRenderer(tipoDocumento));
        TablaProcesada.getColumnModel().getColumn(10).setCellEditor(new ComboBoxTableCellRenderer(excepcionAportar));
        TablaProcesada.getColumnModel().getColumn(7).setCellEditor(new ComboBoxTableCellRenderer(relacionLaboral));
        TablaProcesada.getColumnModel().getColumn(8).setCellEditor(new ComboBoxTableCellRenderer(relacionLaboral));
        TablaProcesada.getColumnModel().getColumn(9).setCellEditor(new ComboBoxTableCellRenderer(relacionLaboral));
        TablaProcesada.getColumnModel().getColumn(15).setCellEditor(new ComboBoxTableCellRenderer(tipoTrabajo));
    }

    private void AFPNet() {
        modelo1.setRowCount(0);
        int c = 1;
        for (int i = 0; i < TablaInicial.getRowCount(); i++) {
            if (TablaInicial.getValueAt(i, 7).toString().contains("AFP")) {

                String[] datos = new String[17];
                datos[0] = String.valueOf(c);
                datos[1] = "";
                if (TablaInicial.getValueAt(i, 1).toString().length() <= 8) {
                     datos[2] = "[0] - DNI";
                } else {
                     datos[2] = "[1] - Carnet de Extranjería";
                }
               
                datos[3] = TablaInicial.getValueAt(i, 1).toString();

                String[] nomape = TablaInicial.getValueAt(i, 2).toString().split(",", 2);
                datos[4] = nomape[0].trim().split("\\s+")[0];
                datos[5] = nomape[0].trim().split("\\s+")[1];
                datos[6] = nomape[1].trim();
                datos[7] = "[S] - SI";

                String fechaIngreso = TablaInicial.getValueAt(i, 5).toString();
                String fechaCese = TablaInicial.getValueAt(i, 6).toString();
                int añoChooser = jYearChooser1.getYear();
                int mesChooser = jMonthChooser1.getMonth() + 1; // El método getMonth() devuelve el mes indexado desde 0, por lo que se suma 1 para obtener el mes real.

                if (fechaIngreso.isEmpty()) {
                    datos[8] = "[N] - NO";
                } else {
                    LocalDate fecha = LocalDate.parse(fechaIngreso, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    int añoTabla = fecha.getYear();
                    int mesTabla = fecha.getMonthValue();

                    datos[8] = (añoTabla == añoChooser && mesTabla == mesChooser) ? "[S] - SI" : "[N] - NO";
                }

                if (fechaCese.isEmpty()) {
                    datos[9] = "[N] - NO";
                } else {
                    LocalDate fecha = LocalDate.parse(fechaCese, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    int añoTabla = fecha.getYear();
                    int mesTabla = fecha.getMonthValue();

                    datos[9] = (añoTabla == añoChooser && mesTabla == mesChooser) ? "[S] - SI" : "[N] - NO";
                }

                if (Integer.parseInt(TablaInicial.getValueAt(i, 30).toString()) >= 30) {
                    datos[10] = "[L] - Licencia sin goce de haber.";
                } else {
                    datos[10] = "";
                }

                datos[11] = TablaInicial.getValueAt(i, 57).toString();
                datos[12] = "0";
                datos[13] = "0";
                datos[14] = "0";
                datos[15] = "[N] - Dependiente Normal";
                datos[16] = "";
                c++;

                modelo1.addRow(datos);
            }
        }
        packColumns(TablaProcesada);
        
        jButton3.setEnabled(true);
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
    
    public void filterTable(JTable table, String filterText, int indiceColumna) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        TableRowSorter<DefaultTableModel> rowSorter = (TableRowSorter<DefaultTableModel>) table.getRowSorter();
        if (filterText.trim().length() == 0) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + filterText, indiceColumna));
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
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jYearChooser1 = new com.toedter.calendar.JYearChooser();
        jLabel5 = new javax.swing.JLabel();
        jMonthChooser1 = new com.toedter.calendar.JMonthChooser();
        jTextField2 = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        rucPlanilla = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        razonSocialPlanilla = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaInicial = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        rb_dni = new javax.swing.JRadioButton();
        rb_trabajador = new javax.swing.JRadioButton();
        rb_cargo = new javax.swing.JRadioButton();
        rb_centrocosto = new javax.swing.JRadioButton();
        jTextField5 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        rb_regimen = new javax.swing.JRadioButton();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TablaProcesada = new javax.swing.JTable();
        jSeparator2 = new javax.swing.JSeparator();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("AFP NET");

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jPanel4.setBackground(new java.awt.Color(0, 102, 153));

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Excel para presentación a la AFPNet");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
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

        jButton2.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_add_32px.png"))); // NOI18N
        jButton2.setText("Abrir Excel - Planilla");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel13.setText("RUC:");

        rucPlanilla.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        rucPlanilla.setForeground(new java.awt.Color(255, 0, 0));
        rucPlanilla.setText("20505606435");

        jLabel11.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel11.setText("Razón Social:");

        razonSocialPlanilla.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        razonSocialPlanilla.setForeground(new java.awt.Color(255, 0, 0));
        razonSocialPlanilla.setText("ASOCIACION CIRCULO MILITAR DEL PERU");

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
                        .addComponent(rucPlanilla, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(razonSocialPlanilla, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 464, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jTextField2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jYearChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jMonthChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rucPlanilla, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(razonSocialPlanilla, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField2))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "Carga de Datos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 12))); // NOI18N

        TablaInicial.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        TablaInicial.setRowHeight(28);
        TablaInicial.setShowHorizontalLines(true);
        TablaInicial.setShowVerticalLines(true);
        TablaInicial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaInicialMouseClicked(evt);
            }
        });
        TablaInicial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TablaInicialKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(TablaInicial);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filtros", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 12))); // NOI18N

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

        jTextField5.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        jTextField5.setEnabled(false);

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

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
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
                .addComponent(jTextField5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rb_dni)
                    .addComponent(rb_trabajador)
                    .addComponent(rb_cargo)
                    .addComponent(rb_centrocosto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4)
                    .addComponent(rb_regimen))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jButton1.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_outgoing_data_32px.png"))); // NOI18N
        jButton1.setText("GENERAR EXCEL AFP.NET");
        jButton1.setEnabled(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "Datos Procesados", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 12))); // NOI18N

        TablaProcesada.setModel(modelo1);
        TablaProcesada.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        TablaProcesada.setRowHeight(28);
        TablaProcesada.setShowHorizontalLines(true);
        TablaProcesada.setShowVerticalLines(true);
        TablaProcesada.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TablaProcesadaMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(TablaProcesada);

        jButton3.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Microsoft excel.png"))); // NOI18N
        jButton3.setText("EXPORTAR EXCEL AFP.NET");
        jButton3.setEnabled(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(jSeparator2)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Crear un nuevo FileChooser
        jButton3.setEnabled(false);
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        String nombreEnvio = jYearChooser1.getYear() + "_" + String.format("%02d", (jMonthChooser1.getMonth()+1)) + "_" + rucPlanilla.getText() + "_AFPNET";
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar como");
        fileChooser.setSelectedFile(new File(nombreEnvio));

        // Mostrar el FileChooser para seleccionar la ubicación y el nombre del archivo
        int userSelection = fileChooser.showSaveDialog(null);

        // Si el usuario selecciona guardar
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String nombreArchivo = fileToSave.getAbsolutePath();

            try {
                // Crear un nuevo libro de trabajo
                XSSFWorkbook libro = new XSSFWorkbook();
                XSSFSheet hoja = libro.createSheet("Planilla");

                // Iterar sobre los datos de la tabla y escribir en el libro
                DefaultTableModel model = (DefaultTableModel) TablaProcesada.getModel();
                for (int i = 0; i < model.getRowCount(); i++) {
                    XSSFRow fila = hoja.createRow(i);
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        switch (j) {
                            case 0:
                            fila.createCell(j).setCellValue(Integer.parseInt(model.getValueAt(i, j).toString()));
                            break;
                            case 2:
                            fila.createCell(j).setCellValue(Integer.parseInt(tipoDocumento.get(model.getValueAt(i, j).toString())));
                            break;
                            case 3:
                            fila.createCell(j).setCellValue(model.getValueAt(i, j).toString());
                            break;

                            case 7:
                            case 8:
                            case 9:
                            fila.createCell(j).setCellValue(relacionLaboral.get(model.getValueAt(i, j).toString()));
                            break;
                            case 10:
                            fila.createCell(j).setCellValue(excepcionAportar.get(model.getValueAt(i, j).toString()));
                            break;
                            case 11:
                            fila.createCell(j).setCellValue(Double.parseDouble(model.getValueAt(i, j).toString()));
                            break;
                            case 12:
                            case 13:
                            case 14:
                            fila.createCell(j).setCellValue(Integer.parseInt(model.getValueAt(i, j).toString()));
                            break;
                            case 15:
                            fila.createCell(j).setCellValue(tipoTrabajo.get(model.getValueAt(i, j).toString()));
                            break;

                            default:
                            if (model.getValueAt(i, j) instanceof String string) {
                                fila.createCell(j).setCellValue(string);
                            } else if (model.getValueAt(i, j) instanceof Integer integer) {
                                fila.createCell(j).setCellValue(integer);
                            } else if (model.getValueAt(i, j) instanceof Double double1) {
                                fila.createCell(j).setCellValue(double1);
                            }
                            break;

                        }

                    }
                }

                // Escribir el libro en un archivo
                FileOutputStream fileOut = new FileOutputStream(nombreArchivo + ".xlsx");
                libro.write(fileOut);
                fileOut.close();
                
                JOptionPane.showMessageDialog(null, "<html>El archivo de <b>ENVIO EXCEL</b> se ha exportado exitosamente con el siguiente nombre:<br><br><font color='red'><b>" + nombreEnvio + ".xlsx </font></b></html>", "Exportación Exitosa", JOptionPane.INFORMATION_MESSAGE);
                
                if (JOptionPane.showConfirmDialog(null, "¿Desea exportar un excel para verificar de informacion enviada?", "Confirmación", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    ExcelExporter.exportToExcel2(nombreArchivo, "AFPNET_DATA", TablaProcesada, this, "DATA");
                    JOptionPane.showMessageDialog(null, "<html>El archivo de <b>EXCEL</b> se ha exportado exitosamente con el siguiente nombre:<br><br><font color='red'><b>" + nombreEnvio + "_DATA.xlsx </font></b></html>", "Exportación Exitosa", JOptionPane.INFORMATION_MESSAGE);
                }

                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                jButton3.setEnabled(true);
                
                // Mostrar mensaje de éxito
                
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al exportar el archivo EXCEL");
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                jButton3.setEnabled(true);
            }
        } else {
            // Si el usuario cancela la operación
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            jButton3.setEnabled(true);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jFileChooser1 = new JFileChooser();
        jFileChooser1.setDialogTitle("Elija el archivo de Excel");
        jFileChooser1.setFileFilter(new FileNameExtensionFilter("Archivos Excel", "xlsx"));
        
        
        File lastDirectory = getLastDirectory();

        // Establecer la última ubicación como directorio inicial del JFileChooser
        if (lastDirectory != null && lastDirectory.exists()) {
            jFileChooser1.setCurrentDirectory(lastDirectory);
        }
        
        int result = jFileChooser1.showOpenDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jFileChooser1.getSelectedFile();
            jTextField2.setText(selectedFile.getAbsolutePath());
            try {
                DefaultTableModel model = readExcel(selectedFile);

                // Verifica el número de columnas
                int columnCount = model.getColumnCount();
                if (columnCount != 95) {
                    JOptionPane.showMessageDialog(null, "El archivo Excel debe contener exactamente 95 columnas.", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Sale del método si el número de columnas no es 95
                }
                
                
                TablaInicial.setModel(model);
                

                JTableHeader header = TablaInicial.getTableHeader();
                header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 40)); // Establece la altura deseada (por ejemplo, 30 píxeles)

                // Cambiar el color del encabezado
                header.setBackground(new java.awt.Color(0, 102, 153)); // Melon (RGB: 255, 140, 105)
                header.setForeground(new java.awt.Color(255, 255, 255)); // Melon (RGB: 255, 140, 105)
                TablaInicial.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));
                
                trsfiltro = new TableRowSorter(TablaInicial.getModel());
                TablaInicial.setRowSorter(trsfiltro);
                
                rb_dni.setEnabled(true);
                rb_cargo.setEnabled(true);
                rb_centrocosto.setEnabled(true);
                rb_regimen.setEnabled(true);
                rb_trabajador.setEnabled(true);
                
                jTextField5.setEnabled(true);
                
                jButton4.setEnabled(true);
                jButton1.setEnabled(true);
                
                packColumns(TablaInicial);
                saveLastDirectory(selectedFile.getParentFile());

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error al leer el archivo Excel", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        AFPNet();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void rb_centrocostoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_centrocostoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rb_centrocostoActionPerformed

    private void rb_dniItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rb_dniItemStateChanged
        if (rb_dni.isSelected()) {
            jTextField5.setText("");
            repaint();
            filterTable(TablaInicial, "", 1);
            jTextField5.requestFocus();
        }
    }//GEN-LAST:event_rb_dniItemStateChanged

    private void rb_trabajadorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rb_trabajadorItemStateChanged
        if (rb_trabajador.isSelected()) {
            jTextField5.setText("");
            repaint();
            filterTable(TablaInicial, "", 2);
            jTextField5.requestFocus();
        }
    }//GEN-LAST:event_rb_trabajadorItemStateChanged

    private void rb_cargoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rb_cargoItemStateChanged
        if (rb_cargo.isSelected()) {
            jTextField5.setText("");
            repaint();
            filterTable(TablaInicial, "", 9);
            jTextField5.requestFocus();
        }
    }//GEN-LAST:event_rb_cargoItemStateChanged

    private void rb_centrocostoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rb_centrocostoItemStateChanged
        if (rb_centrocosto.isSelected()) {
            jTextField5.setText("");
            repaint();
            filterTable(TablaInicial, "", 10);
            jTextField5.requestFocus();
        }
    }//GEN-LAST:event_rb_centrocostoItemStateChanged

    private void rb_regimenItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rb_regimenItemStateChanged
        if (rb_regimen.isSelected()) {
            jTextField5.setText("");
            repaint();
            filterTable(TablaInicial, "", 7);
            jTextField5.requestFocus();
        }
    }//GEN-LAST:event_rb_regimenItemStateChanged

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
            jTextField5.setText("");
            repaint();
            filterTable(TablaInicial, "", 2);
            jTextField5.requestFocus();
            rb_trabajador.setSelected(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void TablaProcesadaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaProcesadaMouseClicked
        /*if (evt.getButton() == MouseEvent.BUTTON1) {
             // Obtener el valor de la segunda columna de la nueva fila
                    Object cellValue = modelo1.getValueAt(TablaProcesada.getSelectedRow(), 3);
                    
                    // Buscar y seleccionar la fila correspondiente en la otra tabla
                    for (int row = 0; row < TablaInicial.getRowCount(); row++) {
                        Object value = TablaInicial.getValueAt(row, 1);
                        if (value != null && value.equals(cellValue)) {
                            TablaInicial.changeSelection(row, 1, false, false);
                            break;
                        }
                    }
        }*/
    }//GEN-LAST:event_TablaProcesadaMouseClicked

    private void TablaInicialMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaInicialMouseClicked
        /*if (evt.getButton() == MouseEvent.BUTTON1) {
             // Obtener el valor de la segunda columna de la nueva fila
                    Object cellValue = TablaInicial.getValueAt(TablaInicial.getSelectedRow(), 1);
                    
                    // Buscar y seleccionar la fila correspondiente en la otra tabla
                    for (int row = 0; row < TablaProcesada.getRowCount(); row++) {
                        Object value = TablaProcesada.getValueAt(row, 3);
                        if (value != null && value.equals(cellValue)) {
                            TablaProcesada.changeSelection(row, 1, false, false);
                            break;
                        }
                    }
        }*/
    }//GEN-LAST:event_TablaInicialMouseClicked

    private void TablaInicialKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TablaInicialKeyPressed
       
    }//GEN-LAST:event_TablaInicialKeyPressed


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
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Excel_AFPNet().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TablaInicial;
    private javax.swing.JTable TablaProcesada;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private com.toedter.calendar.JMonthChooser jMonthChooser1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField5;
    private com.toedter.calendar.JYearChooser jYearChooser1;
    private javax.swing.JLabel razonSocialPlanilla;
    private javax.swing.JRadioButton rb_cargo;
    private javax.swing.JRadioButton rb_centrocosto;
    private javax.swing.JRadioButton rb_dni;
    private javax.swing.JRadioButton rb_regimen;
    private javax.swing.JRadioButton rb_trabajador;
    private javax.swing.JLabel rucPlanilla;
    // End of variables declaration//GEN-END:variables
}
