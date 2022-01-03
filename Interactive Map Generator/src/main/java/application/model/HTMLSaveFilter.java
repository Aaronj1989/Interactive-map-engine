package application.model;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class HTMLSaveFilter extends FileFilter {

	@Override
	public boolean accept(File f) {
		if(f.isDirectory()) {
			return false;
		}
		String s = f.getName().toLowerCase();
		return s.endsWith(".html");
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
 return"*.html,*.HTML";
	}

}
