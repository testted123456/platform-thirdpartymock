package com.platform.apps.thirdpartymock.component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.web.servlet.tags.form.InputTag;

import com.platform.apps.thirdpartymock.util.FastPayUtil;
import com.platform.apps.thirdpartymock.util.SignatureUtilQuickPay;
import com.sun.corba.se.spi.orbutil.fsm.Input;

public class Test {

	public static void main(String[] args) throws Exception {
		/*String xml =
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?><ROOT><MSG_HEAD><tranCode>ZF001</tranCode><systemNo>quickp2018071118433400017591</systemNo><merchId>1028</merchId><date>20180711</date><time>184334</time></MSG_HEAD><MSG_BODY><cardName>姚银兵</cardName><cardNo>6217710204605794</cardNo><orderAmt>111.00</orderAmt><orderTitle>kuaijiezhifu</orderTitle><orderDesc>快捷支付</orderDesc><subMerch>88745D279111910</subMerch><bizType>100099</bizType><subMerchNo>000100200000005</subMerchNo></MSG_BODY></ROOT>";

		Document document = new SAXReader().read(new ByteArrayInputStream(xml.getBytes("UTF-8")));
		Node node = document.selectSingleNode("/ROOT/MSG_HEAD/tranCode");
		String tranCode = node.getText();
		System.out.println(tranCode);
		System.out.println(document.asXML());*/
		
		System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

	}

}
