
import java.io.*;

import bwapi.Game;
import bwapi.Position;
import bwapi.Unit;

public class Escenario implements World{

	private int SC_WIDTH;
	private int SC_HEIGHT;
	private Game game;
	private Unit unit;
	private int lastAc;
	
	// player position
	private int posX, posY;

	private boolean valid(int x, int y) {
		Position P = new Position(x,y);
		
		if(!P.isValid())
			return false;
		
		return (0 <= x) && (x < SC_WIDTH) &&
				(0 <= y) && (y < SC_HEIGHT);
	}
	
	//constructor
	public Escenario(Game g,Unit u){
		this.game =  g;
		SC_HEIGHT = game.mapHeight();
		SC_WIDTH = game.mapWidth();
		this.unit = u;
		posX = (int) unit.getPosition().getX() / 32;
		posY = (int) unit.getPosition().getY() / 32;
		lastAc = -1;
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
	public double execute(int action) {
		lastAc = action;
		int[] dx = { 1, 0, -1, 0 };
		int[] dy = { 0, 1, 0, -1 };
		double reward = 0;

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

	/*static boolean leerMapa(String mapa) {
	      File archivo = null;
	      FileReader fr = null;
	      BufferedReader br = null;
	      boolean exito=true;
	      try {
	         // Apertura del fichero y creacion de BufferedReader para poder
	         // hacer una lectura comoda (disponer del metodo readLine()).
	         archivo = new File ("mapa.txt");
	         fr = new FileReader (archivo);
	         br = new BufferedReader(fr);
	 
	         // Lectura del fichero
	         String linea;
	         //comprobacion
	         while((linea=br.readLine())!=null)
	            System.out.println(linea);
	      }
	      catch(Exception e){
	         e.printStackTrace();
	      }finally{
	         // En el finally cerramos el fichero, para asegurarnos
	         // que se cierra tanto si todo va bien como si salta 
	         // una excepcion.
	         try{                    
	            if( null != fr ){   
	               fr.close();     
	            }                  
	         }catch (Exception e2){ 
	            e2.printStackTrace();
	         }
	      }
		return exito;
	}
	
	
	public static boolean guardarMapa(String mapa){
		boolean exito=true;
	
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter("mapa.txt");
            pw = new PrintWriter(fichero);
            pw.println("falta texto ");
 
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           // Nuevamente aprovechamos el finally para 
           // asegurarnos que se cierra el fichero.
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
 
		return exito;
	}*/
}
