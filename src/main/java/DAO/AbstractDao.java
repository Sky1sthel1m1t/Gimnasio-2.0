package DAO;

import Modelo.Conexion;

import java.sql.SQLException;
import java.util.ArrayList;

public abstract class AbstractDao<T> {
    protected Conexion con = Conexion.getInstance();

    public abstract ArrayList<T> getAll();

    public abstract void insert(T valor) throws SQLException;

    public abstract ArrayList<String> getColumnas();
}
