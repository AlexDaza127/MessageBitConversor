/**
 *
 */
package compound;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bitconvesor.BitConversor;
import bitconvesor.UniformQuantifierConversor;
import statisticalstructure.StatisticalStructureMessage;
import translator.AlphabetMilitary;
import utilities.UtilsMessage;

/**
 * Clase encriptadora del mensaje de texto, en esta clase se puede elegir si se
 * quiere decodificar o solo genera el mensaje encriptado en voltajes
 *
 * @author Michael
 *
 */
public class MessageBitEncoder {

  /**
   * Metodo principal de la clase encriptadora
   *
   * @param args mensaje encriptado
   */
  @SuppressWarnings("resource")
  public static void main(String[] args) {
    BitConversor bitConversor = new BitConversor();
    UtilsMessage utils = new UtilsMessage();
    UniformQuantifierConversor uniformQuantifierConversor = new UniformQuantifierConversor();
    StatisticalStructureMessage statisticalStructureMessage = new StatisticalStructureMessage();
    AlphabetMilitary military = new AlphabetMilitary();
    List<List<String>> dataCalculoBits = new ArrayList<>();
    long inicio = 0;

    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    LocalDateTime date_of_today = LocalDateTime.now();
    String formattedDate = date_of_today.format(format);
    inicio = System.currentTimeMillis();

    utils.createDirs();
    String strContent = utils.contentFileMessageToEncode();

    if (strContent.equals("") || strContent == null) {
      System.out.println("No hay contenido disponible en la carpeta MensajeEntrada");
    } else {
      System.out.println("Se inserto el contenido exitosamente");
      System.out.println("Elija la cantidad de bloques a estructurar (2 a 8):");
      try {
        Scanner cantBloq = new Scanner(System.in);
        int cb = cantBloq.nextInt();
        if (cb >= 2 && cb <= 8) {
          statisticalStructureMessage.triggerStatisticalStructure(strContent, "Encoder", cb);
        } else {
          statisticalStructureMessage.triggerStatisticalStructure(strContent, "Encoder", 100);
          System.out.println("Se eligio un numero fuera de rango, se omite el paso");
        }
      } catch (Exception e) {
        System.out.println("No se digito numero");
      }

      System.out.println("Elija la cantidad de bits a codificar:");
      Scanner scBit = new Scanner(System.in);
      String strBit = scBit.nextLine();

      System.out.println("¿Quiere decodificar el mensaje? Y");
      Scanner descisionDecBit = new Scanner(System.in);
      String strDescBit = descisionDecBit.nextLine();

      switch (strBit) {
        case "8":
          System.out.println("8 bits");
          dataCalculoBits.addAll(bitConversor.dataContentCalculateBits(8, 8, formattedDate, true));
          uniformQuantifierConversor.messageEncoder(8, strContent, dataCalculoBits.get(1), dataCalculoBits.get(2),
                  formattedDate, strDescBit);
          break;
        case "9":
          System.out.println("9 bits");
          dataCalculoBits.addAll(bitConversor.dataContentCalculateBits(9, 16, formattedDate, true));

          System.out.println("¿Quiere codificar el mensaje en alfabeto militar? Y");
          Scanner idioma9 = new Scanner(System.in);
          String elecIdioma9 = idioma9.nextLine();
          if (elecIdioma9.equals("Y")) {
            inicio = System.currentTimeMillis();
            strContent = military.latinoToMilitary(strContent);
          }

          uniformQuantifierConversor.messageEncoder(9, strContent, dataCalculoBits.get(1), dataCalculoBits.get(2),
                  formattedDate, strDescBit);
          break;
        case "10":
          System.out.println("10 bits");
          dataCalculoBits.addAll(bitConversor.dataContentCalculateBits(10, 32, formattedDate, true));

          System.out.println("¿Quiere codificar el mensaje en alfabeto militar? Y");
          Scanner idioma10 = new Scanner(System.in);
          String elecIdioma10 = idioma10.nextLine();
          if (elecIdioma10.equals("Y")) {
            inicio = System.currentTimeMillis();
            strContent = military.latinoToMilitary(strContent);
          }

          uniformQuantifierConversor.messageEncoder(10, strContent, dataCalculoBits.get(1),
                  dataCalculoBits.get(2), formattedDate, strDescBit);
          break;
        default:
          System.out.println("No es valido");
          break;
      }
    }
    long fin = System.currentTimeMillis();
    double tiempoTotal = (double) ((fin - inicio));
    System.out.println("Tiempo total de ejecucion: " + tiempoTotal + " ms");
    System.out.println("Se termina el proceso de encriptacion");
    System.out.println("-------------------------------------------------");
  }

}
