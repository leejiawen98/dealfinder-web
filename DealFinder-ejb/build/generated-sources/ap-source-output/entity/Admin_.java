package entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-03-25T22:06:11")
@StaticMetamodel(Admin.class)
public class Admin_ { 

    public static volatile SingularAttribute<Admin, String> firstName;
    public static volatile SingularAttribute<Admin, String> lastName;
    public static volatile SingularAttribute<Admin, String> password;
    public static volatile SingularAttribute<Admin, String> salt;
    public static volatile SingularAttribute<Admin, Long> adminId;
    public static volatile SingularAttribute<Admin, String> username;

}