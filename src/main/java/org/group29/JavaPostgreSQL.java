package org.group29;

import javafx.scene.control.Alert;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaPostgreSQL {

    public static void writeToDatabase(String userName, String userPassword){

        String url = "jbdc:postgresql://localhost:5432/CarRental";
        String user = "postgres";
        String password = "7232";

        String query = "INSERT INTO users(username , password) VALUES (?, ?)";
        try(Connection con =DriverManager.getConnection(url,user,password);
            PreparedStatement pst=con.prepareStatement(query)) {


            pst.setString(1,userName);
            pst.setString(2,userPassword);
            pst.executeUpdate();
            System.out.println("Successfully created.");

        } catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE,ex.getMessage(),ex);
        }
    }

    public static void loginToDatabase(String userName, String userPassword) {
        String url = "jdbc:postgresql://localhost:5432/CarRental";
        String user = "postgres";
        String password = "7232";

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
                    }
                }
            }
        }catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE,ex.getMessage(),ex);
        }
    }
}
