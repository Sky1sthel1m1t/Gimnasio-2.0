package Modelo;

import DAO.TipoDao;

public record Movimientos (Integer id, String nombre, String descripcion, int dañoCuracion, int tipo_id) {

    private static TipoDao tipoDao = new TipoDao();

    public String[] getDatos(String tipo){
        return new String[]{id + "", nombre, descripcion, dañoCuracion + "", tipo};
    }

    public String[] getDatos(){
        String tipo = tipoDao.getNombre(tipo_id);
        return new String[]{id + "", nombre, descripcion, dañoCuracion + "", tipo};
    }

}
