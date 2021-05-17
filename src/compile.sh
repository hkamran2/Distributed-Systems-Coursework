#!/bin/bash
#Get the file name to compile and run
echo Enter file name please:

read fileName

mpijavac $fileName.java
