import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

/** Datos comunes de un equipo. Las subclases definen el cobro particular. */
public abstract class Equipo {
    private final String codigo;
    private final String marca;
    private final String modelo;
    private final BigDecimal tarifaDiaria;
    private boolean disponible;

    public Equipo(String codigo, String marca, String modelo, BigDecimal tarifaDiaria) {
        this.codigo = normalizarCodigo(codigo);
        if (marca == null || marca.trim().isEmpty() || modelo == null || modelo.trim().isEmpty()) {
            throw new IllegalArgumentException("La marca y el modelo no pueden estar vacios.");
        }
        if (tarifaDiaria == null || tarifaDiaria.signum() <= 0) {
            throw new IllegalArgumentException("La tarifa debe ser mayor que cero.");
        }
        this.marca = marca.trim();
        this.modelo = modelo.trim();
        this.tarifaDiaria = tarifaDiaria;
        this.disponible = true;
    }

    public static String normalizarCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El codigo no puede estar vacio.");
        }
        return codigo.trim().toUpperCase(Locale.ROOT);
    }

    public String getCodigo() { return codigo; }
    public boolean isDisponible() { return disponible; }
    protected BigDecimal getTarifaDiaria() { return tarifaDiaria; }

    /** Redondea una sola vez, al terminar el calculo de todo el alquiler. */
    public final BigDecimal cotizar(int dias) {
        if (dias <= 0) throw new IllegalArgumentException("Los dias deben ser enteros positivos.");
        return calcularCosto(dias).setScale(2, RoundingMode.HALF_UP);
    }

    protected abstract BigDecimal calcularCosto(int dias);
    public abstract String getCategoria();
    protected abstract String getCaracteristicas();

    // Acceso de paquete: la interfaz usa GestorAlquileres para cambiar el estado.
    void alquilar() {
        if (!disponible) throw new IllegalStateException("El equipo ya esta alquilado.");
        disponible = false;
    }

    void devolver() {
        if (disponible) throw new IllegalStateException("El equipo ya esta disponible.");
        disponible = true;
    }

    @Override
    public String toString() {
        return codigo + " | " + getCategoria() + " | " + marca + " " + modelo
            + " | Tarifa Q" + tarifaDiaria.setScale(2, RoundingMode.HALF_UP).toPlainString()
            + " | " + getCaracteristicas() + " | " + (disponible ? "Disponible" : "Alquilado");
    }
}
