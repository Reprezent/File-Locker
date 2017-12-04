#!/bin/sh
key_dir="CBCKeys"
length=32
numkeys=20
i=1
mkdir -p $key_dir
while [ $i -le $numkeys ]; do
    printf "Generating key %d" $i
    ../random_hex $length > $key_dir/$i.key
    printf "    done\n"
    i=$(($i + 1))
done

