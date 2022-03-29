import os
import sys
import time
import unittest
from HtmlTestRunner import HTMLTestRunner
from common.logger import MyLogging
from common import glo
from datetime import datetime

# 获取当前py文件绝对路径
curr_path = os.path.dirname(os.path.realpath(__file__))
# 声明log
log = MyLogging(__name__).logger


# 1. 加载测试用例
def all_test():
    case_path = os.path.join(curr_path, "test_case")
    suite = unittest.TestLoader().discover(start_dir=case_path, pattern="fris_*.py", top_level_dir=None)
    # suite = unittest.TestLoader().discover(start_dir=case_path, pattern="fris_disease_type.py", top_level_dir=None)
    return suite


# 2. 执行测试用例
def run():
    now = time.strftime("%Y_%m_%d_%H_%M_%S")
    # 测试报告路径
    file_name = os.path.join(curr_path, "report") + "/report_" + now + ".html"
    f = open(file_name, "w", encoding='UTF-8')
    # HTMLReports_path = os.path.join(curr_path, "reports")
    runner = HTMLTestRunner(stream=f,
                            report_title="Fris 2.0 - UI Test Report",\
                            descriptions="环境: win10, 浏览器：Chrome")
    runner.run(all_test())
    f.close()


# 3. 获取最新的测试报告

def get_report(report_path):
    list = os.listdir(report_path)
    list.sort(key=lambda x: os.path.getmtime(os.path.join(report_path)))
    print("测试报告：", list[-1])
    report_file = os.path.join(report_path, list[-1])
    return report_file


if __name__ == "__main__":
    # clear history logs and reports
    while True:
        os.system("cmd /c start " + curr_path + "\\clearLogAndReport.bat")
        run()
        time.sleep(2)
        now = datetime.now()
        ddl_time = "2022-02-25 19:10:00"
        ddl = datetime.strptime(ddl_time, "%Y-%m-%d %H:%M:%S")
        if now > ddl:
            break