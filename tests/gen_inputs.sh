#!/bin/sh
input_dir="inputs"
increment=2
length=2
mkdir -p $input_dir
while [ $length -le 4094 ]; do
    printf "Generating input of size %8d" $length
    ../random_bits $length > $input_dir/`printf "%04d" $length`.in
    printf "\n" >> $input_dir/`printf "%04d" $length`.in
    printf "   done\n"
    length=$(($length + $increment))
done
