#include <cs50.h>
#include <ctype.h>
#include <stdio.h>
#include <string.h>
#include <math.h>

int main(int argc, string argv[])
{
    string key = argv[1];

    // Validate key for length and number of arguments
    if (argc != 2 || strlen(key) != 26)
    {
        printf("Error, execute (./substitution KEY) should be a single key of 26 letters for the program to work\n");
        return 1;
    }

    // Validate key for only alphabetical
    for (int i = 0; i < strlen(key); i++)
    {
        if (!isalpha(key[i]))
        {
            printf("Error, key should be alphabetical letters only\n");
            return 1;
        }
    }

    // Validate for no repeats and each letter of alphabet
    for (int i = 0; i < strlen(key); i++)
    {
        for (int j = i + 1; j < strlen(key); j++)
        {
            if (toupper(key[i]) == toupper(key[j]))
            {
                printf("Error, key should have one of each aplhabetical letter\n");
                return 1;
            }
        }
    }

    // Get plain text from user
    string plaintext = get_string("plaintext: ");

    // Convert everything to upper case to print cipher
    for (int i = 0; i < strlen(key); i++)
    {
        if (islower(key[i]))
        {
            key[i] = key[i] - 32;
        }
    }

    // Print cipher text
    printf("ciphertext: ");

    // All plaintext possibilities
    for (int i = 0; i < strlen(plaintext); i++)
    {
        if (isupper(plaintext[i]))
        {
            int letter = plaintext[i] - 65;
            printf("%c", key[letter]);
        }
        else if (islower(plaintext[i]))
        {
            int letter = plaintext[i] - 97;
            printf("%c", key[letter] + 32);
        }
        else
        {
            printf("%c", plaintext[i]);
        }
    }
    printf("\n");
    return 0;
}