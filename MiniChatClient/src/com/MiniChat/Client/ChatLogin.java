package com.MiniChat.Client;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;

import javax.swing.JTextField;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;


public class ChatLogin extends JFrame {
	
	private static ChatLogin frame;
	private JPanel contentPane;
	private static JTextField textAccount;
	private JLabel labAccount;
	private JLabel labPwd;
	private static JPasswordField textPwd;
	private static JCheckBox checkRemPwd;
	private static JCheckBox checkAutoLogin;
	private JButton btnRegister;
	private static JButton btnLogin;
	private String ip;
	private int port;
	private Socket client;
	private ArrayList<String> fList;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {		
		//視窗初始化
		frame = new ChatLogin();
		frame.setVisible(true);
		
		//自動登入		
		if(checkAutoLogin.isSelected() ){		
			btnLogin.doClick();
		}
	}

	/**
	 * Create the frame.
	 */
	public ChatLogin() {
		ImageIcon icon = new ImageIcon("icon/client_icon.png");
		setIconImage(icon.getImage());
		setBackground(Color.DARK_GRAY);
		setTitle("MiniChat");
		setLocationRelativeTo(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(262, 458);
		setLocationRelativeTo(this);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		//帳號標籤
		labAccount = new JLabel("帳號：");
		labAccount.setForeground(Color.WHITE);
		labAccount.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
		GridBagConstraints gbc_labAccount = new GridBagConstraints();
		gbc_labAccount.insets = new Insets(0, 0, 5, 5);
		gbc_labAccount.anchor = GridBagConstraints.WEST;
		gbc_labAccount.gridx = 1;
		gbc_labAccount.gridy = 4;
		contentPane.add(labAccount, gbc_labAccount);
		//帳號欄位
		textAccount = new JTextField();
		textAccount.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
		GridBagConstraints gbc_textAccount = new GridBagConstraints();
		gbc_textAccount.gridwidth = 3;
		gbc_textAccount.insets = new Insets(0, 0, 5, 5);
		gbc_textAccount.fill = GridBagConstraints.HORIZONTAL;
		gbc_textAccount.gridx = 2;
		gbc_textAccount.gridy = 4;
		contentPane.add(textAccount, gbc_textAccount);
		textAccount.setColumns(10);
		//密碼標籤
		labPwd = new JLabel("密碼：");
		labPwd.setForeground(Color.WHITE);
		labPwd.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
		GridBagConstraints gbc_labPwd = new GridBagConstraints();
		gbc_labPwd.anchor = GridBagConstraints.WEST;
		gbc_labPwd.insets = new Insets(0, 0, 5, 5);
		gbc_labPwd.gridx = 1;
		gbc_labPwd.gridy = 5;
		contentPane.add(labPwd, gbc_labPwd);
		//密碼欄位
		textPwd = new JPasswordField();
		textPwd.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
		GridBagConstraints gbc_textPwd = new GridBagConstraints();
		gbc_textPwd.gridwidth = 3;
		gbc_textPwd.insets = new Insets(0, 0, 5, 5);
		gbc_textPwd.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPwd.gridx = 2;
		gbc_textPwd.gridy = 5;
		contentPane.add(textPwd, gbc_textPwd);
		textPwd.setColumns(10);
		//記住密碼選項
		checkRemPwd = new JCheckBox("記住密碼");
		checkRemPwd.setForeground(Color.WHITE);
		checkRemPwd.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
		checkRemPwd.setBackground(Color.DARK_GRAY);
		GridBagConstraints gbc_checkRemPwd = new GridBagConstraints();
		gbc_checkRemPwd.gridwidth = 3;
		gbc_checkRemPwd.insets = new Insets(0, 0, 5, 5);
		gbc_checkRemPwd.gridx = 1;
		gbc_checkRemPwd.gridy = 6;
		contentPane.add(checkRemPwd, gbc_checkRemPwd);
		//自動登入選項
		checkAutoLogin = new JCheckBox("自動登入");
		checkAutoLogin.setForeground(Color.WHITE);
		checkAutoLogin.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
		checkAutoLogin.setBackground(Color.DARK_GRAY);
		GridBagConstraints gbc_checkAutoLogin = new GridBagConstraints();
		gbc_checkAutoLogin.insets = new Insets(0, 0, 5, 5);
		gbc_checkAutoLogin.gridx = 4;
		gbc_checkAutoLogin.gridy = 6;
		contentPane.add(checkAutoLogin, gbc_checkAutoLogin);
		//註冊按鈕
		btnRegister = new JButton("註冊");
		btnRegister.setForeground(Color.WHITE);
		btnRegister.setBackground(Color.GRAY);
		btnRegister.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
		GridBagConstraints gbc_btnRegister = new GridBagConstraints();
		gbc_btnRegister.gridwidth = 3;
		gbc_btnRegister.insets = new Insets(0, 0, 5, 5);
		gbc_btnRegister.gridx = 1;
		gbc_btnRegister.gridy = 8;
		contentPane.add(btnRegister, gbc_btnRegister);
		//登入按鈕
		btnLogin = new JButton("登入");
		btnLogin.setForeground(Color.WHITE);
		btnLogin.setBackground(Color.GRAY);
		btnLogin.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
		GridBagConstraints gbc_btnLogin = new GridBagConstraints();
		gbc_btnLogin.insets = new Insets(0, 0, 5, 5);
		gbc_btnLogin.gridx = 4;
		gbc_btnLogin.gridy = 8;
		contentPane.add(btnLogin, gbc_btnLogin);
		//初始化IP及Port
		if( !LoadIpAndPort() ){
			JOptionPane.showMessageDialog(null, "初始化失敗!!", "錯誤", JOptionPane.ERROR_MESSAGE );
		}		
		//連線到Server
		//connectToServer();
		
		//註冊按鈕事件
		btnRegister.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//連線	
				connectToServer();
				
				String account = textAccount.getText().trim();
				String password = String.valueOf(textPwd.getPassword()).trim();
				if( !account.equals("") && !password.equals("") ){
					if( Command("REG " + account + " " + password)){
						JOptionPane.showMessageDialog(null, "註冊成功!!", "會員註冊", JOptionPane.INFORMATION_MESSAGE );
					}
					else{
						JOptionPane.showMessageDialog(null, "註冊失敗!!", "會員註冊", JOptionPane.ERROR_MESSAGE );
					}
				}
				else{
					JOptionPane.showMessageDialog(null, "請輸入帳號密碼！！", "會員註冊", JOptionPane.ERROR_MESSAGE );
				}
				
			}
		});
		//登入按鈕事件
		btnLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				//儲存登入資料
				saveMemberData();
		
				//連線
				connectToServer();
				
				String account = textAccount.getText().trim();
				String password = String.valueOf(textPwd.getPassword()).trim();
				if( !account.equals("") && !password.equals("") ){
					if( Command("LOG " + account + " " + password)){					
						getFriendList(account);
						ChatClient cc = new ChatClient(client, account, fList);
						cc.setLocation(frame.getLocation());
						cc.setVisible(true);
						dispose();
					}
					else{
						JOptionPane.showMessageDialog(null, "登入失敗!!", "會員登入", JOptionPane.ERROR_MESSAGE );
					}
				}
				else{
					JOptionPane.showMessageDialog(null, "請輸入帳號密碼！！", "會員登入", JOptionPane.ERROR_MESSAGE );
				}
			}			
		});
		//初始化登入資訊
		if( !LoadMemberData() ){
			JOptionPane.showMessageDialog(null, "初始化失敗!!", "錯誤", JOptionPane.ERROR_MESSAGE );
		}
	}
	/**
	 * 初始化IP及Port	
	 */
	public boolean LoadIpAndPort(){
		try {
			FileReader fr = new FileReader("dat/Server.dat");
			BufferedReader bf = new BufferedReader(fr);
			
			ip = bf.readLine();
			port = Integer.parseInt( bf.readLine() );			
			fr.close();	
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 初始化登入資訊	
	 */
	public static boolean LoadMemberData(){
		try {
			FileReader fr = new FileReader("dat/User.dat");
			BufferedReader bf = new BufferedReader(fr);
			
			
			if( bf.readLine().equals("true") )
				checkRemPwd.setSelected(true);
			else{
				checkRemPwd.setSelected(false);
				textAccount.setText("");
				textPwd.setText("");
			}
				
			
			if( bf.readLine().equals("true") )
				checkAutoLogin.setSelected(true);
			else
				checkAutoLogin.setSelected(false);
			
			if( checkRemPwd.isSelected() ){				
				textAccount.setText( bf.readLine() );
				textPwd.setText( bf.readLine() );
			}
			fr.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 儲存登入資訊
	 */
	public boolean saveMemberData(){
		try {
			FileWriter fw = new FileWriter("dat/User.dat",false);
			fw.write( checkRemPwd.isSelected() + "\r\n" );
			fw.write( checkAutoLogin.isSelected() + "\r\n" );
			fw.write( textAccount.getText() + "\r\n" );
			fw.write( String.valueOf(textPwd.getPassword()) + "\r\n" );			
			fw.flush();
			fw.close();	
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 傳送指令給Server
	 */
	public boolean Command(String cmd){
		
		try {			
			//傳送帳號資訊
			DataOutputStream out = new DataOutputStream(client.getOutputStream());
			out.writeUTF(cmd);
            out.flush();
            //接收回傳訊息
            DataInputStream in = new DataInputStream(client.getInputStream());
            return in.readBoolean();
		} catch (IOException e) {	
			e.printStackTrace();			
		}
		return false;
	}
	/**
	 * 取得好友列表
	 */
	public boolean getFriendList(String account){
		try {
			fList = new ArrayList<String>();
			//傳送帳號資訊
			DataOutputStream out = new DataOutputStream(client.getOutputStream());
			out.writeUTF("RFL " + account);
	        out.flush();
			//接收回傳訊息
			int friNum;						
			DataInputStream in = new DataInputStream(client.getInputStream());
			friNum = in.readInt();
			for(int i = 0 ; i < friNum ; i++ ){
				String friTmp = in.readUTF();
				fList.add( friTmp );
			}
			return in.readBoolean();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 連線到Server
	 */
	public boolean connectToServer(){
		try {
			client = new Socket();
			InetSocketAddress isa = new InetSocketAddress(ip, port);
			client.connect(isa,10000);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
