//======================================================================================================================
package prog02;

import java.io.*;
import java.util.Scanner;

/**
 * This is an implementation of PhoneDirectory that uses a sorted
 * array to store the entries.
 *
 * @author 
 */
public class SortedPD extends ArrayBasedPD {
//======================================================================================================================
    /** Remove an entry from the directory.
     @param index The index in theDirectory of the entry to remove.
     @return The DirectoryEntry that was just removed.
     */
    protected DirectoryEntry remove (int index) {
        DirectoryEntry entry = theDirectory[index];
        for (int i = index; i < size - 1; i++) {
            theDirectory[i] = theDirectory[i + 1];
        }
        size--;
        return entry;
    }
//----------------------------------------------------------------------------------------------------------------------
    /** Add an entry to the directory.
     @param index The index at which to add the entry to theDirectory.
     @param newEntry The new entry to add.
     @return The DirectoryEntry that was just added.
     */
    protected DirectoryEntry add (int index, DirectoryEntry newEntry) {
        if (size == theDirectory.length)
            reallocate();
        for (int j = size; j > index; j--) {
           theDirectory[j] = theDirectory[j - 1];
        }
        theDirectory[index] = newEntry;
        size++;
        return newEntry;
    }
//----------------------------------------------------------------------------------------------------------------------
    /** Determine if name is located at index.
     @param index The index to be checked.
     @param name The name that might be located at that index.
     @return true if a DirectoryEntry with that name is located at
     that index.
     */
    protected boolean found (int index, String name) {
        if (index < size)
            if (!theDirectory[index].getName().equals(name))
                return false;

        return index < size;
    }
//----------------------------------------------------------------------------------------------------------------------
    /** Find an entry in the directory.
     @param name The name to be found
     @return The index of the entry with that name or, if it is not
     there, the index where it should be added.
     */
    protected int find (String name) {
        int low = 0;
        int high = size;
        int middle;

        while (low < high) {
            middle = (low + high) / 2;
            String temp = theDirectory[middle].getName();
            //int compare = name.compareTo(theDirectory[middle].getName());
            if (temp.compareTo(name) >= 0)
                high = middle;
            else
              low = middle + 1;
        }

        return high;
    }
//----------------------------------------------------------------------------------------------------------------------
}
//======================================================================================================================
