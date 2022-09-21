package bitconvesor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import utilities.UtilsMessage;

/**
 * Esta clase permite convertir de analogo a digital y de digital a analogo,
 * igualmente permite convertir los valores a voltaje
 *
 */
public class UniformQuantifierConversor {
	UtilsMessage utils = new UtilsMessage();
	private DecimalFormat formatoBits = null;

	/**
	 * Este metodo toma los bits y la data del mensaje y la convierte en el mensaje
	 * con los valores en binario
	 * 
	 * @param cantBit
	 * @param data
	 * @return
	 */
	public String messageBinario(int cantBit, String content) {
		char[] charArray = content.toCharArray();
		StringBuilder palabra = new StringBuilder();
		String cantCaracterBits = "";
		for (int i = 0; i < cantBit; i++) {
			cantCaracterBits += "0";
		}
		formatoBits = new DecimalFormat(cantCaracterBits);

		for (int i = 0; i < charArray.length; i++) {
			String s = Integer.toBinaryString((int) charArray[i]);
			if (i != charArray.length - 1) {
				palabra.append(formatoBits.format(Integer.valueOf(s)) + "-");
			} else {
				palabra.append(formatoBits.format(Integer.valueOf(s)));
			}
		}

		utils.createFiles(palabra.toString(), "MessageBinary");
		return palabra.toString();
	}

	/**
	 * Este metodo toma los bits y la data del mensaje y la convierte en el mensaje
	 * con los valores en voltajes
	 * 
	 * @param cantBit
	 * @param data
	 * @return
	 */
	public String messageVoltage(int cantBit, String content, List<String> segmentos, List<String> intervalos) {
		StringBuilder rangoVoltajes = new StringBuilder();
		rangoVoltajes.delete(0, rangoVoltajes.length());
		
		if (cantBit == 8) {
			String[] dataBinary = content.split("-");
			String signoDataDec = "";
			String segmentosDataDec = "";
			String intervaloDataDec = "";
			List<String> datosVoltajes = new ArrayList<>();

			for (String data : dataBinary) {
				String signoData = data.substring(0, 1);
				signoDataDec = String.valueOf(Integer.parseInt(data.substring(0, 1), 2)).equals("0") ? "+" : "-";
				String segmentosData = data.substring(1, 4);
				segmentosDataDec = String.valueOf(Integer.parseInt(data.substring(1, 4), 2));
				String intervaloData = data.substring(4, 8);
				intervaloDataDec = String.valueOf(Integer.parseInt(data.substring(4, 8), 2));
				datosVoltajes.add(signoDataDec + "|" + segmentosDataDec + "|" + intervaloDataDec);
				System.out.println("signoData = " + signoData);
				System.out.println("segmentosData = " + segmentosData);
				System.out.println("intervaloData = " + intervaloData);
				System.out.println("numero = " + signoDataDec + "|" + segmentosDataDec + "|" + intervaloDataDec);
			}

			
			double segMin = 0;
			double interMin = 0;
			double interMax = 0;
			double valorVoltaje = 0;

			for (String volts : datosVoltajes) {
				String[] cantVolts = volts.split("|");

				for (String seg : segmentos) {
					String[] cantSeg = seg.split("-");

					if (cantVolts[1].equals(cantSeg[0])) {
						segMin = Double.valueOf(cantSeg[1].replace(",", "."));
					}
				}

				for (String inter : intervalos) {
					String[] cantInter = inter.split("-");
					System.out.println("cantVolts[2] = " + cantVolts[2]);
					System.out.println("cantInter[0] = " + cantInter[0]);
					if (cantVolts[2].equals(cantInter[0])) {
						System.out.println("inter = " + inter);
						interMin = Double.valueOf(cantInter[1].replace(",", "."));
						interMax = Double.valueOf(cantInter[2].replace(",", "."));
						break;
					}
				}

				valorVoltaje = ((segMin + interMin) - (segMin + interMax)) / 2;
				System.out.println("valorVoltaje = " + valorVoltaje);
				
				rangoVoltajes.append(cantVolts[0]+String.valueOf(valorVoltaje)+" ");
			}
		}
		createDataCodTxt(rangoVoltajes.toString());
		return rangoVoltajes.toString();
	}
	
	
	public void createDataCodTxt(String dataVoltajes) {
		UtilsMessage utils = new UtilsMessage();
		utils.createFiles(dataVoltajes.toString(), "MessageVoltaje");
	}

	/**
	 * Este metodo toma el contendio del mensaje de analogo a digital (aqui es donde
	 * se puede usar el codigo UNICODE para codificar)
	 * 
	 * @param content
	 * @return
	 */
	public String convertirAnalogoDigital(String content) {

		return null;
	}

	/**
	 * Este metodo toma el contendio del mensaje de digital a analogo (aqui es donde
	 * se puede usar el codigo UNICODE para decodificar)
	 * 
	 * @param content
	 * @return
	 */
	public String convertirDigitalAnalogo(String content) {

		return null;
	}

}
