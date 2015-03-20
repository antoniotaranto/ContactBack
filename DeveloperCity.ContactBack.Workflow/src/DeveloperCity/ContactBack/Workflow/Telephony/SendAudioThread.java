/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.Workflow.Telephony;

import DeveloperCity.ContactBack.Workflow.Exception.ForceStopAudioException;
import java.io.File;
import java.io.FileInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.PortUnreachableException;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.log4j.Logger;

/**
 *
 * @author lbarbosa
 */
public class SendAudioThread implements Runnable {

    private static Logger logger = Logger.getLogger(SendAudioThread.class);
    private InetAddress ip;
    private int port;
    private long identifier;
    private List<String> fileQueue;
    private boolean alive = true;
    private boolean playing = false;
    private boolean askingStop = false;
    private String filesFolder;
    private Thread myThread;
    private SocketAddress address;
    private List<SendAudioEventListener> sendAudioEventListeners;

    public interface SendAudioEventListener {
        void SendAudioCompleteEvent(SendAudioThread thread, String file);
        void SendAudioListCompleteEvent(SendAudioThread thread, List<String> files);
    }

    public SendAudioThread(String filesFolder, long identifier, InetAddress ip, int port) throws SocketException {
        this.identifier = identifier;
        sendAudioEventListeners = new ArrayList<SendAudioEventListener>();
        myThread = new Thread(this);
        myThread.setName(String.format("AudioSocket %s:%d", ip.getHostAddress(), port));
        this.ip = ip;
        this.port = port;
        this.filesFolder = filesFolder;
        this.fileQueue = null;
        address =
                new InetSocketAddress(ip, port);
        myThread.start();
    }

    public List<String> getFileQueue() {
        return fileQueue;
    }

    public void setFileQueue(String... files) {
        List<String> array = java.util.Arrays.asList(files);
        setFileQueue(array);
    }
    public void setFileQueue(List<String> fileQueue) {
        if (playing) {
            askStop();

            while (playing) {
                try {
                    Thread.sleep(10);
                } catch (Exception e) {
                }
            }
        }

        this.fileQueue = fileQueue;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isPlaying() {
        return playing;
    }

    public boolean isAskingStop() {
        return askingStop;
    }

    public void setAlive(boolean isAlive) {
        this.alive = isAlive;
    }

    void askStop() {
        askingStop = true;
    }

    public void run() {
        if (logger.isInfoEnabled()) {
            logger.info("Running thread " + Thread.currentThread().getName());
        }
        while (alive) {
            while (fileQueue == null || fileQueue.isEmpty()) {
                try {
                    Thread.sleep(300);
                } catch (Exception e) {
                }
            }
            DatagramPacket packet = null;
            try {
                playing = true;
                DatagramSocket socket = new DatagramSocket();
                logger.info("Connecting");
                socket.connect(address);
                logger.info("Connected");
                // a)  8000Hz (8000 samples/second)
                //     1 sample = 0,125 ms (125 microseconds)
                //     1 ms = 8 samples
                // b)  G.711 => 1 sample = 1 byte (8 bits)
                //     8 bits/sample * 8000 samples/second = 64Kbits/second

                // 20 ms / packet
                // 1 packet = 20 * 8 samples
                // 1 packet = 20 * 8 * 1 byte
                // 1 packet = 160 bytes (payload-audio) + RTP header (12 bytes)
                // 160 samples
                final int waveHeaderSize = 44;
                final int headerByteCount = 12;
                final int payloadByteCount = 160; // 240 para 30ms
                final int sampleTime = 125; // micro seconds
                final int milisecondsPerPacket = 20; // 20ms

                Random r = new Random();
                short sequence = (short) r.nextInt(Short.MAX_VALUE);
                int syncId = r.nextInt();
                int interval = 0;
                long last = new java.util.Date().getTime();
                boolean first = true;
                for (String fileItem : fileQueue) {
                    String file = filesFolder + fileItem;
                    if ( (!alive) || askingStop ) {
                        askingStop = false;
                        try { socket.close(); } catch(Exception e) { }
                        throw new ForceStopAudioException();
                    }
                    if (file == null || file.length() == 0) {
                        continue;
                    }
                    if (logger.isInfoEnabled()) {
                        logger.info("Play file " + file);
                    }
                    File f = null;
                    try { f = new File(file); }
                    catch(Exception e) { logger.error(e); }
                    if (f==null || (!f.exists()) || (!f.isFile())) {
                        logger.warn(String.format("Invalid file %s", file));
                        continue;
                    }
                    FileInputStream fis = new FileInputStream(f);

                    int count = 0;
                    byte[] sendBuffer = new byte[headerByteCount + payloadByteCount];
                    fis.skip(waveHeaderSize);
                    while (alive && playing && ((count = fis.read(sendBuffer, headerByteCount, payloadByteCount)) > 0)) {
                        interval += sampleTime;
                        sendBuffer = createRTPHeader(sendBuffer, sequence, (interval / 0x10000), (interval % 0x10000), (syncId / 0x10000), (syncId % 0x10000));
                        packet = new DatagramPacket(sendBuffer, count + headerByteCount );

                        for (int i = 0; i < 3; i++) {
                            try {
                                socket.send(packet);
                                break;
                            } catch (PortUnreachableException e) {
                                if (i < 2) {
                                    logger.info(String.format("PortUnreachable (%s:%d, %s)", socket.getInetAddress(), socket.getPort(), file));
                                } else {
                                    logger.error( String.format("Packet error (%s:%d, %s)", socket.getInetAddress(), socket.getPort(), file), e);
                                }
                            }
                            if (socket.isClosed() || (!socket.isConnected())) {
                                if (logger.isInfoEnabled()) {
                                    logger.info("Reconnecting");
                                }
                                socket.connect(address);
                                if (logger.isInfoEnabled()) {
                                    logger.info("Connected");
                                }
                            }
                        }
                        try { Thread.sleep(last + milisecondsPerPacket - (4 + new java.util.Date().getTime())); } catch (Exception e) { }
                        last = new java.util.Date().getTime();
                        sequence++;

                        if (askingStop) {
                            askingStop = false;
                            try { socket.close(); } catch(Exception e) { }
                            try { fis.close(); } catch(Exception e) { }
                            throw new ForceStopAudioException();
                        }
                        first = false;
                    }
                    fis.close();
                    for (SendAudioEventListener observer : sendAudioEventListeners) {
                        observer.SendAudioCompleteEvent(this, file);
                    }
                }
                socket.close();
                for (SendAudioEventListener observer : sendAudioEventListeners) {
                    observer.SendAudioListCompleteEvent(this, fileQueue);
                }
                if (logger.isInfoEnabled()) {
                    logger.info("End of file queue");
                }
                fileQueue = null;
                playing = false;
            } catch (ForceStopAudioException fsae) {
                if (logger.isInfoEnabled()) {
                    logger.info("Forced end of file queue");
                }
                for (SendAudioEventListener observer : sendAudioEventListeners) {
                    observer.SendAudioListCompleteEvent(this, fileQueue);
                }

                fileQueue = null;
                playing = false;
            } catch (Exception e) {
                logger.error(e);
                for (SendAudioEventListener observer : sendAudioEventListeners) {
                    observer.SendAudioListCompleteEvent(this, fileQueue);
                }
                fileQueue = null;
                playing = false;
            }
        }
    }

    private byte[] createRTPHeader(byte[] packet, short sequence, int timeStampTop, int timeStampBottom, int syncIdTop, int syncIdBottom) {
        packet[0] = (byte) 0x80; // 10000000
        packet[1] = (byte) 0x00;
        packet[2] = (byte) (sequence / 0x100);
        packet[3] = (byte) (sequence % 0x100);
        packet[4] = (byte) (timeStampTop / 0x100);
        packet[5] = (byte) (timeStampTop % 0x100);
        packet[6] = (byte) (timeStampBottom / 0x100);
        packet[7] = (byte) (timeStampBottom % 0x100);
        packet[8] = (byte) (syncIdTop / 0x100);
        packet[9] = (byte) (syncIdTop % 0x100);
        packet[10] = (byte) (syncIdBottom / 0x100);
        packet[11] = (byte) (syncIdBottom % 0x100);
        return packet;
    }

    public void dispose() {
        if (myThread != null && myThread.isAlive()) {
            try {
                myThread.interrupt();
            } catch (Exception e) {
            }
        }
        myThread = null;
        ip = null;
        address = null;
    }

    public long getIdentifier() {
        return identifier;
    }

    public void addSendAudioEventListener(SendAudioEventListener sendAudioEventListener) {
        this.sendAudioEventListeners.add(sendAudioEventListener);
    }
    public void removeSendAudioEventListener(SendAudioEventListener sendAudioEventListener) {
        this.sendAudioEventListeners.remove(sendAudioEventListener);
    }
}
