package co.edu.upb.train_management_system.model.observer;

public interface Observable {

  public void attach(Observer observer);

  public void detach(Observer observer);

  public void notifyObservers();

}