package com.inova.etf.qr.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.inova.etf.qr.exception.QrGenerateFailedException;
import com.inova.etf.qr.request.QrGenerateRequest;
import com.inova.etf.qr.service.GenerateQrCode;
import com.inova.etf.qr.service.QrService;
import com.inova.etf.qr.util.JacksonUtil;

@Service
public class QrServiceImpl implements QrService {
	private Logger logger;
	private GenerateQrCode generateQrCode;
	@Value("${qr.height}")
	private int height;
	@Value("${qr.width}")
	private int width;

	public QrServiceImpl(GenerateQrCode generateQrCode) {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.generateQrCode = generateQrCode;
	}

	@Override
	public byte[] generateQr(QrGenerateRequest request) throws QrGenerateFailedException {
		logger.info("Generate Qr service {} -> {} -> {}", request.getUniqueIdentifier(), height, width);
		return generateQrCode.generateQr(JacksonUtil.getToString(request), height, width);
	}

	@Override
	public byte[] generateQrAndStore(QrGenerateRequest request) throws QrGenerateFailedException {
		logger.info("Generate Qr service {} -> {} -> {}", request.getUniqueIdentifier(), height, width);
		return generateQrCode.generateQrAndStore(JacksonUtil.getToString(request), height, width);
	}

	@Override
	public byte[] generateQrAndStoreWithLogo(QrGenerateRequest request) throws QrGenerateFailedException {
		logger.info("Generate Qr service {} -> {} -> {}", request.getUniqueIdentifier(), height, width);
		return generateQrCode.generateQrAndStoreWithLogo(JacksonUtil.getToString(request), height, width);
	}

	@Override
	public Map<String, Object> generateQrAndStoreWithLogoPath(QrGenerateRequest request)
			throws QrGenerateFailedException {
		logger.info("Generate Qr service {} -> {} -> {}", request.getUniqueIdentifier(), height, width);
		return generateQrCode.generateQrAndStoreWithLogoPath(JacksonUtil.getToString(request), height, width);
	}
}
