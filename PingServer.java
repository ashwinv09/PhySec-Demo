import java.io.*;
import java.lang.*;
import java.net.ServerSocket;
import java.net.Socket;

public class PingServer {
    private static final String REPLY_MSG = "pong";

    public static void main(String[] args) {
        System.out.println("Ping server is now listening..");
        int clientNumber = 0;
        ServerSocket listener = null;
        try {
            listener = new ServerSocket(9898);
            while (true) {
                new Capitalizer(listener.accept(), clientNumber++).start();
            }
        } catch(IOException ioe) {
            System.out.println("IOException in starting PingServer server socket");
        } finally {
            if (listener != null) {
                try {
                    listener.close();
                } catch (IOException e) {
                    System.out.println("IOException in closing PingServer server socket");
                }
            }
        }
    }

    private static class Capitalizer extends Thread {
        private Socket socket;
        private int clientNumber;

        public Capitalizer(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            System.out.println("New connection with client " + socket.getInetAddress());
        }

        public void run() {
            try {
                BufferedReader inSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter outSocket = new PrintWriter(socket.getOutputStream(), true);
                int ctr = 0, keyBits, numberOfPings = 100;
                String clientSignals[];

                // agreeing upon parameters

                keyBits = Integer.parseInt(inSocket.readLine());

                if ((keyBits > 32) && (keyBits <= 80))
                    numberOfPings = 180;
                else if ((keyBits > 80) && (keyBits <= 160))
                    numberOfPings = 360;
                else if (keyBits > 160)
                    numberOfPings = 720;

                outSocket.println(String.valueOf(numberOfPings));
                RssiList.getInstance().setSize(numberOfPings);
                RssiList.getInstance().setKeyBits(keyBits);
                System.out.println("Number of pings = " + RssiList.getInstance().getSize() + "\tDesired key length = " + RssiList.getInstance().getKeyBits());

                // agreement complete

                String[] cmdRssi = { "/bin/sh", "-c", "iw wlx74da381a9616 station dump | grep -i signal: |awk -F: \'{print $2}\' |awk \'{print $1}\'" };

                while (true) {
                    String input = inSocket.readLine();
                    Process process = Runtime.getRuntime().exec(cmdRssi);
                    BufferedReader inProcess = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String s;
                    double mappedRssiVal;
                    int intRssi;

                    try {
                        while ((s = inProcess.readLine()) != null) {
                            mappedRssiVal = (109.3768327 * Math.exp(0.002786941462 * Integer.parseInt(s)));     // RSSI values mapped to exponential function
                            intRssi = Math.min((int)(Math.ceil(mappedRssiVal)), 100);
                            System.out.println(intRssi + "  ---> ping number " + (ctr+1));

                            RssiList.getInstance().getRssiQueueServer().offer(intRssi);    // adding rssi values to synchronous list
                            RssiList.getInstance().getServerBackupQueue().offer(intRssi);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (input == null) {
                        break;
                    }

                    ctr++;
                    outSocket.println(REPLY_MSG);

                    if (ctr % 10 == 0) {
                        clientSignals = inSocket.readLine().split(",");
                        for (String sig : clientSignals) {
                            System.out.println("Client queue size increased = " + RssiList.getInstance().getRssiQueueClient().size());
                            RssiList.getInstance().getRssiQueueClient().offer(new Integer(sig));
                            RssiList.getInstance().getClientBackupQueue().offer(new Integer(sig));
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("IOException while communicating with " + socket.getInetAddress() + ": " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("IOException in closing client socket!!");
                }
                System.out.println("Connection with client number " + (clientNumber+1) + " terminated!");

                System.out.println(RssiList.getInstance().getRssiQueueServer().toString());
                System.out.println(RssiList.getInstance().getRssiQueueClient().toString());

            }
        }
    }
}