package org.group29;

import javafx.scene.control.Alert;
import org.group29.entities.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaPostgreSQL {
    private static final String databaseUrl = "jdbc:postgresql://localhost:5432/CarRental";
    private static final String databaseUser = "postgres";
    private static final String databasePassword = "1234";

    /*================================================================================================================*/
    /*============================= General Functions for working with the DB ========================================*/
    /*================================================================================================================*/


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


    public static void addVehicle(int vehicleClass,int vehicleCategory, int vehicleFirmId,String vehicleCharacteristics,boolean vehicleForSmokers){
        try(Connection con = DriverManager.getConnection(databaseUrl,databaseUser,databasePassword)){
            String query = "INSERT INTO car(class,category,characteristics,smoking,firm_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertStatement=con.prepareStatement(query);
            insertStatement.setInt(1,vehicleClass);
            insertStatement.setInt(2,vehicleCategory);
            insertStatement.setInt(5,vehicleFirmId);
            insertStatement.setString(3,vehicleCharacteristics);
            insertStatement.setBoolean(4,vehicleForSmokers);
            insertStatement.executeUpdate();


            String getLastRow = "SELECT MAX(id) AS resId FROM car WHERE firm_id = ?";
            PreparedStatement getLastRowStatement = con.prepareStatement(getLastRow);
            getLastRowStatement.setInt(1,vehicleFirmId);
            ResultSet res = getLastRowStatement.executeQuery();

            while (res.next()){
                query = "INSERT INTO condition(odometer,damages,car_id) VALUES(0,'', ?)";
                insertStatement=con.prepareStatement(query);
                System.out.println(res.getString("resId"));
                insertStatement.setInt(1,res.getInt("resId"));
                insertStatement.executeUpdate();
            }

        }catch (SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
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



    /*================================================================================================================*/
    /*============================ Operator panel functions for interacting with DB ==================================*/
    /*================================================================================================================*/


    public static void addClient(String clientName,String clientPhone){
        String query = "SELECT 1 FROM client WHERE name = ? AND phone = ? AND firm_id = ?";
        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, clientName);
            statement.setString(2, clientPhone);
            statement.setInt(3, Data.operatorId);
            ResultSet res = statement.executeQuery();

            if(!res.isBeforeFirst()){
                String insertString = "INSERT INTO client(name,phone,firm_id) VALUES (?, ? ,?)";
                PreparedStatement insertStatement = con.prepareStatement(insertString);
                insertStatement.setString(1, clientName);
                insertStatement.setString(2, clientPhone);
                insertStatement.setInt(3, Data.operatorId);
                insertStatement.executeUpdate();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Client already exists");
                alert.show();
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static  void  rentCar(int carId , int client_id, int condition_id, Date rental_date, int  duration , int firm_id){

        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){

            if(!isOverlapping(carId,duration,rental_date)) {
                String insertString = "INSERT INTO rental(car_id,client_id,condition_id,rental_date,duration,firm_id) VALUES (?, ? ,? , ? ,? ,?)";
                PreparedStatement insertStatement = con.prepareStatement(insertString);
                insertStatement.setInt(1, carId);
                insertStatement.setInt(2, client_id);
                insertStatement.setInt(3, condition_id);
                insertStatement.setDate(4, rental_date);
                insertStatement.setInt(5, duration);
                insertStatement.setInt(6, firm_id);
                insertStatement.executeUpdate();

                checkForRentDays(); // sets car to rented if its rented for the same day
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Rent days for this car are overlapping !");
                alert.show();
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

    }

    public static String getCarCondition(int carId){
        String query = "SELECT damages FROM condition WHERE car_id = ?";
        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,carId);
            ResultSet res = statement.executeQuery();
            while (res.next()){
                return res.getString("damages");
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return "No data";
    }



    public static String getCarOdometer(int carId){
        String query = "SELECT odometer FROM condition WHERE car_id = ?";
        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,carId);
            ResultSet res = statement.executeQuery();
            while (res.next()){
                Integer temp = res.getInt("odometer");
                return temp.toString();
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return "No data";
    }

    public static void checkForRentDays(){

        String query = "SELECT car_id FROM rental WHERE rental_date = ?";

        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            PreparedStatement statement = con.prepareStatement(query);
            statement.setDate(1,Date.valueOf(LocalDate.now()));
            ResultSet res = statement.executeQuery();
            while (res.next()){
                    query="UPDATE car SET is_rented=true WHERE id = ?";
                    PreparedStatement statement1 = con.prepareStatement(query);
                    statement1.setInt(1,res.getInt("car_id"));
                    statement1.executeUpdate();
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

    }

    public static boolean isOverlapping(int car_id , int duration , Date rental_date){
        boolean isOverlapped=false;
        LocalDate enteredRentDay = (rental_date.toLocalDate().plusDays(duration));

        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){

            String insertString = "SELECT car_id,rental_date,duration FROM rental WHERE car_id = ? AND is_returned = false";
            PreparedStatement insertStatement = con.prepareStatement(insertString);
            insertStatement.setInt(1,car_id);
            ResultSet res = insertStatement.executeQuery();

            while (res.next()){
                LocalDate checkingDate = res.getDate("rental_date").toLocalDate().plusDays(res.getInt("duration"));
                if ( (rental_date.toLocalDate().isBefore(checkingDate) ||  rental_date.toLocalDate().equals(checkingDate) ) &&
                        (enteredRentDay.isAfter(res.getDate("rental_date").toLocalDate()) || enteredRentDay.equals(res.getDate("rental_date")))
                    ){
                    isOverlapped=true;
                    break;
                }
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return isOverlapped;
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

    public static Vehicles[] getVehicles(){
        ArrayList<Vehicles> vehicleArray = new ArrayList<>();
        String query = "SELECT id,class,category,characteristics,smoking,is_rented FROM car WHERE firm_id = ? AND is_rented=false";

        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,Data.operatorId);
            ResultSet res = statement.executeQuery();

            while(res.next()){
                vehicleArray.add(new Vehicles(  res.getInt("id"),
                                                res.getInt("class"),
                                                res.getInt("category"),
                                                Data.operatorId,
                                                res.getString("characteristics"),
                                                res.getBoolean("smoking"),
                                                res.getBoolean("is_rented")));
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return vehicleArray.toArray(new Vehicles[0]);
    }

    public static Client[] getClients(){
        ArrayList<Client> clientArray = new ArrayList<>();
        String query = "SELECT id,name,phone,rating FROM client WHERE firm_id = ?";

        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,Data.operatorId);
            ResultSet res = statement.executeQuery();

            while(res.next()){
               clientArray.add(new Client(  res.getInt("id"),
                                            Data.operatorId,
                                            res.getInt("rating"),
                                            res.getString("name"),
                                            res.getString("phone")));
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return clientArray.toArray(new Client[0]);
    }


    /*================================================================================================================*/
    /*=========================================== Converters of data =================================================*/
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

    public static int carIdToConditionId(int carId){
        int idToReturn=0;
        String query = "SELECT id FROM condition WHERE car_id = ?";

        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){

            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,carId);
            ResultSet res = statement.executeQuery();

            while(res.next()){
                idToReturn=res.getInt("id");
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

       return idToReturn;
    }

}
