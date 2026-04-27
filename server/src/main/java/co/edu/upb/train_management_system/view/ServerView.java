package co.edu.upb.train_management_system.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.function.UnaryOperator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import co.edu.upb.train_management_system.model.history.History;
import co.edu.upb.train_management_system.model.observer.Observer;
import co.edu.upb.train_management_system.model.observer.Subject;

public class ServerView extends Observer {

    private JButton button;
    private JButton stopButton;
    private JButton testButton;
    private JLabel console;
    private JPanel mainPanel;
    private JPanel panelButton;
    private JFrame frame;
    private String title;

    public ServerView(String title, Subject subject) {
        super(subject);
        this.title = title;
        buildComponents();   // inicializa todo aquí, sin depender del .form
    }

    private void buildComponents() {
        // Panel consola
        JPanel panelConsole = new JPanel(new BorderLayout());
        panelConsole.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        console = new JLabel("Estado: esperando...", SwingConstants.LEFT);
        console.setOpaque(true);
        console.setBackground(Color.WHITE);
        console.setFont(new Font("Monospaced", Font.PLAIN, 13));
        console.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        panelConsole.add(console, BorderLayout.CENTER);

        // Panel botones
        panelButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));

        button = new JButton("▶ Iniciar Servidor");
        button.setBackground(new Color(34, 139, 80));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);

        stopButton = new JButton("⏹ Detener");
        stopButton.setBackground(new Color(200, 50, 50));
        stopButton.setForeground(Color.WHITE);
        stopButton.setFocusPainted(false);
        stopButton.setEnabled(false);

        panelButton.add(button);
        panelButton.add(stopButton);

        // Panel principal
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panelConsole, BorderLayout.CENTER);
        mainPanel.add(panelButton, BorderLayout.SOUTH);
    }

    public void initComponents(UnaryOperator<Void> onStart, UnaryOperator<Void> onStop) {
        if (GraphicsEnvironment.isHeadless()) return;

        if (frame == null) {
            frame = new JFrame(title);
            frame.setContentPane(mainPanel);
            frame.setSize(600, 250);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);
        }

        button.addActionListener(e -> onStart.apply(null));
        stopButton.addActionListener(e -> onStop.apply(null));

        frame.setVisible(true);
    }

    public void startStatus(String status) {
        button.setEnabled(false);
        stopButton.setEnabled(true);
        getHistory().addAction(status);
    }

    public void stopStatus(String status) {
        button.setEnabled(true);
        stopButton.setEnabled(false);
        getHistory().addAction(status);
    }

    public void showTestButton() {
        if (testButton == null) {
            testButton = new JButton("🧪 Prueba Login");
            testButton.addActionListener(e -> new LoginView());
            panelButton.add(testButton);
            panelButton.revalidate();
            panelButton.repaint();
        }
    }

    @Override
    public void update() {
        console.setText("  Status: " + getHistory().getLastAction());
    }

    public History getHistory() {
        return (History) subject;
    }

    public void setMessage(String msg) {
        getHistory().addAction(msg);
    }
}