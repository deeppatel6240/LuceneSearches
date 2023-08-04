package org.example.threading;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class IndexPath {
    public static void main(String[] args) {
        String startDateString = "2023/12/13 01:00:00";
        String endDateString = "2023/12/13 06:00:00"; // Adjust the end time as needed
        String variable = "date"; // Change this to "index" if you want to process based on index

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        try {
            Date startDate = sdf.parse(startDateString);
            Date endDate = sdf.parse(endDateString);

            int numberOfThreads = 7; // Change this value to adjust the number of parallel threads

            ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

            if (variable.equalsIgnoreCase("date")) {
                long timeDifference = endDate.getTime() - startDate.getTime();
                long interval = timeDifference / numberOfThreads;
                for (int i = 0; i < numberOfThreads; i++) {
                    Date currentStartDate = new Date(startDate.getTime() + (i * interval));
                    Date currentEndDate = new Date(startDate.getTime() + ((i + 1) * interval));
                    executorService.submit(() -> process(currentStartDate, currentEndDate));
                }
            } else if (variable.equalsIgnoreCase("index")) {
                long timeDifference = endDate.getTime() - startDate.getTime();
                long interval = timeDifference / numberOfThreads;
                for (int i = 0; i < numberOfThreads; i++) {
                    String indexPath = String.format("/root/collection/storage/logs/%tY/%tm/%td/%02d", startDate, startDate, startDate, i);
                    executorService.submit(() -> process(indexPath));
                }
            } else {
                System.err.println("Invalid variable value. Please use 'index' or 'date'.");
            }

            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); // Wait for all tasks to complete
        } catch (ParseException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Replace this method with your actual processing logic
    private static void process(Date startDate, Date endDate) {
        System.out.println("Processing from: " + startDate + " to: " + endDate);
        // Your processing logic goes here
    }

    // Replace this method with your actual processing logic
    private static void process(String path) {
        System.out.println("Processing: " + path);
        // Your processing logic goes here
    }
}
