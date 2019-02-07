package FrontEnd;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Frankline Sable on 11/26/2016.
 */
public class incre  extends JFrame{

    private  int count=0;
    public incre(){

        JTextField te=new JTextField(20);
        te.setEditable(false);

        te.setText(Integer.toString(count));

        JButton button=new JButton("Click") ;
        button.addActionListener(e -> {

            count+=3
            ;
            te.setText(Integer.toString(count));

        });

        setSize(500,400);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        add(button);
        add(te);


    }
    public static void main(String[] args){

        new incre();
    }
}
