package Allsnake;

/**
 * The map class
 */
public class Map {
//Make Map class a singleton patten
	//Initialize map
	private static Map map = new Map();
	//Set coordinates array
	private int[][] grid = null;
	//Set game size
	private int gameSize = 100;

	/**
	 * Set game grid
	 */
	public Map() {
		grid = new int[gameSize][gameSize];
	}

	/**
	 *Get map
	 * @return map
	 */
	public static  Map getMap() {
		return map;
	}

	/**
	 * Return map grid
	 * @return grid
	 */
	public int[][] getGrid() {
		return grid;
	}

	/**
	 * Get game size
	 * @return gameSize
	 */
	public int getGameSize(){
		return gameSize;
	}

	/**
	 * Set map information
	 * @param index1
	 * @param index2
	 * @param num
	 */
	public synchronized void setMapInfo(int index1, int index2, int num) {
		grid[index1][index2] = num;
	}

	/**
	 * Get map information
	 * @param index1
	 * @param index2
	 * @return
	 */
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
