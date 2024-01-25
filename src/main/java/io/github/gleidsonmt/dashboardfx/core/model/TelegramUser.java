package io.github.gleidsonmt.dashboardfx.core.model;

import io.github.gleidsonmt.dashboardfx.core.Context;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Date: 2024/1/25
 * Author: SilentSherlock
 * Description: Telegram user model
 */
@Slf4j
@Data
public class TelegramUser {

    private Context context;
    private String phoneNumber;

    public static TelegramUser createTelegramUser(Context context) {
        return new TelegramUser(context);
    }

    public TelegramUser(Context context) {
        this.context = context;
    }

    /**
     * user login, callback for caller
     * @param phoneNumber
     */
    public void login(String phoneNumber) {
        // TODO: 2024/1/25 迁移login功能
    }

    /**
     * load user chat list
     */
    public void loadChatList() {

    }
}
