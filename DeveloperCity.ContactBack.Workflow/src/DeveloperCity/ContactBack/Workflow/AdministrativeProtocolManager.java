/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.Workflow;

import DeveloperCity.ContactBack.DomainModel.Agent;
import DeveloperCity.Serialization.Binary;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import org.apache.log4j.Logger;

/**
 *
 * @author lbarbosa
 */
public class AdministrativeProtocolManager implements Runnable {

    private static Logger logger = Logger.getLogger(AdministrativeProtocolManager.class);
    private ServerSocket serverSocket;
    private AdministrativeService sAdministrative;
    private final static String encoding = "ISO-8859-1"; //"UTF8"

    public AdministrativeProtocolManager(AdministrativeService sAdministrative, int port) throws IOException {
        this.sAdministrative = sAdministrative;
        serverSocket = new ServerSocket(port);
    }

    public void run() {
        while (true) {
            if (!sAdministrative.isAdministrativeServiceRunning()) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    logger.error(ex);
                }
                continue;
            }
            try {
                Socket socket = serverSocket.accept();
                Thread receivedConnection = new Thread(new ProtocolHandler(socket));
                receivedConnection.setName(String.format("AdminSocket %s:%d", socket.getRemoteSocketAddress(), socket.getPort()));
                receivedConnection.start();
                Thread.sleep(100);
            } catch (IOException e) {
                logger.error(e);
            } catch(Exception e) {
                
            }
        }
    }

    private class ProtocolHandler implements Runnable {

        private Socket clientSocket;

        ProtocolHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public void run() {
            try {
                //
                // Read a message sent by client application
                //
                while (clientSocket.isConnected()) {
                    InputStream in = clientSocket.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(in, encoding));
                    String message = br.readLine();

                    if (message == null) {
                        continue;
                    }

                    byte[] responseFromHandler = receivedMessage(message);

                    if (responseFromHandler == null || responseFromHandler.length == 0) {
                        try {
                            if (clientSocket.isConnected()) {
                                clientSocket.close();
                            }
                        } catch (Exception e) {
                        }
                    }
                    if (!clientSocket.isConnected()) {
                        continue;
                    }

                    OutputStream out = clientSocket.getOutputStream();
                    DataOutputStream dos = new DataOutputStream(out);
                    dos.write(responseFromHandler);
                    dos.flush();

                    if (!clientSocket.isConnected()) {
                        continue;
                    }
                    try {
                        clientSocket.close();
                    } catch (Exception e) {
                    }
                }
            } catch (IOException e) {
            }
            this.clientSocket = null;
        }

        private byte[] receivedMessage(String message) throws IOException {
            if (message == null) {
                return null;
            }
            else if (message.startsWith("@qu")) {
                return requestQueue();
            }
            else if (message.startsWith("@qc")) {
                return requestQueueCount();
            }
            else if (message.startsWith("@cl")) {
                return requestCurrentCalls();
            }
            else if (message.startsWith("@pp")) {
                return requestPriorityPolicy();
            }
            else if (message.startsWith("@ag")) {
                return requestAgents();
            }
            else if (message.startsWith("@di")) {
                return applicationDiagnostics();
            }
            else if (message.startsWith("@a=")) {
                long agentID = 0;
                try {
                    String aID = message.substring(message.indexOf("@a=") + 3);
                    agentID = Long.valueOf(aID);
                    if (agentID == 0) {
                        throw new Exception("Invalid agent ID");
                    }
                } catch(Exception e) { return Binary.Serialize(false); }
                return requestAgent(agentID);
            }
            else if (message.startsWith("-shd")) {
                return shutdown();
            }
            else if (message.startsWith("*db")) {
                return refreshData();
            }
            else if (message.startsWith("*qu")) {
                return refreshQueueData();
            }
            else if (message.startsWith("*pp")) {
                return reloadPriorityPolicy();
            }
            else if (message.startsWith("*bl")) {
                return reloadBlackList();
            }
            else if (message.startsWith("*hd")) {
                return reloadHolidays();
            }
            else if (message.startsWith("*st")) {
                return reloadSetup();
            }
            else if (message.startsWith("?ad")) {
                return requestIsAdministrativeRunning();
            }
            else if (message.startsWith("?qu")) {
                return requestIsQueueRunning();
            }
            else if (message.startsWith("$qu=0")) {
                return stopQueueProcess();
            }
            else if (message.startsWith("$qu=1")) {
                return startQueueProcess();
            }
            else if (message.startsWith("?di")) {
                return requestIsDialerRunning();
            }
            else if (message.startsWith("$di=0")) {
                return stopDialerProcess();
            }
            else if (message.startsWith("$di=1")) {
                return startDialerProcess();
            }
            else if (message.startsWith("!Br?b=0")) {
                long agentID = 0;
                long breakID = 0;
                try {
                    String aID = message.substring(message.indexOf("agent=") + 6);
                    if (aID.contains("&")) {
                        aID = aID.substring(0, aID.indexOf("&"));
                    }

                    String bID = message.substring(message.indexOf("break=") + 6);
                    if (bID.contains("&")) {
                        bID = bID.substring(0, bID.indexOf("&"));
                    }
                    agentID = Long.valueOf(aID);
                    breakID = Long.valueOf(bID);

                    if (agentID == 0 || breakID == 0) {
                        throw new Exception("Invalid data to end break");
                    }
                } catch(Exception e) { return Binary.Serialize(false); }
                return endAgentBreak(agentID, breakID);
            }
            else if (message.startsWith("!Br?b=1")) {
                long agentID = 0;
                long breakTypeID = 0;
                try {
                    String aID = message.substring(message.indexOf("agent=") + 6);
                    if (aID.contains("&")) {
                        aID = aID.substring(0, aID.indexOf("&"));
                    }

                    String bID = message.substring(message.indexOf("break=") + 6);
                    if (bID.contains("&")) {
                        bID = bID.substring(0, bID.indexOf("&"));
                    }
                    agentID = Long.valueOf(aID);
                    breakTypeID = Long.valueOf(bID);

                    if (agentID == 0 || breakTypeID == 0) {
                        throw new Exception("Invalid data to start break");
                    }
                } catch(Exception e) { return Binary.Serialize(false); }
                return startAgentBreak(agentID, breakTypeID);
            }
            else if (message.startsWith("!Ag?s=0")) {
                long agentID = 0;
                try {
                    String aID = message.substring(message.indexOf("agent=") + 6);
                    if (aID.contains("&")) {
                        aID = aID.substring(0, aID.indexOf("&"));
                    }

                    agentID = Long.valueOf(aID);

                    if (agentID == 0) {
                        throw new Exception("Invalid data to start break");
                    }
                } catch(Exception e) { return Binary.Serialize(false); }
                return endAgentSession(agentID);
            }
            else if (message.startsWith("!Ag?s=1")) {
                long agentID = 0;
                try {
                    String aID = message.substring(message.indexOf("agent=") + 6);
                    if (aID.contains("&")) {
                        aID = aID.substring(0, aID.indexOf("&"));
                    }

                    agentID = Long.valueOf(aID);

                    if (agentID == 0) {
                        throw new Exception("Invalid data to start break");
                    }
                } catch(Exception e) { return Binary.Serialize(false); }
                return startAgentSession(agentID);
            }
            else if (message.startsWith("!Ag?s=2")) {
                long agentID = 0;
                long breakTypeID = 0;
                try {
                    String aID = message.substring(message.indexOf("agent=") + 6);
                    if (aID.contains("&")) {
                        aID = aID.substring(0, aID.indexOf("&"));
                    }

                    String bID = message.substring(message.indexOf("break=") + 6);
                    if (bID.contains("&")) {
                        bID = bID.substring(0, bID.indexOf("&"));
                    }
                    agentID = Long.valueOf(aID);
                    breakTypeID = Long.valueOf(bID);

                    if (agentID == 0 || breakTypeID == 0) {
                        throw new Exception("Invalid data to end break");
                    }
                } catch(Exception e) { return Binary.Serialize(false); }
                return restartAgentSession(agentID, breakTypeID);
            }
            else if (message.startsWith("%Ag|")) {
                String agentJson = message.substring(4);
                return changeAgent(agentJson);
            }
            else if (message.startsWith("%wq|")) {
                long customerID = 0;
                String number = "";
                try {
                    String cID = message.substring(message.indexOf("u=") + 2);
                    if (cID.contains("&")) {
                        cID = cID.substring(0, cID.indexOf("&"));
                    }

                    number = message.substring(message.indexOf("n=") + 2);
                    if (number.contains("&")) {
                        number = number.substring(0, number.indexOf("&"));
                    }

                    customerID = Long.valueOf(cID);

                    if (customerID == 0) {
                        throw new Exception("ID do cliente incorreto.");
                    }
                } catch(Exception e) { return Binary.Serialize(e.getMessage()); }
                return addWebQueue(customerID, number);
            }
            else if (message.startsWith("%mb|")) {
                long callID = 0;
                try {
                    String cID = message.substring(message.indexOf("c=") + 2);
                    if (cID.contains("&")) {
                        cID = cID.substring(0, cID.indexOf("&"));
                    }

                    callID = Long.valueOf(cID);

                    if (callID == 0) {
                        throw new Exception("ID da chamada incorreta.");
                    }
                } catch(Exception e) { return Binary.Serialize(e.getMessage()); }
                return setMailBox(callID);
            }

            return null;

            //this.sAdministrative.applySetupChanges(null);
            //this.sAdministrative.modifyCallManager(null);
            //this.sAdministrative.startQueueManager(null);
            //this.sAdministrative.stopQueueManager(null);
        }

        // <editor-fold defaultstate="collapsed" desc="Agent">
        private byte[] requestAgents() throws IOException {
            return sAdministrative.getAgentsJson().getBytes(encoding);
        }
        private byte[] requestAgent(long agentID) throws IOException {
            return sAdministrative.getAgentJson(agentID).getBytes(encoding);
        }
        private byte[] changeAgent(String agentJson) throws IOException {
            Agent agent = null;
            try {
                agent = DeveloperCity.Serialization.JSON.Deserialize(agentJson, Agent.class);
                if (agent == null) { throw new Exception("Invalid deserialization"); }
            }
            catch(Exception e) { return Binary.Serialize("DeserializeError"); }

            return Binary.Serialize(sAdministrative.changeAgent(agent));
        }
        private byte[] startAgentBreak(long agentID, long breakTypeID) throws IOException {
            Boolean retValue = sAdministrative.startAgentBreak(agentID, breakTypeID);
            return Binary.Serialize(retValue);
        }
        private byte[] endAgentBreak(long agentID, long breakID) throws IOException {
            Boolean retValue = sAdministrative.endAgentBreak(agentID, breakID);
            return Binary.Serialize(retValue);
        }
        private byte[] startAgentSession(long agentID) throws IOException {
            Boolean retValue = sAdministrative.startAgentSession(agentID);
            return Binary.Serialize(retValue);
        }
        private byte[] restartAgentSession(long agentID, long breakTypeID) throws IOException {
            Boolean retValue = sAdministrative.restartAgentSession(agentID, breakTypeID);
            return Binary.Serialize(retValue);
        }
        private byte[] endAgentSession(long agentID) throws IOException {
            Boolean retValue = sAdministrative.endAgentSession(agentID);
            return Binary.Serialize(retValue);
        }
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Queue">
        private byte[] requestQueue() throws IOException {
            return sAdministrative.getQueueJson().getBytes(encoding);
        }
        private byte[] requestQueueCount() throws IOException {
            return sAdministrative.getQueueCountJson().getBytes(encoding);
        }
        private byte[] refreshQueueData() throws IOException {

            Boolean b = Boolean.valueOf(sAdministrative.refreshQueueData());
            return Binary.Serialize(b);
        }
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Call">
        private byte[] requestCurrentCalls() throws IOException {
            return sAdministrative.getCurrentCallsJson().getBytes(encoding);
        }
        private byte[] addWebQueue(long customerID, String number) throws IOException {
            try {
                String status = sAdministrative.addWebQueue(customerID, number);
                return Binary.Serialize(status);
            } catch(Exception e) { return Binary.Serialize(e.getMessage()); }
        }
        private byte[] setMailBox(long callID) throws IOException {
            try {
                String status = sAdministrative.setMailBox(callID);
                return Binary.Serialize(status);
            } catch(Exception e) { return Binary.Serialize(e.getMessage()); }
        }
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Setup">
        private byte[] requestPriorityPolicy() throws IOException {
            return sAdministrative.getPriorityPolicyJson().getBytes(encoding);
        }
        private byte[] reloadPriorityPolicy() throws IOException {
            Boolean retValue = sAdministrative.reloadPriorityPolicy();
            return Binary.Serialize(retValue);
        }
        private byte[] reloadBlackList() throws IOException {
            Boolean retValue = sAdministrative.reloadBlackList();
            return Binary.Serialize(retValue);
        }
        private byte[] reloadSetup() throws IOException {
            Boolean retValue = sAdministrative.reloadSetup();
            return Binary.Serialize(retValue);
        }
        private byte[] reloadHolidays() throws IOException {
            Boolean retValue = sAdministrative.reloadHolidays();
            return Binary.Serialize(retValue);
        }
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Process and diagnostics">
        private byte[] refreshData() throws IOException {

            Boolean b = Boolean.valueOf(sAdministrative.refreshRealTimeData());
            return Binary.Serialize(b);
        }
        private byte[] applicationDiagnostics() throws IOException {
            return sAdministrative.getApplicationDiagnosticsJson().getBytes(encoding);
        }
        private byte[] requestIsQueueRunning() throws IOException {
            return sAdministrative.isQueueManagerRunning() ? "OK".getBytes(Charset.forName(encoding)) : "Error".getBytes(Charset.forName(encoding));
        }
        private byte[] requestIsAdministrativeRunning() throws IOException {
            return sAdministrative.isAdministrativeServiceRunning() ? "OK".getBytes(Charset.forName(encoding)) : "Error".getBytes(Charset.forName(encoding));
        }
        private byte[] requestIsDialerRunning() throws IOException {
            return sAdministrative.isDialerManagerRunning() ? "OK".getBytes(Charset.forName(encoding)) : "Error".getBytes(Charset.forName(encoding));
        }
        private byte[] stopQueueProcess() throws IOException {
            sAdministrative.stopQueueManager();
            return requestIsQueueRunning();
        }
        private byte[] startQueueProcess() throws IOException {
            sAdministrative.startQueueManager();
            return requestIsQueueRunning();
        }
        private byte[] stopDialerProcess() throws IOException {
            sAdministrative.stopDialerManager();
            return requestIsDialerRunning();
        }
        private byte[] startDialerProcess() throws IOException {
            sAdministrative.startDialerManager();
            return requestIsDialerRunning();
        }
        private byte[] shutdown() throws IOException {
            byte[] retValue = "OK".getBytes(Charset.forName(encoding));
            System.exit(0);
            return retValue;
        }
        // </editor-fold>
    }
}
