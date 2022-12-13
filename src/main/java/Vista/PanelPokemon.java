package Vista;

import DAO.PokemonDao;
import DAO.TipoDao;
import Modelo.Pokemon;
import Modelo.Tipo;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class PanelPokemon extends AbstractPanel<PokemonDao> {

    private PokemonDao dao = new PokemonDao();
    private TipoDao tipoDao = new TipoDao();
    private DefaultListModel<Tipo> model = new DefaultListModel<>();
    private JLabel lbId = new JLabel("Id: ");
    private JLabel lbNombre = new JLabel("Nombre: ");
    private JLabel lbsaludmaxima = new JLabel("Salud Maxima: ");
    private JLabel lbsaludactual = new JLabel("Salud Actual: ");
    private JLabel lbDisponibilidad = new JLabel("Disponibilidad: ");
    private JLabel lbTipos = new JLabel("Tipos: ");
    private JTextField txtId = new JTextField();
    private JTextField txtNombre = new JTextField();
    private JTextField txtsaludmaxima = new JTextField();
    private JTextField txtsaludactual = new JTextField();
    private JCheckBox checkBox = new JCheckBox();
    private JButton btnRegistrar = new JButton("Registrar");
    private JButton btnUpdate = new JButton("Modo Actualizar");
    private JButton btnCurarPokemones = new JButton("Curar a todos los pokemones");
    private JButton btnCurarPokemonesDebilitados = new JButton("Curar a todos los pokemones debilitados");
    private JList<Tipo> lista;
    private ArrayList<Tipo> tiposSeleccionados = new ArrayList<>();
    private boolean modoUpdate = false;

    public PanelPokemon(Frame frame, int porcentaje) {
        super(frame, porcentaje);
        llenarTipos();
        cargarPanelRegistro();
        cargarPanelDatos(dao);
    }

    @Override
    public void leerDatos(DefaultTableModel modelo) {
        modelo.addColumn("tipo");
        modelo.setRowCount(0);
        for (Pokemon p : dao.getAll()) {
            modelo.addRow(p.getDatos());
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
        int alturaLista = 200;

        btnUpdate.setBounds(super.panelRegistro.getWidth() - 150, 0, 150, 30);
        btnUpdate.setBackground(Color.red);

        lbId.setBounds(x - (ancho), y, ancho, altura);
        lbId.setHorizontalAlignment(JLabel.RIGHT);
        txtId.setBounds(x, y, ancho, altura);
        y += espaciadoY;
        lbNombre.setBounds(x - ancho, y, ancho, altura);
        lbNombre.setHorizontalAlignment(JLabel.RIGHT);
        txtNombre.setBounds(x, y, ancho, altura);
        y += espaciadoY;
        lbsaludmaxima.setBounds(x - ancho, y, ancho, altura);
        lbsaludmaxima.setHorizontalAlignment(JLabel.RIGHT);
        txtsaludmaxima.setBounds(x, y, ancho, altura);
        y += espaciadoY;
        lbsaludactual.setBounds(x - ancho, y, ancho, altura);
        lbsaludactual.setHorizontalAlignment(JLabel.RIGHT);
        txtsaludactual.setBounds(x, y, ancho, altura);
        y += espaciadoY;
        lbDisponibilidad.setBounds(x - ancho, y, ancho, altura);
        lbDisponibilidad.setHorizontalAlignment(JLabel.RIGHT);
        checkBox.setBounds(x, y, ancho, altura);
        y += espaciadoY;
        lbTipos.setBounds(x - ancho, y, ancho, altura);
        lbTipos.setHorizontalAlignment(JLabel.RIGHT);
        lista.setBounds(x, y, ancho, alturaLista);
        y += espaciadoY + alturaLista;
        btnRegistrar.setBounds(x - (anchoBtn), y, anchoBtn * 2, altura);
        btnCurarPokemones.setBounds(x - (anchoBtn * 3) - 10, y, anchoBtn * 2, altura);
        btnCurarPokemonesDebilitados.setBounds(x + (anchoBtn) + 10, y, anchoBtn * 2 , altura);

        btnRegistrar.addActionListener(e -> {
            String aux = txtId.getText().trim();
            Integer id = null;

            if (!aux.isEmpty()){
                id = Integer.parseInt(aux);
            }

            String nombre = txtNombre.getText();
            aux = txtsaludmaxima.getText().trim();

            if (aux.isEmpty()){
                JOptionPane.showMessageDialog(null, "Ingrese la salud maxima del pokemon");
                return;
            }

            int saludMaxima = Integer.parseInt(aux);
            aux = txtsaludactual.getText().trim();
            int saludActual = 0;

            if (aux.isEmpty() && modoUpdate){
                JOptionPane.showMessageDialog(null, "Ingrese la vida actual del pokemon");
            } else if (!aux.isEmpty()) {
                saludActual = Integer.parseInt(aux);
            }

            boolean disponibilidad = checkBox.isSelected();

            if (tiposSeleccionados.size() == 0){
                JOptionPane.showMessageDialog(null, "Se debe seleccionar uno o dos tipos para el pokemon");
                return;
            }

            ArrayList<Integer> tipos = new ArrayList<>();
            for (Tipo t: tiposSeleccionados) {
                tipos.add(t.id());
            }

            if (tipos.size() > 2){
                JOptionPane.showMessageDialog(null, "Un pokemon solo puede tener maximo 2 tipos");
                return;
            }

            Pokemon nuevo = new Pokemon(id, nombre, saludMaxima, saludActual, disponibilidad, null, tipos);
            try {
                dao.insert(nuevo);
                JOptionPane.showMessageDialog(null, "Se ha registrado el pokemon correctamente");
                leerDatos(super.defaultTableModel);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        });

        btnUpdate.addActionListener(e -> {
            modoUpdate = !modoUpdate;

            lbId.setVisible(modoUpdate);
            txtId.setVisible(modoUpdate);
            lbsaludactual.setVisible(modoUpdate);
            txtsaludactual.setVisible(modoUpdate);
            lbDisponibilidad.setVisible(modoUpdate);
            checkBox.setVisible(modoUpdate);

            if (modoUpdate){
                btnRegistrar.setText("Actualizar");
                btnUpdate.setBackground(Color.green);
            } else {
                btnRegistrar.setText("Registrar");
                btnUpdate.setBackground(Color.red);
            }
            vaciarCampos();
        });

        btnCurarPokemones.addActionListener(e -> {
            dao.curarPokemones();
            JOptionPane.showMessageDialog(null, "Se han curado a todos los pokemones");
            leerDatos(super.defaultTableModel);
        });

        btnCurarPokemonesDebilitados.addActionListener(e -> {
            dao.curarPokemonesDebilitados();
            JOptionPane.showMessageDialog(null, "Se han curado a todos los pokemones debilitados");
            leerDatos(super.defaultTableModel);
        });

        lista.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (lista.getSelectedValuesList().isEmpty()) {
                    tiposSeleccionados = new ArrayList<>();
                    return;
                }
                tiposSeleccionados = (ArrayList<Tipo>) lista.getSelectedValuesList();
            }
        });

        lbId.setVisible(modoUpdate);
        txtId.setVisible(modoUpdate);
        lbsaludactual.setVisible(modoUpdate);
        txtsaludactual.setVisible(modoUpdate);
        lbDisponibilidad.setVisible(modoUpdate);
        checkBox.setVisible(modoUpdate);

        super.panelRegistro.add(lbId);
        super.panelRegistro.add(lbNombre);
        super.panelRegistro.add(lbsaludactual);
        super.panelRegistro.add(lbsaludmaxima);
        super.panelRegistro.add(lbDisponibilidad);
        super.panelRegistro.add(lbTipos);
        super.panelRegistro.add(txtId);
        super.panelRegistro.add(txtNombre);
        super.panelRegistro.add(txtsaludactual);
        super.panelRegistro.add(txtsaludmaxima);
        super.panelRegistro.add(checkBox);
        super.panelRegistro.add(lista);
        super.panelRegistro.add(btnRegistrar);
        super.panelRegistro.add(btnUpdate);
        super.panelRegistro.add(btnCurarPokemones);
        super.panelRegistro.add(btnCurarPokemonesDebilitados);
    }

    private void vaciarCampos(){
        txtId.setText("");
        txtsaludactual.setText("");
        txtsaludmaxima.setText("");
        txtNombre.setText("");
    }

    private void llenarTipos() {
        for (Tipo t : tipoDao.getAll()) {
            model.addElement(t);
        }
        lista = new JList<>(model);
        lista.setSelectionModel(new DefaultListSelectionModel() {
            @Override
            public void setSelectionInterval(int index0, int index1) {
                if (super.isSelectedIndex(index0)) {
                    super.removeSelectionInterval(index0, index1);
                } else {
                    super.addSelectionInterval(index0, index1);
                }
            }
        });
    }
}
