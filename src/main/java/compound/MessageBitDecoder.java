/**
 * 
 */
package compound;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import bitconvesor.BitConversor;
import bitconvesor.UniformQuantifierConversor;
import utilities.UtilsMessage;

/**
 * @author Michael
 *
 */
public class MessageBitDecoder {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BitConversor bitConversor = new BitConversor();
		UtilsMessage utils = new UtilsMessage();
		UniformQuantifierConversor uniformQuantifierConversor = new UniformQuantifierConversor();
		List<List<String>> dataCalculoBits = new ArrayList<>();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		LocalDateTime date_of_today = LocalDateTime.now();
		String formattedDate = date_of_today.format(format);
		
		dataCalculoBits.addAll(bitConversor.dataContentCalculateBits(8, 8, "", false));
		dataCalculoBits.addAll(bitConversor.dataContentCalculateBits(9, 16, "", false));
		dataCalculoBits.addAll(bitConversor.dataContentCalculateBits(10, 32, "", false));
		
		List<String> contentVoltList = utils.contentFileVoltToDecode();
		try {
			if (contentVoltList != null) {
				
				uniformQuantifierConversor.messageDecoded(contentVoltList.get(0), 10, dataCalculoBits.get(7), dataCalculoBits.get(8), 1, 6, 10,"10bits "+formattedDate);
				uniformQuantifierConversor.messageDecoded(contentVoltList.get(0), 9, dataCalculoBits.get(4), dataCalculoBits.get(5), 1, 5, 9,"9bits "+formattedDate);
				uniformQuantifierConversor.messageDecoded(contentVoltList.get(0), 8, dataCalculoBits.get(1), dataCalculoBits.get(2), 1, 4, 8,"8bits "+formattedDate);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
	}	

	}

}
