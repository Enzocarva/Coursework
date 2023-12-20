#include <stdio.h>
#include <stdlib.h>

double *input_matrix(int *rows, int *columns, char *name);
int matrix_multiply(double *m1, int m1Rows, int m1Columns, double *m2, int m2Rows, int m2Columns, double *m3);
void output_matrix(double *matrix, int rows, int columns);

int main(void)
{

    double *m1, *m2, *m3;
    int m1_rows, m1_columns, m2_rows, m2_columns;

    if (((m1 = input_matrix(&m1_rows, &m1_columns, "Matrix 1")) != NULL) &&
        ((m2 = input_matrix(&m2_rows, &m2_columns, "Matrix 2")) != NULL) &&
        ((m3 = malloc(m1_rows * m2_columns * sizeof(double))) != NULL))
    {
        printf("Matrix 1\n");
        output_matrix(m1, m1_rows, m1_columns);
        printf("Matrix 2\n");
        output_matrix(m2, m2_rows, m2_columns);
        if (matrix_multiply(m1, m1_rows, m1_columns, m2, m2_rows, m2_columns, m3))
        {
            printf("Product\n");
            output_matrix(m3, m1_rows, m2_columns);
            free(m1);
            free(m2);
            free(m3);
            return (0);
        }
        else
        {
            printf("Error in dimensions\n");
            free(m1);
            free(m2);
            free(m3);
            return (-1);
        }
    }
    else
    {
        free(m1);
        free(m2);
        free(m3);
        printf("Error allocating memory\n");
        return (-2);
    }
}

double *input_matrix(int *rows, int *columns, char *name)
{
    // Get the dimensions of the matrix from the user
    printf("Input the number of rows for %s    : ", name);
    scanf("%d", rows);
    printf("Input the number of columns for %s : ", name);
    scanf("%d", columns);

    // Verify the input
    if (*rows <= 0 || *columns <= 0)
    {
        printf("Invalid dimensions\n");
        return NULL;
    }

    double *matrix = malloc(*rows * *columns * sizeof(double));
    if (matrix == NULL)
    {
        printf("Error while trying to allocate memory for %s\n", name);
        return NULL;
    }

    // Make the matrix and allocate heap memory for it
    double value = 1;
    for (int i = 0; i < *rows; i++)
    {
        for (int j = 0; j < *columns; j++)
        {
            matrix[i * (*columns) + j] = value++;
        }
    }
    return matrix;
}

int matrix_multiply(double *m1, int m1Rows, int m1Columns, double *m2, int m2Rows, int m2Columns, double *m3)
{
    // Check if the matrices dimensions are valid for multiplication
    if (m1Columns != m2Rows)
    {
        return 0; // Invalid dimensions for multiplication
    }

    for (int i = 0; i < m1Rows; i++)
    {
        for (int j = 0; j < m2Columns; j++)
        {
            m3[i * m2Columns + j] = 0;
            for (int k = 0; k < m1Columns; k++) // can be k < m2Rows as well because they should be equal
            {
                m3[i * m2Columns + j] += m1[i * m1Columns + k] * m2[k * m2Columns + j];
            }
        }
    }

    return 1;
}

void output_matrix(double *matrix, int rows, int columns)
{
    for (int i = 0; i < rows; i++)
    {
        for (int j = 0; j < columns; j++)
        {
            printf("%lf ", matrix[i * columns + j]);
        }
        printf("\n");
    }
}