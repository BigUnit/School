#!/opt/local/bin/python

"""
Nathan Marcotte
V00876934
SENG 265 A3
sengfmt2.py
11/29/18
"""

import sys
import argparse
import fileinput
from formatter import Formatter

def main():
    fi = sys.argv[1]
    fmt = Formatter(filename=fi)
    
    fmtOut=fmt.get_lines()

    for line in fmtOut:
        print (line)

if __name__ == "__main__":
    main()
