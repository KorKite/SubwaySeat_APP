import pyrebase
import json

with open("auth/firebaseAuth.json") as f:
    config = json.load(f)

firebase = pyrebase.initialize_app(config)
db = firebase.database()

# db.child("subway").child("line1").child("subwayId1").update({"location":"SeoulStn"})
# db.child("subway").child("line1").child("subwayId2").update({"location":"SeoulStn"})

# db.child("seat").child("line1").child("subwayId1").child("block4").child("seat3").update({"status":"4min", "userid":"32fwa"})
# db.child("seat").child("line1").child("subwayId2").child("block1").child("seat5").update({"status":"3min", "userid":"12esad"})
# db.child("seat").child("line1").child("subwayId2").child("block1").child("seat3").update({"status":"20min", "userid":"qdw2"})
print(db.child("subway").child("line1").get().val().keys())


