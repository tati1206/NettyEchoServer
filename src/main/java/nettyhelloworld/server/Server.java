package nettyhelloworld.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
    private final int PORT;

    /**
     * Este es un grupo de event loops, este objecto se encarga de gestionar y almacenar los event loops.
     *
     * Cada vez que se crea un nuevo canal, a este se le asigna un event loop. Un event loop puede tener asignados varios canales a la vez, pero esto depende de como se implemente
     * y del funcionamiento interno de netty.
     *
     * @see EventLoopGroup
     * @see NioEventLoopGroup
     * @see io.netty.channel.EventLoop
     */
    private final EventLoopGroup group = new NioEventLoopGroup();

    /**
     * Este objecto se encarga de hacer mucho mas sencillo inicializar un servidor.
     *
     * Basicamente lo que hace este objeto es crear un {@link io.netty.channel.ServerChannel}
     */
    private final ServerBootstrap bootstrap = new ServerBootstrap();

    public Server(int port){
        this.PORT = port;

        bootstrap.group(group) // Asignamos que grupo de eventloops vamos a utilizar

                .channel(NioServerSocketChannel.class) // Asignamos que tipo de ServerChannel vamos a utilizar

                /**
                 * En esta funcion asignamos cual va a ser el ChannelHandler que se va a utilizar para cuando se encuentre una nueva conexion.
                 *
                 * En este caso estamos utilizando ChannelInitializer, que es una implementacion de ChannelInboundHandler que trae metodos los cuales hacen mas sencilla la inicializacion de un canal.
                 *
                 * Al channel initializer le pasamos que tipo de Channel va a ser utilizado, en este caso como estamos haciendo un servidor TCP estamos utilizando SocketChannel.
                 */
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    /**
                     * Este metodo se va a encargar de hacer las configuraciones iniciales del canal.
                     */
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ServerChannelHandler()); // En esta linea estamos agregando al ChannelPipeline un ChannelInboundHandler, que se va a ejecutar cada vez que el canal reciba un evento de entrada
                    }
                })
                .localAddress(this.PORT); // Asignamos el puerto
    }

    public static void main(String[] args) {
        Server server = new Server(25565);
        server.startServer();
    }

    public void startServer() {

        /**
         * Aqui lo que hacemos es usar la funcion bind.
         *
         * La funcion bind es asincrona por lo cual esta retorna un {@link io.netty.channel.ChannelFuture}. Este objeto siempre va a ser el resultado de una operacion I/O asincrona
         *
         * A un channel future podemos agregarle listeners que se ejecuten cuando la operacion asincrona se finalize, como estamos haciendo en este caso.
         */

        bootstrap.bind().addListener(future -> {
            System.out.printf("Server listening on port: %d\n", this.PORT);
        });
    }

}
