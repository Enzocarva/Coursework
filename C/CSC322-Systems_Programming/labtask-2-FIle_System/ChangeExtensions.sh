# Change the extension of a file from .txt to .text for example. Usage: ./ChangeExtensions txt text

#!/bin/tcsh

# If there are less than two arguments, exit with an error message
if ($#argv < 2 ) then
    echo "Must input 2 aruments, original extension and extension to change to."
    exit 1
endif

# Check if files with the given extension exist
if (`find *.$1 | wc -l` < 1) then
	echo "No files of this type were found"
    exit
endif

# "Grab" each file with the given extension and change their extension with mv command
foreach File (*.$1)
	set name = `basename $File .$1`
	mv $File $name.$2
	echo "$File is now $name.$2"
end
