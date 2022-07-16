package model;
/** Outsourced is a child class of the Part class and inherits most of its values from it.
 *  @author Joseph Foley*/
public class Outsourced extends Part {
    private String companyName;

    /**Constructor*/
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**Getter, Setter*/
    public String getCompanyName() { return this.companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
}
