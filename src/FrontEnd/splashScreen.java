package FrontEnd;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Frankline Sable on 11/27/2016. At Maseno university
 */
public class splashScreen {

    private int count = 0;

    private splashScreen() {

        TitledBorder border = new TitledBorder(BorderFactory.createRaisedBevelBorder(), "Please Wait while loading...", TitledBorder.LEFT, TitledBorder.ABOVE_TOP);
        border.setTitleFont(new Font("Gotham light", Font.PLAIN, 14));
        border.setTitleColor(Color.RED);

        JLabel logoImg = new JLabel("by Roy Wafula", (new ImageIcon(getClass().getResource("graphics/splashLogo.png"))), JLabel.CENTER);
        logoImg.setFont(new Font("Gotham light", Font.PLAIN, 14));
        logoImg.setHorizontalTextPosition(JLabel.CENTER);
        logoImg.setVerticalTextPosition(JLabel.BOTTOM);
        logoImg.setForeground(Color.RED);
        JProgressBar progressBar = new JProgressBar(SwingConstants.HORIZONTAL, 0, 3);
        progressBar.setBorder(border);

        EventQueue.invokeLater(() -> {

            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            JWindow frame = new JWindow();
            frame.setAlwaysOnTop(true);
            frame.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        SwingUtilities.getWindowAncestor(e.getComponent()).dispose();
                    }
                }
            });
            frame.setBackground(new Color(0, 0, 0, 0));
            //frame.setContentPane(new TranslucentPane());
            frame.setLayout(new BorderLayout());
            frame.add(logoImg, BorderLayout.CENTER);
            frame.add(progressBar, BorderLayout.SOUTH);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.setIconImage((new ImageIcon(getClass().getResource("graphics/lifereminderIcon.png"))).getImage());


            new Timer(800, e -> {
                progressBar.setValue(count);
                if (count == 4) {
                    ((Timer) e.getSource()).stop();
                    frame.dispose();
                    new credentialsVerifier();
                }
                count++;
            }).start();

        });
    }

    public static void main(String[] args) {

        new splashScreen();
    }

}
