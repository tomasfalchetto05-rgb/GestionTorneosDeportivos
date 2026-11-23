package com.torneo.modelo;

import com.torneo.excepciones.ValidacionException;

/** Puntos obtenidos por un participante en una competencia. */
public class Resultado {
    private final int codigo;
    private final Participante participante;
    private final Modalidad modalidad;
    private final int puntos;

    public Resultado(int codigo, Participante participante, Modalidad modalidad, int puntos) {
        if (codigo <= 0) throw new ValidacionException("El codigo del resultado debe ser positivo.");
        if (puntos < 0) throw new ValidacionException("Los puntos no pueden ser negativos.");
        this.codigo = codigo;
        this.participante = participante;
        this.modalidad = modalidad;
        this.puntos = puntos;
    }

    public int getCodigo() { return codigo; }
    public Participante getParticipante() { return participante; }
    public Modalidad getModalidad() { return modalidad; }
    public int getPuntos() { return puntos; }

    @Override
    public String toString() {
        return "Resultado " + codigo + " | " + participante.getNombre() + " (cod. " + participante.getCodigo()
                + ") | " + modalidad.getNombre() + " | " + puntos + " pts";
    }
}
