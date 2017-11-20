#include <random>
#include <iostream>
#include <ios>
#include <cstdlib>
#include <string>
#include <iomanip>
#include <cstdio>

using namespace std;

// Generates random hex IV's of 128 bits, with 0 entropy
int main(int argc, char** argv)
{

    random_device rd;

    if(argc == 1)
    {
        cout << right << setfill('0') << setw(8) << hex << rd();
        cout << right << setfill('0') << setw(8) << hex << rd();
        cout << right << setfill('0') << setw(8) << hex << rd();
        cout << right << setfill('0') << setw(8) << hex << rd();
//        printf("%04x", rd());
        return 0;
    }
    
    int i = stol(string(argv[1]));
    while(i > 0)
    {
        cout << setfill('0') << setw(8) << hex << rd();
        // printf("%04x", rd());
        i -= 4;
    }

    if(i)
    {
        unsigned int temp = (rd() << (4-i) * 8) >> (4-i) * 8;
        cout << setfill('0') << setw(i) << hex << temp;
    }

    return 0;
}

