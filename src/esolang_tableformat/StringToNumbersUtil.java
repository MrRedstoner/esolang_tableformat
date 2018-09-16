package esolang_tableformat;

import java.util.Scanner;

public class StringToNumbersUtil {

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Scanner sc=new Scanner(System.in);
		while(true){
			String s=sc.nextLine();
			for(char c:s.toCharArray()){
				System.out.print((int)c+" ");
			}
			System.out.println();
		}
	}
}