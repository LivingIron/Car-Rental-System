package org.group29;

import javafx.scene.control.Alert;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaPostgreSQL {

    public static void writeToDatabase(String userName, String userPassword){
        String url = "jbdc:postgresql://localhost:5432/CarRental";
        String user = "postgres";
        String password = "1234";

        String query = String.format("INSERT INTO users(username, password) VALUES (%s, %s)", "'" + userName + "'", "'" + userPassword + "'");
        try(Connection con = DriverManager.getConnection(url, user, password)){
            con.prepareStatement(query).executeUpdate();

            System.out.println("Successfully created.");
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static boolean loginToDatabase(String userName, String userPassword) {
        String url = "jdbc:postgresql://localhost:5432/CarRental";
        String user = "postgres";
        String password = "1234";

        String query = String.format("SELECT password FROM users WHERE username = %s", "'" + userName + "'");
        try(Connection con = DriverManager.getConnection(url, user, password)){
            ResultSet res = con.prepareStatement(query).executeQuery();

            if(!res.isBeforeFirst()){
                System.out.println("User not found in the database");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided Credentials are incorrect");
                alert.show();
            }
            else{
                while (res.next()){
                    String retrievedPassword= res.getString("password");
                    if(retrievedPassword.equals(userPassword)){
                        System.out.println("LOGGED IN!!!!");
                        return true;
                    }
                }
            }
        }
        catch(SQLException ex){
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return false;
    }

    //to be fixed
    public static void AddFirm(String firmName){
        String url = "jbdc:postgresql://localhost:5432/CarRental";
        String user = "postgres";
        String password = "1234";

        String query = String.format("SELECT firm_name FROM firm WHERE firm_name =", "'" + firmName + "'");
        try(Connection con = DriverManager.getConnection(url, user, password)){

            ResultSet res = con.prepareStatement(query).executeQuery();
            if(!res.isBeforeFirst()){

                String AddFirm = "INSERT INTO firm(firm_name) VALUES (?)";
                PreparedStatement pst=con.prepareStatement(AddFirm);
                pst.setString(1,firmName);
                pst.executeUpdate(AddFirm);

            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Firm name is taken");
                alert.show();
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }


}
