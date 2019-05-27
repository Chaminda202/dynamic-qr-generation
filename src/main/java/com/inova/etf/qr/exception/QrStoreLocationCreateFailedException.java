package com.inova.etf.qr.exception;

import com.inova.etf.qr.util.Util;

public class QrStoreLocationCreateFailedException extends QrGenerateFailedException{
	private static final long serialVersionUID = 5447513430339596645L;
	@Override
	public String getMessage() {
		return Util.QR_STORE_LOCATION_CREATE_FAILED_MESSAGE;
	}
}
