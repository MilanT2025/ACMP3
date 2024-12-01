package main;


import java.awt.*;
import java.awt.image.BufferedImage;

public class BlurChild extends BlurComponent implements BlurChildData {

    private Style style;
    private BufferedImage buffImage;

    public BlurChild() {
        init();
    }

    public BlurChild(Style style) {
        this();
        this.style = style;
        style.install(this);
    }

    private void init() {
        setOpaque(true);
    }

    @Override
    public BufferedImage getImageAt(Shape shape) {
        if (buffImage == null) {
            return null;
        }
        Rectangle rec = shape.getBounds();
        Utils.fixRectangle(rec, buffImage);
        return buffImage.getSubimage(rec.x, rec.y, rec.width, rec.height);
    }

    @Override
    public Style getStyle() {
        return style;
    }

    @Override
    public Component getSource() {
        return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        buffImage = BlurRenderer.render(g, this, style);
        super.paintComponent(g);
    }

    @Override
    public Insets getInsets() {
        if (style != null && style.getBorder() != null) {
            return style.getBorder().getInsets();
        }
        return super.getInsets();
    }
}
