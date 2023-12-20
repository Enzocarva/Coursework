#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#define MAX_TAXES 50000.0

void inputs(float *income, float *expenses);
void computeTaxableIncome(float *income, float *expenses, float *taxableIncome);
void getTaxGroup(float *taxableIncome, char *taxGroup);
void computeTax(float *taxAmount, char *taxGroup, float *taxableIncome);
void printResults(float *income, float *expenses, float *taxableIncome, char *taxGroup, float *taxAmount);

int main(void)
{
    // Get income and expenses through input
    float income, expenses;
    inputs(&income, &expenses);

    // Compute taxable income
    float taxableIncome;
    computeTaxableIncome(&income, &expenses, &taxableIncome);

    // Choose tax group
    char taxGroup;
    getTaxGroup(&taxableIncome, &taxGroup);

    // Compute tax
    float taxAmount;
    computeTax(&taxAmount, &taxGroup, &taxableIncome);

    // Print results
    printResults(&income, &expenses, &taxableIncome, &taxGroup, &taxAmount);
}

void inputs(float *income, float *expenses)
{
    int amountEntered;

    do
    {
        printf("Enter next amount : ");
        scanf("%d", &amountEntered);
        if (amountEntered > 0)
        {
            *income += amountEntered;
        }
        else
        {
            // amountEntered = abs(amountEntered);
            *expenses += (abs(amountEntered));
        }
    } while (amountEntered != 0);
}

void computeTaxableIncome(float *income, float *expenses, float *taxableIncome)
{
    *income > *expenses ? (*taxableIncome = (*income - *expenses)) : (*taxableIncome = 0.0);
}

void getTaxGroup(float *taxableIncome, char *taxGroup)
{
    if (*taxableIncome >= 500000)
    {
        *taxGroup = 'S';
    }
    else if (*taxableIncome >= 200000)
    {
        *taxGroup = 'Q';
    }
    else if (*taxableIncome >= 100000)
    {
        *taxGroup = 'M';
    }
    else if (*taxableIncome >= 50000)
    {
        *taxGroup = 'A';
    }
    else if (*taxableIncome >= 20000)
    {
        *taxGroup = 'R';
    }
    else
    {
        *taxGroup = 'P';
    }
}

void computeTax(float *taxAmount, char *taxGroup, float *taxableIncome)
{
    if (*taxGroup == 'S' || *taxGroup == 'Q')
    {
        *taxAmount = *taxableIncome * 0.25;
        if (*taxAmount >= MAX_TAXES)
        {
            *taxAmount = MAX_TAXES;
        }
    }
    else if (*taxGroup == 'M')
    {
        *taxAmount = *taxableIncome * 0.10;
    }
    else if (*taxGroup == 'A' || *taxGroup == 'R')
    {
        *taxAmount = *taxableIncome * 0.03;
    }
    else
    {
        *taxAmount = 0.0;
    }
}

void printResults(float *income, float *expenses, float *taxableIncome, char *taxGroup, float *taxAmount)
{
    printf("Income         = $ %.2f\n", *income);
    printf("Deductions     = $ %9.2f\n", *expenses);
    printf("Taxable income = $ %.2f\n", *taxableIncome);
    printf("Tax group      = %c\n", *taxGroup);
    printf("Tax owed       = $ %.2f\n", *taxAmount);
}