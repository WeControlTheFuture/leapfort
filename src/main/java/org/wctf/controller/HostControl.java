package org.wctf.controller;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wctf.model.Host;
import org.wctf.model.Hosts;

import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/host")
public class HostControl {

	@RequestMapping(value = "/get", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String getHost() {
		// System.out.println("call get host.....");
		Collection<Host> hosts = new ArrayList<>();
		hosts = Hosts.getHosts();
		// System.out.println((new Gson()).toJson(hosts));
		return (new Gson()).toJson(hosts);
	}
	
	
}
