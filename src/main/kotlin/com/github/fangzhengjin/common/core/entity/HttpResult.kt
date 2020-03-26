package com.github.fangzhengjin.common.core.entity

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fangzhengjin.common.core.jackson.utils.JacksonSerializeUtils
import org.springframework.http.HttpStatus
import java.io.Serializable

/**
 * @version V1.0
 * @title: HttpResult
 * @package com.github.fangzhengjin.common.core.entity
 * @description: 响应数据标准类
 * @author fangzhengjin
 * @date 2019/1/28 14:52
 */
class HttpResult<T> private constructor(
        var code: Int? = 200,
        var message: String? = "",
        @Suppress("UNCHECKED_CAST")
        var body: T? = "" as T
) : Serializable {

    val isOk: Boolean
        get() {
            return HttpStatus.OK.value() == this.code
        }

    override fun toString(): String {
        val objectMapper = JacksonSerializeUtils.defaultJacksonConfig(ObjectMapper())
        return objectMapper.writeValueAsString(this)
    }

    companion object {

        @JvmStatic
        @JvmOverloads
        fun ok(message: String? = null): HttpResult<String> {
            return HttpResult(
                    code = HttpStatus.OK.value(),
                    message = message ?: HttpStatus.OK.reasonPhrase,
                    body = null
            )
        }

        @JvmStatic
        @JvmOverloads
        fun <T> ok(body: T, message: String? = null): HttpResult<T> {
            return HttpResult(
                    code = HttpStatus.OK.value(),
                    message = message ?: HttpStatus.OK.reasonPhrase,
                    body = body
            )
        }

        @JvmOverloads
        @JvmStatic
        fun <T> ok(
                body: T,
                clazz: Class<T>,
                includeFields: Set<String> = setOf(),
                excludeFields: Set<String> = setOf()
        ): String {
            val httpResult = HttpResult(
                    code = HttpStatus.OK.value(),
                    message = HttpStatus.OK.reasonPhrase,
                    body = body
            )
            if (!includeFields.isNullOrEmpty()) {
                // include
                return JacksonSerializeUtils.serializeFieldsFilterInclude(httpResult, clazz, *includeFields.toTypedArray())
            }
            if (!excludeFields.isNullOrEmpty()) {
                // exclude
                return JacksonSerializeUtils.serializeFieldsFilterExclude(httpResult, clazz, *excludeFields.toTypedArray())
            }
            throw RuntimeException("includeFields or excludeFields list not found")
        }

        @JvmStatic
        @JvmOverloads
        fun <T> fail(
                message: String? = null,
                code: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR
        ): HttpResult<T> {
            return HttpResult(
                    code = code.value(),
                    message = message ?: code.reasonPhrase,
                    body = null
            )
        }

        @JvmStatic
        @JvmOverloads
        fun <T> fail(
                message: String? = null,
                code: Int
        ): HttpResult<T> {
            return HttpResult(
                    code = code,
                    message = message,
                    body = null
            )
        }

        @JvmStatic
        @JvmOverloads
        fun <T> fail(
                message: String? = null,
                code: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
                body: T?
        ): HttpResult<T> {
            @Suppress("UNCHECKED_CAST")
            return HttpResult(
                    code = code.value(),
                    message = message ?: code.reasonPhrase,
                    body = body
            )
        }

        @JvmStatic
        @JvmOverloads
        fun <T> fail(
                message: String? = null,
                code: Int,
                body: T?
        ): HttpResult<T> {
            @Suppress("UNCHECKED_CAST")
            return HttpResult(
                    code = code,
                    message = message,
                    body = body
            )
        }
    }
}