#!/bin/sh
#################################################################
# SCRIPT COPY TOY DATA WITHIN LOCAL FILE SYSTEM   
#################################################################

copy_txt_file(){

for txtfile in $(ls $HOME/analysis/SPARK_/*.txt);
    do
        echo $txtfile
        cp $txtfile data/
    done 
}
copy_txt_file(){

for csvfile in $(ls $HOME/analysis/SPARK_/*.csv);
    do
        echo $csvfile
        cp $csvfile data/
    done 
} 

copy_txt_file
copy_txt_file