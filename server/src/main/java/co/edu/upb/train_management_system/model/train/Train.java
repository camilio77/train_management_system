package co.edu.upb.train_management_system.model.train;

import java.io.Serializable;

import co.edu.upb.app.LinkedList.doubly.LinkedList;
import co.edu.upb.app.Stack.dinamic.Stack;
import co.edu.upb.train_management_system.model.wagon.PassengerWagon;
import co.edu.upb.train_management_system.model.wagon.Wagon;

public class Train implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String type;
    private int mileage;
    private LinkedList<Wagon> wagons;

    public Train(String id, String name) {
        this.id = id;
        this.name = name;
        this.wagons = new LinkedList<>();

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMileage() {
        return mileage;
    }

    public Wagon[] getWagons() {
        return wagons.toArray();
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void addWagon(Wagon wagon) {
        wagons.add(wagon);
    }

    public void removeWagon(Wagon wagon) {
        wagons.remove(wagon);
    }

    public int getCapacity() {
        int[] capacity = {0};
        wagons.forEach(wagon -> {
            if (wagon instanceof PassengerWagon) {
                capacity[0] += ((PassengerWagon) wagon).getPassengerCount();
            }
            return null;
        });
        return capacity[0];
    }

    public Stack<Wagon> getDischargeOrder() {
        Stack<Wagon> stack = new Stack<>();
        wagons.forEach(wagon -> {
            if (wagon instanceof PassengerWagon) {
                stack.push(wagon);
            }
            return null;
        });
        return stack;
    }

    public String getType(){
        return type;
    }

}
