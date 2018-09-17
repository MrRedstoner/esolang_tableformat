package esolang_tableformat;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Stack;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class EsolangMachine {
	private enum Direction{
		UP,DOWN,LEFT,RIGHT;
		
		static Direction clockwise(Direction d){
			switch(d){
			case DOWN:return LEFT;
			case LEFT:return UP;
			case RIGHT:return DOWN;
			case UP:return RIGHT;
			}
			return d;
		}
		
		static Direction counterClockwise(Direction d){
			switch(d){
			case DOWN:return RIGHT;
			case LEFT:return DOWN;
			case RIGHT:return UP;
			case UP:return LEFT;
			}
			return d;
		}
	}
	
	private class Memory{
		Memory(){
			f=new int[128];
			g=new ArrayDeque<Integer>();
			h=new ArrayDeque<Integer>();
			i=new Stack<Integer>();
			j=new Stack<Integer>();
		}
		
		int a;
		int b;
		int c,d;
		int e;
		int[] f;
		ArrayDeque<Integer> g,h;
		Stack<Integer> i,j;
		
		public void print() {
			System.out.println("a: "+a);
			System.out.println("b: "+b);
			System.out.println("c: "+c);
			System.out.println("d: "+d);
			System.out.println("e: "+e);
			System.out.println("f: "+Arrays.toString(f));
			System.out.println("g: "+g.toString());
			System.out.println("h: "+h.toString());
			System.out.println("i: "+i.toString());
			System.out.println("j: "+j.toString());
		}
	}
	
	private enum Special{
		NONE,NEXT_AS_NUMBER,PAUSE
	}
	
	private XSSFSheet sheet;
	private int[] position;//{row,column}
	private Direction direction;
	private Memory memory;
	private Special special;
	private long pauseUntil;
	private Input in;
	private Output out;
	private boolean debug=false;
	
	EsolangMachine(XSSFSheet sheet,Input in,Output out){
		this.sheet=sheet;
		this.in=in;
		this.out=out;
		
		position=new int[]{0,0};
		direction=Direction.RIGHT;
		memory=new Memory();
		special=Special.NONE;
	}
	
	void setDebug(boolean b){
		debug=b;
	}
	
	boolean step(){//returns true for halt
		if(special==Special.PAUSE){
			if(System.currentTimeMillis()>=pauseUntil){
				special=Special.NONE;
			}
			return false;
		}
		
		XSSFCell cell=sheet.getRow(position[0]).getCell(position[1]);
		if(cell==null){
			onHalt();
			return true;
		}
		int value=CellFormatToNumberConvertor.getNumberFor(cell);
		
		if(debug){
			System.out.println(value);
		}
		
		if(special==Special.NEXT_AS_NUMBER){
			memory.a=value;
			special=Special.NONE;
		}else{
			switch(value){
				case 0:{//do nothing
				}break;
				case 1:{//go up
					direction=Direction.UP;
				}break;
				case 2:{//go down
					direction=Direction.DOWN;
				}break;
				case 3:{//go left
					direction=Direction.LEFT;
				}break;
				case 4:{//go right
					direction=Direction.RIGHT;
				}break;
				case 5:{//next as number
					special=Special.NEXT_AS_NUMBER;
				}break;
				case 6:{//input number		
					memory.a=in.readNumber();
				}break;
				case 7:{//input character
					memory.a=in.readCharacter();
				}break;
				case 8:{//output number
					out.writeNumber(memory.a);
				}break;
				case 9:{//output character
					out.writeCharacter(memory.a);
				}break;
				case 10:{//move to B
					memory.b=memory.a;
				}break;
				case 11:{//move from B
					memory.a=memory.b;
				}break;
				case 12:{//move to C
					memory.c=memory.a;
				}break;
				case 13:{//move from C
					memory.a=memory.c;
				}break;
				case 14:{//move to D
					memory.d=memory.a;
				}break;
				case 15:{//move from D
					memory.a=memory.d;
				}break;
				case 16:{//move from E
					memory.a=memory.e;
				}break;
				case 17:{//move to F
					try {
						memory.f[memory.b]=memory.a;
					} catch (Exception e) {}
				}break;
				case 18:{//move from F
					try {
						memory.a=memory.f[memory.b];
					} catch (Exception e) {}
				}break;
				case 19:{//move to G
					memory.g.add(memory.a);
				}break;
				case 20:{//move from G
					if(!memory.g.isEmpty()){
						memory.a=memory.g.pop();
					}
				}break;
				case 21:{//move to H
					memory.h.add(memory.a);
				}break;
				case 22:{//move from H
					if(!memory.h.isEmpty()){
						memory.a=memory.h.pop();
					}
				}break;
				case 23:{//move to I
					memory.i.add(memory.a);
				}break;
				case 24:{//move from I
					if(!memory.i.isEmpty()){
						memory.a=memory.i.pop();
					}
				}break;
				case 25:{//move to J
					memory.j.add(memory.a);
				}break;
				case 26:{//move from J
					if(!memory.j.isEmpty()){
						memory.a=memory.j.pop();
					}
				}break;
				case 27:{//negate
					memory.e=memory.c^Integer.MAX_VALUE;
				}break;
				case 28:{//and
					memory.e=memory.c&memory.d;
				}break;
				case 29:{//or
					memory.e=memory.c|memory.d;
				}break;
				case 30:{//xor
					memory.e=memory.c^memory.d;
				}break;
				case 31:{//add
					memory.e=memory.c+memory.d;
				}break;
				case 32:{//subtract
					memory.e=memory.c-memory.d;
				}break;
				case 33:{//multiply
					memory.e=memory.c*memory.d;
				}break;
				case 34:{//divide
					memory.e=memory.c/memory.d;
				}break;
				case 35:{//modulo
					memory.e=memory.c%memory.d;
				}break;
				case 36:{//switch
					if(memory.a>0){
						direction=Direction.clockwise(direction);
					}else if(memory.a<0){
						direction=Direction.counterClockwise(direction);
					}
				}break;
				case 37:{//pause
					special=Special.PAUSE;
					pauseUntil=System.currentTimeMillis()+memory.a;
				}break;
				case 38:{//end
					onHalt();
					return true;
				}
			}
		}
		
		switch(direction){
			case DOWN:{
				position[0]++;
			}break;
			case LEFT:{
				position[1]--;
			}break;
			case RIGHT:{
				position[1]++;
			}break;
			case UP:{
				position[0]--;
			}break;
		}
		
		return false;
	}
	
	private void onHalt(){
		if(debug)memory.print();
	}
}