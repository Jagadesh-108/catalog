import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SecretSharing {

    // Function to decode the value from a specific base to decimal
    private static long decodeValue(String base, String value) {
        long result = 0;
        long baseValue = Long.parseLong(base);
        for (char ch : value.toCharArray()) {
            result = result * baseValue + (ch - '0');
        }
        return result;
    }

    // Function to calculate the constant term (c) using Lagrange interpolation
    private static long calculateSecret(List<Long> xValues, List<Long> yValues) {
        long secret = 0;
        int k = xValues.size();

        for (int i = 0; i < k; i++) {
            long term = yValues.get(i);
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    term *= (0 - xValues.get(j));
                    term /= (xValues.get(i) - xValues.get(j));
                }
            }
            secret += term;
        }

        return secret;
    }

    public static void main(String[] args) {
        try {
            // Load JSON input
            String content = new String(Files.readAllBytes(Paths.get("input.json")));
            JSONObject inputData = new JSONObject(content);

            int n = inputData.getJSONObject("keys").getInt("n");
            int k = inputData.getJSONObject("keys").getInt("k");

            List<Long> xValues = new ArrayList<>();
            List<Long> yValues = new ArrayList<>();

            // Decode the x and y values
            for (int i = 1; i <= n; i++) {
                JSONObject root = inputData.getJSONObject(String.valueOf(i));
                String base = root.getString("base");
                String value = root.getString("value");

                long x = i; // x is the key (1, 2, ... n)
                long y = decodeValue(base, value); // Decode y value

                xValues.add(x);
                yValues.add(y);
            }

            // Calculate the secret term (c)
            long secret = calculateSecret(xValues, yValues);

            // Output the secret
            System.out.println("The secret (constant term c) is: " + secret);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}