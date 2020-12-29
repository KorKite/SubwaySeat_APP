# Main App
import time
from subwayAPI import MetroAPI


if __name__ =="__main__":
    Metro = MetroAPI()
    while True:
        time.sleep(30)
        Metro.run()
