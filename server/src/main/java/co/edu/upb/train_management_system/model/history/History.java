package co.edu.upb.train_management_system.model.history;

import co.edu.upb.app.Stack.dinamic.Stack;
import co.edu.upb.train_management_system.model.observer.Subject;

public class History extends Subject {

  private Stack<Action> actions;

  public History() {
    this.actions = new Stack<>();
  }

  public void addAction(String description) {
    this.actions.push(new Action(description));
    this.notifyObservers();
  }

  public String getLastAction() {
    if (actions.isEmpty()) {
      return "No actions yet.";
    }
    Action lastAction = actions.peek();
    return lastAction.getTimestamp() + ": " + lastAction.getDescription();
  }
}