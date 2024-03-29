package io.github.gleidsonmt.dashboardfx.core.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class JsonUtils {

    public static void writeObjectToJsonFile(Object object, String filePath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(object);

        try {
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs(); // Create parent directories if they don't exist
            }

            if (!file.exists()) {
                Files.write(Paths.get(filePath), json.getBytes());
            } else {
                try (FileWriter writer = new FileWriter(file, true)) {
                    writer.append(json);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}


