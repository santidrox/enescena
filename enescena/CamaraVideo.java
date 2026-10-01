import java.math.BigDecimal;

public class CamaraVideo extends Equipo {
    private final int resolucion;

    public CamaraVideo(String codigo, String marca, String modelo, BigDecimal tarifaDiaria,
                       int resolucion) {
        super(codigo, marca, modelo, tarifaDiaria);
        if (resolucion <= 0) throw new IllegalArgumentException("La resolucion debe ser positiva.");
        this.resolucion = resolucion;
    }

    @Override
    protected BigDecimal calcularCosto(int dias) {
        BigDecimal adicional = resolucion > 1080 ? new BigDecimal("75") : BigDecimal.ZERO;
        return getTarifaDiaria().multiply(BigDecimal.valueOf(dias)).add(adicional);
    }

    @Override
    public String getCategoria() { return "Camara de video"; }
    @Override
    protected String getCaracteristicas() { return "Resolucion: " + resolucion + " pixeles verticales"; }
}
