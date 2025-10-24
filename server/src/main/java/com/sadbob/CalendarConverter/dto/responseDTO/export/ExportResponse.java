package com.sadbob.CalendarConverter.dto.responseDTO.export;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExportResponse {
    private String message;
    private String downloadUrl;
    private String fileType;
    private long fileSize;
}
