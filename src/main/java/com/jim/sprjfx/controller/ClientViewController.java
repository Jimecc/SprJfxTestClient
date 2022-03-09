package com.jim.sprjfx.controller;

import com.jim.sprjfx.handler.Client;
import de.felixroske.jfxsupport.FXMLController;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@Slf4j
@FXMLController
public class ClientViewController {

    @Autowired
    private Client client;
    public TextField textField3;
    public TextField textField4;
    public TextField textField5;
    public TextField textField6;
    public TextField textField7;

    public void onMouseClicked_button_submit() throws IOException {
        String mark    = textField3.getText() ;
        String id      = textField4.getText() ;
        String info1   = textField5.getText() ;
        String info2   = textField6.getText() ;
        String standby = textField7.getText() ;
//        String socketMessage = "{\"mark\":\""+mark+"\",\"id\":"+id+"\",\"info1\":\""+info1+"\",info2\":\""+info2+"\",\"standby\":\""+standby+"\"}";

        String socketMessage = "{mark:\""+mark+"\",id:\""+id+"\",info1:\""+info1+"\",info2:\""+info2+"\",standby:\""+standby+"\"}";
        client.socketClient(socketMessage);
    }
}
