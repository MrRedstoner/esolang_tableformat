package esolang_tableformat;

import java.util.Arrays;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

public class CellFormatToNumberConvertor {
	public static final int DIFF=5;
	public static final int BASE=85;
	
	public static int getNumberFor(Cell c){
		int out=0;
		XSSFCellStyle cs=(XSSFCellStyle) c.getCellStyle();
		
		try {
			XSSFColor back=cs.getFillForegroundColorColor();//actually is cell background
			
			byte[]backByte;
			if(back!=null){
				backByte=back.getRGB();
			}else{
				backByte=new byte[]{(byte) 255,(byte) 255,(byte) 255};
			}
			/*color is as: 0 > 0 ; 85 > 85 ; 170 > -86 ; 255 > -1 ; solvable by &0xFF */
			short[]backShort=new short[3];
			for(int i=0;i<backShort.length;i++){
				backShort[i]=(short) (0xFF&backByte[i]);
			}
			
			//System.out.println(Arrays.toString(backShort));
			
			for(int i=2;i>=0;i--){
				short current=backShort[i];//partial color to process, starting from the least significant
				int index=2-i;
				index<<=1;//multiply by 2;
				int powOf2=1<<index;//significance of partial color
				//System.out.print(current+" "+powOf2);
				for(int j=0;j<=3;j++){
					int middle=BASE*j;
					if(isInRange(current,middle,DIFF)){
						//System.out.println(" "+j+" "+(j*powOf2));
						out+=(j*powOf2);
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			XSSFColor front=cs.getFont().getXSSFColor();
			
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
			
			//System.out.println(Arrays.toString(frontShort));
			
			for(int i=2;i>=0;i--){
				short current=frontShort[i];//partial color to process, starting from the least significant
				int index=2-i;
				index<<=1;//multiply by 2;
				int powOf2=1<<index;//significance of partial color
				powOf2<<=6;
				//System.out.print(current+" "+powOf2);
				for(int j=0;j<=3;j++){
					int middle=BASE*j;
					if(isInRange(current,middle,DIFF)){
						//System.out.println(" "+j+" "+(j*powOf2));
						out+=(j*powOf2);
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			XSSFColor under=cs.getBottomBorderXSSFColor();
			
			byte[]underByte;
			if(under!=null){
				underByte=under.getRGB();
			}else{
				underByte=new byte[]{(byte) 255,(byte) 255,(byte) 255};
			}
			/*color is as: 0 > 0 ; 85 > 85 ; 170 > -86 ; 255 > -1 ; solvable by &0xFF */
			short[]underShort=new short[3];
			for(int i=0;i<underShort.length;i++){
				underShort[i]=(short) (0xFF&underByte[i]);
			}
			
			//System.out.println(Arrays.toString(underShort));
			
			for(int i=2;i>=0;i--){
				short current=underShort[i];//partial color to process, starting from the least significant
				int index=2-i;
				index<<=1;//multiply by 2;
				int powOf2=1<<index;//significance of partial color
				powOf2<<=12;
				//System.out.print(current+" "+powOf2);
				for(int j=0;j<=3;j++){
					int middle=BASE*j;
					if(isInRange(current,middle,DIFF)){
						//System.out.println(" "+j+" "+(j*powOf2));
						out+=(j*powOf2);
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			XSSFColor right=cs.getRightBorderXSSFColor();
			
			byte[]rightByte;
			if(right!=null){
				rightByte=right.getRGB();
			}else{
				rightByte=new byte[]{(byte) 255,(byte) 255,(byte) 255};
			}
			/*color is as: 0 > 0 ; 85 > 85 ; 170 > -86 ; 255 > -1 ; solvable by &0xFF */
			short[]rightShort=new short[3];
			for(int i=0;i<rightShort.length;i++){
				rightShort[i]=(short) (0xFF&rightByte[i]);
			}
			
			//System.out.println(Arrays.toString(underShort));
			
			for(int i=2;i>=0;i--){
				short current=rightShort[i];//partial color to process, starting from the least significant
				int index=2-i;
				index<<=1;//multiply by 2;
				int powOf2=1<<index;//significance of partial color
				powOf2<<=18;
				//System.out.print(current+" "+powOf2);
				for(int j=0;j<=3;j++){
					int middle=BASE*j;
					if(isInRange(current,middle,DIFF)){
						//System.out.println(" "+j+" "+(j*powOf2));
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