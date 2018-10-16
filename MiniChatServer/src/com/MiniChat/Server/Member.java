package com.MiniChat.Server;

import java.net.Socket;
import java.util.ArrayList;

public class Member {
	protected int id;
	protected String account;
	protected String password;
	protected ArrayList<Integer> friends;
	protected Socket socket;
	
	public Member(int id, String account, String password, ArrayList<Integer> friends) {
		this.id = id;
		this.account = account;
		this.password = password;
		this.friends = friends;
	}
	
	public Member(int id, String account, String password) {
		this.id = id;
		this.account = account;
		this.password = password;
	}
	
	public Member(int id, String account, String password, ArrayList<Integer> friends, Socket socket) {
		this.id = id;
		this.account = account;
		this.password = password;
		this.friends = friends;
		this.socket = socket;
	}
	
	public Member(int id, String account, String password, Socket socket) {
		this.id = id;
		this.account = account;
		this.password = password;
		this.socket = socket;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public ArrayList<Integer> getFriends() {
		return friends;
	}

	public void setFriends(ArrayList<Integer> friends) {
		this.friends = friends;
	}
	
	public boolean containsFriends(int id) {
		return friends.contains(id);
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}	
}
