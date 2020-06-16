package com.mao.annotation

/**
 * 是否不为null检验
 * 用于检验字符串字段不允许为空
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class NeedNotNull

/**
 * 是否为数字检验
 * 用于判断字符串类型字段是否可以转化为数字
 * NeedNumber按执行逻辑已经包含NeedNotNull
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class NeedNumber

/**
 * 用于VARCHAR字段检验
 * 需自己设定最大长度
 * 默认该类型的检验：在字段为null时不做处理
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class NeedRangeLength(val max: Int)

/**
 * 用于text字段检验
 * mysql中text字段最大支持<64KB数据
 * 此处默认存储32KB数据
 * 默认该类型的检验：在字段为null时不做处理
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class NeedRangeText(val max: Int = 32)