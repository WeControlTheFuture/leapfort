package org.wctf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.wctf.model.Host;
import org.wctf.util.JaxbUtil;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public final class SessionPool {
	private static Map<String, SessionThread> pool = new HashMap<>(50);

	private SessionPool() {
	}

	public synchronized static SessionThread createSessionThread(String uuid, Host host) throws JSchException {
		if (pool.size() == 50)
			return null;

		SessionThread sessionThread = pool.get(uuid);

		if (host != null) {
			JSch jsch = new JSch();
			Session session = jsch.getSession(host.getUsername(), host.getIp(), host.getPort());
			session.setPassword(host.getPassword());
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect(30000);

			if (sessionThread != null)
				sessionThread.getSession().disconnect();
			else
				sessionThread = new SessionThread(session);

			pool.put(uuid, sessionThread);
			return sessionThread;
		} else
			return null;

	}

	public static SessionThread getSessionThread(String uuid) {
		return pool.get(uuid);
	}

	public static class SessionThread {
		private Session session;
		private OutputStream outstream;
		private InputStream inputstream;

		public SessionThread(Session session) {
			this.session = session;
		}

		public void start() {
			try {
				Channel channel = session.openChannel("shell");
				channel.connect();
				this.inputstream = channel.getInputStream();
				this.outstream = channel.getOutputStream();
			} catch (JSchException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public Session getSession() {
			return session;
		}

		public OutputStream getOutputStream() {
			return this.outstream;
		}

		public InputStream getInputstream() {
			return inputstream;
		}

	}

	public static void main(String[] args) throws JSchException, JAXBException, IOException, InterruptedException {
		List<Host> hosts = JaxbUtil.unmarshal(Host.class, "src/main/resources/host-info.xml");

		SessionThread sessionThread = SessionPool.createSessionThread("123", hosts.get(0));
		sessionThread.start();
		System.out.println(hosts.get(0));
		// OutputStream outstream = channel.getOutputStream();
		InputStream instream = sessionThread.getInputstream();
		// outstream.write("ls \n".getBytes());
		// outstream.flush();
		while (true) {
			System.out.println("available========" + instream.available());
			byte[] data = new byte[instream.available()];
			int nLen = instream.read(data);

			// if (nLen < 0) {
			// throw new Exception("network error.");
			// }

			// 转换输出结果并打印出来
			String temp = new String(data, 0, nLen, "UTF-8");
			String[] tmpArr = temp.split("\r\n", -1);
			for (String tmp : tmpArr) {
				System.out.println("========" + tmp);
			}
//			System.out.println(temp.indexOf("\r\n"));
//			System.out.println(temp.indexOf("\r"));
//			System.out.println(temp.indexOf("\n"));
			System.out.println(temp);
			Thread.sleep(2000);
		}
		// os.write("ls".getBytes());
		// is.read(b)
	}
}
