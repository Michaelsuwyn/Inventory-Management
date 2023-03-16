package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ModifyPart implements Initializable {
    public RadioButton inHouse;
    public ToggleGroup InOut;
    public RadioButton outSourced;
    public TextField modifyPartId;
    public TextField modifyPartName;
    public TextField modifyPartInv;
    public TextField modifyPartPrice;
    public TextField modifyPartMax;
    public TextField modifyPartMin;
    public TextField machineCompany;
    public Button modifyPartSave;
    public Label machCompLabel;
    public Label errorMessageLabel;
    /**
     * This function initializes the ModifyPart controller
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Part part = MainMenu.selectedPart;
        modifyPartId.setText(String.valueOf(part.getId()));
        modifyPartName.setText(part.getName());
        modifyPartInv.setText(String.valueOf(part.getStock()));
        modifyPartPrice.setText(String.valueOf(part.getPrice()));
        modifyPartMin.setText(String.valueOf(part.getMin()));
        modifyPartMax.setText(String.valueOf(part.getMax()));
        if(part instanceof InHouse){
            machineCompany.setText(String.valueOf(((InHouse) part).getMachineId()));
        }
        else if (part instanceof Outsourced) {
            outSourced.setSelected(true);
            machCompLabel.setText("Company");
            machineCompany.setText(((Outsourced) part).getCompanyName());
        }
    }
    /**
     * This function sets the Company Name field when outsourced is checked
     */
    public void onOutsourceChecked(ActionEvent actionEvent) {

        machineCompany.setPromptText("Company Name");
        machCompLabel.setText("Company");
    }
    /**
     * This function sets the Machine ID field when in-house is checked
     */
    public void onInHouseChecked(ActionEvent actionEvent) {
        machineCompany.setPromptText("Machine ID");
        machCompLabel.setText("Machine ID");
    }
    /**
     * This function navigates away from modify part page to the main menu
     */
    public void cancelModifyPart(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1400, 600);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * This function saves a new part
     */
    public void onModifyPartSave(ActionEvent actionEvent) throws IOException {
        if(!doesErrorExist()){
            String partName = modifyPartName.getText().toString();
            double partPrice = Double.parseDouble(modifyPartPrice.getText());
            int partInv = Integer.parseInt(modifyPartInv.getText());
            int partMin = Integer.parseInt(modifyPartMin.getText());
            int partMax = Integer.parseInt(modifyPartMax.getText());
            int partId = Integer.parseInt(modifyPartId.getText());

            if(inHouse.isSelected()){

                int machineId = Integer.parseInt(machineCompany.getText());
                InHouse newInHouse = new InHouse(partId, partName , partPrice, partInv, partMin, partMax, machineId);
                Inventory.updatePart(newInHouse.getId(), newInHouse);


            } else if (outSourced.isSelected()) {
                String compName = machineCompany.getText().toString();
                Outsourced outSourced = new Outsourced(partId, partName, partPrice, partInv, partMin, partMax, compName);
                Inventory.updatePart(outSourced.getId(), outSourced);
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
     * This function does the error checking for modify part page
     */
    public boolean doesErrorExist(){
        ArrayList<String> errorMessages = new ArrayList<>();
        String errorString = "Errors: " + "\n";

        try {
            Integer.parseInt(modifyPartMax.getText());
        }
        catch (NumberFormatException e){
            String max = "Max value is not an Integer" + "\n";
            errorMessages.add(max);
        }

        try {
            Integer.parseInt(modifyPartMin.getText());
        }
        catch (NumberFormatException e){
            String min = "Min value is not an integer" + "\n";
            errorMessages.add(min);
        }
        try {
            Double.parseDouble(modifyPartPrice.getText());
        }
        catch (NumberFormatException e){
            String price = "Price value is not a Double" + "\n";
            errorMessages.add(price);
        }
        try {
            Integer.parseInt(modifyPartInv.getText());
        }
        catch (NumberFormatException e){
            String inv = "Inventory value is not an integer" + "\n";
            errorMessages.add(inv);
        }
        if(modifyPartName.getText().isEmpty()){
            String nameError = "There is no value for Name Field" + "\n";
            errorMessages.add(nameError);
        }
        if(!modifyPartMin.getText().isEmpty() && !modifyPartMax.getText().isEmpty()){
            if(Integer.parseInt(modifyPartMin.getText()) >= Integer.parseInt(modifyPartMax.getText())){
                String nameError = "Min field must be less than Max field" + "\n";
                errorMessages.add(nameError);
            }
        }
        if(!modifyPartInv.getText().isEmpty()){
            int inv = Integer.parseInt(modifyPartInv.getText());
            int min = Integer.parseInt(modifyPartMin.getText());
            int max = Integer.parseInt(modifyPartMax.getText());
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

