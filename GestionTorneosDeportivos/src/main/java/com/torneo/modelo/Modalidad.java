package com.torneo.modelo;

import com.torneo.excepciones.ValidacionException;

/** Disciplina o categoria deportiva. */
public class Modalidad {
    private final int codigo;
    private final String nombre;
    private final String descripcion;

    public Modalidad(int codigo, String nombre, String descripcion) {
        if (codigo <= 0) throw new ValidacionException("El codigo de la modalidad debe ser positivo.");
        if (nombre.trim().isEmpty()) throw new ValidacionException("El nombre de la modalidad no puede estar vacio.");
        this.codigo = codigo;
        this.nombre = nombre.trim();
        this.descripcion = descripcion.trim();
    }

    public int getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }

    @Override
    public String toString() { return "[" + codigo + "] " + nombre + " - " + descripcion; }
}
