import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.util.mxResources;

import javax.swing.*;

/**
 * Created by ashwinv on 23.05.16 at 17:53.
 */
public class HomePopupMenu extends JPopupMenu {
    public HomePopupMenu(HomeScreen screen) {
//        add(screen.bind(mxResources.get("delete"), mxGraphActions.getDeleteAction())).setEnabled(true);
//        add(screen.bind(mxResources.get("rename"), mxGraphActions.getEditAction())).setEnabled(true);


        add(screen.bind("Run PhySec", HomeActions.getRunPhySecAction())).setEnabled(true);
        addSeparator();
        add(screen.bind("Rename Node", mxGraphActions.getEditAction())).setEnabled(true);
        add(screen.bind("Properties", HomeActions.getShowPropertiesAction())).setEnabled(true);
    }
}