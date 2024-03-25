package io.github.gleidsonmt.dashboardfx.core.process;

import io.github.gleidsonmt.dashboardfx.core.base.FileUtils;
import it.tdlight.jni.TdApi;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Date: 2024/3/25
 * Author: SilentSherlock
 * Description: dispatch chat and process chat's message, every chat
 * has its particular process in most situations
 */
@Slf4j
public class ChannelChatsProcessor {

    public void process(TdApi.Chat chat) {

        List<Long> channelIds = FileUtils.getObserveChannels();
        log.info("channelIds {}", channelIds);

    }

    public void process(List<TdApi.Chat> chats) {
        chats.forEach(this::process);
    }

}
