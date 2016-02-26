package com.samtrest.easy_postboy;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Login extends JDialog
{
	static  Logger log = LoggerFactory.getLogger(Login.class );
	private static final long serialVersionUID = -555512416095209411L;
	JTextField dbname = new JTextField(200), 
			hostname = new JTextField(20),
			port = new JTextField(20),
			username = new JTextField(50);
	JPasswordField psw;
	JComboBox status,reposDB;
	JLabel status_lbl,dbname_lbl,hostname_lbl,port_lbl,username_lbl,psw_lbl,jao_prefix_lbl;
	JButton ok_btn,cancel_btn;
	JPanel cnt_pn = new JPanel(new BorderLayout());
	boolean isForQuery = false;
	DBConnection con;

	public Login(String db,String host,String p,String user){
		super((JFrame)null,"Logon for table select",true);
		isForQuery = true;
		dbname.setText(db);
		hostname.setText(host);
		port.setText(p);
		username.setText(user);
		Memory.getDbConnection().setDBName(dbname.getText());
		Memory.getEpProps().setProperty("dbname",dbname.getText());
		Memory.getDbConnection().setDBHost(hostname.getText());
		Memory.getEpProps().setProperty("hostname",hostname.getText());
		Memory.getDbConnection().setDBPort(port.getText());
		Memory.getEpProps().setProperty("port",port.getText());
		Memory.getDbConnection().setUsr(username.getText());
		Memory.getEpProps().setProperty("username",username.getText());

		showLogin();
	}
	public Login(){
		super((JFrame)null,"Easy Postboy ["+ Sets.POSTBOY_VERSION+"]",true);
		Memory.setDbConnection(new DBConnection());		
		showLogin();
	}
	public Login(String title){
		super((JFrame)null,title,true);
		Memory.setDbConnection(new DBConnection());		
		showLogin();
	}
	public void showLogin(){
		try	{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e){
			log.debug("Exception {}\n{}",UICommonUtil.formatMessage(e));
		}
		URL url = MainFrame.class.getResource("/com/samtrest/icons/oda.gif");

		ImageIcon icon = new ImageIcon(
				java.awt.Toolkit.getDefaultToolkit().createImage(url));

		((Frame)getParent()).setIconImage(icon.getImage());

		this.addWindowListener(new  WindowAdapter()	{
			public void windowClosing(WindowEvent arg0) {
				dispose();
				if (!isForQuery)
					System.exit(0);
			}
			public void windowOpened(WindowEvent arg0) {
				if (!"".equals(dbname.getText()) &&
						!"".equals(hostname.getText())&&
						!"".equals(port.getText()) &&
						!"".equals(username.getText())){
					psw.requestFocusInWindow();
				}
			}
		});

		Container content = getContentPane();

		boolean isAdmin = true || UnderSets.FIRST_PARAM.toUpperCase().equals("ADMIN")||
				UnderSets.FIRST_PARAM.equals("jvs");
		if (isAdmin)
			this.setBounds(300,300,350,250);
		else
			this.setBounds(300,300,350,100);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		content.setLayout(new BorderLayout());

		cnt_pn.setBorder(new EmptyBorder(new Insets(5,5,5,5)));
		cnt_pn.setLayout( new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(2,2,2,2);
		c.fill = GridBagConstraints.HORIZONTAL;


		JLabel dbname_lbl = new JLabel("DB Name:",JLabel.TRAILING);
		dbname_lbl.setLabelFor(dbname);
		if (isAdmin)
			cnt_pn.add(dbname_lbl,c);
		c.gridx = 1;
		c.weightx = 1.0;
		c.gridwidth = 1;
		if (isAdmin)
			cnt_pn.add(dbname,c);
		dbname.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				hostname.requestFocusInWindow();
			}
		}
				);

		JLabel hostname_lbl = new JLabel("Host:",JLabel.TRAILING);
		hostname_lbl.setLabelFor(hostname);
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0.5;
		if (isAdmin)
			cnt_pn.add(hostname_lbl,c);
		c.gridx = 1;
		c.weightx = 0.5;
		if (isAdmin)
			cnt_pn.add(hostname,c);
		hostname.addActionListener(new ActionListener()	{
			public void actionPerformed(ActionEvent e){
				port.requestFocusInWindow();
			}
		}
				);

		JLabel port_lbl = new JLabel("Port:",JLabel.TRAILING);
		port_lbl.setLabelFor(port);
		c.gridy = 2;
		c.gridx = 0;
		if (isAdmin)
			cnt_pn.add(port_lbl,c);
		c.gridx = 1;
		if (isAdmin)
			cnt_pn.add(port,c);
		port.addActionListener(new ActionListener()	{
			public void actionPerformed(ActionEvent e){
				username.requestFocusInWindow();
			}
		}
				);

		JLabel username_lbl = new JLabel("Username:",JLabel.TRAILING);
		if (isForQuery)
			username_lbl.setText("User");
		username_lbl.setLabelFor(username);
		c.gridy = 3;
		c.gridx = 0;
		if (isAdmin)
			cnt_pn.add(username_lbl,c);
		c.gridx = 1;
		if (isAdmin)
			cnt_pn.add(username,c);
		username.addActionListener(new ActionListener()	{
			public void actionPerformed(ActionEvent e){
				psw.requestFocusInWindow();
			}
		}
				);

		if (isForQuery)	{
			JLabel conn_str = new JLabel(username.getText().toLowerCase()+"@"+
					dbname.getText().toLowerCase());
			c.gridy = 3;
			c.gridx = 1;
			cnt_pn.add(conn_str,c);
		}
		JLabel psw_lbl = new JLabel("Password:",JLabel.TRAILING);
		psw = new JPasswordField(20);
		psw_lbl.setLabelFor(psw);
		c.gridy = 4;
		c.gridx = 0;
		cnt_pn.add(psw_lbl,c);
		c.gridx = 1;
		cnt_pn.add(psw,c);
		Memory.getDbConnection().setPwd(psw.getPassword());
		psw.addActionListener(new ActionListener()	{
			public void actionPerformed(ActionEvent e){
				ok_btn.requestFocusInWindow();
			}
		}
				);
		if (!isForQuery){			
			String []allReposTypes = {DBConnection.ORACLE_REPOSITORY_DB,
					DBConnection.HSQL_LOCAL_REPOSITORY_DB,
					DBConnection.HSQL_REMOTE_REPOSITORY_DB};
			String []reposTypes = {DBConnection.ORACLE_REPOSITORY_DB};
			if (Memory.isHsqldbJARexists())	{
				reposDB = new JComboBox(allReposTypes);
				if (Memory.getEpProps().containsKey("ODA_REPOS_TYPE"))	{
					String typeDB = Memory.getEpProps().getProperty("ODA_REPOS_TYPE");
					Memory.setRepositoryType(typeDB);
					if (typeDB.equals(DBConnection.ORACLE_REPOSITORY_DB))
						reposDB.setSelectedIndex(0);
					else if (typeDB.equals(DBConnection.HSQL_LOCAL_REPOSITORY_DB))
						reposDB.setSelectedIndex(1);
					else if (typeDB.equals(DBConnection.HSQL_REMOTE_REPOSITORY_DB))
						reposDB.setSelectedIndex(2);
				}else{
					reposDB.setSelectedIndex(0);
					Memory.setRepositoryType(DBConnection.ORACLE_REPOSITORY_DB);
				}
			}else{
				reposDB = new JComboBox(reposTypes);
				reposDB.setSelectedIndex(0);
				Memory.setRepositoryType(DBConnection.ORACLE_REPOSITORY_DB);
			}
			String []statuses = {"Ready","Create","Remove"};
			status = new JComboBox(statuses);
			if (UnderSets.FIRST_PARAM.toUpperCase().equals("ADMIN")||
					UnderSets.FIRST_PARAM.equals("jvs")){				
				if ( Memory.isHsqldbJARexists()){
					JLabel reposType_lbl = new JLabel("Repository DB type:",JLabel.TRAILING);
					c.gridy = 8;
					reposDB.setPreferredSize(new Dimension(120,20));
					c.gridx = 0;
					reposType_lbl.setLabelFor(reposDB);
					cnt_pn.add(reposType_lbl,c);

					c.gridx = 1;
					c.weightx = 1.0;
					c.gridwidth = 1;
					cnt_pn.add(reposDB,c);
				}
			}
			if (Memory.getEpProps().containsKey("dbname"))
				dbname.setText(Memory.getEpProps().getProperty("dbname"));
			if (Memory.getEpProps().containsKey("hostname"))
				hostname.setText(Memory.getEpProps().getProperty("hostname"));
			if (Memory.getEpProps().containsKey("port"))
				port.setText(Memory.getEpProps().getProperty("port"));
			if (Memory.getEpProps().containsKey("username"))
				username.setText(Memory.getEpProps().getProperty("username"));
			if (!"".equals(dbname.getText())){
				hostname.requestFocusInWindow();
			}
			if (!"".equals(hostname.getText())){
				port.requestFocusInWindow();
			}
			if (!"".equals(port.getText())){
				username.requestFocusInWindow();
			}
			if (!"".equals(username.getText())){
				psw.requestFocusInWindow();
			}
		}else{
			psw.requestFocusInWindow();
		}

		JPanel buttons = new JPanel(new BorderLayout());
		ok_btn = new JButton("Ok");
		ok_btn.setPreferredSize(new Dimension(80,20));
		ok_btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (!isForQuery){
					log.trace("Current repository action '{}'",(String)status.getSelectedItem());
					Memory.getDbConnection().setDBName(dbname.getText());
					Memory.getEpProps().setProperty("dbname",dbname.getText());
					Memory.getDbConnection().setDBHost(hostname.getText());
					Memory.getEpProps().setProperty("hostname",hostname.getText());
					Memory.getDbConnection().setDBPort(port.getText());
					Memory.getEpProps().setProperty("port",port.getText());
					Memory.getDbConnection().setUsr(username.getText());
					Memory.getEpProps().setProperty("username",username.getText());
					Memory.getDbConnection().setPwd(psw.getPassword());

					Memory.setRepositoryType((String)reposDB.getSelectedItem());
					Memory.getEpProps().setProperty("ODA_REPOS_TYPE",(String)reposDB.getSelectedItem());

					Memory.getDbConnection().setConnType(Memory.getRepositoryType());

					try {
						Memory.getDbConnection().createGenDBConnection();;
					} catch (SQLException e2) {
						log.error("SQLException {}\n{}",UICommonUtil.formatMessage(e2));
					}
					EasyPostboyApp.saveProps();

					try {
						Memory.getDbConnection().createGenDBConnection();
					} catch (SQLException e2) {
						log.error("SQLException {}\n{}",UICommonUtil.formatMessage(e2));
					}

					dispose();
					log.trace("Before Current repository action '{}'",(String)status.getSelectedItem());
				}else{
					try {
						con = new DBConnection();
						con.setDBHost(hostname.getText());
						con.setDBName(dbname.getText());
						con.setDBPort(port.getText());
						con.setUsr(username.getText());
						con.setPwd(psw.getPassword());
						con.createGenDBConnection();						
						if (con.getGenDBConn() == null)	{
							psw.requestFocusInWindow();
							return;
						}
					} catch (SQLException e2) {
						log.error("SQLException {}\n{}",UICommonUtil.formatMessage(e2));
					}
					dispose();
				}
			}
		}
				);
		buttons.add(ok_btn,BorderLayout.WEST);

		cancel_btn = new JButton("Cancel");
		cancel_btn.setPreferredSize(new Dimension(80,20));
		cancel_btn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dispose();
				if (!isForQuery)
					System.exit(0);
			}
		});
		buttons.add(cancel_btn,BorderLayout.EAST);
		c.gridy = 10;
		cnt_pn.add(buttons,c);

		content.add(cnt_pn);
		setVisible(true);
		setAlwaysOnTop(true);
		setModal(true);
	}

	public DBConnection getCon() {
		return con;
	}
}
