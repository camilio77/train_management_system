package co.edu.upb.train_management_system.model.user;

public class Admin extends AbstractUserWithPower {
    public Admin(String identificacion, String names, String lastNames, String identificationType, String password) {
        super(identificacion, names, lastNames, identificationType, password);
    }
}
