package com.MiniChat.Server;

import java.net.Socket;
import java.util.ArrayList;

public class OnlineData {	
	private ArrayList<Member> onlineList;

	public OnlineData() {
		onlineList = new ArrayList<Member>();
	}
	
	public ArrayList<Member> getOnlineList() {
		return onlineList;
	}

	//查詢會員是否上線
	public boolean isOnline(String account){
		for(int i = 0 ; i < onlineList.size() ; i++){
			if( onlineList.get(i).getAccount().equals(account) ){
				if( onlineList.get(i).getSocket().isConnected() ){
					return true;
				}
				else{
					return false;
				}
			}
		}
		return false;
	}	
	//查詢會員是否上線
	public boolean isOnline(int id){
		for(int i = 0 ; i < onlineList.size() ; i++){
			if( onlineList.get(i).getId() == id ){
				if( onlineList.get(i).getSocket().isConnected() ){
					return true;
				}
				else{
					return false;
				}
			}
		}
		return false;
	}	
	//取得線上人數
	public int size(){
		return onlineList.size();
	}
	//取得指定會員
	public Member get(int index){
		return onlineList.get(index);
	}
	public Member get(String account){
		return onlineList.get(indexOf(account));
	}
	//新增指定會員
	public void add(Member member){
		onlineList.add(member);
	}
	public void add(Member member, Socket socket){
		member.setSocket(socket);
		onlineList.add(member);
	}
	//刪除指定會員
	public void remove(Member member){
		onlineList.remove(member);
	}
	public void remove(String account){
		onlineList.remove( get(account) );
	}
	//查詢指定會員
	public boolean contains(Member member){
		return onlineList.contains(member);
	}
	public boolean contains(String account){
		for(int i = 0 ; i < onlineList.size() ; i++){
			if( onlineList.get(i).getAccount().equals(account) ){
				return true;
			}
		}
		return false;
	}
	public boolean contains(int id){
		for(int i = 0 ; i < onlineList.size() ; i++){
			if( onlineList.get(i).getId() == id ){
				return true;
			}
		}
		return false;
	}
	//查詢指定會員索引
	public int indexOf(Member member){
		if( contains(member) )
			return onlineList.indexOf(member);
		return -1;
	}
	public int indexOf(String account){
		for(int i = 0 ; i < onlineList.size() ; i++){
			if( onlineList.get(i).getAccount().equals(account) ){
				return i;
			}
		}
		return -1;
	}
	public int indexOf(int id){
		for(int i = 0 ; i < onlineList.size() ; i++){
			if( onlineList.get(i).getId() == id ){
				return i;
			}
		}
		return -1;
	}
	//查詢指定會員Socket
	public Socket getSocket(Member member){
		return onlineList.get( indexOf(member) ).getSocket();
	}
	public Socket getSocket(String account){
		return onlineList.get( indexOf(account) ).getSocket();
	}
	public Socket getSocket(int id){
		return onlineList.get( indexOf(id) ).getSocket();
	}
	
}
