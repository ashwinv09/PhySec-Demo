import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class TimeSeriesDemo10 extends ApplicationFrame {
    public TimeSeriesDemo10(final String title) {
        super(title);

        final TimeSeries series = new TimeSeries("Per Minute Data", Minute.class);
        final Hour hour = new Hour();
        series.add(new Minute(1, hour), 10.2);
        series.add(new Minute(3, hour), 17.3);
        series.add(new Minute(9, hour), 14.6);
        series.add(new Minute(11, hour), 11.9);
        series.add(new Minute(15, hour), 13.5);
        series.add(new Minute(19, hour), 10.9);
        final TimeSeriesCollection dataset = new TimeSeriesCollection(series);
        final JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Time Series Demo 10",
                "Time",
                "Value",
                dataset,
                true,
                true,
                false
        );
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);

    }

    public static void main(final String[] args) {
        final TimeSeriesDemo10 demo = new TimeSeriesDemo10("Time Series Demo 10");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}