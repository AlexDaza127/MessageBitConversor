package bitconvesor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import utilities.UtilsMessage;

/**
 * Esta clase permite convertir a los bit que se requieran, segun se para 8, 9 y
 * 10 bits, va a tener el metodo que permite realizar la tabla de segmentos y la
 * tabla de intervalos para cada caso
 * 
 * @author Michael
 *
 */
public class BitConversor {
	String dateNameTxt = "";
	private DecimalFormat formato1 = new DecimalFormat("####.####");
	private StringBuilder dataCodificacion = new StringBuilder();
	private int cantBitTxtName = 0;

	public List<List<String>> dataContentCalculateBits(int cantBit, double cantSegmentos, String formattedDate, boolean fileCreate) {
		dateNameTxt = formattedDate;
		List<List<String>> dataCalculoBits = new ArrayList<>();
		dataCalculoBits.addAll(calculoBit(cantBit, cantSegmentos, fileCreate));
		return dataCalculoBits;
	}

	/**
	 * Metodo para el calculo de milivoltios que se usara para hallar la tabla de
	 * segmentos e intervalos
	 * 
	 * @param cantBit
	 * @param cantSegmentos
	 * @param data
	 * @return
	 */
	public List<List<String>> calculoBit(int cantBit, double cantSegmentos, boolean fileCreate) {
		List<List<String>> dataCalculoBits = new ArrayList<>();

		List<String> voltaje = new ArrayList<>();
		double valorMiliVoltios = (1000 / (16 * cantSegmentos));
		dataCodificacion.append(+cantBit + " bits\r\n\r\n");
		dataCodificacion.append("Voltaje = " + formato1.format(valorMiliVoltios).replace(",", ".") + " mV\r\n\r\n");
		voltaje.add(formato1.format(valorMiliVoltios));
		dataCalculoBits.add(0, voltaje);
		if (cantBit == 8) {
			dataCalculoBits.add(1, tablaSegmentos(valorMiliVoltios, 8));
			cantBitTxtName = cantBit;
		} else if (cantBit == 9) {
			dataCalculoBits.add(1, tablaSegmentos(valorMiliVoltios, 16));
			cantBitTxtName = cantBit;
		} else if (cantBit == 10) {
			dataCalculoBits.add(1, tablaSegmentos(valorMiliVoltios, 32));
			cantBitTxtName = cantBit;
		}
		dataCalculoBits.add(2, tablaIntervalos(valorMiliVoltios));

		if (fileCreate) {
			createDataCodTxt();
		}
		return dataCalculoBits;
	}

	/**
	 * Metodo para crear la tabla de segmentos
	 * 
	 * @param valorMiliVoltios
	 * @param cantSegmentos
	 * @return
	 */
	public List<String> tablaSegmentos(double valorMiliVoltios, int cantSegmentos) {

		double cantidadSegmento = valorMiliVoltios * 16;
		// System.out.println("Tamaño del Segmento = " + cantidadSegmento);
		dataCodificacion.append("\r\nTamano del Segmento = " + cantidadSegmento + "\r\n");
		List<String> tablaValoresSegmentos = new ArrayList<>();
		List<String> valoresSegmentos = new ArrayList<>();

		for (int i = 0; i < cantSegmentos; i++) {
			double valor = cantidadSegmento * i;
			tablaValoresSegmentos.add(String.valueOf(formato1.format(valor)));
			if (i == cantSegmentos - 1) {
				valor = cantidadSegmento * cantSegmentos;
				tablaValoresSegmentos.add(String.valueOf(formato1.format(valor)));
			}
		}

		// System.out.println("\r\nTabla de Segmentos");
		dataCodificacion.append("\r\nTabla de Segmentos\r\n");
		for (int i = 0; i < tablaValoresSegmentos.size() - 1; i++) {
			valoresSegmentos.add(i + "|" + tablaValoresSegmentos.get(i) + "|" + tablaValoresSegmentos.get(i + 1));
			// System.out.println("|" + tablaValoresSegmentos.get(i) + " - " +
			// tablaValoresSegmentos.get(i + 1) + "|");
			dataCodificacion.append(
					"|" + i + "|" + tablaValoresSegmentos.get(i) + " - " + tablaValoresSegmentos.get(i + 1) + "|\r\n");
		}
		dataCodificacion.append("\r\n\r\n");

		return valoresSegmentos;
	}

	/**
	 * Metodo para crear la tabla de intervalos
	 * 
	 * @param valorMiliVoltios
	 * @return
	 */
	public List<String> tablaIntervalos(double valorMiliVoltios) {

		List<String> tablaValoresIntervalos = new ArrayList<>();
		List<String> valoresIntervalos = new ArrayList<>();
		for (int i = 0; i < 17; i++) {
			double valor = valorMiliVoltios * i;
			tablaValoresIntervalos.add(String.valueOf(formato1.format(valor)));
		}
		// System.out.println("\r\nTabla de Intervalos");
		dataCodificacion.append("\r\nTabla de Intervalos\r\n");
		for (int i = 0; i < tablaValoresIntervalos.size() - 1; i++) {
			valoresIntervalos.add(i + "|" + tablaValoresIntervalos.get(i) + "|" + tablaValoresIntervalos.get(i + 1));
			// System.out.println("|" + tablaValoresIntervalos.get(i) + " - " +
			// tablaValoresIntervalos.get(i + 1) + "|");
			dataCodificacion.append("|" + i + "|" + tablaValoresIntervalos.get(i) + " - "
					+ tablaValoresIntervalos.get(i + 1) + "|\r\n");
		}

		return valoresIntervalos;
	}

	public void createDataCodTxt() {
		UtilsMessage utils = new UtilsMessage();
		utils.createFiles(dataCodificacion.toString(), "MessageDataDod", cantBitTxtName + "Bit" + dateNameTxt);
		dataCodificacion.delete(0, dataCodificacion.length());
	}

}
