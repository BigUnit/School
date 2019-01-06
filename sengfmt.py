#!/usr/bin/env python3 

# File: sengfmt.py 
# Student Name  : Nathan Marcotte 
# Student Number: V00876934 
# SENG 265 - Assignment 2

import argparse
import sys

# Set up global variables
margin = 0
fmt = True
maxwidth = -1
cap = False	

fmtWords=["?maxwidth","?mrgn","?fmt","?cap"] #words to check for at the start of lines for formatting parameters
output = ""

def Margins():
	global output
	output += " " * margin

def resetMargin():
	global output
	temp = output
	output = ""
	Margins()
	output+=temp.lstrip()

def Parameters(inLine):
	global maxwidth
	global margin
	global fmt
	global cap
	
	if inLine :

		sLine = inLine[0][:]


		if sLine == fmtWords[0] :	#Sets maxwidth if there is one
			maxwidth = int(inLine[1][:])
			return True
			
		if sLine == fmtWords[1] : #Sets margin if there is one
			if inLine[1][0] == "+":
				margin += int(inLine[1][1:])
				if maxwidth != -1 :
					if margin > maxwidth - 20 :
						margin = maxwidth -20

			elif inLine[1][0] == "-":
				margin -= int(inLine[1][1:])
				if margin < 0 :
					margin=0
			else :
				margin = int(inLine[1])
			return True
				

			
		if sLine == fmtWords[2] : #Sets cap if specified, cap = 0 if not specified
			if inLine[1][:] == "off" :  #fmt = false indicates it is meant to be turned off
				fmt = False
			elif inLine[1][:] == "on" : #fmt =true indicates the text is meant to be formatted, 
				fmt = True
			return True

		if sLine == fmtWords[3] : #Sets cap if specified, cap = 0 if not specified
			if inLine[1][:] == "off":  #cap = -1 indicates no caps
				cap = False
			elif inLine[1][:] == "on" : #cap =1 indicates capitalization
				cap = True
			return True

def buildOutput(wordsForLine, curWidth):
	global output
	for word in wordsForLine:
		if not output:
			Margins()
		if len(word) + len(output) < curWidth  :
			if margin != len(output) :
			 	output += " "
			output+=word
		else:
			#resetMargin()
			outPrint()
			
			if maxwidth != -1:
				print("")
			
			Margins()
			output+=word	

def outPrint():
	global output

	if maxwidth!=-1 and fmt == True and len(output.split()) > 1:
		justify() 

	if fmt == True and cap == True :
		print(output.upper(),end="")
	else :
		print(output,end="")

	output = ""
			

def justify():
	global output
	oldMargin = len(output) - len(output.lstrip())

	#output = output.lstrip()
	spacesToGive = maxwidth - len(output)
	words = output.split() 	#remeber to remove trailing whitespaces / add margin back
	wordsAndSpaces = []

	for word in words :
		wordsAndSpaces+=[[word,1]]

	wordsAndSpaces[len(wordsAndSpaces)-1][1] = 0 #last word in line doesn't have a space after
	index = 0 #set to margin in real program maybe

	while spacesToGive > 0 :
		if index == len(wordsAndSpaces)-1 :
			index = 0
			
		wordsAndSpaces[index][1]+=1
		spacesToGive-=1
		index+=1

		


	newOut= ""

	for index in  range(0,len(wordsAndSpaces)) :
		newOut+= wordsAndSpaces[index][0] + (wordsAndSpaces[index][1] * " ")


	output=oldMargin * " " + newOut

def main():
	global output

	inputText = open("../../a_test.txt","r")
	inputLines = inputText.readlines()
	
	#try:
	#	inputText = open(sys.argv[1],"r")	
	#except:
	#	inputText = sys.stdin
	#with inputText:
	#	inputLines = inputText.readlines()


	for line in inputLines:
		if line :
			currLine = line.split()
			if Parameters(currLine) == True:
				continue
		
			lineNoFmt = line[:]

			if fmt == True and maxwidth == -1 :
				Margins()
				output+=lineNoFmt
				outPrint()

			elif fmt == True and maxwidth != -1 :
				buildOutput(currLine,maxwidth)
				if lineNoFmt[0] == "\n" :
					outPrint()
					print("\n")
					continue
				
			else :
				print(lineNoFmt,end="") 

	outPrint()
	if maxwidth == -1:
		print(end="")
	else :
		print("")

if __name__ == "__main__": 
	main() 
