package xypipeline;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

class Command {
	private String ID;
	private String Program;
	private String Script;
	private Parameter[] Parameters;
	private String Stdout;
	private String Stderr;
	private String Check=null;
	Command(Element cmd){
		this.ID = cmd.getAttribute(Consts.XML_ATT_ID);
		this.Program = cmd.getElementsByTagName(Consts.XML_TAG_PROGRAM).item(0).getTextContent();
		this.Script = cmd.getElementsByTagName(Consts.XML_TAG_SCRIPT).item(0).getTextContent();
		this.Stdout = cmd.getElementsByTagName(Consts.XML_TAG_STDOUT).item(0).getTextContent();
		this.Stderr = cmd.getElementsByTagName(Consts.XML_TAG_STDERR).item(0).getTextContent();
		
		NodeList ps = cmd.getElementsByTagName(Consts.XML_TAG_PARAMETER);
		Parameters = new Parameter[ps.getLength()];
		for(int i = 0 ; i < ps.getLength() ; i++){
			Element p = (Element)ps.item(i);
			String p_id = p.getAttribute(Consts.XML_ATT_ID);
			String p_type = p.getAttribute(Consts.XML_ATT_TYPE);
			String p_option = p.getAttribute(Consts.XML_ATT_OPTION);
			String p_default = p.getTextContent();
			Parameters[i] = new Parameter(p_id,p_type,p_option,p_default);
		}
	}
	String getID(){
		return ID;
	}
	String getCheck(){
		return Check;
	}
	void setCheck(String Check){
		this.Check=Check;
	}
	void setStdout(String Stdout){
		this.Stdout=Stdout;
	}
	void setStderr(String Stderr){
		this.Stderr=Stderr;
	}
	void write_cmd2xml(Document doc){
		XmlWriter.append_text_element(doc, doc.getElementsByTagName(Consts.DATA_ROOT).item(0), Consts.XML_TAG_PARAMETER, getParameters());
	}
	String run(String[] parameters, String id){
		if(parameters.length != Parameters.length)
			return "Parameter Length Mismatch. recevied:"+parameters.length+ " xml:"+Parameters.length;//null;
		ArrayList<String> cmd_temp = new ArrayList<String>();
		if(!"".equals(Program))
			cmd_temp.add(Program);
		if(!"".equals(Script))
			cmd_temp.add(Script);
		for(int i = 0 ; i < Parameters.length ; i++){
			if(Parameters[i].ifRequired() && "".equals(parameters[i]))
				return "Required parameter is empty.";//null;
			if("".equals(Parameters[i].getType()) && !"".equals(Parameters[i].getOption())){
				if(!"".equals(parameters[i]) && parameters[i].equals("true") || "".equals(parameters[i]) && Parameters[i].getDefault().equals("true"))
					cmd_temp.add(Parameters[i].getOption());
			}
			else{
				if(!"".equals(Parameters[i].getOption()))
					cmd_temp.add(Parameters[i].getOption());
				if(!"".equals(parameters[i])){
					if(Parameters[i].getType().equals(Consts.XML_TAG_PATH))
						parameters[i] = Consts.DATAPATH + DatabaseReader.getMagic(id) + "/" + parameters[i];
					cmd_temp.add(parameters[i]);
				}
				else
					cmd_temp.add(Parameters[i].getDefault());
			}
		}
		if(!"".equals(Stdout)){
			cmd_temp.add("1>>");
			cmd_temp.add(Stdout);
		}
		if(!"".equals(Stderr)){
			cmd_temp.add("2>>");
			cmd_temp.add(Stderr);
		}
		String[] cmd = new String[cmd_temp.size()];
		cmd_temp.toArray(cmd);
		return CmdDriver.cmd1(cmd)[1];
	}
	String getParameters(){
		String ParametersList = "";
		for(int i = 0 ; i < Parameters.length ; i++){
			ParametersList += Parameters[i].getID();
			if(!"".equals(Parameters[i].getType()))
				ParametersList += ":" + Parameters[i].getType();
			else
				ParametersList += ":null";
			if(Parameters[i].ifRequired())
				ParametersList += ":required,";
			else
				ParametersList += ":optional,";
		}
		return ParametersList;
	}
}
