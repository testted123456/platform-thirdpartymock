package com.platform.apps.thirdpartymock.rule;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Value;

public abstract class CallBackRule {

	String name;
	Document docRequest;
	Document defaultDocRequest;

	@Value("${xmlPath}")
	String xmlPath;

	public void init() {
		SAXReader saxReader = new SAXReader();

		try {
			File reqFile = new File(xmlPath + name + ".xml");
			docRequest = saxReader.read(reqFile);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Map<String, String> getInfos() {
		SAXReader saxReader = new SAXReader();

		try {
			File reqFile = new File(xmlPath + name + ".xml");
			docRequest = saxReader.read(reqFile);

			File defReqFile = new File(xmlPath + name + "-default.xml");
			defaultDocRequest = saxReader.read(defReqFile);

			Map<String, String> map = new HashMap<>();
			map.put("request", docRequest.asXML());
			map.put("defaultRequest", defaultDocRequest.asXML());

			return map;
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	public abstract boolean callBack(Map<String, String> requestMap);

}
