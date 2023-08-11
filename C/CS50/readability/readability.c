#include <cs50.h>
#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <math.h>

int count_letters(string text);

int count_words(string text);

int count_sentences(string text);

//------------------------------------------------------------------------------------------------------------------------
int main(void)
{
    // Get text from user
    string text = get_string("Text: ");

    // Count the number of letters in the text
    int numLetters = count_letters(text);

    // Count the number of words in the text
    int numWords = count_words(text);

    // Count the number of sentences in the text
    int numSentences = count_sentences(text);

    // Calculate Coleman-Liau index
    double L = (double)numLetters / numWords * 100.0;
    double S = (double)numSentences / numWords * 100.0;

    int index = round((0.0588 * L) - (0.296 * S) - 15.8);

    // Print the result
    if (index >= 16)
    {
        printf("Grade 16+\n");
    }
    else if (index < 1)
    {
        printf("Before Grade 1\n");
    }
    else
    {
        printf("Grade %d\n", index);
    }
}
//------------------------------------------------------------------------------------------------------------------------
int count_letters(string text)
{
    int lengthText = strlen(text);
    int letters = 0;

    for (int i = 0; i < lengthText; i++)
    {
        if ((text[i] >= 'A' && text[i] <= 'Z') || (text[i] >= 'a' && text[i] <= 'z'))
        {
            letters++;
        }
    }
    return letters;
}
//------------------------------------------------------------------------------------------------------------------------
int count_words(string text)
{
    int lengthText = strlen(text);
    int words = 1;

    for (int i = 0; i < lengthText; i++)
    {
        if (text[i] == ' ')
        {
            words++;
        }
    }
    return words;
}
//------------------------------------------------------------------------------------------------------------------------
int count_sentences(string text)
{
    int lengthText = strlen(text);
    int sentences = 0;

    for (int i = 0; i < lengthText; i++)
    {
        if (text[i] == '.' || text[i] == '!' || text[i] == '?')
        {
            sentences++;
        }
    }
    return sentences;
}