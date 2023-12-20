#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

typedef int arrayIntegers[5];
typedef int *arrayOfIntPointers[5];

void initialization(arrayIntegers intArray, arrayOfIntPointers pointerArray)
{
    srand(getpid());
    for (int i = 0; i < 5; i++)
    {
        intArray[i] = rand() % 100; // Generate numbers between 0 and 99 to keep it simpler
        pointerArray[i] = &intArray[i];
    }
}

void printIntArray(arrayIntegers intArray)
{
    for (int i = 0; i < 5; i++)
        printf("%d : %d\n", i, intArray[i]);
}

void printIntPointersArray(arrayOfIntPointers pointerArray)
{
    for (int i = 0; i < 5; i++)
        printf("%d : %d\n", i, *pointerArray[i]);
}

void bubbleSortIntegerArray(arrayIntegers intArray)
{
    // int arrLength = sizeof(integers) / sizeof(integers[0]);
    int swapped, tmp;
    for (int i = 0; i < 5 - 1; i++)
    {
        swapped = 0;
        for (int j = 0; j < 5 - i - 1; j++)
        {
            if (intArray[j] > intArray[j + 1])
            {
                tmp = intArray[j];
                intArray[j] = intArray[j + 1];
                intArray[j + 1] = tmp;
                swapped = 1;
            }
        }
        if (swapped == 0)
            break;
    }
}

void bubbleSortIntPointerArray(arrayOfIntPointers pointerArray)
{
    // int arrLength = sizeof(intPointers) / sizeof(intPointers[0]);
    int swapped;
    int *tmp;
    for (int i = 0; i < 5 - 1; i++)
    {
        swapped = 0;
        for (int j = 0; j < 5 - i - 1; j++)
        {
            if (*pointerArray[j] > *pointerArray[j + 1])
            {
                tmp = pointerArray[j];
                pointerArray[j] = pointerArray[j + 1];
                pointerArray[j + 1] = tmp;
                swapped = 1;
            }
        }
        if (swapped == 0)
            break;
    }
}

int main(void)
{
    arrayIntegers intArray;
    arrayOfIntPointers pointerArray;

    initialization(intArray, pointerArray);
    printf("---- Initialized array of integers ----\n");
    printIntArray(intArray);

    bubbleSortIntPointerArray(pointerArray);
    printf("---- Sorted array of pointers ----\n");
    printIntPointersArray(pointerArray);

    bubbleSortIntegerArray(intArray);
    printf(" ---- Sorted array of integers ----\n");
    printIntArray(intArray);

    printf(" ---- Array of pointers ----\n");
    printIntPointersArray(pointerArray);
}