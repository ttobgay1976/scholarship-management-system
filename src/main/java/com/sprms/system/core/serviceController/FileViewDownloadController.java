package com.sprms.system.core.serviceController;

import java.net.MalformedURLException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/file")
public class FileViewDownloadController {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(FileViewDownloadController.class);

//	getting the upload directory
	@Value("${file.upload-dir}")
	private String uploadDir;

	@GetMapping("/download/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws Exception {

		logger.info("@@@Calling the downloadFile proc.....................");

		Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();

		Resource resource = new UrlResource(filePath.toUri());
		if (!resource.exists())
			throw new RuntimeException("File not found");

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

	// @GetMapping("/view/{fileName:.+}")
	/*
	 * public ResponseEntity<Resource> viewFile(@PathVariable String fileName)
	 * throws Exception {
	 * 
	 * logger.info("@@@Calling the viewFile proc.....................");
	 * 
	 * Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
	 * 
	 * System.out.println("@@@Check the file path:" + filePath);
	 * 
	 * Resource resource = new UrlResource(filePath.toUri()); if
	 * (!resource.exists()) throw new RuntimeException("File not found");
	 * 
	 * String contentType = Files.probeContentType(filePath); if (contentType ==
	 * null) contentType = "application/octet-stream";
	 * 
	 * return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
	 * .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" +
	 * resource.getFilename() + "\"") .body(resource); }
	 */

	// modified version code
	@GetMapping("/view/{scholarshipRegistrationId}/{fileName:.+}")
	public ResponseEntity<?> viewFile(@PathVariable Long scholarshipRegistrationId,@PathVariable String fileName) {

		try {

			logger.info("@@@Calling the viewFile proc.....................");

			//Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
			Path filePath = Paths.get(uploadDir, "scholarship", scholarshipRegistrationId.toString(), fileName).normalize();

			Resource resource = new UrlResource(filePath.toUri());

			if (!resource.exists() || !resource.isReadable()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found: " + fileName);
			}

			String contentType = Files.probeContentType(filePath);
			if (contentType == null) {
				contentType = "application/octet-stream";
			}

			return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
					.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
					.body(resource);

		} catch (MalformedURLException e) {

			logger.error("Invalid file URL: {}", fileName, e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file path");

		} catch (Exception e) {

			logger.error("Error while viewing file: {}", fileName, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while reading file");
		}
	}
}
