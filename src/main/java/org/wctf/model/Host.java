package org.wctf.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "host")
@XmlType(propOrder = { "name", "ip", "port", "username", "password" })
public class Host {
	private String name;
	private String ip;
	private Integer port;
	private String username;
	private String password;

	@Override
	public String toString() {
		return "Host [name=" + name + ", ip=" + ip + ", port=" + port + ", username=" + username + ", password=" + password + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
