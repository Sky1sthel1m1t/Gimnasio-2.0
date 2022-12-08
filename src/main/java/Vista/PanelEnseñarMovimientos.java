package Vista;

import DAO.MovimientosDao;
import DAO.PokemonDao;
import DAO.TipoDao;
import Modelo.Movimientos;
import Modelo.Pokemon;
import Modelo.Tipo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class PanelEnseñarMovimientos extends JPanel {

    private DefaultTableModel modeloPokemones = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private DefaultTableModel modeloMovimientos = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private DefaultTableModel modeloMovientosAgregados = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private JTable tablePokemones = new JTable();
    private JTable tableMovimientos = new JTable();
    private JTable tableSeccionados = new JTable();
    private JScrollPane scrollPanePokemones = new JScrollPane();
    private JScrollPane scrollPaneMovimientos = new JScrollPane();
    private JScrollPane scrollPaneSeccionados = new JScrollPane();

    private JLabel lbPokemon = new JLabel("Selecciona al pokemon que quieres enseñarle movimientos: ");
    private JLabel lbMovimientos = new JLabel("Aqui saldran los movimientos asignables: ");
    private JLabel lbSeleccionados = new JLabel("Estos son los movimientos que aprendera el pokemon: ");

    private JButton btnVolver = new JButton("Volver");

    private PokemonDao pokemonDao = new PokemonDao();
    private MovimientosDao movimientosDao = new MovimientosDao();
    private TipoDao tipoDao = new TipoDao();

    private Frame frame;
    private Pokemon pokemonSeleccionado;

    public PanelEnseñarMovimientos(Frame frame) {
        this.frame = frame;
        this.setSize(frame.getPanelPrincipal().getSize());
        this.setPreferredSize(frame.getPanelPrincipal().getSize());
        llenarColumnas();
        llenarDatosPokemon();
        init1();
    }

    public void init1(){
        this.setLayout(null);
        int mitadAncho = this.getWidth() / 2;
        int mitadAltura = this.getHeight() / 2;
        int anchoTablas = 400;
        int alturaTablas = 300;
        int y = 100;

        btnVolver.setBounds(0,0,75, 30);

        lbPokemon.setBounds(mitadAncho - anchoTablas, y, anchoTablas, 30);
        lbMovimientos.setBounds(mitadAncho + 10, y, anchoTablas, 30);
        y+=40;
        scrollPanePokemones.setBounds(mitadAncho - anchoTablas, y, anchoTablas, alturaTablas);
        scrollPaneMovimientos.setBounds(mitadAncho + 10, y, anchoTablas, alturaTablas);
        y+= alturaTablas + 10;
        lbSeleccionados.setBounds(mitadAncho - (anchoTablas/2), y, anchoTablas, 30);
        y+=40;
        scrollPaneSeccionados.setBounds(mitadAncho - (anchoTablas/2), y, anchoTablas, alturaTablas);

        btnVolver.addActionListener(e -> {
            frame.volver();
        });

        tablePokemones.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int fila = tablePokemones.getSelectedRow();

                    int pokemonId = Integer.parseInt((String) modeloPokemones.getValueAt(fila,0));
                    pokemonSeleccionado = pokemonDao.get(pokemonId);

                    ArrayList<Integer> aux = pokemonDao.getTipos(pokemonId);
                    Integer[] tipos = new Integer[aux.size()];
                    tipos = aux.toArray(tipos);

                    llenarDatosMovimientos(tipos);
                    llenarDatosMovAprendidos(pokemonSeleccionado.id());
                }
            }
        });

        tableMovimientos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int fila = tableMovimientos.getSelectedRow();

                    int movId = Integer.parseInt((String) modeloMovimientos.getValueAt(fila,0));

                    Movimientos movimientos = movimientosDao.get(movId);

                    String mensaje = "Desea que " + pokemonSeleccionado.nombre() + " aprenda " + movimientos.nombre() + "?";

                    int respuesta = JOptionPane.showConfirmDialog(null,mensaje);

                    if (respuesta == JOptionPane.YES_OPTION){
                        try {
                            pokemonDao.aprenderMovimiento(pokemonSeleccionado.id(), movimientos.id());
                            JOptionPane.showMessageDialog(null,
                                    "El pokemon ha aprendido con exito el movimiento seleccionado");
                            llenarDatosMovAprendidos(pokemonSeleccionado.id());
                            llenarDatosPokemon();
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage());
                        }
                    }
                }
            }
        });

        tableSeccionados.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int fila = tableSeccionados.getSelectedRow();

                    int movId = Integer.parseInt((String) modeloMovientosAgregados.getValueAt(fila,0));

                    Movimientos movimientos = movimientosDao.get(movId);

                    String mensaje = "Desea que " + pokemonSeleccionado.nombre() + " olvide " + movimientos.nombre() + "?";

                    int respuesta = JOptionPane.showConfirmDialog(null,mensaje);

                    if (respuesta == JOptionPane.YES_OPTION){
                        try {
                            pokemonDao.olvidarMovimiento(pokemonSeleccionado.id(), movimientos.id());
                            JOptionPane.showMessageDialog(null,
                                    "El pokemon ha olvidado con exito el movimiento seleccionado");
                            llenarDatosMovAprendidos(pokemonSeleccionado.id());
                            llenarDatosPokemon();
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, ex.getMessage());
                        }
                    }
                }
            }
        });

        this.add(lbPokemon);
        this.add(lbMovimientos);
        this.add(lbSeleccionados);
        this.add(scrollPaneMovimientos);
        this.add(scrollPanePokemones);
        this.add(scrollPaneSeccionados);
        this.add(btnVolver);
    }

    public void llenarColumnas(){
        for (String s : pokemonDao.getColumnas()) {
            modeloPokemones.addColumn(s);
        }
        modeloPokemones.addColumn("tipos");

        for (String s : movimientosDao.getColumnas()) {
            modeloMovimientos.addColumn(s);
            modeloMovientosAgregados.addColumn(s);
        }

        tablePokemones = new JTable(modeloPokemones);
        scrollPanePokemones = new JScrollPane(tablePokemones);

        tableMovimientos = new JTable(modeloMovimientos);
        scrollPaneMovimientos = new JScrollPane(tableMovimientos);

        tableSeccionados = new JTable(modeloMovientosAgregados);
        scrollPaneSeccionados = new JScrollPane(tableSeccionados);
    }

    public void llenarDatosPokemon(){
        modeloPokemones.setRowCount(0);
        for (Pokemon p: pokemonDao.getAll()) {
            modeloPokemones.addRow(p.getDatos());
        }
    }

    public void llenarDatosMovimientos(Integer... tipos_id){
        modeloMovimientos.setRowCount(0);
        for (int i = 0; i < tipos_id.length; i++) {
            int tipoId = tipos_id[i];
            for (Movimientos movimientos: movimientosDao.getPorTipo(tipoId)) {
                modeloMovimientos.addRow(movimientos.getDatos(tipoDao.getNombre(tipoId)));
            }
        }
    }

    public void llenarDatosMovAprendidos(int pokemon_id){
        modeloMovientosAgregados.setRowCount(0);
        for (Movimientos m: movimientosDao.getMovimientosPokemon(pokemon_id)) {
            modeloMovientosAgregados.addRow(m.getDatos());
        }
    }
}
