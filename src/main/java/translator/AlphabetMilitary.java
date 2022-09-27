package translator;

import java.util.HashMap;
import java.util.Map;

public class AlphabetMilitary {

	public String latinoToMilitary(String content) {
		
		char[] charArray = content.toCharArray();
		StringBuilder messageMilitary = new StringBuilder();
		Map<String,String> data = dataMilitary();
		for (int i = 0; i < charArray.length; i++) {
			String s = String.valueOf(charArray[i]);
			if(data.containsKey(s.toUpperCase())) {
			  String ss = data.get(s.toUpperCase());
			  messageMilitary.append(ss);
			}else {
				System.out.println(s.toUpperCase());
			  messageMilitary.append(s);
			}
			
		}
		
		
		return messageMilitary.toString();
	}
	
	public Map<String,String> dataMilitary(){
		Map<String,String> data = new HashMap<>();
		
		data.put("A", "ALPHA");
		data.put("B", "BRAVO");
		data.put("C", "CHARLIE");
		data.put("D", "DELTA");
		data.put("E", "ECHO");
		data.put("F", "FOXTROT");
		data.put("G", "GOLF");
		data.put("H", "HOTEL");
		data.put("I", "INDIA");
		data.put("J", "JULIET");
		data.put("K", "KILO");
		data.put("L", "LIMA");
		data.put("M", "MIKE");
		data.put("N", "NOVEMBER");
		data.put("O", "OSCAR");
		data.put("P", "PAPA");
		data.put("Q", "QUEBEC");
		data.put("R", "ROMEO");
		data.put("S", "SIERRA");
		data.put("T", "TANGO");
		data.put("U", "UNIFORM");
		data.put("V", "VICTOR");
		data.put("W", "WHISKEY");
		data.put("X", "XRAY");
		data.put("Y", "YANKEE");
		data.put("Z", "ZULU");
		
		return data;
	}
	
}
