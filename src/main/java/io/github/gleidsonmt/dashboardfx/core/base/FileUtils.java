package io.github.gleidsonmt.dashboardfx.core.base;

import io.github.gleidsonmt.dashboardfx.core.process.ChatsProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassLoaderUtils;
import org.apache.commons.lang3.ClassPathUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Date: 2024/3/25
 * Author: SilentSherlock
 * Description: describe the file
 */
@Slf4j
public class FileUtils {

    /**
     * read observe channel id and processor from local config file
     * @return
     */
    public static Map<Long, ChatsProcessor> getObserveChannels(String filePath) {
        Map<Long, ChatsProcessor> map = new HashMap<>();
        if (null == filePath || filePath.isBlank()) {
            log.error("invalid filePath {} ", filePath);
            return map;
        }

        List<String> rows = readFileByRow(filePath);
        if (rows.isEmpty()) {
            log.info("empty chat config");
            return map;
        }

        rows.forEach(row -> {
            String[] configs = row.split(",");
            long chatId = Long.parseLong(configs[0]);
            try {
                Class<?> clazz = Class.forName(configs[1].trim());
                ChatsProcessor chatsProcessor = (ChatsProcessor) clazz.getDeclaredConstructor().newInstance();
                map.put(chatId, chatsProcessor);
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                log.info(e.getMessage());
                e.printStackTrace();
            }
        });

        return map;
    }

    public static List<String> readFileByRow(String filePath) {
        // 使用try-with-resources语句自动关闭资源
        List<String> rows = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            // 逐行读取文件内容，直到读取完毕
            while ((line = reader.readLine()) != null) {
                // 处理读取到的每一行数据
                log.info("current line {}", line.trim());
                if (!line.trim().isBlank()) rows.add(line.trim());
            }
        } catch (IOException e) {
            // 异常处理
            e.printStackTrace();
        }
        return rows;
    }
}
