import os
from jinja2 import Environment, FileSystemLoader
from datetime import datetime
from bs4 import BeautifulSoup
import re
from config.public_data.report import Status


curr_path = os.path.dirname(os.path.realpath(__file__))
summary_dir = curr_path + "/summary"

class SummaryReport:
    @staticmethod
    def go(title="Fris Test Summary"):
        start_time = datetime.now()
        test_suite = total_num = pass_num = fail_num = error_num = skip_num = 0
        controller_info = []
        report_dir = curr_path + "/../reports"
        htmls = SummaryReport.get_all_files(report_dir)
        for html in htmls:
            html_info = SummaryReport.get_html_info(html)
            # head
            report_start_time = html_info["start_time"]
            if report_start_time < start_time:
                start_time = report_start_time
            test_suite += 1
            # total
            total_num += html_info["total"]
            pass_num += html_info["pass"]
            fail_num += html_info["fail"]
            error_num += html_info["error"]
            skip_num += html_info["skip"]
            # controller - status
            status = Status.success.value
            if html_info["fail"] > 0:
                status = Status.danger.value
            elif html_info["error"] > 0:
                status = Status.warning.value
            # controller - file name
            file_name = html.split("\\")[-1]
            controller_name = file_name.split(".")[0]
            controller = re.sub(r"TestResults_fris_", "", controller_name)
            controller_info.append({"status": status, "controller": controller, "info": html_info["summary"]})
        start_time_str = datetime.strftime(start_time, "%Y-%m-%d %H:%M:%S")
        summary = "Test Suite: %d, Total Case: %d, Pass: %d, Fail: %d, Error: %d, Skip: %d" % \
                  (test_suite, total_num, pass_num, fail_num, error_num, skip_num)
        head = {"title": title, "start_time": start_time_str, "summary": summary}
        total = {"pass": pass_num, "fail": fail_num, "error": error_num, "skip": skip_num}
        SummaryReport.generate_file(head, total, controller_info)

    @staticmethod
    def get_all_files(base):
        for root, ds, fs in os.walk(base):
            for f in fs:
                if f.endswith(".html"):
                    full_name = os.path.join(root, f)
                    yield full_name

    @staticmethod
    def get_html_info(html):
        start_time = summary = None
        total_num = pass_num = fail_num = error_num = skip_num = 0
        soup = BeautifulSoup(open(html, 'rb'), features="html.parser")
        h2 = soup.h2
        for p in h2.next_siblings:
            if "Start Time" in p.text:
                start_time_str = re.findall(r"(\d{4}-\d{1,2}-\d{1,2}\s\d{1,2}:\d{1,2}:\d{1,2})", p.text)[0]
                start_time = datetime.strptime(start_time_str, "%Y-%m-%d %H:%M:%S")
            elif "Summary" in p.text:
                summary = p.next.next_sibling
                summary_group = summary.split(",")
                for info in summary_group:
                    if "Total" in info:
                        total_num = re.findall(r"\d+", info)[0]
                    elif "Pass" in info:
                        pass_num = re.findall(r"\d+", info)[0]
                    elif "Fail" in info:
                        fail_num = re.findall(r"\d+", info)[0]
                    elif "Error" in info:
                        error_num = re.findall(r"\d+", info)[0]
                    elif "Skip" in info:
                        skip_num = re.findall(r"\d+", info)[0]
        return {"start_time": start_time, "summary": summary, "total": int(total_num), "pass": int(pass_num),
                "fail": int(fail_num), "error": int(error_num), "skip": int(skip_num)}

    @staticmethod
    def generate_file(head, total, items):
        env = Environment(loader=FileSystemLoader(curr_path + '/template'))
        template = env.get_template('summary_temp.html')
        now = datetime.now()
        now_time = datetime.strftime(now, '%Y%m%d_%H%M%S')
        file_name = summary_dir + "/summary_" + now_time + ".html"
        print(file_name)
        # input()
        print(template.render(head=head, total=total, items=items))
        with open(file_name, 'wb') as f:
            out = template.render(head=head, total=total, items=items)
            f.write(out.encode("utf-8"))
            f.close()


if __name__ == '__main__':
    SummaryReport.go()
