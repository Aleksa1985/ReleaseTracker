package com.lexsoft.releasetracker.controller;

import static com.lexsoft.releasetracker.dto.constants.ConstantHolder.UUID_VALIDATION_REGEX;

import com.lexsoft.releasetracker.dto.ReleaseDto;
import com.lexsoft.releasetracker.dto.ReleaseDtoSearch;
import com.lexsoft.releasetracker.dto.ReleaseWrapper;
import com.lexsoft.releasetracker.exception.model.ExceptionResponse;
import com.lexsoft.releasetracker.fascade.ReleaseFascade;
import com.lexsoft.releasetracker.validator.ReleaseValidator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/releases")
@RequiredArgsConstructor
public class ReleaseController {

    private final ReleaseValidator releaseValidator;
    private final ReleaseFascade releaseFascade;

    @Operation(summary = "Find release By ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Everything went well, release is updated."),
            @ApiResponse(responseCode = "400", description = "Wrong request format.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Server error.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public ResponseEntity<ReleaseDto> findReleaseById(
            @Parameter(in = ParameterIn.PATH, description = "ID in path in UUID format.", example = "123e4567-e89b-12d3-a456-426655440000")
            @Valid @Pattern(regexp = UUID_VALIDATION_REGEX, message = "ID must be in UUID format.")
            @PathVariable("id") String releaseId) {
        return ResponseEntity.status(HttpStatus.OK).body(releaseFascade.getRelease(UUID.fromString(releaseId)));
    }

    @Operation(summary = "Find Releases filtering by parameters.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Everything went well, You will recieve some response."),
            @ApiResponse(responseCode = "400", description = "Wrong request format.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Server error.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<ReleaseWrapper> findReleases(@Valid ReleaseDtoSearch searchDto) {
        releaseValidator.validateReleaseDtoSearch(searchDto);
        return ResponseEntity.ok(releaseFascade.getReleases(searchDto.getName(),
                searchDto.getDescription(),
                searchDto.getReleaseDateFrom(),
                searchDto.getReleaseDateTo(),
                searchDto.getStatus(),
                searchDto.getPage(),
                searchDto.getSize()));
    }

    @Operation(summary = "Save release.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Everything went well, release is created."),
            @ApiResponse(responseCode = "400", description = "Wrong request format.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Server error.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ReleaseDto> saveRelease(@Valid @RequestBody ReleaseDto releaseDto) {
        releaseValidator.validateReleaseDto(releaseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(releaseFascade.saveRelease(releaseDto));
    }

    @Operation(summary = "Update release.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Everything went well, release is updated."),
            @ApiResponse(responseCode = "400", description = "Wrong request format.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Server error.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @RequestMapping(method = RequestMethod.PUT, path = "/{id}")
    public ResponseEntity<ReleaseDto> updateRelease(
            @Parameter(in = ParameterIn.PATH, description = "ID in path in UUID format.", example = "123e4567-e89b-12d3-a456-426655440000")
            @Valid @Pattern(regexp = UUID_VALIDATION_REGEX, message = "ID must be in UUID format.")
            @PathVariable("id") String releaseId,
            @Valid @RequestBody ReleaseDto releaseDto) {
        releaseValidator.validateReleaseDto(releaseDto);
        return ResponseEntity.status(HttpStatus.OK).body(releaseFascade.updateRelease(UUID.fromString(releaseId), releaseDto));
    }

    @Operation(summary = "Delete release.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Everything went well, release is deleted."),
            @ApiResponse(responseCode = "400", description = "Wrong request format.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class))),
            @ApiResponse(responseCode = "500", description = "Server error.", content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class)))
    })
    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public ResponseEntity<ReleaseDto> deleteRelease(
            @Parameter(in = ParameterIn.PATH, description = "ID in path in UUID format.", example = "123e4567-e89b-12d3-a456-426655440000")
            @Valid @Pattern(regexp = UUID_VALIDATION_REGEX, message = "ID must be in UUID format.")
            @PathVariable("id") String releaseId) {
        releaseFascade.deleteRelease(UUID.fromString(releaseId));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
