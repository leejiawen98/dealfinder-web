package entity;

import entity.Customer;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-03-25T22:06:11")
@StaticMetamodel(CreditCard.class)
public class CreditCard_ { 

    public static volatile SingularAttribute<CreditCard, String> cvv;
    public static volatile SingularAttribute<CreditCard, Long> ccId;
    public static volatile SingularAttribute<CreditCard, String> ccNum;
    public static volatile SingularAttribute<CreditCard, String> ccName;
    public static volatile SingularAttribute<CreditCard, Date> expDate;
    public static volatile SingularAttribute<CreditCard, Customer> customer;

}