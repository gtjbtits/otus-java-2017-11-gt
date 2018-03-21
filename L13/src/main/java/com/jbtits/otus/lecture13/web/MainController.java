package com.jbtits.otus.lecture13.web;

import com.jbtits.otus.lecture13.cache.CacheService;
import com.jbtits.otus.lecture13.dbService.dataSets.DataSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class MainController {
    private static final int PERIOD_MS = 1000;
    private static final String REFRESH_VARIABLE_NAME = "refreshPeriod";

    @Autowired
    private CacheService<String, DataSet> cacheService;

    @RequestMapping(method = RequestMethod.GET)
    public String printStatistics(ModelMap model) {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("cacheHits", String.valueOf(cacheService.getHitCount()));
        pageVariables.put("cacheMisses", String.valueOf(cacheService.getMissCount()));
        pageVariables.put("cacheHitsPercentage", String.format("%d%%", cacheService.getHitPercentage()));
        pageVariables.put("cacheMissesPercentage", String.format("%d%%", cacheService.getMissPercentage()));
        pageVariables.put("cacheMaxElementsSize", String.valueOf(cacheService.getMaxSize()));
        pageVariables.put("cacheIdleTimeout", String.valueOf(cacheService.getIdleTimeout()));
        pageVariables.put(REFRESH_VARIABLE_NAME, String.valueOf(PERIOD_MS));

        model.addAllAttributes(pageVariables);
        return "statistics";
    }
}
