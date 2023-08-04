package org.example.threading;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DynamicParallelismExample {
    public static void main(String[] args) {
        String startDateString = "2023/12/13 01:00:00";
        String endDateString = "2023/12/16 01:00:00";
        String variable = "index"; // Change this to "date" if you want to process based on date

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        try {
            Date startDate = sdf.parse(startDateString);
            Date endDate = sdf.parse(endDateString);

            int numberOfThreads = 7; // Change this value to adjust the number of parallel threads

            ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

            if (variable.equalsIgnoreCase("index")) {
                for (int i = 0; i < numberOfThreads; i++) {
                    String indexPath = String.format("/root/collection/storage/logs/%tY/%tm/%td/%02d", startDate, startDate, startDate, i);
                    executorService.submit(() -> process(indexPath));
                }
            } else if (variable.equalsIgnoreCase("date")) {
                while (startDate.before(endDate)) {
                    String dateString = String.format("/root/collection/storage/logs/%tY/%tm/%td", startDate, startDate, startDate);
                    executorService.submit(() -> process(dateString));
                    startDate.setTime(startDate.getTime() + (60 * 60 * 1000)); // Adding 3 hours to the start date
                }
            } else {
                System.err.println("Invalid variable value. Please use 'index' or 'date'.");
            }

            executorService.shutdown();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // Replace this method with your actual processing logic
    private static void process(String path) {
        System.out.println("Processing: " + path);
        // Your processing logic goes here
    }
}
