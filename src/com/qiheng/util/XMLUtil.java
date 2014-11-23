package com.qiheng.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * login: 1 
 * chat_message: 2 
 * server_message: 3
 * userList: 4
 * client_closed: 5
 * server_closed: 6
 * login_result:  7
 */

public class XMLUtil {

	/**
	 * @return
	 */
	private static Document constructDocument() {
		Document document = DocumentHelper.createDocument();
		Element root = DocumentHelper.createElement("message");
		document.setRootElement(root);
		return document;

	}

	/**
	 * 客户端向服务端发送登录XML信息
	 * <message>
	 * 			<type>1</type>
	 * 			<user>qiheng</user>
	 * </message>
	 * 
	 */
	public static String constructLoginXML(String userName) {
		Document document = XMLUtil.constructDocument();
		Element root = document.getRootElement();
		Element type = root.addElement("type");
		type.setText("1");
		Element user = root.addElement("user");
		user.setText(userName);

		return document.asXML();
	}

	/**
	 * 从客户端发送的XML信息中解析出用户名
	 * 
	 * @return
	 */
	public static String extractUsername(String xml) {
		SAXReader reader = new SAXReader();
		Document document=null;
		try {
			document = reader.read(new StringReader(xml));
			Element root = document.getRootElement();

			Element userElement = root.element("user");
			return userElement.getText();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * 从客户端发送的XML信息中解析出聊天信息
	 * 
	 */
	public static String extractContent(String xml) {
		SAXReader reader = new SAXReader();
		Document document=null;
		try {
			document = reader.read(new StringReader(xml));
			Element root = document.getRootElement();

			Element contentElement = root.element("content");
			return contentElement.getText();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 构造向客户端发送的用户列表
	 * <message>
	 * 			<type>4</type>
	 * 			<user>qiheng</user>
	 * 			<user>zhangsan</user>
	 * 			<user>lisi</user>
	 * </message>
	 * @return
	 */
	public static String constructUserList(Set<String> users) {
		Document document = XMLUtil.constructDocument();
		Element root = document.getRootElement();
		Element type = root.addElement("type");
		type.setText("4");

		for (String str : users) {
			Element user = root.addElement("user");
			user.setText(str);
		}
		return document.asXML();
	}
	
	/**
	 * 构造向客户端发送的聊天内容
	 * <message>
	 * 			<type>4</type>
	 * 			<content>qiheng:hello world!</content>
	 * </message>
	 * @return
	 */
	public static String constructServerMessageXML(String message) {
		Document document = XMLUtil.constructDocument();
		Element root = document.getRootElement();
		Element type = root.addElement("type");
		type.setText("3");

		Element content=root.addElement("content");
		content.setText(message);
		
		return document.asXML();
	}
	
	/**
	 * 从服务器发送的XML信息中解析出用户名
	 * 
	 * @return
	 */

	@SuppressWarnings("rawtypes")
	public static List<String> extractUserList(String xml) {
		List<String> list = new ArrayList<String>();
		try {

			SAXReader reader = new SAXReader();
			Document document = null;

			document = reader.read(new StringReader(xml));
			for (Iterator iterator = document.getRootElement().elementIterator(
					"user"); iterator.hasNext();) {
				Element element = (Element) iterator.next();
				list.add(element.getText());
			}

		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	

	/**
	 * 解析出type类型
	 * 
	 * @return
	 */
	public static String getType(String xml) {
		try {

			SAXReader reader = new SAXReader();
			Document document = null;

			document = reader.read(new StringReader(xml));
			Element root = document.getRootElement();
			Element type = root.element("type");
			return type.getText();

		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 构造客户端发送信息的XML
	 * <message>
	 * 			<type>2</type>
	 * 			<user>qiheng</user>
	 * 			<content>chat message....</content>
	 * </message>
	 * 
	 * 
	 */
	public static String constructMessageXML(String userName, String info) {
		Document document = XMLUtil.constructDocument();
		Element root = document.getRootElement();
		Element type = root.addElement("type");
		type.setText("2");
		Element user = root.addElement("user");
		user.setText(userName);
		Element content = root.addElement("content");
		content.setText(info);

		return document.asXML();
	}
	
	/**
	 * 构造服务器端关闭信息的XML
	 * <message>
	 * 			<type>6</type>
	 * </message>
	 * 
	 * 
	 */
	public static String constructServerClosedXML() {
		
		Document document = XMLUtil.constructDocument();
		Element root = document.getRootElement();
		Element type = root.addElement("type");
		type.setText("6");
		return document.asXML();
	}
	
	/**
	 * 构造客户端发送关闭信息的XML
	 * <message>
	 * 			<type>5</type>
	 * 			<user>qiheng</user>
	 * </message>
	 * 
	 * 
	 */
	public static String constructClientClosedXML(String userName) {
		Document document = XMLUtil.constructDocument();
		Element root = document.getRootElement();
		Element type = root.addElement("type");
		type.setText("5");
		Element user = root.addElement("user");
		user.setText(userName);
		
		return document.asXML();
	}
	
	/**
	 * 
	 * 像客户端返回用户名是否重名
	 * <message>
	 * 			<type>7</type>
	 * 			<result>success</result>
	 * </message>
	 * 
	 */
	
	public static String constructloginResultXML(String result){
		Document document = XMLUtil.constructDocument();
		Element root = document.getRootElement();
		Element type = root.addElement("type");
		type.setText("7");
		Element resultElement = root.addElement("content");
		resultElement.setText(result);
		
		return document.asXML();
	}
}
