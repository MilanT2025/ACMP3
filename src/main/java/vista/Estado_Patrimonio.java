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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
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
import java.text.DateFormatSymbols;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.HashMap;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.TableModelEvent;
import main.Application;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Farmacia-Ingeniero
 */
public class Estado_Patrimonio extends javax.swing.JFrame {

    private final Modelo modelo = new Modelo();
    Calendar calendar;
    SimpleDateFormat dateFormat = new SimpleDateFormat("'Al' dd 'de' MMMM yyyy");
    String ultimoDia;

    /**
     * Creates new form LibrosE_SIRE
     */
    public Estado_Patrimonio() {
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
        mes.setMonth(fechaMesAnterior.getMonthValue() - 1);

        llenar_tabla();

        btn_cargar.doClick();
        
        mes.addPropertyChangeListener("month", (java.beans.PropertyChangeEvent evt) -> {
            btn_cargar.doClick();
        });
        año.addPropertyChangeListener("year", (java.beans.PropertyChangeEvent evt) -> {
            btn_cargar.doClick();
        });
    }

    public class Modelo extends DefaultTableModel {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    private void llenar_tabla() {
        modelo.setRowCount(0);
        modelo.setColumnCount(0);

        tb_resultado.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

        calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, año.getYear());
        calendar.set(Calendar.MONTH, mes.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        ultimoDia = dateFormat.format(calendar.getTime());
        jLabel7.setText(ultimoDia);

        modelo.addColumn("En soles");
        modelo.addColumn("Capital");
        modelo.addColumn("Capital Adicional");
        modelo.addColumn("Reservas");
        modelo.addColumn("Resultados Acumulados");
        modelo.addColumn("Total Patrimonio");

        modelo.addRow(new String[]{"Saldo al 01 de " + new DateFormatSymbols().getMonths()[mes.getMonth()] + " " + año.getYear()});
        modelo.addRow(new String[]{"Ajuste"});
        modelo.addRow(new String[]{"Resultado del periodo"});
        modelo.addRow(new String[]{"Total resultados Integrales del ejercicio"});
        modelo.addRow(new String[]{"Saldo al " + ultimoDia});

        tb_resultado.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                } else {
                    if (row == 0 || row == 3 || row == 4) {
                        c.setBackground(new Color(255, 230, 153));
                        c.setForeground(Color.BLACK);
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                    } else {
                        c.setBackground(table.getBackground());
                        c.setForeground(table.getForeground());
                    }
                }
                // Centrar columnas desde la columna 1 hasta la última
                if (column > 1) {
                    setHorizontalAlignment(JLabel.CENTER);
                }
                return c;
            }
        });

        // Crear un renderizador personalizado para las celdas
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Aplicar negrita y color específico a las filas especificadas
                if (row == 0 || row == 3 || row == 4) {
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                    c.setBackground(new Color(255, 230, 153));
                } else {
                    c.setBackground(table.getBackground());
                    c.setForeground(table.getForeground());
                }

                return c;
            }
        };

        // Aplicar el renderizador personalizado a la columna 0
        tb_resultado.getColumnModel().getColumn(0).setCellRenderer(renderer);

        DefaultTableCellRenderer renderer1 = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Aplicar negrita y color específico a la última columna
                if (row == 0 || row == 3 || row == 4) {
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                    c.setBackground(new Color(255, 230, 153));
                } else {
                    c.setBackground(table.getBackground());
                    c.setForeground(table.getForeground());
                }

                if (column == table.getColumnCount() - 1) { // Última columna
                    c.setFont(c.getFont().deriveFont(Font.BOLD));
                    setHorizontalAlignment(JLabel.CENTER);
                } else {
                    c.setFont(c.getFont().deriveFont(Font.PLAIN));
                }

                return c;
            }
        };

        // Aplicar el renderizador personalizado a la última columna
        tb_resultado.getColumnModel().getColumn(tb_resultado.getColumnCount() - 1).setCellRenderer(renderer1); // Última columna

        año.addPropertyChangeListener("year", (java.beans.PropertyChangeEvent evt) -> {
            modelo.setRowCount(0);
            modelo.setColumnCount(0);

            tb_resultado.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

            calendar = Calendar.getInstance();

            calendar.set(Calendar.YEAR, año.getYear());
            calendar.set(Calendar.MONTH, mes.getMonth());
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

            ultimoDia = dateFormat.format(calendar.getTime());
            jLabel7.setText(ultimoDia);

            modelo.addColumn("En soles");
            modelo.addColumn("Capital");
            modelo.addColumn("Capital Adicional");
            modelo.addColumn("Reservas");
            modelo.addColumn("Resultados Acumulados");
            modelo.addColumn("Total Patrimonio");

            modelo.addRow(new String[]{"Saldo al 01 de " + new DateFormatSymbols().getMonths()[mes.getMonth()] + " " + año.getYear()});
            modelo.addRow(new String[]{"Ajuste"});
            modelo.addRow(new String[]{"Resultado del periodo"});
            modelo.addRow(new String[]{"Total resultados Integrales del ejercicio"});
            modelo.addRow(new String[]{"Saldo al " + ultimoDia});

            // Notificar a la tabla que los datos han cambiado
            tb_resultado.tableChanged(new TableModelEvent(modelo));

            tb_resultado.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (isSelected) {
                        c.setBackground(table.getSelectionBackground());
                    } else {
                        if (row == 0 || row == 3 || row == 4) {
                            c.setBackground(new Color(255, 230, 153));
                            c.setForeground(Color.BLACK);
                            c.setFont(c.getFont().deriveFont(Font.BOLD));
                        } else {
                            c.setBackground(table.getBackground());
                            c.setForeground(table.getForeground());
                        }
                    }
                    // Centrar columnas desde la columna 1 hasta la última
                    if (column > 1) {
                        setHorizontalAlignment(JLabel.CENTER);
                    }
                    return c;
                }
            });

            // Crear un renderizador personalizado para las celdas
            DefaultTableCellRenderer r1 = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    // Aplicar negrita y color específico a las filas especificadas
                    if (row == 0 || row == 3 || row == 4) {
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                        c.setBackground(new Color(255, 230, 153));
                    } else {
                        c.setBackground(table.getBackground());
                        c.setForeground(table.getForeground());
                    }

                    return c;
                }
            };

            // Aplicar el renderizador personalizado a la columna 0
            tb_resultado.getColumnModel().getColumn(0).setCellRenderer(r1);

            DefaultTableCellRenderer r2 = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    // Aplicar negrita y color específico a la última columna
                    if (row == 0 || row == 3 || row == 4) {
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                        c.setBackground(new Color(255, 230, 153));
                    } else {
                        c.setBackground(table.getBackground());
                        c.setForeground(table.getForeground());
                    }

                    if (column == table.getColumnCount() - 1) { // Última columna
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                        setHorizontalAlignment(JLabel.CENTER);
                    } else {
                        c.setFont(c.getFont().deriveFont(Font.PLAIN));
                    }

                    return c;
                }
            };

            // Aplicar el renderizador personalizado a la última columna
            tb_resultado.getColumnModel().getColumn(tb_resultado.getColumnCount() - 1).setCellRenderer(r2); // Última columna
        });

        mes.addPropertyChangeListener("month", (java.beans.PropertyChangeEvent evt) -> {
            modelo.setRowCount(0);
            modelo.setColumnCount(0);

            tb_resultado.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

            calendar = Calendar.getInstance();

            calendar.set(Calendar.YEAR, año.getYear());
            calendar.set(Calendar.MONTH, mes.getMonth());
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

            ultimoDia = dateFormat.format(calendar.getTime());
            jLabel7.setText(ultimoDia);

            modelo.addColumn("En soles");
            modelo.addColumn("Capital");
            modelo.addColumn("Capital Adicional");
            modelo.addColumn("Reservas");
            modelo.addColumn("Resultados Acumulados");
            modelo.addColumn("Total Patrimonio");

            modelo.addRow(new String[]{"Saldo al 01 de " + new DateFormatSymbols().getMonths()[mes.getMonth()] + " " + año.getYear()});
            modelo.addRow(new String[]{"Ajuste"});
            modelo.addRow(new String[]{"Resultado del periodo"});
            modelo.addRow(new String[]{"Total resultados Integrales del ejercicio"});
            modelo.addRow(new String[]{"Saldo al " + ultimoDia});

            tb_resultado.tableChanged(new TableModelEvent(modelo));

            tb_resultado.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (isSelected) {
                        c.setBackground(table.getSelectionBackground());
                    } else {
                        if (row == 0 || row == 3 || row == 4) {
                            c.setBackground(new Color(255, 230, 153));
                            c.setForeground(Color.BLACK);
                            c.setFont(c.getFont().deriveFont(Font.BOLD));
                        } else {
                            c.setBackground(table.getBackground());
                            c.setForeground(table.getForeground());
                        }
                    }
                    // Centrar columnas desde la columna 1 hasta la última
                    if (column > 1) {
                        setHorizontalAlignment(JLabel.CENTER);
                    }
                    return c;
                }
            });

            // Crear un renderizador personalizado para las celdas
            DefaultTableCellRenderer r3 = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    // Aplicar negrita y color específico a las filas especificadas
                    if (row == 0 || row == 3 || row == 4) {
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                        c.setBackground(new Color(255, 230, 153));
                    } else {
                        c.setBackground(table.getBackground());
                        c.setForeground(table.getForeground());
                    }

                    return c;
                }
            };

            // Aplicar el renderizador personalizado a la columna 0
            tb_resultado.getColumnModel().getColumn(0).setCellRenderer(r3);

            DefaultTableCellRenderer r4 = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    // Aplicar negrita y color específico a la última columna
                    if (row == 0 || row == 3 || row == 4) {
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                        c.setBackground(new Color(255, 230, 153));
                    } else {
                        c.setBackground(table.getBackground());
                        c.setForeground(table.getForeground());
                    }

                    if (column == table.getColumnCount() - 1) { // Última columna
                        c.setFont(c.getFont().deriveFont(Font.BOLD));
                        setHorizontalAlignment(JLabel.CENTER);
                    } else {
                        c.setFont(c.getFont().deriveFont(Font.PLAIN));
                    }

                    return c;
                }
            };

            // Aplicar el renderizador personalizado a la última columna
            tb_resultado.getColumnModel().getColumn(tb_resultado.getColumnCount() - 1).setCellRenderer(r4); // Última columna
        });
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

    public void datos_principal() {
        DecimalFormat df = new DecimalFormat("###,###,##0.00");
        try {
            Connection con = Conexion.getConnection();
            Statement st = con.createStatement();
            String sql = "SELECT Cuenta, [Haber Anterior] - [Debe Anterior] AS T1,  [Haber Mes (sumar)] - [Debe Mes (restando)] AS T2 FROM patrimonio WHERE Cuenta IN (501111, 522211, 589111) AND Año = " + año.getYear() + " AND Mes = " + (mes.getMonth()+1) + " ";
            
            ResultSet rs = st.executeQuery(sql);
           
            while (rs.next()) {
                
                if (rs.getString(1).equals("501111")) {
                    modelo.setValueAt(df.format(rs.getDouble(2)), 0, 1);
                    modelo.setValueAt(df.format(rs.getDouble(3)), 1, 1);
                }
                if (rs.getString(1).equals("522211")) {
                    modelo.setValueAt(df.format(rs.getDouble(2)), 0, 2);
                    modelo.setValueAt(df.format(rs.getDouble(3)), 1, 2);
                }
                if (rs.getString(1).equals("589111")) {
                    modelo.setValueAt(df.format(rs.getDouble(2)), 0, 3);
                    modelo.setValueAt(df.format(rs.getDouble(3)), 1, 3);
                }
            }
            
            sql = "SELECT Cuenta, [Debe Anterior], [Haber Anterior], [Debe Mes (restando)], [Haber Mes (sumar)] FROM patrimonio WHERE Cuenta IN (591111, 591211, 592211, 592111) AND Año = " + año.getYear() + " AND Mes = " + (mes.getMonth()+1) + " ";
            rs = st.executeQuery(sql);
            Double valor1 = 0.0, valor2 = 0.0, total1, total2, valor3 = 0.0, valor4 = 0.0;
            while (rs.next()) {
                valor1 = valor1 + rs.getDouble(2);
                valor2 = valor2 + rs.getDouble(3);
                valor3 = valor3 + rs.getDouble(4);
                valor4 = valor4 + rs.getDouble(5);
            }
            
            total1 = valor2 - valor1;
            total2 = valor4 - valor3;
            
            modelo.setValueAt(df.format(total1), 0, 4);
            modelo.setValueAt(df.format(total2), 1, 4);
            
            sql = "SELECT SUM(Activo) - SUM(Pasivo) AS [Resultado del periodo] FROM patrimonio WHERE Año = " + año.getYear() + " AND Mes = " + (mes.getMonth()+1) + " ";
            rs = st.executeQuery(sql);
            if (rs.next()) {
                modelo.setValueAt(df.format(rs.getDouble(1)), 2, 4);
            }
            
            rs.close();
            st.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(Flujo_Efectivo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void datos_adicionales() {
        DecimalFormat df = new DecimalFormat("###,###,##0.00");
        Object value1 = tb_resultado.getValueAt(1, 4);
        Object value2 = tb_resultado.getValueAt(2, 4);
        if (value1 != null && value2 != null) {
            double sumavalor = Double.parseDouble(value1.toString().replaceAll(",", "")) + Double.parseDouble(value2.toString().replaceAll(",", ""));
            tb_resultado.setValueAt(df.format(sumavalor), 3, 4);
        }
        
        for (int j = 1; j <= 4; j++) {
            Object v1 = tb_resultado.getValueAt(0, j);
            Object v2 = tb_resultado.getValueAt(1, j);
            Object v3 = tb_resultado.getValueAt(2, j);

            // Reemplazar valores nulos o en blanco con 0.0
            double valor1 = (v1 != null && !v1.toString().isEmpty()) ? Double.parseDouble(v1.toString().replaceAll(",", "")) : 0.0;
            double valor2 = (v2 != null && !v2.toString().isEmpty()) ? Double.parseDouble(v2.toString().replaceAll(",", "")) : 0.0;
            double valor3 = (v3 != null && !v3.toString().isEmpty()) ? Double.parseDouble(v3.toString().replaceAll(",", "")) : 0.0;

            // Calcular la suma de los valores
            double sumavalor = valor1 + valor2 + valor3;

            // Establecer el valor en la celda correspondiente
            tb_resultado.setValueAt(df.format(sumavalor), 4, j);
        }

    }

    private void datos_sumarTotalizado() {
        DecimalFormat df = new DecimalFormat("###,###,##0.00");
        for (int i = 0; i < tb_resultado.getRowCount(); i++) {
            
                double sumaFila = 0;
                for (int j = 0; j < tb_resultado.getColumnCount() - 1; j++) {
                    Object value = tb_resultado.getValueAt(i, j);
                    double valor = 0;
                    if (value != null) {
                        try {
                            valor = Double.parseDouble(value.toString().replaceAll(",", ""));
                        } catch (NumberFormatException e) {
                            // Si no se puede parsear a double, se considera como 0
                        }
                    }
                    sumaFila += valor;
                }
                tb_resultado.setValueAt(df.format(sumaFila), i, tb_resultado.getColumnCount() - 1);
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
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        btn_mensual = new javax.swing.JButton();
        btn_cargar = new javax.swing.JButton();
        año = new com.toedter.calendar.JYearChooser();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        mes = new com.toedter.calendar.JMonthChooser();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        AgregarNuevaCuenta = new javax.swing.JMenuItem();

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
        setTitle("Estado de Cambio en el Patrimonio Neto");

        jPanel6.setBackground(new java.awt.Color(0, 102, 153));

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Estado de Cambio en el Patrimonio Neto");
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

        jLabel6.setFont(new java.awt.Font("Bahnschrift", 0, 13)); // NOI18N
        jLabel6.setText("Estado de Cambio en el Patrimonio Neto");

        jLabel7.setFont(new java.awt.Font("Bahnschrift", 0, 13)); // NOI18N
        jLabel7.setText("Al 31 de Abril 2024");

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true), "Opciones", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 13))); // NOI18N

        btn_mensual.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btn_mensual.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/File.png"))); // NOI18N
        btn_mensual.setText("Imprimir Reporte Mensual");
        btn_mensual.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btn_mensual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_mensualActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_mensual)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_mensual)
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

        jLabel8.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel8.setText("Mes:");

        mes.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 437, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(año, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8)
                        .addGap(10, 10, 10)
                        .addComponent(mes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_cargar))
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING))
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
                        .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(año, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_cargar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(mes, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 545, Short.MAX_VALUE)
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
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 1164, Short.MAX_VALUE)
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

        AgregarNuevaCuenta.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        AgregarNuevaCuenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/sobresalir.png"))); // NOI18N
        AgregarNuevaCuenta.setText("N° de Cuenta");
        AgregarNuevaCuenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AgregarNuevaCuentaActionPerformed(evt);
            }
        });
        jMenu1.add(AgregarNuevaCuenta);

        jMenuBar1.add(jMenu1);

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
        datos_principal();
        datos_adicionales();
        datos_sumarTotalizado();
        packColumns(tb_resultado);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }//GEN-LAST:event_btn_cargarActionPerformed

    private void btn_mensualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_mensualActionPerformed
        int month = mes.getMonth();
        int year = año.getYear();

        if (tb_resultado.getValueAt(4, 5) == null) {
            JOptionPane.showMessageDialog(this, "El valor final de Total de Patrimonio se encuentra vacio, verifique que se haya cargado todos los datos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Connection con = Conexion.getConnection();
            btn_mensual.setEnabled(false);
            setCursor(new Cursor(Cursor.WAIT_CURSOR));

            // Obtener el último día del mes
            YearMonth yearMonth = YearMonth.of(year, (month + 1));
            LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();

            // Formatear la salida
            String resultado = "Al " + lastDayOfMonth.getDayOfMonth() + " de " + lastDayOfMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + lastDayOfMonth.getYear();

            
            LocalDate primerDiaDelMes = LocalDate.of(year, (month+1), 1);

            String resultado2 = "Saldo al 01 de " + primerDiaDelMes.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " de " + primerDiaDelMes.getYear();

            Map parameters = new HashMap();

            parameters.put("fechaFin", resultado);
            parameters.put("M1", resultado2);
            
            int c = 2;
            
            for (int i = 0; i < tb_resultado.getRowCount(); i++) {
                for (int j = 1; j < tb_resultado.getColumnCount(); j++) {
                    if (tb_resultado.getValueAt(i, j) == null) {
                        parameters.put("M"+c, "");
                    } else {
                        parameters.put("M"+c, tb_resultado.getValueAt(i, j));
                    }
                    
                    c++;
                }
            }

            JasperReport report = JasperCompileManager.compileReport(System.getProperty("user.dir") + "/reportes/FPatrimonioAcmp.jrxml");
            JasperPrint print = JasperFillManager.fillReport(report, parameters, con);
            JasperViewer viewer = new JasperViewer(print, false);

            JPanel panel = new JPanel(new BorderLayout());
            panel.add(viewer.getContentPane(), BorderLayout.CENTER);

            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Rectangle bounds = env.getMaximumWindowBounds();

            JDialog dialog = new JDialog();
            dialog.setTitle("Estado de Patrimonio - Mensual");
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setModal(true);
            dialog.setSize(bounds.width, bounds.height);
            dialog.setLocationRelativeTo(null);
            dialog.getContentPane().add(panel);
            btn_mensual.setEnabled(true);
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            dialog.setVisible(true);

        } catch (SQLException | JRException ex) {
            Logger.getLogger(Flujo_Efectivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_mensualActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        CargaArchivos ini = new CargaArchivos(this, true);
        ini.toFront();
        ini.setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void tb_resultadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_resultadoMouseClicked
        if (evt.getClickCount() == 2) {
            int fila = tb_resultado.getSelectedRow();
            int columna = tb_resultado.getSelectedColumn();
            if (tb_resultado.getValueAt(fila, columna) == null || tb_resultado.getValueAt(fila, columna).toString().equals("") || tb_resultado.getValueAt(fila, columna).toString().isEmpty()) {
                //JOptionPane.showMessageDialog(this, "Este campo no contiene informacion detallada para mostrar", "Mensaje", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String concepto = tb_resultado.getValueAt(fila, 0).toString();
            
            if ((fila == 0 || fila == 1) && (columna == 1)) {
                int columnasumar = 0;
                if (fila == 0) {
                    columnasumar = 3;
                }else if (fila == 1){
                    columnasumar = 4;
                }
                Detallado ini = new Detallado(this, true);
                ini.cargarDatosPatrimonio(año.getYear(), (mes.getMonth() + 1), "capital", "501111", concepto, columnasumar);
                ini.toFront();
                ini.setVisible(true);
            }

            if ((fila == 0 || fila == 1) && (columna == 2)) {
                int columnasumar = 0;
                if (fila == 0) {
                    columnasumar = 3;
                }else if (fila == 1){
                    columnasumar = 4;
                }
                Detallado ini = new Detallado(this, true);
                ini.cargarDatosPatrimonio(año.getYear(), (mes.getMonth() + 1), "capital", "522211", concepto, columnasumar);
                ini.toFront();
                ini.setVisible(true);
            }

            if ((fila == 0 || fila == 1) && (columna == 3)) {
                int columnasumar = 0;
                if (fila == 0) {
                    columnasumar = 3;
                }else if (fila == 1){
                    columnasumar = 4;
                }
                Detallado ini = new Detallado(this, true);
                ini.cargarDatosPatrimonio(año.getYear(), (mes.getMonth() + 1), "capital", "589111", concepto, columnasumar);
                ini.toFront();
                ini.setVisible(true);
            }

            
        }
    }//GEN-LAST:event_tb_resultadoMouseClicked

    private void AgregarNuevaCuentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AgregarNuevaCuentaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AgregarNuevaCuentaActionPerformed

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
                new Estado_Patrimonio().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem AgregarNuevaCuenta;
    private com.toedter.calendar.JYearChooser año;
    private javax.swing.JButton btn_cargar;
    private javax.swing.JButton btn_mensual;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private com.toedter.calendar.JMonthChooser mes;
    private javax.swing.JTable tb_EgresosTemporal;
    private javax.swing.JTable tb_IngresosTemporal;
    private javax.swing.JTable tb_cuenta33;
    private javax.swing.JTable tb_resultado;
    // End of variables declaration//GEN-END:variables
}
