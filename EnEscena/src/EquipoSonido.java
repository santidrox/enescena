import java.math.BigDecimal;

public class EquipoSonido extends Equipo {
    private final BigDecimal potencia;

    public EquipoSonido(String codigo, String marca, String modelo, BigDecimal tarifaDiaria,
                        BigDecimal potencia) {
        super(codigo, marca, modelo, tarifaDiaria);
        if (potencia == null || potencia.signum() <= 0) {
            throw new IllegalArgumentException("La potencia debe ser positiva.");
        }
        this.potencia = potencia;
    }

    @Override
    protected BigDecimal calcularCosto(int dias) {
        return getTarifaDiaria().add(potencia.multiply(new BigDecimal("100")))
                .multiply(BigDecimal.valueOf(dias));
    }

    @Override
    public String getCategoria() { return "Equipo de sonido"; }
    @Override
    protected String getCaracteristicas() { return "Potencia nominal: " + potencia.toPlainString() + " kW"; }
}
