package application.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class FileDuplicator {


	//If createPackage is true, generates a custom file save it to the users system when they're done editing.
	//If createPackage is false indicates its just creating a new file so the user can start editing.
	public void duplicateFile(File fileToCopy, File fileToSave, boolean createPackage) {
		FileWriter writer;
		StringBuffer buffer = new StringBuffer();
		try {
			if (!(fileToCopy.length() == 0)) {
				Scanner scanner;
				scanner = new Scanner(fileToCopy);

				while (scanner.hasNext()) {
					String line = scanner.nextLine();
					if (createPackage && line.contains("stylesheet")) {
						line = "<link rel=\"stylesheet\" href=\"./styles/map-styles.css\">";
					}
					buffer.append(line + System.lineSeparator());
				}
			}
			String fileContents = buffer.toString();
			writer = new FileWriter(fileToSave);
			writer.write(fileContents);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (createPackage) {
			File parentDir = new File(fileToSave.getParent() + "\\maps package");
			if(parentDir.exists()) {
			String fileId = incrementFileNameId(parentDir.getAbsolutePath());
				parentDir = new File(fileId);
			}
			parentDir.mkdir();
			packageFiles(fileToSave, parentDir);
		}
	}
  // Generates a file package consisting of the HTML and css file
	public void packageFiles(File fileToSave, File directory) {
		Path temp;
		try {
			temp = Files.move(Paths.get(fileToSave.getAbsolutePath()),
					Paths.get(directory.getAbsolutePath() + "/" + fileToSave.getName()));
			if (temp != null) {
				System.out.println("file Moved successfully");
			} else {
				System.out.println("we have a problem");
			}
			File cssFile = new File(getClass().getResource("/styles/map-styles.css").toURI());
			File stylesDirectory = new File(directory + "\\styles");
			stylesDirectory.mkdir();
			temp = Files.copy(Paths.get(cssFile.getAbsolutePath()),
					Paths.get(stylesDirectory.getAbsolutePath() + "/" + cssFile.getName()));
		} 
			catch (IOException | URISyntaxException ex) {         
			ex.printStackTrace();
		}
	}
	
	//if file already exists this method just adds a number to the new file name so there is no naming conflict
	//with other files
	public String incrementFileNameId(String fileName) {
		Pattern p = Pattern.compile("(.+\\((\\d+))\\)$");
		Matcher match = p.matcher(fileName);
		if (match.find()) {	
		int id = Integer.parseInt(match.group(2));
		fileName = fileName.replace(match.group(2), String.valueOf(++id));
		} else {
			fileName = fileName +"(1)";
		}
		File file = new File(fileName);
		if(file.exists()) {
			fileName = incrementFileNameId(fileName);
		}
		return fileName;
		}
	}

