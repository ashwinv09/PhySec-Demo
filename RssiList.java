import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by ashwinv on 28.06.16 at 18:32.
 */
public class RssiList {
    private int size = 100, keyBits = 32;

    private BlockingQueue<Integer> RssiQueueServer, RssiQueueClient, ServerBackupQueue, ClientBackupQueue;
    private volatile String serverPrelimKey, clientPrelimKey;

    private boolean processComplete;

    public BlockingQueue getRssiQueueServer() {
        return RssiQueueServer;
    }

    public BlockingQueue getRssiQueueClient() {
        return RssiQueueClient;
    }

    public BlockingQueue<Integer> getServerBackupQueue() { return ServerBackupQueue; }

    public BlockingQueue<Integer> getClientBackupQueue() { return ClientBackupQueue; }

    public String getServerPrelimKey() { return serverPrelimKey; }

    public String getClientPrelimKey() { return clientPrelimKey; }

    public void setClientPrelimKey(String clientPrelimKey) { this.clientPrelimKey = clientPrelimKey; }

    public void setServerPrelimKey(String serverPrelimKey) { this.serverPrelimKey = serverPrelimKey; }

    public boolean isProcessComplete() { return processComplete; }

    public void setProcessComplete(boolean processComplete) { this.processComplete = processComplete; }

    public int getSize() { return size; }

    public void setSize(int size) { this.size = size; }

    public int getKeyBits() { return keyBits; }

    public void setKeyBits(int keyBits) { this.keyBits = keyBits; }

    private static RssiList ourInstance = null;

    public static RssiList getInstance() {
        if(ourInstance == null) {
            ourInstance = new RssiList();
        }

        return ourInstance;
    }

    private RssiList() {
        RssiQueueServer = new ArrayBlockingQueue<>(size+10);
        RssiQueueClient = new ArrayBlockingQueue<>(size+10);
        ServerBackupQueue = new ArrayBlockingQueue<>(size+10);
        ClientBackupQueue = new ArrayBlockingQueue<>(size+10);

        serverPrelimKey = "";
        clientPrelimKey = "";

        processComplete = false;
    }
}
