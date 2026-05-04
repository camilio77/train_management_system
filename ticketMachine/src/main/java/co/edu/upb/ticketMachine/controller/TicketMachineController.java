package co.edu.upb.ticketMachine.controller;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import co.edu.upb.app.LinkedList.singly.LinkedList;
import co.edu.upb.ticketMachine.model.TicketMachineModel;
import co.edu.upb.ticketMachine.view.TicketMachineView;
import co.edu.upb.ticketMachine.view.UserDataView;
import co.edu.upb.train_management_system.model.route.Route;
import co.edu.upb.train_management_system.model.station.Station;
import co.edu.upb.train_management_system.model.ticket.Ticket;

public class TicketMachineController {

    private final TicketMachineModel model;
    private final TicketMachineView  view;
    private final String             userId;
    private final String             userName;

    public TicketMachineController(TicketMachineModel model, TicketMachineView view,
                                   String userId, String userName) {
        this.model    = model;
        this.view     = view;
        this.userId   = userId;
        this.userName = userName;
    }

    public void init() {
        loadStations();
        bindEvents();
    }

    private void bindEvents() {
        view.onComprar(this::handleComprar);
        view.onVolver(() -> {
            view.close();
            UserDataView userDataView = new UserDataView();
            UserDataController ctrl   = new UserDataController(model, userDataView);
            ctrl.init();
        });
    }

    private void loadStations() {
        try {
            LinkedList<Station> estaciones = model.getStationService().getAll();
            List<String> nombres = new ArrayList<>();
            estaciones.forEach(s -> { nombres.add(s.getName()); return null; });

            LinkedList<Route> rutas = model.getRouteService().getAll();
            List<int[]> connections = new ArrayList<>();
            rutas.forEach(r -> {
                String oName = r.getOriginName();
                String dName = r.getDestinationName();
                if (oName == null || dName == null) return null;
                int a = nombres.indexOf(oName);
                int b = nombres.indexOf(dName);
                if (a >= 0 && b >= 0) connections.add(new int[]{a, b});
                return null;
            });

            view.loadStations(nombres, connections);

        } catch (Exception ex) {
            view.showError("Error cargando estaciones: " + ex.getMessage());
        }
    }

    private void handleComprar() {
        String origen    = view.getOrigen();
        String destino   = view.getDestino();
        String categoria = view.getCategoria();
        double peso1     = view.getPeso1();
        double peso2     = view.getPeso2();

        if (origen == null || destino == null) {
            view.showError("Selecciona origen y destino en el mapa.");
            return;
        }
        if (origen.equals(destino)) {
            view.showError("El origen y el destino deben ser diferentes.");
            return;
        }
        if (peso1 > 80 || peso2 > 80) {
            view.showError("El peso máximo por maleta es 80 kg.");
            return;
        }

        try {
            LinkedList<String> path = model.getTicketService()
                .findPathByRoutes(origen, destino);

            if (path == null || path.isEmpty()) {
                view.showError("No existe camino entre " + origen + " y " + destino +
                    "\nVerifica que haya rutas registradas que conecten estas estaciones.");
                return;
            }

            List<String> pathList = new ArrayList<>();
            path.forEach(s -> { pathList.add(s); return null; });
            view.highlightPath(pathList);

            StringBuilder rutaStr = new StringBuilder();
            for (int i = 0; i < pathList.size(); i++) {
                rutaStr.append(pathList.get(i));
                if (i < pathList.size() - 1) rutaStr.append(" → ");
            }

            int numTramos = pathList.size() - 1;
            int precioPorTramo = switch (categoria) {
                case "PREMIUM"   -> 150000;
                case "EJECUTIVA" -> 80000;
                default          -> 40000;
            };
            int precioTotal = precioPorTramo * numTramos;

            int confirm = JOptionPane.showConfirmDialog(null,
                "Resumen de tu compra:\n\n"
                + "👤 Pasajero: " + userName + " (ID: " + userId + ")\n"
                + "🛤  Ruta:     " + rutaStr + "\n"
                + "🔀 Tramos:   " + numTramos + "\n"
                + "🎫 Categoría: " + categoria + "\n"
                + "💰 Precio por tramo: $" + String.format("%,d", precioPorTramo).replace(",", ".") + "\n"
                + "💰 Precio total:     $" + String.format("%,d", precioTotal).replace(",", ".")
                + (peso1 > 0 ? "\n🧳 Maleta 1: " + peso1 + " kg" : "")
                + (peso2 > 0 ? "\n🧳 Maleta 2: " + peso2 + " kg" : "")
                + "\n\n¿Confirmar compra?",
                "Confirmar compra", JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION) return;

            double pesoTotal = peso1 + peso2;
            LinkedList<Ticket> tickets = model.getTicketService().buyTicketsAsGuest(
                userId, userName, origen, destino, categoria, pesoTotal);

            if (tickets == null || tickets.isEmpty()) {
                view.showError("No se pudieron generar tickets. "
                    + "Verifica disponibilidad de asientos.");
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("¡Compra exitosa! ").append(tickets.size())
              .append(" ticket(s) generados:\n\n");
            int[] tramo = {1};
            tickets.forEach(t -> {
                String to = t.getRoutes().length > 0 ?
                    t.getRoutes()[0].getOriginName() : "—";
                String td = t.getRoutes().length > 0 ?
                    t.getRoutes()[0].getDestinationName() : "—";
                sb.append("  Tramo ").append(tramo[0]++).append(": ")
                  .append(to).append(" → ").append(td).append("\n")
                  .append("  🎫 Ticket #").append(t.getId())
                  .append("  |  Asiento ").append(t.getNumeroAsiento())
                  .append("  |  $")
                  .append(String.format("%,d", (long) t.getTotal()).replace(",", "."))
                  .append("\n\n");
                return null;
            });
            if (pesoTotal > 0)
                sb.append("🧳 Equipaje total: ").append(pesoTotal).append(" kg");
            sb.append("\n\nGuarda el número de tu ticket para abordaje.");

            view.showSuccess(sb.toString());
            view.close();
            UserDataView userDataView = new UserDataView();
            new UserDataController(model, userDataView).init();

        } catch (Exception ex) {
            view.showError("Error al procesar la compra: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}