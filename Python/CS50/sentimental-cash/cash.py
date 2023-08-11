# TODO

def main():
    # Get cents in dollars
    while True:
        try:
            dollars = float(input("Change owed: "))
            if dollars > 0:
                break
        except ValueError:
            print("Error, not an integer")

    # rounds dollars to cents (usage is to put 0.41 for change owed instead of 41)
    dollars = round(dollars * 100)

    quarters = calculateQuarters(dollars)
    dollars = dollars - quarters * 25

    dimes = calculateDimes(dollars)
    dollars = dollars - dimes * 10

    nickels = calculateNickels(dollars)
    dollars = dollars - nickels * 5

    pennies = calculatePennies(dollars)
    dollars = dollars - pennies * 1

    change = quarters + dimes + nickels + pennies

    print(change)


# All of the functions that calculate the coins
def calculateQuarters(dollars):
    return int(dollars / 25)


def calculateDimes(dollars):
    return int(dollars / 10)


def calculateNickels(dollars):
    return int(dollars / 5)


def calculatePennies(dollars):
    return int(dollars / 1)


# Call main!
main()
