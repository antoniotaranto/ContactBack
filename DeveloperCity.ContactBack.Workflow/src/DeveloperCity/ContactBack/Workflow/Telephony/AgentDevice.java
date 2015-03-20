/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.Workflow.Telephony;

import DeveloperCity.ContactBack.DomainModel.Agent;
import DeveloperCity.ContactBack.Workflow.IAgentBreakObserver;
import DeveloperCity.ContactBack.Workflow.IAgentCallObserver;
import com.cisco.jtapi.extensions.CiscoCall;
import com.cisco.jtapi.extensions.CiscoCallEv;
import com.cisco.jtapi.extensions.CiscoTermInServiceEv;
import com.cisco.jtapi.extensions.CiscoTermOutOfServiceEv;
import com.cisco.jtapi.extensions.CiscoTerminal;
import java.util.ArrayList;
import java.util.List;
import javax.telephony.Connection;
import javax.telephony.callcontrol.CallControlCallObserver;
import javax.telephony.callcontrol.CallControlTerminalObserver;
import javax.telephony.callcontrol.events.CallCtlConnEstablishedEv;
import javax.telephony.events.CallActiveEv;
import javax.telephony.events.ConnFailedEv;
import javax.telephony.events.ConnInProgressEv;
import javax.telephony.events.TermConnDroppedEv;
import javax.telephony.events.TermConnRingingEv;
import org.apache.log4j.Logger;

/**
 *
 * @author lbarbosa
 */
public class AgentDevice implements CallControlCallObserver, CallControlTerminalObserver {

    private static Logger logger = Logger.getLogger(AgentDevice.class);
    private CiscoTerminal terminal;
    private Agent agent;
    private List<IAgentCallObserver> agentCallObservers = new ArrayList<IAgentCallObserver>();
    private List<IAgentBreakObserver> agentBreakObservers = new ArrayList<IAgentBreakObserver>();

    public AgentDevice(CiscoTerminal terminal, Agent agent) {
        this.terminal = terminal;
        this.agent = agent;
    }

    public void callChangedEvent(javax.telephony.events.CallEv[] events) {
        // Received Call On Agent
        for (javax.telephony.events.CallEv ev : events) {
            if (ev == null) {
                continue;
            }
            if (!(ev instanceof CiscoCallEv)) {
                continue;
            }
            CiscoCallEv ciscoEv = (CiscoCallEv) ev;
            if (ciscoEv.getCall() == null) {
                continue;
            }

            CiscoCall call = ((CiscoCall) ciscoEv.getCall());
            String from = call.getCallingAddress() == null ? "" : call.getCallingAddress().getName();
            String to = call.getCalledAddress() == null ? "" : call.getCalledAddress().getName();
            String lastRedirectTo = call.getLastRedirectedAddress() == null ? "" : call.getLastRedirectedAddress().getName();
            boolean isIncomingCall = false;
            boolean isOutgoingCall = false;
            long callID = call.getCallID().intValue();
            if (from.equals(agent.getDirectoryNumber())) {
                isOutgoingCall = true;
            }
            if (to.equals(agent.getDirectoryNumber()) || lastRedirectTo.equals(agent.getDirectoryNumber())) {
                isIncomingCall = true;
            }

            // Recebi ligação
            // Atendi ligação
            // Efetuei ligação
            // Fui atendido
            // Coloquei em hold
            // Voltei do hold
            // Finalizei ligação
            //   --> em andamento
            //   --> em hold remoto
            //   --> em hold local
            //   --> chamando remoto
            //   --> chamando local
            //   --> ocupado
            // Fui finalizado
            //   --> em andamento
            //   --> em hold remoto
            //   --> em hold local
            //   --> chamando remoto
            //   --> chamando local
            // Transferi
            //   --> com consulta
            //   --> sem consulta
            // Fui transferido
            //   --> com consulta
            //   --> sem consulta
            // Conference call
            //   --> Remoto iniciou
            //   --> Local iniciou
            //   --> Remotos finalizaram
            //   --> Local finalizou
            if (ciscoEv instanceof CallActiveEv) {
                // Tirando do gancho para iniciar nova ligação
                if (isOutgoingCall) {
                    for (IAgentCallObserver observer : agentCallObservers) {
                        observer.AgentStartingNewCall(agent, callID);
                    }
                }
            }
            else if (ciscoEv instanceof ConnInProgressEv) {
                // Discou, aguardando resultado
                if (isOutgoingCall) {
                    for (IAgentCallObserver observer : agentCallObservers) {
                        observer.AgentOutgoingCallStarting(agent, to, callID);
                    }
                }
            }
            else if (ciscoEv instanceof CallCtlConnEstablishedEv) {
                CallCtlConnEstablishedEv callCtlConnEstablishedEv = (CallCtlConnEstablishedEv) ciscoEv;
                if (callCtlConnEstablishedEv.getCalledAddress() == callCtlConnEstablishedEv.getConnection().getAddress()) {
                    // Discou, cliente remoto atendeu
                    if (isOutgoingCall) {
                        for (IAgentCallObserver observer : agentCallObservers) {
                            observer.AgentOutgoingCallAnswered(agent, to, callID);
                        }
                    }
                }
            }
            else if (ciscoEv instanceof ConnFailedEv) {
                ConnFailedEv connFailedEv = (ConnFailedEv) ciscoEv;
                if (connFailedEv.getConnection().getState() == Connection.FAILED &&
                    connFailedEv.getCall().getState() == javax.telephony.Call.ACTIVE) {
                    // Discou, mas deu ocupado
                    if (isOutgoingCall) {
                        for (IAgentCallObserver observer : agentCallObservers) {
                            observer.AgentOutgoingCallBusy(agent, to, callID);
                        }
                    }
                }
            }
            else if (ciscoEv.getID() == TermConnRingingEv.ID && ciscoEv.getMetaCode() == 128) {
                // Chamando...
                if (isOutgoingCall) {
                    for (IAgentCallObserver observer : agentCallObservers) {
                        observer.AgentOutgoingCallRinging(agent, to, callID);
                    }
                }
                else if (isIncomingCall) {
                    for (IAgentCallObserver observer : agentCallObservers) {
                        observer.AgentIncomingCallRinging(from, agent, callID);
                    }
                }
            }
            else if (ciscoEv instanceof TermConnDroppedEv) {
                // Chamada terminou para o agente (transferência, finalização)
                for (IAgentCallObserver observer : agentCallObservers) {
                    observer.AgentEndedCall(agent, callID);
                }
            }
        }
    }

    public void terminalChangedEvent(javax.telephony.events.TermEv[] events) {
        for (javax.telephony.events.TermEv ev : events) {
            if (ev instanceof CiscoTermOutOfServiceEv) {
                for (IAgentBreakObserver observer : agentBreakObservers) {
                    observer.OutOfServicePhone(agent);
                }
            }
            else if (ev instanceof CiscoTermInServiceEv) {
                for (IAgentBreakObserver observer : agentBreakObservers) {
                    observer.InServicePhone(agent);
                }
            }
        }
    }

    public void addAgentCallObserver(IAgentCallObserver observer) {
        agentCallObservers.add(observer);
    }
    public void removeAgentCallObserver(IAgentCallObserver observer) {
        agentCallObservers.remove(observer);
    }
    public void addAgentBreakListener(IAgentBreakObserver observer) {
        agentBreakObservers.add(observer);
    }
    public void removeAgentBreakListener(IAgentBreakObserver observer) {
        agentBreakObservers.remove(observer);
    }

    public Agent getAgent() {
        return agent;
    }
    public CiscoTerminal getTerminal() {
        return terminal;
    }
}
