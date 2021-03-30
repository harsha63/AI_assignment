
# Python program to create a simple GUI 
# Simple Quiz using Tkinter 
  
#import everything from tkinter 
from tkinter import *
  
# and import messagebox as mb from tkinter 
from tkinter import messagebox as mb 
  
#import json to use json file for data 
import json
import subprocess




globalInput = ""
x = ""
totals = []
gui = Tk() 

e = Entry(gui)
e.pack()
e.focus_set()

# set the size of the GUI Window 
gui.geometry("900x500")

gui.title("RedBlueGreen")

class Problem:
    def __init__(self):
        self.pres=0
        self.x = ""
        self.totals  = []
        self.submit = Button(gui, text="Submit",command=self.calculate, 
        width=10,bg="blue",fg="white",font=("ariel",16,"bold"))
        self.submit.place(x=350,y=380)
        #self.display_state()
    
    def next_button(self):
        next_button = Button(gui, text="Next",command=self.next_btn, 
        width=10,bg="blue",fg="white",font=("ariel",16,"bold"))
        next_button.place(x=350,y=380)
    def calculate(self):
        global e
        g = e.get()
        x,y = subprocess.Popen("RedBlueGreen.exe "+g  , stdout = subprocess.PIPE, stderr = subprocess.PIPE, shell = True).communicate()
        self.x = x.decode()

        self.x = self.x.split('\n')
        self.totals = self.x[0].split(" ")
        print(self.totals[2])
        #self.submit.destroy()
        self.next_button()
        self.display_state()
    def nextbuttons(self):
        next_button = Button(gui, text="Next",command=self.next_btn, 
        width=10,bg="blue",fg="white",font=("ariel",16,"bold"))
        next_button.place(x=350,y=380)
    def next_btn(self):
        if(self.pres<len(self.x)):
            self.pres+=1
            self.display_state()
        if(self.pres==len(self.x)):
            gui.destroy()
    def display_state(self):
        disp = "West bank \t\t East bank\n"
        print(self.x)
        print(self.pres)
        h = self.x[self.pres].split(" ")
        print(self.totals)
        disp+=h[0]+" \t\t\t\t "+str((int(self.totals[0])-int(h[0])))+"\n"
        disp+=h[1]+" \t\t\t\t "+str((int(self.totals[1])-int(h[1])))+"\n"
        disp+=h[2][:-1]+" \t\t\t\t "+str((int(self.totals[2])-int(h[2][:-1])))+"\n"
        print(disp)
        q_no = Label(gui, text=disp, width=100, height = 10,
        font=( 'ariel' ,16, 'bold' ), anchor= 'w' )
        q_no.place(x=100,y=100)
prob = Problem()
gui.mainloop()
            
