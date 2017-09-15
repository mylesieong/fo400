# Friend of AS400 Introduction

fo400 (friend of as400) is an utility that provides source code viewer and saver. fo400 is package as a window executable so that it can be installed as a command line tool. 

## How to use

To view a source code or pf member on stdout, use: 

`fo400 ZECSDAPP/QRPGLESRC/ECSDACT`

To save the target to current folder(the content will be saved with its original name and a its attribute as file extension), use: 

`fo400 -f ZECSDAPP/QRPGLESRC/ECSDACT` 

Please note that any other different forms of arguments will not be accepts by the tool.

## How to build and install

Use stardard maven command to build. And add the exe file to system path. Please note that the config.properties file should be place under folder specified as $FO400 environment variable and the name should not be changed.
