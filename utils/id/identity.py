import random
import re
from datetime import datetime, timedelta

import utils.id.constant as const


class IdNumber(str):
    def __init__(self, id_number):
        super(IdNumber, self).__init__()
        self.id = id_number
        self.area_id = self.id[0:6]
        self.birth_year = self.id[6:10]
        self.birth_month = self.id[10:12]
        self.birth_day = self.id[12:14]

    def get_area_name(self):
        """根据区域编号取出区域名称"""
        return const.AREA_INFO[self.area_id]

    def get_birthday(self):
        """通过身份证号获取出生日期"""
        return "{0}-{1}-{2}".format(self.birth_year, self.birth_month, self.birth_day)

    def get_age(self):
        """通过身份证号获取年龄"""
        now = (datetime.now() + timedelta(days=1))
        year, month, day = now.year, now.month, now.day
        if year == int(self.birth_year):
            return 0
        else:
            if int(self.birth_month) > month or (int(self.birth_month) == month and int(self.birth_day) > day):
                return year - int(self.birth_year) - 1
            else:
                return year - int(self.birth_year)

    def get_sex(self):
        """通过身份证号获取性别，女生0，男生1"""
        return int(self.id[16:17]) % 2

    def get_check_digit(self):
        """通过身份证号获取校验码"""
        check_sum = 0
        for i in range(0, 17):
            check_sum += ((1 << (17-i)) % 11) * int(self.id[i])
        check_digit = (12 - (check_sum % 11)) % 11
        return check_digit if check_digit < 10 else 'X'

    @classmethod
    def verify_id(cls, id_number):
        """校验身份证是否正确"""
        if re.match(const.ID_NUMBER_18_REGEX, id_number):
            check_digit = cls(id_number).get_check_digit()
            return str(check_digit) == id_number[-1]
        else:
            return bool(re.match(const.ID_NUMBER_15_REGEX, id_number))

    @classmethod
    def generate_id(cls, sex=0):
        """随机生成身份证号，sex=0女性，sex=1男性"""
        # 随机生成一个区域码（6位）
        id_number = str(random.choice(list(const.AREA_INFO.keys())))
        # 限定出生日期范围（8位）
        start = datetime.strptime("1940-01-01", "%Y-%m-%d")
        end = datetime.strptime("2010-01-01", "%Y-%m-%d")
        birth_days = datetime.strftime(start + timedelta(random.randint(0, (end - start).days + 1)), "%Y%m%d")
        id_number += str(birth_days)
        # 顺序码（2位）
        id_number += str(random.randint(10, 99))
        # 性别码（1位）
        id_number += str(random.randrange(sex, 10, step=2))
        # 校验码
        return id_number + str(cls(id_number).get_check_digit())

if __name__ == '__main__':
    random_sex = random.randint(0, 1)  # 男1 女0
    id_number = IdNumber.generate_id(random_sex)
    print(IdNumber(id_number).area_id)   # 地址编码
    print(IdNumber(id_number).get_area_name())    # 地址
    print(IdNumber(id_number).get_birthday())    # 生日
    print(IdNumber(id_number).get_age())    # 年龄
    print(IdNumber(id_number).get_sex())   # 性别
    print(IdNumber(id_number).get_check_digit())   # 校验码
    print(IdNumber.verify_id(id_number))   # 校验身份证是否正确
