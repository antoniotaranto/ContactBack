/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.Serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author lbarbosa
 */
public final class Binary {

    private Binary() {
    }

    public static byte[] Serialize(Object o) throws java.io.IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        byte[] retValue = baos.toByteArray();
        baos.close();
        oos.close();
        return retValue;
    }

    public static <T> T Deserialize(byte[] b, Class<T> type) throws java.io.IOException, java.lang.ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object out = ois.readObject();
        T retValue = (T) out;
        return retValue;
    }
}
