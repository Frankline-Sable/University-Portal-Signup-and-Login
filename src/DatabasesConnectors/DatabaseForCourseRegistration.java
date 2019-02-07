package DatabasesConnectors;

import java.sql.*;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Class has been Written by Frankline Sable, As on 10/29/2016
 * You have no write to modify anything written here, unless you've
 * consulted me.
 */
public class DatabaseForCourseRegistration {

    private Statement stmt;
    private Connection conn = null;

    public DatabaseForCourseRegistration() {
        try {
            conn = DriverManager.getConnection("" +
                    "jdbc:mysql://localhost/database_revised?user=root&password=");//Testing Whether There is still a connection
            stmt = null;

        } catch (SQLException ex) {

            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    public void createTable(String username) {
        String query = "CREATE TABLE " + username + " ( Course_Code TEXT, Course_Name TEXT,Lecturer_Name TEXT, YEAR INT, Semester INT, Registration_Status TEXT )";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);

        } catch (SQLException ex) {

            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    public Boolean updateTableStudent(String Values[], String username) {
        String query = "INSERT INTO " + username + " VALUES(\"" + Values[0] + "\",\"" + Values[1] + "\",\"" + Values[2] + "\"," + Values[3] + "," + Values[4] + ",\"" + "REGISTERED" + "\")";
        String query2 = "SELECT *FROM " + username;
        ResultSet rs = null;
        Boolean exist = false;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query2);

            while (rs.next()) {
                String data = rs.getString("Course_Code");
                if (data.equalsIgnoreCase(Values[0])) {
                    exist = true;
                }
            }
            if (!exist)
                stmt.executeUpdate(query);

        } catch (SQLException ex) {

            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return exist;
    }

    public void deleteData(String row, String username) {

        Statement stmt = null;
        ResultSet rs = null;
        String query = "DELETE FROM " + username + " WHERE Course_Name=\"" + row + "\"";


        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);

        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        }
    }

    public void checkData(String username) {

        String query1 = "SELECT *FROM " + username;
        String query2 = "SELECT *FROM lecturerandcourse";
        String[] data1 = new String[100];
        String[] data2 = new String[100];
        int count = 0, count2 = 0;

        Statement stmt = null;
        ResultSet rs = null;
        ResultSet rs2 = null;

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query1);
            while (rs.next()) {

                data1[count] = rs.getString("Course_Code");
                count++;
            }
            rs2 = stmt.executeQuery(query2);
            while (rs2.next()) {

                data2[count2] = rs2.getString("Course_Code");
                count2++;
            }


        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        } finally {
            String[] arrayFormatted = compareArrays(data1, data2);

            String queryA = "UPDATE lecturerandcourse SET Registration_Status=\"NOT REGISTERED\"";

            try {
                stmt.executeUpdate(queryA);
                for (int i = 0; i < arrayFormatted.length; i++) {

                    if (arrayFormatted[i] != null) {
                        String queryB = "UPDATE lecturerandcourse SET Registration_Status=\"REGISTERED\" WHERE Course_Code=\"" + arrayFormatted[i] + "\"";
                        stmt.executeUpdate(queryB);

                    }
                }
            } catch (SQLException ex) {
                System.err.println("SQLException: " + ex.getMessage());
                System.err.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());

            }
        }
    }

    private String[] compareArrays(String data1[], String data2[]) {
        String arr[] = new String[data1.length];
        int count = 0;
        for (int j = 0; j < 100; j++) {
            for (int i = 0; i < 100; i++) {

                if (data2[i] != null || data1[i] != null) {
                    if (data2[i].equals(data1[j])) {
                        arr[count] = data1[j];
                        count++;
                    }
                } else {
                    break;
                }
            }
        }
        return new HashSet<>(Arrays.asList(arr)).toArray(new String[0]);
    }

}
