package com.noway.avro.client.main;

import com.James.Tools.Return;
import com.infogen.core.json.Parameter;
import com.noway.avro.client.connect.AvroRpcClient;
import com.noway.avro.client.proto.Message;
import com.noway.avro.client.proto.TestObject;
import com.noway.avro.client.tools.JsonConvert;
import org.apache.avro.AvroRemoteException;
import org.apache.avro.util.Utf8;

import java.io.IOException;
import java.util.Map;

/**
 * Created by ziqing.chen
 * on 2017/4/17.
 */
public class ClientStart {

    public static void main( String[] args ) throws IOException {
        Message message = new Message();
        message.setParam( new Utf8( ( "{\"imei\":\"http://www.ppdai.com/blacklistdetail/cy780309\",\"appID\":\"1\",\"orgID\":\"414e7b8c5c4c4ab9bc7a83bf2d5a95cb\"}" ) ) );
        message.setAppId( "1" );
        message.setOrgId( "414e7b8c5c4c4ab9bc7a83bf2d5a95cb" );
        message.setRequestName( new Utf8( "taskAdd" ) );
        message.setId( "123" );
        message.setUrl( "DNQSN9DWHG75" );
        try {
            String response = AvroRpcClient.getInstance().sendRequest( "127.0.0.1", 40012, message );
            System.out.println( "接受到请求返回" + response );
            TestObject res = JsonConvert.toObject( response, TestObject.class );
            System.out.println( res.getData() );
            System.out.println( "id : " + res.getData().get( "id" ));
        } catch ( AvroRemoteException e ) {
            e.printStackTrace();
            //			logger.error( "调用" + message.getRequestName() + "接口异常", e );
        } catch ( IOException e ) {
            System.out.println( "无法连接到远程服务器" );
            System.exit( 0 );
        }
    }
}
