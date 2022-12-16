package org.alexcawl.javalab4.controllers;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.alexcawl.javalab4.models.dtos.RequestDTO;
import org.alexcawl.javalab4.models.exceptions.TaskValidationException;
import org.alexcawl.javalab4.services.LoadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1")
public class LoadingController {
    @Autowired
    private LoadingService loadingService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные найдены.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Невалидные параметры запроса.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Стартовая страница не найдена.", content = @Content(mediaType = "application/json"))
    })
    @PostMapping(value = "/load", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> load(@RequestBody RequestDTO request) {
        try {
            loadingService.sendTask(request.getURL(), request.getDepth());
            return new ResponseEntity<Void>(HttpStatus.OK);
        } catch (TaskValidationException exception) {
            System.out.println("Bad request: " + request.toString());
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
    }
}
