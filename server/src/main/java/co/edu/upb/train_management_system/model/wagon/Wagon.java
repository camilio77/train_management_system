package co.edu.upb.train_management_system.model.wagon;

import java.io.Serializable;

public class Wagon implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    public Wagon(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
