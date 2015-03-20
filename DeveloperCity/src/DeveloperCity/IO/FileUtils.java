/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.IO;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author lbarbosa
 */
public class FileUtils {

    public static String FileToBase64(String filePath) throws IOException {
        File fileInfo = new File(filePath);
        if (fileInfo.length() > Integer.MAX_VALUE) {
            throw new IOException("File size too large to index");
        }

        FileInputStream fis = null;
        byte[] fileData = null;
        try {
            fis = new FileInputStream(fileInfo);
            fileData = new byte[(int) fileInfo.length()];
            fis.read(fileData);
            fis.close();
        } catch (IOException e) {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception ex) {
                }
            }
            throw e;
        } catch (Exception e) {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception ex) {
                }
            }
            throw new IOException(e);
        }
        String s = "";
        if (fileData != null) {
            s = new sun.misc.BASE64Encoder().encode(fileData);
        }
        return s;
    }
}
