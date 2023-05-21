package io.shyftlabs.srms.rest.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.shyftlabs.srms.domain.Result;
import io.shyftlabs.srms.rest.request.ResultRequest;
import io.shyftlabs.srms.rest.resource.ResultResource;
import io.shyftlabs.srms.rest.resource.converter.ResultConverter;
import io.shyftlabs.srms.service.ResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/results")
@Tag(name = "Results", description = "Results for Students' courses")
public class ResultController extends BaseController {

	@Autowired
	private ResultService resultService;

	private ResultConverter resultConverter = new ResultConverter();

	private Logger logger = LoggerFactory.getLogger(ResultController.class);


	@Operation(summary = "Get all results")
	@GetMapping
	public ResponseEntity<List<ResultResource>> getAllResults() {
		List<Result> results = resultService.getAllResults();
		logger.info("Found {} results in DB", results.size());
		List<ResultResource> resources = resultConverter.modelToResources(results);
		return ResponseEntity.status(HttpStatus.OK).body(resources);
	}

	@Operation(summary = "Create a result")
	@PostMapping
	public ResponseEntity<ResultResource> addCourse(@Valid @RequestBody ResultRequest resultReq) {
		// Check user token (authentication)
		checkToken();

		// Create the object in DB and return its resource
		Result result = resultConverter.requestToModel(resultReq);
		result = resultService.createResult(result);
		ResultResource createdResource = resultConverter.modelToResource(result);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdResource);
	}

}
