package press.turngeek.mycampaign.services;

import press.turngeek.mycampaign.model.Campaign;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;

@Stateless
public class CampaignServiceBean implements CampaignService {
 
    @Inject
    EntityManager entityManager;


    @Override
    public List<Campaign> getAllCampaigns() {
        TypedQuery<Campaign> query = entityManager.createNamedQuery(Campaign.findAll, Campaign.class);
        List<Campaign> campaigns = query.getResultList();
        //Lambda-Expression: campaigns.forEach(campaign -> campaign.setAmountDonatedSoFar(getAmountDonatedSoFar(campaign)));
        for (Campaign campaign : campaigns) {
            campaign.setAmountDonatedSoFar(getAmountDonatedSoFar(campaign));
        }
        return campaigns;
    }
  
    public void addCampaign(Campaign campaign) {
        entityManager.persist(campaign);
    }

    public void updateCampaign(Campaign campaign) {
        entityManager.merge(campaign);
    }

    public void deleteCampaign(Campaign campaign) {
        Campaign managedCampaign = entityManager.find(Campaign.class, campaign.getId());
        entityManager.remove(managedCampaign);
    }
  
  private Double getAmountDonatedSoFar(Campaign campaign) {
    TypedQuery<Double> query = entityManager.createNamedQuery(Campaign.getAmountDonatedSoFar, Double.class);
    query.setParameter("campaign", campaign);
    Double result = query.getSingleResult();
    if (result == null)
        result = 0d;
    return result;
}


}