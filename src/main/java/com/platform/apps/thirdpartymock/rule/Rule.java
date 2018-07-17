package com.platform.apps.thirdpartymock.rule;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public abstract class Rule {
	
	String name;
	Document docRequest;
	Document docResponse;
	Document docDefaultResponse;
	
	public void init() {
		SAXReader saxReader = new SAXReader();
		
		try {
			InputStream  reqIS = this.getClass().getResourceAsStream("/xml/" + name + "-Req.xml");
			InputStream  resIS = this.getClass().getResourceAsStream("/xml/" + name + "-Res.xml");
			
			docRequest = saxReader.read(reqIS);
			docResponse = saxReader.read(resIS);
		}catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Map<String, String> getInfos(){
			SAXReader saxReader = new SAXReader();
			
			try {
				InputStream  reqIS = this.getClass().getResourceAsStream("/xml/" + name + "-Req.xml");
				InputStream  resIS = this.getClass().getResourceAsStream("/xml/" + name + "-Res.xml");
				InputStream  deRresIS = this.getClass().getResourceAsStream("/xml/" + name + "-Res-default.xml");
				docRequest = saxReader.read(reqIS);
				docResponse = saxReader.read(resIS);
				docDefaultResponse = saxReader.read(deRresIS);
				
				String request = docRequest.asXML();
				System.out.println("request is:" + request);
				String response = docResponse.asXML();
				System.out.println("response is:" + response);
				String defaultResponse = docDefaultResponse.asXML();
				System.out.println("defaultResponse is:" + defaultResponse);
				
				Map<String, String> map = new HashMap<>();
				map.put("request", request);
				map.put("response", response);
				map.put("defaultResponse", defaultResponse);
				
				return map;
			}catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
	}
	
	//设置响应
	public void setResponse(String name, String response) {
		Writer out = null;
		XMLWriter writer = null;
		
		try {
			Document document = DocumentHelper.parseText(response);
			OutputFormat format = OutputFormat.createCompactFormat();
			format.setEncoding("UTF-8");
			format.setIndent(true); //设置是否缩进
			format.setIndent("    "); //以四个空格方式实现缩进
			format.setNewlines(true); //设置是否换行
			URL url = this.getClass().getResource("/xml/" + name + "-Res.xml");
			File file = new File(url.toURI());
			out = new FileWriter(file);
			writer = new XMLWriter(out, format);
			writer.write(document);
			writer.flush();
			out.flush();
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			if(null != writer) {
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(null != out) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		InputStream  resIS = this.getClass().getResourceAsStream("/xml/" + name + "-Res.xml");
		SAXReader saxReader = new SAXReader();
		try {
			docResponse = saxReader.read(resIS);
			System.out.println("xxx: " + docResponse.asXML());
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//响应设置为默认
	public boolean setDefault(String name) {
		InputStream is = null;
		OutputStream os = null;
		
		try {
		    is = 
		    this.getClass().getResourceAsStream("/xml/" + name + "-Res-default.xml");
			URL url = this.getClass().getResource("/xml/" + name + "-Res.xml");
			File file = new File(url.toURI());
			os = new FileOutputStream(file);
			byte [] buffer = new byte[1024];
			int size = 0;
			
			while((size = is.read(buffer)) != -1){
				os.write(buffer);
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}finally {
			if(null != is) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
			}
			if(null != os) {
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 规则处理后的响应
	 * @return 响应接口字符串
	 */
	public abstract String getRuleResponse(Document reqDocument);
}
