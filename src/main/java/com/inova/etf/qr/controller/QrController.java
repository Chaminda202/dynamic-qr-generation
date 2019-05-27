package com.inova.etf.qr.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inova.etf.qr.request.QrGenerateRequest;
import com.inova.etf.qr.service.QrService;
import com.inova.etf.qr.util.Util;

@RestController
@RequestMapping("/api/qr")
public class QrController {
	private Logger logger;
	private QrService qrService;

	public QrController(QrService qrService) {
		this.logger = LoggerFactory.getLogger(this.getClass());
		this.qrService = qrService;
	}

	@PostMapping(value = "/generate", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> generateQr(@RequestBody QrGenerateRequest request) {
		logger.info("Generate Qr controller {} -> {} -> {}", request.getUniqueIdentifier(), request.getPrice(),
				request.getCount());
		Map<String, Object> response = new HashMap<>();
		try {
			response.put("QR_CODE", qrService.generateQr(request));
			response.put("STATUS", Util.STATUS_SUCCESS);
			response.put("MESSAGE", Util.OR_GENERATION_SUCCESS_MESSAGE);
			logger.info("Validate otp controller {}", Util.STATUS_SUCCESS);
		} catch (Exception e) {
			logger.error("Generate Qr controller {} -> {}", Util.STATUS_FAILED, e.getMessage());
			response.put("STATUS", Util.STATUS_FAILED);
			response.put("MESSAGE", Util.QR_GENERATION_FAILED_MESSAGE);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping(value = "/generatewtlogo", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> generateQrWithLogo(@RequestBody QrGenerateRequest request) {
		logger.info("Generate Qr controller {} -> {} -> {}", request.getUniqueIdentifier(), request.getPrice(),
				request.getCount());
		Map<String, Object> response = new HashMap<>();
		try {
			response.put("QR_CODE", qrService.generateQrAndStoreWithLogo(request));
			response.put("STATUS", Util.STATUS_SUCCESS);
			response.put("MESSAGE", Util.OR_GENERATION_SUCCESS_MESSAGE);
			logger.info("Validate otp controller {}", Util.STATUS_SUCCESS);
		} catch (Exception e) {
			logger.error("Generate Qr controller {} -> {}", Util.STATUS_FAILED, e.getMessage());
			response.put("STATUS", Util.STATUS_FAILED);
			response.put("MESSAGE", Util.QR_GENERATION_FAILED_MESSAGE);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping(value = "/generatewtlogopath", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> generateQrWithLogoPath(@RequestBody QrGenerateRequest request) {
		logger.info("Generate Qr controller {} -> {} -> {}", request.getUniqueIdentifier(), request.getPrice(),
				request.getCount());
		Map<String, Object> response = new HashMap<>();
		try {
			Map<String, Object> res = qrService.generateQrAndStoreWithLogoPath(request);
			response.put("QR_CODE", res.get("qr_code"));
			response.put("QR_URL", res.get("qr_url"));
			response.put("STATUS", Util.STATUS_SUCCESS);
			response.put("MESSAGE", Util.OR_GENERATION_SUCCESS_MESSAGE);
			logger.info("Validate otp controller {}", Util.STATUS_SUCCESS);
		} catch (Exception e) {
			logger.error("Generate Qr controller {} -> {}", Util.STATUS_FAILED, e.getMessage());
			response.put("STATUS", Util.STATUS_FAILED);
			response.put("MESSAGE", Util.QR_GENERATION_FAILED_MESSAGE);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping(value = "/genstore", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> generateQrAndStore(@RequestBody QrGenerateRequest request) {
		logger.info("Generate Qr controller {} -> {} -> {}", request.getUniqueIdentifier(), request.getPrice(),
				request.getCount());
		Map<String, Object> response = new HashMap<>();
		try {
			response.put("QR_CODE", qrService.generateQrAndStore(request));
			response.put("STATUS", Util.STATUS_SUCCESS);
			response.put("MESSAGE", Util.OR_GENERATION_SUCCESS_MESSAGE);
			logger.info("Validate otp controller {}", Util.STATUS_SUCCESS);
		} catch (Exception e) {
			logger.error("Generate Qr controller {} -> {}", Util.STATUS_FAILED, e.getMessage());
			response.put("STATUS", Util.STATUS_FAILED);
			response.put("MESSAGE", Util.QR_GENERATION_FAILED_MESSAGE);
		}
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
