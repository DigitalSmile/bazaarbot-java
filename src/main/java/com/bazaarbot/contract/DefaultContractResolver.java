package com.bazaarbot.contract;

import com.bazaarbot.ICommodity;
import com.bazaarbot.agent.BasicAgent;

public class DefaultContractResolver implements IContractResolver {
    @Override
    public void newContract(BasicAgent provider, BasicAgent receiver, ICommodity good, double units, double clearingPrice) {
        BasicAgent seller = provider;
        BasicAgent buyer = receiver;
        seller.changeInventory(good, -units, 0);
        buyer.changeInventory(good, units, clearingPrice);
    }
}
