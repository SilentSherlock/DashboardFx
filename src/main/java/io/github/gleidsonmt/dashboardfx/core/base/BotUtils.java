package io.github.gleidsonmt.dashboardfx.core.base;

import org.telegram.telegrambots.bots.DefaultBotOptions;

/**
 * Date: 2023/11/14
 * Author: SilentSherlock
 * Description: offer static method for bot
 */
public class BotUtils {

    public static DefaultBotOptions getDefaultOption() {
        DefaultBotOptions options = new DefaultBotOptions();
        options.setProxyHost(AppConst.Proxy.proxy_host);
        options.setProxyPort(AppConst.Proxy.proxy_port);
        options.setProxyType(DefaultBotOptions.ProxyType.HTTP);
        return options;
    }
}
