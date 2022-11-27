package Modelo;

public record Combate(int id, String fecha, String hora) {
    public String[] getDatos(){
        String[] aux = hora.split("\\.");
        return new String[]{id + "", fecha, aux[0]};
    }
}
