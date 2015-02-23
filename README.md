# Memory Matcher
A simple memory matching card game

## Requirements

1. Login to the app (just a user name, store it locally to keep aggregate score)
2. User can swipe between 2 screens
3. On each screen there are 4 rows and 3 columns of cards
4. Cards are all numbered 1 - 6 and colored Red and Black and "turned over"
5. One tap turns over the first card
6. Second tap turns over the 2nd card
7. On a match they both remain turned over
8. On no match the both revert to a hidden state
9. Once all the cards are matched, the player is presented with their score and the option to shuffle and play again or log out. On a match the user is awarded 10 points, on a no match scenario the user loses one point. 
10. If the player leaves a game at any point, their state is persisted so that he/she can return to the game, or if the application is closed and they login with the same user name the game is resumed at the same state it was when they left