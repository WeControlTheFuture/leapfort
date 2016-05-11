package org.wctf.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.wctf.util.JaxbUtil;

public class Hosts {

	private static Map<String, Host> hosts = new HashMap<>();

	public static Collection<Host> getHosts() {
		if (hosts.size() == 0) {
			init();
		}
		return hosts.values();
	}

	private synchronized static void init() {
		if (hosts.size() == 0)
			try {
				List<Host> hostList = JaxbUtil.unmarshal(Host.class, "src/main/resources/host-info.xml");
				for (Host h : hostList) {
					hosts.put(h.getName(), h);
				}
			} catch (JAXBException e) {
				e.printStackTrace();
			}
	}

	public static Host getHost(String name) {
		return hosts.get(name);
	}
}
