#include <cs50.h>
#include <stdio.h>
#include <string.h>

// Max number of candidates
#define MAX 9

// preferences[i][j] is number of voters who prefer i over j
int preferences[MAX][MAX];

// locked[i][j] means i is locked in over j
bool locked[MAX][MAX];

// Each pair has a winner, loser
typedef struct
{
    int winner;
    int loser;
}
pair;

// Array of candidates
string candidates[MAX];
pair pairs[MAX * (MAX - 1) / 2];

int pair_count;
int candidate_count;

// Function prototypes
bool vote(int rank, string name, int ranks[]);
void record_preferences(int ranks[]);
void add_pairs(void);
void sort_pairs(void);
void lock_pairs(void);
void print_winner(void);

bool has_cycle(int winner, int loser)
{
    while (winner != -1 && winner != loser)
    {
        bool found = false;
        for (int i = 0; i < candidate_count; i++)
        {
            if (locked[i][winner])
            {
                found = true;
                winner = i;
            }
        }
        // Break out of while loop and return false, No cycle case
        if (!found)
        {
            winner = -1;
        }
    }
    // Break out of loop and return true, Yes cycle case
    if (winner == loser)
    {
        return true;
    }
    return false;
}

int main(int argc, string argv[])
{
    // Check for invalid usage
    if (argc < 2)
    {
        printf("Usage: tideman [candidate ...]\n");
        return 1;
    }

    // Populate array of candidates
    candidate_count = argc - 1;
    if (candidate_count > MAX)
    {
        printf("Maximum number of candidates is %i\n", MAX);
        return 2;
    }
    for (int i = 0; i < candidate_count; i++)
    {
        candidates[i] = argv[i + 1];
    }

    // Clear graph of locked in pairs
    for (int i = 0; i < candidate_count; i++)
    {
        for (int j = 0; j < candidate_count; j++)
        {
            locked[i][j] = false;
        }
    }

    pair_count = 0;
    int voter_count = get_int("Number of voters: ");

    // Query for votes
    for (int i = 0; i < voter_count; i++)
    {
        // ranks[i] is voter's ith preference
        int ranks[candidate_count];

        // Query for each rank
        for (int j = 0; j < candidate_count; j++)
        {
            string name = get_string("Rank %i: ", j + 1);

            if (!vote(j, name, ranks))
            {
                printf("Invalid vote.\n");
                return 3;
            }
        }

        record_preferences(ranks);

        printf("\n");
    }

    add_pairs();
    sort_pairs();
    lock_pairs();
    print_winner();
    return 0;
}

// Update ranks given a new vote
bool vote(int rank, string name, int ranks[])
{
    // TODO
    // Check if candidate name is valid
    for (int i = 0; i < candidate_count; i++)
    {
        if (strcmp(candidates[i], name) == 0)
        {
            ranks[rank] = i;
            return true;
        }
    }
    return false;
}

// Update preferences given one voter's ranks
void record_preferences(int ranks[])
{
    // TODO
    for (int i = 0; i < candidate_count - 1; i++)
    {
        for (int j = i + 1; j < candidate_count; j++)
        {
            preferences[ranks[i]][ranks[j]]++;
        }
    }
    return;
}

// Record pairs of candidates where one is preferred over the other
void add_pairs(void)
{
    // TODO
    /*
    for (int i = 0; i < candidate_count; i++)
    {
        for (int j = 0; j < candidate_count; j++)
        {
            // Calculate margin of victory, positive cases
            if (preferences[i][j] - preferences[j][i] > 0)
            {
                pairs[pair_count].winner = i;
                pairs[pair_count].loser = j;
                pair_count++;
            }
            // Calculate margin of victory, negative cases
            else if (preferences[i][j] - preferences[j][i] < 0)
            {
                pairs[pair_count].winner = j;
                pairs[pair_count].loser = i;
                pair_count++;
            }
        }
    }
    // Divide by 2 because my loop goes over each pair twice instead of once, code could be better
    pair_count /= 2;
    */
    for (int row = 0; row < MAX; row++)
    {
        for (int col = row + 1; col < MAX; col++)
        {
            int candidateI = preferences[row][col];
            int candidateJ = preferences[col][row];
            if (candidateI != candidateJ)
            {
                pair p;
                if (candidateI > candidateJ)
                {
                    p.winner = row;
                    p.loser = col;
                }
                else
                {
                    p.winner = col;
                    p.loser = row;
                }
                pairs[pair_count++] = p;
            }
        }
    }
    return;
}

// Sort pairs in decreasing order by strength of victory
void sort_pairs(void)
{
    for (int i = 0; i < pair_count; i++)
    {
        // Get margin of victory for index i in the pairs[] array
        int marginI = preferences[pairs[i].winner][pairs[i].loser] - preferences[pairs[i].loser][pairs[i].winner];
        for (int j = i + 1; j < pair_count; j++)
        {
            // Get margin of victory for index j in the pairs[] array
            int marginJ = preferences[pairs[j].winner][pairs[j].loser] - preferences[pairs[j].loser][pairs[j].winner];
            if (marginI < marginJ)
            {
                // Selection sort, O(n^2), nested for loops
                pair temp = pairs[i];
                pairs[i] = pairs[j];
                pairs[j] = temp;
            }
        }
    }
    return;
}

// Lock pairs into the candidate graph in order, without creating cycles
void lock_pairs(void)
{
    // TODO
    for (int i = 0; i < pair_count; i++)
    {
        if (!has_cycle(pairs[i].winner, pairs[i].loser))
        {
            locked[pairs[i].winner][pairs[i].loser] = true;
        }
    }
}

// Print the winner of the election
void print_winner(void)
{
    // TODO
    for (int col = 0; col < pair_count; col++)
    {
        bool source = true;
        for (int row = 0; row < pair_count; row++)
        {
            if (locked[row][col] == true)
            {
                source = false;
                break;
            }
        }
        // Source will be true if there is not a single true in the locked array under the column that it's currently looking at (no one beats column)
        if (source == true)
        {
            printf("%s\n", candidates[col]);
            return;
        }
    }
    return;
}