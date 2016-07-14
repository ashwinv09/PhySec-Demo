import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by ashwinv on 23.05.16 at 10:55.
 */
public class LibTest extends JFrame {
    public JPopupMenu popup;
    public mxGraphComponent graphComponent;
    public LibTest() {
        super("PhySec Demonstrator Lib Test");

        popup = new JPopupMenu();
        ActionListener menuListener = new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.out.println("Popup menu item [" +
                        event.getActionCommand() + "] was pressed.");
            }
        };

        JMenuItem item;
        popup.add(item = new JMenuItem("Left"));
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.addActionListener(menuListener);
        popup.add(item = new JMenuItem("Center"));
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.addActionListener(menuListener);
        popup.add(item = new JMenuItem("Right"));
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.addActionListener(menuListener);
        popup.add(item = new JMenuItem("Full"));
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        item.addActionListener(menuListener);
        popup.addSeparator();
        popup.add(item = new JMenuItem("Settings . . ."));
        item.addActionListener(menuListener);

        popup.setLabel("Justification");
        popup.setBorder(new BevelBorder(BevelBorder.RAISED));
//        popup.addPopupMenuListener(new PopupPrintListener());

//        addMouseListener(new MousePopupListener());

        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        try
        {
            Object v1 = graph.insertVertex(parent, null, "Base Station", 20, 20, 80, 30, "defaultVertex;fillColor=green");
            Object v2 = graph.insertVertex(parent, null, "Client1", 240, 150, 80, 30);
            Object v3 = graph.insertVertex(parent, null, "Client2", 80, 120, 80, 30);
            graph.insertEdge(parent, null, "", v1, v2, "defaultEdge;strokeColor=red");
            graph.insertEdge(parent, null, "", v1, v3, "fillColor=red");
        }
        finally
        {
            graph.getModel().endUpdate();
        }

        graphComponent = new mxGraphComponent(graph);
//        graphComponent.setComponentPopupMenu(popup);

        /*MouseListener pop = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mxCell cell = getGraphComponent.getCellAt(e.getX(), e.getY());
                Component c = graphComponent.getComponentAt(e.getX(), e.getY());
                Point p = SwingUtilities.convertPoint(c, e.getX(), e.getY(), graphComponent.getViewport());

                if (graph.getModel().isVertex(cell)) {
                    popup.show(c, (int) p.getX(), (int) p.getY());
                }

                graphComponent.getGraphControl().addMouseListener(pop);
            }
        };*/

        getContentPane().add(graphComponent);

        graphComponent.getGraphControl().addMouseListener(new MousePopupListener());
    }

    class MousePopupListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) { checkPopup(e); }
        public void mouseClicked(MouseEvent e) { checkPopup(e); }
        public void mouseReleased(MouseEvent e) { checkPopup(e); }

        private void checkPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
//                popup.show(LibTest.this, e.getX(), e.getY());
                Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), graphComponent);
                popup.show(graphComponent, pt.x, pt.y);
                e.consume();
            }
        }
    }

    /*class PopupPrintListener implements PopupMenuListener {
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            System.out.println("Popup menu will be visible!");
        }
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            System.out.println("Popup menu will be invisible!");
        }
        public void popupMenuCanceled(PopupMenuEvent e) {
            System.out.println("Popup menu is hidden!");
        }
    }*/

    public static void main(String[] args) {
        LibTest frame  = new LibTest();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 320);
        frame.setVisible(true);
        /*JFrame frame = new JFrame("Popup Menu Example");
        LibTest frame  = new LibTest();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new LibTest());
        frame.setSize(400, 300);
        frame.setVisible(true);*/
    }
}
