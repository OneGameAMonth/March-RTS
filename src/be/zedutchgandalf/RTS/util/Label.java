/*
 * File:   Label.java
 * Author: Robin Arys <contact@zedutchgandalf.be>
 * 
 * Created on 30/03/2013, 11:58:31
 *
 * Copyright (c)2013 Robin Arys <contact@zedutchgandalf.be>
 */

package be.zedutchgandalf.RTS.util;

import java.util.HashSet;
import java.util.Set;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;


public class Label implements Updatable {
    private String text;
    private int x, y;
    private int graphWidth, graphHeight;
    private Rectangle boundingBox;
    private Set<LabelListener> listeners;
    private Color color;
    private boolean mouseOver;
    private Image graphic;
    
    /**
     * The most basic constructor.
     * @param text The string to draw.
     * @param x The x coordinate of the label.
     * @param y The y coordinate of the label.
     * @param u The updater for this label. Set to null if you don't need one.
     */
    public Label(String text, int x, int y, Updater u) {
	this.text = text;
	this.x = x;
	this.y = y;
	if(u != null){
	    u.requestUpdating(this);
	}
    }

    /**
     * Constructor.
     * @param text - The string to draw.
     * @param x The x coordinate of the label (-1 = center).
     * @param y The y coordinate of the label (-1 = center).
     * @param u The updater for this label. Set to null if you don't need one.
     * @param c The GameContainer used to center the label to.
     */
    public Label(String text, int x, int y, Updater u, GameContainer c) {
	this(text, x, y, u);
	if(x == -1)
	    centerX(c);
	if(y == -1)
	    centerY(c);
    }
    
    /**
     * Constructor which centers the label to the middle of the GameContainer.
     * Used for labels which don't have to be updated.
     * @param text The string to draw.
     * @param c The GameContainer used to center the label to.
     */
    public Label(String text, GameContainer c){
	this(text, -1, -1, null, c);
    }
    
    /**
     * Set an image to be displayed by the label.
     * @param img The Image to display. Set to null to remove the Image.
     * @return The Label itself, for chaining.
     */
    public Label setGraphic(Image img){
	this.graphic = img;
	if(graphic == null){
	    graphWidth = graphHeight = 0;
	} else {
	    graphWidth = graphic.getWidth();
	    graphHeight = graphic.getHeight();
	}
	return this;
    }
    
    /**
     * Get the current displayed graphic.
     * @return The Image, if it exists. Null otherwise.
     */
    public Image getGraphic(){
	return graphic;
    }
    
    /**
     * Add a new LabelListener to this Label.
     * @param listener A LabelListener for this label.
     * @return The Label itself, for chaining.
     * @see LabelListener
     */
    public Label addListener(LabelListener listener){
	if(listeners == null)
	    listeners = new HashSet<LabelListener>();
	listeners.add(listener);
	return this;
    }
    
    /**
     * Get the bounding box for this label. Mostly used for mouseOver-like
     * events.
     * @return The bounding box for this label.
     */
    public Rectangle getBoundingBox(){
	if(boundingBox == null)
	    boundingBox = new Rectangle(x-5, y-1,
		    text.length()*9+10 + graphWidth, graphHeight > 20 ? graphHeight : 20);
	return boundingBox;
    }
    
    /**
     * Return the color of the text in this label.
     * @return The current text color.
     * @see Color
     */
    public Color getColor() {
	return color;
    }

    /**
     * Set the color of the text in this label.
     * @param color The color in which to display the text.
     * @return The label itself, for chaining.
     */
    public Label setColor(Color color) {
	this.color = color;
	return this;
    }

    /**
     * Get the text currently displayed in this label.
     * @return A String containing all text in this label.
     */
    public String getText() {
	return text;
    }

    public void setText(String text) {
	this.text = text;
    }

    public int getX() {
	return x;
    }

    public void setX(int x) {
	if(x != this.x)
	    boundingBox = null;
	this.x = x;
    }

    public int getY() {
	return y;
    }

    public void setY(int y) {
	if(y != this.y)
	    boundingBox = null;
	this.y = y;
    }
    
    /**
     * Draw the label.
     * @param g The Graphics object needed to draw the label.
     */
    public void draw(Graphics g){
	draw(g, x, y, false);
    }
    
    /**
     * Draw the label at point (x,y).
     * Warning! This does not override the actual location of the label.
     * It just draws it at another location.
     * @param g The Graphics object needed to draw the label.
     * @param x The x-coordinate of the label.
     * @param y The y-coordinate of the label.
     */
    public void draw(Graphics g, int x, int y){
	draw(g, x, y, false);
    }
    
    /**
     * The main draw method.
     * @param g The Graphics object needed to draw the label.
     * @param x The x-coordinate of the label.
     * @param y The y-coordinate of the label.
     * @param renderBoundingBox Set to true if you want to draw the bounding box
     * around this label.
     */
    public void draw(Graphics g, int x, int y, boolean renderBoundingBox){
	Color c = g.getColor();
	if(color != null)
	    g.setColor(color);
	if(graphic != null)
	    graphic.draw(x, y);
	g.drawString(text, x+graphWidth, y);
	if(renderBoundingBox){
	    g.setColor(Color.yellow);
	    g.draw(getBoundingBox());
	}
	g.setColor(c);
    }
    
    /**
     * Draw the label with or without it's bounding box.
     * @param g The Graphics object needed to draw the label.
     * @param renderBoundingBox Set to true if you want to draw this label's bounding box.
     */
    public void draw(Graphics g, boolean renderBoundingBox){
	draw(g, x, y, renderBoundingBox);
    }
    
    public final void centerX(GameContainer c){
	this.x = c.getWidth()/2 - this.text.length()*9/2;
    }
    
    public final void centerY(GameContainer c){
	this.x = c.getHeight()/2 - 16;
    }
    
    public Set<LabelListener> getListeners(){
	return listeners;
    }

    @Override
    public void update(GameContainer container, int delta) {
	Input input = container.getInput();
	if(listeners != null){
	    if(getBoundingBox().contains(input.getMouseX(), input.getMouseY())){
		mouseOver = true;
		for(LabelListener l : listeners)
		    l.onMouseOver();
		if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
		    for(LabelListener l : listeners)
			l.onClick(Input.MOUSE_LEFT_BUTTON);
		}
		if(input.isMousePressed(Input.MOUSE_RIGHT_BUTTON))
		    for(LabelListener l : listeners)
			l.onClick(Input.MOUSE_RIGHT_BUTTON);
		if(input.isMousePressed(Input.MOUSE_MIDDLE_BUTTON))
		    for(LabelListener l : listeners)
			l.onClick(Input.MOUSE_MIDDLE_BUTTON);
	    } else if(mouseOver){
		mouseOver = false;
		for(LabelListener l : listeners){
		    l.onMouseLeft();
		}
	    }
	}
    }
}
