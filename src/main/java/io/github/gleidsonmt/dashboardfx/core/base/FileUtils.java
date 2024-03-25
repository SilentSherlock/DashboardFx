package io.github.gleidsonmt.dashboardfx.core.base;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ClassLoaderUtils;
import org.apache.commons.lang3.ClassPathUtils;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Date: 2024/3/25
 * Author: SilentSherlock
 * Description: describe the file
 */
@Slf4j
public class FileUtils {

    /**
     * read observe channel id from local resources file
     * @return
     */
    public static List<Long> getObserveChannels() {

        // 使用try-with-resources语句自动关闭资源
        List<Long> channelChatIds = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(AppConst.File.observe_channels))) {
            String line;
            // 逐行读取文件内容，直到读取完毕
            while ((line = reader.readLine()) != null) {
                // 处理读取到的每一行数据
                log.info("current line {}", line);
                channelChatIds.add(Long.parseLong(line.trim()));
            }
        } catch (IOException e) {
            // 异常处理
            e.printStackTrace();
        }
        return channelChatIds;
    }
}
