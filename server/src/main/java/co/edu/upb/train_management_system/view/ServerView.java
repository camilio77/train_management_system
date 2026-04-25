package co.edu.upb.train_management_system.view;

import java.util.function.UnaryOperator;

import co.edu.upb.train_management_system.model.history.History;
import co.edu.upb.train_management_system.model.observer.Observer;
import co.edu.upb.train_management_system.model.observer.Subject;

import javax.swing.*;
import java.awt.*;
import java.awt.GraphicsEnvironment;
import java.util.function.UnaryOperator;

public class ServerView extends Observer{
    private JPanel mainPanel;
    private JButton button;
    private JPanel panelButton;
    private JPanel panelConsole;
    private JLabel console;

    private String title;
    private JFrame frame;

    public ServerView(String title, Subject subject) {
        super(subject);
        this.title = title;
        // No inicializas los componentes aquí, el .form ya lo hace
    }

    public void initComponents(UnaryOperator<Void> fn) {
        console.setOpaque(true);
        console.setBackground(new Color(255, 255, 255));

        if (GraphicsEnvironment.isHeadless()) return;

        if (frame == null) frame = new JFrame(title);

        frame.setContentPane(mainPanel);
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        button.addActionListener(event -> fn.apply(null));

        frame.setVisible(true);
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
