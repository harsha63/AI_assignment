#!/usr/bin/python3
import os
import aiml
from tinydb import TinyDB, Query

user = ""
db = TinyDB('actual.json')
username = input("What is your username?\n")
name = input("What is your Name?\n")
ask = Query()
if(len(db.search(ask.username==username and ask.Name==name))==0):
	print("No user found. Login failed.")
	quit()
else:
	print("User found, logged in")
	user = username

BRAIN_FILE="brain.dump"

k = aiml.Kernel()
person = db.search(ask.username==user)
person = person[0]
k.setPredicate("name", person['Name'])
k.setPredicate("address", person['Address'])
# To increase the startup speed of the bot it is
# possible to save the parsed aiml files as a
# dump. This code checks if a dump exists and
# otherwise loads the aiml from the xml files
# and saves the brain dump.
if os.path.exists(BRAIN_FILE):
    print("Loading from brain file: " + BRAIN_FILE)
    k.loadBrain(BRAIN_FILE)
else:
    print("Parsing aiml files")
    k.bootstrap(learnFiles="std-startup.aiml", commands="load aiml b")
    print("Saving brain file: " + BRAIN_FILE)
    k.saveBrain(BRAIN_FILE)

# Endless loop which passes the input to the bot and prints
# its response
while True:
    input_text = input("> ")
    response = k.respond(input_text)
    print(response)