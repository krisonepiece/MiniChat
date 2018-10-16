package com.MiniChat.Server;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


public class ClientT extends Thread{	

	private Socket socket;
	private ArrayList<Member> mList;
	private ArrayList<Member> mCopyList;
	private UiController UI;
	private String account;
	private OnlineData onlineData;
	private SimpleAttributeSet attrset;
	
	
	public ClientT (Socket socket, ArrayList<Member> mList, UiController uiController, OnlineData onlineData){		

		this.socket = socket;
		this.mList = mList;
		this.UI = uiController;
		this.onlineData = onlineData;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			//socket.setSoTimeout(15000);
			
			DataInputStream in;
			in = new java.io.DataInputStream(socket.getInputStream());
			while(true){					
				String data = in.readUTF();
				eventSelect(data);
				//textPrint.append("[Client] : " + data);
				/*for(int i = 0 ;i<clientList.size() ;i++){
					DataOutputStream out =  new DataOutputStream(clientList.get(i).getOutputStream());
					out.writeUTF(data);
				}
				*/
		       // System.out.println("[Server Mesg] : Get a Mesg From Client \n" + data);
			}
	        
		} catch (SocketException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			/*中斷連線例外*/
			
			//通知好友離線
			noticeFriend("FQT", account);
			//從線上清單移除
			onlineData.remove(account);
			//更新會員列表
			updateMemberList("MQT", account);
			
			attrset = new SimpleAttributeSet();
			StyleConstants.setForeground(attrset, Color.red);
			StyleConstants.setBold(attrset, true);
			insert("[Server] : Disconnect - InetAddress = "
	                + socket.getInetAddress() + " Name: " + account, attrset);
			/*UI.textPrint.append("[Server] : Disconnect - InetAddress = "
	                + socket.getInetAddress() + " Name: " + account + "\n");*/
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	/**
	 * 事件選擇器
	 */
	public void eventSelect(String Mesg){
		StringTokenizer stk = new StringTokenizer(Mesg);
		String flag = stk.nextToken().trim();
		boolean reg;
		String password;
		String friend;
		switch( flag ){
			//會員註冊事件
			case "REG":	
				reg = false;
				account = stk.nextToken().trim();
				password = stk.nextToken().trim();
				reg = serverRegister( account, password );
				if( reg ){
					ChatServer.LoadMemberData();
					UI.memberListModel.addElement(account + " (離線)");
				}				
				try {					
					DataOutputStream out = new DataOutputStream(socket.getOutputStream());
					out.writeBoolean(reg);
					out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			//會員登入事件
			case "LOG":
				reg = false;
				account = stk.nextToken().trim();
				password = stk.nextToken().trim();
				reg = serverLogin( account, password );
				if(reg){
					//登入成功，加入線上清單
					for(int i = 0 ; i < mList.size() ; i++){
						if( mList.get(i).getAccount().equals(account) ){
							onlineData.add(mList.get(i), socket);
							updateMemberList("MON", account);
						}
					}
					attrset = new SimpleAttributeSet();
					StyleConstants.setForeground(attrset, Color.green);
					StyleConstants.setBold(attrset, true);
					insert("[Server] : Get a new Client - InetAddress = "
			                + socket.getInetAddress() + " Name: " + account, attrset);

					//通知好友上線
					noticeFriend("FON", account);
				}
				try {					
					DataOutputStream out = new DataOutputStream(socket.getOutputStream());
					out.writeBoolean(reg);
					out.flush();
					//待寫*******回傳好友資訊
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			//會員新增好友
			case "ADD":	
				account = stk.nextToken().trim();
				friend = stk.nextToken().trim();	
				reg = serverAddFriend( toId(account), toId(friend) );				
				try {					
					DataOutputStream out = new DataOutputStream(socket.getOutputStream());
					out.writeUTF("ADDR " + friend + " " + reg);
					out.flush();
					if( reg ){
						if( onlineData.isOnline( friend ) ){	//若新增的好友在線
							out.writeUTF("FON");				//通知使用者好友在線
							out.flush();
							noticeFriend("FON", account);		//通知好友使用者在線
						}
						else{
							out.writeUTF("FQT " + friend);
							out.flush();
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			//會員刪除好友
			case "DEL":	
				account = stk.nextToken().trim();
				friend = stk.nextToken().trim();	
				reg = serverDelFriend( toId(account), toId(friend) );				
				
				break;
			//回傳好友列表
			case "RFL":	
				account = stk.nextToken().trim();
				reg = requestFriendList( account );				
				try {					
					DataOutputStream out = new DataOutputStream(socket.getOutputStream());
					out.writeBoolean(reg);
					out.flush();			
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			//傳遞訊息
			case "SMG":	
				account = stk.nextToken().trim();
				friend = stk.nextToken().trim();
				String mesg;
				//空白字元判斷
				if( stk.hasMoreTokens() ){
					mesg = stk.nextToken().trim();
				}
				else{
					mesg = "";
				}				
				reg = clientSendMesg( account, friend, mesg );
				break;
			//回傳在線好友列表
			case "ROF":	
				account = stk.nextToken().trim();				
				reg = sendOnlineFriend(account);
				break;
			//傳遞猜拳訊息
			case "PSS":	
			case "PSSR":				
				account = stk.nextToken().trim();
				friend = stk.nextToken().trim();
				String hand = stk.nextToken().trim();			
				reg = SendPssGame( flag, account, friend, hand );
				break;
			//傳遞嘲諷訊息
			case "LOL":					
				account = stk.nextToken().trim();
				friend = stk.nextToken().trim();		
				reg = SendLolMesg( flag, account, friend);
				break;
		}
	}
	/**
	 * Server端會員註冊
	 */
	public boolean serverRegister(String account, String password){
		if( !checkAccount(account) ){
			FileWriter fw;
			Member m = new Member(mList.size() + 1, account, password);
			try {
				mList.add(m);
				fw = new FileWriter("dat/Member.dat",false);
				for(int i = 0 ; i < mList.size() ; i++){
					fw.write(mList.get(i).getId() + " " + mList.get(i).getAccount() 
							+ " " + mList.get(i).getPassword() + "\r\n");
					fw.flush();
				}
		        fw.close();
		        return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}		
		return false;				
	}
	/**
	 * 會員登入
	 */
	public boolean serverLogin(String account, String password){
		if( checkMember(account, password) ){			
			return true;
		}	
		return false;			
	}
	/**
	 * 檢查帳號存在
	 */
	public boolean checkAccount(String account){
		for(int i = 0 ; i < mList.size() ; i++){
			if( mList.get(i).getAccount().equals(account) ){
				return true;
			}
		}
		return false;
	}
	/**
	 * 驗證帳密資料
	 */
	public boolean checkMember(String account, String password){
		for(int i = 0 ; i < mList.size() ; i++){
			if( mList.get(i).getAccount().equals(account) ){
				if( mList.get(i).getPassword().equals(password) ){
					if( !onlineData.isOnline(account) ){
						return true;
					}					
				}
				return false;
			}
		}
		return false;
	}
	/**
	 * 帳號轉id
	 */
	public int toId(String account){
		for(int i = 0 ; i < mList.size() ; i++){
			if( mList.get(i).getAccount().equals(account) ){
				return mList.get(i).getId();		
			}
		}
		return -1;
	}
	/**
	 * id轉帳號
	 */
	public String toAccount(int id){
		for(int i = 0 ; i < mList.size() ; i++){
			if( mList.get(i).getId() == id ){
				return mList.get(i).getAccount();		
			}
		}
		return null;
	}
	/**
	 * 新增好友
	 */
	public boolean serverAddFriend(int id, int idFri){
		if( id != idFri && idFri != -1 ){
			for(int i = 0 ; i < mList.size() ; i++){
				if( mList.get(i).getId() == id ){
					if( !mList.get(i).getFriends().contains(new Integer(idFri)) ){
						mList.get(i).getFriends().add( new Integer(idFri) );
						updateFriend();
						return true;
					}					
				}
			}
		}		
		return false;
	}
	/**
	 * 刪除好友
	 */
	public boolean serverDelFriend(int id, int idFri){
		if( idFri != -1 ){
			for(int i = 0 ; i < mList.size() ; i++){
				if( mList.get(i).getId() == id ){
					if( mList.get(i).getFriends().contains(new Integer(idFri)) ){
						mList.get(i).getFriends().remove( new Integer(idFri) );
						updateFriend();
						return true;
					}					
				}
			}
		}		
		return false;
	}
	/**
	 * 更新好友檔案
	 */
	public void updateFriend(){
		FileWriter fw;
		String Ftmp = "";
		try {
			fw = new FileWriter("dat/Friend.dat",false);
			for(int i = 0 ; i < mList.size() ; i++){
				Ftmp = "" + mList.get(i).getId();
				for(int j = 0 ; j < mList.get(i).getFriends().size() ; j++){
					Ftmp = Ftmp + " " + mList.get(i).getFriends().get(j);
				}
				Ftmp = Ftmp + "\r\n";
				fw.write( Ftmp );
				fw.flush();
			}
	        fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	/**
	 * 回傳好友列表
	 */
	public boolean requestFriendList(String account){
		//傳送帳號資訊
		try {
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			for(int i = 0 ; i < mList.size() ; i++){
				if( mList.get(i).getAccount().equals(account) ){
					int friNum = mList.get(i).getFriends().size();
					out.writeInt( friNum );					
					for(int j = 0 ; j < mList.get(i).getFriends().size() ; j++){
						String friTmp = toAccount(mList.get(i).getFriends().get(j));
						out.writeUTF( friTmp );						
					}
					out.flush();
					return true;
				}
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 更新會員列表
	 */
	public void updateMemberList(String flag, String account){
		for(int j = 0 ; j < UI.memberListModel.getSize() ; j++){
			String tmpMember = UI.memberListModel.getElementAt(j);
			StringTokenizer stkMember = new StringTokenizer(tmpMember);
			tmpMember = stkMember.nextToken();
			if( account.equals(tmpMember) ){
				if( flag.equals("MON") )
					UI.memberListModel.set(j, tmpMember + " (上線中)");
				else
					UI.memberListModel.set(j, tmpMember + " (離線)");
				break;
			}
		}
	}
	/**
	 * 傳送訊息
	 */
	public boolean clientSendMesg(String account, String friend, String mesg){
		try {			
			if( onlineData.isOnline(friend) ){				
				DataOutputStream out = new DataOutputStream(onlineData.getSocket(friend).getOutputStream());
				out.writeUTF( "RMG " + account + " " + mesg );
				out.flush();
				return true;
			}		
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 通知好友上線(FON)或離線(FQT)
	 */
	public boolean noticeFriend(String flag, String account){
		try {			
			for(int i = 0 ; i < mList.size() ; i++){
				if( mList.get(i).getAccount().equals(account) ){
					int friNum = mList.get(i).getFriends().size();				
					for(int j = 0 ; j < friNum ; j++){
						if( onlineData.isOnline( mList.get(i).getFriends().get(j) ) ){
							Socket friSocket = onlineData.get( onlineData.indexOf( mList.get(i).getFriends().get(j) ) ).getSocket();
							//String friAccount = onlineData.get( onlineData.indexOf( mList.get(i).getFriends().get(j) ) ).getAccount();
							DataOutputStream out = new DataOutputStream(friSocket.getOutputStream());
							out.writeUTF( flag + " " + account );
							out.flush();
							
						}											
					}
					return true;
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 回傳在線好友列表
	 */
	public boolean sendOnlineFriend(String account){
		try {
			for(int i = 0 ; i < mList.size() ; i++){
				if( mList.get(i).getAccount().equals(account) ){
					int friNum = mList.get(i).getFriends().size();	
					String cmd = "SOF";
					for(int j = 0 ; j < friNum ; j++){
						if( onlineData.isOnline( mList.get(i).getFriends().get(j) ) ){
							cmd += " " + onlineData.get( onlineData.indexOf( mList.get(i).getFriends().get(j) ) ).getAccount();						
						}											
					}
					DataOutputStream out = new DataOutputStream(socket.getOutputStream());
					out.writeUTF( cmd );
					out.flush();
					return true;
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 將字串插入JTextPane中
	 */
	public void insert(String str, AttributeSet attrset) {
		Document docs = UI.textPrint.getDocument();// 利用getDocument()方法取得JTextPane的Document
												// instance.0
		str = str + "\n";
		try {
			docs.insertString(docs.getLength(), str, attrset);
			UI.textPrint.setCaretPosition(docs.getLength()); 	//自動捲動到底部
		} catch (BadLocationException ble) {
			System.out.println("BadLocationException:" + ble);
		}
	}
	/**
	 * 傳送猜拳訊息
	 */
	public boolean SendPssGame(String flag, String account, String friend, String hand){
		try {			
			if( onlineData.isOnline(friend) ){				
				DataOutputStream out = new DataOutputStream(onlineData.getSocket(friend).getOutputStream());
				out.writeUTF( flag + " " + account + " " + hand );
				out.flush();
				return true;
			}		
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 傳送嘲諷訊息
	 */
	public boolean SendLolMesg(String flag, String account, String friend){
		try {			
			if( onlineData.isOnline(friend) ){				
				DataOutputStream out = new DataOutputStream(onlineData.getSocket(friend).getOutputStream());
				out.writeUTF( flag + " " + account);
				out.flush();
				return true;
			}		
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	
}