package Vista;

import DAO.AbstractDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public abstract class AbstractPanel<E extends AbstractDao> extends JPanel {
    protected DefaultTableModel defaultTableModel = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    protected JTable jTable;
    protected JScrollPane scrollPane;

    protected JPanel panelDatos;
    protected JPanel panelRegistro;
    protected Frame frame;

    private int porcentaje;
    private JButton btnVolver = new JButton("Volver");

    public AbstractPanel(Frame frame, int porcentaje) {
        this.frame = frame;
        this.porcentaje = porcentaje;
        this.setPreferredSize(frame.getPanelPrincipal().getSize());
        initPaneles();
    }

    private void initPaneles(){
        this.setLayout(null);

        this.panelDatos = new JPanel();
        this.panelRegistro = new JPanel();
        int ancho = frame.getPanelPrincipal().getSize().width;
        int altura = (frame.getPanelPrincipal().getSize().height * porcentaje) / 100;
        btnVolver.setBounds(0,0,100,30);

        btnVolver.addActionListener(e -> frame.volver());

        this.setSize(frame.getPanelPrincipal().getSize());
        panelRegistro.setBounds(0,0, ancho, altura);
        panelDatos.setBounds(0, altura, ancho, (frame.getPanelPrincipal().getSize().height - altura));
        panelRegistro.add(btnVolver);

        panelRegistro.setLayout(null);
        panelDatos.setLayout(null);

        this.add(panelDatos);
        this.add(panelRegistro);
    }

    public void cargarPanelDatos(E valor){
        ArrayList<String> columnas = valor.getColumnas();

        for (String s: columnas) {
            defaultTableModel.addColumn(s);
        }

        leerDatos(defaultTableModel);

        jTable = new JTable(defaultTableModel);
        scrollPane = new JScrollPane(jTable);

        scrollPane.setBounds(0,0,panelDatos.getWidth(), panelDatos.getHeight());

        panelDatos.add(scrollPane);
    }

    public abstract void leerDatos(DefaultTableModel modelo);

    public abstract void cargarPanelRegistro();
}
