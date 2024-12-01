package main;


import javax.swing.*;
import java.awt.*;

public class StyleRippleEffect implements StylePaint {

    private Color color;
    private int duration = 700;
    private float opacity = 0.2f;
    private RippleEffect rippleEffect;

    public StyleRippleEffect() {
    }

    public StyleRippleEffect(Color color) {
        this.color = color;
    }

    public StyleRippleEffect(Color color, int duration) {
        this.color = color;
        this.duration = duration;
    }

    public StyleRippleEffect(Color color, int duration, float opacity) {
        this.color = color;
        this.duration = duration;
        this.opacity = opacity;
    }

    public void install(JComponent component) {
        rippleEffect = new RippleEffect(component, color, duration, opacity);
    }

    public StyleRippleEffect setColor(Color color) {
        this.color = color;
        return this;
    }

    public StyleRippleEffect setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public StyleRippleEffect setOpacity(float opacity) {
        this.opacity = opacity;
        return this;
    }

    @Override
    public void paint(Component com, Graphics g, StyleShape shape) {
        if (rippleEffect != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            rippleEffect.renderer(g2, shape.getShape());
            g2.dispose();
        }
    }
}
