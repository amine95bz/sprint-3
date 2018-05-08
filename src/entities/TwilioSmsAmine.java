/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

/**
 *
 * @author Amine
 */
public class TwilioSmsAmine {
    
    public static final String ACCOUNT_SID = "ACef0e4186d8c088a72270a465ce8ce3ab";
    public static final String AUTH_TOKEN = "f039dfe9129458c4ef5f98e260ada346";

    public void sendSms(String body) {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message.creator(new PhoneNumber("+21621514714"),
        new PhoneNumber("+12562910160"), 
        body).create();
    }
    
}
