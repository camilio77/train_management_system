package co.edu.upb.client.controller;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import co.edu.upb.app.LinkedList.singly.LinkedList;
import co.edu.upb.client.model.ClientModel;
import co.edu.upb.client.view.BuyTicketView;
import co.edu.upb.train_management_system.model.route.Route;
import co.edu.upb.train_management_system.model.station.Station;
import co.edu.upb.train_management_system.model.ticket.Ticket;
import co.edu.upb.train_management_system.model.user.Passenger;

public class BuyTicketController {

    private final ClientModel   model;
    private final BuyTicketView view;
    private final Passenger     passenger;

    public BuyTicketController(ClientModel model, BuyTicketView view, Passenger passenger) {
        this.model     = model;
        this.view      = view;
        this.passenger = passenger;

        loadStations();
        view.onBuy(() -> handleBuy());
    }

    private void loadStations() {
        try {
            // Cargar estaciones
            LinkedList<Station> estaciones = model.getStationService().getAll();
            List<String> nombresList = new ArrayList<>();
            estaciones.forEach(s -> { nombresList.add(s.getName()); return null; });

            // Cargar rutas para dibujar conexiones en el mapa
            LinkedList<Route> rutas = model.getRouteService().getAll();
            List<int[]> connections = new ArrayList<>();
            rutas.forEach(r -> {
                String oName = r.getOriginName();
                String dName = r.getDestinationName();
                int idxA = nombresList.indexOf(oName);
                int idxB = nombresList.indexOf(dName);
                if (idxA >= 0 && idxB >= 0)
                    connections.add(new int[]{idxA, idxB});
                return null;
            });

            view.loadStations(nombresList, connections);

        } catch (Exception ex) {
            view.showError("Error cargando estaciones: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void handleBuy() {
        String origen    = view.getOrigen();
        String destino   = view.getDestino();
        String categoria = view.getCategoria();
        double peso1     = view.getPesoEquipaje1();
        double peso2     = view.getPesoEquipaje2();

        if (origen == null || destino == null) {
            view.showError("Selecciona origen y destino en el mapa.");
            return;
        }
        if (origen.equals(destino)) {
            view.showError("Origen y destino deben ser diferentes.");
            return;
        }
        if (peso1 > 80 || peso2 > 80) {
            view.showError("El peso máximo por maleta es 80 kg.");
            return;
        }

        try {
            // Verificar que exista una ruta entre origen y destino
            final Route[] rutaEncontrada = {null};
            model.getRouteService().getAll().forEach(r -> {
                if (rutaEncontrada[0] == null
                        && r.getOriginName() != null
                        && r.getDestinationName() != null
                        && r.getOriginName().equals(origen)
                        && r.getDestinationName().equals(destino)) {
                    rutaEncontrada[0] = r;
                }
                return null;
            });

            if (rutaEncontrada[0] == null) {
                view.showError("No existe una ruta directa entre\n"
                    + origen + " → " + destino
                    + "\nVerifica que haya una ruta registrada con ese origen y destino.");
                return;
            }

            // Resaltar en el mapa
            List<String> pathList = new ArrayList<>();
            pathList.add(origen);
            pathList.add(destino);
            view.highlightPath(pathList);

            // Confirmar
            int confirm = JOptionPane.showConfirmDialog(null,
                "Ruta: " + origen + " → " + destino +
                "\nTren: " + rutaEncontrada[0].getTrainName() +
                "\nCategoría: " + categoria +
                (peso1 > 0 ? "\nMaleta 1: " + peso1 + " kg" : "") +
                (peso2 > 0 ? "\nMaleta 2: " + peso2 + " kg" : "") +
                "\n\n¿Confirmar compra?",
                "Confirmar compra", JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION) return;

            // Comprar — usa nombres de estación directamente como pide TicketInterface
            double pesoTotal = peso1 + peso2;
            LinkedList<Ticket> tickets = model.getTicketService().buyTickets(
                passenger.getIdentificacion(),
                origen,
                destino,
                categoria,
                pesoTotal
            );

            if (tickets == null || tickets.isEmpty()) {
                view.showError("No se pudieron generar tickets. Verifica disponibilidad.");
                return;
            }

            // Resumen
            StringBuilder sb = new StringBuilder();
            sb.append("¡Compra exitosa! ").append(tickets.size()).append(" ticket(s):\n\n");
            tickets.forEach(t -> {
                sb.append("Ticket #").append(t.getId())
                  .append(" | Asiento ").append(t.getNumeroAsiento())
                  .append(" | $").append(
                      String.format("%,d", (long) t.getTotal()).replace(",", "."))
                  .append("\n");
                return null;
            });
            if (pesoTotal > 0)
                sb.append("\nEquipaje total: ").append(pesoTotal).append(" kg");

            view.showSuccess(sb.toString());
            view.close();

        } catch (Exception ex) {
            view.showError("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}