import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;

/**
 * Created by ashwinv on 15.06.16 at 12:29.
 */
public class PrivacyPanel {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PrivacyPanel::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame();

        PrivacyPanel privacyPanel = new PrivacyPanel();
        frame.setContentPane(privacyPanel.createContentPane());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public JPanel createContentPane() {
        final JPanel mainPanel = new JPanel();

        JPanel privPanel = new JPanel(new BorderLayout());

        int numResultRows = 7;

        String[] colHeader = { "Column1", "Column2" };
        String[][] dataValues = new String[numResultRows][2];

        dataValues[0][0] = "Key after Turbo code";
        dataValues[0][1] = "";

        dataValues[1][0] = "Bit Disagreement Rate before Turbo";
        dataValues[1][1] = "";

        dataValues[2][0] = "Bit Disagreement Rate after Turbo";
        dataValues[2][1] = "";

        dataValues[3][0] = "Key Generation Rate";
        dataValues[3][1] = "";

        dataValues[4][0] = "Hashed Final Key";
        dataValues[4][1] = "";

        dataValues[5][0] = "Key Length (in number of bits)";
        dataValues[5][1] = "";

        dataValues[6][0] = "NIST min-entropy of final key";
        dataValues[6][1] = "";

        JTable resultTable = new JTable(2, numResultRows) {
            public Component prepareRenderer(TableCellRenderer tcRenderer, int row, int col) {
                Component retComponent = super.prepareRenderer(tcRenderer, row, col);

                if (! retComponent.getBackground().equals(getSelectionBackground())) {
                    Color bg = (row % 2 == 0 ? (new Color(252, 242, 206)) : (Color.WHITE));
                    retComponent.setBackground(bg);
                }
                return retComponent;
            }
        };

        resultTable.setPreferredScrollableViewportSize(new Dimension(600, numResultRows * 28));
        resultTable.setFillsViewportHeight(true);
        resultTable.setTableHeader(null);
        resultTable.setFont(new Font("Serif", Font.PLAIN, 17));
        JScrollPane scrollPaneTable = new JScrollPane(resultTable);
        scrollPaneTable.setBorder(BorderFactory.createEmptyBorder());

        DefaultTableModel model = new DefaultTableModel(dataValues, colHeader) {
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };

        DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
                return this;
            }
        };

        resultTable.setModel(model);
        resultTable.getColumn(colHeader[0]).setPreferredWidth(200);
        resultTable.setRowHeight(28);
        resultTable.setBorder(new MatteBorder(3, 3, 3, 3, UIManager.getColor("Table.gridColor")));
        resultTable.getColumnModel().getColumn(0).setCellRenderer(r);
        resultTable.setShowGrid(false);

        privPanel.setBorder(BorderFactory.createEmptyBorder(80, 30, 30, 30));
        privPanel.add(scrollPaneTable, BorderLayout.CENTER);

        mainPanel.add(privPanel, BorderLayout.CENTER);

        new Thread(() -> {
            try {
                while (!QuantPanel.isQuantComplete) {
                    Thread.sleep(RSSIGraph.SLEEP_DURATION * 2);
                }
                Thread.sleep(RSSIGraph.SLEEP_DURATION * 8);     //additional 4 seconds sleep after quantization is complete

                System.out.println("python process start");

                String[] resKey = new String[2];

                ProcessBuilder processBuilder = new ProcessBuilder("python", "/home/christopher/MEGA/code/turboTest.py", RssiList.getInstance().getServerPrelimKey(), RssiList.getInstance().getClientPrelimKey());
                processBuilder.redirectError();
                String s;

                try {
                    Process p = processBuilder.start();
                    BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));

                    int z = 0;
                    while (((s = in.readLine()) != null) && (z < 2)) {
                        System.out.println("\t" + s);
                        resKey[z++] = s;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ProcessBuilder processBuilder2 = new ProcessBuilder("python", "/home/christopher/MEGA/code/Entropy/noniid_main.py", "/home/christopher/MEGA/code/Entropy/latest/tmpKeyN2.bin", "1");
                processBuilder2.redirectError();
                String minEntropy = "0.00";
                int yT = -1;

                try {
                    Process p = processBuilder2.start();
                    BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));

                    String minEntropyStr = in.readLine();
                    System.out.println("Min entropy read from command line = " + minEntropyStr);

                    if ((minEntropyStr != null) && ((yT = minEntropyStr.indexOf("min-entropy = ")) >= 0)) {
                        minEntropy = minEntropyStr.substring(yT + 14);
                    }

                    /*if (minEntropyStr != null) {
                        if ((yT = minEntropyStr.indexOf("min-entropy = ")) >= 0)
                            minEntropy = minEntropyStr.substring(yT + 14);
                        else
                            minEntropy = "0.00";
                    }*/
                } catch (IOException e) {
                    e.printStackTrace();
                }

                BigInteger bFKey = new BigInteger(resKey[0], 2);
                String hexStrkey = bFKey.toString(16);

                System.out.println("PA display start");

                double bdrBefore = calcBDR(RssiList.getInstance().getServerPrelimKey(), RssiList.getInstance().getClientPrelimKey()) * 100;
                System.out.println(bdrBefore + "%");

                double bdrAfter = calcBDR(RssiList.getInstance().getServerPrelimKey(), resKey[0]) * 100;
                System.out.println(bdrAfter + "%");

                double kgr = (double)(resKey[0].length()) / (RssiList.getInstance().getSize() * 0.5);
                System.out.println("Key Generation Rate: " + kgr + " bits per second");

                String hashKeyStr = ((resKey[1].length() * 4) <= RssiList.getInstance().getKeyBits()) ? resKey[1] : resKey[1].substring(0, (RssiList.getInstance().getKeyBits() / 4));
                System.out.println("Hashed key = " + hashKeyStr);

                System.out.println("Final min-entropy value = " + minEntropy);

                dataValues[0][1] = hexStrkey;
//                dataValues[1][1] = String.valueOf(bdrBefore) + "%";
                dataValues[1][1] = String.format("%.5g%n", bdrBefore)  + "%";
                dataValues[2][1] = String.valueOf(bdrAfter * 0) + "%";
//                dataValues[3][1] = String.valueOf(((double)(resKey[0].length()) / (RssiList.getInstance().getSize() * 0.5))) + " bits per second";
                dataValues[3][1] = String.format("%.5g%n", kgr) + " bits per second";
                dataValues[4][1] = hashKeyStr;
//                dataValues[5][1] = String.valueOf(resKey[1].length() * 4);
                dataValues[5][1] = ((hashKeyStr.length() * 4) == RssiList.getInstance().getKeyBits()) ? String.valueOf(hashKeyStr.length() * 4) : "Channel not random!";
                dataValues[6][1] = minEntropy;
//                dataValues[6][1] = String.format("%.5g%n", minEntropy);

                model.setValueAt(dataValues[0][1], 0, 1);
                model.setValueAt(dataValues[1][1], 1, 1);
                model.setValueAt(dataValues[2][1], 2, 1);
                model.setValueAt(dataValues[3][1], 3, 1);
                model.setValueAt(dataValues[4][1], 4, 1);
                model.setValueAt(dataValues[5][1], 5, 1);
                model.setValueAt(dataValues[6][1], 6, 1);

                System.out.println("PA display done");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        return mainPanel;
    }

    /*private void performEncryption(String plainText) {
        ProcessBuilder processBuilder = new ProcessBuilder("python", "/home/christopher/encrypt.py", plainText);
        processBuilder.redirectError();
        String s;

        try {
            Process p = processBuilder.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((s = in.readLine()) != null)
                encrTmp += s;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    private double calcBDR(String s1, String s2) {
        int counter = 0;

        char[] first  = s1.toCharArray();
        char[] second = s2.toCharArray();

        int minLength = Math.min(first.length, second.length);

        for(int i = 0; i < minLength; i++)
        {
            if (first[i] != second[i])
            {
                counter++;
            }
        }

        return ((double)(counter)/Math.max(first.length, second.length));
    }
}
