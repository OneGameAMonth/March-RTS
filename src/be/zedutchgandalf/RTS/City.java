/*
 * File:   City.java
 * Author: Robin Arys <contact@zedutchgandalf.be>
 * 
 * Created on 24/03/2013, 14:46:28
 *
 * Copyright (c)2013 Robin Arys <contact@zedutchgandalf.be>
 */

package be.zedutchgandalf.RTS;

import be.zedutchgandalf.RTS.util.Label;
import be.zedutchgandalf.RTS.util.LabelListener;
import be.zedutchgandalf.RTS.util.Updatable;
import be.zedutchgandalf.RTS.util.Updater;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;


public class City extends Tower implements  Updater {
    
    private static final int MAX_LEVEL = 3;
    private static HashMap<Player, Image[]> sprites;
    private static Image[] shadow, shadowTop;
    private Label upgrade;
    private boolean drawUpgradeBB;
    private Set<Updatable> needsUpdating;
    int level = 1;
    
    public City(int x_loc, int y_loc, GameContainer gc, Game g) {
	super(x_loc, y_loc, gc, g);
	this.WIDTH = 32;
	this.HEIGHT = 25;
	needsUpdating = new HashSet<Updatable>();
	try {
	    upgrade = new Label(""+getUpgradeCost(), x+WIDTH, y, this).setGraphic(new Image("upgradeGold.png"));
	} catch (SlickException ex) {
	    System.err.println("Could not load upgradeGold.png!");
	    Logger.getLogger(City.class.getName()).log(Level.SEVERE, null, ex);
	} finally {
	    upgrade.addListener(new LabelListener() {
		@Override
		public void onClick(int button) {
		    if(!tryToUpgrade()){
			System.out.println("Could not upgrade. And I don't know why.");
		    }
		}
		@Override
		public void onMouseOver() {}
		@Override
		public void onMouseLeft() {}
	    });
	}
    }
    
    public boolean tryToUpgrade(){
	if(level == MAX_LEVEL || getOwner() == null)
	    return false;
	if(getOwner().getGold() >= getUpgradeCost()){
	    getOwner().payGold(getUpgradeCost());
	    setLevel(level+1);
	    if(level < MAX_LEVEL)
		upgrade.setText(""+getUpgradeCost());
	    else
		upgrade = null;
	    return true;
	}
	return false;
    }
    
    private int getUpgradeCost(){
	return level*40;
    }

    @Override
    public Rectangle getBoundingBox() {
	Rectangle village = super.getBoundingBox();
	if(isSelected() && upgrade != null){
	    return new Rectangle(village.getX(), village.getY(),
		    village.getWidth() + upgrade.getBoundingBox().getWidth(),
		    village.getHeight() + upgrade.getBoundingBox().getHeight());
	}
	return village;
    }
    
    public static void loadSprites(){
	try {
	    shadow = new Image[]{new Image("city_shadow_1.png"),new Image("city_shadow_2.png"),new Image("city_shadow_3.png")};
	    shadowTop = new Image[]{new Image("city_shadow_2_top.png"),new Image("city_shadow_3_top.png")};
	    sprites = new HashMap<Player, Image[]>();
	    sprites.put(null, new Image[]{new Image("city_empty_1.png"), new Image("city_empty_2.png"), new Image("city_empty_3.png")});
	    sprites.put(game.player, new Image[]{new Image("city_player_1.png"), new Image("city_player_2.png"), new Image("city_player_3.png")});
	    sprites.put(game.opponent, new Image[]{new Image("city_enemy_1.png"), new Image("city_enemy_2.png"), new Image("city_enemy_3.png")});
	} catch (SlickException ex) {
	    System.err.println("Failed to load cities!");
	    Logger.getLogger(Mine.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
	}
    }

    @Override
    public void update(GameContainer gc, int delta, boolean left, boolean right) {
	if(isSelected() && upgrade != null){
	    for(Updatable u : needsUpdating){
		u.update(gc, delta);
	    }
	    if(left && upgrade.getBoundingBox().contains(gc.getInput().getMouseX(), gc.getInput().getMouseY())){
		for(LabelListener l: upgrade.getListeners()){
		    l.onClick(Input.MOUSE_LEFT_BUTTON);
		}
	    }
	}
	super.update(gc, delta, left, right);
    }
    
    @Override
    boolean canAttack() {
	return false;
    }

    @Override
    public void setSelected(boolean selected) {
	super.setSelected(selected);
	
    }

    @Override
    protected void updateEffect(int delta) {
	if(getNumMinions() < level*30)
	    addMinions(level*0.025F);
    }
    
    @Override
    protected City setLevel(int level){
	if(level <= MAX_LEVEL && level > 0){
	    this.level = level;
	} else {
	    System.err.println("Invalid level for a city: "+level);
	}
	return this;
    }
    
    @Override
    public void render(GameContainer gc, Graphics g){
	super.render(gc, g);
	if(isSelected() && upgrade != null){
	    upgrade.draw(g, drawUpgradeBB);
	}
    }
    
    @Override
    protected void drawImage() {
	try{
	    shadow[level-1].draw(x,y);
	    sprites.get(getOwner())[level-1].draw(x, y);
	    if(level >= 2)
		shadowTop[level-2].draw(x,y);
	} catch(Exception e){
	    System.out.println("Trying to reload city sprites.");
	    loadSprites();
	}
    }

    @Override
    protected void drawOval(Graphics g) {
	g.drawOval(x-10, y+HEIGHT*3/4, WIDTH*3/2, HEIGHT/4*3);
    }

    @Override
    public void requestUpdating(Updatable requester) {
	needsUpdating.add(requester);
    }

}
