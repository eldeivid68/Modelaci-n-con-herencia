class Automovil extends Vehiculo {
    private int pasajeros;
    private boolean automatico;

    public Automovil(String placa, String marca, String modelo, double tarifaDiaria,
                     int pasajeros, boolean automatico) {
        super(placa, marca, modelo, tarifaDiaria);
        if (pasajeros <= 0) {
            throw new IllegalArgumentException("La cantidad de pasajeros debe ser mayor que cero.");
        }

        this.pasajeros = pasajeros;
        this.automatico = automatico;
    }

    @Override
    public double calcularCosto(int dias) {
        // Si es automatico, aqui se le suma el extra diario que pide el caso.
        double recargo = automatico ? 50.0 * dias : 0.0;
        return (getTarifaDiaria() * dias) + recargo;
    }

    @Override
    public String getCategoria() {
        return "Automovil";
    }

    @Override
    public String getDetallesCategoria() {
        String transmision = automatico ? "automatico" : "manual";
        return pasajeros + " pasajeros, transmision " + transmision;
    }
}

class Motocicleta extends Vehiculo {
    private int cilindraje;

    public Motocicleta(String placa, String marca, String modelo, double tarifaDiaria, int cilindraje) {
        super(placa, marca, modelo, tarifaDiaria);
        if (cilindraje <= 0) {
            throw new IllegalArgumentException("El cilindraje debe ser mayor que cero.");
        }

        this.cilindraje = cilindraje;
    }

    @Override
    public double calcularCosto(int dias) {
        // El extra de moto grande se cobra una vez, no por dia.
        double recargo = cilindraje > 250 ? 75.0 : 0.0;
        return (getTarifaDiaria() * dias) + recargo;
    }

    @Override
    public String getCategoria() {
        return "Motocicleta";
    }

    @Override
    public String getDetallesCategoria() {
        return cilindraje + " cc";
    }
}

class CamionetaCarga extends Vehiculo {
    private double toneladasCapacidad;

    public CamionetaCarga(String placa, String marca, String modelo, double tarifaDiaria,
                          double toneladasCapacidad) {
        super(placa, marca, modelo, tarifaDiaria);
        if (toneladasCapacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero.");
        }

        this.toneladasCapacidad = toneladasCapacidad;
    }

    @Override
    public double calcularCosto(int dias) {
        // Se cobra por la capacidad maxima, aunque el cliente lleve menos carga.
        double recargo = 100.0 * toneladasCapacidad * dias;
        return (getTarifaDiaria() * dias) + recargo;
    }

    @Override
    public String getCategoria() {
        return "Camioneta de carga";
    }

    @Override
    public String getDetallesCategoria() {
        return String.format("%.2f toneladas de capacidad", toneladasCapacidad);
    }
}
