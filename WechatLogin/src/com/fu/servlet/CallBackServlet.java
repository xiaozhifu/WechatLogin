package com.fu.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fu.util.AuthUtil;

import net.sf.json.JSONObject;

@WebServlet("/callBack")
public class CallBackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String code = request.getParameter("code");
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+AuthUtil.APPID
				+ "&secret="+AuthUtil.APPSECRET
				+ "&code="+code
				+ "&grant_type=authorization_code";
		JSONObject jsonObject = AuthUtil.doGetJson(url);
		String token = null;
		String openid = null;
		if(jsonObject != null){
			token = jsonObject.getString("access_token");
			openid = jsonObject.getString("openid");
		}
		
		String infoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token="+token
				+ "&openid="+openid
				+ "&lang=zh_CN";
		JSONObject userInfo = AuthUtil.doGetJson(infoUrl);
		System.out.println(userInfo);
		
		//1.使用用户信息直接登陆，无需注册和绑定
		if(userInfo != null){
			request.setAttribute("user", userInfo);
			request.getRequestDispatcher("/success.jsp").forward(request, response);
		}
		
		//2.将微信与当前系统账号进行绑定
		//Openid存入数据库。（订阅号与微信开放绑定后，存入unionId）
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
