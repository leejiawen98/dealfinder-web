/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Deal;
import entity.Favourite;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CreateNewFavouritesException;
import util.exception.CustomerNotFoundException;
import util.exception.DealNotFoundException;
import util.exception.FavouriteNotFoundException;

/**
 *
 * @author yeerouhew
 */
@Stateless
public class FavouriteSessionBean implements FavouriteSessionBeanLocal {

    @EJB(name = "DealSessionBeanLocal")
    private DealSessionBeanLocal dealSessionBeanLocal;

    @EJB(name = "CustomerSessionBeanLocal")
    private CustomerSessionBeanLocal customerSessionBeanLocal;

    @PersistenceContext(unitName = "DealFinder-ejbPU")
    private EntityManager em;

    @Override
    public Favourite createNewFavourite(Long customerId, Long dealId, Favourite newFav) throws CreateNewFavouritesException {
        if (newFav != null) {
            try {
                Customer customerEntity = customerSessionBeanLocal.getCustomerByCustomerId(customerId);
                newFav.setCustomer(customerEntity);
                customerEntity.getFavourites().add(newFav);

                Deal deal = dealSessionBeanLocal.retrieveDealByDealId(dealId);
                newFav.setDeal(deal);
                deal.getFavourites().add(newFav);

                em.persist(newFav);
                em.flush();

                return newFav;

            } catch (CustomerNotFoundException | DealNotFoundException ex) {
                throw new CreateNewFavouritesException(ex.getMessage());
            }
        } else {
            throw new CreateNewFavouritesException("Review info misisng");
        }
    }
    
    @Override
    public void deleteFavourite(Long favId) throws FavouriteNotFoundException {
        if(favId != null) {
            Favourite favouriteToRemove = retrieveFavouriteByFavId(favId);

            favouriteToRemove.getCustomer().getFavourites().remove(favouriteToRemove);
            favouriteToRemove.getDeal().getFavourites().remove(favouriteToRemove);

            em.remove(favouriteToRemove);
        } else {
            throw new FavouriteNotFoundException("Favourite not found.");
        }
    }

    @Override
    public List<Favourite> retrieveAllFavourites() {
        Query query = em.createQuery("SELECT f FROM Favourite f ORDER BY f.favouriteId ASC");
        List<Favourite> favourites = query.getResultList();

        for (Favourite favourite : favourites) {
            favourite.getCustomer();
            favourite.getDeal();
        }
        return favourites;
    }

    @Override
    public Favourite retrieveFavouriteByFavId(Long favId) throws FavouriteNotFoundException {
        Favourite favourite = em.find(Favourite.class, favId);

        if (favourite != null) {
            favourite.getCustomer();
            favourite.getDeal();

            return favourite;
        } else {
            throw new FavouriteNotFoundException("Favourite Id " + favId + " does not exist");
        }
    }
    
    @Override
    public List<Favourite> retrieveFavByDealId(Long dealId) {
        Query query = em.createQuery("SELECT f FROM Favourite f WHERE f.deal.dealId = :ininDealId");
        query.setParameter("inDealId", dealId);

        List<Favourite> favourites = query.getResultList();

        for (Favourite favourite : favourites) {
            favourite.getCustomer();
            favourite.getDeal();
        }

        return favourites;
    }
    
    
    @Override
    public List<Favourite> retrieveFavByCustomerId(Long customerId) {
        Query query = em.createQuery("SELECT f FROM Favourite f WHERE f.customer.id = :inCustomerId");
        query.setParameter("inCustomerId", customerId);

        List<Favourite> favourites = query.getResultList();

        for (Favourite favourite : favourites) {
            favourite.getCustomer();
            favourite.getDeal();
        }
        return favourites;
    }
    
    

}
