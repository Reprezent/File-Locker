JC=javac
JFLAGS= -Xlint:all 
SOURCE_DIR=sources
SOURCE_FILES=$(notdir $(wildcard $(SOURCE_DIR)/*.java))
SOURCES=$(wildcard $(SOURCE_DIR)/*.java)
OBJECTS=$(SOURCE_FILES:.java=.class)
CLASSPATH=classes
CLASSES=$(addprefix $(CLASSPATH)/, $(OBJECTS))

all:
# Makes the directory without complaining
	mkdir -p $(CLASSPATH)
	$(JC) $(JFLAGS) -d $(CLASSPATH) $(SOURCES)

test_inputs: $(SOURCE_DIR)/test_inputs.cpp
	g++ -Wall -std=c++11 -o $@ $<

.PHONY: clean
clean:
	rm -f $(CLASSES) random_hex

