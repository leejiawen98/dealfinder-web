/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Favourite;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewFavouritesException;
import util.exception.FavouriteNotFoundException;

/**
 *
 * @author yeerouhew
 */
@Local
public interface FavouriteSessionBeanLocal {

    public Favourite createNewFavourite(Long customerId, Long dealId, Favourite newFav) throws CreateNewFavouritesException;

    public List<Favourite> retrieveAllFavourites();

    public Favourite retrieveFavouriteByFavId(Long favId) throws FavouriteNotFoundException;

    public List<Favourite> retrieveFavByDealId(Long dealId);

    public List<Favourite> retrieveFavByCustomerId(Long customerId);

    public void deleteFavourite(Long favId) throws FavouriteNotFoundException;
    
}
