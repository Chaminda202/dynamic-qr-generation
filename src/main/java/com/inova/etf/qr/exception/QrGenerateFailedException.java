package com.inova.etf.qr.exception;

import com.inova.etf.qr.util.Util;

public class QrGenerateFailedException extends Exception{
	private static final long serialVersionUID = 5447513430339596645L;
	@Override
	public String getMessage() {
		return Util.QR_GENERATION_FAILED_MESSAGE;
	}
}
