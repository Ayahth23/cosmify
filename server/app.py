import os
import json
import requests
import nasapy
from datetime import datetime
import urllib.request
# from gtts import gTTS
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
    d = datetime.today().strftime('%Y-%m-%d')
    apod = nasa.picture_of_the_day(date=d, hd=True)

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
