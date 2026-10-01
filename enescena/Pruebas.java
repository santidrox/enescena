import java.math.BigDecimal;

public class Pruebas {
    private static int aprobadas = 0;
    private static BigDecimal n(String valor) { return new BigDecimal(valor); }
    private static void igual(String caso, Object esperado, Object obtenido) {
        if (!esperado.equals(obtenido)) throw new AssertionError(caso + ": " + obtenido);
        aprobadas++;
        System.out.println("OK | " + caso + " | esperado=" + esperado + " | obtenido=" + obtenido);
    }
    private static void rechaza(String caso, Runnable accion) {
        try { accion.run(); }
        catch (IllegalArgumentException | IllegalStateException e) {
            igual(caso, "Rechazado", "Rechazado");
            return;
        }
        throw new AssertionError("No rechazo: " + caso);
    }
    public static void main(String[] args) {
        GestorAlquileres g = new GestorAlquileres();
        g.registrar(new Proyector("P001", "Epson", "X49", n("150"), 3600, false));
        g.registrar(new Proyector("P002", "BenQ", "EW600", n("150"), 3600, true));
        g.registrar(new CamaraVideo("C001", "Canon", "Vixia", n("100"), 1080));
        g.registrar(new CamaraVideo("C002", "Sony", "AX43", n("100"), 2160));
        g.registrar(new EquipoSonido("S001", "JBL", "EON", n("200"), n("1.5")));
        g.registrar(new EquipoSonido("S002", "Yamaha", "Stagepas", n("120"), n("0.5")));
        igual("Inventario inicial", 6, g.listar().size());
        igual("Disponibles iniciales", 6L, g.listar().stream().filter(Equipo::isDisponible).count());
        igual("Ingresos iniciales", n("0.00"), g.getIngresos());
        String[] codigos = {"P001", "P002", "C001", "C002", "S001", "S002"};
        String[] costos = {"450.00", "600.00", "300.00", "375.00", "1050.00", "510.00"};
        for (int i = 0; i < codigos.length; i++) igual("Cotizar " + codigos[i] + " / 3 dias", n(costos[i]), g.cotizar(codigos[i], 3));
        igual("Camara 2160 / 1 dia", n("175.00"), g.cotizar("C002", 1));
        igual("Cotizar no cobra", n("0.00"), g.getIngresos());
        igual("Cotizar no ocupa", true, g.buscar("S001").isDisponible());
        igual("Cancelar devuelve cero", n("0.00"), g.confirmarAlquiler("S001", 3, false));
        igual("Cancelar no cobra", n("0.00"), g.getIngresos());
        igual("Cancelar no ocupa", true, g.buscar("S001").isDisponible());
        igual("Confirmar S001", n("1050.00"), g.confirmarAlquiler("S001", 3, true));
        igual("Confirmado ocupado", false, g.buscar("S001").isDisponible());
        igual("Ingreso confirmado", n("1050.00"), g.getIngresos());
        igual("Cotizar ocupado permitido", n("700.00"), g.cotizar("S001", 2));
        rechaza("Doble alquiler", () -> g.confirmarAlquiler("S001", 1, true));
        igual("Fallo no cobra", n("1050.00"), g.getIngresos());
        igual("Reporte por categoria", true, g.reporte().contains("Equipo de sonido | 2 | 1 | 1"));
        igual("Reporte total", true, g.reporte().contains("TOTAL | 6 | 5 | 1"));
        g.devolver("S001");
        igual("Devolucion libera", true, g.buscar("S001").isDisponible());
        igual("Devolucion conserva ingreso", n("1050.00"), g.getIngresos());
        rechaza("Devolver disponible", () -> g.devolver("S001"));
        rechaza("Codigo inexistente", () -> g.cotizar("X", 1));
        rechaza("Devolver inexistente", () -> g.devolver("X"));
        rechaza("Alquilar inexistente", () -> g.confirmarAlquiler("X", 1, true));
        rechaza("Dias cero", () -> g.cotizar("P001", 0));
        rechaza("Dias negativos", () -> g.confirmarAlquiler("P001", -1, true));
        rechaza("Codigo repetido normalizado", () -> g.registrar(new CamaraVideo(" p001 ", "A", "B", n("10"), 720)));
        rechaza("Codigo vacio", () -> new CamaraVideo(" ", "A", "B", n("10"), 720));
        rechaza("Tarifa cero", () -> new CamaraVideo("X", "A", "B", n("0"), 720));
        rechaza("Tarifa negativa", () -> new CamaraVideo("X", "A", "B", n("-1"), 720));
        rechaza("Lumenes cero", () -> new Proyector("X", "A", "B", n("1"), 0, true));
        rechaza("Resolucion cero", () -> new CamaraVideo("X", "A", "B", n("1"), 0));
        rechaza("Potencia negativa", () -> new EquipoSonido("X", "A", "B", n("1"), n("-0.5")));
        rechaza("Marca vacia", () -> new CamaraVideo("X", " ", "B", n("1"), 720));
        igual("Rechazos conservan cantidad", 6, g.listar().size());
        igual("Rechazos conservan ingreso", n("1050.00"), g.getIngresos());
        igual("Rechazos conservan disponibilidad", 6L, g.listar().stream().filter(Equipo::isDisponible).count());
        igual("Buscar normaliza codigo", "P001", g.buscar(" p001 ").getCodigo());
        igual("Realquiler permitido", n("350.00"), g.confirmarAlquiler("S001", 1, true));
        igual("Ingreso acumulado", n("1400.00"), g.getIngresos());
        g.registrar(new CamaraVideo("C003", "A", "B", n("80"), 720));
        igual("Registro posterior", 7, g.listar().size());
        igual("Camara 720 sin cargo", n("160.00"), g.cotizar("C003", 2));
        igual("Redondeo monetario final", n("1.01"), new CamaraVideo("R", "A", "B", n("1.005"), 720).cotizar(1));
        System.out.println("RESULTADO: " + aprobadas + " pruebas aprobadas.");
    }
}
