package com.jbtits.otus.lecture12.web;

import com.jbtits.otus.lecture12.auth.AuthFilter;
import com.jbtits.otus.lecture12.auth.AuthService;
import com.jbtits.otus.lecture12.cache.CacheService;
import com.jbtits.otus.lecture12.cache.CacheServiceImpl;
import com.jbtits.otus.lecture12.dbService.DBService;
import com.jbtits.otus.lecture12.dbService.DBServiceHibernateImpl;
import com.jbtits.otus.lecture12.dbService.dataSets.DataSet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class WebServer {
    private final static int PORT = 8080;

    private final DBService dbService;
    private final CacheService<String, DataSet> cacheService;
    private final AuthService authService;

    private int port;

    WebServer(int port) {
        cacheService = new CacheServiceImpl<>(10, 1000);
        dbService = new DBServiceHibernateImpl(cacheService);
        authService = new AuthService(dbService);
        this.port = port;
    }

    private void start() {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new CacheStatisticsServlet(cacheService, dbService)), "/");
        context.addFilter(new FilterHolder(new AuthFilter(dbService)), "/*", EnumSet.of(DispatcherType.ASYNC,
            DispatcherType.ERROR,
            DispatcherType.FORWARD,
            DispatcherType.INCLUDE,
            DispatcherType.REQUEST));

        Server server = new Server(port);
        server.setHandler(new HandlerList(context));

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        int port = PORT;
        if (args.length > 1 && args[0].equals("-p")) {
            int portArg = Integer.parseInt(args[1]);
            if (portArg > 0) {
                port = portArg;
            }
        }
        WebServer webServer = new WebServer(port);
        webServer.start();
    }
}
