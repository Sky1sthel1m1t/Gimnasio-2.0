package DAO;

import Modelo.Tipo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TipoDao extends AbstractDao<Tipo> {
    @Override
    public ArrayList<Tipo> getAll() {
        ArrayList<Tipo> datos = new ArrayList<>();
        String comando = "select * from tipo";

        try {
            con.conectar();
            Statement st = con.getConexion().createStatement();
            ResultSet rs = con.consulta(st, comando);

            while (rs.next()) {
                Tipo tipo = new Tipo(rs.getInt("id"), rs.getString("Nombre"));
                datos.add(tipo);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        con.desconectar();

        return datos;
    }

    @Override
    public void insert(Tipo valor) throws SQLException {
        String comando = "call insacttipo(" + valor.id() + "," + "'" + valor.nombre() + "')";

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
                "AND table_name = 'tipo'" + ";";

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

    public String getNombre(int id) {
        String comando = "select * from tipo t where id = " + id;
        String nombre = "";

        try {
            con.conectar();
            Statement st = con.getConexion().createStatement();
            ResultSet rs = con.consulta(st, comando);

            while (rs.next()) {
                Tipo tipo = new Tipo(rs.getInt("id"), rs.getString("Nombre"));
                nombre = tipo.nombre();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        con.desconectar();
        return nombre;
    }
}
