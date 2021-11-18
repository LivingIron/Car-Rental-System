package org.group29;

import javafx.scene.control.Alert;
import org.group29.entities.Firm;
import org.group29.entities.VehicleCategory;
import org.group29.entities.VehicleClass;

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


    /*================================================================================================================*/
    /*============================= Admin panel functions for interacting with DB=====================================*/
    /*================================================================================================================*/


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

    public static void addOperator(String operatorUsername, String operatorPassword, int operatorFirmId){
        String query = "SELECT 1 FROM users WHERE username = ?";
        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, operatorUsername);
            ResultSet res = statement.executeQuery();

            if(!res.isBeforeFirst()){
                String insertString = "INSERT INTO users (username, password, id_firm) VALUES (?, ?, ?)";
                PreparedStatement insertStatement = con.prepareStatement(insertString);
                insertStatement.setString(1, operatorUsername);
                insertStatement.setString(2, operatorPassword);
                insertStatement.setInt(3, operatorFirmId);
                insertStatement.executeUpdate();
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

    public static void addVehicleAdmin(int vehicleClass,int vehicleCategory, int vehicleFirmId,String vehicleCharacteristics,boolean vehicleForSmokers){
       try(Connection con = DriverManager.getConnection(databaseUrl,databaseUser,databasePassword)){
           String query = "INSERT INTO car(class,category,characteristics,smoking,firm_id) VALUES (?, ?, ?, ?, ?)";
           PreparedStatement insertStatement=con.prepareStatement(query);
           insertStatement.setInt(1,vehicleClass);
           insertStatement.setInt(2,vehicleCategory);
           insertStatement.setInt(5,vehicleFirmId);
           insertStatement.setString(3,vehicleCharacteristics);
           insertStatement.setBoolean(4,vehicleForSmokers);
           insertStatement.executeUpdate();

       }catch (SQLException ex){
           Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
           lgr.log(Level.SEVERE, ex.getMessage(), ex);
       }
    }



    /*================================================================================================================*/
    /*============================== Functions For getting data from comboBoxes ======================================*/
    /*================================================================================================================*/

    public static Firm[] getFirms(){
        ArrayList<Firm> firms = new ArrayList<>();
        String query = "SELECT id, firm_name FROM firm";
        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet res = statement.executeQuery();

            while(res.next()){
                if(res.getInt("id") != 1){
                    firms.add(new Firm(res.getInt("id"), res.getString("firm_name")));
                }
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return firms.toArray(new Firm[0]);
    }

    public static VehicleCategory[] getCategoryNames(){
        ArrayList<VehicleCategory> categoryNames = new ArrayList<>();
        String query = "SELECT id_category,name_category FROM car_category";
        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet res = statement.executeQuery();

            while(res.next()){
                    categoryNames.add(new VehicleCategory(res.getInt("id_category"),res.getString("name_category")));
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return categoryNames.toArray(new VehicleCategory[0]);
    }

    public static VehicleClass[] getClassNames(){
        ArrayList<VehicleClass> classNames = new ArrayList<>();
        String query = "SELECT id_class,name_class FROM car_class";
        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet res = statement.executeQuery();

            while(res.next()){
                classNames.add(new VehicleClass(res.getInt("id_class"),res.getString("name_class")));
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return classNames.toArray(new VehicleClass[0]);
    }


    /*================================================================================================================*/
    /*================================================ Miscellaneous =================================================*/
    /*================================================================================================================*/

    public static int getFirmId(String operatorUsername){
        String query = "SELECT id_firm, username FROM users WHERE username= ?";
        int idToReturn=0;
        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1,operatorUsername);
            ResultSet res = statement.executeQuery();
            while(res.next()){
               idToReturn=res.getInt("id_firm");
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return idToReturn;
    }

}
