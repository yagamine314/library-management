# TODO: Make 'id' the primary key for Livre

- [x] Fix Document.java constructor to accept and set id
- [x] Update create_database.sql: Add id column as PRIMARY KEY, make isbn UNIQUE
- [x] Update LivreDAOImpl.java to use id for primary key operations
- [x] Check and update dependent code in controllers/services if needed
- [x] Run updated SQL script to recreate database (Note: Run the SQL script manually as MySQL is not available in this environment)
- [ ] Test the application
