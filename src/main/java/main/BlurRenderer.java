package main;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BlurRenderer {

    public static BufferedImage render(Graphics g, JComponent component, Style style) {
        BufferedImage paintImage = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = paintImage.createGraphics();
        Rectangle bound = component.getBounds();
        float blur = style == null ? 0 : style.getBlur();

        // create background image
        BlurChildData blurChildData = getBlurChildData(component);
        if (blurChildData == null) {
            return null;
        }
        if (component.isOpaque()) {
            Rectangle rec = SwingUtilities.convertRectangle(component.getParent(), bound, blurChildData.getSource());
            int x = rec.x >= 0 ? 0 : rec.x * -1;
            int y = rec.y >= 0 ? 0 : rec.y * -1;

            Image image = blurChildData.getImageAt(rec);
            if (image != null) {
                g2.drawImage(image, x, y, null);
            }
        }
        if (blurChildData.getStyle() != null) {
            blur += blurChildData.getStyle().getBlur();
        }
        BlurData blurData = getBlurData(component);
        if (blurData != null) {
            Insets insets = new Insets(0, 0, 0, 0);
            Insets margin = new Insets(0, 0, 0, 0);
            if (style != null && style.getBorder() != null) {
                if (style.getBorder().getDropShadow() != null) {
                    insets = style.getBorder().getDropShadow().getInsets();
                }
                if (style.getBorder().getMargin() != null) {
                    margin = UIScale.scale(style.getBorder().getMargin());
                }
            }
            Rectangle blurRec = FlatUIUtils.subtractInsets(bound, insets);
            Rectangle rec = SwingUtilities.convertRectangle(component.getParent(), blurRec, blurData.getSource());
            Shape shape = style == null ? rec : style.getBorder() == null ? rec : style.getBorder().createShape(rec);
            Image image = blurData.getBlurImageAt(shape, blur);
            int x = margin.left + (rec.x >= 0 ? 0 : rec.x * -1);
            int y = margin.top + (rec.y >= 0 ? 0 : rec.y * -1);
            if (image != null) {
                g2.drawImage(image, insets.left + x, insets.top + y, null);
            }
            if (style != null) {
                Rectangle r;
                if (style.getBorder() == null || style.getBorder().getDropShadow() == null) {
                    r = new Rectangle(bound.getSize());
                } else {
                    r = FlatUIUtils.subtractInsets(new Rectangle(bound.getSize()), style.getBorder().getDropShadow().getInsets());
                }
                Shape defaultShape = style.getBorder() == null ? r : style.getBorder().createShape(r);
                style.paint(component, g2, new StyleShape(defaultShape, style));
            }
        }
        g2.dispose();
        g.drawImage(paintImage, 0, 0, null);
        return paintImage;
    }

    private static BlurData getBlurData(Component component) {
        if (component.getParent() != null) {
            if (component.getParent() instanceof BlurData) {
                return (BlurData) component.getParent();
            } else {
                return getBlurData(component.getParent());
            }
        } else {
            return null;
        }
    }

    private static BlurChildData getBlurChildData(Component component) {
        if (component.getParent() != null) {
            if (component.getParent() instanceof BlurChildData) {
                return (BlurChildData) component.getParent();
            } else {
                return getBlurChildData(component.getParent());
            }
        } else {
            return null;
        }
    }
}
