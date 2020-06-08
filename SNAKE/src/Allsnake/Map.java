package Allsnake;

public class Map {
//make Map class a singleton patten
	
	private static Map map = new Map();
	private int[][] grid = null;//2D array represent the location on the map
	private int gameSize = 100;
/**
 * 
 */
public Map() {
	// TODO Auto-generated constructor stub
	grid = new int[gameSize][gameSize];
}
	
	public static  Map getMap() {
		return map;
	}
	public int[][] getgrid() {
		return grid;
	}
	
	public synchronized void setMapInfo(int index1, int index2, int num) {
		grid[index1][index2] = num;
	}
	
	public int getMapInfo(int index1, int index2) {
		return grid[index1][index2];
	}

	public void setMap(int[][] array) {
		grid = array;
	}
}
