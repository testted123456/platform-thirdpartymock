package com.platform.apps.thirdpartymock.rule;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.util.ResourceUtils;

public abstract class Rule {

	String name;
	Document docRequest;
	Document docResponse;
	Document docDefaultResponse;
	
/*	public Rule(String name) {
		this.name = name;
	}*/
	
	public void init() {
		SAXReader saxReader = new SAXReader();
		
		try {
			File requestFile = ResourceUtils.getFile("classpath:xml/" + name + "-Req.xml");
			File responseFile = ResourceUtils.getFile("classpath:xml/" + name + "-Res.xml");
			docRequest = saxReader.read(requestFile);
			docResponse = saxReader.read(responseFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Map<String, String> getInfos(){
			SAXReader saxReader = new SAXReader();
			/*docRequest = saxReader.read("src/main/resources/xml/" + name + "-Req.xml");
			docResponse = saxReader.read("src/main/resources/xml/" + name + "-Res.xml");
			docDefaultResponse = saxReader.read("src/main/resources/xml/" + name + "-Res-default.xml");*/
			
			try {
				File requestFile = ResourceUtils.getFile("classpath:xml/" + name + "-Req.xml");
				File responseFile = ResourceUtils.getFile("classpath:xml/" + name + "-Res.xml");
				File defaultResponseFile  = ResourceUtils.getFile("classpath:xml/" + name + "-Res-default.xml");
				docRequest = saxReader.read(requestFile);
				docResponse = saxReader.read(responseFile);
				docDefaultResponse = saxReader.read(defaultResponseFile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String request = docRequest.asXML();
			String response = docResponse.asXML();
			String defaultResponse = docDefaultResponse.asXML();
			
			Map<String, String> map = new HashMap<>();
			map.put("request", request);
			map.put("response", response);
			map.put("defaultResponse", defaultResponse);
			
			return map;
		
	}
	
	//设置响应
	public void setResponse(String name, String response) {
		StringWriter sw = null;
		
		try {
			Document document = DocumentHelper.parseText(response);
			OutputFormat format = OutputFormat.createCompactFormat();
			format.setEncoding("UTF-8");
			format.setIndent(true); //设置是否缩进
			format.setIndent("    "); //以四个空格方式实现缩进
			format.setNewlines(true); //设置是否换行
			Writer out = new FileWriter("src/main/resources/xml/" + name + "-Res.xml");
			XMLWriter writer = new XMLWriter(out, format);
			writer.write(document);
			writer.close();
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			if(null != sw) {
				try {
					sw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	//响应设置为默认
	public boolean setDefault(String name) {
		InputStream is = null;
		OutputStream os = null;
		
		try {
		    is = new FileInputStream("src/main/resources/xml/" + name + "-Res-default.xml");
			os = new FileOutputStream("src/main/resources/xml/" + name + "-Res.xml");
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
