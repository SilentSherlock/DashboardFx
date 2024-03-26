package io.github.gleidsonmt.dashboardfx.core.process.impl.channel;

import io.github.gleidsonmt.dashboardfx.core.Context;
import io.github.gleidsonmt.dashboardfx.core.process.ChatsProcessor;
import it.tdlight.jni.TdApi;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Date: 2024/3/25
 * Author: SilentSherlock
 * Description: process msg in next channels
 * guangmindin
 */
@Slf4j
public class ChannelChatsProcessor1 implements ChatsProcessor {

    public void process(TdApi.Chat chat, Context context) {



    }

    public void process(List<TdApi.Chat> chats, Context context) {
        chats.forEach(v -> process(v, context));
    }

}
