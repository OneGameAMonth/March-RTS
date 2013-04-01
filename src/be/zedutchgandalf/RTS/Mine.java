/*
 * File:   Mine.java
 * Author: Robin Arys <contact@zedutchgandalf.be>
 * 
 * Created on 10/03/2013, 17:06:48
 *
 * Copyright (c)2013 Robin Arys <contact@zedutchgandalf.be>
 */

package be.zedutchgandalf.RTS;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class Mine extends Tower {
    private static HashMap<Player, Image> sprites;
    private static Image shadow;

    public Mine(int x_loc, int y_loc, GameContainer gc, Game g) {
	super(x_loc, y_loc, gc, g, 15, 15);
    }
    
    public static void loadSprites(){
	try {
	    sprites = new HashMap<Player, Image>();
	    sprites.put(null, new Image("mine_empty.png"));
	    sprites.put(game.opponent, new Image("mine_enemy.png"));
	    sprites.put(game.player, new Image("mine_player.png"));
	    shadow = new Image("shadow_mine.png");
	} catch (SlickException ex) {
	    System.err.println("Failed to load mines!");
	    Logger.getLogger(Mine.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
    
    @Override
    protected void drawImage() {
	shadow.draw(x,y);
	try {
	    sprites.get(getOwner()).draw(x, y);
	} catch(Exception e){
	    System.out.println("Trying to reload mine sprites.");
	    loadSprites();
	}
    }

    @Override
    protected void drawOval(Graphics g) {
	g.drawOval(x-WIDTH/2, y+HEIGHT, WIDTH*5/2, HEIGHT/4*3);
    }
    
    @Override
    boolean canAttack() {
	return false;
    }

    @Override
    boolean canConquer() {
	return false;
    }

    @Override
    protected void updateEffect(int delta) {
	if(getOwner() != null)
	    getOwner().addResources(0.4F*this.getNumMinions()/8);
    }
}
