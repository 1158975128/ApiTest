import smtplib
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
from datetime import datetime
now = datetime.now()
now_time = datetime.strftime(now, '%Y%m%d_%H%M%S')
class EmailManage:
    def send_email1(report_name):
        # 定义SMTP服务器
        smtpserver = 'smtp.163.com'
        username = '18389683007@163.com'
        password = 'OURTBPZCYBHXEYNZ'
        receiver = '1158975128@qq.com'
        # 创建邮件对象
        message = MIMEMultipart('related')
        subject = "fris_" + now_time + "_UI自动化测试报告"
        fujian = MIMEText(open(report_name,'rb').read(),'html','utf-8')
        message['from'] = username
        message['to'] = receiver
        message['subject'] = subject
        message.attach(fujian)
        smtp = smtplib.SMTP()
        smtp.connect(smtpserver)
        smtp.login(username,password)
        smtp.sendmail(username,receiver,message.as_string())
        smtp.quit()


