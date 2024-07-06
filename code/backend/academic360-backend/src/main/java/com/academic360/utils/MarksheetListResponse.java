package com.academic360.utils;

import java.util.List;

import com.academic360.dtos.MarksheetDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MarksheetListResponse {

    private int pageNumber;

    private int pageSize;

    private int totalPages;

    private long totalMarksheets;

    private List<MarksheetDto> marksheets;

}
