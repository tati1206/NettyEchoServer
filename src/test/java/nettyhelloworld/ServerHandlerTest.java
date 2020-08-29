package nettyhelloworld;

import nettyhelloworld.server.ServerChannelHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;

public class ServerHandlerTest {
    @Test
    public void shouldReturnTheSameInputFromClient() {
        // Given

        /**
         * Embedded channel es una implementacion de un channel con propositos de testing.
         * Como parametro al constructor le pasamos en handler o handlers para testear.
         */
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new ServerChannelHandler());

        String expected = "Hello world";

        /**
         * Unpooled es una clase con metodos estaticos, para poder crear ByteBuf's en los casos donde no tengas acceso a un ByteBufAllocator.
         *
         * Hay que tener en cuenta que un Pooled bytebuf tiene un mejor rendimiento y son mas eficientes, por lo cual siempre que tengas acceso a un ByteBufAllocator, trata de crear instancias a partir de el.
         *
         */
        ByteBuf input = Unpooled.buffer();
        input.writeCharSequence(expected, Charset.defaultCharset()); // Escribimos una secuencia de caracteres

        // When
        embeddedChannel.writeInbound(input); // Escribimos como un evento entrante al embeddedChannel

        // Then
        ByteBuf output = embeddedChannel.readOutbound(); // Leemos la salida del channel
        Assert.assertEquals(expected, output.readCharSequence(11, Charset.defaultCharset())); // verificamos que sea igual lo esperado con lo recibido.
    }
}
