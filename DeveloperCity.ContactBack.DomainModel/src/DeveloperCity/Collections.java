/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author lbarbosa
 */
public class Collections {
    public interface Predicate<T> { boolean apply(T type); }

    public static <T> Collection<T> filter(Collection<T> target, Predicate<T> predicate) {
        Collection<T> result = new ArrayList<T>();
        for (T element : target) {
            if (predicate.apply(element)) {
                result.add(element);
            }
        }
        return result;
    }
    public static <T> int count(Collection<T> target, Predicate<T> predicate) {
        int result = 0;
        for (T element : target) {
            if (predicate.apply(element)) {
                result++;
            }
        }
        return result;
    }
    public static <T> boolean contains(Collection<T> target, Predicate<T> predicate) {
        for (T element : target) {
            if (predicate.apply(element)) {
                return true;
            }
        }
        return false;
    }
}
