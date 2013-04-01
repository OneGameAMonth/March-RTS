/*
 * File:   LabelListener.java
 * Author: Robin Arys <contact@zedutchgandalf.be>
 * 
 * Created on 30/03/2013, 12:10:41
 *
 * Copyright (c)2013 Robin Arys <contact@zedutchgandalf.be>
 */

package be.zedutchgandalf.RTS.util;

/**
 * A LabelListener listens for changes in Labels and should act accordingly.
 * @see Label
 * @author Robin Arys <contact@zedutchgandalf.be>
 */
public interface LabelListener {
    /**
     * Fired if a mouse button has been pressed since the last call.
     * @param mouseButton The button that has been pressed.
     */
    public void onClick(int mouseButton);
    public void onMouseOver();
    public void onMouseLeft();
}
