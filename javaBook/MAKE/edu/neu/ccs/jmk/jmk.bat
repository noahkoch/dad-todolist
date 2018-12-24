@echo off
rem This script assumes HOME is set.
rem Edit this script to suit your needs.
set CLASSPATH=.;%HOME%\lib\jmk.jar
javaw edu.neu.ccs.jmk.Make -w %1 %2 %3 %4 %5 %6 %7 %8 %9
