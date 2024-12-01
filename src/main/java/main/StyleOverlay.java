package main;

import com.formdev.flatlaf.ui.FlatUIUtils;

import java.awt.*;

public class StyleOverlay implements StylePaint {

    private GradientColor color;
    private float opacity;

    public StyleOverlay(Color color, float opacity) {
        this.color = new GradientColor(color);
        this.opacity = opacity;
    }

    public StyleOverlay(GradientColor color, float opacity) {
        this.color = color;
        this.opacity = opacity;
    }

    public GradientColor getColor() {
        return color;
    }

    public float getOpacity() {
        return opacity;
    }

    @Override
    public void paint(Component com, Graphics g, StyleShape shape) {
        Graphics2D g2 = (Graphics2D) g.create();
        getColor().paint(com, g2, shape);
        FlatUIUtils.setRenderingHints(g2);
        if (getOpacity() < 1) {
            g2.setComposite(AlphaComposite.SrcOver.derive(getOpacity()));
        }
        g2.fill(shape.getShape());
        g2.dispose();
    }
}
