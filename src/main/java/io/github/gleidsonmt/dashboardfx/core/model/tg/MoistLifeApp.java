package io.github.gleidsonmt.dashboardfx.core.model.tg;

import io.github.gleidsonmt.dashboardfx.controllers.LoginController;
import io.github.gleidsonmt.dashboardfx.core.Context;
import io.github.gleidsonmt.dashboardfx.core.base.AppConst;
import it.tdlight.client.SimpleAuthenticationSupplier;
import it.tdlight.client.SimpleTelegramClient;
import it.tdlight.client.SimpleTelegramClientBuilder;
import it.tdlight.jni.TdApi;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * Date: 2024/1/7
 * Author: SilentSherlock
 * Description: TG client
 */
@Slf4j
public class MoistLifeApp implements AutoCloseable{

    private final SimpleTelegramClient client;
    private final Context context;

    public SimpleTelegramClient getClient() {
        return client;
    }

    public MoistLifeApp(@NotNull SimpleTelegramClientBuilder builder, SimpleAuthenticationSupplier<?> authenticationSupplier, Context context) {

        //add start handler
        builder.addUpdateHandler(TdApi.UpdateAuthorizationState.class, this::onUpdateAuthorizationState);
        //add msg handler
//        builder.addUpdateHandler(TdApi.UpdateNewMessage.class, this::onUpdateNewMessage);
        //add user log handler
//        builder.addUpdateHandler(TdApi.UpdateUserStatus.class, this::onUpdateUserStatus);
        //add update handler
//        builder.addUpdateHandler(TdApi.Update.class, this::onUpdateCommonHandler);
        this.context = context;
        this.client = builder.build(authenticationSupplier);
    }



    /**
     * handle user state update
     * @param update
     */
    private void onUpdateAuthorizationState(TdApi.UpdateAuthorizationState update) {

        LoginController loginController = (LoginController) context.routes().getView(AppConst.Nav.Login).getController();
        TdApi.AuthorizationState state = update.authorizationState;
        if (state instanceof TdApi.AuthorizationStateReady) {
            log.info("user logged in");
            log.info("context is null :" + (null == context));
            context.layout().setNav(context.routes().getView(AppConst.Nav.Drawer));
            log.info("layout set drawer");
            context.routes().nav(AppConst.Nav.Dash);
            log.info("route dash");
        } else if (state instanceof TdApi.AuthorizationStateClosing) {
            log.info("user closing");
        } else if (state instanceof TdApi.AuthorizationStateClosed) {
            log.info("user close");
        } else if (state instanceof TdApi.AuthorizationStateLoggingOut) {
            log.info("user logged out");
        } else if (state instanceof TdApi.AuthorizationStateWaitCode) {
            log.info("login need WaitCode");
            loginController.register.setVisible(false);
            loginController.btn_enter.setText("Validate Code");
            loginController.phoneNumber.setPromptText("请输入收到的验证码");

        } else if (state instanceof TdApi.AuthorizationStateWaitPassword) {
            // 当状态为 AuthorizationStateWaitPassword 时，提示用户输入两步验证密码
            log.info("login need WaitPassword");
            loginController.register.setVisible(false);
            loginController.btn_enter.setText("Validate Password");
            loginController.phoneNumber.setPromptText("请输入两步验证密码");

        }
    }


    /**
     * handle user login or logout
     * @param update
     */
    private void onUpdateUserStatus(TdApi.UpdateUserStatus update) {
        TdApi.UserStatus userStatus = update.status;
        long userid = update.userId;
        if (userStatus instanceof TdApi.UserStatusOffline) {
            log.info("user off line, real" + userid);
        } else if (userStatus instanceof TdApi.UserStatusOnline) {
            log.info("user on line, real" + userid);
        }
    }

    /**
     * handle new Msg
     * @param update
     */
    private void onUpdateNewMessage(TdApi.UpdateNewMessage update) {
        //get msg content
        System.out.println("收到消息来自：" + update.message.senderId);
        TdApi.MessageContent messageContent = update.message.content;

        String text = "";
        if (messageContent instanceof TdApi.MessageText) {
            text = ((TdApi.MessageText) messageContent).text.text;
        } else {
            // return other msg's type
            text = messageContent.getClass().getSimpleName();
        }

        //send msg
        long chatId = update.message.chatId;
        String finalText = text;
        client.send(new TdApi.GetChat(chatId))
                .whenCompleteAsync((chatIdResult, error) -> {
                    if (error != null) {
                        log.error("return msg error");
                    } else {
                        // Get the chat name
                        String title = chatIdResult.title;
                        // Print the message
                        System.out.printf("Received new message from chat %s (%s): %s%n", title, chatId, finalText);
                    }
                });
    }

    /**
     * dispatch common update, after receive from TG server
     * @param update
     */
    private void onUpdateCommonHandler(TdApi.Object update) {
        log.info("MoistLife receive a update " + update.toString());
        switch (update.getConstructor()) {
            case TdApi.GetChats.CONSTRUCTOR:
                handleGetChats((TdApi.Chats) update);
        }
    }

    /**
     * handle receive chat list of login account
     * @param chats
     */
    private void handleGetChats(TdApi.Chats chats) {
        log.info("handle account chats");

    }

    @Override
    public void close() throws Exception {
        client.close();
    }

}

