/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack;

import DeveloperCity.ContactBack.Workflow.ApplicationDiagnostics;
import java.net.URL;
import java.util.Date;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import org.apache.log4j.Logger;

/**
 *
 * @author lbarbosa
 */
class PackageVersion {
    private ApplicationDiagnostics applicationDiagnostics;
    private static Logger logger = Logger.getLogger(PackageVersion.class);

    PackageVersion() {
        try
        {
            String className = this.getClass().getSimpleName();
            String classFileName = className + ".class";
            String classFilePath = this.getClass().getPackage().toString().replace('.', '/') + "/"+ className;
            String pathToThisClass = this.getClass().getResource(classFileName).toString();
            int indexOfExclamation = pathToThisClass.indexOf("!");
            String pathToManifest =
                    (indexOfExclamation >= 0)
                        ? pathToThisClass.substring(0, indexOfExclamation + 1) + "/META-INF/MANIFEST.MF"
                        : pathToThisClass.substring(0, pathToThisClass.indexOf("/build/classes/")) + "/manifest.mf";
            Manifest manifest = new Manifest(new URL(pathToManifest).openStream());
            Attributes attr = manifest.getMainAttributes();
            applicationDiagnostics = new ApplicationDiagnostics(
                    new Date(),
                    attr.getValue("user"),
                    attr.getValue("date"),
                    attr.getValue(Attributes.Name.IMPLEMENTATION_TITLE),
                    attr.getValue(Attributes.Name.IMPLEMENTATION_VENDOR),
                    attr.getValue(Attributes.Name.IMPLEMENTATION_VERSION)
                    );
        }
        catch (Exception e)
        {
            logger.error(e);
        }
    }

    public ApplicationDiagnostics getApplicationDiagnostics() {
        return applicationDiagnostics;
    }

    public void setApplicationDiagnostics(ApplicationDiagnostics applicationDiagnostics) {
        this.applicationDiagnostics = applicationDiagnostics;
    }
}
