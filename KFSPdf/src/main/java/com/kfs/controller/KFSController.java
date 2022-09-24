package com.kfs.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kfs.dto.KfSValue;
import com.kfs.service.FileStorageService;
import com.kfs.service.kfsService;

@RestController
public class KFSController {
	
	Logger log=LoggerFactory.getLogger(KFSController.class);

	@Autowired
	private kfsService kfsService;
	@Autowired
    private FileStorageService fileStorageService;
	
	
	@GetMapping(value="/kfs")
	public Object getKFS(@RequestBody KfSValue kfSValue , HttpServletRequest request)
	{
      try {   
    	  log.info("-------------------------method start-----------------------");
    	  log.info("kfsvalue:{}",kfSValue);
		float interestRate=kfSValue.getInterestRate();
		double balance= kfSValue.getBalance();
		int terms=kfSValue.getTerms();
		this.kfsService.KFSPdf(interestRate,balance,terms);
		
		 Resource resource = fileStorageService.loadFileAsResource("kfs.pdf");
		  String contentType = null;
	        try {
	            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
	        } catch (IOException ex) {
	            log.info("Could not determine file type.");
	        }

		
		//return new ResponseEntity<>(kfSValue,HttpStatus.OK);
		
		   return ResponseEntity.ok()
	                .contentType(MediaType.parseMediaType(contentType))
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
	                .body(resource);
      }
      catch(Exception e)
      {
    	  return new ResponseEntity<>(e.getMessage(),HttpStatus.OK);  
      }
      finally {
    	 
    	  log.info("-------------------------method end-----------------------");
      }
	}
	
	@GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws Exception
	{
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
