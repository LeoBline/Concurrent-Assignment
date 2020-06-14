package Allsnake;

public class Buffer {
	//MovementList used to stord the palyer's move.Thread will take it and update the snake.
	private int[] MovementList;
	//Inptre used to record the append and take position.
	private int Inptr;
	public Buffer() {
		super();
		//it can store 40 chars.
		MovementList = new int[100];
		Inptr = 0;
	}
	//we use ' ' present null.
	public synchronized int take(int i) {
		if(Inptr != 0) {
			int a =  MovementList[Inptr];
			MovementList[Inptr] = ' ';
			Inptr = Inptr - 1;
			return a;
		}
		else {
			//if input==0 than it will return i.the i is the old direction
			return i;
		}
	}
	public synchronized void append(int a) {
		if(Inptr < 40) {
			Inptr = Inptr + 1;
			MovementList[Inptr] = a;
		}
	}

}
