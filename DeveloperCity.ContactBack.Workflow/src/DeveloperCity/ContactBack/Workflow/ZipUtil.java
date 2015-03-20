package DeveloperCity.ContactBack.Workflow;

import java.io.*;
import java.util.zip.*;

/**
 *
 * @author lbarbosa
 */
public class ZipUtil {

    static final int BUFFER = 2048;

    public static void ZipFile(String sourceUncompressed, String destinationZip) throws FileNotFoundException, IOException {
        BufferedInputStream origin = null;
        FileOutputStream dest = new FileOutputStream(destinationZip);
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
        byte data[] = new byte[BUFFER];

        FileInputStream fi = new FileInputStream(sourceUncompressed);
        origin = new BufferedInputStream(fi, BUFFER);
        ZipEntry entry = new ZipEntry(sourceUncompressed);
        out.putNextEntry(entry);
        int count;
        while ((count = origin.read(data, 0, BUFFER)) != -1) {
            out.write(data, 0, count);
        }
        origin.close();
        out.close();
    }

    public static void ZipFile(String sourceUncompressed, OutputStream destinationStream) throws FileNotFoundException, IOException {
        BufferedInputStream origin = null;
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(destinationStream));
        byte data[] = new byte[BUFFER];
        String fileName = sourceUncompressed.substring(1 + sourceUncompressed.lastIndexOf(System.getProperty("file.separator")) );

        FileInputStream fi = new FileInputStream(sourceUncompressed);
        origin = new BufferedInputStream(fi, BUFFER);
        ZipEntry entry = new ZipEntry(fileName);

        out.putNextEntry(entry);
        int count;
        while ((count = origin.read(data, 0, BUFFER)) != -1) {
            out.write(data, 0, count);
        }
        origin.close();
        out.close();
    }

    public static void ZipFolder(String folderUncompressed, String destinationZip) throws FileNotFoundException, IOException {
        if (folderUncompressed.endsWith(System.getProperty("file.separator"))) {
            folderUncompressed = folderUncompressed.substring(0, folderUncompressed.length() - 1);
        }
        BufferedInputStream origin = null;
        FileOutputStream dest = new FileOutputStream(destinationZip);
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
        byte data[] = new byte[BUFFER];

        File sourceFolder = new File(folderUncompressed);
        if (sourceFolder == null || (!sourceFolder.exists()) || (!sourceFolder.isDirectory())) {
            throw new FileNotFoundException(folderUncompressed);
        }
        String files[] = sourceFolder.list();

        for (int i = 0; i < files.length; i++) {
            FileInputStream fi = new FileInputStream(files[i]);
            origin = new BufferedInputStream(fi, BUFFER);
            ZipEntry entry = new ZipEntry(files[i].replace(folderUncompressed, ""));
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0,
                                        BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            origin.close();
        }
        out.close();
    }

    public static void ZipFolder(String folderUncompressed, OutputStream destinationStream) throws FileNotFoundException, IOException {
        if (folderUncompressed.endsWith(System.getProperty("file.separator"))) {
            folderUncompressed = folderUncompressed.substring(0, folderUncompressed.length() - 1);
        }
        BufferedInputStream origin = null;
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(destinationStream));
        byte data[] = new byte[BUFFER];

        File sourceFolder = new File(folderUncompressed);
        if (sourceFolder == null || (!sourceFolder.exists()) || (!sourceFolder.isDirectory())) {
            throw new FileNotFoundException(folderUncompressed);
        }
        String files[] = sourceFolder.list();

        for (int i = 0; i < files.length; i++) {
            FileInputStream fi = new FileInputStream(files[i]);
            origin = new BufferedInputStream(fi, BUFFER);
            ZipEntry entry = new ZipEntry(files[i].replace(folderUncompressed, ""));
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0,
                                        BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            origin.close();
        }
        out.close();
    }
}
