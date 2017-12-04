#!/bin/sh
priv_dir="private_keys"
pub_dir="public_keys"
increment=16
length=40
mkdir -p $priv_dir $pub_dir
while [ $length -le 4096 ]; do
    printf "Generating key of size %d" $length
    ../rsa-keygen -p $pub_dir/$length.public -s $priv_dir/$length.private -n $length
    printf "    done\n"
    if [ $length -lt 256 ]; then
        length=$(($length + 8))
    else
        length=$(($length + $increment))
    fi
done

