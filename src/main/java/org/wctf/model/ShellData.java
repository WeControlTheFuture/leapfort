package org.wctf.model;

public class ShellData {
	private String line;
	private String end = "";

	public ShellData(String line) {
		this.line = line;
	}

	public ShellData(String line, String end) {
		this.line = line;
		this.end = end;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

}
