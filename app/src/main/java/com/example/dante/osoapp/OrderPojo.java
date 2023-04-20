package com.example.dante.osoapp;

import java.util.List;

public class OrderPojo {
    private String orderid;
    private String custoer_email;
    private String  delivery_Address;
    private String id;
    private List<CartPojo> list;
    private int count;
    private String status;
    private String total;


    public OrderPojo(String orderid, String custoer_email, String delivery_Address,String id,String status,String total, List<CartPojo> list) {
        this.orderid = orderid;
        this.custoer_email = custoer_email;
        this.delivery_Address = delivery_Address;
        this.list = list;
        this.id=id;
        this.status=status;
        this.total=total;
    }

    public OrderPojo(String orderid, String custoer_email, String delivery_Address, String total, int count) {
        this.orderid = orderid;
        this.custoer_email = custoer_email;
        this.delivery_Address = delivery_Address;
        this.total = total;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getCustoer_email() {
        return custoer_email;
    }

    public void setCustoer_email(String custoer_email) {
        this.custoer_email = custoer_email;
    }

    public String getDelivery_Address() {
        return delivery_Address;
    }

    public void setDelivery_Address(String delivery_Address) {
        this.delivery_Address = delivery_Address;
    }

    public List<CartPojo> getList() {
        return list;
    }

    public void setList(List<CartPojo> list) {
        this.list = list;
    }
}
