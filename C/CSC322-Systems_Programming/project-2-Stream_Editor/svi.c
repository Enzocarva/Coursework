#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <math.h>

#define EDIT_STRING_MAX 80
#define TOTAL_STRING_MAX 256
#define MAX_EDIT_OPERATIONS 100

//------------------------------------- TypeDefs ------------------------------------------------------
typedef enum
{
    none,
    text,
    range,
} LineRangeSpecificierType;

typedef struct
{
    LineRangeSpecificierType type;
    union
    {
        char text[EDIT_STRING_MAX];
        struct
        {
            int start;
            int end;
        } range;
    } data;
} LineRangeSpecifier;

typedef struct
{
    LineRangeSpecifier lineSpecifier;
    char editOperationSpecifier;
    char editOperationData1[EDIT_STRING_MAX];
    char editOperationData2[EDIT_STRING_MAX];
} EditOperation;

//--------------------------------- Function Prototypes -----------------------------------------------
void stringToEditOpertaion(EditOperation editOperationschar[], char *line, int *numEditOps);
void getEditOpData(char *restOfLine, EditOperation *currentEditOp);
int isBetween(int *x, int *start, int *end);
void executeEditOperation(EditOperation *currentEditOp, char currentInputLine[], int *isDeleted);
void replaceString(char *source, size_t sourceSize, char *replace, char *with);

//--------------------------------------- Main --------------------------------------------------------
int main(int argc, char *argv[])
{
    EditOperation editOperations[MAX_EDIT_OPERATIONS];
    int numEditOps = 0;
    char inputLine[TOTAL_STRING_MAX];
    char cleanedInputLine[TOTAL_STRING_MAX];
    int currentStdInLineNum = 1;
    int isDeleted;

    // Check for corect usage, Open edits.txt file, Read all the lines/edits and store them
    if (argc != 2) // FIXME
    {
        printf("Usage: cat input.txt | ./svi editx.txt\n");
        return (EXIT_FAILURE);
    }

    FILE *filePointer = fopen(argv[1], "r"); // edits.txt file pointer
    if (filePointer == NULL)
    {
        printf("Error trying to open edits.txt file\n");
        return (EXIT_FAILURE);
    }

    while (fgets(inputLine, TOTAL_STRING_MAX, filePointer)) // edits.txt
    {
        char *dupedLine = strdup(inputLine); // not necessary, preserve original string
        stringToEditOpertaion(editOperations, dupedLine, &numEditOps);
        free(dupedLine);
    }
    fclose(filePointer); // closed edits.txt file pointer

    // Get the input to be edited from stdin
    while (fgets(inputLine, TOTAL_STRING_MAX, stdin)) // fgets eliminates last char of input file (add an extra char)
    {
        // Get rid of \n from fgets and reset it every time and set deleted to false (0)
        memset(cleanedInputLine, 0, sizeof(cleanedInputLine));
        strncpy(cleanedInputLine, inputLine, strlen(inputLine) - 1);
        isDeleted = 0;

        for (int i = 0; i < numEditOps; i++)
        {
            // If line in the range specified or contains the <text>, do the edit
            if (editOperations[i].lineSpecifier.type == range)
            {
                if (isBetween(&currentStdInLineNum, &editOperations[i].lineSpecifier.data.range.start,
                              &editOperations[i].lineSpecifier.data.range.end))
                {
                    // current line is in an edit's range
                    executeEditOperation(&editOperations[i], cleanedInputLine, &isDeleted);
                }
            }
            else if (editOperations[i].lineSpecifier.type == text)
            {
                if (strstr(inputLine, editOperations[i].lineSpecifier.data.text) != NULL)
                {
                    // current line has an edit's word/string
                    executeEditOperation(&editOperations[i], cleanedInputLine, &isDeleted);
                }
            }
            else // line specifier type == none (all lines in the file)
            {
                executeEditOperation(&editOperations[i], cleanedInputLine, &isDeleted);
            }
            // Stop going through further operations if the line has been deleted
            if (isDeleted == 1)
            {
                break;
            }
        }

        // Output the edited line (unless deleted) and increment line number
        if (isDeleted == 0)
        {
            printf("%s\n", cleanedInputLine);
        }
        currentStdInLineNum++;
    }
}

//-------------------------------- Get Edit Operations functions --------------------------------------
// Createa a new edit operation based on the string input from the edits.txt file and put them in array
void stringToEditOpertaion(EditOperation editOperations[], char *line, int *numEditOps)
{
    EditOperation newEditOp;
    char *tok;
    tok = strtok(line, "/"); // tok 1

    // Get all the info based on the type of line specifier
    if (line[0] == '/')
    {
        // Get the type and data for lineSpecifier
        newEditOp.lineSpecifier.type = text;
        strcpy(newEditOp.lineSpecifier.data.text, tok);

        // Get the operation specifier and data
        tok = strtok(NULL, "/"); // tok 2
        newEditOp.editOperationSpecifier = tok[0];
        getEditOpData(tok, &newEditOp);
    }
    else if (isdigit(line[0]))
    {
        // Get the type and range start and end
        newEditOp.lineSpecifier.type = range;
        newEditOp.lineSpecifier.data.range.start = atoi(tok);

        // Get length of start number to know where to look for second number
        int num1Length = 0;
        if (newEditOp.lineSpecifier.data.range.start != 0)
        {
            num1Length = floor(log10(abs(newEditOp.lineSpecifier.data.range.start))) + 1;
        }

        newEditOp.lineSpecifier.data.range.end = atoi(tok + num1Length + 1);

        // Get the operation specifier and data
        tok = strtok(NULL, "/"); // tok 2
        newEditOp.editOperationSpecifier = tok[0];
        getEditOpData(tok, &newEditOp);
    }
    else // lineSpecifier type == none
    {
        // Get the type and data for lineSpecifier
        newEditOp.lineSpecifier.type = none;

        // Get the operation specifier and data
        newEditOp.editOperationSpecifier = tok[0];
        getEditOpData(tok, &newEditOp);
    }

    // Add the new editOp to the array that stores them
    editOperations[(*numEditOps)] = newEditOp;
    (*numEditOps)++;
}

// Gets the data based on the edit operation
void getEditOpData(char *restOfLine, EditOperation *currentEditOp)
{
    // if op is i, a, o, ignore string 2 | if op is d ignore string 1 and 2 | if op is s store str1 & 2
    switch (currentEditOp->editOperationSpecifier)
    {
    case 'A':
    case 'I':
    case 'O':
        memset(currentEditOp->editOperationData1, 0, sizeof(currentEditOp->editOperationData1));
        strncpy(currentEditOp->editOperationData1, restOfLine + 1, strlen(restOfLine) - 2);
        break;
    case 'd':
        break;
    case 's':
        restOfLine = strtok(NULL, "/"); // tok 3
        strcpy(currentEditOp->editOperationData1, restOfLine);
        restOfLine = strtok(NULL, "/"); // tok 4
        strcpy(currentEditOp->editOperationData2, restOfLine);
        break;
    default:
        break;
    }
}

//------------------------------ Execute Edit Operations functions ------------------------------------
// Checks if x is between start and end
int isBetween(int *x, int *start, int *end)
{
    if (*x >= *start && *x <= *end)
        return 1;
    return 0;
}

// Executes the edit operation based on it's specifier
void executeEditOperation(EditOperation *currentEditOp, char currentInputLine[], int *isDeleted)
{
    int len;
    switch (currentEditOp->editOperationSpecifier)
    {
    case 'A': // Appends the <text> at the end of the line.
        strncat(currentInputLine, currentEditOp->editOperationData1, TOTAL_STRING_MAX - 1);
        break;
    case 'I': // Inserts the <text> at the start of the line.
        len = strlen(currentEditOp->editOperationData1);
        memmove(currentInputLine + len, currentInputLine, strlen(currentInputLine) + 1);
        memcpy(currentInputLine, currentEditOp->editOperationData1, len);
        break;
    case 'O': // Inserts the <text> in a new line before the current line.
        printf("%s\n", currentEditOp->editOperationData1);
        break;
    case 'd': // Deletes the line from the file.
        *isDeleted = 1;
        break;
    case 's': // Replaces the first occurence of <old text>, in the line, with <new text>.
        replaceString(currentInputLine, TOTAL_STRING_MAX, currentEditOp->editOperationData1,
                      currentEditOp->editOperationData2);
        break;
    default:
        break;
    }
}

// Takes in a string "source" and replaces a substring within it
void replaceString(char *source, size_t sourceSize, char *replace, char *with)
{
    char *substringInSource = strstr(source, replace);
    if (substringInSource == NULL)
    {
        return;
    }

    // Make sure buffer overlow doesn't happen (allocate enough memory for a bigger word)
    if (sourceSize < strlen(source) + (strlen(with) - strlen(replace)) + 1)
    {
        printf("Buffer size is too small\n");
        return;
    }

    memmove(substringInSource + strlen(with), substringInSource + strlen(replace),
            strlen(substringInSource) - strlen(replace) + 1);

    memcpy(substringInSource, with, strlen(with));
}