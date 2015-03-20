/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.Serialization;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;

/**
 *
 * @author lbarbosa
 */
public final class XML {

    private XML() {
    }

    public static String Serialize(Object o) throws java.io.IOException {
        return Serialize(o, null);
    }

    public static String Serialize(Object o, String encoding) throws java.io.IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(baos);
        java.beans.XMLEncoder encoder = new java.beans.XMLEncoder(bos);

        encoder.writeObject(o);
        encoder.flush();
        String retValue = encoding == null ? baos.toString() : baos.toString(encoding);
        encoder.close();
        bos.close();
        baos.close();
        return retValue;
    }

    public static <T> T Deserialize(String s, Class<T> type) throws java.io.IOException, java.lang.ClassNotFoundException {
        return Deserialize(s, null, type);
    }

    public static <T> T Deserialize(String s, String encoding, Class<T> type) throws java.io.IOException, java.lang.ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(encoding == null ? s.getBytes() : s.getBytes(encoding));
        BufferedInputStream bis = new java.io.BufferedInputStream(bais);
        java.beans.XMLDecoder decoder = new java.beans.XMLDecoder(bis);
        Object out = decoder.readObject();
        T retValue = (T) out;
        return retValue;
    }
}
