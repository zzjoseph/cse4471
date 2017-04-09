package com.example.denny.qrcode;

public class cacheData {

	private int _id;
	private String _webname;
	private String _malicious;

	public cacheData(String webname, String mal){
		this._webname = webname;
		this._malicious = mal;
	}

	public void set_id(int _id){
		this._id = _id;
	}

	public void set_webname(String _webname){
		this._webname = _webname;
	}

	public void set_malicious(String _malicious){
		this._malicious = _malicious;
	}

	public int get_id() {
		return _id;
	}

	public String get_webname() {
		return _webname;
	}

	public String get_malicious() {
		return _malicious;
	}
}
