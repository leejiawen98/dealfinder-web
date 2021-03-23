package entity;

import entity.BankAccount;
import entity.Deal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-03-23T16:46:52")
@StaticMetamodel(Business.class)
public class Business_ extends User_ {

    public static volatile SingularAttribute<Business, BankAccount> bankAccount;
    public static volatile ListAttribute<Business, Deal> deals;
    public static volatile SingularAttribute<Business, String> name;

}