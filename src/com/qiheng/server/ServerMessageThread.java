package com.qiheng.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.qiheng.util.CharacterUtil;
import com.qiheng.util.XMLUtil;

public class ServerMessageThread extends Thread {
	private Server server;
	private InputStream in;
	private OutputStream out;

	public ServerMessageThread(Server server, Socket socket) {

		try {
			this.server = server;
			this.in = socket.getInputStream();
			this.out = socket.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 更新用户列表
	// 首先更新服务器端用户列表，然后更新用户端
	public void updateUserList() {
		// 用户名集合
		Set<String> users = this.server.getMap().keySet();
		// 构造向客户端发送的信息
		String xml = XMLUtil.constructUserList(users);

		String str = "";
		for (String user : users) {
			str += user + "\n";
		}
		// 更新服务器端列表
		this.server.getTextarea().setText(str);

		// 更新客户端列表
		Collection<ServerMessageThread> cols = this.server.getMap().values();
		for (ServerMessageThread smt : cols) {
			smt.sendMessage(xml);
		}

	}

	public void sendMessage(String message) {
		try {
			this.out.write(message.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			try {

				byte[] buf = new byte[5000];
				int length = this.in.read(buf);
				// 客户端发来的信息
				String xml = new String(buf, 0, length);
				int type = Integer.parseInt(XMLUtil.getType(xml));

				// 聊天数据
				if (type == CharacterUtil.CLIENT_MESSAGE) {
					// System.out.println(xml);

					// 解析出用户名
					String username = XMLUtil.extractUsername(xml);
					// 解析出聊天内容
					String content = XMLUtil.extractContent(xml);

					// 构造像客户端发送的信息
					String message = username + ":" + content;
					
					String xmlMessage=XMLUtil.constructServerMessageXML(message);
					Map<String, ServerMessageThread> map=this.server.getMap();
					Collection<ServerMessageThread> cols = map.values();
					for (ServerMessageThread smt : cols) {
						smt.sendMessage(xmlMessage);
					}
					
				}
				//关闭客户端窗口
				else if(type == CharacterUtil.CLIENT_CLOSED){
					String user=XMLUtil.extractUsername(xml);
					
					//获得待删除线程对象
					ServerMessageThread smt = this.server.getMap().get(user);
					//向客户端发送退出信息
					smt.sendMessage(xml);
					

					//从用户列表中将该用户删除
					this.server.getMap().remove(user);
					//更新用户列表
					this.updateUserList();
					
					this.in.close();
					this.out.close();
					break;//关闭线程
					
				}
				

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
