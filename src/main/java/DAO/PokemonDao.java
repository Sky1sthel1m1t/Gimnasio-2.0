package DAO;

import Modelo.Pokemon;
import Modelo.Tipo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PokemonDao extends AbstractDao<Pokemon> {

    @Override
    public ArrayList<Pokemon> getAll() {
        ArrayList<Pokemon> datos = new ArrayList<>();
        String comando = "SELECT *, gettipos(id) as tipos FROM pokemon";

        try {
            con.conectar();
            Statement st = con.getConexion().createStatement();
            ResultSet rs = con.consulta(st, comando);

            while (rs.next()) {
                Pokemon pokemon = new Pokemon(rs.getInt("id"),
                        rs.getString("Nombre"),
                        rs.getInt("saludmaxima"),
                        rs.getInt("saludactual"),
                        rs.getBoolean("disponibilidad"),
                        rs.getString("tipos"));
                datos.add(pokemon);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        con.desconectar();

        return datos;
    }

    @Override
    public void insert(Pokemon valor) throws SQLException {

        String tipos = "array[";
        String separador = "";

        for (int s : valor.tipos_id()) {
            tipos += separador + s;
            separador = ",";
        }

        tipos += "]";

        String comando = "call registrarpokemon (" + "'" + valor.nombre() + "'" + ","
                + valor.saludMaxima() + ","
                + tipos + ");";


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
                "AND table_name = 'pokemon'" + ";";

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

    public Pokemon get(int id) {
        Pokemon datos = null;
        String comando = "SELECT *, gettipos(id) as tipos FROM pokemon WHERE id = " + id;

        try {
            con.conectar();
            Statement st = con.getConexion().createStatement();
            ResultSet rs = con.consulta(st, comando);

            if (rs.next()) {
                datos = new Pokemon(rs.getInt("id"),
                        rs.getString("Nombre"),
                        rs.getInt("saludmaxima"),
                        rs.getInt("saludactual"),
                        rs.getBoolean("disponibilidad"),
                        rs.getString("tipos"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        con.desconectar();

        return datos;
    }

    public ArrayList<Integer> getTipos(int id){
        ArrayList<Integer> tipos = new ArrayList<>();
        String comando = "select tipo_id from tipopokemon t where pokemon_id = " + id;

        try {
            con.conectar();
            Statement st = con.getConexion().createStatement();
            ResultSet rs = con.consulta(st, comando);

            while (rs.next()) {
                tipos.add(rs.getInt("tipo_id"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        con.desconectar();

        return tipos;
    }

    public boolean esDerrotado(int id){
        String comando = "select esDerrotado("+ id +") as b;";
        boolean bool = false;

        try {
            con.conectar();
            Statement statement = con.getConexion().createStatement();
            ResultSet rs = con.consulta(statement, comando);

            if (rs.next()){
                bool = rs.getBoolean("b");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return bool;
    }

    public int getvidaActual(int id){
        String comando = "SELECT saludactual AS vida FROM pokemon p WHERE id = " + id;
        int vida = 0;

        try {
            con.conectar();
            Statement statement = con.getConexion().createStatement();
            ResultSet rs = con.consulta(statement, comando);

            if (rs.next()){
                vida = rs.getInt("vida");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return vida;
    }

    public void curarPokemones(){
        String comando = "call curarPokemones()";

        try {
            con.conectar();
            Statement st = con.getConexion().createStatement();
            con.ejecutar(st, comando);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        con.desconectar();
    }

    public void curarPokemonesDebilitados(){
        String comando = "call curarPokemonesDebilitados()";

        try {
            con.conectar();
            Statement st = con.getConexion().createStatement();
            con.ejecutar(st, comando);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        con.desconectar();
    }

    public void aprenderMovimiento(int pokemonId, int movId) throws SQLException {
        String comando = "call enseñarmovimiento( " + pokemonId + "," + movId + ");";

        con.conectar();
        Statement statement = con.getConexion().createStatement();
        con.ejecutar(statement, comando);

        con.desconectar();
    }

    public void olvidarMovimiento(int pokemonId, int movId) throws SQLException {
        String comando = "call olvidarMovimiento( " + pokemonId + "," + movId + ");";

        con.conectar();
        Statement statement = con.getConexion().createStatement();
        con.ejecutar(statement, comando);

        con.desconectar();
    }
}
