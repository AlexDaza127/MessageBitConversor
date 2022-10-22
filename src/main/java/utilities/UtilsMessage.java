package utilities;

import java.io.File;
import java.io.IOException;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Clase que contiene los metodos de distintas utilidades
 * 
 * @author Michael
 *
 */
public class UtilsMessage {

	/**
	 * Metodo que permite crear automaticamente las respectivas carpetas de los mensajes de entrada, de los
	 * mensajes codificados en binarios, voltajes; sus respectivas configuraciones
	 * dependiendo de los bits y la decodificacion del mensaje
	 */
	public void createDirs() {
		List<File> directorioList = new ArrayList<>();

		directorioList.add(new File("./MensajesDecodificado"));
		directorioList.add(new File("./MensajesBinarios"));
		directorioList.add(new File("./MensajesVoltajes"));
		directorioList.add(new File("./MensajesDataCodificacion"));
		directorioList.add(new File("./MensajesEstadisticos"));
		directorioList.add(new File("./MensajesTramaDecode"));
		directorioList.add(new File("./MensajesTramaEncode"));
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
	 * Metodo para crear los archivos de los respectivos mensajes encriptados y desencriptados en sus respectivas carpetas
	 * 
	 * @param content     contenido del mensaje
	 * @param typeMessage tipo de mensaje a craer en la carpeta especifica
	 * @param dataID fecha de identificación del archivo generado
	 */
	public void createFiles(String content, String typeMessage, String dataID) {
		try {
			String fileNameBin = "./MensajesBinarios/MensajeBinario" + dataID + ".txt";
			String fileNameVolt = "./MensajesVoltajes/MensajeVoltaje" + dataID + ".txt";
			String fileNameDecod = "./MensajesDecodificado/MensajeDecodificado" + dataID + ".txt";
			String fileNameDataCod = "./MensajesDataCodificacion/MensajeDataCodificacion" + dataID + ".txt";
			String fileNameStatistic = "./MensajesEstadisticos/MensajeEstadistico" + dataID + ".txt";
			String fileTramaDecode = "./MensajesTramaDecode/MensajesTramaDecode" + dataID + ".txt";
			String fileTramaEncode = "./MensajesTramaEncode/MensajesTramaEncode" + dataID + ".txt";
			List<String> lines = Arrays.asList(content);
			Path file = null;
			if (typeMessage.equals("MessageBinary")) {
				file = Paths.get(fileNameBin);
				System.out.println("Se creo: MensajeBinario" + dataID + ".txt");
			} else if (typeMessage.equals("MessageVoltaje")) {
				file = Paths.get(fileNameVolt);
				System.out.println("Se creo: MensajeVoltaje" + dataID + ".txt");
			} else if (typeMessage.equals("MessageDecod")) {
				file = Paths.get(fileNameDecod);
				System.out.println("Se creo: MensajeDecodificado" + dataID + ".txt");
			} else if (typeMessage.equals("MessageDataDod")) {
				file = Paths.get(fileNameDataCod);
				System.out.println("Se creo: MensajeDataCodificacion" + dataID + ".txt");
			}else if (typeMessage.equals("MessageStatistical")) {
				file = Paths.get(fileNameStatistic);
				System.out.println("Se creo: MensajeEstadistico" + dataID + ".txt");
			}else if (typeMessage.equals("MessageTramaDecode")) {
				file = Paths.get(fileTramaDecode);
				System.out.println("Se creo: MessageTramaDecode" + dataID + ".txt");
			}else if (typeMessage.equals("MessageTramaEncode")) {
				file = Paths.get(fileTramaEncode);
				System.out.println("Se creo: MessageTramaEncode" + dataID + ".txt");
			}
			
			
			try {
				Files.write(file, lines, Charset.forName("Windows-1252"));
			} catch (CharacterCodingException ex) {
				System.out.println(ex.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo que permite leer los archivos alojados en la carpeta de archivos encriptados (carpeta MensajesVoltajes)
	 * @return contenido del mensaje encriptado
	 */
	public String contentFileVoltToDecode() {
		String contentVolt = "";
		File folder = new File("MensajesVoltajes");

		try {
			if (folder.listFiles().length != 0) {
				for (File file : folder.listFiles()) {
					if (!file.isDirectory()) {
						System.out.println("Se esta procesando el archivo: " + file.getName() + "\r\n");
						Path path = Paths.get(folder.getAbsolutePath() + "\\" + file.getName());
						contentVolt = (Files.readString(path, Charset.forName("UTF-8"))).trim();
					}
				}
				return contentVolt;
			}
		} catch (Exception e) {
			System.out.println("No hay archivos para procesar en la carpeta MensajeVoltaje");
		}
		return contentVolt;
	}
	
	/**
	 * Metodo que permite leer los archivos alojados en la carpeta de archivos para encriptar (carpeta MensajeEntrada)
	 * @return contenido del mensaje a encriptar
	 */
	public String contentFileMessageToEncode() {
		String content = "";
		File folder = new File("MensajeEntrada");

		try {
			if (folder.listFiles().length != 0) {
				for (File file : folder.listFiles()) {
					if (!file.isDirectory()) {
						System.out.println("Se esta procesando el archivo: " + file.getName() + "\r\n");
						Path path = Paths.get(folder.getAbsolutePath() + "\\" + file.getName());
						content = (Files.readString(path, Charset.forName("UTF-8"))).trim();
					}
				}
				return content;
			}
		} catch (Exception e) {
			System.out.println("No hay archivos para procesar en la carpeta MensajeEntrada");
		}
		return content;
	}
}
