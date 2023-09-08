package com.lexsoft.releasetracker.dto;

import static com.lexsoft.releasetracker.dto.constants.ConstantHolder.UUID_VALIDATION_REGEX;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Schema(description = "Release DTO object.")
public class ReleaseDto {

    @Pattern(regexp = UUID_VALIDATION_REGEX, message = "ID must be in UUID format.")
    @Schema(description = "ID in UUID format.", example = "123e4567-e89b-12d3-a456-426655440000")
    private String id;

    @NotEmpty(message = "Name field is mandatory.")
    @Schema(description = "Name of a release.")
    private String name;

    @NotEmpty(message = "Description field is mandatory.")
    @Schema(description = "Description of a release.")
    private String description;

    @NotEmpty(message = "Status field is mandatory.")
    @Schema(description = "Release Status.", example = "Created")
    private String status;

    @NotEmpty(message = "ReleaseDate field is mandatory.")
    @Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "Release Date must be in format dd/MM/yyyy.")
    @Schema(description = "Released date in dd/MM/yyyy format.", example = "12/12/2024")
    private String releaseDate;

    @Schema(description = "Date of release creation. Only populated by system.")
    private String createdAt;

    @Schema(description = "Date of the last release update. Only populated by system.")
    private String lastUpdateAt;

    @Builder
    public ReleaseDto(String id, String name, String description, String status, String releaseDate, String createdAt, String lastUpdateAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.releaseDate = releaseDate;
        this.createdAt = createdAt;
        this.lastUpdateAt = lastUpdateAt;
    }
}
