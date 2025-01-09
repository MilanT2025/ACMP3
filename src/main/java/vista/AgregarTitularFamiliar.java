/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package vista;

import Controlador.Conexion;
import Controlador.Options;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Farmacia-Ingeniero
 */
public class AgregarTitularFamiliar extends javax.swing.JDialog {
    private final Modelo modelo = new Modelo();

    private void llenar_tabla() {
        String[] columnNames = new String[]{
            "PARENTESCO",
            "DNI",
            "APELLIDOS Y NOMBRES",
            "MONTO"
        };

        modelo.setColumnIdentifiers(columnNames);

        JTableHeader header = tbfamiliares.getTableHeader();
        header.setPreferredSize(new java.awt.Dimension(header.getWidth(), 40));

        header.setBackground(new java.awt.Color(255, 217, 102));
        tbfamiliares.getTableHeader().setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 12));

        TableColumnModel columnModel = tbfamiliares.getColumnModel();
        for (int col = 0; col < tbfamiliares.getColumnCount(); col++) {
            TableColumn tableColumn = columnModel.getColumn(col);
            Component comp = header.getDefaultRenderer().getTableCellRendererComponent(tbfamiliares, tableColumn.getHeaderValue(), false, false, 0, col);
            int width = comp.getPreferredSize().width + 25;
            tableColumn.setPreferredWidth(width);
        }
    }

    private void guardarFamiliar() {
        try {
            Connection con = Conexion.getConnection();
            String query = "INSERT INTO Familiar (IdPersonal, TipoFamiliar, DniFamiliar, Nomape, Monto) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstm = con.prepareStatement(query);
            pstm.setInt(1, Integer.parseInt(((Options) cbnomape.getSelectedItem()).getValue()));
            pstm.setString(2, cbtipo.getSelectedItem().toString());
            pstm.setString(3, txtdni.getText());
            pstm.setString(4, txtnomape.getText());
            pstm.setDouble(5, Double.parseDouble(txtmonto.getText()));
            int filasAfectadas = pstm.executeUpdate();
          
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(this, "Se ha guardado correctamente el registro", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                // Opcional: Actualiza la tabla después de la eliminación
                cargarTitularFamiliar();
                cargartotal();
            } else {
                JOptionPane.showMessageDialog(this, "Ha ocurrido un error al guardar elregistro", "Error", JOptionPane.ERROR_MESSAGE);
            }


            pstm.close();
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(AgregarTitularFamiliar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void limpiarCampos() {
        cbtipo.setSelectedIndex(0);
        txtotros.setText("");
        txtdni.setText("");
        txtnomape.setText("");
        txtmonto.setText("");
    }

    private void actualizarMontoCopere() {
       try {
            Connection con = Conexion.getConnection();
            String query = "UPDATE EstructuraCopere SET Monto_Descuento = ? WHERE Numero_CIP = (SELECT NroCip FROM Personal WHERE IdPersonal = ?) AND Estado = 1";
            PreparedStatement pstm = con.prepareStatement(query);
            pstm.setDouble(1, totalFinal);
            pstm.setDouble(2, Integer.parseInt(((Options) cbnomape.getSelectedItem()).getValue()));
            
            int filasAfectadas = pstm.executeUpdate();
          
            if (filasAfectadas > 0) {
                CargaCopere.jButton1.doClick();
                JOptionPane.showMessageDialog(this, "Se ha actualizado el monto a pagar del Aportante", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Ha Ocurrido un Error al Actualizar el Monto a Pagar", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            pstm.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Manejo de excepciones
        }
    }

    private void actualizarMontoCaja() {
        try {
            Connection con = Conexion.getConnection();
            String query = "UPDATE EstructuraCajaPensiones SET Monto_Descuento = ? WHERE Documento = (SELECT DNI FROM Personal WHERE IdPersonal = ?) AND Estado = 1";
            PreparedStatement pstm = con.prepareStatement(query);
            pstm.setDouble(1, totalFinal);
            pstm.setDouble(2, Integer.parseInt(((Options) cbnomape.getSelectedItem()).getValue()));
            
            int filasAfectadas = pstm.executeUpdate();
          
            if (filasAfectadas > 0) {
                CargaCopere.jButton1.doClick();
                JOptionPane.showMessageDialog(this, "Se ha actualizado el monto a pagar del Aportante", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Ha Ocurrido un Error al Actualizar el Monto a Pagar", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            pstm.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Manejo de excepciones
        }
    }

    private void actualizarMontoOprefa() {
        try {
            Connection con = Conexion.getConnection();
            String query = "UPDATE EstructuraOprefa SET Monto_Descuento = ? WHERE DNI = (SELECT DNI FROM Personal WHERE IdPersonal = ?) AND Estado = 1";
            PreparedStatement pstm = con.prepareStatement(query);
            pstm.setDouble(1, totalFinal);
            pstm.setDouble(2, Integer.parseInt(((Options) cbnomape.getSelectedItem()).getValue()));
            
            int filasAfectadas = pstm.executeUpdate();
          
            if (filasAfectadas > 0) {
                CargaCopere.jButton1.doClick();
                JOptionPane.showMessageDialog(this, "Se ha actualizado el monto a pagar del Aportante", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Ha Ocurrido un Error al Actualizar el Monto a Pagar", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            pstm.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Manejo de excepciones
        }
    }

    public class Modelo extends DefaultTableModel {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }
    
    private void eliminarRegistro() {
        // Verifica si hay una fila seleccionada
        int filaSeleccionada = tbfamiliares.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un registro para eliminar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String dnifamiliar = tbfamiliares.getValueAt(filaSeleccionada, 1).toString();

        // Muestra un mensaje de confirmación
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que deseas eliminar este registro?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        // Si el usuario confirma, procede con la eliminación
        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                Connection con = Conexion.getConnection();
                String query = "DELETE FROM Familiar WHERE DniFamiliar = ? AND IdPersonal = ?";
                PreparedStatement pstm = con.prepareStatement(query);
                pstm.setString(1, dnifamiliar);
                pstm.setInt(2, Integer.parseInt(((Options) cbnomape.getSelectedItem()).getValue()));
                int filasAfectadas = pstm.executeUpdate();

                if (filasAfectadas > 0) {
                    JOptionPane.showMessageDialog(this, "Registro eliminado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    // Opcional: Actualiza la tabla después de la eliminación
                    cargarTitularFamiliar();
                    cargartotal();
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró el registro a eliminar", "Error", JOptionPane.ERROR_MESSAGE);
                }

                pstm.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(AgregarTitularFamiliar.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Error al eliminar el registro", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Creates new form ReporteContable
     */
    public AgregarTitularFamiliar(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        jLabel6.setVisible(false);
        txtotros.setVisible(false);
        llenar_tabla();
        cargaDatosCopere();
        this.setLocationRelativeTo(null);
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
        jLabel2 = new javax.swing.JLabel();
        cbregcontable = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbfamiliares = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        cbtipo = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        txtdni = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtnomape = new javax.swing.JTextField();
        txtmonto = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtotros = new javax.swing.JTextField();
        btnagregar = new javax.swing.JButton();
        cbnomape = new javax.swing.JComboBox();
        btnguardar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(0, 102, 102));

        jLabel1.setFont(new java.awt.Font("Roboto", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("TITULAR / FAMILIAR");

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
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel2.setText("Reg. Contable:");

        cbregcontable.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        cbregcontable.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Copere", "Caja de Pensiones", "Oprefa" }));
        cbregcontable.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbregcontableItemStateChanged(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel4.setText("Nombres Apellidos:");

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Familiares Asignados", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Roboto", 1, 12), new java.awt.Color(255, 0, 0))); // NOI18N

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Total S/ 0.00", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Roboto", 1, 13), new java.awt.Color(255, 0, 0))); // NOI18N

        tbfamiliares.setFont(new java.awt.Font("Roboto", 0, 12)); // NOI18N
        tbfamiliares.setModel(modelo);
        tbfamiliares.setRowHeight(30);
        tbfamiliares.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbfamiliaresKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tbfamiliares);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel5.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel5.setText("Tipo Familiar:");

        cbtipo.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        cbtipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TITULAR", "PADRE / MADRE", "HIJOS > 28 AÑOS", "HIJOS > 36 AÑOS" }));
        cbtipo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbtipoItemStateChanged(evt);
            }
        });
        cbtipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbtipoActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel6.setText("Otros:");

        txtdni.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel7.setText("Apellidos y Nombres:");

        txtnomape.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N

        txtmonto.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel8.setText("Monto:");

        jLabel9.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        jLabel9.setText("DNI:");

        txtotros.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N

        btnagregar.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        btnagregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icons8_add_32px.png"))); // NOI18N
        btnagregar.setText("Asignar Titular / Familiar");
        btnagregar.setEnabled(false);
        btnagregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnagregarActionPerformed(evt);
            }
        });

        cbnomape.setFont(new java.awt.Font("Roboto", 0, 13)); // NOI18N
        cbnomape.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "<<Seleccione>>" }));
        cbnomape.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbnomapeItemStateChanged(evt);
            }
        });

        btnguardar.setFont(new java.awt.Font("Roboto", 1, 13)); // NOI18N
        btnguardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Save.png"))); // NOI18N
        btnguardar.setText("Modificar Monto de Pago");
        btnguardar.setEnabled(false);
        btnguardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnguardarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnagregar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(cbregcontable, 0, 429, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbnomape, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cbtipo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtnomape)
                            .addComponent(txtmonto)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtotros)
                            .addComponent(txtdni)))
                    .addComponent(btnguardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cbregcontable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbnomape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbtipo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtotros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtdni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtnomape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtmonto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnagregar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnguardar)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbregcontableItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbregcontableItemStateChanged
        switch (cbregcontable.getSelectedItem().toString()) {
            case "Copere":
                cargaDatosCopere();
                break;
            case "Caja de Pensiones":
                cargaDatosCaja();
                break;
            case "Oprefa":
                cargaDatosOprefa();
                break;
        }
    }//GEN-LAST:event_cbregcontableItemStateChanged

    private void cbtipoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbtipoItemStateChanged
        if (cbtipo.getSelectedItem().toString().equals("OTROS")) {
            jLabel6.setVisible(true);
            txtotros.setVisible(true);
        } else {
            jLabel6.setVisible(false);
            txtotros.setVisible(false);
        }
    }//GEN-LAST:event_cbtipoItemStateChanged

    private void cbtipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbtipoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbtipoActionPerformed

    private void cbnomapeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbnomapeItemStateChanged
        if (cbnomape.getSelectedIndex() > 0) {
            cargarTitularFamiliar();
        }
    }//GEN-LAST:event_cbnomapeItemStateChanged

    private void btnagregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnagregarActionPerformed
        guardarFamiliar();
    }//GEN-LAST:event_btnagregarActionPerformed

    private void tbfamiliaresKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbfamiliaresKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_DELETE) {
            eliminarRegistro();
        }
    }//GEN-LAST:event_tbfamiliaresKeyPressed

    private void btnguardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnguardarActionPerformed
       switch (cbregcontable.getSelectedItem().toString()) {
            case "Copere":
                actualizarMontoCopere();
                break;
            case "Caja de Pensiones":
                actualizarMontoCaja();
                break;
            case "Oprefa":
                actualizarMontoOprefa();
                break;
        }
    }//GEN-LAST:event_btnguardarActionPerformed

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
            java.util.logging.Logger.getLogger(AgregarTitularFamiliar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AgregarTitularFamiliar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AgregarTitularFamiliar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AgregarTitularFamiliar.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AgregarTitularFamiliar dialog = new AgregarTitularFamiliar(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnagregar;
    private javax.swing.JButton btnguardar;
    private javax.swing.JComboBox cbnomape;
    private javax.swing.JComboBox<String> cbregcontable;
    private javax.swing.JComboBox cbtipo;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable tbfamiliares;
    private javax.swing.JTextField txtdni;
    private javax.swing.JTextField txtmonto;
    private javax.swing.JTextField txtnomape;
    private javax.swing.JTextField txtotros;
    // End of variables declaration//GEN-END:variables

    private void cargaDatosCopere() {
        try {
            Connection con = Conexion.getConnection();
            Statement stmt = con.createStatement();

            String query = "SELECT pe.IdPersonal, CONCAT(pe.A_Paterno, ' ', pe.A_Materno, ' ', pe.Nombres) AS Nomape "
                    + "FROM EstructuraCopere ec "
                    + "INNER JOIN Personal AS pe ON ec.Numero_CIP = pe.NroCip ORDER BY Nomape";
            ResultSet rs = stmt.executeQuery(query);
            cbnomape.removeAllItems();
            cbnomape.addItem(new Options("9999", "<<Seleccionar>>"));
            while (rs.next()) {
                cbnomape.addItem(new Options(rs.getString(1), rs.getString(2)));
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Manejo de excepciones
        }
    }

    private void cargaDatosCaja() {
        try {
            Connection con = Conexion.getConnection();
            Statement stmt = con.createStatement();

            String query = "select pe.IdPersonal, CONCAT(pe.A_Paterno, ' ', pe.A_Materno, ' ', pe.Nombres) AS Nomape from EstructuraCajaPensiones ecp "
                    + "inner join Personal as pe on ecp.Codigo_CIP = pe.NroCip ORDER BY Nomape";
            ResultSet rs = stmt.executeQuery(query);

            cbnomape.removeAllItems();
            cbnomape.addItem(new Options("9999", "<<Seleccionar>>"));
            while (rs.next()) {
                cbnomape.addItem(new Options(rs.getString(1), rs.getString(2)));
            }

            // Cerrar la conexión
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Manejo de excepciones
        }
    }

    private void cargaDatosOprefa() {
        try {
            // Establecer la conexión a la base de datos
            Connection con = Conexion.getConnection();
            Statement stmt = con.createStatement();

            // Ejecutar la consulta
            String query = "select pe.IdPersonal, CONCAT(pe.A_Paterno, ' ', pe.A_Materno, ' ', pe.Nombres) AS Nomape from EstructuraOprefa eo "
                    + "inner join Personal as pe on eo.DNI = pe.Dni ORDER BY Nomape";
            ResultSet rs = stmt.executeQuery(query);

            // Limpiar el JComboBox antes de agregar nuevos elementos
            cbnomape.removeAllItems();
            // Agregar la opción <<Seleccionar>>
            cbnomape.addItem(new Options("9999", "<<Seleccionar>>"));
            // Llenar el JComboBox con los resultados
            while (rs.next()) {
                cbnomape.addItem(new Options(rs.getString(1), rs.getString(2)));
            }

            // Cerrar la conexión
            rs.close();
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Manejo de excepciones
        }
    }

    private void cargarTitularFamiliar() {
        modelo.setRowCount(0);
        Object valorinicial = cbnomape.getSelectedItem();
        if (valorinicial != null) {
            try {
                Object[] data = new Object[4];
                Connection con = Conexion.getConnection();

                String query = "SELECT TipoFamiliar, DniFamiliar, Nomape, Monto FROM Familiar WHERE IdPersonal = ?";
                PreparedStatement pstm = con.prepareStatement(query);
                pstm.setInt(1, Integer.parseInt(((Options) cbnomape.getSelectedItem()).getValue()));
                ResultSet rs = pstm.executeQuery();
                while (rs.next()) {
                    for (int i = 0; i < data.length; i++) {
                        data[i] = rs.getObject(i + 1);
                    }
                    modelo.addRow(data);
                    
                }
                btnagregar.setEnabled(true);
                cargartotal();

                rs.close();
                pstm.close();
                con.close();
            } catch (SQLException e) {
                e.printStackTrace(); // Manejo de excepciones
            }
        }

    }
    Double totalFinal = 0.0;
    private void cargartotal() {
        
        if (modelo.getRowCount() > 0) {
            btnguardar.setEnabled(true);
        } else {
            btnguardar.setEnabled(false);
        }
        
        double total = 0.0;

        // Iterar sobre las filas del modelo del JTable
        for (int i = 0; i < modelo.getRowCount(); i++) {
            // Asegúrate de que la columna de monto sea numérica
            Object valor = modelo.getValueAt(i, 3); // Columna 4 (índice 3)
            if (valor != null) {
                try {
                    total += Double.parseDouble(valor.toString());
                    
                } catch (NumberFormatException e) {
                    // Manejo de errores si no se puede convertir a número
                    System.err.println("Error al convertir el valor: " + valor);
                    
                }
            }
        }

        // Actualizar el título del JPanel con el total
        javax.swing.border.TitledBorder border = (javax.swing.border.TitledBorder) jPanel5.getBorder();
        totalFinal = total;
        border.setTitle("Total S/ " + String.format("%.2f", total));
        jPanel5.repaint(); // Asegúrate de repintar el JPanel para que se actualice el título

    }
}
