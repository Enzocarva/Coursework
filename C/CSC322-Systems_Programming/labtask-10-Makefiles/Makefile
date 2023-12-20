#	Makefile for the NameAge program

CC = gcc
LD = $(CC)
DEPEND = mkdep

CFLAGS = -O2 -Wall

CFILES =\
	mystrings.c\
	Person.c\
	NameAge.c

HFILES =\
	boolean.h\
	mystrings.h\
	Person.h

OFILES = $(CFILES:%.c=%.o)

NameAge: $(OFILES)
	$(LD) -o $@ $(OFILES)

NameAge.o: NameAge.c boolean.h mystrings.h Person.h

Person.o: Person.c Person.h

depend:
	$(DEPEND) $(CFLAGS) $(CFILES)

clean:
	rm -f *.o *.bak