package entity;

import entity.Category;
import entity.Deal;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2021-03-26T18:53:51")
@StaticMetamodel(Category.class)
public class Category_ { 

    public static volatile ListAttribute<Category, Deal> deals;
    public static volatile SingularAttribute<Category, String> name;
    public static volatile SingularAttribute<Category, String> description;
    public static volatile SingularAttribute<Category, Category> parentCategory;
    public static volatile SingularAttribute<Category, Long> categoryId;
    public static volatile ListAttribute<Category, Category> subCategories;

}