package DeveloperCity.Net.Sockets;

/**
 *
 * @author lbarbosa
 */
public interface IRemoteDataReceiver
{
    void ReceivedDataEvent(byte[] data, java.net.InetAddress address, int port);
}
