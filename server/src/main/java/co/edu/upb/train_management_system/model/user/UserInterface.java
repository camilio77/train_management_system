package co.edu.upb.train_management_system.model.user;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface UserInterface {
    public boolean addCellphone(String cellPhoneNumber) throws RemoteException;

    public boolean deleteCellPhone(String cellPhone) throws RemoteException;
}
