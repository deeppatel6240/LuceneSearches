package org.example.datedynamic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DynamicIndexPathGenerator {
    public static void main(String[] args) {
        String startDateTimeStr = "2023/12/13 01:00:00";
        String endDateTimeStr = "2023/12/13 02:50:00";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime startDateTime = LocalDateTime.parse(startDateTimeStr, formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(endDateTimeStr, formatter);

        while (startDateTime.isBefore(endDateTime)) {
            generateIndexPath(startDateTime);
            startDateTime = startDateTime.plusHours(1);
        }
    }

    private static void generateIndexPath(LocalDateTime dateTime) {
        String basePath = "/root/collection/storage/logs/";
        String indexBasePath = String.format("%04d/%02d/%02d/%02d/", dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), dateTime.getHour());

        for (int index = 0; index < 3; index++) {
            String indexPath = basePath + indexBasePath + "index-" + index;
            System.out.println(indexPath);
        }

        System.out.println("---------------------------------------");
    }
}
