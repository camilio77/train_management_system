package co.edu.upb.train_management_system.model.user;

import java.io.Serializable;

public abstract class AbstractUserWithPower extends AbstractUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private String password;
    public AbstractUserWithPower(String identificacion, String names, String lastNames, String identificationType, String password) {
        super(identificacion, names, lastNames, identificationType);
        this.password = password;
    }
}
