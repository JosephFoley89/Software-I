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
import model.ErrorMessage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
/** Controller for the AddPart window.
 *  @author Joseph Foley*/
public class AddPart implements Initializable {
    private ErrorMessage error;
    @FXML
    private Label partIDLabel;
    @FXML
    private RadioButton inHouseRadio;
    @FXML
    private RadioButton outsourcedRadio;
    @FXML
    private TextField nameTB;
    @FXML
    private TextField inventoryTB;
    @FXML
    private TextField priceTB;
    @FXML
    private TextField minTB;
    @FXML
    private TextField maxTB;
    @FXML
    private TextField companyMachineTB;

    //Displays confirmation dialogue which will return to the main screen should the user click OK.
    @FXML
    void cancelButtonAction(ActionEvent e) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("ALERT!");
        alert.setContentText("Do you wish to cancel and return to the main screen?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) { returnToMain(e); }
    }

    //Changing the label text based on the presently selected radio button option
    @FXML
    void inHouseRadioButtonAction(ActionEvent e) { partIDLabel.setText("Machine ID"); }
    @FXML
    void outsourcedRadioButtonAction(ActionEvent e) { partIDLabel.setText("Company Name"); }

    //Logic to check whether or not the stock AND min are valid. Displays the appropriate
    //error message if not.
    private boolean stockValid(int min, int max, int stocked) {
        boolean isValid = true;

        if (min > max || min < 0) {
            error.getMessage("invalidMin", "Add", "Part");
            isValid = false;
        } else if (stocked > max || stocked < min) {
            error.getMessage("invalidStock", "Add", "Part");
            isValid = false;
        }

        return isValid;
    }

    //Function to return to the main screen
    private void returnToMain(ActionEvent e) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setTitle("Main");
        stage.setScene(scene);
        stage.show();
    }

    //Function to attempt to save and throw errors should an exception or a missing value be present
    //Returns to the main page should everything go well.
    //
    @FXML
    void saveButtonAction(ActionEvent e) throws IOException {
        try {
            int id = 0;
            String name = nameTB.getText();
            Double price = Double.parseDouble(priceTB.getText());
            int stock = Integer.parseInt(inventoryTB.getText());
            int min = Integer.parseInt(minTB.getText());
            int max = Integer.parseInt(maxTB.getText());
            int machineID;
            String companyName;
            boolean success = false;

            if (name.isEmpty()) {
                error.getMessage("emptyName", "Add", "Part");
            } else {
                if (stockValid(min, max, stock)) {
                    if (inHouseRadio.isSelected()) {
                        try {
                            machineID = Integer.parseInt(companyMachineTB.getText());
                            InHouse newAddition = new InHouse(id, name, price, stock, min, max, machineID);
                            newAddition.setId(Inventory.getNewPartID());
                            Inventory.addPart(newAddition);
                            success = true;
                        } catch (Exception ex) {
                            error.getMessage("invalidCharsMID", "Add", "Part");
                        }
                    } else if (outsourcedRadio.isSelected()) {
                        companyName = companyMachineTB.getText();
                        Outsourced newAddition = new Outsourced(id, name, price, stock, min, max, companyName);
                        newAddition.setId(Inventory.getNewPartID());
                        Inventory.addPart(newAddition);
                        success = true;
                    }
                    if (success) { returnToMain(e); }
                }
            }
        } catch(Exception ex) {
            error.getMessage("missingValues", "Add", "Part");
        }
    }

    //Setting the initial value of the radio button when the window is loaded and initializing the ErrorMessage
    //class to display error messages.
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        error = new ErrorMessage();
        inHouseRadio.setSelected(true);
    }
}
