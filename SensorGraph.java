import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class SensorGraph {

    private static int x = 0;

    public static void main(String[] args) {

        // create and configure the window
        final JFrame window = new JFrame();
        window.setTitle("Sensor Graph GUI");
        window.setSize(600, 400);
        window.setLayout(new BorderLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final XYSeries series = new XYSeries("RSSI");
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart(null, "Time", "RSSI", dataset);
        window.add(new ChartPanel(chart), BorderLayout.CENTER);

        window.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent windowEvent) {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Scanner scanner = new Scanner(new File("/home/christopher/AliceLarge.csv"));
                            while (scanner.hasNextLine()) {
                                int number = Integer.parseInt(scanner.nextLine());
                                series.add(x++, number);
                                window.repaint();
                                Thread.sleep(500);
                            }
                        } catch (FileNotFoundException fe) {
                            fe.printStackTrace();
                        } catch (InterruptedException ie) {
                            ie.printStackTrace();
                        }
                    }
                };
                thread.start();
            }

            @Override
            public void windowClosing(WindowEvent windowEvent) { }

            @Override
            public void windowClosed(WindowEvent windowEvent) { }

            @Override
            public void windowIconified(WindowEvent windowEvent) { }

            @Override
            public void windowDeiconified(WindowEvent windowEvent) { }

            @Override
            public void windowActivated(WindowEvent windowEvent) { }

            @Override
            public void windowDeactivated(WindowEvent windowEvent) { }
        });

        // create a drop-down box and connect button, then place them at the top of the window
        /*final JButton connectButton = new JButton("Start");
        JPanel topPanel = new JPanel();
        topPanel.add(connectButton);
        window.add(topPanel, BorderLayout.NORTH);

        // create the line graph
        final XYSeries series = new XYSeries("Light Sensor Readings");
        XYSeriesCollection dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createXYLineChart("Light Sensor Readings", "Time (seconds)", "ADC Reading", dataset);
        window.add(new ChartPanel(chart), BorderLayout.CENTER);

        // configure the connect button and use another thread to listen for data
        connectButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                // create a new thread that listens for incoming text and populates the graph
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Scanner scanner = new Scanner(new File("/home/christopher/AliceLarge.csv"));
                            while (scanner.hasNextLine()) {
                                int number = Integer.parseInt(scanner.nextLine());
                                series.add(x++, number);
                                window.repaint();
                                Thread.sleep(500);
                            }
                        } catch (FileNotFoundException fe) {
                            fe.printStackTrace();
                        } catch (InterruptedException ie) {
                            ie.printStackTrace();
                        }
//                        scanner.close();
                    }
                };
                thread.start();
            }
        });*/

        // show the window
        window.setVisible(true);
    }

}