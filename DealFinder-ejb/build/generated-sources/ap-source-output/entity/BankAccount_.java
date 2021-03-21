package entity;

import entity.Business;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-03-22T01:14:22")
@StaticMetamodel(BankAccount.class)
public class BankAccount_ { 

    public static volatile SingularAttribute<BankAccount, String> accNum;
    public static volatile SingularAttribute<BankAccount, String> bank;
    public static volatile SingularAttribute<BankAccount, Business> business;
    public static volatile SingularAttribute<BankAccount, Long> accId;

}