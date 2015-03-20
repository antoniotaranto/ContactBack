package DeveloperCity.ContactBack.DomainModel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author lbarbosa
 */
public class Customer extends User implements Serializable {

    private static final long serialVersionUID = 1L;
    private String MobilePhone;

    public Customer() {
    }

    public Customer(long UserID, String Username, String Name, String Email, Date Birthday, String Password, UserStatus UserStatus, List<UserPermission> Permissions, List<UserReport> Reports, List<UserChart> Charts, String MobilePhone) {
        super(UserID, Username, Name, Email, Birthday, Password, UserStatus, Permissions, Reports, Charts);
        this.MobilePhone = MobilePhone;
    }

    public String getMobilePhone() {
        return MobilePhone;
    }

    public void setMobilePhone(String MobilePhone) {
        this.MobilePhone = MobilePhone;
    }
}
