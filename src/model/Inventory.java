package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.util.Optional;

public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * This function adds a part to the allParts list
     */
    public static void addPart(Part part){

        allParts.add(part);
    }
    /**
     * @return the allParts list
     */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }
    /**
     * This function adds a product to the allProducts List
     */
    public static void addProduct(Product product){
        allProducts.add(product);
    }
    /**
     * This function queries the allParts list for a part by id
     */
    public static Part lookupPart(int partId) {
       for(int i = 0; i < allParts.size(); i++){
           if(allParts.get(i).getId() == partId){
               return allParts.get(i);
           }
       }
       return null;
    }
    /**
     * This function deletes a part from the allParts list
     */
    public static boolean deletePart(Part selectedPart){
       return allParts.remove(selectedPart);
    }
    /**
     * This function updates a part
     */
    public static void updatePart(int index, Part selectedPart){
       Part partToUpdate = lookupPart(selectedPart.getId());
       partToUpdate.setId(index);
       partToUpdate.setName(selectedPart.getName());
       partToUpdate.setPrice(selectedPart.getPrice());
       partToUpdate.setStock(selectedPart.getStock());
       partToUpdate.setMin(selectedPart.getMin());
       partToUpdate.setMax(selectedPart.getMax());

       if(partToUpdate instanceof InHouse){
           try{
               ((InHouse) partToUpdate).setMachineId(((InHouse) selectedPart).getMachineId());
           }
           catch (ClassCastException e){
               Outsourced castOutsource = (Outsourced) selectedPart;
               castOutsource.getCompanyName();
               Inventory.addPart(castOutsource);
               Inventory.deletePart(partToUpdate);

           }
       }
       else if (partToUpdate instanceof Outsourced) {
           try {
               ((Outsourced) partToUpdate).setCompanyName(((Outsourced)selectedPart).getCompanyName());
           }
           catch (ClassCastException e){
               InHouse castInhouse = (InHouse) selectedPart;
               castInhouse.getMachineId();
               Inventory.addPart(castInhouse);
               Inventory.deletePart(partToUpdate);
           }
       }
    }
    /**
     * This function returns a product from the allProducts list by id.
     */
    public static Product lookupProduct(int productId){
        for(int i =0; i < allProducts.size(); i++){
            if(allProducts.get(i).getId() == productId){
                return allProducts.get(i);
            }
        }
        return null;
    }
    /**
     * This function updates a product
     */
    public static void updateProduct(int index, Product newProduct){
        Product prodToUpdate = lookupProduct(newProduct.getId());
        prodToUpdate.setId(index);
        prodToUpdate.setName(newProduct.getName());
        prodToUpdate.setPrice(newProduct.getPrice());
        prodToUpdate.setStock(newProduct.getStock());
        prodToUpdate.setMin(newProduct.getMin());
        prodToUpdate.setMax(newProduct.getMax());
    }
    /**
     * @return the allProducts list
     */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }
    /**
     * This function deletes a product from the allProducts list
     */
    public static boolean deleteProduct(Product selectedProduct){
        return allProducts.remove(selectedProduct);
    }
    /**
     * This function looks up a part from the allParts
     */
    public static ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> searchParts = FXCollections.observableArrayList();
        ObservableList<Part> allParts = Inventory.getAllParts();

        for(Part p : allParts){
            if(p.getName().toLowerCase().contains(partName.toLowerCase())){
                searchParts.add(p);
            }
        }
        return searchParts;
    }
    /**
     * This function looks up a part from the allProducts
     */
    public static ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> searchProducts = FXCollections.observableArrayList();
        ObservableList<Product> allProducts = Inventory.getAllProducts();

        for(Product p : allProducts){
            if(p.getName().toLowerCase().contains(productName.toLowerCase())){
                searchProducts.add(p);
            }
        }
        return searchProducts;
    }
}
