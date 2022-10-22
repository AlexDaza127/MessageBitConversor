package statisticalstructure;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import utilities.UtilsMessage;

/**
 * Clase que calcula al estadistica estructural del contendio de los mensajes
 * 
 * @author Michael
 *
 */
public class StatisticalStructureMessage {

	private double contTotalSymbol = 0;
	private double contTotalProbSymbol = 0;

	/**
	 * Método principal de la clase, aqui se genera el archivo consolidado en la
	 * tabla de estadistica estructural
	 * 
	 * @param content contenido del mensaje
	 * @param type    tipo de estadistica, sea para el contendio del encriptado o
	 *                desencriptado
	 */
	public void triggerStatisticalStructure(String content, String type) {
		Map<String, Double> frequencySymbol = new HashMap<>();
		Map<String, Double> probabilitySymbol = new HashMap<>();
		Map<String, Double> frequencyBitsSymbol = new HashMap<>();
		Map<String, Double> expectedValueSymbol = new HashMap<>();
		StringBuilder processData = new StringBuilder();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		LocalDateTime date_of_today = LocalDateTime.now();
		String formattedDate = date_of_today.format(format);
		DecimalFormat df = new DecimalFormat("##0.0000");

		if (type.equals("Encoder")) {
			frequencySymbol.putAll(frecuencyEncoder(content));
		} else if (type.equals("Decoder")) {
			frequencySymbol.putAll(frecuencyDecoder(content));
		}

		probabilitySymbol.putAll(probability(frequencySymbol, contTotalSymbol));
		frequencyBitsSymbol.putAll(frequencyBits(probabilitySymbol));
		expectedValueSymbol.putAll(expectedValue(probabilitySymbol, frequencyBitsSymbol));

		processData.append("Simbolo\t|Frecuencia\t|Probabilidad\t|Frecuencia Bits\t|Esperanza\r\n");

		List<String> orderAlphabet = new ArrayList<>();
		List<Double> orderProb = new ArrayList<>();

		for (String f : frequencySymbol.keySet()) {
			orderAlphabet.add(f);
		}

		for (Double f : probabilitySymbol.values()) {
			orderProb.add(f);
		}

		Collections.sort(orderAlphabet);

		for (String f : orderAlphabet) {
			processData.append(f + "\t\t|" + frequencySymbol.get(f) + "\t\t|" + df.format(probabilitySymbol.get(f))
					+ "\t\t\t|" + df.format(frequencyBitsSymbol.get(f)) + "\t\t\t\t|"
					+ df.format(expectedValueSymbol.get(f)) + "\r\n");
		}
		processData.append(
				"\r\n\r\n\r\ncantidad total frecuencia de simbolos contados = " + (int) contTotalSymbol + "\r\n");
		processData.append("cantidad total probabilidad de simbolos contados = "
				+ df.format(contTotalProbSymbol).replace(",", "."));

		Collections.sort(orderProb);
		Collections.reverse(orderProb);
		processData.append("\r\nOrden de mayor a menor frecuencia de aparición obtenida: \r\n");

		String getKeyFromValue = "";
		for (Double f : orderProb) {
			getKeyFromValue += getSingleKeyFromValue(probabilitySymbol, f) + "|";
		}
		processData.append(getKeyFromValue);

		UtilsMessage utilMessage = new UtilsMessage();
		utilMessage.createFiles(processData.toString(), "MessageStatistical", formattedDate);

	}

	/**
	 * Organiza las llaves de los hashmap, así se obtiene el simbolo que mas aparece
	 * del mayor a menor en el contenido del mensaje
	 * 
	 * @param <K>   key
	 * @param <V>   value
	 * @param map   hashmap
	 * @param value valor
	 * @return key al que pertenece el valor buscado
	 */
	public static <K, V> K getSingleKeyFromValue(Map<K, V> map, V value) {
		for (Map.Entry<K, V> entry : map.entrySet()) {
			if (Objects.equals(value, entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}

	/**
	 * Frecuencia con la que aparece en el contendio un simbolo en un mensaje
	 * encriptado
	 * 
	 * @param contentMessage contenido del mensaje
	 * @return frequencySymbol columna de frecuencias
	 */
	public Map<String, Double> frecuencyDecoder(String contentMessage) {
		Map<String, Double> frequencySymbol = new HashMap<>();
		Map<String, String> orderSymbol = new HashMap<>();
		LocalDateTime date_of_today = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		String formattedDate = date_of_today.format(format);
		String[] contentSplit = contentMessage.split(" ");
		contTotalSymbol = 0;

		for (int i = 0; i < contentSplit.length; i++) {
			
			String symbolString = contentSplit[i];
				if (frequencySymbol.containsKey(symbolString)) {
					double cont = frequencySymbol.get(symbolString) + 1;
					frequencySymbol.put(symbolString, cont);
					orderSymbol.put(symbolString, orderSymbol.get(symbolString) + ";" + i);
				} else {
					frequencySymbol.put(symbolString, 1.0);
					orderSymbol.put(symbolString, String.valueOf(i));
				}
		}

		for (String f : frequencySymbol.keySet()) {
			contTotalSymbol += frequencySymbol.get(f);
		}		
		
		StringBuilder trama = new StringBuilder();
		trama.append("Simbolo\t|Frecuencia\t|Orden de aparición\r\n");			
		for (String k : orderSymbol.keySet()) {
			trama.append(k + "\t|" + frequencySymbol.get(k) +"\t|" + orderSymbol.get(k)+"\r\n");
		}
		
		UtilsMessage utilMessage = new UtilsMessage();
		utilMessage.createFiles(trama.toString(), "MessageTramaDecode", formattedDate);

		return frequencySymbol;
	}
	
	public Map<String, String> dataSpecial() {
		Map<String, String> specialChar = new HashMap<>();
		specialChar.put("+250.5", "space");
		specialChar.put("+125.5", "space");
		specialChar.put("+63.0", "space");
		specialChar.put("+102.0625", "return");
		specialChar.put("+39.5625", "return");
		specialChar.put("+20.0312", "return");
		specialChar.put("+78.625", "new line");
		specialChar.put("+51.2812", "new line");
		specialChar.put("+25.8906", "new line");
		return null;
	}

	/**
	 * Frecuencia con la que aparece en el contenido un simbolo en texto
	 * 
	 * @param contentMessage contenido del mensaje
	 * @return frequencySymbol columna de frecuencias
	 */
	public Map<String, Double> frecuencyEncoder(String contentMessage) {
		contTotalSymbol = 0;
		Map<String, Double> frequencySymbol = new HashMap<>();
		Map<String, String> orderSymbol = new HashMap<>();
		LocalDateTime date_of_today = LocalDateTime.now();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		String formattedDate = date_of_today.format(format);
		for (int i = 0; i < contentMessage.length(); i++) {
			char letter = contentMessage.charAt(i);
			String symbolString = String.valueOf(letter).equals(" ") ? "space" : 
				String.valueOf(letter).equals("\r") ? "return" : String.valueOf(letter).equals("\n") ? "new line" : String.valueOf(letter);

				if (frequencySymbol.containsKey(symbolString)) {
					double cont = frequencySymbol.get(symbolString) + 1;
					frequencySymbol.put(symbolString, cont);
					orderSymbol.put(symbolString, orderSymbol.get(symbolString) + ";" + i);
				} else {
					frequencySymbol.put(symbolString, 1.0);
					orderSymbol.put(symbolString, String.valueOf(i));
				}
		}

		for (String f : frequencySymbol.keySet()) {
			contTotalSymbol += frequencySymbol.get(f);
		}
		
		StringBuilder trama = new StringBuilder();
		trama.append("Simbolo\t|Frecuencia\t|Orden de aparición\r\n");			
		for (String k : orderSymbol.keySet()) {
			trama.append(k + "\t|" + frequencySymbol.get(k) +"\t|" + orderSymbol.get(k)+"\r\n");
		}
		
		UtilsMessage utilMessage = new UtilsMessage();
		utilMessage.createFiles(trama.toString(), "MessageTramaEncode", formattedDate);
		

		return frequencySymbol;
	}
	
	
	//TODO ya se hizo la trama, ahora falta es tomar esa trama y convertirla en texto dependiendo de la posicion que dice cada simbolo

	/**
	 * Porcentaje de probabilidad de aparición de los simbolos
	 * 
	 * @param frequencySymbol frecuencia de los simbolos
	 * @param contTotalSymbol conteo total de simbolos
	 * @return probabilitySymbol columna de porcentaje de probabilidad de aparición
	 */
	public Map<String, Double> probability(Map<String, Double> frequencySymbol, double contTotalSymbol) {

		Map<String, Double> probabilitySymbol = new HashMap<>();
		probabilitySymbol.putAll(frequencySymbol);
		DecimalFormat df = new DecimalFormat("##0.0000");

		for (String f : frequencySymbol.keySet()) {
			double probPercent = frequencySymbol.get(f) / contTotalSymbol;
			probabilitySymbol.put(f, Double.valueOf(df.format(probPercent).replace(",", ".")));
		}

		for (String f : probabilitySymbol.keySet()) {
			contTotalProbSymbol += probabilitySymbol.get(f);
		}

		return probabilitySymbol;
	}

	/**
	 * Frecuencia en bits de aparición de los simbolos
	 * 
	 * @param probabilitySymbol porcentaje de probabilidad de aparición de los
	 *                          simbolos
	 * @return frequencyBitsSymbol columna de frecuencias en bits
	 */
	public Map<String, Double> frequencyBits(Map<String, Double> probabilitySymbol) {
		Map<String, Double> frequencyBitsSymbol = new HashMap<>();
		frequencyBitsSymbol.putAll(probabilitySymbol);
		DecimalFormat df = new DecimalFormat("##0.0000");

		for (String f : probabilitySymbol.keySet()) {
			double freqBit = (Math.log(1 / probabilitySymbol.get(f)) / Math.log(2));
			frequencyBitsSymbol.put(f, Double.valueOf(df.format(freqBit).replace(",", ".")));
		}

		return frequencyBitsSymbol;
	}

	/**
	 * Valor esperado (Esperanza) de aparición de los simbolos
	 * 
	 * @param probabilitySymbol   porcentaje de probabilidad de aparición de los
	 *                            simbolos
	 * @param frequencyBitsSymbol frecuencias en bits de aparición de los simbolos
	 * @return expectedValueSymbol columna de valores esperados
	 */
	public Map<String, Double> expectedValue(Map<String, Double> probabilitySymbol,
			Map<String, Double> frequencyBitsSymbol) {
		Map<String, Double> expectedValueSymbol = new HashMap<>();

		expectedValueSymbol.putAll(probabilitySymbol);
		DecimalFormat df = new DecimalFormat("##0.0000");

		for (String f : expectedValueSymbol.keySet()) {
			double expVlue = probabilitySymbol.get(f) * frequencyBitsSymbol.get(f);
			expectedValueSymbol.put(f, Double.valueOf(df.format(expVlue).replace(",", ".")));
		}

		return expectedValueSymbol;
	}

}
