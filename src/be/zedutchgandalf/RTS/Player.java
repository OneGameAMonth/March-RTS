/*
 * File:   Player.java
 * Author: Robin Arys <contact@zedutchgandalf.be>
 * 
 * Created on 10/03/2013, 15:41:44
 * 
 * Copyright (c)2013 Robin Arys <contact@zedutchgandalf.be>
 */

package be.zedutchgandalf.RTS;


class Player {
    private int minions;
    private float gold, resources;
    private Game game;
    
    public Player(Game game){
	minions = 0;
	resources = 0;
	gold = 0;
	this.game = game;
    }
    
    public void reset(){
	setResources(500);
	setMinions(0);
	setGold(0);
    }

    public int getMinions() {
	return minions;
    }

    public int getResources() {
	return (int)(resources/10);
    }

    public int getGold() {
	return (int) gold;
    }

    private void setMinions(int minions) {
	this.minions = minions;
    }

    public void setResources(float resources) {
	this.resources = resources;
    }
    
    public void setGold(float gold) {
	this.gold = gold;
    }
    
    public void addResources(float deltaResources){
	this.resources += deltaResources;
    }
    
    public void addGold(float deltaGold) {
	this.gold += deltaGold;
	if(this.gold > 999)
	    this.gold = 999;
    }
    
    public void addMinions(int deltaMinions){
	this.minions += deltaMinions;
    }

    void payResources(int amount) {
	addResources(-amount*10);
    }
    
    void payGold(int amount){
	addResources(-amount);
    }
}
