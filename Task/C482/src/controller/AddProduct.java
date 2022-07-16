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

/** Controller for the AddProduct window.
 *  @author Joseph Foley*/
public class AddProduct implements Initializable {
    private ErrorMessage error;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    //Part Table components
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
    /**
    Function to handle the click event on the add button object.
    checks if there is a part highlighted/selected and if so, adds
    the part to the associatedParts list and finally sets the
    table rows to the assoicatedParts  table.*/
    @FXML
    void addButtonAction(ActionEvent e) {
        Part part = partTable.getSelectionModel().getSelectedItem();

        if (part == null) {
            error.getMessage("missingPart", "Add", "Product");
        } else {
            associatedParts.add(part);
            assocPartTable.setItems(associatedParts);
        }
    }

    /**
    Function to handle the cancel button click event
    provides the user with a dialog window to confirm whether
    or not they wish to return to the main screen.*/
    @FXML
    void cancelButtonAction(ActionEvent e) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Please confirm");
        alert.setContentText("Do you wish to cancel your changes and return to the main screen?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) { returnToMain(e); }
    }
    /**
    Function to handle the search button click event
    which searches the list for all matching results
    and adds filters them into the partTable.*/
    @FXML
    void partSearchAction(ActionEvent e) {
        ObservableList<Part> allParts = Inventory.getAllParts();
        ObservableList<Part> result = FXCollections.observableArrayList();
        String search = partSearchTB.getText();

        for (Part part : allParts) {
            if (String.valueOf(part.getId()).contains(search) || part.getName().contains(search)) {
                result.add(part);
            }
        }

        partTable.setItems(result);

        if (result.size() == 0) { error.getMessage("missingPart", "Add", "Product");}
    }
    /**sets the part table to all when the part textfield is empty. Event fires when a key is pressed.*/
    @FXML
    void partSearchKeyDown(KeyEvent e) {
        if (partSearchTB.getText().isEmpty()) { partTable.setItems(Inventory.getAllParts()); }
    }

    /**Event handler for the remove button click event. Provides a dialog window
    and advises the user that there is no selected part if no part is selected
    otherwise, it provides a confirmation request window to the user and removes
    the part if the user confirms their request.*/
    @FXML
    void removeButtonAction(ActionEvent e) {
        Part part = assocPartTable.getSelectionModel().getSelectedItem();
        if (part == null) {
            error.getMessage("missingPart", "Add", "Product");
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Please Confirm");
            alert.setContentText("Do you want to remove the selected part?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                associatedParts.remove(part);
                assocPartTable.setItems(associatedParts);
            }
        }
    }

    /**event handler for the save button click event.
    tries to convert the values from a string to an int/double based on the field.
    if that fails or there is an empty value, the user receives an error message.
    then it checks to see if there is a value in the name string. If not, an error
    message is presented. Otherwise, it checks to confirm the stock and min values
    are valid. If that's indeed the case, the newAddition product is created and the
    parts are looped through and added to it. Finally, the new class instance is added
    to the inventory and the user is returned to the main screen.*/
    @FXML
    void saveButtonAction(ActionEvent e) throws IOException {
        try {
            int id = 0;
            String name = productNameTB.getText();
            Double price = Double.parseDouble(productPriceTB.getText());
            int stock = Integer.parseInt(productInventoryTB.getText());
            int min = Integer.parseInt(productMinTB.getText());
            int max = Integer.parseInt(productMaxTB.getText());

            if (name.isEmpty()) {
                error.getMessage("emptyName", "Add", "Product");
            } else {
                if(stockValid(min, max, stock)) {
                    Product newAddition = new Product(id, name, price, stock, min, max);

                    for(Part part : associatedParts) {
                        newAddition.addPart(part);
                    }

                    newAddition.setID(Inventory.getNewProductID());
                    Inventory.addProduct(newAddition);
                    returnToMain(e);
                }
            }
        } catch (Exception ex) {
            error.getMessage("emptyValues", "Add", "Product");
        }
    }

    /**Function to return the user to the main screen.*/
    private void returnToMain(ActionEvent e) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setTitle("Main");
        stage.setScene(scene);
        stage.show();
    }

    /**function used to determine if the stock and min values are valid.*/
    private boolean stockValid(int min, int max, int stocked) {
        boolean isValid = true;

        if (min > max || min < 0) {
            error.getMessage("invalidMin", "Add", "Product");
            isValid = false;
        } else if (stocked > max || stocked < min) {
            error.getMessage("invalidStock", "Add", "Product");
            isValid = false;
        }

        return isValid;
    }

    /**Setting the initial conditions of the window and instantiating a new ErrorMessage class instance*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        error = new ErrorMessage();
        //All existing parts
        partTable.setItems(Inventory.getAllParts());
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        //Associated Parts
        assocPartIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}
