package Allsnake;

public class Map {
//make Map class a singleton patten
	
	private static Map map = new Map();
	private int[][] grid = null;
	
	private Map(){
		
	}
	
	public static  Map getMap() {
		return map;
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
