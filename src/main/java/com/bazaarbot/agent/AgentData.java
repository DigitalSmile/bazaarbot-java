//
// Translated by CS2J (http://www.cs2j.com): 2019-08-12 9:59:30 PM
//

package com.bazaarbot.agent;

import com.bazaarbot.inventory.InventoryData;

/**
 * The most fundamental agent class, and has as little implementation as possible.
 * In most cases you should start by extending Agent instead of this.
 *
 * @author larsiusprime
 */
public class AgentData {
    private String agentClassName;
    private double money;
    private InventoryData inventory;
    private String logicName;
    private AgentSimulation agentSimulation;
    private Integer lookBack;

    public AgentData(String agentClassName, double money, String logicName) {
        this.agentClassName = agentClassName;
        this.money = money;
        this.logicName = logicName;
    }

    public String getAgentClassName() {
        return agentClassName;
    }

    public void setAgentClassName(String agentClassName) {
        this.agentClassName = agentClassName;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public InventoryData getInventory() {
        return inventory;
    }

    public void setInventory(InventoryData inventory) {
        this.inventory = inventory;
    }

    public String getLogicName() {
        return logicName;
    }

    public void setLogicName(String logicName) {
        this.logicName = logicName;
    }

    public AgentSimulation getAgentSimulation() {
        return agentSimulation;
    }

    public void setAgentSimulation(AgentSimulation agentSimulation) {
        this.agentSimulation = agentSimulation;
    }

    public Integer getLookBack() {
        return lookBack;
    }

    public void setLookBack(Integer lookBack) {
        this.lookBack = lookBack;
    }
}


