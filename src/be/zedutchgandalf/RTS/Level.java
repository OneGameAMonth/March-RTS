/*
 * File:   Level.java
 * Author: Robin Arys <contact@zedutchgandalf.be>
 * 
 * Created on 30/03/2013, 13:39:24
 *
 * Copyright (c)2013 Robin Arys <contact@zedutchgandalf.be>
 */

package be.zedutchgandalf.RTS;

import org.newdawn.slick.GameContainer;


public class Level {
    int level;
    Game game;
    
    public Level(int level, Game game){
	this.level = level;
	this.game = game;
    }
    
    public void setup(Tower[] towers, GameContainer container, Player player, Player opponent){
	switch(level){
	    default: System.err.println("Level undefined.");
		break;
	    case 1:
		game.player.reset();
		game.opponent.reset();
		towers[0]=new Tower(230, container.getHeight()/2+70, container, game);
		towers[1]=new Tower(230, container.getHeight()/2-120, container, game);
		towers[2]=new Tower(300, container.getHeight()-110, container, game);
		towers[3]=new Tower(300, 0, container, game);
		towers[4]=new Tower(400, container.getHeight()-140, container, game).addMinions(10).setOwner(player);		//starttoren speler
		towers[5]=new Tower(400, 80, container, game).addMinions(10).setOwner(opponent);				//starttoren opponent
		towers[6]=new Mine(400, container.getHeight()-50, container, game).setOwner(player);		//startmijn speler
		towers[7]=new Mine(400, 0, container, game).setOwner(opponent);					//startmijn opponent
		towers[8]=new Tower(500, container.getHeight()-110, container, game);
		towers[9]=new Tower(500, 0, container, game);
		towers[10]=new Tower(570, container.getHeight()/2+70, container, game);
		towers[11]=new Tower(570, container.getHeight()/2-120, container, game);
		towers[12]=new Mine(300, container.getHeight()/2-20, container, game);
		towers[13]=new GoldMine(500, container.getHeight()/2-20, container, game);
		towers[14]=new City(390, container.getHeight()/4+20, container, game);				    //bovenste midden
		towers[15]=new City(390, container.getHeight()/8*5+20, container, game);			    //onderste midden
		towers[16]=new City(390, container.getHeight()/2-20, container, game).addMinions(20).setLevel(2);//midden
		towers[17]=new GoldMine(100, container.getHeight()/4, container, game);
		towers[18]=new Mine(container.getWidth()-100, container.getHeight()/4, container, game);
		towers[19]=new Mine(100, container.getHeight()/8*5+20, container, game);
		towers[20]=new GoldMine(container.getWidth()-100, container.getHeight()/8*5+20, container, game);
		break;
	}
    }

    public int getLevel() {
	return level;
    }

    public void setLevel(int level) {
	this.level = level;
    }
}
