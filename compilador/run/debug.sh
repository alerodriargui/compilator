#!/bin/sh

gcc -no-pie -g ./output/AssemblerCode_NOT_Optimized.s -o program
gdb ./program