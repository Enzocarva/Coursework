#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define INITIAL_SIZE 1

// Declare the enum for the type of animal
typedef enum
{
    mammal,
    insect,
    bird,
    fish,
    error
} animalType;

// Species struct declaration
typedef struct
{
    char name[128];
    float size;
    animalType type;
} species;

// Function delcarations
animalType stringToEnum(void);
char *enumToString(animalType type);

int main(void)
{
    int length = INITIAL_SIZE;
    species *newSpeciesCatalog;
    int index = 0;

    // Declare the array with the initial size of 1
    newSpeciesCatalog = (species *)malloc(INITIAL_SIZE * sizeof(species));

    while (1)
    {
        species newSpecies;
        printf("Enter animal information (\"exit\" to exit)\nWhat is the name : ");
        scanf("%s", newSpecies.name);
        if (!strcasecmp(newSpecies.name, "exit"))
        {
            break;
        }
        printf("What is the size : ");
        scanf("%f", &newSpecies.size);
        newSpecies.type = stringToEnum();

        // Check if there is enough space to store new species, reallocate if not
        if (index == length)
        {
            length *= 2;
            newSpeciesCatalog = realloc(newSpeciesCatalog, length * sizeof(species));
        }
        newSpeciesCatalog[index++] = newSpecies;
    }

    // When loop finishes and all data is collected, print the results
    printf("\nThe following new species were found:\n");
    for (int i = 0; i < index; i++) // index is the number of elements added...
    {
        printf("%-20s has size %6.2f and is a %s\n", newSpeciesCatalog[i].name,
               newSpeciesCatalog[i].size, enumToString(newSpeciesCatalog[i].type));
    }

    // Free any allocated memory
    free(newSpeciesCatalog);

    return (EXIT_SUCCESS);
}

animalType stringToEnum(void)
{
    char answer[128];
    printf("What is the type : ");
    scanf("%s", answer);

    if (!strcasecmp(answer, "mammal"))
    {
        return mammal;
    }
    if (!strcasecmp(answer, "insect"))
    {
        return insect;
    }
    if (!strcasecmp(answer, "bird"))
    {
        return bird;
    }
    if (!strcasecmp(answer, "fish"))
    {
        return fish;
    }

    return error;
}

char *enumToString(animalType type)
{
    switch (type)
    {
    case mammal:
        return ("mammal");
        break;
    case insect:
        return ("insect");
        break;
    case bird:
        return ("bird");
        break;
    case fish:
        return ("fish");
        break;
    default:
        return "error";
        break;
    }
}