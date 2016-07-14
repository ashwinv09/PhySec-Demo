import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by ashwinv on 31.05.16 at 10:37.
 */
public class MainScreen implements ActionListener {
    private JButton channelMeasurementButton, quantizationButton, informationReconciliationButton, privacyAmpliflicationButton;
    private JPanel cardPanel;
    private String cardNames[] = new String[4];

    private JPanel createContentPane() {
        JPanel mainGUI = new JPanel();

        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 50, 50));

        channelMeasurementButton = new JButton("<html><center>Channel<br>Measurement</center></html>");
        channelMeasurementButton.addActionListener(this);
        buttonPanel.add(channelMeasurementButton);

        quantizationButton = new JButton("<html><center>Quantization</center></html>");
        quantizationButton.setBackground(new Color(0xA2A2A2));
        quantizationButton.addActionListener(this);
        buttonPanel.add(quantizationButton);

        informationReconciliationButton =  new JButton("<html><center>Information<br>Reconciliation</center></html>");
        informationReconciliationButton.setBackground(new Color(0xA2A2A2));
        informationReconciliationButton.addActionListener(this);
        buttonPanel.add(informationReconciliationButton);

        privacyAmpliflicationButton = new JButton("<html><center>Privacy<br>Amplification</center></html>");
        privacyAmpliflicationButton.setBackground(new Color(0xA2A2A2));
        privacyAmpliflicationButton.addActionListener(this);
        buttonPanel.add(privacyAmpliflicationButton);

        JPanel channelMeasurementPanel = new JPanel();

        channelMeasurementPanel.add(new RSSIGraph().createContentPane());

        /*channelMeasurementPanel.setLayout(new BoxLayout(channelMeasurementPanel, BoxLayout.LINE_AXIS));
        channelMeasurementPanel.setPreferredSize(new Dimension(600, 400));
        channelMeasurementPanel.setBackground(new Color(0xFF0000));*/

        JPanel quantizationPanel = new JPanel();
//        quantizationPanel.setLayout(new BoxLayout(quantizationPanel, BoxLayout.LINE_AXIS));
//        quantizationPanel.setPreferredSize(new Dimension(600, 400));
//        quantizationPanel.setBackground(new Color(0xFFF517));
        quantizationPanel.add(new QuantPanel().createContentPane());

        JPanel informationReconciliationPanel = new JPanel();
        informationReconciliationPanel.setLayout(new BoxLayout(informationReconciliationPanel, BoxLayout.LINE_AXIS));
//        informationReconciliationPanel.setPreferredSize(new Dimension(600, 400));
//        informationReconciliationPanel.setBackground(new Color(0x1B9326));
        informationReconciliationPanel.add(new IRPanel().createContentPane());

        JPanel privacyAmplificationPanel = new JPanel();
        privacyAmplificationPanel.setLayout(new BoxLayout(privacyAmplificationPanel, BoxLayout.LINE_AXIS));
//        privacyAmplificationPanel.setPreferredSize(new Dimension(600, 400));
//        privacyAmplificationPanel.setBackground(new Color(0x439CFF));
        privacyAmplificationPanel.add(new PrivacyPanel().createContentPane());

        cardPanel = new JPanel(new CardLayout(20, 40));

        cardNames[0] = "Channel Measurement";
        cardNames[1] = "Quantization";
        cardNames[2] = "Information Reconciliation";
        cardNames[3] = "Privacy Amplification";

        cardPanel.add(channelMeasurementPanel, cardNames[0]);
        cardPanel.add(quantizationPanel, cardNames[1]);
        cardPanel.add(informationReconciliationPanel, cardNames[2]);
        cardPanel.add(privacyAmplificationPanel, cardNames[3]);

        JPanel fullPanel = new JPanel();
        fullPanel.setLayout(new BorderLayout());

        fullPanel.add(buttonPanel, BorderLayout.PAGE_START);
        fullPanel.add(cardPanel, BorderLayout.CENTER);

        mainGUI.add(fullPanel);
        mainGUI.setOpaque(true);
        return mainGUI;
    }

    public void actionPerformed(ActionEvent e) {
        CardLayout cards = (CardLayout)(cardPanel.getLayout());

        if (e.getSource() == channelMeasurementButton) {
            cards.show(cardPanel, cardNames[0]);
            setButtonFocus(true, false, false, false);
        }
        else if (e.getSource() == quantizationButton) {
            cards.show(cardPanel, cardNames[1]);
            setButtonFocus(false, true, false, false);
        }
        else if (e.getSource() == informationReconciliationButton) {
            cards.show(cardPanel, cardNames[2]);
            setButtonFocus(false, false, true, false);
        }
        else if (e.getSource() == privacyAmpliflicationButton) {
            cards.show(cardPanel, cardNames[3]);
            setButtonFocus(false, false, false, true);
        }
    }

    private void setButtonFocus(boolean btn1, boolean btn2, boolean btn3, boolean btn4) {
        if (btn1) channelMeasurementButton.setBackground(null);
        else channelMeasurementButton.setBackground(new Color(0xA2A2A2));

        if (btn2) quantizationButton.setBackground(null);
        else quantizationButton.setBackground(new Color(0xA2A2A2));
        if (btn3) informationReconciliationButton.setBackground(null);
        else informationReconciliationButton.setBackground(new Color(0xA2A2A2));

        if (btn4) privacyAmpliflicationButton.setBackground(null);
        else privacyAmpliflicationButton.setBackground(new Color(0xA2A2A2));
    }

    private static void createAndShowGUI() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("PhySec Trial");
        frame.setSize(600, 400);

        MainScreen mainScreen = new MainScreen();
        frame.setContentPane(mainScreen.createContentPane());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }
}