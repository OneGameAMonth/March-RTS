/*
 * File:   Tower.java
 * Author: Robin Arys <contact@zedutchgandalf.be>
 * 
 * Created on 10/03/2013, 15:43:08
 * 
 * Copyright (c)2013 Robin Arys <contact@zedutchgandalf.be>
 */

package be.zedutchgandalf.RTS;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;


class Tower {
    protected int x, y;
    private Player owner;
    private float numMinions;
    private boolean prepareAttack, prepareDefense, selected;
    protected int WIDTH = 15, HEIGHT = 40;
    protected static Game game;
    private Rectangle boundingBox;
    
    private static HashMap<Player, Image> sprites;
    private static HashMap<Player, Image> backPlates;
    private static Tower lastSelected;
    private static Image shadow;
    
    public Tower(int x_loc, int y_loc, GameContainer gc, Game g, int w, int h){
	this.WIDTH = w;
	this.HEIGHT = h;
	if(x_loc - this.WIDTH - 10 >= 0 && x_loc < gc.getWidth()-this.WIDTH-10){
	    x = x_loc;
	} else if(x_loc <= 0){
	    x = this.WIDTH+5;
	} else {
	    x = gc.getWidth() - this.WIDTH-15;
	}
	if(y_loc - this.HEIGHT - 20 >= 0 && y_loc < gc.getHeight()-this.HEIGHT-20){
	    y = y_loc;
	} else if(y_loc <= 0){
	    y = this.HEIGHT;
	} else {
	    y = gc.getHeight()- this.HEIGHT*2;
	}
	game = g;
	owner = null;
	numMinions = 20;
    }
    
    public Tower(int x_loc, int y_loc, GameContainer gc, Game g){
	this(x_loc, y_loc, gc, g, 15, 40);
    }
    
    public static void init(Game game){
	Tower.game = game;
	Tower.loadSprites();
	City.loadSprites();
	Mine.loadSprites();
	GoldMine.loadSprites();
    }
    
    public static void loadSprites(){
	try {
	    sprites = new HashMap<Player, Image>();
	    sprites.put(null, new Image("tower_empty.png"));
	    sprites.put(game.player, new Image("tower_player.png"));
	    sprites.put(game.opponent, new Image("tower_enemy.png"));
	    shadow = new Image("shadow.png");
	    backPlates = new HashMap<Player, Image>();
	    backPlates.put(null, new Image("backplate_empty.png"));
	    backPlates.put(game.player, new Image("backplate_player.png"));
	    backPlates.put(game.opponent, new Image("backplate_enemy.png"));
	} catch (SlickException ex) {
	    System.err.println("Failed to load tower sprites!");
	    Logger.getLogger(Tower.class.getName()).log(Level.SEVERE, null, ex);
	}
    }
    
    public Rectangle getBoundingBox(){
	if(boundingBox == null)
	    boundingBox = new Rectangle(x-5, y-5, WIDTH+13, HEIGHT+18);
	return boundingBox;
    }
    
    public void	update(GameContainer gc, int delta, boolean left, boolean right){
	if(left && getBoundingBox().contains(gc.getInput().getMouseX(), gc.getInput().getMouseY())){
	    setSelected(owner == game.player);
	}else if(left && isSelected()){
	    setSelected(false);
	}else if(right && getBoundingBox().contains(gc.getInput().getMouseX(), gc.getInput().getMouseY())){
	    if(owner == game.player && !(this instanceof City))
		setSelected(true);
	} else if(right && isSelected()){
	    setSelected(false);
	}
	updateEffect(delta);
    }
    
    public void render(GameContainer gc, Graphics g){
	/*if(isSelected()){
	    g.setColor(Color.yellow);
	    g.drawRoundRect(x-WIDTH/2, y-HEIGHT/2, 2*WIDTH, 2*HEIGHT, 1);
	}
	if(owner == null){
	    g.setColor(Color.white);
	} else if(owner == game.player){
	    g.setColor(Color.blue);
	} else {
	    g.setColor(Color.red);
	}
	g.draw(getBoundingBox());*/
	if(isPrepareAttack()){
	    //g.drawString("!", x+WIDTH/2-2, y);
	} else if(isPrepareDefense()){
	    g.setColor(Color.yellow);
	    drawOval(g);
	}
	drawImage();
	try {
	    if(this instanceof City)
		backPlates.get(getOwner()).draw(x+1, y+HEIGHT+11);
	    else 
		backPlates.get(getOwner()).draw(x-4, y+HEIGHT+11);
	} catch(Exception e){
	    System.out.println("Trying to reload backplates.");
	    loadSprites();
	}
	g.setColor(Color.white);
	int xD = getNumMinions() < 10 ? 5 : 0;
	if(this instanceof City)
	    xD += 5;
	g.drawString(""+getNumMinions(), x + xD, y+HEIGHT+13);
    }
    
    protected void drawOval(Graphics g){
	g.drawOval(x-WIDTH/2, y+HEIGHT/8*7, WIDTH*2, HEIGHT/2);
    }
    
    protected void drawImage(){
	try{
	    shadow.draw(x, y);
	    sprites.get(owner).draw(x, y);
	} catch(Exception e){
	    System.out.println("Trying to reload city sprites.");
	    loadSprites();
	}
    }

    public boolean isSelected() {
	return selected;
    }

    public void setSelected(boolean selected) {
	if(selected && lastSelected != null){
	    lastSelected.setSelected(false);
	}
	if(selected)
	    lastSelected = this;
	this.selected = selected;
    }

    public boolean isPrepareDefense() {
	return prepareDefense;
    }

    public void setPrepareDefense(boolean prepareDefense) {
	this.prepareDefense = prepareDefense;
    }
    
    public boolean isPrepareAttack() {
	return prepareAttack;
    }

    void setPrepareAttack(boolean prepareAttack) {
	this.prepareAttack = prepareAttack;
    }

    int getX() {
	return x;
    }

    int getY() {
	return y;
    }

    Player getOwner() {
	return owner;
    }

    Tower setOwner(Player owner) {
	this.owner = owner;
	owner.addMinions(this.getNumMinions());
	return this;
    }

    int getNumMinions() {
	return (int) numMinions;
    }

    void setNumMinions(float numMinions) {
	this.numMinions = numMinions;
	if(this.numMinions <= 0){
	    owner = null;
	    this.numMinions = 0;
	}
    }
    
    boolean canAttack(){
	return true;
    }
    
    boolean canConquer(){
	return true;
    }
    
    boolean canSupport() {
	return true;
    }

    void attackedBy(int minions, Tower source) {
	int temp = getNumMinions() - minions;
	if(owner != null)
	    owner.addMinions(-getNumMinions());
	this.setNumMinions(getNumMinions() - minions);
	if(owner != null)
	    owner.addMinions(getNumMinions());
    }
    
    void conqueredBy(int minions, Tower source) {
	int temp = getNumMinions() - minions;
	if(owner != null)
	    owner.addMinions(-getNumMinions());
	this.setNumMinions(getNumMinions() - minions);
	if(numMinions == 0){
	    owner = source.getOwner();
	    setNumMinions(-temp);
	}
	if(owner != null)
	    owner.addMinions(getNumMinions());
    }

    protected void updateEffect(int delta) {}
    protected City setLevel(int level){ return null;}

    Tower addMinions(float deltaMinions) {
	if(owner != null)
	    owner.addMinions(-getNumMinions());
	setNumMinions(numMinions+deltaMinions);
	if(owner != null)
	    owner.addMinions(getNumMinions());
	return this;
    }
}
