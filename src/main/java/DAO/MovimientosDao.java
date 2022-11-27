package DAO;

import Modelo.Movimientos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MovimientosDao extends AbstractDao<Movimientos> {
    @Override
    public ArrayList<Movimientos> getAll() {
        ArrayList<Movimientos> datos = new ArrayList<>();
        String comando = "SELECT * FROM movimientos";

        try {
            con.conectar();
            Statement st = con.getConexion().createStatement();
            ResultSet rs = con.consulta(st, comando);

            while (rs.next()) {
                Movimientos m = new Movimientos(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getInt("dañocuracion"),
                        rs.getInt("tipo_id")
                );
                datos.add(m);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        con.desconectar();

        return datos;
    }

    @Override
    public void insert(Movimientos valor) throws SQLException {
        String comando = "call insActMovimiento (" + valor.id() + ","
                + "'" + valor.nombre() + "'" + ","
                + "'" + valor.descripcion() + "'" + ","
                + valor.dañoCuracion() + ","
                + valor.tipo_id() + ");";


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
                "AND table_name = 'movimientos'" + ";";

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

    public ArrayList<Movimientos> getMovimientosPokemon(int id) {
        ArrayList<Movimientos> datos = new ArrayList<>();
        String comando = "SELECT movimiento_id FROM pokemonsabe p WHERE pokemon_id = " + id;

        try {
            con.conectar();
            Statement st = con.getConexion().createStatement();
            ResultSet rs = con.consulta(st, comando);

            while (rs.next()) {
                int movID = rs.getInt("movimiento_id");
                Movimientos m = get(movID);
                datos.add(m);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return datos;
    }

    public Movimientos get(int id) {
        Movimientos datos = null;
        String comando = "SELECT * FROM movimientos WHERE id = " + id;

        try {
            con.conectar();
            Statement st = con.getConexion().createStatement();
            ResultSet rs = con.consulta(st, comando);

            if (rs.next()) {
                datos = new Movimientos(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getInt("dañocuracion"),
                        rs.getInt("tipo_id")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        con.desconectar();

        return datos;
    }

    public ArrayList<Movimientos> getPorTipo(int tipo_id){
        ArrayList<Movimientos> datos = new ArrayList<>();
        String comando = "SELECT * FROM movimientos WHERE tipo_id = " + tipo_id;

        try {
            con.conectar();
            Statement st = con.getConexion().createStatement();
            ResultSet rs = con.consulta(st, comando);

            while (rs.next()) {
                Movimientos m = new Movimientos(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getInt("dañocuracion"),
                        rs.getInt("tipo_id")
                );
                datos.add(m);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        con.desconectar();

        return datos;
    }
}
