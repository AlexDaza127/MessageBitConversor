package bitconvesor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase permite convertir a los bit que se requieran, segun se para 8, 9 y
 * 10 bits, va a tener el metodo que permite realizar la tabla de segmentos y la
 * tabla de intervalos para cada caso
 *
 */
public class BitConversor {

	private DecimalFormat formato1 = new DecimalFormat("####.####");

	// Este metodo toma la cantidad de bits que se quiere convertir y la data, da
	// como resultado la data convertida
	public void calculoBit(int cantBit, String data) {
		
		double valorMiliVoltios = ((1000 / (16 * (double) cantBit)));
		System.out.println("Voltaje = " + formato1.format(valorMiliVoltios).replace(",", ".") + " mV");

		if (cantBit == 8) {
			tablaSegmentos(valorMiliVoltios, 8);
		} else if (cantBit == 9) {
			tablaSegmentos(valorMiliVoltios, 16);
		} else if (cantBit == 10) {
			tablaSegmentos(valorMiliVoltios, 32);
		}
	}

	public List<String> tablaSegmentos(double valorMiliVoltios, int cantSegmentos) {

		double tamañoSegmento = valorMiliVoltios * 16;
		System.out.println("Tamaño del Segmento = " + tamañoSegmento);

		List<String> tablaValoresSegmentos = new ArrayList<>();
		List<String> valoresSegmentos = new ArrayList<>();

		for (int i = 0; i < cantSegmentos; i++) {
			double valor = tamañoSegmento * i;
			tablaValoresSegmentos.add(String.valueOf(formato1.format(valor)));
			if (i == cantSegmentos - 1) {
				valor = tamañoSegmento * cantSegmentos;
				tablaValoresSegmentos.add(String.valueOf(formato1.format(valor)));
			}
		}

		System.out.println("\r\nTabla de Segmentos");
		for (int i = 0; i < tablaValoresSegmentos.size() - 1; i++) {
			valoresSegmentos.add(tablaValoresSegmentos.get(i) + "-" + tablaValoresSegmentos.get(i + 1));
			System.out.println("|" + tablaValoresSegmentos.get(i) + " - " + tablaValoresSegmentos.get(i + 1) + "|");
		}
		
		tablaIntervalos(valorMiliVoltios);
		return valoresSegmentos;
	}

	public List<String> tablaIntervalos(double valorMiliVoltios) {

		List<String> tablaValoresIntervalos = new ArrayList<>();
		List<String> valoresIntervalos = new ArrayList<>();
		for (int i = 0; i < 17; i++) {
			double valor = valorMiliVoltios * i;
			tablaValoresIntervalos.add(String.valueOf(formato1.format(valor)));
		}
		System.out.println("\r\nTabla de Intervalos");
		for (int i = 0; i < tablaValoresIntervalos.size() - 1; i++) {
			valoresIntervalos.add(tablaValoresIntervalos.get(i) + "-" + tablaValoresIntervalos.get(i + 1));
			System.out.println("|" + tablaValoresIntervalos.get(i) + " - " + tablaValoresIntervalos.get(i + 1) + "|");
		}

		return valoresIntervalos;
	}
}
