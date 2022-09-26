/**
 * 
 */
package compound;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bitconvesor.BitConversor;
import bitconvesor.UniformQuantifierConversor;
import utilities.UtilsMessage;

/**
 * Esta clase es la principal, aqui se toma los datos para transformalos de
 * analogo a digital
 *
 */
public class MessageBitEncoder {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		BitConversor bitConversor = new BitConversor();
		UtilsMessage utils = new UtilsMessage();
		UniformQuantifierConversor uniformQuantifierConversor = new UniformQuantifierConversor();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		LocalDateTime date_of_today = LocalDateTime.now();
		String formattedDate = date_of_today.format(format);
		List<List<String>> dataCalculoBits = new ArrayList<>();
		utils.createDirs();
		Path path = Paths.get("./MensajeEntrada/input.txt");
		long inicio = 0;
		try {
			String strContent = "";
			
			strContent = Files.readString(path, Charset.forName("UTF-8"));

			if (strContent.equals("") || strContent == null) {
				System.out.println("No hay contenido disponible en la carpeta MensajeEntrada");
			} else {
				System.out.println("Se inserto el contenido exitosamente");
				System.out.println("Elija la cantidad de bits a codificar:");
				Scanner scBit = new Scanner(System.in);
				String strBit = scBit.nextLine();
				inicio = System.currentTimeMillis();
				switch (strBit) {
				case "8":
					System.out.println("8 bits");
					dataCalculoBits.addAll(bitConversor.dataContentCalculateBits(8, 8, formattedDate,true));
					uniformQuantifierConversor.messageEncoder(8, strContent, dataCalculoBits.get(1),
							dataCalculoBits.get(2), formattedDate);
					break;
				case "9":
					System.out.println("9 bits");
					dataCalculoBits.addAll(bitConversor.dataContentCalculateBits(9, 16, formattedDate,true));
					uniformQuantifierConversor.messageEncoder(9, strContent, dataCalculoBits.get(1),
							dataCalculoBits.get(2), formattedDate);
					break;
				case "10":
					System.out.println("10 bits");
					dataCalculoBits.addAll(bitConversor.dataContentCalculateBits(10, 32, formattedDate,true));
					uniformQuantifierConversor.messageEncoder(10, strContent, dataCalculoBits.get(1),
							dataCalculoBits.get(2), formattedDate);
					break;
				default:
					System.out.println("No es valido");
					break;
				}
			}
			long fin = System.currentTimeMillis();
			double tiempoTotal = (double) ((fin - inicio));
			System.out.println("Tiempo total de ejecución: " + tiempoTotal +" ms");
			System.out.println("Se termina el proceso");
			System.out.println("-------------------------------------------------");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
