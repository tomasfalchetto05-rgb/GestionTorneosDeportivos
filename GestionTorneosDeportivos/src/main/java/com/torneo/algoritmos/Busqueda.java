package com.torneo.algoritmos;

import com.torneo.modelo.Participante;
import java.util.List;

/** Busquedas manuales por codigo. Devuelven la posicion encontrada o -1. */
public class Busqueda {

    /** Recorre la lista completa. Mejor caso O(1), promedio y peor caso O(n). No requiere orden. */
    public static int lineal(List<Participante> lista, int codigo) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getCodigo() == codigo) return i;
        }
        return -1;
    }

    /** Descarta la mitad en cada paso. Mejor caso O(1), promedio y peor caso O(log n). Requiere lista ordenada por codigo. */
    public static int binaria(List<Participante> lista, int codigo) {
        int bajo = 0, alto = lista.size() - 1;
        while (bajo <= alto) {
            int medio = (bajo + alto) / 2;
            int actual = lista.get(medio).getCodigo();
            if (actual == codigo) return medio;
            if (actual < codigo) bajo = medio + 1;
            else alto = medio - 1;
        }
        return -1;
    }

    /** Variante binaria: primera posicion con codigo >= al buscado (donde insertar manteniendo el orden). */
    public static int posicionInsercion(List<Participante> lista, int codigo) {
        int bajo = 0, alto = lista.size();
        while (bajo < alto) {
            int medio = (bajo + alto) / 2;
            if (lista.get(medio).getCodigo() < codigo) bajo = medio + 1;
            else alto = medio;
        }
        return bajo;
    }
}
