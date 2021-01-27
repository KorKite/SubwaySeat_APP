# Main App
import time
from subwayAPI import MetroAPI

from loger import log

if __name__ =="__main__":
    Metro = MetroAPI()
    while True:
        for i in range(3):
            if Metro.run(i):
                print(f"Fire Base Updated - API CODE {i}")
                log(f"Fire Base Updated, API CODE {i}")
                time.sleep(15)
            else:
                log("Wait for 1hour to start subway")
                time.slepp(60 * 60)