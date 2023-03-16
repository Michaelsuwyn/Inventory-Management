package model;

public class InHouse extends Part {

    private int machineId;
    /**
     * This function defines the constructor for InHouse.
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }
    /**
     * @param machineId the machineId to set
     */
    public void setMachineId(int machineId){
        this.machineId = machineId;
    }
    /**
     * @return the machine ID
     */
    public int getMachineId(){
        return machineId;
    }
}
