# EnEscena - Ejercicio 4: Herencia

Aplicación de consola en Java para registrar, consultar, cotizar, alquilar y devolver proyectores, cámaras de video y equipos de sonido. Mantiene los ingresos y el reporte por categoría durante la ejecución.

## Carpetas del proyecto

| Carpeta | Contenido |
|---|---|
| `src` | Los seis archivos Java de la aplicación, sin comentarios. |
| `bin` | Archivos `.class` generados al compilar. Se pueden regenerar. |
| `pruebas` | Pruebas.java y evidencia de las ejecuciones. |
| `documentacion` | Informe del ejercicio en PDF y Word. |

No se necesitan `lib` ni `data`: se utiliza únicamente la biblioteca estándar de Java y la información se mantiene en memoria.

## Requisitos

JDK 17 o superior, con `java` y `javac` disponibles en la terminal. No requiere bibliotecas externas.

## Compilar y ejecutar

Extraiga el ZIP y abra una terminal en la carpeta `EnEscena`, donde está este README, no dentro de `src`. La carpeta `bin` ya está incluida; el comando `mkdir bin` solo es necesario si se eliminó:

```bash
mkdir bin
javac -encoding UTF-8 -d bin src/*.java
java -cp bin Main
```

Si `bin` ya existe, omita `mkdir bin`. En Windows puede usar PowerShell o CMD.

## Menú

- 1: registrar un proyector, cámara o equipo de sonido.
- 2: mostrar inventario, características, tarifa y disponibilidad.
- 3: cotizar por código y días; también permite cotizar equipos ocupados.
- 4: mostrar el total y pedir `s` para confirmar el alquiler o `n` para cancelar.
- 5: registrar la devolución sin modificar los ingresos.
- 6: mostrar cantidades totales, disponibles y alquiladas por categoría e ingresos.
- 0: salir.

Los códigos se normalizan a mayúsculas y se eliminan espacios en los extremos. Las entradas decimales aceptan punto o coma, sin separadores de miles. Los días, lúmenes y resoluciones deben ser enteros positivos. Se muestran dos decimales monetarios; se redondea el total final con HALF_UP.

## Equipos de demostración

Los modelos son datos ilustrativos, no una ficha técnica comercial.

| Código | Tipo | Tarifa diaria | Característica | Total de 3 días |
|---|---|---:|---|---:|
| P001 | Proyector | Q150.00 | 3600 lúmenes, sin conexión inalámbrica | Q450.00 |
| P002 | Proyector | Q150.00 | 3600 lúmenes, inalámbrico | Q600.00 |
| C001 | Cámara | Q100.00 | 1080 píxeles verticales | Q300.00 |
| C002 | Cámara | Q100.00 | 2160 píxeles verticales | Q375.00 |
| S001 | Sonido | Q200.00 | 1.5 kW | Q1050.00 |
| S002 | Sonido | Q120.00 | 0.5 kW | Q510.00 |

Al iniciar, los seis están disponibles y los ingresos son Q0.00. Los datos no persisten al cerrar.

## Pruebas

```bash
javac -encoding UTF-8 -d bin src/*.java pruebas/Pruebas.java
java -cp bin Pruebas
```

Resultado de referencia: 48 pruebas aprobadas. El programa de pruebas lanza `AssertionError` si una comprobación falla. Las pruebas usan su propio inventario.

La carpeta `pruebas` incluye cuatro entradas de consola y sus salidas reales, además de los resultados del modelo. Para reproducir una sesión en Bash o CMD:

```bash
java -cp bin Main < pruebas/01-flujo-entrada.txt
```

En PowerShell:

```powershell
Get-Content pruebas/01-flujo-entrada.txt | java -cp bin Main
```

Repita con los archivos 02, 03 y 04. El PDF contiene los resultados esperados y obtenidos.

## Estructura

- `src/Equipo.java`: clase abstracta, validación común y disponibilidad.
- `src/Proyector.java`, `src/CamaraVideo.java`, `src/EquipoSonido.java`: características y cálculos particulares.
- `src/GestorAlquileres.java`: inventario, unicidad, operaciones e ingresos.
- `src/Main.java`: interfaz y datos iniciales.
- `pruebas/Pruebas.java`: verificación automática del modelo.
- `documentacion/Informe-EnEscena.pdf`: análisis, diseño UML integrado y evidencia.

## Subir a GitHub y entregar

1. Cree un repositorio público llamado `EnEscena` en GitHub.
2. No suba los archivos `.class` de `bin`. Suba las carpetas `src`, `pruebas`, `documentacion`, el `README.md` y `.gitignore`, conservando la estructura. Puede arrastrarlos mediante **Add file > Upload files**.
3. Confirme los archivos mediante **Commit changes**.
4. Compruebe que el repositorio sea público y copie su URL.
5. En Canvas entregue `Informe-EnEscena.pdf` y la URL del repositorio público.

El ZIP es un paquete para extraer: no sustituye el PDF ni la URL solicitados en Canvas. No suba únicamente el ZIP a GitHub.
