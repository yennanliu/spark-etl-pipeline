#!/bin/sh
#################################################################
# SCRIPT COPY TOY DATA WITHIN LOCAL FILE SYSTEM   
#################################################################

check_file_exist(){

if [[ -d $HOME/analysis ]]; 
    then 
        echo ">>>> file (dataset) already exist"
    else 
        echo ">>>> file (dataset) not exist, download them now..."
        #cd ~ && git clone https://github.com/yennanliu/analysis.git
        echo ">>>> download OK!"
fi
}

copy_txt_file(){

for txtfile in $(ls $HOME/analysis/SPARK_/*.txt);
    do
        echo $txtfile
        cp $txtfile data/
    done 
}
copy_csv_file(){

for csvfile in $(ls $HOME/analysis/SPARK_/*.csv);
    do
        echo $csvfile
        cp $csvfile data/
    done 
} 

check_file_exist
copy_txt_file
copy_csv_file