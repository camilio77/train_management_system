package co.edu.upb.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class PassengerPanelView {

    public static class ProfileData {

        public final String nombres, apellidos, tipoId, direccion;

        public ProfileData(String n, String a, String t, String d) {
            nombres = n;
            apellidos = a;
            tipoId = t;
            direccion = d;
        }
    }

    private JFrame frame;
    private JLabel nameLabel;
    private JLabel bannerActivo;

    private DefaultTableModel routeModel;
    private JTable routeTable;

    private DefaultTableModel ticketModel;
    private JTable ticketTable;

    private JButton btnLogout, btnRefresh, btnBuyTicket, btnEditProfile, btnViewTickets;

    private static final Color DARK_BLUE = new Color(30, 58, 95);
    private static final Color BG = new Color(245, 247, 250);
    private static final Color GREEN = new Color(34, 139, 80);

    public PassengerPanelView(String passengerName) {
        build(passengerName);
    }

    private void build(String passengerName) {
        frame = new JFrame("Train Management — Pasajero");
        frame.setSize(1050, 650);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(DARK_BLUE);
        header.setBorder(new EmptyBorder(14, 24, 14, 24));

        JLabel title = new JLabel("🚆 Train Management");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        nameLabel = new JLabel("👤 " + passengerName);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        nameLabel.setForeground(new Color(180, 200, 220));

        btnEditProfile = headerBtn("✏ Mi Perfil", new Color(60, 100, 150));
        btnLogout = headerBtn("Cerrar Sesión", new Color(220, 60, 60));

        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        headerRight.setOpaque(false);
        headerRight.add(nameLabel);
        headerRight.add(btnEditProfile);
        headerRight.add(btnLogout);

        header.add(title, BorderLayout.WEST);
        header.add(headerRight, BorderLayout.EAST);

        bannerActivo = new JLabel(" ", JLabel.CENTER);
        bannerActivo.setFont(new Font("Arial", Font.BOLD, 13));
        bannerActivo.setOpaque(true);
        bannerActivo.setBackground(new Color(220, 245, 225));
        bannerActivo.setForeground(new Color(20, 100, 50));
        bannerActivo.setBorder(new EmptyBorder(6, 0, 6, 0));
        bannerActivo.setVisible(false);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 13));
        tabs.addTab("🛤 Rutas disponibles", buildRouteTab());
        tabs.addTab("🎫 Mis Tickets", buildTicketTab());

        JPanel center = new JPanel(new BorderLayout());
        center.add(bannerActivo, BorderLayout.NORTH);
        center.add(tabs, BorderLayout.CENTER);

        frame.add(header, BorderLayout.NORTH);
        frame.add(center, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private JPanel buildRouteTab() {
        String[] cols = {"ID", "Tren", "Origen", "Destino", "Fecha Salida", "Fecha Llegada"};
        routeModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        routeTable = styledTable(routeModel);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        buttons.setBackground(BG);

        btnRefresh = actionBtn("↻ Actualizar", new Color(100, 110, 125));
        btnBuyTicket = actionBtn("🎫 Comprar Ticket", GREEN);

        buttons.add(btnRefresh);
        buttons.add(btnBuyTicket);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(10, 14, 10, 14));
        panel.add(buttons, BorderLayout.NORTH);
        panel.add(new JScrollPane(routeTable), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildTicketTab() {
        String[] cols = {"ID", "Origen", "Destino", "Categoría", "Valor", "Asiento", "Estado"};
        ticketModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        ticketTable = styledTable(ticketModel);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        buttons.setBackground(BG);

        btnViewTickets = actionBtn("↻ Actualizar tickets", new Color(100, 110, 125));
        buttons.add(btnViewTickets);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(10, 14, 10, 14));
        panel.add(buttons, BorderLayout.NORTH);
        panel.add(new JScrollPane(ticketTable), BorderLayout.CENTER);
        return panel;
    }

    public void addRoute(String id, String tren, String origen,
            String destino, String salida, String llegada) {
        routeModel.addRow(new Object[]{id, tren, origen, destino, salida, llegada});
    }

    public void clearRoutes() {
        routeModel.setRowCount(0);
    }

    public void addTicket(String id, String origen, String destino,
            String cat, String valor, String asiento, String estado) {
        ticketModel.addRow(new Object[]{id, origen, destino, cat, valor, asiento, estado});
    }

    public void clearTickets() {
        ticketModel.setRowCount(0);
    }

    public void setActiveTicketBanner(boolean hasActive) {
        SwingUtilities.invokeLater(() -> {
            if (hasActive) {
                bannerActivo.setText("✅ Tienes un ticket activo — revisa la pestaña 'Mis Tickets'");
                bannerActivo.setVisible(true);
            } else {
                bannerActivo.setVisible(false);
            }
        });
    }

    public void updateName(String fullName) {
        SwingUtilities.invokeLater(() -> nameLabel.setText("👤 " + fullName));
    }

    public ProfileData showEditProfileDialog(String nombres, String apellidos,
            String tipoId, String direccion) {
        JTextField fNombres = new JTextField(nombres, 18);
        JTextField fApellidos = new JTextField(apellidos, 18);
        JComboBox<String> fTipo = new JComboBox<>(new String[]{"CC", "TI", "CE"});
        fTipo.setSelectedItem(tipoId);
        JTextField fDireccion = new JTextField(direccion, 18);

        Object[] fields = {
            "Nombres:", fNombres,
            "Apellidos:", fApellidos,
            "Tipo ID:", fTipo,
            "Dirección:", fDireccion
        };

        int r = JOptionPane.showConfirmDialog(frame, fields,
                "Editar mi perfil", JOptionPane.OK_CANCEL_OPTION);
        if (r != JOptionPane.OK_OPTION) {
            return null;
        }

        String n = fNombres.getText().trim();
        String a = fApellidos.getText().trim();
        if (n.isEmpty() || a.isEmpty()) {
            showError("Nombres y apellidos no pueden estar vacíos.");
            return null;
        }
        return new ProfileData(n, a, (String) fTipo.getSelectedItem(),
                fDireccion.getText().trim());
    }

    public void onLogout(Runnable h) {
        btnLogout.addActionListener(e -> h.run());
    }

    public void onRefresh(Runnable h) {
        btnRefresh.addActionListener(e -> h.run());
    }

    public void onBuyTicket(Runnable h) {
        btnBuyTicket.addActionListener(e -> h.run());
    }

    public void onEditProfile(Runnable h) {
        btnEditProfile.addActionListener(e -> h.run());
    }

    public void onViewTickets(Runnable h) {
        btnViewTickets.addActionListener(e -> h.run());
    }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(frame, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccess(String msg) {
        JOptionPane.showMessageDialog(frame, msg, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public void close() {
        frame.dispose();
    }

    public String getSelectedRouteId() {
        int row = routeTable.getSelectedRow();
        return row < 0 ? null : routeModel.getValueAt(row, 0).toString();
    }

    public String getSelectedRouteOrigen() {
        int row = routeTable.getSelectedRow();
        return row < 0 ? null : routeModel.getValueAt(row, 2).toString();
    }

    public String getSelectedRouteDestino() {
        int row = routeTable.getSelectedRow();
        return row < 0 ? null : routeModel.getValueAt(row, 3).toString();
    }

    private JTable styledTable(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setRowHeight(28);
        t.setFont(new Font("Arial", Font.PLAIN, 13));
        t.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        t.getTableHeader().setBackground(DARK_BLUE);
        t.getTableHeader().setForeground(Color.WHITE);
        t.setSelectionBackground(new Color(200, 215, 235));
        t.setGridColor(new Color(220, 225, 230));
        t.setShowGrid(true);
        return t;
    }

    private JButton actionBtn(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        return btn;
    }

    private JButton headerBtn(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
