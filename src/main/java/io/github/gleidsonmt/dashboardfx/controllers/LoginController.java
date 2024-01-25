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
import io.github.gleidsonmt.dashboardfx.core.model.tg.MoistLifeAppThread;
import it.tdlight.jni.TdApi;
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
    public TextField phoneNumber;


    @FXML
    private void login() throws InterruptedException {
        log.info("Telegram user login");
        String phoneNumber = this.phoneNumber.getText();
        if (!register.isVisible()) {

            log.info("validate state");

            if ("Validate Code".equals(btn_enter.getText())) {
                context.moistLifeApp()
                        .getClient()
                        .send(new TdApi.CheckAuthenticationCode(phoneNumber), result -> {
                            if (result.isError()) {
                                this.phoneNumber.setPromptText("Wrong Code, Please Check the Code");
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
                                this.phoneNumber.setPromptText("Wrong Password, Please Check the Password");
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

            log.info("start tg login thread");
            MoistLifeAppThread moistLifeAppThread = new MoistLifeAppThread(context);
            moistLifeAppThread.setPhoneNumber(phoneNumber);

            log.info("add AuthorizationState handler");
            Thread thread = new Thread(moistLifeAppThread, "MoistLife86");
            thread.start();
            this.wait(10000);
            log.info("login start");
            /*context.moistLifeApp()
                    .getClient()
                    .addUpdateHandler(TdApi.UpdateAuthorizationState.class, this::onUpdateAuthorizationState);*/

        }

    }


}
