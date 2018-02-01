
pic2plot -T ps output.pic |
gs -q -r360 -dNOPAUSE -sDEVICE=pnm -sOutputFile=-  - -c quit |
pnmcrop |
pnmscale 0.25 |
ppmtogif >FILENAME.gif

#apt-get install netpbm
