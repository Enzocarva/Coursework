# TODO

# get input for height from the user
while True:
    try:
        height = int(input("Height: "))
        if height >= 1 and height <= 8:
            break
    except ValueError:
        print("Error")


# print the pyramid using nested for loops
for i in range(height):
    for j in range(height + i + 3):
        if j == height or j == height + 1 or (i + j) < (height - 1):
            print(" ", end="")
        else:
            print("#", end="")

    print()