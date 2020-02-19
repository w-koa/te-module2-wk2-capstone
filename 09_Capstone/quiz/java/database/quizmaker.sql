DROP TABLE IF EXISTS results;
DROP TABLE IF EXISTS answers;
DROP TABLE IF EXISTS questions;

CREATE TABLE questions (
    id serial PRIMARY KEY,
    question_text VARCHAR(255) NOT NULL
);

CREATE TABLE answers (
    id serial PRIMARY KEY,
    question_id int REFERENCES questions(id),
    answer_text VARCHAR(255) NOT NULL,
    is_correct BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE results (
    id serial PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    total_questions int NOT NULL,
    correct_questions int NOT NULL
);

INSERT INTO questions (question_text) VALUES ('What color is the sky?');
INSERT INTO questions (question_text) VALUES ('What''s in my pocket?');
INSERT INTO questions (question_text) VALUES ('What walks on four legs in morning, two at noon, and three in the evening?');
INSERT INTO questions (question_text) VALUES ('Mac or PC?');
INSERT INTO questions (question_text) VALUES ('Foster''s is Australian for what?');

INSERT INTO answers (question_id, answer_text, is_correct) VALUES (1, 'Yellow', false);
INSERT INTO answers (question_id, answer_text, is_correct) VALUES (1, 'Red', false);
INSERT INTO answers (question_id, answer_text, is_correct) VALUES (1, 'Blue', true);
INSERT INTO answers (question_id, answer_text, is_correct) VALUES (1, 'Orange', false);

INSERT INTO answers (question_id, answer_text, is_correct) VALUES (2, 'Filthy Hobbites', false);
INSERT INTO answers (question_id, answer_text, is_correct) VALUES (2, 'Ring', true);
INSERT INTO answers (question_id, answer_text, is_correct) VALUES (2, 'Fish', false);
INSERT INTO answers (question_id, answer_text, is_correct) VALUES (2, 'Hole', false);
INSERT INTO answers (question_id, answer_text, is_correct) VALUES (2, 'Is it juicy?', false);

INSERT INTO answers (question_id, answer_text, is_correct) VALUES (3, 'Filthy Hobbites', false);
INSERT INTO answers (question_id, answer_text, is_correct) VALUES (3, 'Gorilla', false);
INSERT INTO answers (question_id, answer_text, is_correct) VALUES (3, 'Fish', false);
INSERT INTO answers (question_id, answer_text, is_correct) VALUES (3, 'Mole', false);
INSERT INTO answers (question_id, answer_text, is_correct) VALUES (3, 'Man', true);

INSERT INTO answers (question_id, answer_text, is_correct) VALUES (4, 'Mac', false);
INSERT INTO answers (question_id, answer_text, is_correct) VALUES (4, 'PC', false);
INSERT INTO answers (question_id, answer_text, is_correct) VALUES (4, 'Linux', true);

INSERT INTO answers (question_id, answer_text, is_correct) VALUES (5, 'Drop Bear', false);
INSERT INTO answers (question_id, answer_text, is_correct) VALUES (5, 'Beer', true);
INSERT INTO answers (question_id, answer_text, is_correct) VALUES (5, 'Steve', false);