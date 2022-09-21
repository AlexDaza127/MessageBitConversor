package utilities;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Clase que permite alojar metodos para distintas utilidades
 *
 */
public class UtilsMessage {
	DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddSSmm");

	public void createDirs() {
		List<File> directorioList = new ArrayList<>();

		directorioList.add(new File("./MensajesDecodificado"));
		directorioList.add(new File("./MensajesBinarios"));
		directorioList.add(new File("./MensajesVoltajes"));
		directorioList.add(new File("./MensajesDataCodificacion"));
		for (File directorio : directorioList) {
			if (!directorio.exists()) {
				if (directorio.mkdirs()) {
					System.out.println("Directorio creado");
				} else {
					System.out.println("Error al crear directorio");
				}
			}
		}
	}

	public void createFiles(String content, String typeMessage) {
		try {
			LocalDateTime date_of_today = LocalDateTime.now();
			String formattedDate = date_of_today.format(format);
			String fileNameBin = "./MensajesBinarios/MensajeBinario" + formattedDate + ".txt";
			String fileNameVolt = "./MensajesVoltajes/MensajeVoltaje" + formattedDate + ".txt";
			String fileNameDecod = "./MensajesDecodificado/MensajeDecodificado" + formattedDate + ".txt";
			String fileNameDataCod = "./MensajesDataCodificacion/MensajeDataCodificacion" + formattedDate + ".txt";
			List<String> lines = Arrays.asList(content);

			if(typeMessage.equals("MessageBinary")) {
				Path file = Paths.get(fileNameBin);
				Files.write(file, lines, StandardCharsets.UTF_8);
			}else if(typeMessage.equals("MessageVoltaje")) {
				Path file = Paths.get(fileNameVolt);
				Files.write(file, lines, StandardCharsets.UTF_8);
			}else if(typeMessage.equals("MessageDecod")) {
				Path file = Paths.get(fileNameDecod);
				Files.write(file, lines, StandardCharsets.UTF_8);
			}else if(typeMessage.equals("MessageDataDod")) {
				Path file = Paths.get(fileNameDataCod);
				Files.write(file, lines, StandardCharsets.UTF_8);
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
