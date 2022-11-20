package binarystructure;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utilities.UtilsMessage;

public class BinaryStructure {

    private double sumDistProb = 0;
    private double longWord = 0;

    public void principalBinaryStructure(String content, int cantBlock) {
        String dataBlockDivided = "";
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime date_of_today = LocalDateTime.now();
        String formattedDate = date_of_today.format(format);
        DecimalFormat df = new DecimalFormat("##0.0000");
        Map<String, Double> dataBlockFrequency = new HashMap<>();
        Map<String, Double> dataBlockProbability = new HashMap<>();
        Map<String, Double> dataBlockCombination = new HashMap<>();

        dataBlockDivided = blockDivided(content, cantBlock);
        dataBlockFrequency.putAll(blockFrequency(dataBlockDivided));
        dataBlockProbability.putAll(blockProbability(dataBlockFrequency));
        dataBlockCombination.putAll(blockCombination(dataBlockProbability, cantBlock));

        StringBuilder data = new StringBuilder();
        data.append("Bloque | Frecuencia | Probabilidad \r\n");
        for (String block : dataBlockFrequency.keySet()) {
            data.append(block + " | " + dataBlockFrequency.get(block) + " | " + df.format(dataBlockProbability.get(block)).replace(",", ".") + " \r\n");
        }
        data.append("\r\n\r\n");
        data.append("Combinacion de bloques de cantidad " + cantBlock + "\r\n\r\n");
        for (String block : dataBlockCombination.keySet()) {
            data.append(block + " = " + dataBlockCombination.get(block) + "\r\n");
        }

        UtilsMessage utilMessage = new UtilsMessage();
        utilMessage.createFiles(data.toString(), "MessageBinStruct", formattedDate);
    }

    /**
     * Método que divide el contenido del mensaje en la cantidad de bloques de
     * simbolos predefinidos
     *
     * @param content contenido del mensaje
     * @param cantBlock candidad de bloques a dividir el contenido del mensaje
     * @return contenido dividido en bloques
     */
    public String blockDivided(String content, int cantBlock) {
        StringBuilder block = new StringBuilder();

        for (int i = 0; i < content.length(); i += cantBlock) {
            longWord += 1;
            if (content.length() >= (i + cantBlock)) {
                block.append(content.substring(i, i + cantBlock) + "|");
            } else {
                block.append(content.substring(i, content.length()) + "|");
                break;
            }
        }

        return block.toString();
    }

    /**
     * frecuencia de aparicion de cada bloque de simbolos
     *
     * @param blocks contenido del mensaje dividido en bloques
     * @param longSymbol longitu de letras les contenido
     * @return tabla de bloques y la frecuencia de aparicion por cada uno de los
     * bloques
     */
    public Map<String, Double> blockFrequency(String blocks) {
        Map<String, Double> freqBlock = new HashMap<>();

        String[] blocksDivide = blocks.split("\\|");

        for (String block : blocksDivide) {
            String bDiv = String.valueOf(block).equals(" ") ? "space"
                    : String.valueOf(block).equals("\r") ? "return"
                    : String.valueOf(block).equals("\n") ? "new line" : String.valueOf(block);
            if (freqBlock.containsKey(bDiv)) {
                double cont = freqBlock.get(bDiv) + 1;
                freqBlock.put(bDiv, cont);
            } else {
                freqBlock.put(bDiv, 1.0);
            }
        }

        return freqBlock;
    }

    /**
     * probabilidad de aparicion de cada bloque de simbolos
     *
     * @param blocks contenido del mensaje dividido en bloques
     * @param longSymbol longitu de letras les contenido
     * @return tabla de bloques y la frecuencia de aparicion por cada uno de los
     * bloques
     */
    public Map<String, Double> blockProbability(Map<String, Double> freqBlock) {
        Map<String, Double> probBlock = new HashMap<>();

        for (String block : freqBlock.keySet()) {
            probBlock.put(block, freqBlock.get(block) / longWord);
            sumDistProb += (freqBlock.get(block) / longWord); // suma total de la probabilidad, esta tiene que dar 1
        }

        return probBlock;
    }

    public Map<String, Double> blockCombination(Map<String, Double> probBlock, int cantBlock) {
        StringBuilder multiProbBlock = new StringBuilder();
        Map<String, Double> combination = new HashMap<>();
        List<String> orderBlock =  new ArrayList<>();
        System.out.println("probBlock.size() = " + probBlock.size());
        orderBlock.addAll(probBlock.keySet());
        Collections.sort(orderBlock);
        for (int i = 0; i < orderBlock.size(); i++) {
        	for (int j = 0; j < orderBlock.size(); j++) {
        		
        		
        	 System.out.println("orderBlock = " + orderBlock.get(i) + " * " + orderBlock.get(j));
        	}
        }

        return combination;
    }

}
