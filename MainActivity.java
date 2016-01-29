package com.WYX.Snake;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import android.view.*;
import android.view.View.*;
import android.content.*;

public class MainActivity extends Activity
{
	static TextView t,b;
	static EditText e;
	static TreeMap<String,String> obj;
	static Writer w;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
		t=(TextView)findViewById(R.id.t);
		b=(TextView)findViewById(R.id.b);
		e=(EditText)findViewById(R.id.e);
		TextView b1=(TextView)findViewById(R.id.b1);
		b1.setText("Esc");
		TextView b2=(TextView)findViewById(R.id.b2);
		b2.setText("<<");
		TextView b3=(TextView)findViewById(R.id.b3);
		b3.setText("()");
		TextView b4=(TextView)findViewById(R.id.b4);
		b4.setText("''");
		TextView b5=(TextView)findViewById(R.id.b5);
		b5.setText("=");
		TextView b6=(TextView)findViewById(R.id.b6);
		b6.setText("+");
		TextView b7=(TextView)findViewById(R.id.b7);
		b7.setText("-");
		TextView b8=(TextView)findViewById(R.id.b8);
		b8.setText("/");
		TextView b9=(TextView)findViewById(R.id.b9);
		b9.setText("*");
		TextView b10=(TextView)findViewById(R.id.b10);
		b10.setText(";");
		TextView b11=(TextView)findViewById(R.id.b11);
		b11.setText("&");
		TextView b12=(TextView)findViewById(R.id.b12);
		b12.setText("!");
		TextView b13=(TextView)findViewById(R.id.b13);
		b13.setText("|");
		obj=new TreeMap<String,String>();
		w=new Writer(t);
		t.append("Snake waked up......");
		b.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				String str=e.getText().toString();
				e.setText(null);
				t.append("\nI:");
				str=str.replaceAll("\n","");
				if(str.isEmpty()){
					t.append("nothing");
				}else{
					if(str.endsWith(";")){
						StringBuffer sb=new StringBuffer(str);
						sb.setLength(sb.length()-1);
						str=sb.toString();
					}
				t.append(str.replaceAll(";","\n  "));
				w=null;
				w=new Writer(t);
				w.read(str);
				}
			}
		});
		b1.setOnClickListener(new OnClickListener(){
				public void onClick(View v){
					if(w!=null&&w.alive){
						w.kill();
					}
				}
			});
		b2.setOnClickListener(new OnClickListener(){
				public void onClick(View v){
					StringBuffer sb=new StringBuffer(e.getText().toString());
					int index=e.getSelectionStart();
					sb.insert(index,"<<");
					e.setText(sb.toString());
					e.setSelection(index+2);
				}
			});
		b3.setOnClickListener(new OnClickListener(){
				public void onClick(View v){
					StringBuffer sb=new StringBuffer(e.getText().toString());
					int index=e.getSelectionStart();
					sb.insert(index,"()");
					e.setText(sb.toString());
					e.setSelection(index+1);
				}
			});
		b4.setOnClickListener(new OnClickListener(){
				public void onClick(View v){
					StringBuffer sb=new StringBuffer(e.getText().toString());
					int index=e.getSelectionStart();
					sb.insert(index,"''");
					e.setText(sb.toString());
					e.setSelection(index+1);
				}
			});
		new tv().set(b5);
		new tv().set(b6);
		new tv().set(b7);
		new tv().set(b8);
		new tv().set(b9);
		new tv().set(b10);
		new tv().set(b11);
		new tv().set(b12);
		new tv().set(b13);
    }
	static class Writer{
		TextView jta;
		boolean head,alive;
		String str;
		Writer(TextView jta){
			this.jta=jta;
			this.head=true;
			this.alive=false;
		}
		public void kill(){
			if(alive){this.alive=false;
				say("Stoped!");
			}
		}
		private void say(String str){
			if(head){
				jta.append("\nSnake:"+str);
				head=false;
			}else{
				jta.append("\n            "+str);
			}
		}
		private void out(String str){
			str=str.replace("\\n","\n            ");
			if(head){
				jta.append("\nSnake:"+str);
				head=false;
			}else{
				jta.append(str);
			}
		}
		public static String[] split(String str,String spl){
			String[] s=str.split(spl);
			String[] S=s;
			for(int i=0;i<s.length-1;i++){
				if(s[i].endsWith("\\")){
					s[i]=s[i].replace("\\","")+spl;
					for(int j=i;j<s.length-1;j++){
						s[j]=s[j]+s[j+1];
					}
					S=new String[s.length-1];
					for(int i1=0;i1<S.length;i1++){
						S[i1]=s[i1];
					}
					s=S;
				}
			}
			return S;
		}
		public void read(){
			this.alive=true;
			String[] s=split(str, ";");
			for(int I=0;I<s.length;I++){
				if(!alive){
					break;
				}
				String[] fir=split(s[I],"<<");
				if(fir.length==2){
					if(fir[0].equals("out")){
						try {
							out(new Calculator().calculate(fir[1]));
						} catch (Exception e) {
							say("Calculate failed!"+e.getMessage());
							break;
						}
					}else if(noo(fir[0])){
						try {
							obj.put(fir[0],new Calculator().calculate(fir[1]));
						} catch (NumberFormatException e) {
							say("Calculate failed!");
							break;
						}
					}else{
						say("Bad object name!");
						break;
					}
				}else{
					say("Error!");
					break;
				}
			}
			alive=false;
			head=true;
		}
		public void read(String str){
			this.str=str;
			read();
		}
		private static boolean noo(String name){
			char[] C=name.toCharArray();
			int[] c=new int[C.length];
			for(int i=0;i<c.length;i++){
				c[i]=Character.getNumericValue(C[i]);
			}
			if(c.length!=0){
				if(c[0]>=10&&c[0]<=35){
					for(int i=1;i<c.length;i++){
						if(!(c[i]>=0&&c[i]<=35)){
							return false;
						}
					}return true;				
				}else{
					return false;
				}
			}else{
				return false;
			}
		}
	}
}
class tv{
	String text;
	TextView t=MainActivity.t;
	EditText e=MainActivity.e;
	public void set(TextView tv){
		this.text=tv.getText().toString();
		tv.setOnClickListener(new OnClickListener(){
				public void onClick(View v){
					StringBuffer sb=new StringBuffer(e.getText().toString());
					int index=e.getSelectionStart();
					sb.insert(index,text);
					e.setText(sb.toString());
					e.setSelection(index+1);
				}
			});
	}
}
