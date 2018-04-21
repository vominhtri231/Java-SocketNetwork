package Calculate;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Calculater {
	public static String calculate(String input)  {
		// 3+5
		ScriptEngineManager mgr = new ScriptEngineManager();
	    ScriptEngine engine = mgr.getEngineByName("JavaScript");
	   
	    try {
			return engine.eval(input).toString();
		} catch (ScriptException e) {
			e.printStackTrace();
			return null;
		}
	
	}
	
	public static double eval(final String str) {
	    return new Object() {
	        int pos = -1, ch;

	        void nextChar() {
	            ch = (++pos < str.length()) ? str.charAt(pos) : -1;
	        }

	        boolean eat(int charToEat) {
	            while (ch == ' ') nextChar();
	            if (ch == charToEat) {
	                nextChar();
	                return true;
	            }
	            return false;
	        }

	        double parse() {
	            nextChar();
	            double x = parseExpression();
	            if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
	            return x;
	        }

	        // Grammar:
	        // expression = term | expression `+` term | expression `-` term
	        // term = factor | term `*` factor | term `/` factor
	        // factor = `+` factor | `-` factor | `(` expression `)`
	        //        | number | functionName factor | factor `^` factor

	        double parseExpression() {
	            double x = parseTerm();
	            for (;;) {
	                if      (eat('+')) x += parseTerm(); // addition
	                else if (eat('-')) x -= parseTerm(); // subtraction
	                else return x;
	            }
	        }

	        double parseTerm() {
	            double x = parseFactor();
	            for (;;) {
	                if      (eat('*')) x *= parseFactor(); // multiplication
	                else if (eat('/')) x /= parseFactor(); // division
	                else return x;
	            }
	        }

	        double parseFactor() {
	            if (eat('+')) return parseFactor(); // unary plus
	            if (eat('-')) return -parseFactor(); // unary minus

	            double x;
	            int startPos = this.pos;
	            if (eat('(')) { // parentheses
	                x = parseExpression();
	                eat(')');
	            } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
	                while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
	                x = Double.parseDouble(str.substring(startPos, this.pos));
	            } else if (ch >= 'a' && ch <= 'z') { // functions
	                while (ch >= 'a' && ch <= 'z') nextChar();
	                String func = str.substring(startPos, this.pos);
	                x = parseFactor();
	                if (func.equals("sqrt")) x = Math.sqrt(x);
	                else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
	                else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
	                else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
	                else throw new RuntimeException("Unknown function: " + func);
	            } else {
	                throw new RuntimeException("Unexpected: " + (char)ch);
	            }

	            if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

	            return x;
	        }
	    }.parse();
	}
	
	public  static double myCalculate(String input) {
		class Helper {
			
			int ch,pos=-1;
			Helper(){
				nextChar();
			}
			
			private void nextChar() {
				pos++;
				if(pos<input.length()) {
					ch=input.charAt(pos);
				}else ch=-1;
			}
			
			private boolean checkNext(char c) {
				while(ch==' ') nextChar();
				if(ch==c) {
					nextChar();
					return true;
				}
				return false;
			}

			//
			double parseExpression(){
				double x=parseTemp();
				while(true) {
					if(checkNext('+')) x+=parseTemp();
					else if(checkNext('-')) x-=parseTemp();
					else return x;
				}
			}

			
			private double parseTemp() {
				double x=parseFactor();
				while(true) {
					if(checkNext('*')) x*=parseTemp();
					else if(checkNext('/')) x/=parseTemp();
					else return x;
				}
			}

			private double parseFactor() {
				double x;
	            int startPos = this.pos;
	            if (checkNext('(')) {
	            	// parentheses
	                x = parseExpression();
	                checkNext(')');
	            } else { 
	            	// numbers
	                while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
	                x = Double.parseDouble(input.substring(startPos, this.pos));
	            }
	            return x;
			}
		}
		return new Helper().parseExpression();
	}
	
	public static void main(String[] args) {
		
		System.out.println(myCalculate("7*(1+0.5)+1.5*(2+5*(1+3*4))"));
	}
}
