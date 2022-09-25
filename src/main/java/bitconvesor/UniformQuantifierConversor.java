package bitconvesor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import utilities.UtilsMessage;

/**
 * Esta clase permite realizar las conversiones en binario, voltajes y volver a
 * decodificar el contendio del mensaje dependiendo de los bits configurados (8,
 * 9 o 10 bits).
 */
public class UniformQuantifierConversor {

	private DecimalFormat formatoBits = null;;
	String dateNameTxt = "";

	/**
	 * Metodo que convierte el contendio del mensaje en binario dependendiendo de la
	 * configuracion de los bits
	 * 
	 * @param cantBit cantidad de bits configurados (8, 9 o 10)
	 * @param content contenido del mensaje a convertir en binario
	 * @return mensaje en binario creado en un archivo .txt
	 */
	public String messageBinario(int cantBit, String content,String formattedDate) {
		dateNameTxt = formattedDate;
		char[] charArray = content.toCharArray();
		StringBuilder palabra = new StringBuilder();
		String cantCaracterBits = "";
		for (int i = 0; i < cantBit; i++) {
			cantCaracterBits += "0";
		}
		formatoBits = new DecimalFormat(cantCaracterBits);

		for (int i = 0; i < charArray.length; i++) {

			// System.out.println("(int) charArray[i]) = " + ((int) charArray[i]));// TODO

			String s = String.format("%" + (cantBit - 1) + "s", Integer.toBinaryString((int) charArray[i])).trim();
			if (s.length() < 12) {
				if (i != charArray.length - 1) {
					palabra.append(formatoBits.format(Integer.valueOf(s)) + "|");
				} else {
					palabra.append(formatoBits.format(Integer.valueOf(s)));
				}
			}

		}

		// System.out.println("1 palabra.toString() = " + palabra.toString());
		createFileTxt(palabra.toString(), "MessageBinary");
		return palabra.toString();
	}

	/**
	 * Metodo que permite elegir en que tipo de bits se creara el mensaje codificado
	 * en voltajes
	 * 
	 * @param cantBit        cantidad de bits configurados (8, 9 o 10)
	 * @param content        contenido del mensaje en binario
	 * @param segmentos      contenido de la tabla de segementos de los bits
	 *                       configurados
	 * @param intervalos     contenido de la tabla de intervalos de los bits
	 *                       configurados
	 * @param formattedDate2
	 * 
	 * @return mensaje codificado en voltajes en un archivo .txt
	 */
	public void messageVoltage(int cantBit, String content, List<String> segmentos, List<String> intervalos,
			String formattedDate) {
		dateNameTxt = formattedDate;
		StringBuilder rangoVoltajes = new StringBuilder();
		rangoVoltajes.delete(0, rangoVoltajes.length());
		// System.out.println("-------------------------------");
		if (cantBit == 8) {
			rangoVoltajes.append(createMessageVoltage(cantBit, content, segmentos, intervalos, 1, 4, 8));
		} else if (cantBit == 9) {
			rangoVoltajes.append(createMessageVoltage(cantBit, content, segmentos, intervalos, 1, 5, 9));
		} else if (cantBit == 10) {
			rangoVoltajes.append(createMessageVoltage(cantBit, content, segmentos, intervalos, 1, 6, 10));
		}
		createFileTxt(rangoVoltajes.toString(), "MessageVoltaje");
		System.out.println("Se termina el proceso");
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
	public StringBuilder createMessageVoltage(int cantBit, String content, List<String> segmentos,
			List<String> intervalos, int signo, int cantSegm, int cantInterv) {
		StringBuilder rangoVoltajes = new StringBuilder();
		rangoVoltajes.delete(0, rangoVoltajes.length());
		String[] dataBinary = content.split("\\|");
		String signoDataDec = "";
		String segmentosDataDec = "";
		String intervaloDataDec = "";
		List<String> datosVoltajes = new ArrayList<>();

		for (String data : dataBinary) {
			// String signoData = data.substring(0, signo);
			signoDataDec = String.valueOf(Integer.parseInt(data.substring(0, signo), 2)).equals("0") ? "+" : "-";
			// String segmentosData = data.substring(signo, cantSegm);
			segmentosDataDec = String.valueOf(Integer.parseInt(data.substring(signo, cantSegm), 2));
			// String intervaloData = data.substring(cantSegm, cantInterv);
			intervaloDataDec = String.valueOf(Integer.parseInt(data.substring(cantSegm, cantInterv), 2));
			datosVoltajes.add(signoDataDec + "|" + segmentosDataDec + "|" + intervaloDataDec);
			/*
			 * System.out.println("signoData = " + signoData);
			 * System.out.println("segmentosData = " + segmentosData);
			 * System.out.println("intervaloData = " + intervaloData);
			 * System.out.println("numero = " + signoDataDec + "|" + segmentosDataDec + "|"
			 * + intervaloDataDec);
			 */
		}
		// System.out.println("-------------------------------");

		double segMin = 0;
		double interMin = 0;
		double interMax = 0;
		double valorVoltaje = 0;

		for (String volts : datosVoltajes) {
			String[] cantVolts = volts.split("\\|");
			// System.out.println("volts = " + volts);
			for (String seg : segmentos) {
				String[] cantSeg = seg.split("\\|");
				if (cantVolts[1].equals(cantSeg[0])) {
					segMin = Double.valueOf(cantSeg[1].replace(",", "."));
				}
			}

			for (String inter : intervalos) {
				String[] cantInter = inter.split("\\|");
				if (cantVolts[2].equals(cantInter[0])) {
					// System.out.println("inter = " + inter);
					interMin = Double.valueOf(cantInter[1].replace(",", "."));
					interMax = Double.valueOf(cantInter[2].replace(",", "."));
					break;
				}
			}
			// System.out.println("-------------------------------");
			// System.out.println("segMin = " + segMin);
			// System.out.println("interMin = " + interMin);

			valorVoltaje = Math.abs((segMin + interMin) + 0.5);
			// System.out.println("valorVoltaje = " + valorVoltaje);
			/*
			 * valorVoltaje = ((segMin * + interMin) - (segMin + interMax));
			 * System.out.println("segMin = " + segMin); System.out.println("interMin = " +
			 * interMin); System.out.println("interMax = " + interMax);
			 */
			interMin = segMin + interMin;
			interMax = segMin + interMax;
			// System.out.println("valorVoltaje = " + "((" + interMin + ") - (" + interMax +
			// ") = " + valorVoltaje);
			// System.out.println("-------------------------------");

			rangoVoltajes.append(cantVolts[0] + String.valueOf(valorVoltaje) + " ");
		}

		messageDecoded(rangoVoltajes.toString(), cantBit, segmentos, intervalos, signo, cantSegm, cantInterv);
		return rangoVoltajes;
	}

	public void messageDecoded(String contentVoltage, int cantBit, List<String> segmentos, List<String> intervalos,
			int signo, int cantSegm, int cantInterv) {

		StringBuilder wordVoltValue = new StringBuilder();
		String[] voltList = contentVoltage.split(" ");

		for (String volt : voltList) {

			String signValue = (volt.substring(0, signo)).equals("+") ? "0" : "1";
			double voltValue = Double.valueOf((volt.substring(signo, volt.length())).replace(",", "."));

			double segmValue = 0;
			double interValue = 0;

			for (String segmList : segmentos) {
				String[] segm = segmList.split("\\|");
				double segmRangeMin = Double.valueOf(segm[1].replace(",", "."));
				double segmRangeMax = Double.valueOf(segm[2].replace(",", "."));
				// System.out.println(segmRangeMin + " >= " + voltValue + " && " + segmRangeMax
				// + " <= " + voltValue);
				if (segmRangeMin < voltValue && segmRangeMax > voltValue) {
					interValue = voltValue - segmRangeMin;
					segmValue = Double.valueOf(segm[0]);
					// System.out.println("segmValue = " + segmValue);
					break;
				}
			}

			for (String interList : intervalos) {
				String[] inter = interList.split("\\|");
				double interRangeMin = Double.valueOf(inter[1].replace(",", "."));
				double interRangeMax = Double.valueOf(inter[2].replace(",", "."));
				// System.out.println("1- interValue = " + interValue);
				// interValue = interValue + ((interRangeMax - interRangeMin) / 2);
				// System.out.println("inter = " + interRangeMin + " < " + interValue + " && " +
				// interRangeMax + " > " + interValue);
				// System.out.println("1- interValue = " + interValue);
				if (interRangeMin <= interValue && interRangeMax >= interValue) {
					interValue = Double.valueOf(inter[0]);
					// System.out.println("2- interValue = " + interValue);
					break;
				}
			}

			wordVoltValue.append(signValue);
			wordVoltValue.append("|" + String.valueOf((int) segmValue) + "|");
			wordVoltValue.append(String.valueOf((int) interValue) + " ");
		}

		// System.out.println("palabra = " + wordVoltValue.toString());
		if (cantBit == 8) {
			decimalToBinario(wordVoltValue.toString(), 3, 4);
		} else if (cantBit == 9) {
			decimalToBinario(wordVoltValue.toString(), 4, 4);
		} else {
			decimalToBinario(wordVoltValue.toString(), 5, 4);
		}

	}

	/**
	 * Este metodo convierte los valor de voltaje hechos en decimal a binario
	 * decodificado
	 * 
	 * @param wordVoltValue
	 * @param cantSegmBin
	 * @param cantIntervBin
	 */
	public void decimalToBinario(String wordVoltValue, int cantSegmBin, int cantIntervBin) {

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
		binarioToMessageDecodificade(wordBinValue.toString());
	}

	public void binarioToMessageDecodificade(String binContent) {// TODO

		String[] contBinValue = binContent.split("\\|");
		StringBuilder wordDecod = new StringBuilder();
		for (String binValue : contBinValue) {

			int binValueInt = Integer.parseInt(binValue, 2);
			// System.out.println("binValueInt = " + binValueInt);
			wordDecod.append(String.valueOf(Character.toChars(Integer.parseInt(String.valueOf(binValueInt), 10))));
		}

		// System.out.println(wordDecod.toString());

		createFileTxt(wordDecod.toString(), "MessageDecod");// Se crea el mensaje decodificado
	}

	public void createFileTxt(String dataVoltajes, String type) {
		UtilsMessage utils = new UtilsMessage();
		utils.createFiles(dataVoltajes.toString(), type, dateNameTxt);
	}

}
