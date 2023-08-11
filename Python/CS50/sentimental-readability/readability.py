# TODO

def main():
    # Get text from user
    text = input("Text: ")

    # Count the number of letters in the text
    numLetters = countLetters(text)

    # Count the number of words in the text
    numWords = countWords(text)

    # Count the number of sentences in the text
    numSentences = countSentences(text)

    # Calculate Coleman_Liau index
    l = float(numLetters / numWords * 100.0)
    s = float(numSentences / numWords * 100.0)

    index = round((0.0588 * l) - (0.296 * s) - 15.8)

    # Print result
    if index >= 16:
        print("Grade 16+")
    elif index < 1:
        print("Before Grade 1")
    else:
        print("Grade", index)


def countLetters(text):
    letters = 0

    for i in range(len(text)):
        if (text[i] >= 'A' and text[i] <= 'Z') or (text[i] >= 'a' and text[i] <= 'z'):
            letters += 1

    return letters


def countWords(text):
    words = 1

    for i in range(len(text)):
        if text[i] == ' ':
            words += 1

    return words


def countSentences(text):
    sentences = 0

    for i in range(len(text)):
        if text[i] == '.' or text[i] == '!' or text[i] == '?':
            sentences += 1

    return sentences


# Call main
main()