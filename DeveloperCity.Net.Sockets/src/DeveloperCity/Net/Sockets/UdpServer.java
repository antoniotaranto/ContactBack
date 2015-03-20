package DeveloperCity.Net.Sockets;

/**
 *
 * @author lbarbosa
 */
import java.net.*;

public class UdpServer
{
    DatagramSocket udp;
    boolean activeServer;

    public UdpServer(int port) throws SocketException {
        udp = new DatagramSocket(port);
        activeServer = false;
    }

    public void StartServer(IRemoteDataReceiver dataReceiver) {
        activeServer = true;
        while (activeServer) {
            try {
                byte[] buffer = new byte[8192];
                DatagramPacket dtp = new DatagramPacket(buffer, buffer.length);
                udp.receive(dtp);
                byte[] data = new byte[dtp.getLength()];
                System.arraycopy(dtp.getData(), 0, data, 0, dtp.getLength());
                int port = dtp.getPort();
                InetAddress ipAddress = dtp.getAddress();
                dataReceiver.ReceivedDataEvent(data, ipAddress, port);
                data = null;
                port = 0;
                ipAddress = null;
                buffer = null;
                dtp = null;
            } catch (Exception ex) {
                System.out.print(ex.toString());
            }
            try {
                Thread.sleep(200);
            } catch (Exception ex) {
                
            }
        }
    }

    public void StopServer() {
        activeServer = false;

        if (udp == null) {
            return;
        }
        try {
            udp.close();
        } catch (Exception ex) {
        }
    }

    public void Dispose() {
        StopServer();
        this.udp = null;
    }
}