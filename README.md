MiniChat
===

# 簡介
提供使用者基本的聊天功能，並補足現今通訊軟體較缺乏的使用者互動，我們增加了遊戲及貼圖功能，讓彼此在聊天之餘，還能透過遊戲來增加情感。

# 系統規格
* 編輯器  
  Eclipse
* 開發語言  
  JAVA
* 圖形介面  
  JAVA Swing
* 網路協定  
  TCP
* 實作方法  
  透過Socket在Client和Server間傳遞訊息
  
# 模組功能與說明
## Server
* 會員模組  
    * 顯示會員列表：顯示註冊的所有會員
    * 顯示會員狀態：顯示會員當前連線狀態
* 訊息模組  
    * 廣播訊息：對所有在線使用者發出訊息
    * 顯示連接訊息：當有使用者連入，或是中斷連線時，顯示在訊息視窗
## Client
* 登入模組  
    * 會員註冊：將會員資料新增至Server端的member.dat
    * 會員登入：搜尋Server端的member.dat，回傳帳號資訊及好友列表
    * 記住帳密：在Client端建立User.dat儲存會員資料
    * 自動登入：自動設定User.dat的內容後登入
* 好友模組  
    * 好友列表：讀取Server回傳的好友資料，顯示在列表，並即時顯示好友在線狀態
    * 新增好友：在Server端的Friend.dat中新增好友
    * 刪除好友：在Server端的Friend.dat中刪除好友
* 聊天室模組  
    * 傳送文字訊息：與好友進行聊天
    * 猜拳遊戲：可與好友進行猜拳遊戲互動
    * 嘲諷功能：可傳送貼圖嘲諷好友
    * 遊戲回饋：對使用者進行遊戲的比分作出獎勵和懲罰

# 遊戲規則
## 猜拳遊戲  
* 與一般猜拳規則相同
## 遊戲回饋  
* 每贏 3 場，聊天字體大小 + 3
* 每輸 5 場，聊天字體大小 – 1
## 嘲諷功能  
* 勝場 – 敗場 >= 2 才可開啟嘲諷功能
* 依據勝率的多寡，可使用不同的貼圖

# 網路架構  
所有Client 及 Server 溝通，皆透過Socket傳遞訊息，訊息以「指令化」的方式傳遞，再透過「事件選擇器」，判斷接收指令方要執行的動作。  

例如：Client登入事件即會發送「LOG 會員帳號 會員密碼」這道訊息給Server。  

<img src="https://i.imgur.com/hsHMHWq.png" height="650" width="700">

# 模組間互動規則與資料傳輸格式

## Server

| 類別名稱 | 傳輸格式 |功能描述|
| :-: | :-: | - |
| ChatServer | String | 監聽 Client 是否發出連線請求，連線建立後，將與 Client 後續溝通的工作交給 ClientT 執行緒去做，並繼續監聽。 |
| ClientT | String | 負責接收 Client 的訊息，透過事件選擇器來判斷 Client 欲執行的動作 |
| Member | X | 會員資料，包含會員名稱、密碼、好友名單、Socket、遊戲資料…等 |
| OnlineData | X | 線上會員資料，儲存在線的會員 |
| UiController | Object | 負責管理 ChatServer 的 UI 界面 |

## Client

| 模組名稱 | 傳輸格式 | 功能描述 |
| :-: | :-: | - |
| ClientLogin | String| 註冊及登入功能，傳送會員資料給 ClientT，驗證會員資料 |
| ChatClient | String| 負責好友管理及狀態顯示 |
| ClientRoom | String| 聊天視窗，發送訊息至 ClientT，透過 ID 將訊息發給特定對象 |
| ServerT | String| 負責接收 Server 傳送的訊息，透過事件選擇器，判斷欲執行的動作 |
| PssGame | X | 猜拳遊戲資料，儲存勝敗場數、出拳狀況…等 |
| UiController | Object | 負責管理 ChatClient 的 UI 界面 |

# 事件選擇器
![](https://i.imgur.com/RsZH0hx.jpg)

# 執行檔下載
* [Server](https://drive.google.com/open?id=1mP7OzQKFzdEYxdeKNqOUz1Rpfm1agtvW)
* [Client](https://drive.google.com/open?id=1haorsQBZ9240-x7NI54rTdLoLpW2LEfK)

# 使用與安裝說明
## 前置安裝
1. 上 [JAVA官網](https://www.java.com/zh_TW/) 下載 JAVA  
![](https://i.imgur.com/rUGDjdb.jpg)

## Server 安裝
1. 解壓縮 MiniChatServer.rar 檔，即會產生兩個資料夾和一個執行檔，點選MiniChatServer.exe即可開啟Server連線及會員管理。  
![](https://i.imgur.com/7yp8izu.jpg)

## Client 安裝
1. 解壓縮 MiniChatClient.rar 檔，即會產生兩個資料夾和一個執行檔。  
![](https://i.imgur.com/tiYALVw.jpg)
2. 修改Server.dat中的IP及Port符合實際情況，即可點選MiniChatClient.exe，登入進行聊天。  
![](https://i.imgur.com/rG7nHn6.jpg)


# 程式介面
## Server  
![](https://i.imgur.com/FCnRHS8.jpg)

## Client
### 登入模組  
![](https://i.imgur.com/ubaWWRi.jpg)

### 好友模組  
<img src="https://i.imgur.com/om2Ady8.jpg" height="500" width="280"><img src="https://i.imgur.com/wsFOyNL.jpg" height="500" width="280"><img src="https://i.imgur.com/o4L5udX.jpg" height="500" width="280">  

### 聊天室模組  
![](https://i.imgur.com/E4qv7dB.jpg)
![](https://i.imgur.com/0hPXpOI.jpg)


# 遇到問題與解決方法
1. Q：Java GUI 的排版費時  
   A：安裝 Eclipse 插件 WindowBuilder  

2. Q：執行緒與主執行緒的 UI 無法溝通  
   A：建立一個 UiController 將介面元件交由他來操作  

3. Q：多執行緒同時修改同一個檔案造成資料不同步  
   A：使用 synchronized 將正在被執行的方法鎖定  
