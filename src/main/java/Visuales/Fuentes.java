package Visuales;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

public class Fuentes {
    private static Fuentes fuentes;
    private Font hollow = null;
    private Font solid = null;

    public Fuentes() {
        cargarHollow();
        cargarSolid();
    }

    public static Fuentes getFuentes(){
        if (fuentes == null){
            fuentes = new Fuentes();
        }
        return fuentes;
    }

    /* Font.PLAIN = 0 , Font.BOLD = 1 , Font.ITALIC = 2
     * tamanio = float
     */
    public Font getHollow(int tamaño) {
        return hollow.deriveFont(Font.PLAIN, tamaño);
    }

    public Font getSolid(int tamaño) {
        return solid.deriveFont(Font.PLAIN, tamaño);
    }

    private void cargarSolid(){
        String fontName = "Fuentes/Pokemon Solid.ttf";
        try {
            //Se carga la fuente
            InputStream is = new BufferedInputStream(new FileInputStream(fontName));
            solid = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (Exception ex) {
            //Si existe un error se carga fuente por defecto ARIAL
            System.err.println(fontName + " No se cargo la fuente");
            solid = new Font("Arial", Font.PLAIN, 14);
        }
    }

    private void cargarHollow(){
        String fontName = "Fuentes/Pokemon Hollow.ttf" ;
        try {
            //Se carga la fuente
            InputStream is = new BufferedInputStream(new FileInputStream(fontName));
            hollow = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (Exception ex) {
            //Si existe un error se carga fuente por defecto ARIAL
            System.err.println(fontName + " No se cargo la fuente");
            hollow = new Font("Arial", Font.PLAIN, 14);
        }
    }
}
