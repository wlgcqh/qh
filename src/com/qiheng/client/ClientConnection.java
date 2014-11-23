package com.qiheng.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import javax.swing.JOptionPane;

import com.qiheng.util.CharacterUtil;
import com.qiheng.util.XMLUtil;

public class ClientConnection extends Thread {
	private String userName;
	private String hostAddress;
	private int port;
	private Client client;

	private Socket socket;

	private InputStream in;
	private OutputStream out;
	private ChatClient chatClient;
	
	
	public ClientConnection(String userName, String hostAddress, int port,
			Client client) {
		super();
		this.userName = userName;
		this.hostAddress = hostAddress;
		this.port = port;
		this.client = client;
		connect2Server();
		
	}
	
	
	
	public Socket getSocket() {
		return socket;
	}



	public void setSocket(Socket socket) {
		this.socket = socket;
	}



	public String getUserName() {
		return userName;
	}
	
	//登录成功返回true
	public boolean login() {

		try {

			String info = XMLUtil.constructLoginXML(this.userName);
			this.out.write(info.getBytes());// 发送XML信息，包含用户名

			byte[] buf = new byte[5000];
			int length = this.in.read(buf);

			String loginResultXML = new String(buf, 0, length);
			//System.out.println("response: " + response);
			String loginResult=XMLUtil.extractContent(loginResultXML);
			System.out.println(loginResult);
			//登陆成功
			if(CharacterUtil.SUCCESS.equals(loginResult)){
				// 打开聊天窗口
				this.chatClient = new ChatClient(this);

				this.client.setVisible(false);
				return true;
			}
			//登录失败
			else{
				return false;
			}
			

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void connect2Server() {
		try {
			this.socket = new Socket(this.hostAddress, this.port);
			this.in = this.socket.getInputStream();
			this.out = this.socket.getOutputStream();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while (true) {
				byte[] buf = new byte[5000];
				int length;

				length = this.in.read(buf);
				String info = new String(buf, 0, length);
				// System.out.println(info);
				int type = Integer.parseInt(XMLUtil.getType(info));

				// 在线用户列表
				if (type == CharacterUtil.USERLIST) {
					List<String> list = XMLUtil.extractUserList(info);
					String str = "";
					for (String user : list) {
						str += user + "\n";
					}
					// System.out.println(str);
					this.chatClient.getJTextArea2().setText(str);
				}
				// 聊天信息
				else if (type == CharacterUtil.SERVER_MESSAGE) {
					// System.out.println(info);
					String content = XMLUtil.extractContent(info);
					System.out.println(content);
					this.chatClient.getJTextArea1().append(content + "\n");
				}
				// 服务端关闭信息
				else if (type == CharacterUtil.SERVER_CLOSED) {
					JOptionPane.showMessageDialog(this.chatClient, "服务器已关闭！",
							"警告", JOptionPane.WARNING_MESSAGE);
					System.exit(0);
					
				}
				// 关闭服务端
				else if(type== CharacterUtil.CLIENT_CLOSED){
					this.in.close();
					this.out.close();
					this.socket.close();
					System.exit(0);//退出
				}

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendMessage(String msg, int type) {
		try {
			String xml = null;
			// 向服务器构造聊天信息
			if (type == CharacterUtil.CLIENT_MESSAGE) {
				xml = XMLUtil.constructMessageXML(this.userName, msg);
			}
			else if(type == CharacterUtil.CLIENT_CLOSED){
				xml = XMLUtil.constructClientClosedXML(this.userName);
			}
			
			// 向服务器发送聊天信息
			this.out.write(xml.getBytes());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}