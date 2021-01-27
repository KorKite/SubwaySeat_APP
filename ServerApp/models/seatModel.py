import pyrebase
import json

class seatModel:
    """ subway위치와 현재 있는 지하철을 저장하는 모델"""
    def __init__(self):
        with open("auth/firebaseAuth.json") as f:
            config = json.load(f)

        self.firebase = pyrebase.initialize_app(config)
        self.db = self.firebase.database()
        
        
    def update(self):
        WHOLE_info = self.db.child("SeatStatus").get()
        up = WHOLE_info.val()["4호선"]["상행"]
        # for train in up.keys():
        #     traindst = self.db.child("SubwayLoaction").child(train)
        
        down = WHOLE_info.val()["4호선"]["하행"]
        for train in down.keys():
            traind = self.db.child("SubwayLoaction").child("하행").child(train).get().val()
            # traindst = traind["현재역"]
            
            if traind == None:
                self.db.child("SeatStatus").child(train).remove()
                break
            else:
                traindst = traind["현재역"]
                for block in down[train]:
                    if block == None:
                        continue
                    else:
                        for seat in block.keys():
                            seatInfo = block[seat]
                            if seatInfo["dst"] == traindst:
                                self.db.child("SeatStatus").child("하행").child(train).child(block).child(seat).remove()


if __name__ == "__main__":
    model = seatModel()
    model.update()