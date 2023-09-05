package com.lexsoft.releasetracker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Schema(description = "Release DTO search.")
public class ReleaseDtoSearch {

    @Schema(description = "Some part of release name is sufficient for filtering.", example = "whatever you want.")
    String name;

    @Schema(description = "Some part of release description is sufficient for filtering.", example = "whatever you want.")
    String description;

    @Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "releaseDateFrom must be in format dd/MM/yyyy.")
    @Schema(description = "releaseDateFrom in dd/MM/yyyy format.", example = "12/12/2024")
    String releaseDateFrom;

    @Pattern(regexp = "\\d{2}/\\d{2}/\\d{4}", message = "releaseDateTo must be in format dd/MM/yyyy.")
    @Schema(description = "releaseDateTo in dd/MM/yyyy format.", example = "12/12/2024")
    String releaseDateTo;

    @Schema(description = "Release Status.", example = "Created")
    String status;

    @NotNull
    @Min(0)
    @Schema(description = "Number of resulting page. Minimum is 0.", example = "2")
    Integer page;

    @NotNull
    @Min(1)
    @Max(50)
    @Schema(description = "Number of records on resulting page. Minimum is 1, maximum 50.", example = "20")
    Integer size;
}
