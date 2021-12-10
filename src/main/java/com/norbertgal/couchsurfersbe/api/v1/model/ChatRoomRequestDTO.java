package com.norbertgal.couchsurfersbe.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomRequestDTO {

    @JsonProperty("chat_room_name")
    private String chatRoomName;

    @JsonProperty("recipient_email")
    private String recipientEmail;
}
