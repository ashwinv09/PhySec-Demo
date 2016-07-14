import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

/**
 * Created by ashwinv on 06.06.16 at 16:31.
 */
public class RSSIGraph {
    private static final int SLIDING_WINDOW_LENGTH = 100;
    static final int SLEEP_DURATION = 500;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RSSIGraph::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame();

        RSSIGraph rssiGraph = new RSSIGraph();
        frame.setContentPane(rssiGraph.createContentPane());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public JPanel createContentPane() {
        final JPanel mainGraphPanel = new JPanel();

        final XYSeries seriesA = new XYSeries("Server");
        final XYSeries seriesB = new XYSeries("Client RPi");

        seriesA.setMaximumItemCount(SLIDING_WINDOW_LENGTH);
        seriesB.setMaximumItemCount(SLIDING_WINDOW_LENGTH);

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(seriesA);
        dataset.addSeries(seriesB);

        JFreeChart chart = ChartFactory.createXYLineChart(null, "Time", "RSSI", dataset);

        XYPlot xyPlot = (XYPlot) chart.getPlot();
        ValueAxis rssiAxis = xyPlot.getRangeAxis();
        rssiAxis.setRange(70, 110);

        mainGraphPanel.add(new ChartPanel(chart), BorderLayout.CENTER);

        /*new Thread(() -> {
                performChannelMeasurement();
        }).start();*/

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    int x = 0;
                    /*Scanner scanAlice = new Scanner(new File("/home/christopher/MEGA/code/combo/clientSignals.txt"));
                    scanAlice.useDelimiter(",");
                    Scanner scanBob = new Scanner(new File("/home/christopher/MEGA/code/combo/serverSignals.txt"));
                    scanBob.useDelimiter(",");*/

                    /*while (scanAlice.hasNextLine() && scanBob.hasNextLine()) {
                        int numA = Integer.parseInt(scanAlice.nextLine());
                        int numB = Integer.parseInt(scanBob.nextLine());*/

//                    System.out.println("\nSERVER LIST SIZE = " + RssiList.getInstance().getRssiQueueServer().size());
//                    System.out.println("\nCLIENT LIST SIZE = " + RssiList.getInstance().getRssiQueueClient().size());

                    /*while ((RssiList.getInstance().getRssiQueueServer().size() > 0)
                            && (RssiList.getInstance().getRssiQueueClient().size() > 0)) {

                        int numA = (Integer) RssiList.getInstance().getRssiQueueServer().poll();
                        int numB = (Integer) RssiList.getInstance().getRssiQueueClient().poll();

                        seriesA.add(x, numA);
                        seriesB.add(x, numB);

                        mainGraphPanel.repaint();
                        x++;
                        Thread.sleep(SLEEP_DURATION);
                    }*/

//                    Thread.sleep(5000);

                    while (x < RssiList.getInstance().getSize()) {
                        System.out.println("\nSERVER LIST SIZE = " + RssiList.getInstance().getRssiQueueServer().size());
//                        int numA = (Integer) RssiList.getInstance().getRssiQueueServer().poll(10L, TimeUnit.SECONDS);
                        int numA = (Integer) RssiList.getInstance().getRssiQueueServer().take();

                        System.out.println("\nCLIENT LIST SIZE = " + RssiList.getInstance().getRssiQueueClient().size());
//                        int numB = (Integer) RssiList.getInstance().getRssiQueueClient().poll(10L, TimeUnit.SECONDS);
                        int numB = (Integer) RssiList.getInstance().getRssiQueueClient().take();

//                        RssiList.getInstance().getRssiServerArr().add(numA);
//                        RssiList.getInstance().getRssiClientArr().add(numB);

                        seriesA.add(x, numA);
                        seriesB.add(x, numB);

                        mainGraphPanel.repaint();
                        x++;
                        Thread.sleep(SLEEP_DURATION);

//                        if ((RssiList.getInstance().getRssiQueueServer().size() == 0) && (RssiList.getInstance().getRssiQueueClient().size() == 0))
//                            break;
                    }

/*//                    if ((RssiList.getInstance().getRssiQueueServer().size() == 0) || (RssiList.getInstance().getRssiQueueClient().size() == 0))
                    Thread.sleep(6000);

                    System.out.println("\n at start SERVER LIST SIZE = " + RssiList.getInstance().getRssiQueueServer().size());
                    System.out.println("\n at start CLIENT LIST SIZE = " + RssiList.getInstance().getRssiQueueClient().size());

                    Iterator<Integer> itServer = RssiList.getInstance().getRssiQueueServer().iterator();
                    Iterator<Integer> itClient = RssiList.getInstance().getRssiQueueClient().iterator();

                    while (true) {
                        System.out.println("************** x = " + x + " **************");

                        System.out.println("\nSERVER LIST SIZE = " + RssiList.getInstance().getRssiQueueServer().size());
                        int numA = itServer.next();

                        System.out.println("\nCLIENT LIST SIZE = " + RssiList.getInstance().getRssiQueueClient().size());

                        if (!itClient.hasNext()) {
                            Thread.sleep(10000);
//                            if (RssiList.getInstance().getRssiQueueClient().size() >= 99)
//                                break;
                        }

                        int numB = itClient.next();

//                        RssiList.getInstance().getRssiServerArr().add(numA);
//                        RssiList.getInstance().getRssiClientArr().add(numB);

                        seriesA.add(x, numA);
                        seriesB.add(x, numB);

                        mainGraphPanel.repaint();
                        x++;
                        Thread.sleep(SLEEP_DURATION);

                        if (x == 100)
                            break;
                    }*/

                    /*while (scanAlice.hasNext() && scanBob.hasNext()) {
                        int numA = Integer.parseInt(scanAlice.next());
                        int numB = Integer.parseInt(scanBob.next());

                        seriesA.add(x, numA);
                        seriesB.add(x, numB);

                        mainGraphPanel.repaint();
                        x++;
                        Thread.sleep(SLEEP_DURATION);
                    }*/

                    /*scanAlice.close();
                    scanBob.close();*/
                /*} catch (FileNotFoundException fe) {
                    fe.printStackTrace();*/
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                } catch (NullPointerException npe) {
                    System.out.println("Queue empty!");
                }
            }
        };
        thread.start();

        return mainGraphPanel;
    }
}
