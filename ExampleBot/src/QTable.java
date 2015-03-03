


public class QTable {
	private int tam,acc;
	private double[][] table;
	
	public QTable(int numStates, int numActions) {
		table = new double[numStates][numActions];
		tam = table.length;
		acc = table[0].length;
		clear();
	}
	
	public int getTam(){
		return tam;
	}
	
	public int getAcc(){
		return acc;
	}

	public void show(){
		System.out.println("TABLA");
		for (int i=0; i<table.length; i++) {
			for (int j=0; j<table[i].length; j++) {
				System.out.print(table[i][j]);
			}
			System.out.println();
		}
		
	}
	
	public void clear() {
		for (int i=0; i<table.length; i++) {
			for (int j=0; j<table[i].length; j++) {
				table[i][j] = 1;
			}
		}
	}
	
	public String line(int n){
		String s = "";
		
		if(n >= 0 && n < table.length){
			for(int i = 0; i < table[n].length; i++){
				s = s.concat(" ").concat(Double.toString(table[n][i]));
			}
		}
		
		return s;
	}
	
	public double get(int state, int action) {
		return table[state][action];
	}
	
	public void set(int state, int action, double reward) {
		table[state][action] = reward;
	}

	public double bestReward(int state) {
		double best = table[state][0];
		for (int i=1; i<table[state].length; i++) {
			best = Math.max(best, table[state][i]);
		}
		return best;
	}
}
