package main;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import javax.swing.*;
import java.awt.*;

public class Application extends JFrame {

    private DesktopPaneCustom desktopPaneCustom;

    public Application() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(false);
        setSize(new Dimension(1366, 768));
        setLocationRelativeTo(null);
        setResizable(false);
        setIconImage(new ImageIcon(System.getProperty("user.dir") + "/logoACMP.png").getImage());
        setTitle("Automatización de Procesos - Asociación Circulo Militar del Perú - v2.0");

        // Pasamos la referencia de la ventana principal (this) al constructor de DesktopPaneCustom
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
