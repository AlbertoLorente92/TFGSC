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
	private int cont;
	private Player self;

	private boolean fin;
	private QLearner q;
	private World w;

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
				cont = 0;
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
				System.out.println(marine.getPosition().getX() / 32 + " "
						+ marine.getPosition().getY() / 32);

				w = new Escenario(game, marine);

				// QTable qT = Fichero.leeTabla();
				// q = new QLearner(w,qT);

				q = new QLearner(w);

				game.setLocalSpeed(0);
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
					}
				}
			}

			@Override
			public void onFrame() {
				game.setTextSize(10);
				if(!marine.isMoving()){
				//if (w.state() != w.lastState()) {

					int mov = q.move();
					if (mov == 0) { // DERECHA
						int x = marine.getPosition().getX() + 32;
						if(x>game.mapWidth()){
							x = game.mapWidth();
						}
						Position p = new Position(x, marine.getPosition().getY());
						if(p.isValid()){
							marine.move(p);
							System.out.println("DERECHA - "+ marine.getPosition().getX() / 32 + " "+ marine.getPosition().getY() / 32);
						}
					} else if (mov == 1) { // ABAJO
						int y = marine.getPosition().getY() + 32;
						if(y>game.mapHeight()){
							y = game.mapHeight();
						}
						
						Position p = new Position(marine.getPosition().getX(),y);
						
						marine.move(p);
						System.out.println("ABAJO - "+ marine.getPosition().getX() / 32 + " "+ marine.getPosition().getY() / 32);
					
					} else if (mov == 2) { // IZQUIERDA
						int x = marine.getPosition().getX() - 32;
						if(x<0){
							x = 0;
						}
						Position p = new Position(x, marine.getPosition().getY());
					
						marine.move(p);
						System.out.println("IZQUIERDA - "+ marine.getPosition().getX() / 32 + " "+ marine.getPosition().getY() / 32);
					
					} else if (mov == 3) { // ARRIBA
						int y = marine.getPosition().getY() - 32;
						if(y<0){
							y = 0;
						}
						Position p = new Position(marine.getPosition().getX(),y);
						
						marine.move(p);
						System.out.println("ARRIBA - "+ marine.getPosition().getX() / 32 + " "+ marine.getPosition().getY() / 32);
					
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
