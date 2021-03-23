package entity;

import entity.Deal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-03-23T16:46:52")
@StaticMetamodel(Review.class)
public class Review_ { 

    public static volatile SingularAttribute<Review, Deal> deal;
    public static volatile SingularAttribute<Review, Integer> dealRating;
    public static volatile SingularAttribute<Review, String> description;
    public static volatile SingularAttribute<Review, Long> reviewId;

}