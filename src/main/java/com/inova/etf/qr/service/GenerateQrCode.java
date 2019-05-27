package com.inova.etf.qr.service;

import java.util.Map;

import com.inova.etf.qr.exception.QrGenerateFailedException;

public interface GenerateQrCode {
	public byte[] generateQr(String content, int width, int height) throws QrGenerateFailedException;
	public byte[] generateQrAndStore(String content, int width, int height) throws QrGenerateFailedException;
	public byte[] generateQrAndStoreWithLogo(String content, int width, int height) throws QrGenerateFailedException;
	public Map<String, Object> generateQrAndStoreWithLogoPath(String content, int width, int height) throws QrGenerateFailedException;
}
