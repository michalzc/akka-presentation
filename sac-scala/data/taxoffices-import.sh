#!/usr/bin/env bash
mongoimport --db akkapresentation --type csv --collection taxoffices --fields name,postCode,city,address,phone,fax,email,usId --file taxoffices.csv
