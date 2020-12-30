import pyrebase
import json

with open("auth/firebaseAuth.json") as f:
    config = json.load(f)

with open("./models/lin4Station.json") as f:
    stationinfo = json.load(f)
firebase = pyrebase.initialize_app(config)
db = firebase.database()

station = db.child("StationInfo").child("4호선").update(stationinfo)

# print(db.child("SubwayLocation").child("Line4").get().val().keys())




