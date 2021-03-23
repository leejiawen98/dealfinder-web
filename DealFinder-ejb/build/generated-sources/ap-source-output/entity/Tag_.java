package entity;

import entity.Deal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-03-23T16:46:52")
@StaticMetamodel(Tag.class)
public class Tag_ { 

    public static volatile SingularAttribute<Tag, Long> tagId;
    public static volatile ListAttribute<Tag, Deal> deals;
    public static volatile SingularAttribute<Tag, String> name;

}