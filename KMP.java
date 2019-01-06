package code;
/* 
(originally from R.I. Nishat - 08/02/2014)
(revised by N. Mehta - 11/7/2018)
*/

/*
 * Nathan Marcotte
 * CSC 226 A4
 * 11/26/18
 * v00876934
 * 
 * */

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;


public class KMP{
 private String pattern;
 private int[][] dfa;
 private final int alphaLen = 4; // {A, C, G, T}
//algs 4
 
 public int alphaMap(char c){ // GIVE EACH CHARACTER OF ALPHABET A CORRRESPONDING INDEX IN DFA 
	 if(c=='A') {
		 return 0;
	 } else if(c=='C') {
		 return 1;
	 } else if(c=='G') {
		 return 2;
	 } else if(c=='T') {
		 return 3;
	 }
	 
	 return -1;
 }
 
 public KMP(String pattern){  
	this.pattern = pattern;
	int patLen = pattern.length();
	//System.out.println(pattern.charAt(0));
	dfa=new int[alphaLen][patLen];
	dfa[alphaMap(pattern.charAt(0))][0] = 1;
	
	for(int i = 0, j = 1; j<patLen; j++) {
			for(int cop = 0; cop<alphaLen; cop++)
				dfa[cop][j] = dfa[cop][i];
			
		dfa[alphaMap(pattern.charAt(j))][j] = j+1;
		i = dfa[alphaMap(pattern.charAt(j))][i];
	}
	
 }
 
 public int search(String txt){  
     int patLen = pattern.length();
     int strLen = txt.length();
     
     // System.out.println(txt);
     
     int i, j;
     
     for (i = 0,j = 0; i < strLen && j < patLen; i++) {
         j = dfa[alphaMap(txt.charAt(i))][j];
     }
     
    // System.out.println(i);
   //  System.out.println(j);
     
     if (j == patLen) 
    	 return i-j;
     
     return strLen;                    
 }
 
 
 public static void main(String[] args) throws FileNotFoundException{
	Scanner s;
	if (args.length > 0){
	    try{
		s = new Scanner(new File(args[0]));
	    }
	    catch(java.io.FileNotFoundException e){
		System.out.println("Unable to open "+args[0]+ ".");
		return;
	    }
	    System.out.println("Opened file "+args[0] + ".");
	    String text = "";
	    while(s.hasNext()){
		text += s.next();
	    }
	    
	    for(int i = 1; i < args.length; i++){
		KMP k = new KMP(args[i]);
		int index = k.search(text);
		if(index >= text.length()){
		    System.out.println(args[i] + " was not found.");
		}
		else System.out.println("The string \"" + args[i] + "\" was found at index " + index + ".");
	    }
	}
	else{
	    System.out.println("usage: java SubstringSearch <filename> <pattern_1> <pattern_2> ... <pattern_n>.");
	}
 }
}