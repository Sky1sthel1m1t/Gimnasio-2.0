package Modelo;

public record Entrenador(String ci, String nombre, String apellido) {

    public String[] getDatos() {
        return new String[]{ci, nombre, apellido};
    }

    @Override
    public String toString() {
        return ci + " - " + nombre + " " + apellido;
    }
}
