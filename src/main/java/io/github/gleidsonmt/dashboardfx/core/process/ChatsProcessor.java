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
    /**
     * process single message
     * @param chat message history in this chat will be fetched and processed
     * @param context application context
     * @param firstFlag true--message history never processed, will fetch message history up to message_max_size, even if the message viewed or not
     */
    void process(TdApi.Chat chat, Context context, boolean firstFlag);
    void process(List<TdApi.Chat> chats, Context context, boolean firstFlag);

    /**
     *
     * @param message
     * @return true-unread
     */
    default boolean isMessageUnRead(TdApi.Message message) {
        return !message.isOutgoing && message.containsUnreadMention;
    }
}
