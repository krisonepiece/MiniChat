package com.MiniChat.Client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;



public class ServerT extends Thread{	
	private Socket socket;
	private String account;
	private ArrayList<ChatRoom> rList;
	private ArrayList<String> fList;
	private ArrayList<String> fCopyList;
	private UiController UI;
	
	public ServerT (Socket socket, String account, ArrayList<String> fList, ArrayList<ChatRoom> rList, UiController uiController){		
		this.socket = socket;
		this.account = account;
		this.fList = fList;		
		this.rList = rList;
		this.UI = uiController;
	}
	
	@Override
	public void run() {
		try {
			DataInputStream in;
			in = new java.io.DataInputStream(socket.getInputStream());
			
			//初始化好友列表
			/*UI.friendListModel.clear();
			for(int i = 0 ; i < fList.size() ; i++){
				UI.friendListModel.addElement(fList.get(i) + " (離線)");
			}*/
			
			while(true){
				String data = in.readUTF();
				eventSelect(data);
			}
	        
		} catch (SocketException e) {
			e.printStackTrace();			
		} catch (IOException e) {			
			e.printStackTrace();
			
		}
		
	}
	/**
	 * 事件選擇器
	 */
	public void eventSelect(String even){
		StringTokenizer stk = new StringTokenizer(even);
		String flag = stk.nextToken().trim();
		boolean reg;
		String friend;
		String mesg;
		switch( flag ){
			//接收新增好友回傳
			case "ADDR":
				friend = stk.nextToken().trim();
				mesg = stk.nextToken().trim();
				if( mesg.equals("true") ){
					UI.friendListModel.addElement(friend + " (離線)");
					DataInputStream in;
					try {
						in = new java.io.DataInputStream(socket.getInputStream());
						String isOnlineFlag = in.readUTF();
						fList.add(friend);
						updateFriendList(isOnlineFlag, friend);
						JOptionPane.showMessageDialog(null, "新增成功!!", "新增好友", JOptionPane.INFORMATION_MESSAGE );
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else{
					JOptionPane.showMessageDialog(null, "新增失敗!!", "新增好友", JOptionPane.INFORMATION_MESSAGE );
				}
				break;
			//接收訊息事件
			case "RMG":	
				reg = false;
				friend = stk.nextToken().trim();
				//空白字元判斷
				if( stk.hasMoreTokens() ){
					mesg = stk.nextToken().trim();
				}
				else{
					mesg = "";
				}
				reg = receiveMesg( friend, mesg );

				break;
			//接收廣播事件
			case "SBD":	
				mesg = stk.nextToken().trim();
				for(int i = 0 ; i < rList.size() ; i++){
					rList.get(i).appendBoardcast("[BoardCast] : " + mesg);
				}
				break;
			//接收好友離線事件
			case "FQT":	
				friend = stk.nextToken().trim();	
				updateFriendList("FQT", friend);	
				break;
			//接收好友上線事件
			case "FON":	
				friend = stk.nextToken().trim();		
				updateFriendList("FON", friend);
				break;
			//接收在線好友列表
			case "SOF":
				while( stk.hasMoreTokens() ){
					friend = stk.nextToken().trim();					
					updateFriendList("FON", friend);
				}
				break;
			//接收猜拳信息
			case "PSS":
			case "PSSR":
				friend = stk.nextToken().trim();
				String hand = stk.nextToken().trim();
				receivePssGame(flag, friend, hand);	
				break;
			//接收嘲諷信息
			case "LOL":
				friend = stk.nextToken().trim();	
				receiveLolMesg(flag, friend);	
				break;
		}
	}
	/**
	 * 接收聊天訊息
	 */
	public boolean receiveMesg(String friend, String mesg){
		ChatRoom cr;
		if( ( cr = roomIsVisible( friend ) ) == null ){
			cr = new ChatRoom(socket, account, friend);
			rList.add(cr);					
		}
		cr.setVisible(true);
		cr.appendTextPrint(friend + ": " + mesg);
		return true;
	}
	/**
	 * 接收猜拳訊息
	 */
	public boolean receivePssGame(String flag, String friend, String hand){
		ChatRoom cr;
		if( flag.equals("PSS") ){
			if( ( cr = roomIsVisible( friend ) ) == null ){
				cr = new ChatRoom(socket, account, friend);
				rList.add(cr);								
			}
			cr.setVisible(true);
			cr.createPssGame(account);		//建立遊戲
			cr.appendPssMesg(friend, "secret");	//顯示好友發出挑戰			
			cr.getPssGame().setPlaying(true);	//開啟遊戲狀態
			cr.getPssGame().setHand(hand);		//儲存對手出拳	
		}
		else{			
			if( ( cr = roomIsVisible( friend ) ) != null ){
				cr.setVisible(true);
				int result;
				cr.appendPssMesg(friend, hand);	//顯示好友出拳
				result = cr.getPssGame().duel(hand);
				if( result == 1 ){			//對手勝利
					cr.getPssGame().Lose();
					cr.appendPssResult(-1);
				}
				else if( result == -1 ){	//對手失敗
					cr.getPssGame().Win();
					cr.appendPssResult(1);
				}
				else{						//平手
					cr.appendPssResult(0);
				}				
				cr.getPssGame().setHand(null);
			}
		}
			
		return true;
	}
	/**
	 * 接收嘲諷訊息
	 */
	public boolean receiveLolMesg(String flag, String friend){
		ChatRoom cr;
		if( ( cr = roomIsVisible( friend ) ) == null ){
			cr = new ChatRoom(socket, account, friend);
			rList.add(cr);					
		}
		cr.setVisible(true);
		int winSubLose = cr.getPssGame().getLose() - cr.getPssGame().getWin();
		if( winSubLose < 4){
			cr.appendLolMesg("icon/duncan.png");
		}
		else if( winSubLose < 6){
			cr.appendLolMesg("icon/bye.png");
		}
		else if( winSubLose < 8){
			cr.appendLolMesg("icon/man.png");
		}
		else if( winSubLose >= 8){
			cr.appendLolMesg("icon/egg.gif");
		}
		return true;
	}
	/**
	 * 檢查聊天視窗是否開啟
	 */
	public ChatRoom roomIsVisible(String friend){
		for(int i = 0 ; i < rList.size() ; i++){
			if( rList.get(i).getFriend().equals(friend) ){
				return rList.get(i);
			}
		}	
		return null;
	}

	public ArrayList<ChatRoom> getrList() {
		return rList;
	}

	public void addrList(ChatRoom cr) {
		this.rList.add(cr);
	}	
	/**
	 * 更新好友上線(FON)或離線(FQT)
	 */
	public void updateFriendList(String flag, String friend){
		for(int j = 0 ; j < UI.friendListModel.getSize() ; j++){
			String tmpFriend = UI.friendListModel.getElementAt(j);
			StringTokenizer stkFriend = new StringTokenizer(tmpFriend);
			tmpFriend = stkFriend.nextToken();
			if( friend.equals(tmpFriend) ){
				if( flag.equals("FON") )
					UI.friendListModel.set(j, tmpFriend + " (上線中)");
				else
					UI.friendListModel.set(j, tmpFriend + " (離線)");
				break;
			}
		}
		//在聊天室窗中顯示
		for(int i = 0 ; i < UI.listRoom.size() ; i++){
			if( UI.listRoom.get(i).getFriend().equals(friend) ){
				UI.listRoom.get(i).getPssGame().reset();//遊戲資料重置
				if( flag.equals("FON") ){
					UI.listRoom.get(i).appendSystemMesg("[System] : " + friend + " 上線囉！");
					UI.listRoom.get(i).btnSetEnabled(true);
				}					
				else{
					UI.listRoom.get(i).appendSystemMesg("[System] : " + friend + " 已經離線！");
					UI.listRoom.get(i).btnSetEnabled(false);
				}				
			}
		}
	}	
}
