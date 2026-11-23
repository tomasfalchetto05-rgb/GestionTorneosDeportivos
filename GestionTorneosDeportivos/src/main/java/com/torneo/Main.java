package com.torneo;

import com.torneo.excepciones.ValidacionException;
import com.torneo.modelo.*;
import com.torneo.servicio.PruebasRendimiento;
import com.torneo.servicio.Torneo;
import java.util.ArrayList;
import java.util.Scanner;

/** Menu de consola */
public class Main {
    private static final Scanner in = new Scanner(System.in);
    private static final Torneo torneo = new Torneo();
    private static final int LIMITE = 20; // maximo de filas que se imprimen en listados largos

    public static void main(String[] args) {
        int opcion;
        do {
            System.out.println("""

                    ===== GESTION DE TORNEOS DEPORTIVOS =====
                     1. Registrar modalidad
                     2. Registrar participante individual
                     3. Registrar equipo
                     4. Registrar resultado
                     5. Localizar participante por codigo
                     6. Tabla de posiciones
                     7. Consultar resultados
                     8. Estadisticas
                     9. Pruebas de rendimiento
                    10. Cargar datos de demostracion (500)
                     0. Salir""");
            opcion = leerEntero("Opcion: ");
            try {
                ejecutar(opcion);
            } catch (ValidacionException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private static void ejecutar(int opcion) {
        switch (opcion) {
            case 1 -> System.out.println("Registrada: " + torneo.registrarModalidad(
                    leerEntero("Codigo: "), leerTexto("Nombre: "), leerTexto("Descripcion: ")));
            case 2 -> System.out.println("Registrado: " + torneo.registrarIndividual(
                    leerEntero("Codigo: "), leerTexto("Nombre: "), leerEntero("Edad: "), leerModalidad()));
            case 3 -> System.out.println("Registrado: " + torneo.registrarEquipo(
                    leerEntero("Codigo: "), leerTexto("Nombre: "), leerEntero("Integrantes: "), leerModalidad()));
            case 4 -> {
                Resultado r = torneo.registrarResultado(
                        leerEntero("Codigo del resultado: "), leerEntero("Codigo del participante: "), leerEntero("Puntos: "));
                System.out.println("Registrado: " + r + "\nPuntaje final del participante: "
                        + r.getParticipante().calcularPuntajeFinal());
            }
            case 5 -> localizar();
            case 6 -> mostrarTabla();
            case 7 -> consultarResultados();
            case 8 -> estadisticas();
            case 9 -> PruebasRendimiento.ejecutar();
            case 10 -> {
                torneo.cargarDatosDemo(500);
                System.out.println("Datos de demostracion cargados.");
            }
            case 0 -> System.out.println("Hasta luego.");
            default -> System.out.println("Opcion invalida.");
        }
    }

    private static void localizar() {
        Participante p = torneo.buscarParticipante(leerEntero("Codigo a buscar: ")); // busqueda binaria
        if (p == null) {
            System.out.println("No existe ese participante.");
            return;
        }
        System.out.println(p);
        for (Resultado r : p.getResultados()) System.out.println("   " + r);
    }

    private static void mostrarTabla() {
        boolean merge = leerEntero("Algoritmo (1 = Merge Sort, 2 = Insercion): ") != 2;
        ArrayList<Participante> tabla = torneo.tablaDePosiciones(merge);
        System.out.printf("%-4s %-8s %-18s %-11s %-11s %6s%n", "Pos", "Codigo", "Nombre", "Tipo", "Modalidad", "Puntos");
        for (int i = 0; i < Math.min(LIMITE, tabla.size()); i++) {
            Participante p = tabla.get(i);
            System.out.printf("%-4d %-8d %-18s %-11s %-11s %6d%n", i + 1, p.getCodigo(), p.getNombre(),
                    p.getTipo(), p.getModalidad().getNombre(), p.calcularPuntajeFinal());
        }
        if (tabla.size() > LIMITE) System.out.println("... se muestran " + LIMITE + " de " + tabla.size());
    }

    private static void consultarResultados() {
        ArrayList<Resultado> lista = torneo.getResultados();
        for (int i = 0; i < Math.min(LIMITE, lista.size()); i++) System.out.println(lista.get(i));
        if (lista.size() > LIMITE) System.out.println("... se muestran " + LIMITE + " de " + lista.size());
        if (lista.isEmpty()) System.out.println("No hay resultados registrados.");
    }

    private static void estadisticas() {
        if (torneo.getParticipantes().isEmpty()) {
            System.out.println("No hay participantes registrados.");
            return;
        }
        System.out.println("Mayor puntaje: " + torneo.participanteConMayorPuntaje());
        System.out.println("Cantidad de participantes: " + torneo.getParticipantes().size());
        System.out.printf("Promedio general de puntos: %.2f%n", torneo.promedioPuntos());
    }

    /** Muestra las modalidades disponibles y pide el codigo de una. */
    private static int leerModalidad() {
        for (Modalidad m : torneo.getModalidades()) System.out.println("   " + m);
        return leerEntero("Codigo de modalidad: ");
    }

    /** Repite la pregunta hasta que se ingrese un entero. */
    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(in.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero valido.");
            }
        }
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return in.nextLine();
    }
}
