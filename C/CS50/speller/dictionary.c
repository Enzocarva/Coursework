// Implements a dictionary's functionality

// hash function credits go to Dr. Victor Milenkovic, for showing me this in class at University of Miami.

#include <ctype.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <strings.h>

#include "dictionary.h"

// Represents a node in a hash table
typedef struct node
{
    char word[LENGTH + 1];
    struct node *next;
}
node;

// TODO: Choose number of buckets in hash table
const unsigned int N = 1000;

// Variable delcarations for number of words in the dictionary and hash index
unsigned int wordCount = 0;
unsigned int hashIndex = 0;

// Hash table
node *table[N];

// Returns true if word is in dictionary, else false
bool check(const char *word)
{
    // TODO
    hashIndex = hash(word);
    node *pointer = table[hashIndex];

    while (pointer != NULL)
    {
        if (strcasecmp(word, pointer->word) == 0)
        {
            return true;
        }
        pointer = pointer->next;
    }
    return false;
}

// Hashes word to a number
unsigned int hash(const char *word)
{
    // TODO: Improve this hash function
    unsigned long index = 0;
    int length = strlen(word);
    for (int i = 0; i < length; i++)
    {
        char c = tolower(word[i]);
        index = 31 * index + c;
    }
    return index % N;
}

// Loads dictionary into memory, returning true if successful, else false
bool load(const char *dictionary)
{
    // TODO
    // Open dictionary file and check if pointer returned by fopen is NULL
    FILE *dict = fopen(dictionary, "r");
    if (dict == NULL)
    {
        printf("could not open %s.\n", dictionary);
        return false;
    }

    // Read strings from dictionary file one at a time, storing them on tempWord before copying them over
    char tempWord[LENGTH + 1];
    while (fscanf(dict, "%s", tempWord) != EOF)
    {
        node *new = malloc(sizeof(node));
        if (new == NULL)
        {
            printf("Could not allocate memory for node.");
            return false;
        }

        // Copy word into node that will go in hash table
        strcpy(new->word, tempWord);
        new->next = NULL;

        hashIndex = hash(tempWord);
        new->next = table[hashIndex];
        table[hashIndex] = new;
        wordCount++;
    }

    // Check if there were any errors reading the file
    if (ferror(dict))
    {
        fclose(dict);
        printf("Error reading dictionary file\n");
        return false;
    }

    // Close the dictionary file and return successful (true)
    fclose(dict);
    return true;
}

// Returns number of words in dictionary if loaded, else 0 if not yet loaded
unsigned int size(void)
{
    // TODO
    if (wordCount > 0)
    {
        return wordCount;
    }
    return 0;
}

// Unloads dictionary from memory, returning true if successful, else false
bool unload(void)
{
    // TODO
    for (int i = 0; i < N; i++)
    {
        node *pointer = table[i];
        node *tmp = table[i];
        while (pointer != NULL)
        {
            tmp = pointer;
            pointer = pointer->next;
            free(tmp);
        }
    }
    return true;
}
