package esolang_tableformat;

import java.util.Arrays;

import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXf;

public class CellFormatToNumberConvertor {
	private static StylesTable mst;
	
	static void init(XSSFWorkbook workbook){
		mst = null;
		for (POIXMLDocumentPart part :workbook.getRelations()) {
			if (part instanceof StylesTable) {
				mst = (StylesTable) part;
				break;
			}
		}
	}
	
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
		
		try {//seems to get the / instead of the \
			CTXf ctxf=cs.getCoreXf();
			int borderIndex=(int) ctxf.getBorderId();
			CTBorder ctb=mst.getBorderAt(borderIndex).getCTBorder();
			CTColor slash=ctb.getDiagonal().getColor();
			
			byte[]slashByte;
			if(slash!=null){
				slashByte=slash.getRgb();
			}else{
				System.out.println("trouble");
				slashByte=new byte[]{(byte) 255,(byte) 255,(byte) 255};
			}
			/*color is as: 0 > 0 ; 85 > 85 ; 170 > -86 ; 255 > -1 ; solvable by &0xFF */
			short[]slashShort=new short[3];
			for(int i=0;i<slashShort.length;i++){
				slashShort[i]=(short) (0xFF&slashByte[i]);
			}
			
			System.out.println(Arrays.toString(slashShort));
			
			for(int i=2;i>=0;i--){
				short current=slashShort[i];//partial color to process, starting from the least significant
				int index=2-i;
				index<<=1;//multiply by 2;
				int powOf2=1<<index;//significance of partial color
				powOf2<<=12;
				System.out.print(current+" "+powOf2);
				for(int j=0;j<=3;j++){
					int middle=BASE*j;
					if(isInRange(current,middle,DIFF)){
						System.out.println(" "+j+" "+(j*powOf2));
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