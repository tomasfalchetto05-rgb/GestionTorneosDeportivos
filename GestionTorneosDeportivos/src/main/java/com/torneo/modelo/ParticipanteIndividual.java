package com.torneo.modelo;

import com.torneo.excepciones.ValidacionException;

/** Deportista individual: su puntaje final es la suma directa de sus puntos. */
public class ParticipanteIndividual extends Participante {
    private final int edad;

    public ParticipanteIndividual(int codigo, String nombre, int edad, Modalidad modalidad) {
        super(codigo, nombre, modalidad);
        if (edad <= 0) throw new ValidacionException("La edad debe ser positiva.");
        this.edad = edad;
    }

    public int getEdad() { return edad; }

    @Override
    public String getTipo() { return "Individual"; }

    @Override
    public int calcularPuntajeFinal() { return getPuntosAcumulados(); }

    @Override
    public String toString() { return super.toString() + " | Edad: " + edad; }
}
