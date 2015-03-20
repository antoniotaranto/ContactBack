/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Workflow;

import DeveloperCity.ContactBack.DomainModel.Agent;
import DeveloperCity.ContactBack.DomainModel.Call;
import DeveloperCity.ContactBack.DomainModel.CallStatus;
import DeveloperCity.ContactBack.DomainModel.Queue;
import DeveloperCity.ContactBack.DomainModel.QueueStatus;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.log4j.Logger;

/**
 *
 * @author lbarbosa
 */
public class DialerManager implements IAgentCallObserver {
    private static Logger logger = Logger.getLogger(DialerManager.class);
    private Date lastPair;
    private final Object lastPairSync;
    private RealTimeData realTimeData;
    private QueueService sQueue;
    private CallService sCall;
    private AgentManager agentManager;
    private CallManagerService sCallManager;
    private boolean executing = false;
    private PlanCall planCall;
    private Timer tmrPlanCall;

    public DialerManager(RealTimeData realTimeData, QueueService sQueue, CallService sCall, AgentManager agentManager, CallManagerService sCallManager) {
        lastPair = new Date();
        lastPairSync = new Object();
        this.realTimeData = realTimeData;
        this.sQueue = sQueue;
        this.sCall = sCall;
        this.agentManager = agentManager;
        this.sCallManager = sCallManager;
        this.planCall = new PlanCall(this);
        this.tmrPlanCall = new Timer("PlanCallTick");
    }

    private class PlanCall extends TimerTask {
        private Logger logger = Logger.getLogger(PlanCall.class);
        private DialerManager dialerManager;
        PlanCall (DialerManager dialerManager) {
            this.dialerManager = dialerManager;
        }

        @Override
        public void run() {
            PairAndDialTime();
        }

        private synchronized void PairAndDialTime() {
            realTimeData.LockToCallBack(dialerManager);
        }


    }

    public void Start() {
        this.executing = true;
        tmrPlanCall.schedule(planCall, 0, 5000);
        this.sCallManager.addAgentCallObserver(this);
    }

    public void Stop() {
        this.executing = false;
        tmrPlanCall.cancel();
        this.sCallManager.removeAgentCallObserver(this);
    }

    public boolean isExecuting() {
        return executing;
    }

    public List<Call> Pair(List<Agent> agents, List<Queue> queue) {
        logger.info(String.format("Pair(%d agents, %d queueItems)", agents.size(), queue.size()));
        List<Call> retValue = new ArrayList<Call>();
        int min = Math.min(agents.size(), queue.size());
        if (min == 0) {
            return retValue;
        }
        for (int i = 0; i < min; i++) {
            Agent caller = agents.get(i);
            Queue queueItem = queue.get(i);
            Call newCall = new Call();
            newCall.setAgent(caller);
            newCall.setStartTime(new java.util.Date());
            newCall.setCallStatus(CallStatus.Planning);
            newCall.setQueue(queueItem);
            //caller.setCurrentCall(newCall);
            queueItem.setQueueStatus(QueueStatus.CallStatus);
            retValue.add(newCall);
        }
        return retValue;
    }

    public List<Call> Dial(List<Call> programmingCalls) {
        List<Call> retValue = new ArrayList<Call>();

        for (int i = 0; i < programmingCalls.size(); i ++) {
            Call call = programmingCalls.get(i);

            try {
                logger.info(String.format("Call %s", call));
                call = sCallManager.Dial(call);
                if (call.getCallManagerCallID() != 0 && call.getCallStatus() != CallStatus.Error) {
                    logger.info(String.format("Called, CCM CallID: %d", call.getCallManagerCallID()));
                    call.setCallStatus(CallStatus.Dialing);
                    //call.getAgent().setCurrentCall(call);
                    call.getQueue().setQueueStatus(QueueStatus.CallStatus);
                    retValue.add(call);
                } else {
                    throw new Exception("Invalid Cisco call ID (zero)");
                }
            }
            catch(Exception e) {
                logger.error(e);
                //call.getAgent().setCurrentCall(null);
                call.getQueue().setQueueStatus(QueueStatus.InQueue);
                call.setCallStatus(CallStatus.Error);
                call.setEndTime(new java.util.Date());
                continue;
            }

            programmingCalls.set(i, call);
        }
        return retValue;
    }

    public Call SaveCall(Call call, Agent agent, Queue queue, boolean saveAgent) throws Exception {
        call = sCall.setCall(call, agent, queue, saveAgent);
        return call;
    }

    public void AgentIncomingCallRinging(String from, Agent to, long callManagerCallID) {
    }

    public void AgentStartingNewCall(Agent from, long callManagerCallID) {
    }

    public void AgentOutgoingCallStarting(Agent from, String to, long callManagerCallID) {
        logger.info(String.format("AgentOutgoingCallStarting(%s, %s, %d)", from, to, callManagerCallID));
        Call call = realTimeData.StartedCall(from, to, callManagerCallID, this);
        logger.info("AgentOutgoingCallStarting(Agent, String, long) !");
    }

    public void AgentOutgoingCallRinging(Agent from, String to, long callManagerCallID) {
        logger.info(String.format("AgentOutgoingCallRinging(%s, %s, %d)", from, to, callManagerCallID));
        Call call = realTimeData.RingingCall(from, to, callManagerCallID, this);
        logger.info("AgentOutgoingCallRinging(Agent, String, long) !");
    }

    public void AgentOutgoingCallAnswered(Agent from, String to, long callManagerCallID) {
        logger.info(String.format("AgentOutgoingCallAnswered(%s, %s, %d)", from, to, callManagerCallID));
        Call call = realTimeData.AnsweredCall(from, to, callManagerCallID, this);
        logger.info("AgentOutgoingCallAnswered(Agent, String, long) !");
    }

    public void AgentOutgoingCallBusy(Agent from, String to, long callManagerCallID) {
        logger.info(String.format("AgentOutgoingCallBusy(%s, %s, %d)", from, to, callManagerCallID));
        Call call = realTimeData.FailedCall(from, to, callManagerCallID, this);
        if (call != null) {
            sCallManager.EndCall(callManagerCallID);
        }
        logger.info("AgentOutgoingCallBusy(Agent, String, long) !");
    }

    public void AgentEndedCall(Agent agent, long callManagerCallID) {
        logger.info(String.format("AgentEndedCall(%s, %d)", agent, callManagerCallID));
        Call call = realTimeData.EndedCall(agent, callManagerCallID, this);
        logger.info("AgentEndedCall(Agent, long) !");
    }

}
