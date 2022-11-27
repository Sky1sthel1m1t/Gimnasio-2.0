package Modelo;

public record Tipo(Integer id, String nombre) {

    public String[] getDatos(){
        return new String[]{id + "", nombre};
    }

    @Override
    public String toString() {
        return nombre;
    }
}
