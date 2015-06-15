package xypipeline;

import java.io.FileWriter;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;

public class XmlWriter {
	Document doc;
	XmlWriter(){
		init(Consts.DATA_ROOT);
	}
	XmlWriter(String roottag){
		init(roottag);
	}
	static Document init(String roottag){
		Document doc;
		DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db=dbf.newDocumentBuilder();
			doc=db.newDocument();
			Element root=doc.createElement(roottag);
			doc.appendChild(root);
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	public static Element append_text_element(Document doc,Node parent,String tag,String text){
		Element offspring=doc.createElement(tag);
		offspring.appendChild(doc.createTextNode(text));
		parent.appendChild(offspring);
		return offspring;
	}
	static String xml2string(Document doc){
		String xml = "";
		try{
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			xml = writer.getBuffer().toString().replaceAll("\n|\r", "");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return xml;
	}
	static void xml2file(Document doc, String file){
		try{
			FileWriter fw = new FileWriter(file);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			String xml = writer.getBuffer().toString();
			fw.write(xml);
			fw.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	static Element write_list(Document doc,String[] list,String tag){
		StringBuffer temp=new StringBuffer();
		for(int i=0;i<list.length;i++){
			if(list[i]==null)
				continue;
			if(i>0)
				temp.append(",");
			temp.append(list[i]);
		}
		return XmlWriter.append_text_element(doc,doc.getElementsByTagName(Consts.DATA_ROOT).item(0),tag,temp.toString());
	}
}