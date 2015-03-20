/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Workflow;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

/**
 *
 * @author lbarbosa
 */
public class ApplicationDiagnostics {
    private static Logger logger = Logger.getLogger(ApplicationDiagnostics.class);
    private Date StartTime;
    private String BuiltBy;
    private String BuiltDate;
    private String ImplementationTitle;
    private String ImplementationVendor;
    private String ImplementationVersion;

    public ApplicationDiagnostics(Date StartTime, String BuiltBy, String BuiltDate, String ImplementationTitle, String ImplementationVendor, String ImplementationVersion) {
        this.StartTime = StartTime;
        this.BuiltBy = BuiltBy;
        this.BuiltDate = BuiltDate;
        this.ImplementationTitle = ImplementationTitle;
        this.ImplementationVendor = ImplementationVendor;
        this.ImplementationVersion = ImplementationVersion;
    }

    private java.lang.management.MemoryMXBean getMemory() {
        return ManagementFactory.getMemoryMXBean();
    }
    public void logAll(Priority p) {
        logger.log(p, "-------------------------------------------------------------------------------------------------------------------------------------------");
        logger.log(p, String.format("Application: %s %s %s", getImplementationVendor(), getImplementationTitle(), getImplementationVersion()));
        logger.log(p, String.format("Built: on %s by %s", getBuiltDate(), getBuiltBy()));
        logger.log(p, String.format("OS: %s (%s) %s", getOSName(), getOSArch(), getOSVersion()));
        logger.log(p, String.format("Java: %s by %s (%s)", getJavaVersion(), getJavaVendor(), getJavaHome()));
        logger.log(p, String.format("User: %s (%s)", getUserName(), getUserHome()));
        logger.log(p, String.format("Current directory: %s", getCurrentDirectory()));
        logger.log(p, String.format("Current timezone: %s", getCurrentTimeZone()));
        logger.log(p, String.format("Current offset: %d", getCurrentOffset()));
        logger.log(p, String.format("Current daylight savings: %d", getDaylightSavingsOffset()));
        logger.log(p, String.format("App Memory Heap: Max %d, Init %d, Committed %d, Used %d", getAppMemoryHeapMax(), getAppMemoryHeapInit(), getAppMemoryHeapCommitted(), getAppMemoryHeapUsed()));
        logger.log(p, String.format("App Memory Non-Heap: Max %d, Init %d, Committed %d, Used %d", getAppMemoryNonHeapMax(), getAppMemoryNonHeapInit(), getAppMemoryNonHeapCommitted(), getAppMemoryNonHeapUsed()));
        logger.log(p, String.format("VM Memory: free %d, max %d, total %d", getVMMemoryFree(), getVMMemoryMax(), getVMMemoryTotal()));
        logger.log(p, String.format("Objects With Pending Finalization: %d", getObjectPendingFinalizationCount()));
        logger.log(p, String.format("Processors: %d", getProcessors()));
        logger.log(p, "-------------------------------------------------------------------------------------------------------------------------------------------");
    }

    public String toJson() throws IOException {
        return DeveloperCity.Serialization.JSON.Serialize(this, null, null);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String lf = System.getProperty("line.separator");
        sb.append(String.format("Application: %s %s %s%s", ImplementationVendor, ImplementationTitle, ImplementationVersion, lf));
        sb.append(String.format("Built: on %s by %s%s", BuiltDate, BuiltBy, lf));
        sb.append(String.format("OS: %s (%s) %s%s", getOSName(), getOSArch(), getOSVersion(), lf));
        sb.append(String.format("Java: %s by %s (%s)%s", getJavaVersion(), getJavaVendor(), getJavaHome(), lf));
        sb.append(String.format("User: %s (%s)%s", getUserName(), getUserHome(), lf));
        sb.append(String.format("Current directory: %s%s", getCurrentDirectory(), lf));
        sb.append(String.format("Current timezone: %s%s", getCurrentTimeZone(), lf ));
        sb.append(String.format("Current offset: %d%s", getCurrentOffset(), lf ));
        sb.append(String.format("Current daylight savings: %d%s", getDaylightSavingsOffset(), lf ));
        sb.append(String.format("App Memory Heap: Max %d, Init %d, Committed %d, Used %d%s", getAppMemoryHeapMax(), getAppMemoryHeapInit(), getAppMemoryHeapCommitted(), getAppMemoryHeapUsed(), lf));
        sb.append(String.format("App Memory Non-Heap: Max %d, Init %d, Committed %d, Used %d%s", getAppMemoryNonHeapMax(), getAppMemoryNonHeapInit(), getAppMemoryNonHeapCommitted(), getAppMemoryNonHeapUsed(), lf));
        sb.append(String.format("VM Memory: free %d, max %d, total %d%s", getVMMemoryFree(), getVMMemoryMax(), getVMMemoryTotal(), lf));
        sb.append(String.format("Objects With Pending Finalization: %d%s", getObjectPendingFinalizationCount(), lf));
        sb.append(String.format("Processors: %d", getProcessors()));
        return sb.toString();
    }

    public Date getStartTime() {
        return StartTime;
    }
    private void setStartTime(Date StartTime) {
        this.StartTime = StartTime;
    }
    public String getBuiltBy() {
        return BuiltBy;
    }
    private void setBuiltBy(String BuiltBy) {
        this.BuiltBy = BuiltBy;
    }
    public String getBuiltDate() {
        return BuiltDate;
    }
    private void setBuiltDate(String BuiltDate) {
        this.BuiltDate = BuiltDate;
    }
    public String getImplementationTitle() {
        return ImplementationTitle;
    }
    private void setImplementationTitle(String ImplementationTitle) {
        this.ImplementationTitle = ImplementationTitle;
    }
    public String getImplementationVendor() {
        return ImplementationVendor;
    }
    private void setImplementationVendor(String ImplementationVendor) {
        this.ImplementationVendor = ImplementationVendor;
    }
    public String getImplementationVersion() {
        return ImplementationVersion;
    }
    private void setImplementationVersion(String ImplementationVersion) {
        this.ImplementationVersion = ImplementationVersion;
    }

    public long getAppMemoryHeapUsed() {
        return getMemory().getHeapMemoryUsage().getUsed();
    }
    private void setAppMemoryHeapUsed(long AppMemoryHeap) { }
    public long getAppMemoryHeapCommitted() {
        return getMemory().getHeapMemoryUsage().getCommitted();
    }
    private void setAppMemoryHeapCommitted(long AppMemoryHeap) { }
    public long getAppMemoryHeapInit() {
        return getMemory().getHeapMemoryUsage().getInit();
    }
    private void setAppMemoryHeapInit(long AppMemoryHeap) { }
    public long getAppMemoryHeapMax() {
        return getMemory().getHeapMemoryUsage().getMax();
    }
    private void setAppMemoryHeapMax(long AppMemoryHeap) { }

    public long getAppMemoryNonHeapUsed() {
        return getMemory().getNonHeapMemoryUsage().getUsed();
    }
    private void setAppMemoryNonHeapUsed(long AppMemoryTotal) { }
    public long getAppMemoryNonHeapCommitted() {
        return getMemory().getNonHeapMemoryUsage().getCommitted();
    }
    private void setAppMemoryNonHeapCommitted(long AppMemoryTotal) { }
    public long getAppMemoryNonHeapInit() {
        return getMemory().getNonHeapMemoryUsage().getInit();
    }
    private void setAppMemoryNonHeapInit(long AppMemoryTotal) { }
    public long getAppMemoryNonHeapMax() {
        return getMemory().getNonHeapMemoryUsage().getMax();
    }
    private void setAppMemoryNonHeapMax(long AppMemoryTotal) { }

    public int getObjectPendingFinalizationCount() {
        return getMemory().getObjectPendingFinalizationCount();
    }
    private void setObjectPendingFinalizationCount(int ObjectPendingFinalizationCount) { }

    public String getCurrentDirectory() {
        return System.getProperty("user.dir");
    }
    private void setCurrentDirectory(String CurrentDirectory) { }

    public String getCurrentTimeZone() {
        return System.getProperty("user.timezone");
    }
    private void setCurrentTimeZone(String CurrentTimeZone) { }

    public int getCurrentOffset() {
        return TimeZone.getDefault().getOffset(Calendar.getInstance().getTime().getTime());
    }

    public void setCurrentOffset(int CurrentOffset) { }

    public int getDaylightSavingsOffset() {
        return TimeZone.getDefault().getDSTSavings();
    }

    public void setDaylightSavingsOffset(int DaylightSavingsOffset) { }

    public String getJavaHome() {
        return System.getProperty("java.home");
    }
    private void setJavaHome(String JavaHome) { }

    public String getJavaVendor() {
        return System.getProperty("java.vendor");
    }
    private void setJavaVendor(String JavaVendor) { }

    public String getJavaVersion() {
        return System.getProperty("java.version");
    }
    private void setJavaVersion(String JavaVersion) { }

    public String getOSArch() {
        return System.getProperty("os.arch");
    }
    private void setOSArch(String OSArch) { }

    public String getOSName() {
        return System.getProperty("os.name");
    }
    private void setOSName(String OSName) { }

    public String getOSVersion() {
        return System.getProperty("os.version");
    }
    private void setOSVersion(String OSVersion) { }

    public int getProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }
    private void setProcessors(int Processors) { }

    public String getUserHome() {
        return System.getProperty("user.home");
    }
    private void setUserHome(String UserHome) { }

    public String getUserName() {
        return System.getProperty("user.name");
    }
    private void setUserName(String UserName) { }

    public long getVMMemoryFree() {
        return Runtime.getRuntime().freeMemory();
    }
    private void setVMMemoryFree(long VMMemoryFree) { }

    public long getVMMemoryMax() {
        return Runtime.getRuntime().maxMemory();
    }
    private void setVMMemoryMax(long VMMemoryMax) { }

    public long getVMMemoryTotal() {
        return Runtime.getRuntime().totalMemory();
    }
    private void setVMMemoryTotal(long VMMemoryTotal) { }
}
