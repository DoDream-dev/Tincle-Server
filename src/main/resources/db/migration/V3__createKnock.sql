CREATE TABLE knock (
    knock_id bigint NOT NULL AUTO_INCREMENT,
    account_id bigint null,
    send_account_id bigint null,
    created_at datetime(6)  null,
    modified_at varchar(255) null,
    visibility bit not null,
    PRIMARY KEY (knock_id),
    FOREIGN KEY (account_id) REFERENCES tinqle.account (account_id),
    FOREIGN KEY (send_account_id) REFERENCES tinqle.account (account_id)
);