/**
 * 
 */
package compound;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bitconvesor.BitConversor;
import bitconvesor.UniformQuantifierConversor;
import utilities.UtilsMessage;

/**
 * Esta clase es la principal, aqui se toma los datos para transformalos de
 * analogo a digital, sus respectivos voltajes y codigo binario
 *
 */
public class MessageBitConversor {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		BitConversor bitConversor = new BitConversor();
		UtilsMessage utils = new UtilsMessage();
		UniformQuantifierConversor uniformQuantifierConversor = new UniformQuantifierConversor();
		List<List<String>> dataCalculoBits = new ArrayList<>();
		utils.createDirs();
		do {
			System.out.println("Introduzca una mensaje:");
			Scanner scContent = new Scanner(System.in);
			String strContent = scContent.nextLine();

			System.out.println("Elija la cantidad de bits a codificar:");
			Scanner scBit = new Scanner(System.in);
			String strBit = scBit.nextLine();
			String messageBinaryContent = "";
			switch (strBit) {
			case "8":
				System.out.println("8 bits");
				dataCalculoBits.addAll(bitConversor.dataContentCalculateBits(8, 8));
				messageBinaryContent = uniformQuantifierConversor.messageBinario(8, strContent);
				uniformQuantifierConversor.messageVoltage(8, messageBinaryContent, dataCalculoBits.get(1),
						dataCalculoBits.get(2));
				break;
			case "9":
				System.out.println("9 bits");
				dataCalculoBits.addAll(bitConversor.dataContentCalculateBits(9, 16));
				messageBinaryContent = uniformQuantifierConversor.messageBinario(9, strContent);
				uniformQuantifierConversor.messageVoltage(9, messageBinaryContent, dataCalculoBits.get(1),
						dataCalculoBits.get(2));

				break;
			case "10":
				System.out.println("10 bits");
				dataCalculoBits.addAll(bitConversor.dataContentCalculateBits(10, 32));
				messageBinaryContent = uniformQuantifierConversor.messageBinario(10, strContent);
				uniformQuantifierConversor.messageVoltage(10, messageBinaryContent, dataCalculoBits.get(1),
						dataCalculoBits.get(2));
				break;
			default:
				System.out.println("No es valido");
				break;
			}
			System.out.println("-------------------------------------------------");
		} while (true);
	}

}
