#include <stdio.h>
#define ROAD_WIDTH 0.01
#define KILL_CONSTANT 1.47

int main(void)
{
    int roadLength, numKangaroos;
    float squareSide;

    printf("Enter side of square in km  : ");
    scanf("%f", &squareSide);

    printf("Enter road's length in km   : ");
    scanf("%d", &roadLength);

    printf("Enter number of 'roos       : ");
    scanf("%d", &numKangaroos);

    float density = numKangaroos / (squareSide * squareSide);
    float roadSurfaceArea = roadLength * ROAD_WIDTH;
    float expectedKills = density * roadSurfaceArea * KILL_CONSTANT;

    printf("Expected number of kills is : %.1f\n", expectedKills);
}