package Vista;

import DAO.EntrenadorDao;
import Modelo.Entrenador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PanelEntrenador extends AbstractPanel<EntrenadorDao>{

    private EntrenadorDao dao = new EntrenadorDao();
    private JLabel lbCi = new JLabel("CI: ");
    private JLabel lbNombre = new JLabel("Nombre: ");
    private JLabel lbApellido = new JLabel("Apellido: ");
    private JTextField txtCi = new JTextField();
    private JTextField txtNombre = new JTextField();
    private JTextField txtApellido = new JTextField();
    private JButton btnRegistrar = new JButton("Registrar/Actualizar");

    public PanelEntrenador(Frame frame, int porcentaje) {
        super(frame, porcentaje);
        cargarPanelRegistro();
        cargarPanelDatos(dao);
    }

    @Override
    public void leerDatos(DefaultTableModel modelo) {
        modelo.setRowCount(0);
        for (Entrenador e: dao.getAll()) {
            modelo.addRow(e.getDatos());
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

        lbCi.setBounds(x-(ancho), y, ancho, altura);
        lbCi.setHorizontalAlignment(JLabel.RIGHT);
        txtCi.setBounds(x, y, ancho,altura);
        y += espaciadoY;
        lbNombre.setBounds(x-ancho, y, ancho, altura);
        lbNombre.setHorizontalAlignment(JLabel.RIGHT);
        txtNombre.setBounds(x, y, ancho,altura);
        y += espaciadoY;
        lbApellido.setBounds(x-ancho, y, ancho, altura);
        lbApellido.setHorizontalAlignment(JLabel.RIGHT);
        txtApellido.setBounds(x, y, ancho,altura);
        y += espaciadoY;
        btnRegistrar.setBounds(x - (anchoBtn), y, anchoBtn*2, altura);

        btnRegistrar.addActionListener(e -> {
            if (!verificarCI()){
                JOptionPane.showMessageDialog(null, "El CI son solos numeros sin espacios");
                return;
            }

            String ci = txtCi.getText();
            String nombre = txtNombre.getText();
            String apellido = txtApellido.getText();

            Entrenador entrenador = new Entrenador(ci,nombre,apellido);

            try {
                dao.insert(entrenador);
                JOptionPane.showMessageDialog(null, "Se ha registrado con exito al nuevo entrenador");
                leerDatos(defaultTableModel);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
                throw new RuntimeException(ex);
            }
        });

        super.panelRegistro.add(lbCi);
        super.panelRegistro.add(lbNombre);
        super.panelRegistro.add(lbApellido);
        super.panelRegistro.add(txtCi);
        super.panelRegistro.add(txtNombre);
        super.panelRegistro.add(txtApellido);
        super.panelRegistro.add(btnRegistrar);
    }

    private boolean verificarCI(){
        Pattern pattern = Pattern.compile("^\\d+$");
        Matcher matcher = pattern.matcher(txtCi.getText().trim());
        return matcher.find();
    }
}
