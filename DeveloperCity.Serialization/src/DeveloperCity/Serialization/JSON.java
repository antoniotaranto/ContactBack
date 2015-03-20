/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.Serialization;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.List;

/**
 *
 * @author lbarbosa
 */
public final class JSON {

    private JSON() {
    }

    public static String Serialize(Object o, List includes, List excludes) throws java.io.IOException {
        JSONSerializer s = new JSONSerializer();
        if (includes != null) {
            s.setIncludes(includes);
        }
        if (excludes != null) {
            s.setExcludes(excludes);
        }
        return s.serialize(o);
    }

    public static String Serialize(Object o) throws java.io.IOException {
        return Serialize(o, null, null);
    }

    public static String DeepSerialize(Object o) throws java.io.IOException {
        JSONSerializer s = new JSONSerializer();
        return s.deepSerialize(o);
    }

    public static <T> T Deserialize(String s, Class<T> type) throws java.io.IOException, java.lang.ClassNotFoundException {
        JSONDeserializer<T> d = new JSONDeserializer<T>();
        return d.deserialize(s);
    }
}
