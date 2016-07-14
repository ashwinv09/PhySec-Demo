import javax.swing.*;
import java.awt.*;

/**
 * Created by ashwinv on 24.05.16 at 15:03.
 */
public class NodePropertiesFrame extends JDialog {
    public NodePropertiesFrame(Frame owner) {
        super(owner);
        setTitle("Properties");
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new BorderLayout()) {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, Color.WHITE, getWidth(), 0, getBackground()));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }
}
