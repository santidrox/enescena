import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GestorAlquileres {
    private final Map<String, Equipo> inventario;
    private BigDecimal ingresos;

    public GestorAlquileres() {
        inventario = new LinkedHashMap<>();
        ingresos = new BigDecimal("0.00");
    }

    public void registrar(Equipo equipo) {
        if (equipo == null) throw new IllegalArgumentException("El equipo no puede ser nulo.");
        if (inventario.containsKey(equipo.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un equipo con ese codigo.");
        }
        if (!equipo.isDisponible()) throw new IllegalArgumentException("El equipo nuevo debe estar disponible.");
        inventario.put(equipo.getCodigo(), equipo);
    }

    public Equipo buscar(String codigo) {
        Equipo equipo = inventario.get(Equipo.normalizarCodigo(codigo));
        if (equipo == null) throw new IllegalArgumentException("No existe un equipo con ese codigo.");
        return equipo;
    }

    public List<Equipo> listar() {
        return Collections.unmodifiableList(new ArrayList<>(inventario.values()));
    }

    public BigDecimal cotizar(String codigo, int dias) { return buscar(codigo).cotizar(dias); }

    public BigDecimal confirmarAlquiler(String codigo, int dias, boolean acepta) {
        Equipo equipo = buscar(codigo);
        if (!equipo.isDisponible()) throw new IllegalStateException("El equipo ya esta alquilado.");
        BigDecimal total = equipo.cotizar(dias);
        if (!acepta) return new BigDecimal("0.00");
        equipo.alquilar();
        ingresos = ingresos.add(total);
        return total;
    }

    public void devolver(String codigo) { buscar(codigo).devolver(); }
    public BigDecimal getIngresos() { return ingresos; }

    public String reporte() {
        Map<String, int[]> categorias = new LinkedHashMap<>();
        int disponibles = 0;
        for (Equipo equipo : inventario.values()) {
            int[] datos = categorias.computeIfAbsent(equipo.getCategoria(), clave -> new int[3]);
            datos[0]++;
            if (equipo.isDisponible()) { datos[1]++; disponibles++; }
            else datos[2]++;
        }
        StringBuilder texto = new StringBuilder("Categoria | Total | Disponibles | Alquilados\n");
        for (Map.Entry<String, int[]> fila : categorias.entrySet()) {
            int[] datos = fila.getValue();
            texto.append(fila.getKey()).append(" | ").append(datos[0]).append(" | ")
                 .append(datos[1]).append(" | ").append(datos[2]).append('\n');
        }
        return texto.append("TOTAL | ").append(inventario.size()).append(" | ")
                .append(disponibles).append(" | ").append(inventario.size() - disponibles)
                .append("\nIngresos acumulados: Q").append(ingresos.toPlainString()).toString();
    }
}
