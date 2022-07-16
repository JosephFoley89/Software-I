package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Product;
/**
 Javadoc located in JAVADOC folder
 @author Joseph Foley
 */

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/main.fxml"));
        stage.setTitle("Main");
        stage.setScene(new Scene(root, 900, 400));
        stage.show();
    }

    public static void main(String[] args) {
        //Adding dummy parts and products
        InHouse engine = new InHouse(Inventory.getNewPartID(), "Engine", 300.00, 10, 5, 15, 69);
        InHouse wheel = new InHouse(Inventory.getNewPartID(), "Wheel", 100.00, 100, 20, 160, 42);
        InHouse door = new InHouse(Inventory.getNewPartID(), "Door", 25.00, 4, 0, 10, 007);
        InHouse grill = new InHouse(Inventory.getNewPartID(), "Grill", 100.00, 3, 1, 5, 92889);
        InHouse seat = new InHouse(Inventory.getNewPartID(), "Seat", 275.00, 10, 5, 50, 613);

        Outsourced cd = new Outsourced(Inventory.getNewPartID(), "CD Player", 80.00, 5, 3, 9, "BOOM! ThunderBumper");
        Outsourced eightTrack = new Outsourced(Inventory.getNewPartID(), "Eight Track Player", 10.00, 500, 30, 1000, "RetroSound.mp4");
        Outsourced cassette = new Outsourced(Inventory.getNewPartID(), "Cassette Player", 3.00, 25, 10, 40, "High Fidelity, Side B");
        Outsourced tweeter = new Outsourced(Inventory.getNewPartID(), "Tweeter Speaker", 10.00, 45, 30, 50, "All Your Bass Are Belong To Us");
        Outsourced subwoofer = new Outsourced(Inventory.getNewPartID(), "Subwoofer Speaker", 100.00, 35, 30, 50, "Don't Drop Me Now");
        Outsourced amp = new Outsourced(Inventory.getNewPartID(), "Amplifier", 125.00, 21, 10, 100, "Turn Down 4 Watt");

        Product shiftyCar = new Product(Inventory.getNewProductID(), "Shifty Car", 10000.00, 20, 5, 50);
        Product decentCar = new Product(Inventory.getNewProductID(), "Decent Car", 15000.00, 30, 10, 100);
        Product awesomeCar = new Product(Inventory.getNewProductID(), "Awesome Car", 20000.00, 40, 15, 200);
        Product fastCar = new Product(Inventory.getNewProductID(), "Fast Car", 40000.00, 10, 1, 20);

        //Setting up the shifty car
        shiftyCar.addPart(engine);
        shiftyCar.addPart(wheel);
        shiftyCar.addPart(eightTrack);
        shiftyCar.addPart(tweeter);

        //Setting up the decent car
        decentCar.addPart(engine);
        decentCar.addPart(wheel);
        decentCar.addPart(door);
        decentCar.addPart(seat);
        decentCar.addPart(cassette);
        decentCar.addPart(tweeter);

        //Setting up the awesome car
        awesomeCar.addPart(engine);
        awesomeCar.addPart(wheel);
        awesomeCar.addPart(door);
        awesomeCar.addPart(grill);
        awesomeCar.addPart(seat);
        awesomeCar.addPart(cd);
        awesomeCar.addPart(tweeter);
        awesomeCar.addPart(subwoofer);
        awesomeCar.addPart(amp);

        //Setting up the fast car
        fastCar.addPart(engine);
        fastCar.addPart(engine);
        fastCar.addPart(engine);
        fastCar.addPart(engine);
        fastCar.addPart(engine);
        fastCar.addPart(engine);
        fastCar.addPart(engine);
        fastCar.addPart(seat);

        //Adding parts to the inventory
        Inventory.addPart(engine);
        Inventory.addPart(wheel);
        Inventory.addPart(door);
        Inventory.addPart(grill);
        Inventory.addPart(seat);
        Inventory.addPart(cd);
        Inventory.addPart(eightTrack);
        Inventory.addPart(cassette);
        Inventory.addPart(tweeter);
        Inventory.addPart(subwoofer);
        Inventory.addPart(amp);

        //Adding products to the inventory
        Inventory.addProduct(shiftyCar);
        Inventory.addProduct(decentCar);
        Inventory.addProduct(awesomeCar);
        Inventory.addProduct(fastCar);

        launch(args);
    }
}