# get request from Subway API Server
import requests
from models.subwayModel import subwayModel
import json
import time

class MetroAPI:
    def __init__(self):
        with open("./auth/metroAuth.json") as f:
            self.auth = json.load(f)["authKey1"] #인증키
        self.lines = [4,]
        self.subModel = subwayModel()
        self.Location = {
            "4호선":{
                "상행":{},
                "하행":{}
            }
        }
    
    def makeUrl(self, line):
        """ API에서 활용되는 Url형태로 변경 """ 
        return f"http://swopenAPI.seoul.go.kr/api/subway/{self.auth}/json/realtimePosition/0/100/{line}호선"
        
    def requestUrl(self, url):
        """ API 서버에 데이터 호출 """ 
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
        self.subModel.remove()
        for status in subwayStatuses:
            self.parse(status)
        # jsonDumped = json.dumps(self.Location,ensure_ascii=False)
        self.subModel.updateLocation(self.Location)

    def run(self):
        """ 
            run을 실행하면 자동으로 API에서 데이터를 받아와
            데이터베이스에 저장할 수 있도록함
            
        """
        for line in self.lines:
            URL = self.makeUrl(line)
            REQUESTED_JSON = self.requestUrl(URL)
            self.parseRequest(REQUESTED_JSON)

if __name__ == "__main__":
    MetroAPI().run()
    