import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by ashwinv on 23.05.16 at 11:21.
 */
public class HomeScreen extends JFrame {
    protected final mxGraphComponent graphComponent;
    protected HomePopupMenu homeMenu;
    public JPopupMenu popup;

    public HomeScreen() throws UnknownHostException {
        super("PhySec Demonstrator");

        homeMenu = new HomePopupMenu(HomeScreen.this);
        popup = new JPopupMenu();

        JMenuItem item;
        popup.add(item = new JMenuItem("Left"));
        item.setHorizontalTextPosition(JMenuItem.RIGHT);
        popup.add(item = new JMenuItem("Center"));
        item.setHorizontalTextPosition(JMenuItem.RIGHT);

        NwElement nBaseStation = new NwElement("Base Station", InetAddress.getByName("192.168.5.101"), "B0:8d:5c:12:4c:86", NetworkElementType.BASE_STATION_ELEMENT);
        NwElement nClientAlice = new NwElement("Alice", InetAddress.getByName("192.168.5.105"), "A0:8d:5c:12:4c:86", NetworkElementType.CLIENT_ELEMENT);
        NwElement nClientEve = new NwElement("Eve", InetAddress.getByName("192.168.5.106"), "E0:8d:5c:12:4c:86", NetworkElementType.CLIENT_ELEMENT);
        //TODO NwConnector

        final mxGraph graph = new mxGraph() {
            public String convertValueToString(Object cell) {
                if (cell instanceof mxCell) {
                    Object value = ((mxCell) cell).getValue();

                    if (value instanceof NwElement) {
                        return ((NwElement) value).getElementName();
                    }
                }
                return super.convertValueToString(cell);
            }
        };

        final Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        try
        {
            Object vBaseStn = graph.insertVertex(parent, null, nBaseStation, 160, 145, 120, 50, "defaultVertex;fillColor=yellow");
            Object vClient1 = graph.insertVertex(parent, null, nClientAlice, 300, 30, 120, 50);
            Object vClient2 = graph.insertVertex(parent, null, nClientEve, 140, 280, 120, 50);
            Object vEdge1 = graph.insertEdge(parent, null, "", vBaseStn, vClient1, "defaultEdge;strokeColor=red;startArrow=classic");
            Object vEdge2 = graph.insertEdge(parent, null, "", vBaseStn, vClient2, "defaultEdge;strokeColor=red;startArrow=classic");
        }
        finally
        {
            graph.getModel().endUpdate();
        }

        graphComponent = new mxGraphComponent(graph);
        getContentPane().add(graphComponent);

        graphComponent.getGraphControl().addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e) {
                if (e.isControlDown()) {
                    try {
                        NwElement nClientNew = new NwElement("", InetAddress.getByName("192.168.5.107"), "N0:8d:5c:12:4c:86", NetworkElementType.CLIENT_ELEMENT);
                        graph.insertVertex(parent, null, nClientNew, e.getX() - 40, e.getY() - 15, 80, 30);
                    } catch (UnknownHostException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        installPopupListener();
        graphComponent.setEnterStopsCellEditing(true);
    }

    public static void main(String[] args) {
        HomeScreen frame = null;
        try {
            frame = new HomeScreen();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
//        frame.setSize(400, 320);
        frame.setVisible(true);
    }

    public mxGraphComponent getGraphComponent()
    {
        return graphComponent;
    }

    public Action bind(String name, final Action action) {
        AbstractAction newAction = new AbstractAction(name) {
            public void actionPerformed(ActionEvent e)
            {
                action.actionPerformed(new ActionEvent(getGraphComponent(), e.getID(), e.getActionCommand()));
            }
        };
        newAction.putValue(Action.SHORT_DESCRIPTION, action.getValue(Action.SHORT_DESCRIPTION));

        return newAction;
    }

    protected void showGraphPopupMenu(MouseEvent e)
    {
        Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), graphComponent);

        if (graphComponent.getCellAt(e.getX(), e.getY()) != null) {
            homeMenu.show(graphComponent, pt.x, pt.y);
            e.consume();
        }
    }

    protected void installPopupListener() {
        graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) { checkPopup(e); }

            @Override
            public void mouseClicked(MouseEvent e) { checkPopup(e); }

            @Override
            public void mousePressed(MouseEvent e) { checkPopup(e); }

            private void checkPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showGraphPopupMenu(e);
                }
            }
        });
    }
}
