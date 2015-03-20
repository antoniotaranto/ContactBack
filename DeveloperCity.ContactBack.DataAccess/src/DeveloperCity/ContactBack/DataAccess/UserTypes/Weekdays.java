/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.DataAccess.UserTypes;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.hibernate.HibernateException;

/**
 *
 * @author lbarbosa
 */
public class Weekdays implements org.hibernate.usertype.UserType {

    public int[] sqlTypes() {
        return new int[]{java.sql.Types.SMALLINT};
    }

    @SuppressWarnings("rawtypes")
    public Class returnedClass() {
        return List.class;
    }

    public boolean equals(Object arg0, Object arg1) throws HibernateException {
        if (arg0 == arg1) {
            return true;
        }
        if (arg0 == null || arg1 == null) {
            return false;
        }
        return arg0.equals(arg1);
    }

    public int hashCode(Object arg0) throws HibernateException {
        return arg0 == null ? Boolean.class.hashCode() + 473 : arg0.hashCode();
    }

    public Object nullSafeGet(ResultSet arg0, String[] arg1, Object arg2) throws HibernateException, SQLException {
        Short obj = (Short) org.hibernate.Hibernate.SHORT.nullSafeGet(arg0, arg1[0]);
        if (obj == null) {
            return null;
        }
        return DeveloperCity.ContactBack.DomainModel.Weekdays.getFrom( obj );
    }

    @SuppressWarnings("unchecked")
    public void nullSafeSet(PreparedStatement arg0, Object arg1, int arg2) throws HibernateException, SQLException {
        if (arg1 != null) {
            try {
                List<DeveloperCity.ContactBack.DomainModel.Weekdays> v = (List<DeveloperCity.ContactBack.DomainModel.Weekdays>) arg1;
                org.hibernate.Hibernate.SHORT.nullSafeSet(arg0, DeveloperCity.ContactBack.DomainModel.Weekdays.getFrom(v), arg2);
            } catch(Exception e) { org.hibernate.Hibernate.SHORT.nullSafeSet(arg0, 0, arg2); }
        }
        else {
            org.hibernate.Hibernate.SHORT.nullSafeSet(arg0, 0, arg2);
        }
    }

    @SuppressWarnings("unchecked")
    public Object deepCopy(Object arg0) throws HibernateException {
        if (arg0 == null) {
            return null;
        }
        else {
            try {
                List<DeveloperCity.ContactBack.DomainModel.Weekdays> cloneFrom = (List<DeveloperCity.ContactBack.DomainModel.Weekdays>) arg0;
                List<DeveloperCity.ContactBack.DomainModel.Weekdays> cloneTo = DeveloperCity.ContactBack.DomainModel.Weekdays.getFrom(DeveloperCity.ContactBack.DomainModel.Weekdays.getFrom(cloneFrom));
                return cloneTo;
            } catch(Exception e) { return null; }
        }
    }

    public boolean isMutable() {
        return false;
    }

    public Serializable disassemble(Object arg0) throws HibernateException {
        return (Serializable) deepCopy(arg0);
    }

    public Object assemble(Serializable arg0, Object arg1) throws HibernateException {
        return deepCopy(arg0);
    }

    public Object replace(Object arg0, Object arg1, Object arg2) throws HibernateException {
        return deepCopy(arg0);
    }
}
