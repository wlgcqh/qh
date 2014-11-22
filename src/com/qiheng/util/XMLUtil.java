package com.qiheng.util;

import java.io.StringReader;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * login 1
 * 
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
	 * 
	 * @return
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
		Document document;
		try {
			document = reader.read(new StringReader(xml));
			Element root = document.getRootElement();

			Element typeElement = root.element("type");
			Element userElement = root.element("user");
			return userElement.getText();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
