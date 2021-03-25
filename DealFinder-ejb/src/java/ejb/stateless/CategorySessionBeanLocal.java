/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.stateless;

import entity.Category;
import java.util.List;
import javax.ejb.Local;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewCategoryException;
import util.exception.DeleteCategoryException;
import util.exception.InputDataValidationException;
import util.exception.UpdateCategoryException;

/**
 *
 * @author Aaron Tan
 */
@Local
public interface CategorySessionBeanLocal {
    public Category createNewCategoryEntity(Category newCategoryEntity, Long parentCategoryId) throws InputDataValidationException, CreateNewCategoryException;
    
    List<Category> retrieveAllCategories();
    
    List<Category> retrieveAllRootCategories();
    
    List<Category> retrieveAllLeafCategories();
    
    List<Category> retrieveAllCategoriesWithoutProduct();
    
    Category retrieveCategoryByCategoryId(Long categoryId) throws CategoryNotFoundException;
    
    void updateCategory(Category categoryEntity, Long parentCategoryId) throws InputDataValidationException, CategoryNotFoundException, UpdateCategoryException;
    
    void deleteCategory(Long categoryId) throws CategoryNotFoundException, DeleteCategoryException;
}

