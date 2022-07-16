package model;

import javafx.scene.control.Alert;

import java.util.Locale;
/** ErrorMessage is a class that handles all error messaging and its presentation to the user
 *  @author Joseph Foley*/
public class ErrorMessage {
    /**
    Class created specifically to handle error messaging. This reduces the amount of code necessary.
    The sole method in this class is the getMessage which could likely be named generate message as
    well. It takes the error string, matches it to a case in the switch statement. Sets the header
    text to the action attempting to be performed and the type of class being manipulated/added/removed,
    i.e. "Unable to modify product".
     */
    public void getMessage(String error, String action, String type) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Unable to " + action.toLowerCase(Locale.ROOT) + " " + type.toLowerCase(Locale.ROOT));
        switch (error) {
            case "missingValues":
                alert.setContentText("The form contains blank fields or invalid values.");
                break;
            case "invalidCharsMID":
                alert.setContentText("The 'Machine ID' field must contain only numbers.");
                break;
            case "invalidCharsMin":
                alert.setContentText("The 'Min' field must contain only numbers.");
                break;
            case "invalidCharsStock":
                alert.setContentText("The 'Inventory' field must contain only numbers.");
                break;
            case "emptyName":
                alert.setContentText("The 'Name' field must not be empty.");
                break;
            case "invalidMin":
                alert.setContentText("The 'Min' value must be greater than 0 and less than the 'Max' value.");
                break;
            case "invalidStock":
                alert.setContentText("The 'Inventory' value must be greater than the 'Min' value and less than the 'Max' value.");
                break;
            case "missingPart":
                alert.setContentText("The part cannot be found.");
                break;
            case "missingProduct":
                alert.setContentText("The product cannot be found.");
                break;
            case "noPartSelected":
                alert.setContentText("No parts were selected.");
                break;
            case "noProductSelected":
                alert.setContentText("No products were selected.");
                break;
            case "removePartsFirst":
                alert.setContentText("Please remove all parts prior to removing the product.");
                break;
        }
        alert.showAndWait();
    }
}
