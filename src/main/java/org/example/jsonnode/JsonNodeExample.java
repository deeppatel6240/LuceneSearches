package org.example.jsonnode;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonNodeExample {

    public static void main(String[] args) {
        String jsonResponse = "{\"responseTime\":\"2023-07-28T15:50:57.280069\",\"status\":\"OK\",\"statusCode\":200,\"message\":\"Global Settings map with field and value fetched successfully!\",\"method\":\"SettingsController.getListOfSettingFieldValues\",\"executionMessage\":\"Implemented business logic of service class method\",\"data\":{\"settings\":{\"kf2\":\"v2\",\"kf1\":\"v1\"}}}";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonResponse);

            // Get the "data" node
            JsonNode dataNode = root.get("data.settings");

            if (dataNode != null && dataNode.isObject()) {
                // Get the "settings" node
                JsonNode settingsNode = dataNode.get("settings");

                if (settingsNode != null && settingsNode.isObject()) {
                    // Process the "settings" node here
                    // For example, iterate through the fields and values
                    for (JsonNode fieldNode : settingsNode) {
                        String fieldName = fieldNode.fieldNames().next();
                        String fieldValue = fieldNode.get(fieldName).asText();
                        System.out.println("Field: " + fieldName + ", Value: " + fieldValue);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
