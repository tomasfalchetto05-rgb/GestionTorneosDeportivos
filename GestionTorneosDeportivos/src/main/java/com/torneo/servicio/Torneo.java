package com.torneo.servicio;

import com.torneo.algoritmos.Busqueda;
import com.torneo.algoritmos.Ordenamiento;
import com.torneo.excepciones.ValidacionException;
import com.torneo.modelo.*;
import java.util.ArrayList;
import java.util.Random;

/** Administra participantes, modalidades y resultados. La lista de participantes se mantiene ordenada por codigo. */
public class Torneo {
    private final ArrayList<Participante> participantes = new ArrayList<>();
    private final ArrayList<Modalidad> modalidades = new ArrayList<>();
    private final ArrayList<Resultado> resultados = new ArrayList<>();

    // ---------- Registro (con validaciones) ----------

    public Modalidad registrarModalidad(int codigo, String nombre, String descripcion) {
        Modalidad nueva = new Modalidad(codigo, nombre, descripcion);
        if (buscarModalidad(codigo) != null) throw new ValidacionException("Ya existe la modalidad " + codigo + ".");
        modalidades.add(nueva);
        return nueva;
    }

    public Participante registrarIndividual(int codigo, String nombre, int edad, int codModalidad) {
        return agregarParticipante(new ParticipanteIndividual(codigo, nombre, edad, modalidadValida(codModalidad)));
    }

    public Participante registrarEquipo(int codigo, String nombre, int integrantes, int codModalidad) {
        return agregarParticipante(new ParticipanteEquipo(codigo, nombre, integrantes, modalidadValida(codModalidad)));
    }

    /** Inserta en su lugar (busqueda binaria) para mantener el orden por codigo rechaza codigos repetidos. */
    private Participante agregarParticipante(Participante nuevo) {
        int pos = Busqueda.posicionInsercion(participantes, nuevo.getCodigo());
        if (pos < participantes.size() && participantes.get(pos).getCodigo() == nuevo.getCodigo()) {
            throw new ValidacionException("Ya existe un participante con el codigo " + nuevo.getCodigo() + ".");
        }
        participantes.add(pos, nuevo);
        return nuevo;
    }

    /** Registra los puntos y actualiza el puntaje acumulado del participante. */
    public Resultado registrarResultado(int codResultado, int codParticipante, int puntos) {
        Participante p = buscarParticipante(codParticipante);
        if (p == null) throw new ValidacionException("No existe el participante " + codParticipante + ".");
        if (existeResultado(codResultado)) throw new ValidacionException("Ya existe el resultado " + codResultado + ".");
        Resultado r = new Resultado(codResultado, p, p.getModalidad(), puntos);
        p.agregarResultado(r);
        resultados.add(r);
        return r;
    }

    // ---------- Busquedas ----------

    public Participante buscarParticipante(int codigo) {
        int pos = Busqueda.binaria(participantes, codigo);
        return pos >= 0 ? participantes.get(pos) : null;
    }

    public Modalidad buscarModalidad(int codigo) {
        for (Modalidad m : modalidades) {
            if (m.getCodigo() == codigo) return m;
        }
        return null;
    }

    private Modalidad modalidadValida(int codigo) {
        Modalidad m = buscarModalidad(codigo);
        if (m == null) throw new ValidacionException("La modalidad " + codigo + " no existe.");
        return m;
    }

    private boolean existeResultado(int codigo) {
        for (Resultado r : resultados) {
            if (r.getCodigo() == codigo) return true;
        }
        return false;
    }

    // ---------- Tabla de posiciones y estadisticas ----------

    public ArrayList<Participante> tablaDePosiciones(boolean usarMergeSort) {
        return usarMergeSort ? Ordenamiento.mergeSort(participantes) : Ordenamiento.insercion(participantes);
    }

    /** Mayor puntaje final ante empate queda el de menor codigo*/
    public Participante participanteConMayorPuntaje() {
        Participante mejor = null;
        for (Participante p : participantes) {
            if (mejor == null || p.calcularPuntajeFinal() > mejor.calcularPuntajeFinal()) mejor = p;
        }
        return mejor;
    }

    public double promedioPuntos() {
        if (participantes.isEmpty()) return 0;
        int suma = 0;
        for (Participante p : participantes) suma += p.calcularPuntajeFinal();
        return (double) suma / participantes.size();
    }

    public ArrayList<Participante> getParticipantes() { return participantes; }
    public ArrayList<Modalidad> getModalidades() { return modalidades; }
    public ArrayList<Resultado> getResultados() { return resultados; }

    // ---------- Datos de demostracion ----------

    public void cargarDatosDemo(int cantidad) {
        String[] deportes = {"Futbol", "Natacion", "Atletismo", "Ajedrez", "Voley"};
        for (int i = 0; i < deportes.length; i++) {
            if (buscarModalidad(i + 1) == null) registrarModalidad(i + 1, deportes[i], "Modalidad de demostracion");
        }
        Random azar = new Random(2026);
        int codigo = 1000, codResultado = 1;
        for (int i = 0; i < cantidad; i++) {
            while (buscarParticipante(codigo) != null) codigo++;
            int modalidad = 1 + azar.nextInt(deportes.length);
            if (i % 5 == 4) registrarEquipo(codigo, "Equipo " + codigo, 2 + azar.nextInt(10), modalidad);
            else registrarIndividual(codigo, "Atleta " + codigo, 15 + azar.nextInt(35), modalidad);
            int cantidadResultados = 1 + azar.nextInt(3);
            for (int r = 0; r < cantidadResultados; r++) {
                while (existeResultado(codResultado)) codResultado++;
                registrarResultado(codResultado, codigo, azar.nextInt(41));
            }
        }
    }
}
