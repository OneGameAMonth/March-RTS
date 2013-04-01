/*
 * File:   BasicUpdatableGame.java
 * Author: Robin Arys <contact@zedutchgandalf.be>
 * 
 * Created on 30/03/2013, 12:16:36
 *
 * Copyright (c)2013 Robin Arys <contact@zedutchgandalf.be>
 */

package be.zedutchgandalf.RTS.util;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;


public abstract class BasicUpdatableGame extends BasicGame {
    public BasicUpdatableGame(String title) {
	super(title);
    }
    
    /**
     * @see org.newdawn.slick.Game#update(org.newdawn.slick.GameContainer, int)
     */
    @Override
    public abstract void update(GameContainer container, int delta) throws SlickException;
    
    public abstract void requestUpdate();
}
