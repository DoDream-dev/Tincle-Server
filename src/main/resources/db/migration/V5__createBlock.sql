CREATE TABLE block (
   block_id bigint NOT NULL AUTO_INCREMENT,
   requester_account_id bigint null,
   blocked_account_id bigint null,
   created_at datetime(6)  null,
   modified_at varchar(255) null,
   visibility bit not null,
   PRIMARY KEY (block_id),
   FOREIGN KEY (requester_account_id) REFERENCES tinqle.account (account_id),
   FOREIGN KEY (blocked_account_id) REFERENCES tinqle.account (account_id)
);