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

	//take keypress from buffer
	public synchronized int take(int i) {
		if(Inptr != 0) {
			int a =  MovementList[0];
			Inptr = Inptr - 1;
			for(int z=0;z<Inptr;z++) {
				MovementList[z]=MovementList[z+1];
			}
			return a;
		}
		else {
			return i;
		}
	}
	//append keypress to buffer
	public synchronized void append(int a) {
		if(Inptr < 40) {

			MovementList[Inptr] = a;
			Inptr = Inptr + 1;
		}
	}

}
