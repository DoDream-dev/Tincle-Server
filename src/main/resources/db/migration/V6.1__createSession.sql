CREATE TABLE session (
                      id bigint NOT NULL AUTO_INCREMENT,
                      account_id bigint null,
                      room_id bigint null,
                      session_id varchar(255) null,
                      created_at datetime(6)  null,
                      modified_at varchar(255) null,
                      visibility bit not null,
                      PRIMARY KEY (id),
                      FOREIGN KEY (account_id) REFERENCES tinqle.account (account_id),
                      FOREIGN KEY (room_id) REFERENCES tinqle.room (room_id)
);
ALTER TABLE account ADD COLUMN session_id varchar(255) default null;


