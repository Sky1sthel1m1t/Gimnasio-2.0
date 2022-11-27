package Modelo;

public record Historial(int numTurno, String nombreEntrenador, String nombrePokemon, int vidaActual, String movimiento, String tipoAccion) {
    public String[] getDatos(){
        return new String[]{numTurno + "", nombreEntrenador, nombrePokemon, vidaActual + "", movimiento, tipoAccion};
    }
}
