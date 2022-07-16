package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**Product Class that houses product data
 *  @author Joseph Foley*/
public class Product {
    private int ID;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**Constructor*/
    public Product(int ID, String name, double price, int stock, int min, int max) {
        this.ID = ID;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /**Getters, Setters*/
    public int getID() { return this.ID; }
    public String getName() { return this.name; }
    public double getPrice() { return this.price; }
    public int getStock() { return this.stock; }
    public int getMin() { return this.min; }
    public int getMax() { return this.max; }
    public ObservableList<Part> getAssociatedParts() { return this.associatedParts; }

    public void setID(int ID) { this.ID = ID; }
    public void setName(String name) { this.name = name; }
    public void setPrice(double price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
    public void setMin(int min) { this.min = min; }
    public void setMax(int max) { this.max = max; }

    /**Associated Part methods*/
    public void addPart(Part part) { associatedParts.add(part); }
    public boolean removePart(Part part) {
        if (associatedParts.contains(part)) {
            associatedParts.remove(part);
            return true;
        }
        return false;
    }

}
