package com.noway.avro.client.connect;

import com.noway.avro.client.proto.AvroRequestProto;
import com.noway.avro.client.proto.Message;
import com.noway.avro.client.tools.CodeEnum;
import com.noway.avro.client.tools.JsonConvert;
import com.noway.avro.client.tools.Return;
import org.apache.avro.ipc.NettyTransceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class AvroRpcClient {

    private static final Log logger = LogFactory.getLog( AvroRpcClient.class.getName() );

    private static class InnerInstance {
        public static final AvroRpcClient instance = new AvroRpcClient();
    }

    public static AvroRpcClient getInstance() {
        return InnerInstance.instance;
    }

    private static class TransceiverThreadFactory implements ThreadFactory {
        private final AtomicInteger threadId = new AtomicInteger( 0 );
        private final String prefix;

        public TransceiverThreadFactory( String prefix ) {
            this.prefix = prefix;
        }

        @Override
        public Thread newThread( Runnable r ) {
            Thread thread = new Thread( r );
            thread.setDaemon( true );
            thread.setName( prefix + " " + threadId.incrementAndGet() );
            return thread;
        }
    }

    public static NettyTransceiver create( String hostname, int port, boolean keepAlive ) throws IOException {
        Map< String, Object > options = new HashMap< String, Object >( 3 );
        options.put( NettyTransceiver.NETTY_TCP_NODELAY_OPTION, NettyTransceiver.DEFAULT_TCP_NODELAY_VALUE );
        options.put( "keepAlive", keepAlive );
        options.put( NettyTransceiver.NETTY_CONNECT_TIMEOUT_OPTION, NettyTransceiver.DEFAULT_CONNECTION_TIMEOUT_MILLIS );
        return new NettyTransceiver( new InetSocketAddress( hostname, port ), new NioClientSocketChannelFactory( Executors.newCachedThreadPool( new DaemonThreadFactory( new TransceiverThreadFactory( "avro-client-boss" ) ) ), Executors.newCachedThreadPool( new DaemonThreadFactory( new TransceiverThreadFactory( "avro-client-worker" ) ) ) ), options );
    }

    public static Return call( String hostname, int port, Message message ) {
        NettyTransceiver client;
        try {
            client = new NettyTransceiver( new InetSocketAddress( hostname, port ) );
            AvroRequestProto proxy = ( AvroRequestProto ) SpecificRequestor.getClient( AvroRequestProto.class, client );
            logger.info( "avro client初始化成功" );
            String response = proxy.send( message ).toString();
            if ( client != null && client.isConnected() ) {
                client.close();
            }
            return JsonConvert.toObject( response, Return.class );
        } catch ( IOException e ) {
            e.printStackTrace();
            logger.error( "调用nettyavro异常", e );
            return Return.FAIL( CodeEnum.SERVER_PROCESS_FAIL.code, CodeEnum.SERVER_PROCESS_FAIL.name() );
        }
    }

    public String sendRequest( String hostname, int port, Message message ) throws IOException {
        return bindProxy( initClient( hostname, port ) ).send( message ).toString();
    }

    private AvroRequestProto bindProxy( NettyTransceiver client ) throws IOException {
        return ( AvroRequestProto ) SpecificRequestor.getClient( AvroRequestProto.class, client );
    }

    private NettyTransceiver initClient( String hostname, int port ) {
        try {
            NettyTransceiver client = new NettyTransceiver( new InetSocketAddress( hostname, port ) );
            return client;
        } catch ( IOException e ) {
            e.printStackTrace();
            logger.error( "初始化netty client 异常", e );
            return null;
        }
    }

    private static class DaemonThreadFactory implements ThreadFactory {
        private ThreadFactory delegate;

        DaemonThreadFactory() {
            this.delegate = Executors.defaultThreadFactory();
        }

        DaemonThreadFactory( ThreadFactory delegate ) {
            this.delegate = delegate;
        }

        @Override
        public Thread newThread( Runnable r ) {
            Thread thread = delegate.newThread( r );
            thread.setDaemon( true );
            return thread;
        }
    }

}
