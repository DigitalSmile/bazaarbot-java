//
// Translated by CS2J (http://www.cs2j.com): 2019-08-12 9:59:30 PM
//

package com.bazaarbot.agent;

import com.bazaarbot.ICommodity;
import com.bazaarbot.market.Market;
import com.bazaarbot.market.Offer;

/**
 * An agent that performs the basic logic from the Doran & Parberry article
 *
 * @author
 */
public class Agent extends BasicAgent {
    //lowest possible price
    public Agent(AgentData data) {
        super(data);
    }

    private static final int OBSERVE_WINDOW = 10;
    private static final int LOOKBACK_RANGE = 15;

    @Override
    public Offer createBid(Market market, ICommodity good, double limit) {
        int bidPrice = 0;
        // determinePriceOf(good);  bids are now made "at market", no price determination needed
        double ideal = determinePurchaseQuantity(OBSERVE_WINDOW, market.getAverageHistoricalPrice(good, LOOKBACK_RANGE), good);
        //can't buy more than limit
        double quantityToBuy = ideal > limit ? limit : ideal;
        if (quantityToBuy > 0) {
            return new Offer(getId(), good, quantityToBuy, bidPrice);
        }

        return null;
    }

    //TODO: Determine the right naming for this number
    private static final double SOME_MAGIC_NUMBER = 1.02;

    @Override
    public Offer createAsk(Market bazaar, ICommodity commodity, double limit_) {
        double askPrice = getInventory().query_cost(commodity) * SOME_MAGIC_NUMBER;
        //asks are fair prices:  costs + small profit
        double quantityToSell = getInventory().query(commodity);
        //put asks out for all inventory
        if (quantityToSell > 0) {
            return new Offer(getId(), commodity, quantityToSell, askPrice);
        }

        return null;
    }

    @Override
    public void generateOffers(Market bazaar, ICommodity commodity) {
        Offer offer;
        double surplus = getInventory().surplus(commodity);
        if (surplus >= 1) {
            offer = createAsk(bazaar, commodity, 1);
            if (offer != null) {
                bazaar.ask(offer);
            }
        } else {
            Double shortage = getInventory().shortage(commodity);
            Double space = getInventory().getEmptySpace();
            if (shortage > 0 && space > 0) {
                double limit;
                if (shortage <= space) {
                    //enough space for ideal order
                    limit = shortage;
                } else {
                    //not enough space for ideal order
                    limit = space;
                }
                // Math.Floor(space / shortage);
                if (limit > 0) {
                    offer = createBid(bazaar, commodity, limit);
                    if (offer != null) {
                        bazaar.bid(offer);
                    }

                }

            }

        }
    }

}


