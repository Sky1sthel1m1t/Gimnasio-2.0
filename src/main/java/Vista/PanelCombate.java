package Vista;

import DAO.EnfrentamientoDao;
import DAO.EntrenadorDao;
import DAO.MovimientosDao;
import DAO.PokemonDao;
import Modelo.*;
import Visuales.Fuentes;
import Visuales.ImageLoader;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PanelCombate extends JPanel implements Runnable {

    private Frame frame;
    private DefaultTableModel modelo1 = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private JTable table1;
    private JTable table2;
    private JScrollPane scrollPane1;
    private JScrollPane scrollPane2;
    private JLabel lbCi1 = new JLabel("CI del Entrenador 1: ");
    private JLabel lbCi2 = new JLabel("CI del Entrenador 2: ");
    private JLabel lbPokemon1 = new JLabel("Seleccionar pokemon del entrenador 1: ");
    private JLabel lbPokemon2 = new JLabel("Seleccionar pokemon del entrenador 2: ");
    private JTextField txtCi1 = new JTextField();
    private JTextField txtCi2 = new JTextField();
    private JButton btnEmpezarCombate = new JButton("Empezar combate");
    private JButton btnVolver = new JButton("Volver");

    private JLabel lbNombre1 = new JLabel();
    private JLabel lbNombre2 = new JLabel();
    private JLabel lbNombrePokemon1 = new JLabel();
    private JLabel lbNombrePokemon2 = new JLabel();
    private JLabel lbVidaPokemon1 = new JLabel();
    private JLabel lbVidaPokemon2 = new JLabel();
    private JTextPane textPane = new JTextPane();

    private JPanel panelPrepararCombate = new JPanel();
    private JPanel panelMostrarCombate = new JPanel();

    private PokemonDao pokemonDao = new PokemonDao();
    private EntrenadorDao entrenadorDao = new EntrenadorDao();
    private MovimientosDao movimientosDao = new MovimientosDao();
    private EnfrentamientoDao enfrentamientoDao = new EnfrentamientoDao();

    private Entrenador entrenador1;
    private Entrenador entrenador2;
    private Pokemon pokemon1;
    private Pokemon pokemon2;
    private int numTurno;
    private int combateId;
    private ArrayList<Movimientos> pokeMov1;
    private ArrayList<Movimientos> pokeMov2;
    private Equipo equipo1;
    private Equipo equipo2;

    public PanelCombate(Frame frame) {
        this.frame = frame;
        llenarModelo();
        init1();
    }

    public void init1() {
        this.setLayout(null);
        this.setSize(frame.getPanelPrincipal().getSize());
        this.setPreferredSize(this.getSize());

        panelPrepararCombate.setLayout(null);
        panelMostrarCombate.setLayout(null);

        int mitad = this.getHeight() / 2;
        int anchoTotal = this.getWidth();

        panelPrepararCombate.setBounds(0, 0, anchoTotal, mitad);
        panelPrepararCombate.setSize(new Dimension(anchoTotal, mitad));
        panelPrepararCombate.setPreferredSize(new Dimension(anchoTotal, mitad));
        panelPrepararCombate.setBackground(AbstractPanel.getFondoPanel());
        panelMostrarCombate.setBounds(0, mitad, anchoTotal, mitad);
        panelMostrarCombate.setSize(new Dimension(anchoTotal, mitad));
        panelMostrarCombate.setPreferredSize(new Dimension(anchoTotal, mitad));
        panelMostrarCombate.setVisible(false);

        // Falta hacer mas bonito el mostrar combate

        int x = this.panelPrepararCombate.getWidth() / 2;
        int y = 10;
        int altura = 30;
        int alturaTablas = 200;
        int anchoTablas = 480;
        int espacioY = altura + 10;
        int anchoBtn = 150;

        JLabel lbTitulo = new JLabel("Organizar Combate");
        int anchoTitulo = 400;
        int altoTitulo = 70;

        btnVolver.setBounds(0, 0, 75, 30);
        lbTitulo.setBounds(x - (anchoTitulo/2), y, anchoTitulo, altoTitulo);
        lbTitulo.setFont(Fuentes.getFuentes().getSolid(35));
        lbTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        y += altoTitulo + 10;
        lbCi1.setBounds(x - (anchoTablas), y, anchoTablas, altura);
        y += espacioY;
        txtCi1.setBounds(x - (anchoTablas), y, anchoTablas, altura);
        y -= espacioY;
        lbCi2.setBounds(x + 5, y, anchoTablas, altura);
        y += espacioY;
        txtCi2.setBounds(x + 5, y, anchoTablas, altura);
        y += espacioY;
        lbPokemon1.setBounds(x - (anchoTablas), y, anchoTablas, altura);
        y += espacioY;
        scrollPane1.setBounds(x - anchoTablas, y, anchoTablas, alturaTablas);
        y -= espacioY;
        lbPokemon2.setBounds(x + 5, y, anchoTablas, altura);
        y += espacioY;
        scrollPane2.setBounds(x + 5, y, anchoTablas, alturaTablas);
        y += alturaTablas + 10;
        btnEmpezarCombate.setBounds(x - (anchoBtn / 2), y, anchoBtn, altura);

        btnVolver.addActionListener(e -> {
            frame.volver();
        });

        btnEmpezarCombate.addActionListener(e -> {

            int fila1 = table1.getSelectedRow();
            int fila2 = table2.getSelectedRow();

            if (fila1 < 0 || fila2 < 0){
                JOptionPane.showMessageDialog(null, "Un entrenador no ha seleccionado un pokemon");
                return;
            }

            if (fila1 == fila2) {
                JOptionPane.showMessageDialog(null,
                        "Ambos entrenadores seleccionaron a un mismo pokemon, uno de ellos debe seleccionar otro");
                return;
            } else {
                int idPokemon1 = Integer.parseInt((String) modelo1.getValueAt(fila1, 0));
                int idPokemon2 = Integer.parseInt((String) modelo1.getValueAt(fila2, 0));

                pokemon1 = pokemonDao.get(idPokemon1);
                pokemon2 = pokemonDao.get(idPokemon2);
            }

            String ci1 = txtCi1.getText().trim();
            String ci2 = txtCi2.getText().trim();

            Pattern pattern = Pattern.compile("^\\d+$");
            Matcher matcher1 = pattern.matcher(ci1);
            Matcher matcher2 = pattern.matcher(ci2);

            // Validaciones
            if (matcher1.find() && matcher2.find()) {
                entrenador1 = entrenadorDao.get(ci1);
                entrenador2 = entrenadorDao.get(ci2);

                if (ci1.equals(ci2)){
                    JOptionPane.showMessageDialog(null, "Un entrenador no se puede enfrentar asi mismo");
                    return;
                }

                if (entrenador1 == null){
                    JOptionPane.showMessageDialog(null, "No se ha encontrado al entrenador 1, " +
                            "verifique que el CI sea el correcto");
                    return;
                }

                if (entrenador2 == null){
                    JOptionPane.showMessageDialog(null, "No se ha encontrado al entrenador 2, " +
                            "verifique que el CI sea el correcto");
                    return;
                }

            } else {
                JOptionPane.showMessageDialog(null,
                        "Los CI de cada entrenador solo contienen numeros, sin espacios entre ellos");
                return;
            }

            pokeMov1 = movimientosDao.getMovimientosPokemon(pokemon1.id());
            pokeMov2 = movimientosDao.getMovimientosPokemon(pokemon2.id());

            // Preparando el inicio del combate

            asignarEquipos();
            init2();
            combateId = enfrentamientoDao.getSiguienteID();

            Thread hilo = new Thread(this);
            hilo.start();
        });

        panelPrepararCombate.add(lbCi1);
        panelPrepararCombate.add(txtCi1);
        panelPrepararCombate.add(scrollPane1);
        panelPrepararCombate.add(lbCi2);
        panelPrepararCombate.add(txtCi2);
        panelPrepararCombate.add(scrollPane2);
        panelPrepararCombate.add(btnEmpezarCombate);
        panelPrepararCombate.add(btnVolver);
        panelPrepararCombate.add(lbPokemon1);
        panelPrepararCombate.add(lbPokemon2);
        panelPrepararCombate.add(lbTitulo);
        this.add(panelMostrarCombate);
        this.add(panelPrepararCombate);
    }

    private Movimientos getAtaque(ArrayList<Movimientos> m){
        int num = (int) (Math.random() * m.size());
        return m.get(num);
    }

    public void asignarEquipos(){
        int num = (int) (Math.random() * 2);
        if (num == 0){
            equipo1 = new Equipo(pokemon1, entrenador1, pokeMov1);
            equipo2 = new Equipo(pokemon2, entrenador2, pokeMov2);
        } else {
            equipo2 = new Equipo(pokemon1, entrenador1, pokeMov1);
            equipo1 = new Equipo(pokemon2, entrenador2, pokeMov2);
        }
        numTurno = 0;
    }

    public void init2(){
        int ancho = 200;
        int anchoTotal = panelMostrarCombate.getWidth();
        int alturaTotal = panelMostrarCombate.getHeight();
        int alto = 30;
        int y = 0;
        int espacio = alto + 10;
        JLabel lbVersus = new JLabel();
        Font fuente = Fuentes.getFuentes().getSolid(20);

        lbNombre1.setBounds(0, y, ancho, alto);
        lbNombre1.setFont(fuente);
        lbNombre2.setBounds( anchoTotal - ancho, y,ancho,alto);
        lbNombre2.setFont(fuente);
        lbNombre2.setHorizontalAlignment(SwingConstants.RIGHT);
        y += espacio;
        lbNombrePokemon1.setBounds(0,y, ancho, alto);
        lbNombrePokemon1.setFont(fuente);
        lbNombrePokemon2.setBounds(anchoTotal - ancho, y, ancho, alto);
        lbNombrePokemon2.setHorizontalAlignment(SwingConstants.RIGHT);
        lbNombrePokemon2.setFont(fuente);
        y += espacio;
        lbVidaPokemon1.setBounds(0,y,ancho,alto);
        lbVidaPokemon1.setFont(fuente);
        lbVidaPokemon2.setBounds(anchoTotal - ancho, y,ancho,alto);
        lbVidaPokemon2.setFont(fuente);
        lbVidaPokemon2.setHorizontalAlignment(SwingConstants.RIGHT);
        y += espacio;
        textPane.setBounds(0,y,anchoTotal, alturaTotal - y);

        lbNombre1.setText(entrenador1.nombre() + " " + entrenador1.apellido());
        lbNombre2.setText(entrenador2.nombre() + " " + entrenador2.apellido());
        lbNombrePokemon1.setText(pokemon1.nombre());
        lbNombrePokemon2.setText(pokemon2.nombre());
        lbVidaPokemon1.setText("PS: " + pokemon1.saludActual() + "/" + pokemon1.saludMaxima());
        lbVidaPokemon2.setText("PS: " + pokemon2.saludActual() + "/" + pokemon2.saludMaxima());

        textPane.setEditable(false);

        lbVersus.setBounds((anchoTotal / 2) - (ancho/2), 0, 150, textPane.getY());
        String path = "Imagenes/versus.png";
        lbVersus.setIcon(ImageLoader.getImagen(path, lbVersus.getWidth(), lbVersus.getHeight()));

        panelMostrarCombate.add(lbNombre1);
        panelMostrarCombate.add(lbVidaPokemon1);
        panelMostrarCombate.add(lbNombrePokemon1);
        panelMostrarCombate.add(lbNombre2);
        panelMostrarCombate.add(lbVidaPokemon2);
        panelMostrarCombate.add(lbNombrePokemon2);
        panelMostrarCombate.add(textPane);
        panelMostrarCombate.add(lbVersus);
        panelMostrarCombate.setVisible(true);
    }

    public void llenarModelo() {
        for (String s : pokemonDao.getColumnas()) {
            modelo1.addColumn(s);
        }
        modelo1.addColumn("tipos");

        llenarDatos();

        table1 = new JTable(modelo1);
        table2 = new JTable(modelo1);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < table1.getColumnCount(); i++) {
            table1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            table2.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        scrollPane1 = new JScrollPane(table1);
        scrollPane2 = new JScrollPane(table2);
    }

    private void llenarDatos(){
        modelo1.setRowCount(0);
        for (Pokemon p : pokemonDao.getAll()) {
            if (p.disponibilidad()) {
                modelo1.addRow(p.getDatos());
            }
        }
    }

    private void nuevaLinea(){
        try {
            textPane.getStyledDocument().insertString(
                    textPane.getStyledDocument().getLength(),
                    System.getProperty("line.separator"), null);
        } catch (BadLocationException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void textoNegrita(SimpleAttributeSet attrs, String texto, int tamaño){
        StyleConstants.setBold(attrs, true);
        StyleConstants.setFontSize(attrs, tamaño);
        try {
            textPane.getStyledDocument().insertString(
                    textPane.getStyledDocument().getLength(), texto, attrs);
        } catch (BadLocationException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void textoNormal(SimpleAttributeSet attrs, String texto, int tamaño){
        StyleConstants.setBold(attrs, false);
        StyleConstants.setFontSize(attrs, tamaño);
        try {
            textPane.getStyledDocument().insertString(
                    textPane.getStyledDocument().getLength(), texto, attrs);
        } catch (BadLocationException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void textoGanador(SimpleAttributeSet attrs, String texto, int tamaño){
        StyleConstants.setBold(attrs, true);
        StyleConstants.setFontSize(attrs, tamaño);
        StyleConstants.setAlignment(attrs, SwingConstants.CENTER);
        try {
            textPane.getStyledDocument().insertString(
                    textPane.getStyledDocument().getLength(), texto, attrs);
        } catch (BadLocationException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void run() {
        Enfrentamiento enfrentamiento = new Enfrentamiento(
                pokemon1.id(), pokemon2.id(), entrenador1.ci(), entrenador2.ci(), combateId);
        enfrentamientoDao.iniciarEnfrentamiento(enfrentamiento);
        SimpleAttributeSet attrs = new SimpleAttributeSet();
        int tamañoTextoNormal = 15;
        int tamañoTextoGanador = 18;

        textoNegrita(attrs, equipo1.e().nombre() + " y " + equipo1.p().nombre() + " entran a la arena combate", tamañoTextoNormal);
        nuevaLinea();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        textoNegrita(attrs, equipo2.e().nombre() + " y " + equipo2.p().nombre() + " entran a la arena combate", tamañoTextoNormal);
        nuevaLinea();

        boolean esDerrotado1 = true;
        boolean esDerrotado2 = true;

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        while (esDerrotado1 && esDerrotado2){
            numTurno++;
            String aux;
            String nombreNegrita;

            if (numTurno % 2 == 1){
                enfrentamiento = new Enfrentamiento
                        (equipo1.p().id(), equipo2.p().id(), equipo1.e().ci(), equipo2.e().ci(), combateId);
                Movimientos movimiento = getAtaque(equipo1.movimientos());
                enfrentamientoDao.realizarMovimiento(enfrentamiento, movimiento, numTurno, combateId);
                nombreNegrita = equipo1.e().nombre() + ": ";
                aux = equipo1.p().nombre() + " usa " + movimiento.nombre();
            } else {
                enfrentamiento = new Enfrentamiento
                        (equipo2.p().id(), equipo1.p().id(), equipo2.e().ci(), equipo1.e().ci(), combateId);
                Movimientos movimiento = getAtaque(equipo2.movimientos());
                enfrentamientoDao.realizarMovimiento(enfrentamiento, movimiento, numTurno, combateId);
                nombreNegrita = equipo2.e().nombre() + ": ";
                aux = equipo2.p().nombre() + " usa " + movimiento.nombre();
            }

            lbVidaPokemon1.setText("PS: " + pokemonDao.getvidaActual(pokemon1.id()) + "/" + pokemon1.saludMaxima());
            lbVidaPokemon2.setText("PS: " + pokemonDao.getvidaActual(pokemon2.id()) + "/" + pokemon2.saludMaxima());
            textoNegrita(attrs, nombreNegrita, tamañoTextoNormal);
            textoNormal(attrs, aux, tamañoTextoNormal);
            nuevaLinea();

            esDerrotado1 = !pokemonDao.esDerrotado(equipo1.p().id());
            esDerrotado2 = !pokemonDao.esDerrotado(equipo2.p().id());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if (pokemonDao.esDerrotado(equipo1.p().id())){
            textoNegrita(attrs, equipo1.p().nombre() + " no puede continuar", tamañoTextoNormal);
            nuevaLinea();
            textoGanador(attrs, equipo2.e().nombre() + " y " + equipo2.p().nombre() + " ganan el combate", tamañoTextoGanador);
        }

        if (pokemonDao.esDerrotado(equipo2.p().id())){
            textoNegrita(attrs, equipo2.p().nombre() + " no puede continuar", tamañoTextoNormal);
            nuevaLinea();
            textoGanador(attrs, equipo1.e().nombre() + " y " + equipo1.p().nombre() + " ganan el combate",tamañoTextoGanador);
        }

        llenarDatos();
    }
}
