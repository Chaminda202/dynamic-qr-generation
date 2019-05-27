package com.inova.etf.qr.service.impl;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.inova.etf.qr.enumeration.Colors;
import com.inova.etf.qr.exception.QrGenerateFailedException;
import com.inova.etf.qr.exception.QrLogoReadFailedException;
import com.inova.etf.qr.exception.QrStoreLocationCreateFailedException;
import com.inova.etf.qr.service.GenerateQrCode;
import com.inova.etf.qr.util.Util;

@Service
public class GenerateQrCodeImpl implements GenerateQrCode {
	private Logger logger;
	@Value("${qr.store.location}")
	private String storeLocation;
	@Value("${qr.extension}")
	private String extension;

	public GenerateQrCodeImpl() {
		this.logger = LoggerFactory.getLogger(this.getClass());
	}

	@Override
	public byte[] generateQr(String content, int width, int height) throws QrGenerateFailedException {
		logger.info("QR code generate service {} -> {} -> {}", content, width, height);
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix;
		try {
			bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			MatrixToImageWriter.writeToStream(bitMatrix, "png", byteArrayOutputStream);
			logger.info("QR code generate service {}", Util.STATUS_SUCCESS);
			return byteArrayOutputStream.toByteArray();
		} catch (Exception e) {
			logger.error("QR code generate service {} -> {}", Util.STATUS_FAILED, e.getMessage());
			throw new QrGenerateFailedException();
		}
	}

	@Override
	public byte[] generateQrAndStore(String content, int width, int height) throws QrGenerateFailedException {
		logger.info("QR code generate and store service {} -> {} -> {}", content, width, height);
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix;
		try {
			createDirectory(storeLocation);
			bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			MatrixToImageWriter.writeToStream(bitMatrix, "png", byteArrayOutputStream);
			logger.info("QR code generate and store service {}", Util.STATUS_SUCCESS);
			// Store Image
			Files.copy(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()),
					Paths.get(storeLocation + new Date().getTime() + extension), StandardCopyOption.REPLACE_EXISTING);
			return byteArrayOutputStream.toByteArray();
		} catch (Exception e) {
			logger.error("QR code generate and store service {} -> {}", Util.STATUS_FAILED, e.getMessage());
			throw new QrGenerateFailedException();
		}
	}

	@Override
	public byte[] generateQrAndStoreWithLogo(String content, int width, int height) throws QrGenerateFailedException {
		logger.info("QR code generate and store with logo service {} -> {} -> {}", content, width, height);
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		Map<EncodeHintType, Object> hints = new HashMap<>();
		BitMatrix bitMatrix;
		try {
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			hints.put(EncodeHintType.MARGIN, 1); /* default = 4 */
			createDirectory(storeLocation);
			// Create a qr code with content and a size
			bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

			// Load QR image
			BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, getMatrixConfig());

			// Load logo image
			BufferedImage overly = readImage("src/main/resources/logo/download.png");

			// Calculate the delta height and width between QR code and logo
			int deltaHeight = qrImage.getHeight() - overly.getHeight();
			int deltaWidth = qrImage.getWidth() - overly.getWidth();

			// Initialize combined image
			BufferedImage combined = new BufferedImage(qrImage.getHeight(), qrImage.getWidth(),
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D) combined.getGraphics();

			// Write QR code to new image at position 0/0
			g.drawImage(qrImage, 0, 0, null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

			// Write logo into combine image at position (deltaWidth / 2) and
			// (deltaHeight / 2). Background: Left/Right and Top/Bottom must be
			// the same space for the logo to be centered
			g.drawImage(overly, (int) Math.round(deltaWidth / 2), (int) Math.round(deltaHeight / 2), null);

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			// Write combined image as PNG to OutputStream
			ImageIO.write(combined, "png", byteArrayOutputStream);

			logger.info("QR code generate store with logo service {}", Util.STATUS_SUCCESS);
			String qrImg = new Date().getTime() + extension;
			// Store Image
			Files.copy(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()),
					Paths.get(storeLocation + qrImg), StandardCopyOption.REPLACE_EXISTING);
			return byteArrayOutputStream.toByteArray();
		} catch (Exception e) {
			logger.error("QR code generate store with logo service {} -> {}", Util.STATUS_FAILED, e.getMessage());
			throw new QrGenerateFailedException();
		}
	}
	
	@Override
	public Map<String, Object> generateQrAndStoreWithLogoPath(String content, int width, int height)
			throws QrGenerateFailedException {
		Map<String, Object> response = new HashMap<>();
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		Map<EncodeHintType, Object> hints = new HashMap<>();
		BitMatrix bitMatrix;
		try {
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			hints.put(EncodeHintType.MARGIN, 1); /* default = 4 */
			createDirectory(storeLocation);
			// Create a qr code with content and a size
			bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

			// Load QR image
			BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, getMatrixConfig());

			// Load logo image
			BufferedImage overly = readImage("src/main/resources/logo/download.png");

			// Calculate the delta height and width between QR code and logo
			int deltaHeight = qrImage.getHeight() - overly.getHeight();
			int deltaWidth = qrImage.getWidth() - overly.getWidth();

			// Initialize combined image
			BufferedImage combined = new BufferedImage(qrImage.getHeight(), qrImage.getWidth(),
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D) combined.getGraphics();

			// Write QR code to new image at position 0/0
			g.drawImage(qrImage, 0, 0, null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

			// Write logo into combine image at position (deltaWidth / 2) and
			// (deltaHeight / 2). Background: Left/Right and Top/Bottom must be
			// the same space for the logo to be centered
			g.drawImage(overly, (int) Math.round(deltaWidth / 2), (int) Math.round(deltaHeight / 2), null);

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			// Write combined image as PNG to OutputStream
			ImageIO.write(combined, "png", byteArrayOutputStream);

			logger.info("QR code generate store with logo service {}", Util.STATUS_SUCCESS);
			String qrImg = new Date().getTime() + extension;
			// Store Image
			Files.copy(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()),
					Paths.get(storeLocation + qrImg), StandardCopyOption.REPLACE_EXISTING);
			response.put("qr_code", byteArrayOutputStream.toByteArray());
			response.put("qr_url", "/qrcode/"+qrImg);
			return response;
		} catch (Exception e) {
			logger.error("QR code generate store with logo service {} -> {}", Util.STATUS_FAILED, e.getMessage());
			throw new QrGenerateFailedException();
		}
	}

	private void createDirectory(String filePath) throws QrStoreLocationCreateFailedException {
		File directory = new File(String.valueOf(filePath));
		if (!directory.exists()) {
			try {
				Files.createDirectories(Paths.get(filePath));
			} catch (IOException e) {
				logger.error("QR code generate store with logo service {} -> {} -> {}", "STORE_LOCATION",
						Util.STATUS_FAILED, e.getMessage());
				throw new QrStoreLocationCreateFailedException();
			}
		}
	}

	private BufferedImage readImage(String path) throws QrLogoReadFailedException {
		try {
			return ImageIO.read(new File(path));
		} catch (IOException e) {
			logger.error("QR code generate store with logo service {} -> {} -> {}", "LOGO_READ", Util.STATUS_FAILED,
					e.getMessage());
			throw new QrLogoReadFailedException();
		}
	}

	private MatrixToImageConfig getMatrixConfig() {
		return new MatrixToImageConfig(Colors.WHITE.getArgb(), Colors.ORANGE.getArgb());
	}
}
