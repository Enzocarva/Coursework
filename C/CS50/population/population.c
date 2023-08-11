#include <cs50.h>
#include <stdio.h>

int main(void)
{
    // TODO: Prompt for start size
    int start;
    do
    {
        start = get_int("Start size: ");
    }
    while (start < 9);

    // TODO: Prompt for end size
    int end;
    do
    {
        end = get_int("End size: ");
    }
    while (end < start);

    // TODO: Calculate number of years until we reach threshold
    int years = 0;
    int born = 0;
    int died = 0;

    while (start < end)
    {
        born = start / 3;
        died = start / 4;
        start = start + (born - died);
        years++;
    }

    // TODO: Print number of years
    printf("Years: %i\n", years);
}
