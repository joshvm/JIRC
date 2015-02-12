package jvm.jirc.client;

import jvm.jirc.client.ui.registerlogin.RegisterLoginWindow;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.GraphiteGlassSkin;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Application {

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
            SubstanceLookAndFeel.setSkin(new GraphiteGlassSkin());
            RegisterLoginWindow.showNow();
        });
    }
}
