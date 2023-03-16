package controller;

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
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddProduct implements Initializable {
    public TableView partsTable;
    public TableColumn partID;
    public TableColumn partName;
    public TableColumn partInv;
    public TableColumn partCost;
    public TableView assPartsTable;
    public TableColumn assPartId;
    public TableColumn assPartName;
    public TableColumn assPartInv;
    public TableColumn assPartCost;
    public TextField productId;
    public TextField productName;
    public TextField productInv;
    public TextField productPrice;
    public TextField productMax;
    public TextField productMin;
    public Label errorMessageLabel;
    public TextField searchPartField;

    Product newProduct = new Product(0, "", 0, 0, 0, 0);

    /**
     * This function initializes the addProduct controller
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partsTable.setItems(Inventory.getAllParts());
        partID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partCost.setCellValueFactory(new PropertyValueFactory<>("price"));

        assPartsTable.setItems(newProduct.getAllAssociatedParts());
        assPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        assPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        assPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assPartCost.setCellValueFactory(new PropertyValueFactory<>("price"));

    }
    /**
     * This function navigates to the main menu and cancels product creation
     */
    public void cancelAddProduct(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1400, 600);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * This function adds associated part to a product
     */
    public void addAssociatedPart(ActionEvent actionEvent) {
        MainMenu.selectedPart = (Part) partsTable.getSelectionModel().getSelectedItem();
        newProduct.addAssociatedPart(MainMenu.selectedPart);

    }
    /**
     * This function saves a new product
     */
    public void saveProduct(ActionEvent actionEvent) throws IOException{
        if(!doesErrorExist()){
            int newLen = ++MainMenu.productIdCounter;
            String prodName = productName.getText().toString();
            int prodInv = Integer.parseInt(productInv.getText());
            double prodPrice = Double.parseDouble(productPrice.getText());
            int prodMax = Integer.parseInt(productMax.getText());
            int prodMin = Integer.parseInt(productMin.getText());

            newProduct.setId(newLen);
            newProduct.setName(prodName);
            newProduct.setStock(prodInv);
            newProduct.setPrice(prodPrice);
            newProduct.setMax(prodMax);
            newProduct.setMin(prodMin);

            Inventory.addProduct(newProduct);

            Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1400, 600);
            stage.setTitle("Main Menu");
            stage.setScene(scene);
            stage.show();
        }

    }
    /**
     * This function removes an associated part from a product
     */
    public void removeAssPart(ActionEvent actionEvent) {
        if(assPartsTable.getSelectionModel().getSelectedItem() != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                MainMenu.selectedPart = (Part) assPartsTable.getSelectionModel().getSelectedItem();
                newProduct.deleteAssociatedPart(MainMenu.selectedPart);
            }
        } else {
            Alert noSelection = new Alert(Alert.AlertType.ERROR, "No Part has been selected");
            Optional<ButtonType> result = noSelection.showAndWait();
        }
    }
    /**
     * This function does the error checking for add product page
     */
    public boolean doesErrorExist(){
        ArrayList<String> errorMessages = new ArrayList<>();
        String errorString = "Errors: " + "\n";

        try {
            Integer.parseInt(productMax.getText());
        }
        catch (NumberFormatException e){
            String max = "Max value is not an Integer" + "\n";
            errorMessages.add(max);
        }

        try {
            Integer.parseInt(productMin.getText());
        }
        catch (NumberFormatException e){
            String min = "Min value is not an integer" + "\n";
            errorMessages.add(min);
        }
        try {
            Double.parseDouble(productPrice.getText());
        }
        catch (NumberFormatException e){
            String price = "Price value is not a Double" + "\n";
            errorMessages.add(price);
        }
        try {
            Integer.parseInt(productInv.getText());
        }
        catch (NumberFormatException e){
            String inv = "Inventory value is not an integer" + "\n";
            errorMessages.add(inv);
        }
        if(productName.getText().isEmpty()){
            String nameError = "There is no value for Name Field" + "\n";
            errorMessages.add(nameError);
        }
        if(!productMin.getText().isEmpty() && !productMax.getText().isEmpty()){
            if(Integer.parseInt(productMin.getText()) >= Integer.parseInt(productMax.getText())){
                String nameError = "Min field must be less than Max field" + "\n";
                errorMessages.add(nameError);
            }
        }
        if(!productInv.getText().isEmpty()){
            int inv = Integer.parseInt(productInv.getText());
            int min = Integer.parseInt(productMin.getText());
            int max = Integer.parseInt(productMax.getText());
            if(inv > max || inv < min){
                String nameError = "Inventory must be between Min and Max values" + "\n";
                errorMessages.add(nameError);
            }
        }

        for(int i = 0; i < errorMessages.size(); i++){
            errorString += errorMessages.get(i).toString();
        }
        if(errorMessages.size() !=0){
            errorMessageLabel.setText(errorString);
            return true;
        }
        else {
            return false;
        }
    }
    /**
     * This function searches parts by id or name
     */
    public void searchPart(KeyEvent keyEvent) {
        String search = searchPartField.getText();
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
}


