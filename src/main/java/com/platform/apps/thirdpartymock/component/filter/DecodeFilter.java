package com.platform.apps.thirdpartymock.component.filter;

import java.io.IOException;
import java.io.InputStream;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.CharacterEncodingFilter;
import com.platform.apps.thirdpartymock.util.SMCryptUtil;

@Order(-1)
@WebFilter(urlPatterns={"/mm"},filterName="decodeFilter")
public class DecodeFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		CharacterEncodingFilter c;
		
		String privateKey = SMCryptUtil.smzfPriKey;
		String publicKey = SMCryptUtil.smzfPubKey;
		
        InputStream is = request.getInputStream();
        
        byte [] bytes = new byte[8];
        is.read(bytes);
        
        System.out.println(new String(bytes, "UTF-8"));
        
		try {
			/*String res = RyxAuthUtil.decode(is, "UTF-8", privateKey, publicKey);
			System.out.println(res);*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
