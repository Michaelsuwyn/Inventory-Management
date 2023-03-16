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

public class ModifyProduct implements Initializable {

    public TableView partsTable;
    public TableColumn partId;
    public TableColumn partName;
    public TableColumn partInv;
    public TableColumn partCost;
    public TableColumn assPartId;
    public TableColumn assPartName;
    public TableColumn assPartInv;
    public TableColumn assPartCost;
    public TextField prodId;
    public TextField prodName;
    public TextField prodInv;
    public TextField prodCost;
    public TextField prodMax;
    public TextField prodMin;
    public TableView assPartsTable;
    public Label errorMessageLabel;
    public TextField searchPartField;
    /**
     * This function initializes the ModifyProduct controller
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            Product product = MainMenu.selectedProduct;
            prodId.setText(String.valueOf(product.getId()));
            prodName.setText(product.getName());
            prodInv.setText(String.valueOf(product.getStock()));
            prodCost.setText(String.valueOf(product.getPrice()));
            prodMax.setText(String.valueOf(product.getMax()));
            prodMin.setText(String.valueOf(product.getMin()));

            partsTable.setItems(Inventory.getAllParts());
            partId.setCellValueFactory(new PropertyValueFactory<>("id"));
            partName.setCellValueFactory(new PropertyValueFactory<>("name"));
            partInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
            partCost.setCellValueFactory(new PropertyValueFactory<>("price"));

            assPartsTable.setItems(product.getAllAssociatedParts());
            assPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
            assPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
            assPartInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
            assPartCost.setCellValueFactory(new PropertyValueFactory<>("price"));

    }
    /**
     * This function navigates away from modify product page and back to main menu
     */
    public void cancelModifyProduct(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1400, 600);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * This function adds an associated part to a product
     */
    public void addAssPart(ActionEvent actionEvent) {
        MainMenu.selectedPart = (Part) partsTable.getSelectionModel().getSelectedItem();
        MainMenu.selectedProduct.addAssociatedPart(MainMenu.selectedPart);
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
                MainMenu.selectedProduct.deleteAssociatedPart(MainMenu.selectedPart);
            }
        } else {
            Alert noSelection = new Alert(Alert.AlertType.ERROR, "No Part has been selected");
            Optional<ButtonType> result = noSelection.showAndWait();
        }
    }
    /**
     * This function saves a new product
     */
    public void saveModifyProduct(ActionEvent actionEvent) throws IOException{
        if(!doesErrorExist()){
            String pName = prodName.getText().toString();
            int pInv = Integer.parseInt(prodInv.getText());
            int pMin = Integer.parseInt(prodMin.getText());
            int pMax = Integer.parseInt(prodMax.getText());
            int pId = Integer.parseInt(prodId.getText());
            double pCost = Double.parseDouble(prodCost.getText());
            Product product = new Product(pId,pName,pCost,pInv,pMin,pMax);
            Inventory.updateProduct(product.getId(), product);

            Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1400, 600);
            stage.setTitle("Main Menu");
            stage.setScene(scene);
            stage.show();
        }
    }
    /**
     * This function handles the error checking for product modification
     */
    public boolean doesErrorExist(){
        ArrayList<String> errorMessages = new ArrayList<>();
        String errorString = "Errors: " + "\n";

        try {
            Integer.parseInt(prodMax.getText());
        }
        catch (NumberFormatException e){
            String max = "Max value is not an Integer" + "\n";
            errorMessages.add(max);
        }

        try {
            Integer.parseInt(prodMin.getText());
        }
        catch (NumberFormatException e){
            String min = "Min value is not an integer" + "\n";
            errorMessages.add(min);
        }
        try {
            Double.parseDouble(prodCost.getText());
        }
        catch (NumberFormatException e){
            String price = "Price value is not a Double" + "\n";
            errorMessages.add(price);
        }
        try {
            Integer.parseInt(prodInv.getText());
        }
        catch (NumberFormatException e){
            String inv = "Inventory value is not an integer" + "\n";
            errorMessages.add(inv);
        }
        if(prodName.getText().isEmpty()){
            String nameError = "There is no value for Name Field" + "\n";
            errorMessages.add(nameError);
        }
        if(!prodMin.getText().isEmpty() && !prodMax.getText().isEmpty()){
            if(Integer.parseInt(prodMin.getText()) >= Integer.parseInt(prodMax.getText())){
                String nameError = "Min field must be less than Max field" + "\n";
                errorMessages.add(nameError);
            }
        }
        if(!prodMin.getText().isEmpty()){
            int inv = Integer.parseInt(prodInv.getText());
            int min = Integer.parseInt(prodMin.getText());
            int max = Integer.parseInt(prodMax.getText());
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
     * This function searches for a part by id or name
     */
    public void searchPart(KeyEvent keyEvent) {
        String search = searchPartField.getText();
        ObservableList<Part> searchedParts = Inventory.lookupPart(search);

        if(searchedParts.size() == 0){
            try {
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
