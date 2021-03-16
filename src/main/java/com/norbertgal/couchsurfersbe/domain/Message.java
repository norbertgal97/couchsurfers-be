package com.norbertgal.couchsurfersbe.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "message")
public class Message extends BaseEntity {

    @Column(name = "message")
    private String message;

    @Column(name = "created_at")
    @CreationTimestamp
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @OneToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User user;

}
