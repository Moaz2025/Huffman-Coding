# Huffman-Coding

- This is an implementation of Huffman's algorithm.  
- This implementation allows compressing and decompressing arbitrary files.  
- This implementation collects statistics from the input file first, then applies the
compression algorithm.  
- This program has the capability of considering more than one byte. For example,
instead of just collecting the frequencies and finding codewords for single bytes. The same can
be done assuming the basic unit is n bytes, where n is an integer.

## Running the program
- To use the jar for compressing an input file, the following will be called:  
java -jar jar_name.jar c absolute_path_to_input_file n
- c means compressing the file.
- n is the number of bytes that will be considered together.
- To use it for decompressing an input file, the following be called:  
java -jar jar_name.jar d absolute_path_to_input_file
- If the user chooses to compress a file with the name abc.exe, the compressed file
should have the name n.abc.exe.hc where <n> will be replaced by n (the number of bytes per group).  
- The compressed file will appear in the same directory of the input file.  
- The program will print the compression ratio and the compression time.
- If the user chooses to decompress a file with name abc.exe.hc, the output file will be
named extracted.abc.exe.  
- The decompressed file will appear in the same directory of the input file.
- The program will print the decompression time in this case.
