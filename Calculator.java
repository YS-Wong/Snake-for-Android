package com.WYX.Snake;

import java.util.Collections;
import java.util.Stack;
import java.util.TreeMap;

public class Calculator {
	private Stack<String> postfixStack  = new Stack<String>();//後綴式棧
	private Stack<Character> opStack  = new Stack<Character>();//運算符棧
	private static TreeMap<Character,Integer> OP=new TreeMap<Character,Integer>();
	public String calculate(String str) throws NumberFormatException{
		String result="";
		String[] s=MainActivity.Writer.split(str, "[+]");
		for(int i=0;i<s.length;i++){
			result=result+findString(s[i]);
		}
		return result;
	}
	private String findString(String str) throws NumberFormatException{
		if(str.startsWith("'")&&str.endsWith("'")){
			str=str.replaceFirst("'", "");
			StringBuffer sb=new StringBuffer(str);
			sb.setLength(sb.length()-1);
			return sb.toString();
		}else if((str.startsWith("(")||str.startsWith("double("))&&str.endsWith(")")){
			return calculateNum(str)+"";
		}else if(str.startsWith("int(")&&str.endsWith(")")){
			return (int)calculateNum(str)+"";
		}else{
			if(MainActivity.obj.containsKey(str))return MainActivity.obj.get(str);
			else throw new NumberFormatException();
		}
	}
	private double calculateNum(String expression) throws NumberFormatException {
		OP.put('!', 1);OP.put('^', 1);
		OP.put('*', 2);OP.put('/', 2);OP.put('%', 2);
		OP.put('+', 3);OP.put('-', 3);
		OP.put('>', 4);OP.put('<', 4);
		OP.put('=', 5);OP.put('&', 6);OP.put('|', 7);
		OP.put('(', 8);OP.put(')', 8);
		Stack<String> resultStack  = new Stack<String>();
		prepare(expression);
		Collections.reverse(postfixStack);//將後綴式棧反轉
		String firstValue  ,secondValue,currentValue;//參與計算的第一個值、第二個值和運算符
		while(!postfixStack.isEmpty()) {
			currentValue  = postfixStack.pop();
			if(!isOperator(currentValue.charAt(0))) {//如果不是運算符則存入操作數棧中
				resultStack.push(currentValue);
			} else {//如果是運算符則從操作數棧中取兩個值和該數值一起參與運算
				if(resultStack.empty()){
					secondValue="0";
				}else{
					secondValue=resultStack.pop();
				}
				if(resultStack.empty()){
					firstValue ="0";
				}else{
					firstValue=resultStack.pop();
				}
				String tempResult  = calculate(firstValue, secondValue, currentValue.charAt(0));
				resultStack.push(tempResult);
			}
		}
		return new Double(resultStack.pop());
	}
	/**
     * 數據準備階段將表達式轉換成為後綴式棧
     * @param expression
     */
	private void prepare(String expression) {
		opStack.push(',');//運算符放入棧底元素逗號，此符號優先級最低
		char[] arr  = expression.toCharArray();
		int currentIndex  = 0;//當前字符的位置
		int count = 0;//上次算術運算符到本次算術運算符的字符的長度或者之間的數值
		char currentOp  ,peekOp;//當前操作符和棧頂操作符
		for(int i=0;i<arr.length;i++) {
			currentOp = arr[i];
			if(isOperator(currentOp)) {//如果當前字符是運算符
				if(count > 0) {
					postfixStack.push(new String(arr,currentIndex,count));//取兩個運算符之間的數字
				}
				peekOp = opStack.peek();
				if(currentOp == ')') {//遇到反括號則將運算符棧中的元素迻除到後綴式棧中直到遇到左括號
					while(opStack.peek() != '(') {
						postfixStack.push(String.valueOf(opStack.pop()));
					}
					opStack.pop();
				} else {
					while(currentOp != '(' && peekOp != ',' && compare(currentOp,peekOp) ) {
						postfixStack.push(String.valueOf(opStack.pop()));
						peekOp = opStack.peek();
					}
					opStack.push(currentOp);
				}
				count = 0;
				currentIndex = i+1;
			} else {
				count++;
			}
		}
		if(count > 1 || (count == 1 && !isOperator(arr[currentIndex]))) {//最後一個字符不是括號或者其它運算符的則加入後綴式棧中
			postfixStack.push(new String(arr,currentIndex,count));
		} 
		while(opStack.peek() != ',') {
			postfixStack.push(String.valueOf( opStack.pop()));//將操作符棧中的剩餘的元素添加到後綴式棧中
		}
	}
	private boolean isOperator(char c) {
			return c == '+' || c == '-' || c == '*' || c == '/' || c=='^' ||c=='%'
						||c == '>' || c == '<' || c == '=' || c == '&' || c == '|' ||c== '!' 
						|| c == '(' ||c == ')';
	}
	public  boolean compare(char cur,char peek) {//Compare the priority
		if(OP.get(cur)<OP.get(peek)){
			return false;
		}else{
			return true;
		}
	}
	private String calculate(String firstValue,String secondValue,char currentOp) throws NumberFormatException{
		String result  = "";
		switch(currentOp) {
			case '+':
				result = String.valueOf(ArithHelper.add(firstValue, secondValue));
				break;
			case '-':
				result = String.valueOf(ArithHelper.sub(firstValue, secondValue));
				break;
			case '*':
				result = String.valueOf(ArithHelper.mul(firstValue, secondValue));
				break;
			case '/':
				result = String.valueOf(ArithHelper.div(firstValue, secondValue));
				break;
			case '^':
				result = String.valueOf(ArithHelper.ChengFang(firstValue, secondValue));
				break;
			case '%':
				result = String.valueOf(ArithHelper.YuShu(firstValue, secondValue));
				break;
			case '>':
				result = String.valueOf(ArithHelper.dayu(firstValue, secondValue));
				break;
			case '<':
				result = String.valueOf(ArithHelper.xiaoyu(firstValue, secondValue));
				break;
			case '=':
				result = String.valueOf(ArithHelper.dengyu(firstValue, secondValue));
				break;
			case '&':
				result = String.valueOf(ArithHelper.yu(firstValue, secondValue));
				break;
			case '|':
				result = String.valueOf(ArithHelper.huo(firstValue, secondValue));
				break;
			case '!':
				result = String.valueOf(ArithHelper.fei(firstValue, secondValue));
				break;
		}
		return result;
	}
}
class ArithHelper {
	static double d1,d2;
	private ArithHelper(){}
	private static void findValue(String v1, String v2) throws NumberFormatException{
		try{
			d1=new Double(v1);
		}catch(NumberFormatException e){
			d1=new Double(MainActivity.obj.get(v1));
		}
		try{
			d2=new Double(v2);
		}catch(NumberFormatException e){
			d2=new Double(MainActivity.obj.get(v2));
		}
	}
	public static double add(String v1, String v2) throws NumberFormatException{
		double re=0;
		findValue(v1,v2);
		re=d1+d2;
		return re;
	}
	public static double sub(String v1, String v2) throws NumberFormatException{
		double re=0;
		findValue(v1,v2);
		re=d1-d2;
		return re;
	}
	public static double mul(String v1, String v2) throws NumberFormatException{
		double re=0;
		findValue(v1,v2);
		re=d1*d2;
		return re;
	}
	public static double div(String v1, String v2) throws NumberFormatException{
		double re=0;
		findValue(v1,v2);
		re=d1/d2;
		return re;
	}
	public static double ChengFang(String v1, String v2) throws NumberFormatException{
		double re=0;
		findValue(v1,v2);
		re=Math.pow(d1,d2);
		return re;
	}
	public static double YuShu(String v1, String v2) throws NumberFormatException{
		double re=0;
		findValue(v1,v2);
		re=d1%d2;
		return re;
	}
	public static double dayu(String v1, String v2) throws NumberFormatException{
		double re=0;
		findValue(v1,v2);
		if(d1>d2)re=1;
		return re;
	}
	public static double xiaoyu(String v1, String v2) throws NumberFormatException{
		double re=0;
		findValue(v1,v2);
		if(d1<d2)re=1;
		return re;
	}
	public static double dengyu(String v1, String v2) throws NumberFormatException{
		double re=0;
		if(new Calculator().calculate(v1).equals(new Calculator().calculate(v2)))re=1;
		return re;
	}
	public static double yu(String v1, String v2) throws NumberFormatException{
		double re=0;
		findValue(v1,v2);
		re=(int)d1&(int)d2;
		return re;
	}
	public static double huo(String v1, String v2) throws NumberFormatException{
		double re=0;
		findValue(v1,v2);
		re=(int)d1|(int)d2;
		return re;
	}
	public static double fei(String v1, String v2) throws NumberFormatException{
		double re=0;
		findValue(v1,v2);
		re=d1*~(int)d2;
		return re;
	}
}
