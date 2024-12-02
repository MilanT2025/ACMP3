/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista;

import Controlador.Conexion;
import static Controlador.FuncionesGlobales.getLastDirectory;
import static Controlador.FuncionesGlobales.saveLastDirectory;
import java.awt.Cursor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Farmacia-Ingeniero
 */
public class CargaArchivos extends javax.swing.JDialog {

    /**
     * Creates new form CargaArchivos
     */
    public CargaArchivos(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);

        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaMesAnterior = fechaActual.minusMonths(1);
        mes.setMonth(fechaMesAnterior.getMonthValue() - 1);
        año.setYear(fechaMesAnterior.getYear());
    }

    private void limpiarCampos() {
        cb_tipo.setSelectedIndex(0);
        txt_ruta.setText("");
        jProgressBar1.setValue(0);
    }

    private void limpiarCampos2() {
        cb_tipo2.setSelectedIndex(0);
        jProgressBar2.setValue(0);
    }

    private void cargarIngreso(String filePath) {
        FileInputStream fis = null;
        try {

            fis = new FileInputStream(new File(filePath));
            {

                Workbook workbook = new XSSFWorkbook(fis);

                Sheet sheet = workbook.getSheetAt(0);
                int rowCount = sheet.getLastRowNum();

                int year = año.getYear();
                int month = (mes.getMonth() + 1);

                Connection con = Conexion.getConnection();

                String sql = "delete from ingreso where año = ? and mes = ?";
                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setInt(1, year);
                pstm.setInt(2, month);
                pstm.executeUpdate();

                sql = "INSERT INTO [dbo].[ingreso] VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                pstm = con.prepareStatement(sql);

                for (int i = 1; i <= rowCount; i++) {
                    Row row = sheet.getRow(i);
                    Cell firstCell = row.getCell(0);

                    if (firstCell != null && firstCell.getCellType() == CellType.STRING && "INGRESO".equalsIgnoreCase(firstCell.getStringCellValue())) {
                        pstm.setInt(1, year);
                        pstm.setInt(2, month);

                        for (int j = 0; j < 23; j++) {
                            Cell cell = row.getCell(j);
                            if (cell != null) {
                                switch (cell.getCellType()) {
                                    case NUMERIC:
                                        pstm.setDouble(j + 3, cell.getNumericCellValue());
                                        break;
                                    case STRING:
                                        pstm.setString(j + 3, cell.getStringCellValue());
                                        break;
                                    default:
                                        pstm.setString(j + 3, null);
                                }
                            } else {
                                pstm.setString(j + 3, null);
                            }
                        }
                        pstm.addBatch();
                    }

                }
                pstm.executeBatch();

                sql = "delete from ingreso where año = ? and mes = ? AND Movimiento IS NULL";
                pstm = con.prepareStatement(sql);
                pstm.setInt(1, year);
                pstm.setInt(2, month);
                pstm.executeUpdate();

                pstm.close();
                con.close();

            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(CargaArchivos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | SQLException ex) {
            Logger.getLogger(CargaArchivos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(CargaArchivos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void cargarEgresos(String filePath) {
        FileInputStream fis = null;
        try {

            fis = new FileInputStream(new File(filePath));
            {

                Workbook workbook = new XSSFWorkbook(fis);

                Sheet sheet = workbook.getSheetAt(0);
                int rowCount = sheet.getLastRowNum();

                int year = año.getYear();
                int month = (mes.getMonth() + 1);

                Connection con = Conexion.getConnection();

                String sql = "delete from egreso where año = ? and mes = ?";
                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setInt(1, year);
                pstm.setInt(2, month);
                pstm.executeUpdate();

                sql = "INSERT INTO [dbo].[egreso] VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                pstm = con.prepareStatement(sql);

                for (int i = 1; i <= rowCount; i++) {
                    Row row = sheet.getRow(i);
                    Cell firstCell = row.getCell(0);

                    if (firstCell != null && firstCell.getCellType() == CellType.STRING && "EGRESO".equalsIgnoreCase(firstCell.getStringCellValue())) {
                        pstm.setInt(1, year);
                        pstm.setInt(2, month);

                        for (int j = 0; j < 23; j++) {
                            Cell cell = row.getCell(j);
                            if (cell != null) {
                                switch (cell.getCellType()) {
                                    case NUMERIC:
                                        pstm.setDouble(j + 3, cell.getNumericCellValue());
                                        break;
                                    case STRING:
                                        pstm.setString(j + 3, cell.getStringCellValue());
                                        break;
                                    default:
                                        pstm.setString(j + 3, null);
                                }
                            } else {
                                pstm.setString(j + 3, null);
                            }
                        }
                        pstm.addBatch();
                    }

                    int progress = (int) ((double) i / rowCount * 100);
                    SwingUtilities.invokeLater(() -> jProgressBar1.setValue(progress));
                }
                pstm.executeBatch();

                sql = "delete from egreso where año = ? and mes = ? AND Movimiento IS NULL";
                pstm = con.prepareStatement(sql);
                pstm.setInt(1, year);
                pstm.setInt(2, month);
                pstm.executeUpdate();

                pstm.close();
                con.close();

                JOptionPane.showMessageDialog(this, "El EXCEL DE " + cb_tipo.getSelectedItem().toString().toUpperCase() + " ha sido cargado correctamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);

            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(CargaArchivos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | SQLException ex) {
            Logger.getLogger(CargaArchivos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(CargaArchivos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void cargarCuenta33(String filePath) {
        FileInputStream fis = null;
        try {

            fis = new FileInputStream(new File(filePath));
            {

                Workbook workbook = new XSSFWorkbook(fis);

                Sheet sheet = workbook.getSheetAt(0);
                int rowCount = sheet.getLastRowNum();

                int year = año.getYear();
                int month = (mes.getMonth() + 1);

                Connection con = Conexion.getConnection();

                String sql = "delete from cuenta33 where año = ? and mes = ?";
                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setInt(1, year);
                pstm.setInt(2, month);
                pstm.executeUpdate();

                sql = "INSERT INTO [dbo].[cuenta33] VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                pstm = con.prepareStatement(sql);

                for (int i = 1; i <= rowCount; i++) {
                    Row row = sheet.getRow(i);
                    pstm.setInt(1, year);
                    pstm.setInt(2, month);

                    for (int j = 0; j < 22; j++) {
                        Cell cell = row.getCell(j);
                        if (cell != null) {
                            switch (cell.getCellType()) {
                                case NUMERIC:
                                    pstm.setDouble(j + 3, cell.getNumericCellValue());
                                    break;
                                case STRING:
                                    pstm.setString(j + 3, cell.getStringCellValue());
                                    break;
                                default:
                                    pstm.setString(j + 3, null);
                            }
                        } else {
                            pstm.setString(j + 3, null);
                        }

                    }
                    pstm.addBatch();

                    int progress = (int) ((double) i / rowCount * 100);
                    SwingUtilities.invokeLater(() -> jProgressBar1.setValue(progress));
                }
                pstm.executeBatch();

                sql = "delete from cuenta33 where año = ? and mes = ? AND Periodo IS NULL";
                pstm = con.prepareStatement(sql);
                pstm.setInt(1, year);
                pstm.setInt(2, month);
                pstm.executeUpdate();

                pstm.close();
                con.close();

                JOptionPane.showMessageDialog(this, "El EXCEL DE " + cb_tipo.getSelectedItem().toString().toUpperCase() + " ha sido cargado correctamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);

            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(CargaArchivos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | SQLException ex) {
            Logger.getLogger(CargaArchivos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(CargaArchivos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void cargarHojaBalance(String filePath) {
        FileInputStream fis = null;
        try {

            fis = new FileInputStream(new File(filePath));
            {

                Workbook workbook = new XSSFWorkbook(fis);

                Sheet sheet = workbook.getSheetAt(0);
                int rowCount = sheet.getLastRowNum();

                int year = año.getYear();
                int month = (mes.getMonth() + 1);

                Connection con = Conexion.getConnection();

                String sql = "delete from patrimonio where año = ? and mes = ?";
                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setInt(1, year);
                pstm.setInt(2, month);
                pstm.executeUpdate();

                sql = "INSERT INTO [dbo].[patrimonio] VALUES (?, ?, ?, ?, ?, ?,	?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                pstm = con.prepareStatement(sql);

                for (int i = 1; i <= rowCount; i++) {
                    Row row = sheet.getRow(i);
                    pstm.setInt(1, year);
                    pstm.setInt(2, month);

                    for (int j = 0; j < 16; j++) {
                        Cell cell = row.getCell(j);
                        if (cell != null) {
                            switch (cell.getCellType()) {
                                case NUMERIC:
                                    pstm.setDouble(j + 3, cell.getNumericCellValue());
                                    break;
                                case STRING:
                                    pstm.setString(j + 3, cell.getStringCellValue());
                                    break;
                                default:
                                    pstm.setString(j + 3, null);
                            }
                        } else {
                            pstm.setString(j + 3, null);
                        }

                    }
                    pstm.addBatch();

                    int progress = (int) ((double) i / rowCount * 100);
                    SwingUtilities.invokeLater(() -> jProgressBar1.setValue(progress));
                }
                pstm.executeBatch();

                sql = "delete from patrimonio where año = ? and mes = ? AND Cuenta IS NULL";
                pstm = con.prepareStatement(sql);
                pstm.setInt(1, year);
                pstm.setInt(2, month);
                pstm.executeUpdate();

                pstm.close();
                con.close();

                JOptionPane.showMessageDialog(this, "El EXCEL DE " + cb_tipo.getSelectedItem().toString().toUpperCase() + " ha sido cargado correctamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);

            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(CargaArchivos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | SQLException ex) {
            Logger.getLogger(CargaArchivos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(CargaArchivos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void cargarGlosaEgresos(String filePath) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(filePath));
            {

                Workbook workbook = new XSSFWorkbook(fis);

                Sheet sheet = workbook.getSheetAt(0);
                int rowCount = sheet.getLastRowNum();

                int year = año.getYear();
                int month = (mes.getMonth() + 1);

                Connection con = Conexion.getConnection();

                String sql = "delete from glosaEgreso where año = ? and mes = ?";
                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setInt(1, year);
                pstm.setInt(2, month);
                pstm.executeUpdate();

                sql = "INSERT INTO [dbo].[glosaEgreso] VALUES (?, ?, ?, ?)";

                pstm = con.prepareStatement(sql);

                for (int i = 1; i <= rowCount; i++) {
                    Row row = sheet.getRow(i);
                    pstm.setInt(1, year);
                    pstm.setInt(2, month);

                    int[] columnas = {1, 11};

                    for (int k = 0; k < columnas.length; k++) {
                        int j = columnas[k];
                        Cell cell = row.getCell(j);
                        if (cell != null) {
                            switch (cell.getCellType()) {
                                case STRING:
                                    pstm.setString(k + 3, cell.getStringCellValue());
                                    break;
                                case NUMERIC:
                                    pstm.setString(k + 3, String.valueOf((int) cell.getNumericCellValue()));
                                    break;
                                default:
                                    pstm.setString(k + 3, null);
                                    break;
                            }
                        } else {
                            pstm.setString(k + 3, null);
                        }
                    }
                    pstm.addBatch();

                    int progress = (int) ((double) i / rowCount * 100);
                    SwingUtilities.invokeLater(() -> jProgressBar1.setValue(progress));
                }
                pstm.executeBatch();

                sql = "delete from glosaEgreso where año = ? and mes = ? AND (Numero IS NULL OR Numero = '')";
                pstm = con.prepareStatement(sql);
                pstm.setInt(1, year);
                pstm.setInt(2, month);
                pstm.executeUpdate();

                pstm.close();
                con.close();

                JOptionPane.showMessageDialog(this, "El EXCEL DE " + cb_tipo.getSelectedItem().toString().toUpperCase() + " ha sido cargado correctamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);

            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(CargaArchivos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | SQLException ex) {
            Logger.getLogger(CargaArchivos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(CargaArchivos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void eliminararchivo(String archivo) {
        try {
            int year = año2.getYear();
            int month = (mes2.getMonth() + 1);

            Connection con = Conexion.getConnection();

            String sql = "delete from " + archivo + " where año = ? and mes = ?";
            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setInt(1, year);
            pstm.setInt(2, month);
            pstm.executeUpdate();

            pstm.close();
            con.close();

            SwingUtilities.invokeLater(() -> jProgressBar2.setValue(100));

            JOptionPane.showMessageDialog(this, "El ARCHIVO DE " + cb_tipo2.getSelectedItem().toString().toUpperCase() + " ha sido ELIMINADO correctamente.", "Exito", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            Logger.getLogger(CargaArchivos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void setComponentsEnabled(boolean enabled) {
        btn_seleccionar.setEnabled(enabled);
        btn_subir.setEnabled(enabled);
        cb_tipo.setEnabled(enabled);
        año.setEnabled(enabled);
        mes.setEnabled(enabled);
    }

    private void setComponentsEnabled_delete(boolean enabled) {
        btn_eliminar.setEnabled(enabled);
        cb_tipo2.setEnabled(enabled);
        año2.setEnabled(enabled);
        mes2.setEnabled(enabled);
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        año = new com.toedter.calendar.JYearChooser();
        jLabel5 = new javax.swing.JLabel();
        mes = new com.toedter.calendar.JMonthChooser();
        txt_ruta = new javax.swing.JTextField();
        btn_seleccionar = new javax.swing.JButton();
        btn_subir = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        cb_tipo = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
        jProgressBar1 = new javax.swing.JProgressBar();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        año2 = new com.toedter.calendar.JYearChooser();
        jLabel8 = new javax.swing.JLabel();
        mes2 = new com.toedter.calendar.JMonthChooser();
        btn_eliminar = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        cb_tipo2 = new javax.swing.JComboBox<>();
        jSeparator2 = new javax.swing.JSeparator();
        jProgressBar2 = new javax.swing.JProgressBar();
        jPanel8 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel6.setBackground(new java.awt.Color(0, 102, 153));

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Carga de Archivos");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 950, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "Datos / Carga de Archivo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 13))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel4.setText("Año:");

        jLabel5.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel5.setText("Mes:");

        mes.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        mes.setForeground(new java.awt.Color(255, 0, 0));
        mes.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N

        txt_ruta.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        txt_ruta.setDisabledTextColor(new java.awt.Color(255, 0, 0));
        txt_ruta.setEnabled(false);

        btn_seleccionar.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btn_seleccionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_add_32px.png"))); // NOI18N
        btn_seleccionar.setText("Seleccionar Excel");
        btn_seleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_seleccionarActionPerformed(evt);
            }
        });

        btn_subir.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btn_subir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Upload to the Cloud_1.png"))); // NOI18N
        btn_subir.setText("Subir Excel a BD");
        btn_subir.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btn_subir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_subirActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel6.setText("Tipo de Archivo:");

        cb_tipo.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        cb_tipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<<Seleccionar>>", "Balance de Comprobacion (Hoja de Balance)", "Comprobante de Compra 33 (Cuenta 33)", "Movimientos de Ingresos y Egresos", "Movimientos de Egresos con Glosa" }));

        jProgressBar1.setStringPainted(true);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(btn_subir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(txt_ruta)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_seleccionar, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(año, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_tipo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(año, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mes, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cb_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_seleccionar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_ruta))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_subir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Carga", new javax.swing.ImageIcon(getClass().getResource("/images/Chevron Up.png")), jPanel2); // NOI18N

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true), "Datos / Carga de Archivo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 13))); // NOI18N

        jLabel7.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel7.setText("Año:");

        jLabel8.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel8.setText("Mes:");

        mes2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        mes2.setForeground(new java.awt.Color(255, 0, 0));
        mes2.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N

        btn_eliminar.setFont(new java.awt.Font("Roboto", 1, 12)); // NOI18N
        btn_eliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Delete Trash.png"))); // NOI18N
        btn_eliminar.setText("Eliminar Archivo de la BD");
        btn_eliminar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btn_eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_eliminarActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel9.setText("Tipo de Archivo a Borrar:");

        cb_tipo2.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        cb_tipo2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<<Seleccionar>>", "Balance de Comprobacion (Hoja de Balance)", "Comprobante de Compra 33 (Cuenta 33)", "Movimientos de Ingresos y Egresos", "Movimientos de Egresos con Glosa" }));

        jProgressBar2.setStringPainted(true);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2)
                    .addComponent(btn_eliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jProgressBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(año2, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(mes2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_tipo2, 0, 778, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(año2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mes2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cb_tipo2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_eliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel8.setBackground(new java.awt.Color(0, 102, 153));

        jLabel2.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText(" Borrar Archivos");
        jLabel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 950, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Borrar", new javax.swing.ImageIcon(getClass().getResource("/images/Chevron Down.png")), jPanel3); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_seleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_seleccionarActionPerformed
        jFileChooser1 = new JFileChooser();
        jFileChooser1.setDialogTitle("Elija el archivo de Excel");
        jFileChooser1.setFileFilter(new FileNameExtensionFilter("Archivos Excel", "xlsx"));
        File lastDirectory = getLastDirectory();

        if (lastDirectory != null && lastDirectory.exists()) {
            jFileChooser1.setCurrentDirectory(lastDirectory);
        }
        int result = jFileChooser1.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jFileChooser1.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();
            txt_ruta.setText(filePath);
            saveLastDirectory(selectedFile.getParentFile());
        }
    }//GEN-LAST:event_btn_seleccionarActionPerformed

    private void btn_subirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_subirActionPerformed
        if (cb_tipo.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione el Tipo de Archivo", "Mensaje", JOptionPane.ERROR_MESSAGE);
            cb_tipo.requestFocus();
            cb_tipo.showPopup();
            return;
        }

        if (txt_ruta.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un Archivo", "Mensaje", JOptionPane.ERROR_MESSAGE);
            return;
        }

        setComponentsEnabled(false);
        setCursor(new Cursor(Cursor.WAIT_CURSOR));

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {

                String tipoSeleccionado = cb_tipo.getSelectedItem().toString();
                String ruta = txt_ruta.getText();

                if (JOptionPane.showConfirmDialog(null, "<html>¿Estas seguro que deseas subir el <b><font color='red'>EXCEL DE " + tipoSeleccionado.toUpperCase() + "</font></b><br>correspondiente al <b><font color='red'>MES DE " + new DateFormatSymbols().getMonths()[mes.getMonth()].toUpperCase() + " DEL " + año.getYear() + "</font></b>?", "Confirmación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    switch (tipoSeleccionado) {
                        case "Movimientos de Ingresos y Egresos":
                            cargarIngreso(ruta);
                            cargarEgresos(ruta);
                            break;
                        case "Comprobante de Compra 33 (Cuenta 33)":
                            cargarCuenta33(ruta);
                            break;
                        case "Movimientos de Egresos con Glosa":
                            cargarGlosaEgresos(ruta);
                            break;
                        case "Balance de Comprobacion (Hoja de Balance)":
                            cargarHojaBalance(ruta);
                            break;
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                setComponentsEnabled(true);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                limpiarCampos();
            }
        };

        worker.execute();
    }//GEN-LAST:event_btn_subirActionPerformed

    private void btn_eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_eliminarActionPerformed
        if (cb_tipo2.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione el Tipo de Archivo", "Mensaje", JOptionPane.ERROR_MESSAGE);
            cb_tipo2.requestFocus();
            cb_tipo2.showPopup();
            return;
        }
        setComponentsEnabled(false);
        setCursor(new Cursor(Cursor.WAIT_CURSOR));

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                String tipoSeleccionado = cb_tipo2.getSelectedItem().toString();

                if (JOptionPane.showConfirmDialog(null, "<html>¿Estas seguro que deseas borrar el <b><font color='red'>ARCHIVO DE " + tipoSeleccionado.toUpperCase() + "</font></b><br>correspondiente al <b><font color='red'>MES DE " + new DateFormatSymbols().getMonths()[mes2.getMonth()].toUpperCase() + " DEL " + año2.getYear() + "</font></b>?<br><br><b>NOTA: Al Eliminar el Archivo se vera afectado todos los reportes que esten asociados a este</b></html>", "Confirmación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    switch (tipoSeleccionado) {
                        case "Movimientos de Ingresos y Egresos":
                            eliminararchivo("ingreso");
                            eliminararchivo("egreso");
                            break;
                        case "Comprobante de Compra 33 (Cuenta 33)":
                            eliminararchivo("cuenta33");
                            break;
                        case "Movimientos de Egresos con Glosa":
                            eliminararchivo("glosaEgreso");
                            break;
                        case "Balance de Comprobacion (Hoja de Balance)":
                            eliminararchivo("patrimonio");
                            break;
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                setComponentsEnabled(true);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                limpiarCampos();
            }
        };

        worker.execute();
    }//GEN-LAST:event_btn_eliminarActionPerformed

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
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CargaArchivos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CargaArchivos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CargaArchivos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CargaArchivos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CargaArchivos dialog = new CargaArchivos(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JYearChooser año;
    private com.toedter.calendar.JYearChooser año2;
    private javax.swing.JButton btn_eliminar;
    private javax.swing.JButton btn_seleccionar;
    private javax.swing.JButton btn_subir;
    private javax.swing.JComboBox<String> cb_tipo;
    private javax.swing.JComboBox<String> cb_tipo2;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JProgressBar jProgressBar2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private com.toedter.calendar.JMonthChooser mes;
    private com.toedter.calendar.JMonthChooser mes2;
    private javax.swing.JTextField txt_ruta;
    // End of variables declaration//GEN-END:variables
}
