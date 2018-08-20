package esolang_tableformat;

import java.util.Arrays;
import java.util.Scanner;

public class NumberToFormatUtil {

	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		
		while(true){
			int number=sc.nextInt();
			short[]text,back,under,right;
			text=new short[3];
			back=new short[3];
			under=new short[3];
			right=new short[3];
			
			for(int i=0;i<=22;i+=2){
				int j=3<<i;
				int a=j&number;
				//System.out.println(i+" "+j+" "+a);
				
				switch(i){
					case 0:{
						back[0]=(short) ((a>>i)*85);
					}
					case 2:{
						back[1]=(short) ((a>>i)*85);
					}
					case 4:{
						back[2]=(short) ((a>>i)*85);
					}
					
					case 6:{
						text[0]=(short) ((a>>i)*85);
					}
					case 8:{
						text[1]=(short) ((a>>i)*85);
					}
					case 10:{
						text[2]=(short) ((a>>i)*85);
					}
					
					case 12:{
						under[0]=(short) ((a>>i)*85);
					}
					case 14:{
						under[1]=(short) ((a>>i)*85);
					}
					case 16:{
						under[2]=(short) ((a>>i)*85);
					}
					
					case 18:{
						right[0]=(short) ((a>>i)*85);
					}
					case 20:{
						right[1]=(short) ((a>>i)*85);
					}
					case 22:{
						right[2]=(short) ((a>>i)*85);
					}
				}
			}
			
			System.out.println("back: "+back[2]+' '+back[1]+' '+back[0]);
			System.out.println("text: "+text[2]+' '+text[1]+' '+text[0]);
			System.out.println("under: "+under[2]+' '+under[1]+' '+under[0]);
			System.out.println("right: "+right[2]+' '+right[1]+' '+right[0]);
		}
	}
}