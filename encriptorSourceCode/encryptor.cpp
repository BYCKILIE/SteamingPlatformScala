#include <iostream>
#include <cstring>
#include <fstream>

int processKey(int key) {
    return ((key * key * (key + 2) + 1) / 3) % 256;
}

unsigned char negate(unsigned char byte) {
    return byte ^ 15;
}

unsigned char encryptChar(unsigned char c, int key) {
    int rez = negate(c) + key;
    if (rez < 0) {
        rez = rez + 256;
    } else if (rez > 255) {
        rez = rez - 256;
    }
    return negate(static_cast<unsigned char>(rez));
}

unsigned char decryptChar(unsigned char c, int key) {
    int rez = negate(c) - key;
    if (rez < 0) {
        rez = rez + 256;
    } else if (rez > 255) {
        rez = rez - 256;
    }
    return negate(static_cast<unsigned char>(rez));
}

std::string encrypt(const std::string &data, int key) {
    unsigned len = data.size();
    std::string res;
    res.resize(len);

    for (int i = 0; i < len; ++i) {
        res[i] = static_cast<char>(encryptChar(data[i], key));
    }
    return res;
}

std::string decrypt(const std::string &data, int key) {
    unsigned len = data.size();
    std::string res;
    res.resize(len);

    for (int i = 0; i < len; ++i) {
        res[i] = static_cast<char>(decryptChar(data[i], key));
    }
    return res;
}

void processData(char *filename, int key, bool encryption) {
    std::ifstream file(filename);
    if (!file.is_open()) {
        std::cerr << "Failed to open the file: " << filename << std::endl;
        return;
    }

    std::string line;
    while (std::getline(file, line)) {
        if (encryption) {
            std::cout << encrypt(line, key) << std::endl;
        } else {
            std::cout << decrypt(line, key) << std::endl;
        }
    }
}

int main(int argc, char *argv[]) {
    if (argc != 5) {
        return 1;
    }
    if (strcmp(argv[1], "doBt2505!") != 0) {
        return 1;
    }
    int key = processKey(std::stoi(argv[3]));
    processData(argv[2], key, argv[4][0] == '1');
    return 0;
}
