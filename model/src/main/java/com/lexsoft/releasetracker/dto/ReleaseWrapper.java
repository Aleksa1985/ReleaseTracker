package com.lexsoft.releasetracker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Schema(description = "Response Releases object.")
public class ReleaseWrapper {

    List<ReleaseDto> releases;
    long totalRecords;
    Integer totalPages;

    @Builder
    public ReleaseWrapper(List<ReleaseDto> releases, long totalRecords, Integer totalPages) {
        this.releases = releases;
        this.totalRecords = totalRecords;
        this.totalPages = totalPages;
    }
}
