package DatabasesConnectors;

import javax.swing.*;
import java.sql.*;
import java.util.Arrays;
import java.util.HashSet;

/**
 * Class has been Written by Frankline Sable, As on 10/28/2016
 * You have no write to modify anything written here, unless you've
 * consulted me.
 */
public class DatabaseTabulation {
    private Statement stmt;
    private ResultSet rs;
    //private PreparedStatement ps;
    private Connection conn = null;

    public DatabaseTabulation() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/database_revised?user=root&password=");
            //assume that conn is an already created JDBC connection and its a success
        } catch (SQLException ex) {

            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    public ResultSet fetchData(String password) {

        String query = "SELECT  `First Name`, `Last Name`, `Admn No`,`DOB`, `Course` FROM students_Data WHERE `Admn No`=\"" + password + "\"";

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

        } catch (SQLException ex) {

            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } finally {
            //////////saveDbResources();
        }
        return rs;
    }

    public String[] fetchCourses(int year, int sem) {
        String query = "SELECT Course FROM lecturerandcourse WHERE Year=" + year + " AND semester=" + sem + " ORDER BY Course ASC";
        String data[] = new String[1000];
        int i = 0;

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                data[i] = rs.getString(1);
                i++;
            }

        } catch (SQLException ex) {

            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } finally {
            ////saveDbResources();
        }
        return data;
    }

    public ResultSet reg_CourseInfo(int year, int sem, String username) {

        String query = "SELECT Course_Code, Course_Name, Lecturer_Name,YEAR, Semester FROM " + username + " WHERE Year=" + year + " AND semester=" + sem + " AND Registration_Status=\"REGISTERED\"";

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

        } catch (SQLException ex) {

            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } finally {
            ////saveDbResources();
        }
        return rs;
    }

    public int checkTableExistence(String username) {
        String query = "SELECT *FROM students_data WHERE `First Name`=\"" + username + "\"";

        int i = 0;

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                i = rs.getInt("hasTable");

            }
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        }
        finally {
            ////saveDbResources();
        }
        return i;
    }

    public void updateTableExistence(String username) {
        String query = "UPDATE students_data SET hasTable=1 WHERE `First Name`=\"" + username + "\"";
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);

        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        } finally {
            ////saveDbResources();
        }
    }

    public Boolean updateData(String course, String username) {

        Statement stmt = null;
        ResultSet rs = null;
        String query = "SELECT *FROM `lecturerandcourse` WHERE Course=\"" + course + "\"";
        Boolean registered=false;

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {

                String[] dataMove = {rs.getString("Course_Code"), rs.getString("Course"), rs.getString("Lecturer_Name"), rs.getString("year"), rs.getString("Semester"), rs.getString("Registration_Status")};
                DatabaseForCourseRegistration db = new DatabaseForCourseRegistration();
                if (db.updateTableStudent(dataMove, username)) {
                    registered=false;
                } else{
                    registered=true;
                }
            }


        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        } finally {
        }
        return registered;

    }

    public boolean updateUserData(String column, String data, String pass) {

        Boolean update = false;
        Statement stmt = null;
        ResultSet rs = null;
        String query = "UPDATE students_data SET " + column + "=\"" + data + "\" WHERE `admn no`=\"" + pass + "\"";

        try {
            if (column.equalsIgnoreCase("dob") || column.equalsIgnoreCase("`First Name`")) {
                stmt = conn.createStatement();
                stmt.executeUpdate(query);
                update = true;
            }


        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());

        } finally {
            //////saveDbResources();
        }
        return update;
    }

    public void saveDbResources() {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ignored) {
            }    // ignore

            rs = null;
        } else if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ignored) {
            } // ignore

            stmt = null;
        }
    }
}


