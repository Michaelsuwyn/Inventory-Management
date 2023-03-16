package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddPart implements Initializable {
    public RadioButton inHouse;
    public RadioButton outSourced;
    public TextField addPartId;
    public TextField addPartName;
    public TextField addPartInv;
    public TextField addPartPrice;
    public TextField addPartMax;
    public TextField addPartMin;
    public Button addPartSave;
    public TextField machineCompany;
    public Label machCompLabel;
    public Label errorMessageLabel;


    /**
     * This function initializes the AddPart controller
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    /**
     * This function navigates to main menu and away from add part page
     */
    public void cancelAddPart(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1400, 600);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * This function saves a newly created part
     */
    public void onAddPartSave(ActionEvent actionEvent) throws IOException {
        if(!doesErrorExist()){
            int newLen = ++MainMenu.partIdCounter;

            String partName = addPartName.getText().toString();
            double partPrice = Double.parseDouble(addPartPrice.getText());
            int partInv = Integer.parseInt(addPartInv.getText());
            int partMin = Integer.parseInt(addPartMin.getText());
            int partMax = Integer.parseInt(addPartMax.getText());

            if(inHouse.isSelected()){
                int machineId = Integer.parseInt(machineCompany.getText());
                InHouse newInHouse = new InHouse(newLen, partName , partPrice, partInv, partMin, partMax, machineId);
                Inventory.addPart(newInHouse);
            } else if (outSourced.isSelected()) {
                String compName = machineCompany.getText().toString();
                Outsourced outSourced = new Outsourced(newLen, partName, partPrice, partInv, partMin, partMax, compName);
                Inventory.addPart(outSourced);
            }

            Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1400, 600);
            stage.setTitle("Main Menu");
            stage.setScene(scene);
            stage.show();
        }

    }
    /**
     * This function sets Company Name when outsourced is checked
     */
    public void onOutsourceChecked(ActionEvent actionEvent) {

        machineCompany.setPromptText("Company Name");
        machCompLabel.setText("Company");
    }
    /**
     * This function sets Machine ID when in-house is checked
     */
    public void onInHouseChecked(ActionEvent actionEvent) {
        machineCompany.setPromptText("Machine ID");
        machCompLabel.setText("Machine ID");

    }
    /**
     * This function does the error handling for the add part page
     */
    public boolean doesErrorExist(){
        ArrayList<String> errorMessages = new ArrayList<>();
        String errorString = "Errors: " + "\n";

        try {
            Integer.parseInt(addPartMax.getText());
        }
        catch (NumberFormatException e){
            String max = "Max value is not an Integer" + "\n";
            errorMessages.add(max);
        }

        try {
            Integer.parseInt(addPartMin.getText());
        }
        catch (NumberFormatException e){
            String min = "Min value is not an integer" + "\n";
            errorMessages.add(min);
        }
        try {
            Double.parseDouble(addPartPrice.getText());
        }
        catch (NumberFormatException e){
            String price = "Price value is not a Double" + "\n";
            errorMessages.add(price);
        }
        try {
            Integer.parseInt(addPartInv.getText());
        }
        catch (NumberFormatException e){
            String inv = "Inventory value is not an integer" + "\n";
            errorMessages.add(inv);
        }
        if(addPartName.getText().isEmpty()){
            String nameError = "There is no value for Name Field" + "\n";
            errorMessages.add(nameError);
        }
        if(!addPartMin.getText().isEmpty() && !addPartMax.getText().isEmpty()){
            if(Integer.parseInt(addPartMin.getText()) >= Integer.parseInt(addPartMax.getText())){
                String nameError = "Min field must be less than Max field" + "\n";
                errorMessages.add(nameError);
            }
        }
        if(!addPartInv.getText().isEmpty()){
            int inv = Integer.parseInt(addPartInv.getText());
            int min = Integer.parseInt(addPartMin.getText());
            int max = Integer.parseInt(addPartMax.getText());
            if(inv > max || inv < min){
                String nameError = "Inventory must be between Min and Max values" + "\n";
                errorMessages.add(nameError);
            }
        }
        if(machCompLabel.getText().contains("ID")){
            try {
                Integer.parseInt(machineCompany.getText());
            }
            catch (NumberFormatException e){
                String mach = "Machine ID value is not an Integer" + "\n";
                errorMessages.add(mach);
            }
        }
        if(machCompLabel.getText().contains("Comp") && machineCompany.getText().isEmpty()){
            String nameError = "There is no value for Company" + "\n";
            errorMessages.add(nameError);
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
}



