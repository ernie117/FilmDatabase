#!/bin/sh

set -e

sudo apt install software-properties-common
yes '\n' | sudo add-apt-repository ppa:deadsnakes/ppa
sudo apt update
sudo apt install python3.8
sudo update-alternatives --install /usr/bin/python python /usr/bin/python3.8 2
sudo update-alternatives --set python /usr/bin/python3.8
sudo apt install python3-pip python3-distutils-extra python3-setuptools
pip3 install requests behave
#pip3 install pyOpenSSL
