package wizards.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.*;

import wizards.constants.HTML;
import wizards.entities.Avatar;
import wizards.entities.PointF;
import wizards.entities.World;
import wizards.messages.Location;

public class ClientGUI extends JFrame{
	
	public static final String TITLE="Wizards 1.0";
	private static final String TITLE_MESSAGE=HTML.HTML+HTML.CENTER_TEXT+"Welcome to Wizards!" +
			HTML.NEW_LINE+"Enter your username and IP below:"+HTML.END_HTML;
	private static final String USERNAME_MESSAGE="Username: ";
	private static final String IP_MESSAGE="IP: ";
	private static final String CONNECT_TEXT="Connect";
	
	private static final String WORLD_TITLE="Wizard";
	private static final String LEVEL_LABEL="Level ";
	public static final int WIDTH=800;
	public static final int HEIGHT=600;
	public static final int UF_WIDTH=WIDTH*3/8;
	public static final int UF_HEIGHT=HEIGHT/12;
	public static final int T_WIDTH=WIDTH/4;
	public static final int T_HEIGHT=HEIGHT/6;
	public static final int CB_WIDTH=WIDTH/6;
	public static final int CB_HEIGHT=HEIGHT/12;
	public static final int AJP_WIDTH=WIDTH/4;
	public static final int AJP_HEIGHT=HEIGHT;
	public static final Color AJP_BACKGROUND_COLOR=new Color(200,200,200);
	
	public static final Color BACKGROUND_COLOR=Color.CYAN;
	
	private static JButton connect=new JButton(CONNECT_TEXT);
	private JLabel titleL=new JLabel(TITLE_MESSAGE);
	private JLabel usernameL=new JLabel(USERNAME_MESSAGE);
	private JLabel ipL=new JLabel(IP_MESSAGE);
	private JTextField usernameTF=new JTextField();
	private JTextField ipTF=new JTextField();
	private JPanel userInfo=new JPanel();
	private GridLayout usernameLayout=new GridLayout(2,2);
	
	private String username=null;
	private String ip=null;
	private JLabel avatarTitleL;
	private JLabel avatarUsernameL;
	private JLabel avatarLevelL;
	private WorldGUI worldGUI=new WorldGUI();
	private JPanel avatarGUI;
	
	
	private boolean inWorld=false;
	public ClientGUI(){
		init();
		
	}
	private void init() {
		initUsernameComponents();
		initWindow();
		
	}
	
	private void initWindow() {
		setTitle(TITLE);
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setFocusable(true);
		getContentPane().setBackground(BACKGROUND_COLOR);
		getContentPane().setLayout(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);
	}
	private void initUsernameComponents() {
		titleL.setHorizontalAlignment(JLabel.CENTER);
		getContentPane().add(titleL);
		titleL.setBounds(WIDTH/2-T_WIDTH/2, HEIGHT/5-T_HEIGHT/2, T_WIDTH, T_HEIGHT);
		
		usernameTF.requestFocus();
		usernameTF.setText("");
		usernameTF.setBackground(new Color(180,240,0));
		usernameTF.setHorizontalAlignment(JTextField.CENTER);
		
		ipTF.setText("");
		ipTF.setBackground(new Color(80,240,80));
		ipTF.setHorizontalAlignment(JTextField.CENTER);
		
		usernameL.setHorizontalAlignment(JLabel.CENTER);
		
		ipL.setHorizontalAlignment(JLabel.CENTER);
		
		userInfo.setLayout(usernameLayout);
		userInfo.setBackground(BACKGROUND_COLOR);
		userInfo.add(usernameL);
		userInfo.add(usernameTF);
		userInfo.add(ipL);
		userInfo.add(ipTF);
		
		getContentPane().add(userInfo);
		userInfo.setBounds(WIDTH/2-UF_WIDTH/2,HEIGHT/3-UF_HEIGHT/2,UF_WIDTH,UF_HEIGHT);
		
		connect.setFont(new Font("Arial",Font.PLAIN,16));
		connect.setForeground(new Color(255,255,0));
		connect.setBackground(new Color(0,80,210));
		connect.setHorizontalAlignment(JButton.CENTER);
		connect.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {
				ip=ipTF.getText();
				username=usernameTF.getText();
				if(ip.equals(null)||ip.equals("")||username.equals(null)||username.equals(""))return;
				try {
					System.out.println(ip);
					Client.connect(InetAddress.getByName(ip),username);
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		});
		getContentPane().add(connect);
		connect.setBounds(WIDTH/2-CB_WIDTH/2,HEIGHT*2/3-CB_HEIGHT/2,CB_WIDTH,CB_HEIGHT);
	}
	
	public void goToWorld() {
		inWorld=true;
		getContentPane().removeAll();
		
		getContentPane().add(worldGUI);
		worldGUI.setBounds(0,0,WorldGUI.WIDTH, WorldGUI.HEIGHT);
		worldGUI.setUsername(username);
		
		avatarUsernameL=new JLabel(username);
		avatarUsernameL.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		avatarUsernameL.setFont(new Font("Arial",Font.BOLD,24));
		avatarUsernameL.setForeground(Color.BLUE);
		avatarTitleL=new JLabel(WORLD_TITLE);
		avatarTitleL.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		avatarLevelL=new JLabel(LEVEL_LABEL+"1");
		avatarLevelL.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		
		avatarGUI=new JPanel();
		BoxLayout avatarBL=new BoxLayout(avatarGUI,BoxLayout.Y_AXIS);
		avatarGUI.setLayout(avatarBL);
		avatarGUI.setBackground(AJP_BACKGROUND_COLOR);
		avatarGUI.add(avatarUsernameL);
		avatarGUI.add(avatarTitleL);
		avatarGUI.add(avatarLevelL);
		
		avatarGUI.setBounds(WIDTH*3/4, 0, AJP_WIDTH, AJP_HEIGHT);
		avatarGUI.setVisible(false);
		getContentPane().add(avatarGUI);
		
		avatarGUI.repaint();
		avatarGUI.setVisible(true);
		repaint();
	}
	public WorldGUI getWorldGUI(){return worldGUI;}
	public void updateWorldGUI(World w){
		if(worldGUI!=null)
			worldGUI.setWorld(w);
	}
	public void updateWorldGUI(Location l){
		if(worldGUI!=null)
			worldGUI.updateAvatarLocation(l);
	}
}
