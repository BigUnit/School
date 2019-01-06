#!/opt/local/bin/python

"""
Nathan Marcotte
V00876934
SENG 265 A3
formatter.py
11/29/18
"""

import sys
import re

class Formatter:
    """This is the definition for the class"""

    def __init__(self, filename=None, inputlines=None):
        
        self.fmtWords=["?maxwidth","?mrgn","?fmt","?cap","?replace","?monthabbr"]
        self.margin = 0
        self.fmt = True
        self.maxwidth = -1
        self.cap = False	
        self.output = ""
        self.tempOut = ""
        self.replace = ["",""]
        self.month = False
        
        if filename!=None:
            try:
                inputText = open(filename,"r")	
            except:
                print("Error 1: Invalid File") #INVALID FILE ERROR
                sys.exit()
            with inputText:
                input = inputText.readlines()
        elif inputlines!=None:
                input = inputlines
        else:
            input = sys.stdin

        if not input:
            print("Error 2: Empty Input") #Input is empty
            sys.exit()



        format(self,input)
    
	
    def get_lines(self):
        return self.output.splitlines()


def Margins(self):
    self.tempOut += " " * self.margin

def resetMargin(self):
    temp = self.tempOut
    self.tempOut = ""
    Margins(self)
    self.tempOut+=temp.lstrip()

def Parameters(self,inLine):
    
    if inLine :

        sLine = inLine[0][:]


        if sLine == self.fmtWords[0] :	#Sets maxwidth if there is one
            self.maxwidth = int(inLine[1][:])
            return True
            
        if sLine == self.fmtWords[1] : #Sets margin if there is one
            if inLine[1][0] == "+":
                self.margin += int(inLine[1][1:])
                if self.maxwidth != -1 :
                    if self.margin > self.maxwidth - 20 :
                        self.margin = self.maxwidth -20

            elif inLine[1][0] == "-":
                self.margin -= int(inLine[1][1:])
                if self.margin < 0 :
                    self.margin=0
            else :
                self.margin = int(inLine[1])
            return True
                

            
        if sLine == self.fmtWords[2] : #Sets cap if specified, cap = 0 if not specified
            if inLine[1][:] == "off" :  #fmt = false indicates it is meant to be turned off
                self.fmt = False
            elif inLine[1][:] == "on" : #fmt =true indicates the text is meant to be formatted, 
                self.fmt = True
            return True

        if sLine == self.fmtWords[3] : #Sets cap if specified, cap = 0 if not specified
            if inLine[1][:] == "off":  #cap = -1 indicates no caps
                self.cap = False
            elif inLine[1][:] == "on" : #cap =1 indicates capitalization
                self.cap = True
            return True

        if sLine == self.fmtWords[4] : #replace
           self.replace[0] =inLine[1][:]
           self.replace[1] =inLine[2][:]
           return True

        if sLine == self.fmtWords[5] : #month abbr
            if inLine[1][:] == "off":  
                self.month = False
            elif inLine[1][:] == "on" : 
                self.month = True
            return True

def buildOutput(self,wordsForLine, curWidth):
    for word in wordsForLine:
        word=replacePat(self,word)
        
        if monthDetect(self,word) and self.month == True:
            newFmt=abbr(self,word)
            buildOutput(self,newFmt.split(),curWidth)
            continue
        
        if not self.tempOut:
            Margins(self)
        if len(word) + len(self.tempOut) < curWidth  :
            if self.margin != len(self.tempOut) :
                self.tempOut += " "
            self.tempOut+=word
        else:
            outPrint(self)
            
            if self.maxwidth != -1:
                self.output += '\n'

            Margins(self)
            self.tempOut+=word	

def outPrint(self):

    if self.maxwidth!=-1 and self.fmt == True and len(self.tempOut.split()) > 1:
        justify(self) 


    if self.fmt == True and self.cap == True :
        self.output += self.tempOut.upper()
    else :
        self.output += self.tempOut

    self.tempOut = ""
            

def justify(self):

    oldMargin = len(self.tempOut) - len(self.tempOut.lstrip())

    
    spacesToGive = self.maxwidth - len(self.tempOut)
    words = self.tempOut.split() 	#remeber to remove trailing whitespaces / add margin back
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


    self.tempOut=oldMargin * " " + newOut


def format(self, inputLines):
        
        for line in inputLines:
            if line :
                currLine = line.split()
                if Parameters(self,currLine) == True:
                    continue
            
                lineNoFmt = line[:]

                if self.fmt == True and self.maxwidth == -1 :
                    Margins(self)
                    lineNoFmt=replacePat(self,lineNoFmt)

                    if monthDetect(self,lineNoFmt) and self.month == True:
                        lineNoFmt=abbr(self,lineNoFmt)

                    self.tempOut+=lineNoFmt + '\n'
                    outPrint(self)
                elif self.fmt == True and self.maxwidth != -1 :
                    buildOutput(self,currLine,self.maxwidth)
                    if lineNoFmt[0] == '\n' :
                        outPrint(self)
                        self.output += '\n\n'
                        continue 
                else :
                    self.output += lineNoFmt
            else:   
                outPrint(self)
                self.output+='\n'
                if self.maxwidth != -1:
                    self.output += '\n'
            

        outPrint(self)
        
        
       
            


        #print(self.output.splitlines())

def replacePat(self, string):
    string = re.sub(self.replace[0],self.replace[1],string) 
    return string  
   
def abbr(self,string):    
    months = {1:"Jan.",2:"Feb.",3: "Mar.",4:"Apr.",5: "May.",6:"Jun.",7: "Jul.",8: "Aug.",9: "Sep.",10: "Oct.",11: "Nov.",12: "Dec."}
    pat = r"(\d{2})[\/.-](\d{2})[\/.-](\d{4})"
  
    match = re.search(pat,string)
    if match:
        (month_num , day, year) = match.groups()
        newFmt = months[int(month_num)] +' '+day+', '+year
        string = re.sub(pat,newFmt,string)
    
    return string

def monthDetect(self,string):
     pat = r"(\d{2})[\/.-](\d{2})[\/.-](\d{4})"
     return re.search(pat,string)
