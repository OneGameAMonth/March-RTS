/*
 * File:   Menu.java
 * Author: Robin Arys <contact@zedutchgandalf.be>
 * 
 * Created on 30/03/2013, 11:50:05
 *
 * Copyright (c)2013 Robin Arys <contact@zedutchgandalf.be>
 */

package be.zedutchgandalf.RTS;

import be.zedutchgandalf.RTS.util.Label;
import be.zedutchgandalf.RTS.util.LabelListener;
import be.zedutchgandalf.RTS.util.Updatable;
import be.zedutchgandalf.RTS.util.Updater;
import java.util.HashSet;
import java.util.Set;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;


public class Menu extends BasicGame implements Updater {
    private Label title, fullScreen, windowed, quit, play, tutorial, continueGame, about;
    private Set <Updatable> needsUpdating;
    private boolean requestFullscreen, requestClose, startNewGame, startGame, startTutorial;
    private int playY;
    
    public Menu(String title){
	super(title);
    }
    
    public void reset(){
	startNewGame = false;
	startGame = false;
	startTutorial = false;
	if(Game.gameInProgress)
	    play.setY(playY - 25);
	else
	    play.setY(playY);
    }

    @Override
    public void init(GameContainer container) throws SlickException {
	title = new Label("March RTS v.0.6", -1, 10, null, container);
	about = new Label("Made by Zedutchgandalf for OneGameAMonth.com.\n(c) Zedutchgandalf 2013.", 10, container.getHeight()-40, null);
	playY = container.getHeight()/2;
	play = new Label("New game", -1, playY, this, container);
	continueGame = new Label("Continue", -1, container.getHeight()/2, this, container);
	tutorial = new Label("Tutorial (recommended)", -1, container.getHeight()/2+25, this, container);
	fullScreen = new Label("Fullscreen mode", -1, container.getHeight()/2+50, this, container);
	windowed = new Label("Windowed mode", -1, container.getHeight()/2+50, this, container);
	quit = new Label("Exit game", -1, container.getHeight()/2 + 75, this, container);
	
	fullScreen.addListener(new LabelListener() {
	    @Override
	    public void onClick(int button) {
		requestFullscreen = true;
	    }

	    @Override
	    public void onMouseOver() {
		fullScreen.setColor(Color.yellow);
	    }

	    @Override
	    public void onMouseLeft() {
		fullScreen.setColor(Color.white);
	    }
	});
	windowed.addListener(new LabelListener() {
	    @Override
	    public void onClick(int button) {
		requestFullscreen = false;
	    }

	    @Override
	    public void onMouseOver() {
		windowed.setColor(Color.yellow);
	    }

	    @Override
	    public void onMouseLeft() {
		windowed.setColor(Color.white);
	    }
	});
	play.addListener(new LabelListener() {

	    @Override
	    public void onClick(int button) {
		startNewGame = true;
	    }

	    @Override
	    public void onMouseOver() {
		play.setColor(Color.yellow);
	    }

	    @Override
	    public void onMouseLeft() {
		play.setColor(Color.white);
	    }
	});
	continueGame.addListener(new LabelListener() {
	    @Override
	    public void onClick(int button) {
		startGame = true;
	    }

	    @Override
	    public void onMouseOver() {
		continueGame.setColor(Color.yellow);
	    }

	    @Override
	    public void onMouseLeft() {
		continueGame.setColor(Color.white);
	    }
	});
	quit.addListener(new LabelListener() {

	    @Override
	    public void onClick(int button) {
		System.out.println("Shutting down...");
		requestClose = true;
	    }

	    @Override
	    public void onMouseOver() {
		quit.setColor(Color.yellow);
	    }

	    @Override
	    public void onMouseLeft() {
		quit.setColor(Color.white);
	    }
	});
	tutorial.addListener(new LabelListener() {
	    @Override
	    public void onClick(int button) {
		startTutorial = true;
	    }

	    @Override
	    public void onMouseOver() {
		tutorial.setColor(Color.yellow);
	    }

	    @Override
	    public void onMouseLeft() {
		tutorial.setColor(Color.white);
	    }
	});
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
	update(container, delta, null);
    }
    public void update(GameContainer container, int delta, Game game) throws SlickException {
	if(requestClose){
	    container.exit();
	} else if(startGame){
	   game.changeMenuState(false);
	} else if(startNewGame){
	    game.reset();
	    game.changeMenuState(false);
	} else if(startTutorial){
	    game.playTutorial();
	}
	if(requestFullscreen && !container.isFullscreen()){
	    container.setFullscreen(true);
	} else if(!requestFullscreen && container.isFullscreen()){
	    container.setFullscreen(false);
	}
	
	for(Updatable u : needsUpdating){
	    if(!Game.gameInProgress && u.equals(continueGame))
		continue;
	    if(!u.equals(fullScreen) && !u.equals(windowed))
		u.update(container, delta);
	}
	
	if(container.isFullscreen())
	    windowed.update(container, delta);
	else
	    fullScreen.update(container, delta);
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
	title.draw(g);
	about.draw(g);
	play.draw(g);
	tutorial.draw(g);
	if(Game.gameInProgress)
	    continueGame.draw(g);
	if(container.isFullscreen()){
	    windowed.draw(g);
	} else {
	    fullScreen.draw(g);
	}
	quit.draw(g);
    }

    @Override
    public void requestUpdating(Updatable requester) {
	if(needsUpdating == null)
	    needsUpdating = new HashSet<Updatable>();
	needsUpdating.add(requester);
    }
}
