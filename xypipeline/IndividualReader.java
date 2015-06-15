package xypipeline;

import java.io.File;
import org.w3c.dom.Document;

class IndividualReader{
//	Document doc;
	String id;
	String magic;
	IndividualReader(String id){
		this.id = id;
		magic = DatabaseReader.getMagic(id);
//		doc = new XmlReader(Consts.DATAPATH + magic + "/" + Consts.INDIVIDUAL).getDoc();
	}
	String[] getFiles(){
		String[] cmd =new String[2];
		cmd[0] = "ls";
		cmd[1] = Consts.DATAPATH + magic + "/";
		String[] files = CmdDriver.cmd2(cmd).split("\n");
		return files;
	}
	String getId(){
		return id;
	}
	void removeFile(String file){
		try{
			File src, dest;
			if(magic != null){
				src = new File(Consts.DATAPATH + magic + "/" + file);
				dest = new File(Consts.DATAPATH + magic + "/trash/" + file + "." + System.currentTimeMillis());
				if(src.exists() && !src.isDirectory() && !dest.exists())
					src.renameTo(dest);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	String getInformation1(){
		//For normal people
		return null;
	}
	String getInformation2(){
		//For specialist
		return null;
	}
}