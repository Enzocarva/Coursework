#include <stdio.h>
#include <cs50.h>

int main(void)
{
    //asks user for name and prints it
    string name = get_string("Wha's your name? ");
    printf("hello, %s\n", name);
}