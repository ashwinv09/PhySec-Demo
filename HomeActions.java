import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by ashwinv on 23.05.16 at 18:48.
 */
public class HomeActions {
    static final Action showPropertiesAction = new ShowPropertiesAction("properties");

    static final Action runPhysecAction = new RunPhySecAction("run physec");

    public static Action getShowPropertiesAction() {
        return showPropertiesAction;
    }

    public static class ShowPropertiesAction extends AbstractAction {
        public ShowPropertiesAction(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            System.out.println("ShowPropertiesAction pressed!");
            //TODO create dialog to show element properties
        }
    }

    public static Action getRunPhySecAction() {
        return runPhysecAction;
    }

    public static class RunPhySecAction extends AbstractAction {
        public RunPhySecAction(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            System.out.println("RunPhysecAction pressed!");

            new Thread(() -> {
//                executePythonScript();
                PingServer.main(new String[] {});
            }).start();

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MainScreen.main(new String[] {});
        }

        /*private void executePythonScript() {
            ProcessBuilder processBuilder = new ProcessBuilder("python", "/home/christopher/MEGA/code/combo/sampleChannelServer.py");
            processBuilder.redirectError();
            String s;

            try {
                Process p = processBuilder.start();
                BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((s = in.readLine()) != null)
                    System.out.println(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
    }

}
