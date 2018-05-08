/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.twilio.Twilio;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
//TwilioSms sms = new TwilioSms();
//
//        
//        
//        sms.sendSms("l'etudiant "+Session.getUsernameThisUser()+" a choisi l'annonce dont l'addresse est " + colocation.getAdresse());

/**
 *
 * @author Karim
 */
public class TwilioSms {

    public static final String ACCOUNT_SID = "AC7344f0a6be1c6df386c779884ece9ae3";
    public static final String AUTH_TOKEN = "fb4c8031aa64c610ef4d3131313303f0";

    public void sendSms(String body) {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message.creator(new PhoneNumber("+21656103222"),
        new PhoneNumber("+14078907039"), 
        body).create();
    }
}
