import java.io.IOException;
import java.sql.SQLException;

public class RunServer
{
    public static void main(String[] args)
    {
        try
        {
            Server server = new Server();
            server.startServer();
            System.out.println("server started.");
            System.out.println("listening for client.");
            server.listen();
            System.out.println("client connected.");
            server.createStreams();
            System.out.println("streams to client created.");
            server.process();
        }
        catch (SQLException | IOException e)
        {
            System.out.println(e);
        }
    }
}
