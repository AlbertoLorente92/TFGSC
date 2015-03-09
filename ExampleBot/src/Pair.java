
public class Pair {
	private int direccion;
	private boolean canMove;
	
	public Pair(int d,boolean b){
		direccion = d;
		canMove = b;
	}
	
	public int getDireccion(){
		return direccion;
	}
	
	public boolean getCanMove(){
		return canMove;
	}
}
