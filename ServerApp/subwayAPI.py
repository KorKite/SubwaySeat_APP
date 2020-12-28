# get request from Subway API Server
import requests
from models.subwayModel import subwayModel
import json

class MetroAPI:
    def __init__(self):
        with open("./auth/metroAuth.json") as f:
            self.auth = json.load(f) #인증키
    
    def makeUrl(self):
        """ API에서 활용되는 Url형태로 변경 """ 
        pass

    def requestUrl(self, url):
        """ API 서버에 데이터 호출 """ 
        pass

    def parseRequest(self):
        """ 받아온 데이터를 처리 """ 
        pass

    def saveDB(self, data):
        """ 생성한 데이터를 데이터베이스에 저장 """
        subwayModel.updateSubway()
        subwayModel.updateLocation()

    def run(self):
        """ 
            run을 실행하면 자동으로 API에서 데이터를 받아와
            데이터베이스에 저장할 수 있도록함
            
        """
        pass