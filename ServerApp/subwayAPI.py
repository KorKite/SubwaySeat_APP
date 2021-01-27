# get request from Subway API Server
from models.subwayModel import subwayModel
from models.seatModel import seatModel
import json
import time
import requests
from loger import log


class MetroAPI:
    def __init__(self):
        self.auth = []
        with open("./auth/metroAuth.json") as f:
            self.auth= json.load(f)["authKey1"] #인증키


        self.lines = [4,]
        self.subModel = subwayModel()
        self.seatModel = seatModel()
        self.initialize()
    
    def initialize(self):
        self.Location = {
            "4호선":{
                "상행":{},
                "하행":{}
            }
        }


    def makeUrl(self, line, keyIndex):
        """ API에서 활용되는 Url형태로 변경 """ 
        return f"http://swopenAPI.seoul.go.kr/api/subway/{self.auth[keyIndex]}/json/realtimePosition/0/100/{line}호선"
        # if self.auth_turn%3 == 0:
        #     self.auth_turn+= 1
        #     return f"http://swopenAPI.seoul.go.kr/api/subway/{self.auth}/json/realtimePosition/0/100/{line}호선"
        # elif self.auth_turn%3==1:
        #     self.auth_turn+= 1
        #     return f"http://swopenAPI.seoul.go.kr/api/subway/{self.auth2}/json/realtimePosition/0/100/{line}호선" 
        # else:
        #     self.auth_turn+= 1
        #     return f"http://swopenAPI.seoul.go.kr/api/subway/{self.auth3}/json/realtimePosition/0/100/{line}호선" 
        
    def requestUrl(self, url):
        """ API 서버에 데이터 호출 """ 
        print(requests.get(url))
        return dict(requests.get(url).json())["realtimePositionList"]


    def parse(self, locationData):
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
        
        self.Location[line][updownList[int(updown)]][trainId] = updatedData

    def parseRequest(self, JSON_FILE):
        """ 받아온 데이터를 처리 """
        subwayStatuses = JSON_FILE
        
        for status in subwayStatuses:
            self.parse(status)
        self.subModel.updateLocation(self.Location)

    def run(self, keyIndex):
        """ 
            run을 실행하면 자동으로 API에서 데이터를 받아와
            데이터베이스에 저장할 수 있도록함
            
        """
        self.initialize()
        self.subModel.remove()
        for line in self.lines:
            URL = self.makeUrl(line, keyIndex)
            try:
                REQUESTED_JSON = self.requestUrl(URL)
                self.parseRequest(REQUESTED_JSON)
                return True

            except:
                log("Subway Time Done or Error")
                return False
        self.seatModel.update()


if __name__ == "__main__":
    MetroAPI().run()
