package org.group29;

import javafx.scene.control.Alert;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaPostgreSQL {
    private static final String databaseUrl = "jdbc:postgresql://localhost:5432/CarRental";
    private static final String databaseUser = "postgres";
    private static final String databasePassword = "1234";

    public static boolean loginToDatabase(String userName, String userPassword) {
        String query = "SELECT password FROM users WHERE username = ?";
        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, userName);
            ResultSet res = statement.executeQuery();

            if(!res.isBeforeFirst()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided Credentials are incorrect");
                alert.show();
            }
            else{
                while (res.next()){
                    String retrievedPassword = res.getString("password");
                    if(retrievedPassword.equals(userPassword)) return true;
                }
            }
        }
        catch(SQLException ex){
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return false;
    }

    public static void addFirm(String firmName){
        String query = "SELECT 1 FROM firm WHERE firm_name = ?";
        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, firmName);
            ResultSet res = statement.executeQuery();

            if(!res.isBeforeFirst()){
                String insertString = "INSERT INTO firm(firm_name) VALUES (?)";
                PreparedStatement insertStatement = con.prepareStatement(insertString);
                insertStatement.setString(1, firmName);
                insertStatement.executeUpdate();
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

    public static void addOperator(String operatorUsername, String operatorPassword, String operatorFirm){
        String query = "SELECT 1 FROM users WHERE username = ?";
        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, operatorUsername);
            ResultSet res = statement.executeQuery();

            if(!res.isBeforeFirst()){
                int firmID = firmNameToId(operatorFirm);

                if(firmID == 0){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Firm does not exist!");
                    alert.show();
                }
                else{
                    String insertString = "INSERT INTO users (username, password, id_firm) VALUES (?, ?, ?)";
                    PreparedStatement insertStatement = con.prepareStatement(insertString);
                    insertStatement.setString(1, operatorUsername);
                    insertStatement.setString(2, operatorPassword);
                    insertStatement.setInt(3, firmID);
                    insertStatement.executeUpdate();
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

    // Function that takes the firmName , joins users to firm and searches and compares the firmname
    // If the function succeeds it returns the index of the firm , otherwise it returns 0

    /*-----      Needs to be repurposed to work with all tables when searching for firm id      -----*/

    public static int firmNameToId(String firmName){
        if(firmName.equals("AdminCompany")) return 1;
        int firmIndex = 0;

        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
           ResultSet res = con.prepareStatement("SELECT users.id_firm,firm.firm_name,firm.id FROM users FULL JOIN firm ON users.id_firm = firm.id ").executeQuery();

           while (res.next()){
               Object item = res.getObject("firm_name");
               String strValue = (item == null ? null : item.toString());
               if(strValue != null && strValue.equals(firmName)){
                    firmIndex = res.getInt("id");
                    break;
               }
           }
           return firmIndex;
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return 0;
    }

    public static String[] getFirmNames(){
        ArrayList<String> firmNames = new ArrayList<>();
        String query = "SELECT firm_name FROM firm";
        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet res = statement.executeQuery();

            while(res.next()){
                firmNames.add(res.getString("firm_name"));
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return firmNames.toArray(new String[0]);
    }
}
