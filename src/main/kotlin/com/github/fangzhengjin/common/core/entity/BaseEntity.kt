package com.github.fangzhengjin.common.core.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import io.swagger.annotations.ApiModelProperty
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*
import javax.persistence.*
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlTransient

/**
 * @version V1.0
 * @title: BaseEntity
 * @package com.github.fangzhengjin.common.core.entity
 * @description: 实体类基类
 * @author fangzhengjin
 * @date 2019/1/28 14:52
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
@XmlAccessorType(XmlAccessType.NONE)
abstract class BaseEntity : IdEntity() {

    @ApiModelProperty(readOnly = true, accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @XmlTransient
//    @JSONField(name = "createdTime", format = "yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @OrderBy("created_time desc")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    open var createdTime: Date? = null

    @ApiModelProperty(readOnly = true, accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @XmlTransient
    @CreatedBy
    open var createdUser: String? = null

    @ApiModelProperty(readOnly = true, accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @XmlTransient
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    open var modifiedTime: Date? = null

    @ApiModelProperty(readOnly = true, accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @XmlTransient
    @LastModifiedBy
    open var modifiedUser: String? = null


    @ApiModelProperty(hidden = true, readOnly = true, accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @XmlTransient
    @JsonIgnore
    @Column(name = "is_delete")
    var delete = false

    @PrePersist
    fun onPersist() {
        createdTime = if (Objects.isNull(createdTime)) Date() else createdTime
        modifiedTime = if (Objects.isNull(modifiedTime)) Date() else modifiedTime
    }

    @PreUpdate
    fun onUpdate() {
        modifiedTime = Date()
    }

}
