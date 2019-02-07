package support;

import javax.swing.plaf.basic.BasicPasswordFieldUI;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.plaf.basic.BasicPasswordFieldUI;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.JTextComponent;

/**
 * Created by Frankline Sable on 11/26/2016.
 */
public class JPasswordFieldHintUI extends BasicPasswordFieldUI implements FocusListener {

    private String hint;
    private Color hintColor;

    public JPasswordFieldHintUI(String hint, Color hintColor) {
        this.hint = hint;
        this.hintColor = hintColor;
    }

    private void repaint() {
        if (getComponent() != null) {
            getComponent().repaint();
        }
    }

    @Override
    protected void paintSafely(Graphics g) {
        // Render the default text field UI
        super.paintSafely(g);
        // Render the hint text
        JTextComponent component = getComponent();
        if (component.getText().length() == 0 && !component.hasFocus()) {
            g.setColor(hintColor);
            int padding = (component.getHeight() - component.getFont().getSize()) / 2;
            int inset = 3;
            g.drawString(hint, inset, component.getHeight() - padding - inset);
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        repaint();
    }

    @Override
    public void focusLost(FocusEvent e) {
        repaint();
    }

    @Override
    public void installListeners() {
        super.installListeners();
        getComponent().addFocusListener(this);
    }

    @Override
    public void uninstallListeners() {
        super.uninstallListeners();
        getComponent().removeFocusListener(this);
    }
}


