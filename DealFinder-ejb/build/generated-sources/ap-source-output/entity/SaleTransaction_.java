package entity;

import entity.Deal;
import entity.User;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-03-25T22:06:11")
@StaticMetamodel(SaleTransaction.class)
public class SaleTransaction_ { 

    public static volatile SingularAttribute<SaleTransaction, BigDecimal> totalAmount;
    public static volatile SingularAttribute<SaleTransaction, Deal> deal;
    public static volatile SingularAttribute<SaleTransaction, Integer> quantity;
    public static volatile SingularAttribute<SaleTransaction, Date> transactionDateTime;
    public static volatile SingularAttribute<SaleTransaction, BigDecimal> price;
    public static volatile SingularAttribute<SaleTransaction, Long> saleTransactionId;
    public static volatile SingularAttribute<SaleTransaction, User> user;

}