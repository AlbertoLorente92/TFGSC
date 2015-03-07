import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import bwapi.*;
import bwta.*;

public class TestBot1 {

	private Mirror mirror = new Mirror();

	private Unit marine;

	private Game game;
	private Player self;

	private QLearner q;
	private World w;
	private Position marineP;
	private DateFormat dateFormat;
	private Date date;

	public void run() {
		mirror.getModule().setEventListener(new DefaultBWListener() {
			// @Override
			/*
			 * public void onUnitCreate(Unit unit) {
			 * System.out.println("New unit " + unit.getType()); }
			 */

			@Override
			public void onStart() {
				dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				date = new Date();
				game = mirror.getGame();
				self = game.self();
				// Use BWTA to analyze map
				// This may take a few minutes if the map is processed first
				// time!
				System.out.println("Analyzing map...");
				BWTA.readMap();
				BWTA.analyze();

				getMarine();

				System.out.println("Map data ready");

				System.out.println(game.mapHeight() + " " + game.mapWidth());

				w = new Escenario(game, marine);

				// QTable qT = Fichero.leeTabla();
				// q = new QLearner(w,qT);

				q = new QLearner(w);

				game.setLocalSpeed(100);
				// game.setGUI(false);
				game.enableFlag(0); // This command allows you to manually
									// control the units during the game.

			}

			public void onEnd(boolean isWinner) {
				System.out.println("END");
				q.endOfGame();
				Fichero.escribirTabla(q.qTable());
			}

			private void getMarine() {
				for (Unit myUnit : self.getUnits()) {
					if (myUnit.getType() == UnitType.Terran_Marine) {
						marine = myUnit;
						marineP = marine.getPosition();
					}
				}
			}
			
			private boolean itsInside(int top,int bot,int right,int left,Position p){
				int x = p.getX();
				int y = p.getY();
				
				return (left<=x) && (x<=right) && (bot>=y) && (y>=top);				
			}
			
			private Position dontCollision(Position p){				
				for (Unit myUnit : game.getAllUnits()) {
					if (myUnit.getType() != UnitType.Terran_Marine && itsInside(myUnit.getTop(),myUnit.getBottom(),myUnit.getRight(),myUnit.getLeft(),p)) {
						System.out.println("Movimiento incorrecto");
						return new Position(marine.getPosition().getX(),marine.getPosition().getY());
					}
				}
				return p;
			}
			
			private Position makeItValid(Position p){
				int x = p.getX();
				int y = p.getY();
				
				//Is x correct?
				if(x>(game.mapWidth()*32)){
					x = game.mapWidth()*32;
				}else if(x<0){
					x = 0;
				}
				
				//Is y correct?
				if(y>(game.mapHeight()*32)){
					y = game.mapHeight()*32;
				}else if(y<0){
					y = 0;
				}
				
				return dontCollision(new Position(x,y));
			}

			@Override
			public void onFrame() {
				game.setTextSize(10);
				if(!marine.isMoving()){
					marineP = marine.getPosition();
					System.out.println(marine.getPosition().getX() / 32 + " - " + marine.getPosition().getY() / 32);
					int mov = q.move();
					if (mov == 0) { // DERECHA
						System.out.println("DERECHA");
						Position p = makeItValid(new Position(marine.getPosition().getX() + 32, marine.getPosition().getY()));
						
						marine.move(p);
						
					
					} else if (mov == 1) { // ABAJO	
						System.out.println("ABAJO");
						Position p = makeItValid(new Position(marine.getPosition().getX(),marine.getPosition().getY() + 32));
						
						marine.move(p);
						
					
					} else if (mov == 2) { // IZQUIERDA
						System.out.println("IZQUIERDA");
						Position p = makeItValid(new Position(marine.getPosition().getX() - 32, marine.getPosition().getY()));
					
						marine.move(p);
						
					
					} else if (mov == 3) { // ARRIBA
						System.out.println("ARRIBA");
						Position p = makeItValid(new Position(marine.getPosition().getX(),marine.getPosition().getY() - 32));
						
						marine.move(p);
						
					
					}

					if (marine.isStuck())
						System.out.println("STUCK");
				}
			}
		});

		mirror.startGame();

	}

	public static void main(String... args) {
		new TestBot1().run();
	}

}
