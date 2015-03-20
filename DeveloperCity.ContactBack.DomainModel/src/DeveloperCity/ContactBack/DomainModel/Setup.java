/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.DomainModel;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author lbarbosa
 */
public class Setup implements Serializable {

    private static final long serialVersionUID = 1L;
    private ConfigIVR configIVR;
    private short SetupID;
    private int TimeBetweenCallBacks;
    private Integer LateCallBackAfter;
    private Integer LateCallBackTime;
    private int EndOfQueue;
    private int MaxCallBacks;
    private String CTI_DeviceName;
    private String IVR_DeviceName;
    private String VoiceFolder;
    private String PrefixDial;
    private DestinationType InternalExtensionDestination;
    private DestinationType MobilePhoneDestination;
    private DestinationType LandLineDestination;
    private DestinationType InvalidNumberDestination;
    private java.util.Date CreatedOn = new java.util.Date();
    private java.util.Date ModifiedOn;
    private short ShiftWeekdayStartHour;
    private short ShiftWeekdayEndHour;
    private short ShiftWeekdayStartMinute;
    private short ShiftWeekdayEndMinute;
    private short ShiftSaturdayStartHour;
    private short ShiftSaturdayEndHour;
    private short ShiftSaturdayStartMinute;
    private short ShiftSaturdayEndMinute;
    private String QueueSuccessDevice;
    private String QueueAlreadyDevice;
    private String QueueInvalidNumberDevice;
    private String QueueNotInShiftTimeDevice;
    private String SMSUrl;
    private String SMSMessage;
    private String SMSAccount;
    private String SMSCode;
    private String SMSFrom;
    private String DefaultPassword;
    private String ProxyIP;
    private Integer ProxyPort;

    protected final long SystemBreakDeviceOff = 5000;

    public Setup() {
    }

    public String getCTI_DeviceName() {
        return CTI_DeviceName;
    }

    public void setCTI_DeviceName(String CTI_DeviceName) {
        this.CTI_DeviceName = CTI_DeviceName;
    }

    public String getIVR_DeviceName() {
        return IVR_DeviceName;
    }

    public void setIVR_DeviceName(String IVR_DeviceName) {
        this.IVR_DeviceName = IVR_DeviceName;
    }

    public String getVoiceFolder() {
        return VoiceFolder;
    }

    public void setVoiceFolder(String VoiceFolder) {
        this.VoiceFolder = VoiceFolder;
    }

    public Date getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(Date CreatedOn) {
        this.CreatedOn = CreatedOn;
    }

    public int getEndOfQueue() {
        return EndOfQueue;
    }

    public void setEndOfQueue(int EndOfQueue) {
        this.EndOfQueue = EndOfQueue;
    }

    public DestinationType getInternalExtensionDestination() {
        return InternalExtensionDestination;
    }

    public void setInternalExtensionDestination(DestinationType InternalExtensionDestination) {
        this.InternalExtensionDestination = InternalExtensionDestination;
    }

    public DestinationType getLandLineDestination() {
        return LandLineDestination;
    }

    public void setLandLineDestination(DestinationType LandLineDestination) {
        this.LandLineDestination = LandLineDestination;
    }

    public DestinationType getInvalidNumberDestination() {
        return InvalidNumberDestination;
    }

    public void setInvalidNumberDestination(DestinationType InvalidNumberDestination) {
        this.InvalidNumberDestination = InvalidNumberDestination;
    }

    public Integer getLateCallBackAfter() {
        return LateCallBackAfter;
    }

    public void setLateCallBackAfter(Integer LateCallBackAfter) {
        this.LateCallBackAfter = LateCallBackAfter;
    }

    public Integer getLateCallBackTime() {
        return LateCallBackTime;
    }

    public void setLateCallBackTime(Integer LateCallBackTime) {
        this.LateCallBackTime = LateCallBackTime;
    }

    public int getMaxCallBacks() {
        return MaxCallBacks;
    }

    public void setMaxCallBacks(int MaxCallBacks) {
        this.MaxCallBacks = MaxCallBacks;
    }

    public DestinationType getMobilePhoneDestination() {
        return MobilePhoneDestination;
    }

    public void setMobilePhoneDestination(DestinationType MobilePhoneDestination) {
        this.MobilePhoneDestination = MobilePhoneDestination;
    }

    public Date getModifiedOn() {
        return ModifiedOn;
    }

    public void setModifiedOn(Date ModifiedOn) {
        this.ModifiedOn = ModifiedOn;
    }

    public String getPrefixDial() {
        return PrefixDial;
    }

    public void setPrefixDial(String PrefixDial) {
        this.PrefixDial = PrefixDial;
    }

    public short getSetupID() {
        return SetupID;
    }

    public void setSetupID(short SetupID) {
        this.SetupID = SetupID;
    }

    public short getShiftSaturdayEndHour() {
        return ShiftSaturdayEndHour;
    }

    public void setShiftSaturdayEndHour(short ShiftSaturdayEndHour) {
        this.ShiftSaturdayEndHour = ShiftSaturdayEndHour;
    }

    public short getShiftSaturdayEndMinute() {
        return ShiftSaturdayEndMinute;
    }

    public void setShiftSaturdayEndMinute(short ShiftSaturdayEndMinute) {
        this.ShiftSaturdayEndMinute = ShiftSaturdayEndMinute;
    }

    public short getShiftSaturdayStartHour() {
        return ShiftSaturdayStartHour;
    }

    public void setShiftSaturdayStartHour(short ShiftSaturdayStartHour) {
        this.ShiftSaturdayStartHour = ShiftSaturdayStartHour;
    }

    public short getShiftSaturdayStartMinute() {
        return ShiftSaturdayStartMinute;
    }

    public void setShiftSaturdayStartMinute(short ShiftSaturdayStartMinute) {
        this.ShiftSaturdayStartMinute = ShiftSaturdayStartMinute;
    }

    public short getShiftWeekdayEndHour() {
        return ShiftWeekdayEndHour;
    }

    public void setShiftWeekdayEndHour(short ShiftWeekdayEndHour) {
        this.ShiftWeekdayEndHour = ShiftWeekdayEndHour;
    }

    public short getShiftWeekdayEndMinute() {
        return ShiftWeekdayEndMinute;
    }

    public void setShiftWeekdayEndMinute(short ShiftWeekdayEndMinute) {
        this.ShiftWeekdayEndMinute = ShiftWeekdayEndMinute;
    }

    public short getShiftWeekdayStartHour() {
        return ShiftWeekdayStartHour;
    }

    public void setShiftWeekdayStartHour(short ShiftWeekdayStartHour) {
        this.ShiftWeekdayStartHour = ShiftWeekdayStartHour;
    }

    public short getShiftWeekdayStartMinute() {
        return ShiftWeekdayStartMinute;
    }

    public void setShiftWeekdayStartMinute(short ShiftWeekdayStartMinute) {
        this.ShiftWeekdayStartMinute = ShiftWeekdayStartMinute;
    }

    public int getTimeBetweenCallBacks() {
        return TimeBetweenCallBacks;
    }

    public void setTimeBetweenCallBacks(int TimeBetweenCallBacks) {
        this.TimeBetweenCallBacks = TimeBetweenCallBacks;
    }

    public long getSystemBreakDeviceOff() {
        return this.SystemBreakDeviceOff;
    }

    public String getQueueAlreadyDevice() {
        return QueueAlreadyDevice;
    }

    public String getQueueInvalidNumberDevice() {
        return QueueInvalidNumberDevice;
    }

    public String getQueueNotInShiftTimeDevice() {
        return QueueNotInShiftTimeDevice;
    }

    public String getQueueSuccessDevice() {
        return QueueSuccessDevice;
    }

    public void setQueueAlreadyDevice(String QueueAlreadyDevice) {
        this.QueueAlreadyDevice = QueueAlreadyDevice;
    }

    public void setQueueInvalidNumberDevice(String QueueInvalidNumberDevice) {
        this.QueueInvalidNumberDevice = QueueInvalidNumberDevice;
    }

    public void setQueueNotInShiftTimeDevice(String QueueNotInShiftTimeDevice) {
        this.QueueNotInShiftTimeDevice = QueueNotInShiftTimeDevice;
    }

    public void setQueueSuccessDevice(String QueueSuccessDevice) {
        this.QueueSuccessDevice = QueueSuccessDevice;
    }

    public String getSMSMessage() {
        return SMSMessage;
    }

    public void setSMSMessage(String SMSMessage) {
        this.SMSMessage = SMSMessage;
    }

    public String getSMSUrl() {
        return SMSUrl;
    }

    public void setSMSUrl(String SMSUrl) {
        this.SMSUrl = SMSUrl;
    }

    public String getSMSAccount() {
        return SMSAccount;
    }

    public void setSMSAccount(String SMSAccount) {
        this.SMSAccount = SMSAccount;
    }

    public String getSMSCode() {
        return SMSCode;
    }

    public void setSMSCode(String SMSCode) {
        this.SMSCode = SMSCode;
    }

    public String getSMSFrom() {
        return SMSFrom;
    }

    public void setSMSFrom(String SMSFrom) {
        this.SMSFrom = SMSFrom;
    }

    public String getDefaultPassword() {
        return this.DefaultPassword;
    }

    public String getProxyIP() {
        return ProxyIP;
    }

    public void setProxyIP(String ProxyIP) {
        this.ProxyIP = ProxyIP;
    }

    public Integer getProxyPort() {
        return ProxyPort;
    }

    public void setProxyPort(Integer ProxyPort) {
        this.ProxyPort = ProxyPort;
    }

    public void setDefaultPassword(String DefaultPassword) {
        this.DefaultPassword = DefaultPassword;
    }
    public ConfigIVR getConfigIVR() {
        return configIVR;
    }

    public void setConfigIVR(ConfigIVR configIVR) {
        this.configIVR = configIVR;
    }
}