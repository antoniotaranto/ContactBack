/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.DataAccess.UserTypes;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.hibernate.HibernateException;

/**
 *
 * @author lbarbosa
 */
public class UserStatus implements org.hibernate.usertype.UserType {

    public int[] sqlTypes() {
        return new int[]{java.sql.Types.SMALLINT};
    }

    @SuppressWarnings("rawtypes")
    public Class returnedClass() {
        return DeveloperCity.ContactBack.DomainModel.UserStatus.class;
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
        return DeveloperCity.ContactBack.DomainModel.UserStatus.getFrom( obj );
    }

    public void nullSafeSet(PreparedStatement arg0, Object arg1, int arg2) throws HibernateException, SQLException {
        if (arg1 != null) {
            DeveloperCity.ContactBack.DomainModel.UserStatus v = (DeveloperCity.ContactBack.DomainModel.UserStatus) arg1;
            org.hibernate.Hibernate.SHORT.nullSafeSet(arg0, v.getStatus(), arg2);
        }
        else {
            org.hibernate.Hibernate.SHORT.nullSafeSet(arg0, arg1, arg2);
        }
    }

    public Object deepCopy(Object arg0) throws HibernateException {
        if (arg0 == null) {
            return null;
        }
        else {
            return DeveloperCity.ContactBack.DomainModel.UserStatus.getFrom( ((DeveloperCity.ContactBack.DomainModel.UserStatus) arg0).getStatus() );
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
