import java.io.Serializable;
import java.net.InetAddress;

/**
 * Created by ashwinv on 23.05.16 at 12:34.
 */
public class NwElement implements Serializable {
    private String elementName;
    private InetAddress elementIP;
    private String elementMacAddr;
    private NetworkElementType elementType;

    public String getElementMacAddr() {
        return elementMacAddr;
    }

    public NetworkElementType getElementType() {
        return elementType;
    }

    public InetAddress getElementIP() {
        return elementIP;
    }

    public String getElementName() {
        return elementName;
    }

    public NwElement(String elementName, InetAddress elementIP, String elementMacAddr, NetworkElementType elementType) {
        this.elementName = elementName;
        this.elementIP = elementIP;
        this.elementMacAddr = elementMacAddr;
        this.elementType = elementType;
    }
}
