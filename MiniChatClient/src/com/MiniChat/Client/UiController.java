package com.MiniChat.Client;

import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JList;

public class UiController {
	
	public JList<String> listFriend;
	public DefaultListModel<String> friendListModel;
	public ArrayList<ChatRoom> listRoom;

	public UiController(JList<String> listFriend,
			DefaultListModel<String> friendListModel, ArrayList<ChatRoom> listRoom) {
		this.listFriend = listFriend;
		this.friendListModel = friendListModel;
		this.listRoom = listRoom;
	}
	
}
