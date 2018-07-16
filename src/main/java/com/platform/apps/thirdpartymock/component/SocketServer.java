package com.platform.apps.thirdpartymock.component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.platform.apps.thirdpartymock.rule.Rule;
import com.platform.apps.thirdpartymock.rule.ZF001Rule;
import com.platform.apps.thirdpartymock.rule.ZF003Rule;
import com.platform.apps.thirdpartymock.util.FastPayUtil;

@Component
public class SocketServer {
	
	public static Logger logger = LoggerFactory.getLogger(SocketServer.class);
	
	@Value("${socketServer.port}")
    String port;
	
	@Autowired
	ApplicationContextProvider applicationContextProvider;

	// 解码buffer
	private Charset cs = Charset.forName("UTF-8");
	
	//请求消息第一个字段表示报文长度
	private static ByteBuffer firstDomainBuffer = ByteBuffer.allocate(8);
	
	// 选择器（叫监听器更准确些吧应该）
	private static Selector selector;

	/**
	 * 启动socket服务，开启监听
	 * 
	 * @param port
	 * @throws IOException
	 */
	public void startSocketServer() {
		try {
			// 打开通信信道
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			// 设置为非阻塞
			serverSocketChannel.configureBlocking(false);
			// 获取套接字
			ServerSocket serverSocket = serverSocketChannel.socket();
			// 绑定端口号
			serverSocket.bind(new InetSocketAddress(Integer.valueOf(port)));
			// 打开监听器
			selector = Selector.open();
			// 将通信信道注册到监听器
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

			// 监听器会一直监听，如果客户端有请求就会进入相应的事件处理
			while (true) {
				selector.select();// select方法会一直阻塞直到有相关事件发生或超时
				Set<SelectionKey> selectionKeys = selector.selectedKeys();// 监听到的事件
				
				for (SelectionKey key : selectionKeys) {
					handle(key);
				}
				
				selectionKeys.clear();// 清除处理过的事件
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 处理不同的事件
	 * 
	 * @param selectionKey
	 * @throws IOException
	 */
	private void handle(SelectionKey selectionKey) throws IOException {
		ServerSocketChannel serverSocketChannel = null;
		SocketChannel socketChannel = null;
		String requestMsg = "";
		int count = 0;
		if (selectionKey.isAcceptable()) {
			logger.info("select key is acceptable...");
			
			// 每有客户端连接，即注册通信信道为可读
			serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
			socketChannel = serverSocketChannel.accept();
			socketChannel.configureBlocking(false);
			socketChannel.register(selector, SelectionKey.OP_READ);

		} else if (selectionKey.isReadable()) {
			socketChannel = (SocketChannel) selectionKey.channel();
			
			firstDomainBuffer.clear();
			count = socketChannel.read(firstDomainBuffer);
			String reqLength = new String(firstDomainBuffer.array(), "UTF-8");
			int length = Integer.valueOf(reqLength);
			
			//请求消息中剩余4个域
			ByteBuffer rBuffer = ByteBuffer.allocate(length);
			rBuffer.clear();
			count = socketChannel.read(rBuffer);
			
			// 读取数据
			if (count > 0) {
				rBuffer.flip();
				requestMsg = String.valueOf(cs.decode(rBuffer).array());
			}
			
			String enCoding = "UTF-8";
			String  publicKey=  "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1NyXKwaNA180nCE+6Zrq\r\n" + 
					"9RoCFDY+oyoVy7aQsVFUbYtkP6UTSjDyvtGx5uRLjUMIFNDSz2a6J/lzg4uiu7r4\r\n" + 
					"O8yK8ItRVckWGnjZuFUQmVpeTpgaEmL1uYfaGl2ZT7ebPef9cCI1lhxmcPA9yBYB\r\n" + 
					"Ip7xFtg5DsXGU3FhR15NK0BcpjgmszAABV/eydndUeE8OJ9YKgbEeuyIP9zAuLDC\r\n" + 
					"rwgQ6mszrkOm50frB4Rr1cht5N6NYiSusvP8N0f6TZmz0Cub9P7nSLkerinmqiwW\r\n" + 
					"3wsnYUkR4aoqhF0NpRSVUpGjxEkEq3M5jYOHrEQQegj+s8R1w3Orf2soFn7JYd/y\r\n" + 
					"QwIDAQAB";
			String privateKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDU3JcrBo0DXzSc\r\n" + 
					"IT7pmur1GgIUNj6jKhXLtpCxUVRti2Q/pRNKMPK+0bHm5EuNQwgU0NLPZron+XOD\r\n" + 
					"i6K7uvg7zIrwi1FVyRYaeNm4VRCZWl5OmBoSYvW5h9oaXZlPt5s95/1wIjWWHGZw\r\n" + 
					"8D3IFgEinvEW2DkOxcZTcWFHXk0rQFymOCazMAAFX97J2d1R4Tw4n1gqBsR67Ig/\r\n" + 
					"3MC4sMKvCBDqazOuQ6bnR+sHhGvVyG3k3o1iJK6y8/w3R/pNmbPQK5v0/udIuR6u\r\n" + 
					"KeaqLBbfCydhSRHhqiqEXQ2lFJVSkaPESQSrczmNg4esRBB6CP6zxHXDc6t/aygW\r\n" + 
					"fslh3/JDAgMBAAECggEBAILqZOLmdYX0YRQBcoMUb5ttcofh0OZjNOKElzsnTSjO\r\n" + 
					"iUqHiXSg5DsuFx9tm3X3GxpKUM2UXbvGNSBbmiuh05hbtbw5wz1inoCLgURYtQvM\r\n" + 
					"Q6JM9AQI7x47io2asXcQ8p7BduMndxOqxeqCJ01PE0WqwZaOR5FpXA/n9K+DNSz6\r\n" + 
					"mpebPT8C6dpM8YFwNpK3uyqCvtCHXzNKIdjLsUpnOHFNxptQGertNgWWcDFnPqCB\r\n" + 
					"FHWGZB/koZcc1nkcbRsHHtDc3p8eHq/yDjDnCy+spaJCscpIz/cH92uxJ0UQc6j6\r\n" + 
					"IrbTvjkeLNuVqRfUPtxBHCh/9gF0CiIEl9Z67zBp/fECgYEA7A2DckkI2hSs3Aje\r\n" + 
					"zETeskMp9bN4r73+WE4y3pnAO+jw1J/f7R/IDLK0diPkFfMvX1dvSHwDAPoloFrg\r\n" + 
					"iM4MFEd5LJBKtESgi87pv9upEREHl172JZCqFqAChjMUMxZyhTFltQ0Mlvr29JYx\r\n" + 
					"FyiP3bBmNavufT2ASkXd7V1klGsCgYEA5tljW9uHNJdZ4oTPjDib/gbFyhhDWpdc\r\n" + 
					"UtG2n5QCTFoENS1eROeytMfBzZd/VYzVGHGi2zMXXHCvVEjDcj0MPIY9tw3962Ya\r\n" + 
					"lHDr5TGUaRNPdA6t/F31EW+tbQdxIgidO10/qg4d8FHW2SGPtumn3x8SUwJ3LcZB\r\n" + 
					"AMp01auez4kCgYBt9N2GHv9AluDunjMqc+pIn4CsMy3Er3wLJmILzxL81UVZxh9J\r\n" + 
					"FcTaJWRqo81bbCxk/RWZbir4CY39ZFezYke+Ko0HdS9XSaB/f6Li/C+FA6nFmTaP\r\n" + 
					"yRNx9pc2bBS8t3ghEyGNdMaojb77r/quDvb1DXaPCcfODxfzgRgfuaFggQKBgQDD\r\n" + 
					"asb3XZDI7k6CuJbRRqKWmbkZaQyrRXI8EZcc4zhG1fS151/jyJe4/ViL53E9RGin\r\n" + 
					"tpp8IHXoJOPm32nD7PXMzn6z1T5b5SUe/7BRZnURte0Oe9bAyfDI/9a5cxFMFCq1\r\n" + 
					"tCxOkoC5jS3oxMZZutYGxNDIQFAPCcLBHqFkt1FKsQKBgQDQlLXTCx+nP7J8P8IF\r\n" + 
					"VSzgfkRJoHyE0dI5wVKmeSIyspBFOWN3ONNhARbx0Et4lk8C3mjnvipqV2bpJ3DA\r\n" + 
					"jr8xS6lfLXDjhNdc/qm9wDqqOtGUxC2ZaT9yROviL9jPvdU7zl98DUe2zOMZ5lGU\r\n" + 
					"miMf91dHF++BxD1gljaaD0aJPA==";
		
			try {
				String res = FastPayUtil.decode(firstDomainBuffer.array(), rBuffer.array(), enCoding, privateKey, publicKey);
				logger.info("客户端的消息:" + res);
				Document document = new SAXReader().read(new ByteArrayInputStream(res.getBytes("UTF-8")));
				Node node = document.selectSingleNode("/ROOT/MSG_HEAD/tranCode");
				String tranCode = node.getText();
				
//				System.out.println(document.getText());
				
				logger.info("请求码：{}", tranCode );
				
				if(null != tranCode) {
					Rule rule = applicationContextProvider.getBean(tranCode + "Rule", com.platform.apps.thirdpartymock.rule.Rule.class);
					rule.init();
					String response = rule.getRuleResponse(document);
					byte[] resByte = FastPayUtil.encode(response, "1028", enCoding, privateKey, publicKey);
					socketChannel.write(ByteBuffer.wrap(resByte));
				}
				
				/*if("ZF001".equals(tranCode)) {
					zf001Rule.init();
					String response = zf001Rule.getRuleResponse(document);
					byte[] resByte = FastPayUtil.encode(response, "1028", enCoding, privateKey, publicKey);
					socketChannel.write(ByteBuffer.wrap(resByte));
				}*/
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			socketChannel.close();
		}
	}

}
