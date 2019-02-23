#!/bin/bash

for f in `ls entradas`; do
    echo `cat entradas/$f | awk '// {gsub (/\.I\./, "→");gsub (/\.A\./, "∧");gsub (/\.O\./, "∨");gsub (/\.N\./, "¬"); print}'`
    java Tableau < entradas/$f;
    echo;
done;
