/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Business;
import entity.Category;
import entity.Deal;
import entity.SaleTransaction;
import entity.Tag;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.BusinessNotFoundException;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewDealException;
import util.exception.DealNotFoundException;
import util.exception.DealQtyInsufficientException;
import util.exception.DealSerialNumberExistException;
import util.exception.DeleteDealException;
import util.exception.InputDataValidationException;
import util.exception.TagNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateDealException;

/**
 *
 * @author yeerouhew
 */
@Stateless
public class DealSessionBean implements DealSessionBeanLocal {

    @EJB
    private BusinessSessionBeanLocal businessSessionBeanLocal;

    @EJB
    private SaleTransactionSessionBeanLocal saleTransactionSessionBeanLocal;

    @EJB
    private TagSessionBeanLocal tagSessionBeanLocal;

    @EJB
    private CategorySessionBeanLocal categorySessionBeanLocal;

    @PersistenceContext(unitName = "DealFinder-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public DealSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Deal createNewDeal(Deal newDeal, Long categoryId, List<Long> tagIds, Long businessId) throws CreateNewDealException, CategoryNotFoundException, DealSerialNumberExistException, UnknownPersistenceException, InputDataValidationException, BusinessNotFoundException {
        Set<ConstraintViolation<Deal>> constraintViolations = validator.validate(newDeal);

        if (constraintViolations.isEmpty()) {
            try {
                if (categoryId == null) {
                    throw new CreateNewDealException("The new product must be associated to a leaf category");
                }

                Category category = categorySessionBeanLocal.retrieveCategoryByCategoryId(categoryId);

                if (!category.getSubCategories().isEmpty()) {
                    throw new CreateNewDealException("Selected category for the new deal is not a leaf category");
                }

                Business business = businessSessionBeanLocal.getBusinessByBusinessId(businessId);

                em.persist(newDeal);
                newDeal.setBusiness(business);
                newDeal.setCategory(category);

                if (tagIds != null && (!tagIds.isEmpty())) {
                    for (Long tagId : tagIds) {
                        Tag tag = tagSessionBeanLocal.retrieveTagByTagId(tagId);
                        newDeal.addTag(tag);
                    }
                }

                em.flush();
                return newDeal;

            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new DealSerialNumberExistException();
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } catch (CategoryNotFoundException | TagNotFoundException ex) {
                throw new CreateNewDealException("An error has occurred while creating new deal: " + ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public List<Deal> retrieveAllDeals() {
        Query query = em.createQuery("SELECT d FROM Deal d ORDER BY d.serialNum ASC");
        List<Deal> deals = query.getResultList();

        for (Deal deal : deals) {
            deal.getCategory();
            deal.getTags().size();
            deal.getBusiness();
            deal.getReviews().size();
            deal.getCustomers().size();
            deal.getFavCustomers().size();
            deal.getSaleTransactions().size();
        }

        return deals;
    }

    @Override
    public List<Deal> searchDealByName(String searchString) {
        Query query = em.createQuery("SELECT d FROM Deal d WHERE d.dealName LIKE :inSearchString ORDER BY d.serialNum ASC");
        query.setParameter("inSearchString", searchString);

        List<Deal> deals = query.getResultList();

        for (Deal deal : deals) {
            deal.getCategory();
            deal.getTags().size();
            deal.getBusiness();
            deal.getReviews().size();
            deal.getCustomers().size();
            deal.getFavCustomers().size();
            deal.getSaleTransactions().size();
        }

        return deals;
    }

    @Override
    public List<Deal> filterDealByCategory(Long categoryId) throws CategoryNotFoundException {
        List<Deal> deals = new ArrayList<>();
        Category category = categorySessionBeanLocal.retrieveCategoryByCategoryId(categoryId);

        if (category.getSubCategories().isEmpty()) {
            deals = category.getDeals();
        } else {
            for (Category subCategory : category.getSubCategories()) {
                deals.addAll(addSubCategoryProducts(subCategory));
            }
        }

        for (Deal deal : deals) {
            deal.getCategory();
            deal.getTags().size();
        }

        Collections.sort(deals, new Comparator<Deal>() {
            public int compare(Deal d1, Deal d2) {
                return d1.getSerialNum().compareTo(d2.getSerialNum());
            }
        });

        return deals;
    }

    @Override
    public List<Deal> filterDealByTags(List<Long> tagIds, String condition) {
        List<Deal> deals = new ArrayList<>();

        if (tagIds == null || tagIds.isEmpty() || (!condition.equals("AND") && !condition.equals("OR"))) {
            return deals;
        } else {
            if (condition.equals("OR")) {
                Query query = em.createQuery("SELECT DISTINCT d FROM Deal d, IN (d.tags) t WHERE t.tagId IN :inTagIds ORDER BY d.serialNum ASC");
                query.setParameter("inTagIds", tagIds);
                deals = query.getResultList();
            } else {
                String selectClause = "SELECT d FROM Deal d";
                String whereClause = "";
                Boolean firstTag = true;
                Integer tagCount = 1;

                for (Long tagId : tagIds) {
                    selectClause += ", IN (d.tags) t" + tagCount;

                    if (firstTag) {
                        whereClause = "WHERE t1.tagId = " + tagId;
                        firstTag = false;
                    } else {
                        whereClause += " AND t" + tagCount + ".tagId = " + tagId;
                    }

                    tagCount++;
                }

                String jpql = selectClause + " " + whereClause + " ORDER BY d.serialNum ASC";
                Query query = em.createQuery(jpql);
                deals = query.getResultList();
            }

            for (Deal deal : deals) {
                deal.getCategory();
                deal.getTags().size();
            }

            Collections.sort(deals, new Comparator<Deal>() {
                public int compare(Deal d1, Deal d2) {
                    return d1.getSerialNum().compareTo(d2.getSerialNum());
                }
            });

            return deals;
        }
    }

    @Override
    public Deal retrieveDealByDealId(Long dealId) throws DealNotFoundException {
        Deal deal = em.find(Deal.class, dealId);

        if (deal != null) {
            deal.getCategory();
            deal.getTags().size();
            deal.getBusiness();
            deal.getReviews().size();
            deal.getCustomers().size();
            deal.getFavCustomers().size();
            deal.getSaleTransactions().size();

            return deal;
        } else {
            throw new DealNotFoundException("Deal Id " + dealId + " does not exist!");
        }
    }

    @Override
    public Deal retrieveDealByDealSerialNum(String serialNum) throws DealNotFoundException {
        Query query = em.createQuery("SELECT d FROM Deal d WHERE d.serialNum = :inSerialNum");
        query.setParameter("inSerialNum", serialNum);

        try {
            Deal deal = (Deal) query.getSingleResult();
            deal.getCategory();
            deal.getTags().size();
            deal.getBusiness();
            deal.getReviews().size();
            deal.getCustomers().size();
            deal.getFavCustomers().size();
            deal.getSaleTransactions().size();

            return deal;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new DealNotFoundException("Serial Number " + serialNum + " does not exist!");
        }
    }

    @Override
    public List<Deal> retrieveDealByBusinessId(Long businessId) {
        Query query = em.createQuery("SELECT d FROM Deal d WHERE d.business.id = :inBusinessId");
        query.setParameter("inBusinessId", businessId);

        List<Deal> deals = query.getResultList();

        for (Deal deal : deals) {
            deal.getCategory();
            deal.getTags().size();
            deal.getBusiness();
            deal.getReviews().size();
            deal.getCustomers().size();
            deal.getFavCustomers().size();
            deal.getSaleTransactions().size();
        }

        return deals;
    }

    @Override
    public void updateDeal(Deal deal, Long categoryId, List<Long> tagIds) throws DealNotFoundException, InputDataValidationException, UpdateDealException, TagNotFoundException, CategoryNotFoundException {
        if (deal != null && deal.getDealId() != null) {
            Set<ConstraintViolation<Deal>> constraintViolations = validator.validate(deal);

            if (constraintViolations.isEmpty()) {
                Deal dealToUpdate = retrieveDealByDealId(deal.getDealId());

                if (dealToUpdate.getSerialNum().equals(deal.getSerialNum())) {

                    if (categoryId != null && (!dealToUpdate.getCategory().getCategoryId().equals(categoryId))) {
                        Category categoryToUpdate = categorySessionBeanLocal.retrieveCategoryByCategoryId(categoryId);

                        if (!categoryToUpdate.getSubCategories().isEmpty()) {
                            throw new UpdateDealException("Selected category for the new deal is not a leaf category");
                        }

                        dealToUpdate.setCategory(categoryToUpdate);
                    }

                    if (tagIds != null) {
                        for (Tag tag : dealToUpdate.getTags()) {
                            tag.getDeals().remove(dealToUpdate);
                        }

                        dealToUpdate.getTags().clear();

                        for (Long tagId : tagIds) {
                            Tag tag = tagSessionBeanLocal.retrieveTagByTagId(tagId);
                            dealToUpdate.addTag(tag);
                        }
                    }

                    dealToUpdate.setDealName(deal.getDealName());
                    dealToUpdate.setDescription(deal.getDescription());
                    dealToUpdate.setStartDateTime(deal.getStartDateTime());
                    dealToUpdate.setEndDateTime(deal.getEndDateTime());
                    dealToUpdate.setQuantityLeft(deal.getQuantityLeft());
                    dealToUpdate.setUnitPrice(deal.getUnitPrice());

                } else {
                    throw new UpdateDealException("Serial Number of deal record to be updated does not match the existing record");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new DealNotFoundException("Deal ID not provided for product to be updated");
        }
    }

    @Override
    public void deleteDeal(Long dealId) throws DealNotFoundException {
        Deal dealToRemove = retrieveDealByDealId(dealId);

        List<SaleTransaction> saleTransactions = saleTransactionSessionBeanLocal.retrieveSaleTransactionDealByDealId(dealId);

        if(saleTransactions.isEmpty()){
            dealToRemove.getBusiness().getDeals().remove(dealToRemove);
            dealToRemove.getReviews().clear();
            dealToRemove.getCustomers().clear();
            dealToRemove.getFavCustomers().clear();

            em.remove(dealToRemove);
        } else {
            dealToRemove.setEnabled(false);
            updateDealStatus(dealToRemove);
        }
        
    }

    @Override
    public void debitQtyOnHand(Long dealId, Integer qtyToDebit) throws DealQtyInsufficientException, DealNotFoundException {
        Deal deal = retrieveDealByDealId(dealId);

        if (deal.getQuantityLeft() >= qtyToDebit) {
            deal.setQuantityLeft(deal.getQuantityLeft() - qtyToDebit);
        } else {
            throw new DealQtyInsufficientException("Deal " + deal.getSerialNum() + " quantity left is " + deal.getQuantityLeft() + " :not sufficient");
        }
    }

    @Override
    public void creditQtyOnHand(Long dealId, Integer qtyToCredit) throws DealNotFoundException {
        Deal deal = retrieveDealByDealId(dealId);
        deal.setQuantityLeft(deal.getQuantityLeft() + qtyToCredit);
    }

    private List<Deal> addSubCategoryProducts(Category category) {
        List<Deal> deals = new ArrayList<>();

        if (category.getSubCategories().isEmpty()) {
            return category.getDeals();
        } else {
            for (Category subCategory : category.getSubCategories()) {
                deals.addAll(addSubCategoryProducts(subCategory));
            }

            return deals;
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Deal>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

    public Deal updateDealStatus(Deal deal) throws DealNotFoundException {
        try {
            Deal dealUpdate = retrieveDealByDealId(deal.getDealId());
            dealUpdate.setEnabled(deal.isEnabled());
            return dealUpdate;
        } catch (DealNotFoundException ex) {
            throw new DealNotFoundException(ex.getMessage());
        }
    }
}
