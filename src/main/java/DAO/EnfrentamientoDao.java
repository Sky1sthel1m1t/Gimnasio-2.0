package DAO;

import Modelo.Conexion;
import Modelo.Enfrentamiento;
import Modelo.Movimientos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EnfrentamientoDao {
    private Conexion con = Conexion.getInstance();

    public int getSiguienteID(){
        String comandoCrearCombate = "select getSiguienteCombateId() as id;";
        Integer num = null;

        try {
            con.conectar();
            Statement st = con.getConexion().createStatement();
            ResultSet rs = con.consulta(st, comandoCrearCombate);

            if (rs.next()){
                num = rs.getInt("id");
            }

        } catch (SQLException e){
            throw new RuntimeException(e);
        }

        con.desconectar();

        return num+1;
    }

    public void iniciarEnfrentamiento(Enfrentamiento enfrentamiento){
        String comandoCrearCombate = "call insActCombate (null, null, null)";

        String comando = "call iniciarCombate(" +
                enfrentamiento.pokemon_id1() + "," +
                enfrentamiento.pokemon_id2() + "," +
                "'" + enfrentamiento.entrenador_ci1() + "'" + "," +
                "'" + enfrentamiento.entrenador_ci2() + "'" + "," +
                enfrentamiento.combate_id() + ");";

        try {
            con.conectar();
            Statement st = con.getConexion().createStatement();
            con.ejecutar(st, comandoCrearCombate);
            con.ejecutar(st, comando);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        con.desconectar();
    }

    public void realizarMovimiento(Enfrentamiento enfrentamiento, Movimientos movimientos,
                                   int numTurno, int combate_id){

        String comando = "call realizarMovimiento("
                + "'" + enfrentamiento.entrenador_ci1() + "'" + ","
                + "'" + enfrentamiento.entrenador_ci2() + "'" + ","
                + enfrentamiento.pokemon_id1() + ","
                + enfrentamiento.pokemon_id2() + ","
                + movimientos.id() + ","
                + numTurno + ","
                + combate_id + ");";

        try {
            con.conectar();
            Statement st = con.getConexion().createStatement();
            con.ejecutar(st, comando);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        con.desconectar();
    }

}
