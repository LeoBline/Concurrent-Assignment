package Allsnake;

public class Map {
//make Map class a singleton patten
	
	private static Map map = new Map();
	private int[][] grid = null;
	private int gameSize = 80;
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
	
	public void setMapInfo(int index1, int index2, int num) {
		grid[index1][index2] = num;
	}
	
	public int getMapInfo(int index1, int index2) {
		return grid[index1][index2];
	}

	public void setMap(int[][] array) {
		grid = array;
	}
}
