package FrontEnd;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Frankline Sable on 11/26/2016.
 */
public class FrameTest {

    static JFrame frame;
    public FrameTest(){

        ImageIcon loaderImg = new ImageIcon(getClass().getResource("Graphics/Presentation1.png"));
        JLabel dialogLabel = new JLabel("Che", loaderImg, JLabel.CENTER);
        dialogLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        dialogLabel.setVerticalTextPosition(JLabel.TOP);
        dialogLabel.setHorizontalTextPosition(JLabel.CENTER);
        dialogLabel.setBounds(50,100,200,60);

        frame = new JFrame();
        frame.setVisible(true);
        frame.setSize(800, 500);
        // frame.setAlwaysOnTop(true);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(0,0,0,0));
        frame.add(dialogLabel);
    }
    public static void main(String[] args) {

       // JFrame.setDefaultLookAndFeelDecorated(true);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {




                new FrameTest();

            }
        });

    }
}
