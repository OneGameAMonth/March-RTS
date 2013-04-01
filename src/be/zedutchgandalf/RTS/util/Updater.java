/*
 * File:   Updater.java
 * Author: Robin Arys <contact@zedutchgandalf.be>
 * 
 * Created on 30/03/2013, 12:22:01
 *
 * Copyright (c)2013 Robin Arys <contact@zedutchgandalf.be>
 */

package be.zedutchgandalf.RTS.util;

/**
 *
 * @author Robin Arys <contact@zedutchgandalf.be>
 */
public interface Updater {
    abstract void requestUpdating(Updatable requester);
}
