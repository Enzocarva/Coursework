"""
Course: Python for Scientists (Part-I)
"""


# %%
def author():
    """
    return your name
    """
    return "Enzo Carvalho"


# %%
import random
import copy


# %%
def DrawBoard(Board):
    """
    Parameter: Board is a 3x3 matrix (a nested list).
    Return: None
    Description: this function prints the chess board
    hint: Board[i][j] is ' ' or 'X' or 'O' in row-i and col-j
          use print function
    """
    for i in range(3):
        for j in range(3):
            print(Board[i][j], end="")
            if j < 2:
                print("|", end="")
        print()
        if i < 2:
            print("-+-+-")
    print()


# %%
def IsSpaceFree(Board, i, j):
    """
    Parameters: Board is the game board, a 3x3 matrix
                i is the row index, j is the col index
    Return: True or False
    Description:
        (1) return True  if Board[i][j] is empty (' ')
        (2) return False if Board[i][j] is not empty
        (3) return False if i or j is invalid (e.g. i = -1 or 100)
        think about the order of (1) (2) (3)
    """
    if (0 > i > 3) or (0 > j > 3):
        return False

    if Board[i][j] == " ":
        return True

    return False


# %%
def GetNumberOfChessPieces(Board):
    """
    Parameter: Board is the game board, a 3x3 matrix
    Return: the number of chess piceces on Board
            i.e. the total number of 'X' and 'O'
    hint: define a counter and use a nested for loop, like this
          for i in 0 to 3
              for j in 0 to 3
                  add one to the counter if Board[i][j] is not empty
    """
    count = 0
    for i in range(3):
        for j in range(3):
            if Board[i][j] != " ":
                count += 1
    return count


# %%
def IsBoardFull(Board):
    """
    Parameter: Board is the game board, a 3x3 matrix
    Return: True or False
    Description:
        return True if the Board is fully occupied
        return False otherwise
    hint: use GetNumberOfChessPieces
    """
    if GetNumberOfChessPieces(Board) < 9:
        return False
    return True


# %%
def IsBoardEmpty(Board):
    """
    Parameter: Board is the game board, a 3x3 matrix
    Return: True or False
    Description:
        return True if the Board is empty
        return False otherwise
    hint: use GetNumberOfChessPieces
    """
    if GetNumberOfChessPieces(Board) == 0:
        return True
    return False


# %%
def UpdateBoard(Board, Tag, Choice):
    """
    Parameters:
        Board is the game board, a 3x3 matrix
        Tag is 'O' or 'X'
        Choice is a tuple (row, col) from HumanPlayer or ComputerPlayer
    Return: None
    Description:
         Update the Board after a player makes a choice
         Set an element of the Board to Tag at the location (row, col)
    """
    Board[Choice[0]][Choice[1]] = Tag


# %%
def HumanPlayer(Tag, Board):
    """
    Parameters:
        Tag is 'X' or 'O'. If Tag is 'X': HumanPlayer is PlayerX who goes first
        Board is the game board, a 3x3 matrix
    Return: ChoiceOfHumanPlayer, it is a tuple (row, col)
    Description:
        This function will NOT return until it gets a valid input from the user
    Attention:
        Board is NOT modified in this function
    hint:
        a while loop is needed, see HumanPlayer in rock-papper-scissors
        the user needs to input row-index and col-index, where a new chess will be placed
        use int() to convert string to int
        use try-except to handle exceptions if the user inputs some random string
        if (row, col) has been occupied, then ask the user to choose another spot
        if (row, col) is invalid, then ask the user to choose a valid spot
    """
    # Check for a valid input
    while True:
        print("make your choice\n")

        try:
            row = int(input("row = "))
            print()
            col = int(input("col = "))
        except ValueError:
            print("Oops! That is not a valid number. Try again...")
            continue

        if (not (0 <= row <= 2)) or (not (0 <= col <= 2)):
            print("Invalid selection for row or col, out of bounds. Choose a valid spot")
            continue

        if not IsSpaceFree(Board, row, col):
            print("This spot is occupied. Choose another one")
            continue

        break

    print("HumanPlayer(", Tag, ") has made the choice", sep="")
    ChoiceOfHumanPlayer = (row, col)
    return ChoiceOfHumanPlayer


# %%
def ComputerPlayer(Tag, Board, N=1):
    """
    Parameters:
        Tag is 'X' or 'O'. If Tag is 'X': ComputerPlayer is PlayerX who goes first
        Board is the game board, a 3x3 matrix
        N: think N steps ahead, default N=1
    Return: ChoiceOfComputerPlayer, it is a tuple (row, col)
    Description:
        ComputerPlayer will choose an empty spot on the board
        a random strategy in a while loop:
            (1) randomly choose a spot on the Board
            (2) if the spot is empty then return the choice (row, col)
            (3) if the spot is not empty then go to (1)
    Attention:
        Board is NOT modified in this function
    """
    # Computer just picks a random empty spot
    # while True:
    #     row = random.randrange(3)
    #     col = random.randrange(3)
    #     if IsSpaceFree(Board, row, col):
    #         ChoiceOfComputerPlayer = (row, col)
    #         break

    # Computer thinks one step ahead

    # if board is empty, place X (since you start) in top left corner
    if IsBoardEmpty(Board):
        print("ComputerPlayer(", Tag, ") has made the choice", sep="")
        return (0, 0)

    # Check if computer can win in next move and if so make that move
    for i in range(3):
        for j in range(3):
            if IsSpaceFree(Board, i, j):
                newBoard = copy.deepcopy(Board)
                newBoard[i][j] = Tag
                if Judge(newBoard) in [1, 2]:
                    print("ComputerPlayer(", Tag, ") has made the choice", sep="")
                    return (i, j)

    # Check if human can win in the next move and if so, block that move
    # I would have put this code in a function to avoid repeating it here, but the
    # instructions said not to add new functions...
    if Tag == "X":  # Change the tag to the human's tag
        HumanTag = "O"
    else:
        HumanTag = "X"

    for i in range(3):
        for j in range(3):
            if IsSpaceFree(Board, i, j):
                newBoard = copy.deepcopy(Board)
                newBoard[i][j] = HumanTag
                if Judge(newBoard) in [1, 2]:
                    print("ComputerPlayer(", Tag, ") has made the choice", sep="")
                    return (i, j)

    # If no move either wins the game or blocks the winning move, make a random move
    for i in range(3):
        for j in range(3):
            if IsSpaceFree(Board, i, j):
                print("ComputerPlayer(", Tag, ") has made the choice", sep="")
                return (i, j)


# %%
def Judge(Board):
    """
    Parameter:
         Board is the current game board, a 3x3 matrix
    Return: Outcome, an integer
        Outcome is 0 if the game is still in progress
        Outcome is 1 if player X wins
        Outcome is 2 if player O wins
        Outcome is 3 if it is a tie (no winner)
    Description:
        this funtion determines the Outcome of the game
    hint:
        (1) check if anyone wins, i.e., three 'X' or 'O' in
            top row, middle row, bottom row
            lef col, middle col, right col
            two diagonals
            use a if-statment to check if three 'X'/'O' in a row
        (2) if no one wins, then check if it is a tie
            note: if the board is fully occupied, then it is a tie
        (3) otherwise, the game is still in progress
    """
    # Check if anyone wins
    xWin = False
    oWin = False
    # Row cases
    if (
        (Board[0][0] == Board[0][1] == Board[0][2] == "X")
        or (Board[1][0] == Board[1][1] == Board[1][2] == "X")
        or (Board[2][0] == Board[2][1] == Board[2][2] == "X")
    ):
        xWin = True
    elif (
        (Board[0][0] == Board[0][1] == Board[0][2] == "O")
        or (Board[1][0] == Board[1][1] == Board[1][2] == "O")
        or (Board[2][0] == Board[2][1] == Board[2][2] == "O")
    ):
        oWin = True

    # Collumn cases
    elif (
        (Board[0][0] == Board[1][0] == Board[2][0] == "X")
        or (Board[0][1] == Board[1][1] == Board[2][1] == "X")
        or (Board[0][2] == Board[1][2] == Board[2][2] == "X")
    ):
        xWin = True
    elif (
        (Board[0][0] == Board[1][0] == Board[2][0] == "O")
        or (Board[0][1] == Board[1][1] == Board[2][1] == "O")
        or (Board[0][2] == Board[1][2] == Board[2][2] == "O")
    ):
        oWin = True

    # Diagonal and anti diagonal cases
    elif (Board[0][0] == Board[1][1] == Board[2][2] == "X") or (
        Board[0][2] == Board[1][1] == Board[2][0] == "X"
    ):
        xWin = True
    elif (Board[0][0] == Board[1][1] == Board[2][2] == "O") or (
        Board[0][2] == Board[1][1] == Board[2][0] == "O"
    ):
        oWin = True

    # X player wins, return 1
    if xWin:
        return 1
    # O player wins, return 2
    elif oWin:
        return 2

    # Game is a tie, return 3
    elif IsBoardFull(Board):
        return 3

    # Else, game is still in progress, return 0
    return 0


# %%
def ShowOutcome(Outcome, NameX, NameO):
    """
    Parameters:
        Outcome is from Judge
        NameX is the name of PlayerX who goes first at the beginning
        NameO is the name of PlayerO
    Return: None
    Description:
        print a meassage about the Outcome
        NameX/NameO may be 'human' or 'computer'
    hint: the message could be
        PlayerX (NameX, X) wins
        PlayerO (NameO, O) wins
        the game is still in progress
        it is a tie
    """
    if Outcome == 1:
        print("PlayerX (", NameX, ", X) wins", sep="")

    elif Outcome == 2:
        print("PlayerO (", NameO, ", O) wins", sep="")

    elif Outcome == 3:
        print("it is a tie!")

    else:
        print("the game is still in progress")


# %% read but do not modify this function
def Which_Player_goes_first():
    """
    Parameter: None
    Return: two function objects: PlayerX, PlayerO
    Description:
        Randomly choose which player goes first.
        PlayerX/PlayerO is ComputerPlayer or HumanPlayer
    """
    if random.randint(0, 1) == 0:
        print("Computer player goes first")
        PlayerX = ComputerPlayer
        PlayerO = HumanPlayer
    else:
        print("Human player goes first")
        PlayerO = ComputerPlayer
        PlayerX = HumanPlayer
    return PlayerX, PlayerO


# %% the game
def TicTacToeGame():
    # ---------------------------------------------------
    print("Wellcome to Tic Tac Toe Game")
    Board = [[" ", " ", " "], [" ", " ", " "], [" ", " ", " "]]
    DrawBoard(Board)
    # determine the order
    PlayerX, PlayerO = Which_Player_goes_first()
    # get the name of each function object
    NameX = PlayerX.__name__
    NameO = PlayerO.__name__
    # ---------------------------------------------------
    # suggested steps in a while loop:
    # (1)  get a choice from PlayerX, e.g. ChoiceX=PlayerX('X', Board)
    # (2)  update the Board
    # (3)  draw the Board
    # (4)  get the outcome from Judge
    # (5)  show the outcome
    # (6)  if the game is completed (win or tie), then break the loop
    # (7)  get a choice from PlayerO
    # (8)  update the Board
    # (9)  draw the Board
    # (10) get the outcome from Judge
    # (11) show the outcome
    # (12) if the game is completed (win or tie), then break the loop
    # ---------------------------------------------------
    # your code starts from here
    while True:
        ChoiceX = PlayerX("X", Board)
        UpdateBoard(Board, "X", ChoiceX)
        DrawBoard(Board)
        Outcome = Judge(Board)
        ShowOutcome(Outcome, NameX, NameO)
        if Outcome in [1, 2, 3]:
            break

        ChoiceO = PlayerO("O", Board)
        UpdateBoard(Board, "O", ChoiceO)
        DrawBoard(Board)
        Outcome = Judge(Board)
        ShowOutcome(Outcome, NameX, NameO)
        if Outcome in [1, 2, 3]:
            break


# %% play the game many rounds until the user wants to quit
# read but do not modify this function
def PlayGame():
    while True:
        TicTacToeGame()
        print("Do you want to play again? (yes or no)")
        if not input().lower().startswith("y"):
            break
    print("GameOver")


# %% do not modify anything below
if __name__ == "__main__":
    PlayGame()
