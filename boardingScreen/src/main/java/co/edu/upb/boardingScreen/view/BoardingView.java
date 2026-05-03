package co.edu.upb.boardingScreen.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BoardingView {

    private JFrame            frame;
    private JComboBox<String> comboRutas;
    private JButton           btnCargar;
    private JButton           btnLogout;
    private JLabel            lblInfo;
    private DefaultTableModel tableModel;
    private JTable            table;

    private static final Color DARK_BLUE = new Color(20, 60, 100);
    private static final Color BG        = new Color(245, 247, 250);

    public BoardingView(String userName) {
        frame = new JFrame("Sistema de Abordaje");
        frame.setSize(950, 620);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // ── Header ────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(DARK_BLUE);
        header.setBorder(new EmptyBorder(14, 24, 14, 24));

        JLabel title = new JLabel("🚂 Orden de Abordaje");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        JLabel nameLabel = new JLabel("👤 " + userName);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        nameLabel.setForeground(new Color(180, 200, 220));

        btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setBackground(new Color(200, 50, 50));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        headerRight.setOpaque(false);
        headerRight.add(nameLabel);
        headerRight.add(btnLogout);

        header.add(title,       BorderLayout.WEST);
        header.add(headerRight, BorderLayout.EAST);

        // ── Selección de ruta ─────────────────────────────────────
        JPanel routePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        routePanel.setBackground(new Color(235, 240, 248));
        routePanel.setBorder(new EmptyBorder(4, 16, 4, 16));

        JLabel lblRuta = new JLabel("Ruta:");
        lblRuta.setFont(new Font("Arial", Font.BOLD, 13));

        comboRutas = new JComboBox<>();
        comboRutas.setPreferredSize(new Dimension(430, 32));
        comboRutas.setFont(new Font("Arial", Font.PLAIN, 13));

        btnCargar = new JButton("🔍 Ver Orden de Abordaje");
        btnCargar.setBackground(DARK_BLUE);
        btnCargar.setForeground(Color.WHITE);
        btnCargar.setFocusPainted(false);
        btnCargar.setBorderPainted(false);
        btnCargar.setFont(new Font("Arial", Font.BOLD, 12));
        btnCargar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        routePanel.add(lblRuta);
        routePanel.add(comboRutas);
        routePanel.add(btnCargar);

        // ── Info ──────────────────────────────────────────────────
        lblInfo = new JLabel(" ");
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 12));
        lblInfo.setForeground(new Color(100, 110, 125));
        lblInfo.setBorder(new EmptyBorder(4, 20, 4, 0));

        // ── Tabla ─────────────────────────────────────────────────
        String[] cols = {"#", "ID Ticket", "Pasajero", "Categoría", "Vagón", "Asiento"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(DARK_BLUE);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(200, 215, 235));
        table.setGridColor(new Color(220, 225, 230));

        // Colorear filas por categoría
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(
                        t, val, sel, foc, row, col);
                if (!sel) {
                    Object cat = tableModel.getValueAt(row, 3);
                    if ("PREMIUM".equals(cat))
                        c.setBackground(new Color(255, 248, 220));
                    else if ("EJECUTIVA".equals(cat))
                        c.setBackground(new Color(225, 235, 255));
                    else
                        c.setBackground(Color.WHITE);
                }
                return c;
            }
        });

        JScrollPane scroll = new JScrollPane(table);

        // Leyenda
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 4));
        legend.setBackground(BG);
        legend.add(legendItem(new Color(255, 248, 220), "PREMIUM"));
        legend.add(legendItem(new Color(225, 235, 255), "EJECUTIVA"));
        legend.add(legendItem(Color.WHITE,              "ESTÁNDAR"));

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(BG);
        center.setBorder(new EmptyBorder(10, 16, 10, 16));
        center.add(lblInfo, BorderLayout.NORTH);
        center.add(scroll,  BorderLayout.CENTER);
        center.add(legend,  BorderLayout.SOUTH);

        JPanel top = new JPanel(new BorderLayout());
        top.add(header,     BorderLayout.NORTH);
        top.add(routePanel, BorderLayout.SOUTH);

        frame.add(top,    BorderLayout.NORTH);
        frame.add(center, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private JPanel legendItem(Color color, String label) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        p.setOpaque(false);
        JLabel dot = new JLabel("   ");
        dot.setOpaque(true);
        dot.setBackground(color);
        dot.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 220)));
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.PLAIN, 11));
        p.add(dot);
        p.add(lbl);
        return p;
    }

    // ── API para el controller ────────────────────────────────────

    public void addRouteOption(String id, String label) {
        comboRutas.addItem(id + " | " + label);
    }

    public void clearRoutes() { comboRutas.removeAllItems(); }

    public String getSelectedRouteId() {
        Object sel = comboRutas.getSelectedItem();
        if (sel == null) return null;
        return sel.toString().split("\\|")[0].trim();
    }

    public void addBoardingRow(int orden, String idTicket, String pasajero,
                               String categoria, String vagon, String asiento) {
        tableModel.addRow(new Object[]{orden, idTicket, pasajero,
                                       categoria, vagon, asiento});
    }

    public void clearTable() { tableModel.setRowCount(0); }

    public void setInfo(String msg) {
        SwingUtilities.invokeLater(() -> lblInfo.setText(msg));
    }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(frame, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void onLoad(Runnable handler) {
        btnCargar.addActionListener(e -> handler.run());
        comboRutas.addActionListener(e -> handler.run());
    }

    public void onLogout(Runnable handler) {
        btnLogout.addActionListener(e -> handler.run());
    }

    public void close() { frame.dispose(); }
}