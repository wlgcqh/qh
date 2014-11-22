package com.qiheng.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

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
				
				String userName=XMLUtil.extractUsername(info);
				
				out.write("success".getBytes());
				
				//创建新的线程，用于处理聊天信息
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
