#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_STRING 1024

int main(void)
{
    // Open file and check it's not null
    FILE *file = fopen("MyQualities.txt", "w");
    if (file == NULL)
    {
        printf("Could not open file MyQualities.txt\n");
        exit(EXIT_FAILURE);
    }

    printf("    Please enter sentences, . to end.\n");

    char input[128];
    char tmpQuality[128];
    char qualitiesString[MAX_STRING];
    while (1)
    {
        fgets(input, sizeof(input), stdin);

        // If input string starts with "I am " then copy that quality to the qualities
        if (strstr(input, "I am ") != NULL)
        {
            // Reset the tmp char array to get rid of left over characters
            memset(tmpQuality, 0, sizeof(tmpQuality));

            // strncpy(destination, source + startIndex, endIndex - startIndex)
            strncpy(tmpQuality, input + 5, strlen(input) - 6); // -5 -1 to remove \n

            // Concatenate extracted quality into the qualities string, append a comma and space after each quality
            strncat(qualitiesString, tmpQuality, 127); // - 1 to account for \0 strncat will add
            strncat(qualitiesString, ", ", 127);

            // Print quality into the MyQualities.txt file
            fprintf(file, "%s\n", tmpQuality);
        }

        // Exit loop if input string is a period ".\n" (must include \n because fgets appends that to the end of every string it reads)
        if (!strcmp(input, ".\n"))
        {
            qualitiesString[strlen(qualitiesString) - 2] = '\0';
            break;
        }
    }

    printf("The qualities are %s.\n", qualitiesString);

    // Close the file for writing and open it for reading
    fclose(file);
    file = fopen("MyQualities.txt", "r");

    printf("\nConfirming the saved qualities...\n");
    while (fgets(input, 128, file))
    {
        printf("%s", input);
    }

    fclose(file);
    if (remove("MyQualities.txt") != 0)
    {
        printf("Error trying to delete file\n");
    }
}