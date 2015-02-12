package jvm.jirc.client.res;

import javax.swing.ImageIcon;

public final class Res {

    public static final ImageIcon DOG_256 = icon("dog", 256);

    private Res(){}

    private static ImageIcon icon(final String name, final int size){
        return new ImageIcon(Res.class.getResource(String.format("img/%s_%d.png", name, size)));
    }
}
