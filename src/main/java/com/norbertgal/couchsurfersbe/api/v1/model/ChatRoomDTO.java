package com.norbertgal.couchsurfersbe.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDTO {
    private Long id;

    @JsonProperty("my_id")
    private Long myId;

    @JsonProperty("recipient_email")
    private String recipientEmail;

    @JsonProperty("chat_room_name")
    private String chatRoomName;
}
