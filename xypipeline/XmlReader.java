package xypipeline;

import java.io.*;
import javax.xml.parsers.*;

import org.w3c.dom.*;

class XmlReader {
	Document doc;
	XmlReader(File xml){
		DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
		FileInputStream xmlis=null;
		try {
			DocumentBuilder db=dbf.newDocumentBuilder();
			xmlis=new FileInputStream(xml);
			doc=db.parse(xmlis);
			doc.normalize();
			xmlis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	XmlReader(String xml){
		DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db=dbf.newDocumentBuilder();
			doc=db.parse(xml);
			doc.normalize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	Document getDoc(){
		return doc;
	}
}