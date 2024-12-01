package main;


import javax.swing.*;
import java.awt.*;

public class Style implements StylePaint {

    private float blur;
    private StyleBorder border;
    private StyleOverlay overlay;
    private StyleRippleEffect rippleEffect;

    public Style() {
    }

    public Style setBlur(float blur) {
        this.blur = blur;
        return this;
    }

    public Style setBorder(StyleBorder style) {
        this.border = style;
        return this;
    }

    public Style setOverlay(StyleOverlay style) {
        this.overlay = style;
        return this;
    }

    public Style setRippleEffect(StyleRippleEffect style) {
        this.rippleEffect = style;
        return this;
    }

    public float getBlur() {
        return blur;
    }

    public StyleBorder getBorder() {
        return border;
    }

    public StyleOverlay getOverlay() {
        return overlay;
    }

    public StyleRippleEffect getRippleEffect() {
        return rippleEffect;
    }

    public void install(JComponent component) {
        if (rippleEffect != null) {
            rippleEffect.install(component);
        }
    }

    @Override
    public void paint(Component com, Graphics g, StyleShape shape) {
        StyleOverlay overlay = getOverlay();
        StyleBorder border = getBorder();
        StyleRippleEffect rippleEffect = getRippleEffect();
        if (overlay != null && overlay.getOpacity() > 0f) {
            overlay.paint(com, g, shape);
        }
        if (border != null && (border.getBorderWidth() > 0 || border.getDropShadow() != null)) {
            border.paint(com, g, shape);
        }
        if (rippleEffect != null) {
            rippleEffect.paint(com, g, shape);
        }
    }
}
