package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/** Inventory is a class that houses the various part and product data.
 * There are several methods available for adding, removing parts/products
 *  @author Joseph Foley*/
public class Inventory {
    private static int partID = 0;
    private static int productID = 0;
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**Getters*/
    public static ObservableList<Part> getAllParts() { return allParts; }
    public static ObservableList<Product> getAllProducts() { return allProducts; }
    public static int getNewPartID() { return ++partID; }
    public static int getNewProductID() { return ++productID; }

    /**Add Part/Product*/
    public static void addPart(Part part) { allParts.add(part); }
    public static void addProduct(Product product) { allProducts.add(product); }

    /**Remove Part/Product*/
    public static boolean removePart(Part part) {
        if (allParts.contains(part)) {
            allParts.remove(part);
            return true;
        }
        return false;
    }

    public static boolean removeProduct(Product product) {
        if (allProducts.contains(product)) {
            allProducts.remove(product);
            return true;
        }
        return false;
    }

    /**Update Part/Product by setting the value at the provided index to the provided part*/
    public static void updatePart(int index, Part part) { allParts.set(index, part); }
    public static void updateProduct(int index, Product product) { allProducts.set(index, product); }

    //Search functionality
    /**
    *
    * The search functionality works identically between the two classes, so I'll describe them once.
    * The lookupByID method returns a single result if one is found by matching the ID in the args,
    * otherwise it returns null. The lookupByName method returns an observable list of results which
    * may or may not be populated based on the provided args.
    *
    * */
    public static Part lookupPartByID(int partID) {
        Part result = null;

        for (Part part : allParts) {
            if (part.getId() == partID) {
                result = part;
                break;
            }
        }
        return result;
    }

    public static ObservableList<Part> lookupPartByName(String name) {
        ObservableList<Part> result = FXCollections.observableArrayList();

        for (Part part : allParts) {
            if (part.getName().equals(name)) {
                result.add(part);
            }
        }

        return result;
    }

    public static Product lookupProductByID(int productID) {
        Product result = null;

        for (Product product : allProducts) {
            if (product.getID() == productID) {
                result = product;
                break;
            }
        }

        return result;
    }

    public static ObservableList<Product> lookupProductByName(String name) {
        ObservableList<Product> result = FXCollections.observableArrayList();

        for (Product product : allProducts) {
            if (product.getName().equals(name)) {
                result.add(product);
            }
        }

        return result;
    }

}
