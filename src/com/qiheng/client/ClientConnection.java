package com.qiheng.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.qiheng.util.XMLUtil;

public class ClientConnection extends Thread {
	private String userName;
	private String hostAddress;
	private int port;
	private Client client;

	private Socket socket;

	private InputStream in;
	private OutputStream out;

	public ClientConnection(String userName, String hostAddress, int port,
			Client client) {
		super();
		this.userName = userName;
		this.hostAddress = hostAddress;
		this.port = port;
		this.client = client;
		connect2Server();
		login();
	}

	private void login() {

		try {

			String info = XMLUtil.constructLoginXML(this.userName);
			this.out.write(info.getBytes());// 发送XML信息，包含用户名

			byte[] buf = new byte[5000];
			int length = this.in.read(buf);

			String response = new String(buf, 0, length);
			System.out.println("response: " + response);

			// 打开聊天窗口
			ChatClient chatClient = new ChatClient();

			this.client.setVisible(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	}
}
