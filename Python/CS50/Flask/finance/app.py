import os

from cs50 import SQL
from flask import Flask, flash, redirect, render_template, request, session
from flask_session import Session
from tempfile import mkdtemp
from werkzeug.security import check_password_hash, generate_password_hash

from helpers import apology, login_required, lookup, usd
import datetime

# Configure application
app = Flask(__name__)

# Custom filter
app.jinja_env.filters["usd"] = usd

# Configure session to use filesystem (instead of signed cookies)
app.config["SESSION_PERMANENT"] = False
app.config["SESSION_TYPE"] = "filesystem"
Session(app)

# Configure CS50 Library to use SQLite database
db = SQL("sqlite:///finance.db")


@app.after_request
def after_request(response):
    """Ensure responses aren't cached"""
    response.headers["Cache-Control"] = "no-cache, no-store, must-revalidate"
    response.headers["Expires"] = 0
    response.headers["Pragma"] = "no-cache"
    return response


@app.route("/")
@login_required
def index():
    """Show portfolio of stocks"""

    # Define current user's ID.
    currentUserID = session["user_id"]

    # Query database for symbols, total shares, and cash
    stocks = db.execute("SELECT symbol, SUM(shares) AS shares FROM portfolio WHERE user_id = ? GROUP BY symbol;", currentUserID)
    cash = db.execute("SELECT cash FROM users WHERE id = ?", currentUserID)[0]["cash"]

    # Get the total value of all stocks
    total = cash

    # Get all of the user stocks' current prices and add it to the stocks list
    for stock in stocks:
        temp = lookup(stock["symbol"])
        stock["price"] = temp["price"]
        total += stock["price"] * stock["shares"]

    return render_template("index.html", stocks=stocks, cash=cash, total=total, usd=usd)


@app.route("/buy", methods=["GET", "POST"])
@login_required
def buy():
    """Buy shares of stock"""

    # GET method displays the input form to buy stocks
    if request.method == "GET":
        return render_template("buy.html")

    # POST method submits the user's request to buy x shares of a stock
    else:
        # Check if stock is blank and exists and if shares is blank or negative number. Declare symbol, shares, stock and ID
        symbol = request.form.get("symbol").upper() # Make the stock symbol upper case
        shares = request.form.get("shares")
        stock = lookup(symbol)
        currentUserID = session["user_id"]

        if not symbol or stock == None:
            return apology("stock doesn't exist")

        if not shares or not shares.isnumeric() or float(shares) < 0.01:
            return apology("invalid number of shares", 400)

        # Check if user has enough cash to purchase stock
        transactionValue = stock["price"] * float(shares)
        userCash = db.execute("SELECT cash FROM users WHERE id = ?", currentUserID)

        if userCash[0]["cash"] < transactionValue:
            return apology("not enough cash available to purchase stock")

        # Update the user's cash amount
        updatedCash = userCash[0]["cash"] - transactionValue
        db.execute("UPDATE users SET cash = ? WHERE id = ?", updatedCash, currentUserID)

        # Set date variable
        date = datetime.datetime.now()

        # Update transactions table in SQL
        db.execute("INSERT INTO transactions (user_id, symbol, shares, price, date, type) VALUES (?, ?, ?, ?, ?, ?)", currentUserID,
                   symbol, shares, stock["price"], date, "Bought")

        # Query database to get stocks the user owns
        stocks = db.execute("SELECT symbol, SUM(shares) AS shares FROM portfolio WHERE user_id = ? GROUP BY symbol", currentUserID)

        # Check if stock is already in portfolio, if so just update the number of shares, else insert new stock
        ownedStocks = [ sub['symbol'] for sub in stocks ]
        if symbol in ownedStocks:
            selectedStockShares = db.execute("SELECT SUM(shares) AS shares FROM portfolio WHERE symbol = ? AND user_id = ?", symbol, currentUserID)[0]["shares"]
            db.execute("UPDATE portfolio SET shares = ? WHERE user_id = ? AND symbol = ?", (selectedStockShares + float(shares)), currentUserID, symbol)

        else:
            # Update portfolio table in SQL
            db.execute("INSERT INTO portfolio (user_id, symbol, shares, price) VALUES (?, ?, ?, ?)", currentUserID,
                    symbol, float(shares), stock["price"])

        # display a confirmation message and redirect to home page
        flash("Purchased Successfully")
        return redirect("/")


@app.route("/history")
@login_required
def history():
    """Show history of transactions"""

    # Get current user's ID
    userID = session["user_id"]

    # Query database for symbols and shares
    stocks = db.execute("SELECT symbol, price, type, date, shares FROM transactions WHERE user_id = ?", userID)

    return render_template("history.html", stocks=stocks, usd=usd)


@app.route("/login", methods=["GET", "POST"])
def login():
    """Log user in"""

    # Forget any user_id
    session.clear()

    # User reached route via POST (as by submitting a form via POST)
    if request.method == "POST":

        # Ensure username was submitted
        if not request.form.get("username"):
            return apology("must provide username", 403)

        # Ensure password was submitted
        elif not request.form.get("password"):
            return apology("must provide password", 403)

        # Query database for username
        rows = db.execute("SELECT * FROM users WHERE username = ?", request.form.get("username"))

        # Ensure username exists and password is correct
        if len(rows) != 1 or not check_password_hash(rows[0]["hash"], request.form.get("password")):
            return apology("invalid username and/or password", 403)

        # Remember which user has logged in
        session["user_id"] = rows[0]["id"]

        # Redirect user to home page
        return redirect("/")

    # User reached route via GET (as by clicking a link or via redirect)
    else:
        return render_template("login.html")


@app.route("/logout")
def logout():
    """Log user out"""

    # Forget any user_id
    session.clear()

    # Redirect user to login form
    return redirect("/")


@app.route("/quote", methods=["GET", "POST"])
@login_required
def quote():
    """Get stock quote."""

    # GET method displays the form to quote for a stock
    if request.method == "GET":
        return render_template("quote.html")

    # POST method receives the symbol and redirects to "/quoted" to show the stats of the stock
    else:

        # stockQuote is a dictionary, lookup returns a dict with name, price, symbol, check if lookup was successful
        stockQuote = lookup(request.form.get("symbol"))

        if stockQuote == None or not request.form.get("symbol"):
            return apology("symbol doesn't exist")

        return render_template("quoted.html", stockQuote=stockQuote, price=usd(stockQuote["price"]))


@app.route("/register", methods=["GET", "POST"])
def register():
    """Register user"""

    # User reached route via "GET" (just trying to access the page), send them to "/register" webpage
    if request.method == "GET":
        return render_template("register.html")

    # POST method
    else:

        # Check username and password are not blank or username already exists or username is already taken
        if not request.form.get("username") or not request.form.get("password"):
            return apology("must provide a username and password")

        if request.form.get("password") != request.form.get("confirmation"):
            return apology("password and confirmation don't match")

        rows = db.execute("SELECT * FROM users WHERE username = ?", request.form.get("username"))
        if len(rows) != 0:
            return apology("username is already taken")

        # Generate password hash and insert user into the database
        passwordHash = generate_password_hash(request.form.get("password"))
        newUserID = db.execute("INSERT INTO users (username, hash) VALUES(?, ?)", request.form.get("username"), passwordHash)

        # log the user in and redirect to homepage
        session["user_id"] = newUserID
        return redirect("/")


@app.route("/sell", methods=["GET", "POST"])
@login_required
def sell():
    """Sell shares of stock"""

    # Get current logged in user's ID
    userID = session["user_id"]

    # GET method, display sell page
    if request.method == "GET":

        # Get all of the stocks the user owns
        stocks = db.execute("SELECT symbol, SUM(shares) AS shares FROM portfolio WHERE user_id = ? GROUP BY symbol;", userID)

        return render_template("sell.html", stocks=stocks)

    # POST method, use the received info from the form to sell the user's stock and update accordingly
    else:

        # Get all of the stocks the user owns to double check input (prevent attacks)
        stocks = db.execute("SELECT symbol, SUM(shares) AS shares FROM portfolio WHERE user_id = ? GROUP BY symbol;", userID)

        symbol = request.form.get("symbol")
        numShares = request.form.get("shares")

        # Get all values of 'symbol' key in stocks list of dictionaries
        ownedStocks = [ sub['symbol'] for sub in stocks ]

        # Check if either input is null or invalid (not positive or not a stock they own)
        if not symbol or symbol not in ownedStocks:
            return apology("invalid stock selection")

        if not numShares or float(numShares) < 0.01:
            return apology("invalid number of shares")

        # Update symbol to upper case after checks (None value causes error from Select input in html)
        symbol = symbol.upper()

        # Check that user owns the number of shares selected
        selectedStockShares = db.execute("SELECT SUM(shares) AS shares FROM portfolio WHERE symbol = ? AND user_id = ?", symbol, userID)[0]["shares"]
        if float(numShares) > selectedStockShares:
            return apology("you don't own that many shares of this stock")

        # Get the price the stock sold for
        price = (lookup(symbol)["price"]) * float(numShares)

        # Get the user's cash (cash + price just sold) balance from SQL
        cash = db.execute("SELECT cash FROM users WHERE id = ?", userID)[0]["cash"] + price

        # Set date variable
        date = datetime.datetime.now()

        # Update transactions table in SQL
        db.execute("INSERT INTO transactions (user_id, symbol, shares, price, date, type) VALUES (?, ?, ?, ?, ?, ?)", userID,
                   symbol, numShares, price, date, "Sold")

        # Update user's cash balance in SQL
        db.execute("UPDATE users SET cash = ? WHERE id = ?", cash, userID)

        # Update portfolio table in SQL
        if float(numShares) == selectedStockShares:
            db.execute("DELETE FROM portfolio WHERE symbol = ? AND user_id = ?", symbol, userID)

        else:
            db.execute("UPDATE portfolio SET shares = ? WHERE user_id = ? AND symbol = ?",
                       (selectedStockShares - float(numShares)), userID, symbol)

        flash("Sold Successfully")
        return redirect("/")


@app.route("/add", methods=["GET", "POST"])
@login_required
def add():
    """Add more cash to your account"""

    # Current user's ID:
    userID = session["user_id"]

    # GET method
    if request.method == "GET":
        return render_template("add.html")

    # POST method
    else:
        amount = request.form.get("amount")

        # Make sure input is valid
        if not amount or float(amount) < 0.01:
            return apology("invalid amount of money added")

        # get user's current cash and update it
        cash = db.execute("SELECT cash FROM users WHERE id = ?", userID)[0]["cash"]
        cash += float(amount)

        db.execute("UPDATE users SET cash = ? WHERE id = ?", cash, userID)

        flash("Added Funds Successfully")
        return redirect("/")