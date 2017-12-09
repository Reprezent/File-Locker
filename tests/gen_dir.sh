#!/bin/sh
input_dir="test_dir"
increment=2
length=2
i=1

mkdir -p $input_dir
while [ $i -le 5 ]; do
    j=$RANDOM
    printf "Generating input of size %8d" $(($j % 200))
    ../random_bytes $(($j % 200)) > $input_dir/$i
    printf "   done\n"
    length=$(($length + $increment))
    i=$(($i + 1))
done
