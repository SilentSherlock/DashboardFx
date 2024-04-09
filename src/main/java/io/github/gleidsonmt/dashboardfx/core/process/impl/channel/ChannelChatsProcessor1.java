package io.github.gleidsonmt.dashboardfx.core.process.impl.channel;

import io.github.gleidsonmt.dashboardfx.core.Context;
import io.github.gleidsonmt.dashboardfx.core.base.AppConst;
import io.github.gleidsonmt.dashboardfx.core.base.JsonUtils;
import io.github.gleidsonmt.dashboardfx.core.process.ChatsProcessor;
import it.tdlight.jni.TdApi;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Date: 2024/3/25
 * Author: SilentSherlock
 * Description: process msg in below channels, in most conditions, only receive messages from
 * channel owner unless the login user is owner, ChannelChatsProcessor1 used in messages all send by channel owner
 * guangmindin
 */
@Slf4j
public class ChannelChatsProcessor1 implements ChatsProcessor {

    public void process(TdApi.Chat chat, Context context, boolean firstFlag) {

        log.info("ChannelChatsProcessor1 start");

        Map<Long, TdApi.Message> unProcessMessages = new HashMap<>();
        long fromMsgId = 0;
        do {
            log.info("fetch unread message");
            CompletableFuture<TdApi.Messages> futureMessages = context.moistLifeApp().getClient().send(
                    new TdApi.GetChatHistory(chat.id, fromMsgId, 0, 100, false)
            );

            log.info("check current bulk messages, and mark all as viewed, if the earliest message (length -1)" +
                    "viewed already, means there's no unread message need fetch ");
            TdApi.Message[] unreadMessages = futureMessages.join().messages;
            Arrays.stream(unreadMessages).forEach(message -> {
                unProcessMessages.put(message.id, message);
            });
            fromMsgId = unreadMessages[unreadMessages.length-1].id;
            if (!isMessageUnRead(unreadMessages[unreadMessages.length-1]) || 10000 <= unProcessMessages.size()) {
                log.info("all unread message has been fetched and unProcessMessage size {}", unProcessMessages.size());
                fromMsgId = 0;
            }

            log.info("send view mark to server");
            log.info("send view mark pause for test handle message");
            /*context.moistLifeApp().getClient().send(new TdApi.ViewMessages(chat.id,
                    Arrays.stream(unreadMessages).mapToLong(message -> message.id).toArray(),
                    null,
                    true
                    ));*/

        } while (fromMsgId != 0);

        unProcessMessages.forEach((aLong, message) -> handleMessage(message));

    }

    public void process(List<TdApi.Chat> chats, Context context, boolean firstFlag) {
        chats.forEach(v -> process(v, context, firstFlag));
    }

    private boolean handleMessage(TdApi.Message message) {
        log.info("message is {}", message);
        TdApi.MessageContent content = message.content;

        JsonUtils.writeObjectToJsonFile(content, AppConst.File.channel_message_folder.concat(String.valueOf(message.chatId).concat(".json")));

        return true;
    }

}
