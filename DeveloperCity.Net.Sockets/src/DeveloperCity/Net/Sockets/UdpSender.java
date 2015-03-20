package DeveloperCity.Net.Sockets;

/**
 *
 * @author lbarbosa
 */
import java.net.*;

public class UdpSender
{
    DatagramSocket udp;
    InetAddress remoteAddress;
    int remotePort;

    public UdpSender(int localPort, InetAddress remoteAddress, int remotePort) throws SocketException {
        udp = null;
        if (localPort > 0) {
            udp = new DatagramSocket(localPort);
        } else {
            udp = new DatagramSocket();
        }
        this.remoteAddress = remoteAddress;
        this.remotePort = remotePort;
    }

    public void Send(byte[] datatoSend) {
        try {
            DatagramPacket dtp = new DatagramPacket(datatoSend, datatoSend.length, remoteAddress, remotePort);
            udp.send(dtp);
            udp.close();
        } catch(Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public void Dispose() {
        this.udp = null;
    }

    public InetAddress getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(InetAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }
}