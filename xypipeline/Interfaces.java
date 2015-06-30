package xypipeline;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.fileupload.disk.*;
import org.apache.commons.fileupload.util.*;
import org.apache.commons.fileupload.servlet.*;
import org.apache.commons.fileupload.*;
import org.w3c.dom.Document;

import javax.servlet.*;
import javax.servlet.http.*;



public class Interfaces extends HttpServlet{
	
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		res.setContentType("application/xml");
		HttpSession session=req.getSession();
		if(res!=null){
			res.setHeader("Cache-Control", "no-cache,must-revalidate");
			res.setHeader("Pragma", "no-cache");
			res.setHeader("Expires", "-1");
		}
		Document doc = XmlWriter.init(Consts.DATA_ROOT);
		String action=req.getParameter("action");
		String a = null;
		if (action.equals("getPipelines")){
			XmlWriter.write_list(doc, PipelineReader.getPipelines(), Consts.XML_TAG_PIPELINE);
			a = XmlWriter.xml2string(doc);
		}
		else if (action.equals("getSession")){
			a = session.getId();
		}
		else if (action.equals("getCommands")){
			XmlWriter.write_list(doc, PipelineReader.getCommands(req.getParameter("pipeline")), Consts.XML_TAG_COMMAND);
			a = XmlWriter.xml2string(doc);
		}
		else if (action.equals("getParameters")){
			PipelineReader.getCommand(req.getParameter("pipeline"), Integer.parseInt(req.getParameter("command"))).write_cmd2xml(doc);
			a = XmlWriter.xml2string(doc);
		}
		else if (action.equals("getIndividuals")){
			XmlWriter.write_list(doc, DatabaseReader.getIndividuals(), Consts.XML_TAG_INDIVIDUAL);
			a = XmlWriter.xml2string(doc);
		}
		else if (action.equals("newIndividual")){
			DatabaseReader.newIndividual(req.getParameter("id"));
		}
		else if (action.equals("usingIndividual")){
			//consider to return individual occupy
			String id = req.getParameter("id");
			DatabaseReader.usingIndividual(id);
			session.setAttribute("Individual", new IndividualReader(id));
		}
		else if (action.equals("releaseIndividual")){
			DatabaseReader.releaseIndividual(req.getParameter("id"));
			session.setAttribute("Individual", null);
		}
		else if (action.equals("removeIndividual")){
			DatabaseReader.removeIndividual(req.getParameter("id"));
		}
		else if (action.equals("removeFile")){
			if(session.getAttribute("Individual") != null){
				IndividualReader ir = (IndividualReader) session.getAttribute("Individual");
				ir.removeFile(req.getParameter("file"));
			}
		}
		else if (action.equals("getFiles")){
			if(session.getAttribute("Individual") != null){
				IndividualReader ir = (IndividualReader) session.getAttribute("Individual");
				XmlWriter.write_list(doc, ir.getFiles(), Consts.XML_TAG_FILE);
				a = XmlWriter.xml2string(doc);
			}
		}

		PrintWriter out;
		if(a!=null){
			if (GzipUtils.isGzipSupported(req) && !GzipUtils.isGzipDisabled(req) && a.length()>1024*16){
				out = GzipUtils.getGzipWriter(res);
				res.setHeader("Content-Encoding", "gzip");
			}
			else
				out = res.getWriter();
			out.print(a);
			out.close();
		}
	}
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
		//res.setContentType("application/xml");
		HttpSession session=req.getSession();
		if(res!=null){
			res.setHeader("Cache-Control", "no-cache,must-revalidate");
			res.setHeader("Pragma", "no-cache");
			res.setHeader("Expires", "-1");
		}
//		Instance ins=(Instance) session.getAttribute("Instance");
		String action=req.getParameter("action");
		String a = null;
		
				
		if (action.equals("run")){
			String contentType=req.getContentType();
			if(contentType.indexOf("multipart/form-data")>=0){
				DiskFileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				try{
					List fileItems = upload.parseRequest(req);
					java.util.Iterator i = fileItems.iterator();
					while(i.hasNext()){
						FileItem fi = (FileItem)i.next();
						if(fi.isFormField()&&fi.getFieldName().equals("parameters")){
							if(session.getAttribute("Individual") != null){
								String id = ((IndividualReader) session.getAttribute("Individual")).getId();
								/* 2015.6.30 Change:
								 *    the splitter of parameters is changed from ',' to '!'. 
								 *    The reason is that there is the splitter of filter's options is the same as ','. 
								 *    Change2
								 *    The split("!") is replaced with split("!",-1).
								 *    Because the one parameter split function does not store empty element after splitting string with splitter.*/
								a = PipelineReader.getCommand(req.getParameter("pipeline"), Integer.parseInt(req.getParameter("command"))).run(fi.getString().split("!",-1),id);
								//a +="  pipeline:"+req.getParameter("pipeline")+"  command:"+req.getParameter("command")+"   params:"+fi.getString()+" length:"+fi.getString().split("!",-1).length+" id:"+id;
							}
							res.setStatus(200);
						}
					}
				}catch(Exception e){
					e.printStackTrace();
					a+="  Exception:"+e;
				}finally{
				}
			}
		}
		
		
		PrintWriter out;
		/*
		out = res.getWriter();
		out.print("action: "+action+" ");
		out.print("Pipeline:");
		out.print(req.getParameter("pipeline"));
		out.print(" Command:");
		out.print(req.getParameter("command"));
		out.println("a rslt:"+a);
		out.close();
        */
		if(a!=null){
			if (GzipUtils.isGzipSupported(req) && !GzipUtils.isGzipDisabled(req) && a.length()>1024*16){
				out = GzipUtils.getGzipWriter(res);
				res.setHeader("Content-Encoding", "gzip");
			}
			else
				out = res.getWriter();
			out.print(a);
			out.close();
		}
		
		
	}
	public static String md5 (String s){
		char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
		try{
			byte[] bytes = s.getBytes();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(bytes);
			byte[] updateBytes = md.digest();
			int len = updateBytes.length;
			char myChar[] = new char[len*2];
			int k = 0;
			for(int i=0;i<len;i++){
				byte byte0 = updateBytes[i];
				myChar[k++] = hexDigits[byte0>>>4 & 0x0f];
				myChar[k++] = hexDigits[byte0 & 0x0f];
			}
			return new String(myChar);
		} catch (Exception e){
			return null;
		}
	}
	public static boolean ifURLexists(String URLName){
		try{
			HttpURLConnection.setFollowRedirects(false);
			HttpURLConnection con = (HttpURLConnection) new URL(URLName).openConnection();
			con.setRequestMethod("HEAD");
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		}
		catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
}

class GzipUtils {
	public static boolean isGzipSupported(HttpServletRequest request) {
		String encodings = request.getHeader("Accept-Encoding");
		return ((encodings != null) && (encodings.indexOf("gzip") != -1));
	}
	public static boolean isGzipDisabled(HttpServletRequest request) {
        String flag = request.getParameter("disableGzip");
        return ((flag != null) && (!flag.equalsIgnoreCase("false")));
    }
	public static PrintWriter getGzipWriter(HttpServletResponse response){
        try {
            return (new PrintWriter(new GZIPOutputStream(response.getOutputStream())));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}
