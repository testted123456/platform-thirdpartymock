package com.platform.apps.thirdpartymock.rule.upzf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import com.alibaba.fastjson.JSONObject;

public abstract class UPZFRule {
	
	public static Logger logger = LoggerFactory.getLogger(UPZFRule.class);

	
	String name;
	Document docRequest;
	Document docResponse;
	Document docDefaultResponse;
	
	@Value("${xmlPath}")
    String xmlPath;
	
	/**
	 * 加载xml文件
	 */
	public void init() {
		SAXReader saxReader = new SAXReader();
		
		try {
			File reqFile = new File(xmlPath + "upzf" + File.separator + name + "-Req.xml");
			File resFile = new File(xmlPath + "upzf" + File.separator + name + "-Res.xml");
			docRequest = saxReader.read(reqFile);
			docResponse = saxReader.read(resFile);
		}catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取请求、响应
	 * @return
	 */
	public Map<String, String> getInfos(){ 
			SAXReader saxReader = new SAXReader();
			
			try {
				File reqFile = new File(xmlPath + "upzf" + File.separator + name + "-Req.xml");
				docRequest = saxReader.read(reqFile);
				
				File resFile = new File(xmlPath + "upzf" + File.separator + name + "-Res.xml");
				docResponse = saxReader.read(resFile);
				
				File resDefFile = new File(xmlPath + "upzf" + File.separator + name + "-Res-default.xml");
				docDefaultResponse = saxReader.read(resDefFile);
				
				String request = docRequest.asXML();
				logger.info("request is:" + request);
//				System.out.println("request is:" + request);
				String response = docResponse.asXML();
				logger.info("response is:" + response);
				String defaultResponse = docDefaultResponse.asXML();
				logger.info("defaultResponse is:" + defaultResponse);
				
				Map<String, String> map = new HashMap<>();
				map.put("request", request);
				map.put("response", response);
				map.put("defaultResponse", defaultResponse);
				
				return map;
			}catch (Exception e) {
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
			Document document = DocumentHelper.parseText(response.trim());
			OutputFormat format = OutputFormat.createCompactFormat();
			format.setEncoding("UTF-8");
			format.setIndent(true); //设置是否缩进
			format.setIndent("    "); //以四个空格方式实现缩进
			format.setNewlines(true); //设置是否换行
			File file = new File(xmlPath + "upzf" + File.separator + name + "-Res.xml");
			out = new FileWriter(file);
			writer = new XMLWriter(out, format);
			writer.write(document);
			writer.flush();
			out.flush();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
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
		
		SAXReader saxReader = new SAXReader();
		try {
			docResponse = saxReader.read(new File(xmlPath + "upzf" + File.separator + name + "-Res.xml"));
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
		    is = new FileInputStream(xmlPath + name + "-Res-default.xml");
			os = new FileOutputStream(xmlPath + name + "-Res.xml");
			byte [] buffer = new byte[1024];
			int size = 0;
			
			while((size = is.read(buffer)) != -1){
				os.write(buffer, 0, size);
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
	public abstract JSONObject getRuleResponse(JSONObject reqJson);
}
