package Vista;

import DAO.MovimientosDao;
import DAO.TipoDao;
import Modelo.Movimientos;
import Modelo.Tipo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class PanelMovimiento extends AbstractPanel<MovimientosDao>{

    private MovimientosDao dao = new MovimientosDao();
    private TipoDao tipoDao = new TipoDao();
    private JLabel lbId = new JLabel("Id: ");
    private JLabel lbNombre = new JLabel("Nombre: ");
    private JLabel lbDescripcion = new JLabel("Descripción: ");
    private JLabel lbDañoCuracion = new JLabel("Daño/Curacion: ");
    private JLabel lbTipo = new JLabel("Tipo: ");
    private JTextField txtId = new JTextField();
    private JTextField txtNombre = new JTextField();
    private JTextField txtDescripcion = new JTextField();
    private JTextField txtDañoCuracion = new JTextField();
    private JComboBox<Tipo> comboBox = new JComboBox<>();
    private JButton btnRegistrar = new JButton("Registrar/Actualizar");
    private JButton btnUpdate = new JButton("Modo update");
    private boolean modoUpdate = false;

    public PanelMovimiento(Frame frame, int porcentaje, String titulo) {
        super(frame, porcentaje, titulo);
        cargarPanelRegistro();
        cargarPanelDatos(dao);
        llenarCombox();
    }

    @Override
    public void leerDatos(DefaultTableModel modelo) {
        modelo.setRowCount(0);
        for (Movimientos e: dao.getAll()) {
            String nombre = tipoDao.getNombre(e.tipo_id());
            modelo.addRow(e.getDatos(nombre));
        }
    }

    @Override
    public void cargarPanelRegistro() {
        int x = super.frame.getPanelPrincipal().getSize().width / 2;
        int y = 60;
        int altura = 30;
        int ancho = 220;
        int espaciadoY = 40;
        int anchoBtn = 100;

        btnUpdate.setBounds(super.frame.getPanelPrincipal().getSize().width - 150, 0, 150, 30);
        btnUpdate.setBackground(Color.red);

        lbId.setBounds(x-(ancho), y, ancho, altura);
        lbId.setHorizontalAlignment(JLabel.RIGHT);
        txtId.setBounds(x, y, ancho,altura);
        y += espaciadoY;
        lbNombre.setBounds(x-ancho, y, ancho, altura);
        lbNombre.setHorizontalAlignment(JLabel.RIGHT);
        txtNombre.setBounds(x, y, ancho,altura);
        y += espaciadoY;
        lbDescripcion.setBounds(x-ancho, y, ancho, altura);
        lbDescripcion.setHorizontalAlignment(JLabel.RIGHT);
        txtDescripcion.setBounds(x, y, ancho,altura);
        y += espaciadoY;
        lbDañoCuracion.setBounds(x-ancho, y, ancho, altura);
        lbDañoCuracion.setHorizontalAlignment(JLabel.RIGHT);
        txtDañoCuracion.setBounds(x, y, ancho,altura);
        y += espaciadoY;
        lbTipo.setBounds(x-ancho, y, ancho, altura);
        lbTipo.setHorizontalAlignment(JLabel.RIGHT);
        comboBox.setBounds(x, y, ancho,altura);
        y += espaciadoY;
        btnRegistrar.setBounds(x - (anchoBtn), y, anchoBtn*2, altura);

        btnRegistrar.addActionListener(e -> {

            String aux = txtId.getText().trim();
            Integer id = null;

            if (!aux.isEmpty()){
                id = Integer.parseInt(aux);
            }

            String nombre = txtNombre.getText().trim();
            String descripcion = txtDescripcion.getText().trim();

            aux = txtDañoCuracion.getText().trim();
            if (aux.isEmpty()){
                JOptionPane.showMessageDialog(null, "Ingrese la cantidad de daño o de curacion que hace el ataque");
                return;
            }
            int dañoCuracion = Integer.parseInt(aux);

            if (comboBox.getSelectedItem() == null){
                JOptionPane.showMessageDialog(null, "Seleccione un tipo para el ataque");
                return;
            }

            Tipo tipoSeleccionado = (Tipo) comboBox.getSelectedItem();
            int tipo_id = tipoSeleccionado.id();

            Movimientos movimiento = new Movimientos(id, nombre, descripcion, dañoCuracion, tipo_id);

            try {
                dao.insert(movimiento);
                JOptionPane.showMessageDialog(null, "Se registro con exito");
                leerDatos(super.defaultTableModel);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
                return;
            }

            vaciarCampos();
        });

        btnUpdate.addActionListener(e -> {
            modoUpdate = !modoUpdate;

            lbId.setVisible(modoUpdate);
            txtId.setVisible(modoUpdate);

            if (modoUpdate){
                btnRegistrar.setText("Actualizar");
                btnUpdate.setBackground(Color.green);
            } else {
                btnRegistrar.setText("Registrar");
                btnUpdate.setBackground(Color.red);
            }

            vaciarCampos();
        });

        lbId.setVisible(false);
        txtId.setVisible(false);

        super.panelRegistro.add(lbId);
        super.panelRegistro.add(lbNombre);
        super.panelRegistro.add(lbDescripcion);
        super.panelRegistro.add(lbDañoCuracion);
        super.panelRegistro.add(lbTipo);
        super.panelRegistro.add(txtId);
        super.panelRegistro.add(txtNombre);
        super.panelRegistro.add(txtDescripcion);
        super.panelRegistro.add(txtDañoCuracion);
        super.panelRegistro.add(comboBox);
        super.panelRegistro.add(btnRegistrar);
        super.panelRegistro.add(btnUpdate);
    }

    private void llenarCombox(){
        for (Tipo t : tipoDao.getAll()){
            comboBox.addItem(t);
        }
        comboBox.setSelectedItem(null);
    }

    private void vaciarCampos(){
        txtId.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtDañoCuracion.setText("");
    }
}
