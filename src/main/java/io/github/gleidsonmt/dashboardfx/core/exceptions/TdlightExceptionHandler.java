package io.github.gleidsonmt.dashboardfx.core.exceptions;

import it.tdlight.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Date: 2024/1/23
 * Author: SilentSherlock
 * Description: handle tdlight-java result exception
 */
@Slf4j
public class TdlightExceptionHandler implements ExceptionHandler {

    @Override
    public void onException(Throwable e) {
        log.error(e.getMessage());
        e.printStackTrace();
        throw new RuntimeException(e.getMessage());
    }
}
