package co.edu.upb.train_management_system.model.train;

import java.io.Serializable;

import co.edu.upb.app.LinkedList.doubly.LinkedList;
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
        
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
