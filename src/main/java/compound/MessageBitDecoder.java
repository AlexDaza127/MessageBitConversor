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
 * Clase decodificadora de mensajes encriptados en voltajes
 * 
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
		long inicio = System.currentTimeMillis();
		dataCalculoBits.addAll(bitConversor.dataContentCalculateBits(8, 8, "", false));
		dataCalculoBits.addAll(bitConversor.dataContentCalculateBits(9, 16, "", false));
		dataCalculoBits.addAll(bitConversor.dataContentCalculateBits(10, 32, "", false));

		String contentVoltList = utils.contentFileVoltToDecode();
		try {
			if (contentVoltList != null) {
				uniformQuantifierConversor.messageDecoded(contentVoltList, 10, dataCalculoBits.get(7),
						dataCalculoBits.get(8), 1, 6, 10, "10bits" + formattedDate);
				uniformQuantifierConversor.messageDecoded(contentVoltList, 9, dataCalculoBits.get(4),
						dataCalculoBits.get(5), 1, 5, 9, "9bits" + formattedDate);
				uniformQuantifierConversor.messageDecoded(contentVoltList, 8, dataCalculoBits.get(1),
						dataCalculoBits.get(2), 1, 4, 8, "8bits" + formattedDate);
				long fin = System.currentTimeMillis();
				double tiempoTotal = (double) ((fin - inicio));
				System.out.println("Tiempo total de ejecución: " + tiempoTotal + " ms");
				System.out.println("Se termina el proceso de desencriptación");
				System.out.println("-------------------------------------------------");
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

}
