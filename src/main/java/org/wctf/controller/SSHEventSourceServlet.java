package org.wctf.controller;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.servlets.EventSource;
import org.eclipse.jetty.servlets.EventSourceServlet;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

//@Controller
//@RequestMapping(value = "/ssh")
public class SSHEventSourceServlet extends EventSourceServlet {

//	@RequestMapping(value = "/stream", produces = "text/event-stream; charset=UTF-8")
//	@ResponseBody
	protected EventSource newEventSource(HttpServletRequest request) {
		System.out.println("call SSHEventSourceServlet.....");
		return new SSHEventSource();
	}

}
