package jvm.jirc.client.util;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public final class Utils {

    private Utils(){}

    public static void show(final int type, final String title, final String msg){
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, msg, title, type));
    }

    public static void showMsg(final String title, final String msg){
        show(JOptionPane.PLAIN_MESSAGE, title, msg);
    }

    public static void showInfo(final String title, final String msg){
        show(JOptionPane.INFORMATION_MESSAGE, title, msg);
    }

    public static void showWarning(final String title, final String msg){
        show(JOptionPane.WARNING_MESSAGE, title, msg);
    }

    public static void showError(final String title, final String msg){
        show(JOptionPane.ERROR_MESSAGE, title, msg);
    }

    public static boolean isValidChars(final String str, final String exceptions){
        for(final char c : str.toCharArray())
            if(!Character.isLetterOrDigit(c) && exceptions.indexOf(c) < 0)
                return false;
        return true;
    }

    public static boolean isValidLength(final int length, final int minLength, final int maxLength){
        return length >= minLength && length <= maxLength;
    }

    public static void hide(final JFrame window){
        SwingUtilities.invokeLater(() -> window.setVisible(false));
    }

    public static void show(final JFrame window){
        SwingUtilities.invokeLater(() -> {
            window.setLocationRelativeTo(null);
            window.setVisible(true);
            window.toFront();
        });
    }
}
