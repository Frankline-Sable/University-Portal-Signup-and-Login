package FrontEnd;

import support.Wallpaper_Layering;

import javax.swing.*;
import javax.swing.plaf.LayerUI;
import java.awt.*;
import java.awt.image.ImageObserver;

/**
 * Created by Frankline Sable on 11/26/2016.
 */
public class logIn extends JFrame {

    private JPanel wallpaperPanel;
    public logIn(){
        super("Please Log in Here");

        wallpaperPanel=new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
              //  g.fillRect(0,0,this.getWidth(),this.getHeight()/4);

                ImageIcon icon=new ImageIcon(getClass().getResource("Graphics/Presentation1.png"));
                Image img=icon.getImage();

                g.drawImage(img,0,0,this.getWidth(),this.getHeight()/4,null);
            }
        };


        LayerUI<JPanel> panelWallpaper=new Wallpaper_Layering();
        JLayer<JPanel> applyWallpaperToPanel=new JLayer<>( wallpaperPanel,panelWallpaper);

        setUndecorated(true); //NB:: Undecorated is followed by visibility for easier display
        setVisible(true);
        getContentPane().setBackground(Color.decode("#000000"));
        setSize(700,500);
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setIconImage((new ImageIcon(getClass().getResource("graphics/lifereminderIcon.png"))).getImage());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        add(wallpaperPanel, BorderLayout.CENTER);

    }
    public static void main(String[] args){
        new logIn();
    }
}
