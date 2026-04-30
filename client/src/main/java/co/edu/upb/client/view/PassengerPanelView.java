package co.edu.upb.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class PassengerPanelView {

    private JFrame            frame;
    private DefaultTableModel tableModel;
    private JButton           btnLogout;
    private JButton           btnRefresh;

    private static final Color DARK_BLUE = new Color(30, 58, 95);
    private static final Color BG        = new Color(245, 247, 250);

    public PassengerPanelView(String passengerName) {
        build(passengerName);
    }

    private void build(String passengerName) {
        frame = new JFrame("Train Management — Pasajero");
        frame.setSize(1000, 600);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // ── Header ────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(DARK_BLUE);
        header.setBorder(new EmptyBorder(14, 24, 14, 24));

        JLabel title = new JLabel("🚆 Train Management — Rutas disponibles");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        JLabel nameLabel = new JLabel("👤 " + passengerName);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        nameLabel.setForeground(new Color(180, 200, 220));

        btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setBackground(new Color(220, 60, 60));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        headerRight.setOpaque(false);
        headerRight.add(nameLabel);
        headerRight.add(btnLogout);

        header.add(title, BorderLayout.WEST);
        header.add(headerRight, BorderLayout.EAST);

        // ── Tabla de rutas ────────────────────────────────────────
        String[] cols = {"ID", "Tren", "Origen", "Destino", "Fecha Salida", "Fecha Llegada"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(DARK_BLUE);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(200, 215, 235));
        table.setGridColor(new Color(220, 225, 230));
        table.setShowGrid(true);

        // ── Botones ───────────────────────────────────────────────
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        buttons.setBackground(BG);

        btnRefresh = new JButton("↻ Actualizar");
        btnRefresh.setBackground(new Color(100, 110, 125));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setBorderPainted(false);
        btnRefresh.setFont(new Font("Arial", Font.BOLD, 12));
        btnRefresh.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRefresh.setBorder(new EmptyBorder(8, 16, 8, 16));
        buttons.add(btnRefresh);

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(BG);
        center.setBorder(new EmptyBorder(12, 16, 12, 16));
        center.add(buttons, BorderLayout.NORTH);
        center.add(new JScrollPane(table), BorderLayout.CENTER);

        frame.add(header, BorderLayout.NORTH);
        frame.add(center, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // ── Métodos para el controller ────────────────────────────────

    public void addRoute(String id, String tren, String origen,
                         String destino, String salida, String llegada) {
        tableModel.addRow(new Object[]{id, tren, origen, destino, salida, llegada});
    }

    public void clearRoutes() {
        tableModel.setRowCount(0);
    }

    public void onLogout(Runnable handler) {
        btnLogout.addActionListener(e -> handler.run());
    }

    public void onRefresh(Runnable handler) {
        btnRefresh.addActionListener(e -> handler.run());
    }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(frame, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void close() { frame.dispose(); }
}