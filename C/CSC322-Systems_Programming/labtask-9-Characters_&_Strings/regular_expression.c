#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <regex.h>

#define MAX_STRING 256
#define DELIMITERS ".!?,"
typedef char string[MAX_STRING];

int main(void)
{
    string inputString, inputRegEx;
    char *token;
    int words;
    regex_t compiledRegEx;

    printf("Please enter the srting to analyse\n");
    scanf(" %[^\n]s", inputString);

    printf("Please enter the regular expression : ");
    scanf(" %[^\n]s", inputRegEx);

    // Check if the regular expression compiles correctly
    if (regcomp(&compiledRegEx, inputRegEx, REG_EXTENDED) != 0)
    {
        printf("Something went wrong with the regular expression\n");
        exit(EXIT_FAILURE);
    }

    char *copyInputString = inputString; // for safety due to how strtok alters the string
    while ((token = strtok_r(copyInputString, DELIMITERS, &copyInputString)))
    {
        // Remove the space in front of every sentence after the first
        string copyToken;
        strcpy(copyToken, token);
        if (token[0] == ' ')
        {
            strncpy(copyToken, token + 1, strlen(token));
        }
        printf("%s\n", copyToken);

        // print if the regular expression "hits" by executing it
        if (regexec(&compiledRegEx, copyToken, 0, NULL, 0) == 0)
        {
            printf("Yes    ");
        }
        else
        {
            printf("No     ");
        }

        words = 1;
        for (int i = 0; i < strlen(copyToken); i++)
        {
            if (copyToken[i] == ' ')
            {
                words++;
            }
        }
        printf("%d words\n", words);
        memset(copyToken, 0, sizeof(copyToken));
    }
    regfree(&compiledRegEx);
    return (EXIT_SUCCESS);
}