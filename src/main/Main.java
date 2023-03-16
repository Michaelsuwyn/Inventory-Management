package main;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

import java.sql.Array;
import java.util.ArrayList;
/**
 * FUTURE ENHANCEMENTS:
 * - Form for creation of a new part based off of an already existing part
 * - Form for creation of a new product based off of an already existing product
 * - Create pivot tables where users can view all data of parts/products easily
 * - Pending sales form to update the Inventory/Stock fields.
 * RUNTIME ERROR in Main Menu > searchProducts:
 * was receiving a NumberFormat Exception when no search results matched.
 * This was due to the search checking for String value first and then Integer Value.
 * If neither was the case it was throwing the exception. To fix this, I wrapped the
 * parseInt function in a try / catch block and alert the user if no match is found.
 *
 * JavaDoc has been submitted as a separate folder with this submission.
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        addTestData();

        Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setTitle("Main Menu");
        stage.setScene(new Scene(root, 1400, 600));
        stage.show();
    }
    /**
     * This function adds the Test Data
     * RUNTIME ERROR: Was receiving error when trying to create Part and passing in Machine ID. I realized that
     * I needed to create an InHouse object and not Part. I decided to create both InHouse and Outsourced Objects
     * for the test data.
     */
    private void addTestData(){
        InHouse brakes = new InHouse(1, "Brakes", 14.99, 5, 2, 10, 435);
        Inventory.addPart(brakes);
        InHouse tires = new InHouse(2, "Tires", 29.99, 10, 2, 20, 438);
        Inventory.addPart(tires);
        Outsourced shocks = new Outsourced(3, "Shocks", 25.99, 20, 1, 100, "Bikes Plus");
        Inventory.addPart(shocks);
        Product mongoose = new Product(1, "Mongoose", 199.99, 20, 1, 25);
        Inventory.addProduct(mongoose);
        Product huffy = new Product(2, "Huffy", 150.99, 5, 1, 10);
        Inventory.addProduct(huffy);

    }

    public static void main(String[] args){
        launch(args);
    }
}
