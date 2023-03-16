package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainMenu implements Initializable {
    public TableView partsTable;
    public TableColumn partIdCol;
    public TableColumn partNameCol;
    public TableColumn invLabCol;
    public TableColumn priceCol;

    public TableView productsTable;
    public TableColumn productIdCol;
    public TableColumn productNameCol;
    public TableColumn productInvCol;
    public TableColumn productPriceCol;

    public static int partIdCounter = 3;
    public static int productIdCounter = 2;
    public static Part selectedPart;
    public static Product selectedProduct;
    public TextField searchPartsField;
    public TextField searchProductsLabel;

    /**
     * This function initializes the main menu controller
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partsTable.setItems(Inventory.getAllParts());
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        invLabCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productsTable.setItems(Inventory.getAllProducts());
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }
    /**
     * This function navigates to the add part page
     */
    public void addPartMenu(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * This function navigates to the modify part page
     */
    public void modifyPartMenu(ActionEvent actionEvent) throws IOException {
        if(partsTable.getSelectionModel().getSelectedItem() != null){
            selectedPart = (Part) partsTable.getSelectionModel().getSelectedItem();

            Parent root = FXMLLoader.load(getClass().getResource("/view/ModifyPart.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);
            stage.setTitle("Modify Part");
            stage.setScene(scene);
            stage.show();
        }
        else {
            Alert noModify = new Alert(Alert.AlertType.ERROR, "Please select a Product to modify.");
            Optional<ButtonType> result = noModify.showAndWait();
        }


    }
    /**
     * This function deletes a selected part
     */
    public void deletePart(ActionEvent actionEvent) {
        if (partsTable.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                selectedPart = (Part) partsTable.getSelectionModel().getSelectedItem();
                Inventory.deletePart(selectedPart);
            }
        } else {
            Alert noSelection = new Alert(Alert.AlertType.ERROR, "No Part has been selected");
            Optional<ButtonType> result = noSelection.showAndWait();
        }

    }



    /**
     * This function deletes a selected product
     */
    public void deleteProduct(ActionEvent actionEvent) {
        if (productsTable.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                selectedProduct = (Product) productsTable.getSelectionModel().getSelectedItem();
                if(selectedProduct.getAllAssociatedParts().size() == 0){
                    Inventory.deleteProduct(selectedProduct);
                }
                else {
                    Alert assParts = new Alert(Alert.AlertType.ERROR, "You must delete the associated parts before product can be deleted");
                    Optional<ButtonType> assPartResult = assParts.showAndWait();
                }
            }
        } else {
            Alert noSelection = new Alert(Alert.AlertType.ERROR, "No Part has been selected");
            Optional<ButtonType> result = noSelection.showAndWait();
        }
    }
    /**
     * This function navigates to the add product page
     */
    public void addProductMenu(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1000, 600);
        stage.setTitle("Add Product");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * This function navigates to the modify product page
     */
    public void modifyProductMenu(ActionEvent actionEvent) throws IOException {
        if(productsTable.getSelectionModel().getSelectedItem() != null){
            selectedProduct = (Product) productsTable.getSelectionModel().getSelectedItem();

            Parent root = FXMLLoader.load(getClass().getResource("/view/ModifyProduct.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1000, 600);
            stage.setTitle("Modify Product");
            stage.setScene(scene);
            stage.show();
        }
        else {
            Alert noModify = new Alert(Alert.AlertType.ERROR, "Please select a Product to modify.");
            Optional<ButtonType> result = noModify.showAndWait();
        }
    }
    /**
     * This function exits the application
     */
    public void exitBtn(ActionEvent actionEvent) {
        Platform.exit();
    }

    /**
     * This function searches for parts by id or name
     */
    public void searchParts(KeyEvent keyEvent) {
    String search = searchPartsField.getText();
    ObservableList<Part> searchedParts = Inventory.lookupPart(search);

    if(searchedParts.size() == 0){
        try{
            int idSearch = Integer.parseInt(search);
            Part p = Inventory.lookupPart(idSearch);
            if(p != null){
                searchedParts.add(p);
            }
            else {
                Alert noSearch = new Alert(Alert.AlertType.ERROR, "No Items are found that match.");
                Optional<ButtonType> result = noSearch.showAndWait();
            }
        }
        catch (NumberFormatException e){
            Alert noSearch = new Alert(Alert.AlertType.ERROR, "No Items are found that match.");
            Optional<ButtonType> result = noSearch.showAndWait();
        }

    }
        partsTable.setItems(searchedParts);

    }

    /**
     * This function searches for products by id or name
     * RUNTIME ERROR: was receiving a NumberFormat Exception when no search results matched.
     * This was due to the search checking for String value first and then Integer Value.
     * If neither was the case it was throwing the exception. To fix this, I wrapped the
     * parseInt function in a try / catch block and alert the user if no match is found.
     */
    public void searchProducts(KeyEvent keyEvent) {
        String search = searchProductsLabel.getText();
        ObservableList<Product> searchedProducts = Inventory.lookupProduct(search);

        if(searchedProducts.size() ==0){
            try{
                int idSearch = Integer.parseInt(search);
                Product p = Inventory.lookupProduct(idSearch);
                if(p != null){
                    searchedProducts.add(p);

                }
                else {
                    Alert noSearch = new Alert(Alert.AlertType.ERROR, "No Items are found that match.");
                    Optional<ButtonType> result = noSearch.showAndWait();
                }

            }
            catch(NumberFormatException e){
                Alert noSearch = new Alert(Alert.AlertType.ERROR, "No Items are found that match.");
                Optional<ButtonType> result = noSearch.showAndWait();
            }
        }
        productsTable.setItems(searchedProducts);
    }
}
