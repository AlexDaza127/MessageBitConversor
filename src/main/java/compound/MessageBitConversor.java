/**
 * 
 */
package compound;

import java.util.Scanner;

import bitconvesor.BitConversor;

/**
 * Esta clase es la principal, aqui se toma los datos para transformalos de
 * analogo a digital, sus respectivos voltajes y codigo binario
 *
 */
public class MessageBitConversor {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BitConversor bitConversor = new BitConversor();
		do {
			System.out.println("Introduzca una mensaje:");
			Scanner scContent = new Scanner(System.in);
			String strContent = scContent.nextLine();

			System.out.println("Elija la cantidad de bits a codificar:");
			Scanner scBit = new Scanner(System.in);
			String strBit = scBit.nextLine();

			switch (strBit) {
			case "8":
				System.out.println("8 bits");
				bitConversor.calculoBit(8,strContent);
				break;
			case "9":
				System.out.println("9 bits");
				bitConversor.calculoBit(9,strContent);
				break;
			case "10":
				System.out.println("10 bits");
				bitConversor.calculoBit(10,strContent);
				break;
			default:
				System.out.println("No es valido");
				break;
			}
			System.out.println("-------------------------------------------------");
		} while (true);
	}

}
