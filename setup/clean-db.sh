#!/bin/sh

sudo -u postgres -i psql -U postgres -c 'DROP DATABASE srms'
sudo -u postgres -i psql -U postgres -c 'DROP USER srms_usr'
