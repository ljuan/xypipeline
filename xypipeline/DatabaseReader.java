package xypipeline;

import java.io.File;
import java.util.HashMap;
import java.util.Random;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

class DatabaseReader{
	static Document doc;
	static HashMap<String, String> Individuals;
	static {
		doc = new XmlReader(Consts.DATAPATH + Consts.DATABASE).getDoc();
		NodeList individual_list = doc.getElementsByTagName(Consts.XML_TAG_INDIVIDUAL);
		Individuals = new HashMap<String, String>();
		for(int i = 0 ; i < individual_list.getLength() ; i++){
			Element individual = (Element)individual_list.item(i);
			Individuals.put(individual.getAttribute(Consts.XML_ATT_ID),individual.getTextContent());
		}
	}
	DatabaseReader(){
	}
	static String[] getIndividuals(){
		String[] is_id = new String[Individuals.size()];
		Individuals.keySet().toArray(is_id);
//		Arrays.sort(is_id);
		return is_id;
	}
	static String getMagic(String id){
		if(Individuals.containsKey(id))
			return Individuals.get(id);
		else
			return null;
	}
	static void newIndividual(String id){
		if(Individuals.containsKey(id))
			return;
		
		String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		for(int i = 0 ; i < 6 ; i++)
			sb.append(base.charAt(random.nextInt(base.length())));
		
		String magic = Individuals.size() + "." + sb.toString();
		Individuals.put(id, magic);
		
		Element individual = XmlWriter.append_text_element(doc, doc.getElementsByTagName("root").item(0), Consts.XML_TAG_INDIVIDUAL, magic);
		individual.setAttribute(Consts.XML_ATT_ID, id);
		
		File f = new File(Consts.DATAPATH + magic);
		if(!f.exists()){
			f.mkdir();
			new File(Consts.DATAPATH + magic + "/trash").mkdir();
		}
		XmlWriter.xml2file(doc, Consts.DATAPATH + Consts.DATABASE);
	}
	static boolean usingIndividual(String id){//true: now using by the user; false: have been removed or occupied by others.
		String magic = null;
		if(Individuals.containsKey(id))
			magic = Individuals.get(id);
		if(magic != null){
			int index = Integer.parseInt(magic.split("\\.")[0]);
			Element e = (Element)doc.getElementsByTagName(Consts.XML_TAG_INDIVIDUAL).item(index);
			if(!e.hasAttribute(Consts.XML_ATT_FLAG)){
				e.setAttribute(Consts.XML_ATT_FLAG, Consts.XML_FLAG_USING);
				XmlWriter.xml2file(doc, Consts.DATAPATH + Consts.DATABASE);
				return true;
			}
		}
		return false;
	}
	static void releaseIndividual(String id){
		String magic = null;
		if(Individuals.containsKey(id))
			magic = Individuals.get(id);
		if(magic != null){
			int index = Integer.parseInt(magic.split("\\.")[0]);
			Element e = (Element)doc.getElementsByTagName(Consts.XML_TAG_INDIVIDUAL).item(index);
			if(e.getAttribute(Consts.XML_ATT_FLAG).equals(Consts.XML_FLAG_USING)){
				e.removeAttribute(Consts.XML_ATT_FLAG);
				XmlWriter.xml2file(doc, Consts.DATAPATH + Consts.DATABASE);
			}
		}
	}
	static void removeIndividual(String id){
		String magic = null;
		if(Individuals.containsKey(id))
			magic = Individuals.get(id);
		try{
//			File src, dest;
			if(magic != null){
				int index = Integer.parseInt(magic.split("\\.")[0]);
				Element e = (Element)doc.getElementsByTagName(Consts.XML_TAG_INDIVIDUAL).item(index);
				e.setAttribute(Consts.XML_ATT_FLAG, Consts.XML_FLAG_REMOVED);
				
//				src = new File(Consts.DATAPATH + magic);
//				dest = new File(Consts.DATAPATH + "trash/" + magic + "." + System.currentTimeMillis());
//				if(src.exists() && src.isDirectory() && !dest.exists())
//					src.renameTo(dest);
				
				XmlWriter.xml2file(doc, Consts.DATAPATH + Consts.DATABASE);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}