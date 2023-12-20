#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#define FEET_PER_ATMOSPHERE 33

int main(void)
{
    // Variables
    float depth, percentageO2, ambientPressure, pressureO2;
    char groupO2;
    bool maximal_O2_pressure, contingency_O2_pressure;

    // Get inputs
    printf("Enter depth and percentage O2   : ");
    scanf("%f %f", &depth, &percentageO2);

    // Calculations
    ambientPressure = depth / FEET_PER_ATMOSPHERE + 1;
    pressureO2 = percentageO2 / 100.0 * ambientPressure;
    groupO2 = (char)((int)(pressureO2 * 10) + ('A'));
    maximal_O2_pressure = pressureO2 > 1.4 ? true : false;
    contingency_O2_pressure = pressureO2 > 1.6 ? true : false;

    // Print results
    printf("\nAmbient pressure                : %.1f\n", ambientPressure);
    printf("O2 pressure                     : %.2f\n", pressureO2);
    printf("O2 group                        : %c\n\n", groupO2);
    printf("Exceeds maximal O2 pressure     : %s\n", maximal_O2_pressure ? "true" : "false");
    printf("Exceeds contingency O2 pressure : %s\n", contingency_O2_pressure ? "true" : "false");

    return (EXIT_SUCCESS);
}