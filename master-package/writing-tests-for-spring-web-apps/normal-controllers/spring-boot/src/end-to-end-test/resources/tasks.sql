-- Tasks
INSERT INTO tasks
  (id, assignee_id, closer_id, creation_time, creator_id, description, modification_time, modifier_id, resolution, status, title, version)
VALUES
  (1, 1, 1, now(), 1, 'This example contains end-to-end tests', now(), 1, 'DONE', 'CLOSED', 'Write example application', 0);
INSERT INTO tasks
(id, assignee_id, closer_id, creation_time, creator_id, description, modification_time, modifier_id, resolution, status, title, version)
VALUES
  (2, null, null, now(), 1, 'This lesson talks about end-to-end testing', now(), 1, null, 'OPEN', 'Write lesson', 0);

ALTER TABLE tasks ALTER COLUMN id RESTART WITH 3;

-- Tags
INSERT INTO tags
(id, creation_time, modification_time, name, version)
VALUES
  (1, now(), now(), 'example', 0);
INSERT INTO tags
(id, creation_time, modification_time, name, version)
VALUES
  (2, now(), now(), 'lesson', 0);

ALTER TABLE tags ALTER COLUMN id RESTART WITH 3;

INSERT INTO tasks_tags (task_id, tag_id) VALUES (1, 1);
INSERT INTO tasks_tags (task_id, tag_id) VALUES (2, 2);