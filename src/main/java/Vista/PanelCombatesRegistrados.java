package Vista;

import DAO.CombatesDao;
import Modelo.Combate;
import Modelo.Historial;
import Modelo.Movimientos;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class PanelCombatesRegistrados extends AbstractPanel<CombatesDao>{

    private JLabel lbTitulo = new JLabel("Combates Registrados");

    private DefaultTableModel modelo = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private JTable tablaHistorial;
    private JScrollPane scrollPaneHistorial;

    private CombatesDao combatesDao = new CombatesDao();


    public PanelCombatesRegistrados(Frame frame, int porcentaje) {
        super(frame, porcentaje);
        cargarPanelDatos(combatesDao);
        cargarPanelRegistro();
        agregarDobleClic();
        initHistorial();
    }

    @Override
    public void leerDatos(DefaultTableModel modelo) {
        modelo.setRowCount(0);
        for (Combate c:  combatesDao.getAll()) {
            modelo.addRow(c.getDatos());
        }
    }

    public void initHistorial(){
        modelo.addColumn("Numero de turno");
        modelo.addColumn("Entrenador");
        modelo.addColumn("Pokemon");
        modelo.addColumn("Vida Actual");
        modelo.addColumn("Movimiento");
        modelo.addColumn("Tipo de accion");

        tablaHistorial = new JTable(modelo);
        scrollPaneHistorial = new JScrollPane(tablaHistorial);
    }

    public void mostrarHistorial(int id){
        modelo.setRowCount(0);
        for (Historial h: combatesDao.getHistorial(id)) {
            modelo.addRow(h.getDatos());
        }
    }

    public void agregarDobleClic(){
        super.jTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int fila = jTable.getSelectedRow();

                    int id = Integer.parseInt((String) defaultTableModel.getValueAt(fila, 0));

                    mostrarHistorial(id);
                    UIManager.put("OptionPane.minimumSize", new Dimension(750,400));
                    JOptionPane.showMessageDialog(null, scrollPaneHistorial, "Historial de combate", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    @Override
    public void cargarPanelRegistro() {
        int x = panelRegistro.getWidth() / 2;
        int y = panelRegistro.getHeight() / 2;
        int ancho = 200;
        int altura = 50;

        lbTitulo.setHorizontalAlignment(JLabel.CENTER);
        lbTitulo.setBounds(x-(ancho / 2), y - (altura / 2),  ancho, altura);

        panelRegistro.add(lbTitulo);
    }
}
