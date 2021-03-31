from tinydb import TinyDB, Query

def usernameTaking(db, ask):
	a = input("What is your username?")
	
	if len(db.search(ask.username==a))>0:
		print("Username already taken")
		usernameTaking()
	else:
		db.insert({'username':a,'Name':'','Address':''})
		print("Username set")
		return a
ask = Query()
db = TinyDB('actual.json')

user = usernameTaking(db,ask)

Name = input("What is your name?")
Address = input("What is your Address?")

db.update({'Address':Address,'Name':Name},ask.username==user)
print("Done")