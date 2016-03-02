package com.samtrest.easy_postboy;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainFrame extends JFrame {
	static  Logger log = LoggerFactory.getLogger(MainFrame.class );
	private ToolbarActions verticalToolBar = new ToolbarActions();
	private JMenuItem openFileMenItem,openDefFileMenItem,logonToDBMenItem,checkTemplateMenItem,
	createTargetFilesMenItem,copyClipBoardMenuItem,infoMenuItem,quitMenuItem;
	JTextField fileTemplateNameField = new JTextField();
	JTextField fileJSONNameField = new JTextField();

	public MainFrame(String title,boolean isVisible) throws HeadlessException {
		super(title);
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e){
			log.debug("Exception {}\n{}",UICommonUtil.formatMessage(e));
		}
		this.addWindowListener(new WindowAdapter()		{
			public void windowClosing(WindowEvent arg0) {
				EasyPostboyApp.saveProps();
				System.exit(0);
			}
		});

		URL url = MainFrame.class.getResource("/com/samtrest/icons/small_postboy.gif");

		ImageIcon icon = new ImageIcon(
				java.awt.Toolkit.getDefaultToolkit().createImage(url));

		super.setIconImage(icon.getImage());

		Container content = getContentPane();

		this.setBounds(100,30,900,700);
		//		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		content.setLayout(new BorderLayout());
		//		Menu
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		JMenu actionMenu = new JMenu("Action");
		menuBar.add(actionMenu);
		JMenu aboutMenu = new JMenu("About");
		menuBar.add(aboutMenu);

		// File menu		
		logonToDBMenItem = new JMenuItem(verticalToolBar.getLogonToDBAction().getValue(AbstractAction.SHORT_DESCRIPTION).toString());
		logonToDBMenItem.setEnabled(true);
		logonToDBMenItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				verticalToolBar.getLogonToDBAction().actionPerformed(arg0);
			}
		});	
		openFileMenItem = new JMenuItem(verticalToolBar.getOpenFileAction().getValue(AbstractAction.SHORT_DESCRIPTION).toString());
		openFileMenItem.setEnabled(true);
		openFileMenItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				verticalToolBar.getOpenFileAction().actionPerformed(arg0);
			}
		});	
		openDefFileMenItem = new JMenuItem(verticalToolBar.getOpenDefineFile().getValue(AbstractAction.SHORT_DESCRIPTION).toString());
		openDefFileMenItem.setEnabled(true);
		openDefFileMenItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				verticalToolBar.getOpenDefineFile().actionPerformed(arg0);
			}
		});	
		quitMenuItem = new JMenuItem(verticalToolBar.getQuitAction().getValue(AbstractAction.SHORT_DESCRIPTION).toString());
		quitMenuItem.setEnabled(true);
		quitMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				verticalToolBar.getQuitAction().actionPerformed(arg0);
			}
		});	

		fileMenu.add(logonToDBMenItem);
		fileMenu.add(openDefFileMenItem);
		fileMenu.add(openFileMenItem);
		fileMenu.addSeparator();
		fileMenu.add(quitMenuItem);

		//Action menu
		checkTemplateMenItem = new JMenuItem(verticalToolBar.getCheckTemplateAction().getValue(AbstractAction.SHORT_DESCRIPTION).toString());
		checkTemplateMenItem.setEnabled(true);
		checkTemplateMenItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				verticalToolBar.getCheckTemplateAction().actionPerformed(arg0);
			}
		});	
		createTargetFilesMenItem = new JMenuItem(verticalToolBar.getCreateFileAction().getValue(AbstractAction.SHORT_DESCRIPTION).toString());
		createTargetFilesMenItem.setEnabled(true);
		createTargetFilesMenItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				verticalToolBar.getCreateFileAction().actionPerformed(arg0);
			}
		});	

		actionMenu.add(checkTemplateMenItem);
		actionMenu.add(createTargetFilesMenItem);


		// About menu
		JMenuItem online_menu_item = new JMenuItem(verticalToolBar.onlineAction.getValue(AbstractAction.SHORT_DESCRIPTION).toString());
		online_menu_item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				verticalToolBar.onlineAction.actionPerformed(arg0);
			}
		});		
		JMenuItem about_menu_item = new JMenuItem(verticalToolBar.aboutAction.getValue(AbstractAction.SHORT_DESCRIPTION).toString());
		about_menu_item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				verticalToolBar.aboutAction.actionPerformed(arg0);
			}
		});		
		JMenuItem news_menu_item = new JMenuItem(verticalToolBar.newsAction.getValue(AbstractAction.SHORT_DESCRIPTION).toString());
		news_menu_item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				verticalToolBar.newsAction.actionPerformed(arg0);
			}
		});

		aboutMenu.add(online_menu_item);
		aboutMenu.add(news_menu_item);
		aboutMenu.add(about_menu_item);
		this.setJMenuBar(menuBar);
		content.add(verticalToolBar,BorderLayout.WEST);		

		Dimension dim = new Dimension(150,20);

		JTextPane sqlArea = new JTextPane();
		sqlArea.setFont(Sets.BOLD_FONT);
		JViewport sqlViewport = new JViewport();
		sqlViewport.setView(sqlArea);
		JScrollPane sqlScrollPane = new JScrollPane(sqlViewport);
		sqlScrollPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(
						"SQL Statement"),
				BorderFactory.createEmptyBorder(2,2,2,2)));
		sqlScrollPane.getVerticalScrollBar().setUnitIncrement(20);

		JTextPane tagArea = new JTextPane();
		JViewport tagViewport = new JViewport();
		tagViewport.setView(tagArea);
		JScrollPane tagScrollPane = new JScrollPane(tagViewport);
		tagScrollPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(
						"Available tags"),
				BorderFactory.createEmptyBorder(2,2,2,2)));
		tagScrollPane.getVerticalScrollBar().setUnitIncrement(20);


		JSplitPane sqlTagSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,sqlScrollPane,tagScrollPane);
		sqlTagSplitPane.setOneTouchExpandable(true);
		sqlTagSplitPane.setResizeWeight(0.5);

		JTextPane filesTextPane = new JTextPane();
		filesTextPane.setLayout(new BorderLayout());
		filesTextPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder(
						"Definition files"),
				BorderFactory.createEmptyBorder(2,2,2,2)));
		filesTextPane.setPreferredSize(new Dimension(900,80));
		
		JTextPane fileRTFTextPane = new JTextPane();
		fileRTFTextPane.setPreferredSize(new Dimension(900,25));
		fileRTFTextPane.setLayout(new BorderLayout());
		JLabel RTFfileNameLabel = new JLabel("RTF File:",JLabel.TRAILING);
		RTFfileNameLabel.setAlignmentY(10);
		RTFfileNameLabel.setLabelFor(fileTemplateNameField);
		fileTemplateNameField.setPreferredSize(new Dimension(500,20));
//		fileTemplateNameField.setMinimumSize(new Dimension(100,50));		
		fileTemplateNameField.setEditable(false);

		fileRTFTextPane.add(fileTemplateNameField,BorderLayout.CENTER);

		JButton openRTFFileButton = new JButton(verticalToolBar.getOpenFileAction().getValue(AbstractAction.SHORT_DESCRIPTION).toString());
		openRTFFileButton.setPreferredSize(dim);
		openRTFFileButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				verticalToolBar.getOpenFileAction().actionPerformed(e);
			}
		}
				);
		fileRTFTextPane.add(openRTFFileButton,BorderLayout.EAST);
		fileRTFTextPane.add(RTFfileNameLabel,BorderLayout.WEST);
		filesTextPane.add(fileRTFTextPane,BorderLayout.NORTH);

		JTextPane fileJSONPane = new JTextPane();
		fileJSONPane.setLayout(new BorderLayout());
		fileJSONPane.setPreferredSize(new Dimension(900,25));
		JLabel JSONfileNameLabel = new JLabel("JSON File:",JLabel.TRAILING);
		JSONfileNameLabel.setLabelFor(fileJSONNameField);
		fileJSONNameField.setPreferredSize(new Dimension(500,20));
//		fileJSONNameField.setMinimumSize(new Dimension(100,50));
		fileJSONNameField.setEditable(false);
		
		fileJSONPane.add(fileJSONNameField,BorderLayout.CENTER);

		JButton openJSONFileButton = new JButton(verticalToolBar.getOpenDefineFile().getValue(AbstractAction.SHORT_DESCRIPTION).toString());
		openJSONFileButton.setPreferredSize(dim);
		openJSONFileButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				verticalToolBar.getOpenDefineFile().actionPerformed(e);
			}
		}
				);
		fileJSONPane.add(openJSONFileButton,BorderLayout.EAST);
		fileJSONPane.add(JSONfileNameLabel,BorderLayout.WEST);
		filesTextPane.add(fileJSONPane,BorderLayout.SOUTH);
		
		JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,filesTextPane,sqlTagSplitPane);
		content.add(mainSplitPane,BorderLayout.CENTER);
		
		setVisible(isVisible);
	}
	public void message(String t){
		JOptionPane.showMessageDialog(this,t);
	}
	public void message(String t,String tt){
		JOptionPane.showMessageDialog(this,t,tt,JOptionPane.INFORMATION_MESSAGE);
	}
	public boolean question (String t){
		return (JOptionPane.showConfirmDialog(this,t,"Warning",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION);
	}
}
