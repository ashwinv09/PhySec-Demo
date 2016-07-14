import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by ashwinv on 06.06.16 at 17:14.
 */
public class QuantPanel {
    private static final int SLIDING_WINDOW_LENGTH = 50;
    //private static final int BLOCK_SIZE = 5;
    private static final double ALPHA = 0.01;

    static BlockingQueue<Integer> prelimKeyServer = new ArrayBlockingQueue<>(RssiList.getInstance().getSize() + 10);
    static BlockingQueue<Integer> prelimKeyClient = new ArrayBlockingQueue<>(RssiList.getInstance().getSize() + 10);

    public static volatile boolean isQuantComplete = false;

    public static int blockSize = 5;
    public static double alpha;
    public static QuantAlgorithm algorithm;

    public static QuantAlgorithm getAlgorithm() {
        return algorithm;
    }

    public static void setAlgorithm(QuantAlgorithm algorithm) {
        QuantPanel.algorithm = algorithm;
    }

    public static double getAlpha() {
        return alpha;
    }

    public static void setAlpha(double alpha) {
        QuantPanel.alpha = alpha;
    }

    public static int getBlockSize() {
        return blockSize;
    }

    public static void setBlockSize(int blockSize) {
        QuantPanel.blockSize = blockSize;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(QuantPanel::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame();

        QuantPanel quantizationPanel = new QuantPanel();
        frame.setContentPane(quantizationPanel.createContentPane());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    JPanel createContentPane() {
        JPanel mainQuantizationPanel = new JPanel(new BorderLayout());

        JPanel controlPane = new JPanel();
        controlPane.add(Box.createRigidArea(new Dimension(10,0)));

        JPanel algRadioPanel = new JPanel();
        algRadioPanel.setBorder(BorderFactory.createTitledBorder("Quantization algorithm"));

        JRadioButton algJanaRadioBtn = new JRadioButton("Jana\'s", true);
        JRadioButton algAdaptiveRadioBtn = new JRadioButton("Adaptive");

        ButtonGroup algBtnGroup = new ButtonGroup();
        algBtnGroup.add(algJanaRadioBtn);
        algBtnGroup.add(algAdaptiveRadioBtn);

        algRadioPanel.add(algJanaRadioBtn);
        algRadioPanel.add(algAdaptiveRadioBtn);
        controlPane.add(algRadioPanel);
        controlPane.add(Box.createRigidArea(new Dimension(20,0)));

        Integer[] blockSizes = new Integer[] {5, 10, 20, 25, 50, 100, 200};
        JComboBox<Integer> blockSizeComboBox = new JComboBox<>(blockSizes);
        blockSizeComboBox.addActionListener(new Action() {
            @Override
            public Object getValue(String s) { return null; }

            @Override
            public void putValue(String s, Object o) { }

            @Override
            public void setEnabled(boolean b) { }

            @Override
            public boolean isEnabled() { return false; }

            @Override
            public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) { }

            @Override
            public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) { }

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int currentBlockSize = (Integer) blockSizeComboBox.getSelectedItem();
                System.out.println("Quantization block size changed to " + currentBlockSize);
//                setBlockSize(currentBlockSize);
            }
        });
        controlPane.add(blockSizeComboBox);
        controlPane.add(Box.createRigidArea(new Dimension(20,0)));

        Double[] alphas = new Double[] {0.05, 0.1, 0.2, 0.25, 0.4, 0.5, 0.75, 0.8, 0.9, 1.0};
        JComboBox<Double> alphaComboBox = new JComboBox(alphas);
        controlPane.add(alphaComboBox);
        controlPane.add(Box.createRigidArea(new Dimension(40,0)));

        JButton startQuantizationButton = new JButton("START");
        startQuantizationButton.setFont(new Font("Serif", Font.BOLD, 14));
        controlPane.add(startQuantizationButton);
        controlPane.add(Box.createRigidArea(new Dimension(10,0)));

        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.LINE_AXIS));
        displayPanel.add(drawBinaryGraphPanel(), BorderLayout.CENTER);

        mainQuantizationPanel.add(controlPane, BorderLayout.PAGE_START);
        mainQuantizationPanel.add(displayPanel, BorderLayout.CENTER);
        mainQuantizationPanel.setOpaque(true);
        return mainQuantizationPanel;
    }

    private JPanel drawBinaryGraphPanel() {
        final JPanel mainGraphPanel = new JPanel();

        final XYSeries seriesQuantAlice = new XYSeries("Server Quantized");
        final XYSeries seriesQuantBob = new XYSeries("Client RPi Quantized");

        seriesQuantAlice.setMaximumItemCount(SLIDING_WINDOW_LENGTH);
        seriesQuantBob.setMaximumItemCount(SLIDING_WINDOW_LENGTH);

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(seriesQuantAlice);
        dataset.addSeries(seriesQuantBob);

        JFreeChart chart = ChartFactory.createXYStepChart(null, null, null, dataset);

        XYPlot xyPlot = (XYPlot) chart.getPlot();
        xyPlot.setDomainGridlinesVisible(true);
        xyPlot.setDomainGridlinePaint(new Color(0x439CFF));
        xyPlot.setRangeGridlinesVisible(false);
        ValueAxis bitAxis = xyPlot.getRangeAxis();
        bitAxis.setUpperMargin(0.5);
        ValueAxis timeAxis = xyPlot.getDomainAxis();
        timeAxis.setVisible(false);

        mainGraphPanel.add(new ChartPanel(chart, 600, 200, 600, 200, 600, 200, false, false, false, false, false, false), BorderLayout.CENTER);

        Thread thrTmp = new Thread() {
            @Override
            public void run() {
                performQuant();
            }
        };
        thrTmp.start();

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    int x = 0;

                    Thread.sleep(6000);

                    while (!isQuantComplete || (prelimKeyServer.size() <= 1)) {
                        System.out.println("Xctr = " + x);
                        System.out.println("\nprelim key SERVER LIST SIZE = " + prelimKeyServer.size());
                        System.out.println("\nprelim key CLIENT LIST SIZE = " + prelimKeyClient.size());

                        int numS = prelimKeyServer.take();
                        int numC = prelimKeyClient.take();

                        seriesQuantAlice.add(x, numS);
                        seriesQuantBob.add(x, (numC + 0.03));   //vertical offset

                        mainGraphPanel.repaint();
                        x++;
                        Thread.sleep(RSSIGraph.SLEEP_DURATION);
                    }

                    System.out.println("Quantization display complete!");

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

        return mainGraphPanel;
    }

    private void performQuant() {
        try {
            int ctr = 0;
            String strServerPrelimKey = "", strClientPrelimKey = "";

            while (ctr < RssiList.getInstance().getSize()) {
                System.out.println("ctr = " + ctr);
                ArrayList<Integer> serverBlock = new ArrayList<>(getBlockSize());
                ArrayList<Integer> clientBlock = new ArrayList<>(getBlockSize());

                System.out.println("Backup length S : " + RssiList.getInstance().getServerBackupQueue().size());
                System.out.println("Backup length C : " + RssiList.getInstance().getClientBackupQueue().size());

                /*for (int i = 0; i < 5; i++) {
                    serverBlock.add(RssiList.getInstance().getServerBackupQueue().take());
                    clientBlock.add(RssiList.getInstance().getClientBackupQueue().take());
                }*/

                RssiList.getInstance().getServerBackupQueue().drainTo(serverBlock, getBlockSize());
                RssiList.getInstance().getClientBackupQueue().drainTo(clientBlock, getBlockSize());

                while (serverBlock.size() < getBlockSize())
                    RssiList.getInstance().getServerBackupQueue().drainTo(serverBlock, (getBlockSize() - serverBlock.size()));

                while (clientBlock.size() < getBlockSize())
                    RssiList.getInstance().getClientBackupQueue().drainTo(clientBlock, (getBlockSize() - clientBlock.size()));

                System.out.println("server block = " + serverBlock);
                System.out.println("client block = " + clientBlock);

                ArrayList<Byte> quantServer = new JanaQuantization().quantize(serverBlock, ALPHA);
                ArrayList<Byte> quantClient = new JanaQuantization().quantize(clientBlock, ALPHA);

                for (Byte bS : quantServer) {
                    strServerPrelimKey += bS + ",";
                    prelimKeyServer.offer(bS.intValue());
                }

                for (Byte bC : quantClient) {
                    strClientPrelimKey += bC + ",";
                    prelimKeyClient.offer(bC.intValue());
                }

                System.out.println("Quant server = " + quantServer);
                System.out.println("Quant client = " + quantClient);

                ctr += getBlockSize();
            }

            prelimKeyServer.offer(0);
            prelimKeyClient.offer(0);   //end of transmission

            RssiList.getInstance().setServerPrelimKey(strServerPrelimKey.replace(",", ""));
            RssiList.getInstance().setClientPrelimKey(strClientPrelimKey.replace(",", ""));

            isQuantComplete = true;
//            RssiList.getInstance().setProcessComplete(true);
            System.out.println("****************** DONE ******************");

        } catch (Exception e) {
            System.out.println("Exception in quantizing..");
        }

        System.out.println("Server bits: " + RssiList.getInstance().getServerPrelimKey());
        System.out.println("Client bits: " + RssiList.getInstance().getClientPrelimKey());
    }
}
