package org.wctf.controller;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.servlets.EventSource;
import org.eclipse.jetty.servlets.EventSourceServlet;

public class SSHEventSourceServlet extends EventSourceServlet {

	@Override
	protected EventSource newEventSource(HttpServletRequest request) {
		return null;
	}

}
