from datetime import datetime


class ProjectTemplateObject:
    def __init__(self, id):
        self.id = id
        self._code = None
        self._name = None
        self._type = None    # 治疗或评定
        self._bedside = None
        self._unit = None
        self._count = None    # 单次剂量
        self._money = None    # 次单价
        self._unit_price = None   # 剂量单价
        self._frequency = None    # 频次time
        self._short_long = None
        self._total = None     # 治疗次数
        self._total_amount = None     # 总剂量
        self._duration = None    # 时长
        self._pst = None
        self._equipment_id = None
        self._equipment_name = None
        self._relevance = None    # 关键字
        self._content_type = None    # 帮助类型
        self._content = None

    @property
    def code(self):
        return self._code

    @code.setter
    def code(self, code):
        self._code = code

    @property
    def name(self):
        return self._name

    @name.setter
    def name(self, name):
        self._name = name

    @property
    def type(self):
        return self._type

    @type.setter
    def type(self, type):
        self._type = type

    @property
    def bedside(self):
        return self._bedside

    @bedside.setter
    def bedside(self, bedside):
        self._bedside = bedside

    @property
    def unit(self):
        return self._unit

    @unit.setter
    def unit(self, unit):
        self._unit = unit

    @property
    def count(self):
        return self._count

    @count.setter
    def count(self, count):
        self._count = count

    @property
    def money(self):
        return self._money

    @money.setter
    def money(self, money):
        self._money = money

    @property
    def unit_price(self):
        return self._unit_price

    @unit_price.setter
    def unit_price(self, unit_price):
        self._unit_price = unit_price

    @property
    def frequency(self):
        return self._frequency

    @frequency.setter
    def frequency(self, frequency):
        self._frequency = frequency

    @property
    def short_long(self):
        return self._short_long

    @short_long.setter
    def short_long(self, short_long):
        self._short_long = short_long

    @property
    def total(self):
        return self._total

    @total.setter
    def total(self, total):
        self._total = total

    @property
    def total_amount(self):
        return self._total_amount

    @total_amount.setter
    def total_amount(self, total_amount):
        self._total_amount = total_amount

    @property
    def duration(self):
        return self._duration

    @duration.setter
    def duration(self, duration):
        self._duration = duration

    @property
    def pst(self):
        return self._pst

    @pst.setter
    def pst(self, pst):
        self._pst = pst

    @property
    def equipment_id(self):
        return self._equipment_id

    @equipment_id.setter
    def equipment_id(self, equipment_id):
        self._equipment_id = equipment_id

    @property
    def equipment_name(self):
        return self._equipment_name

    @equipment_name.setter
    def equipment_name(self, equipment_name):
        self._equipment_name = equipment_name

    @property
    def relevance(self):
        return self._relevance

    @relevance.setter
    def relevance(self, relevance):
        self._relevance = relevance

    @property
    def content_type(self):
        return self._content_type

    @content_type.setter
    def content_type(self, content_type):
        self._content_type = content_type

    @property
    def content(self):
        return self._content

    @content.setter
    def content(self, content):
        self._content = content
