import java.math.BigDecimal;

public class Proyector extends Equipo {
    private final int luminosidad;
    private final boolean inalambrico;

    public Proyector(String codigo, String marca, String modelo, BigDecimal tarifaDiaria,
                     int luminosidad, boolean inalambrico) {
        super(codigo, marca, modelo, tarifaDiaria);
        if (luminosidad <= 0) throw new IllegalArgumentException("La luminosidad debe ser positiva.");
        this.luminosidad = luminosidad;
        this.inalambrico = inalambrico;
    }

    @Override
    protected BigDecimal calcularCosto(int dias) {
        BigDecimal adicional = inalambrico ? new BigDecimal("50") : BigDecimal.ZERO;
        return getTarifaDiaria().add(adicional).multiply(BigDecimal.valueOf(dias));
    }

    @Override
    public String getCategoria() { return "Proyector"; }
    @Override
    protected String getCaracteristicas() {
        return luminosidad + " lumenes | Inalambrico: " + (inalambrico ? "Si" : "No");
    }
}
