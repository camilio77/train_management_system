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

    private final ClientModel model;
    private final BuyTicketView view;
    private final Passenger passenger;

    public BuyTicketController(ClientModel model, BuyTicketView view, Passenger passenger) {
        this.model = model;
        this.view = view;
        this.passenger = passenger;

        loadStations();
        view.onBuy(this::handleBuy);
    }

    private void loadStations() {
        try {
            // 1. Obtener estaciones
            LinkedList<Station> estaciones = model.getStationService().getAll();
            List<String> nombresList = new ArrayList<>();
            estaciones.forEach(s -> {
                nombresList.add(s.getName());
                return null;
            });

            // 2. Construir conexiones SOLO desde rutas registradas
            // (solo tramos que tienen una ruta con tren asignado)
            LinkedList<Route> rutas = model.getRouteService().getAll();
            List<int[]> connections = new ArrayList<>();
            rutas.forEach(r -> {
                String oName = r.getOriginName();
                String dName = r.getDestinationName();
                if (oName == null || dName == null)
                    return null;

                int idxA = nombresList.indexOf(oName);
                int idxB = nombresList.indexOf(dName);
                if (idxA >= 0 && idxB >= 0)
                    connections.add(new int[] { idxA, idxB }); // sin km porque no lo tenemos aquí
                return null;
            });

            view.loadStations(nombresList, connections);

        } catch (Exception ex) {
            view.showError("Error cargando estaciones: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void handleBuy() {
        String origen = view.getOrigen();
        String destino = view.getDestino();
        String categoria = view.getCategoria();
        double peso1 = view.getPesoEquipaje1();
        double peso2 = view.getPesoEquipaje2();

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
            // 1. Calcular camino con Dijkstra ANTES de confirmar
            LinkedList<String> path = model.getTicketService().findPathByRoutes(origen, destino);

            if (path == null || path.isEmpty()) {
                view.showError("No existe camino entre " + origen + " y " + destino +
                        "\nVerifica que haya conexiones entre estaciones.");
                return;
            }

            // 2. Mostrar camino en el mapa
            List<String> pathList = new ArrayList<>();
            path.forEach(s -> {
                pathList.add(s);
                return null;
            });
            view.highlightPath(pathList);

            // 3. Calcular distancia total
            int distanciaKm = 0;

            // 4. Construir resumen del camino para confirmación
            StringBuilder rutaStr = new StringBuilder();
            for (int i = 0; i < pathList.size(); i++) {
                rutaStr.append(pathList.get(i));
                if (i < pathList.size() - 1)
                    rutaStr.append(" → ");
            }

            int numTramos = pathList.size() - 1;
            int precioPorTramo = switch (categoria) {
                case "PREMIUM" -> 150000;
                case "EJECUTIVA" -> 80000;
                default -> 40000;
            };
            int precioTotal = precioPorTramo * numTramos;

            int confirm = JOptionPane.showConfirmDialog(null,
                    "Ruta calculada: " + rutaStr +
                            "\nNúmero de tramos: " + numTramos +
                            "\nCategoría: " + categoria +
                            "\nPrecio por tramo: $" + String.format("%,d", precioPorTramo).replace(",", ".") +
                            "\nPrecio total: $" + String.format("%,d", precioTotal).replace(",", ".") +
                            (peso1 > 0 ? "\nMaleta 1: " + peso1 + " kg" : "") +
                            (peso2 > 0 ? "\nMaleta 2: " + peso2 + " kg" : "") +
                            "\n\n¿Confirmar compra?",
                    "Confirmar compra", JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION)
                return;

            double pesoTotal = peso1 + peso2;
            LinkedList<Ticket> tickets = model.getTicketService().buyTickets(
                    passenger.getIdentificacion(),
                    origen, destino, categoria, pesoTotal);

            if (tickets == null || tickets.isEmpty()) {
                view.showError("No se pudieron generar tickets.\n" +
                        "Verifica que haya rutas registradas para cada tramo del camino.");
                return;
            }

            // 6. Mostrar resumen de compra
            StringBuilder sb = new StringBuilder();
            sb.append("¡Compra exitosa! ").append(tickets.size()).append(" ticket(s):\n\n");
            int[] tramo = { 1 };
            tickets.forEach(t -> {
                String to = t.getRoutes().length > 0 ? t.getRoutes()[0].getOriginName() : "—";
                String td = t.getRoutes().length > 0 ? t.getRoutes()[0].getDestinationName() : "—";
                sb.append("Tramo ").append(tramo[0]++).append(": ")
                        .append(to).append(" → ").append(td).append("\n")
                        .append("  Ticket #").append(t.getId())
                        .append(" | Asiento ").append(t.getNumeroAsiento())
                        .append(" | $").append(
                                String.format("%,d", (long) t.getTotal()).replace(",", "."))
                        .append("\n\n");
                return null;
            });
            if (pesoTotal > 0)
                sb.append("Equipaje total: ").append(pesoTotal).append(" kg\n");

            view.showSuccess(sb.toString());
            view.close();

        } catch (Exception ex) {
            view.showError("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}