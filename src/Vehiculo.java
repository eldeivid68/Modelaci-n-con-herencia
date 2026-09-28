// Clase base: aqui va lo que todos los vehiculos tienen si o si.
abstract class Vehiculo {
    private String placa;
    private String marca;
    private String modelo;
    private double tarifaDiaria;
    private boolean disponible;

    public Vehiculo(String placa, String marca, String modelo, double tarifaDiaria) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa no puede estar vacia.");
        }
        if (tarifaDiaria <= 0) {
            throw new IllegalArgumentException("La tarifa diaria debe ser mayor que cero.");
        }

        this.placa = placa.trim().toUpperCase();
        this.marca = marca;
        this.modelo = modelo;
        this.tarifaDiaria = tarifaDiaria;
        this.disponible = true;
    }

    public String getPlaca() {
        return placa;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public double getTarifaDiaria() {
        return tarifaDiaria;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void alquilar() {
        disponible = false;
    }

    public void devolver() {
        disponible = true;
    }

    // Cada tipo de vehiculo hace su propio calculo, porque no todos cobran igual.
    public abstract double calcularCosto(int dias);

    public abstract String getCategoria();

    public abstract String getDetallesCategoria();

    public String getEstado() {
        return disponible ? "Disponible" : "Alquilado";
    }

    public String getResumen() {
        return String.format(
                "%s | %s %s | Tarifa: Q%.2f | %s | %s",
                placa, marca, modelo, tarifaDiaria, getEstado(), getDetallesCategoria()
        );
    }
}
