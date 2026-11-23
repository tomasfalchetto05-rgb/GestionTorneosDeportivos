# Sistema de Gestión de Torneos Deportivos

Trabajo Integrador Final — **Programación II 2026** · Tecnicatura en Informática
**Alumno:** Falchetto Tomás · **Proyecto 3** · Aplicación de consola en **Java** (modalidad individual)

Aplicación que permite administrar un torneo con deportistas individuales y equipos: registrar participantes y
modalidades, cargar resultados, actualizar puntajes, generar la tabla de posiciones y comparar el rendimiento de
distintos algoritmos de búsqueda y ordenamiento.

## Funcionalidades

| Requerimiento | Dónde se resuelve |
|---|---|
| RF01 Registrar participantes (individuales y equipos, código único) | Menú 2 y 3 · `Torneo.registrarIndividual / registrarEquipo` |
| RF02 Registrar modalidades (código, nombre, descripción) | Menú 1 · `Torneo.registrarModalidad` |
| RF03 Asociar participante con modalidad válida | Al registrar el participante se elige una modalidad existente |
| RF04 Registrar resultados | Menú 4 · `Torneo.registrarResultado` |
| RF05 Actualizar puntaje acumulado | `Participante.agregarResultado` (automático al cargar un resultado) |
| RF06 Localizar participante por código | Menú 5 · búsqueda binaria (`Busqueda.binaria`) |
| RF07 Tabla de posiciones (puntaje desc., desempate por código asc.) | Menú 6 · Merge Sort o inserción (a elección) |
| RF08 Consultar resultados y participantes asociados | Menú 7 (todos los resultados) y menú 5 (resultados de un participante) |
| RF09 Estadísticas (mayor puntaje, cantidad, promedio) | Menú 8 |
| RF10 Evaluar rendimiento con tiempos de ejecución | Menú 9 · `PruebasRendimiento` |

Además, la opción **10** carga 500 participantes de demostración (semilla fija) para probar el sistema rápidamente.
Los listados largos muestran las primeras 20 filas (constante `LIMITE` en `Main`).

### Reglas de puntaje
* **Individual:** puntaje final = puntos acumulados.
* **Equipo:** puntaje final = puntos acumulados + **10** de bonificación fija cuando el acumulado llega a **50** o más.
* La tabla de posiciones y las estadísticas usan el **puntaje final**. El promedio general es el promedio de los puntajes finales.

### Validaciones
Códigos únicos (participantes, modalidades y resultados), nombres no vacíos, edad positiva, equipos con al menos un
integrante, puntos no negativos, participante y modalidad existentes, y errores de ingreso (texto donde se espera un
número, opciones inexistentes). Los errores de negocio se informan con la excepción propia `ValidacionException` sin
cerrar el programa.

## Estructura del proyecto

```
GestionTorneosDeportivos/
├── README.md
├── .gitignore
├── docs/
│   ├── diagrama-clases.png
│   └── informe-tecnico.pdf
└── src/main/java/com/torneo/
    ├── Main.java                        # menú de consola
    ├── modelo/                          # Participante (abstracta), ParticipanteIndividual,
    │                                    # ParticipanteEquipo, Modalidad, Resultado
    ├── servicio/                        # Torneo, PruebasRendimiento
    ├── algoritmos/                      # Busqueda (lineal, binaria), Ordenamiento (inserción, Merge Sort)
    └── excepciones/                     # ValidacionException
```

## Requisitos
* JDK 17 o superior (desarrollado y probado con OpenJDK 21).
* No usa Maven ni librerías externas.

## Compilar y ejecutar

Desde la carpeta raíz del proyecto (`GestionTorneosDeportivos/`):

**Linux / macOS**
```bash
mkdir -p out
javac -encoding UTF-8 -d out $(find src/main/java -name "*.java")
java -cp out com.torneo.Main
```

**Windows (CMD)**
```bat
mkdir out
dir /s /b src\main\java\*.java > fuentes.txt
javac -encoding UTF-8 -d out @fuentes.txt
java -cp out com.torneo.Main
```

**Desde un IDE (IntelliJ IDEA, Eclipse, NetBeans, VS Code):** abrir la carpeta, marcar `src/main/java` como carpeta de
fuentes y ejecutar la clase `com.torneo.Main`.

### Prueba rápida
1. Ejecutar la aplicación y elegir **10** (cargar datos de demostración).
2. **6** para ver la tabla de posiciones, **5** con el código `1000` para localizar un participante, **8** para las estadísticas.
3. **9** para correr las pruebas de rendimiento (demora aproximadamente entre 5 y 30 segundos según el equipo).

## Algoritmos (programados manualmente)
No se usan `Collections.sort()`, `Arrays.sort()`, `Collections.binarySearch()` ni streams.

| Algoritmo | Mejor | Promedio | Peor |
|---|---|---|---|
| Búsqueda lineal | O(1) | O(n) | O(n) |
| Búsqueda binaria | O(1) | O(log n) | O(log n) |
| Ordenamiento por inserción | O(n) | O(n²) | O(n²) |
| Merge Sort (recursivo) | O(n log n) | O(n log n) | O(n log n) |

La lista de participantes se mantiene **ordenada por código** (inserción en la posición hallada con búsqueda binaria),
lo que permite localizar participantes en O(log n) y rechazar códigos repetidos.

## Pruebas de rendimiento
Con conjuntos de 100, 1.000 y 10.000 participantes generados en memoria, se mide con `System.nanoTime()`:
preparación de datos (por separado), búsqueda lineal vs. binaria (código existente e inexistente) y ordenamiento por
inserción vs. Merge Sort sobre copias idénticas. Cada prueba se repite 10 veces y se informa el promedio; en las
búsquedas cada repetición ejecuta un lote de 1.000 búsquedas y se informa el tiempo por búsqueda. Los tiempos dependen
de la computadora, por lo que pueden diferir de los del informe.

## Documentación
* `docs/diagrama-clases.png` — diagrama de clases UML.
* `docs/informe-tecnico.pdf` — informe técnico (diseño, algoritmos, complejidad, mediciones y conclusión).
