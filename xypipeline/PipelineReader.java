package xypipeline;

import java.util.Arrays;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

class PipelineReader{
	static Document doc;
	static HashMap<String, Command[]> Pipelines;
	static{
		doc=new XmlReader(Consts.PROGPATH + Consts.PIPELINE).getDoc();
		pipeline_list();
	}
	PipelineReader(){
	}
	static void pipeline_list(){
		NodeList pipeline_list = doc.getElementsByTagName(Consts.XML_TAG_PIPELINE);
		Pipelines = new HashMap<String, Command[]>();
		for(int i = 0 ; i < pipeline_list.getLength() ; i++){
			NodeList cs_nodes = ((Element)pipeline_list.item(i)).getElementsByTagName(Consts.XML_TAG_COMMAND);
			Command[] cs = new Command[cs_nodes.getLength()];
			for(int j = 0 ; j < cs_nodes.getLength() ; j++)
				cs[j] = new Command((Element)cs_nodes.item(j));
			Pipelines.put(pipeline_list.item(i).getAttributes().getNamedItem(Consts.XML_ATT_ID).getTextContent(),cs);
		}
	}
	static String[] getPipelines(){
		String[] ps_id = new String[Pipelines.size()];
		Pipelines.keySet().toArray(ps_id);
		Arrays.sort(ps_id);
		return ps_id;
	}
	static String[] getCommands(String pipeline){
		if(Pipelines.containsKey(pipeline)){
			Command[] cs = Pipelines.get(pipeline);
			String[] cs_id = new String[cs.length];
			for(int i = 0 ; i < cs.length ; i++){
				cs_id[i] = cs[i].getID();
			}
			return cs_id;
		}
		else
			return null;
	}
	static Command getCommand(String pipeline, int command){
		if(Pipelines.containsKey(pipeline) && command >= 0 && command < Pipelines.get(pipeline).length)
			return Pipelines.get(pipeline)[command];
		return null;
	}
}