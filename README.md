# KeywordSearch
A simple search algorithm that allows a user to retrieve sentences from a file with the queried word.

## Build Requirements
* Java
* Scala
* sbt

## Executable Requirements
* Java
The compiled .jar file can be found [here](https://github.com/aprusik/KeywordSearch/releases/tag/v1.0).

## Usage
The program can be run from the command line with the following:
```
java -jar KeywordSearchExercise-assembly-0.1 [filename] [arguments]
```
For the filename, include the path to the file you wish to search.  For arguments, include any arguments you wish to use, separated by spaces.  They currently available arguments are listed below.

After the program begins, input your search query and send a carriage return.  The program will then print any sentances from the input file that contain that query.  If no sentances are found, it will return nothing.  By default this search is case-INSENSITIVE.

By default, you will be able to continue sending queries as long as you wish.  To close the program, send a carriage return without typing a query.

### Arguments
* `-c` Turns ON case-sensitivity for the search algorithm.
* `-l` Turns OFF looping; only one query will be allowed, then the program will close.
