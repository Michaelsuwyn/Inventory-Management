package model;

public class Outsourced extends Part{

    private String companyName;
    /**
     * This function defines the constructor for Outsoruced.
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }
    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }
    /**
     * @return the company name
     */
    public String getCompanyName(){
        return companyName;
    }
}
