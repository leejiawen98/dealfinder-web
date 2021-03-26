package entity;

import entity.Business;
import entity.Category;
import entity.Customer;
import entity.Review;
import entity.SaleTransaction;
import entity.Tag;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-03-26T18:53:51")
@StaticMetamodel(Deal.class)
public class Deal_ { 

    public static volatile SingularAttribute<Deal, BigDecimal> unitPrice;
    public static volatile SingularAttribute<Deal, String> serialNum;
    public static volatile SingularAttribute<Deal, Business> business;
    public static volatile SingularAttribute<Deal, Long> dealId;
    public static volatile SingularAttribute<Deal, String> description;
    public static volatile SingularAttribute<Deal, Date> endDateTime;
    public static volatile ListAttribute<Deal, SaleTransaction> saleTransactions;
    public static volatile ListAttribute<Deal, Tag> tags;
    public static volatile SingularAttribute<Deal, String> dealName;
    public static volatile ListAttribute<Deal, Customer> favCustomers;
    public static volatile SingularAttribute<Deal, Date> startDateTime;
    public static volatile SingularAttribute<Deal, Integer> quantityLeft;
    public static volatile ListAttribute<Deal, Review> reviews;
    public static volatile ListAttribute<Deal, Customer> customers;
    public static volatile SingularAttribute<Deal, Category> category;

}