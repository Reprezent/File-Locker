// Richard Riedel, J.T. Liso, Sean Whalen
// CS 583 Fall 2017
// Programming Assignment 2
// This file tests key sizes that are stored int he directory tests/public_keys, tests/private_keys.
// It uses inputs stored in tests/inputs. The file names will be stored in numerical format with 
// whatever file extension.
// This is mainly used EXTENSIVELY test our rsa-dec and rsa-enc.

#include <dirent.h>
#include <stdio.h>
#include <map>
#include <string>
#include <iostream>
#include <stdlib.h>
#include <fstream>
#include <algorithm>
#include <sstream>
#include <signal.h>
#include <unistd.h>
#include <iomanip>

using namespace std;

// Loads all files from the specified directory into the map.
void loadIntoMap(const char* dir, map<unsigned int, string>& map)
{
    DIR* test_directory;
    struct dirent* directory_entry;
    string directory;
    string temp, number_conv;
    unsigned int num;

    // adds the ending / so we can just append this to the other strings.
    directory = dir;
    directory.push_back('/');

    test_directory = opendir(dir);
    if(test_directory == NULL)
    {
        perror(NULL);
        return;
    }

    // Iterates over the directory contents.
    while((directory_entry = readdir(test_directory)) != NULL)
    {
        // Skip over all files that are prepended with a .
        if(directory_entry->d_name[0] == '.')
            continue;
        temp = directory_entry->d_name;
        number_conv = temp;
        num = stoul(number_conv.erase(number_conv.find_first_of('.')));
        temp.insert(0, directory);
        map.insert(make_pair(num, temp));
    }
    closedir(test_directory);

}
template<typename InputIterator1, typename InputIterator2>
bool range_equal(InputIterator1 first1, InputIterator1 last1,  InputIterator2 first2, InputIterator2 last2)
{
    while(first1 != last1 && first2 != last2)
    {
        if(*first1 != *first2) return false;
        ++first1;
        ++first2;
    }
    return (first1 == last1) && (first2 == last2);
}
bool compare_files(const std::string& filename1, const std::string& filename2)
{
    ifstream file1(filename1);
    ifstream file2(filename2);

    istreambuf_iterator<char> begin1(file1);
    istreambuf_iterator<char> begin2(file2);

    istreambuf_iterator<char> end;

    return range_equal(begin1, end, begin2, end);
}

int main(int argc, char** argv)
{
    const char* test_dir_name = "tests/inputs";
    const char* pub_key_dir_name = "tests/public_keys";
    const char* pri_key_dir_name = "tests/private_keys";
    string encr_dir_name = "tests/encrypted_files/";
    string outputs_dir_name = "tests/outputs/";
    string command_builder;

    int startingBits = 40;

    if(argc == 2)
    {
        startingBits = stoi(argv[1]);
    }

    map<unsigned int, string> inputs;
    map<unsigned int, string> pub_keys;
    map<unsigned int, string> pri_keys;

    loadIntoMap(test_dir_name, inputs);
    loadIntoMap(pub_key_dir_name, pub_keys);
    loadIntoMap(pri_key_dir_name, pri_keys);

    string enc_file, output_file;
    int rv;
    map<unsigned int, string>::iterator mit, upper_bound;
    for(pair<const unsigned int, string> i : pub_keys)
    {
        if(i.first < startingBits)
            continue;
        // Find the corresponding private key.
        mit = pri_keys.find(i.first);
        if(mit == pri_keys.end())
            continue;

        upper_bound = inputs.upper_bound(i.first/2 - 24);
        if(upper_bound == inputs.end())
            continue;

        cout << "Testing Key " << setw(4) << left << i.first << endl;
        for(map<unsigned int, string>::iterator it = inputs.begin(); it != upper_bound; it++)
        {
            enc_file = encr_dir_name + to_string(i.first) + '_' + to_string(it->first);
            output_file = outputs_dir_name + to_string(i.first) + '_' + to_string(it->first);
            command_builder.clear();
            command_builder.append("./rsa-enc -k ").append(i.second)   // Public Key File
                           .append(" -i ").append(it->second) // Input File
                           .append(" -o ").append(enc_file);  // Ciphertext
            
            cout << "\r   Encrypting File: " << it->second;
//            cout << endl;
#if 0
            cout << endl << "   Public Key File: " << i.second << endl
                 << "   Input File: " << it->second << endl
                 << "   Ciphertext File: " << enc_file << endl;
#endif
            cout.flush();
            // Encrypt the data.
            rv =  system(command_builder.c_str());
            if(WIFSIGNALED(rv) &&
                    (WTERMSIG(rv) == SIGINT || WTERMSIG(rv) == SIGQUIT))
                return 3;
            if(rv != 0)
            {
                cout << "     FAILED" << endl;
                return 2;
            }


            command_builder.clear();
            command_builder.append("./rsa-dec -k ").append(mit->second)   // Private Key File
                           .append(" -i ").append(enc_file) // Old output file
                           .append(" -o ").append(output_file);  // Plaintext
            cout << "    Decrypting File: " << enc_file;
            cout.flush();
            system(command_builder.c_str());
            if(WIFSIGNALED(rv) &&
                    (WTERMSIG(rv) == SIGINT || WTERMSIG(rv) == SIGQUIT))
                return 3;
            if(rv != 0)
            {
                cout << "     FAILED" << endl;
                return 2;
            }
                    
            if(!compare_files(it->second, output_file))
            {
                cout << "     FAILED" << endl;
                return 2;
            }
            /*
               cout << "./rsa-enc"
               << "-k" <<  i.second.c_str() // Public Key File
               << "-i" <<  it->second.c_str() // Input File
               << "-o" <<  enc_file << endl;
               */
        }
        
        cout << "      PASSED" << endl;
        remove(output_file.c_str());
        remove(enc_file.c_str());
        /*
           cout << "Key Size: " << i.first << endl;
           for(map<unsigned int, string>::iterator it = inputs.begin(); it != upper_bound; it++)
           {
           cout << "   Input Size: " << it->first << endl;
           }
           */
    }


    /*
       for(pair<const unsigned int, string>& i : inputs)
       {
       cout << i.first << " has value " << i.second << endl;
       }
       */
    return 0;        
}
