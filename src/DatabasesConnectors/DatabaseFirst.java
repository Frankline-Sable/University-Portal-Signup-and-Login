package DatabasesConnectors;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Frankline Sable on 11/26/2016.
 */
public class DatabaseFirst {
    private int  accessLevel=0;
    private int infoVerified=0;
    private Boolean passVerified=false;
    private Connection conn= null;
    private Statement stmt = null;

    public DatabaseFirst(){

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/database_revised?user=root&password=");
            //assume that conn is an already created JDBC connection and its a success
        } catch (SQLException ex) {

            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    public int signIn(String username, String password) {


        ResultSet rs = null;
        PreparedStatement ps = null;
        int count=0;;

        try {
            String query1 = "SELECT * from `students_data` where `First Name`=?";
            String query2 = "SELECT * from `students_data` where `Admn No`=?";

            ps = conn.prepareStatement(query1);
            ps.setString(1, username);
            rs = ps.executeQuery();

            while (rs.next()) {
                count++;
            }
            if (count == 1) {
                infoVerified = 1;

            } else if (count > 1) {
                infoVerified = 11;
            } else {
                infoVerified = 111;
            }


            ps = conn.prepareStatement(query2);
            ps.setString(1, password);
            rs = ps.executeQuery();
            count = 0;

            while (rs.next()) {
                count++;
            }
            passVerified = count >= 1;
        } catch (SQLException ex) {

            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } finally {

            // it is a good idea to release resources in a finally{} block
            // in reverse-order of their creation if they are no-longer needed
            if(infoVerified==1 && passVerified){
                //pass and username correct
                ps=null;
                rs=null;
                String queryExactMatch="SELECT * from `students_data` where `First Name`=? AND `Admn No`=?";
                try {

                    ps = conn.prepareStatement(queryExactMatch);
                    ps.setString(1, username);
                    ps.setString(2, password);
                    rs = ps.executeQuery();
                    count = 0;
                    while (rs.next()) {
                        count++;
                    }
                    if (count >= 1) {
                        passVerified = true;
                        accessLevel=1;
                    } else {
                        passVerified = false;
                        accessLevel=3;
                    }

                } catch (SQLException e) {
                    System.err.println("SQLException: " + e.getMessage());
                    System.err.println("SQLState: " + e.getSQLState());
                    System.out.println("VendorError: " + e.getErrorCode());
                }

            }
            else if(infoVerified==11){
                //pass and username correct but username repeated in database
                accessLevel=2;
            }
            else if(infoVerified==1 && !passVerified){
                //Wrong password
                accessLevel=3;
            }
            else if(infoVerified!=1 && infoVerified!=11 && !passVerified){
                //Wrong info
                accessLevel=4;
            }

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ignored) {
                }    // ignore

            } else if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ignored) {
                } // ignore

            }
        }

        return accessLevel;
    }
    public Boolean signUp(String fName, String sName,String adm,java.sql.Date dob, String cName ){

        String query = "INSERT INTO `students_data` (`First Name`, `Last Name`, `Admn No`, `DOB`, `Course`, `hasTable`) VALUES(\"" +fName + "\",\"" + sName + "\",\"" +  adm+ "\",\"" + dob + "\",\"" + cName+"\",0)";
        String query2 = "SELECT *FROM students_Data";
        ResultSet rs;
        Boolean exist = false;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query2);

            while (rs.next()) {
                String data = rs.getString("Admn No");
                if (data.equalsIgnoreCase(adm)) {
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

}
