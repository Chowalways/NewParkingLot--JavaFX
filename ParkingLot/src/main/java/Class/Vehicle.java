package Class;

import Abstract.BasicObj;

public class Vehicle extends BasicObj {

    private int numberOfWheels;
    private double weight;

    public Vehicle(String id, int numberOfWheels, double weight) {
        super(id);
        this.numberOfWheels = numberOfWheels;
        this.weight = weight;
    }

}
