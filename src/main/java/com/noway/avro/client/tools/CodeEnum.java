package com.noway.avro.client.tools;


public enum CodeEnum {

	/** 10000 system message **/
	SUCCESS( 10200, "成功" ), 
	/** 10100 Server error message **/
	ERROR( 10100, "异常错误" ),
	SERVER_PROCESS_FAIL( 10101, "服务器操作失败" ),
	MYSQL_CONNECT_FAIL( 10102, "MySQL连接失败" ),
	REDIS_CONNECT_FAIL( 10103, "Redis连接失败" ),
	/** 10300 avro error message **/
	AVRO_CONNECTION_MAX_LIIMT( 10300, "avro超过连接最大数" ),
	AVRO_CONNECTION_NOT_AVAILABLE( 10301, "无效的avro连接" ),
	AVRO_REMOTE_CONNECTION_ERROR( 10302, "调用远程服务器接口异常" ),
	AVRO_REMOTE_BIND_ERROR( 10303, "绑定远程接口异常" ),
	AVRO_PARAMETERS_INCORRECT( 10304, "RequestName参数不正确" ),
	AVRO_SERVICE_NOT_FOUND( 10305, "没有指定服务" )
	;
	public String note;
	public Integer code;

	private CodeEnum( Integer code, String note ) {
		this.note = note;
		this.code = code;
	}

}
