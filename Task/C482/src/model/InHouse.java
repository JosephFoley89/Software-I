package model;
/** Inhouse is a child class of the Part class and inherits most of its values from it.
 *  @author Joseph Foley*/
public class InHouse extends Part {
    private int machineID;

    /**Constructor*/
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineID) {
        super(id, name, price, stock, min, max);
        this.machineID = machineID;
    }

    /**Getter, Setter*/
    public int getMachineID() { return this.machineID; }
    public void setMachineID(int machineID) { this.machineID = machineID; }
}
