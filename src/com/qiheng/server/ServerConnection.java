package com.qiheng.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import javax.swing.JOptionPane;

import com.qiheng.util.CharacterUtil;
import com.qiheng.util.XMLUtil;

public class ServerConnection extends Thread {
	private ServerSocket serverSocket;
	private Server server;

	public ServerConnection(Server server, int port) {
		try {
			this.server = server;
			this.serverSocket = new ServerSocket(port);

			this.server.getLabel2().setText("运行");
			this.server.getButton().setEnabled(false);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this.server, "端口号被占用！", "警告",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void run() {
		while (true) {
			Socket socket = null;
			try {
				socket = this.serverSocket.accept();
				InputStream in = socket.getInputStream();
				OutputStream out = socket.getOutputStream();

				byte[] buf = new byte[5000];
				int length = in.read(buf);
				// 客户端发送的登录XML信息
				String info = new String(buf, 0, length);

				String userName = XMLUtil.extractUsername(info);
				
				String result=null;
				if(this.server.getMap().containsKey(userName)){
					result = CharacterUtil.FAILED;
				}
				else{
					result = CharacterUtil.SUCCESS;
					
				}
				
				String xml=XMLUtil.constructloginResultXML(result);
				out.write(xml.getBytes());
				System.out.println(xml);
				
				if(!this.server.getMap().containsKey(userName)){
				// 创建新的线程，用于处理聊天信息
				ServerMessageThread serverMessageThread = new ServerMessageThread(
						this.server, socket);
				//将用户名与相应的线程对象加入map中
				this.server.getMap().put(userName, serverMessageThread);
				
				//更新用户列表
				serverMessageThread.updateUserList();
				
				serverMessageThread.start();
				
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}