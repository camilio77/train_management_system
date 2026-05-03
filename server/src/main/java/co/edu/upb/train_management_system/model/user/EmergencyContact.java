package co.edu.upb.train_management_system.model.user;

import java.io.Serializable;

public class EmergencyContact extends AbstractUser implements Serializable {

    public EmergencyContact(String identificacion, String names, String lastNames, String identificationType) {
        super(identificacion, names, lastNames, identificationType);
    }
}
