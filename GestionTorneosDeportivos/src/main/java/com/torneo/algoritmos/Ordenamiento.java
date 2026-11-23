package com.torneo.algoritmos;

import com.torneo.modelo.Participante;
import java.util.ArrayList;
import java.util.List;

/** Ordenamientos manuales para la tabla de posiciones. No modifican la lista original: devuelven una nueva. */
public class Ordenamiento {

    /** Criterio: mayor puntaje final primero; si empatan, menor codigo primero. */
    public static boolean precede(Participante a, Participante b) {
        if (a.calcularPuntajeFinal() != b.calcularPuntajeFinal()) {
            return a.calcularPuntajeFinal() > b.calcularPuntajeFinal();
        }
        return a.getCodigo() < b.getCodigo();
    }

    /** Insercion: mejor caso O(n) (ya ordenada), promedio y peor caso O(n^2). */
    public static ArrayList<Participante> insercion(List<Participante> origen) {
        ArrayList<Participante> lista = new ArrayList<>(origen);
        for (int i = 1; i < lista.size(); i++) {
            Participante clave = lista.get(i);
            int j = i - 1;
            while (j >= 0 && precede(clave, lista.get(j))) {
                lista.set(j + 1, lista.get(j));
                j--;
            }
            lista.set(j + 1, clave);
        }
        return lista;
    }

    /** Merge Sort recursivo: divide a la mitad, ordena cada parte y las fusiona. O(n log n) en todos los casos. */
    public static ArrayList<Participante> mergeSort(List<Participante> lista) {
        if (lista.size() <= 1) return new ArrayList<>(lista);
        int medio = lista.size() / 2;
        return fusionar(mergeSort(lista.subList(0, medio)), mergeSort(lista.subList(medio, lista.size())));
    }

    /** Une dos listas ya ordenadas tomando siempre el primero que corresponde. */
    private static ArrayList<Participante> fusionar(List<Participante> a, List<Participante> b) {
        ArrayList<Participante> resultado = new ArrayList<>();
        int i = 0, j = 0;
        while (i < a.size() && j < b.size()) {
            if (precede(b.get(j), a.get(i))) resultado.add(b.get(j++));
            else resultado.add(a.get(i++));
        }
        while (i < a.size()) resultado.add(a.get(i++));
        while (j < b.size()) resultado.add(b.get(j++));
        return resultado;
    }
}
