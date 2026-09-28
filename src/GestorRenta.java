import java.util.ArrayList;
import java.util.List;

class GestorRenta {
    private List<Vehiculo> vehiculos;
    private double ingresosAcumulados;

    public GestorRenta() {
        vehiculos = new ArrayList<>();
        ingresosAcumulados = 0.0;
    }

    public boolean registrarVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null || buscarPorPlaca(vehiculo.getPlaca()) != null) {
            return false;
        }

        vehiculos.add(vehiculo);
        return true;
    }

    public Vehiculo buscarPorPlaca(String placa) {
        if (placa == null) {
            return null;
        }

        String placaBuscada = placa.trim().toUpperCase();
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getPlaca().equals(placaBuscada)) {
                return vehiculo;
            }
        }
        return null;
    }

    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public double cotizar(String placa, int dias) {
        validarDias(dias);
        Vehiculo vehiculo = buscarPorPlaca(placa);
        if (vehiculo == null) {
            return -1.0;
        }
        return vehiculo.calcularCosto(dias);
    }

    public double confirmarAlquiler(String placa, int dias) {
        validarDias(dias);
        Vehiculo vehiculo = buscarPorPlaca(placa);
        if (vehiculo == null || !vehiculo.isDisponible()) {
            return -1.0;
        }

        // Solo aqui se toca el dinero, porque cotizar no deberia sumar nada.
        double total = vehiculo.calcularCosto(dias);
        vehiculo.alquilar();
        ingresosAcumulados += total;
        return total;
    }

    public boolean registrarDevolucion(String placa) {
        Vehiculo vehiculo = buscarPorPlaca(placa);
        if (vehiculo == null || vehiculo.isDisponible()) {
            return false;
        }

        vehiculo.devolver();
        return true;
    }

    public double getIngresosAcumulados() {
        return ingresosAcumulados;
    }

    public int contarDisponibles() {
        int total = 0;
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.isDisponible()) {
                total++;
            }
        }
        return total;
    }

    public int contarAlquilados() {
        return vehiculos.size() - contarDisponibles();
    }

    public int contarPorCategoria(String categoria, boolean disponibles) {
        int total = 0;
        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getCategoria().equals(categoria) && vehiculo.isDisponible() == disponibles) {
                total++;
            }
        }
        return total;
    }

    private void validarDias(int dias) {
        if (dias <= 0) {
            throw new IllegalArgumentException("Los dias de alquiler deben ser enteros positivos.");
        }
    }
}
