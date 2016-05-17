package org.wctf.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wctf.SessionPool;
import org.wctf.SessionPool.SessionThread;
import org.wctf.model.Host;
import org.wctf.model.Hosts;
import org.wctf.model.ShellData;

import com.google.gson.Gson;
import com.jcraft.jsch.JSchException;

@Controller
@RequestMapping(value = "/ssh")
public class SSHControl {

	@RequestMapping(value = "/create/{connectionId}/{name}")
	@ResponseBody
	public String createSession(HttpServletResponse response, @PathVariable String connectionId, @PathVariable String name) {
		response.setContentType("text/event-stream");
		response.setCharacterEncoding("UTF-8");
		if ("null".equals(connectionId)) {
			try {
				connectionId = initConnect(response);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSchException e) {
				e.printStackTrace();
			}
		}
		Host host = Hosts.getHost(name);
		System.out.println(host);
		try {
			SessionThread session = SessionPool.createSessionThread(connectionId, host);
			session.start();
		} catch (JSchException e) {
			e.printStackTrace();
		}
		return connectionId;
	}

	@RequestMapping(value = "/stream/{connectionId}")
	public void stream(HttpServletRequest request, HttpServletResponse response, @PathVariable String connectionId) {
		try {
			response.setContentType("text/event-stream");
			response.setCharacterEncoding("UTF-8");
			PrintWriter writer = response.getWriter();
			if ("null".equals(connectionId)) {
				connectionId = initConnect(response);
			}
			SessionThread sessionThread = SessionPool.getSessionThread(connectionId);
			if (sessionThread != null) {
				InputStream instream = sessionThread.getInputstream();
				int num = instream.available();
				System.out.println("available ......." + num);
				if (num > 0) {
					byte[] data = new byte[instream.available()];
					int nLen = instream.read(data);
					String temp = new String(data, 0, nLen, "UTF-8");
					ShellData shellData = new ShellData(temp);
					writer.write("event:data\n");
					writer.write("data: " + (new Gson()).toJson(shellData) + "\n\n");
					writer.flush();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/send/{connectionId}", method = RequestMethod.POST)
	public void send(HttpServletRequest request, HttpServletResponse response, @PathVariable(value = "connectionId") String connectionId, @RequestParam String data) {
		// System.out.println("connectionId=======" + connectionId);
		System.out.println("send data===============" + data);
		SessionThread sessionThread = SessionPool.getSessionThread(connectionId);
		if (sessionThread != null) {
			OutputStream outputStream = sessionThread.getOutputStream();
			if (outputStream != null) {
				try {
					outputStream.write(data.getBytes());
					outputStream.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String initConnect(HttpServletResponse response) throws IOException, JSchException {
		System.out.println("create connectid");
		String connectId = UUID.randomUUID().toString();
		PrintWriter writer = response.getWriter();
		writer.write("event:connectionId\n");
		writer.write("data:" + connectId + "\n\n");
		writer.flush();
		return connectId;
	}
}
