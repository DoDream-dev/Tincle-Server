ALTER TABLE emoticon ADD COLUMN comment_id bigint default null;

alter table emoticon add constraint fk_emoticon_comment_id foreign key (comment_id) references comment(comment_id)