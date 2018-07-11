package esolang_tableformat;

import java.util.ArrayDeque;
import java.util.Stack;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class EsolangMachine {
	private enum Direction{
		UP,DOWN,LEFT,RIGHT
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
	
	EsolangMachine(XSSFSheet sheet,Input in,Output out){
		this.sheet=sheet;
		this.in=in;
		this.out=out;
		
		position=new int[]{0,0};
		direction=Direction.RIGHT;
		memory=new Memory();
		special=Special.NONE;
	}
	
	void step(){
		if(special==Special.PAUSE){
			if(System.currentTimeMillis()>=pauseUntil){
				special=Special.NONE;
			}
			return;
		}
		
		XSSFCell cell=sheet.getRow(position[0]).getCell(position[1]);
		int value=CellFormatToNumberConvertor.getNumberFor(cell);
		
		if(special==Special.NEXT_AS_NUMBER){
			memory.a=value;
		}else{
			switch(value){
				case 0:{//do nothing
				}break;
				case 1:{//go up
					direction=Direction.UP;
				}break;
				case 2:{//
					
				}break;
				case 3:{//
					
				}break;
				case 4:{//
					
				}break;
				case 5:{//
					
				}break;
				case 6:{//
					
				}break;
				/*
go down
go left
go right
next as number
input number
input character
output number
output character
move to B
move from B
move to C
move from C
move to D
move from D
move from E
move to F
move from F
move to G
move from G
move to H
move from H
move to I
move from I
move to J
move from J
negate
and
or
xor
add
substract
multiply
divide
modulo
switch
pause
end

				 */
			}
		}
		
		switch(direction){
			case DOWN:{
				position[1]++;
			}break;
			case LEFT:{
				position[0]--;
			}break;
			case RIGHT:{
				position[0]++;
			}break;
			case UP:{
				position[1]--;
			}break;
		}
	}
}