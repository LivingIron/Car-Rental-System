package org.group29;

import javafx.scene.control.Alert;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaPostgreSQL {
    private static final String databaseUrl = "jdbc:postgresql://localhost:5432/CarRental";
    private static final String databaseUser = "postgres";
    private static final String databasePassword = "1234";

    public static void writeToDatabase(String userName, String userPassword){
        String query = String.format("INSERT INTO users(username, password) VALUES (%s, %s)", "'" + userName + "'", "'" + userPassword + "'");
        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            con.prepareStatement(query).executeUpdate();

            System.out.println("Successfully created.");
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static boolean loginToDatabase(String userName, String userPassword) {
        String query = String.format("SELECT password FROM users WHERE username = %s", "'" + userName + "'");
        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
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

    public static void AddFirm(String firmName){
        String query = String.format("SELECT firm_name FROM firm WHERE firm_name=%s", "'" + firmName + "'");
        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){

            ResultSet res = con.prepareStatement(query).executeQuery();
            if(!res.isBeforeFirst()){
                String insertStatement = String.format("INSERT INTO firm(firm_name) VALUES (%s)", "'" + firmName + "'");
                con.prepareStatement(insertStatement).executeUpdate();
            }
            else{
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

    public static void AddOperator(String operatorUsername,String operatorPassword,String operatorConfirmPassword,String operatorFirm){

        String query = String.format("SELECT username FROM users WHERE username=%s", "'" + operatorUsername + "'");
        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){

            ResultSet res=con.prepareStatement(query).executeQuery();
            if(!res.isBeforeFirst()){

                if(operatorPassword.equals(operatorConfirmPassword)){
                    res= con.prepareStatement(String.format("SELECT users.id_firm,firm.firm_name,firm.id FROM users FULL JOIN firm ON users.id_firm = firm.id ")).executeQuery();
                    boolean found=false;
                    String firmIndex="2";
                    while (res.next()){
                        if(res.getString("firm_name").equals(operatorFirm)){
                            found=true;
                            firmIndex=res.getString("id");
                        }
                    }
                    if(found == false || operatorFirm .equals("AdminCompany")){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Firm does not exist !");
                        alert.show();
                    }else{

                        con.prepareStatement(String.format("INSERT INTO users (username,password,id_firm) VALUES (%s,%s,%s)","'" +operatorUsername +"'","'"+operatorPassword+"'","'"+firmIndex+"'")).executeUpdate();
                    }

                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Passwords dont match!");
                    alert.show();
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Username is already taken!");
                alert.show();
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

}
