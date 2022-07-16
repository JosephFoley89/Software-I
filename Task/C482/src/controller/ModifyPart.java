package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;
import model.ErrorMessage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
/** Controller for the ModifyPart window.
 *  @author Joseph Foley*/
public class ModifyPart implements Initializable {
    private ErrorMessage error;
    private Part part;
    @FXML
    private TextField partIDTB;
    @FXML
    private Label partIDLabel;
    @FXML
    private RadioButton inHouseRadio;
    @FXML
    private RadioButton outsourcedRadio;
    @FXML
    private ToggleGroup tgPartType;
    @FXML
    private TextField partNameTB;
    @FXML
    private TextField partStockTB;
    @FXML
    private TextField partPriceTB;
    @FXML
    private TextField partMaxTB;
    @FXML
    private TextField partMinTB;
    @FXML
    private TextField companyMachineTB;

    /**Function to handle the cancel button click event.
    provides the user with a confirmation dialog
    and returns to the main screen if the OK button is
    clicked.*/
    @FXML
    void cancelButtonAction(ActionEvent e) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm");
        alert.setContentText("Do you wish to cancel your changes and return to the main screen?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) { returnToMain(e); }
    }

    /**Functions to handle the click events for both radio buttons.
    Setting the partIDLabel appropriately based on which radio button is selected.*/
    @FXML
    void inHouseRadioAction(ActionEvent e) {
        partIDLabel.setText("Machine ID");
    }

    @FXML
    void outsourcedRadioAction(ActionEvent e) {
        partIDLabel.setText("Company Name");
    }

    /**Function to handle the save button click event.
    Tries to convert the text values to int, double based on field
    displays an error message if it's unsuccessful. Otherwise, it
    checks to confirm the stock and min values are valid. If they
    are not, an error message is displayed. If they are, it checks
    to see which radio button is selected and attemptes to create
    a new instance of the appropriate class. If that fails, an
    error is displayed to the user. If it's successful, the
    modified part is added to the inventory if the name is changed.
    Otherwise it replaces its current part in the inventory list.
    The user is returned to the home page once it is successful.*/
    @FXML
    void saveButtonAction(ActionEvent e) throws IOException {
        try {
            int ID = part.getId();
            String name = partNameTB.getText();
            Double price = Double.parseDouble(partPriceTB.getText());
            int min = Integer.parseInt(partMinTB.getText());
            int max = Integer.parseInt(partMaxTB.getText());
            int stock = Integer.parseInt(partStockTB.getText());
            int machineID;
            String companyName;
            boolean success = false;

            if (stockValid(min, max, stock)) {
                if (inHouseRadio.isSelected()) {
                    try {
                        machineID = Integer.parseInt(companyMachineTB.getText());
                        InHouse newAddition = new InHouse(ID, name, price, stock, min, max, machineID);
                        Inventory.updatePart(part.getId()-1, newAddition);
                        success = true;
                    } catch (Exception ex) {
                        error.getMessage("invalidCharsMID", "Modify", "Part");
                    }
                } else if (outsourcedRadio.isSelected()) {
                    try {
                        companyName = companyMachineTB.getText();
                        Outsourced newAddition = new Outsourced(ID, name, price, stock, min, max, companyName);
                        Inventory.updatePart(part.getId()-1, newAddition);
                        success = true;
                    } catch (Exception ex) {
                        error.getMessage("emptyName", "Modify", "Part");
                    }
                }

                if (success) { returnToMain(e); }
            }
        } catch(Exception ex) {
            error.getMessage("missingValues", "Modify", "Part");
        }
    }

    /**Returns the user to the main window.*/
    private void returnToMain(ActionEvent e) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setTitle("Main");
        stage.setScene(scene);
        stage.show();
    }

    /**Function to check and confirm whether or not the min and stock values are valid.*/
    private boolean stockValid(int min, int max, int stocked) {
        boolean isValid = true;

        if (min > max || min < 0) {
            error.getMessage("invalidMin", "Modify", "Part");
            isValid = false;
        } else if (stocked > max || stocked < min) {
            error.getMessage("invalidStock", "Modify", "Part");
            isValid = false;
        }

        return isValid;
    }

    /**initializing the window and setting the values and initializing a new instance of the errormessage class.*/
    public void initialize(URL url, ResourceBundle resourceBundle) {
        error = new ErrorMessage();
        part = main.getPartToModify();

        if (part instanceof InHouse) {
            inHouseRadio.setSelected(true);
            partIDLabel.setText("Machine ID");
            companyMachineTB.setText(String.valueOf(((InHouse) part).getMachineID()));
        } else if (part instanceof Outsourced) {
            outsourcedRadio.setSelected(true);
            partIDLabel.setText("Company Name");
            companyMachineTB.setText(String.valueOf(((Outsourced) part).getCompanyName()));

        }

        partNameTB.setText(String.valueOf(part.getName()));
        partStockTB.setText(String.valueOf(part.getStock()));
        partMaxTB.setText(String.valueOf(part.getMax()));
        partMinTB.setText(String.valueOf(part.getMin()));
        partPriceTB.setText(String.valueOf(part.getPrice()));
        partIDTB.setText(String.valueOf(part.getId()));
    }
}