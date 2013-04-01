/*
 * File:   updatable.java
 * Author: Robin Arys <contact@zedutchgandalf.be>
 * 
 * Created on 30/03/2013, 12:15:41
 *
 * Copyright (c)2013 Robin Arys <contact@zedutchgandalf.be>
 */

package be.zedutchgandalf.RTS.util;

import org.newdawn.slick.GameContainer;

/**
 *
 * @author Robin Arys <contact@zedutchgandalf.be>
 */
public interface Updatable {
    abstract void update(GameContainer container, int delta);
}