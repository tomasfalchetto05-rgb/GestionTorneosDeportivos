package com.torneo.excepciones;

/** Error de validacion de datos (sin "throws": es una excepcion no verificada). */
public class ValidacionException extends RuntimeException {
    public ValidacionException(String mensaje) {
        super(mensaje);
    }
}
