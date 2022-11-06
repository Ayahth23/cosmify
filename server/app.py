import os
import json
import time
import requests
import nasapy
import urllib.request
from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route('/', methods=['GET'])
def ping():
    return "hello", 200

def make_config(link, title):
    return {'imageURL': link, 'imageTitle': title}

@app.route('/space-picture', methods=['GET'])
def pic_of_day():
    k = os.getenv('NASA_KEY')
    nasa = nasapy.Nasa(key = k)
    apod = nasa.picture_of_the_day()
    if(apod["media_type"] == "image"):

        #POINT B:
        #Displaying hd images only:
        if("hdurl" in apod.keys()):


            if("hdurl" in apod.keys() and "title" in apod.keys()):
                app_config= make_config(apod["hdurl"], apod["title"])
                return json.dumps(app_config)


    #POINT I:
    #If media type is not image:
    else:
        print("Sorry, Image not available!")

@app.route('/fact-of-day', methods=['GET'])
def fact_of_day():
    facts = ["Space does not begin at a specific altitude above the Earth, but the Kármán line at 100 km is a commonly used definition.",
            "The temperature in the void of space is about −270.45 °C.", "Space is a hard vacuum, meaning it is a void containing very little matter.",
            "There is no sound in space because molecules are too far apart to transmit sound.",
            "The space between galaxies is not completely empty but has an average of one atom per cubic meter.",
            "There are an estimated 100-400 billion stars in our galaxy, the Milky Way.",
            "The universe is observed to be 13.8 billion years old and has been expanding since its formation in the Big Bang."
            "In the observable universe there are an estimated 2 trillion (2,000,000,000,000) galaxies."
            "The International Space Station is the largest ever crewed object in space.",
            "Spacecraft have visited all the known planets in our solar system."]
    day = int((time.time()//86400)%len(facts))

    return json.dumps({"fact": facts[day]})

@app.route('/next-launch', methods=['GET'])
def next_launch():
    req = requests.get('https://fdo.rocketlaunch.live/json/launches/next/5').json()
    first = req['result'][0]
    return first
