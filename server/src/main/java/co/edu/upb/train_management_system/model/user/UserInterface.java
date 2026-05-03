package co.edu.upb.train_management_system.model.user;

import java.rmi.Remote;
import java.rmi.RemoteException;

import co.edu.upb.app.LinkedList.singly.LinkedList;

public interface UserInterface extends Remote {
    AbstractUserWithPower login(String identificacion, String contrasena) throws RemoteException, Exception;

    boolean registerPassenger(Passenger passenger) throws RemoteException, Exception;

    LinkedList<Passenger> getAllPassengers() throws RemoteException, Exception;

    boolean updatePassenger(String id, String nombres, String apellidos, String tipoId, String direccion)
            throws RemoteException, Exception;

    boolean deleteUser(String identificacion) throws RemoteException, Exception;

    boolean registerEmployee(Employee employee) throws RemoteException, Exception;

    LinkedList<Employee> getAllEmployees() throws RemoteException, Exception;

    boolean updateEmployee(String id, String nombres, String apellidos, String tipoId)
            throws RemoteException, Exception;

    boolean updateAdmin(String id, String nombres, String apellidos, String tipoId, String pass)
            throws RemoteException, Exception;

    Passenger loginPassenger(String identificacion, String password) throws RemoteException, Exception;
}