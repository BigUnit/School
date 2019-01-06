/*
 * Nathan Marcotte
 * V00876934
 * 6/10/18
 *
 * UVic SENG 265, Fall 2018, A#1
 *
 * This will contain a solution to sengfmt. In order to complete the
 * task of formatting a file, it must open and read the file (hint: 
 * using fopen() and fgets() method) and format the text content base on the 
 * commands in the file. The program should output the formated content 
 * to the command line screen by default (hint: using printf() method).
 *
 * Supported commands include:
 * ?width width :  Each line following the command will be formatted such 
 *                 that there is never more than width characters in each line 
 * ?mrgn left   :  Each line following the command will be indented left spaces 
 *                 from the left-hand margin.
 * ?fmt on/off  :  This is used to turn formatting on and off. 
 */


#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define max_line_len   133	/* max chars per line is 132, +1 for null terminator  */



static int width  = -1;   /* width being used*/
static int in_width = -1; /*width that's read from file*/
static int margin =  0;
static int fmt    =  0;




int Parameters(char *in_value, char *parameters){ /* Reads parameters from file */
    char temp_format[4];
    int temp_width;
    int temp_margin;

	    if(strcmp("?width",parameters)==0){ /* Assigns width */
        in_value = strtok(NULL, " ");
        sscanf(in_value, "%d", &temp_width);
        width = temp_width;
        in_width = width;
        fmt = 1;
        return 1;
    }
   
    if(strcmp("?mrgn",parameters)==0 && fmt==1){ /* assigns margin  */
        in_value = strtok(NULL, " ");
        sscanf(in_value, "%d", &temp_margin);
        margin = temp_margin;
        return 1;

    }
   
    if(strcmp("?fmt",parameters)==0){ /* assigns fmt value  */
        in_value = strtok(NULL, " ");
        sscanf(in_value, "%s", temp_format);
        if(strcmp("on",temp_format)==0){
            fmt = 1;
            width = in_width;
        }
        if(strcmp("off",temp_format)==0){
            fmt = 0;
            width = max_line_len;
        }
        return 1;
    }
	
    return 0;
}


void Margins(char *current_line){ /*adds required amount of spaces to be equal to margin value  */
  if(fmt==1 && strlen(current_line)==0){
        int i;
        for(i=0;i<margin;i++){
            strcat(current_line, " ");
        }
    }
}


int main(int argc, char *argv[]) {

 FILE *input_file = fopen(argv[1], "r");				

    char input_line[max_line_len] = "\0";		
    int curr_L_len = 0;			
    char curr_L[max_line_len] = "\0";	

    char input_word[max_line_len] = "\0";		
    int cur_W_len = 0;			

   
	
	  if(input_file == NULL){	 /*exits if file cant be read*/
	  fprintf(stderr,"error opening: %s \n",argv[1]);
        exit(1);
    }
	
	
    while(fgets(input_line, max_line_len, input_file)!=NULL){
        int param;
        char *token;

        char line_fmtoff[max_line_len] = "\0"; /* if format is off this is the default line to use */
        strcpy(line_fmtoff,input_line);


        if(strncmp(input_line, "\n",1)==0 && fmt==1){
            strcat(curr_L, "\n");
            printf("%s", curr_L);

            strcpy(curr_L, "\n\0");
            printf("%s", curr_L);

            strcpy(curr_L, "\0");
        }else{

            token=strtok(input_line, "\t "); /*splits line into tokens i.e. individual words */

            while(token){

                sscanf(token, "%s", input_word);
                cur_W_len = strlen(input_word);


                if((param = Parameters(token, input_word))==0 && fmt==1){ /*check what formatting to apply*/
                    Margins(curr_L);
                    curr_L_len = strlen(curr_L);

                    if(curr_L_len+cur_W_len <= width-1){

                        if(margin!=curr_L_len){ /*checks if line is empty*/
                            strcat(curr_L, " ");
                        }

                        strcat(curr_L, input_word); /*adds word to line*/

                    }else{
                        strcat(curr_L, "\n");
                        printf("%s", curr_L);

                        strcpy(curr_L, "\0"); /*resets as new line*/
                        curr_L_len = 0;

                        Margins(curr_L);
                        strcat(curr_L, input_word); /*adds word to new line*/
                    }


                }else if(fmt==0 && param == 0){
                    printf("%s", line_fmtoff);
                    break;
                }

                token = strtok(NULL, " "); /*will advance to next word*/
            }
        }

    }

    printf("%s", curr_L); /*last line*/

	
	
	
	
	fclose(input_file);
	exit(0);
}
