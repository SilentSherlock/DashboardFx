/*
 *
 *    Copyright (C) Gleidson Neves da Silveira
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package io.github.gleidsonmt.dashboardfx.controllers;

import io.github.gleidsonmt.dashboardfx.core.base.AppConst;
import io.github.gleidsonmt.dashboardfx.core.base.MyPropertiesUtil;
import io.github.gleidsonmt.dashboardfx.core.interfaces.ActionView;
import io.github.gleidsonmt.dashboardfx.core.model.tg.MoistLifeApp;
import it.tdlight.jni.TdApi;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Gleidson Neves da Silveira | gleidisonmt@gmail.com
 * Create on  06/06/2023
 */
@Slf4j
public class LoginController extends ActionView {

    @FXML
    public Button btn_enter;
    @FXML
    public Button register;
    @FXML
    public TextField phoneNumberField;


    @FXML
    private void login() {
        log.info("Telegram user login");
        String phoneNumber = this.phoneNumberField.getText();
        if (!register.isVisible()) {

            log.info("validate state");

            if ("Validate Code".equals(btn_enter.getText())) {
                context.moistLifeApp()
                        .getClient()
                        .send(new TdApi.CheckAuthenticationCode(phoneNumber), result -> {
                            if (result.isError()) {
                                //current code untested, update GUI, maybe use Platform.runLater
                                this.phoneNumberField.setPromptText("Wrong Code, Please Check the Code");
                            } else {
                                log.info("login success!");
                                context.layout().setNav(context.routes().getView(AppConst.Nav.Drawer));
                                context.routes().nav(AppConst.Nav.Dash);
                            }
                        });
            } else if ("Validate Password".equals(btn_enter.getText())) {
                context.moistLifeApp()
                        .getClient()
                        .send(new TdApi.CheckAuthenticationPassword(phoneNumber), result -> {
                            if (result.isError()) {
                                this.phoneNumberField.setPromptText("Wrong Password, Please Check the Password");
                            } else {
                                log.info("login success!");
                                context.layout().setNav(context.routes().getView(AppConst.Nav.Drawer));
                                context.routes().nav(AppConst.Nav.Dash);
                            }
                        });
            }

        } else {

            log.info("login state");

            if (StringUtils.isBlank(phoneNumber)) {
                log.error("phone number is blank, use default phone number");
            }
            phoneNumber = MyPropertiesUtil.getProperty(AppConst.Tg.user_phone_number);

            MoistLifeApp.login(context, phoneNumber, update -> {
                TdApi.AuthorizationState state = update.authorizationState;
                if (state instanceof TdApi.AuthorizationStateReady) {
                    log.info("user logged in, put moistLifeApp into context");
//                        context.setMoistLifeApp(moistLifeApp);
                    Platform.runLater(() -> {
                        context.layout().setNav(context.routes().getView(AppConst.Nav.Drawer));
                        log.info("layout set drawer");
                        context.routes().nav(AppConst.Nav.Dash);
                        log.info("route dash");
                    });
                } else if (state instanceof TdApi.AuthorizationStateClosing) {
                    log.info("user closing");
                } else if (state instanceof TdApi.AuthorizationStateClosed) {
                    log.info("user close");
                } else if (state instanceof TdApi.AuthorizationStateLoggingOut) {
                    log.info("user logged out");
                } else if (state instanceof TdApi.AuthorizationStateWaitCode) {
                    Platform.runLater(() -> {
                        log.info("login need WaitCode");
                        register.setVisible(false);
                        btn_enter.setText("Validate Code");
                        phoneNumberField.setPromptText("请输入收到的验证码");
                    });

                } else if (state instanceof TdApi.AuthorizationStateWaitPassword) {
                    // 当状态为 AuthorizationStateWaitPassword 时，提示用户输入两步验证密码
                    Platform.runLater(() -> {
                        log.info("login need WaitPassword");
                        register.setVisible(false);
                        btn_enter.setText("Validate Password");
                        phoneNumberField.setPromptText("请输入两步验证密码");
                    });
                }
            });

            log.info("login end");
        }

    }


}
