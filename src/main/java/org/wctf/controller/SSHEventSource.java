package org.wctf.controller;

import java.io.IOException;

import org.eclipse.jetty.servlets.EventSource;

public class SSHEventSource implements EventSource {

	@Override
	public void onOpen(Emitter emitter) throws IOException {
		emitter.comment("Start sending movement information.");
		emitter.data("aaa");
//		emitter.close();
	}

	@Override
	public void onClose() {
	}

}
