package com.MiniChat.Client;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridBagLayout;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.awt.Color;
import java.awt.Font;

public class ChatClient extends JFrame {
	

	private String account;
	private ArrayList<String> fList;
	private Socket client;	
	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu menuFriend;
	private JMenu menuChat;
	private JMenuItem itemAddFriend;
	private JMenuItem itemDelFriend;
	private JMenuItem itemBeginChat;
	private JMenuItem itemOldMesg;
	private JMenuItem menuLogout;
	private JMenuItem menuExit;
	private JList<String> listFriend;
	private DefaultListModel<String> friendListModel;
	private ArrayList<ChatRoom> listRoom;
	private UiController uiController;
	private ServerT t;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		ChatClient cc = new ChatClient(null, "1234", null);
		cc.setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public ChatClient(Socket client, String account, ArrayList<String> fList) {
		ImageIcon ico = new ImageIcon("icon/client_icon.png");
		setIconImage(ico.getImage());
		setBackground(Color.DARK_GRAY);
		//參數初始化
		this.client = client;
		this.account = account;
		this.fList = fList;
		listRoom = new ArrayList<ChatRoom>();
		
		//視窗初始化
		setTitle(account);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 296, 512);	
		
		//如果按下視窗右上角的x的話
		this.addWindowListener(new WindowAdapter() {		              
            public void windowClosing(WindowEvent e) {
            	try {
					client.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
            	dispose();
            }
		 });
		
		menuBar = new JMenuBar();
		menuBar.setBackground(Color.DARK_GRAY);
		menuBar.setToolTipText("");
		setJMenuBar(menuBar);
		
		menuFriend = new JMenu("好友");
		menuFriend.setForeground(Color.WHITE);
		menuFriend.setBackground(Color.DARK_GRAY);
		menuBar.add(menuFriend);
		
		itemAddFriend = new JMenuItem("新增好友");
		itemAddFriend.setForeground(Color.WHITE);
		itemAddFriend.setBackground(Color.GRAY);
		menuFriend.add(itemAddFriend);
		
		itemDelFriend = new JMenuItem("刪除好友");
		itemDelFriend.setForeground(Color.WHITE);
		itemDelFriend.setBackground(Color.GRAY);
		menuFriend.add(itemDelFriend);
		
		menuChat = new JMenu("聊天");
		menuChat.setForeground(Color.WHITE);
		menuChat.setBackground(Color.DARK_GRAY);
		menuBar.add(menuChat);
		
		itemBeginChat = new JMenuItem("開始聊天");
		itemBeginChat.setForeground(Color.WHITE);
		itemBeginChat.setBackground(Color.GRAY);
		menuChat.add(itemBeginChat);
		
		itemOldMesg = new JMenuItem("歷史訊息");
		itemOldMesg.setForeground(Color.WHITE);
		itemOldMesg.setBackground(Color.GRAY);
		//menuChat.add(itemOldMesg);
		
		menuLogout = new JMenuItem("登出");
		menuLogout.setForeground(Color.WHITE);
		menuLogout.setBackground(Color.DARK_GRAY);
		menuBar.add(menuLogout);
		
		menuExit = new JMenuItem("離開");
		menuExit.setForeground(Color.WHITE);
		menuExit.setBackground(Color.DARK_GRAY);
		menuBar.add(menuExit);
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		listFriend = new JList<String>();
		listFriend.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
		friendListModel = new DefaultListModel<String>();
		listFriend.setModel(friendListModel);
		GridBagConstraints gbc_listFriend = new GridBagConstraints();
		gbc_listFriend.fill = GridBagConstraints.BOTH;
		gbc_listFriend.gridx = 0;
		gbc_listFriend.gridy = 0;		
		contentPane.add(listFriend, gbc_listFriend);	
		
		
		//載入UI控制
		uiController = new UiController(listFriend,	friendListModel, listRoom);				
		
		//開始監聽Server
		t = new ServerT(client, account, fList, listRoom, uiController);
		t.start();
		
		//更新好友列表
		for(int i = 0 ; i < fList.size() ; i++){
			friendListModel.addElement(fList.get(i) +  " (離線)");
		}		
		requestOnlineFriend(account);
		
		//新增好友按鈕事件		
		itemAddFriend.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String result = JOptionPane.showInputDialog(null, "請輸入好友帳號：", "新增好友");
				Command("ADD " + account + " " + result);
			}
		});
		//刪除好友按鈕事件		
		itemDelFriend.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String friend = listFriend.getSelectedValue();
				StringTokenizer stk = new StringTokenizer(friend);
				friend = stk.nextToken();
				Command("DEL " + account + " " + friend);
				fList.remove(friend);
				for(int j = 0 ; j < friendListModel.getSize() ; j++){
					String tmpFriend = friendListModel.getElementAt(j);
					StringTokenizer stkFriend = new StringTokenizer(tmpFriend);
					tmpFriend = stkFriend.nextToken();
					if( friend.equals(tmpFriend) ){
						friendListModel.removeElementAt(j);
						break;
					}
				}
			}
		});
		//開始聊天按鈕事件		
		itemBeginChat.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String friend = listFriend.getSelectedValue();
				StringTokenizer stk = new StringTokenizer(friend);
				friend = stk.nextToken();
				ChatRoom cr;
				if( (cr = roomIsVisible(friend)) == null ){
					cr = new ChatRoom(client, account, friend);					
					listRoom.add(cr);
				}				
				cr.setLocation(getLocation());
				cr.setVisible(true);					
			}
		});
		//登出按鈕事件
		menuLogout.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				for(int i = 0 ; i < listRoom.size() ; i++){
					listRoom.get(i).dispose();
				}
				ChatLogin cl = new ChatLogin();
				cl.setLocation(getLocation());
				cl.setVisible(true);
				dispose();
			}			
		});
		//離開按鈕事件
		menuExit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					client.close();
					System.exit(0);
				} catch (IOException e) {
					e.printStackTrace();
				}						
			}			
		});
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
			//DataInputStream in = new DataInputStream(client.getInputStream());
			//Boolean tmpBool = in.readBoolean();
            return true;
            
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
	 * 要求線上好友名單
	 */
	public void requestOnlineFriend(String account){
		try {
			//傳送帳號資訊
			DataOutputStream out = new DataOutputStream(client.getOutputStream());
			out.writeUTF("ROF " + account);
            out.flush();

		} catch (IOException e) {	
			e.printStackTrace();			
		}
	}
	/**
	 * 檢查聊天視窗是否開啟
	 */
	public ChatRoom roomIsVisible(String friend){
		for(int i = 0 ; i < listRoom.size() ; i++){
			if( listRoom.get(i).getFriend().equals(friend) ){
				return listRoom.get(i);
			}
		}	
		return null;
	}
}
