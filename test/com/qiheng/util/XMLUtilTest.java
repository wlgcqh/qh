package com.qiheng.util;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.StringReader;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Before;
import org.junit.Test;

public class XMLUtilTest {
	private SAXReader reader;
	@Before
	public void setUp(){
		reader=new SAXReader();
	}
	
	@Test
	public void testConstructLoginXML(){
		
		try {
			String xml = XMLUtil.constructLoginXML("qiheng");
			Document document = reader.read(new StringReader(xml));
			Element root = document.getRootElement();
			String rootName = root.getName();
			Element typeElement = root.element("type");
			Element userElement = root.element("user");
			
			assertEquals("message",rootName);
			assertEquals("1",typeElement.getText());
			assertEquals("qiheng",userElement.getText());
			System.out.println(xml);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
		
		
		
	}
	
}
