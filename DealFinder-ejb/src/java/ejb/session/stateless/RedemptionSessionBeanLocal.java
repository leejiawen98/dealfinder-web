/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Redemption;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewRedemptionException;
import util.exception.RedemptionNotFoundException;

/**
 *
 * @author yeerouhew
 */
@Local
public interface RedemptionSessionBeanLocal {

    public Redemption createNewRedemption(Long customerId, Redemption newRedemption, Long dealId) throws CreateNewRedemptionException;

    public Redemption retrieveRedemptionByRedemptionId(Long redemptionId) throws RedemptionNotFoundException;

    public List<Redemption> retrieveAllRedemptions();

    public List<Redemption> retrieveRedemptionByDealId(Long dealId);

    public List<Redemption> retrieveRedemptionByCustomerId(Long customerId);

    public Integer countTheNumberOfRedemptionByDealId(Long dealId);

    public Redemption updateRedemption(Redemption newR) throws RedemptionNotFoundException;

    public List<Redemption> retrieveRedemptionsByCustIdandBizId(Long custId, Long bizId);
    
}
