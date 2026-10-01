import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    private final Scanner entrada;
    private final GestorAlquileres gestor;

    public Main() {
        entrada = new Scanner(System.in);
        gestor = new GestorAlquileres();
        cargarEjemplos();
    }

    public static void main(String[] args) { new Main().ejecutar(); }

    private void cargarEjemplos() {
        gestor.registrar(new Proyector("P001", "Epson", "X49", new BigDecimal("150"), 3600, false));
        gestor.registrar(new Proyector("P002", "BenQ", "EW600", new BigDecimal("150"), 3600, true));
        gestor.registrar(new CamaraVideo("C001", "Canon", "Vixia", new BigDecimal("100"), 1080));
        gestor.registrar(new CamaraVideo("C002", "Sony", "AX43", new BigDecimal("100"), 2160));
        gestor.registrar(new EquipoSonido("S001", "JBL", "EON", new BigDecimal("200"), new BigDecimal("1.5")));
        gestor.registrar(new EquipoSonido("S002", "Yamaha", "Stagepas", new BigDecimal("120"), new BigDecimal("0.5")));
    }

    private void ejecutar() {
        boolean seguir = true;
        while (seguir) {
            System.out.println("\nENESCENA\n1. Registrar equipo\n2. Consultar inventario\n3. Cotizar"
                    + "\n4. Alquilar\n5. Devolver\n6. Reporte general\n0. Salir");
            try {
                switch (leerEntero("Opcion: ")) {
                    case 1: registrar(); break;
                    case 2: gestor.listar().forEach(System.out::println); break;
                    case 3: cotizarOAlquilar(false); break;
                    case 4: cotizarOAlquilar(true); break;
                    case 5: gestor.devolver(leerTexto("Codigo: "));
                            System.out.println("Devolucion registrada. No se realiza otro cobro."); break;
                    case 6: System.out.println(gestor.reporte()); break;
                    case 0: seguir = false; break;
                    default: System.out.println("Opcion no valida.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (NoSuchElementException e) {
                System.out.println("\nFin de entrada. Saliendo sin confirmar operaciones pendientes.");
                seguir = false;
            }
        }
        System.out.println("Hasta luego.");
    }

    private void registrar() {
        int tipo = leerEntero("Tipo (1 proyector, 2 camara, 3 sonido): ");
        if (tipo < 1 || tipo > 3) throw new IllegalArgumentException("Categoria no valida.");
        String codigo = leerTexto("Codigo: ");
        String marca = leerTexto("Marca: ");
        String modelo = leerTexto("Modelo: ");
        BigDecimal tarifa = leerDecimal("Tarifa diaria Q: ");
        Equipo equipo;
        if (tipo == 1) {
            int lumenes = leerEntero("Luminosidad (lumenes enteros): ");
            equipo = new Proyector(codigo, marca, modelo, tarifa, lumenes, leerSiNo("Inalambrico (s/n): "));
        } else if (tipo == 2) {
            equipo = new CamaraVideo(codigo, marca, modelo, tarifa, leerEntero("Resolucion vertical: "));
        } else {
            equipo = new EquipoSonido(codigo, marca, modelo, tarifa, leerDecimal("Potencia nominal kW: "));
        }
        gestor.registrar(equipo);
        System.out.println("Equipo registrado y disponible.");
    }

    private void cotizarOAlquilar(boolean alquilar) {
        String codigo = leerTexto("Codigo: ");
        Equipo equipo = gestor.buscar(codigo);
        if (alquilar && !equipo.isDisponible()) throw new IllegalStateException("El equipo ya esta alquilado.");
        int dias = leerEntero("Dias de alquiler: ");
        BigDecimal total = gestor.cotizar(codigo, dias);
        System.out.println(equipo);
        System.out.println("Dias: " + dias + " | Total: Q" + total.toPlainString());
        if (alquilar) {
            boolean acepta = leerSiNo("Confirmar alquiler y cobro (s/n): ");
            gestor.confirmarAlquiler(codigo, dias, acepta);
            System.out.println(acepta ? "Alquiler confirmado. Cobrado Q" + total.toPlainString()
                                    : "Alquiler cancelado. Sin cambios.");
        } else System.out.println("Solo cotizacion. Sin cambios.");
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return entrada.nextLine().trim();
    }

    private int leerEntero(String mensaje) {
        while (true) {
            try { return Integer.parseInt(leerTexto(mensaje)); }
            catch (NumberFormatException e) { System.out.println("Ingrese un numero entero valido."); }
        }
    }

    private BigDecimal leerDecimal(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            if (texto.matches("[+-]?[0-9]+([.,][0-9]+)?")) {
                return new BigDecimal(texto.replace(',', '.'));
            }
            System.out.println("Ingrese un decimal valido, por ejemplo 1.5.");
        }
    }

    private boolean leerSiNo(String mensaje) {
        while (true) {
            String valor = leerTexto(mensaje);
            if (valor.equalsIgnoreCase("s")) return true;
            if (valor.equalsIgnoreCase("n")) return false;
            System.out.println("Responda s o n.");
        }
    }
}
