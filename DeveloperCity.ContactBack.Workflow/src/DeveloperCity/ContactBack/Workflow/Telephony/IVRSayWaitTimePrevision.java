package DeveloperCity.ContactBack.Workflow.Telephony;

import DeveloperCity.ContactBack.DomainModel.ConfigIVR;
import DeveloperCity.ContactBack.DomainModel.Utils;
import org.apache.log4j.Logger;

/**
 *
 * @author lbarbosa
 */
public class IVRSayWaitTimePrevision extends IVRPlayAndDrop {
    private static Logger logger = Logger.getLogger(IVRSayWaitTimePrevision.class);
    private ConfigIVR configIVR;

    public IVRSayWaitTimePrevision(String deviceName, String waveFolderName, String waveFileName, CallManagerProvider callManagerProvider, int udpLocalPort, ConfigIVR configIVR) {
        super(deviceName, waveFolderName, waveFileName, callManagerProvider, udpLocalPort);
        this.configIVR = configIVR;
    }

    private String getPrevisionAudioFiles(long timeToCallBack) {
        long[] timespan = Utils.TimeSpan(timeToCallBack);
        return "";
    }
}
