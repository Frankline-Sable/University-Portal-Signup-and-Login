package BackEnd;

import DatabasesConnectors.DatabaseForCourseRegistration;
import DatabasesConnectors.DatabaseTabulation;
import FrontEnd.credentialsVerifier;
import net.proteanit.sql.DbUtils;
import support.SpotLight2;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.LayerUI;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Class has been Written by Frankline Sable, As on 10/28/2016
 * You have no write to modify anything written here, unless you've
 * consulted me.
 */
public class DetailsClass {

    private final String password, username;
    private final DatabaseTabulation dbHandler;
    private JFrame frame = null;
    private final JTable table2;
    private final String[] academicYear = {"First Year", "Second Year", "Third Year", "Fourth Year"};
    private final String[] academicSem = {"First Semester", "Second Semester"};
    private final JComboBox comboBox, comboBox2;
    private final Random rand = new Random();
    private final JLabel emptyLabel;
    private final JLabel updateLabel;
    private final JTabbedPane tabbedPane;
    private JScrollPane scrollPaneUserData;
    private final JPanel tabPanel1;
    private String[] arrCoursesOffered;
    private JTable tableOne;
    private JComboBox<String> comboBox4;
    private JComboBox<String> comboBox5;
    private JLabel emptyLabel2;
    private final Border loweredBorder;
    private JList leftList, rightList;
    private final String[] registeredData = new String[10];
    private final String[] deleteData = new String[10];
    private DatabaseForCourseRegistration db;
    private JLabel[] helpLabel = new JLabel[100];
    private Thread threadHandler;
    private final Color commandColor[] = {Color.RED, Color.WHITE, Color.BLUE, Color.GREEN, Color.CYAN, Color.LIGHT_GRAY,        //Creating and initialising array of colours
            Color.ORANGE, Color.PINK, Color.YELLOW, Color.MAGENTA, new Color(139, 0, 0), new Color(221, 160, 221),
            new Color(218, 165, 32), new Color(128, 0, 128), new Color(128, 128, 0), new Color(0, 128, 128)};
    private String movingStringsHelps[] = {"", "Select and Press The Right-Arrowed Button to Register Course(s)", "Select and Press The Left-Arrowed to De-register Course(s)"
            , "Don't forget to Log out once you're done", "Keep an eye out for bugs & Inform The Admin, i.e Mr.Wafula", "You can alternatively, view the online database for the schematics"};

    public DetailsClass(String username, String password) {
        this.password = password;
        this.username = username;
        //We are only depending on the unique field which is the password field and username field from the other classes

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
        dbHandler = new DatabaseTabulation();
        db = new DatabaseForCourseRegistration();

        int check = dbHandler.checkTableExistence(username);
        if (check == 0) {
            db.createTable(username);
            dbHandler.updateTableExistence(username);
        }

        loweredBorder = BorderFactory.createLoweredBevelBorder();

        TitledBorder regCourse = BorderFactory.createTitledBorder(loweredBorder, "Courses You Have Registered Will be Displayed Below", TitledBorder.LEFT, TitledBorder.BELOW_TOP);
        regCourse.setTitleColor(new Color(0, 102, 204).brighter());
        regCourse.setTitleFont(new Font("Gotham Light", Font.PLAIN, 16));

        TitledBorder courseRegBod = BorderFactory.createTitledBorder(loweredBorder, "Filter Data", TitledBorder.LEFT, TitledBorder.BELOW_TOP);
        courseRegBod.setTitleColor(new Color(0, 102, 204).brighter());
        courseRegBod.setTitleFont(new Font("Gotham Light", Font.BOLD, 16));

        JButton viewOnlineDb = new JButton("Online Database");
        viewOnlineDb.setFont(new Font("gotham light", Font.PLAIN, 12));
        viewOnlineDb.setBounds(610, 440, 130, 30);
        viewOnlineDb.addActionListener(e -> {

            Desktop desktop = Desktop.getDesktop();
            viewOnlineDb.setIcon(new ImageIcon(getClass().getResource("Graphics/loading-icon.gif")));
            new Timer(2000, e1 -> {
                viewOnlineDb.setIcon(null);
                URI uri = null;
                try {
                    uri = new URI("http://localhost/phpmyadmin/sql.php?server=1&db=database_revised&table=lecturerandcourse&pos=0&token=0b42ccef250952367592a704b166acac");
                    desktop.browse(uri);
                } catch (IOException ioe) {
                    JOptionPane.showMessageDialog(frame, "The system cannot find the \n" + uri + " \nfile specified", "Unable, To Connect To Database Server", JOptionPane.WARNING_MESSAGE);

                } catch (URISyntaxException use) {
                    JOptionPane.showMessageDialog(frame, "Illegal character in URI path", "Unable, To Connect To Database Server", JOptionPane.WARNING_MESSAGE);
                }
                ((Timer) e1.getSource()).stop();
            }).start();
        });

        JButton logOut = new JButton("Log out");
        logOut.setFont(new Font("gotham light", Font.PLAIN, 12));
        logOut.setBounds(750, 440, 120, 30);
        logOut.addActionListener(
                e -> {
                    logOut.setIcon(new ImageIcon(getClass().getResource("Graphics/loading-icon.gif")));
                    new Timer(3000, e1 -> {
                        dbHandler.saveDbResources();
                        frame.dispose();
                        new credentialsVerifier();
                        ((Timer) e1.getSource()).stop();
                    }).start();

                });

        emptyLabel = new JLabel("Oops! You haven't Registered For any units Here", new ImageIcon(getClass().getResource("graphics/emo_im_crying.png")), JLabel.CENTER);
        emptyLabel.setForeground(new Color(0, 157, 196));
        emptyLabel.setFont(new Font("gotham light", Font.PLAIN, 16));
        emptyLabel.setBounds(200 + 100, 270 / 2 - 20, 450, 30);
        emptyLabel.setVisible(false);

        updateLabel = new JLabel();
        updateLabel.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        updateLabel.setBounds(10, 450, 500, 30);

        //noinspection unchecked
        comboBox = new JComboBox(academicYear);
        comboBox.setSelectedIndex(2);
        comboBox.setBorder(BorderFactory.createRaisedBevelBorder());
        comboBox.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        comboBox.addActionListener(new comboListener());

        //noinspection unchecked
        comboBox2 = new JComboBox(academicSem);
        comboBox2.setBorder(comboBox.getBorder());
        comboBox2.setSelectedIndex(0);
        comboBox2.setFont(comboBox.getFont());
        comboBox2.addActionListener(new comboListener());

        LayerUI<JComboBox> layerUI = new SpotLight2();

        JLayer<JComboBox> comboBoxJLayer = new JLayer<>(comboBox, layerUI);
        comboBoxJLayer.setBounds(5, 50, 130, 30);

        JLayer<JComboBox> comboBox2JLayer = new JLayer<>(comboBox2, layerUI);
        comboBox2JLayer.setBounds(5, 150, 130, 30);

        table2 = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };

        JScrollPane scrollPane2 = new JScrollPane(table2);
        scrollPane2.setBorder(regCourse);
        scrollPane2.setBounds(200, 0, 875 - 200, 230);

        ResultSet rs = dbHandler.reg_CourseInfo(3, 1, username);
        table2.setBackground(new Color(240, 240, 240));
        table2.setModel(DbUtils.resultSetToTableModel(rs));
        table2.setPreferredScrollableViewportSize(new Dimension(900, 270));
        table2.setFillsViewportHeight(true);

        table2.getTableHeader().setBackground(new Color(249, 233, 192));
        table2.getTableHeader().setForeground(new Color(0, 124, 218));
        table2.getTableHeader().setFont(new Font("New Times Roman", Font.PLAIN, 13));
        table2.setFont(new Font("Calibri", Font.PLAIN, 13));
        table2.setAutoCreateRowSorter(true);

        if (table2.getRowCount() < 1) {
            emptyLabel.setVisible(true);
        }
        tabOnePanel();
        tabPanel1 = new JPanel();
        tabPanel1.setBackground(new Color(208, 208, 208));
        tabPanel1.setLayout(null);

        JPanel courseRegPanel = new JPanel();
        courseRegPanel.setBounds(5, 100, 875, 270);
        courseRegPanel.setLayout(null);
        courseRegPanel.setBorder(courseRegBod);
        courseRegPanel.add(emptyLabel);
        courseRegPanel.add(scrollPane2);
        courseRegPanel.add(comboBoxJLayer);
        courseRegPanel.add(comboBox2JLayer);

        tabPanel1.add(logOut);
        tabPanel1.add(viewOnlineDb);
        tabPanel1.add(scrollPaneUserData);
        tabPanel1.add(courseRegPanel);
        tabPanel1.add(updateLabel);

        JPanel tabPanel3 = new JPanel();
        tabPanel3.setLayout(null);
        tabPanel3.add(jListRegisterCourse());
        tabPanel3.setBackground(Color.decode("#AF6E4D"));

        // helpLabel.setBounds(50,20,810,50);

        for (int i = 0; i < movingStringsHelps.length; i++) {

            helpLabel[i] = new JLabel(movingStringsHelps[i]);
            helpLabel[i].setFont(new Font("gotham light", Font.BOLD, 23));
            tabPanel3.add(helpLabel[i]);
            helpLabel[i].setBounds(-810, 20, 820, 50);
        }
        helpLabel[0].setText("Welcome "+username+",to the Course Reg / De-reg Screen");

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        tabbedPane.addTab("Your Information", null, tabPanel1, "View all your details here");
        tabbedPane.addTab("Courses Offered", null, tabPanel3, "Register course here");
        tabbedPane.addChangeListener(new tabHandler());

        frame = new JFrame("Welcome To Maseno university Portal");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(900, 550);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(Color.GRAY);
        frame.setLayout(null);
        frame.setLayout(new BorderLayout());
        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.setIconImage((new ImageIcon(getClass().getResource("graphics/angrybirdsrio.png"))).getImage());

       













	   frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Object[] options = {"Yes, please", "No, thanks"};
                ////new Short_AudioClass("A7");
                ImageIcon exitIcon = new ImageIcon(getClass().getResource("Graphics/exit" + rand.nextInt(3) + ".png"));
                int m = JOptionPane.showOptionDialog(frame, "Do you really want to exit?", "Exit The Student Portal",        //dialog to ask if they want to exit
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, exitIcon, options, options[1]);

                if (m == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }

    private void tabOnePanel() {

        TitledBorder titledBorder = BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "All Your Information Is Contained In This Table", TitledBorder.LEFT, TitledBorder.BELOW_BOTTOM);
        titledBorder.setTitleFont(new Font("gotham light", Font.PLAIN, 14));

        ResultSet rs = dbHandler.fetchData(password);
        tableOne = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return !(column == 1 || column == 2);
            }
        };
        tableOne.setModel(DbUtils.resultSetToTableModel(rs));
        tableOne.setPreferredScrollableViewportSize(new Dimension(850,270));
        tableOne.setBackground(new Color(211, 195, 156));
        tableOne.setFillsViewportHeight(true);
        tableOne.getTableHeader().setBackground(new Color(138, 255, 110));
        tableOne.getModel().addTableModelListener(new userTableListener());
        tableOne.setSelectionBackground(new Color(180, 164, 126));
        tableOne.setFont(new Font("Serif", Font.PLAIN, 14));
        tableOne.getTableHeader().setForeground(new Color(0, 124, 218));
        tableOne.getTableHeader().setFont(new Font("New Times Roman", Font.PLAIN, 13));
        tableOne.setBorder(titledBorder);

        scrollPaneUserData = new JScrollPane(tableOne);
        scrollPaneUserData.setBounds(0, 20, 880, 60);
        scrollPaneUserData.setBorder(loweredBorder);
    }

    private class userTableListener implements TableModelListener {
        @Override
        public void tableChanged(TableModelEvent e) {
            int row = e.getFirstRow();
            int column = e.getColumn();
            TableModel model = (TableModel) e.getSource();
            String columnName = model.getColumnName(column);
            Object data = model.getValueAt(row, column);

            String dataString = data.toString();

            columnName = columnName.replace(" ", "");
            Boolean update = true;

            if (column == 3) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy", Locale.US);
                try {
                    Date date = dateFormat.parse(dataString);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MMMM/yyyy", Locale.US);
                    dataString = dateFormat2.format(calendar.getTime());
                } catch (ParseException ex) {
                    SimpleDateFormat dateFormat3 = new SimpleDateFormat("dd/MMMM/yyyy", Locale.US);
                    try {
                        Date date2 = dateFormat3.parse(dataString);
                        Calendar calendar4 = Calendar.getInstance();
                        calendar4.setTime(date2);
                        dataString = dateFormat3.format(calendar4.getTime());
                    } catch (ParseException ex2) {
                        update = false;
                        JOptionPane.showMessageDialog(frame, "The date is not valid!", "Invalid date!", JOptionPane.ERROR_MESSAGE);
                        ResultSet rs = dbHandler.fetchData(password);
                        tableOne.setModel(DbUtils.resultSetToTableModel(rs));
                        tableOne.getModel().addTableModelListener(new userTableListener());
                    }
                }
            } else {
                if (dataString.length() < 3) {
                    update = false;
                    JOptionPane.showMessageDialog(frame, "Enter a valid name", "Name Invalid!", JOptionPane.ERROR_MESSAGE);
                    ResultSet rs = dbHandler.fetchData(password);
                    tableOne.setModel(DbUtils.resultSetToTableModel(rs));
                    tableOne.getModel().addTableModelListener(new userTableListener());
                }
            }
            if (update) {
                Boolean updated = dbHandler.updateUserData(columnName, dataString, password);

                if (updated) {
                    // Do something with the data...
                    updateLabel.setText(columnName.replace("_", " ") + " updated successfully!");
                    ResultSet rs = dbHandler.fetchData(password);
                    tableOne.setModel(DbUtils.resultSetToTableModel(rs));
                    tableOne.getModel().addTableModelListener(new userTableListener());

                } else {
                    updateLabel.setText("Action Not Allowed for Column " + columnName.replace("_", " "));
                    Toolkit.getDefaultToolkit().beep();
                }
                Timer removeUpdate = new Timer(3000, e1 -> updateLabel.setText(""));
                removeUpdate.start();
            }
        }
    }

    private class comboListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            int indexA = comboBox.getSelectedIndex();
            int indexB = comboBox2.getSelectedIndex();

            ResultSet rs = dbHandler.reg_CourseInfo(indexA + 1, indexB + 1, username);
            table2.setModel(DbUtils.resultSetToTableModel(rs));

            if (table2.getRowCount() < 1) {
                emptyLabel.setVisible(true);
            } else {
                emptyLabel.setVisible(false);
            }

        }
    }

    private class comboListener2 implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            int indexA = comboBox4.getSelectedIndex();
            int indexB = comboBox5.getSelectedIndex();

            arrCoursesOffered = dbHandler.fetchCourses(indexA + 1, indexB + 1);
            rightList.setListData(arrCoursesOffered);

            if (rightList.getLastVisibleIndex() > 80) {
                emptyLabel2.setVisible(true);
            } else {
                emptyLabel2.setVisible(false);
            }
        }
    }

    private class tabHandler implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            ////new Short_AudioClass("A19");
            switch (tabbedPane.getSelectedIndex()) {
                case 0:
                    int indexA = comboBox.getSelectedIndex();
                    int indexB = comboBox2.getSelectedIndex();

                    ResultSet rs = dbHandler.reg_CourseInfo(indexA + 1, indexB + 1, username);
                    table2.setModel(DbUtils.resultSetToTableModel(rs));

                    if (table2.getRowCount() < 1) {
                        emptyLabel.setVisible(true);
                    } else {
                        emptyLabel.setVisible(false);
                    }
                    threadHandler.stop();
                    break;
                case 1:
                    //Do something in the Course tab
                    for (int i = 0; i < movingStringsHelps.length; i++) {
                        helpLabel[i].setBounds(-810, 20, 820, 50);
                    }
                    try {
                        threadHandler.stop();
                        threadHandler = new movingStringHandler();
                        threadHandler.start();
                    } catch (Exception e2) {
                        threadHandler = new movingStringHandler();
                        threadHandler.start();
                    }
                    break;
                default:
                    //do nothing
            }
        }
    }

    private JPanel jListRegisterCourse() {

        TitledBorder border1 = new TitledBorder(BorderFactory.createRaisedBevelBorder(), "Courses Available For Registration", TitledBorder.CENTER, TitledBorder.BELOW_TOP);
        border1.setTitleFont(new Font("Gotham light", Font.PLAIN, 12));

        TitledBorder border2 = new TitledBorder(BorderFactory.createRaisedBevelBorder(), "Courses Picked For Registration", TitledBorder.CENTER, TitledBorder.BELOW_TOP);
        border2.setTitleFont(new Font("Gotham light", Font.PLAIN, 12));

        TitledBorder border3 = new TitledBorder(BorderFactory.createRaisedBevelBorder(), "Dear " + username + ", Please proceed to register for courses", TitledBorder.CENTER, TitledBorder.BELOW_TOP);
        border3.setTitleFont(new Font("Gotham light", Font.BOLD, 15));

        comboBox4 = new JComboBox<>(academicYear);
        comboBox4.setSelectedIndex(2);
        comboBox4.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        comboBox4.addActionListener(new comboListener2());

        comboBox5 = new JComboBox<>(academicSem);
        comboBox5.setSelectedIndex(0);
        comboBox5.setFont(comboBox.getFont());
        comboBox5.addActionListener(new comboListener2());

        arrCoursesOffered = dbHandler.fetchCourses(comboBox4.getSelectedIndex() + 1, comboBox5.getSelectedIndex() + 1);

        rightList = new JList(arrCoursesOffered);
        rightList.setFont(new Font("Gotham light", Font.PLAIN, 14));
        rightList.setVisibleRowCount(5);
        rightList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        leftList = new JList();
        leftList.setFont(new Font("Gotham light", Font.PLAIN, 14));
        leftList.setVisibleRowCount(10);
        leftList.setFixedCellWidth(100);
        leftList.setFixedCellHeight(20);
        leftList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        emptyLabel2 = new JLabel("Courses Not Yet Updated", new ImageIcon(getClass().getResource("graphics/emo_im_embarrassed.png")), JLabel.LEFT);
        emptyLabel2.setHorizontalTextPosition(JLabel.CENTER);
        emptyLabel2.setVerticalTextPosition(JLabel.BOTTOM);
        emptyLabel2.setFont(new Font("gotham Light", Font.PLAIN, 18));
        emptyLabel2.setForeground(Color.BLUE);
        emptyLabel2.setBounds(140, 170, 720, 70);
        emptyLabel2.setVisible(false);

        JScrollPane scrollPane4 = new JScrollPane(rightList);
        scrollPane4.setBounds(120, 20, 295, 365);
        scrollPane4.setBorder(border1);

        JScrollPane scrollPane5 = new JScrollPane(leftList);
        scrollPane5.setBounds(480, 20, 295, 365);
        scrollPane5.setBorder(border2);

        JButton registerRight = new JButton(new ImageIcon(getClass().getResource("graphics/forward_50px.png")));
        registerRight.setRolloverIcon(new ImageIcon(getClass().getResource("graphics/forward_50px2.png")));
        registerRight.setBounds(415, 160, 60, 40);

        JButton registerLeft = new JButton(new ImageIcon(getClass().getResource("graphics/back_50px.png")));
        registerLeft.setRolloverIcon(new ImageIcon(getClass().getResource("graphics/back_50px2.png")));
        registerLeft.setBounds(415, 210, 60, 40);

        JButton submit = new JButton("Submit");
        submit.setBounds(775, 140, 100, 50);
        submit.addActionListener(e -> {

            buildUserData();
        });
        JButton delete = new JButton("Remove");
        delete.setBounds(775, 200, 100, 50);
        delete.addActionListener(e -> {

            deleteUserData();
        });

        registerRight.addActionListener(
                event -> leftList.setListData(rightList.getSelectedValues())
        );
        registerLeft.addActionListener(
                sable -> rightList.setListData(leftList.getSelectedValues())
        );


        LayerUI<JComboBox> layerUI = new SpotLight2();

        JLayer<JComboBox> comboBox4JLayer = new JLayer<>(comboBox4, layerUI);
        comboBox4JLayer.setBounds(5, 150, 110, 30);

        JLayer<JComboBox> comboBox5JLayer = new JLayer<>(comboBox5, layerUI);
        comboBox5JLayer.setBounds(5, 190, 110, 30);

        JPanel courseRegPanel2 = new JPanel();
        courseRegPanel2.setBounds(5, 90, 900, 400);
        courseRegPanel2.setLayout(null);
        courseRegPanel2.setBorder(border3);
        courseRegPanel2.add(comboBox4JLayer);
        courseRegPanel2.add(comboBox5JLayer);
        courseRegPanel2.add(emptyLabel2);
        courseRegPanel2.add(scrollPane4);
        courseRegPanel2.add(scrollPane5);
        courseRegPanel2.add(registerLeft);
        courseRegPanel2.add(registerRight);
        courseRegPanel2.add(submit);
        courseRegPanel2.add(delete);

        return courseRegPanel2;

    }

    private void buildUserData() {

        int count = 0;
        try {
            for (int i = 0; i < 10; i++) {
                registeredData[i] = (String) leftList.getModel().getElementAt(i);
                count++;
            }
        } catch (IndexOutOfBoundsException ignored) {
        } finally {

            Boolean check = true;
            int check2 = 0;
            if (count < 1) {
                JOptionPane.showMessageDialog(frame, "You haven't selected any of the courses!", "No course selected", JOptionPane.WARNING_MESSAGE);

            } else {
                for (int i = 0; i < count; i++) {
                    if (dbHandler.updateData(registeredData[i], username)) {
                        check2++;

                    } else {
                        JOptionPane.showMessageDialog(frame, registeredData[i] + " has already been registered", "Course Already Registered", JOptionPane.WARNING_MESSAGE);
                        check = false;
                    }
                }
                if (check) {
                    JOptionPane.showMessageDialog(frame, "Courses submitted successfully!\nYou can register for more courses..", "Course have been submitted", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    if (check2 > 0) {
                        JOptionPane.showMessageDialog(frame, "The other unregistered units have\nbeen submitted successfully!", "Course have been submitted", JOptionPane.INFORMATION_MESSAGE);

                    }
                }
            }
        }
    }

    private void deleteUserData() {
        int count = 0;
        try {
            for (int i = 0; i < deleteData.length; i++) {
                deleteData[i] = (String) rightList.getModel().getElementAt(i);
                count++;
            }
        } catch (IndexOutOfBoundsException ignored) {
        } finally {

            if (count < 1) {
                JOptionPane.showMessageDialog(frame, "There are no units to de-register!", "Unable to De-register", JOptionPane.WARNING_MESSAGE);

            } else {
                for (int i = 0; i < count; i++) {
                    db.deleteData(deleteData[i], username);
                }
                if (count == 1)
                    JOptionPane.showMessageDialog(frame, deleteData[0] + " Unit has been de-registered!", "One unit de-registered", JOptionPane.INFORMATION_MESSAGE);
                else
                    JOptionPane.showMessageDialog(frame, count + " units have been de-registered!", "Multiple units De-registered!", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private class movingStringHandler extends Thread {
        @Override
        public void run() {

            try {
                for (;;) {

                    for (int i = 0; i < movingStringsHelps.length; i++) {

                        for (int f = -810; f < 895; f++) {
                            Thread.sleep(5);
                            helpLabel[i].setBounds(f, 20, 820, 50);
                            helpLabel[i].setForeground(commandColor[(new Random()).nextInt(commandColor.length)].brighter());
                            if (f == 50) {
                                Thread.sleep(3000);
                            }
                        }
                    }
                }
            } catch (InterruptedException ignored) {}
        }
    }
}
