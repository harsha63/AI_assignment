import flask
from flask import request, jsonify
from tinydb import TinyDB, Query
import aiml


app = flask.Flask(__name__)
app.config["DEBUG"] = True
ask = Query()
db = TinyDB('actual.json')
BRAIN_FILE="brain.dump"
k = aiml.Kernel()
k.bootstrap(learnFiles="std-startup.aiml", commands="load aiml b")
#print("Saving brain file: " + BRAIN_FILE)
#k.saveBrain(BRAIN_FILE)
print(k.respond("Who is Lauren"))


# Create some test data for our catalog in the form of a list of dictionaries.
books = [
    {'id': 0,
     'title': 'A Fire Upon the Deep',
     'author': 'Vernor Vinge',
     'first_sentence': 'The coldsleep itself was dreamless.',
     'year_published': '1992'},
    {'id': 1,
     'title': 'The Ones Who Walk Away From Omelas',
     'author': 'Ursula K. Le Guin',
     'first_sentence': 'With a clamor of bells that set the swallows soaring, the Festival of Summer came to the city Omelas, bright-towered by the sea.',
     'published': '1973'},
    {'id': 2,
     'title': 'Dhalgren',
     'author': 'Samuel R. Delany',
     'first_sentence': 'to wound the autumnal city.',
     'published': '1975'}
]


@app.route('/', methods=['GET'])
def home():
    return '''<h1>Distant Reading Archive</h1>
<p>A prototype API for distant reading of science fiction novels.</p>'''


@app.route('/books', methods=['GET'])
def api_all():
    return jsonify(books)


@app.route('/api/v1/resources/books', methods=['GET'])
def api_id():
    # Check if an ID was provided as part of the URL.
    # If ID is provided, assign it to a variable.
    # If no ID is provided, display an error in the browser.
    if 'id' in request.args:
        id = int(request.args['id'])
    else:
        return "Error: No id field provided. Please specify an id."

    # Create an empty list for our results
    results = []

    # Loop through the data and match results that fit the requested ID.
    # IDs are unique, but other fields might return many results
    for book in books:
        if book['id'] == id:
            results.append(book)

    # Use the jsonify function from Flask to convert our list of
    # Python dictionaries to the JSON format.
    return jsonify(results)


@app.route('/api/login', methods=['GET'])
def usernameTaking():
    a = request.args['username']
    
    if len(db.search(ask.username==a))>0:
        return ("Username already taken")
    else:
        db.insert({'username':a,'Name':'','Address':''})
        print("Username set")
        return '1'

@app.route('/api/v1/update', methods=['GET'])
def updateAccount():
    Name = request.args['name']
    Address = request.args['address']
    username = request.args['username']
    db.update({'Address':Address,'Name':Name},ask.username==username)
    return 'Updated'


@app.route('/api/v1/getData', methods=['GET'])
def getData():
    ids = request.args['id']
    dt = db.search(ask.username==ids)
    return jsonify(dt)


@app.route('/main', methods=['GET'])
def getDatas():
    ids = request.args['message']
    return k.respond(ids)

@app.route('/new', methods=['GET'])
def newUser():
    data = request.args['data']
    data = data.split(',')
    if len(db.search(ask.username==data[0]))>0:
        return ("Username already taken")
    else:
        if(len(data[2])!=10 or len(data[-1])!=10 or not data[2].isnumeric() or not data[-1].isnumeric()):
            return "Registration incomplete, write phone numbers correctly"
        if('@gmail.com' not in data[3]):
            return "Check your email Address, and write it correctly"
        db.insert({'username':data[0],'name':data[1],'phone':data[2],'email':data[3],'address':data[4],'contact':data[5],'contactP':data[6]})
        setVars(data)
        print("Username set")
        #return 'You data has been saved and new user account created.'
    return "Username set for you, "+data[0]

@app.route('/Address', methods=['GET'])
def getAddress():
    ret = k.getPredicate("address")
    return ret

@app.route('/getUser', methods=['GET'])
def getUser():
    ret = k.getPredicate("name")
    return ret


def setVars(data):
    k.setPredicate("Uname",data[0])
    k.setPredicate("name",data[1])
    k.setPredicate("phone", data[2])
    k.setPredicate("email", data[3])
    k.setPredicate("address", data[4])
    k.setPredicate("contact",data[5])
    k.setPredicate("contactP",data[6])



#user = usernameTaking(db,ask)

#Name = input("What is your name?")
#Address = input("What is your Address?")

#db.update({'Address':Address,'Name':Name},ask.username==user)
#print("Done")

app.run()