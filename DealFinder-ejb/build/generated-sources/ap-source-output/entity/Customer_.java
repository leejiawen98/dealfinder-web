package entity;

import entity.CreditCard;
import entity.Deal;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-03-26T18:53:51")
@StaticMetamodel(Customer.class)
public class Customer_ extends User_ {

    public static volatile SingularAttribute<Customer, String> firstName;
    public static volatile SingularAttribute<Customer, String> lastName;
    public static volatile ListAttribute<Customer, Deal> favDeals;
    public static volatile ListAttribute<Customer, Deal> deals;
    public static volatile SingularAttribute<Customer, CreditCard> creditCard;
    public static volatile SingularAttribute<Customer, BigDecimal> eWalletAmount;

}