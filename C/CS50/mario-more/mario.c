#include <cs50.h>
#include <stdio.h>

//methtods declared for latre usage
int get_height(void);
void print_pyramid(int height);

int main(void)
{
    int height = get_height();
    print_pyramid(height);
}

int get_height(void)
{
    int height;

    //prompt user for height checking for errors
    do
    {
        height = get_int("Height: ");
    }
    while (height < 1 || height > 8);
    //check for invalid inputs

    return height;
}

void print_pyramid(height)
{
    //pyramid rows
    for (int i = 0; i < height; i++)
    {
        //pyramid columns
        for (int j = 0; j < height + i + 3; j++)
        {
            //cases for when to pirnt a hash and when to print a blank space
            if (j == height || j == height + 1 || i + j < height - 1)
            {
                printf(" ");
            }
            else
            {
                printf("#");
            }
        }
        printf("\n");
    }
}