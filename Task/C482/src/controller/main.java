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
/** Controller for the main window.
 *  @author Joseph Foley*/
public class main implements Initializable {
    private ErrorMessage error;
    private static Part partToModify;
    private static Product productToModify;

    //Part search, table
    @FXML
    private TextField partSearchTB;

    //Part Table
    @FXML
    private TableView<Part> partTable;
    @FXML
    private TableColumn<Part, Integer> partIDColumn;
    @FXML
    private TableColumn<Part, String> partNameColumn;
    @FXML
    private TableColumn<Part, Integer> partStockColumn;
    @FXML
    private TableColumn<Part, Double> partPriceColumn;

    //START PART EVENTS
    /**
    Event handler for the add part button click event.
    Routes the user to the add part window.*/
    @FXML
    void addPartAction(ActionEvent e) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }
    /**
    Event handler for the remove part button click event
    Provides an error message should the part not be selected,
    otherwise requests the user to confirm they wish to delete
    the part and removes if the confirmation is received.*/
    @FXML
    void removePartAction(ActionEvent e) {
        Part part = partTable.getSelectionModel().getSelectedItem();
        if (part == null) {
            error.getMessage("noPartSelected", "Remove", "Part");
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Please Confirm");
            alert.setContentText("Do you wish to delete the selected part?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) { Inventory.removePart(part); }
        }
    }
    /**
    Event handler for the modify part button click event.
    Gets the selected item or provides the user with an error
    if there is no selected item. Launches the modify
    part window if the user has a selected item.*/
    @FXML
    void modifyPartAction(ActionEvent e) throws IOException {
        partToModify = partTable.getSelectionModel().getSelectedItem();

        if (partToModify == null) {
            error.getMessage("noPartSelected", "Modify", "Part");
        } else {
            Parent parent = FXMLLoader.load(getClass().getResource("/view/ModifyPart.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setTitle("Modify Part");
            stage.setScene(scene);
            stage.show();
        }
    }
    /**
    Event handler for the part search button click event
    Loops through all parts to find matching results
    from the search string and filters them into the part
    table. If no results are found the user receives an
    error message.
    RUNTIME ERROR: I received a null pointer exception because
    of mistakenly making the pointer static. I removed the static
     designation to resolve the error.*/
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
        if (result.size() == 0) { error.getMessage("missingPart", "Find", "Part"); }
    }
    /**
    Event handler for the key down event when the part search textfield is in focus.
    This is here to provide all parts if the field is empty.
    FUTURE ENHANCEMENT: running the search on key down as well so that the search button is
    not necessary.*/
    @FXML
    void partSearchKeyDown(KeyEvent e) {
        if (partSearchTB == null || partSearchTB.getText().isEmpty()) { partTable.setItems(Inventory.getAllParts()); }
    }

    //END PART EVENTS

    //Product search, table
    @FXML
    private TextField productSearchTB;

    //Product Table
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, Integer> productIDColumn;
    @FXML
    private TableColumn<Product, String> productNameColumn;
    @FXML
    private TableColumn<Product, Integer> productStockColumn;
    @FXML
    private TableColumn<Product, Double> productPriceColumn;

    public static Product getProductToModify() { return productToModify; }
    public static Part getPartToModify() { return partToModify; }

    //START PRODUCT EVENTS
    /**
    Function to handle the add product button click event.
    Routes the user to the add product window.
    */@FXML
    void addProductAction(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Add Product");
        stage.setScene(scene);
        stage.show();
    }
    /**
    Function to handle the remove product button click event.
    Removes the product if one is selected and the user confirms
    their intent to delete the product. The user is alerted if there
    is no product selected. Also, if the product has parts associated
    with it, the user will be requested to remove all parts first
    FUTURE ENHANCEMENT: remove all parts when the user confirms they
    wish to delete the product. Perhaps by adding a confirmation dialog
    of all the parts that will be removed from the product and requesting
    confirmation once more.*/
    @FXML
    void removeProductAction(ActionEvent event) {
        Product product = productTable.getSelectionModel().getSelectedItem();

        if (product == null) {
            error.getMessage("noProductSelected", "Remove", "Product");
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Alert");
            alert.setContentText("Do you want to delete the selected product?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                ObservableList<Part> assocParts = product.getAssociatedParts();

                if (assocParts.size() >= 1) {
                    error.getMessage("removePartsFirst", "Remove", "Product");
                } else {
                    Inventory.removeProduct(product);
                }
            }
        }
    }
    /**
    Function to handle the modify product button click event.
    Provides error messaging if no product is selected.
    Otherwise, routes the user to the modify product window.*/
    @FXML
    void modifyProductAction(ActionEvent event) throws IOException {
        productToModify = productTable.getSelectionModel().getSelectedItem();

        if (productToModify == null) {
            error.getMessage("noProductSelected", "Modify", "Product");
        } else {
            Parent parent = FXMLLoader.load(getClass().getResource("/view/ModifyProduct.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Modify Product");
            stage.setScene(scene);
            stage.show();
        }
    }
    /**
    Function to handle the product search button click event.
    Returns a list of all mathcing products and filters them
    into the product table.
     FUTURE ENHANCEMENT: make the search case-insensitive.
     */
    @FXML
    void productSearchAction(ActionEvent event) {
        ObservableList<Product> allProducts = Inventory.getAllProducts();
        ObservableList<Product> result = FXCollections.observableArrayList();
        String searchString = productSearchTB.getText();

        for (Product product : allProducts) {
            if (String.valueOf(product.getID()).contains(searchString) || product.getName().contains(searchString)) {
                result.add(product);
            }
        }

        productTable.setItems(result);

        if (result.size() == 0) { error.getMessage("missingProduct", "Find", "Product"); }
    }
    /**
    Event handler for the key down event when the product search textfield is in focus.
    This is here to provide all products if the field is empty.
    FUTURE ENHANCEMENT: running the search on key down as well so that the search button is
    not necessary.*/
    @FXML
    void productSearchKeyDown(KeyEvent event) {
        if (productSearchTB == null || productSearchTB.getText().isEmpty()) { productTable.setItems(Inventory.getAllProducts()); }
    }

    //END PRODUCT EVENTS

    /**Function for handling the exit button click event. Exits the app.*/
    @FXML
    void exitButtonAction(ActionEvent e) { System.exit(0); }

    /**
    Setting the initial conditions of the main window and initializing a new error message class instance.
    RUNTIME ERROR: I received a null pointer exception when attempting to add all parts to this table
    because of a typo. I corrected the typo to resolve the issue.*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        error = new ErrorMessage();

        partTable.setItems(Inventory.getAllParts());
        partIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTable.setItems(Inventory.getAllProducts());
        productIDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}
