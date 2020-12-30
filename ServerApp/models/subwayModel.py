# API로부터 위치를 받아서 서버에 기록하는 모델
import pyrebase
import json

class subwayModel:
    """ subway위치와 현재 있는 지하철을 저장하는 모델"""
    def __init__(self):
        with open("auth/firebaseAuth.json") as f:
            config = json.load(f)

        self.firebase = pyrebase.initialize_app(config)
        self.db = self.firebase.database()
        
    def updateSubway(self, subways):
        """ 
        어떤 열차가 현재 있는지를 업데이트
        ---
        Parameter
        ---
        subways = [각 열차의 id를 나열한 리스트]
        """ 
        for subway in subways:
            self.db.child(subway)

    def remove(self):
        self.db.child("subwayLocation").remove()


    def updateLocation(self, locationData):
        """
        열차의 위치를 업데이트
        ---
        Parameters
        ---
        locationData [dict] = {id1 : location1, id2 : location2}
        """
        trainId = locationData["trainNo"] # 지하철 id
        line = locationData["subwayNm"] # 호선
        station = locationData["statnNm"] #현재 위치하고 있는 역
        trainStatus = locationData["trainSttus"] #열차 출발 여부
        statusList = ["진입", "도착", "출발"]
        finalDestination = locationData["statnTnm"] # 종착역
        updown = locationData["updnLine"] #상행 하행 여부 (0 상행, 1 하행)
        updownList = ["상행", "하행"]
        updatedData = {
            "현재역":station,
            "열차출발여부":statusList[int(trainStatus)],
            "종착역": finalDestination
        }
        
        self.db.child("SubwayLocation").child(line).child(updownList[int(updown)]).child(trainId).update(updatedData)