package Vista;

import DAO.AbstractDao;
import Visuales.Fuentes;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
    private static Color fondoPanel = new Color(0xEA7070);

    public AbstractPanel(Frame frame, int porcentaje, String titulo) {
        this.frame = frame;
        this.porcentaje = porcentaje;
        this.setPreferredSize(frame.getPanelPrincipal().getSize());
        initPaneles(titulo);
    }

    private void initPaneles(String titulo){
        this.setLayout(null);
        JLabel lbTitulo = new JLabel(titulo);

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

        int anchoTitulo = 500;
        int altoTitulo = 50;

        lbTitulo.setBounds((panelRegistro.getWidth() / 2) - (anchoTitulo / 2), 10, anchoTitulo, altoTitulo);
        lbTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lbTitulo.setFont(Fuentes.getFuentes().getSolid(35));

        panelRegistro.setLayout(null);
        panelDatos.setLayout(null);
        panelRegistro.setBackground(fondoPanel);
        panelRegistro.add(lbTitulo);

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

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < jTable.getColumnCount(); i++) {
            jTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        scrollPane = new JScrollPane(jTable);

        scrollPane.setBounds(0,0,panelDatos.getWidth(), panelDatos.getHeight());

        panelDatos.add(scrollPane);
    }

    public abstract void leerDatos(DefaultTableModel modelo);

    public abstract void cargarPanelRegistro();

    public static Color getFondoPanel() {
        return fondoPanel;
    }
}
