import pyrebase
import json

with open("auth/firebaseAuth.json") as f:
    config = json.load(f)

firebase = pyrebase.initialize_app(config)
db = firebase.database()

# db.child("User").child("sta06167").update({"Password":"asdflj23-1#.f", "name":"Junseo Ko"})
# db.child("User").child("kkunasad").update({"Password":"weqf-2334g0503@)f", "name":"MJ Kim"})
# db.child("User").child("sange1104").update({"Password":"#20fj0AD)394$fes", "name":"Sangeun Lee"})

# db.child("SubwayLocation").child("Line4").child("1201").update({"statnNm":"혜화", "trainSttus":"0","updnLine":"1", "statnTnm":"오이도"})
# db.child("SubwayLocation").child("Line4").child("1202").update({"statnNm":"서울역", "trainSttus":"1","updnLine":"1", "statnTnm":"사당"})
# db.child("SubwayLocation").child("Line4").child("1203").update({"statnNm":"이수", "trainSttus":"0","updnLine":"0", "statnTnm":"당고개"})
# db.child("SubwayLocation").child("Line4").child("1204").update({"statnNm":"안산", "trainSttus":"0","updnLine":"0", "statnTnm":"당고개"})
# db.child("SubwayLocation").child("Line4").child("1205").update({"statnNm":"오이도", "trainSttus":"1","updnLine":"1", "statnTnm":"오이도"})
print(db.child("SubwayLocation").child("Line4").get().val().keys())

# for i in range(10):
#     db.child("SeatStatus").child("Line4").child("1201").child(f"Block{i}")
#     db.child("SeatStatus").child("Line4").child("1202").child(f"Block{i}")
#     db.child("SeatStatus").child("Line4").child("1203").child(f"Block{i}")
#     db.child("SeatStatus").child("Line4").child("1204").child(f"Block{i}")
#     db.child("SeatStatus").child("Line4").child("1205").child(f"Block{i}")



# print(db.child("subway").child("line1").get().val().keys())


