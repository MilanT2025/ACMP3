package main;


import java.awt.*;
import java.awt.image.BufferedImage;

public interface BlurChildData {

    BufferedImage getImageAt(Shape shape);

    Style getStyle();

    Component getSource();
}
