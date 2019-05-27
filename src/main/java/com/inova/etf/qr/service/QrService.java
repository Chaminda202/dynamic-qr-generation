package com.inova.etf.qr.service;

import java.util.Map;

import com.inova.etf.qr.exception.QrGenerateFailedException;
import com.inova.etf.qr.request.QrGenerateRequest;

public interface QrService {
	public byte[] generateQr(QrGenerateRequest request) throws QrGenerateFailedException;
	public byte[] generateQrAndStore(QrGenerateRequest request) throws QrGenerateFailedException;
	public byte[] generateQrAndStoreWithLogo(QrGenerateRequest request) throws QrGenerateFailedException;
	public Map<String, Object> generateQrAndStoreWithLogoPath(QrGenerateRequest request) throws QrGenerateFailedException;
}
