package jvm.jirc.client.ui.util;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class LabelComponent<T extends JComponent> extends JPanel{

    private final JLabel label;
    private final T comp;

    public LabelComponent(final String labelText, final int width, final T comp, final int spacing){
        super(new BorderLayout());
        this.comp = comp;

        label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(width, label.getPreferredSize().height));

        final JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
        container.add(label);
        container.add(Box.createHorizontalStrut(spacing));
        container.add(comp);

        add(container, BorderLayout.CENTER);
    }

    public static <C extends JComponent> LabelComponent create(final String labelText, final int width, final C comp, final int spacing){
        return new LabelComponent<>(labelText, width, comp, spacing);
    }

    public static <C extends JComponent> LabelComponent create(final String labelText, final int width, final C comp){
        return create(labelText, width, comp, 10);
    }
}
