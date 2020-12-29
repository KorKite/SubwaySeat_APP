# get request from Subway API Server
import requests
from models.subwayModel import subwayModel
import json

class MetroAPI:
    def __init__(self):
        with open("./auth/metroAuth.json") as f:
            self.auth = json.load(f)["authKey1"] #인증키
        self.lines = [4,]
        self.subModel = subwayModel
    
    def makeUrl(self, line):
        """ API에서 활용되는 Url형태로 변경 """ 
        return f"http://swopenAPI.seoul.go.kr/api/subway/{self.auth}/json/realtimePosition/0/5/{line}호선"
        
    def requestUrl(self, url):
        """ API 서버에 데이터 호출 """ 
        return requests.get(url).json()

    def parseRequest(self):
        """ 받아온 데이터를 처리 """ 
        
        #데이터 처리하는 코드
        data = "data"
        self.saveDB(data)

    def saveDB(self, data):
        """ 생성한 데이터를 데이터베이스에 저장 """
        self.subModel.updateSubway()
        self.subModel.updateLocation()

    def run(self):
        """ 
            run을 실행하면 자동으로 API에서 데이터를 받아와
            데이터베이스에 저장할 수 있도록함
            
        """
        for line in self.lines:
            URL = self.makeUrl(line)
            REQUESTED_JSON = self.requestUrl(line)
            self.parseRequest(REQUESTED_JSON)

if __name__ == "__main__":
    MetroAPI().requestUrl("temp")