package SNACkk.src;
public class Buffer {
//MovementList used to stord the palyer's move.Thread will take it and update the snake.
private char[] MovementList;
//Inptre used to record the append and take position.
private int Inptr;
public Buffer() {
		super();
		//it can store 40 chars.
		MovementList = new char[40];
		Inptr = 0;
	}
//we use ' ' present null.
public char Take() {
	if(Inptr != 0) {
	char a =  MovementList[Inptr];
	MovementList[Inptr] = ' ';
	Inptr = Inptr - 1;
	return a;
	}
	else {
		return ' ';
	}
}
public void append(char a) {
	if(Inptr < 40) {
	Inptr = Inptr + 1;
	MovementList[Inptr] = a;
	}
}

}
