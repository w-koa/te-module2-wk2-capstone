package com.techelevator;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

public class QuizMakerCLI {

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/quizmaker");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		
		QuizMakerCLI application = new QuizMakerCLI(dataSource);
		application.run();
	}

	public QuizMakerCLI(DataSource datasource) {
		// create your DAOs here
	}
	
	public void run() {
		
	}
}
