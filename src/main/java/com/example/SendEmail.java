package com.example;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.SendTemplatedEmailRequest;
import software.amazon.awssdk.services.ses.model.Template;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SendEmail {

    public static void main(String[] args) {
        try {
            Region region = Region.US_EAST_1;
            SesClient client = SesClient.builder()
                    .region(region)
                    .build();
            String senderEmail = "sender@yopmail.com";
            String receiverEmail = "receiver@yopmail.com";

          /*  SendTemplatedEmailRequest sendTemplatedEmailRequest = SendTemplatedEmailRequest.builder().build();
            sendTemplatedEmailRequest.setTemplate("TransactionalTemplate");
            sendTemplatedEmailRequest.setSource(senderEmail);
            sendTemplatedEmailRequest.setDestination(new Destination(Arrays.asList(receiverEmail)));
            sendTemplatedEmailRequest.setTemplateData(templateData);
            amazonSimpleEmailService.sendTemplatedEmail(sendTemplatedEmailRequest);
*/


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
