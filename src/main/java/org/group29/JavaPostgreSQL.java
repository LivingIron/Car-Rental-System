package org.group29;

import javafx.util.Pair;
import org.group29.entities.*;

import java.sql.*;
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


    public static int loginToDatabase(String userName, String userPassword) {
        String query = "SELECT id, password FROM users WHERE username = ?";
        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, userName);
            ResultSet res = statement.executeQuery();

            if(res.next()){
                String retrievedPassword = res.getString("password");
                if(retrievedPassword.equals(userPassword)) return res.getInt("id");
            }
        }
        catch(SQLException ex){
            Logger lgr = Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return -1;
    }

    public static int addVehicle(Vehicle vehicle){
        try(Connection con = DriverManager.getConnection(databaseUrl,databaseUser,databasePassword)){
            String query = "INSERT INTO car(class, category, characteristics, smoking, firm_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertStatement=con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setInt(1,vehicle.getClassification());
            insertStatement.setInt(2,vehicle.getCategory());
            insertStatement.setString(3, vehicle.getCharacteristics());
            insertStatement.setBoolean(4, vehicle.isSmoking());
            insertStatement.setInt(5,vehicle.getFirm_id());
            insertStatement.executeUpdate();

            ResultSet res = insertStatement.getGeneratedKeys();
            if(res.next()){
                return res.getInt(1);
            }
        }
        catch (SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return -1;
    }

    public static void updateVehicle(Vehicle vehicle){
        try(Connection con = DriverManager.getConnection(databaseUrl,databaseUser,databasePassword)){
            String query = "UPDATE car SET class = ?, category = ?, characteristics = ?, smoking = ?, firm_id = ? WHERE id = ?";

            PreparedStatement updateStatement = con.prepareStatement(query);
            updateStatement.setInt(1,vehicle.getClassification());
            updateStatement.setInt(2,vehicle.getCategory());
            updateStatement.setString(3, vehicle.getCharacteristics());
            updateStatement.setBoolean(4, vehicle.isSmoking());
            updateStatement.setInt(5, vehicle.getFirm_id());
            updateStatement.setInt(6, vehicle.getId());
            updateStatement.executeUpdate();
        }
        catch (SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static void getVehicle(Vehicle vehicle){
        try(Connection con = DriverManager.getConnection(databaseUrl,databaseUser,databasePassword)){
            String query = "SELECT class, category, characteristics, smoking, firm_id FROM car WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,vehicle.getId());
            ResultSet res = statement.executeQuery();

            if(res.next()) {
                vehicle.setCategory(res.getInt("category"));
                vehicle.setClassification(res.getInt("class"));
                vehicle.setCharacteristics(res.getString("characteristics"));
                vehicle.setSmoking(res.getBoolean("smoking"));
                vehicle.setFirm_id(res.getInt("firm_id"));
            }
        }
        catch (SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static void getClient(Client client){
        try(Connection con = DriverManager.getConnection(databaseUrl,databaseUser,databasePassword)){
            String query = "SELECT name, phone, firm_id, rating FROM client WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,client.getId());
            ResultSet res = statement.executeQuery();

            if(res.next()) {
                client.setName(res.getString("name"));
                client.setPhone(res.getString("phone"));
                client.setFirm_id(res.getInt("firm_id"));
                client.setRating(res.getInt("rating"));
            }
        }
        catch (SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static void getCondition(Condition condition){
        try(Connection con = DriverManager.getConnection(databaseUrl,databaseUser,databasePassword)){
            String query = "SELECT odometer, damages, car_id FROM condition WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,condition.getId());
            ResultSet res = statement.executeQuery();

            if(res.next()) {
                condition.setOdometer(res.getInt("odometer"));
                condition.setDamages(res.getString("damages"));
                condition.setVehicle_id(res.getInt("car_id"));
            }
        }
        catch (SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static Condition getCarCondition(int vehicle_id){
        try(Connection con = DriverManager.getConnection(databaseUrl,databaseUser,databasePassword)){
            String query = "SELECT id, odometer, damages, car_id FROM condition WHERE car_id = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,vehicle_id);
            ResultSet res = statement.executeQuery();

            if(res.next())
                return resultToCondition(res);
        }
        catch (SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return null;
    }

    public static void updateCondition(Condition condition){
        try(Connection con = DriverManager.getConnection(databaseUrl,databaseUser,databasePassword)){
            String query = "UPDATE condition SET odometer = ?, damages = ?, car_id = ? WHERE id = ?";

            PreparedStatement updateStatement = con.prepareStatement(query);
            updateStatement.setInt(1, condition.getOdometer());
            updateStatement.setString(2, condition.getDamages());
            updateStatement.setInt(3, condition.getVehicle_id());
            updateStatement.setInt(4, condition.getId());
            updateStatement.executeUpdate();
        }
        catch (SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static void getRental(Rental rental){
        try(Connection con = DriverManager.getConnection(databaseUrl,databaseUser,databasePassword)){
            String query = "SELECT car_id, client_id, condition_id, rental_date, duration, firm_id, is_returned FROM rental WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,rental.getId());
            ResultSet res = statement.executeQuery();

            if(res.next()) {
                rental.setClient(new Client(res.getInt("client_id")));
                rental.setVehicle(new Vehicle(res.getInt("car_id")));
                rental.setCondition(new Condition(res.getInt("condition_id")));
                rental.setRental_date(res.getDate("rental_date"));
                rental.setDuration(res.getInt("duration"));
                rental.setFirm_id(res.getInt("firm_id"));
                rental.setReturned(res.getBoolean("is_returned"));
            }
        }
        catch (SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static void getReturn(Return returnal){
        try(Connection con = DriverManager.getConnection(databaseUrl,databaseUser,databasePassword)){
            String query = "SELECT rental_id, return_date, condition_id, id_price FROM return WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,returnal.getId());
            ResultSet res = statement.executeQuery();

            if(res.next()){
                returnal.setRental(new Rental(res.getInt("rental_id")));
                returnal.setReturn_date(res.getDate("return_date"));
            }
        }
        catch (SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static void getOperator(Operator operator){
        try(Connection con = DriverManager.getConnection(databaseUrl,databaseUser,databasePassword)){
            String query = "SELECT username, id_firm FROM users WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,operator.getId());
            ResultSet res = statement.executeQuery();

            if(res.next()) {
                operator.setUsername(res.getString("username"));
                operator.setFirm_id(res.getInt("id_firm"));
            }
        }
        catch (SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static double getPrice(Return returnal){
        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            String callString = "{ ? = call get_price(?) }";
            CallableStatement checkStatement = con.prepareCall(callString);

            checkStatement.registerOutParameter(1, Types.DOUBLE);
            checkStatement.setInt(2, returnal.getId());

            checkStatement.execute();

            return checkStatement.getDouble(1);
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return -1;
    }

    /*================================================================================================================*/
    /*============================= Admin panel functions for interacting with DB=====================================*/
    /*================================================================================================================*/


    public static int addFirm(Firm firm){
        String query = "SELECT 1 FROM firm WHERE firm_name = ?";
        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, firm.getName());
            ResultSet res = statement.executeQuery();

            if(!res.next()){
                String insertString = "INSERT INTO firm(firm_name) VALUES (?)";
                PreparedStatement insertStatement = con.prepareStatement(insertString, Statement.RETURN_GENERATED_KEYS);
                insertStatement.setString(1, firm.getName());
                insertStatement.executeUpdate();

                res = insertStatement.getGeneratedKeys();
                if(res.next()) {
                    return res.getInt(1);
                }
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return -1;
    }

    public static void updateFirm(Firm firm){
        try(Connection con = DriverManager.getConnection(databaseUrl,databaseUser,databasePassword)){
            String query = "UPDATE firm SET name = ? WHERE id = ?";

            PreparedStatement updateStatement = con.prepareStatement(query);
            updateStatement.setString(1, firm.getName());
            updateStatement.setInt(2, firm.getId());
            updateStatement.executeUpdate();
        }
        catch (SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static int addOperator(Operator operator){
        String query = "SELECT 1 FROM users WHERE username = ?";
        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, operator.getUsername());
            ResultSet res = statement.executeQuery();

            if(!res.next()){
                String insertString = "INSERT INTO users (username, password, id_firm) VALUES (?, ?, ?)";
                PreparedStatement insertStatement = con.prepareStatement(insertString, Statement.RETURN_GENERATED_KEYS);
                insertStatement.setString(1, operator.getUsername());
                insertStatement.setString(2, operator.getPassword());
                insertStatement.setInt(3, operator.getFirm_id());
                insertStatement.executeUpdate();

                res = insertStatement.getGeneratedKeys();
                if(res.next()){
                    return res.getInt(1);
                }
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return -1;
    }

    public static void updateOperator(Operator operator){
        try(Connection con = DriverManager.getConnection(databaseUrl,databaseUser,databasePassword)){
            String query = "UPDATE users SET username = ?, password = ?, firm_id = ? WHERE id = ?";

            PreparedStatement updateStatement = con.prepareStatement(query);
            updateStatement.setString(1, operator.getUsername());
            updateStatement.setString(2, operator.getPassword());
            updateStatement.setInt(3, operator.getFirm_id());
            updateStatement.setInt(4, operator.getId());
            updateStatement.executeUpdate();
        }
        catch (SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static HistoryEntry[] getOperatorHistory(Operator operator, Date startDate, Date endDate){
        ArrayList<HistoryEntry> historyEntries = new ArrayList<>();

        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            String query = "SELECT car_id, client_id, rental_date, duration FROM rental WHERE operator_id = ?" +
                    "AND rental_date >= CAST(? AS Date) AND rental_date <= CAST(? AS Date)";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,operator.getId());
            statement.setDate(2, startDate);
            statement.setDate(3, endDate);
            ResultSet res = statement.executeQuery();

            while(res.next()){
                HistoryEntry newEntry = new HistoryEntry(res.getDate("rental_date"),
                        res.getInt("car_id"),
                        res.getInt("client_id"),
                        res.getInt("duration"));
                historyEntries.add(newEntry);
            }

            query = "SELECT rental.car_id, rental.client_id, return_date FROM return INNER JOIN rental ON rental.id = return.rental_id WHERE return.operator_id = ?" +
                    "AND return.return_date >= CAST(? AS Date) AND return.return_date <= CAST(? AS Date)";
            statement = con.prepareStatement(query);
            statement.setInt(1,operator.getId());
            statement.setDate(2, startDate);
            statement.setDate(3, endDate);
            res = statement.executeQuery();

            while(res.next()){
                HistoryEntry newEntry = new HistoryEntry(res.getDate("return_date"),
                        res.getInt("car_id"),
                        res.getInt("client_id"),
                        0);
                historyEntries.add(newEntry);
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return historyEntries.toArray(new HistoryEntry[0]);
    }

    /*================================================================================================================*/
    /*============================ Operator panel functions for interacting with DB ==================================*/
    /*================================================================================================================*/


    public static int addClient(Client client){
        String query = "SELECT 1 FROM client WHERE name = ? AND phone = ? AND firm_id = ?";
        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, client.getName());
            statement.setString(2, client.getPhone());
            statement.setInt(3, client.getFirm_id());
            ResultSet res = statement.executeQuery();

            if(!res.next()){
                String insertString = "INSERT INTO client(name, phone, firm_id) VALUES (?, ? ,?)";
                PreparedStatement insertStatement = con.prepareStatement(insertString, Statement.RETURN_GENERATED_KEYS);
                insertStatement.setString(1, client.getName());
                insertStatement.setString(2, client.getPhone());
                insertStatement.setInt(3, client.getFirm_id());
                insertStatement.executeUpdate();

                res = insertStatement.getGeneratedKeys();

                if(res.next()){
                    return res.getInt(1);
                }
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return -1;
    }

    public static void updateClient(Client client){
        try(Connection con = DriverManager.getConnection(databaseUrl,databaseUser,databasePassword)){
            String query = "UPDATE client SET name = ?, phone = ?, firm_id = ?, rating = ? WHERE id = ?";

            PreparedStatement updateStatement = con.prepareStatement(query);
            updateStatement.setString(1, client.getName());
            updateStatement.setString(2, client.getPhone());
            updateStatement.setInt(3, client.getFirm_id());
            updateStatement.setInt(4, client.getRating());
            updateStatement.setInt(5, client.getId());
            updateStatement.executeUpdate();
        }
        catch (SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static int addRental(Rental rental){
        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            String callString = "{ ? = call is_rental_overlapped(?, ?, ?) }";
            CallableStatement overlapStatement = con.prepareCall(callString);

            overlapStatement.registerOutParameter(1, Types.BOOLEAN);
            overlapStatement.setInt(2, rental.getVehicle().getId());
            overlapStatement.setDate(3, rental.getRental_date());
            overlapStatement.setInt(4, rental.getDuration());

            overlapStatement.execute();

            boolean isOverlapping = overlapStatement.getBoolean(1);
            if(!isOverlapping) {
                String insertString = "INSERT INTO rental(car_id, client_id, condition_id, rental_date, duration, firm_id) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement insertStatement = con.prepareStatement(insertString, Statement.RETURN_GENERATED_KEYS);
                insertStatement.setInt(1, rental.getVehicle().getId());
                insertStatement.setInt(2, rental.getClient().getId());
                insertStatement.setInt(3, rental.getCondition().getId());
                insertStatement.setDate(4, rental.getRental_date());
                insertStatement.setInt(5, rental.getDuration());
                insertStatement.setInt(6, rental.getFirm_id());
                insertStatement.executeUpdate();

                ResultSet res = insertStatement.getGeneratedKeys();
                if(res.next()){
                    return res.getInt(1);
                }
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return -1;
    }

    public static void updateRental(Rental rental){
        try(Connection con = DriverManager.getConnection(databaseUrl,databaseUser,databasePassword)){
            String query = "UPDATE rental SET car_id = ?, client_id = ?, condition_id = ?, rental_date = ?, duration = ?, firm_id = ?, is_returned = ? WHERE id = ?";

            PreparedStatement updateStatement = con.prepareStatement(query);
            updateStatement.setInt(1,rental.getVehicle().getId());
            updateStatement.setInt(2,rental.getClient().getId());
            updateStatement.setInt(3, rental.getCondition().getId());
            updateStatement.setDate(4, rental.getRental_date());
            updateStatement.setInt(5, rental.getDuration());
            updateStatement.setInt(6, rental.getFirm_id());
            updateStatement.setBoolean(7, rental.isReturned());
            updateStatement.setInt(8, rental.getId());
            updateStatement.executeUpdate();
        }
        catch (SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public static int addReturn(Return returnal){
        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            String callString = "{ ? = call is_return_after_rental(?, ?) }";
            CallableStatement checkStatement = con.prepareCall(callString);

            checkStatement.registerOutParameter(1, Types.BOOLEAN);
            checkStatement.setDate(2, returnal.getReturn_date());
            checkStatement.setInt(3, returnal.getRental().getId());

            checkStatement.execute();

            boolean isValid = checkStatement.getBoolean(1);
            if(isValid){
                String insertString = "INSERT INTO return(rental_id, return_date) VALUES (?, ?)";
                PreparedStatement insertStatement = con.prepareStatement(insertString, Statement.RETURN_GENERATED_KEYS);
                insertStatement.setInt(1, returnal.getRental().getId());
                insertStatement.setDate(2, returnal.getReturn_date());
                insertStatement.executeUpdate();

                ResultSet res = insertStatement.getGeneratedKeys();
                if(res.next()){
                    return res.getInt(1);
                }
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return -1;
    }

    public static Vehicle[] getAvailableVehicles(Date startDate, Date endDate){
        ArrayList<Vehicle> vehicleArray = new ArrayList<>();
        String query = "SELECT car.id AS id, car.class AS class, car.category AS category, car.firm_id AS firm_id, car.characteristics AS characteristics, car.smoking AS smoking " +
                "FROM car LEFT JOIN rental ON rental.car_id = car.id " +
                "WHERE car.firm_id = ? AND (rental.car_id IS NULL OR NOT ((CAST(? AS Date), CAST(? AS Date)) OVERLAPS (rental.rental_date, rental.duration * interval '1 day')))";

        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,Data.operator.getFirm_id());
            statement.setDate(2, startDate);
            statement.setDate(3, endDate);
            ResultSet res = statement.executeQuery();

            while(res.next()){
                vehicleArray.add(resultToVehicle(res));
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return vehicleArray.toArray(new Vehicle[0]);
    }

    public static Rental[] getRentalsByVehicle(Vehicle vehicle, Date startDate, Date endDate){
        ArrayList<Rental> rentalArray = new ArrayList<>();
        String query = "SELECT id, car_id, client_id, condition_id, rental_date, duration, is_returned, firm_id FROM rental WHERE firm_id = ? AND car_id = ? " +
                "AND rental_date >= CAST(? AS Date) AND rental_date <= CAST(? AS Date)";

        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,Data.operator.getFirm_id());
            statement.setInt(2, vehicle.getId());
            statement.setDate(3, startDate);
            statement.setDate(4, endDate);
            ResultSet res = statement.executeQuery();

            while(res.next()){
                rentalArray.add(resultToRental(res));
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return rentalArray.toArray(new Rental[0]);
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
                if(res.getInt("id") != 0){
                    firms.add(resultToFirm(res));
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
        String query = "SELECT id_category, name_category FROM car_category";

        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet res = statement.executeQuery();

            while(res.next()){
                categoryNames.add(resultToCategory(res));
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
        String query = "SELECT id_class, name_class FROM car_class";

        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet res = statement.executeQuery();

            while(res.next()){
                classNames.add(resultToClass(res));
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return classNames.toArray(new VehicleClass[0]);
    }

    public static Vehicle[] getVehicles(){
        ArrayList<Vehicle> vehicleArray = new ArrayList<>();
        String query = "SELECT id, class, category, firm_id, characteristics, smoking FROM car WHERE firm_id = ?";

        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,Data.operator.getFirm_id());
            ResultSet res = statement.executeQuery();

            while(res.next()){
                vehicleArray.add(resultToVehicle(res));
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return vehicleArray.toArray(new Vehicle[0]);
    }

    public static Client[] getClients(){
        ArrayList<Client> clientArray = new ArrayList<>();
        String query = "SELECT id, name, phone, rating, firm_id FROM client WHERE firm_id = ?";

        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,Data.operator.getFirm_id());
            ResultSet res = statement.executeQuery();

            while(res.next()){
               clientArray.add(resultToClient(res));
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
        String query = "SELECT id, car_id, client_id, condition_id, rental_date, duration, is_returned, firm_id FROM rental WHERE firm_id = ?";

        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){

            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,Data.operator.getFirm_id());
            ResultSet res = statement.executeQuery();

            while(res.next()){
                rentalArray.add(resultToRental(res));
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return rentalArray.toArray(new Rental[0]);
    }

    public static Return[] getReturns(){
        ArrayList<Return> returnArray = new ArrayList<>();
        String query = "SELECT return.id AS id, rental_id, return_date FROM return INNER JOIN rental ON rental.id = rental_id WHERE firm_id = ?";

        try(Connection con = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword)){
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, Data.operator.getFirm_id());
            ResultSet res = statement.executeQuery();

            while(res.next()) {
                returnArray.add(resultToReturn(res));
            }
        }
        catch(SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return returnArray.toArray(new Return[0]);
    }

    public static Operator[] getOperators(){
        ArrayList<Operator> operatorArray = new ArrayList<>();
        try(Connection con = DriverManager.getConnection(databaseUrl,databaseUser,databasePassword)){
            String query = "SELECT id, username, id_firm FROM users";
            PreparedStatement statement = con.prepareStatement(query);
            ResultSet res = statement.executeQuery();

            while(res.next()) {
                if(res.getInt("id") != 0){
                    Operator newOperator = new Operator(res.getInt("id"));
                    newOperator.setUsername(res.getString("username"));
                    newOperator.setFirm_id(res.getInt("id_firm"));
                    operatorArray.add(newOperator);
                }
            }
        }
        catch (SQLException ex){
            Logger lgr=Logger.getLogger(JavaPostgreSQL.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return operatorArray.toArray(new Operator[0]);
    }

    /*================================================================================================================*/
    /*=========================================== Converters of data =================================================*/
    /*================================================================================================================*/


    public static Vehicle resultToVehicle(ResultSet res) throws SQLException {
        return new Vehicle(res.getInt("id"),
            res.getInt("class"),
            res.getInt("category"),
            res.getInt("firm_id"),
            res.getString("characteristics"),
            res.getBoolean("smoking"));
    }

    public static Client resultToClient(ResultSet res) throws SQLException {
        return new Client(res.getInt("id"),
                res.getInt("firm_id"),
                res.getInt("rating"),
                res.getString("name"),
                res.getString("phone"));
    }

    public static Condition resultToCondition(ResultSet res) throws SQLException{
        return new Condition(res.getInt("id"),
                res.getInt("odometer"),
                res.getInt("car_id"),
                res.getString("damages"));
    }

    public static Rental resultToRental(ResultSet res) throws SQLException{
        Vehicle newVehicle = new Vehicle(res.getInt("car_id"));
        Client newClient = new Client(res.getInt("client_id"));
        Condition newCondition = new Condition(res.getInt("condition_id"));
        return new Rental(res.getInt("id"),
                res.getInt("firm_id"),
                newVehicle, newClient, newCondition,
                res.getInt("duration"),
                res.getDate("rental_date"),
                res.getBoolean("is_returned"));
    }

    public static Return resultToReturn(ResultSet res) throws SQLException{
        Rental newRental = new Rental(res.getInt("rental_id"));
        return new Return(res.getInt("id"),
                newRental,
                res.getDate("return_date"));
    }

    public static VehicleClass resultToClass(ResultSet res) throws SQLException{
        return new VehicleClass(res.getInt("id_class"),
                res.getString("name_class"));
    }

    public static VehicleCategory resultToCategory(ResultSet res) throws SQLException{
        return new VehicleCategory(res.getInt("id_category"),
                res.getString("name_category"));
    }

    public static Firm resultToFirm(ResultSet res) throws SQLException{
        return new Firm(res.getInt("id"),
                res.getString("firm_name"));
    }
}
