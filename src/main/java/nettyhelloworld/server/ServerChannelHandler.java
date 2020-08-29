package nettyhelloworld.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

/**
 * Este es un ChannelInboundHandler, el cual se agrega en un channel pipeline para poder manejar eventos de entrada.
 *
 * Hay varios tipos de ChannelHandlers. {@link io.netty.channel.ChannelInboundHandler}, {@link io.netty.channel.ChannelOutboundHandler}, {@link io.netty.channel.ChannelDuplexHandler}, etc.
 */
public class ServerChannelHandler extends ChannelInboundHandlerAdapter {

    /**
     * Este metodo se ejecuta cuando un canal se pone activo.
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("New connection from: " + ctx.channel().remoteAddress().toString());
    }

    /**
     * Este metodo se ejecuta cada vez que haya informacion disponible para leer.
     * @param msg Este es un objeto que guarda el mensaje. Es un tipo objeto, ya que nosotros podemos usar decoders y encoders para serializar o deserializar datos.
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf message = (ByteBuf) msg; // Aqui, como en este caso no estamos usando deserializacion, simplemente casteamos el mensaje a un bytebuf, el cual es la minima expresion de los datos en netty.

        System.out.println("Received from " + ctx.channel().remoteAddress().toString() + ": " + message.toString(Charset.defaultCharset()));
        ctx.writeAndFlush(message); // Usamos el metodo writeAndFlush, el cual es simplemente un alias para llamar el metodo write() y el metodo flush
        // El metodo write escribe datos en un buffer.
        // El metodo flush libera el buffer y envia la informacion al proximo handler, o en este caso, al no haber otro handler en el pipeline, al cliente.
    }

    /**
     * Este metodo es llamado cuando el canal se pone inactivo.
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress().toString() + " disconnected");
    }
}
