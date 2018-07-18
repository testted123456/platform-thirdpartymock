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
			String publicKey=  FastPayUtil.localPublicKey;
			String privateKey = FastPayUtil.localPrivateKey;
		
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
