package io.github.gleidsonmt.dashboardfx.core.process;

import io.github.gleidsonmt.dashboardfx.core.Context;
import it.tdlight.jni.TdApi;

import java.util.List;

/**
 * Date: 2024/3/26
 * Author: SilentSherlock
 * Description: dispatch chat and process chat's message, every chat
 * has its particular process in most situations
 */
public interface ChatsProcessor {
    void process(TdApi.Chat chat, Context context);
    void process(List<TdApi.Chat> chats, Context context);

    /**
     *
     * @param message
     * @return true-unread
     */
    default boolean isMessageUnRead(TdApi.Message message) {
        return !message.isOutgoing && message.containsUnreadMention;
    }
}
