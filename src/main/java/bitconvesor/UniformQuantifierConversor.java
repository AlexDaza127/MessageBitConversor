package bitconvesor;

import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utilities.UtilsMessage;

/**
 * Esta clase permite realizar las conversiones en binario, voltajes y volver a
 * decodificar el contendio del mensaje dependiendo de los bits configurados (8,
 * 9 o 10 bits).
 * 
 * @author Michael
 */
public class UniformQuantifierConversor {

	private DecimalFormat formatoBits = null;
	private String dateNameTxt = "";

	/**
	 * Metodo prinicipal del proceso de codificacion de mensajes
	 * 
	 * @param cantBit       cantidad de bit a codificar
	 * @param content       contendio del mensaje a codificar
	 * @param segmentos     contendio de la tabla de segmentos de los bits a
	 *                      codificar
	 * @param intervalos    contendio de la tabla de intervalos de los bits a
	 *                      codificar
	 * @param formattedDate fecha actual para el nombre de archivo
	 * @param strDescBit    indica si se va a decodificar el mensaje
	 */
	public void messageEncoder(int cantBit, String content, List<String> segmentos, List<String> intervalos,
			String formattedDate, String strDescBit) {
		dateNameTxt = formattedDate;
		String messageBin = messageBinario(cantBit, content);
		messageVoltage(cantBit, messageBin, segmentos, intervalos, strDescBit);
	}

	/**
	 * Metodo que convierte el contendio del mensaje en binario dependendiendo de la
	 * configuracion de los bits
	 * 
	 * @param cantBit cantidad de bits configurados (8, 9 o 10)
	 * @param content contenido del mensaje a convertir en binario
	 * @return mensaje en binario creado
	 */
	public String messageBinario(int cantBit, String content) {

		char[] charArray = content.toCharArray();
		StringBuilder messageBin = new StringBuilder();
		String cantCaracterBits = "";

		for (int i = 0; i < cantBit; i++) {
			cantCaracterBits += "0";
		}
		formatoBits = new DecimalFormat(cantCaracterBits);

		try {
			int charL = charArray.length;
			for (int i = 0; i < charL; i++) {
				String s = String.format("%" + (cantBit - 1) + "s", Integer.toBinaryString((int) charArray[i])).trim();
				if (s.length() < 12) {
					messageBin.append(formatoBits.format(Integer.valueOf(s)) + "|");
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		createFileTxt(messageBin.toString(), "MessageBinary");
		return messageBin.toString();
	}

	/**
	 * Metodo que permite elegir en que tipo de bits se creara el mensaje codificado
	 * en voltajes
	 * 
	 * @param cantBit    cantidad de bits configurados (8, 9 o 10)
	 * @param content    contenido del mensaje en binario
	 * @param segmentos  contenido de la tabla de segementos de los bits
	 *                   configurados
	 * @param intervalos contenido de la tabla de intervalos de los bits
	 *                   configurados
	 * @param strDescBit indica si se va a decodificar el mensaje *
	 * @return mensaje codificado en voltajes
	 */
	public void messageVoltage(int cantBit, String content, List<String> segmentos, List<String> intervalos,
			String strDescBit) {
		if (cantBit == 8) {
			createMessageVoltage(cantBit, content, segmentos, intervalos, 1, 4, 8, strDescBit);
		} else if (cantBit == 9) {
			createMessageVoltage(cantBit, content, segmentos, intervalos, 1, 5, 9, strDescBit);
		} else if (cantBit == 10) {
			createMessageVoltage(cantBit, content, segmentos, intervalos, 1, 6, 10, strDescBit);
		}
	}

	/**
	 * Metodo que permite calcular el mensaje en voltios dependiendo de los bits
	 * configurados
	 * 
	 * @param cantBit    cantidad de bits (8, 9 o 10)
	 * @param content    contenido del mensaje (mensaje en binario)
	 * @param segmentos  contenido de la tabla de segmentos
	 * @param intervalos contendio de la tabla de intervalos
	 * @param signo      signo que depende de los bits configurado
	 * @param cantSegm   cantidad de segmentos de los bits configurados
	 * @param cantInterv cantidad de intervalos de los bits configurados
	 * @param strDescBit indica si se va a decodificar el mensaje 
	 * @return calculos del mensaje codificado en voltajes
	 */
	public void createMessageVoltage(int cantBit, String content, List<String> segmentos, List<String> intervalos,
			int signo, int cantSegm, int cantInterv, String strDescBit) {
		StringBuilder rangoVoltajes = new StringBuilder();
		String[] dataBinary = content.split("\\|");
		String signoDataDec = "";
		String segmentosDataDec = "";
		String intervaloDataDec = "";
		double segMin = 0;
		double interMin = 0;
		double valorVoltaje = 0;

		for (String data : dataBinary) {
			signoDataDec = String.valueOf(Integer.parseInt(data.substring(0, signo), 2)).equals("0") ? "+" : "-";
			segmentosDataDec = String.valueOf(Integer.parseInt(data.substring(signo, cantSegm), 2));
			intervaloDataDec = String.valueOf(Integer.parseInt(data.substring(cantSegm, cantInterv), 2));
			for (String seg : segmentos) {
				String[] cantSeg = seg.split("\\|");
				if (segmentosDataDec.equals(cantSeg[0])) {
					segMin = Double.valueOf(cantSeg[1].replace(",", "."));
					break;
				}
			}

			for (String inter : intervalos) {
				String[] cantInter = inter.split("\\|");
				if (intervaloDataDec.equals(cantInter[0])) {
					interMin = Double.valueOf(cantInter[1].replace(",", "."));
					break;
				}
			}
			valorVoltaje = Math.abs((segMin + interMin) + 0.5);
			rangoVoltajes.append(signoDataDec + String.valueOf(valorVoltaje) + " ");
		}

		createFileTxt(rangoVoltajes.toString(), "MessageVoltaje");
		if (strDescBit.equals("Y")) {
			messageDecoded(rangoVoltajes.toString(), cantBit, segmentos, intervalos, signo, cantSegm, cantInterv,
					dateNameTxt, " ");
		}
	}

	/**
	 * Metodo decodificador del mensaje encriptado, sea preconfigurado los bits o en busqueda a ciegas
	 * @param contentVoltage contendio del mensaje encriptado en voltajes con separacion de espacios
	 * @param cantBit cantidad de bits a decodificar
	 * @param segmentos datos de la tabla de segmentos
	 * @param intervalos datos de la tabla de intervalos
	 * @param signo indicar de signos de voltaje (0 = + | 1 = -)
	 * @param cantSegm cantidad de segmentos por configuración de bits decodificadores
	 * @param cantInterv cantidad de intervalos por configuración de bits decodificadores
	 * @param formattedDate fecha actual para la generacion del archivo .txt con el contenido decodificado
	 * @param separateVolt caracter de separacion del archivo encriptado
	 */
	public void messageDecoded(String contentVoltage, int cantBit, List<String> segmentos, List<String> intervalos,
			int signo, int cantSegm, int cantInterv, String formattedDate, String separateVolt) {
		dateNameTxt = formattedDate;
		StringBuilder wordVoltValue = new StringBuilder();
		
		String[] voltList = contentVoltage.split(separateVolt);

		for (String volt : voltList) {

			String signValue = (volt.substring(0, signo)).equals("+") ? "0" : "1";
			double voltValue = Double.valueOf((volt.substring(signo, volt.length())).replace(",", "."));
			double segmValue = 0;
			double interValue = 0;

			for (String segmList : segmentos) {
				String[] segm = segmList.split("\\|");
				double segmRangeMin = Double.valueOf(segm[1].replace(",", "."));
				double segmRangeMax = Double.valueOf(segm[2].replace(",", "."));
				if (segmRangeMin < voltValue && segmRangeMax > voltValue) {
					interValue = voltValue - segmRangeMin;
					segmValue = Double.valueOf(segm[0]);
					break;
				}
			}

			for (String interList : intervalos) {
				String[] inter = interList.split("\\|");
				double interRangeMin = Double.valueOf(inter[1].replace(",", "."));
				double interRangeMax = Double.valueOf(inter[2].replace(",", "."));
				if (interRangeMin <= interValue && interRangeMax >= interValue) {
					interValue = Double.valueOf(inter[0]);
					break;
				}
			}

			wordVoltValue.append(
					signValue + "|" + String.valueOf((int) segmValue) + "|" + String.valueOf((int) interValue) + " ");
		}

		if (cantBit == 8) {
			decimalToBinario(wordVoltValue.toString(), 3, 4);
		} else if (cantBit == 9) {
			decimalToBinario(wordVoltValue.toString(), 4, 4);
		} else {
			decimalToBinario(wordVoltValue.toString(), 5, 4);
		}

	}

	/**
	 * Método que decodifica los valor de voltaje hechos en decimal a binario
	 * 	 * 
	 * @param wordVoltValue contenido del mensaje de palabras en voltajes
	 * @param cantSegmBin   cantidad de segmentos que se toman del numero en binario
	 *                      por bit procesado
	 * @param cantIntervBin cantidad de intervalos que se toman del numero en
	 *                      binario por bit procesado
	 */
	public void decimalToBinario(String wordVoltValue, int cantSegmBin, int cantIntervBin) {
		StringBuilder wordBinValue = new StringBuilder();
		String[] contValue = wordVoltValue.split(" ");

		for (String contDec : contValue) {
			String[] contDecValue = contDec.split("\\|");
			String segmBin = String
					.format("%" + cantSegmBin + "s", Integer.toBinaryString(Integer.valueOf(contDecValue[1])))
					.replace(" ", "0");
			String interBin = String
					.format("%" + cantIntervBin + "s", Integer.toBinaryString(Integer.valueOf(contDecValue[2])))
					.replace(" ", "0");

			wordBinValue.append(contDecValue[0] + segmBin + interBin + "|");
		}
		binarioToMessageDecodificade(wordBinValue.toString());
	}

	/**
	 * Metodo para decodificar el contenido del mensaje de binario a palabras
	 * 
	 * @param binContent contenido del mensaje en binario
	 */
	public void binarioToMessageDecodificade(String binContent) {

		String[] contBinValue = binContent.split("\\|");
		StringBuilder wordDecod = new StringBuilder();
		for (String binValue : contBinValue) {

			int binValueInt = Integer.parseInt(binValue, 2);
			wordDecod.append(String.valueOf(Character.toChars(Integer.parseInt(String.valueOf(binValueInt), 10))));
		}
		createFileTxt(wordDecod.toString(), "MessageDecod");// Se crea el mensaje decodificado
	}

	/**
	 * Metodo que crea los archivos en las carpetas espeficas de cada tipo de
	 * tratamiendo de datos
	 * 
	 * @param content     contenido del mensaje, sea en voltaje, binario o palabras
	 * @param type        tipo de mensaje, sea voltaje, binario o palabras
	 */
	public void createFileTxt(String content, String type) {
		UtilsMessage utils = new UtilsMessage();

		Pattern pat = Pattern.compile("[A-Za-z0-9, ñÑiíaáeéoóuú.-?+|]+");
		Matcher mat = pat.matcher(content.substring(0, 20));

		if (mat.matches()) {
			utils.createFiles(content, type, dateNameTxt);
		}
	}

}
