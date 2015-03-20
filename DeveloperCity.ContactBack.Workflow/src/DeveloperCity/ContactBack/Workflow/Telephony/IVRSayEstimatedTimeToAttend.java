package DeveloperCity.ContactBack.Workflow.Telephony;

import DeveloperCity.ContactBack.DomainModel.ConfigIVR;
import DeveloperCity.ContactBack.DomainModel.IVR.PlayAndDropCall;
import DeveloperCity.ContactBack.DomainModel.Queue;
import DeveloperCity.ContactBack.Workflow.IWaitTimeEstimativeCalculator;
import org.apache.log4j.Logger;

/**
 *
 * @author lbarbosa
 */
public class IVRSayEstimatedTimeToAttend extends IVRPlayAndDrop {
    private static Logger logger = Logger.getLogger(IVRSayEstimatedTimeToAttend.class);
    private ConfigIVR configIVR;
    private IWaitTimeEstimativeCalculator waitTimeEstimativeCalculator;

    public IVRSayEstimatedTimeToAttend(String deviceName, String waveFolderName, String waveFileName, CallManagerProvider callManagerProvider, int udpLocalPort, ConfigIVR configIVR, IWaitTimeEstimativeCalculator waitTimeEstimativeCalculator) {
        super(deviceName, waveFolderName, waveFileName, callManagerProvider, udpLocalPort);
        this.configIVR = configIVR;
        this.waitTimeEstimativeCalculator = waitTimeEstimativeCalculator;
    }

    @Override
    protected String[] getFilesToPlay(PlayAndDropCall call) {
        logger.info(String.format("getFilesToPlay(PlayAndDropCall call = %s)", call));
        if (waveFileName.contains("%h") || waveFileName.contains("%p")) {
            String wholeText = waveFileName;
            Queue queueItem = waitTimeEstimativeCalculator.getQueueObject(call.getOriginalPhoneNumber());
            if (queueItem == null) {
                return wholeText.replace("%h", "").replace("%p", "").split(",");
            }
            wholeText = wholeText.contains("%h") ?
                (wholeText.replace("%h", configIVR.getFullTimeFiles(waitTimeEstimativeCalculator.calculate(queueItem), false))) :
                (wholeText);
            wholeText = wholeText.contains("%H") ?
                (wholeText.replace("%H", configIVR.getFullTimeFiles(waitTimeEstimativeCalculator.calculate(queueItem), true))) :
                (wholeText);
            wholeText = wholeText.contains("%p") ?
                (wholeText.replace("%p", configIVR.getNumberM(String.valueOf(queueItem.getEntryPosition())))) :
                (wholeText);
            logger.debug(wholeText);
            logger.info("getFilesToPlay(PlayAndDropCall call) !");
            return wholeText.split(",");
        } else { logger.debug(waveFileName); logger.info("getFilesToPlay(PlayAndDropCall call) !"); return waveFileName.split(","); }
    }
}
