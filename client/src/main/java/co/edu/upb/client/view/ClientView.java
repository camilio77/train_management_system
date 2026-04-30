package co.edu.upb.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.function.UnaryOperator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class ClientView {

    private JFrame  frame;
    private JLabel  console;
    private JButton btnConnect;
    private JButton btnDisconnect;
    private final String title;

    public ClientView(String title) {
        this.title = title;
        buildComponents();
    }

    private void buildComponents() {
        // ── consola ───────────────────────────────────────────────
        JPanel panelConsole = new JPanel(new BorderLayout());
        panelConsole.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        console = new JLabel("Estado: desconectado", SwingConstants.LEFT);
        console.setOpaque(true);
        console.setBackground(Color.WHITE);
        console.setFont(new Font("Monospaced", Font.PLAIN, 13));
        console.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        panelConsole.add(console, BorderLayout.CENTER);

        // ── botones ───────────────────────────────────────────────
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));

        btnConnect = new JButton("▶ Conectar");
        btnConnect.setBackground(new Color(34, 139, 80));
        btnConnect.setForeground(Color.WHITE);
        btnConnect.setFocusPainted(false);

        btnDisconnect = new JButton("⏹ Desconectar");
        btnDisconnect.setBackground(new Color(200, 50, 50));
        btnDisconnect.setForeground(Color.WHITE);
        btnDisconnect.setFocusPainted(false);
        btnDisconnect.setEnabled(false);

        panelButtons.add(btnConnect);
        panelButtons.add(btnDisconnect);

        // ── frame principal ───────────────────────────────────────
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(panelConsole, BorderLayout.CENTER);
        mainPanel.add(panelButtons, BorderLayout.SOUTH);

        frame = new JFrame(title);
        frame.setContentPane(mainPanel);
        frame.setSize(600, 200);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
    }

    public void initComponents(UnaryOperator<Void> onConnect,
                               UnaryOperator<Void> onDisconnect) {
        btnConnect.addActionListener(e -> onConnect.apply(null));
        btnDisconnect.addActionListener(e -> onDisconnect.apply(null));
        frame.setVisible(true);
    }

    // ── métodos de estado ─────────────────────────────────────────

    public void onConnected() {
        SwingUtilities.invokeLater(() -> {
            btnConnect.setEnabled(false);
            btnDisconnect.setEnabled(true);
            console.setBackground(new Color(220, 255, 220));
        });
    }

    public void onDisconnected() {
        SwingUtilities.invokeLater(() -> {
            btnConnect.setEnabled(true);
            btnDisconnect.setEnabled(false);
            console.setBackground(Color.WHITE);
        });
    }

    public void onConnectionFailed() {
        SwingUtilities.invokeLater(() -> {
            console.setBackground(new Color(255, 220, 220));
            btnConnect.setEnabled(true);
        });
    }

    public void setLog(String msg) {
        SwingUtilities.invokeLater(() -> console.setText("  " + msg));
    }
}