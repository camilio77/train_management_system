package co.edu.upb.train_management_system.model.observer;

import co.edu.upb.app.LinkedList.singly.LinkedList;

public abstract class Subject implements Observable {

  protected LinkedList<Observer> observers;

  protected Subject() {
    this.observers = new LinkedList<>();
  }

  @Override
  public void attach(Observer observer) {
    this.observers.add(observer);
  }

  @Override
  public void detach(Observer observer) {
    this.observers.remove(observer);
  }

  @Override
  public void notifyObservers() {
    this.observers.forEach(observer -> {
      observer.update();
      return null;
    });
  }

}