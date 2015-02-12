package jvm.jirc.client.ui.loading;

import jvm.jirc.client.entity.manager.ProfileManager;
import jvm.jirc.client.net.Connection;
import jvm.jirc.client.res.Res;
import jvm.jirc.client.ui.client.ClientWindow;
import jvm.jirc.client.util.Utils;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;

public class LoadingWindow extends JFrame {

    private static final String DEFAULT_INFO = "Be patient as we are getting things ready!";

    private static LoadingWindow instance;

    private final JLabel infoLabel;

    public LoadingWindow(){
        super("JIRC - Loading...");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setUndecorated(true);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        setLayout(new BorderLayout());

        infoLabel = new JLabel(DEFAULT_INFO, JLabel.CENTER);
        infoLabel.setFont(infoLabel.getFont().deriveFont(35f));

        add(new JLabel(Res.DOG_256, JLabel.CENTER), BorderLayout.CENTER);
        add(infoLabel, BorderLayout.SOUTH);

        pack();
    }

    private void info(final String info){
        SwingUtilities.invokeLater(() -> {
            infoLabel.setText(info);
            infoLabel.repaint();
        });
    }

    private void initFinished(){
        info("Get Ready To JIRC, " + ProfileManager.getInstance().getMyProfile().getUser());
        new Thread(() -> {
            Utils.hide(this);
            info(DEFAULT_INFO);
            if(!Connection.connected())
                return;
            ClientWindow.showNow();
        }).start();
    }

    public static LoadingWindow getInstance(){
        if(instance == null)
            instance = new LoadingWindow();
        return instance;
    }

    public static boolean isCreated(){
        return instance != null;
    }

    public static void showNow(){
        SwingUtilities.invokeLater(() -> Utils.show(getInstance()));
    }

    public static void ready(){
        SwingUtilities.invokeLater(() -> getInstance().initFinished());
    }
}
