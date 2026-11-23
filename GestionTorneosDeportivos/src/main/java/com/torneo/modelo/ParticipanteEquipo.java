package com.torneo.modelo;

import com.torneo.excepciones.ValidacionException;

/** Equipo: recibe una bonificacion fija al alcanzar cierta cantidad de puntos. */
public class ParticipanteEquipo extends Participante {
    public static final int UMBRAL_BONIFICACION = 50;
    public static final int BONIFICACION = 10;
    private final int integrantes;

    public ParticipanteEquipo(int codigo, String nombre, int integrantes, Modalidad modalidad) {
        super(codigo, nombre, modalidad);
        if (integrantes < 1) throw new ValidacionException("El equipo debe tener al menos un integrante.");
        this.integrantes = integrantes;
    }

    public int getIntegrantes() { return integrantes; }

    @Override
    public String getTipo() { return "Equipo"; }

    @Override
    public int calcularPuntajeFinal() {
        int puntos = getPuntosAcumulados();
        return puntos >= UMBRAL_BONIFICACION ? puntos + BONIFICACION : puntos;
    }

    @Override
    public String toString() { return super.toString() + " | Integrantes: " + integrantes; }
}
