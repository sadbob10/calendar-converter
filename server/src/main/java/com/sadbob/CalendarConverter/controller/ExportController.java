package com.sadbob.CalendarConverter.controller;

import com.sadbob.CalendarConverter.dto.requestDTO.export.ExportRequest;
import com.sadbob.CalendarConverter.service.impl.DataExportServiceImpl;
import com.sadbob.CalendarConverter.service.ICalExportService;
import com.sadbob.CalendarConverter.service.PdfExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/export")
@Tag(name = "Calendar Export", description = "Calendar export and download APIs")
@CrossOrigin(origins = "*")
public class ExportController {

    private final ICalExportService icalExportService;
    private final PdfExportService pdfExportService;
    private final DataExportServiceImpl dataExportService;


    public ExportController(ICalExportService icalExportService, PdfExportService pdfExportService, DataExportServiceImpl dataExportService) {
        this.icalExportService = icalExportService;
        this.pdfExportService = pdfExportService;
        this.dataExportService = dataExportService;
    }

    @PostMapping("/ics/holidays")
    @Operation(summary = "Export holidays as ICS file")
    public ResponseEntity<byte[]> exportHolidaysICS(@RequestBody ExportRequest request) {
        try {
            String icsContent = icalExportService.generateHolidaysICS(
                    request.getCalendarType(),
                    request.getYear() != null ? request.getYear() : java.time.LocalDate.now().getYear()
            );

            String filename = String.format("%s-holidays-%d.ics",
                    request.getCalendarType(),
                    request.getYear() != null ? request.getYear() : java.time.LocalDate.now().getYear());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("text/calendar"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(icsContent.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/ics/holidays/{calendarType}/{year}")
    @Operation(summary = "Export holidays for a specific year as ICS file")
    public ResponseEntity<byte[]> exportHolidaysICS(
            @PathVariable String calendarType,
            @PathVariable int year) {

        ExportRequest request = new ExportRequest();
        request.setCalendarType(calendarType);
        request.setYear(year);

        return exportHolidaysICS(request);
    }

    @GetMapping("/ics/holidays/{calendarType}")
    @Operation(summary = "Export current year holidays as ICS file")
    public ResponseEntity<byte[]> exportCurrentYearHolidaysICS(@PathVariable String calendarType) {
        int currentYear = java.time.LocalDate.now().getYear();

        ExportRequest request = new ExportRequest();
        request.setCalendarType(calendarType);
        request.setYear(currentYear);

        return exportHolidaysICS(request);
    }

    // Simple endpoint to preview ICS content
    @GetMapping("/ics/holidays/{calendarType}/{year}/preview")
    @Operation(summary = "Preview holidays ICS content")
    public ResponseEntity<String> previewHolidaysICS(
            @PathVariable String calendarType,
            @PathVariable int year) {

        String icsContent = icalExportService.generateHolidaysICS(calendarType, year);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(icsContent);
    }


    @GetMapping("/pdf/calendar/{calendarType}/{year}/{month}")
    @Operation(summary = "Export monthly calendar as PDF")
    public ResponseEntity<byte[]> exportCalendarPdf(
            @PathVariable String calendarType,
            @PathVariable int year,
            @PathVariable int month) {

        try {
            byte[] pdfBytes = pdfExportService.generateSimpleCalendarPdf(calendarType, year, month);

            String filename = String.format("%s-calendar-%d-%02d.pdf",
                    calendarType, year, month);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(pdfBytes);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/pdf/calendar/{calendarType}/current")
    @Operation(summary = "Export current month calendar as PDF")
    public ResponseEntity<byte[]> exportCurrentMonthCalendarPdf(@PathVariable String calendarType) {
        java.time.LocalDate today = java.time.LocalDate.now();
        return exportCalendarPdf(calendarType, today.getYear(), today.getMonthValue());
    }

    @PostMapping("/pdf/calendar")
    @Operation(summary = "Export calendar as PDF with custom options")
    public ResponseEntity<byte[]> exportCalendarPdfWithOptions(@RequestBody ExportRequest request) {
        try {
            int year = request.getYear() != null ? request.getYear() : java.time.LocalDate.now().getYear();
            int month = request.getMonth() != null ? request.getMonth() : java.time.LocalDate.now().getMonthValue();

            byte[] pdfBytes = pdfExportService.generateSimpleCalendarPdf(
                    request.getCalendarType(), year, month);

            String filename = String.format("%s-calendar-%d-%02d.pdf",
                    request.getCalendarType(), year, month);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(pdfBytes);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // JSON Export Endpoints
    @GetMapping("/json/holidays/{calendarType}")
    @Operation(summary = "Export all holidays as JSON")
    public ResponseEntity<String> exportHolidaysJson(@PathVariable String calendarType) {
        try {
            String jsonContent = dataExportService.exportHolidaysJson(calendarType, null);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonContent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error generating JSON: " + e.getMessage());
        }
    }

    @GetMapping("/json/holidays/{calendarType}/{year}")
    @Operation(summary = "Export holidays for specific year as JSON")
    public ResponseEntity<String> exportHolidaysJson(
            @PathVariable String calendarType,
            @PathVariable int year) {
        try {
            String jsonContent = dataExportService.exportHolidaysJson(calendarType, year);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonContent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error generating JSON: " + e.getMessage());
        }
    }

    @GetMapping("/json/calendar/{calendarType}/{year}/{month}")
    @Operation(summary = "Export monthly calendar data as JSON")
    public ResponseEntity<String> exportCalendarJson(
            @PathVariable String calendarType,
            @PathVariable int year,
            @PathVariable int month) {
        try {
            String jsonContent = dataExportService.exportCalendarDataJson(calendarType, year, month);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonContent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error generating JSON: " + e.getMessage());
        }
    }

    // CSV Export Endpoints
    @GetMapping("/csv/holidays/{calendarType}")
    @Operation(summary = "Export all holidays as CSV")
    public ResponseEntity<byte[]> exportHolidaysCsv(@PathVariable String calendarType) {
        try {
            byte[] csvContent = dataExportService.exportHolidaysCsv(calendarType, null);

            String filename = String.format("%s-holidays.csv", calendarType);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(csvContent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/csv/holidays/{calendarType}/{year}")
    @Operation(summary = "Export holidays for specific year as CSV")
    public ResponseEntity<byte[]> exportHolidaysCsv(
            @PathVariable String calendarType,
            @PathVariable int year) {
        try {
            byte[] csvContent = dataExportService.exportHolidaysCsv(calendarType, year);

            String filename = String.format("%s-holidays-%d.csv", calendarType, year);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(csvContent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/csv/calendar/{calendarType}/{year}/{month}")
    @Operation(summary = "Export monthly calendar data as CSV")
    public ResponseEntity<byte[]> exportCalendarCsv(
            @PathVariable String calendarType,
            @PathVariable int year,
            @PathVariable int month) {
        try {
            byte[] csvContent = dataExportService.exportCalendarDataCsv(calendarType, year, month);

            String filename = String.format("%s-calendar-%d-%02d.csv", calendarType, year, month);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(csvContent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}


