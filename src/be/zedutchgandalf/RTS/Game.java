/*
 * File:   Game.java
 * Author: Robin Arys <contact@zedutchgandalf.be>
 * 
 * Created on 10/03/2013, 15:38:58
 *
 * Copyright (c)2013 Robin Arys <contact@zedutchgandalf.be>
 */

package be.zedutchgandalf.RTS;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Image;


public class Game extends BasicGame {
    private boolean isMenu = true, isTutorial = false;
    public static boolean gameInProgress;
    private Menu menu;
    private static final int ATTACKCOST = 50;
    Player player, opponent;
    Tower[] towers = new Tower[21];
    Random rand;
    int[] mouseDrag, mouseDragR;
    Tower source, target, sourceR, targetR;
    TowerMode tmode;
    Image background;
    Image resources, gold, minion, resBackground;
    private Color resColor = Color.white;
    private GameState gameState;
    private long resColorTimer;
    private int resColorFlicker = 1200, tutStep;
    private be.zedutchgandalf.RTS.Level level;
    private boolean levelReset;

    public Game(String title) {
	super(title);
	rand = new Random();
	menu = new Menu(title);
    }
    
    public void reset(){
	player = new Player(this);
	opponent = new Player(this);
	tmode = TowerMode.ATTACK;
	level = new be.zedutchgandalf.RTS.Level(1, this);
	levelReset = true;
	gameState = GameState.NORMAL;
    }

    @Override
    public void init(GameContainer container) throws SlickException {
	menu.init(container);
	reset();
	
	try {
	    background = new Image("map.png");
	} catch (SlickException ex) {
	    System.err.println("Could not load background.");
	    Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
	}
	
	Tower.init(this);
	
	try {
	    resources = new Image("resources.png");
	    gold = new Image("gold.png");
	    minion = new Image("minion.png");
	    resBackground = new Image("resBackground.png");
	} catch (SlickException ex) {
	    System.err.println("Could not load resources.");
	    Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
	}
	
	level.setup(towers, container, player, opponent);
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
	if(levelReset){
	    level.setup(towers, container, player, opponent);
	    levelReset = false;
	}
	
	if(isMenu){
	    menu.update(container, delta, this);
	    return;
	} else if(container.getInput().isKeyPressed(Input.KEY_ESCAPE)){
	    changeMenuState(true);
	}
	
	if(isTutorial) {
	    //TODO: interactive tutorial.
	} else if(gameState == GameState.NORMAL) {
	    if(container.getInput().isKeyPressed(Input.KEY_A)){
		tmode = TowerMode.ATTACK;
	    } else if(container.getInput().isKeyPressed(Input.KEY_C)){
		tmode = TowerMode.CONQUER;
	    } else if(container.getInput().isKeyPressed(Input.KEY_S)){
		tmode = TowerMode.SUPPORT;
	    } else if(container.getInput().isKeyPressed(Input.KEY_SPACE)){
		player.addGold(100);
		player.addResources(1000);
	    }
	    boolean left = container.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON);
	    boolean right = container.getInput().isMousePressed(Input.MOUSE_RIGHT_BUTTON);
	    if(container.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON) && mouseDrag == null){
		mouseDrag = new int[4];
		mouseDrag[0] = container.getInput().getMouseX();
		mouseDrag[1] = container.getInput().getMouseY();
	    }
	    if(container.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
		mouseDrag[2] = container.getInput().getMouseX();
		mouseDrag[3] = container.getInput().getMouseY();
	    } else {
		if(mouseDrag != null && source != null && target != null){
		    if(tmode != TowerMode.ATTACK && tmode != TowerMode.CONQUER)
			tmode = TowerMode.ATTACK;
		    attackPhase(source, target);
		}
		mouseDrag = null;
		source = null;
	    }
	    target = null;

	    if(container.getInput().isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON) && mouseDragR == null){
		mouseDragR = new int[4];
		mouseDragR[0] = container.getInput().getMouseX();
		mouseDragR[1] = container.getInput().getMouseY();
	    }
	    if(container.getInput().isMouseButtonDown(Input.MOUSE_RIGHT_BUTTON)){
		mouseDragR[2] = container.getInput().getMouseX();
		mouseDragR[3] = container.getInput().getMouseY();
	    } else {
		if(mouseDragR != null && sourceR != null && targetR != null){
		    tmode = TowerMode.SUPPORT;
		    attackPhase(sourceR, targetR);
		}
		mouseDragR = null;
		sourceR = null;
	    }
	    targetR = null;

	    for(Tower t : towers){
		t.setPrepareAttack(false);
		t.setPrepareDefense(false);
		if(mouseDrag != null){
		    if(t.getBoundingBox().contains(mouseDrag[0], mouseDrag[1]) && t.getOwner() == player){
			source = t;
			t.setPrepareAttack(true);
		    } else if(t.getBoundingBox().contains(mouseDrag[2], mouseDrag[3]) && source != null){
			target = t;
			t.setPrepareDefense(true);
		    }
		} else if(mouseDragR != null){
		    if(t.getBoundingBox().contains(mouseDragR[0], mouseDragR[1]) && t.getOwner() == player){
			tmode = TowerMode.SUPPORT;
			sourceR = t;
			t.setPrepareAttack(true);
		    } else if(t.getBoundingBox().contains(mouseDragR[2], mouseDragR[3]) && sourceR != null){
			targetR = t;
			t.setPrepareDefense(true);
		    }
		}
		t.update(container, delta, left, right);
	    }
	}
	if(!isMenu && !isTutorial && (opponent.getMinions() <= 0 || player.getMinions() <= 0)){
	    Over();
	}
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
	if(isMenu){
	    menu.render(container, g);
	} else if(isTutorial) {
	    //TODO: interactive tutorial-level...
	    g.drawString("Tutorial", container.getWidth()/2 - 45, 10);
	    //This will have to do for now :S
	    new Image("tutorial.png").draw(0, 0);
	} else {
	    background.draw();
	    for (Tower t : towers){
		t.render(container, g);
	    }
	    g.setLineWidth(3F);
	    if(mouseDrag != null && source != null){
		if(tmode != TowerMode.SUPPORT)
		    g.setColor(Color.red);
		else
		    g.setColor(Color.white);
		g.drawLine(mouseDrag[0], mouseDrag[1], mouseDrag[2], mouseDrag[3]);
	    } else if(mouseDragR != null && sourceR != null){
		g.setColor(Color.white);
		g.drawLine(mouseDragR[0], mouseDragR[1], mouseDragR[2], mouseDragR[3]);
	    }

	    drawResPanels(container, g);
	}
	
	if(gameState == GameState.WIN){
	    new Image("win.png").draw(200, 150);
	} else if(gameState == GameState.LOSE){
	    new Image("lose.png").draw(200, 150);
	}
    }
    
    public void drawResPanels(GameContainer container, Graphics g){
	//Flicker resources when trying to attack but not enough resources available.
	long now = System.currentTimeMillis();
	if(resColor.equals(Color.red) &&
		resColorTimer + resColorFlicker/3 <= now &&
		resColorTimer + resColorFlicker/3*2 >= now)
	    resColor = Color.white;
	else if(resColor.equals(Color.white) &&
		resColorTimer + resColorFlicker/3*2 <= now &&
		resColorTimer + resColorFlicker >= now)
	    resColor = Color.red;
	else if((resColor.equals(Color.red) &&
		resColorTimer + resColorFlicker <= now) ||
		player.getResources() >= ATTACKCOST)
	    resColor = Color.white;

	resBackground.draw(3,container.getHeight()-76);
	resources.draw(10,container.getHeight()-26);
	g.setColor(resColor);
	g.drawString(""+player.getResources(), 32, container.getHeight()-28);
	g.setColor(Color.white);
	gold.draw(10,container.getHeight()-48);
	g.drawString(""+player.getGold(), 32, container.getHeight()-50);
	minion.draw(10,container.getHeight()-68);
	g.drawString(""+player.getMinions(), 32, container.getHeight()-70);
	resBackground.draw(container.getWidth()-69,5);
	minion.draw(container.getWidth()-63, 12);
	g.drawString(""+opponent.getMinions(), container.getWidth()-40, 10);
	gold.draw(container.getWidth()-63, 32);
	g.drawString(""+opponent.getGold(), container.getWidth()-40, 32);
	resources.draw(container.getWidth()-63, 55);
	g.drawString(""+opponent.getResources(), container.getWidth()-40, 53);
	g.drawString(""+tmode, 10, 25);
    }
    
    public void playTutorial(){
	isTutorial = true;
	changeMenuState(false);
	tutStep = 0;
    }
    
    public void changeMenuState(boolean menuVisible){
	isMenu = menuVisible;
	if(!isMenu && !isTutorial && gameState == GameState.NORMAL)
	    gameInProgress = true;
	else if(gameState == GameState.WIN || gameState == GameState.LOSE){
	    gameInProgress = false;
	}
	if(isMenu){
	    menu.reset();
	    isTutorial = false;
	    gameState = GameState.NORMAL;
	}
    }

    private void attackPhase(Tower source, Tower target) {
	if(source == null || target == null){
	    System.err.println("NULL TOWER");
	    return;
	}
	switch(tmode){
	    default:
	    case NONE: 
		System.out.println("tmode is NONE");
		break;
	    case ATTACK:
		if(target.getOwner() == source.getOwner()){
		    System.out.println("You can't attack yourself!");
		    break;
		}
		if(source.canAttack() && source.getOwner().getResources() >= ATTACKCOST){
		    target.attackedBy((int) (1 + (source.getNumMinions()/2)*rand.nextFloat()), source);
		    source.getOwner().payResources(ATTACKCOST);
		    break;
		}
		resColor = Color.red;
		resColorTimer = System.currentTimeMillis();
		//System.out.println("Not enough resources.");
		break;
	    case CONQUER:
		if(target.getOwner() == source.getOwner()){
		    System.out.println("You can't conquer yourself!");
		    break;
		}
		if(source.canConquer() && source.getNumMinions() > 1){
		    target.conqueredBy(source.getNumMinions()/2, source);
		    source.addMinions(-source.getNumMinions()/2);
		    break;
		}
		System.out.println("You need more people to conquer another structure.");
		break;
	    case SUPPORT:
		if(source.getOwner() != null && target.getOwner() != null && source.getOwner().equals(player) && target.getOwner().equals(player) && source.canSupport() && source.getNumMinions() > 1){
		    target.addMinions(source.getNumMinions()/2);
		    source.addMinions(-source.getNumMinions()/2);
		}
		tmode = TowerMode.ATTACK;
		break;
	}
    }
    
    public void Over(){
	if(opponent.getMinions() <= 0){
	    gameState = GameState.WIN;
	} else if(player.getMinions() <= 0){	//YOU LOSE
	    gameState = GameState.LOSE;
	} else {
	    System.err.println("ERROR: no one won or lost. What the fuck happened?");
	    System.err.println("Player minions: "+player.getMinions());
	    System.err.println("Opponent minions: "+opponent.getMinions());
	}
    }
}
