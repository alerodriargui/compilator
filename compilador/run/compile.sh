#!/bin/sh
rm -f ./program
gcc -no-pie ./output/AssemblerCode_NOT_Optimized.s -o program
FILE=./program
if [ -f "$FILE" ]; then
  $FILE
fi