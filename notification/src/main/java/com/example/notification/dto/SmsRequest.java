package com.example.notification.dto;

import lombok.Data;

@Data
public class SmsRequest {
       private String number;
       private String content;
       
}
