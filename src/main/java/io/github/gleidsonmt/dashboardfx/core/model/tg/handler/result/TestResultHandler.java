package io.github.gleidsonmt.dashboardfx.core.model.tg.handler.result;

import it.tdlight.client.GenericResultHandler;
import it.tdlight.client.Result;
import it.tdlight.jni.TdApi;

/**
 * Date: 2024/1/26
 * Author: SilentSherlock
 * Description: describe the file
 */
public class TestResultHandler implements GenericResultHandler<TdApi.GetChats> {
    @Override
    public void onResult(Result<TdApi.GetChats> result) {

    }
}
