package Vista;

import DAO.TipoDao;
import Modelo.Tipo;
import Visuales.TextPrompt;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;

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
        int x = super.frame.getPanelPrincipal().getSize().width / 2;
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

        btnRegistrar.addActionListener(e -> {
            String aux = txtId.getText().trim();
            String nombre  = txtNombre.getText().trim();

            if (nombre.isBlank()){
                JOptionPane.showMessageDialog(null,"El nombre no puede estar vacio");
                return;
            }

            Tipo tipo;

            if (aux.isBlank()){
                tipo = new Tipo(null, nombre);
            } else {
                try {
                    int id = Integer.parseInt(aux);
                    tipo = new Tipo(id,nombre);
                } catch (Exception ex){
                    JOptionPane.showMessageDialog(null,"El id solo puede ser un numero entero");
                    return;
                }
            }

            try {
                dao.insert(tipo);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Ha ocurrido el siguiente error: " + ex.getMessage());
            }
        });

        super.panelRegistro.add(lbId);
        super.panelRegistro.add(lbNombre);
        super.panelRegistro.add(txtId);
        super.panelRegistro.add(txtNombre);
        super.panelRegistro.add(btnRegistrar);
    }
}
