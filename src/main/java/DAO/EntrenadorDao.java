package DAO;

import Modelo.Entrenador;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class EntrenadorDao extends AbstractDao<Entrenador> {
    @Override
    public ArrayList<Entrenador> getAll() {
        ArrayList<Entrenador> datos = new ArrayList<>();
        String comando = "SELECT * FROM entrenador";

        try {
            con.conectar();
            Statement st = con.getConexion().createStatement();
            ResultSet rs = con.consulta(st, comando);

            while (rs.next()) {
                Entrenador e = new Entrenador(
                        rs.getString("ci"),
                        rs.getString("nombre"),
                        rs.getString("apellido")
                );
                datos.add(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        con.desconectar();

        return datos;
    }

    @Override
    public void insert(Entrenador valor) throws SQLException {
        String comando = "call insActEntrenador (" + "'" + valor.ci() + "'" + ","
                + "'" + valor.nombre() + "'" + ","
                + "'" + valor.apellido() + "'" + ");";


        con.conectar();
        Statement st = con.getConexion().createStatement();
        con.ejecutar(st, comando);


        con.desconectar();
    }

    @Override
    public ArrayList<String> getColumnas() {
        ArrayList<String> columnas = new ArrayList<>();

        String comando = "SELECT column_name " +
                "FROM information_schema.columns " +
                "WHERE table_schema = 'public' " +
                "AND table_name = 'entrenador'" + ";";

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

    public Entrenador get(String id) {
        Entrenador datos = null;
        String comando = "SELECT * FROM entrenador WHERE ci LIKE '" + id + "';";

        try {
            con.conectar();
            Statement st = con.getConexion().createStatement();
            ResultSet rs = con.consulta(st, comando);

            if (rs.next()) {
                datos = new Entrenador(
                        rs.getString("ci"),
                        rs.getString("nombre"),
                        rs.getString("apellido")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        con.desconectar();

        return datos;
    }
}
