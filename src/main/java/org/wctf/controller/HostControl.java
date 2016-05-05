package org.wctf.controller;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wctf.model.Host;
import org.wctf.util.JaxbUtil;

import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/host")
public class HostControl {

	@RequestMapping(value = "/get", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	@ResponseBody
	public String getHost() {
		System.out.println("call  get host.....");
		List<Host> hosts = new ArrayList<>();
		try {
			hosts = JaxbUtil.unmarshal(Host.class, "src/main/resources/host-info.xml");
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		System.out.println((new Gson()).toJson(hosts));
		return (new Gson()).toJson(hosts);
	}
}
