# Tic Tac Toe
This is a simple [Tic Tac Toe](https://en.wikipedia.org/wiki/Tic-tac-toe) game played in the console.
It's a project I completed for [HyperSkill](https://hyperskill.org/) with some tweaks beyond
the requirements to make it a little better. 
The rules are the same as in the original game.
It features **PvP**, **PvE** and **AI Battles** in which 2 **AIs** battle against each other.


## How to play
To start a **new play session** you have to input "*start* **var1** **var2**".
After each game you will be prompted to input the settings you want (the variables) in that *exact* syntax if you want to play more games (menu loop).
If you wish to **finish playing**, simply type "*exit*".
**var1** determines the party which makes their move first (mark X) and **var2** the one that will go second (mark O).
The variables can either be "user", "easy", "medium" or "hard".
**"user"** represents a **human player**, and the other possible variables represent the **difficulty of the AI.**
If you for example want to play against another friend (PvP) you would type "*start* **user user**".
For playing against the AI (PvE) either "*start* **user** *difficulty*" or "*start* *difficulty* **user**" (e.g. "*start* **hard user**")
depending if you want to go first or second.
And lastly if you want to see **AIs battling** each other type "*start difficulty difficulty*" (e.g. "*start* **easy hard**"
for an easy AI to do the first move and battle a hard AI).

## The different AI difficulties

### Easy
- Makes a **random** move
- Any cell can be chosen as long as it's not occupied by a mark (valid move)

### Medium
- Tries to **win** if it can accomplish that in **one move** (already 2 in a row by chance)
- Plays the necessary move to **block** the opponent if he already has 2 in a row and would win by his next turn
- If the conditions above are not present, makes a **random** move just like in **Easy**

### Hard
- Makes the **best** move every time
- Because Hard resembles **perfect play** by making no mistakes, it can't lose and will always **draw**
  if the opponent plays optimally too or **win** if the opponent makes a mistake

## Concepts used for the game logic

### How the game checks for a "game over"
The algorithm used for **evaluating the state of the game** and if a win condition is met is
utilizing [Integer factorization](https://en.wikipedia.org/wiki/Integer_factorization) with
prime numbers. Every **cell** of the game (3 rows with 3 cells each totalling 9) has a **number assigned** to it.
This number is either equivalent one of the **prime numbers** 1-9 (e.g. 2, 3, 5, 7, 11...) or **1.**
The evaluation of a given cell depends on if its occupied by the marker the game is currently checking for a win.
For example if the game wants do determine if **X** has won, all cells which are **occupied by X** have their
corresponding **prime number evaluated** for the next step. All other cells are valued as **1.**

Once the game has an **2D Array** resembling the 3x3 Tic Tac Toe board full of numbers (each cell being either a prime number or a 1),
it calculates the products from every possible **full row** that would **result in a win** (8 in total).
These 8 products are then scaned for containing one of those numbers: *30, 1000, 7429, 238, 627, 1495, 935 or 506.*
Each of those numbers is a product of multiplying 3 prime numbers in a given row.
The row **CANNOT** contain a 1 for this to be accomplished. That means if one of those **numbers** is found within the **products of the 8 rows** that are calculated for our current board,
it must mean that the mark we are checking has a **full row** and ultimately **won** the game.
[Here](http://prntscr.com/13hhg4a) is a graphic for reference.

### Unbeatable algorithm used for Hard
The algorithm used for **Hard** (and to a lesser extent Medium) is called [Minimax](https://en.wikipedia.org/wiki/Minimax)
and is assisted by [Alpha-beta pruning](https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning).
Explaining these concepts here would be too long and complex. You can read more about them in the referenced pages. :)
