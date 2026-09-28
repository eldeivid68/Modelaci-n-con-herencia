import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static GestorRenta gestor = new GestorRenta();

    public static void main(String[] args) {
        cargarDatosIniciales();
        mostrarMenu();
    }

    private static void cargarDatosIniciales() {
        // Datos de prueba para que al correrlo ya se miren todos los cobros.
        gestor.registrarVehiculo(new Automovil("A001ABC", "Toyota", "Corolla", 180.00, 5, true));
        gestor.registrarVehiculo(new Automovil("A002ABC", "Honda", "Civic", 160.00, 5, false));
        gestor.registrarVehiculo(new Motocicleta("M001ABC", "Yamaha", "FZ", 90.00, 150));
        gestor.registrarVehiculo(new Motocicleta("M002ABC", "Kawasaki", "Ninja", 140.00, 400));
        gestor.registrarVehiculo(new CamionetaCarga("C001ABC", "Nissan", "NP300", 200.00, 1.5));
        gestor.registrarVehiculo(new CamionetaCarga("C002ABC", "Isuzu", "NPR", 260.00, 3.0));
    }

    private static void mostrarMenu() {
        int opcion;
        do {
            System.out.println("\n========== RentaMovil ==========");
            System.out.println("1. Registrar vehiculo");
            System.out.println("2. Consultar flota");
            System.out.println("3. Cotizar alquiler");
            System.out.println("4. Confirmar alquiler");
            System.out.println("5. Registrar devolucion");
            System.out.println("6. Reporte general");
            System.out.println("0. Salir");
            opcion = leerEntero("Elige una opcion: ");

            switch (opcion) {
                case 1:
                    registrarVehiculo();
                    break;
                case 2:
                    consultarFlota();
                    break;
                case 3:
                    cotizarAlquiler();
                    break;
                case 4:
                    confirmarAlquiler();
                    break;
                case 5:
                    registrarDevolucion();
                    break;
                case 6:
                    mostrarReporteGeneral();
                    break;
                case 0:
                    System.out.println("Saliendo del sistema. Hasta luego.");
                    break;
                default:
                    System.out.println("Opcion invalida. Intenta otra vez.");
                    break;
            }
        } while (opcion != 0);
    }

    private static void registrarVehiculo() {
        System.out.println("\n--- Registrar vehiculo ---");
        System.out.println("1. Automovil");
        System.out.println("2. Motocicleta");
        System.out.println("3. Camioneta de carga");
        int tipo = leerEntero("Tipo de vehiculo: ");

        String placa = leerTexto("Placa: ").toUpperCase();
        if (placa.isEmpty()) {
            System.out.println("No se aceptan placas vacias.");
            return;
        }
        // La placa es como el DPI del vehiculo: no deberia repetirse.
        if (gestor.buscarPorPlaca(placa) != null) {
            System.out.println("Ya existe un vehiculo con esa placa.");
            return;
        }

        String marca = leerTexto("Marca: ");
        String modelo = leerTexto("Modelo: ");
        double tarifa = leerDouble("Tarifa diaria: Q");

        try {
            Vehiculo vehiculo;
            if (tipo == 1) {
                int pasajeros = leerEntero("Cantidad de pasajeros: ");
                boolean automatico = leerSiNo("Tiene transmision automatica? (s/n): ");
                vehiculo = new Automovil(placa, marca, modelo, tarifa, pasajeros, automatico);
            } else if (tipo == 2) {
                int cilindraje = leerEntero("Cilindraje en cc: ");
                vehiculo = new Motocicleta(placa, marca, modelo, tarifa, cilindraje);
            } else if (tipo == 3) {
                double capacidad = leerDouble("Capacidad maxima en toneladas: ");
                vehiculo = new CamionetaCarga(placa, marca, modelo, tarifa, capacidad);
            } else {
                System.out.println("Tipo de vehiculo invalido.");
                return;
            }

            if (gestor.registrarVehiculo(vehiculo)) {
                System.out.println("Vehiculo registrado correctamente.");
            } else {
                System.out.println("No se pudo registrar el vehiculo.");
            }
        } catch (IllegalArgumentException error) {
            System.out.println("Registro rechazado: " + error.getMessage());
        }
    }

    private static void consultarFlota() {
        System.out.println("\n--- Flota registrada ---");
        for (Vehiculo vehiculo : gestor.getVehiculos()) {
            System.out.println(vehiculo.getCategoria() + " -> " + vehiculo.getResumen());
        }
    }

    private static void cotizarAlquiler() {
        System.out.println("\n--- Cotizar alquiler ---");
        String placa = leerTexto("Placa: ");
        int dias = leerEntero("Dias de alquiler: ");
        Vehiculo vehiculo = gestor.buscarPorPlaca(placa);

        if (vehiculo == null) {
            System.out.println("No existe un vehiculo con esa placa.");
            return;
        }

        try {
            double total = gestor.cotizar(placa, dias);
            System.out.println("Vehiculo: " + vehiculo.getResumen());
            System.out.printf("Cotizacion por %d dias: Q%.2f%n", dias, total);
            // Importante: preguntar precio no significa que ya se alquilo.
            System.out.println("Esta cotizacion no modifica disponibilidad ni ingresos.");
        } catch (IllegalArgumentException error) {
            System.out.println("Cotizacion rechazada: " + error.getMessage());
        }
    }

    private static void confirmarAlquiler() {
        System.out.println("\n--- Confirmar alquiler ---");
        String placa = leerTexto("Placa: ");
        int dias = leerEntero("Dias de alquiler: ");
        Vehiculo vehiculo = gestor.buscarPorPlaca(placa);

        if (vehiculo == null) {
            System.out.println("No existe un vehiculo con esa placa.");
            return;
        }
        if (!vehiculo.isDisponible()) {
            System.out.println("El vehiculo esta ocupado. No se puede alquilar dos veces al mismo tiempo.");
            return;
        }

        try {
            double total = gestor.cotizar(placa, dias);
            System.out.printf("Total a cobrar: Q%.2f%n", total);
            boolean acepta = leerSiNo("Confirmar alquiler? (s/n): ");
            if (!acepta) {
                // Si el cliente se echa para atras, todo se queda como estaba.
                System.out.println("Operacion cancelada. No se modificaron disponibilidad ni ingresos.");
                return;
            }

            double cobrado = gestor.confirmarAlquiler(placa, dias);
            System.out.printf("Alquiler confirmado. Ingreso registrado: Q%.2f%n", cobrado);
        } catch (IllegalArgumentException error) {
            System.out.println("Alquiler rechazado: " + error.getMessage());
        }
    }

    private static void registrarDevolucion() {
        System.out.println("\n--- Registrar devolucion ---");
        String placa = leerTexto("Placa: ");
        Vehiculo vehiculo = gestor.buscarPorPlaca(placa);

        if (vehiculo == null) {
            System.out.println("No existe un vehiculo con esa placa.");
            return;
        }

        if (gestor.registrarDevolucion(placa)) {
            System.out.println("Devolucion registrada. El vehiculo vuelve a estar disponible.");
        } else {
            System.out.println("No se puede devolver un vehiculo que ya esta disponible.");
        }
    }

    private static void mostrarReporteGeneral() {
        System.out.println("\n--- Reporte general ---");
        System.out.println("Vehiculos registrados: " + gestor.getVehiculos().size());
        System.out.println("Disponibles: " + gestor.contarDisponibles());
        System.out.println("Alquilados: " + gestor.contarAlquilados());
        imprimirCategoria("Automovil");
        imprimirCategoria("Motocicleta");
        imprimirCategoria("Camioneta de carga");
        System.out.printf("Ingresos acumulados: Q%.2f%n", gestor.getIngresosAcumulados());
    }

    private static void imprimirCategoria(String categoria) {
        int disponibles = gestor.contarPorCategoria(categoria, true);
        int alquilados = gestor.contarPorCategoria(categoria, false);
        System.out.printf("%s -> disponibles: %d, alquilados: %d%n", categoria, disponibles, alquilados);
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        // Este ciclo evita que el programa truene si alguien escribe letras.
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException error) {
                System.out.println("Entrada invalida. Debes escribir un numero entero.");
            }
        }
    }

    private static double leerDouble(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();
            try {
                return Double.parseDouble(entrada);
            } catch (NumberFormatException error) {
                System.out.println("Entrada invalida. Debes escribir un numero.");
            }
        }
    }

    private static boolean leerSiNo(String mensaje) {
        while (true) {
            String respuesta = leerTexto(mensaje).toLowerCase();
            if (respuesta.equals("s") || respuesta.equals("si")) {
                return true;
            }
            if (respuesta.equals("n") || respuesta.equals("no")) {
                return false;
            }
            System.out.println("Responde con s o n.");
        }
    }
}
