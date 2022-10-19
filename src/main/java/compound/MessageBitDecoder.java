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
import statisticalstructure.StatisticalStructureMessage;
import utilities.UtilsMessage;

/**
 * Clase decodificadora de mensajes encriptados en voltajes
 * @author Michael
 *
 */
public class MessageBitDecoder {

	/**
	 * Metodo principal de la clase decodificadora
	 * @param args mensaje desencriptado
	 * 
	 */
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		UniformQuantifierConversor uniformQuantifierConversor = new UniformQuantifierConversor();
		StatisticalStructureMessage statisticalStructureMessage = new StatisticalStructureMessage();
		BitConversor bitConversor = new BitConversor();
		UtilsMessage utils = new UtilsMessage();

		List<List<String>> dataCalculoBits = new ArrayList<>();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		LocalDateTime date_of_today = LocalDateTime.now();
		String formattedDate = date_of_today.format(format);
		long inicio = System.currentTimeMillis();
		dataCalculoBits.addAll(bitConversor.dataContentCalculateBits(8, 8, "", false));
		dataCalculoBits.addAll(bitConversor.dataContentCalculateBits(9, 16, "", false));
		dataCalculoBits.addAll(bitConversor.dataContentCalculateBits(10, 32, "", false));

		String contentVoltList = utils.contentFileVoltToDecode();
		statisticalStructureMessage.triggerStatisticalStructure(contentVoltList, "Decoder");
		try {
			if (contentVoltList != null) {
				String separate = " ";
				uniformQuantifierConversor.messageDecoded(contentVoltList, 10, dataCalculoBits.get(7),
						dataCalculoBits.get(8), 1, 6, 10, "10bits" + formattedDate, separate);
				uniformQuantifierConversor.messageDecoded(contentVoltList, 9, dataCalculoBits.get(4),
						dataCalculoBits.get(5), 1, 5, 9, "9bits" + formattedDate, separate);
				uniformQuantifierConversor.messageDecoded(contentVoltList, 8, dataCalculoBits.get(1),
						dataCalculoBits.get(2), 1, 4, 8, "8bits" + formattedDate, separate);
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
