import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.math.BigInteger;

/**
 * Created by ashwinv on 14.06.16 at 17:36.
 */
public class IRPanel {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(IRPanel::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame();

        IRPanel irPanel = new IRPanel();
        frame.setContentPane(irPanel.createContentPane());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public JPanel createContentPane() {
        final JPanel mainPanel = new JPanel();

        /*new Thread(() -> {
            File file = new File("/home/christopher/tmpThrd1.txt");

            TailerListenerAdapter listener = new TailerListenerAdapter() {
                @Override
                public void handle(String line)
                {
//                    System.out.println("IR " + line);
                }
            };

            Tailer.create(file, listener, 4000);
        }).start();*/

        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.LINE_AXIS));
        displayPanel.add(drawIRPanel(), BorderLayout.CENTER);

        mainPanel.add(displayPanel, BorderLayout.CENTER);
        mainPanel.setOpaque(true);
        return mainPanel;
    }

    private JPanel drawIRPanel() {
        final JPanel mainGraphPanel = new JPanel();

        String pkeyStr = "", pkeyClientStr = "";
        Border textAreaBorder = LineBorder.createGrayLineBorder();

        try{
            pkeyStr = RssiList.getInstance().getServerPrelimKey();
            pkeyClientStr = RssiList.getInstance().getClientPrelimKey();
        } catch (Exception e) {
            System.out.println("Exception in IR");
        }

        JTextArea pkeyTxt = new JTextArea(pkeyStr);
        pkeyTxt.setEditable(false);
        pkeyTxt.setCursor(null);
        pkeyTxt.setOpaque(false);
        pkeyTxt.setFocusable(false);
        pkeyTxt.setLineWrap(true);
        pkeyTxt.setWrapStyleWord(true);
        pkeyTxt.setBorder(textAreaBorder);
        pkeyTxt.setFont(new Font("Serif", Font.PLAIN, 17));
        JScrollPane pkeyPane = new JScrollPane(pkeyTxt);

        JPanel fullPrelimPanel = new JPanel(new BorderLayout());
        JLabel pkeyLabel = new JLabel("Preliminary Key Server");
        fullPrelimPanel.add(pkeyLabel, BorderLayout.PAGE_START);
        fullPrelimPanel.add(pkeyPane, BorderLayout.CENTER);

        JTextArea fkeyTxt = new JTextArea(pkeyClientStr);
        fkeyTxt.setEditable(false);
        fkeyTxt.setCursor(null);
        fkeyTxt.setOpaque(false);
        fkeyTxt.setFocusable(false);
        fkeyTxt.setLineWrap(true);
        fkeyTxt.setWrapStyleWord(true);
        fkeyTxt.setBorder(textAreaBorder);
        fkeyTxt.setFont(new Font("Serif", Font.PLAIN, 17));
        JScrollPane fkeyPane = new JScrollPane(fkeyTxt);

        JPanel fullFinalPanel = new JPanel(new BorderLayout());
        JLabel fkeyLabel = new JLabel("<html><br>Preliminary Key Client</html>");
        fullFinalPanel.add(fkeyLabel, BorderLayout.PAGE_START);
        fullFinalPanel.add(fkeyPane, BorderLayout.CENTER);

        Border paneEdge = BorderFactory.createEmptyBorder(10,10,10,10);
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, fullPrelimPanel, fullFinalPanel);
        splitPane.setBorder(paneEdge);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(190);

        Dimension minSize = new Dimension(250, 150);
        pkeyPane.setMinimumSize(minSize);
        fkeyPane.setMinimumSize(minSize);

        splitPane.setPreferredSize(new Dimension(600, 400));
        mainGraphPanel.add(splitPane);

        new Thread(() -> {
            try {
//                Thread.sleep(RssiList.getInstance().getSize() * 500);

                /*int timer = 0;
                while (!QuantPanel.isQuantComplete && (timer < (RssiList.getInstance().getSize() * RSSIGraph.SLEEP_DURATION)) ) {
                    Thread.sleep(RSSIGraph.SLEEP_DURATION * 2);
                    timer += RSSIGraph.SLEEP_DURATION * 2;
                }*/

                while (!QuantPanel.isQuantComplete) {
                    Thread.sleep(RSSIGraph.SLEEP_DURATION * 2);
                }

                System.out.println("IR display start");

                String S = RssiList.getInstance().getServerPrelimKey();
                System.out.println("IR S = "+ S);

                /*int rS = S.length() % 4;
                System.out.println("Slen: " + S.length() + "   " + rS);*/

                String C = RssiList.getInstance().getClientPrelimKey();
                System.out.println("IR C = "+ C);

                /*int rC = C.length() % 4;
                System.out.println("Clen: " + C.length() + "   " + rC);*/

//                long decimalS = Long.parseLong(RssiList.getInstance().getServerPrelimKey().replace(",", ""), 2);
//                String hexStrS = Long.toHexString(Long.parseLong(S.substring(0, S.length() - rS - 1), 2));

//                long decimalC = Long.parseLong(RssiList.getInstance().getClientPrelimKey().replace(",", ""), 2);
//                String hexStrC = Long.toHexString(Long.parseLong(C.substring(0, C.length() - rC - 1), 2));


                BigInteger bS = new BigInteger(S, 2);
                BigInteger bC = new BigInteger(C, 2);

                /*pkeyTxt.setText(RssiList.getInstance().getServerPrelimKey().replace(",", "") + "\n\nServer key in hexadecimal format: " + bS.toString(16));
                pkeyTxt.update(pkeyTxt.getGraphics());
                fkeyTxt.setText(RssiList.getInstance().getClientPrelimKey().replace(",", "") + "\n\nClient key in hexadecimal format: " + bC.toString(16));
                fkeyTxt.update(fkeyTxt.getGraphics());*/

                pkeyTxt.append(RssiList.getInstance().getServerPrelimKey() + "\n\nServer key in hexadecimal format: " + bS.toString(16));
                pkeyTxt.setCaretPosition(pkeyTxt.getDocument().getLength());

                fkeyTxt.append(RssiList.getInstance().getClientPrelimKey() + "\n\nClient key in hexadecimal format: " + bC.toString(16));
                fkeyTxt.setCaretPosition(fkeyTxt.getDocument().getLength());

                System.out.println("IR display done");
//                mainGraphPanel.repaint();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        return mainGraphPanel;
    }
}
