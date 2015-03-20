/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.Workflow.Telephony;

import com.cisco.cti.util.Condition;
import javax.telephony.JtapiPeer;
import javax.telephony.JtapiPeerFactory;
import javax.telephony.JtapiPeerUnavailableException;
import javax.telephony.MethodNotSupportedException;
import javax.telephony.Provider;
import javax.telephony.ProviderObserver;
import javax.telephony.ResourceUnavailableException;
import javax.telephony.events.ProvEv;
import javax.telephony.events.ProvInServiceEv;
import org.apache.log4j.Logger;

/**
 *
 * @author lbarbosa
 */
public class CallManagerProvider implements ProviderObserver {

    private static Logger logger = Logger.getLogger(CallManagerProvider.class);
    private Condition conditionInService = new Condition();
    private Provider provider;

    public CallManagerProvider() {
    }

    public synchronized void providerChangedEvent(ProvEv[] eventList) {
        if (eventList != null) {
            for (int i = 0; i < eventList.length; i++) {
                if (eventList[i] != null && eventList[i].getProvider() != null) {
                    try {
                        logger.warn(String.format("Provider Event: \"%s\" => %s", eventList[i].getProvider().getName(), eventList[i].toString()));
                    } catch (Exception e) { logger.error(e); }
                } else { logger.warn(eventList[i]); }
                if (eventList[i] instanceof ProvInServiceEv) {
                    conditionInService.set();
                }
            }
        }
    }

    public void Connect(String connectionString) throws JtapiPeerUnavailableException, ResourceUnavailableException, MethodNotSupportedException {
        logger.info("Initializing Jtapi");
        JtapiPeer peer = JtapiPeerFactory.getJtapiPeer(null);
        if (logger.isInfoEnabled()) {
            logger.info("Opening provider");
        }
        provider = peer.getProvider(connectionString);
        provider.addObserver(this);
        conditionInService.waitTrue();
    }

    public Provider getProvider() {
        return provider;
    }
}
