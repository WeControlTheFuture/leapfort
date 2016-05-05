package org.wctf.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.wctf.model.Host;
import org.wctf.model.Wrapper;

public class JaxbUtil {
	/**
	 * JavaBean转换成xml 默认编码UTF-8
	 * 
	 * @param obj
	 * @param writer
	 * @return
	 */
	public static String convertToXml(Object obj) {
		return convertToXml(obj, "UTF-8");
	}

	/**
	 * JavaBean转换成xml
	 * 
	 * @param obj
	 * @param encoding
	 * @return
	 */
	public static String convertToXml(Object obj, String encoding) {
		String result = null;
		try {
			JAXBContext context = JAXBContext.newInstance(obj.getClass());
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);

			StringWriter writer = new StringWriter();
			marshaller.marshal(obj, writer);
			result = writer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * xml转换成JavaBean
	 * 
	 * @param xml
	 * @param c
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T converyToJavaBean(String xml, Class<T> c) {
		T t = null;
		try {
			JAXBContext context = JAXBContext.newInstance(c);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			t = (T) unmarshaller.unmarshal(new StringReader(xml));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return t;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> unmarshal(Class<T> clazz, String xmlLocation) throws JAXBException {
		StreamSource xml = new StreamSource(xmlLocation);
		JAXBContext context = JAXBContext.newInstance(clazz, Wrapper.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Wrapper<T> wrapper = (Wrapper<T>) unmarshaller.unmarshal(xml, Wrapper.class).getValue();
		return wrapper.getItems();
	}

	public static void main(String[] args) {
		List<Host> hosts;
		try {
			hosts = JaxbUtil.unmarshal(Host.class, "src/main/resources/host-info.xml");
			System.out.println(hosts);
			for (Host h : hosts)
				System.out.println(h.getName());
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
