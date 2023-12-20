#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAXIMAL_PEOPLE 10
#define MAXIMAL_STRING_LENGTH 128

/*----Define data types */
typedef char string[MAXIMAL_STRING_LENGTH];
typedef struct personTag
{
    string name;
    int age;
    struct personTag *bestFriend;
} personType;
typedef personType *personPointer;
typedef personPointer peoplePointers[MAXIMAL_PEOPLE];

int main(void)
{
    peoplePointers peopleArray;
    int count = 0;

    // Add name and age of people
    while (count < MAXIMAL_PEOPLE)
    {
        personPointer currentPerson = (personType *)malloc(sizeof(personPointer));
        printf("Please enter person name, \"exit\" to exit : ");
        scanf("%s", currentPerson->name);
        if (strcasecmp(currentPerson->name, "exit") == 0)
        {
            break;
        }
        printf("Please enter %s's age : ", currentPerson->name);
        scanf("%d", &currentPerson->age);
        peopleArray[count] = currentPerson;
        currentPerson->bestFriend = NULL;
        count++; // number of people added
    }

    // Add people's best friend
    for (int i = 0; i < count; i++)
    {
        string tmpBFName;
        printf("Who is %s's best friend? ", peopleArray[i]->name);
        scanf("%s", tmpBFName);
        for (int j = 0; j < count; j++)
        {
            // if bestfriend's name exists in array
            if (strcasecmp(peopleArray[j]->name, tmpBFName) == 0)
            {
                // make person asked about above's bestfriend, the name inserted
                peopleArray[i]->bestFriend = peopleArray[j];
                break;
            }
        }
    }

    // print output
    for (int i = 0; i < count; i++)
    {
        printf("%-20s is %d, and his/her best friend is %s\n", peopleArray[i]->name, peopleArray[i]->age,
               peopleArray[i]->bestFriend->name);
    }

    // Free people
    for (int i = 0; i < count; i++)
    {
        free(peopleArray[i]);
    }

    return (EXIT_SUCCESS);
}
