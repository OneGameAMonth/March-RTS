/*
 * File:   MarchRTS.java
 * Author: Robin Arys <contact@zedutchgandalf.be>
 * 
 * Created on 10/03/2013, 15:35:36
 *
 * Copyright (c)2013 Robin Arys <contact@zedutchgandalf.be>
 */

package be.zedutchgandalf.RTS;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Robin Arys <contact@zedutchgandalf.be>
 */
public class MarchRTS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SlickException {
	AppGameContainer app = new AppGameContainer(new Game("March - RTS v.0.6"));
	
	app.setDisplayMode(800, 600, false); // 800x600, no fullscreen.
	app.setMaximumLogicUpdateInterval(50);
	app.setMinimumLogicUpdateInterval(50);
	app.start();
    }
}
