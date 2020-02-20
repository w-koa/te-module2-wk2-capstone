# QuizMaker

## QuizMaker (Capstone)

Write a program that "gives" a quiz. The quiz `questions` and `answers` will be pulled from the included SQL database. Once a user starts a quiz, the program will display each question that is in the database and its possible answers. The program will wait for the user's answer before moving onto the next question. Once all the questions have been asked,the results will be saved with the user's name back to the `results` table in that database.

All database access will be handled via DAOs and domain objects.

### The requirements are listed below:

- As a user, I can start a quiz that will ask me all the questions in the database.
- As a user, I can see the quiz questions one at a time and pick from the available answers.
    - If I chose an option that is not an answer, ask me again until I submit a valid answer.
- As a user, I can see what answer I chose and what the correct answer was after I answer each question.
- As a user, I can see what my overall score was after the quiz is complete.
    - This score should be saved to the results table with the quiz taker's name.
- As a user, I can see the top 5 scores from users in the database.

Examples

#### Main Menu
```
1. Start Quiz
2. See Top Scores
3. Exit

What would you like to do? 
```

#### Quiz question display and invalid option handling
```
What's your name? Mark Hamill

Welcome Mark Hamill. Here's the quiz!

What color is the sky?
1. Yellow
2. Blue
3. Green
4. Red

Your answer: 8

Invalid answer!

What color is the sky?
1. Yellow
2. Blue
3. Green
4. Red

Your answer: 4
Sorry that isn't correct! The correct answer was Blue.
``` 

#### Quiz Finish
```
You got 1 answer(s) correct out of 2 questions asked
```

#### Top Score Display
```
1 - Mark Hamill:   100% (5 out of 5 questions)
2 - Harrison Ford:  80% (4 out of 5 questions)
3 - Alec Guinness:  80% (4 out of 5 questions)
4 - Peter Mayhew:   60% (3 out of 5 questions)
5 - Frank Oz:       20% (1 out of 5 questions)
```