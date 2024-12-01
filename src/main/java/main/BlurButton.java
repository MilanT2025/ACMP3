package main;

import com.formdev.flatlaf.ui.FlatButtonUI;
import com.formdev.flatlaf.ui.FlatUIUtils;
import main.BlurChildData;
import main.BlurRenderer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BlurButton extends JButton implements BlurChildData {

    @Override
    public void updateUI() {
        super.updateUI();
        setUI(new BlurButtonUI());
    }

    private Style style;
    private BufferedImage buffImage;

    public BlurButton(Style style) {
        this.style = style;
        style.install(this);
        init();
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
    public Insets getInsets() {
        boolean emptyBorder = getBorder() instanceof EmptyBorder;
        if (style != null && (style.getBorder() != null || emptyBorder)) {
            Insets insets = style.getBorder().getInsets();
            if (emptyBorder) {
                insets = FlatUIUtils.addInsets(insets, super.getInsets());
            }
            return FlatUIUtils.addInsets(insets, super.getMargin());
        }
        return super.getInsets();
    }

    private class BlurButtonUI extends FlatButtonUI {

        private Border oldBorder;

        public BlurButtonUI() {
            super(false);
        }

        @Override
        protected void installDefaults(AbstractButton b) {
            super.installDefaults(b);
            oldBorder = b.getBorder();
            b.setBorder(BorderFactory.createEmptyBorder());
        }

        @Override
        public void uninstallUI(JComponent c) {
            super.uninstallUI(c);
            c.setBorder(oldBorder);
            oldBorder = null;
        }

        @Override
        protected void paintBackground(Graphics g, JComponent c) {
            buffImage = BlurRenderer.render(g, BlurButton.this, style);
        }
    }
}
