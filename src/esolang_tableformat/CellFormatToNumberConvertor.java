package esolang_tableformat;

import java.util.Arrays;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

public class CellFormatToNumberConvertor {
	public static final int DIFF=5;
	public static final int BASE=85;
	
	public static int getNumberFor(Cell c){
		int out=0;
		CellStyle cs=c.getCellStyle();
		
		try {
			XSSFColor front=(XSSFColor) cs.getFillForegroundColorColor();
			
			byte[]frontByte;
			if(front!=null){
				frontByte=front.getRGB();
			}else{
				frontByte=new byte[]{(byte) 255,(byte) 255,(byte) 255};
			}
			/*color is as: 0 > 0 ; 85 > 85 ; 170 > -86 ; 255 > -1 ; solvable by &0xFF */
			short[]frontShort=new short[3];
			for(int i=0;i<frontShort.length;i++){
				frontShort[i]=(short) (0xFF&frontByte[i]);
			}
			
			System.out.println(Arrays.toString(frontShort));
			
			for(int i=2;i>=0;i--){
				short current=frontShort[i];//partial color to process, starting from the least significant
				int index=2-i;
				index<<=1;//multiply by 2;
				int powOf2=1<<index;//significance of partial color
				//System.out.print(current+" "+powOf2);
				for(int j=0;j<=3;j++){
					int middle=BASE*j;
					if(isInRange(current,middle,DIFF)){
						//System.out.println(" "+j);
						out+=(j*powOf2);
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return out;
	}
	
	public static boolean isInRange(int number, int middle, int maxDiff){
		if(Math.abs(middle-number)<=maxDiff){
			return true;
		}else{
			return false;
		}
	}
}