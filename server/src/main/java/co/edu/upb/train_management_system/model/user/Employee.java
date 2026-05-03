package co.edu.upb.train_management_system.model.user;

public class Employee extends AbstractUserWithPower {
    public Employee(String identificacion, String names, String lastNames, String identificationType, String password) {
        super(identificacion, names, lastNames, identificationType, password);
    }
}
