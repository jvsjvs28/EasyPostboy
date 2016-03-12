package com.samtrest.easy_postboy;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

public class JsonChooser extends JFrame {
	JFileChooser fc = new JFileChooser();
	String path;
	
	public JsonChooser(JTextField dir,int dialog_type,String title)
	{
		super();
		setTitle(title);
		fc.setDialogTitle(title);
		fc.setFileSelectionMode((dialog_type));
		fc.addChoosableFileFilter(new JSONFilesFilter());
		if (!"".equals(dir.getText()))
		{
			File f = new File(dir.getText());
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			fc.setDialogTitle(title);
			fc.setFileSelectionMode((dialog_type));
			fc.addChoosableFileFilter(new JSONFilesFilter());
			if (f.exists())
			{
				if (f.isDirectory())
					fc.setCurrentDirectory(f);
				else
					fc.setCurrentDirectory(new File(f.getParent()));
			}
			else
				fc.setCurrentDirectory(new File(f.getParent()));
			
		}	
		int res = fc.showOpenDialog(this);
		if (res == JFileChooser.APPROVE_OPTION)
			path = fc.getSelectedFile().toString();
		else
			path = "";
	}
	
	class JSONFilesFilter extends FileFilter
	{
		public boolean accept (File file)
		{
			if (file.isDirectory()) return true;
			return (file.getName().toLowerCase().endsWith(".json"));
		}
		public String getDescription()
		{
			return "JSON definition files(json)";
		}
	}

	public String getPath() {
		return path;
	}

}
