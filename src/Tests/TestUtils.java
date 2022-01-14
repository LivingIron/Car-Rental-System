import java.sql.*;

public class TestUtils {
    private static final String databaseUrl = "jdbc:postgresql://localhost:5432/CarRental";
    private static final String databaseUser = "postgres";
    private static final String databasePassword = "7232";

    public static void deleteVehicle(int id){
        try(Connection con = DriverManager.getConnection(databaseUrl,databaseUser,databasePassword)){
            String query = "DELETE FROM condition WHERE car_id = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
            query = "DELETE FROM car WHERE id = ?";
            statement = con.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public static void deletePhoto(int id){
        try(Connection con = DriverManager.getConnection(databaseUrl,databaseUser,databasePassword)){
            String query = "DELETE FROM photo WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1,id);
            statement.executeUpdate();
        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }
    }
    public static void deleteFirm(int id){
        try(Connection con = DriverManager.getConnection(databaseUrl,databaseUser,databasePassword)){
            String query = "DELETE FROM firm WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public static void deleteClient(int id){
        try(Connection con = DriverManager.getConnection(databaseUrl,databaseUser,databasePassword)){
            String query = "DELETE FROM client WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public static void deleteOperator(int id){
        try(Connection con = DriverManager.getConnection(databaseUrl,databaseUser,databasePassword)){
            String query = "DELETE FROM users WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void deleteRental(int id){
        try(Connection con = DriverManager.getConnection(databaseUrl,databaseUser,databasePassword)){
            String query = "DELETE FROM rental WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public static void deleteReturn(int id){
        try(Connection con = DriverManager.getConnection(databaseUrl,databaseUser,databasePassword)){
            String query = "DELETE FROM return WHERE id = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
