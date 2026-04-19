package co.edu.upb.train_management_system.model.wagon;

public class Wagon implements WagonInterface {
    private String id;

    public Wagon(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
