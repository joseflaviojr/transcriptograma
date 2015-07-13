CC=gcc
CXX=g++
RM=rm -f

SRCS=Biblioteca.cpp Claritate.cpp CodigoDemocratico.cpp CFM.cpp
OBJS=$(subst .cpp,.o,$(SRCS))

all: Claritate CodigoDemocratico CFM

Biblioteca.o: Biblioteca.h Biblioteca.cpp

Claritate: Claritate.o Biblioteca.o
	$(CXX) Claritate.o Biblioteca.o -o Claritate

Claritate.o: Claritate.cpp

CodigoDemocratico: CodigoDemocratico.o Biblioteca.o
	$(CXX) CodigoDemocratico.o Biblioteca.o -o CodigoDemocratico

CodigoDemocratico.o: CodigoDemocratico.cpp

CFM: CFM.o Biblioteca.o
	$(CXX) CFM.o Biblioteca.o -o CFM

CFM.o: CFM.cpp

clean:
	$(RM) $(OBJS)

dist-clean: clean
	$(RM) Claritate CodigoDemocratico CFM