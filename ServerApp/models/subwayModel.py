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


    def updateLocation(self, locationData):
        """
        열차의 위치를 업데이트
        ---
        Parameters
        ---
        locationData [dict] = {id1 : location1, id2 : location2}
        """
        self.db.child("subwayLocation").push(locationData)