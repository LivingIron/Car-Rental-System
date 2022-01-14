import org.group29.Data;
import org.group29.JavaPostgreSQL;
import org.group29.entities.*;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class JavaPostgreSQLTests {
    @Test
    public void loginWrongCredentialsTest(){
        assertEquals(-1, JavaPostgreSQL.loginToDatabase("wrongUsername", "wrongPassword"));
    }
    @Test
    public void loginAdminCredentialsTest(){
        assertEquals(0, JavaPostgreSQL.loginToDatabase("admin", "admin"));
    }

    @Test
    public void addOperatorTest(){
        Operator testOperator = new Operator(-1, 0, "testUser", "testPass");
        int returnedID = JavaPostgreSQL.addOperator(testOperator);

        assertNotEquals(-1, returnedID);

        TestUtils.deleteOperator(returnedID);
    }
    @Test
    public void addExistingOperatorTest(){
        Operator testOperator = new Operator(-1, 0, "testUser", "testPass");
        int firstID = JavaPostgreSQL.addOperator(testOperator);
        int returnedID = JavaPostgreSQL.addOperator(testOperator);

        assertEquals(-1, returnedID);

        TestUtils.deleteOperator(firstID);
    }
    @Test
    public void getOperatorTest(){
        Operator testOperator = new Operator(-1, 0, "testUser", "testPass");
        int returnedID = JavaPostgreSQL.addOperator(testOperator);
        Operator operatorToFind = new Operator(returnedID);
        JavaPostgreSQL.getOperator(operatorToFind);

        assertEquals("testUser", operatorToFind.getUsername());
        assertEquals("testPass", operatorToFind.getPassword());

        TestUtils.deleteOperator(returnedID);
    }
    @Test
    public void getOperatorNonExistantTest(){
        Operator testOperator = new Operator(Integer.MAX_VALUE);
        JavaPostgreSQL.getOperator(testOperator);

        assertNull(testOperator.getUsername());
        assertNull(testOperator.getPassword());
    }
    @Test
    public void updateOperatorTest(){
        Operator testOperator = new Operator(-1, 0, "testUser", "testPass");
        int returnedID = JavaPostgreSQL.addOperator(testOperator);
        testOperator.setId(returnedID);
        testOperator.setUsername("updatedUser");
        testOperator.setPassword("updatedPass");
        JavaPostgreSQL.updateOperator(testOperator);
        Operator operatorToFind = new Operator(returnedID);
        JavaPostgreSQL.getOperator(operatorToFind);

        assertEquals("updatedUser", operatorToFind.getUsername());
        assertEquals("updatedPass", operatorToFind.getPassword());

        TestUtils.deleteOperator(returnedID);
    }
    @Test
    public void getOperatorsTest(){
        Operator testOperator = new Operator(-1, 0, "testUser", "testPass");
        int returnedID = JavaPostgreSQL.addOperator(testOperator);
        Operator[] operatorsToFind = JavaPostgreSQL.getOperators();

        assertNotNull(operatorsToFind);

        TestUtils.deleteOperator(returnedID);
    }
    @Test
    public void getOperatorsCountTest(){
        Operator testOperator = new Operator(-1, 0, "testUser", "testPass");
        int returnedID = JavaPostgreSQL.addOperator(testOperator);
        Operator[] operatorsToFind = JavaPostgreSQL.getOperators();
        int initialCount = operatorsToFind.length;
        testOperator.setUsername("secondTestUser");
        int secondID = JavaPostgreSQL.addOperator(testOperator);
        operatorsToFind = JavaPostgreSQL.getOperators();
        int newCount = operatorsToFind.length;

        assertEquals(initialCount + 1, newCount);

        TestUtils.deleteOperator(returnedID);
        TestUtils.deleteOperator(secondID);
    }
    @Test
    public void loginOperatorCredentialsTest(){
        Operator testOperator = new Operator(-1, 0, "testUser", "testPass");
        int returnedID = JavaPostgreSQL.addOperator(testOperator);

        assertEquals(returnedID, JavaPostgreSQL.loginToDatabase("testUser", "testPass"));

        TestUtils.deleteOperator(returnedID);
    }

    @Test
    public void addVehicleTest(){
        Vehicle testVehicle = new Vehicle(-1, 1, 1, 0, "test", false);
        int returnedID = JavaPostgreSQL.addVehicle(testVehicle);

        assertNotEquals(-1, returnedID);

        TestUtils.deleteVehicle(returnedID);
    }
    @Test
    public void getVehicleTest(){
        Vehicle testVehicle = new Vehicle(-1, 1, 1, 0, "test", false);
        int returnedID = JavaPostgreSQL.addVehicle(testVehicle);
        Vehicle vehicleToFind = new Vehicle(returnedID);
        JavaPostgreSQL.getVehicle(vehicleToFind);

        assertEquals("test", vehicleToFind.getCharacteristics());

        TestUtils.deleteVehicle(returnedID);
    }
    @Test
    public void getVehicleNonExistantTest(){
        Vehicle testVehicle = new Vehicle(Integer.MAX_VALUE);
        JavaPostgreSQL.getVehicle(testVehicle);

        assertNull(testVehicle.getCharacteristics());
    }
    @Test
    public void updateVehicleTest(){
        Vehicle testVehicle = new Vehicle(-1, 1, 1, 0, "test", false);
        int returnedID = JavaPostgreSQL.addVehicle(testVehicle);
        testVehicle.setId(returnedID);
        testVehicle.setCharacteristics("updated test");
        JavaPostgreSQL.updateVehicle(testVehicle);

        Vehicle vehicleToFind = new Vehicle(returnedID);
        JavaPostgreSQL.getVehicle(vehicleToFind);

        assertEquals("updated test", vehicleToFind.getCharacteristics());

        TestUtils.deleteVehicle(returnedID);
    }
    @Test
    public void getVehiclesCountTest(){
        int initialCount = JavaPostgreSQL.getVehicles().length;
        Vehicle testVehicle = new Vehicle(-1, 1, 1, 0, "test", false);
        int returnedID = JavaPostgreSQL.addVehicle(testVehicle);
        int increasedCount = JavaPostgreSQL.getVehicles().length;
        assertEquals(initialCount + 1, increasedCount);

        TestUtils.deleteVehicle(returnedID);
    }

    @Test
    public void addFirmTest(){
        Firm testFirm = new Firm(-1, "test");
        int returnedID = JavaPostgreSQL.addFirm(testFirm);

        assertNotEquals(-1, returnedID);

        TestUtils.deleteFirm(returnedID);
    }
    @Test
    public void addExistingFirmTest(){
        Firm testFirm = new Firm(-1, "test");
        int firstID = JavaPostgreSQL.addFirm(testFirm);
        int returnedID = JavaPostgreSQL.addFirm(testFirm);

        assertEquals(-1, returnedID);

        TestUtils.deleteFirm(firstID);
    }
    @Test
    public void updateFirmTest(){
        Firm testFirm = new Firm(-1, "test");
        int returnedID = JavaPostgreSQL.addFirm(testFirm);
        testFirm.setId(returnedID);
        testFirm.setName("updated test");
        JavaPostgreSQL.updateFirm(testFirm);

        Optional<Firm> found = Arrays.stream(JavaPostgreSQL.getFirms()).filter(f -> f.getId() == returnedID).findFirst();
        assert found.isPresent();
        assertEquals("updated test", found.get().getName());

        TestUtils.deleteFirm(returnedID);
    }
    @Test
    public void getFirmsCountTest(){
        int initialCount = JavaPostgreSQL.getFirms().length;
        Firm testFirm = new Firm(-1, "test");
        int returnedID = JavaPostgreSQL.addFirm(testFirm);
        int increasedCount = JavaPostgreSQL.getFirms().length;
        assertEquals(initialCount + 1, increasedCount);

        TestUtils.deleteFirm(returnedID);
    }

    @Test
    public void addPhotoTest(){
        Vehicle testVehicle = new Vehicle(-1, 1, 1, 0, "test", false);
        int vehicleID = JavaPostgreSQL.addVehicle(testVehicle);
        VehiclePhoto testPhoto = new VehiclePhoto(-1, vehicleID, new byte[0]);
        int returnedID = JavaPostgreSQL.addPhoto(testPhoto);

        assertNotEquals(-1, returnedID);

        TestUtils.deletePhoto(returnedID);
        TestUtils.deleteVehicle(vehicleID);
    }
    @Test
    public void getPhotoTest(){
        Vehicle testVehicle = new Vehicle(-1, 1, 1, 0, "test", false);
        int vehicleID = JavaPostgreSQL.addVehicle(testVehicle);
        VehiclePhoto testPhoto = new VehiclePhoto(-1, vehicleID, new byte[0]);
        int returnedID = JavaPostgreSQL.addPhoto(testPhoto);
        VehiclePhoto photoToFind = new VehiclePhoto(returnedID);
        JavaPostgreSQL.getPhoto(photoToFind);

        assertNotEquals(-1, photoToFind.getId());

        TestUtils.deletePhoto(returnedID);
        TestUtils.deleteVehicle(vehicleID);
    }
    @Test
    public void getPhotoNonExistantTest(){
        VehiclePhoto testPhoto = new VehiclePhoto(Integer.MAX_VALUE);
        JavaPostgreSQL.getPhoto(testPhoto);

        assertNull(testPhoto.getByteArray());
    }
    @Test
    public void deletePhotoTest(){
        Vehicle testVehicle = new Vehicle(-1, 1, 1, 0, "test", false);
        int vehicleID = JavaPostgreSQL.addVehicle(testVehicle);
        VehiclePhoto testPhoto = new VehiclePhoto(-1, vehicleID, new byte[0]);
        int returnedID = JavaPostgreSQL.addPhoto(testPhoto);
        testPhoto.setId(returnedID);
        JavaPostgreSQL.deletePhoto(testPhoto);
        VehiclePhoto photoToFind = new VehiclePhoto(returnedID);
        JavaPostgreSQL.getPhoto(photoToFind);

        assertNull(photoToFind.getByteArray());

        TestUtils.deleteVehicle(vehicleID);
    }
    @Test
    public void getPhotosByVehicleTest(){
        Vehicle testVehicle = new Vehicle(-1, 1, 1, 0, "test", false);
        int vehicleID = JavaPostgreSQL.addVehicle(testVehicle);
        testVehicle.setId(vehicleID);
        VehiclePhoto testPhoto = new VehiclePhoto(-1, vehicleID, new byte[0]);
        JavaPostgreSQL.addPhoto(testPhoto);

        VehiclePhoto[] photosToFind = JavaPostgreSQL.getPhotosByVehicle(testVehicle);

        assertNotNull(photosToFind);

        for(VehiclePhoto p : photosToFind) TestUtils.deletePhoto(p.getId());
        TestUtils.deleteVehicle(vehicleID);
    }
    @Test
    public void getPhotosByVehicleCountTest(){
        Vehicle testVehicle = new Vehicle(-1, 1, 1, 0, "test", false);
        int vehicleID = JavaPostgreSQL.addVehicle(testVehicle);
        testVehicle.setId(vehicleID);
        VehiclePhoto testPhoto = new VehiclePhoto(-1, vehicleID, new byte[0]);
        JavaPostgreSQL.addPhoto(testPhoto);
        JavaPostgreSQL.addPhoto(testPhoto);

        VehiclePhoto[] photosToFind = JavaPostgreSQL.getPhotosByVehicle(testVehicle);

        assertEquals(2, photosToFind.length);

        for(VehiclePhoto p : photosToFind) TestUtils.deletePhoto(p.getId());
        TestUtils.deleteVehicle(vehicleID);
    }

    @Test
    public void getCarConditionTest(){
        Vehicle testVehicle = new Vehicle(-1, 1, 1, 0, "test", false);
        int vehicleID = JavaPostgreSQL.addVehicle(testVehicle);
        Condition testCondition = JavaPostgreSQL.getCarCondition(vehicleID);

        assertNotNull(testCondition);

        TestUtils.deleteVehicle(vehicleID);
    }
    @Test
    public void getCarConditionNonExistantTest(){
        Condition testCondition = JavaPostgreSQL.getCarCondition(Integer.MAX_VALUE);

        assertNull(testCondition);
    }
    @Test
    public void getConditionTest(){
        Vehicle testVehicle = new Vehicle(-1, 1, 1, 0, "test", false);
        int vehicleID = JavaPostgreSQL.addVehicle(testVehicle);
        Condition testCondition = JavaPostgreSQL.getCarCondition(vehicleID);
        Condition conditionToFind = new Condition(-1);
        assert testCondition != null;
        conditionToFind.setId(testCondition.getId());
        JavaPostgreSQL.getCondition(conditionToFind);

        assertNotNull(conditionToFind);

        TestUtils.deleteVehicle(vehicleID);
    }
    @Test
    public void getConditionNonExistantTest(){
        Condition testCondition = new Condition(Integer.MAX_VALUE);
        JavaPostgreSQL.getCondition(testCondition);

        assertNull(testCondition.getDamages());
    }
    @Test
    public void updateConditionTest(){
        Vehicle testVehicle = new Vehicle(-1, 1, 1, 0, "test", false);
        int vehicleID = JavaPostgreSQL.addVehicle(testVehicle);
        Condition testCondition = JavaPostgreSQL.getCarCondition(vehicleID);
        assert testCondition != null;
        testCondition.setDamages("updated test");

        JavaPostgreSQL.updateCondition(testCondition);
        Condition conditionToFind = new Condition(testCondition.getId());
        JavaPostgreSQL.getCondition(conditionToFind);

        assertEquals("updated test", conditionToFind.getDamages());

        TestUtils.deleteVehicle(vehicleID);
    }

    @Test
    public void addClientTest(){
        Client testClient = new Client(-1, 0, 0, "test client", "0000000000");
        int returnedID = JavaPostgreSQL.addClient(testClient);

        assertNotEquals(-1, returnedID);

        TestUtils.deleteClient(returnedID);
    }
    @Test
    public void addExistingClientTest(){
        Client testClient = new Client(-1, 0, 0, "test client", "0000000000");
        int firstID = JavaPostgreSQL.addClient(testClient);
        int returnedID = JavaPostgreSQL.addClient(testClient);

        assertEquals(-1, returnedID);

        TestUtils.deleteClient(firstID);
    }
    @Test
    public void updateClientTest(){
        Client testClient = new Client(-1, 0, 0, "test client", "0000000000");
        int returnedID = JavaPostgreSQL.addClient(testClient);
        testClient.setId(returnedID);
        testClient.setName("updated test");
        JavaPostgreSQL.updateClient(testClient);
        Client clientToFind = new Client(returnedID);
        JavaPostgreSQL.getClient(clientToFind);

        assertEquals("updated test", clientToFind.getName());

        TestUtils.deleteClient(returnedID);
    }
    @Test
    public void getClientsTest(){
        Client testClient = new Client(-1, 0, 0, "test client", "0000000000");
        int returnedID = JavaPostgreSQL.addClient(testClient);
        Client[] clientsToFind = JavaPostgreSQL.getClients();

        assertNotNull(clientsToFind);

        TestUtils.deleteClient(returnedID);
    }
    @Test
    public void getClientsCountTest(){
        Client testClient = new Client(-1, 0, 0, "test client", "0000000000");
        int returnedID = JavaPostgreSQL.addClient(testClient);
        Client[] clientsToFind = JavaPostgreSQL.getClients();
        int initialCount = clientsToFind.length;
        testClient.setName("second test client");
        int secondID = JavaPostgreSQL.addClient(testClient);
        clientsToFind = JavaPostgreSQL.getClients();
        int increasedCount = clientsToFind.length;

        assertEquals(initialCount + 1, increasedCount);

        TestUtils.deleteClient(returnedID);
        TestUtils.deleteClient(secondID);
    }

    @Test
    public void getCategoryNamesTest(){
        VehicleCategory[] categoriesToFind = JavaPostgreSQL.getCategoryNames();

        assertNotNull(categoriesToFind);
    }
    @Test
    public void getClassificationNamesTest(){
        VehicleClass[] classesToFind = JavaPostgreSQL.getClassNames();

        assertNotNull(classesToFind);
    }

    @Test
    public void addRentalTest(){
        int vehicleID = -1, clientID = -1, returnedID = -1;
        try {
        Vehicle testVehicle = new Vehicle(-1, 1, 1, 0, "test", false);
        Client testClient = new Client(-1, 0, 0, "test client", "0000000000");
        vehicleID = JavaPostgreSQL.addVehicle(testVehicle);
        testVehicle.setId(vehicleID);
        clientID = JavaPostgreSQL.addClient(testClient);
        testClient.setId(clientID);
        Condition testCondition = JavaPostgreSQL.getCarCondition(testVehicle.getId());
            Rental testRental = new Rental(-1, 0, testVehicle, testClient, testCondition, 1, Date.valueOf(LocalDate.now()), false);
            returnedID = JavaPostgreSQL.addRental(testRental);

            assertNotEquals(-1, returnedID);
        }
        finally{
            TestUtils.deleteRental(returnedID);
            TestUtils.deleteClient(clientID);
            TestUtils.deleteVehicle(vehicleID);
        }
    }
    @Test
    public void getRentalTest(){
        int vehicleID = -1, clientID = -1, returnedID = -1;
        try {
            Vehicle testVehicle = new Vehicle(-1, 1, 1, 0, "test", false);
            Client testClient = new Client(-1, 0, 0, "test client", "0000000000");
            vehicleID = JavaPostgreSQL.addVehicle(testVehicle);
            testVehicle.setId(vehicleID);
            clientID = JavaPostgreSQL.addClient(testClient);
            testClient.setId(clientID);
            Condition testCondition = JavaPostgreSQL.getCarCondition(testVehicle.getId());
            Rental testRental = new Rental(-1, 0, testVehicle, testClient, testCondition, 1, Date.valueOf(LocalDate.now()), false);
            returnedID = JavaPostgreSQL.addRental(testRental);

            Rental rentalToFind = new Rental(returnedID);
            JavaPostgreSQL.getRental(rentalToFind);
            assertNotNull(rentalToFind.getClient());
            assertNotNull(rentalToFind.getVehicle());
            assertNotNull(rentalToFind.getCondition());

        }
        finally {
            TestUtils.deleteRental(returnedID);
            TestUtils.deleteClient(clientID);
            TestUtils.deleteVehicle(vehicleID);
        }
    }
    @Test
    public void updateRentalTest(){
        int vehicleID = -1, clientID = -1, returnedID = -1;
        try {
        Vehicle testVehicle = new Vehicle(-1, 1, 1, 0, "test", false);
        Client testClient = new Client(-1, 0, 0, "test client", "0000000000");
        vehicleID = JavaPostgreSQL.addVehicle(testVehicle);
        testVehicle.setId(vehicleID);
        clientID = JavaPostgreSQL.addClient(testClient);
        testClient.setId(clientID);
        Condition testCondition = JavaPostgreSQL.getCarCondition(testVehicle.getId());
        Rental testRental = new Rental(-1, 0, testVehicle, testClient, testCondition, 1, Date.valueOf(LocalDate.now()), false);
        returnedID = JavaPostgreSQL.addRental(testRental);
        testRental.setId(returnedID);
        testRental.setDuration(2);
        JavaPostgreSQL.updateRental(testRental);

        Rental rentalToFind = new Rental(returnedID);
        JavaPostgreSQL.getRental(rentalToFind);
        assertEquals(2, rentalToFind.getDuration());

        }
        finally {
            TestUtils.deleteRental(returnedID);
            TestUtils.deleteClient(clientID);
            TestUtils.deleteVehicle(vehicleID);
        }
    }
    @Test
    public void getRentalsByVehicleTest(){
        int vehicleID = -1, clientID = -1, returnedID = -1;
        try{
            Vehicle testVehicle = new Vehicle(-1, 1, 1, 0, "test", false);
            Client testClient = new Client(-1, 0, 0, "test client", "0000000000");
            Data.operator = new Operator(-1, 0, "", "");
            vehicleID = JavaPostgreSQL.addVehicle(testVehicle);
            testVehicle.setId(vehicleID);
            clientID = JavaPostgreSQL.addClient(testClient);
            testClient.setId(clientID);
            Condition testCondition = JavaPostgreSQL.getCarCondition(testVehicle.getId());
            Rental testRental = new Rental(-1, 0, testVehicle, testClient, testCondition, 1, Date.valueOf(LocalDate.now()), false);
            returnedID = JavaPostgreSQL.addRental(testRental);

            Rental[] rentalsToFind = JavaPostgreSQL.getRentalsByVehicle(testVehicle, Date.valueOf(LocalDate.now().minusDays(1)), Date.valueOf(LocalDate.now()));
            assertEquals(1, rentalsToFind.length);

        }
        finally {
            TestUtils.deleteRental(returnedID);
            TestUtils.deleteClient(clientID);
            TestUtils.deleteVehicle(vehicleID);
        }
    }
    @Test
    public void getRentalsTest(){
        int vehicleID = -1, clientID = -1, returnedID = -1;
        try{
            Vehicle testVehicle = new Vehicle(-1, 1, 1, 0, "test", false);
            Client testClient = new Client(-1, 0, 0, "test client", "0000000000");
            Data.operator = new Operator(-1, 0, "", "");
            vehicleID = JavaPostgreSQL.addVehicle(testVehicle);
            testVehicle.setId(vehicleID);
            clientID = JavaPostgreSQL.addClient(testClient);
            testClient.setId(clientID);
            Condition testCondition = JavaPostgreSQL.getCarCondition(testVehicle.getId());
            Rental testRental = new Rental(-1, 0, testVehicle, testClient, testCondition, 1, Date.valueOf(LocalDate.now()), false);
            returnedID = JavaPostgreSQL.addRental(testRental);

            Rental[] rentalsToFind = JavaPostgreSQL.getRentals();
            assertNotEquals(0, rentalsToFind.length);
        }
        finally {
            TestUtils.deleteRental(returnedID);
            TestUtils.deleteClient(clientID);
            TestUtils.deleteVehicle(vehicleID);
        }
    }

    @Test
    public void getAvailableVehiclesTest(){
        Vehicle testVehicle = new Vehicle(-1, 1, 1, 0, "test", false);
        Data.operator = new Operator(-1, 0, "", "");
        int vehicleID = JavaPostgreSQL.addVehicle(testVehicle);

        Vehicle[] vehiclesToFind = JavaPostgreSQL.getAvailableVehicles(Date.valueOf(LocalDate.now().minusDays(1)), Date.valueOf(LocalDate.now().plusDays(1)));
        assertNotEquals(0, vehiclesToFind.length);

        TestUtils.deleteVehicle(vehicleID);
    }
    @Test
    public void getAvailableVehiclesUnavailableTest(){
        int vehicleID = -1, clientID = -1, returnedID = -1;
        try {
            Vehicle testVehicle = new Vehicle(-1, 1, 1, 0, "test", false);
            Client testClient = new Client(-1, 0, 0, "test client", "0000000000");
            Data.operator = new Operator(-1, 0, "", "");
            vehicleID = JavaPostgreSQL.addVehicle(testVehicle);
            testVehicle.setId(vehicleID);
            clientID = JavaPostgreSQL.addClient(testClient);
            testClient.setId(clientID);
            Condition testCondition = JavaPostgreSQL.getCarCondition(testVehicle.getId());
            Rental testRental = new Rental(-1, 0, testVehicle, testClient, testCondition, 1, Date.valueOf(LocalDate.now()), false);
            returnedID = JavaPostgreSQL.addRental(testRental);

            Vehicle[] vehiclesToFind = JavaPostgreSQL.getAvailableVehicles(Date.valueOf(LocalDate.now().minusDays(1)), Date.valueOf(LocalDate.now().plusDays(1)));
            assertEquals(0, vehiclesToFind.length);
        }
        finally {
            TestUtils.deleteRental(returnedID);
            TestUtils.deleteClient(clientID);
            TestUtils.deleteVehicle(vehicleID);
        }
    }

    @Test
    public void addReturnTest(){
        int vehicleID = -1, clientID = -1, rentalID = -1, returnedID = -1;
        try {
            Vehicle testVehicle = new Vehicle(-1, 1, 1, 0, "test", false);
            Client testClient = new Client(-1, 0, 0, "test client", "0000000000");
            Data.operator = new Operator(-1, 0, "", "");
            vehicleID = JavaPostgreSQL.addVehicle(testVehicle);
            testVehicle.setId(vehicleID);
            clientID = JavaPostgreSQL.addClient(testClient);
            testClient.setId(clientID);
            Condition testCondition = JavaPostgreSQL.getCarCondition(testVehicle.getId());
            Rental testRental = new Rental(-1, 0, testVehicle, testClient, testCondition, 1, Date.valueOf(LocalDate.now()), false);
            rentalID = JavaPostgreSQL.addRental(testRental);
            testRental.setId(rentalID);
            Return testReturn = new Return(-1, testRental, Date.valueOf(LocalDate.now().plusDays(1)));
            returnedID = JavaPostgreSQL.addReturn(testReturn);

            assertNotEquals(-1, returnedID);
        }
        finally {
            TestUtils.deleteReturn(returnedID);
            TestUtils.deleteRental(rentalID);
            TestUtils.deleteClient(clientID);
            TestUtils.deleteVehicle(vehicleID);
        }
    }
    @Test
    public void addReturnBeforeRentalTest(){
        int vehicleID = -1, clientID = -1, rentalID = -1, returnedID = -1;
        try {
            Vehicle testVehicle = new Vehicle(-1, 1, 1, 0, "test", false);
            Client testClient = new Client(-1, 0, 0, "test client", "0000000000");
            Data.operator = new Operator(-1, 0, "", "");
            vehicleID = JavaPostgreSQL.addVehicle(testVehicle);
            testVehicle.setId(vehicleID);
            clientID = JavaPostgreSQL.addClient(testClient);
            testClient.setId(clientID);
            Condition testCondition = JavaPostgreSQL.getCarCondition(testVehicle.getId());
            Rental testRental = new Rental(-1, 0, testVehicle, testClient, testCondition, 1, Date.valueOf(LocalDate.now()), false);
            rentalID = JavaPostgreSQL.addRental(testRental);
            testRental.setId(rentalID);
            Return testReturn = new Return(-1, testRental, Date.valueOf(LocalDate.now().minusDays(2)));
            returnedID = JavaPostgreSQL.addReturn(testReturn);

            assertEquals(-1, returnedID);
        }
        finally {
            TestUtils.deleteReturn(returnedID);
            TestUtils.deleteRental(rentalID);
            TestUtils.deleteClient(clientID);
            TestUtils.deleteVehicle(vehicleID);
        }
    }
    @Test
    public void getReturnTest(){
        int vehicleID = -1, clientID = -1, rentalID = -1, returnedID = -1;
        try {
            Vehicle testVehicle = new Vehicle(-1, 1, 1, 0, "test", false);
            Client testClient = new Client(-1, 0, 0, "test client", "0000000000");
            Data.operator = new Operator(-1, 0, "", "");
            vehicleID = JavaPostgreSQL.addVehicle(testVehicle);
            testVehicle.setId(vehicleID);
            clientID = JavaPostgreSQL.addClient(testClient);
            testClient.setId(clientID);
            Condition testCondition = JavaPostgreSQL.getCarCondition(testVehicle.getId());
            Rental testRental = new Rental(-1, 0, testVehicle, testClient, testCondition, 1, Date.valueOf(LocalDate.now()), false);
            rentalID = JavaPostgreSQL.addRental(testRental);
            testRental.setId(rentalID);
            Return testReturn = new Return(-1, testRental, Date.valueOf(LocalDate.now().plusDays(1)));
            returnedID = JavaPostgreSQL.addReturn(testReturn);
            Return returnToFind = new Return(returnedID, null, null);
            JavaPostgreSQL.getReturn(returnToFind);

            assertNotNull(returnToFind.getRental());
        }
        finally {
            TestUtils.deleteReturn(returnedID);
            TestUtils.deleteRental(rentalID);
            TestUtils.deleteClient(clientID);
            TestUtils.deleteVehicle(vehicleID);
        }
    }
    @Test
    public void getReturnsTest(){
        int vehicleID = -1, clientID = -1, rentalID = -1, returnedID = -1;
        try{
            Vehicle testVehicle = new Vehicle(-1, 1, 1, 0, "test", false);
            Client testClient = new Client(-1, 0, 0, "test client", "0000000000");
            Data.operator = new Operator(-1, 0, "", "");
            vehicleID = JavaPostgreSQL.addVehicle(testVehicle);
            testVehicle.setId(vehicleID);
            clientID = JavaPostgreSQL.addClient(testClient);
            testClient.setId(clientID);
            Condition testCondition = JavaPostgreSQL.getCarCondition(testVehicle.getId());
            Rental testRental = new Rental(-1, 0, testVehicle, testClient, testCondition, 1, Date.valueOf(LocalDate.now()), false);
            rentalID = JavaPostgreSQL.addRental(testRental);
            testRental.setId(rentalID);
            Return testReturn = new Return(-1, testRental, Date.valueOf(LocalDate.now().plusDays(1)));
            returnedID = JavaPostgreSQL.addReturn(testReturn);
            Return[] returnsToFind = JavaPostgreSQL.getReturns();

            assertNotEquals(0, returnsToFind.length);
        }
        finally {
            TestUtils.deleteReturn(returnedID);
            TestUtils.deleteRental(rentalID);
            TestUtils.deleteClient(clientID);
            TestUtils.deleteVehicle(vehicleID);
        }
    }

    @Test
    public void getPriceTest(){
        int vehicleID = -1, clientID = -1, rentalID = -1, returnedID = -1;
        try{
            Vehicle testVehicle = new Vehicle(-1, 1, 1, 0, "test", false);
            Client testClient = new Client(-1, 0, 0, "test client", "0000000000");
            Data.operator = new Operator(-1, 0, "", "");
            vehicleID = JavaPostgreSQL.addVehicle(testVehicle);
            testVehicle.setId(vehicleID);
            clientID = JavaPostgreSQL.addClient(testClient);
            testClient.setId(clientID);
            Condition testCondition = JavaPostgreSQL.getCarCondition(testVehicle.getId());
            Rental testRental = new Rental(-1, 0, testVehicle, testClient, testCondition, 1, Date.valueOf(LocalDate.now()), false);
            rentalID = JavaPostgreSQL.addRental(testRental);
            testRental.setId(rentalID);
            Return testReturn = new Return(-1, testRental, Date.valueOf(LocalDate.now().plusDays(1)));
            returnedID = JavaPostgreSQL.addReturn(testReturn);
            testReturn.setId(returnedID);
            double price = JavaPostgreSQL.getPrice(testReturn);

            assertNotEquals(-1, price);
        }
        finally {
            TestUtils.deleteReturn(returnedID);
            TestUtils.deleteRental(rentalID);
            TestUtils.deleteClient(clientID);
            TestUtils.deleteVehicle(vehicleID);
        }
    }

    @Test
    public void getOperatorHistoryTest(){
        int vehicleID = -1, clientID = -1, rentalID = -1, returnedID = -1;
        try{
            Vehicle testVehicle = new Vehicle(-1, 1, 1, 0, "test", false);
            Client testClient = new Client(-1, 0, 0, "test client", "0000000000");
            Data.operator = new Operator(-1, 0, "", "");
            vehicleID = JavaPostgreSQL.addVehicle(testVehicle);
            testVehicle.setId(vehicleID);
            clientID = JavaPostgreSQL.addClient(testClient);
            testClient.setId(clientID);
            Condition testCondition = JavaPostgreSQL.getCarCondition(testVehicle.getId());
            Rental testRental = new Rental(-1, 0, testVehicle, testClient, testCondition, 1, Date.valueOf(LocalDate.now()), false);
            rentalID = JavaPostgreSQL.addRental(testRental);
            testRental.setId(rentalID);
            Return testReturn = new Return(-1, testRental, Date.valueOf(LocalDate.now().plusDays(1)));
            returnedID = JavaPostgreSQL.addReturn(testReturn);
            testReturn.setId(returnedID);
            HistoryEntry[] history = JavaPostgreSQL.getOperatorHistory(Data.operator, Date.valueOf(LocalDate.now().minusDays(1)), Date.valueOf(LocalDate.now().plusDays(1)));

            assertNotNull(history);
        }
        finally {
            TestUtils.deleteReturn(returnedID);
            TestUtils.deleteRental(rentalID);
            TestUtils.deleteClient(clientID);
            TestUtils.deleteVehicle(vehicleID);
        }
    }
}
