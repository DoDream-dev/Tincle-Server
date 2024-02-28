CREATE TABLE room (
                       room_id bigint NOT NULL AUTO_INCREMENT,
                       starter_id bigint null,
                       friend_id bigint null,
                       is_deleted_from_starter bit not null,
                       is_deleted_from_friend bit not null,
                       created_at datetime(6)  null,
                       modified_at varchar(255) null,
                       visibility bit not null,
                       PRIMARY KEY (room_id),
                       FOREIGN KEY (starter_id) REFERENCES tinqle.account (account_id),
                       FOREIGN KEY (friend_id) REFERENCES tinqle.account (account_id)
);

CREATE TABLE message (
                      message_id bigint NOT NULL AUTO_INCREMENT,
                      sender_id bigint null,
                      receiver_id bigint null,
                      room_id bigint null,
                      content VARCHAR(255) null,
                      is_read_from_receiver bit not null,
                      is_deleted_from_sender bit not null,
                      is_deleted_from_receiver bit not null,
                      created_at datetime(6)  null,
                      modified_at varchar(255) null,
                      visibility bit not null,
                      PRIMARY KEY (message_id),
                      FOREIGN KEY (sender_id) REFERENCES tinqle.account (account_id),
                      FOREIGN KEY (receiver_id) REFERENCES tinqle.account (account_id),
                      FOREIGN KEY (room_id) REFERENCES tinqle.room (room_id)
);