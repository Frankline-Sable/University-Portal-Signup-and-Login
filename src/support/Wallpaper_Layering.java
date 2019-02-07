package support;

import javax.swing.*;
import javax.swing.plaf.LayerUI;
import java.awt.*;

/**
 * Created by Frankline Sable on 11/26/2016.
 */
public class Wallpaper_Layering extends LayerUI<JPanel> {
    @Override
    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);

        Graphics2D g2=(Graphics2D) g.create();

        int w=c.getWidth();
        int h=c.getHeight();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5F));
        g2.setPaint(new GradientPaint(0,0,Color.decode("#299617"),0, h, Color.decode("#33CC99")));
        g2.fillRect(0,0,w,h);
        g2.dispose();
    }
}
