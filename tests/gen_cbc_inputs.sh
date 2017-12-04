#!/bin/sh
input_dir="CBCInputs"
increment=2
increment2=3
increment3=7
length=2
mkdir -p $input_dir
while [ $length -le 4094 ]; do
    printf "Generating input of size %8d" $length
    ../random_binary $length > $input_dir/`printf "%04d" $length`.in
    printf "   done\n"
    length=$(($length + $increment))

    printf "Generating input of size %8d" $length
    ../random_binary $length > $input_dir/`printf "%04d" $length`.in
    printf "   done\n"
    length=$(($length + $increment2))

    printf "Generating input of size %8d" $length
    ../random_binary $length > $input_dir/`printf "%04d" $length`.in
    printf "   done\n"
    length=$(($length + $increment3))
done
