package Modelo;

import java.util.ArrayList;

public record Pokemon(Integer id, String nombre, int saludMaxima, int saludActual, boolean disponibilidad,
                      String tipos, ArrayList<Integer> tipos_id) {

    public Pokemon(int id, String nombre, int saludMaxima, int saludActual, boolean disponibilidad, String tipos) {
        this(id, nombre, saludMaxima, saludActual, disponibilidad, tipos, null);
    }

    public Pokemon(int id, String nombre, int saludMaxima, int saludActual, boolean disponibilidad, ArrayList<Integer> tipos_id) {
        this(id, nombre, saludMaxima, saludActual, disponibilidad, null, tipos_id);
    }

    public String[] getDatos(){
        String disponible;
        if (disponibilidad){
            disponible = "Disponible";
        } else {
            disponible = "No disponible";
        }
        return new String[]{id + "", nombre, saludMaxima + "", saludActual + "", disponible, tipos};
    }

    @Override
    public String toString() {
        return nombre + " " + saludActual + "/" + saludMaxima;
    }
}
