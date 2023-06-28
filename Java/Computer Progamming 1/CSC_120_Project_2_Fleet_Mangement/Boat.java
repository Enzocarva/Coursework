//======================================================================================================================
import java.io.Serializable;
import java.util.ArrayList;
//----------------------------------------------------------------------------------------------------------------------
public class Boat implements Serializable{
//----------------------------------------------------------------------------------------------------------------------
    private BoatType boatType;
    private String name;
    private int year;
    private String make;
    private double feet;
    private double purchasePrice;
    private double expense;
    private static ArrayList<Boat> boatList = new ArrayList<>();
//----------------------------------------------------------------------------------------------------------------------
    public Boat(BoatType boatType, String name, int year, String make, double feet, double purchasePrice, double expense) {

        super();
        this.boatType = boatType;
        this.name = name;
        this.year = year;
        this.make = make;
        this.feet = feet;
        this.purchasePrice = purchasePrice;
        this.expense = expense;
    }
//----------------------------------------------------------------------------------------------------------------------
    public String toString() {
        return (boatType + " \t\t" + name + "    \t" + year + " \t\t" + make + "   \t" + feet + "' : Paid $ " + purchasePrice + " : " + "Spent $ " + expense);
    }
//----------------------------------------------------------------------------------------------------------------------

  // ----- make all "setters and getters" methods ----
    public BoatType getBoatType() {
        return boatType;
    }
//----------------------------------------------------------------------------------------------------------------------
    public void setBoatType(BoatType boatType) {
        this.boatType = boatType;
    }
//----------------------------------------------------------------------------------------------------------------------
    public String getName() {
        return name;
    }
//----------------------------------------------------------------------------------------------------------------------
    public void setName(String name) {
        this.name = name;
    }
//----------------------------------------------------------------------------------------------------------------------
    public String getMake() {
        return make;
    }
//----------------------------------------------------------------------------------------------------------------------
    public void setMake(String make) {
        this.make = make;
    }
//----------------------------------------------------------------------------------------------------------------------
    public int getYear() {
        return year;
    }
//----------------------------------------------------------------------------------------------------------------------
    public void setYear(int year) {
        this.year = year;
    }
//----------------------------------------------------------------------------------------------------------------------
    public double getFeet() {
        return feet;
    }
//----------------------------------------------------------------------------------------------------------------------
    public void setFeet(double feet) {
        this.feet = feet;
    }
//----------------------------------------------------------------------------------------------------------------------
    public double getPurchasePrice() {
        return purchasePrice;
    }
//----------------------------------------------------------------------------------------------------------------------
    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }
//----------------------------------------------------------------------------------------------------------------------
    public double getExpense() {
        return expense;
    }
//----------------------------------------------------------------------------------------------------------------------
    public void setExpense(double expense) {
        this.expense = expense;
    }
//----------------------------------------------------------------------------------------------------------------------
    public static ArrayList<Boat> getBoatList() {
        return boatList;
    }
//----------------------------------------------------------------------------------------------------------------------
    public static void setBoatList(ArrayList<Boat> list) {
        boatList = list;
    }
//----------------------------------------------------------------------------------------------------------------------
    public static double getPaidTotal() {

        double paidTotal = 0;

        for (Boat boat:boatList) {
            paidTotal=paidTotal+boat.getPurchasePrice();
        }
        return paidTotal;
    }
//----------------------------------------------------------------------------------------------------------------------
    public static double getSpentTotal() {

        double spentTotal = 0;
        for (Boat boat:boatList) {
            spentTotal = spentTotal + boat.getExpense();
        }
        return spentTotal;
    }
//----------------------------------------------------------------------------------------------------------------------
   // method to check if expenses are permitted
    public boolean checkExpense(double amt) {

        if (this.getPurchasePrice()<amt) {
            System.out.println("Expense not permitted, only " + (this.getPurchasePrice()-this.getExpense()) + " left to spend.");
            return false;
        }
        return true;
    }
//----------------------------------------------------------------------------------------------------------------------
}
//======================================================================================================================