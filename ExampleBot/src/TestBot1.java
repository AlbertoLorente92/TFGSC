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

public class TestBot1{

    private Mirror mirror = new Mirror();
    
    private Unit marine;

    private Game game;

    private Player self;

    private boolean fin;
    private QLearner q;
    private World w;
    
    private DateFormat dateFormat;
	private Date date;
    
    public void run() {
        mirror.getModule().setEventListener(new DefaultBWListener() {
           // @Override
           /* public void onUnitCreate(Unit unit) {
                System.out.println("New unit " + unit.getType());
            }*/

            @Override
            public void onStart() {
                dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            	date = new Date();
            	
                game = mirror.getGame();
                self = game.self();
                //Use BWTA to analyze map
                //This may take a few minutes if the map is processed first time!
                System.out.println("Analyzing map...");
                BWTA.readMap();
                BWTA.analyze();
                
                getMarine();
                
                System.out.println("Map data ready");
                
                System.out.println(game.mapHeight() + " " + game.mapWidth());
                System.out.println(marine.getPosition().getX() / 32 + " " + marine.getPosition().getY() / 32);
                
                
                w = new Escenario(game,marine);
                q = new QLearner(w);
                game.enableFlag(1); // This command allows you to manually control the units during the game.
                
            }
            
            public void onEnd(boolean isWinner){
            	System.out.println("END");
            	System.out.println(marine.getPosition().getX() / 32 + " " + marine.getPosition().getY() / 32);
            	q.endOfGame(); 
            	Fichero.escribirTabla(q.qTable());
            	//q.mostrarQ();
            }

            private void getMarine() {
				// TODO Auto-generated method stub
            	for (Unit myUnit : self.getUnits()) {
            		if(myUnit.getType() == UnitType.Terran_Marine){
            			marine = myUnit;
            		}            			
            	}
			}

			@Override
            public void onFrame() {
                game.setTextSize(10);
                game.drawTextScreen(10, 10, "Playing as " + self.getName() + " - " + self.getRace());
                StringBuilder units = new StringBuilder("My units:\n");
                
                Position p = new Position(marine.getPosition().getX()-32,marine.getPosition().getY());
                int mov = q.move();
                
                if(mov == 0){
                	System.out.println("DERECHA");
                	marine.move(new Position(marine.getPosition().getX()+32,marine.getPosition().getY()));
                }
                
                if(mov == 1){
                	System.out.println("ABAJO");
                	marine.move(new Position(marine.getPosition().getX(),marine.getPosition().getY()+32));
                }
                
                if(mov == 2){
                	System.out.println("IZQUIERDA");
                	marine.move(new Position(marine.getPosition().getX()-32,marine.getPosition().getY()));
                }
                
                if(mov == 3){
                	System.out.println("ARRIBA");
                	marine.move(new Position(marine.getPosition().getX(),marine.getPosition().getY()-32));
                }
                
                if(mov!= 0 && mov!= 1 && mov!= 2 && mov!= 3)
                	System.out.println("CAGADA");
                /*Position p = new Position(marine.getPosition().getX()-32,marine.getPosition().getY());
                
                if(!p.isValid())
					System.err.println("posicion no valida");
                else
                	marine.move(p);*/
				
                
                
                //draw my units on screen
                game.drawTextScreen(10, 25, units.toString());
            }
        });

        mirror.startGame();
        
    }

    public static void main(String... args) {
        new TestBot1().run();
    }

}
