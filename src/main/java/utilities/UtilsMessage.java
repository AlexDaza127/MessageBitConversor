package utilities;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Clase que contiene los metodos para distintas utilidades
 *
 */
public class UtilsMessage {
	
	/**
	 * Metodo que crear al respectivas carpetas de los mensajes de entrada, de los mensajes codificados en binarios, voltajes; sus respectivas configuraciones
	 * dependiendo de los bits y la decodificacion del mensaje
	 */
	public void createDirs() {
		List<File> directorioList = new ArrayList<>();

		directorioList.add(new File("./MensajesDecodificado"));
		directorioList.add(new File("./MensajesBinarios"));
		directorioList.add(new File("./MensajesVoltajes"));
		directorioList.add(new File("./MensajesDataCodificacion"));
		directorioList.add(new File("./MensajeEntrada"));
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

	/**
	 * Crea los archivos de los respectivos mensajes en sus respectivas carpetas
	 * @param content contenido del mensaje
	 * @param typeMessage tipo de mensaje a craer en la carpeta especifica
	 */
	public void createFiles(String content, String typeMessage, String dataID) {
		try {
			String fileNameBin = "./MensajesBinarios/MensajeBinario" + dataID + ".txt";
			String fileNameVolt = "./MensajesVoltajes/MensajeVoltaje" + dataID + ".txt";
			String fileNameDecod = "./MensajesDecodificado/MensajeDecodificado" + dataID + ".txt";
			String fileNameDataCod = "./MensajesDataCodificacion/MensajeDataCodificacion" + dataID + "Bit.txt";
			List<String> lines = Arrays.asList(content);
			Path file = null;
			if(typeMessage.equals("MessageBinary")) {
				file = Paths.get(fileNameBin);
				System.out.println("Se creo: MensajeBinario" + dataID + ".txt");
			}else if(typeMessage.equals("MessageVoltaje")) {
				file = Paths.get(fileNameVolt);
				System.out.println("Se creo: MensajeVoltaje" + dataID + ".txt");
			}else if(typeMessage.equals("MessageDecod")) {
				file = Paths.get(fileNameDecod);
				System.out.println("Se creo: MensajeDecodificado" + dataID + ".txt");
			}else if(typeMessage.equals("MessageDataDod")) {
				file = Paths.get(fileNameDataCod);
				System.out.println("Se creo: MensajeDataCodificacion" + dataID + ".txt");
			}			
			Files.write(file, lines, Charset.forName("Windows-1252"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
