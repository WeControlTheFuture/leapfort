package org.wctf;

import java.util.HashMap;
import java.util.Map;

import org.wctf.model.Host;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public final class SessionPool {
	private static Map<String, Session> pool = new HashMap<>(50);

	private SessionPool() {
	}

	public synchronized static Session getSessionInfo(Host host) throws JSchException {
		if (pool.size() == 50)
			return null;
		JSch jsch = new JSch();
		Session session = jsch.getSession(host.getUsername(), host.getIp(), host.getPort());
		session.setPassword(host.getPassword());
		session.setConfig("StrictHostKeyChecking", "no");
		session.connect(30000);

		Channel channel = session.openChannel("shell");
		channel.connect();
		pool.put(session.toString(), session);
		return session;
	}

}
