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
	private int[] position;
	private Direction direction;
	private Memory memory;
	
	EsolangMachine(XSSFSheet sheet){
		this.sheet=sheet;
		position=new int[]{0,0};
		direction=Direction.RIGHT;
		memory=new Memory();
	}
	
	void step(){
		XSSFCell cell=sheet.getRow(position[0]).getCell(position[1]);
		int value=CellFormatToNumberConvertor.getNumberFor(cell);
	}
}