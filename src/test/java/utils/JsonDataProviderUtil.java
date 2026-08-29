package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Reads test data from src/test/resources/testdata.json so values used by
 * TestNG @DataProvider methods are never hardcoded in the test classes.
 */
public class JsonDataProviderUtil {

    private static JsonNode rootNode;

    private static synchronized JsonNode getRoot() {
        if (rootNode == null) {
            try (InputStream input = JsonDataProviderUtil.class.getClassLoader()
                    .getResourceAsStream("testdata.json")) {
                if (input == null) {
                    throw new RuntimeException("testdata.json not found in classpath (src/test/resources)");
                }
                ObjectMapper mapper = new ObjectMapper();
                rootNode = mapper.readTree(input);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load testdata.json", e);
            }
        }
        return rootNode;
    }

    /**
     * Returns the array of records under the given key as a List of Maps,
     * e.g. getRecords("invalidLogin") -> [{username=..., password=...}, ...]
     */
    public static List<Map<String, String>> getRecords(String key) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = getRoot().get(key);
            if (node == null) {
                throw new RuntimeException("Key '" + key + "' not found in testdata.json");
            }
            List<Map<String, String>> records = new ArrayList<>();
            for (JsonNode entry : node) {
                Map<String, String> map = mapper.convertValue(entry, Map.class);
                records.add(map);
            }
            return records;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse test data for key '" + key + "'", e);
        }
    }

    /** Convenience accessor for a single field from the first record under a key. */
    public static String getField(String key, String field) {
        return getRecords(key).get(0).get(field);
    }
}
