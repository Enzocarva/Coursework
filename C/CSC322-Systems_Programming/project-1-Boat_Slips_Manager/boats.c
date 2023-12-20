#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <math.h>

#define MAX_STRING 127
#define MAX_BOATS 120
#define SLIP_$_MONTH_PER_FOOT 12.5
#define LAND_$_MONTH_PER_FOOT 14.0
#define TRAILOR_$_MONTH_PER_FOOT 25.0
#define STORAGE_$_MONTH_PER_FOOT 11.20

//------------------------------------------ Typedefs -------------------------------------------------
// enum for type of place for the boats
typedef enum
{
    slip,
    land,
    trailor,
    storage,
    no_place
} PlaceType;

typedef char String[MAX_STRING];

// Union for extra information based on the place
typedef union
{
    unsigned int slipNumber;
    char bayLetter;
    String trailor;
    unsigned int storageNumber;
} ExtraInfo;

// Boat datatype
typedef struct
{
    String name;
    unsigned int feet;
    PlaceType place;
    ExtraInfo extra;
    float moneyOwed;
} Boat;

//--------------------------------------------- Function Prototypes -----------------------------------
void inventory(Boat *boatFleet[], int *count);
void add(Boat *boatFleet[], char *line, int *count);
void removeBoat(Boat *boatFleet[], int *count, char *name);
void payment(Boat *boatFleet[], int *count, char *name);
void month(Boat *boatFleet[], int *count);
void sortBoats(Boat *boatFleet[], int *count);
PlaceType StringToPlaceType(char *PlaceString);
char *PlaceToString(PlaceType Place);

//------------------------------------------------- Main ----------------------------------------------
int main(int arc, char *argv[])
{
    Boat *boatFleet[MAX_BOATS];
    int count = 0;
    char menuOption;
    char csvLine[1024];

    // Check command args, open CSV file and load the data from it, read it into the boatFleet array
    if (arc != 2)
    {
        printf("Usage: ./boats BoatData.csv\n");
        return (EXIT_FAILURE);
    }
    FILE *inputFile = fopen(argv[1], "r");
    if (inputFile == NULL)
    {
        printf("Could not open input CSV file\n");
        return (EXIT_FAILURE);
    }
    while (fgets(csvLine, 1024, inputFile))
    {
        char *tmpLine = strdup(csvLine); // Not necessary
        add(boatFleet, tmpLine, &count);
        free(tmpLine);
    }
    // Close the file after reading
    fclose(inputFile);

    // Welcome to the program
    printf("Welcome to the Boat Management system\n---------------------------------------\n");

    // Loop for menu options
    while (count < MAX_BOATS && toupper(menuOption) != 'X')
    {
        printf("\n(I)nventory, (A)dd, (R)emove, (P)ayment, (M)onth, e(X)it : ");
        scanf(" %c", &menuOption);

        switch (toupper(menuOption))
        {
        case 'I':
            inventory(boatFleet, &count);
            break;
        case 'A':
            printf("Please enter the boat data in CSV format                 : ");
            scanf("%s", csvLine);
            add(boatFleet, csvLine, &count);
            break;
        case 'R':
            printf("Please enter the boat name                               : ");
            scanf(" %[^\n]s", csvLine); // Allows space character in input
            removeBoat(boatFleet, &count, csvLine);
            break;
        case 'P':
            printf("Please enter the boat name                               : ");
            scanf(" %[^\n]s", csvLine);
            payment(boatFleet, &count, csvLine);
            break;
        case 'M':
            month(boatFleet, &count);
            break;
        case 'X':
            printf("Exiting the Boat Management System\n");

            // Open file for writing into .csv
            inputFile = fopen(argv[1], "w");
            if (inputFile == NULL)
            {
                printf("Could not open input CSV file\n");
                return (EXIT_FAILURE);
            }

            // Loop over all boat entries in the "fleet" and write them out to the csv
            for (int i = 0; i < count; i++)
            {
                fprintf(inputFile, "%s,%d,%s,", boatFleet[i]->name, boatFleet[i]->feet,
                        PlaceToString(boatFleet[i]->place));

                switch (boatFleet[i]->place)
                {
                case slip:
                    fprintf(inputFile, "%d,", boatFleet[i]->extra.slipNumber);
                    break;
                case land:
                    fprintf(inputFile, "%c,", boatFleet[i]->extra.bayLetter);
                    break;
                case trailor:
                    fprintf(inputFile, "%s,", boatFleet[i]->extra.trailor);
                    break;
                case storage:
                    fprintf(inputFile, "%d,", boatFleet[i]->extra.storageNumber);
                    break;
                case no_place:
                    fprintf(inputFile, "No place,");
                    break;
                default:
                    printf("\nError in exit menu option\n");
                    exit(EXIT_FAILURE);
                    break;
                }

                fprintf(inputFile, "%.2f\n", boatFleet[i]->moneyOwed);

                // Free all boats from memory
                free(boatFleet[i]);
            }

            // Close the file after writing to it
            fclose(inputFile);
            break;
        default:
            printf("Invalid option %c\n", menuOption);
            break;
        }
    }
}

//---------------------------------------------- Menu option functions --------------------------------
//---- Print the inventory sorted alphabetically
void inventory(Boat *boatFleet[], int *count)
{
    // Sort the boats alphabetically by name
    sortBoats(boatFleet, count);

    // Print the boats information
    for (int i = 0; i < *count; i++)
    {
        if (boatFleet[i] == NULL)
        {
            continue;
        }

        printf("%-20s %2d' %8s ", boatFleet[i]->name, boatFleet[i]->feet,
               PlaceToString(boatFleet[i]->place));

        switch (boatFleet[i]->place)
        {
        case slip:
            printf("  # %2d", boatFleet[i]->extra.slipNumber);
            break;
        case land:
            printf("%6c", boatFleet[i]->extra.bayLetter);
            break;
        case trailor:
            printf("%6s", boatFleet[i]->extra.trailor);
            break;
        case storage:
            printf("%6d", boatFleet[i]->extra.storageNumber);
            break;
        case no_place:
            printf("No place");
            break;
        default:
            printf("\nError in inventory()\n");
            exit(EXIT_FAILURE);
            break;
        }

        printf("   Owes $%7.2f\n", boatFleet[i]->moneyOwed);
    }
}

//---- Add boats to the fleet (array)
void add(Boat *boatFleet[], char *line, int *count)
{
    Boat *newBoat;
    newBoat = (Boat *)malloc(sizeof(Boat));
    if (newBoat == NULL)
    {
        printf("Could not allocate memory for new boat\n");
        return;
    }

    // Add the boats name
    char *tok;
    tok = strtok(line, ",");
    strncpy(newBoat->name, tok, MAX_STRING); // Will only copy a name up to MAX_STRING (127)

    // Add the feet size
    tok = strtok(NULL, ",");
    newBoat->feet = atoi(tok);

    // Add the place
    tok = strtok(NULL, ",");
    newBoat->place = StringToPlaceType(tok);

    // Add the extra information
    tok = strtok(NULL, ",");
    switch (newBoat->place)
    {
    case slip:
        newBoat->extra.slipNumber = atoi(tok);
        break;
    case land:
        newBoat->extra.bayLetter = toupper(tok[0]);
        break;
    case trailor:
        strcpy(newBoat->extra.trailor, tok);
        break;
    case storage:
        newBoat->extra.storageNumber = atoi(tok);
        break;
    case no_place:
        newBoat->extra.bayLetter = ' ';
        break;
    default:
        printf("Error adding extra info to boat in add()\n");
        exit(EXIT_FAILURE);
        break;
    }

    // Add the money owed
    tok = strtok(NULL, ",");
    newBoat->moneyOwed = atof(tok);

    // Add the new boat to the array
    boatFleet[*count] = newBoat;
    (*count)++;
}

//---- Remove boats from the fleet (array)
void removeBoat(Boat *boatFleet[], int *count, char *name)
{
    int lastIndex = *count - 1;

    for (int i = 0; i < *count; i++)
    {
        if (!strcasecmp(boatFleet[i]->name, name)) // Match found
        {
            boatFleet[i] = boatFleet[lastIndex]; // Override removed boat with last boat
            boatFleet[lastIndex] = NULL;
            (*count)--;
            return;
        }
    }
    // Went through all boats and none matched
    printf("No boat with that name\n");
}

//---- Make a payment up to the amount owed
void payment(Boat *boatFleet[], int *count, char *name)
{
    float payment;
    for (int i = 0; i < *count; i++)
    {
        if (!strcasecmp(boatFleet[i]->name, name))
        {
            printf("Please enter amount to be paid                           : ");
            scanf("%f", &payment);

            if (boatFleet[i]->moneyOwed < payment) // payment is more than owed, return
            {
                printf("That is more than the amount owed, $%.2f\n", boatFleet[i]->moneyOwed);
                return;
            }

            // Make payment
            boatFleet[i]->moneyOwed -= payment;
            return;
        }
    }
    printf("No boat with that name\n");
}

//---- Add monthly costs (as if a month went by)
void month(Boat *boatFleet[], int *count)
{
    for (int i = 0; i < *count; i++)
    {
        switch (boatFleet[i]->place)
        {
        case slip:
            boatFleet[i]->moneyOwed += SLIP_$_MONTH_PER_FOOT * (float)boatFleet[i]->feet;
            break;
        case land:
            boatFleet[i]->moneyOwed += LAND_$_MONTH_PER_FOOT * (float)boatFleet[i]->feet;
            break;
        case trailor:
            boatFleet[i]->moneyOwed += TRAILOR_$_MONTH_PER_FOOT * (float)boatFleet[i]->feet;
            break;
        case storage:
            boatFleet[i]->moneyOwed += STORAGE_$_MONTH_PER_FOOT * (float)boatFleet[i]->feet;
            break;
        case no_place:
            break;
        default:
            printf("\nError in extraInfoToString()\n");
            exit(EXIT_FAILURE);
            break;
        }
    }
}
//---------------------------------------------- All Other functions ----------------------------------
//---- Compare pointers to sort based on them
int comparator(const void *p1, const void *p2)
{
    const char *name1 = (*((Boat **)p1))->name; // (*((Boat **)p1))->name double pointer to then
    const char *name2 = (*((Boat **)p2))->name; // dereference, just how qsort works
    return (strcasecmp(name1, name2));
}

//---- Quick Sort
void sortBoats(Boat *boatFleet[], int *count)
{
    qsort(boatFleet, *count, sizeof(Boat *), comparator);
}

//---- Convert a string to a place
PlaceType StringToPlaceType(char *PlaceString)
{
    if (!strcasecmp(PlaceString, "slip"))
    {
        return (slip);
    }
    if (!strcasecmp(PlaceString, "land"))
    {
        return (land);
    }
    if (!strcasecmp(PlaceString, "trailor"))
    {
        return (trailor);
    }
    if (!strcasecmp(PlaceString, "storage"))
    {
        return (storage);
    }
    return (no_place);
}

//---- Convert a place to a string
char *PlaceToString(PlaceType Place)
{
    switch (Place)
    {
    case slip:
        return ("slip");
    case land:
        return ("land");
    case trailor:
        return ("trailor");
    case storage:
        return ("storage");
    case no_place:
        return ("no_place");
    default:
        printf("Error in PlaceToString()\n");
        exit(EXIT_FAILURE);
        break;
    }
}
