package co.edu.upb.train_management_system.view;

import java.awt.*;
import java.sql.Timestamp;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import co.edu.upb.app.LinkedList.singly.LinkedList;
import co.edu.upb.train_management_system.DataBase.DatabaseConnection;
import co.edu.upb.train_management_system.model.route.Route;
import co.edu.upb.train_management_system.model.route.RouteService;
import co.edu.upb.train_management_system.model.station.Station;
import co.edu.upb.train_management_system.model.station.StationService;
import co.edu.upb.train_management_system.model.train.Train;
import co.edu.upb.train_management_system.model.train.TrainService;
import co.edu.upb.train_management_system.model.user.AbstractUserWithPower;
import co.edu.upb.train_management_system.model.user.Passenger;
import co.edu.upb.train_management_system.model.user.UserService;
import co.edu.upb.train_management_system.model.wagon.WagonService;

public class EmployeePanelView {
    private JFrame frame;

    private static final Color DARK_GREEN = new Color(20, 100, 70);
    private static final Color BG = new Color(245, 247, 250);

    public EmployeePanelView(AbstractUserWithPower employee) {
        frame = new JFrame("Panel Empleado — " + employee.getFullName());
        frame.setSize(1100, 680);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(DARK_GREEN);
        header.setBorder(new EmptyBorder(14, 24, 14, 24));

        JLabel title = new JLabel("🚆 Train Management — Panel Empleado");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        JLabel empName = new JLabel("👤 " + employee.getFullName());
        empName.setFont(new Font("Arial", Font.PLAIN, 13));
        empName.setForeground(new Color(180, 230, 210));

        JButton btnEditProfile = new JButton("✏ Mi Perfil");
        btnEditProfile.setBackground(new Color(30, 140, 100));
        btnEditProfile.setForeground(Color.WHITE);
        btnEditProfile.setFocusPainted(false);
        btnEditProfile.setBorderPainted(false);
        btnEditProfile.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEditProfile.addActionListener(e -> showEditProfileDialog(employee));

        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setBackground(new Color(220, 60, 60));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> {
            frame.dispose();
            new LoginView();
        });

        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        headerRight.setOpaque(false);
        headerRight.add(empName);
        headerRight.add(btnEditProfile);
        headerRight.add(btnLogout);

        header.add(title, BorderLayout.WEST);
        header.add(headerRight, BorderLayout.EAST);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 13));
        tabs.setBackground(BG);
        tabs.addTab("🚂 Trenes", buildTrainTab());
        tabs.addTab("🏛 Estaciones", buildStationTab());
        tabs.addTab("🛤 Rutas", buildRouteTab());
        tabs.addTab("🚃 Vagones", buildWagonTab());
        tabs.addTab("👥 Pasajeros", buildPassengerTab());

        frame.add(header, BorderLayout.NORTH);
        frame.add(tabs, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // ─── TRENES — solo lectura ─────────────────────────────────────
    private JPanel buildTrainTab() {
        String[] cols = {"ID", "Nombre", "Tipo", "Kilometraje"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = styledTable(model);
        loadTrains(model);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        buttons.setBackground(BG);
        JButton btnRefresh = actionButton("↻ Actualizar", new Color(100, 110, 125));
        buttons.add(btnRefresh);
        btnRefresh.addActionListener(e -> loadTrains(model));
        return buildTabPanel(table, buttons);
    }

    private void loadTrains(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            TrainService.getInstance().getAll().forEach(t -> {
                model.addRow(new Object[]{t.getId(), t.getName(), t.getType(), t.getMileage()});
                return null;
            });
        } catch (Exception ex) {
            showError(ex);
        }
    }

    // ─── ESTACIONES — solo lectura ─────────────────────────────────
    private JPanel buildStationTab() {
        String[] cols = {"ID", "Nombre"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = styledTable(model);
        loadStations(model);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        buttons.setBackground(BG);
        JButton btnRefresh = actionButton("↻ Actualizar", new Color(100, 110, 125));
        buttons.add(btnRefresh);
        btnRefresh.addActionListener(e -> loadStations(model));
        return buildTabPanel(table, buttons);
    }

    private void loadStations(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            StationService.getInstance().getAll().forEach(s -> {
                model.addRow(new Object[]{s.getId(), s.getName()});
                return null;
            });
        } catch (Exception ex) {
            showError(ex);
        }
    }

    // ─── RUTAS — CRUD completo ─────────────────────────────────────
    private JPanel buildRouteTab() {
        String[] cols = {"ID", "Tren", "Origen", "Destino", "Fecha Salida", "Fecha Llegada"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = styledTable(model);
        loadRoutes(model);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        buttons.setBackground(BG);

        JButton btnAdd = actionButton("+ Agregar", new Color(34, 139, 80));
        JButton btnEdit = actionButton("✏ Editar", new Color(30, 58, 95));
        JButton btnDelete = actionButton("🗑 Eliminar", new Color(200, 50, 50));
        JButton btnRefresh = actionButton("↻ Actualizar", new Color(100, 110, 125));

        buttons.add(btnAdd);
        buttons.add(btnEdit);
        buttons.add(btnDelete);
        buttons.add(btnRefresh);

        btnRefresh.addActionListener(e -> loadRoutes(model));

        btnAdd.addActionListener(e -> {
            try {
                LinkedList<Train> trenes = TrainService.getInstance().getAll();
                LinkedList<Station> estaciones = StationService.getInstance().getAll();

                if (trenes.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "No hay trenes registrados.");
                    return;
                }
                if (estaciones.size() < 2) {
                    JOptionPane.showMessageDialog(frame, "Se necesitan al menos 2 estaciones.");
                    return;
                }

                JComboBox<Train> cmbTren = new JComboBox<>(trenes.toArray());
                cmbTren.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
                    JLabel lbl = new JLabel(value != null ? value.getId() + " — " + value.getName() : "");
                    if (isSelected) {
                        lbl.setOpaque(true);
                        lbl.setBackground(list.getSelectionBackground());
                    }
                    return lbl;
                });

                JComboBox<Station> cmbOrigen = new JComboBox<>(estaciones.toArray());
                JComboBox<Station> cmbDestino = new JComboBox<>(estaciones.toArray());
                cmbDestino.setSelectedIndex(1);

                var stRenderer = new DefaultListCellRenderer() {
                    public Component getListCellRendererComponent(JList<?> list, Object value,
                                                                  int index, boolean isSelected, boolean cellHasFocus) {
                        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                        if (value instanceof Station s) setText(s.getName());
                        return this;
                    }
                };
                cmbOrigen.setRenderer(stRenderer);
                cmbDestino.setRenderer(stRenderer);

                JTextField salida = new JTextField("2025-06-01 08:00:00");
                JTextField llegada = new JTextField("2025-06-01 12:00:00");

                Object[] fields = {
                        "Tren:", cmbTren,
                        "Origen:", cmbOrigen,
                        "Destino:", cmbDestino,
                        "Fecha Salida (yyyy-MM-dd HH:mm:ss):", salida,
                        "Fecha Llegada:", llegada
                };

                int r = JOptionPane.showConfirmDialog(frame, fields, "Agregar Ruta", JOptionPane.OK_CANCEL_OPTION);
                if (r == JOptionPane.OK_OPTION) {
                    Station origen = (Station) cmbOrigen.getSelectedItem();
                    Station destino = (Station) cmbDestino.getSelectedItem();

                    if (origen.getId().equals(destino.getId())) {
                        JOptionPane.showMessageDialog(frame, "Origen y destino no pueden ser iguales.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Train trenSel = (Train) cmbTren.getSelectedItem();
                    RouteService.getInstance().create(
                            trenSel.getId(), origen.getId(), destino.getId(),
                            Timestamp.valueOf(salida.getText().trim()),
                            Timestamp.valueOf(llegada.getText().trim())
                    );
                    loadRoutes(model);
                }
            } catch (Exception ex) {
                showError(ex);
            }
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(frame, "Selecciona una ruta.");
                return;
            }

            JTextField salida = new JTextField(model.getValueAt(row, 4).toString());
            JTextField llegada = new JTextField(model.getValueAt(row, 5).toString());
            Object[] fields = {"Fecha Salida:", salida, "Fecha Llegada:", llegada};

            int r = JOptionPane.showConfirmDialog(frame, fields, "Editar Ruta", JOptionPane.OK_CANCEL_OPTION);
            if (r == JOptionPane.OK_OPTION) {
                try {
                    RouteService.getInstance().update(
                            model.getValueAt(row, 0).toString(),
                            Timestamp.valueOf(salida.getText().trim()),
                            Timestamp.valueOf(llegada.getText().trim())
                    );
                    loadRoutes(model);
                } catch (Exception ex) {
                    showError(ex);
                }
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(frame, "Selecciona una ruta.");
                return;
            }

            String id = model.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(frame, "¿Eliminar ruta " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    RouteService.getInstance().delete(id);
                    loadRoutes(model);
                } catch (Exception ex) {
                    showError(ex);
                }
            }
        });

        return buildTabPanel(table, buttons);
    }

    private void loadRoutes(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            RouteService.getInstance().getAll().forEach(r -> {
                model.addRow(new Object[]{
                        r.getId(),
                        r.getTrainName(),
                        r.getOriginName(),
                        r.getDestinationName(),
                        r.getDateOfLeaving(),
                        r.getDateOfArrival()
                });
                return null;
            });
        } catch (Exception ex) {
            showError(ex);
        }
    }

    // ─── VAGONES — CRUD completo ───────────────────────────────────
    private JPanel buildWagonTab() {
        String[] cols = {"ID", "ID Tren", "Tipo", "Capacidad"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = styledTable(model);
        loadWagons(model);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        buttons.setBackground(BG);

        JButton btnAdd = actionButton("+ Agregar", new Color(34, 139, 80));
        JButton btnEdit = actionButton("✏ Editar", new Color(30, 58, 95));
        JButton btnDelete = actionButton("🗑 Eliminar", new Color(200, 50, 50));
        JButton btnRefresh = actionButton("↻ Actualizar", new Color(100, 110, 125));

        buttons.add(btnAdd);
        buttons.add(btnEdit);
        buttons.add(btnDelete);
        buttons.add(btnRefresh);

        btnRefresh.addActionListener(e -> loadWagons(model));

        btnAdd.addActionListener(e -> {
            try {
                LinkedList<Train> trenes = TrainService.getInstance().getAll();
                if (trenes.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "No hay trenes registrados.");
                    return;
                }

                JComboBox<Train> cmbTren = new JComboBox<>(trenes.toArray());
                cmbTren.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
                    JLabel lbl = new JLabel(value != null ? value.getId() + " — " + value.getName() : "");
                    if (isSelected) {
                        lbl.setOpaque(true);
                        lbl.setBackground(list.getSelectionBackground());
                    }
                    return lbl;
                });

                JComboBox<String> tipo = new JComboBox<>(new String[]{"PASAJEROS", "EQUIPAJE"});
                JTextField capacidad = new JTextField("32");

                // Ajusta la capacidad por defecto según el tipo seleccionado
                tipo.addActionListener(ev -> {
                    if ("PASAJEROS".equals(tipo.getSelectedItem())) capacidad.setText("32");
                    else capacidad.setText("0");
                });

                Object[] fields = {"Tren:", cmbTren, "Tipo:", tipo, "Capacidad:", capacidad};
                int r = JOptionPane.showConfirmDialog(frame, fields, "Agregar Vagón", JOptionPane.OK_CANCEL_OPTION);
                if (r == JOptionPane.OK_OPTION) {
                    Train trenSel = (Train) cmbTren.getSelectedItem();
                    WagonService.getInstance().create(
                            Integer.parseInt(trenSel.getId()),
                            (String) tipo.getSelectedItem(),
                            Integer.parseInt(capacidad.getText().trim())
                    );
                    loadWagons(model);
                }
            } catch (Exception ex) {
                showError(ex);
            }
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(frame, "Selecciona un vagón.");
                return;
            }

            int id = Integer.parseInt(model.getValueAt(row, 0).toString());
            JComboBox<String> tipo = new JComboBox<>(new String[]{"PASAJEROS", "EQUIPAJE"});
            tipo.setSelectedItem(model.getValueAt(row, 2).toString());
            JTextField capacidad = new JTextField(model.getValueAt(row, 3).toString());
            Object[] fields = {"Tipo:", tipo, "Capacidad:", capacidad};
            int r = JOptionPane.showConfirmDialog(frame, fields, "Editar Vagón", JOptionPane.OK_CANCEL_OPTION);
            if (r == JOptionPane.OK_OPTION) {
                try {
                    WagonService.getInstance().update(id,
                            (String) tipo.getSelectedItem(),
                            Integer.parseInt(capacidad.getText().trim())
                    );
                    loadWagons(model);
                } catch (Exception ex) {
                    showError(ex);
                }
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(frame, "Selecciona un vagón.");
                return;
            }

            int id = Integer.parseInt(model.getValueAt(row, 0).toString());
            int confirm = JOptionPane.showConfirmDialog(frame, "¿Eliminar vagón " + id + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    WagonService.getInstance().delete(id);
                    loadWagons(model);
                } catch (Exception ex) {
                    showError(ex);
                }
            }
        });

        return buildTabPanel(table, buttons);
    }

    private void loadWagons(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            String sql = "SELECT id_vagon, id_tren, tipo, capacidad FROM vagon";
            var rs = DatabaseConnection.getConnection().createStatement().executeQuery(sql);
            while (rs.next())
                model.addRow(new Object[]{rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getInt(4)});
        } catch (Exception ex) {
            showError(ex);
        }
    }

    // ─── PASAJEROS — ver y editar, sin eliminar ────────────────────
    private JPanel buildPassengerTab() {
        String[] cols = {"Identificación", "Nombres", "Apellidos", "Tipo ID", "Dirección"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = styledTable(model);
        loadPassengers(model);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        buttons.setBackground(BG);

        JButton btnEdit = actionButton("✏ Editar", new Color(30, 58, 95));
        JButton btnRefresh = actionButton("↻ Actualizar", new Color(100, 110, 125));
        buttons.add(btnEdit);
        buttons.add(btnRefresh);

        btnRefresh.addActionListener(e -> loadPassengers(model));

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(frame, "Selecciona un pasajero.");
                return;
            }

            String id = model.getValueAt(row, 0).toString();
            JTextField nombres = new JTextField(model.getValueAt(row, 1).toString());
            JTextField apellidos = new JTextField(model.getValueAt(row, 2).toString());
            JComboBox<String> tipoId = new JComboBox<>(new String[]{"CC", "TI", "CE"});
            tipoId.setSelectedItem(model.getValueAt(row, 3).toString());
            JTextField direccion = new JTextField(model.getValueAt(row, 4).toString());
            Object[] fields = {"Nombres:", nombres, "Apellidos:", apellidos,
                    "Tipo ID:", tipoId, "Dirección:", direccion};
            int r = JOptionPane.showConfirmDialog(frame, fields, "Editar Pasajero", JOptionPane.OK_CANCEL_OPTION);
            if (r == JOptionPane.OK_OPTION) {
                try {
                    UserService.getInstance().updatePassenger(id,
                            nombres.getText().trim(), apellidos.getText().trim(),
                            (String) tipoId.getSelectedItem(), direccion.getText().trim());
                    loadPassengers(model);
                } catch (Exception ex) {
                    showError(ex);
                }
            }
        });

        return buildTabPanel(table, buttons);
    }

    private void loadPassengers(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            UserService.getInstance().getAllPassengers().forEach(p -> {
                String[] parts = p.getFullName().split(" ", 2);
                model.addRow(new Object[]{
                        p.getIdentificacion(), parts[0],
                        parts.length > 1 ? parts[1] : "",
                        p.getIdentificationType(), p.getAddress()
                });
                return null;
            });
        } catch (Exception ex) {
            showError(ex);
        }
    }

    // ─── EDITAR MI PERFIL ──────────────────────────────────────────
    private void showEditProfileDialog(AbstractUserWithPower employee) {
        String[] parts = employee.getFullName().split(" ", 2);
        JTextField nombres = new JTextField(parts[0]);
        JTextField apellidos = new JTextField(parts.length > 1 ? parts[1] : "");
        JComboBox<String> tipoId = new JComboBox<>(new String[]{"CC", "TI", "CE"});
        tipoId.setSelectedItem(employee.getIdentificationType());
        JPasswordField pass = new JPasswordField();
        JPasswordField confirmPass = new JPasswordField();

        Object[] fields = {"Nombres:", nombres, "Apellidos:", apellidos,
                "Tipo ID:", tipoId, "Nueva contraseña:", pass, "Confirmar contraseña:", confirmPass};

        int r = JOptionPane.showConfirmDialog(frame, fields, "Editar mi perfil", JOptionPane.OK_CANCEL_OPTION);
        if (r == JOptionPane.OK_OPTION) {
            String p1 = new String(pass.getPassword());
            String p2 = new String(confirmPass.getPassword());
            if (!p1.equals(p2)) {
                JOptionPane.showMessageDialog(frame, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (p1.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "La contraseña no puede estar vacía.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                UserService.getInstance().updateEmployee(
                        employee.getIdentificacion(),
                        nombres.getText().trim(), apellidos.getText().trim(),
                        (String) tipoId.getSelectedItem()
                );
                JOptionPane.showMessageDialog(frame, "Perfil actualizado correctamente.");
            } catch (Exception ex) {
                showError(ex);
            }
        }
    }

    // ─── HELPERS ──────────────────────────────────────────────────
    private JPanel buildTabPanel(JTable table, JPanel buttons) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(12, 16, 12, 16));
        panel.add(buttons, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private JTable styledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(DARK_GREEN);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(190, 230, 210));
        table.setGridColor(new Color(220, 225, 230));
        table.setShowGrid(true);
        return table;
    }

    private JButton actionButton(String text, Color color) {
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

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    }
}