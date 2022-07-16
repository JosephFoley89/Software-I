package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;
import model.ErrorMessage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** Controller for the ModifyPart window.
 *  @author Joseph Foley*/
public class ModifyProduct implements Initializable {
    Product product;
    ObservableList<Part> assocParts = FXCollections.observableArrayList();
    //Part Table components

    ErrorMessage error;
    @FXML
    private TableView<Part> partTable;
    @FXML
    private TableColumn<Part, Integer> partIDColumn;
    @FXML
    private TableColumn<Part, String> partNameColumn;
    @FXML
    private TableColumn<Part, Integer> partInventoryColumn;
    @FXML
    private TableColumn<Part, Double> partPriceColumn;

    //Associated Parts table components
    @FXML
    private TableView<Part> assocPartTable;
    @FXML
    private TableColumn<Part, Integer> assocPartIDColumn;
    @FXML
    private TableColumn<Part, String> assocPartNameColumn;
    @FXML
    private TableColumn<Part, Integer> assocPartInventoryColumn;
    @FXML
    private TableColumn<Part, Double> assocPartPriceColumn;

    @FXML
    private TextField partSearchTB;
    @FXML
    private TextField productIDTB;
    @FXML
    private TextField productNameTB;
    @FXML
    private TextField productInventoryTB;
    @FXML
    private TextField productPriceTB;
    @FXML
    private TextField productMaxTB;
    @FXML
    private TextField productMinTB;

    /**Function to handle the add button click event.
    Checks the parts table to confirm whether or not
    there is a selected part. IF there is it is added
    to the assocPart table. Otherwise, an error is
    provided to the user.*/
    @FXML
    void addButtonAction(ActionEvent event) {
        Part selectedPart = partTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            error.getMessage("missingPart", "Modify", "Product");
        } else {
            assocParts.add(selectedPart);
            assocPartTable.setItems(assocParts);
        }
    }

    /**Function to handle the cancel button click event.
    Provides the user with a confirmation message and
    returns the user to the main screen if the OK button
    is clicked.*/
    @FXML
    void cancelButtonAction(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert");
        alert.setContentText("Do you want cancel changes and return to the main screen?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) { returnToMainScreen(event); }
    }

    /**Function that handles the remove button click event.
    Checks if there is a part selected, provides an error
    to the user if not. Otherwise, a confirmation is given
    to the user to confirm whether or not they wish to
    remove the part.*/
    @FXML
    void removeButtonAction(ActionEvent event) {
        Part selectedPart = assocPartTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            error.getMessage("missingPart", "Modify", "Product");
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert");
            alert.setContentText("Do you want to remove the selected part?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                assocParts.remove(selectedPart);
                assocPartTable.setItems(assocParts);
            }
        }
    }

    /**Function to confirm the min and stock values are valid.*/
    private boolean stockValid(int min, int max, int stocked) {
        boolean isValid = true;

        if (min > max || min < 0) {
            error.getMessage("invalidMin", "Modify", "Product");
            isValid = false;
        } else if (stocked > max || stocked < min) {
            error.getMessage("invalidStock", "Modify", "Product");
            isValid = false;
        }

        return isValid;
    }

    /**Function to handle the save button click event.
    Tries to convert the text values to integer, double based on field.
    Provides the user with an error message if it's unsuccessful.
    Otherwise, it checks it the name is empty and provides the user
    with an error if it is. Otherwise, it confirms the stock, min values
    are both valid and provides the user with ab error message if not.
    Finally, adds the new product to the inventory and returns to the main window.*/
    @FXML
    void saveButtonAction(ActionEvent event) throws IOException {
        try {
            int id = product.getID();
            String name = productNameTB.getText();
            Double price = Double.parseDouble(productPriceTB.getText());
            int stock = Integer.parseInt(productInventoryTB.getText());
            int min = Integer.parseInt(productMinTB.getText());
            int max = Integer.parseInt(productMaxTB.getText());

            if (name.isEmpty()) {
                error.getMessage("emptyName", "Modify", "Product");
            } else {
                if (stockValid(min, max, stock)) {
                    Product newAddition = new Product(id, name, price, stock, min, max);

                    for (Part part : assocParts) {
                        newAddition.addPart(part);
                    }
                    Inventory.updateProduct(product.getID()-1, newAddition);
                    returnToMainScreen(event);
                }
            }
        } catch (Exception e){
            error.getMessage("missingValues", "Modify", "Product");
        }
    }

    /**Function to handle the search button click event.
    Sets the partTable to the results of the search.
    Provides the user with an error message if no
    results are found.
    RUNTIME ERROR: Null pointer error received when the search textfield was empty.
    I updated the code to check for that prior to searching. "if (partSearchTB != null)"*/
    @FXML
    void searchBtnAction(ActionEvent event) {
        ObservableList<Part> allParts = Inventory.getAllParts();
        ObservableList<Part> partsFound = FXCollections.observableArrayList();

        if (partSearchTB != null) {
            String searchString = partSearchTB.getText();

            for (Part part : allParts) {
                if (String.valueOf(part.getId()).contains(searchString) || part.getName().contains(searchString)) {
                    partsFound.add(part);
                }
            }

            partTable.setItems(partsFound);
        }

        if (partsFound.size() == 0) { error.getMessage("missingPart", "Modify", "Product"); }
    }

    /**Function to handle the key down event when the part search text field is in focus.
    FUTURE ENHANCEMENT: run the search when a key is pressed.*/
    @FXML
    void searchKeyDown(KeyEvent event) {
        if (partSearchTB.getText().isEmpty()) { partTable.setItems(Inventory.getAllParts()); }
    }

    /**function that returns the user to the main page*/
    private void returnToMainScreen(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("../view/main.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Main");
        stage.setScene(scene);
        stage.show();
    }

    /**Setting the default values once the window opens and initializing a new instance of the
    error message class.*/

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        error = new ErrorMessage();

        product = main.getProductToModify();
        assocParts = product.getAssociatedParts();

        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        partTable.setItems(Inventory.getAllParts());

        assocPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        assocPartTable.setItems(assocParts);

        productIDTB.setText(String.valueOf(product.getID()));
        productNameTB.setText(product.getName());
        productInventoryTB.setText(String.valueOf(product.getStock()));
        productPriceTB.setText(String.valueOf(product.getPrice()));
        productMaxTB.setText(String.valueOf(product.getMax()));
        productMinTB.setText(String.valueOf(product.getMin()));
    }
}
