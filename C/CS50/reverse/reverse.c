#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "wav.h"

int check_format(WAVHEADER header);
int get_block_size(WAVHEADER header);

int main(int argc, char *argv[])
{
    // Ensure proper usage
    // TODO #1
    if (argc != 3)
    {
        printf("Usage: ./reverse input.wav output.wav\n");
        return 1;
    }

    // Open input file for reading
    // TODO #2
    FILE *input = fopen(argv[1], "r");
    if (input == NULL)
    {
        printf("Could not open file\n");
        return 1;
    }

    // Read header
    // TODO #3
    WAVHEADER header;
    fread(&header, sizeof(WAVHEADER), 1, input);

    // Use check_format to ensure WAV format
    // TODO #4
    if (check_format(header) == 0)
    {
        printf("Not a .wav file, format not supported\n");
        return 1;
    }

    if (header.audioFormat != 1)
    {
        printf("Not a .wav file, format not supported\n");
        return 1;
    }

    // Open output file for writing
    // TODO #5
    FILE *output = fopen(argv[2], "w");
    if (output == NULL)
    {
        printf("Could not open file\n");
        return 1;
    }

    // Write header to file
    // TODO #6
    fwrite(&header, sizeof(WAVHEADER), 1, output);

    // Use get_block_size to calculate size of block
    // TODO #7
    int blockSize = get_block_size(header);

    // Write reversed audio to file
    // TODO #8
    if (fseek(input, blockSize, SEEK_END))
    {
        return 1;
    }
    BYTE buffer[blockSize];
    while (ftell(input) - blockSize > sizeof(header))
    {
        if (fseek(input, -2 * blockSize, SEEK_CUR))
        {
            return 1;
        }
        fread(buffer, blockSize, 1, input);
        fwrite(buffer, blockSize, 1, output);
    }

    // Close files
    fclose(input);
    fclose(output);
}

int check_format(WAVHEADER header)
{
    // TODO #4
    if (header.format[0] == 'W' && header.format[1] == 'A' &&
        header.format[2] == 'V' && header.format[3] == 'E')
    {
        return 1;
    }
    return 0;
}

int get_block_size(WAVHEADER header)
{
    // TODO #7
    return (header.bitsPerSample / 8) * header.numChannels;
}