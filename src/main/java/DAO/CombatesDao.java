package DAO;

import Modelo.Acciones;
import Modelo.Combate;
import Modelo.Entrenador;
import Modelo.Historial;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CombatesDao extends AbstractDao<Combate>{
    @Override
    public ArrayList<Combate> getAll() {
        ArrayList<Combate> datos = new ArrayList<>();
        String comando = "SELECT * FROM combates";

        try {
            con.conectar();
            Statement st = con.getConexion().createStatement();
            ResultSet rs = con.consulta(st, comando);

            while (rs.next()) {
                Combate c = new Combate(
                        rs.getInt("id"),
                        rs.getString("fecha"),
                        rs.getString("hora")
                );
                datos.add(c);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        con.desconectar();

        return datos;
    }

    @Override
    public void insert(Combate valor) throws SQLException {

    }

    @Override
    public ArrayList<String> getColumnas() {
        ArrayList<String> columnas = new ArrayList<>();

        String comando = "SELECT column_name " +
                "FROM information_schema.columns " +
                "WHERE table_schema = 'public' " +
                "AND table_name = 'combates'" + ";";

        try {
            con.conectar();
            Statement statement = con.getConexion().createStatement();
            ResultSet rs = con.consulta(statement, comando);

            while (rs.next()) {
                String aux = rs.getString("column_name");
                columnas.add(aux);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        con.desconectar();

        return columnas;
    }

    public ArrayList<Historial> getHistorial(int combateId){
        ArrayList<Historial> datos = new ArrayList<>();
        String comando = "SELECT a.numeroturno, concat_ws(' ', e.nombre, e.apellido) as Entrenador, p.nombre as Pokemon" +
                ", a.vidaactual, coalesce(m.nombre, 'No hubo movimiento') as movimiento, a.tipoaccion FROM historial h \n" +
                "join acciones a on a.id = h.acciones_id \n" +
                "join entrenador e on a.entrenador_ci = e.ci \n" +
                "join pokemon p on a.pokemon_id = p.id \n" +
                "left join movimientos m on m.id = a.movimiento_id\n" +
                "where combate_id = " + combateId + ";";

        try {
            con.conectar();
            Statement statement = con.getConexion().createStatement();
            ResultSet rs = con.consulta(statement, comando);

            while (rs.next()){
                Historial h = new Historial(
                        rs.getInt("numeroturno"),
                        rs.getString("entrenador"),
                        rs.getString("pokemon"),
                        rs.getInt("vidaactual"),
                        rs.getString("movimiento"),
                        rs.getString("tipoaccion")
                );
                datos.add(h);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return datos;
    }
}
