package com.sadbob.CalendarConverter.service.calenderExport;

import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.sadbob.CalendarConverter.entity.Holiday;
import com.sadbob.CalendarConverter.service.HolidayService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

import static com.sadbob.CalendarConverter.service.calenderExport.DataExportService.getString;

@Service
public class PdfExportService {

    private final HolidayService holidayService;

    // Colors for styling
    private static final Color HEADER_COLOR = new DeviceRgb(70, 130, 180);
    private static final Color HOLIDAY_COLOR = new DeviceRgb(220, 20, 60);
    private static final Color WEEKEND_COLOR = new DeviceRgb(240, 240, 240);
    private static final Color TODAY_COLOR = new DeviceRgb(255, 255, 200); // Light yellow for today

    public PdfExportService(HolidayService holidayService) {
        this.holidayService = holidayService;
    }

    public byte[] generateSimpleCalendarPdf(String calendarType, int year, int month) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Add title
            addTitle(document, calendarType, year, month);

            // Get holidays
            List<Holiday> holidays = holidayService.getHolidaysForMonth(calendarType, year, month);

            // Create calendar table based on calendar type
            Table calendarTable = createCalendarTable(calendarType, year, month, holidays);
            document.add(calendarTable);

            // Add holidays legend
            addHolidaysLegend(document, holidays);

            document.close();
            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF: " + e.getMessage(), e);
        }
    }

    private void addTitle(Document document, String calendarType, int year, int month) {
        String monthName = getMonthName(month, calendarType);
        String title = String.format("%s Calendar - %s %d",
                calendarType.toUpperCase(), monthName, year);

        Paragraph titleParagraph = new Paragraph(title)
                .setFontSize(20)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);

        document.add(titleParagraph);
    }

    private Table createCalendarTable(String calendarType, int year, int month, List<Holiday> holidays) {
        float[] columnWidths = new float[7];
        for (int i = 0; i < 7; i++) {
            columnWidths[i] = 1;
        }

        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        // Add weekday headers
        addWeekdayHeaders(table);

        // Handle different calendar types
        if ("ethiopian".equalsIgnoreCase(calendarType)) {
            createEthiopianCalendar(table, year, month, holidays);
        } else if ("hijri".equalsIgnoreCase(calendarType)) {
            createHijriCalendar(table, year, month, holidays);
        } else {
            createGregorianCalendar(table, year, month, holidays);
        }

        return table;
    }

    private void createGregorianCalendar(Table table, int year, int month, List<Holiday> holidays) {
        LocalDate firstDay = LocalDate.of(year, month, 1);
        int daysInMonth = firstDay.lengthOfMonth();
        int startDayOfWeek = firstDay.getDayOfWeek().getValue() % 7; // 0=Sunday

        // Fill empty cells before first day
        for (int i = 0; i < startDayOfWeek; i++) {
            table.addCell(createEmptyCell());
        }

        // Add days of the month
        LocalDate today = LocalDate.now();
        for (int day = 1; day <= daysInMonth; day++) {
            boolean isToday = (year == today.getYear() && month == today.getMonthValue() && day == today.getDayOfMonth());
            table.addCell(createDayCell(day, holidays, startDayOfWeek + day - 1, isToday));
        }

        // Fill remaining empty cells
        int totalCells = 42; // 6 weeks
        int filledCells = startDayOfWeek + daysInMonth;
        for (int i = filledCells; i < totalCells; i++) {
            table.addCell(createEmptyCell());
        }
    }

    private void createEthiopianCalendar(Table table, int year, int month, List<Holiday> holidays) {
        // Ethiopian months have 30 days, except Pagume (month 13) which has 5-6 days
        int daysInMonth = (month == 13) ? 6 : 30; // Simplified - always show 6 days for Pagume

        // Simplified start day (in reality, this would need proper Ethiopian date calculation)
        int startDayOfWeek = 0; // Default to Sunday start

        // Fill empty cells before first day
        for (int i = 0; i < startDayOfWeek; i++) {
            table.addCell(createEmptyCell());
        }

        // Add days of the month
        for (int day = 1; day <= daysInMonth; day++) {
            table.addCell(createDayCell(day, holidays, startDayOfWeek + day - 1, false));
        }

        // Fill remaining empty cells
        int totalCells = 42;
        int filledCells = startDayOfWeek + daysInMonth;
        for (int i = filledCells; i < totalCells; i++) {
            table.addCell(createEmptyCell());
        }
    }

    private void createHijriCalendar(Table table, int year, int month, List<Holiday> holidays) {
        // Hijri months have 29-30 days - using 30 for simplicity
        int daysInMonth = 30;
        int startDayOfWeek = 0; // Default to Sunday start

        // Fill empty cells before first day
        for (int i = 0; i < startDayOfWeek; i++) {
            table.addCell(createEmptyCell());
        }

        // Add days of the month
        for (int day = 1; day <= daysInMonth; day++) {
            table.addCell(createDayCell(day, holidays, startDayOfWeek + day - 1, false));
        }

        // Fill remaining empty cells
        int totalCells = 42;
        int filledCells = startDayOfWeek + daysInMonth;
        for (int i = filledCells; i < totalCells; i++) {
            table.addCell(createEmptyCell());
        }
    }

    private void addWeekdayHeaders(Table table) {
        String[] weekdays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

        for (String day : weekdays) {
            Cell headerCell = new Cell()
                    .add(new Paragraph(day))
                    .setBackgroundColor(HEADER_COLOR)
                    .setFontColor(new DeviceRgb(255, 255, 255))
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(8);
            table.addHeaderCell(headerCell);
        }
    }

    private Cell createEmptyCell() {
        return new Cell()
                .add(new Paragraph(""))
                .setPadding(8)
                .setTextAlignment(TextAlignment.CENTER);
    }

    private Cell createDayCell(int day, List<Holiday> holidays, int cellIndex, boolean isToday) {
        Paragraph dayParagraph = new Paragraph(String.valueOf(day));

        Cell cell = new Cell()
                .add(dayParagraph)
                .setPadding(8)
                .setTextAlignment(TextAlignment.CENTER);

        // Check if it's a weekend
        int dayOfWeek = cellIndex % 7;
        boolean isWeekend = (dayOfWeek == 0) || (dayOfWeek == 6); // Sunday or Saturday

        // Check if it's a holiday
        boolean isHoliday = holidays.stream()
                .anyMatch(holiday -> holiday.getDayOfMonth() == day);

        if (isToday) {
            cell.setBackgroundColor(TODAY_COLOR)
                    .setBold();
        } else if (isHoliday) {
            cell.setBackgroundColor(HOLIDAY_COLOR)
                    .setFontColor(new DeviceRgb(255, 255, 255))
                    .setBold();
        } else if (isWeekend) {
            cell.setBackgroundColor(WEEKEND_COLOR);
        }

        return cell;
    }

    private void addHolidaysLegend(Document document, List<Holiday> holidays) {
        if (holidays.isEmpty()) return;

        document.add(new Paragraph("\n"));

        Paragraph legendTitle = new Paragraph("Holidays this month:")
                .setBold()
                .setMarginBottom(10);
        document.add(legendTitle);

        for (Holiday holiday : holidays) {
            String holidayText = String.format("• %d/%d: %s (%s)",
                    holiday.getMonthNumber(), holiday.getDayOfMonth(),
                    holiday.getName(),
                    holiday.getDescription());

            Paragraph holidayParagraph = new Paragraph(holidayText)
                    .setMarginBottom(3);
            document.add(holidayParagraph);
        }
    }

    private String getMonthName(int month, String calendarType) {
        return getString(month, calendarType);
    }
}