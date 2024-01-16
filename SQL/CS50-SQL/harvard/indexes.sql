CREATE INDEX enrollments_course_index ON enrollments (course_id);
CREATE INDEX enrollments_student_index ON enrollments (student_id);

CREATE INDEX course_semester_index ON courses (semester);

CREATE INDEX satifies_course_id_index ON satisfies (course_id);
