import os
import sys
import time
import unittest
from HtmlTestRunner import HTMLTestRunner
from common.logger import MyLogging
from utils.summary_report import SummaryReport
from common.send_email import EmailManage
from datetime import datetime

# 获取当前py文件绝对路径
curr_path = os.path.dirname(os.path.realpath(__file__))
# 声明log
log = MyLogging(__name__).logger
email_path = curr_path + '/utils/summary'



# 1. 加载测试用例
def all_test():
    # 获取testcase文件夹路径
    case_path = os.path.join(curr_path, "test_case")
    # 使用TestLoader()通过discover来执行case_path下的所有测试用例
    suite = unittest.TestLoader().discover(start_dir=case_path, pattern="fris_department.py", top_level_dir=None)
    # suite = unittest.TestLoader().discover(start_dir=case_path, pattern="fris_behavior_patient_controller.py", top_level_dir=None)
    # suite = unittest.TestLoader().discover(start_dir=case_path, pattern="fris_*.py", top_level_dir=None)
    return suite


# 2. 执行测试用例
def run():
    now = time.strftime("%Y_%m_%d_%H_%M_%S")
    # 测试报告路径
    file_name = os.path.join(curr_path, "report") + "/report_" + now + ".html"
    print(file_name)
    f = open(file_name, "w", encoding='UTF-8')
    # HTMLReports_path = os.path.join(curr_path, "reports")
    runner = HTMLTestRunner(stream=f, report_title="Fris Server - API Test Report",\
                      descriptions="环境: win10, 浏览器：Chrome")
    print(runner)
    # input()
    runner.run(all_test())
    f.close()


# 3. 获取最新的测试报告
def get_report(report_path):
    list = os.listdir(report_path)
    list.sort(key=lambda x: os.path.getmtime(os.path.join(report_path)))
    print("测试报告：", list[-1])
    report_file = os.path.join(report_path, list[-1])
    return report_file

# 4.获取发送邮件的报告路径
def get_email_path(path):
    for file in os.listdir(email_path):
        if os.path.isfile(os.path.join(email_path, file)) and file.endswith('.html'):
            path = os.path.join(email_path, file)
    return path

if __name__ == "__main__":
    # clear history logs and reports
    # cmd = "cmd /c start " + curr_path + "\\clearLogAndReport.bat"
    # os.system("cmd /c start " + curr_path + "\\clearLogAndReport.bat")
    # print(f"------{cmd}-----")

    run()
    time.sleep(5)
    # 生成报告
    SummaryReport.go()
    time.sleep(5)
    # 发送邮件
    email_html_path = get_email_path(email_path)
    EmailManage.send_email1(email_html_path)
