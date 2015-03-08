
import java.io.*;
import java.util.List;

import bwapi.Game;
import bwapi.Position;
import bwapi.Unit;

public class Escenario implements World{

	private int SC_WIDTH;
	private int SC_HEIGHT;
	private List<Unit> l;
	private int lastAc,lastSt;
	
	// player position
	private int posX, posY;

	private boolean itsInside(int top,int bot,int right,int left){
		int x = posX*32;
		int y = posY*32;
		
		return (left<=x) && (x<=right) && (bot>=y) && (y>=top);				
	}
	
	private boolean valid(int x, int y) {		
		if((0 <= x) && (x < SC_WIDTH) && (0 <= y) && (y < SC_HEIGHT)){
			boolean dontCol = true;
			int i = 0;
			Unit m;
			
			while(dontCol && i < l.size()){
				m = l.get(i);
				if(itsInside(m.getTop(),m.getBottom(),m.getRight(),m.getLeft())){
					dontCol = false;
				}
				i++;
			}
			
			return dontCol;
		}else{
			return false;
		}
	}
	
	//constructor
	public Escenario(Game g,Unit u){
		SC_HEIGHT = g.mapHeight();
		SC_WIDTH = g.mapWidth();
		l = g.getAllUnits();
		posX = (int) u.getPosition().getX() / 32;
		posY = (int) u.getPosition().getY() / 32;
		
		for(Unit m: g.getAllUnits()){
			if(m!=u){
				l.add(m);
			}
		}
		
		lastAc = -1;
		lastSt = posY * SC_WIDTH + posX;
	}
	
	
	@Override
	public int numStates() {
		// TODO Auto-generated method stub
		return SC_HEIGHT * SC_WIDTH;
	}
	
	@Override
	public int numActions() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	
	/**
	 * accion = [0,3]
	 * 0 = Derecha
	 * 1 = Abajo
	 * 2 = Izquierda
	 * 3 = Arriba
	 */
	public double execute(int action) {
		int[] dx = { 1, 0, -1, 0 };
		int[] dy = { 0, 1, 0, -1 };
		double reward = 0;
		
		lastAc = action;
		lastSt = posY * SC_WIDTH + posX;
		
		posX += dx[action];
		posY += dy[action];
		
		if (!valid(posX, posY)) {
			posX -= dx[action];
			posY -= dy[action];
			reward = -1;
		}
		
		return reward;
	}
	
	public int lastAction(){
		return lastAc;
	}
	
	public int lastState(){
		return lastSt;
	}
	
	@Override
	public int state() {
		// TODO Auto-generated method stub
		return posY * SC_WIDTH + posX;
	}

	@Override
	public boolean finalState() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
