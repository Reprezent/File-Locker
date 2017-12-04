#include <random>
#include <unistd.h>
#include <algorithm>
#include <chrono>
#include <cstring>

using namespace std;

// Generates random hex IV's of 128 bits, with 0 entropy
int main(int argc, char** argv)
{
    int i = atoi(argv[1]);

    mt19937 rand(chrono::high_resolution_clock::now().time_since_epoch().count());

    unsigned int val;
    unsigned char* buffer = new unsigned char[i];
    unsigned char* temp = buffer;
    for(int j = 0; j < i; j+= sizeof(unsigned int))
    {
        val = rand();
        memcpy(temp, &val, min(i-j, static_cast<int>(sizeof(unsigned int))));
        temp += sizeof(unsigned int);
    }

    write(STDOUT_FILENO, reinterpret_cast<void*>(buffer), i);

    delete[] buffer;
    return 0;
}


