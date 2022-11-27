package Vista;

import DAO.TipoDao;
import Modelo.Tipo;
import PlaceHolder.TextPrompt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PanelTipos extends AbstractPanel<TipoDao>{
    private JLabel lbId = new JLabel("ID: ");
    private JLabel lbNombre = new JLabel("Nombre: ");
    private JTextField txtId = new JTextField();
    private JTextField txtNombre = new JTextField();
    private JButton btnRegistrar = new JButton("Registrar/Actualizar");

    private TipoDao dao = new TipoDao();

    public PanelTipos(Frame frame) {
        super(frame, 30);
        cargarPanelRegistro();
        cargarPanelDatos(dao);
    }

    @Override
    public void leerDatos(DefaultTableModel modelo) {
        modelo.setRowCount(0);
        for (Tipo t: dao.getAll()) {
            modelo.addRow(t.getDatos());
        }
    }

    @Override
    public void cargarPanelRegistro() {
        int x = super.frame.getSize().width / 2;
        int y = 20;
        int altura = 30;
        int ancho = 220;
        int espaciadoY = 40;
        int anchoBtn = 100;

        lbId.setBounds(x-(ancho), y, ancho, altura);
        lbId.setHorizontalAlignment(JLabel.RIGHT);
        txtId.setBounds(x, y, ancho,altura);
        TextPrompt idPh = new TextPrompt("Dejar vacio para insertar un nuevo tipo", txtId);
        y += espaciadoY;
        lbNombre.setBounds(x-ancho, y, ancho, altura);
        lbNombre.setHorizontalAlignment(JLabel.RIGHT);
        txtNombre.setBounds(x, y, ancho,altura);
        y += espaciadoY;
        btnRegistrar.setBounds(x - (anchoBtn), y, anchoBtn*2, altura);

        super.panelRegistro.add(lbId);
        super.panelRegistro.add(lbNombre);
        super.panelRegistro.add(txtId);
        super.panelRegistro.add(txtNombre);
        super.panelRegistro.add(btnRegistrar);
    }
}
