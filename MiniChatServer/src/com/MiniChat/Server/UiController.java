package com.MiniChat.Server;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTextPane;

public class UiController {
	public JTextPane textPrint;		//顯示文字區域
	public JList listMember;			//會員檢視清單	
	public DefaultListModel<String> memberListModel;

	public UiController(JTextPane textPrint, JList listMember,
			DefaultListModel<String> memberListModel) {
		this.textPrint = textPrint;
		this.listMember = listMember;
		this.memberListModel = memberListModel;
	}

	public JTextPane textPrint() {
		return textPrint;
	}

	public JList listMember() {
		return listMember;
	}

	public DefaultListModel<String> memberListModel() {
		return memberListModel;
	}
	
}
