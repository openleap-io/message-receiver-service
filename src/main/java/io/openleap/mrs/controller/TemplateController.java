package io.openleap.mrs.controller;

import io.openleap.mrs.model.template.TemplateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mrs/template")
@Tag(name = "Message Template API", description = "Endpoints for managing message templates.")
public class TemplateController {
    @PostMapping
    @Operation(summary = "Create a new message template", description = "Creates a new message template based on the provided request.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Template created successfully",
                    content = @Content(schema = @Schema(implementation = TemplateRequest.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input provided",
                    content = @Content(examples = @ExampleObject(value = "{ \"error\": \"Invalid input\" }")))
    })
    public ResponseEntity<TemplateRequest> createTemplate(@Valid @RequestBody TemplateRequest templateRequest) {
        // Logic to handle the creation of the template
        return new ResponseEntity<>(templateRequest, HttpStatus.CREATED);
    }

    @PatchMapping("/template")
    @Operation(summary = "Update an existing message template", description = "Updates an existing message template based on the provided request.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Template updated successfully",
                    content = @Content(schema = @Schema(implementation = TemplateRequest.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input provided",
                    content = @Content(examples = @ExampleObject(value = "{ \"error\": \"Invalid input\" }")))
    })
    public ResponseEntity<TemplateRequest> updateTemplate(@Valid @RequestBody TemplateRequest templateRequest) {
        // Logic to handle the update of the template
        return new ResponseEntity<>(templateRequest, HttpStatus.OK);
    }

    @Operation(summary = "Get a message template by name", description = "Retrieves a message template by its name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Template retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TemplateRequest.class))),
            @ApiResponse(responseCode = "404", description = "Template not found",
                    content = @Content(examples = @ExampleObject(value = "{ \"error\": \"Template not found\" }")))
    })
    @GetMapping("/template/{name}")
    public ResponseEntity<TemplateRequest> getTemplateByName(@PathVariable String name) {
        // Logic to retrieve
        return new ResponseEntity<>(new TemplateRequest(), HttpStatus.OK);
    }
}
