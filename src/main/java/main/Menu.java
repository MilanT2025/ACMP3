package main;

import Controlador.Conexion;
import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import vista.Backup;
import vista.CTS;
import vista.CargaCopere;
import vista.CuentasporPagar;
import vista.Depreciacion;
import vista.Estado_Patrimonio;
import vista.Excel_AFPNet;
import vista.Flujo_Caja;
import vista.Flujo_Efectivo;
import vista.Gratificacion;
import vista.LibroMayor;
import vista.LibrosE_SIRE;
import vista.Mercaderia;
import vista.Txt_Plame;
import vista.Vacaciones;

public class Menu extends JPanel {

    private DesktopPaneCustom desktopPaneCustom;
    private Application application;
    private String usuario;

    public Menu(DesktopPaneCustom desktopPaneCustom, Application application, String usuario) {
        this.desktopPaneCustom = desktopPaneCustom;
        this.application = application;
        this.usuario = usuario;
        init();
    }

    private void init() {
        background = new BlurBackground(new ImageIcon(getClass().getResource("/raven/image/background.png")).getImage());
        background.setLayout(new MigLayout("al center center"));
        blurChild = new BlurChild(getStyle());
        blurChild.setLayout(new MigLayout("insets 50,wrap 5,gap 20", "[sg,fill]", "[sg,fill]"));
        background.add(blurChild);

        setLayout(new BorderLayout());

        add(background);

        createMenu();
    }

    private void createMenu() {
        Item itemAFPNet = new Item("Excel AFPNet", "raven/icons/icons8-excel-24.svg");
        Item itemPDTPlame = new Item("TXT PDT Plame", "raven/icons/txt-svgrepo.svg");
        Item itemSIRE = new Item("<html><center>Libros Electronicos<br>SIRE</html>", "raven/icons/books-svgrepo-com.svg");
        Item itemPatrimonio = new Item("<html><center>Estado Cambio de<br>Patrimonio Neto</html>", "raven/icons/money-svgrepo-com.svg");
        Item itemFlujoEfectivo = new Item("Flujo de Efectivo", "raven/icons/money-transfer-svgrepo-com.svg");
        Item itemFlujoCaja = new Item("Flujo de Caja", "raven/icons/cash-register-svgrepo-com.svg");

        Item itemAsociados = new Item("<html><center>Control de<br>Universo de Asociado</html>s", "raven/icons/add-user-business-man-employee-svgrepo-com.svg");
        Item itemLibroMayor = new Item("<html><center>Libro Mayor<br>(Cuentas Contables Clase 6)</html>", "raven/icons/book-svgrepo-com.svg");
        Item itemMercaderias = new Item("<html><center>Valorizacion de<br>Mercaderias y Existencias</html>", "raven/icons/stock-svgrepo-com.svg");
        Item itemDepreciacion = new Item("<html><center>Depreciacion de Activos Fijos y<br>Amortización de Intangibles</html>", "raven/icons/down-trend-round-svgrepo-com.svg");
        Item itemCTS = new Item("<html><center>Calculo Mensual de<br>CTS</html>", "raven/icons/time-is-money-svgrepo-com.svg");
        Item itemGratificacion = new Item("<html><center>Calculo Mensual de<br>Gratificacion y Bonificaciones</html>", "raven/icons/medal-gold-winner-2-svgrepo-com.svg");
        Item itemVacaciones = new Item("<html><center>Calculo Mensual de<br>Vacaciones</html>", "raven/icons/airplane-filled-fly-svgrepo-com.svg");
        
        Item itemTesoreria = new Item("<html><center>Cuentas por Pagar<br>(Tesoreria)</html>", "raven/menu/user.svg");
        Item itemBackup = new Item("<html><center>Copia de Seguridad<br>de Base de Datos</html>", "raven/menu/setting.svg");
        
        itemAFPNet.addActionListener(e -> {
            FlatLightLaf.setup();
            Excel_AFPNet ini = new Excel_AFPNet(this.usuario);
            ini.setVisible(true);
            application.setVisible(false);

        });

        itemPDTPlame.addActionListener(e -> {
            FlatLightLaf.setup();
            Txt_Plame ini = new Txt_Plame(this.usuario);
            ini.setVisible(true);
            application.setVisible(false);
        });

        itemSIRE.addActionListener(e -> {
            FlatLightLaf.setup();
            LibrosE_SIRE ini = new LibrosE_SIRE(this.usuario);
            ini.setVisible(true);
            application.setVisible(false);

        });

        itemPatrimonio.addActionListener(e -> {
            FlatLightLaf.setup();
            Estado_Patrimonio ini = new Estado_Patrimonio(this.usuario);
            ini.setVisible(true);
            application.setVisible(false);
        });

        itemFlujoEfectivo.addActionListener(e -> {
            FlatLightLaf.setup();
            Flujo_Efectivo ini = new Flujo_Efectivo(this.usuario);
            ini.setVisible(true);
            application.setVisible(false);
        });

        itemFlujoCaja.addActionListener(e -> {
            FlatLightLaf.setup();
            Flujo_Caja ini = new Flujo_Caja(this.usuario);
            ini.setVisible(true);
            application.setVisible(false);
        });

        itemAsociados.addActionListener(e -> {
            FlatLightLaf.setup();
            CargaCopere ini = new CargaCopere(this.usuario);
            ini.setVisible(true);
            application.setVisible(false);
        });

        itemLibroMayor.addActionListener(e -> {
            FlatLightLaf.setup();
            LibroMayor ini = new LibroMayor(this.usuario);
            ini.setVisible(true);
            application.setVisible(false);
        });

        itemMercaderias.addActionListener(e -> {
            FlatLightLaf.setup();
            Mercaderia ini = new Mercaderia(this.usuario);
            ini.setVisible(true);
            application.setVisible(false);
        });

        itemDepreciacion.addActionListener(e -> {
            FlatLightLaf.setup();
            Depreciacion ini = new Depreciacion(this.usuario);
            ini.setVisible(true);
            application.setVisible(false);
        });

        itemCTS.addActionListener(e -> {
            FlatLightLaf.setup();
            CTS ini = new CTS(this.usuario);
            ini.setVisible(true);
            application.setVisible(false);
        });

        itemGratificacion.addActionListener(e -> {
            FlatLightLaf.setup();
            Gratificacion ini = new Gratificacion(this.usuario);
            ini.setVisible(true);
            application.setVisible(false);
        });

        itemVacaciones.addActionListener(e -> {
            FlatLightLaf.setup();
            Vacaciones ini = new Vacaciones(this.usuario);
            ini.setVisible(true);
            application.setVisible(false);
        });
        
        itemTesoreria.addActionListener(e -> {
            FlatLightLaf.setup();
            CuentasporPagar ini = new CuentasporPagar(this.usuario);
            ini.setVisible(true);
            application.setVisible(false);
        });
        
        itemBackup.addActionListener(e -> {
                FlatLightLaf.setup();
                Backup ini = new Backup(this.usuario);
                ini.setVisible(true);
                application.setVisible(false);
        });
        
        componentesMap.put("itemAFPNet", itemAFPNet);
        componentesMap.put("itemPDTPlame", itemPDTPlame);
        componentesMap.put("itemSIRE", itemSIRE);
        componentesMap.put("itemPatrimonio", itemPatrimonio);
        componentesMap.put("itemFlujoEfectivo", itemFlujoEfectivo);
        componentesMap.put("itemFlujoCaja", itemFlujoCaja);
        componentesMap.put("itemAsociados", itemAsociados);
        componentesMap.put("itemLibroMayor", itemLibroMayor);
        componentesMap.put("itemMercaderias", itemMercaderias);
        componentesMap.put("itemDepreciacion", itemDepreciacion);
        componentesMap.put("itemCTS", itemCTS);
        componentesMap.put("itemGratificacion", itemGratificacion);
        componentesMap.put("itemVacaciones", itemVacaciones);
        componentesMap.put("itemTesoreria", itemTesoreria);
        componentesMap.put("itemBackup", itemBackup);
        
        verificarModulos(this.usuario);
        

        /*blurChild.add(itemAFPNet);
        blurChild.add(itemPDTPlame);
        blurChild.add(itemSIRE);
        blurChild.add(itemPatrimonio);
        blurChild.add(itemFlujoEfectivo);
        blurChild.add(itemFlujoCaja);
        blurChild.add(itemAsociados);
        blurChild.add(itemLibroMayor);
        blurChild.add(itemMercaderias);
        blurChild.add(itemDepreciacion);
        blurChild.add(itemCTS);
        blurChild.add(itemGratificacion);
        blurChild.add(itemVacaciones);
        blurChild.add(itemTesoreria);
        blurChild.add(itemBackup);
        blurChild.add(itemUsuario);*/
    }

    private Style getStyle() {
        return new Style()
                .setBlur(30)
                .setBorder(new StyleBorder(30)
                        .setBorderWidth(1.5f)
                        .setOpacity(0.25f)
                        .setBorderColor(new GradientColor(new Color(150, 150, 150), new Color(230, 230, 230), new Point2D.Float(0, 0), new Point2D.Float(0, 1)))
                        .setDropShadow(new StyleDropShadow(new Color(0, 0, 0), 0.2f, new Insets(12, 12, 20, 20)))
                )
                .setOverlay(new StyleOverlay(new Color(255, 255, 255), 0.1f));
    }

    private BlurChild blurChild;
    private BlurBackground background;
    
    private Map<String, JComponent> componentesMap = new HashMap<>();
    
    
    private void verificarModulos(String usuario) {
        try {
            Connection con = Conexion.getConnection();
            String sql = "SELECT Modulo FROM Usuario U "
                    + "INNER JOIN UsuarioRol UR ON U.idRol = UR.idRol "
                    + "WHERE usuario = ?";

            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, usuario);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String nombreModulo = rs.getString("Modulo");
                agregarComponente(nombreModulo);
            }

            rs.close();
            pst.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

// Método para agregar componentes dinámicamente
    private void agregarComponente(String nombreModulo) {
        if (componentesMap.containsKey(nombreModulo)) {
            blurChild.add(componentesMap.get(nombreModulo));
        } 
    }
}

