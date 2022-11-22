package statisticalstructure;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import utilities.UtilsMessage;

/**
 * Clase que calcula al estadistica estructural del contendio de los mensajes
 *
 * @author Michael
 *
 */
public class StatisticalStructureMessage {

  private double contTotalSymbol = 0;
  private double contTotalProbSymbol = 0;
  private double contTotalEntropy = 0;

  /**
   * M�todo principal de la clase, aqui se genera el archivo consolidado en la
   * tabla de estadistica estructural
   *
   * @param content contenido del mensaje
   * @param type tipo de estadistica, sea para el contendio del encriptado o
   * desencriptado
   */
  public String triggerStatisticalStructure(String content, String type, int cantBlock) {
    if (content != null) {
      Map<String, Double> frequencySymbol = new HashMap<>();
      Map<String, Double> probabilitySymbol = new HashMap<>();
      Map<String, Double> frequencyBitsSymbol = new HashMap<>();
      Map<String, Double> expectedValueSymbol = new HashMap<>();
      StringBuilder processData = new StringBuilder();
      DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
      LocalDateTime date_of_today = LocalDateTime.now();
      String formattedDate = date_of_today.format(format);
      DecimalFormat df = new DecimalFormat("##0.0000");

      if (type.equals("Encoder")) {
        frequencySymbol.putAll(frecuencyEncoder(content));
      } else if (type.equals("Decoder")) {
        frequencySymbol.putAll(frecuencyDecoder(content));
      }

      probabilitySymbol.putAll(probability(frequencySymbol, contTotalSymbol));
      frequencyBitsSymbol.putAll(frequencyBits(probabilitySymbol));
      expectedValueSymbol.putAll(expectedValue(probabilitySymbol, frequencyBitsSymbol));

      processData.append("Simbolo\t|Frecuencia\t|Probabilidad\t|Frecuencia Bits\t|Esperanza\r\n");

      List<String> orderAlphabet = new ArrayList<>();
      List<Double> orderProb = new ArrayList<>();

      for (String f : frequencySymbol.keySet()) {
        orderAlphabet.add(f);
      }

      for (Double f : probabilitySymbol.values()) {
        if (!orderProb.contains(f)) {
          orderProb.add(f);
        }
      }

      Collections.sort(orderAlphabet);

      for (String f : orderAlphabet) {
        processData.append(f + "\t\t|" + frequencySymbol.get(f) + "\t\t|" + df.format(probabilitySymbol.get(f))
                + "\t\t\t|" + df.format(frequencyBitsSymbol.get(f)) + "\t\t\t\t|"
                + df.format(expectedValueSymbol.get(f)) + "\r\n");
      }
      processData.append(
              "\r\n\r\n\r\ncantidad total frecuencia de simbolos contados = " + (int) contTotalSymbol + "\r\n");
      processData.append("cantidad total probabilidad de simbolos contados = "
              + df.format(contTotalProbSymbol).replace(",", ".") + "\r\n");
      processData.append("cantidad total probabilidad de simbolos contados = "
              + df.format(contTotalEntropy).replace(",", "."));

      Collections.sort(orderProb);
      Collections.reverse(orderProb);

      processData.append("\r\nOrden de mayor a menor frecuencia de aparición obtenida: \r\n");

      String getKeyFromValue = "";

      for (Double f : orderProb) {
        getKeyFromValue += getSingleKeyFromValue(probabilitySymbol, f) + "|";
      }
      processData.append(getKeyFromValue);

      if (cantBlock != 100) {
        blockCombination(probabilitySymbol, cantBlock);
      }

      UtilsMessage utilMessage = new UtilsMessage();
      utilMessage.createFiles(processData.toString(), "MessageStatistical", formattedDate);
      return content;
    }
    return null;
  }

  /**
   * Organiza las llaves de los hashmap, asi se obtiene el simbolo que mas
   * aparece del mayor a menor en el contenido del mensaje
   *
   * @param <K> key
   * @param <V> value
   * @param map hashmap
   * @param value valor
   * @return key al que pertenece el valor buscado
   */
  public static <K, V> K getSingleKeyFromValue(Map<K, V> map, V value) {
    for (Map.Entry<K, V> entry : map.entrySet()) {
      if (Objects.equals(value, entry.getValue())) {
        return entry.getKey();
      }
    }
    return null;
  }

  /**
   * Frecuencia con la que aparece en el contenido un simbolo en un mensaje
   * encriptado
   *
   * @param contentMessage contenido del mensaje
   * @return frequencySymbol columna de frecuencias
   */
  public Map<String, Double> frecuencyDecoder(String contentMessage) {
    Map<String, Double> frequencySymbol = new HashMap<>();
    Map<String, String> tramaSymbol = new HashMap<>();
    LocalDateTime date_of_today = LocalDateTime.now();
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    String formattedDate = date_of_today.format(format);
    String[] contentSplit = contentMessage.split(" ");
    contTotalSymbol = 0;

    for (int i = 0; i < contentSplit.length; i++) {

      String symbolString = contentSplit[i];
      if (frequencySymbol.containsKey(symbolString)) {
        double cont = frequencySymbol.get(symbolString) + 1;
        frequencySymbol.put(symbolString, cont);
        tramaSymbol.put(symbolString, tramaSymbol.get(symbolString) + ";" + i);
      } else {
        frequencySymbol.put(symbolString, 1.0);
        tramaSymbol.put(symbolString, String.valueOf(i));
      }
    }

    for (String f : frequencySymbol.keySet()) {
      contTotalSymbol += frequencySymbol.get(f);
    }

    StringBuilder trama = new StringBuilder();
    trama.append("Simbolo|Frecuencia|Orden de aparición\r\n");
    for (String k : tramaSymbol.keySet()) {
      trama.append(k + "|" + frequencySymbol.get(k) + "|" + tramaSymbol.get(k) + "\r\n");
    }

    UtilsMessage utilMessage = new UtilsMessage();
    utilMessage.createFiles(trama.toString(), "MessageTramaDecode", formattedDate);
    createFileTrama(tramaSymbol);
    return frequencySymbol;
  }

  /**
   * Toma la trama de symbolos y crea el archivo con los datos de simbolos,
   * frecuencia y lugar de aparición
   *
   * @param tramaSymbol contenido de la trama
   */
  public void createFileTrama(Map<String, String> tramaSymbol) {
    StringBuilder contentMessage = new StringBuilder();
    Map<String, String> mapContent = new HashMap<>();
    LocalDateTime date_of_today = LocalDateTime.now();
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    String formattedDate = date_of_today.format(format);

    for (String k : tramaSymbol.keySet()) {
      String[] numChar = tramaSymbol.get(k).split(";");
      for (String string : numChar) {
        mapContent.put(string, k);
      }
    }

    for (int i = 0; i < contTotalSymbol; i++) {
      contentMessage.append(mapContent.get(String.valueOf(i)) + " ");
    }

    UtilsMessage utilMessage = new UtilsMessage();
    utilMessage.createFiles(contentMessage.toString(), "MessageVoltaje", "Trama" + formattedDate);
  }

  /**
   * Frecuencia con la que aparece en el contenido un simbolo en texto
   *
   * @param contentMessage contenido del mensaje
   * @return frequencySymbol columna de frecuencias
   */
  public Map<String, Double> frecuencyEncoder(String contentMessage) {
    contTotalSymbol = 0;
    Map<String, Double> frequencySymbol = new HashMap<>();
    Map<String, String> tramaSymbol = new HashMap<>();
    LocalDateTime date_of_today = LocalDateTime.now();
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    String formattedDate = date_of_today.format(format);
    for (int i = 0; i < contentMessage.length(); i++) {
      char letter = contentMessage.charAt(i);
      String symbolString = String.valueOf(letter).equals(" ") ? "space"
              : String.valueOf(letter).equals("\r") ? "return"
              : String.valueOf(letter).equals("\n") ? "new line" : String.valueOf(letter);

      if (frequencySymbol.containsKey(symbolString)) {
        double cont = frequencySymbol.get(symbolString) + 1;
        frequencySymbol.put(symbolString, cont);
        tramaSymbol.put(symbolString, tramaSymbol.get(symbolString) + ";" + i);
      } else {
        frequencySymbol.put(symbolString, 1.0);
        tramaSymbol.put(symbolString, String.valueOf(i));
      }
    }

    for (String f : frequencySymbol.keySet()) {
      contTotalSymbol += frequencySymbol.get(f);
    }

    StringBuilder trama = new StringBuilder();
    trama.append("Simbolo|Frecuencia|Orden de aparición\r\n");
    for (String k : tramaSymbol.keySet()) {
      trama.append(k + "|" + frequencySymbol.get(k) + "|" + tramaSymbol.get(k) + "\r\n");
    }

    UtilsMessage utilMessage = new UtilsMessage();
    utilMessage.createFiles(trama.toString(), "MessageTramaEncode", formattedDate);
    return frequencySymbol;
  }

  /**
   * Porcentaje de probabilidad de aparición de los simbolos
   *
   * @param frequencySymbol frecuencia de los simbolos
   * @param contTotalSymbol conteo total de simbolos
   * @return probabilitySymbol columna de porcentaje de probabilidad de
   * aparición
   */
  public Map<String, Double> probability(Map<String, Double> frequencySymbol, double contTotalSymbol) {

    Map<String, Double> probabilitySymbol = new HashMap<>();
    probabilitySymbol.putAll(frequencySymbol);
    DecimalFormat df = new DecimalFormat("##0.0000");

    for (String f : frequencySymbol.keySet()) {
      double probPercent = frequencySymbol.get(f) / contTotalSymbol;
      probabilitySymbol.put(f, Double.valueOf(df.format(probPercent).replace(",", ".")));
    }

    for (String f : probabilitySymbol.keySet()) {
      contTotalProbSymbol += probabilitySymbol.get(f);
    }

    return probabilitySymbol;
  }

  /**
   * Estimacion de la estructura binaria del mensaje dependiendo de la cantidad
   * de bloques que se tomen
   *
   * @param probBlock diccionario de datos con los simbolos y sus probabilidades
   * de aparicion
   * @param cantBlock cantidad de bloques a combinar
   * @return
   */
  public void blockCombination(Map<String, Double> probBlock, int cantBlock) {
    DecimalFormat df = new DecimalFormat("##0.0000000000");
    List<String> orderBlock = new ArrayList<>();
    orderBlock.addAll(probBlock.keySet());
    Collections.sort(orderBlock);

    BufferedWriter bw = null;
    FileWriter fw = null;

    try {

      File fileDel = new File("./MensajeEstructuraBinaria/MensajesEstructuraBinaria.txt");
      if (fileDel.delete()) {
        System.out.println("Se elimino el archivo: MensajesEstructuraBinaria.txt");
      } else {
        System.out.println("No se elimino el archivo: MensajesEstructuraBinaria.txt");
      }
      File file = new File("./MensajeEstructuraBinaria/MensajesEstructuraBinaria.txt");

      if (!file.exists()) {
        file.createNewFile();
        System.out.println("Se creo el archivo: MensajesEstructuraBinaria.txt");
      }

      fw = new FileWriter(file.getAbsoluteFile(), true);
      bw = new BufferedWriter(fw);

      bw.write("Combinacion de los simbolos del mensaje en bloques de " + cantBlock + "\r\n\r\n");

      double sumaProbabilidades = 0;
      for (int i = 0; i < orderBlock.size(); i++) {
        double valueProdProb = 0;
        for (int j = 0; j < orderBlock.size(); j++) {

          if (cantBlock == 2) {
            valueProdProb = probBlock.get(orderBlock.get(i).trim()) * probBlock.get(orderBlock.get(j).trim());
            sumaProbabilidades += valueProdProb;
            bw.write(orderBlock.get(i) + " * " + orderBlock.get(j) + " = " + df.format(valueProdProb) + "\r\n");
          }

          if (cantBlock >= 3) {
            for (int k = 0; k < orderBlock.size(); k++) {

              if (cantBlock == 3) {
                valueProdProb = probBlock.get(orderBlock.get(i).trim())
                        * probBlock.get(orderBlock.get(j).trim())
                        * probBlock.get(orderBlock.get(k).trim());
                sumaProbabilidades += valueProdProb;
                bw.write(orderBlock.get(i) + " * " + orderBlock.get(j) + " * " + orderBlock.get(k) + " = " + df.format(valueProdProb) + "\r\n");
              }

              if (cantBlock >= 4) {
                for (int l = 0; l < orderBlock.size(); l++) {

                  if (cantBlock == 4) {
                    valueProdProb = probBlock.get(orderBlock.get(i).trim())
                            * probBlock.get(orderBlock.get(j).trim())
                            * probBlock.get(orderBlock.get(k).trim())
                            * probBlock.get(orderBlock.get(l).trim());
                    sumaProbabilidades += valueProdProb;
                    bw.write(orderBlock.get(i) + " * " + orderBlock.get(j) + " * " + orderBlock.get(k) + " * " + orderBlock.get(l) + " = " + df.format(valueProdProb) + "\r\n");
                  }

                  if (cantBlock >= 5) {
                    for (int m = 0; m < orderBlock.size(); m++) {

                      if (cantBlock == 5) {
                        valueProdProb = probBlock.get(orderBlock.get(i).trim())
                                * probBlock.get(orderBlock.get(j).trim())
                                * probBlock.get(orderBlock.get(k).trim())
                                * probBlock.get(orderBlock.get(l).trim())
                                * probBlock.get(orderBlock.get(m).trim());
                        sumaProbabilidades += valueProdProb;
                        bw.write(orderBlock.get(i) + " * " + orderBlock.get(j) + " * " + orderBlock.get(k) + " * " + orderBlock.get(l) + " * " + orderBlock.get(m) + " = " + df.format(valueProdProb) + "\r\n");
                      }

                      if (cantBlock >= 6) {
                        for (int n = 0; n < orderBlock.size(); n++) {

                          if (cantBlock == 6) {
                            valueProdProb = probBlock.get(orderBlock.get(i).trim())
                                    * probBlock.get(orderBlock.get(j).trim())
                                    * probBlock.get(orderBlock.get(k).trim())
                                    * probBlock.get(orderBlock.get(l).trim())
                                    * probBlock.get(orderBlock.get(m).trim())
                                    * probBlock.get(orderBlock.get(n).trim());
                            sumaProbabilidades += valueProdProb;
                            bw.write(orderBlock.get(i) + " * " + orderBlock.get(j) + " * " + orderBlock.get(k) + " * " + orderBlock.get(l) + " * " + orderBlock.get(m) + " * " + orderBlock.get(n) + " = " + df.format(valueProdProb) + "\r\n");
                          }

                          if (cantBlock >= 7) {
                            for (int o = 0; o < orderBlock.size(); o++) {

                              if (cantBlock == 7) {
                                valueProdProb = probBlock.get(orderBlock.get(i).trim())
                                        * probBlock.get(orderBlock.get(j).trim())
                                        * probBlock.get(orderBlock.get(k).trim())
                                        * probBlock.get(orderBlock.get(l).trim())
                                        * probBlock.get(orderBlock.get(m).trim())
                                        * probBlock.get(orderBlock.get(n).trim())
                                        * probBlock.get(orderBlock.get(o).trim());
                                sumaProbabilidades += valueProdProb;
                                bw.write(orderBlock.get(i) + " * " + orderBlock.get(j) + " * " + orderBlock.get(k) + " * " + orderBlock.get(l) + " * " + orderBlock.get(m) + " * " + orderBlock.get(n) + " * " + orderBlock.get(o) + " = " + df.format(valueProdProb) + "\r\n");
                              }

                              if (cantBlock >= 8) {
                                for (int p = 0; p < orderBlock.size(); p++) {
                                  if (cantBlock == 8) {
                                    valueProdProb = probBlock.get(orderBlock.get(i).trim())
                                            * probBlock.get(orderBlock.get(j).trim())
                                            * probBlock.get(orderBlock.get(k).trim())
                                            * probBlock.get(orderBlock.get(l).trim())
                                            * probBlock.get(orderBlock.get(m).trim())
                                            * probBlock.get(orderBlock.get(n).trim())
                                            * probBlock.get(orderBlock.get(o).trim())
                                            * probBlock.get(orderBlock.get(p).trim());
                                    sumaProbabilidades += valueProdProb;
                                    bw.write(orderBlock.get(i) + " * " + orderBlock.get(j) + " * " + orderBlock.get(k) + " * " + orderBlock.get(l) + " * " + orderBlock.get(m) + " * " + orderBlock.get(n) + " * " + orderBlock.get(o) + " * " + orderBlock.get(p) + " = " + df.format(valueProdProb) + "\r\n");
                                  }
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }

      bw.write("\r\n\r\n Longitud de palabra = " + probBlock.size());
      bw.write("\r\n Suma de probabilidades = " + sumaProbabilidades);

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (bw != null) {
          bw.close();
        }
        if (fw != null) {
          fw.close();
        }
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }

  /**
   * Frecuencia en bits de aparicion de los simbolos
   *
   * @param probabilitySymbol porcentaje de probabilidad de aparición de los
   * simbolos
   * @return frequencyBitsSymbol columna de frecuencias en bits
   */
  public Map<String, Double> frequencyBits(Map<String, Double> probabilitySymbol) {
    Map<String, Double> frequencyBitsSymbol = new HashMap<>();
    frequencyBitsSymbol.putAll(probabilitySymbol);
    DecimalFormat df = new DecimalFormat("##0.0000");

    for (String f : probabilitySymbol.keySet()) {
      double freqBit = probabilitySymbol.get(f) * (Math.log(1 / probabilitySymbol.get(f)) / Math.log(2));
      frequencyBitsSymbol.put(f, Double.valueOf(df.format(freqBit).replace(",", ".")));
      contTotalEntropy += freqBit;
    }

    return frequencyBitsSymbol;
  }

  /**
   * Valor esperado (Esperanza) de aparición de los simbolos
   *
   * @param probabilitySymbol porcentaje de probabilidad de aparición de los
   * simbolos
   * @param frequencyBitsSymbol frecuencias en bits de aparición de los
   * simbolos
   * @return expectedValueSymbol columna de valores esperados
   */
  public Map<String, Double> expectedValue(Map<String, Double> probabilitySymbol,
          Map<String, Double> frequencyBitsSymbol) {
    Map<String, Double> expectedValueSymbol = new HashMap<>();

    expectedValueSymbol.putAll(probabilitySymbol);
    DecimalFormat df = new DecimalFormat("##0.0000");

    for (String f : expectedValueSymbol.keySet()) {
      double expVlue = probabilitySymbol.get(f) * frequencyBitsSymbol.get(f);
      expectedValueSymbol.put(f, Double.valueOf(df.format(expVlue).replace(",", ".")));
    }

    return expectedValueSymbol;
  }

}
