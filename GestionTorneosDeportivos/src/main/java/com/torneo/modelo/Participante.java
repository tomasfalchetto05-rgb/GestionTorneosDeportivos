package com.torneo.modelo;

import com.torneo.excepciones.ValidacionException;
import java.util.ArrayList;

/** Competidor del torneo. Cada subclase calcula a su modo el puntaje final (polimorfismo). */
public abstract class Participante {
    private final int codigo;
    private final String nombre;
    private final Modalidad modalidad;
    private int puntosAcumulados;
    private final ArrayList<Resultado> resultados = new ArrayList<>();

    protected Participante(int codigo, String nombre, Modalidad modalidad) {
        if (codigo <= 0) throw new ValidacionException("El codigo del participante debe ser positivo.");
        if (nombre.trim().isEmpty()) throw new ValidacionException("El nombre no puede estar vacio.");
        this.codigo = codigo;
        this.nombre = nombre.trim();
        this.modalidad = modalidad;
    }

    public abstract String getTipo();
    public abstract int calcularPuntajeFinal();

    public void acumularPuntos(int puntos) {
        if (puntos < 0) throw new ValidacionException("Los puntos no pueden ser negativos.");
        puntosAcumulados += puntos;
    }

    /** Asocia el resultado al participante y actualiza su puntaje acumulado. */
    public void agregarResultado(Resultado resultado) {
        acumularPuntos(resultado.getPuntos());
        resultados.add(resultado);
    }

    public int getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public Modalidad getModalidad() { return modalidad; }
    public int getPuntosAcumulados() { return puntosAcumulados; }
    public ArrayList<Resultado> getResultados() { return resultados; }

    @Override
    public String toString() {
        return String.format("[%d] %s | %s | %s | Acumulado: %d | Final: %d", codigo, nombre, getTipo(),
                modalidad.getNombre(), puntosAcumulados, calcularPuntajeFinal());
    }
}
