package com.farata.course.mwd.auction.entity;

import javax.json.JsonObject;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.math.BigDecimal;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BidDTO {

    private int prodictId;
    private BigDecimal amount;
    private int desiredQuantity;
    private int userId;

    public BidDTO(int prodictId, BigDecimal amount, int desiredQuantity,int userId) {
        this.prodictId = prodictId;
        this.userId = userId;
        this.amount = amount;
        this.desiredQuantity = desiredQuantity;
    }

    public BidDTO(){}

    @XmlTransient
    public JsonObject getJsonObject() {
        return null;
    }

    public int getProductId() {
        return prodictId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public int getDesiredQuantity() {
        return desiredQuantity;
    }

    public int getUserId() {
        return userId;
    }

    public void setProductId(int id) {
        this.prodictId = id;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setDesiredQuantity(int quantity) {
        this.desiredQuantity = quantity;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


}
