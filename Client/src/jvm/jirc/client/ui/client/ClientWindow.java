package jvm.jirc.client.ui.client;

import jvm.jirc.client.entity.manager.ProfileManager;
import jvm.jirc.client.net.Connection;
import jvm.jirc.client.res.Res;
import jvm.jirc.client.util.Utils;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientWindow extends JFrame {

    private static ClientWindow instance;

    public ClientWindow(){
        super("JIRC");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(final WindowEvent e){
                        Connection.disconnect();
                    }
                }
        );
        setLayout(new BorderLayout());

        add(new JLabel(Res.DOG_256), BorderLayout.CENTER);

        pack();
    }

    private void init(){
        setTitle("JIRC - " + ProfileManager.getInstance().getMyProfile().getUser());
        //update everything
    }

    public static ClientWindow getInstance(){
        if(instance == null)
            instance = new ClientWindow();
        return instance;
    }

    public static boolean isCreated(){
        return instance != null;
    }

    public static void showNow(){
        SwingUtilities.invokeLater(() -> {
            Utils.show(getInstance());
            getInstance().init();
        });
    }
}
