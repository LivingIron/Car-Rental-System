package sample;

import javafx.scene.control.Alert;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaPostgreSql {

    public static void writeToDatabase(String userName,String userPassword){

        String url = "jbdc:postgresql://localhost:5432/cars";
        String user = "postgres";
        String password = "1234";

        String name = userName;

        String pass = userPassword;

        String query = "INSERT INTO users(username , password) VALUES (?, ?)";
        try(Connection con =DriverManager.getConnection(url,user,password);
            PreparedStatement pst=con.prepareStatement(query)) {


            pst.setString(1,name);
            pst.setString(2,pass);
            pst.executeUpdate();
            System.out.println("Successfully created.");

        }catch(SQLException ex){

            Logger lgr=Logger.getLogger(JavaPostgreSql.class.getName());
            lgr.log(Level.SEVERE,ex.getMessage(),ex);
        }

    }
    public static void loginToDatabase(String userName,String userPassword) {


        String url = "jdbc:postgresql://localhost:5432/cars";
        String user = "postgres";
        String password = "1234";

        String name = userName;
        String pass = userPassword;
        ResultSet res=null;

        String query = "SELECT password,username FROM users WHERE username = ? AND password = ? ";
        try(Connection con =DriverManager.getConnection(url,user,password);
            PreparedStatement pst=con.prepareStatement(query)) {


            pst.setString(1,name);
            pst.setString(2,pass);
            res=pst.executeQuery();

            if(!res.isBeforeFirst()){
                System.out.println("User not found in the database");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided Credentials are incorrect");
                alert.show();
            }else{
                while (res.next()){
                String retrievedUserName= res.getString("username");
                String retrievedPassword= res.getString("password");
                if(retrievedUserName.equals(user) && retrievedPassword.equals(password)){
                    System.out.println("LOGGED IN!!!!");
                }
                }
            }



        }catch(SQLException ex){

            Logger lgr=Logger.getLogger(JavaPostgreSql.class.getName());
            lgr.log(Level.SEVERE,ex.getMessage(),ex);
        }




    }
}
