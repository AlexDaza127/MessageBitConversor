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

	private DecimalFormat formatoBits = null;;
	String dateNameTxt = "";

	/**
	 * Metodo prinicipal del proces de codificacion de mensajes
	 * 
	 * @param cantBit       cantidad de bit a codificar
	 * @param content       contendio del mensaje a codificar
	 * @param segmentos     contendio de la tabla de segmentos de los bits a
	 *                      codificar
	 * @param intervalos    contendio de la tabla de intervalos de los bits a
	 *                      codificar
	 * @param formattedDate fecha actual para el nombre de archivo
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
	 * @return mensaje en binario creado en un archivo .txt
	 */
	public String messageBinario(int cantBit, String content) {

		char[] charArray = content.toCharArray();
		StringBuilder messageBin = new StringBuilder();
		String cantCaracterBits = "";
		for (int i = 0; i < cantBit; i++) {
			cantCaracterBits += "0";
		}
		formatoBits = new DecimalFormat(cantCaracterBits);

		for (int i = 0; i < charArray.length; i++) {
			String s = String.format("%" + (cantBit - 1) + "s", Integer.toBinaryString((int) charArray[i])).trim();
			if (s.length() < 12) {
				messageBin.append(formatoBits.format(Integer.valueOf(s)) + "|");
			}else {
				System.out.println(s);
			}
		}

		createFileTxt(messageBin.toString(), "MessageBinary", "");
		return messageBin.toString();
	}

	/**
	 * Metodo que permite elegir en que tipo de bits se creara el mensaje codificado
	 * en voltajes
	 * 
	 * @param cantBit       cantidad de bits configurados (8, 9 o 10)
	 * @param content       contenido del mensaje en binario
	 * @param segmentos     contenido de la tabla de segementos de los bits
	 *                      configurados
	 * @param intervalos    contenido de la tabla de intervalos de los bits
	 *                      configurados
	 * @param formattedDate fecha actual para el nombre del archivo
	 * @return mensaje codificado en voltajes en un archivo .txt
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
	 * Metodo que crear el mensaje en voltios dependiendo de los bits configurados
	 * 
	 * @param cantBit    cantidad de bits (8, 9 o 10)
	 * @param content    contenido del mensaje (mensaje en binario)
	 * @param segmentos  contenido de la tabla de segmentos
	 * @param intervalos contendio de la tabla de intervalos
	 * @param signo      signo que depende de los bits configurado
	 * @param cantSegm   cantidad de segmentos de los bits configurados
	 * @param cantInterv cantidad de intervalos de los bits configurados
	 * @return mensaje codificado en voltajes
	 */
	public void createMessageVoltage(int cantBit, String content, List<String> segmentos, List<String> intervalos,
			int signo, int cantSegm, int cantInterv, String strDescBit) {
		StringBuilder rangoVoltajes = new StringBuilder();
		rangoVoltajes.delete(0, rangoVoltajes.length());
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

		createFileTxt(rangoVoltajes.toString(), "MessageVoltaje", "");
		if (strDescBit.equals("Y")) {
			messageDecoded(rangoVoltajes.toString(), cantBit, segmentos, intervalos, signo, cantSegm, cantInterv,
					dateNameTxt);
		}
	}

	public void messageDecoded(String contentVoltage, int cantBit, List<String> segmentos, List<String> intervalos,
			int signo, int cantSegm, int cantInterv, String formattedDate) {
		dateNameTxt = formattedDate;
		StringBuilder wordVoltValue = new StringBuilder();
		String[] voltList = contentVoltage.split(" ");
		String messageDecode = "ninguno";

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

			wordVoltValue.append(signValue);
			wordVoltValue.append("|" + String.valueOf((int) segmValue) + "|");
			wordVoltValue.append(String.valueOf((int) interValue) + " ");

		}

		messageDecode = cantBit + "bits";
		if (cantBit == 8) {
			decimalToBinario(wordVoltValue.toString(), 3, 4, messageDecode);
		} else if (cantBit == 9) {
			decimalToBinario(wordVoltValue.toString(), 4, 4, messageDecode);
		} else {
			decimalToBinario(wordVoltValue.toString(), 5, 4, messageDecode);
		}

	}

	/**
	 * Este metodo convierte los valor de voltaje hechos en decimal a binario
	 * decodificado
	 * 
	 * @param wordVoltValue contenido del mensaje de palabras en voltajes
	 * @param cantSegmBin   cantidad de segmentos que se toman del numero en binario
	 *                      por bit procesado
	 * @param cantIntervBin cantidad de intervalos que se toman del numero en
	 *                      binario por bit procesado
	 */
	public void decimalToBinario(String wordVoltValue, int cantSegmBin, int cantIntervBin, String blindSearch) {

		String[] contValue = wordVoltValue.split(" ");
		StringBuilder wordBinValue = new StringBuilder();

		for (String contDec : contValue) {
			String[] contDecValue = contDec.split("\\|");
			String segmBin = String
					.format("%" + cantSegmBin + "s", Integer.toBinaryString(Integer.valueOf(contDecValue[1])))
					.replace(" ", "0");
			String interBin = String
					.format("%" + cantIntervBin + "s", Integer.toBinaryString(Integer.valueOf(contDecValue[2])))
					.replace(" ", "0");

			wordBinValue.append(contDecValue[0] + segmBin + interBin + "|"); // mensaje de decimal a binario
		}
		binarioToMessageDecodificade(wordBinValue.toString(), blindSearch);
	}

	/**
	 * Metodo para decodificar el contenido del mensaje de binario a palabras
	 * 
	 * @param binContent contenido del mensaje en binario
	 */
	public void binarioToMessageDecodificade(String binContent, String blindSearch) {

		String[] contBinValue = binContent.split("\\|");
		StringBuilder wordDecod = new StringBuilder();
		for (String binValue : contBinValue) {

			int binValueInt = Integer.parseInt(binValue, 2);
			wordDecod.append(String.valueOf(Character.toChars(Integer.parseInt(String.valueOf(binValueInt), 10))));
		}
		createFileTxt(wordDecod.toString(), "MessageDecod", blindSearch);// Se crea el mensaje decodificado
	}

	/**
	 * Metodo que crea los archivos en las carpetas espeficas de cada tipo de
	 * tratamiendo de datos
	 * 
	 * @param content contenido del mensaje, sea en voltaje, binario o palabras
	 * @param type    tipo de mensaje, sea voltaje, binario o palabras
	 * @param blindSearch identifica si la decodificacion es a ciegas o no
	 */
	public void createFileTxt(String content, String type, String blindSearch) {
		UtilsMessage utils = new UtilsMessage();

		Pattern pat = Pattern.compile("[A-Za-z0-9, ñÑiíaáeéoóuú.-?]+");
		Matcher mat = pat.matcher(content.substring(0, 20));

		if (mat.matches() || blindSearch.isEmpty()) {
			utils.createFiles(content, type, dateNameTxt);
		}
	}

}
