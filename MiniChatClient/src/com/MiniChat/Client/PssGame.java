package com.MiniChat.Client;

public class PssGame {
	private String account;
	private int win;
	private int lose;
	private String hand;
	private boolean Playing;	
	
	public PssGame(String account) {
		this.account = account;
		this.win = 0;
		this.lose = 0;
		this.hand = null;
		this.Playing = false;
	}
	
	/**
	 * 查詢遊戲狀態
	 */
	public boolean isPlaying(){
		return Playing;
	}
	/**
	 * 判斷遊戲勝負
	 */
	public int duel(String hand){
		int winTheGame = 1;
		int loseTheGame = -1;
		int drawTheGame = 0;
		int exception = 100;
		
		if(hand.equals("scissors")){
			if( this.hand.equals("paper") ){
				return winTheGame;
			}
			else if( this.hand.equals("stone") ){
				return loseTheGame;
			}
			else{
				return drawTheGame;
			}
		}
		else if(hand.equals("stone")){
			if( this.hand.equals("scissors") ){
				return winTheGame;
			}
			else if( this.hand.equals("paper") ){
				return loseTheGame;
			}
			else{
				return drawTheGame;
			}
		}
		else if(hand.equals("paper")){
			if( this.hand.equals("stone") ){
				return winTheGame;
			}
			else if( this.hand.equals("scissors") ){
				return loseTheGame;
			}
			else{
				return drawTheGame;
			}
		}
		return exception;
	}

	public int getWin() {
		return win;
	}

	public void Win() {
		this.win++;
	}

	public int getLose() {
		return lose;
	}

	public void Lose() {
		this.lose++;
	}

	public String getHand() {
		return this.hand;
	}

	public void setHand(String hand) {
		this.hand = hand;
	}

	public String getAccount() {
		return account;
	}

	public void setPlaying(boolean playing) {
		Playing = playing;
	}
	public void reset() {
		this.win = 0;
		this.lose = 0;
		this.hand = null;
		this.Playing = false;
	}	
}
