#include "helpers.h"
#include <math.h>

// Convert image to grayscale
void grayscale(int height, int width, RGBTRIPLE image[height][width])
{
    // Go through each pixel in "image" with this nested for-loop
    for (int i = 0; i < height; i++)
    {
        for (int j = 0; j < width; j++)
        {
            float average = (image[i][j].rgbtRed + image[i][j].rgbtGreen + image[i][j].rgbtBlue) / 3.0;
            average = round(average);
            image[i][j].rgbtRed = average;
            image[i][j].rgbtGreen = average;
            image[i][j].rgbtBlue = average;
        }
    }
    return;
}

// Reflect image horizontally
void reflect(int height, int width, RGBTRIPLE image[height][width])
{
    for (int i = 0; i < height; i++)
    {
        for (int j = 0; j < width / 2; j++)
        {
            RGBTRIPLE temp = image[i][j];
            image[i][j] = image[i][width - (j + 1)];
            image[i][width - (j + 1)] = temp;
        }
    }
    return;
}

// Blur image
void blur(int height, int width, RGBTRIPLE image[height][width])
{
    RGBTRIPLE temp[height][width];

    // Make a temporary copy of the image
    for (int i = 0; i < height; i++)
    {
        for (int j = 0; j < width; j++)
        {
            temp[i][j] = image[i][j];
        }
    }

    // Access all the pixels on the actual image
    for (int i = 0; i < height; i++)
    {
        for (int j = 0; j < width; j++)
        {
            // Declare total variables of each color and initialize them to 0 for every pixel
            int total_red, total_green, total_blue;
            total_red = total_green = total_blue = 0;
            float pixel_count = 0.0;

            // Access all the pixels that are on row or column away (9x9 grid)
            for (int row = - 1; row < 2; row++)
            {
                for (int col = - 1; col < 2; col++)
                {
                    int current_row = i + row;
                    int current_col = j + col;

                    // Check if pixels are valid (not outside of image)
                    if (current_row < 0 || current_row >= height || current_col < 0 || current_col >= width)
                    {
                        continue;
                    }

                    // Get total values of each color for all pixels scanned
                    total_red += image[current_row][current_col].rgbtRed;
                    total_green += image[current_row][current_col].rgbtGreen;
                    total_blue += image[current_row][current_col].rgbtBlue;

                    // Count every valid pixel scanned to then use it to divide by this number to get average
                    pixel_count++;
                }
            }
            // Calculate the average of each color for all pixels that it scanned through, rounding no nearest integer
            temp[i][j].rgbtRed = round(total_red / pixel_count);
            temp[i][j].rgbtGreen = round(total_green / pixel_count);
            temp[i][j].rgbtBlue = round(total_blue / pixel_count);
        }
    }

    // Copy blurred pixels into original image
    for (int i = 0; i < height; i++)
    {
        for (int j = 0; j < width; j++)
        {
            image[i][j].rgbtRed = temp[i][j].rgbtRed;
            image[i][j].rgbtGreen = temp[i][j].rgbtGreen;
            image[i][j].rgbtBlue = temp[i][j].rgbtBlue;
        }
    }
    return;
}

// Detect edges
void edges(int height, int width, RGBTRIPLE image[height][width])
{
    // Make a temporary copy of the image
    RGBTRIPLE temp[height][width];
    for (int i = 0; i < height; i++)
    {
        for (int j = 0; j < width; j++)
        {
            temp[i][j] = image[i][j];
        }
    }

    // Gx and Gy matrix values (each bracket is one row)
    int gx_matrix[3][3] = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
    int gy_matrix[3][3] = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};

    // Access all the pixels on the image
    for (int i = 0; i < height; i++)
    {
        for (int j = 0; j < width; j++)
        {
            // Initialize all total grid color values for every pixel (Gx and Gy and the final values)
            int gx_red, gx_green, gx_blue, gy_red, gy_green, gy_blue;
            gx_red = gx_green = gx_blue = gy_red = gy_green = gy_blue = 0;

            // Access all pixels one row or column away from current pixel (9x9 grid)
            for (int row = 0; row < 3; row++)
            {
                for (int col = 0; col < 3; col++)
                {
                    int current_row = i - 1 + row;
                    int current_col = j - 1 + col;

                    // Check if pixels are outside of image, if so turn them entirely black
                    if (current_row < 0 || current_row >= height || current_col < 0 || current_col >= width)
                    {
                        continue;
                    }

                    // Calculate total of each color for every pixel in 9x9 grid
                    gx_red += image[current_row][current_col].rgbtRed * gx_matrix[row][col];
                    gx_green += image[current_row][current_col].rgbtGreen * gx_matrix[row][col];
                    gx_blue += image[current_row][current_col].rgbtBlue * gx_matrix[row][col];

                    gy_red += image[current_row][current_col].rgbtRed * gy_matrix[row][col];
                    gy_green += image[current_row][current_col].rgbtGreen * gy_matrix[row][col];
                    gy_blue += image[current_row][current_col].rgbtBlue * gy_matrix[row][col];
                }
            }
            // Combine Gx and Gy values into one with Sobel algorithm and apply it to temp's pixels
            int final_red = round(sqrt(pow(gx_red, 2.0) + pow(gy_red, 2.0)));
            int final_green = round(sqrt(pow(gx_green, 2.0) + pow(gy_green, 2.0)));
            int final_blue = round(sqrt(pow(gx_blue, 2.0) + pow(gy_blue, 2.0)));

            if (final_red > 255)
            {
                final_red = 255;
            }
            if (final_green > 255)
            {
                final_green = 255;
            }
            if (final_blue > 255)
            {
                final_blue = 255;
            }

            temp[i][j].rgbtRed = final_red;
            temp[i][j].rgbtGreen = final_green;
            temp[i][j].rgbtBlue = final_blue;
        }
    }

    // Copy altered pixels from temp into original image
    for (int i = 0; i < height; i++)
    {
        for (int j = 0; j < width; j++)
        {
            image[i][j].rgbtRed = temp[i][j].rgbtRed;
            image[i][j].rgbtGreen = temp[i][j].rgbtGreen;
            image[i][j].rgbtBlue = temp[i][j].rgbtBlue;
        }
    }
    return;
}
