package SNACkk.src;

import com.sun.source.util.TaskEvent;

public class Buffer {

private char[] MovementList;
private int Inptr;
public Buffer() {
		super();
		MovementList = new char[40];
		Inptr = 0;
	}
public char Take() {
	char a =  MovementList[Inptr];
	MovementList[Inptr] = ' ';
	Inptr = Inptr - 1;
	return a;
}
public void append(char a) {
	Inptr = Inptr + 1;
	MovementList[Inptr] = a;
}

}
