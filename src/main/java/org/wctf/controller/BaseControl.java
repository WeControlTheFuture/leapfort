package org.wctf.controller;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wctf.SessionPool;
import org.wctf.SessionPool.SessionThread;

import com.jcraft.jsch.ChannelSftp;

@Controller
public class BaseControl {
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index() {
		return "index";
	}

	@RequestMapping(value = "/upload/{connectionId}", method = RequestMethod.POST)
	@ResponseBody
	public String uploadHandlerForUploadify(HttpServletRequest request, @PathVariable String connectionId) {
		SessionThread sessionThread = SessionPool.getSessionThread(connectionId);
		if (sessionThread == null) {
			return "nosession";
		}
		ChannelSftp channelSftp = sessionThread.getSftpChannel();
		OutputStream sftpOutputStream = null;
		String savePath = request.getServletContext().getRealPath("");
		savePath = savePath + "/uploads/";
		File f1 = new File(savePath);
		System.out.println(savePath);

		if (!f1.exists()) {
			f1.mkdirs();
		}
		DiskFileItemFactory fac = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(fac);
		upload.setHeaderEncoding("utf-8");
		List<FileItem> fileList = null;
		try {
			fileList = upload.parseRequest(request);
		} catch (FileUploadException ex) {
			return "error";
		}
		Iterator<FileItem> it = fileList.iterator();
		String name = "";

		while (it.hasNext()) {
			FileItem item = it.next();
			if (!item.isFormField()) {
				name = item.getName();
				System.out.println("name==========" + name);
				if (name == null || name.trim().equals("")) {
					continue;
				}

				try {
					String pwd = channelSftp.pwd();
					sftpOutputStream = channelSftp.put(pwd + "/" + name);
					InputStream is = item.getInputStream();
					byte b[] = new byte[1024 * 1024];
					int n;
					while ((n = is.read(b)) != -1) {
						sftpOutputStream.write(b, 0, n);
					}
					sftpOutputStream.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return "finish";
	}
}
