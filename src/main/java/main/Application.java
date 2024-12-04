package main;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Application extends JFrame {

    private DesktopPaneCustom desktopPaneCustom;

    public Application() {
        
        Locale locale = new Locale("es", "PE");
        Locale.setDefault(locale);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
        symbols.setGroupingSeparator(','); 
        symbols.setDecimalSeparator('.');
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(false);
        setSize(new Dimension(1366, 768));
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(new ImageIcon(System.getProperty("user.dir") + "/logoACMP.png").getImage());
        setTitle("Automatización de Procesos - Asociación Circulo Militar del Perú - v2.0");
        desktopPaneCustom = new DesktopPaneCustom(this);
        getContentPane().add(desktopPaneCustom);
    }

    public static void main(String[] args) {
        FlatMacDarkLaf.setup();
        EventQueue.invokeLater(() -> new Application().setVisible(true));
    }

    public void ocultarVentana() {
        System.out.println("entro");
        setVisible(false);
    }
}
