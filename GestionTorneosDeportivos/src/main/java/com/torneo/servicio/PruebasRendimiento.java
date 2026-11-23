package com.torneo.servicio;

import com.torneo.algoritmos.Busqueda;
import com.torneo.algoritmos.Ordenamiento;
import com.torneo.modelo.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** compara busquedas y ordenamientos con System.nanoTime() sobre datos generados en memoria. */
public class PruebasRendimiento {
    private static final int[] TAMANIOS = {100, 1000, 10000};
    private static final int REPETICIONES = 10;     // cada prueba se repite y se informa el promedio
    private static final int LOTE = 1000;           // busquedas por repeticion (una sola dura apenas nanosegundos)
    private static final long CALENTAMIENTO = 200_000_000L; // 0,2 s de "calentamiento" de la JVM, no se mide
    private static final DecimalFormat FMT = new DecimalFormat("#,##0", new DecimalFormatSymbols() {{
        setGroupingSeparator('.');
    }});
    private static long sumidero; // acumula resultados para que la JVM no elimine el codigo medido

    public static void ejecutar() {
        // columnas: 0 preparacion | 1 lineal existe | 2 binaria existe | 3 lineal inexistente | 4 binaria inexistente | 5 insercion | 6 merge
        long[][] r = new long[TAMANIOS.length][7];
        for (int t = 0; t < TAMANIOS.length; t++) {
            int n = TAMANIOS[t];
            ArrayList<Participante> datos = null;
            long total = 0;
            for (int i = 0; i < REPETICIONES; i++) { // la preparacion se mide aparte
                long inicio = System.nanoTime();
                datos = generarDatos(n);
                total += System.nanoTime() - inicio;
            }
            r[t][0] = total / REPETICIONES;
            int existente = 2 * n - 1, inexistente = 2; // los codigos generados son impares
            r[t][1] = medirBusqueda(false, datos, existente);
            r[t][2] = medirBusqueda(true, datos, existente);
            r[t][3] = medirBusqueda(false, datos, inexistente);
            r[t][4] = medirBusqueda(true, datos, inexistente);
            r[t][5] = medirOrden(false, datos);
            r[t][6] = medirOrden(true, datos);
        }
        System.out.println("=== PRUEBAS DE RENDIMIENTO (promedio de " + REPETICIONES + " repeticiones) ===");
        tabla("Tabla 1. Preparacion de datos", r, new String[]{"Generacion de datos (ns)"}, 0);
        tabla("Tabla 2. Busqueda de codigo EXISTENTE (ns por busqueda)", r, new String[]{"Busqueda lineal (ns)", "Busqueda binaria (ns)"}, 1, 2);
        tabla("Tabla 3. Busqueda de codigo INEXISTENTE (ns por busqueda)", r, new String[]{"Busqueda lineal (ns)", "Busqueda binaria (ns)"}, 3, 4);
        tabla("Tabla 4. Ordenamiento de copias identicas (ns)", r, new String[]{"Algoritmo simple (ns)", "Merge Sort (ns)"}, 5, 6);
        System.out.println("(Algoritmo simple = insercion)  [control: " + sumidero + "]");
    }

    /** Participantes con codigos impares crecientes (1, 3, 5...) y puntos al azar con semilla fija (hay muchos empates). */
    private static ArrayList<Participante> generarDatos(int n) {
        Modalidad modalidad = new Modalidad(1, "Prueba", "Datos de prueba");
        Random azar = new Random(12345);
        ArrayList<Participante> lista = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int codigo = 2 * i + 1;
            Participante p = (i % 4 == 3) ? new ParticipanteEquipo(codigo, "Equipo " + codigo, 5, modalidad)
                                          : new ParticipanteIndividual(codigo, "Atleta " + codigo, 20, modalidad);
            p.acumularPuntos(azar.nextInt(101));
            lista.add(p);
        }
        return lista;
    }

    private static long medirBusqueda(boolean binaria, ArrayList<Participante> datos, int codigo) {
        long fin = System.nanoTime() + CALENTAMIENTO;
        while (System.nanoTime() < fin) lote(binaria, datos, codigo);
        long total = 0;
        for (int i = 0; i < REPETICIONES; i++) {
            long inicio = System.nanoTime();
            lote(binaria, datos, codigo);
            total += System.nanoTime() - inicio;
        }
        return total / REPETICIONES / LOTE;
    }

    private static void lote(boolean binaria, List<Participante> datos, int codigo) {
        for (int i = 0; i < LOTE; i++) {
            sumidero += binaria ? Busqueda.binaria(datos, codigo) : Busqueda.lineal(datos, codigo);
        }
    }

    private static long medirOrden(boolean merge, ArrayList<Participante> datos) {
        long fin = System.nanoTime() + CALENTAMIENTO;
        while (System.nanoTime() < fin) ordenar(merge, datos);
        long total = 0;
        for (int i = 0; i < REPETICIONES; i++) {
            ArrayList<Participante> copia = new ArrayList<>(datos); // copia identica, fuera de la medicion
            long inicio = System.nanoTime();
            sumidero += ordenar(merge, copia).size();
            total += System.nanoTime() - inicio;
        }
        return total / REPETICIONES;
    }

    private static ArrayList<Participante> ordenar(boolean merge, List<Participante> lista) {
        return merge ? Ordenamiento.mergeSort(lista) : Ordenamiento.insercion(lista);
    }

    private static void tabla(String titulo, long[][] r, String[] cabeceras, int... columnas) {
        System.out.println("\n" + titulo);
        String linea = String.format("%9s", "Cantidad");
        for (String c : cabeceras) linea += String.format(" | %24s", c);
        System.out.println(linea);
        for (int t = 0; t < TAMANIOS.length; t++) {
            linea = String.format("%9s", FMT.format(TAMANIOS[t]));
            for (int c : columnas) linea += String.format(" | %24s", FMT.format(r[t][c]));
            System.out.println(linea);
        }
    }
}
