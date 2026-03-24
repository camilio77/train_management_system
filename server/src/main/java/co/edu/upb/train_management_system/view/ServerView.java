package co.edu.upb.train_management_system.view;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.function.UnaryOperator;
import java.awt.GraphicsEnvironment;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import co.edu.upb.train_management_system.model.history.History;
import co.edu.upb.train_management_system.model.observer.Observer;
import co.edu.upb.train_management_system.model.observer.Subject;

public class ServerView extends Observer {
  private String title;
  private JFrame frame;
  private JButton button;
  private JPanel panelButton;
  private JPanel panelConsole;
  private JLabel console;

  public ServerView(String title, Subject subject) {
    super(subject);
    this.title = title;
    this.frame = new JFrame(this.title);
    this.button = new JButton("Start Server");
    this.panelButton = new JPanel();
    this.panelConsole = new JPanel();
    this.console = new JLabel("Status: Stopped.");
  }

  public void initComponents(UnaryOperator<Void> fn) {
    console.setOpaque(true);
    console.setBackground(new Color(255, 255, 255));
    if (panelConsole.getComponentCount() == 0) {
      panelConsole.add(console);
    }
    panelConsole.setLayout(new GridLayout(1, 1));
    if (panelButton.getComponentCount() == 0) {
      panelButton.setLayout(new GridLayout(3, 3));
      startButton(fn);
    }

    if (GraphicsEnvironment.isHeadless()) {
      return;
    }

    if (frame == null) {
      frame = new JFrame(title);
    }
    frame.setSize(400, 200);
    frame.setLayout(new GridLayout(2, 1));
    frame.add(panelButton);
    frame.add(panelConsole);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  private void startButton(UnaryOperator<Void> fn) {
    button.addActionListener(event -> fn.apply(null));
    for (int i = 0; i < 6; i++) {
      if (i == 4) {
        panelButton.add(button);
      }
      panelButton.add(new JLabel());
    }
  }

  public void startStatus(String status) {
    button.setText(status);
    button.setEnabled(false);
    this.getHistory().addAction(status);
  }

  @Override
  public void update() {
    console.setText("Status: " + this.getHistory().getLastAction());
  }

  public History getHistory() {
    return (History) subject;
  }

  public void setMessage(String msg) {
    this.getHistory().addAction(msg);
  }
}