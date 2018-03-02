package com.jbtits.otus.lecture12.web;

import com.jbtits.otus.lecture12.cache.CacheService;
import com.jbtits.otus.lecture12.dbService.DBService;
import com.jbtits.otus.lecture12.dbService.dataSets.DataSet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class CacheStatisticsServlet extends HttpServlet {
    private final CacheService<String, DataSet> cacheService;
    private final DBService dbService;

    private static final int PERIOD_MS = 1000;
    private static final String REFRESH_VARIABLE_NAME = "refreshPeriod";

    public CacheStatisticsServlet(CacheService<String, DataSet> cacheService, DBService dbService) {
        this.cacheService = cacheService;
        this.dbService = dbService;
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("cacheHits", String.valueOf(cacheService.getHitCount()));
        pageVariables.put("cacheMisses", String.valueOf(cacheService.getMissCount()));
        pageVariables.put("cacheHitsPercentage", String.format("%d%%", cacheService.getHitPercentage()));
        pageVariables.put("cacheMissesPercentage", String.format("%d%%", cacheService.getMissPercentage()));
        pageVariables.put("cacheMaxElementsSize", String.valueOf(cacheService.getMaxSize()));
        pageVariables.put("cacheIdleTimeout", String.valueOf(cacheService.getIdleTimeout()));
        pageVariables.put(REFRESH_VARIABLE_NAME, String.valueOf(PERIOD_MS));

        resp.setContentType("text/html;charset=utf-8");
        resp.getWriter().println(TemplateProcessor.instance().getPage("statistics.html", pageVariables));
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
