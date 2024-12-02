package main;

import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import vista.CargaCopere;
import vista.Estado_Patrimonio;
import vista.Excel_AFPNet;
import vista.Flujo_Caja;
import vista.Flujo_Efectivo;
import vista.LibroMayor;
import vista.LibrosE_SIRE;
import vista.Txt_Plame;

public class Menu extends JPanel {

    private DesktopPaneCustom desktopPaneCustom;
    private Application application;

    public Menu(DesktopPaneCustom desktopPaneCustom, Application application) {
        this.desktopPaneCustom = desktopPaneCustom;
        this.application = application;
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

        itemAFPNet.addActionListener(e -> {
            FlatLightLaf.setup();
            Excel_AFPNet ini = new Excel_AFPNet();
            ini.setVisible(true);
            application.setVisible(false);

        });

        itemPDTPlame.addActionListener(e -> {
            FlatLightLaf.setup();
            Txt_Plame ini = new Txt_Plame();
            ini.setVisible(true);
            application.setVisible(false);
        });

        itemSIRE.addActionListener(e -> {
            FlatLightLaf.setup();
            LibrosE_SIRE ini = new LibrosE_SIRE();
            ini.setVisible(true);
            application.setVisible(false);

        });

        itemPatrimonio.addActionListener(e -> {
            FlatLightLaf.setup();
            Estado_Patrimonio ini = new Estado_Patrimonio();
            ini.setVisible(true);
            application.setVisible(false);
        });

        itemFlujoEfectivo.addActionListener(e -> {
            FlatLightLaf.setup();
            Flujo_Efectivo ini = new Flujo_Efectivo();
            ini.setVisible(true);
            application.setVisible(false);
        });

        itemFlujoCaja.addActionListener(e -> {
            FlatLightLaf.setup();
            Flujo_Caja ini = new Flujo_Caja();
            ini.setVisible(true);
            application.setVisible(false);
        });

        itemAsociados.addActionListener(e -> {
            FlatLightLaf.setup();
            CargaCopere ini = new CargaCopere();
            ini.setVisible(true);
            application.setVisible(false);
        });

        itemLibroMayor.addActionListener(e -> {
            FlatLightLaf.setup();
            LibroMayor ini = new LibroMayor();
            ini.setVisible(true);
            application.setVisible(false);
        });

        itemMercaderias.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "*************** Modulo en Mantenimiento ***************", "Matenimiento", JOptionPane.INFORMATION_MESSAGE);
        });

        itemDepreciacion.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "*************** Modulo en Mantenimiento ***************", "Matenimiento", JOptionPane.INFORMATION_MESSAGE);
        });

        itemCTS.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "*************** Modulo en Mantenimiento ***************", "Matenimiento", JOptionPane.INFORMATION_MESSAGE);
        });

        itemGratificacion.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "*************** Modulo en Mantenimiento ***************", "Matenimiento", JOptionPane.INFORMATION_MESSAGE);
        });

        itemVacaciones.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "*************** Modulo en Mantenimiento ***************", "Matenimiento", JOptionPane.INFORMATION_MESSAGE);
        });

        blurChild.add(itemAFPNet);
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
}
