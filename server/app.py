import os
import json
import requests

from flask import Flask, request

app = Flask(__name__)

@app.route('/', methods=['GET'])
def ping():
    return "ok", 200
