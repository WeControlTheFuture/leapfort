package org.wctf.test;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.wctf.model.Host;
import org.wctf.util.JaxbUtil;

public class XmlConvert {

	@Test
	public void unmarshalTest() throws JAXBException {
		List<Host> hosts = JaxbUtil.unmarshal(Host.class, "host-info.xml");
		System.out.println(hosts);
	}

}
