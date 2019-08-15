//
// Translated by CS2J (http://www.cs2j.com): 2019-08-12 9:59:30 PM
//

package com.bazaarbot.agent;

import com.bazaarbot.ICommodity;
import com.bazaarbot.PriceBelief;
import com.bazaarbot.PriceRange;
import com.bazaarbot.inventory.Inventory;
import com.bazaarbot.market.Market;
import com.bazaarbot.market.Offer;

import java.util.HashMap;
import java.util.UUID;

public abstract class BasicAgent {
    private final int id = UUID.randomUUID().hashCode();
    //unique integer identifier
    private final String agentName;
    private final AgentSimulation agentSimulation;
    private final Inventory inventory = new Inventory();
    private final HashMap<ICommodity, PriceBelief> goodsPriceBelief = new HashMap<>();

    private double moneyAvailable;
    private double moneyLastRound;
    private double moneySpent;

    public BasicAgent(AgentData data) {
        this.agentName = data.getAgentClassName();
        this.moneyAvailable = data.getMoney();
        this.inventory.fromData(data.getInventory());
        this.agentSimulation = data.getAgentSimulation();
    }

    public void simulate(Market market) {
        this.moneyLastRound = this.moneyAvailable;
        agentSimulation.perform(this, market);
    }

    public abstract void generateOffers(Market bazaar, ICommodity good);

    public abstract void updatePriceModel(Market bazaar, String act, ICommodity good, boolean success, double unitPrice);

    public abstract Offer createBid(Market bazaar, ICommodity good, double limit);

    public abstract Offer createAsk(Market bazaar, ICommodity commodity_, double limit_);

    public final double queryInventory(ICommodity good) {
        return inventory.query(good);
    }

    public final void addInventoryItem(ICommodity good, double amount) {
        inventory.add(good, amount, (moneySpent >= 1 ? moneySpent : 1) / amount);
    }

    public final void consumeInventoryItem(ICommodity good, double amount) {
        if (good.getName().compareTo("money") == 0) {
            this.moneyAvailable += amount;
            if (amount < 0)
                moneySpent += (-amount);
        } else {
            double price = inventory.change(good, amount, 0);
            if (amount < 0)
                moneySpent += (-amount) * price;
        }
    }

    public final void changeInventory(ICommodity good, double amount, double unitCost) {
        if (good.getName().compareTo("money") == 0) {
            this.moneyAvailable += amount;
        } else {
            inventory.change(good, amount, unitCost);
        }
    }

    protected double determineSaleQuantity(int observeWindow, double averageHistoricalPrice, ICommodity commodity) {
        if (averageHistoricalPrice <= 0) {
            return 0;
        }
        PriceRange tradingRange = observeTradingRange(commodity, observeWindow);
        double favorability = tradingRange.positionInRange(averageHistoricalPrice);
        //position_in_range: high means price is at a high point
        //TODO: What is going on here?
        double amountToSell = Math.round(favorability * inventory.surplus(commodity));
        //double amount_to_sell = inventory.query(commodity);
        if (amountToSell < 1) {
            amountToSell = 1;
        }

        return amountToSell;
    }


    protected double determinePurchaseQuantity(int observeWindow, double averageHistoricalPrice, ICommodity commodity) {
        if (averageHistoricalPrice <= 0) {
            return 0;
        }
        PriceRange tradingRange = observeTradingRange(commodity, observeWindow);
        double favorability = tradingRange.positionInRange(averageHistoricalPrice);
        //double
        favorability = 1 - favorability;
        //do 1 - favorability to see how close we are to the low end
        double amountToBuy = Math.round(favorability * inventory.shortage(commodity));
        //double
        if (amountToBuy < 1) {
            amountToBuy = 1;
        }

        return amountToBuy;
    }

    private PriceRange observeTradingRange(ICommodity good, int window) {
        if (!goodsPriceBelief.containsKey(good))
            goodsPriceBelief.put(good, new PriceBelief());

        return goodsPriceBelief.get(good).observe(window);
    }

    public final void updatePriceModel(Market market, String buy, ICommodity good, boolean b) {
        updatePriceModel(market, buy, good, b, 0);
    }

    public AgentSnapshot getSnapshot() {
        return new AgentSnapshot(getAgentName(), getMoneyAvailable(), new Inventory(inventory));
    }

    public boolean isInventoryFull() {
        return inventory.getEmptySpace() == 0;
    }

    public double getProfitFromLastRound() {
        return moneyAvailable - moneyLastRound;
    }

    public String getAgentName() {
        return agentName;
    }

    public double getMoneyAvailable() {
        return moneyAvailable;
    }

    public void setMoneyAvailable(double value) {
        moneyAvailable = value;
    }

    public int getId() {
        return id;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public HashMap<ICommodity, PriceBelief> getGoodsPriceBelief() {
        return goodsPriceBelief;
    }
}


