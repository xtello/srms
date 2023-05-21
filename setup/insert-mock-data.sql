INSERT INTO students (first_name, family_name, date_of_birth, email) 
              VALUES ('John',     'Doe',       '2000-04-22',  'john.doe@gmail.com'),
                     ('Jane',     'Doe',       '2001-05-21',  'jane.doe@gmail.com')
;

INSERT INTO courses  (name) 
              VALUES ('Maths'),
                     ('Computer Science')
;

INSERT INTO results  (student_id, course_id, score) 
              VALUES (1,          1,         'A'),
                     (1,          2,         'B'),
                     (2,          1,         'A')
;
