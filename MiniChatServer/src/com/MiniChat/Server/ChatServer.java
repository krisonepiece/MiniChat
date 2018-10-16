package com.MiniChat.Server;


import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import java.awt.Color;
import java.awt.GridBagLayout;

import javax.swing.JTextArea;

import java.awt.GridBagConstraints;

import javax.swing.JButton;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JList;

import java.awt.Font;

public class ChatServer extends JFrame {

	private JPanel contentPane;	
	private static JTextPane textPrint;		//顯示文字區域
	private static JList<String> listMember;		//會員檢視清單	
	private static JTextArea textMsg;		//訊息文字區域
	private static JButton btnSendMsg;		//送出廣播按鈕
	private static JButton btnAddMember;	//新增會員按鈕
	private static JButton btnEditMember;	//修改會員按鈕
	private static JButton btnDelMember;	//刪除會員按鈕
	private static JButton btnDisconnect;	//中斷連線按鈕
	private static ArrayList<Socket> clientList = new ArrayList<Socket>();
	private static ServerSocket server;
	private static int ServerPort = 8888;
	private static Socket socket;
	private static ArrayList<Member> mList;
	private static OnlineData onlineData;
	private static DefaultListModel<String> memberListModel;
	private static UiController uiController;
	private static SimpleAttributeSet attrset;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		//初始化視窗		
		ChatServer frame = new ChatServer();
		frame.setVisible(true);	
		
		//載入UI控制
		uiController = new UiController(textPrint, listMember, memberListModel);
		
		//載入會員資料
		LoadMemberData();
		for(int i = 0 ; i < mList.size() ; i++){
			memberListModel.addElement(mList.get(i).account + " (離線)");
		}		
		onlineData = new OnlineData();		
	
		
		//建立連線
		socket = null;
		try {
			server = new ServerSocket(ServerPort);
			attrset = new SimpleAttributeSet();
			StyleConstants.setForeground(attrset, Color.blue);
			StyleConstants.setBold(attrset, true);
			insert("[Server] : Server Start!!", attrset);
			while(true){
				/*attrset = new SimpleAttributeSet();
				StyleConstants.setForeground(attrset, Color.blue);
				StyleConstants.setBold(attrset, true);
				insert("[Server] : Wait a new Client...", attrset);*/
				socket = server.accept();
				clientList.add(socket);
				ClientT t = new ClientT(socket, mList, uiController, onlineData);
				t.start();
			}
		} catch (IOException e) {
			attrset = new SimpleAttributeSet();
			StyleConstants.setForeground(attrset, Color.blue);
			StyleConstants.setBold(attrset, true);
			insert("[Server] : ERROR " + e.toString(), attrset);
			
			//textPrint.append("[Server] : ERROR\n" + e.toString());
			e.printStackTrace();
		}		
	}

	/**
	 * Create the frame.
	 */
	public ChatServer() {
		ImageIcon icon = new ImageIcon("icon/server_icon.png");
		setIconImage(icon.getImage());
		setTitle("MiniChatServer");		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(660, 463);
		setLocationRelativeTo(this);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, 1.0, 0.2, 0.3, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0};
		contentPane.setLayout(gbl_contentPane);
		//顯示文字區域
		textPrint = new JTextPane();
		textPrint.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
		textPrint.setEditable(false);
		textPrint.setText("");
		textPrint.setAutoscrolls(true);
		JScrollPane scrollpane = new JScrollPane(textPrint,
				 ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				 ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); 
		GridBagConstraints gbc_textPrint = new GridBagConstraints();
		gbc_textPrint.gridheight = 3;
		gbc_textPrint.gridwidth = 4;
		gbc_textPrint.insets = new Insets(0, 0, 5, 5);
		gbc_textPrint.fill = GridBagConstraints.BOTH;
		gbc_textPrint.gridx = 0;
		gbc_textPrint.gridy = 0;
		contentPane.add(scrollpane, gbc_textPrint);
		//會員檢視清單
		listMember = new JList();
		listMember.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
		memberListModel = new DefaultListModel<String>();
		listMember.setModel(memberListModel);
		listMember.setToolTipText("");
		GridBagConstraints gbc_listMember = new GridBagConstraints();
		gbc_listMember.insets = new Insets(0, 0, 5, 0);
		gbc_listMember.fill = GridBagConstraints.BOTH;
		gbc_listMember.gridx = 4;
		gbc_listMember.gridy = 0;
		contentPane.add(listMember, gbc_listMember);
		//送出廣播按鈕
		btnSendMsg = new JButton("傳送");
		btnSendMsg.setForeground(Color.WHITE);
		btnSendMsg.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
		btnSendMsg.setBackground(Color.GRAY);
		GridBagConstraints gbc_btnSendMsg = new GridBagConstraints();
		gbc_btnSendMsg.fill = GridBagConstraints.BOTH;
		gbc_btnSendMsg.gridheight = 2;
		gbc_btnSendMsg.insets = new Insets(0, 0, 5, 5);
		gbc_btnSendMsg.gridx = 3;
		gbc_btnSendMsg.gridy = 3;
		contentPane.add(btnSendMsg, gbc_btnSendMsg);
		//訊息文字區域
		textMsg = new JTextArea();
		textMsg.setFont(new Font("微軟正黑體", Font.PLAIN, 13));
		GridBagConstraints gbc_textMsg = new GridBagConstraints();
		gbc_textMsg.gridwidth = 3;
		gbc_textMsg.gridheight = 2;
		gbc_textMsg.insets = new Insets(0, 0, 5, 5);
		gbc_textMsg.fill = GridBagConstraints.BOTH;
		gbc_textMsg.gridx = 0;
		gbc_textMsg.gridy = 3;
		contentPane.add(textMsg, gbc_textMsg);
		//新增會員按鈕
		btnAddMember = new JButton("新增會員");
		btnAddMember.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
		btnAddMember.setForeground(Color.WHITE);
		btnAddMember.setBackground(Color.GRAY);
		GridBagConstraints gbc_btnAddMember = new GridBagConstraints();
		gbc_btnAddMember.insets = new Insets(0, 0, 5, 0);
		gbc_btnAddMember.gridx = 4;
		gbc_btnAddMember.gridy = 1;
		//contentPane.add(btnAddMember, gbc_btnAddMember);
		//修改會員按鈕
		btnEditMember = new JButton("修改會員");
		btnEditMember.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
		btnEditMember.setForeground(Color.WHITE);
		btnEditMember.setBackground(Color.GRAY);
		GridBagConstraints gbc_btnEditMember = new GridBagConstraints();
		gbc_btnEditMember.insets = new Insets(0, 0, 5, 0);
		gbc_btnEditMember.gridx = 4;
		gbc_btnEditMember.gridy = 2;
		//contentPane.add(btnEditMember, gbc_btnEditMember);
		//刪除會員按鈕
		btnDelMember = new JButton("刪除會員");
		btnDelMember.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
		btnDelMember.setForeground(Color.WHITE);
		btnDelMember.setBackground(Color.GRAY);
		GridBagConstraints gbc_btnDelMember = new GridBagConstraints();
		gbc_btnDelMember.insets = new Insets(0, 0, 5, 0);
		gbc_btnDelMember.gridx = 4;
		gbc_btnDelMember.gridy = 3;
		//contentPane.add(btnDelMember, gbc_btnDelMember);
		//中斷連線按鈕
		btnDisconnect = new JButton("中斷連線");
		btnDisconnect.setForeground(Color.WHITE);
		btnDisconnect.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
		btnDisconnect.setBackground(Color.GRAY);
		GridBagConstraints gbc_btnDisconnect = new GridBagConstraints();
		gbc_btnDisconnect.gridx = 4;
		gbc_btnDisconnect.gridy = 4;
		//contentPane.add(btnDisconnect, gbc_btnDisconnect);
		
		//送出廣播訊息
		btnSendMsg.addActionListener(new ActionListener() {					
			@Override
			public void actionPerformed(ActionEvent e) {
				DataOutputStream out;
				try {
														
					String mesg = textMsg.getText();
					for(int i = 0 ; i < onlineData.size() ; i++){
						out = new java.io.DataOutputStream(onlineData.get(i).getSocket().getOutputStream());
						out.writeUTF("SBD " + mesg);
					}	
					attrset = new SimpleAttributeSet();
					StyleConstants.setForeground(attrset, Color.blue);
					StyleConstants.setBold(attrset, true);
					insert("[Boardcast] : " + mesg, attrset);
					textMsg.setText("");
				} catch (IOException e1) {
					e1.printStackTrace();
				}						
			}
		});
	}
	/**
	 * 載入會員資料
	 */
	public static void LoadMemberData(){
		FileReader frM;
		FileReader frF;
		mList = new ArrayList<Member>();
		try {
			//讀取會員資料
			frM = new FileReader("dat/Member.dat");	
			BufferedReader brM = new BufferedReader(frM);
			
			String mesg;			
	        while ((mesg = brM.readLine()) != null) {
	        	StringTokenizer stk = new StringTokenizer(mesg); 
	        	ArrayList<Integer> fList = new ArrayList<Integer>();
	        	Member m = new Member(Integer.parseInt(stk.nextToken().trim()), stk.nextToken().trim(),
	        							stk.nextToken().trim(), fList);
	        	mList.add(m);
	        }
	        frM.close();
	        //讀取好友資料	        
	        frF = new FileReader("dat/Friend.dat");	
	        BufferedReader brF = new BufferedReader(frF);
	        int id;
	        int idF;
	        ArrayList<Integer> tmpList;
	        while ((mesg = brF.readLine()) != null) {
	        	StringTokenizer stk = new StringTokenizer(mesg);
	        	id = Integer.parseInt( stk.nextToken().trim() ); 
	        	tmpList = new ArrayList<Integer>();
	        	while( stk.hasMoreTokens() ){
	        		idF = Integer.parseInt( stk.nextToken().trim() );
	        		tmpList.add( new Integer(idF) );		        		
	        	}
	        	if( tmpList.size() != 0){
	        		for(int i = 0 ; i < mList.size() ; i++){
		        		if( mList.get(i).getId() == id ){
		        			for(int j = 0 ; j < tmpList.size() ; j++){
		        				mList.get(i).getFriends().add( tmpList.get(j) );
		        			}	        			
			        	}
		        	}
	        	}
	        }
	        frF.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	/**
	 * 將字串插入JTextPane中
	 */
	public static void insert(String str, AttributeSet attrset) {
		Document docs = textPrint.getDocument();// 利用getDocument()方法取得JTextPane的Document
												// instance.0
		str = str + "\n";
		try {			
			docs.insertString(docs.getLength(), str, attrset);
			textPrint.setCaretPosition(docs.getLength()); //自動捲動到底部
		} catch (BadLocationException ble) {
			System.out.println("BadLocationException:" + ble);
		}
	}
}
