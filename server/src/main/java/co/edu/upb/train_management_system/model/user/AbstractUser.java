package co.edu.upb.train_management_system.model.user;

import co.edu.upb.app.LinkedList.singly.LinkedList;

public abstract class AbstractUser implements UserInterface{
    private int identificacion;
    private String names;
    private String lastNames;
    private String identificationType;
    protected String password;
    private LinkedList<String> cellPhones;

    public AbstractUser(int identificacion, String names, String lastNames, String identificationType) {
        this.identificacion = identificacion;
        this.names = names;
        this.lastNames = lastNames;
        this.identificationType = identificationType;
        this.cellPhones = new LinkedList<>();
    }

    public int getIdentificacion() {
        return identificacion;
    }

    public boolean setIdentificacion(int identificacion) {
        this.identificacion = identificacion;
        return true;
    }

    public boolean setNames(String names) {
        this.names = names;
        return true;
    }

    public String getFullName() {
        return names + " " + lastNames;
    }

    public boolean setLastNames(String lastNames) {
        this.lastNames = lastNames;
        return true;
    }

    public String getIdentificationType() {
        return identificationType;
    }

    public String getPassword() {
        return password;
    }

    public boolean setPassword(String password) {
        this.password = password;
        return true;
    }

    public String getCellPhones() {
        return cellPhones.toString();
    }

    public boolean addCellphone(String cellPhoneNumber){
        return cellPhones.add(cellPhoneNumber);
    }

    public boolean deleteCellPhone(String cellPhone){
        return cellPhones.remove(cellPhone);
    }
}
