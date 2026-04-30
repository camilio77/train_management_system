package co.edu.upb.train_management_system.model.station;

import java.io.Serializable;

public class Station implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;

    public Station(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean setName(String name) {
        this.name = name;
        return true;
    }

    public boolean setId(String id) {
        this.id = id;
        return true;
    }

}
