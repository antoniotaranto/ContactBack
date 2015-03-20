/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Workflow;

import DeveloperCity.ContactBack.DomainModel.Agent;
import DeveloperCity.ContactBack.DomainModel.Call;
import DeveloperCity.ContactBack.DomainModel.Queue;
import DeveloperCity.ContactBack.Exception.AgentInUseException;
import DeveloperCity.ContactBack.Exception.InvalidDirectoryNumberException;
import DeveloperCity.ContactBack.Exception.InvalidTerminalException;
import DeveloperCity.Serialization.Binary;
import flexjson.JSONDeserializer;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author lbarbosa
 */
public class UItoProcess {
    private static Logger logger = Logger.getLogger(UItoProcess.class);
    private final static String encoding = "ISO-8859-1"; //"UTF8"

    public UItoProcess() {
    }

    public boolean askBreakEnd(long agentID, long breakID) throws IOException, Exception {
        byte[] response = sendProtocolMessage(String.format("!Br?b=0&agent=%d&break=%d", agentID, breakID));
        Boolean ok = Binary.Deserialize(response, Boolean.class);
        return ok;
    }
    public boolean askBreakStart(long agentID, long breakTypeID) throws IOException, Exception {
        byte[] response = sendProtocolMessage(String.format("!Br?b=1&agent=%d&break=%d", agentID, breakTypeID));
        Boolean ok = Binary.Deserialize(response, Boolean.class);
        return ok;
    }
    public boolean askSessionRestart(long agentID, long breakTypeID) throws IOException, Exception {
        byte[] response = sendProtocolMessage(String.format("!Ag?s=2&agent=%d&break=%d", agentID, breakTypeID));
        Boolean ok = Binary.Deserialize(response, Boolean.class);
        return ok;
    }
    public boolean askSessionStart(long agentID) throws IOException, Exception {
        byte[] response = sendProtocolMessage(String.format("!Ag?s=1&agent=%d", agentID));
        Boolean ok = Binary.Deserialize(response, Boolean.class);
        return ok;
    }
    public boolean askSessionEnd(long agentID) throws IOException, Exception {
        byte[] response = sendProtocolMessage(String.format("!Ag?s=0&agent=%d", agentID));
        Boolean ok = Binary.Deserialize(response, Boolean.class);
        return ok;
    }
    public List<Agent> reqAgents() throws IOException, Exception {
        JSONDeserializer<List<Agent>> des =
                new JSONDeserializer<List<Agent>>().use("values", Agent.class);

        String response = reqAgentsJson();
        List<Agent> agents = des.deserialize(response);
        return agents;
    }
    public List<Queue> reqQueue() throws IOException, Exception {
        JSONDeserializer<List<Queue>> des =
                new JSONDeserializer<List<Queue>>().use("values", Queue.class);

        String response = reqQueueJson();
        List<Queue> queue = des.deserialize(response);
        return queue;
    }
    public List<Call> reqCalls() throws IOException, Exception {
        JSONDeserializer<List<Call>> des =
                new JSONDeserializer<List<Call>>().use("values", Call.class);

        String response = reqCallsJson();
        List<Call> calls = des.deserialize(response);
        return calls;
    }
    public String reqAgentsJson() throws IOException, Exception {
        return new String(sendProtocolMessage("@ag"), encoding);
    }
    public String reqAgentJson(long agentID) throws IOException, Exception {
        return new String(sendProtocolMessage(String.format("@a=%d", agentID)), encoding);
    }
    public String reqQueueJson() throws IOException, Exception {
        return new String(sendProtocolMessage("@qu"), encoding);
    }
    public String reqQueueCountJson() throws IOException, Exception {
        return new String(sendProtocolMessage("@qc"), encoding);
    }
    public String reqCallsJson() throws IOException, Exception {
        return new String(sendProtocolMessage("@cl"), encoding);
    }
    public String reqApplicationDiagnosticsJson() throws IOException, Exception {
        return new String(sendProtocolMessage("@di"), encoding);
    }
    public boolean refreshAll() throws IOException, Exception {
        byte[] response = sendProtocolMessage("*db");
        Boolean ok = Binary.Deserialize(response, Boolean.class);
        return ok;
    }
    public boolean refreshQueue() throws IOException, Exception {
        byte[] response = sendProtocolMessage("*qu");
        Boolean ok = Binary.Deserialize(response, Boolean.class);
        return ok;
    }
    public boolean reloadPriorityPolicy() throws IOException, Exception {
        byte[] response = sendProtocolMessage("*pp");
        Boolean ok = Binary.Deserialize(response, Boolean.class);
        return ok;
    }
    public boolean reloadBlackList() throws IOException, Exception {
        byte[] response = sendProtocolMessage("*bl");
        Boolean ok = Binary.Deserialize(response, Boolean.class);
        return ok;
    }
    public boolean reloadHolidays() throws IOException, Exception {
        byte[] response = sendProtocolMessage("*hd");
        Boolean ok = Binary.Deserialize(response, Boolean.class);
        return ok;
    }
    public boolean reloadSetup() throws IOException, Exception {
        byte[] response = sendProtocolMessage("*st");
        Boolean ok = Binary.Deserialize(response, Boolean.class);
        return ok;
    }
    public boolean webQueueAdd(long userID, String callBackNumber) throws IOException, Exception {
        byte[] response = sendProtocolMessage("%wq|u=" + String.valueOf(userID) + "&n=" + callBackNumber);
        String message = Binary.Deserialize(response, String.class);
        if (message.equals("Success")) { return true; }
        
        throw new Exception(message);
    }
    public boolean setMailBox(long callID) throws IOException, Exception {
        byte[] response = sendProtocolMessage("%mb|c=" + String.valueOf(callID));
        String message = Binary.Deserialize(response, String.class);
        if (message.equals("Success")) { return true; }

        throw new Exception(message);
    }
    public boolean saveAgent(Agent agent) throws InvalidTerminalException, InvalidDirectoryNumberException, AgentInUseException, IOException, Exception {
        String agentJson = DeveloperCity.Serialization.JSON.Serialize(agent);
        byte[] response = sendProtocolMessage( "%Ag|" + agentJson );
        String message = Binary.Deserialize(response, String.class);
        if (message.equals("InvalidTerminalException")) { throw new InvalidTerminalException(); }
        else if (message.equals("InvalidDirectoryNumberException")) { throw new InvalidDirectoryNumberException(); }
        else if (message.equals("AgentInUseException")) { throw new AgentInUseException(); }
        else if (message.equals("Success")) { return true; }

        throw new Exception(message);
    }
    public boolean isAdministrativeRunning() throws IOException, Exception {
        byte[] response = sendProtocolMessage("?ad");
        String ok = Binary.Deserialize(response, String.class);
        return ok.equalsIgnoreCase("OK");
    }
    public boolean isQueueRunning() throws IOException, Exception {
        byte[] response = sendProtocolMessage("?qu");
        String ok = Binary.Deserialize(response, String.class);
        return ok.equalsIgnoreCase("OK");
    }
    public boolean isDialerRunning() throws IOException, Exception {
        byte[] response = sendProtocolMessage("?di");
        String ok = Binary.Deserialize(response, String.class);
        return ok.equalsIgnoreCase("OK");
    }
    public boolean stopQueueProcess() throws IOException, Exception {
        byte[] response = sendProtocolMessage("$qu=0");
        String ok = Binary.Deserialize(response, String.class);
        return ok.equalsIgnoreCase("OK");
    }
    public boolean startApplication(String startCommand) throws IOException, Exception {
        if (isAlive()) {
            return true;
        }
        try {
            Runtime.getRuntime().exec(startCommand);
        } catch(Exception e) {return false;}
        return true;
    }
    public boolean stopApplication() throws IOException, Exception {
        try {
            byte[] response = sendProtocolMessage("-shd");
        } catch(Exception e) { }
        return !isAlive();
    }
    public boolean isAlive() throws IOException, Exception {
        byte[] response = null;
        try {
            response = sendProtocolMessage("?ad");
        } catch(Exception e) {
            response = null;
        }

        return (response == null) ? false : (new String(response, Charset.forName(encoding))).equals("OK");
    }
    public boolean startQueueProcess() throws IOException, Exception {
        byte[] response = sendProtocolMessage("$qu=1");
        String ok = Binary.Deserialize(response, String.class);
        return ok.equalsIgnoreCase("OK");
    }
    public boolean stopDialerProcess() throws IOException, Exception {
        byte[] response = sendProtocolMessage("$di=0");
        String ok = Binary.Deserialize(response, String.class);
        return ok.equalsIgnoreCase("OK");
    }
    public boolean startDialerProcess() throws IOException, Exception {
        byte[] response = sendProtocolMessage("$di=1");
        String ok = Binary.Deserialize(response, String.class);
        return ok.equalsIgnoreCase("OK");
    }
    public boolean startAgentBreak(long agentID, long breakTypeID) throws IOException, Exception {
        byte[] response = sendProtocolMessage(String.format("!Br?b=1&agent=%d&break=%d", agentID, breakTypeID));
        Boolean ok = Binary.Deserialize(response, Boolean.class);
        return ok;
    }
    public boolean endAgentBreak(long agentID, long breakID) throws IOException, Exception {
        byte[] response = sendProtocolMessage(String.format("!Br?b=0&agent=%d&break=%d", agentID, breakID));
        Boolean ok = Binary.Deserialize(response, Boolean.class);
        return ok;
    }
    public boolean startAgentSession(long agentID) throws IOException, Exception {
        byte[] response = sendProtocolMessage(String.format("!Ag?s=0&agent=%d", agentID));
        Boolean ok = Binary.Deserialize(response, Boolean.class);
        return ok;
    }
    public boolean restartAgentSession(long agentID, long breakTypeID) throws IOException, Exception {
        byte[] response = sendProtocolMessage(String.format("!Ag?s=2&agent=%d&break=%d", agentID, breakTypeID));
        Boolean ok = Binary.Deserialize(response, Boolean.class);
        return ok;
    }
    public boolean endAgentSession(long agentID) throws IOException, Exception {
        byte[] response = sendProtocolMessage(String.format("!Ag?s=1&agent=%d", agentID));
        Boolean ok = Binary.Deserialize(response, Boolean.class);
        return ok;
    }

    private static byte[] sendProtocolMessage(String command) throws IOException, ClassNotFoundException, Exception {
        Socket socket = new Socket();
        SocketAddress address = new InetSocketAddress("localhost", 1910);

        try { socket.connect(address); }
        catch(Exception e) { logger.error(e); throw e; }

        OutputStream out = socket.getOutputStream();
        InputStream in = socket.getInputStream();

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out, encoding));
        bw.write(command.concat("\n"));
        bw.flush();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int b = 0;
        while ( (b = in.read()) >= 0 ) {
            baos.write(b);
        }
        baos.flush();
        try { socket.close(); } catch(Exception e) { logger.error(e); return null; }
        byte[] retValue = baos.toByteArray();
        baos.close();
        return retValue;
    }
}
