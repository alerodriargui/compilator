#!/bin/sh

gcc -no-pie -g src/output/AssemblerCode_NOT_Optimized.s -o program
gdb ./program