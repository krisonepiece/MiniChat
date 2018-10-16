package com.MiniChat.Client;

import java.awt.Color;

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

import java.awt.GridBagLayout;

import javax.swing.JTextArea;

import java.awt.GridBagConstraints;

import javax.swing.JButton;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.awt.Font;

public class ChatRoom extends JFrame {

	private JPanel contentPane;
	private JTextPane textPrint;		//顯示文字區域		
	private JTextArea textMsg;		//訊息文字區域
	private JButton btnGame;			//啟動遊戲按鈕
	private JButton btnSendMsg;		//傳送訊息按鈕
	private SimpleAttributeSet attrset;	//JTextPane文字格式
	private Socket socket;
	private String account;			//使用者帳號	
	private String friend;			//好友帳號
	private PssGame pssGame;		//猜拳遊戲資料
	private JButton btnScissors;	//出剪刀按鈕
	private JButton btnStone;		//出石頭按鈕
	private JButton btnPaper;		//出布按鈕
	private int accFontSize = 12;
	private int friFontSize = 12;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		ChatRoom cr = new ChatRoom(null, "1234", "123");
		cr.setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public ChatRoom(Socket socket, String account, String friend) {
		//參數初始化
		this.socket = socket;
		this.account = account;
		this.friend = friend;
		
		//視窗初始化
		ImageIcon ico = new ImageIcon("icon/client_icon.png");
		setIconImage(ico.getImage());
		setTitle("ChatRoom - " + friend);
		//setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 627, 490);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.1, 0.1, 0.1, 0.1, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.1, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 0.0, 0.1, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		this.addWindowListener(new WindowAdapter() {	//如果按下視窗右上角的x的話	              
              public void windowClosing(WindowEvent e) {
            	  dispose();
              }
		});

		textPrint = new JTextPane();
		textPrint.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
		textPrint.setEditable(false);
		textPrint.setText("");
		textPrint.setAutoscrolls(true);
		JScrollPane scrollpane = new JScrollPane(textPrint,
				 ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				 ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); 
		GridBagConstraints gbc_textPrint = new GridBagConstraints();
		gbc_textPrint.gridwidth = 11;
		gbc_textPrint.insets = new Insets(0, 0, 5, 0);
		gbc_textPrint.fill = GridBagConstraints.BOTH;
		gbc_textPrint.gridx = 0;
		gbc_textPrint.gridy = 0;
		contentPane.add(scrollpane, gbc_textPrint);
		
		btnGame = new JButton("嘲諷");		
		btnGame.setForeground(Color.WHITE);
		btnGame.setBackground(Color.GRAY);
		btnGame.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
		GridBagConstraints gbc_btnGame = new GridBagConstraints();
		gbc_btnGame.insets = new Insets(0, 0, 5, 5);
		gbc_btnGame.gridx = 0;
		gbc_btnGame.gridy = 1;
		contentPane.add(btnGame, gbc_btnGame);
		
		ImageIcon icoScissors = new ImageIcon("icon/scissors.png");
		btnScissors = new JButton(icoScissors);	
		btnScissors.setBackground(Color.GRAY);
		//btnScissors.setContentAreaFilled(false);
		btnScissors.setMargin(new Insets(0,0,0,0));
		GridBagConstraints gbc_btnScissors = new GridBagConstraints();
		gbc_btnScissors.insets = new Insets(0, 0, 5, 5);
		gbc_btnScissors.gridx = 1;
		gbc_btnScissors.gridy = 1;
		contentPane.add(btnScissors, gbc_btnScissors);
		
		ImageIcon icoStone = new ImageIcon("icon/stone.png");
		btnStone = new JButton(icoStone);
		btnStone.setBackground(Color.GRAY);
		//btnStone.setContentAreaFilled(false);
		btnStone.setMargin(new Insets(0,0,0,0));
		GridBagConstraints gbc_btnStone = new GridBagConstraints();
		gbc_btnStone.insets = new Insets(0, 0, 5, 5);
		gbc_btnStone.gridx = 2;
		gbc_btnStone.gridy = 1;
		contentPane.add(btnStone, gbc_btnStone);
		
		ImageIcon icoPaper = new ImageIcon("icon/paper.png");		
		btnPaper = new JButton(icoPaper);
		btnPaper.setBackground(Color.GRAY);
		//btnPaper.setContentAreaFilled(false);
		btnPaper.setMargin(new Insets(0,0,0,0));
		GridBagConstraints gbc_btnPaper = new GridBagConstraints();
		gbc_btnPaper.insets = new Insets(0, 0, 5, 5);
		gbc_btnPaper.gridx = 3;
		gbc_btnPaper.gridy = 1;
		contentPane.add(btnPaper, gbc_btnPaper);
		
		textMsg = new JTextArea();
		textMsg.setFont(new Font("微軟正黑體", Font.PLAIN, 13));
		textMsg.setText("");
		GridBagConstraints gbc_textMsg = new GridBagConstraints();
		gbc_textMsg.gridwidth = 10;
		gbc_textMsg.insets = new Insets(0, 0, 0, 5);
		gbc_textMsg.fill = GridBagConstraints.BOTH;
		gbc_textMsg.gridx = 0;
		gbc_textMsg.gridy = 2;
		contentPane.add(textMsg, gbc_textMsg);
		
		btnSendMsg = new JButton("傳送");
		btnSendMsg.setForeground(Color.WHITE);
		btnSendMsg.setBackground(Color.GRAY);
		btnSendMsg.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
		GridBagConstraints gbc_btnSendMsg = new GridBagConstraints();
		gbc_btnSendMsg.fill = GridBagConstraints.BOTH;
		gbc_btnSendMsg.gridx = 10;
		gbc_btnSendMsg.gridy = 2;
		contentPane.add(btnSendMsg, gbc_btnSendMsg);
		
		//傳送按鈕事件
		btnSendMsg.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String mesg = textMsg.getText();
				if( Command("SMG " + account + " " + friend + " " + mesg)){
					attrset = gameAccRule();
					insert(account + ": " + mesg, attrset);

					textMsg.setText("");
				}
				else{
					sendMesgError();
				}
			}
		});
		//剪刀按鈕事件
		btnScissors.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				createPssGame(account, friend, "scissors");
			}
		});
		//石頭按鈕事件
		btnStone.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {				
				createPssGame(account, friend, "stone");				
			}
		});
		//布按鈕事件
		btnPaper.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {				
				createPssGame(account, friend, "paper");
			}
		});
		//嘲諷按鈕事件
		btnGame.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				createPssGame(account);
				
				int winSubLose = pssGame.getWin() - pssGame.getLose();
				//勝場減敗場高於2, 才可使用嘲諷功能
				if( winSubLose < 2){
					attrset = new SimpleAttributeSet();
					ImageIcon icoFace = new ImageIcon("icon/face.png");	
					textPrint.insertIcon(icoFace);
					insert("", attrset);	
					StyleConstants.setForeground(attrset, Color.red);
					StyleConstants.setBold(attrset, true);
					insert("[System] 您被反嘲諷了！！", attrset);					
				}
				else if( winSubLose < 4){
					appendLolMesg("icon/duncan.png");
					Command("LOL " + account + " " + friend);
				}
				else if( winSubLose < 6){
					appendLolMesg("icon/bye.png");
					Command("LOL " + account + " " + friend);
				}
				else if( winSubLose < 8){
					appendLolMesg("icon/man.png");
					Command("LOL " + account + " " + friend);
				}
				else if( winSubLose >= 8){
					appendLolMesg("icon/egg.gif");
					Command("LOL " + account + " " + friend);
				}
				
				/*
				if( pssGame == null ){
					createPssGame(account);					
				}
				insert(pssGame.getWin() + " : " + pssGame.getLose(), attrset);
				*/	
			}
		});
	}
	
	/**
	 * 傳送指令給Server
	 */
	public boolean Command(String cmd){
		
		try {			
			//傳送帳號資訊
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			out.writeUTF(cmd);
            out.flush();
            //接收回傳訊息
            /*DataInputStream in = new DataInputStream(socket.getInputStream());
            return in.readBoolean();*/
            return true;
		} catch (IOException e) {	
			e.printStackTrace();			
		}
		return false;
	}

	public PssGame getPssGame() {
		return pssGame;
	}

	public void setPssGame(PssGame pssGame) {
		this.pssGame = pssGame;
	}

	public String getAccount() {
		return account;
	}

	public String getFriend() {
		return friend;
	}
	//顯示聊天訊息
	public void appendTextPrint(String mesg) {		
		attrset = gameFriRule();
		insert(mesg, attrset);
	}
	//顯示嘲諷訊息
	public void appendLolMesg(String path) {
		ImageIcon ico = new ImageIcon(path);	
		textPrint.insertIcon(ico);
		attrset = new SimpleAttributeSet();
		insert("", attrset);	
	}
	//顯示系統訊息
	public void appendSystemMesg(String mesg) {
		attrset = new SimpleAttributeSet();
		StyleConstants.setForeground(attrset, Color.red);
		StyleConstants.setBold(attrset, true);
		insert(mesg, attrset);
	}
	//顯示廣播訊息
	public void appendBoardcast(String mesg) {
		attrset = new SimpleAttributeSet();
		StyleConstants.setForeground(attrset, Color.blue);
		StyleConstants.setBold(attrset, true);
		insert(mesg, attrset);
	}
	//顯示遊戲訊息
	public void appendPssMesg(String player, String hand) {
		String tmpHand;
		String tmpPlayer;
		if( hand.equals("scissors") )
			tmpHand = "出 [剪刀] ！";
		else if( hand.equals("stone") )
			tmpHand = "出 [石頭] ！";
		else if( hand.equals("paper") )
			tmpHand = "出 [布] ！";
		else
			tmpHand = "發出猜拳挑戰！";
		
		if( player.equals(account) )
			tmpPlayer = "您";
		else
			tmpPlayer = player + " 向您";
		
		attrset = new SimpleAttributeSet();
		StyleConstants.setForeground(attrset, Color.GRAY);
		StyleConstants.setBold(attrset, true);
		insert("＊Game＊  " + tmpPlayer + tmpHand, attrset);
	}
	//顯示遊戲結果
		public void appendPssResult(int result) {
			String tmpResult;
			String tmpPlayer;
			if( result == 1 )
				tmpResult = "恭喜您獲勝了！";
			else if( result == -1 )
				tmpResult = "真可惜您輸了！";
			else if( result == 0 )
				tmpResult = "平分秋色！";
			else
				tmpResult = "錯誤！";

			attrset = new SimpleAttributeSet();
			StyleConstants.setForeground(attrset, Color.DARK_GRAY);
			StyleConstants.setBold(attrset, true);
			insert("＊Game＊  " + tmpResult, attrset);
			PssGameSwitch(true);
		}
	
	public void btnSetEnabled(boolean enabled) {
		btnSendMsg.setEnabled(enabled);
	}
	/**
	 * 將字串插入JTextPane中
	 */
	public void insert(String str, AttributeSet attrset) {
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
	/**
	 * 發送訊息失敗
	 */
	public void sendMesgError() {
		attrset = new SimpleAttributeSet();
		StyleConstants.setForeground(attrset, Color.red);
		StyleConstants.setBold(attrset, true);
		StyleConstants.setItalic(attrset, true);
		insert("[System] : 訊息發送失敗！！", attrset);
	}
	/**
	 * 建立猜拳遊戲
	 */
	public void createPssGame(String account) {
		if( pssGame == null )
			pssGame = new PssGame(account);
	}

	/**
	 * 建立猜拳遊戲
	 */
	public void createPssGame(String account, String friend, String hand) {
		PssGameSwitch(false);
		appendPssMesg(account, hand);	//顯示出拳
		if( pssGame == null ){
			pssGame = new PssGame(account);
			Command("PSS " + account + " " + friend + " " + hand);	//建立遊戲
			pssGame.setHand(hand);
		}
		else{
			if( pssGame.isPlaying() ){	//回覆遊戲
				appendPssMesg(friend, pssGame.getHand());	//顯示好友出拳
				int result;
				result = pssGame.duel(hand);
				if( result == 1 ){			//遊戲勝利
					pssGame.Win();
					appendPssResult(1);
				}
				else if( result == -1 ){	//遊戲失敗
					pssGame.Lose();
					appendPssResult(-1);
				}
				else{						//平手
					appendPssResult(0);	
				}					
				Command("PSSR " + account + " " + friend + " " + hand);
				//離開遊戲狀態
				pssGame.setPlaying(false);
				pssGame.setHand(null);						
			}
			else{	//建立遊戲
				Command("PSS " + account + " " + friend + " " + hand);
				pssGame.setHand(hand);
			}
		}
	}
	/**
	 * 猜拳按鈕開關
	 */
	public void PssGameSwitch(boolean s) {
		if( s == true ){
			btnScissors.setEnabled(true);
			btnStone.setEnabled(true);
			btnPaper.setEnabled(true);
		}
		else{
			btnScissors.setEnabled(false);
			btnStone.setEnabled(false);
			btnPaper.setEnabled(false);
		}		
	}
	/**
	 * 遊戲規則
	 */
	public SimpleAttributeSet gameAccRule(){
		createPssGame(account);
		if( pssGame.getWin() > 0){
			accFontSize = 12 + (pssGame.getWin() / 3) * 3;
		}
		if( pssGame.getLose() > 0){
			accFontSize = 12 - pssGame.getLose() / 5;
		}	
		attrset = new SimpleAttributeSet();
		StyleConstants.setForeground(attrset, Color.black);
		//判斷例外
		if( accFontSize > 100 || accFontSize <= 0 )
			accFontSize = 12;
		StyleConstants.setFontSize(attrset, accFontSize);
		return attrset;
	}
	public SimpleAttributeSet gameFriRule(){
		createPssGame(account);
		if( pssGame.getWin() > 0){
			friFontSize = 12 - pssGame.getWin() / 5;
		}
		if( pssGame.getLose() > 0){
			friFontSize = 12 + (pssGame.getLose() / 3) * 3;
		}	
		attrset = new SimpleAttributeSet();
		StyleConstants.setForeground(attrset, Color.black);
		//判斷例外
		if( friFontSize > 100 || friFontSize <= 0 )
			friFontSize = 12;
		StyleConstants.setFontSize(attrset, friFontSize);
		return attrset;
	}
}
