#!/bin/bash
#Get the file name to compile and run
echo Enter file name please:

read fileName

echo Enter the number of cores to run the program on: 

read cores

mpijavac $fileName.java

mpirun --allow-run-as-root -np $cores java $fileName