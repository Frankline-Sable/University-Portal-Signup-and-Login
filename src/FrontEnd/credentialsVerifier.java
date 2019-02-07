package FrontEnd;

import BackEnd.DetailsClass;
import DatabasesConnectors.DatabaseFirst;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;
import support.JPasswordFieldHintUI;
import support.JTextFieldHintUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.Random;

/**
 * Class has been Written by Frankline Sable, As on 10/28/2016
 * You have no write to modify anything written here, unless you've
 * consulted me.
 */
public class credentialsVerifier {

    private static JFrame frame, frame2;
    private final Border emptyBorder, loweredBorder;
    private final Random rand = new Random();
    private JPanel dataEntryPanel, frame2Panel, headSignUp;
    private ImageIcon exitIcon;
    private JTextField userField, passField;
    private JButton SignInButton;
    private static JDialog loadingDialog;
    private JTextField errorConnectingLabel, errorDataLabel;
    private final JTextField[] textFieldArray = new JTextField[5];
    private final String[] textFieldArrayHint = {"First Name", "Last Name", "Admission Number"};
    private JDatePickerImpl datePicker;
    private JLabel[] errorIcons = new JLabel[10];
    private JLabel headerLabel;
    private final DatabaseFirst dbHandler;
    private Color headerColor[] = {Color.decode("#652DC1"), Color.decode("#FF6037"), Color.decode("#E30B5C"), Color.decode("#FF3855"), Color.decode("#0066CC"), Color.decode("#87421F")};
    private Boolean avoidDuplication = true,avoidDuplication2 = true,avoidDuplication3 = true,avoidDuplication4 = true;
    private JComboBox<String> courseComboBox;
    private String courses[] = {"Bsc. Computer Science", "Bsc. Computer Technology", "Bsc. Information Technology"};

    public credentialsVerifier() {

        /*
        UIManager.put("nimbusBase", new Color(255,50,40));
            UIManager.put("nimbusBlueGrey", new Color(10,200,5));
            UIManager.put("control", new Color(0,10,10));
         */

        try {

            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }

        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel
        }

        dbHandler = new DatabaseFirst();
        emptyBorder = BorderFactory.createEmptyBorder();
        loweredBorder = BorderFactory.createLoweredBevelBorder();

        initializeLogIn();
        initializeSignUp();

        ImageIcon loaderImg = new ImageIcon(getClass().getResource("Graphics/default.gif"));
        JLabel dialogLabel = new JLabel("Checking entered credentials...", loaderImg, JLabel.CENTER);
        dialogLabel.setFont(new Font("gotham light", Font.PLAIN, 23));
        dialogLabel.setForeground(Color.RED);
        dialogLabel.setVerticalTextPosition(JLabel.BOTTOM);
        dialogLabel.setHorizontalTextPosition(JLabel.CENTER);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }


        loadingDialog = new JDialog(frame, true) {
            @Override
            protected void processWindowEvent(WindowEvent e) {
                super.processWindowEvent(e);

                    new Timer(3000, e1 -> {
                        Timer ev = (Timer) e1.getSource();
                        loadingDialog.setModal(false);
                        loadingDialog.dispose();
                        ev.stop();
                        triggerSignIn();
                    }).start();
            }

        };
        loadingDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        loadingDialog.setUndecorated(true);
        loadingDialog.setVisible(false);
        loadingDialog.setSize(350, 170);
        loadingDialog.setOpacity(0.80f);
        loadingDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        loadingDialog.setLocationRelativeTo(null);
        loadingDialog.setBackground(new Color(0, 0, 0, 0));
        loadingDialog.setLayout(new BorderLayout());

        loadingDialog.add(dialogLabel, BorderLayout.CENTER);


        frame = new JFrame();
        frame.setUndecorated(true); //NB:: Undecorated is followed by visibility for easier display
        frame.setVisible(true);
        frame.setSize(800, 500);
        // frame.setAlwaysOnTop(true);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(dataEntryPanel);

        frame2 = new JFrame();
        frame2.setUndecorated(true); //NB:: Undecorated is followed by visibility for easier display
        frame2.setVisible(false);
        frame2.setSize(800, 500);
        // frame.setAlwaysOnTop(true);
        frame2.setLocationRelativeTo(null);
        frame2.setLayout(new BorderLayout());
        frame2.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setIconImage((new ImageIcon(getClass().getResource("graphics/lifereminderIcon.png"))).getImage());

        frame2.add(frame2Panel);


    }


    private void initializeLogIn() {

        TitledBorder titledBorder = BorderFactory.createTitledBorder(BorderFactory.createRaisedBevelBorder(), "Sign In Below Using Your Credentials", TitledBorder.LEFT, TitledBorder.BELOW_TOP);
        titledBorder.setTitleColor(new Color(250, 250, 250).brighter());
        titledBorder.setTitleFont(new Font("Bradley Hand ITC", Font.BOLD, 20));

        JLabel logoLabel = new JLabel("MASENO UNIVERSITY");
        logoLabel.setToolTipText("The Fountain Of Excellence");
        logoLabel.setForeground(new Color(224, 224, 224));
        logoLabel.setFont(new Font("Times New Roman", Font.BOLD, 50));
        logoLabel.setBounds(30, 10, 550, 150);

        JLabel logoLabel2 = new JLabel("The Fountain Of Excellence");
        logoLabel2.setToolTipText("The Fountain Of Excellence");
        logoLabel2.setForeground(new Color(224, 224, 224));
        logoLabel2.setFont(new Font("Lucida Handwriting", Font.BOLD, 18));
        logoLabel2.setBounds(30, 150, 550, 25);

        JLabel logoIcon = new JLabel();
        logoIcon.setIcon(new ImageIcon(getClass().getResource("Graphics/Maseno_University_Official_avatar.png")));
        logoIcon.setBounds(640, 10, 150, 170);

        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("gotham light", Font.PLAIN, 16));
        userLabel.setBounds(20, 50, 100, 30);
        userLabel.setForeground(Color.WHITE.brighter());

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(userLabel.getFont());
        passLabel.setBounds(20, 100, 100, 30);
        passLabel.setForeground(userLabel.getForeground());

        userField = new JTextField();
        userField.setBorder(BorderFactory.createCompoundBorder(emptyBorder, loweredBorder));
        userField.setFont(new Font("gotham light", Font.PLAIN, 14));
        userField.setBounds(130, 50, 500, 30);
        userField.setUI(new JTextFieldHintUI("Enter Your First Name ", Color.GRAY));
        userField.addKeyListener(new keyHandler());

        passField = new JPasswordField();
        passField.setUI(new JPasswordFieldHintUI("eg.CI/00054/015", Color.GRAY));
        passField.setBounds(130, 100, 500, 30);
        passField.setBorder(userField.getBorder());
        passField.setFont(userField.getFont());
        passField.addKeyListener(new keyHandler());

        SignInButton = new JButton("Sign In", new ImageIcon(getClass().getResource("Graphics/okay_go1.png")));
        SignInButton.setRolloverIcon(new ImageIcon(getClass().getResource("Graphics/okay_go.png")));
        SignInButton.setVerticalTextPosition(AbstractButton.CENTER);
        SignInButton.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
        SignInButton.setForeground(Color.BLACK.brighter());
        SignInButton.setFont(new Font("gotham light", Font.PLAIN, 14));
        SignInButton.setBounds(20, 155, 100, 30);
        SignInButton.setBorder(BorderFactory.createEmptyBorder());
        SignInButton.setBackground(new Color(55, 157, 250));
        SignInButton.setEnabled(false);
        SignInButton.putClientProperty("JComponent.sizeVariant", "mini");


        ImageIcon helpIcon = new ImageIcon(getClass().getResource("Graphics/help_52px.png"));
        ImageIcon helpIcon2 = new ImageIcon(getClass().getResource("Graphics/help_48px.png"));

        exitIcon = new ImageIcon(getClass().getResource("Graphics/shutdown_52px.png"));
        ImageIcon exitIcon2 = new ImageIcon(getClass().getResource("Graphics/shutdown_48px.png"));

        ImageIcon addIcon = new ImageIcon(getClass().getResource("Graphics/plus_52px.png"));
        ImageIcon addIcon2 = new ImageIcon(getClass().getResource("Graphics/plus_48px2.png"));

        JButton addAccountButton = new JButton(addIcon);
        addAccountButton.setRolloverIcon(addIcon2);
        addAccountButton.setBackground(Color.decode("#0081AB"));
        addAccountButton.setBounds(720, 183, 24, 24);
        addAccountButton.setBorder(emptyBorder);
        addAccountButton.setToolTipText("Click to addAccount.");
        addAccountButton.addActionListener(e -> {
            frame.setVisible(false);
            frame2.setVisible(true);

            new Thread(() -> {
                int yMin = headSignUp.getY() - 15;
                int yMax = headSignUp.getY() + headSignUp.getHeight() - 40;

                try {
                    for (int f = 0; f < 3; f++) {
                        for (int i = yMin; i <= yMax; i++) {
                            headerLabel.setBounds(headerLabel.getX(), i, headerLabel.getWidth(), headerLabel.getHeight());
                            Thread.sleep(10);
                        }
                        headerLabel.setForeground(headerColor[f]);
                        for (int i = yMax; i >= yMin; i--) {
                            headerLabel.setBounds(headerLabel.getX(), i, headerLabel.getWidth(), headerLabel.getHeight());
                            Thread.sleep(10);
                        }
                        headerLabel.setForeground(headerColor[f + 1]);
                    }
                    for (int f = 0; f < 20; f++) {
                        headerLabel.setBounds(10, 50, 800, 70);
                        for (int i = headerLabel.getX(); i < 120; i++) {
                            headerLabel.setBounds(i, headerLabel.getY(), headerLabel.getWidth(), headerLabel.getHeight());
                            Thread.sleep(10);
                        }
                        headerLabel.setForeground(headerColor[(new Random()).nextInt(headerColor.length)]);
                        for (int i = headerLabel.getX(); i >= 10; i--) {
                            headerLabel.setBounds(i, headerLabel.getY(), headerLabel.getWidth(), headerLabel.getHeight());
                            Thread.sleep(10);
                        }
                        headerLabel.setForeground(headerColor[(new Random()).nextInt(headerColor.length)]);
                    }
                } catch (InterruptedException ignored) {
                }
            }).start();

        });

        JButton helpButton = new JButton(helpIcon);
        helpButton.setRolloverIcon(helpIcon2);
        helpButton.setBackground(Color.decode("#0081AB"));
        helpButton.setBounds(720, 212, 24, 24);
        helpButton.setBorder(emptyBorder);
        helpButton.setToolTipText("Click for help.");

        JButton exitButton = new JButton(exitIcon);
        exitButton.setRolloverIcon(exitIcon2);
        exitButton.setBackground(Color.decode("#0081AB"));
        exitButton.setBounds(720, 241, 24, 24);
        exitButton.setBorder(emptyBorder);
        exitButton.setToolTipText("Click to Exit.");

        errorConnectingLabel = new JTextField("A server connection is required.  Please connect to the server and try again");
        errorConnectingLabel.setEditable(false);
        errorConnectingLabel.setFont(new Font("Eras Light ITC", Font.PLAIN, 14));
        errorConnectingLabel.setBackground(Color.RED);
        errorConnectingLabel.setForeground(Color.WHITE);
        errorConnectingLabel.setBounds(130, 280, 460, 25);

        JPanel signUpPanel = new welPanelClass();
        signUpPanel.setBounds(20, 200, 750, 270);
        signUpPanel.setBorder(titledBorder);
        signUpPanel.setLayout(null);
        signUpPanel.add(userLabel);
        signUpPanel.add(userField);
        signUpPanel.add(passLabel);
        signUpPanel.add(passField);
        signUpPanel.add(SignInButton);
        signUpPanel.add(helpButton);
        signUpPanel.add(exitButton);
        signUpPanel.add(addAccountButton);
        //  signUpPanel.add(loadingIcon);
        signUpPanel.add(errorConnectingLabel);

        dataEntryPanel = new welPanelClass();
        dataEntryPanel.setLayout(null);

        dataEntryPanel.add(logoLabel);
        dataEntryPanel.add(logoLabel2);
        dataEntryPanel.add(logoIcon);
        dataEntryPanel.add(signUpPanel);

        SignInButton.addActionListener(e -> {
            avoidDuplication2=true;
            avoidDuplication3=true;
            avoidDuplication4=true;
            loadingDialog.setVisible(true);
        });
        helpButton.addActionListener(e -> {
            JTextArea helpText = new JTextArea("In the 'Username field' enter your First name \n" +
                    "while in the 'Password field' enter your Admission number.\n" +
                    "Don't forget to use slashes('/') in the password field.");
            helpText.setBackground(new Color(240, 240, 240));
            helpText.setFont(new Font("Calibri Light", Font.PLAIN, 14));
            JOptionPane.showMessageDialog(frame, helpText, "Help Signing in", JOptionPane.INFORMATION_MESSAGE, helpIcon2);
        });
        exitButton.addActionListener(e -> exitMethod());
    }

    private void initializeSignUp() {

        TitledBorder titledBorder = BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Create a Students Account", TitledBorder.LEFT, TitledBorder.ABOVE_TOP);
        titledBorder.setTitleColor(new Color(250, 250, 250).brighter());
        titledBorder.setTitleFont(new Font("gotham light", Font.PLAIN, 16));

        TitledBorder datePickerBorder = BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Date of Birth (D.O.B)", TitledBorder.LEFT, TitledBorder.BELOW_TOP);
        datePickerBorder.setTitleColor(new Color(250, 250, 250).brighter());
        datePickerBorder.setTitleFont(new Font("gotham light", Font.PLAIN, 14));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            frame2.setVisible(false);
            frame.setVisible(true);
        });
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> exitMethod());
        JButton createAccountButton = new JButton("Create an Account");
        createAccountButton.setFont(new Font("gotham light", Font.PLAIN, 16));
        createAccountButton.setBounds(450, 250, 300, 30);
        createAccountButton.addActionListener(e -> {

            createAccountButton.setIcon(new ImageIcon(getClass().getResource("Graphics/loading-icon.gif")));
            new Timer(3000, e1 -> {
                createAccountButton.setIcon(null);
                ((Timer) e1.getSource()).stop();
                signUP();
            }).start();

        });

        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        SqlDateModel model = new SqlDateModel();
        model.setDate(1995, 9, 27);
        model.setSelected(true);
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        datePicker.setBounds(450, 40, 300, 50);
        datePicker.setBorder(datePickerBorder);
        datePicker.setBackground(Color.decode("#2887C8"));
        datePicker.setFont(new Font("gotham light", Font.PLAIN, 16));

        errorDataLabel = new JTextField("Error");
        errorDataLabel.setEditable(false);
        errorDataLabel.setFont(new Font("Eras Light ITC", Font.PLAIN, 14));
        errorDataLabel.setBackground(Color.RED);
        errorDataLabel.setForeground(Color.WHITE);
        errorDataLabel.setBounds(200, 310, 300, 25);

        frame2Panel = new welPanelClass();
        frame2Panel.setLayout(null);


        headerLabel = new JLabel("MASENO UNIVERSITY");
        headerLabel.setFont(new Font("Times New Roman", Font.BOLD, 50));
        headerLabel.setForeground(Color.decode("#FF6037"));
        headerLabel.setBounds(10, 50, 800, 70);

        headSignUp = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(new ImageIcon(getClass().getResource("graphics/msu_banner2.png")).getImage(), 0, 0, 800, 140, null);
                g.drawImage(new ImageIcon(getClass().getResource("graphics/Maseno_University_Official_avatar.png")).getImage(), 640, -5, null);
            }
        };
        headSignUp.setBounds(0, 0, 800, 140);
        headSignUp.setLayout(null);
        headSignUp.add(headerLabel);

        JPanel navPanel = new welPanelClass();
        navPanel.setBounds(5, 310, 770, 35);
        navPanel.setLayout(new BorderLayout());
        navPanel.setBackground(Color.LIGHT_GRAY);
        navPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        navPanel.add(backButton, BorderLayout.WEST);
        navPanel.add(closeButton, BorderLayout.EAST);

        JPanel enterDataPanel = new welPanelClass();
        enterDataPanel.setBorder(titledBorder);
        enterDataPanel.setBounds(5, 140, 780, 350);
        enterDataPanel.setLayout(null);

        int spacing = 50;
        for (int i = 0; i < textFieldArrayHint.length; i++) {

            textFieldArray[i] = new JTextField();
            textFieldArray[i].setBorder(BorderFactory.createCompoundBorder(emptyBorder, loweredBorder));
            textFieldArray[i].setFont(new Font("gotham light", Font.PLAIN, 14));
            textFieldArray[i].setBounds(10, spacing, 300, 30);
            textFieldArray[i].setUI(new JTextFieldHintUI(textFieldArrayHint[i], Color.GRAY));

            errorIcons[i] = new JLabel(new ImageIcon(getClass().getResource("Graphics/error_48px.png")));
            errorIcons[i].setToolTipText("Please enter " + textFieldArrayHint[i]);
            errorIcons[i].setVisible(false);
            errorIcons[i].setBounds(310, spacing, 24, 24);
            spacing += 60;
            enterDataPanel.add(textFieldArray[i]);
            enterDataPanel.add(errorIcons[i]);
            textFieldArray[i].addKeyListener(new keyHandler2());
        }
        TitledBorder courseBorder = BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Select a Course", TitledBorder.CENTER, TitledBorder.BELOW_TOP);
        courseBorder.setTitleColor(new Color(250, 250, 250).brighter());
        courseBorder.setTitleFont(new Font("gotham light", Font.PLAIN, 11));
        courseBorder.setTitleColor(new Color(27, 99, 250).brighter());


        courseComboBox = new JComboBox<>(courses);
        courseComboBox.setBorder(courseBorder);
        courseComboBox.setBounds(10, 230, 300, 40);
        courseComboBox.setFont(new Font("gotham light", Font.PLAIN, 13));

        enterDataPanel.add(courseComboBox);
        enterDataPanel.add(datePicker);
        enterDataPanel.add(createAccountButton);
        enterDataPanel.add(navPanel);
        enterDataPanel.add(errorDataLabel);

        frame2Panel.add(headSignUp);
        frame2Panel.add(enterDataPanel);

    }

    private class welPanelClass extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.decode("#02A4D3"));

            ImageIcon backIcon = new ImageIcon(getClass().getResource("Graphics/back_img1.png"));
            Image backImg = backIcon.getImage();

            g.fillRect(0, 0, dataEntryPanel.getWidth(), dataEntryPanel.getHeight());
            g.drawImage(backImg, 0, 0, null);
        }
    }

    private class keyHandler extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {

            if (userField.getText().length() > 2 && passField.getText().length() > 2) {
                SignInButton.setEnabled(true);

                //noinspection StatementWithEmptyBody
                if (e.getKeyCode() == 10) {
                    loadingDialog.setVisible(true);
                }
            } else {
                SignInButton.setEnabled(false);
            }
        }
    }

    private void triggerSignIn() {

        int Access = 0;
        Boolean serverDisconnected = false;
        try {
            Access = dbHandler.signIn(userField.getText().toLowerCase(), passField.getText().toLowerCase());
        } catch (NullPointerException ignored) {
            int errorPos = errorConnectingLabel.getY();
            serverDisconnected = true;

            new Thread(() -> {
                try {
                    for (int i = errorPos; i >= 240; i--) {
                        errorConnectingLabel.setBounds(130, i, 460, 25);
                        Thread.sleep(10);
                    }
                } catch (InterruptedException ignored2) {/*ignored*/
                }
            }).start();
        } finally {
            if (Access == 1) {

                if (avoidDuplication) {
                    //Grant Access;
                    frame.dispose();
                    frame2.dispose();
                    new DetailsClass(userField.getText(), passField.getText());
                    avoidDuplication = false;
                }


            } else if (Access == 2) {
                //rare case, unlikely to occur, just taking precautions
                if (avoidDuplication2) {
                JOptionPane.showMessageDialog(frame, "Multiple Accounts exist with that username,\nSorry we can't let you in,\nDo Contact the database admin",
                        "Error, Multiple Accounts Detected", JOptionPane.ERROR_MESSAGE);}
                avoidDuplication2 = false;
            } else if (Access == 3) {
                if (avoidDuplication3) {
                    JOptionPane.showMessageDialog(frame, "Invalid password. Please try again.",
                            "Password For That Account is invalid", JOptionPane.ERROR_MESSAGE);
                    avoidDuplication3 = false;
                }
            }else {
                if (avoidDuplication4) {
                    if (!serverDisconnected) {
                        JOptionPane.showMessageDialog(frame, "Account does not exist!\n Please contact the database admin\nfor assistance",
                                "Error Message", JOptionPane.ERROR_MESSAGE);
                    }
                    avoidDuplication4 = false;
                }
            }
        }
    }

    private void exitMethod() {
        try {
            int randi = rand.nextInt(3) + 1;
            ImageIcon byeIcon = new ImageIcon(getClass().getResource("Graphics/pic1 (" + randi + ").png"));
            JLabel label = new JLabel("Thanks For Reaching This Far. Goodbye!", exitIcon, SwingConstants.HORIZONTAL);
            label.setFont(new Font("Calibri Light", Font.PLAIN, 14));

            JOptionPane.showMessageDialog(frame, label, "Action Exit", JOptionPane.INFORMATION_MESSAGE, byeIcon);
            Thread.sleep(2000);

        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        System.exit(0);
    }

    private class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

        private final String datePattern = "yyyy-MM-dd";
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }

            return "";
        }
    }

    private void signUP() {
        Boolean inadequate = false;
        for (int i = 0; i < textFieldArrayHint.length; i++) {
            if (textFieldArray[i].getText().length() < 1) {
                errorIcons[i].setVisible(true);
                inadequate = true;
            }
        }
        if (inadequate) {
            setErrorDataLabel("Please fill in all the details, inorder to proceed");
            return;
        }
        String fName = textFieldArray[0].getText();
        String lName = textFieldArray[1].getText();
        String adm = textFieldArray[2].getText();
        String course = (String) courseComboBox.getSelectedItem();
        java.sql.Date selectedDate = (java.sql.Date) datePicker.getModel().getValue();

        if (dbHandler.signUp(fName, lName, adm, selectedDate, course)) {
            setErrorDataLabel("The account already exist, proceed with login in!");
        } else {

            JOptionPane.showMessageDialog(frame2, "Account was created successfully, \nPlease proceed with logging in...",
                    "Account Added Successfully!", JOptionPane.INFORMATION_MESSAGE);

            frame2.setVisible(false);
            frame.setVisible(true);
        }

    }

    private class keyHandler2 extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getSource() == textFieldArray[0]) {
                errorIcons[0].setVisible(false);
            } else if (e.getSource() == textFieldArray[1]) {
                errorIcons[1].setVisible(false);
            } else if (e.getSource() == textFieldArray[2]) {
                errorIcons[2].setVisible(false);
            } else {
                errorIcons[3].setVisible(false);
            }
            int errorPos = errorDataLabel.getY();

            if (errorPos <= 285) {
                new Thread(() -> {
                    try {
                        for (int f = errorPos; f < 310; f++) {
                            errorDataLabel.setBounds(200, f, 300, 25);
                            Thread.sleep(10);
                        }
                    } catch (InterruptedException ignored) {
                    }
                }).start();
            }
        }
    }

    private void setErrorDataLabel(String whatToSay) {

        errorDataLabel.setText(whatToSay);
        int errorPos = errorDataLabel.getY();

        new Thread(() -> {
            try {
                for (int f = errorPos; f >= 285; f--) {
                    errorDataLabel.setBounds(200, f, 300, 25);
                    Thread.sleep(10);
                }
            } catch (InterruptedException ignored) {
            }
        }).start();
    }
}