package com.zw.admin.server.model;

import lombok.Data;

@Data
public class Sn extends BaseEntity<Long> {

    /**
     *
     */
    private static final long serialVersionUID = 5123514895947475306L;
    private String snId;
    private String pcId;
    private String machineId;
    private String location;

}
