DROP TABLE IF EXISTS my_client;
CREATE TABLE my_client(
    	id SERIAL PRIMARY KEY, 
	name VARCHAR(255),
	phone VARCHAR(255),
	address VARCHAR(255),
	email VARCHAR(255),
	balance VARCHAR(255)
); 
