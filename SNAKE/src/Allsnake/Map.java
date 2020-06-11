package Allsnake;

public class Map {
//make Map class a singleton patten
	
	private static Map map = new Map();
	private int[][] grid = null;
	private int gameSize = 100;


	public Map() {
		grid = new int[gameSize][gameSize];
	}
	
	public static  Map getMap() {
		return map;
	}

	public int[][] getGrid() {
		return grid;
	}

	public int getGameSize(){
		return gameSize;
	}

	public synchronized void setMapInfo(int index1, int index2, int num) {
		grid[index1][index2] = num;
	}
	
	public synchronized int getMapInfo(int index1, int index2) {
		return grid[index1][index2];
	}


	/**
	 * Make sure only one snake will get food when Multiple snakes tried to get bonus.
	 * @param x
	 * @param y
	 * @return Whether this location is food or not
	 */
	public synchronized boolean eatFood(int x, int y) {
		if(grid[x][y]==1||grid[x][y]==2||grid[x][y]==3) {
			grid[x][y]=0;
			return true;
		}else {
			return false;
		}
	}

}
