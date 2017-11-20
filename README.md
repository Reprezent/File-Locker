## COSC 483/583: Applied Cryptography
### Programming Assignment 2: RSA Due: 11:59:59 pm November 10, 2017
#### Ground Rules

- You may choose to work with up to two other student if you wish.
- Only one submission is required per group, please ensure that both group members names are on the submitted copy.
- Work must be submitted electronically via github.com. **You are expected to re-register
your group using the same method as programming assignment 1.** The choice of programming language is yours, but your software will be expected to operate in an environment of my choosing, specifically an arch linux virtual machine.


#### Implementing Padded RSA
Your task for this assignment is to correctly implement an asymmetric key cipher. You are expected to implement RSA key generation and padded RSA encryption/decryption (Construction 11.30 in the text book).

You are **NOT** allowed to use built in implementations of RSA key generation, or existing implementations of RSA. You can assume that the size of *r* in your implementation should be half the bit size of *N*, you may assume that all inputs will fit appropriately in a single encryption element.

#### You are expected to provide the following deliverables, any missing deliverables will result in point loss:
- Source code for your system.
- A Makefile named Makefile which will result in the appropriate software artifacts being generated (or a blank Makefile if compilation is not needed).
- A file named groupMembers.txt containing all group members, this is required for groups of size one.
- I will expect the following software artifacts (executable programs) to exist in your project directory post execution of the make command.
  - rsa-enc : encrypts an integer using RSA
  - rsa-dec : decrypts an encryption of an integer using RSA
  - rsa-keygen : creates a valid RSA public key/private key pair and stores them in files
- rsa-enc and rsa-dec should take the following argument flags:
  + -k <key file> : required, specifies a file storing a valid RSA key in the example format
  + -i <input file> : required, specifies the path of the file containing an integer in Z∗n in String form (base 10) that is being operated on
  + -o <output file> : required, specifies the path of the file where the resulting output is stored in String form (base 10) 1
- rsa-keygen should take the following argument flags:
  + -p <public key file>: required, specifies the file to store the public key
  + -s <secret key file>: required, specifies the file to store the private key
  + -n <number of bits>: required, specifies the number of bits in your N

#### Key File Formats
Key files should be three lines long, containing Strings representing the integercomponents of the key values in base 10. For public key files the first line should contain the numberrepresenting the number of bits in N, the second line should contain N, and the third line containsthe number representing e. For the private key files, the first line should contain the number of bitsrepresenting N, the second line contains N, and the third line contains the number representing d. Example files will be posted on the website.

#### Extra Credit Portions
There are two portions of extra credit that will be awarded if you use your own implementations of specific functions rather than a provided implementation from a library.
  - Implement generation of random prime numbers of a specific bit length. Your testing of the primality of a potential candidate number should use a probabilistic prime test, you should implement the test based on research of the appropriate algorithms. A recommendation is the Miller-Rabin primaility test.
  - Implement an efficient modular exponentiation algorithm.
