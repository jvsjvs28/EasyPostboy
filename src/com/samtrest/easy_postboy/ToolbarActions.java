package com.samtrest.easy_postboy;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToolbarActions extends JToolBar  {
	static  Logger log = LoggerFactory.getLogger(ToolbarActions.class );
	OpenTemplateFileAction openTemplateFileAction = new OpenTemplateFileAction();
	OpenMapperFile openMapperFile = new OpenMapperFile();
	OpenDataFileAction openDataFileAction = new OpenDataFileAction();
	QuitAction quitAction = new QuitAction();
	AboutAction aboutAction = new AboutAction();
	OnlineAction onlineAction = new OnlineAction();
	NewsAction newsAction = new NewsAction();
	LogonToDBAction logonToDBAction = new LogonToDBAction();
	CreateFileAction createFileAction = new CreateFileAction();
	CheckTemplateAction checkTemplateAction = new CheckTemplateAction();

	public class LogonToDBAction extends AbstractAction {

		public LogonToDBAction() {
			putValue(AbstractAction.SMALL_ICON,UICommonUtil.createImageIcon("logon.gif"));
			putValue(AbstractAction.SHORT_DESCRIPTION,"Logon to DB");
		}

		public void actionPerformed(ActionEvent arg0) {
			new Login();
		}
	}
	public class OpenMapperFile extends AbstractAction {

		public OpenMapperFile() {
			putValue(AbstractAction.SMALL_ICON,UICommonUtil.createImageIcon("open_mapper_file.gif"));
			putValue(AbstractAction.SHORT_DESCRIPTION,"Open definition file");
		}

		public void actionPerformed(ActionEvent arg0) {
			if (!isEnabled())
				return;
			Memory.mainFrame.tagMapperFilenameField.setText(new JsonChooser(Memory.mainFrame.tagMapperFilenameField,JFileChooser.FILES_ONLY,
					"Choose mapping json file").getPath());
			Memory.tagMapperJsonFile = new File(Memory.mainFrame.tagMapperFilenameField.getText());

			Memory.getEpProps().setProperty(Sets.MAP_FILE_PROPERTY_NAME,"" + Memory.mainFrame.tagMapperFilenameField.getText());
		}
	}
	public class OpenTemplateFileAction extends AbstractAction {

		public OpenTemplateFileAction() {
			putValue(AbstractAction.SMALL_ICON,UICommonUtil.createImageIcon("open_template_file.gif"));
			putValue(AbstractAction.SHORT_DESCRIPTION,"Open template RTF file");
		}

		public void actionPerformed(ActionEvent arg0) {
			if (!isEnabled())
				return;
			Memory.mainFrame.fileTemplateNameField.setText(new RTFChooser(Memory.mainFrame.fileTemplateNameField,JFileChooser.FILES_ONLY,
					"Choose RTF file").getPath());
			if (!"".equals(Memory.mainFrame.fileTemplateNameField.getText())){
				Memory.rtfProcessor = new RtfTemplate(
						               new File(Memory.mainFrame.fileTemplateNameField.getText()));
			}
			Memory.getEpProps().setProperty(Sets.TEMPLATE_FILE_PROPERTY_NAME,"" + Memory.mainFrame.fileTemplateNameField.getText());
		}
	}

	public class OpenDataFileAction extends AbstractAction {

		public OpenDataFileAction() {
			putValue(AbstractAction.SMALL_ICON,UICommonUtil.createImageIcon("open_data_file.gif"));
			putValue(AbstractAction.SHORT_DESCRIPTION,"Open data file");
		}

		public void actionPerformed(ActionEvent arg0) {
			if (!isEnabled())
				return;
			Memory.mainFrame.dataFilenameField.setText(new JsonChooser(Memory.mainFrame.dataFilenameField,JFileChooser.FILES_ONLY,
					"Choose data json file").getPath());
			Memory.dataJsonFile = new File(Memory.mainFrame.dataFilenameField.getText());

			if (log.isTraceEnabled()){
				Memory.tagMapper.printJson();
			}
			Memory.getEpProps().setProperty(Sets.DATA_FILE_PROPERTY_NAME,"" + Memory.mainFrame.tagMapperFilenameField.getText());
		}
	}

	public class CreateFileAction extends AbstractAction {

		public CreateFileAction() {
			putValue(AbstractAction.SMALL_ICON,UICommonUtil.createImageIcon("create_file.gif"));
			putValue(AbstractAction.SHORT_DESCRIPTION,"Create new files");
		}

		public void actionPerformed(ActionEvent arg0) {
			if (!isEnabled())
				return;
			Memory.setTargetDir(new RTFChooser(Memory.mainFrame.fileTemplateNameField,JFileChooser.DIRECTORIES_ONLY,
					"Choose target directory").getPath());
		}
	}

	public class QuitAction extends AbstractAction {
		public QuitAction() {
			putValue(AbstractAction.SMALL_ICON,UICommonUtil.createImageIcon("quit.gif"));
			putValue(AbstractAction.SHORT_DESCRIPTION,"Exit");
		}
		public void actionPerformed(ActionEvent arg0) {
			EasyPostboyApp.saveProps();
			System.exit(0);
		}
	}

	public class CheckTemplateAction extends AbstractAction {
		public CheckTemplateAction() {
			putValue(AbstractAction.SMALL_ICON,UICommonUtil.createImageIcon("check_template.gif"));
			putValue(AbstractAction.SHORT_DESCRIPTION,"Check template");
		}
		public void actionPerformed(ActionEvent arg0) {

		}
	}

	public class AboutAction extends AbstractAction {

		public AboutAction() {
			putValue(AbstractAction.SMALL_ICON,UICommonUtil.createImageIcon("about.gif"));
			putValue(AbstractAction.SHORT_DESCRIPTION,"About Easy Postboy tool");
		}

		public void actionPerformed(ActionEvent arg0) {
			Memory.mainFrame.message("Easy Postboy\n    Version "+
					Sets.POSTBOY_VERSION +
					"\nPostboy Online:   www.samtrest.com\nE-Mail:      support@samtrest.com\n"+
					Memory.getTrial_msg(),"");
		}
	}
	public class OnlineAction extends AbstractAction {

		public OnlineAction() {
			putValue(AbstractAction.SMALL_ICON,UICommonUtil.createImageIcon("oda.gif"));
			putValue(AbstractAction.SHORT_DESCRIPTION,"Easy Postboy Online");
		}

		public void actionPerformed(ActionEvent arg0) {
			String os = System.getProperty("os.name").toLowerCase();
			Runtime rt = Runtime.getRuntime();
			try{
				if (os.indexOf( "win" ) >= 0) {
					// this doesn't support showing urls in the form of "page.html#nameLink" 
					rt.exec( "rundll32 url.dll,FileProtocolHandler " + Sets.ODA_URL);
				} else if (os.indexOf( "mac" ) >= 0) {
					rt.exec( "open " + Sets.ODA_URL);
				} else if (os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0) {
					// Do a best guess on unix until we get a platform independent way
					// Build a list of browsers to try, in this order.
					String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
							"netscape","opera","links","lynx"};
					// Build a command string which looks like "browser1 "url" || browser2 "url" ||..."
					StringBuffer cmd = new StringBuffer();
					for (int i=0; i<browsers.length; i++)
						cmd.append( (i==0  ? "" : " || " ) + browsers[i] +" \"" + Sets.ODA_URL + "\" ");

					rt.exec(new String[] { "sh", "-c", cmd.toString() });

				} else {
					return;
				}
			}catch (Exception e){
				return;
			}
			return;		
		}
	}

	public class NewsAction extends AbstractAction {
		private JDialog frame = new JDialog();
		public NewsAction() {
			putValue(AbstractAction.SMALL_ICON,UICommonUtil.createImageIcon("news.gif"));
			putValue(AbstractAction.SHORT_DESCRIPTION,"What is new?");
		}

		public void actionPerformed(ActionEvent arg0) {
			frame.setBounds(200,100,500,600);
			frame.setTitle("Easy Postboy News  ");
			URL url = MainFrame.class.getResource("/com/samtrest/icons/news.gif");

			ImageIcon icon = new ImageIcon(
					java.awt.Toolkit.getDefaultToolkit().createImage(url));

			((Frame)frame.getParent()).setIconImage(icon.getImage());
			try
			{
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			}
			catch(Exception e)
			{}
			frame.addWindowListener(new  WindowAdapter()
			{
				public void windowClosing(WindowEvent arg0) {
					frame.dispose();
				}
			});
			JTextPane news_presentation = new JTextPane();
			news_presentation.setText(Sets.WHAT_NEW);
			JViewport news_vp = new JViewport();
			news_vp.setView(news_presentation);
			JScrollPane news_scrl_pn = new JScrollPane(news_vp);
			//			news_presentation.setBackground(Sets.CONTEXT_COLOR);
			//			news_presentation.setFont(Sets.getContextFont());
			news_presentation.setEditable(false);
			news_scrl_pn.getHorizontalScrollBar().setUnitIncrement(20);
			news_scrl_pn.getVerticalScrollBar().setUnitIncrement(20);
			news_scrl_pn.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createTitledBorder(
							"News"),
							BorderFactory.createEmptyBorder(2,2,2,2)));
			frame.getContentPane().add(news_scrl_pn);
			frame.setVisible(true);
		}
	}
	public OpenTemplateFileAction getOpenFileAction() {
		return openTemplateFileAction;
	}

	public ToolbarActions()
	{
		this.setRollover(true);
		this.setOrientation(JToolBar.VERTICAL);

		Dimension separatorDim = new Dimension(10,5);
		Dimension separatorBigDim = new Dimension(10,10);
		add(logonToDBAction);
		add(openMapperFile);
		add(openTemplateFileAction);
		addSeparator(separatorDim);
		add(checkTemplateAction);
		addSeparator(separatorDim);
		add(createFileAction);
		addSeparator(separatorDim);
		add(quitAction);
	}

	public QuitAction getQuitAction() {
		return quitAction;
	}

	public LogonToDBAction getLogonToDBAction() {
		return logonToDBAction;
	}

	public CreateFileAction getCreateFileAction() {
		return createFileAction;
	}

	public CheckTemplateAction getCheckTemplateAction() {
		return checkTemplateAction;
	}

	public OpenTemplateFileAction getOpenTemplateFileAction() {
		return openTemplateFileAction;
	}

	public OpenMapperFile getOpenDefineFile() {
		return openMapperFile;
	}

	public OpenDataFileAction getOpenDataFileAction() {
		return openDataFileAction;
	}

}
