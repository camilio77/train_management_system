package co.edu.upb.ticketValidator.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ValidatorView {

    private JFrame frame;
    private JComboBox<String> comboRutas;
    private JTextField fieldTicketId;
    private JButton btnCargarRuta;
    private JButton btnValidar;
    private JButton btnLogout;
    private JLabel lblResultado;
    private DefaultTableModel ticketsModel;
    private JTable ticketsTable;

    private static final Color DARK_BLUE  = new Color(30, 58, 95);
    private static final Color BG         = new Color(245, 247, 250);
    private static final Color GREEN      = new Color(34, 139, 80);
    private static final Color RED        = new Color(200, 50, 50);

    public ValidatorView(String userName) {
        frame = new JFrame("Validador de Tickets");
        frame.setSize(900, 600);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        // ── Header ────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(DARK_BLUE);
        header.setBorder(new EmptyBorder(14, 24, 14, 24));

        JLabel title = new JLabel("🎫 Validador de Tickets");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        JLabel nameLabel = new JLabel("👤 " + userName);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        nameLabel.setForeground(new Color(180, 200, 220));

        btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setBackground(RED);
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

        // ── Panel superior: selección de ruta ─────────────────────
        JPanel routePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        routePanel.setBackground(new Color(235, 240, 248));
        routePanel.setBorder(new EmptyBorder(4, 16, 4, 16));

        JLabel lblRuta = new JLabel("Ruta:");
        lblRuta.setFont(new Font("Arial", Font.BOLD, 13));

        comboRutas = new JComboBox<>();
        comboRutas.setPreferredSize(new Dimension(350, 32));
        comboRutas.setFont(new Font("Arial", Font.PLAIN, 13));

        btnCargarRuta = new JButton("↻ Cargar Rutas");
        btnCargarRuta.setBackground(new Color(100, 110, 125));
        btnCargarRuta.setForeground(Color.WHITE);
        btnCargarRuta.setFocusPainted(false);
        btnCargarRuta.setBorderPainted(false);
        btnCargarRuta.setFont(new Font("Arial", Font.BOLD, 12));
        btnCargarRuta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        routePanel.add(lblRuta);
        routePanel.add(comboRutas);
        routePanel.add(btnCargarRuta);

        // ── Panel central: tabla de tickets de la ruta ────────────
        String[] cols = {"ID Ticket", "Pasajero", "Categoría", "Asiento", "Estado"};
        ticketsModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        ticketsTable = new JTable(ticketsModel);
        ticketsTable.setRowHeight(28);
        ticketsTable.setFont(new Font("Arial", Font.PLAIN, 13));
        ticketsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        ticketsTable.getTableHeader().setBackground(DARK_BLUE);
        ticketsTable.getTableHeader().setForeground(Color.WHITE);
        ticketsTable.setSelectionBackground(new Color(200, 215, 235));
        ticketsTable.setGridColor(new Color(220, 225, 230));
        ticketsTable.setShowGrid(true);

        JScrollPane scroll = new JScrollPane(ticketsTable);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(BG);
        tablePanel.setBorder(new EmptyBorder(10, 16, 10, 16));
        tablePanel.add(new JLabel("  Tickets de la ruta seleccionada:",
            new ImageIcon(), SwingConstants.LEFT), BorderLayout.NORTH);
        tablePanel.add(scroll, BorderLayout.CENTER);

        // ── Panel inferior: validación por ID ─────────────────────
        JPanel validatePanel = new JPanel(new BorderLayout());
        validatePanel.setBackground(new Color(235, 240, 248));
        validatePanel.setBorder(new EmptyBorder(12, 16, 12, 16));

        JPanel inputRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        inputRow.setOpaque(false);

        JLabel lblTicket = new JLabel("ID Ticket a validar:");
        lblTicket.setFont(new Font("Arial", Font.BOLD, 13));

        fieldTicketId = new JTextField(10);
        fieldTicketId.setFont(new Font("Arial", Font.PLAIN, 14));
        fieldTicketId.setPreferredSize(new Dimension(140, 34));
        fieldTicketId.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 220)),
            new EmptyBorder(4, 8, 4, 8)));

        btnValidar = new JButton("✔ Validar Ticket");
        btnValidar.setBackground(GREEN);
        btnValidar.setForeground(Color.WHITE);
        btnValidar.setFocusPainted(false);
        btnValidar.setBorderPainted(false);
        btnValidar.setFont(new Font("Arial", Font.BOLD, 13));
        btnValidar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        inputRow.add(lblTicket);
        inputRow.add(fieldTicketId);
        inputRow.add(btnValidar);

        lblResultado = new JLabel(" ");
        lblResultado.setFont(new Font("Arial", Font.BOLD, 14));
        lblResultado.setBorder(new EmptyBorder(8, 0, 0, 0));

        validatePanel.add(inputRow, BorderLayout.NORTH);
        validatePanel.add(lblResultado, BorderLayout.CENTER);

        // ── Armar frame ───────────────────────────────────────────
        JPanel center = new JPanel(new BorderLayout());
        center.add(routePanel,   BorderLayout.NORTH);
        center.add(tablePanel,   BorderLayout.CENTER);
        center.add(validatePanel, BorderLayout.SOUTH);

        frame.add(header, BorderLayout.NORTH);
        frame.add(center, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // ── Métodos para el controller ────────────────────────────────

    public void addRouteOption(String id, String label) {
        comboRutas.addItem(id + " | " + label);
    }

    public void clearRoutes() { comboRutas.removeAllItems(); }

    public String getSelectedRouteId() {
        Object sel = comboRutas.getSelectedItem();
        if (sel == null) return null;
        return sel.toString().split("\\|")[0].trim();
    }

    public void addTicketRow(String id, String pasajero,
                             String categoria, String asiento, String estado) {
        ticketsModel.addRow(new Object[]{id, pasajero, categoria, asiento, estado});
    }

    public void clearTickets() { ticketsModel.setRowCount(0); }

    public String getTicketId() { return fieldTicketId.getText().trim(); }

    public void showValidResult(boolean valid, String message) {
        SwingUtilities.invokeLater(() -> {
            lblResultado.setForeground(valid ? new Color(20, 130, 60) : RED);
            lblResultado.setText(valid ? "✔ " + message : "✖ " + message);
        });
    }

    public void onLoadRoutes(Runnable handler) {
        btnCargarRuta.addActionListener(e -> handler.run());
        // Cargar al cambiar la selección también
        comboRutas.addActionListener(e -> handler.run());
    }

    public void onValidate(Runnable handler) {
        btnValidar.addActionListener(e -> handler.run());
        fieldTicketId.addActionListener(e -> handler.run()); // Enter en el campo
    }

    public void onLogout(Runnable handler) {
        btnLogout.addActionListener(e -> handler.run());
    }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(frame, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void close() { frame.dispose(); }
}