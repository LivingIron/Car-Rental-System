package org.group29;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import org.group29.entities.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

    public static void returnCar(int rental_id, Date return_date, int condition_id, String damages , int odometer){
        String query = "SELECT odometer FROM condition WHERE car_id = ?";
        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){

            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,rentalIdToCarId(rental_id));
            ResultSet res = statement.executeQuery();

            while (res.next()){

                //checks to see if the new odometer reading is the same as the original reading
                //if the odometer reading hasnt changed sends an error

                if(res.getInt("odometer")>=odometer){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please select a new odometer reading!");
                    alert.show();
                    break;

                }else{

                    query= "SELECT rental_date ,duration FROM rental WHERE id = ?";
                    statement=con.prepareStatement(query);
                    statement.setInt(1,rental_id);

                    ResultSet res2=statement.executeQuery();
                    while (res2.next()){

                         // Checks if the return date is before the original rental date
                         // or if its on the same day , in both those cases sends an error

                        if(     res2.getDate("rental_date").toLocalDate().isAfter(return_date.toLocalDate()) ||
                                return_date.toLocalDate().equals(res2.getDate("rental_date").toLocalDate())){

                            System.out.println(res2.getDate("rental_date").toLocalDate());
                            System.out.println(return_date.toLocalDate());


                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("Please select a date after the rent date!");
                            alert.show();
                            break;

                        }else{

                            //Adds a price row

                            Long days = ChronoUnit.DAYS.between(res2.getDate("rental_date").toLocalDate(),return_date.toLocalDate()); // Gets days the car was taken
                            String[] words = damages.split("\\s+"); // gets damage count
                            int damage_count = words.length;

                            query = "INSERT INTO price (days,kilometers,dmg_count,car_class_id) VALUES (? , ? , ? , ? )";
                            statement=con.prepareStatement(query);
                            statement.setInt(1,days.intValue());
                            statement.setInt(2,odometer - Integer.parseInt(getCarOdometer(rentalIdToCarId(rental_id))));
                            statement.setInt(3,damage_count);
                            statement.setInt(4,carIdToClassId( rentalIdToCarId(rental_id) ));
                            statement.executeUpdate();


                            //Gets the id of the last added price
                            query = "SELECT MAX(id) FROM price";
                            statement=con.prepareStatement(query);
                            ResultSet lastPriceId = statement.executeQuery();
                            int priceId = 0 ;

                            while (lastPriceId.next()){
                                priceId=lastPriceId.getInt("max");
                            }


                            //Adds the data into return table
                            query="INSERT INTO return(rental_id,return_date,condition_id,id_price) VALUES (?, ?, ?, ?)";
                            statement=con.prepareStatement(query);
                            statement.setInt(1,rental_id);
                            statement.setDate(2,return_date);
                            statement.setInt(3,condition_id);
                            statement.setInt(4,priceId);
                            statement.executeUpdate();

                            // Updates the condition for the car

                            updateCondition(condition_id,damages,odometer);

                            //Updated the car rented status to false

                            query = "UPDATE  car SET is_rented=false WHERE id = ?";
                            statement = con.prepareStatement(query);
                            statement.setInt(1,rentalIdToCarId(rental_id));
                            statement.executeUpdate();

                            //Updates the rental returned status to true

                            query = "UPDATE  rental SET is_returned=true WHERE id = ?";
                            statement = con.prepareStatement(query);
                            statement.setInt(1,rental_id);
                            statement.executeUpdate();



                            // Checks if the car is returned after the duration

                            if(res2.getDate("rental_date").toLocalDate().plusDays(res2.getInt("duration")).isBefore(return_date.toLocalDate())){

                                //If the car is returned after the original duration the
                                //client rating goes down by 1
                                //Nothing happens if the client rating is already at 0

                                query = "SELECT rating FROM client  WHERE id = ?";
                                statement = con.prepareStatement(query);
                                statement.setInt(1,rentalIdToClientId(rental_id));
                                ResultSet res3 = statement.executeQuery();
                                while (res3.next()){
                                    if(res3.getInt("rating")==0){
                                        break;
                                    }else{

                                        query = "UPDATE  client SET rating = ? WHERE id = ?";
                                        statement = con.prepareStatement(query);
                                        statement.setInt(1,res3.getInt("rating")-1);
                                        statement.setInt(2,rentalIdToClientId(rental_id));
                                        statement.executeUpdate();

                                    }
                                }
                            }else{

                                //If the car is returned before the original duration the
                                //client rating goes up by 1
                                //Nothing happens if the client rating is already at 10

                                query = "SELECT rating FROM client  WHERE id = ?";
                                statement = con.prepareStatement(query);
                                statement.setInt(1,rentalIdToClientId(rental_id));
                                ResultSet res3 = statement.executeQuery();
                                while (res3.next()){
                                    if(res3.getInt("rating")==10){
                                        break;
                                    }else{

                                        query = "UPDATE  client SET rating = ? WHERE id = ?";
                                        statement = con.prepareStatement(query);
                                        statement.setInt(1,res3.getInt("rating")+1);
                                        statement.setInt(2,rentalIdToClientId(rental_id));
                                        statement.executeUpdate();

                                    }
                                }

                            }

                        }
                    }
                }
            }

            FxmlLoader.switchPane(Data.operatorMainPane,"OperatorMenu");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Car returned !");
            alert.show();
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

    //Function checks  if any rentals start at the current date and updates the car to be rented
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

    //Checks if two dates are overlapping
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

    //updated a condition by id
    public static void updateCondition(int condition_id,String damages ,int odometer){
        String query = "UPDATE condition SET damages = ? , odometer = ?  WHERE id= ? ";
        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){

            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1,damages);
            statement.setInt(2,odometer);
            statement.setInt(3,condition_id);
            statement.executeUpdate();

        }
        catch(SQLException ex){
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

    public static Rental[] getRentals(){
        ArrayList<Rental> rentalArray = new ArrayList<>();
        String query = "SELECT id,car_id,client_id,condition_id,rental_date,duration,is_returned FROM rental WHERE is_returned=false AND firm_id = ?";

        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){

            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,Data.operatorId);
            ResultSet res = statement.executeQuery();

            while(res.next()){
                rentalArray.add(  new Rental( res.getInt("id"),
                                    res.getInt("car_id"),
                                    res.getInt("client_id"),
                                    res.getInt("condition_id"),
                                    res.getInt("duration"),
                                    res.getDate("rental_date"),
                                    res.getBoolean("is_returned"))) ;
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return rentalArray.toArray(new Rental[0]);
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

    public static int rentalIdToCarId(int rentalId){
        int idToReturn=0;
        String query = "SELECT car_id FROM rental WHERE id = ?";

        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){

            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,rentalId);
            ResultSet res = statement.executeQuery();

            while(res.next()){
                idToReturn=res.getInt("car_id");
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return idToReturn;
    }

    public static int rentalIdToClientId(int rentalId){
        int idToReturn=0;
        String query = "SELECT client_id FROM rental WHERE id = ?";

        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){

            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,rentalId);
            ResultSet res = statement.executeQuery();

            while(res.next()){
                idToReturn=res.getInt("client_id");
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return idToReturn;
    }

    public static int carIdToClassId(int carId){
        int idToReturn=0;
        String query = "SELECT class FROM car WHERE id = ?";

        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){

            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,carId);
            ResultSet res = statement.executeQuery();

            while(res.next()){
                idToReturn=res.getInt("class");
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return idToReturn;
    }

}
