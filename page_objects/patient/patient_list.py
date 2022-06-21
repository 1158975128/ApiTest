import os
import time
import random
from selenium import webdriver
from selenium.webdriver.common.by import By
from config.public_data.delay_time import *
from utils.object_map import ObjectMap
from common.logger import MyLogging
from page_objects.navigate_bar import NavigateBar
import pytest_check as check
from selenium.webdriver.remote.errorhandler import ElementNotInteractableException
from selenium.common.exceptions import NoSuchElementException
from utils.exist_tool import Element



log = MyLogging(__name__).logger
map_path = os.path.abspath(os.path.join(os.path.dirname(__file__), "../../page_element"))
patient_list_map = map_path + "/patient/patient_list.xml"

delay_time = DelayTime.short_time.value


class Patient_List():
    def __init__(self, driver):
        self.driver = driver
        self.version = ObjectMap(patient_list_map)
        self.patient = NavigateBar(self.driver)

    def check_doctor_patient_page(self,data):
        '''
        检查页面固定元素是否正确
        '''
        self.patient.go_to_patient()
        time.sleep(3)
        page_list = []
        page_list.append(self.version.getLocator(self.driver, "New_patient").text)
        page_list.append(self.version.getLocator(self.driver, "Origin_droplist").get_attribute('value'))
        page_list.append(self.version.getLocator(self.driver, "Patient_droplist").get_attribute('value'))
        page_list.append(self.version.getLocator(self.driver, "Name_input").get_attribute('placeholder'))
        page_list.append(self.version.getLocator(self.driver, "Date").get_attribute('placeholder'))
        page_list.append(self.version.getLocator(self.driver, "Date_Frame").text)
        page_list.append(self.version.getLocator(self.driver, "Check_End_Date").get_attribute('placeholder'))
        for i in range(len(page_list)):
            check.equal(page_list[i],data[i],"检查页面按钮名称和默认设置是否正确%s---%s"%(page_list[i],data[i]))
        log.info("页面按钮名称和默认设置正确")

    def check_therapist_patient_page(self,data):
        '''
        检查页面固定元素是否正确
        '''
        self.patient.go_to_patient()
        time.sleep(1)
        page_list = []
        page_list.append(self.version.getLocator(self.driver, "Origin_droplist").get_attribute('value'))
        page_list.append(self.version.getLocator(self.driver, "Patient_droplist").get_attribute('value'))
        page_list.append(self.version.getLocator(self.driver, "Name_input").get_attribute('placeholder'))
        page_list.append(self.version.getLocator(self.driver, "Date").get_attribute('placeholder'))
        page_list.append(self.version.getLocator(self.driver, "Date_Frame").text)
        page_list.append(self.version.getLocator(self.driver, "Check_End_Date").get_attribute('placeholder'))
        print(page_list)
        for i in range(len(page_list)):
            check.equal(page_list[i],data[i],"检查页面按钮名称和默认设置是否正确")
        log.info("页面按钮名称和默认设置正确")

    def check_patient_card(self,data):
        '''
        检查患者卡片页面元素是否正确
        :return: new_patient,origin_droplist,patient_droplist
        '''
        self.patient.go_to_patient()
        time.sleep(1)
        card_list = []
        card_list.append(self.version.getLocator(self.driver, "Check_name").get_attribute('textContent').strip())
        card_list.append(self.version.getLocator(self.driver, "Disease_Name").get_attribute('textContent').strip())
        card_list.append(self.version.getLocator(self.driver, "Sex_Value").get_attribute('textContent').strip())
        card_list.append(self.version.getLocator(self.driver, "Source").get_attribute('textContent').strip())
        card_list.append(self.version.getLocator(self.driver, "Sex_logo").get_attribute('class').strip())
        card_list.append(self.version.getLocator(self.driver, "Attending_Doctor").get_attribute('textContent').strip())
        card_list.append(self.version.getLocator(self.driver, "Patient_Department").get_attribute('textContent').strip())
        card_list.append(self.version.getLocator(self.driver, "Cost").get_attribute('textContent').strip())
        card_list.append(self.version.getLocator(self.driver, "Registration_Time").get_attribute('textContent').strip())
        card_list.append(self.version.getLocator(self.driver, "Therapeutist").get_attribute('textContent').strip())
        card_list.append(self.version.getLocator(self.driver, "Appoint_Therapeutist").get_attribute('textContent').strip())
        card_list.append(self.version.getLocator(self.driver, "See_Report").get_attribute('textContent').strip())
        for i in range(len(data)-2):
            check.equal(card_list[i],data[i],"检查姓名、疾病、性别、来源")
        if data[2] == "女":
            check.equal(card_list[4],'icon woman',"检查性别图标是否正确")
        elif data[2] == "男":
            check.equal(card_list[4],'icon man',"检查性别图标是否正确")

        check.equal(card_list[5], 'larry3', "检查医生")
        check.equal(card_list[6], '310-1', "检查科室")
        check.equal(card_list[7], '0.00元', "检查费用")
        check.equal(card_list[8], data[5], "检查登记时间")
        check.equal(card_list[9], '治疗师：-', "检查治疗师")
        check.equal(card_list[10], '指定治疗师', "检查指定治疗师按钮")
        check.equal(card_list[11], '查看报告', "检查查看报告按钮")

    def appoint_therapeutist(self,therapeutist_name):
        '''
        给某个小类指定治疗师
        '''
        self.patient.go_to_patient()
        self.driver.implicitly_wait(20)
        appoint = self.version.getLocator(self.driver, "Appoint_Therapeutist")
        appoint.click()
        choose = self.version.getLocator(self.driver, "Choose_Therapeutist")
        choose.click()
        choose.send_keys(therapeutist_name)
        select = self.version.getLocator(self.driver, "Select_Therapeutist")
        select.click()
        ensure = self.version.getLocator(self.driver, "Ensure")
        ensure.click()
        again_ensure = self.version.getLocator(self.driver, "Again_Ensure")
        again_ensure.click()
        time.sleep(1)
        tips = self.version.getLocator(self.driver, "Tips").text.strip()
        appoint_therapeutist = self.version.getLocator(self.driver, "Appoint_Therapeutist").get_attribute('textContent').strip()
        check.equal(tips,"指定治疗师成功","检查治疗师指定成功")
        check.equal(appoint_therapeutist,"修改治疗师","检查按钮是否变为修改治疗师")

    def verify_paging(self):
        '''
        测试分页功能，如果当前页面卡片>12张时进行翻页操作
        '''
        self.patient.go_to_patient()
        time.sleep(3)
        select_all = self.version.getLocator(self.driver, "Select_All")
        select_all.click()
        patient_all = self.version.getLocator(self.driver, "Patient_droplist")
        patient_all.click()
        self.driver.implicitly_wait(1000)
        sel_all_patient = self.version.getLocator(self.driver, "Sel_All_Patient")
        sel_all_patient.click()
        time.sleep(1)
        cards = self.driver.find_elements(By.CSS_SELECTOR,value=".el-row>div")
        next_page = self.version.getLocator(self.driver, "Next_Page")
        if len(cards) == 12 and next_page.is_displayed():
            next_page.click()
            log.info("翻页成功，已进入下一页")
        else:
            log.info("当前页面卡片小于或等于12张，无法进行翻页操作")


    def verify_screen(self,patient_type):
        try:
            sources = self.driver.find_elements(By.CSS_SELECTOR, value=".el-row>div .card-source")
            origin_droplist = self.version.getLocator(self.driver, "Origin_droplist").get_attribute('value')
            check.equal(origin_droplist, patient_type, "检查患者来源下拉框选中的与传入的患者来源是否一致")
            if len(sources) > 1:
                for source in sources:
                    check.equal(source.get_attribute('textContent').strip(), patient_type, "检查传入的患者类型与页面显示的患者类型是否一致")
            elif len(sources) == 1:
                check.equal(sources.get_attribute('textContent').strip(), patient_type, "检查传入的患者类型与页面显示的患者类型是否一致")
            elif len(sources) == 0:
                log.info("当前%s没有患者" % patient_type)

        except ElementNotInteractableException:
            pass

        except NoSuchElementException:
            pass
    def add_new_patient(self,patient,disease,diagnosis,patient_sex,patient_origin):
        '''
        新增一个住院患者
        :param patient: 传入患者姓名姓名
        :param disease: 传入疾病类型
        :param diagnosis: 传入功能诊断
        :return: 成功返回True ，失败返回False
        '''
        self.patient.go_to_patient()
        self.driver.implicitly_wait(100)
        new_patient = self.version.getLocator(self.driver, "New_patient")
        new_patient.click()
        patient_name = self.version.getLocator(self.driver, "Patient_name")
        patient_name.send_keys(patient)
        time.sleep(1)
        if patient_sex == "男":
            sex_man = self.version.getLocator(self.driver, "Sex_man")
            sex_man.click()
        elif patient_sex == "女":
            sex_woman = self.version.getLocator(self.driver, "Sex_Woman")
            sex_woman.click()
        self.driver.implicitly_wait(100)
        disease_type = self.version.getLocator(self.driver, "Disease_type")
        disease_type.click()
        time.sleep(1)
        disease_type.send_keys(disease)
        time.sleep(1)
        disease_type_select = self.version.getLocator(self.driver, "Disease_type_select")
        disease_type_select.click()
        time.sleep(2)
        if patient_origin == "住院":
            origin_hospital = self.version.getLocator(self.driver, "Origin_hospital")
            origin_hospital.click()
            time.sleep(1)
        elif patient_origin == "其他":
            origin_other = self.version.getLocator(self.driver, "Origin_Other")
            origin_other.click()
            time.sleep(1)
        function_diagnosis = self.version.getLocator(self.driver, "Function_diagnosis")
        function_diagnosis.click()
        time.sleep(1)
        function_diagnosis.send_keys(diagnosis)
        function_diagnosis_select = self.version.getLocator(self.driver, "Function_diagnosis_select")
        function_diagnosis_select.click()
        time.sleep(1)
        doctor = self.version.getLocator(self.driver, "Doctor")
        doctor.click()
        time.sleep(1)
        doctor_select = self.version.getLocator(self.driver, "Doctor_select")
        doctor_select.click()
        time.sleep(1)
        deparetment = self.version.getLocator(self.driver, "Department")
        deparetment.click()
        time.sleep(1)
        deparetment_select = self.version.getLocator(self.driver, "Department_select")
        deparetment_select.click()
        time.sleep(1)
        ensure = self.version.getLocator(self.driver, "Ensure")
        ensure.click()
        time.sleep(1)
        tips = self.version.getLocator(self.driver, "Tips").text.strip()
        if tips == '登记患者成功！':
            return True
        else:
            return False
        log.info("新增成功")

    def find_patient(self,patient_name=None,patient_origin='all',patient_type="全部患者"):
        '''
        查找患者
        :param patient_name: 传入查找的患者姓名
        '''
        self.patient.go_to_patient()
        time.sleep(3)
        select_all = self.version.getLocator(self.driver, "Select_All")
        select_all.click()
        time.sleep(1)
        if patient_origin == "门诊":
            origin_select = self.version.getLocator(self.driver, "Origin_select")
            origin_select.click()
            time.sleep(1)
            outpatient_select = self.version.getLocator(self.driver, "Outpatient_select")
            outpatient_select.click()
        elif patient_origin == "住院":
            origin_select = self.version.getLocator(self.driver, "Origin_select")
            origin_select.click()
            time.sleep(1)
            hospital_select = self.version.getLocator(self.driver, "Hospital_select")
            hospital_select.click()
        elif patient_origin == "其他":
            origin_select = self.version.getLocator(self.driver, "Origin_select")
            origin_select.click()
            time.sleep(1)
            other_select = self.version.getLocator(self.driver, "Other_select")
            other_select.click()
        time.sleep(1)
        if patient_type == "全部患者":
            patient_all = self.version.getLocator(self.driver, "Patient_All")
            patient_all.click()
            sel_all_patient = self.version.getLocator(self.driver, "Sel_All_Patient")
            self.driver.implicitly_wait(100)
            sel_all_patient.click()
        elif patient_type == "我的患者":
            patient_all = self.version.getLocator(self.driver, "Patient_All")
            patient_all.click()
            my_patient = self.version.getLocator(self.driver, "My_Patient")
            self.driver.implicitly_wait(100)
            my_patient.click()
        time.sleep(1)

        if patient_name == None:
            pass
        else:
            name_input = self.version.getLocator(self.driver, "Name_input")
            name_input.click()
            time.sleep(1)
            name_input.clear()
            time.sleep(1)
            name_input.send_keys(patient_name)
            time.sleep(1)

    def check_patient_name(self,patient_name):
        check_names =self.driver.find_elements(By.CSS_SELECTOR,value=".el-row>div .card-head .title")
        if len(check_names) >=1:
            for check_name in check_names:
                check_name = check_name.text
                if patient_name == check_name:
                    check.equal(patient_name,check_name,"检查患者查找成功")
                    log.info("已找到名为%s的患者"%patient_name)
                    return True
                elif patient_name != check_name:
                    log.info("输入查找的患者为%s实际找到的患者为%s"%(patient_name,check_name))
                    return False
        else:
            log.info("没有叫%s的患者"%patient_name)
            return False

    def choose_date(self,choose_month="current"):
        date_logo = self.version.getLocator(self.driver, "Date")
        date_logo.click()
        month = self.version.getLocator(self.driver, "Month").get_attribute('textContent').split("年")[1].strip()
        split_month = month.split("月")[0].strip()
        if choose_month != "current":
            last_month = self.version.getLocator(self.driver, "Last_Month")
            last_month.click()
            self.driver.implicitly_wait(30)
            beginday = self.version.getLocator(self.driver, "Oneday")
            beginday.click()
            self.driver.implicitly_wait(100)
            lastday = self.version.getLocator(self.driver, "Lastmonth_day")
            lastday.click()
            time.sleep(1)
        else:
            beginday = self.version.getLocator(self.driver, "Oneday")
            beginday.click()
            self.driver.implicitly_wait(100)
            lastday = self.version.getLocator(self.driver, "Lastday")
            lastday.click()
            time.sleep(1)
        check_dates = self.driver.find_elements(By.CSS_SELECTOR,value='.el-row>div [class="patient-info patient-register"]>p:nth-child(3)>span')
        if len(check_dates) >= 1:
            for i in range(len(check_dates)):
                registration_time = self.version.getLocator(self.driver, "Registration_Time").get_attribute('textContent').split("年0")[1]
                split_registration_time = registration_time.split("月")[0]
                check.equal(split_month, split_registration_time, "检查当前页面患者的月份于所查找的是否一直")
            log.info('匹配成功')

        elif len(check_dates) == 0:
            log.info('当前日期无患者')
        # print('13:选择日期执行完毕',time.strftime("%Y_%m_%d_%H_%M_%S"))


    def add_treatment_item(self,patient_name,item):
        '''
        新增一个治疗项目
        :param patient_name: 传入患者姓名
        :param item: 治疗项目名称
        :return: 成功返回True ，失败返回False
        '''
        check_name = self.version.getLocator(self.driver, "Check_name")
        if patient_name == check_name.text:
            check_name.click()
            time.sleep(1)
            add_Treatment = self.version.getLocator(self.driver, "Add_Treatment")
            add_Treatment.click()
            item_name = self.version.getLocator(self.driver, "Item_name")
            item_name.click()
            time.sleep(1)
            item_name.send_keys(item)
            time.sleep(1)
            item_name_select = self.version.getLocator(self.driver, "Item_name_select")
            item_name_select.click()
            time.sleep(1)
            region_name = self.version.getLocator(self.driver, "Region_name")
            region_name.click()
            time.sleep(1)
            region_name_select = self.version.getLocator(self.driver, "Region_name_select")
            region_name_select.click()
            time.sleep(1)
            department_name = self.version.getLocator(self.driver, "Department_name")
            department_name.click()
            time.sleep(1)
            department_name_select = self.version.getLocator(self.driver, "Department_name_select")
            department_name_select.click()
            time.sleep(1)
            item_ensure = self.version.getLocator(self.driver, "Item_Ensure")
            item_ensure.click()
            time.sleep(1)
            tips = self.version.getLocator(self.driver, "Tips").text
            if tips == "新增项目成功":
                return True
            else:
                return False

    def check_treatment_name(self,therapeutist_name):
        name = self.version.getLocator(self.driver, "Check_appoint_result").get_attribute('textContent').strip()
        check.equal(therapeutist_name,name,"检查新建的项目是否指定默认治疗师")

    def add_judge_item(self,patient_name,item):
        '''
        新增一个评定项目
        :param patient_name: 传入患者姓名
        :param item: 传入评定项目
        :return: 成功返回True ，失败返回False
        '''
        check_name = self.version.getLocator(self.driver, "Check_name")
        print(check_name.text,patient_name)
        if patient_name == check_name.text:
            check_name.click()
            time.sleep(1)
            Judge_item = self.version.getLocator(self.driver, "Judge_Item")
            Judge_item.click()
            add_Treatment = self.version.getLocator(self.driver, "Add_Treatment")
            add_Treatment.click()
            item_name = self.version.getLocator(self.driver, "Item_name")
            item_name.click()
            time.sleep(1)
            item_name.send_keys(item)
            time.sleep(1)
            item_name_select = self.version.getLocator(self.driver, "Item_name_select")
            item_name_select.click()
            time.sleep(1)
            region_name = self.version.getLocator(self.driver, "Region_name")
            region_name.click()
            time.sleep(1)
            region_name_select = self.version.getLocator(self.driver, "Region_name_select")
            region_name_select.click()
            time.sleep(1)
            hospital_department_name = self.version.getLocator(self.driver, "Hospital_Department_name")
            hospital_department_name.click()
            time.sleep(1)
            department_name_select = self.version.getLocator(self.driver, "Department_name_select")
            department_name_select.click()
            time.sleep(1)
            item_ensure = self.version.getLocator(self.driver, "Item_Ensure")
            item_ensure.click()
            time.sleep(1)
            tips = self.version.getLocator(self.driver, "Tips").text
            if tips == "新增项目成功":
                print("新增治疗项目成功：",tips)
                return True
            else:
                return False

    def leave_hospital(self,patient_name):
        '''
        测试住院患者结束疗程出院
        :param patient_name: 出院患者的姓名
        :return: 成功返回True ，失败返回False
        '''
        check_name = self.version.getLocator(self.driver, "Check_name")
        if patient_name == check_name.text:
            check_name.click()
            time.sleep(1)
            patientInfo = self.version.getLocator(self.driver, "PatientInfo")
            patientInfo.click()
            end_treatment = self.version.getLocator(self.driver, "End_Treatment")
            end_treatment.click()
            leave_time = self.version.getLocator(self.driver, "Time")
            leave_time.click()
            time.sleep(1)
            choose_time = self.version.getLocator(self.driver, "Choose_Time")
            choose_time.click()
            time.sleep(1)
            leave_ensure = self.version.getLocator(self.driver, "Leave_Ensure")
            leave_ensure.click()
            time.sleep(1)
            again_ensure = self.version.getLocator(self.driver, "Again_Ensure")
            again_ensure.click()
            time.sleep(1)
            tips = self.version.getLocator(self.driver, "Tips").text
            if tips == "结束疗程成功":
                return True
            else:
                return False
